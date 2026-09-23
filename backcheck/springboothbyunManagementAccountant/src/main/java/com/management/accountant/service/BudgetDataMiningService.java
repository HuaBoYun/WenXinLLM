package com.management.accountant.service;

import java.util.Map;

/**
 * 预算数据挖掘Service接口
 * 
 * @description 预算数据挖掘业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetDataMiningService {

    /**
     * 模式识别
     * 
     * @param params 识别参数
     * @return 识别结果
     */
    Map<String, Object> patternRecognition(Map<String, Object> params);

    /**
     * 异常检测
     * 
     * @param params 检测参数
     * @return 检测结果
     */
    Map<String, Object> anomalyDetection(Map<String, Object> params);

    /**
     * 关联分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> associationAnalysis(Map<String, Object> params);

    /**
     * 聚类分析
     * 
     * @param params 聚类参数
     * @return 聚类结果
     */
    Map<String, Object> clustering(Map<String, Object> params);

    /**
     * 生成挖掘报告
     *
     * @param params 报告参数
     * @return 报告信息
     */
    Map<String, Object> generateMiningReport(Map<String, Object> params);

    /**
     * 获取挖掘任务列表
     *
     * @param params 查询参数
     * @return 任务列表
     */
    Map<String, Object> getMiningTaskList(Map<String, Object> params);

    /**
     * 获取挖掘统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getMiningStats(Map<String, Object> params);

    /**
     * 创建挖掘任务
     *
     * @param params 任务参数
     * @return 创建结果
     */
    Map<String, Object> createMiningTask(Map<String, Object> params);

    /**
     * 更新挖掘任务
     *
     * @param taskId 任务ID
     * @param params 任务参数
     */
    void updateMiningTask(String taskId, Map<String, Object> params);

    /**
     * 删除挖掘任务
     *
     * @param taskId 任务ID
     */
    void deleteMiningTask(String taskId);

    /**
     * 复制挖掘任务
     *
     * @param taskId 任务ID
     * @return 复制结果
     */
    Map<String, Object> copyMiningTask(String taskId);

    /**
     * 运行挖掘任务
     *
     * @param taskId 任务ID
     * @return 运行结果
     */
    Map<String, Object> runMiningTask(String taskId);

    /**
     * 停止挖掘任务
     *
     * @param taskId 任务ID
     */
    void stopMiningTask(String taskId);

    /**
     * 导出挖掘结果
     *
     * @param taskId 任务ID
     * @return 导出结果
     */
    Map<String, Object> exportMiningResult(String taskId);

    /**
     * 获取挖掘结果
     *
     * @param taskId 任务ID
     * @return 挖掘结果
     */
    Map<String, Object> getMiningResults(String taskId);

    /**
     * 获取挖掘日志
     *
     * @param taskId 任务ID
     * @return 挖掘日志
     */
    Map<String, Object> getMiningLogs(String taskId);
}

