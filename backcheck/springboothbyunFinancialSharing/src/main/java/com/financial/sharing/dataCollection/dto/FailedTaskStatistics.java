package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 失败任务统计DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class FailedTaskStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 失败次数
     */
    private Integer failedCount;

    /**
     * 最近失败时间
     */
    private String lastFailedTime;

    /**
     * 最近失败原因
     */
    private String lastErrorMessage;
}

