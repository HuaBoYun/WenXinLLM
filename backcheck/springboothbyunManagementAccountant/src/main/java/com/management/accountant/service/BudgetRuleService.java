package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetRule;

import java.util.List;
import java.util.Map;

public interface BudgetRuleService {
    BudgetRule create(BudgetRule rule);
    BudgetRule getById(String ruleId);
    void update(BudgetRule rule);
    void delete(String ruleId);
    Map<String, Object> getPage(Map<String, Object> params);
    List<Map<String, Object>> getCategoryTree();
    List<Map<String, Object>> getCategories();
    void updateStatus(String ruleId, String status);
    Map<String, Object> validateCondition(String condition);
    Map<String, Object> testRule(Map<String, Object> params);
    Map<String, Object> validate(String ruleId);
    Map<String, Object> batchValidate(List<String> ids);
    Map<String, Object> getStats();
}

