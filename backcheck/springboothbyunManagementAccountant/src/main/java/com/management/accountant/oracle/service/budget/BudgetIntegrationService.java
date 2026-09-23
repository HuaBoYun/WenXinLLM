package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Integration Service接口
 * 
 * @description Integration业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetIntegrationService {

    /**
     * createERPIntegrationConfig
     */
    Map<String, Object> createERPIntegrationConfig(Map<String, Object> params, String companyId, String userId);

    /**
     * getERPIntegrationConfig
     */
    Map<String, Object> getERPIntegrationConfig(Map<String, Object> params, String companyId, String userId);

    /**
     * testERPConnection
     */
    Map<String, Object> testERPConnection(Map<String, Object> params, String companyId, String userId);

    /**
     * getBIIntegrationConfig
     */
    Map<String, Object> getBIIntegrationConfig(Map<String, Object> params, String companyId, String userId);

    /**
     * testBIConnection
     */
    Map<String, Object> testBIConnection(Map<String, Object> params, String companyId, String userId);

    /**
     * getThirdPartyIntegrationConfig
     */
    Map<String, Object> getThirdPartyIntegrationConfig(Map<String, Object> params, String companyId, String userId);

    /**
     * testThirdPartyConnection
     */
    Map<String, Object> testThirdPartyConnection(Map<String, Object> params, String companyId, String userId);

    /**
     * getDataMapping
     */
    Map<String, Object> getDataMapping(Map<String, Object> params, String companyId, String userId);

    /**
     * testDataMapping
     */
    Map<String, Object> testDataMapping(String companyId, String userId);

    /**
     * getIntegrationLogPage
     */
    Map<String, Object> getIntegrationLogPage(Map<String, Object> params, String companyId, String userId);

    /**
     * getIntegrationLogDetail
     */
    Map<String, Object> getIntegrationLogDetail(String companyId, String userId);

    /**
     * getIntegrationStatusWithParams
     */
    Map<String, Object> getIntegrationStatusWithParams(String companyId, String userId);

    /**
     * getIntegrationPerformanceMetrics
     */
    Map<String, Object> getIntegrationPerformanceMetrics(String companyId, String userId);

    /**
     * getIntegrationRecentActivities
     */
    Map<String, Object> getIntegrationRecentActivities(String companyId, String userId);

    /**
     * getIntegrationNotifications
     */
    Map<String, Object> getIntegrationNotifications(String companyId, String userId);

    /**
     * markIntegrationNotificationAsRead
     */
    Map<String, Object> markIntegrationNotificationAsRead(String companyId, String userId);

}
