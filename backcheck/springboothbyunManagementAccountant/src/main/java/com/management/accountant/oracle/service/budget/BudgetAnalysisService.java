package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Analysis Service接口
 * 
 * @description Analysis业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetAnalysisService {

    /**
     * createMultiDimensionAnalysis
     */
    Map<String, Object> createMultiDimensionAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * executeMultiDimensionAnalysis
     */
    Map<String, Object> executeMultiDimensionAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * sliceAnalysis
     */
    Map<String, Object> sliceAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * pivotAnalysis
     */
    Map<String, Object> pivotAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * budgetActualComparison
     */
    Map<String, Object> budgetActualComparison(Map<String, Object> params, String companyId, String userId);

    /**
     * periodComparison
     */
    Map<String, Object> periodComparison(Map<String, Object> params, String companyId, String userId);

    /**
     * trendAnalysis
     */
    Map<String, Object> trendAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * varianceAnalysis
     */
    Map<String, Object> varianceAnalysis(Map<String, Object> params, String companyId, String userId);

    /**
     * getVarianceAnalysisReport
     */
    Map<String, Object> getVarianceAnalysisReport(Map<String, Object> params, String companyId, String userId);

    /**
     * parseQuery
     */
    Map<String, Object> parseQuery(Map<String, Object> params, String companyId, String userId);

    /**
     * getQuerySuggestions
     */
    Map<String, Object> getQuerySuggestions(String companyId, String userId);

    /**
     * saveQueryHistory
     */
    Map<String, Object> saveQueryHistory(Map<String, Object> params, String companyId, String userId);

    /**
     * getQueryHistory
     */
    Map<String, Object> getQueryHistory(Map<String, Object> params, String companyId, String userId);

    /**
     * getAnalysisRecommendations
     */
    Map<String, Object> getAnalysisRecommendations(Map<String, Object> params, String companyId, String userId);

    /**
     * getIndicatorRecommendations
     */
    Map<String, Object> getIndicatorRecommendations(Map<String, Object> params, String companyId, String userId);

    /**
     * getDimensionRecommendations
     */
    Map<String, Object> getDimensionRecommendations(Map<String, Object> params, String companyId, String userId);

    /**
     * getAnomalyRecommendations
     */
    Map<String, Object> getAnomalyRecommendations(Map<String, Object> params, String companyId, String userId);

    /**
     * feedbackRecommendation
     */
    Map<String, Object> feedbackRecommendation(Map<String, Object> params, String companyId, String userId);

    /**
     * getAnalysisReport
     */
    Map<String, Object> getAnalysisReport(Map<String, Object> params, String companyId, String userId);

    /**
     * exportVarianceAnalysisSingle
     */
    Map<String, Object> exportVarianceAnalysisSingle(String companyId, String userId);

    /**
     * getAnalysisTableData
     */
    Map<String, Object> getAnalysisTableData(String companyId, String userId);

    /**
     * getAnalysisInsights
     */
    Map<String, Object> getAnalysisInsights(String companyId, String userId);

}
