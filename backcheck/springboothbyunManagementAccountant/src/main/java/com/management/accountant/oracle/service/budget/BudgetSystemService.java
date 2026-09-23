package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * System Service接口
 * 
 * @description System业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetSystemService {

    /**
     * copyOrganizationStructure
     */
    Map<String, Object> copyOrganizationStructure(String companyId, String userId);

    /**
     * getSystemModules
     */
    Map<String, Object> getSystemModules(String companyId, String userId);

    /**
     * getSystemHealth
     */
    Map<String, Object> getSystemHealth(String companyId, String userId);

    /**
     * getRecentActivities
     */
    Map<String, Object> getRecentActivities(String companyId, String userId);

}
