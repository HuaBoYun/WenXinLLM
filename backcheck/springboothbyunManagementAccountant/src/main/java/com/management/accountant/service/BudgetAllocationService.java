package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAllocation;

import java.util.List;
import java.util.Map;

public interface BudgetAllocationService {
    BudgetAllocation create(BudgetAllocation allocation);
    BudgetAllocation getById(String allocationId);
    void update(BudgetAllocation allocation);
    void delete(String allocationId);
    Map<String, Object> getPage(Map<String, Object> params);
    Map<String, Object> getStats();
    List<Map<String, Object>> getDimensions();
    List<Map<String, Object>> getTargets();
    List<Map<String, Object>> getTargetsByDimension(String dimension);
    void confirm(String allocationId);
    void batchConfirm(List<String> ids);
    void recalculate();
}

