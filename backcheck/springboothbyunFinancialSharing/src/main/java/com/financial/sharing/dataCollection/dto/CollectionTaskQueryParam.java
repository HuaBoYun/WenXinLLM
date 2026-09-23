package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 归集任务查询参数DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class CollectionTaskQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型：MANUAL(手动)/SCHEDULED(定时)/REALTIME(实时)
     */
    private String taskType;

    /**
     * 数据源ID
     */
    private String sourceId;

    /**
     * 执行状态：PENDING(待执行)/RUNNING(运行中)/SUCCESS(成功)/FAILED(失败)
     */
    private String executeStatus;

    /**
     * 是否启用：Y/N
     */
    private String isEnabled;

    /**
     * 调度类型：ONCE(一次)/DAILY(每日)/WEEKLY(每周)/MONTHLY(每月)/CRON(表达式)
     */
    private String scheduleType;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 每页记录数
     */
    private Integer pageSize;
}

