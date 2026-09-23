package com.financial.sharing.vo.result;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 币种视图对象
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class CurrencyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 币种ID
     */
    private Long currencyId;

    /**
     * 币种代码
     */
    private String currencyCode;

    /**
     * 币种名称
     */
    private String currencyName;

    /**
     * 币种符号
     */
    private String currencySymbol;

    /**
     * 状态（ACTIVE/INACTIVE）
     */
    private String status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 是否为本位币
     */
    private Boolean isBaseCurrency;

    /**
     * 小数位数
     */
    private Integer decimalPlaces;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}