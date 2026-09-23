package com.financial.sharing.enterpriseReport.dto;

import lombok.Data;

/**
 * 指标信息查询参数
 * 
 * @author system
 * @since 2026-01-30
 */
@Data
public class IndicatorQueryParam {

    /**
     * 指标编码
     */
    private String indicatorCode;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 指标类型
     */
    private String indicatorType;

    /**
     * 汇总属性
     */
    private String aggregateType;

    /**
     * 状态
     */
    private String status;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

