package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetWarningRule;
import com.management.accountant.oracle.mapper.budget.BudgetWarningRuleMapper;
import com.management.accountant.service.BudgetWarningRuleService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 预算预警规则Service实现类
 * 
 * @description 预算预警规则业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetWarningRuleServiceImpl implements BudgetWarningRuleService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetWarningRuleMapper warningRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetWarningRule create(BudgetWarningRule rule) {
        // 1. 参数校验
        if (rule == null) {
            throw new ServiceException("规则信息不能为空");
        }
        if (!StringUtils.hasText(rule.getRuleName())) {
            throw new ServiceException("规则名称不能为空");
        }
        if (!StringUtils.hasText(rule.getWarningType())) {
            throw new ServiceException("预警类型不能为空");
        }

        // 2. 前端字段 -> DB字段 转换
        convertFrontendToDb(rule);

        // 3. 检查规则编码是否重复
        if (StringUtils.hasText(rule.getRuleCode())) {
            QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();
            wrapper.eq("RULE_CODE", rule.getRuleCode());
            long count = warningRuleMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("规则编码已存在");
            }
        } else {
            // 自动生成规则编码
            rule.setRuleCode(generateRuleCode());
        }

        // 4. 设置默认值
        if (rule.getDelFlag() == null) {
            rule.setDelFlag(0);
        }
        if (rule.getIsEnabled() == null) {
            rule.setIsEnabled(1);
        }
        if (!StringUtils.hasText(rule.getCompareType())) {
            rule.setCompareType("GT"); // 默认大于
        }
        // 数据库非空字段兜底
        if (!StringUtils.hasText(rule.getWarningLevel())) {
            rule.setWarningLevel("MEDIUM");
        }
        if (rule.getWarningStatus() == null) {
            rule.setWarningStatus("INACTIVE");
        }
        if (rule.getProcessStatus() == null) {
            rule.setProcessStatus("PENDING");
        }
        if (rule.getCurrentValue() == null) {
            rule.setCurrentValue(java.math.BigDecimal.ZERO);
        }
        if (rule.getExceedRate() == null) {
            rule.setExceedRate(java.math.BigDecimal.ZERO);
        }
        rule.setCreateTime(new Date());
        rule.setUpdateTime(new Date());

        // 5. 插入数据库
        int result = warningRuleMapper.insert(rule);
        if (result <= 0) {
            throw new ServiceException("创建预警规则失败");
        }

        log.info("创建预警规则成功，ID: {}", rule.getWarningRuleId());

        // 6. 填充前端字段后返回
        populateFrontendFields(rule);
        return rule;
    }

    @Override
    public BudgetWarningRule getById(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();
        wrapper.eq("WARNING_RULE_ID", ruleId);

        BudgetWarningRule rule = warningRuleMapper.selectOne(wrapper);
        if (rule != null) {
            populateFrontendFields(rule);
        }
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetWarningRule rule) {
        if (rule == null || !StringUtils.hasText(rule.getWarningRuleId())) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetWarningRule existing = getById(rule.getWarningRuleId());
        if (existing == null) {
            throw new ServiceException("预警规则不存在");
        }

        // 前端字段 -> DB字段 转换
        convertFrontendToDb(rule);

        // 检查规则编码是否重复
        if (StringUtils.hasText(rule.getRuleCode()) && !rule.getRuleCode().equals(existing.getRuleCode())) {
            QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();
            wrapper.eq("RULE_CODE", rule.getRuleCode())
                   .ne("WARNING_RULE_ID", rule.getWarningRuleId());
            long count = warningRuleMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("规则编码已存在");
            }
        }

        rule.setUpdateTime(new Date());
        int result = warningRuleMapper.updateById(rule);
        if (result <= 0) {
            throw new ServiceException("更新预警规则失败");
        }

        log.info("更新预警规则成功，ID: {}", rule.getWarningRuleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        // 使用 deleteById，配合 @TableLogic 自动执行软删除
        int result = warningRuleMapper.deleteById(ruleId);
        if (result <= 0) {
            throw new ServiceException("预警规则不存在或已被删除");
        }

        log.info("删除预警规则成功，ID: {}", ruleId);
    }

    @Override
    public PageResult<BudgetWarningRule> getPage(Map<String, Object> params) {
        // 1. 获取分页参数（兼容前端 current/size 和 pageNum/pageSize 两种命名）
        int pageNum = getIntParam(params, "current", getIntParam(params, "pageNum", 1));
        int pageSize = getIntParam(params, "size", getIntParam(params, "pageSize", 20));

        // 2. 构建查询条件
        QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();

        // 规则编码（兼容前端 warningCode 和后端 ruleCode）
        String ruleCode = getStringParam(params, "warningCode", getStringParam(params, "ruleCode", null));
        if (StringUtils.hasText(ruleCode)) {
            wrapper.like("RULE_CODE", ruleCode);
        }

        // 规则名称（兼容前端 warningName 和后端 ruleName）
        String ruleName = getStringParam(params, "warningName", getStringParam(params, "ruleName", null));
        if (StringUtils.hasText(ruleName)) {
            wrapper.like("RULE_NAME", ruleName);
        }

        // 预警类型
        if (params.get("warningType") != null && !params.get("warningType").toString().isEmpty()) {
            wrapper.eq("WARNING_TYPE", params.get("warningType"));
        }

        // 预警级别
        if (params.get("warningLevel") != null && !params.get("warningLevel").toString().isEmpty()) {
            wrapper.eq("WARNING_LEVEL", params.get("warningLevel"));
        }

        // 是否启用（兼容前端 warningStatus 和后端 isEnabled）
        String warningStatus = getStringParam(params, "warningStatus", null);
        if (StringUtils.hasText(warningStatus)) {
            if ("ACTIVE".equals(warningStatus)) {
                wrapper.eq("IS_ENABLED", 1);
            } else if ("INACTIVE".equals(warningStatus)) {
                wrapper.eq("IS_ENABLED", 0);
            }
        } else if (params.get("isEnabled") != null && !params.get("isEnabled").toString().isEmpty()) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetWarningRule> page = new Page<>(pageNum, pageSize);
        IPage<BudgetWarningRule> pageResult = warningRuleMapper.selectPage(page, wrapper);

        // 4. 对查询结果进行前端字段填充
        List<BudgetWarningRule> records = pageResult.getRecords();
        if (records != null) {
            for (BudgetWarningRule rule : records) {
                populateFrontendFields(rule);
            }
        }

        // 5. 封装返回结果
        PageResult<BudgetWarningRule> result = new PageResult<>();
        result.setTlist(records);
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetWarningRule rule = getById(ruleId);
        if (rule == null) {
            throw new ServiceException("预警规则不存在");
        }

        if (Integer.valueOf(1).equals(rule.getIsEnabled())) {
            throw new ServiceException("规则已经是启用状态");
        }

        BudgetWarningRule update = new BudgetWarningRule();
        update.setWarningRuleId(ruleId);
        update.setIsEnabled(1);
        update.setUpdateTime(new Date());

        int result = warningRuleMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("启用规则失败");
        }

        log.info("启用预警规则成功，ID: {}", ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetWarningRule rule = getById(ruleId);
        if (rule == null) {
            throw new ServiceException("预警规则不存在");
        }

        if (Integer.valueOf(0).equals(rule.getIsEnabled())) {
            throw new ServiceException("规则已经是禁用状态");
        }

        BudgetWarningRule update = new BudgetWarningRule();
        update.setWarningRuleId(ruleId);
        update.setIsEnabled(0);
        update.setUpdateTime(new Date());

        int result = warningRuleMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("禁用规则失败");
        }

        log.info("禁用预警规则成功，ID: {}", ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的记录");
        }

        for (String id : ids) {
            try {
                delete(id);
            } catch (Exception e) {
                log.error("批量删除失败，ID: {}", id, e);
            }
        }

        log.info("批量删除完成，数量: {}", ids.size());
    }

    @Override
    public Map<String, Object> triggerWarning(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取检查参数
            String warningType = (String) params.get("warningType");
            BigDecimal value = params.get("value") != null ? 
                new BigDecimal(params.get("value").toString()) : BigDecimal.ZERO;
            
            // 获取生效的预警规则
            List<BudgetWarningRule> rules = getActiveRules(warningType);
            
            List<Map<String, Object>> warnings = new ArrayList<>();
            
            for (BudgetWarningRule rule : rules) {
                // 根据规则类型进行检查
                if ("BUDGET_EXCEED".equals(rule.getWarningType())) {
                    BigDecimal thresholdVal = new BigDecimal(rule.getThreshold());
                    if (value.compareTo(thresholdVal) > 0) {
                        Map<String, Object> warning = new HashMap<>();
                        warning.put("ruleId", rule.getWarningRuleId());
                        warning.put("ruleName", rule.getRuleName());
                        warning.put("warningLevel", rule.getWarningLevel());
                        warning.put("message", "超出阈值预警：当前值 " + value + "，阈值 " + rule.getThreshold());
                        warnings.add(warning);

                        // 发送预警通知
                        sendWarningNotification(rule.getWarningRuleId(), warning.get("message").toString());
                    }
                }
            }
            
            result.put("triggered", !warnings.isEmpty());
            result.put("warnings", warnings);
            result.put("rulesChecked", rules.size());
            
        } catch (Exception e) {
            log.error("触发预警检查异常", e);
            result.put("triggered", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();

        // 总数
        Integer totalCount = warningRuleMapper.selectCount(wrapper).intValue();
        statistics.put("totalCount", totalCount);

        // 启用数量
        wrapper.eq("IS_ENABLED", 1);
        Integer enabledCount = warningRuleMapper.selectCount(wrapper).intValue();
        statistics.put("enabledCount", enabledCount);

        // 禁用数量
        statistics.put("disabledCount", totalCount - enabledCount);

        // 按预警类型统计
        wrapper = new QueryWrapper<BudgetWarningRule>();
        wrapper.select("WARNING_TYPE", "COUNT(*) as count")
               .groupBy("WARNING_TYPE");
        List<Map<String, Object>> typeStats = warningRuleMapper.selectMaps(wrapper);
        statistics.put("typeStatistics", typeStats);

        // 按预警级别统计
        wrapper = new QueryWrapper<BudgetWarningRule>();
        wrapper.select("WARNING_LEVEL", "COUNT(*) as count")
               .groupBy("WARNING_LEVEL");
        List<Map<String, Object>> levelStats = warningRuleMapper.selectMaps(wrapper);
        statistics.put("levelStatistics", levelStats);

        return statistics;
    }

    @Override
    public List<BudgetWarningRule> getActiveRules(String warningType) {
        QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);

        if (StringUtils.hasText(warningType)) {
            wrapper.eq("WARNING_TYPE", warningType);
        }
        
        wrapper.orderByDesc("CREATE_TIME");
        
        return warningRuleMapper.selectList(wrapper);
    }

    @Override
    public void sendWarningNotification(String ruleId, String message) {
        try {
            BudgetWarningRule rule = getById(ruleId);
            if (rule == null) {
                log.warn("预警规则不存在，无法发送通知，ID: {}", ruleId);
                return;
            }

            // TODO: 实现具体的通知发送逻辑
            // 1. 解析预警渠道（EMAIL、SMS、SYSTEM、WEBHOOK）
            // 2. 解析接收人列表
            // 3. 根据消息模板生成通知内容
            // 4. 调用相应的通知服务发送通知

            log.info("发送预警通知，规则: {}, 消息: {}", rule.getRuleName(), message);

        } catch (Exception e) {
            log.error("发送预警通知失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchProcess(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> ruleIds = (List<String>) params.get("ruleIds");
         // 兼容前端发送的 ids 参数
         if (ruleIds == null || ruleIds.isEmpty()) {
             @SuppressWarnings("unchecked")
             List<String> ids = (List<String>) params.get("ids");
             ruleIds = ids;
         }
         String processAction = (String) params.get("action"); // enable, disable, delete
         // 默认操作为禁用
         if (processAction == null || processAction.isEmpty()) {
             processAction = "disable";
         }
 
         if (ruleIds == null || ruleIds.isEmpty()) {
            throw new ServiceException("规则ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String ruleId : ruleIds) {
            try {
                switch (processAction) {
                    case "enable":
                        enable(ruleId);
                        break;
                    case "disable":
                        disable(ruleId);
                        break;
                    case "delete":
                        delete(ruleId);
                        break;
                    default:
                        throw new ServiceException("不支持的操作类型: " + processAction);
                }
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(ruleId);
                log.error("批量处理预警规则失败，ID: {}, 操作: {}", ruleId, processAction, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", ruleIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量处理预警规则完成，操作: {}, 成功: {}, 失败: {}", processAction, successCount, failCount);
        return result;
    }

    @Override
    public List<BudgetWarningRule> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetWarningRule> wrapper = new QueryWrapper<>();

        // 应用查询条件
        if (params.get("warningType") != null && StringUtils.hasText(params.get("warningType").toString())) {
            wrapper.eq("WARNING_TYPE", params.get("warningType"));
        }
        if (params.get("warningLevel") != null && StringUtils.hasText(params.get("warningLevel").toString())) {
            wrapper.eq("WARNING_LEVEL", params.get("warningLevel"));
        }
        if (params.get("isEnabled") != null) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        return warningRuleMapper.selectList(wrapper);
    }

    /**
     * 生成规则编码
     */
    private String generateRuleCode() {
        return "WARN" + System.currentTimeMillis();
    }

    /**
     * 前端字段 -> DB字段 转换（创建/更新时调用）
     */
    private void convertFrontendToDb(BudgetWarningRule rule) {
        // isActive (Boolean) -> isEnabled (Integer)
        if (rule.getIsActive() != null && rule.getIsEnabled() == null) {
            rule.setIsEnabled(Boolean.TRUE.equals(rule.getIsActive()) ? 1 : 0);
        }

        // notificationMethods (List<String>) -> notificationType (String, 逗号分隔)
        if (rule.getNotificationMethods() != null && !rule.getNotificationMethods().isEmpty()) {
            rule.setNotificationType(String.join(",", rule.getNotificationMethods()));
        }

        // notificationUserList (List<Object>) -> notificationUsers (String, 逗号分隔)
        if (rule.getNotificationUserList() != null && !rule.getNotificationUserList().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < rule.getNotificationUserList().size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(rule.getNotificationUserList().get(i));
            }
            rule.setNotificationUsers(sb.toString());
        }

        // thresholdUnit 存入 remark
        if (StringUtils.hasText(rule.getThresholdUnit())) {
            rule.setRemark(rule.getThresholdUnit());
        }

        // warningConditions (List<Object>) -> applicableScope (JSON字符串)
        if (rule.getWarningConditions() != null && !rule.getWarningConditions().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                rule.setApplicableScope(objectMapper.writeValueAsString(rule.getWarningConditions()));
            } catch (Exception e) {
                log.warn("序列化warningConditions失败", e);
            }
        }
    }

    /**
     * DB字段 -> 前端字段 转换（查询返回时调用）
     */
    private void populateFrontendFields(BudgetWarningRule rule) {
        if (rule == null) return;

        // isEnabled (Integer) -> isActive (Boolean)
        rule.setIsActive(Integer.valueOf(1).equals(rule.getIsEnabled()));

        // notificationType (String) -> notificationMethods (List<String>)
        if (StringUtils.hasText(rule.getNotificationType())) {
            rule.setNotificationMethods(Arrays.asList(rule.getNotificationType().split(",")));
        } else {
            rule.setNotificationMethods(new ArrayList<>());
        }

        // notificationUsers (String) -> notificationUserList (List<Object>)
        if (StringUtils.hasText(rule.getNotificationUsers())) {
            List<Object> userList = new ArrayList<>();
            for (String userId : rule.getNotificationUsers().split(",")) {
                if (StringUtils.hasText(userId.trim())) {
                    try {
                        userList.add(Long.parseLong(userId.trim()));
                    } catch (NumberFormatException e) {
                        userList.add(userId.trim());
                    }
                }
            }
            rule.setNotificationUserList(userList);
        } else {
            rule.setNotificationUserList(new ArrayList<>());
        }

        // remark -> thresholdUnit
        if (StringUtils.hasText(rule.getRemark())) {
            rule.setThresholdUnit(rule.getRemark());
        } else {
            rule.setThresholdUnit("%");
        }

        // applicableScope (JSON字符串) -> warningConditions (List<Object>)
        if (StringUtils.hasText(rule.getApplicableScope())) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                List<Object> conditions = objectMapper.readValue(rule.getApplicableScope(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class));
                rule.setWarningConditions(conditions);
            } catch (Exception e) {
                log.warn("反序列化warningConditions失败", e);
                rule.setWarningConditions(new ArrayList<>());
            }
        } else {
            rule.setWarningConditions(new ArrayList<>());
        }

        // 默认值
        if (rule.getAutoProcess() == null) {
            rule.setAutoProcess(false);
        }
        if (rule.getLogTrigger() == null) {
            rule.setLogTrigger(true);
        }
    }

    /**
     * 安全获取String参数
     */
    private String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        Object val = params.get(key);
        if (val != null && StringUtils.hasText(val.toString())) {
            return val.toString();
        }
        return defaultValue;
    }

    /**
     * 安全获取int参数
     */
    private int getIntParam(Map<String, Object> params, String key, int defaultValue) {
        Object val = params.get(key);
        if (val != null) {
            try {
                return Integer.parseInt(val.toString());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importData(org.springframework.web.multipart.MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        try (java.io.InputStream is = file.getInputStream()) {
            org.apache.poi.ss.usermodel.Workbook workbook;
            String filename = file.getOriginalFilename();
            if (filename != null && filename.toLowerCase().endsWith(".xls")) {
                workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook(is);
            } else {
                workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(is);
            }

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new ServiceException("Excel文件中没有工作表");
            }

            // 第一行为标题行，从第二行开始读取数据
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    BudgetWarningRule rule = new BudgetWarningRule();
                    rule.setRuleName(getCellStringValue(row, 0));       // A列：规则名称
                    rule.setRuleCode(getCellStringValue(row, 1));       // B列：规则编码
                    rule.setWarningType(getCellStringValue(row, 2));    // C列：预警类型
                    rule.setWarningLevel(getCellStringValue(row, 3));   // D列：预警级别
                    String thresholdStr = getCellStringValue(row, 4);   // E列：阈值
                    if (StringUtils.hasText(thresholdStr)) {
                        rule.setThreshold(thresholdStr);
                    }
                    rule.setDescription(getCellStringValue(row, 5));    // F列：描述

                    // 跳过空行
                    if (!StringUtils.hasText(rule.getRuleName())) continue;

                    // 设置默认值
                    rule.setCompareType("GT");
                    rule.setIsEnabled(0);
                    rule.setDelFlag(0);
                    rule.setCreateTime(new Date());
                    rule.setUpdateTime(new Date());
                    if (rule.getWarningLevel() == null) rule.setWarningLevel("MEDIUM");
                    if (rule.getWarningType() == null) rule.setWarningType("CUSTOM");

                    warningRuleMapper.insert(rule);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errors.add("第" + (i + 1) + "行导入失败: " + e.getMessage());
                    log.error("导入预警规则第{}行失败", i + 1, e);
                }
            }
            workbook.close();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("解析Excel文件失败: " + e.getMessage());
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        return result;
    }

    /**
     * 获取单元格字符串值
     */
    private String getCellStringValue(org.apache.poi.ss.usermodel.Row row, int cellIndex) {
        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cellIndex);
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                double num = cell.getNumericCellValue();
                if (num == Math.floor(num)) {
                    return String.valueOf((long) num);
                }
                return String.valueOf(num);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}

