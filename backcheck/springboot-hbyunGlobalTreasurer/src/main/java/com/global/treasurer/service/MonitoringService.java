package com.global.treasurer.service;

import com.global.treasurer.entity.TblMonitoring;
import java.util.List;
import java.util.Map;

public interface MonitoringService {
    Map<String, Object> getMonitoringPage(Map<String, Object> params);
    TblMonitoring getMonitoringById(Long monitoringId);
    int createMonitoring(TblMonitoring monitoring);
    int updateMonitoring(TblMonitoring monitoring);
    int deleteMonitoring(List<Long> monitoringIds);
    List<TblMonitoring> getAlerts(Long orgId);
    List<TblMonitoring> getCriticalAlerts(Long orgId);
    Map<String, Object> getSystemHealthStatus(Long orgId);
    Map<String, Object> getDashboardData(Long orgId);
    List<Map<String, Object>> getMonitoringTrend(Map<String, Object> params);
    int sendAlert(Long monitoringId);
}
