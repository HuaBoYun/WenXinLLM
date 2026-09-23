package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Advanced Service接口
 * 
 * @description Advanced业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetAdvancedService {

    /**
     * getFeatureStats
     */
    Map<String, Object> getFeatureStats(String companyId, String userId);

    /**
     * getRecentActivities
     */
    Map<String, Object> getRecentActivities(String companyId, String userId);

    /**
     * getNotifications
     */
    Map<String, Object> getNotifications(String companyId, String userId);

}
