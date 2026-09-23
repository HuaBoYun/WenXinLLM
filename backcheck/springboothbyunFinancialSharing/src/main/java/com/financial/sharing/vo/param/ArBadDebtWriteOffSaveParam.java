package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 坏账核销保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArBadDebtWriteOffSaveParam", description = "坏账核销保存参数")
public class ArBadDebtWriteOffSaveParam {

    @ApiModelProperty(value = "核销ID（更新时必填）")
    private String writeOffId;

    @ApiModelProperty(value = "应收单ID", required = true)
    @NotBlank(message = "应收单ID不能为空")
    private String receivableId;

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @ApiModelProperty(value = "核销金额", required = true)
    @NotNull(message = "核销金额不能为空")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "核销原因", required = true)
    @NotBlank(message = "核销原因不能为空")
    @Size(max = 1000, message = "核销原因长度不能超过1000个字符")
    private String writeOffReason;

    @ApiModelProperty(value = "核销日期")
    private LocalDate writeOffDate;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

