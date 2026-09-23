package com.financial.sharing.enterpriseReport.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报表数据查询结果DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReportDataQueryDTO {

    /**
     * 维度名称（如：组织名称、期间、指标名称等）
     */
    private String dimensionName;

    /**
     * 维度值
     */
    private String dimensionValue;

    /**
     * 数据值
     */
    private BigDecimal dataValue;

    /**
     * 数据数量
     */
    private Integer dataCount;

    /**
     * 数据总和
     */
    private BigDecimal dataSum;

    /**
     * 数据平均值
     */
    private BigDecimal dataAvg;

    /**
     * 数据最大值
     */
    private BigDecimal dataMax;

    /**
     * 数据最小值
     */
    private BigDecimal dataMin;

    /**
     * 期间
     */
    private String period;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 指标ID
     */
    private String indicatorId;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 对比值（用于期间对比）
     */
    private BigDecimal compareValue;

    /**
     * 增长率（用于期间对比）
     */
    private BigDecimal growthRate;

    /**
     * 增长额（用于期间对比）
     */
    private BigDecimal growthAmount;

    /**
     * 子项列表（用于多级汇总）
     */
    private List<ReportDataQueryDTO> children;
}

