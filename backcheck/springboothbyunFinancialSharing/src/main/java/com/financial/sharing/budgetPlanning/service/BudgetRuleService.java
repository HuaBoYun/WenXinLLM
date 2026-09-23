package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetRuleQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetRule;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 业务规则Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetRuleService {

    /**
     * 查询业务规则列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetRule> getRuleList(BudgetRuleQueryParam param);

    /**
     * 查询业务规则列表(不分页)
     * 
     * @param param 查询参数
     * @return 业务规则列表
     */
    List<TblBudgetRule> getRuleListNoPage(BudgetRuleQueryParam param);

    /**
     * 根据ID查询业务规则
     * 
     * @param ruleId 规则ID
     * @return 业务规则
     */
    TblBudgetRule getRuleById(String ruleId);

    /**
     * 新增业务规则
     * 
     * @param rule 业务规则
     */
    void addRule(TblBudgetRule rule);

    /**
     * 修改业务规则
     * 
     * @param rule 业务规则
     */
    void updateRule(TblBudgetRule rule);

    /**
     * 删除业务规则
     * 
     * @param ruleId 规则ID
     */
    void deleteRule(String ruleId);

    /**
     * 批量删除业务规则
     * 
     * @param ruleIds 规则ID列表
     */
    void batchDeleteRule(List<String> ruleIds);

    /**
     * 复制业务规则
     * 
     * @param ruleId 源规则ID
     * @param newRuleCode 新规则编码
     * @param newRuleName 新规则名称
     * @return 新规则ID
     */
    String copyRule(String ruleId, String newRuleCode, String newRuleName);

    /**
     * 启用业务规则
     * 
     * @param ruleId 规则ID
     */
    void enableRule(String ruleId);

    /**
     * 停用业务规则
     * 
     * @param ruleId 规则ID
     */
    void disableRule(String ruleId);
}

