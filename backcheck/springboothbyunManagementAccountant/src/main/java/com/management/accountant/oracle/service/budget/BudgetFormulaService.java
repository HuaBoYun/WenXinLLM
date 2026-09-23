package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Formula Service接口
 * 
 * @description Formula业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetFormulaService {

    /**
     * executeRollingBudget
     */
    Map<String, Object> executeRollingBudget(Map<String, Object> params, String companyId, String userId);

    /**
     * getFormulaTraceTask
     */
    Map<String, Object> getFormulaTraceTask(Map<String, Object> params, String companyId, String userId);

}
