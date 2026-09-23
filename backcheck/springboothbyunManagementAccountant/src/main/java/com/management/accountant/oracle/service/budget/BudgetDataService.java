package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Data Service接口
 * 
 * @description Data业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetDataService {

    /**
     * startBudgetTask
     */
    Map<String, Object> startBudgetTask(Map<String, Object> params, String companyId, String userId);

}
