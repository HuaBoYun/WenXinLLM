package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetRule;

import java.util.List;

/**
 * 预算规则Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetRuleService extends IService<BudgetRule> {

    /**
     * 根据规则编码查询规则
     */
    BudgetRule getByRuleCode(String ruleCode);

    /**
     * 根据规则类型查询规则列表
     */
    List<BudgetRule> listByRuleType(String ruleType);

    /**
     * 查询启用的规则列表
     */
    List<BudgetRule> listEnabledRules();

    /**
     * 根据优先级查询规则列表
     */
    List<BudgetRule> listByPriority(Integer minPriority, Integer maxPriority);

    /**
     * 批量删除规则
     */
    boolean batchDeleteRules(List<String> ruleIds);

    /**
     * 保存或更新规则
     */
    boolean saveOrUpdateRule(BudgetRule rule);
}

