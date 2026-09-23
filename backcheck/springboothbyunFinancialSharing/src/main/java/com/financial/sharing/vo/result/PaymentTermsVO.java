package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账期VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("账期VO")
public class PaymentTermsVO {

    @ApiModelProperty("账期ID")
    private String termsId;

    @ApiModelProperty("账期编码")
    private String termsCode;

    @ApiModelProperty("账期名称")
    private String termsName;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("付款天数")
    private Integer paymentDays;

    @ApiModelProperty("折扣天数")
    private Integer discountDays;

    @ApiModelProperty("折扣率")
    private BigDecimal discountRate;

    @ApiModelProperty("状态(0:停用,1:启用)")
    private Integer status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}

