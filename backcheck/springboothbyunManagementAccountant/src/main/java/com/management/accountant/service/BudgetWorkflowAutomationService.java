package com.management.accountant.service;

import java.util.Map;

/**
 * 预算自动化工作流Service接口
 * 
 * @description 预算自动化工作流业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetWorkflowAutomationService {

    /**
     * 定义工作流
     * 
     * @param params 工作流参数
     * @return 工作流信息
     */
    Map<String, Object> defineWorkflow(Map<String, Object> params);

    /**
     * 执行工作流
     * 
     * @param params 执行参数
     * @return 执行结果
     */
    Map<String, Object> executeWorkflow(Map<String, Object> params);

    /**
     * 任务调度
     * 
     * @param params 调度参数
     * @return 调度信息
     */
    Map<String, Object> scheduleTask(Map<String, Object> params);

    /**
     * 流程监控
     * 
     * @param params 监控参数
     * @return 监控信息
     */
    Map<String, Object> monitorWorkflow(Map<String, Object> params);

    /**
     * 流程优化
     * 
     * @param params 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeWorkflow(Map<String, Object> params);

    /**
     * 停止工作流
     *
     * @param params 停止参数
     * @return 停止结果
     */
    Map<String, Object> stopWorkflow(Map<String, Object> params);

    /**
     * 获取工作流列表
     *
     * @param params 查询参数
     * @return 工作流列表
     */
    Map<String, Object> getWorkflowList(Map<String, Object> params);

    /**
     * 获取工作流统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getWorkflowStats(Map<String, Object> params);

    /**
     * 运行工作流
     *
     * @param workflowId 工作流ID
     * @return 运行结果
     */
    Map<String, Object> runWorkflow(String workflowId);

    /**
     * 停止工作流（按ID）
     *
     * @param workflowId 工作流ID
     */
    void stopWorkflowById(String workflowId);

    /**
     * 复制工作流
     *
     * @param workflowId 工作流ID
     * @return 复制结果
     */
    Map<String, Object> copyWorkflow(String workflowId);

    /**
     * 导出工作流
     *
     * @param workflowId 工作流ID
     * @return 导出结果
     */
    Map<String, Object> exportWorkflow(String workflowId);

    /**
     * 删除工作流
     *
     * @param workflowId 工作流ID
     */
    void deleteWorkflow(String workflowId);
}

