package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Comparison Service接口
 * 
 * @description Comparison业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetComparisonService {

    /**
     * exportAnalysisReport
     */
    Map<String, Object> exportAnalysisReport(Map<String, Object> params, String companyId, String userId);

    /**
     * updateBudgetComparison
     */
    Map<String, Object> updateBudgetComparison(Map<String, Object> params, String companyId, String userId);

}
