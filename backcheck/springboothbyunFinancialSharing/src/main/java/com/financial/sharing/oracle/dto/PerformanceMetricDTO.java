package com.financial.sharing.oracle.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 性能指标DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class PerformanceMetricDTO {

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 指标值
     */
    private BigDecimal value;

    /**
     * 单位
     */
    private String unit;

    /**
     * 时间戳
     */
    private Date timestamp;

    /**
     * 指标类型
     */
    private String metricType;

    /**
     * 标签
     */
    private String tags;

    /**
     * 基准值
     */
    private BigDecimal baseline;

    /**
     * 阈值
     */
    private BigDecimal threshold;

    /**
     * 状态(NORMAL/WARNING/CRITICAL)
     */
    private String status;

    /**
     * 描述
     */
    private String description;
}