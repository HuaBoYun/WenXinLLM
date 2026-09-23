package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetPeriod;

import java.util.List;
import java.util.Map;

public interface BudgetPeriodService {
    BudgetPeriod create(BudgetPeriod period);
    BudgetPeriod getById(String periodId);
    void update(BudgetPeriod period);
    void delete(String periodId);
    Map<String, Object> getPage(Map<String, Object> params);
    void openPeriod(String periodId);
    void closePeriod(String periodId);
    void updateLockStatus(String periodId, Boolean isLocked);
    void initYear(Map<String, Object> params);
    void closeYear(Map<String, Object> params);
    void batchOpen(List<String> ids);
    void batchClose(List<String> ids);
    void setCurrent(String periodId);
    Map<String, Object> getStats();
    void exportPeriods(Map<String, Object> params, javax.servlet.http.HttpServletResponse response);
}

