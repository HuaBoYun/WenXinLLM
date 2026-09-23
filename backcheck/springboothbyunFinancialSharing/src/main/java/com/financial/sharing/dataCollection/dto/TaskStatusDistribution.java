package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务状态分布DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class TaskStatusDistribution implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 任务数量
     */
    private Integer taskCount;

    /**
     * 占比（百分比）
     */
    private Double percentage;
}

