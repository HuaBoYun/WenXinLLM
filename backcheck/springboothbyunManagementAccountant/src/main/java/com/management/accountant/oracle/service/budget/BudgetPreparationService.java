package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Preparation Service接口
 * 
 * @description Preparation业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetPreparationService {

    /**
     * getPreparationStats
     */
    Map<String, Object> getPreparationStats(String companyId, String userId);

    /**
     * getPreparationTodos
     */
    Map<String, Object> getPreparationTodos(String companyId, String userId);

    /**
     * getPreparationProgress
     */
    Map<String, Object> getPreparationProgress(String companyId, String userId);

}
