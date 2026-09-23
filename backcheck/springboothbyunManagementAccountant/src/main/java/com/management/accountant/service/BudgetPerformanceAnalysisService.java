package com.management.accountant.service;

import java.util.Map;

/**
 * 预算绩效分析Service接口
 * 
 * @description 预算绩效分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetPerformanceAnalysisService {

    /**
     * 执行绩效分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> executePerformanceAnalysis(Map<String, Object> params);

    /**
     * 获取KPI指标
     * 
     * @param params 查询参数
     * @return KPI数据
     */
    Map<String, Object> getKPIIndicators(Map<String, Object> params);

    /**
     * 生成绩效报告
     * 
     * @param params 报告参数
     * @return 报告信息
     */
    Map<String, Object> generatePerformanceReport(Map<String, Object> params);
}

