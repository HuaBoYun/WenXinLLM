package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.dto.*;

import java.util.List;

/**
 * 归集监控服务接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface CollectionMonitorService {

    /**
     * 获取整体监控统计
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    CollectionMonitorStatistics getOverallStatistics(Long orgId);

    /**
     * 获取任务执行趋势（最近N天）
     *
     * @param orgId 组织ID
     * @param days 天数
     * @return 趋势数据列表
     */
    List<TaskExecutionTrend> getTaskExecutionTrend(Long orgId, Integer days);

    /**
     * 获取任务状态分布
     *
     * @param orgId 组织ID
     * @return 状态分布列表
     */
    List<TaskStatusDistribution> getTaskStatusDistribution(Long orgId);

    /**
     * 获取失败任务TOP10
     *
     * @param orgId 组织ID
     * @param days 统计天数
     * @return 失败任务列表
     */
    List<FailedTaskStatistics> getTopFailedTasks(Long orgId, Integer days);

    /**
     * 获取执行时长统计（按任务）
     *
     * @param orgId 组织ID
     * @return 执行时长统计列表
     */
    List<TaskExecutionTrend> getExecutionDurationStatistics(Long orgId);
}

