package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.dto.TblBudgetControlRuleQueryParam;
import com.financial.sharing.dto.TblBudgetControlRuleSaveParam;
import com.financial.sharing.entity.TblBudgetControlRule;
import com.financial.sharing.entity.TblBudgetControlScope;
import com.financial.sharing.mapper.TblBudgetControlRuleMapper;
import com.financial.sharing.mapper.TblBudgetControlScopeMapper;
import com.financial.sharing.service.TblBudgetControlRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 预算控制规则Service实现类
 */
@Slf4j
@Service
public class TblBudgetControlRuleServiceImpl extends ServiceImpl<TblBudgetControlRuleMapper, TblBudgetControlRule>
        implements TblBudgetControlRuleService {

    @Resource
    private TblBudgetControlRuleMapper mapper;

    @Resource
    private TblBudgetControlScopeMapper budgetControlScopeMapper;

    @Override
    public MyJsonBean<PageResult> getList(TblBudgetControlRuleQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNo(), param.getPageSize());

            QueryWrapper<TblBudgetControlRule> queryWrapper = new QueryWrapper<>();

            // 规则名称模糊查询
            if (param.getRuleName() != null && !param.getRuleName().isEmpty()) {
                queryWrapper.like("RULE_NAME", param.getRuleName());
            }

            // 规则编码查询
            if (param.getRuleCode() != null && !param.getRuleCode().isEmpty()) {
                queryWrapper.like("RULE_CODE", param.getRuleCode());
            }

            // 控制类型查询
            if (param.getControlType() != null && !param.getControlType().isEmpty()) {
                queryWrapper.eq("CONTROL_TYPE", param.getControlType());
            }

            // 是否启用查询
            if (param.getIsEnabled() != null) {
                queryWrapper.eq("IS_ENABLED", param.getIsEnabled());
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblBudgetControlRule> list = mapper.selectList(queryWrapper);
            PageInfo<TblBudgetControlRule> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            result.setCurrentPage(pageInfo.getPageNum());
            result.setPageSize(pageInfo.getPageSize());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询预算控制规则列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String ruleId) {
        try {
            TblBudgetControlRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }
            return MyJsonBean.successData(rule);
        } catch (Exception e) {
            log.error("查询预算控制规则详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblBudgetControlRuleSaveParam param) {
        try {
            TblBudgetControlRule rule = new TblBudgetControlRule();

            if (param.getRuleId() != null && !param.getRuleId().isEmpty()) {
                // 更新
                rule = mapper.selectById(param.getRuleId());
                if (rule == null) {
                    return MyJsonBean.errorData("规则不存在");
                }
                rule.setUpdateTime(LocalDateTime.now());
                rule.setUpdateUser(param.getUpdateUser());
            } else {
                // 新增
                rule.setCreateTime(LocalDateTime.now());
                rule.setCreateUser(param.getCreateUser());
            }

            // 设置基本属性
            rule.setRuleCode(param.getRuleCode());
            rule.setRuleName(param.getRuleName());
            rule.setControlType(param.getControlType());

            // 类型转换：String to BigDecimal
            if (param.getWarningThreshold() != null && !param.getWarningThreshold().isEmpty()) {
                rule.setWarningThreshold(new BigDecimal(param.getWarningThreshold()));
            }
            if (param.getControlThreshold() != null && !param.getControlThreshold().isEmpty()) {
                rule.setControlThreshold(new BigDecimal(param.getControlThreshold()));
            }

            // 直接设置日期（DTO 和 Entity 都使用 LocalDate）
            rule.setEffectiveDate(param.getEffectiveDate());
            rule.setExpiryDate(param.getExpiryDate());

            rule.setIsEnabled(param.getIsEnabled());
            rule.setRemark(param.getRemark());

            if (param.getRuleId() != null && !param.getRuleId().isEmpty()) {
                mapper.updateById(rule);
            } else {
                mapper.insert(rule);
            }

            return MyJsonBean.successData("保存成功", rule);
        } catch (Exception e) {
            log.error("保存预算控制规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String ruleId) {
        try {
            TblBudgetControlRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            mapper.deleteById(ruleId);

            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除预算控制规则失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String ruleId, Integer isEnabled) {
        try {
            TblBudgetControlRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            rule.setIsEnabled(isEnabled);
            rule.setUpdateTime(LocalDateTime.now());
            mapper.updateById(rule);

            return MyJsonBean.successMsg("状态更新成功");
        } catch (Exception e) {
            log.error("更新预算控制规则状态失败", e);
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean checkBudget(String ruleId, BigDecimal amount, Map<String, Object> checkData) {
        try {
            TblBudgetControlRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            // 从检查数据中提取部门ID、项目ID等信息
            String departmentId = checkData.get("departmentId") != null ? checkData.get("departmentId").toString() : null;
            String projectId = checkData.get("projectId") != null ? checkData.get("projectId").toString() : null;
            String expenseType = checkData.get("expenseType") != null ? checkData.get("expenseType").toString() : null;

            // TODO: 实现完整的预算检查逻辑
            // 1. 根据规则的控制类型和适用范围，判断是否需要检查
            // 2. 查询预算数据和已使用金额
            // 3. 根据规则的预警阈值和控制阈值进行判断

            // 模拟预算检查结果
            BigDecimal budgetAmount = new BigDecimal("100000.00");
            BigDecimal usedAmount = new BigDecimal("75000.00");
            BigDecimal remainAmount = budgetAmount.subtract(usedAmount);
            BigDecimal usageRate = usedAmount.divide(budgetAmount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));

            String checkResult;
            String checkMessage;

            // 根据规则阈值判断
            if (usageRate.compareTo(rule.getControlThreshold()) >= 0) {
                checkResult = "REJECT";
                checkMessage = String.format("预算使用率已达到%.2f%%，超过控制阈值%.2f%%，不允许继续使用",
                        usageRate, rule.getControlThreshold());
            } else if (usageRate.compareTo(rule.getWarningThreshold()) >= 0) {
                checkResult = "WARNING";
                checkMessage = String.format("预算使用率已达到%.2f%%，超过预警阈值%.2f%%，请注意控制",
                        usageRate, rule.getWarningThreshold());
            } else {
                checkResult = "PASS";
                checkMessage = "预算检查通过";
            }

            Map<String, Object> result = new HashMap<>();
            result.put("ruleId", ruleId);
            result.put("ruleName", rule.getRuleName());
            result.put("checkResult", checkResult);
            result.put("checkMessage", checkMessage);
            result.put("budgetAmount", budgetAmount);
            result.put("usedAmount", usedAmount);
            result.put("remainAmount", remainAmount);
            result.put("usageRate", usageRate);
            result.put("checkTime", new Date());

            // 添加检查数据到结果中
            if (departmentId != null) {
                result.put("departmentId", departmentId);
            }
            if (projectId != null) {
                result.put("projectId", projectId);
            }
            if (expenseType != null) {
                result.put("expenseType", expenseType);
            }

            log.info("预算检查完成 - 规则ID: {}, 金额: {}, 检查结果: {}", ruleId, amount, checkResult);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("预算检查失败", e);
            return MyJsonBean.errorData("检查失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getControlScopes(String ruleId) {
        try {
            // 从数据库查询规则的控制范围配置
            List<TblBudgetControlScope> scopeList = budgetControlScopeMapper.selectByRuleId(ruleId);

            // 转换为前端需要的格式
            List<Map<String, Object>> scopes = new ArrayList<>();
            for (TblBudgetControlScope scope : scopeList) {
                Map<String, Object> scopeMap = new HashMap<>();
                scopeMap.put("scopeId", scope.getScopeId());
                scopeMap.put("scopeType", scope.getScopeType());
                scopeMap.put("scopeValue", scope.getScopeValue());
                scopeMap.put("createTime", scope.getCreateTime());
                scopes.add(scopeMap);
            }

            return MyJsonBean.successData(scopes);
        } catch (Exception e) {
            log.error("获取控制范围失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean saveControlScopes(String ruleId, List<Map<String, String>> scopes) {
        try {
            log.info("保存预算管控规则适用范围, ruleId: {}, scopes: {}", ruleId, scopes);

            // 1. 验证规则是否存在
            TblBudgetControlRule rule = super.getById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            // 2. 验证范围数据
            if (scopes == null || scopes.isEmpty()) {
                return MyJsonBean.errorData("范围数据不能为空");
            }

            for (Map<String, String> scope : scopes) {
                String scopeType = scope.get("scopeType");
                String scopeValue = scope.get("scopeValue");

                if (scopeType == null || scopeType.trim().isEmpty()) {
                    return MyJsonBean.errorData("范围类型不能为空");
                }

                if (scopeValue == null || scopeValue.trim().isEmpty()) {
                    return MyJsonBean.errorData("范围值不能为空");
                }
            }

            // 3. 删除旧的范围配置
            budgetControlScopeMapper.deleteByRuleId(ruleId);

            // 4. 构建新的范围实体列表
            List<TblBudgetControlScope> scopeEntities = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();

            for (Map<String, String> scope : scopes) {
                TblBudgetControlScope entity = new TblBudgetControlScope();
                entity.setScopeId(UUID.randomUUID().toString().replace("-", ""));
                entity.setRuleId(ruleId);
                entity.setScopeType(scope.get("scopeType"));
                entity.setScopeValue(scope.get("scopeValue"));
                entity.setCreateTime(now);
                entity.setCreateUser("system"); // TODO: 从上下文获取当前用户
                scopeEntities.add(entity);
            }

            // 5. 批量插入新的范围配置
            if (!scopeEntities.isEmpty()) {
                budgetControlScopeMapper.batchInsert(scopeEntities);
            }

            log.info("保存预算管控规则适用范围成功, ruleId: {}, 保存了 {} 条记录", ruleId, scopeEntities.size());
            return MyJsonBean.ok("保存成功");
        } catch (Exception e) {
            log.error("保存控制范围失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getExecutionRecords(String ruleId) {
        try {
            // TODO: 实现获取执行记录逻辑
            // 这里应该查询规则的执行记录

            List<Map<String, Object>> records = new ArrayList<>();
            Map<String, Object> record1 = new HashMap<>();
            record1.put("recordId", "R001");
            record1.put("executionTime", LocalDateTime.now());
            record1.put("result", "PASS");
            records.add(record1);

            Map<String, Object> record2 = new HashMap<>();
            record2.put("recordId", "R002");
            record2.put("executionTime", LocalDateTime.now());
            record2.put("result", "WARNING");
            records.add(record2);

            return MyJsonBean.successData(records);
        } catch (Exception e) {
            log.error("获取执行记录失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }
}
