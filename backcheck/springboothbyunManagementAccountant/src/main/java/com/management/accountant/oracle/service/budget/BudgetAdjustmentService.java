package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Adjustment Service接口
 * 
 * @description Adjustment业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetAdjustmentService {

    /**
     * createBudgetAdjustment
     */
    Map<String, Object> createBudgetAdjustment(Map<String, Object> params, String companyId, String userId);

}
