package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Calculation Service接口
 * 
 * @description Calculation业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetCalculationService {

    /**
     * submitAdjustmentApproval
     */
    Map<String, Object> submitAdjustmentApproval(Map<String, Object> params, String companyId, String userId);

}
