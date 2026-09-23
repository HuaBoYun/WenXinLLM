package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收款确认参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArReceiptConfirmParam", description = "收款确认参数")
public class ArReceiptConfirmParam {

    @ApiModelProperty(value = "收款单ID", required = true)
    @NotBlank(message = "收款单ID不能为空")
    private String receiptId;

    @ApiModelProperty(value = "确认收款金额", required = true)
    @NotNull(message = "确认收款金额不能为空")
    private BigDecimal confirmAmount;

    @ApiModelProperty(value = "确认收款日期")
    private LocalDate confirmDate;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}

