package com.management.accountant.service;

import java.util.Map;

/**
 * 预算预测分析Service接口
 *
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetForecastAnalysisService {

    /** 分页查询预测分析列表 */
    Map<String, Object> executeForecastAnalysis(Map<String, Object> params);

    /** 创建预测分析 */
    Map<String, Object> createForecast(Map<String, Object> params);

    /** 编辑预测分析 */
    Map<String, Object> updateForecast(Map<String, Object> params);

    /** 获取预测图表数据（预测趋势图） */
    Map<String, Object> getForecastChart(Map<String, Object> params);

    /** 导出预测分析报告 */
    Map<String, Object> exportForecastReport(Map<String, Object> params);

    /** 统计卡片数据 */
    Map<String, Object> getForecastStats();

    /** 4个图表数据（趋势图+模型性能+误差分析+残差分析） */
    Map<String, Object> getForecastChartData(Map<String, Object> params);

    /** 模型信息 */
    Map<String, Object> getForecastModelInfo();

    /** 模型验证 */
    Map<String, Object> validateModel(String modelId);

    /** 模型训练 */
    Map<String, Object> trainModel(Map<String, Object> params);

    /** 预测结果验证 */
    Map<String, Object> validateForecast(String id);

    /** 删除预测分析 */
    void deleteForecast(String id);
}

