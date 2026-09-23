package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 币种汇率保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class CurrencyRateSaveParam {

    /**
     * 汇率ID（更新时必填）
     */
    private Long rateId;

    /**
     * 币种编码
     */
    @NotBlank(message = "币种编码不能为空")
    @Size(max = 10, message = "币种编码长度不能超过10个字符")
    private String currencyCode;

    /**
     * 币种名称
     */
    @NotBlank(message = "币种名称不能为空")
    @Size(max = 50, message = "币种名称长度不能超过50个字符")
    private String currencyName;

    /**
     * 汇率类型(1即期2远期3固定)
     */
    @NotNull(message = "汇率类型不能为空")
    @Min(value = 1, message = "汇率类型值必须在1-3之间")
    @Max(value = 3, message = "汇率类型值必须在1-3之间")
    private Integer rateType;

    /**
     * 汇率
     */
    @NotNull(message = "汇率不能为空")
    @DecimalMin(value = "0.000001", message = "汇率必须大于0")
    @Digits(integer = 6, fraction = 6, message = "汇率格式不正确，最多6位整数和6位小数")
    private BigDecimal exchangeRate;

    /**
     * 汇率日期
     */
    @NotNull(message = "汇率日期不能为空")
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
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;
}
