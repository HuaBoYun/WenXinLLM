package com.financial.sharing.service;

import java.util.Map;

/**
 * 收入分析服务接口
 */
public interface RevenueAnalysisService {
    
    /**
     * 获取收入结构分析
     * @param dimension 分析维度 (1-按产品 2-按客户 3-按部门)
     * @param dateRange 日期范围
     * @return 分析结果
     */
    Map<String, Object> getRevenueStructureAnalysis(Integer dimension, String[] dateRange);
    
    /**
     * 获取收入质量分析
     * @return 分析结果
     */
    Map<String, Object> getRevenueQualityAnalysis();
    
    /**
     * 获取收入预测模型
     * @param model 预测模型类型 (1-线性回归 2-移动平均 3-指数平滑)
     * @param period 预测期数
     * @return 预测结果
     */
    Map<String, Object> getRevenueForecastModel(Integer model, Integer period);
}

