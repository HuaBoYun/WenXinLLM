package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAuditAlert;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

public interface BudgetAuditAlertService {

    PageResult<BudgetAuditAlert> getPage(Map<String, Object> params);

    BudgetAuditAlert getById(String alertId);

    void handleAlert(String alertId, String handlerId, String handlerName, String handleRemark, String alertStatus);

    Map<String, Object> getAlertStats();

    List<BudgetAuditAlert> getPendingAlerts();
}
