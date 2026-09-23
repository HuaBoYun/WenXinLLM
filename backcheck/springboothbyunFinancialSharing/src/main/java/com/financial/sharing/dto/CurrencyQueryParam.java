package com.financial.sharing.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 币种查询参数
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class CurrencyQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 币种代码
     */
    private String currencyCode;

    /**
     * 币种名称
     */
    private String currencyName;

    /**
     * 状态
     */
    private String status;

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