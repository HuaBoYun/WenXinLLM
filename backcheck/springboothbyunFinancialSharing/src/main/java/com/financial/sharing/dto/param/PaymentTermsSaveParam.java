package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 账期保存参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("账期保存参数")
public class PaymentTermsSaveParam {

    @ApiModelProperty("账期ID(新增时为空)")
    private String termsId;

    @NotBlank(message = "账期编码不能为空")
    @ApiModelProperty(value = "账期编码", required = true)
    private String termsCode;

    @NotBlank(message = "账期名称不能为空")
    @ApiModelProperty(value = "账期名称", required = true)
    private String termsName;

    @ApiModelProperty("供应商ID(为空表示通用账期)")
    private String supplierId;

    @NotNull(message = "付款天数不能为空")
    @ApiModelProperty(value = "付款天数", required = true)
    private Integer paymentDays;

    @ApiModelProperty("折扣天数")
    private Integer discountDays;

    @ApiModelProperty("折扣率(0-1)")
    private BigDecimal discountRate;

    @ApiModelProperty("状态(0:停用,1:启用)")
    private Integer status = 1;

    @ApiModelProperty("描述")
    private String description;
}

