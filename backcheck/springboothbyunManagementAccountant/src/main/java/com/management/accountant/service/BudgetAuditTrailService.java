package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAuditTrail;
import com.management.accountant.util.PageResult;
import java.util.List;
import java.util.Map;

public interface BudgetAuditTrailService {
    PageResult<BudgetAuditTrail> getPage(Map<String, Object> params);
    BudgetAuditTrail getById(String auditId);
    Map<String, Object> getStats();
    List<BudgetAuditTrail> exportData(Map<String, Object> params);
    int cleanupLogs(int retentionDays);
    Map<String, Object> getLogAnalysis(Map<String, Object> params);
}

