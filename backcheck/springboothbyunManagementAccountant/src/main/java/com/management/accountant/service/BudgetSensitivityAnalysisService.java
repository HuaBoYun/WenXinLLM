package com.management.accountant.service;

import java.util.Map;

/**
 * 预算敏感性分析Service接口
 * 
 * @description 预算敏感性分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetSensitivityAnalysisService {

    /**
     * 单因素敏感性分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> singleFactorAnalysis(Map<String, Object> params);

    /**
     * 多因素敏感性分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> multiFactorAnalysis(Map<String, Object> params);

    /**
     * 生成敏感性分析报告
     * 
     * @param params 报告参数
     * @return 报告信息
     */
    Map<String, Object> generateReport(Map<String, Object> params);

    Map<String, Object> getSensitivityStats();

    Map<String, Object> getSensitivityChartData(Map<String, Object> params);
}

