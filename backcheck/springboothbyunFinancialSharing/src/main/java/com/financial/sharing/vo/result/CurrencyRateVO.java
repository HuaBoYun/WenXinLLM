package com.financial.sharing.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 币种汇率返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class CurrencyRateVO {

    /**
     * 汇率ID
     */
    private Long rateId;

    /**
     * 币种编码
     */
    private String currencyCode;

    /**
     * 币种名称
     */
    private String currencyName;

    /**
     * 汇率类型(1即期2远期3固定)
     */
    private Integer rateType;

    /**
     * 汇率类型名称
     */
    private String rateTypeName;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 汇率日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rateDate;

    /**
     * 是否本位币(0否1是)
     */
    private Integer isBaseCurrency;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;
}
