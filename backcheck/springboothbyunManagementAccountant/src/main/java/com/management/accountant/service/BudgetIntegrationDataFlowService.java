package com.management.accountant.service;

import java.util.Map;

/**
 * 预算数据流集成Service接口
 * 
 * @description 预算数据流集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationDataFlowService {

    /**
     * 创建数据流
     * 
     * @param params 创建参数
     * @return 数据流信息
     */
    Map<String, Object> createDataFlow(Map<String, Object> params);

    /**
     * 执行数据流
     * 
     * @param params 执行参数
     * @return 执行结果
     */
    Map<String, Object> executeDataFlow(Map<String, Object> params);

    /**
     * 数据转换
     * 
     * @param params 转换参数
     * @return 转换结果
     */
    Map<String, Object> transformData(Map<String, Object> params);

    /**
     * 数据清洗
     * 
     * @param params 清洗参数
     * @return 清洗结果
     */
    Map<String, Object> cleanseData(Map<String, Object> params);

    /**
     * 数据聚合
     * 
     * @param params 聚合参数
     * @return 聚合结果
     */
    Map<String, Object> aggregateData(Map<String, Object> params);

    /**
     * 流式处理
     * 
     * @param params 处理参数
     * @return 处理结果
     */
    Map<String, Object> processStream(Map<String, Object> params);

    /**
     * 数据流监控
     * 
     * @param params 监控参数
     * @return 监控信息
     */
    Map<String, Object> monitorDataFlow(Map<String, Object> params);
}

