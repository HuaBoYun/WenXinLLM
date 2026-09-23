package com.management.accountant.service;

import java.util.Map;

/**
 * 预算优化Service接口
 * 
 * @description 预算优化业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetOptimizationService {

    /**
     * 线性规划优化
     * 
     * @param params 优化参数
     * @return 优化结果
     */
    Map<String, Object> linearProgramming(Map<String, Object> params);

    /**
     * 目标规划
     * 
     * @param params 规划参数
     * @return 规划结果
     */
    Map<String, Object> goalProgramming(Map<String, Object> params);

    /**
     * 多目标优化
     * 
     * @param params 优化参数
     * @return 优化结果
     */
    Map<String, Object> multiObjectiveOptimization(Map<String, Object> params);

    /**
     * 约束优化
     * 
     * @param params 优化参数
     * @return 优化结果
     */
    Map<String, Object> constrainedOptimization(Map<String, Object> params);

    /**
     * 生成优化报告
     *
     * @param params 报告参数
     * @return 报告信息
     */
    Map<String, Object> generateOptimizationReport(Map<String, Object> params);

    /**
     * 获取优化任务列表
     *
     * @param params 查询参数
     * @return 任务列表
     */
    Map<String, Object> getOptimizationList(Map<String, Object> params);

    /**
     * 获取优化统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getOptimizationStats(Map<String, Object> params);

    /**
     * 创建优化任务
     *
     * @param params 任务参数
     * @return 创建结果
     */
    Map<String, Object> createOptimization(Map<String, Object> params);

    /**
     * 更新优化任务
     *
     * @param taskId 任务ID
     * @param params 任务参数
     */
    void updateOptimization(String taskId, Map<String, Object> params);

    /**
     * 删除优化任务
     *
     * @param taskId 任务ID
     */
    void deleteOptimization(String taskId);

    /**
     * 执行优化任务
     *
     * @param taskId 任务ID
     * @return 执行结果
     */
    Map<String, Object> runOptimization(String taskId);

    /**
     * 停止优化任务
     *
     * @param taskId 任务ID
     */
    void stopOptimization(String taskId);

    /**
     * 应用优化结果
     *
     * @param taskId 任务ID
     * @return 应用结果
     */
    Map<String, Object> applyOptimization(String taskId);

    /**
     * 导出优化结果
     *
     * @param taskId 任务ID
     * @return 导出结果
     */
    Map<String, Object> exportOptimization(String taskId);

    /**
     * 复制优化任务
     *
     * @param taskId 任务ID
     * @return 复制结果
     */
    Map<String, Object> copyOptimization(String taskId);

    /**
     * 获取优化结果
     *
     * @param taskId 任务ID
     * @return 优化结果
     */
    Map<String, Object> getOptimizationResults(String taskId);

    /**
     * 获取执行日志
     *
     * @param taskId 任务ID
     * @return 执行日志
     */
    Map<String, Object> getOptimizationLogs(String taskId);
}

