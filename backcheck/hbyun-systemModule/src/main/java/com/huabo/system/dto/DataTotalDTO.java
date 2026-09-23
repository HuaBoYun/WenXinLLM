package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 数据总量DTO
 */
@Data
public class DataTotalDTO {

    /**
     * 风险数据总量
     */
    private BigDecimal fx;

    /**
     * 审计数据总量
     */
    private BigDecimal ck;

    /**
     * 内控数据总量
     */
    private BigDecimal nk;

    /**
     * 整改数据总量
     */
    private BigDecimal zg;
}
