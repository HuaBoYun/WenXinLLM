package com.management.accountant.service;

import java.util.Map;

/**
 * 预算模拟Service接口
 * 
 * @description 预算模拟业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetSimulationService {

    /**
     * 创建模拟场景
     * 
     * @param params 场景参数
     * @return 场景信息
     */
    Map<String, Object> createSimulation(Map<String, Object> params);

    /**
     * 执行模拟
     * 
     * @param params 模拟参数
     * @return 模拟结果
     */
    Map<String, Object> executeSimulation(Map<String, Object> params);

    /**
     * 压力测试
     * 
     * @param params 测试参数
     * @return 测试结果
     */
    Map<String, Object> stressTest(Map<String, Object> params);

    /**
     * 蒙特卡洛模拟
     * 
     * @param params 模拟参数
     * @return 模拟结果
     */
    Map<String, Object> monteCarloSimulation(Map<String, Object> params);

    /**
     * 获取模拟报告
     *
     * @param params 报告参数
     * @return 报告信息
     */
    Map<String, Object> getSimulationReport(Map<String, Object> params);

    /**
     * 获取模拟列表
     *
     * @param params 查询参数
     * @return 模拟列表
     */
    Map<String, Object> getSimulationList(Map<String, Object> params);

    /**
     * 获取模拟统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getSimulationStats(Map<String, Object> params);

    /**
     * 更新模拟
     *
     * @param simulationId 模拟ID
     * @param params 模拟参数
     */
    void updateSimulation(String simulationId, Map<String, Object> params);

    /**
     * 删除模拟
     *
     * @param simulationId 模拟ID
     */
    void deleteSimulation(String simulationId);

    /**
     * 运行模拟
     *
     * @param simulationId 模拟ID
     * @return 运行结果
     */
    Map<String, Object> runSimulation(String simulationId);

    /**
     * 停止模拟
     *
     * @param simulationId 模拟ID
     */
    void stopSimulation(String simulationId);

    /**
     * 复制模拟
     *
     * @param simulationId 模拟ID
     * @return 复制结果
     */
    Map<String, Object> copySimulation(String simulationId);

    /**
     * 导出模拟
     *
     * @param simulationId 模拟ID
     * @return 导出结果
     */
    Map<String, Object> exportSimulation(String simulationId);

    /**
     * 获取模拟场景
     *
     * @param simulationId 模拟ID
     * @return 场景列表
     */
    Map<String, Object> getSimulationScenarios(String simulationId);

    /**
     * 获取模拟日志
     *
     * @param simulationId 模拟ID
     * @return 日志列表
     */
    Map<String, Object> getSimulationLogs(String simulationId);
}

