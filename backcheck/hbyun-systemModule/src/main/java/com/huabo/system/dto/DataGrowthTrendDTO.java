package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 数据增长趋势DTO
 */
@Data
public class DataGrowthTrendDTO {

    /**
     * 年月
     */
    private String yearMonth;

    /**
     * 内控数据量
     */
    private BigDecimal nk;

    /**
     * 风险数据量
     */
    private BigDecimal fx;

    /**
     * 审计数据量
     */
    private BigDecimal ck;

    /**
     * 整改数据量
     */
    private BigDecimal zg;
}
