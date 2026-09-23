package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Variance Service接口
 * 
 * @description Variance业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetVarianceService {

    /**
     * executeBudgetComparison
     */
    Map<String, Object> executeBudgetComparison(Map<String, Object> params, String companyId, String userId);

    /**
     * updateBudgetVariance
     */
    Map<String, Object> updateBudgetVariance(Map<String, Object> params, String companyId, String userId);

    /**
     * executeBudgetTrend
     */
    Map<String, Object> executeBudgetTrend(Map<String, Object> params, String companyId, String userId);

    /**
     * getVarianceAnalysisOrganizations
     */
    Map<String, Object> getVarianceAnalysisOrganizations(String companyId, String userId);

    /**
     * getVarianceAnalysisBudgetAccounts
     */
    Map<String, Object> getVarianceAnalysisBudgetAccounts(String companyId, String userId);

    /**
     * getVarianceAnalysisUsers
     */
    Map<String, Object> getVarianceAnalysisUsers(String companyId, String userId);

    /**
     * updateVarianceAnalysisReason
     */
    Map<String, Object> updateVarianceAnalysisReason(Map<String, Object> params, String companyId, String userId);

    /**
     * exportVarianceAnalysisReport
     */
    Map<String, Object> exportVarianceAnalysisReport(Map<String, Object> params, String companyId, String userId);

}
