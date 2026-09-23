package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 付款单保存参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("付款单保存参数")
public class PaymentOrderSaveParam {

    @ApiModelProperty("付款ID(新增时为空)")
    private String paymentId;

    @ApiModelProperty("付款单号")
    private String paymentNo;

    @NotBlank(message = "供应商ID不能为空")
    @ApiModelProperty(value = "供应商ID", required = true)
    private String supplierId;

    @NotNull(message = "付款金额不能为空")
    @ApiModelProperty(value = "付款金额", required = true)
    private BigDecimal paymentAmount;

    @ApiModelProperty("付款日期")
    private LocalDate paymentDate;

    @NotNull(message = "付款方式不能为空")
    @ApiModelProperty(value = "付款方式(1:现金,2:银行转账,3:支票,4:承兑汇票)", required = true)
    private Integer paymentMethod;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("收款账户")
    private String receiverAccount;

    @ApiModelProperty("收款开户行")
    private String receiverBank;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("备注(前端传递)")
    private String remark;
}

