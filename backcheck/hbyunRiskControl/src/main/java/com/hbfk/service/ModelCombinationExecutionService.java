package com.hbfk.service;

import java.util.Map;

/**
 * 模型组合执行结果服务接口
 * 
 * @author AI Assistant
 * @date 2025-01-29
 */
public interface ModelCombinationExecutionService {

    /**
     * 清理指定组合的执行结果数据
     *
     * @param combinationId 组合ID
     * @return 删除的记录数
     */
    int clearExecutionResultsByCombinationId(String combinationId);

    /**
     * 清理指定配置的执行结果数据
     *
     * @param configId 配置ID
     * @return 删除的记录数
     */
    int clearExecutionResultsByConfigId(String configId);

    /**
     * 清理所有执行结果数据
     *
     * @return 删除的记录数
     */
    int clearAllExecutionResults();

    /**
     * 获取执行结果统计信息
     *
     * @param combinationId 组合ID，为null时统计所有
     * @return 统计信息
     */
    Map<String, Object> getExecutionResultStatistics(String combinationId);

    /**
     * 删除单个执行记录
     *
     * @param executionId 执行ID
     * @return 删除的记录数
     */
    int deleteExecutionRecord(String executionId);
}
