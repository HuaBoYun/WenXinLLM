package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 币种汇率查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyRateQueryParam extends PageableParam {

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
     * 汇率日期开始
     */
    private LocalDate rateDateStart;

    /**
     * 汇率日期结束
     */
    private LocalDate rateDateEnd;

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
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}
