package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 票据兑付参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("票据兑付参数")
public class BillPaymentParam {

    @NotBlank(message = "票据ID不能为空")
    @ApiModelProperty(value = "票据ID", required = true)
    private String billId;

    @ApiModelProperty("兑付日期")
    private LocalDate paymentDate;

    @NotNull(message = "兑付金额不能为空")
    @ApiModelProperty(value = "兑付金额", required = true)
    private BigDecimal paymentAmount;

    @ApiModelProperty("兑付银行")
    private String paymentBank;

    @ApiModelProperty("兑付账号")
    private String paymentAccount;

    @ApiModelProperty("实际到账金额")
    private BigDecimal actualAmount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("兑付方式(1:现金,2:转账)")
    private Integer paymentType = 2;

    @ApiModelProperty("兑付说明")
    private String description;

    @ApiModelProperty("备注")
    private String remarks;
}

