package com.management.accountant.service;

import java.util.Map;

/**
 * 预算对比分析Service接口
 * 
 * @description 预算对比分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetComparisonAnalysisService {

    /**
     * 执行对比分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> executeComparisonAnalysis(Map<String, Object> params);

    /**
     * 获取对比图表数据
     * 
     * @param params 查询参数
     * @return 图表数据
     */
    Map<String, Object> getComparisonChart(Map<String, Object> params);

    /**
     * 导出对比分析报告
     * 
     * @param params 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportComparisonReport(Map<String, Object> params);

    Map<String, Object> getComparisonStats();

    Map<String, Object> getComparisonChartData(Map<String, Object> params);

    /**
     * 获取组织单元选项
     */
    java.util.List<Map<String, Object>> getOrganizations();

    /**
     * 获取预算科目选项
     */
    java.util.List<Map<String, Object>> getBudgetAccounts();

    /**
     * 更新对比分析
     */
    Map<String, Object> updateComparison(Map<String, Object> params);

    /**
     * 批量对比分析
     */
    Map<String, Object> batchComparison(Map<String, Object> params);

    /**
     * 删除对比分析
     */
    Map<String, Object> deleteComparison(Map<String, Object> params);
}

