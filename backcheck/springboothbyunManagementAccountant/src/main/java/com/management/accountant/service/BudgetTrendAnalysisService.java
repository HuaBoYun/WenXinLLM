package com.management.accountant.service;

import java.util.Map;

/**
 * 预算趋势分析Service接口
 * 
 * @description 预算趋势分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetTrendAnalysisService {

    /**
     * 执行趋势分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> executeTrendAnalysis(Map<String, Object> params);

    /**
     * 获取趋势图表数据
     * 
     * @param params 查询参数
     * @return 图表数据
     */
    Map<String, Object> getTrendChart(Map<String, Object> params);

    /**
     * 导出趋势分析报告
     * 
     * @param params 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportTrendReport(Map<String, Object> params);

    Map<String, Object> getTrendStats();

    Map<String, Object> getTrendChartData(Map<String, Object> params);
}

