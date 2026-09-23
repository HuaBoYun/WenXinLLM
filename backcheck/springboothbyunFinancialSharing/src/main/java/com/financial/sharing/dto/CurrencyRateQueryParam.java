package com.financial.sharing.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 汇率查询参数
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class CurrencyRateQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 源币种代码
     */
    private String fromCurrency;

    /**
     * 目标币种代码
     */
    private String toCurrency;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;
}