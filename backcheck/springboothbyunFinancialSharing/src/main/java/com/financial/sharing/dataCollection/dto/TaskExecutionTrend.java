package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务执行趋势DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class TaskExecutionTrend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期（格式：yyyy-MM-dd）
     */
    private String date;

    /**
     * 执行次数
     */
    private Integer executionCount;

    /**
     * 成功次数
     */
    private Integer successCount;

    /**
     * 失败次数
     */
    private Integer failedCount;

    /**
     * 成功率（百分比）
     */
    private Double successRate;
}

