package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetWarningRule;

import java.util.List;

/**
 * 预算预警规则Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetWarningRuleService extends IService<BudgetWarningRule> {

    /**
     * 根据规则编码查询规则
     * 
     * @param ruleCode 规则编码
     * @return 预警规则
     */
    BudgetWarningRule getByRuleCode(String ruleCode);

    /**
     * 查询启用的预警规则
     * 
     * @return 规则列表
     */
    List<BudgetWarningRule> getEnabledRules();

    /**
     * 根据预警类型查询规则
     * 
     * @param warningType 预警类型
     * @return 规则列表
     */
    List<BudgetWarningRule> getByWarningType(String warningType);

    /**
     * 创建预警规则
     * 
     * @param rule 预警规则
     * @return 是否成功
     */
    boolean createRule(BudgetWarningRule rule);

    /**
     * 更新预警规则
     * 
     * @param rule 预警规则
     * @return 是否成功
     */
    boolean updateRule(BudgetWarningRule rule);

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
     * 检查预警触发
     * 
     * @param budgetId 预算ID
     * @return 预警信息列表
     */
    List<java.util.Map<String, Object>> checkWarningTriggers(String budgetId);

    /**
     * 发送预警通知
     * 
     * @param ruleId 规则ID
     * @param message 预警消息
     * @return 是否成功
     */
    boolean sendWarningNotification(String ruleId, String message);

    /**
     * 分页查询规则列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param rule 查询条件
     * @return 规则列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetWarningRule> pageQuery(
        int pageNum, int pageSize, BudgetWarningRule rule);

    /**
     * 检查预警
     *
     * @param budgetId 预算ID
     * @return 预警信息列表
     */
    java.util.List<java.util.Map<String, Object>> checkWarnings(String budgetId);
}

