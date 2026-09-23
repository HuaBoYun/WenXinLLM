package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetWorkflow;

import java.util.List;
import java.util.Map;

public interface BudgetWorkflowService {
    BudgetWorkflow create(BudgetWorkflow workflow);
    BudgetWorkflow getById(String workflowId);
    void update(BudgetWorkflow workflow);
    void delete(String workflowId);
    Map<String, Object> getPage(Map<String, Object> params);
    List<Map<String, Object>> getCategoryTree();
    List<Map<String, Object>> getCategories();
    void deploy(String workflowId);
    void batchDeploy(List<String> ids);
    Map<String, Object> getStats();
}

