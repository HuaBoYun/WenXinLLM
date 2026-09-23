package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报表对比结果DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReportCompareResult {

    /**
     * 项目编码
     */
    private String itemCode;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目层级
     */
    private Integer itemLevel;

    /**
     * 父项目编码
     */
    private String parentCode;

    /**
     * 期间1
     */
    private String period1;

    /**
     * 期间1金额
     */
    private BigDecimal amount1;

    /**
     * 期间2
     */
    private String period2;

    /**
     * 期间2金额
     */
    private BigDecimal amount2;

    /**
     * 差异金额
     */
    private BigDecimal diffAmount;

    /**
     * 差异率(%)
     */
    private BigDecimal diffRate;

    /**
     * 排序号
     */
    private Integer sortOrder;
}

