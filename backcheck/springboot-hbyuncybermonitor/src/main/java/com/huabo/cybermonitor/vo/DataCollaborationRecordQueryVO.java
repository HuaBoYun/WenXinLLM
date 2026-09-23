package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据协同记录查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataCollaborationRecordQueryVO extends BaseVo {

    /**
     * 协同类型
     */
    private String collaborationType;

    /**
     * 源系统
     */
    private String sourceSystem;

    /**
     * 目标系统
     */
    private String targetSystem;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 协同状态
     */
    private String collaborationStatus;

    /**
     * 触发方式
     */
    private String triggerType;

    /**
     * 触发人
     */
    private String triggerUser;

    /**
     * 开始时间-起始
     */
    private String startTimeBegin;

    /**
     * 开始时间-结束
     */
    private String startTimeEnd;

    /**
     * 结束时间-起始
     */
    private String endTimeBegin;

    /**
     * 结束时间-结束
     */
    private String endTimeEnd;

    /**
     * 处理时长-最小值（秒）
     */
    private Integer minProcessingDuration;

    /**
     * 处理时长-最大值（秒）
     */
    private Integer maxProcessingDuration;

    /**
     * 数据量-最小值
     */
    private Long minDataVolume;

    /**
     * 数据量-最大值
     */
    private Long maxDataVolume;

    /**
     * 成功记录数-最小值
     */
    private Integer minSuccessCount;

    /**
     * 成功记录数-最大值
     */
    private Integer maxSuccessCount;

    /**
     * 失败记录数-最小值
     */
    private Integer minFailureCount;

    /**
     * 失败记录数-最大值
     */
    private Integer maxFailureCount;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;

}
