package com.management.accountant.service;

import java.util.Map;

/**
 * 预算批量计算Service接口
 * 
 * @description 预算批量计算业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetBatchCalculationService {

    /**
     * 批量计算预算
     * 
     * @param params 计算参数
     * @return 计算结果
     */
    Map<String, Object> executeBatchCalculation(Map<String, Object> params);

    /**
     * 批量汇总预算
     * 
     * @param params 汇总参数
     * @return 汇总结果
     */
    Map<String, Object> batchSummarize(Map<String, Object> params);

    /**
     * 批量重算预算
     * 
     * @param params 重算参数
     * @return 重算结果
     */
    Map<String, Object> batchRecalculate(Map<String, Object> params);

    /**
     * 获取计算进度
     * 
     * @param taskId 任务ID
     * @return 进度信息
     */
    Map<String, Object> getCalculationProgress(String taskId);

    /**
     * 取消批量计算
     *
     * @param taskId 任务ID
     */
    void cancelCalculation(String taskId);

    /**
     * 获取批量计算任务列表
     *
     * @param params 查询参数
     * @return 任务列表
     */
    Map<String, Object> getTaskList(Map<String, Object> params);

    /**
     * 获取批量计算统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getStats(Map<String, Object> params);

    /**
     * 创建批量计算任务
     *
     * @param params 任务参数
     * @return 创建结果
     */
    Map<String, Object> createTask(Map<String, Object> params);

    /**
     * 更新批量计算任务
     *
     * @param taskId 任务ID
     * @param params 任务参数
     */
    void updateTask(String taskId, Map<String, Object> params);

    /**
     * 删除批量计算任务
     *
     * @param taskId 任务ID
     */
    void deleteTask(String taskId);

    /**
     * 执行批量计算任务
     *
     * @param taskId 任务ID
     * @return 执行结果
     */
    Map<String, Object> runTask(String taskId);

    /**
     * 停止批量计算任务
     *
     * @param taskId 任务ID
     */
    void stopTask(String taskId);

    /**
     * 重试批量计算任务
     *
     * @param taskId 任务ID
     * @return 重试结果
     */
    Map<String, Object> retryTask(String taskId);

    /**
     * 复制批量计算任务
     *
     * @param taskId 任务ID
     * @return 复制结果
     */
    Map<String, Object> copyTask(String taskId);

    /**
     * 导出批量计算结果
     *
     * @param taskId 任务ID
     * @return 导出结果
     */
    Map<String, Object> exportTask(String taskId);

    /**
     * 获取批量计算结果
     *
     * @param taskId 任务ID
     * @return 计算结果
     */
    Map<String, Object> getResults(String taskId);

    /**
     * 获取批量计算日志
     *
     * @param taskId 任务ID
     * @return 日志列表
     */
    Map<String, Object> getLogs(String taskId);
}

