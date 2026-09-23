package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 归集监控统计DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class CollectionMonitorStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务总数
     */
    private Integer totalTasks;

    /**
     * 启用任务数
     */
    private Integer enabledTasks;

    /**
     * 定时任务数
     */
    private Integer scheduledTasks;

    /**
     * 今日执行次数
     */
    private Integer todayExecutions;

    /**
     * 今日成功次数
     */
    private Integer todaySuccessCount;

    /**
     * 今日失败次数
     */
    private Integer todayFailedCount;

    /**
     * 今日成功率（百分比）
     */
    private Double todaySuccessRate;

    /**
     * 本周执行次数
     */
    private Integer weekExecutions;

    /**
     * 本月执行次数
     */
    private Integer monthExecutions;

    /**
     * 累计归集记录数
     */
    private Long totalRecords;

    /**
     * 累计成功记录数
     */
    private Long totalSuccessRecords;

    /**
     * 累计失败记录数
     */
    private Long totalFailedRecords;

    /**
     * 平均执行时长（毫秒）
     */
    private Long avgExecutionDuration;
}

