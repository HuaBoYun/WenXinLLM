package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 归集日志查询参数DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class CollectionLogQueryParam implements Serializable {

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
     * 执行类型：MANUAL(手动)/SCHEDULED(定时)
     */
    private String executeType;

    /**
     * 执行状态：SUCCESS(成功)/FAILED(失败)/STOPPED(已停止)
     */
    private String executeStatus;

    /**
     * 开始时间-起始
     */
    private Date startTimeBegin;

    /**
     * 开始时间-结束
     */
    private Date startTimeEnd;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

