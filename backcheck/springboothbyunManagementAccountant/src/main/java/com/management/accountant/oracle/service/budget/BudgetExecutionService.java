package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Execution Service接口
 * 
 * @description Execution业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetExecutionService {

    /**
     * createExecution
     */
    Map<String, Object> createExecution(Map<String, Object> params, String companyId, String userId);

    /**
     * getExecutionDetail
     */
    Map<String, Object> getExecutionDetail(Map<String, Object> params, String companyId, String userId);

    /**
     * getProgressAnalysis
     */
    Map<String, Object> getProgressAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * getVarianceAnalysis
     */
    Map<String, Object> getVarianceAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * getTrendAnalysis
     */
    Map<String, Object> getTrendAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * generateExecutionReport
     */
    Map<String, Object> generateExecutionReport(Map<String, Object> params, String companyId, String userId);

    /**
     * getBudgetExecutionDetail
     */
    Map<String, Object> getBudgetExecutionDetail(String companyId, String userId);

    /**
     * getBudgetExecutionAccounts
     */
    Map<String, Object> getBudgetExecutionAccounts(String companyId, String userId);

    /**
     * checkBudgetExecutionWarnings
     */
    Map<String, Object> checkBudgetExecutionWarnings(Map<String, Object> params, String companyId, String userId);

    /**
     * exportBudgetExecutionReport
     */
    Map<String, Object> exportBudgetExecutionReport(Map<String, Object> params, String companyId, String userId);

    /**
     * exportBudgetExecutionSingle
     */
    Map<String, Object> exportBudgetExecutionSingle(Map<String, Object> params, String companyId, String userId);

}
