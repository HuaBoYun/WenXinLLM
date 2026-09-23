package com.management.accountant.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetIndicator;
import com.management.accountant.oracle.mapper.budget.BudgetIndicatorMapper;
import com.management.accountant.service.BudgetIndicatorService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;

/**
 * 预算指标管理Service实现类
 * 
 * @description 预算指标管理业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetIndicatorServiceImpl implements BudgetIndicatorService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIndicatorMapper indicatorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetIndicator create(BudgetIndicator indicator) {
        // 1. 参数校验
        if (indicator == null) {
            throw new ServiceException("指标信息不能为空");
        }
        if (!StringUtils.hasText(indicator.getIndicatorCode())) {
            throw new ServiceException("指标编码不能为空");
        }
        if (!StringUtils.hasText(indicator.getIndicatorName())) {
            throw new ServiceException("指标名称不能为空");
        }

        // 2. 检查编码是否重复
        if (checkCodeExists(indicator.getIndicatorCode())) {
            throw new ServiceException("指标编码已存在");
        }

        // 3. 设置默认值
        if (indicator.getIsDeleted() == null) {
            indicator.setIsDeleted(0);
        }
        if (indicator.getIsRequired() == null) {
            indicator.setIsRequired(0);
        }
        if (indicator.getIsCalculated() == null) {
            indicator.setIsCalculated(0);
        }
        if (indicator.getIsEnabled() == null) {
            indicator.setIsEnabled(1);
        }
        if (indicator.getDecimalPlaces() == null) {
            indicator.setDecimalPlaces(2);
        }
        indicator.setCreateTime(new Date());
        indicator.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = indicatorMapper.insert(indicator);
        if (result <= 0) {
            throw new ServiceException("创建指标失败");
        }

        log.info("创建指标成功，ID: {}, 编码: {}", indicator.getIndicatorId(), indicator.getIndicatorCode());
        return indicator;
    }

    @Override
    public BudgetIndicator getById(String indicatorId) {
        if (!StringUtils.hasText(indicatorId)) {
            throw new ServiceException("指标ID不能为空");
        }
        
        QueryWrapper<BudgetIndicator> wrapper = new QueryWrapper<>();
        wrapper.eq("INDICATOR_ID", indicatorId)
               .eq("IS_DELETED", 0);
        
        return indicatorMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetIndicator update(BudgetIndicator indicator) {
        // 1. 参数校验
        if (indicator == null || !StringUtils.hasText(indicator.getIndicatorId())) {
            throw new ServiceException("指标ID不能为空");
        }

        // 2. 检查是否存在
        BudgetIndicator existing = getById(indicator.getIndicatorId());
        if (existing == null) {
            throw new ServiceException("指标不存在");
        }

        // 3. 如果修改了编码，检查新编码是否重复
        if (StringUtils.hasText(indicator.getIndicatorCode()) 
                && !indicator.getIndicatorCode().equals(existing.getIndicatorCode())) {
            if (checkCodeExists(indicator.getIndicatorCode())) {
                throw new ServiceException("指标编码已存在");
            }
        }

        // 4. 更新时间
        indicator.setUpdateTime(new Date());

        // 5. 更新数据库
        int result = indicatorMapper.updateById(indicator);
        if (result <= 0) {
            throw new ServiceException("更新指标失败");
        }

        log.info("更新指标成功，ID: {}", indicator.getIndicatorId());
        return getById(indicator.getIndicatorId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String indicatorId) {
        if (!StringUtils.hasText(indicatorId)) {
            throw new ServiceException("指标ID不能为空");
        }

        // 检查是否存在
        BudgetIndicator existing = getById(indicatorId);
        if (existing == null) {
            throw new ServiceException("指标不存在");
        }

        // 逻辑删除
        BudgetIndicator indicator = new BudgetIndicator();
        indicator.setIndicatorId(indicatorId);
        indicator.setIsDeleted(1);
        indicator.setUpdateTime(new Date());

        int result = indicatorMapper.updateById(indicator);
        if (result <= 0) {
            throw new ServiceException("删除指标失败");
        }

        log.info("删除指标成功，ID: {}", indicatorId);
    }

    @Override
    public PageResult<BudgetIndicator> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetIndicator> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        // 指标名称模糊查询
        if (params.get("indicatorName") != null) {
            wrapper.like("INDICATOR_NAME", params.get("indicatorName"));
        }

        // 指标类型
        if (params.get("indicatorType") != null) {
            wrapper.eq("INDICATOR_TYPE", params.get("indicatorType"));
        }

        // 是否启用
        if (params.get("isEnabled") != null) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        // 是否计算指标
        if (params.get("isCalculated") != null) {
            wrapper.eq("IS_CALCULATED", params.get("isCalculated"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetIndicator> page = new Page<>(pageNum, pageSize);
        IPage<BudgetIndicator> pageResult = indicatorMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetIndicator> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public List<Map<String, Object>> getTree() {
        // 查询所有未删除的指标
        QueryWrapper<BudgetIndicator> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0)
               .eq("IS_ENABLED", 1)
               .orderByAsc("INDICATOR_TYPE");
        List<BudgetIndicator> allIndicators = indicatorMapper.selectList(wrapper);

        // 按类型分组构建树结构
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, List<BudgetIndicator>> typeMap = new HashMap<>();
        
        for (BudgetIndicator indicator : allIndicators) {
            String type = indicator.getIndicatorType();
            if (type == null) {
                type = "custom";
            }
            typeMap.computeIfAbsent(type, k -> new ArrayList<>()).add(indicator);
        }

        // 构建树节点
        for (Map.Entry<String, List<BudgetIndicator>> entry : typeMap.entrySet()) {
            Map<String, Object> typeNode = new HashMap<>();
            typeNode.put("id", entry.getKey());
            typeNode.put("label", getTypeLabel(entry.getKey()));
            typeNode.put("type", "category");
            
            List<Map<String, Object>> children = new ArrayList<>();
            for (BudgetIndicator indicator : entry.getValue()) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", indicator.getIndicatorId());
                node.put("label", indicator.getIndicatorName());
                node.put("code", indicator.getIndicatorCode());
                node.put("type", "indicator");
                node.put("dataType", indicator.getDataType());
                node.put("isCalculated", indicator.getIsCalculated());
                children.add(node);
            }
            typeNode.put("children", children);
            tree.add(typeNode);
        }
        
        return tree;
    }

    /**
     * 获取类型标签
     */
    private String getTypeLabel(String type) {
        switch (type) {
            case "revenue": return "收入类";
            case "cost": return "成本类";
            case "expense": return "费用类";
            case "profit": return "利润类";
            case "asset": return "资产类";
            case "liability": return "负债类";
            case "cashflow": return "现金流类";
            case "custom": return "自定义";
            default: return "其他";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的数据");
        }

        for (String id : ids) {
            delete(id);
        }

        log.info("批量删除指标成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String indicatorId) {
        if (!StringUtils.hasText(indicatorId)) {
            throw new ServiceException("指标ID不能为空");
        }

        BudgetIndicator indicator = getById(indicatorId);
        if (indicator == null) {
            throw new ServiceException("指标不存在");
        }

        BudgetIndicator update = new BudgetIndicator();
        update.setIndicatorId(indicatorId);
        update.setIsEnabled(1);
        update.setUpdateTime(new Date());

        indicatorMapper.updateById(update);
        log.info("启用指标成功，ID: {}", indicatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String indicatorId) {
        if (!StringUtils.hasText(indicatorId)) {
            throw new ServiceException("指标ID不能为空");
        }

        BudgetIndicator indicator = getById(indicatorId);
        if (indicator == null) {
            throw new ServiceException("指标不存在");
        }

        BudgetIndicator update = new BudgetIndicator();
        update.setIndicatorId(indicatorId);
        update.setIsEnabled(0);
        update.setUpdateTime(new Date());

        indicatorMapper.updateById(update);
        log.info("禁用指标成功，ID: {}", indicatorId);
    }

    @Override
    public Map<String, Object> validateFormula(String formula) {
        Map<String, Object> result = new HashMap<>();
        
        if (!StringUtils.hasText(formula)) {
            result.put("valid", false);
            result.put("message", "公式不能为空");
            return result;
        }

        try {
            // 简单的公式验证逻辑
            // 检查括号是否匹配
            int leftCount = 0;
            int rightCount = 0;
            for (char c : formula.toCharArray()) {
                if (c == '(') leftCount++;
                if (c == ')') rightCount++;
            }
            
            if (leftCount != rightCount) {
                result.put("valid", false);
                result.put("message", "括号不匹配");
                return result;
            }

            // 检查是否包含非法字符
            if (!formula.matches("[0-9a-zA-Z_+\\-*/().\\s]+")) {
                result.put("valid", false);
                result.put("message", "公式包含非法字符");
                return result;
            }

            result.put("valid", true);
            result.put("message", "公式验证通过");
        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "公式验证失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    public boolean checkCodeExists(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }

        QueryWrapper<BudgetIndicator> wrapper = new QueryWrapper<>();
        wrapper.eq("INDICATOR_CODE", code)
               .eq("IS_DELETED", 0);

        return indicatorMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要启用的数据");
        }

        for (String id : ids) {
            enable(id);
        }

        log.info("批量启用指标成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDisable(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要禁用的数据");
        }

        for (String id : ids) {
            disable(id);
        }

        log.info("批量禁用指标成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importIndicators(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<BudgetIndicator> successList = new ArrayList<>();
        List<Map<String, String>> errorList = new ArrayList<>();

        try {
            List<BudgetIndicator> indicators = new ArrayList<>();

            EasyExcel.read(file.getInputStream(), BudgetIndicator.class, new AnalysisEventListener<BudgetIndicator>() {
                @Override
                public void invoke(BudgetIndicator indicator, AnalysisContext context) {
                    indicators.add(indicator);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 所有数据读取完成
                }
            }).sheet().doRead();

            // 处理数据
            for (BudgetIndicator indicator : indicators) {
                try {
                    // 检查必填字段
                    if (!StringUtils.hasText(indicator.getIndicatorCode())) {
                        errorList.add(createErrorInfo(indicator, "指标编码不能为空"));
                        continue;
                    }
                    if (!StringUtils.hasText(indicator.getIndicatorName())) {
                        errorList.add(createErrorInfo(indicator, "指标名称不能为空"));
                        continue;
                    }

                    // 检查编码是否重复
                    if (checkCodeExists(indicator.getIndicatorCode())) {
                        errorList.add(createErrorInfo(indicator, "指标编码已存在"));
                        continue;
                    }

                    // 设置默认值
                    if (indicator.getIsDeleted() == null) {
                        indicator.setIsDeleted(0);
                    }
                    if (indicator.getIsRequired() == null) {
                        indicator.setIsRequired(0);
                    }
                    if (indicator.getIsCalculated() == null) {
                        indicator.setIsCalculated(0);
                    }
                    if (indicator.getIsEnabled() == null) {
                        indicator.setIsEnabled(1);
                    }
                    if (indicator.getDecimalPlaces() == null) {
                        indicator.setDecimalPlaces(2);
                    }
                    indicator.setCreateTime(new Date());
                    indicator.setUpdateTime(new Date());

                    // 插入数据库
                    indicatorMapper.insert(indicator);
                    successList.add(indicator);

                } catch (Exception e) {
                    errorList.add(createErrorInfo(indicator, "导入失败：" + e.getMessage()));
                }
            }

            result.put("successCount", successList.size());
            result.put("errorCount", errorList.size());
            result.put("errors", errorList);

            log.info("导入指标完成，成功: {}, 失败: {}", successList.size(), errorList.size());

        } catch (Exception e) {
            log.error("导入指标异常", e);
            throw new ServiceException("导入失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public void exportIndicators(HttpServletResponse response) {
        try {
            // 查询所有未删除的指标
            QueryWrapper<BudgetIndicator> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_DELETED", 0)
                   .orderByDesc("CREATE_TIME");
            List<BudgetIndicator> indicators = indicatorMapper.selectList(wrapper);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("预算指标", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 写入 Excel
            EasyExcel.write(response.getOutputStream(), BudgetIndicator.class)
                    .sheet("预算指标")
                    .doWrite(indicators);

            log.info("导出指标成功，数量: {}", indicators.size());

        } catch (Exception e) {
            log.error("导出指标异常", e);
            throw new ServiceException("导出失败：" + e.getMessage());
        }
    }

    /**
     * 创建错误信息
     */
    private Map<String, String> createErrorInfo(BudgetIndicator indicator, String errorMessage) {
        Map<String, String> errorInfo = new HashMap<>();
        errorInfo.put("indicatorCode", indicator.getIndicatorCode() != null ? indicator.getIndicatorCode() : "");
        errorInfo.put("indicatorName", indicator.getIndicatorName() != null ? indicator.getIndicatorName() : "");
        errorInfo.put("error", errorMessage);
        return errorInfo;
    }
}

