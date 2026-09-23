package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetControlRule;

import java.util.List;

/**
 * 预算控制规则Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetControlRuleService extends IService<BudgetControlRule> {

    /**
     * 根据规则编码查询规则
     * 
     * @param ruleCode 规则编码
     * @return 控制规则
     */
    BudgetControlRule getByRuleCode(String ruleCode);

    /**
     * 查询启用的控制规则
     * 
     * @return 规则列表
     */
    List<BudgetControlRule> getEnabledRules();

    /**
     * 根据控制类型查询规则
     * 
     * @param controlType 控制类型
     * @return 规则列表
     */
    List<BudgetControlRule> getByControlType(String controlType);

    /**
     * 创建控制规则
     * 
     * @param rule 控制规则
     * @return 是否成功
     */
    boolean createRule(BudgetControlRule rule);

    /**
     * 更新控制规则
     * 
     * @param rule 控制规则
     * @return 是否成功
     */
    boolean updateRule(BudgetControlRule rule);

    /**
     * 启用/禁用规则
     * 
     * @param ruleId 规则ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean toggleRule(String ruleId, boolean enabled);

    /**
     * 批量启用/禁用规则
     * 
     * @param ruleIds 规则ID列表
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean batchToggleRules(List<String> ruleIds, boolean enabled);

    /**
     * 批量删除规则
     * 
     * @param ruleIds 规则ID列表
     * @return 是否成功
     */
    boolean batchDeleteRules(List<String> ruleIds);

    /**
     * 验证预算控制
     * 
     * @param budgetId 预算ID
     * @param amount 金额
     * @return 验证结果
     */
    java.util.Map<String, Object> validateBudgetControl(String budgetId, java.math.BigDecimal amount);

    /**
     * 分页查询规则列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param rule 查询条件
     * @return 规则列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetControlRule> pageQuery(
        int pageNum, int pageSize, BudgetControlRule rule);
}

