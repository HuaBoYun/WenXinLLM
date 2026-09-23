package com.management.accountant.service;

import java.util.Map;

/**
 * 预算集成监控Service接口
 * 
 * @description 预算集成监控业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationMonitorService {

    /**
     * 获取监控概览
     * 
     * @param params 查询参数
     * @return 监控概览
     */
    Map<String, Object> getMonitorOverview(Map<String, Object> params);

    /**
     * 性能监控
     * 
     * @param params 监控参数
     * @return 性能信息
     */
    Map<String, Object> monitorPerformance(Map<String, Object> params);

    /**
     * 告警管理
     *
     * @param params 告警参数
     * @return 告警信息
     */
    Map<String, Object> manageAlert(Map<String, Object> params);

    /**
     * 获取日志详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    Map<String, Object> getLogDetail(String logId);
}

