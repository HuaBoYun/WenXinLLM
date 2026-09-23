package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetSystemMonitorEntity;
import com.management.accountant.util.PageResult;
import java.util.List;
import java.util.Map;

public interface BudgetSystemMonitorService {
    PageResult<BudgetSystemMonitorEntity> getPage(Map<String, Object> params);
    BudgetSystemMonitorEntity getById(String monitorId);
    Map<String, Object> getStats();
    List<BudgetSystemMonitorEntity> exportData(Map<String, Object> params);
    int updateMonitor(BudgetSystemMonitorEntity entity);
    BudgetSystemMonitorEntity saveMonitor(BudgetSystemMonitorEntity entity);
    PageResult<BudgetSystemMonitorEntity> getAlerts(Map<String, Object> params);
    PageResult<BudgetSystemMonitorEntity> getServiceMonitors(Map<String, Object> params);
    List<BudgetSystemMonitorEntity> getNetworkTrafficData(int hours);
    Map<String, Object> getMonitorSettings();
    void saveMonitorSettings(Map<String, Object> settings);
}

