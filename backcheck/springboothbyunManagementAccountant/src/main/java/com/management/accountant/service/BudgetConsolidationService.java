package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetConsolidation;

import java.util.List;
import java.util.Map;

public interface BudgetConsolidationService {
    BudgetConsolidation create(BudgetConsolidation consolidation);
    BudgetConsolidation getById(String consolidationId);
    void update(BudgetConsolidation consolidation);
    void delete(String consolidationId);
    Map<String, Object> getPage(Map<String, Object> params);
    Map<String, Object> getStats();
    void execute(String consolidationId);
    void autoConsolidate();
    Map<String, Object> validate(String consolidationId);
    Map<String, Object> batchValidate(List<String> ids);
}

