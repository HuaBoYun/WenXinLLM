package com.financial.sharing.oracle.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 性能告警DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class PerformanceAlertDTO {

    /**
     * 告警ID
     */
    private String alertId;

    /**
     * 告警名称
     */
    @NotBlank(message = "告警名称不能为空")
    private String alertName;

    /**
     * 指标名称
     */
    @NotBlank(message = "指标名称不能为空")
    private String metricName;

    /**
     * 告警条件
     */
    @NotBlank(message = "告警条件不能为空")
    private String condition;

    /**
     * 阈值
     */
    @NotNull(message = "阈值不能为空")
    private Double threshold;

    /**
     * 比较操作符(>/</=/>=/<=)
     */
    @NotBlank(message = "比较操作符不能为空")
    private String operator;

    /**
     * 持续时间(秒)
     */
    private Integer duration;

    /**
     * 告警级别(INFO/WARNING/ERROR/CRITICAL)
     */
    @NotBlank(message = "告警级别不能为空")
    private String severity;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 通知方式
     */
    private String notificationMethods;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 最后触发时间
     */
    private Date lastTriggerTime;

    /**
     * 触发次数
     */
    private Long triggerCount;
}