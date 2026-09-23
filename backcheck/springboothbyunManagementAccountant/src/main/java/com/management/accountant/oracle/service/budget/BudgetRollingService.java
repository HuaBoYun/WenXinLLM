package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Rolling Service接口
 * 
 * @description Rolling业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetRollingService {

    /**
     * createRollingBudgetPlan
     */
    Map<String, Object> createRollingBudgetPlan(Map<String, Object> params, String companyId, String userId);

    /**
     * getRollingBudgetPlan
     */
    Map<String, Object> getRollingBudgetPlan(Map<String, Object> params, String companyId, String userId);

}
