package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 余额调整参数
 *
 * @author system
 * @since 2024-12-07
 */
@Data
@ApiModel("余额调整参数")
public class BalanceAdjustmentParam {

    @ApiModelProperty(value = "账簿ID", required = true)
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    @ApiModelProperty(value = "科目编码", required = true)
    @NotBlank(message = "科目编码不能为空")
    private String accountCode;

    @ApiModelProperty(value = "科目名称", required = true)
    @NotBlank(message = "科目名称不能为空")
    private String accountName;

    @ApiModelProperty(value = "会计期间", required = true)
    @NotBlank(message = "会计期间不能为空")
    private String accountingPeriod;

    @ApiModelProperty(value = "币种代码", required = true)
    @NotBlank(message = "币种代码不能为空")
    private String currencyCode;

    @ApiModelProperty(value = "调整类型：DEBIT-借方调整, CREDIT-贷方调整", required = true)
    @NotBlank(message = "调整类型不能为空")
    private String adjustmentType;

    @ApiModelProperty(value = "调整金额", required = true)
    @NotNull(message = "调整金额不能为空")
    private BigDecimal adjustmentAmount;

    @ApiModelProperty("原期初借方余额")
    private BigDecimal originalBeginningDebit;

    @ApiModelProperty("原期初贷方余额")
    private BigDecimal originalBeginningCredit;

    @ApiModelProperty("原本期借方发生")
    private BigDecimal originalPeriodDebit;

    @ApiModelProperty("原本期贷方发生")
    private BigDecimal originalPeriodCredit;

    @ApiModelProperty("新期初借方余额")
    private BigDecimal newBeginningDebit;

    @ApiModelProperty("新期初贷方余额")
    private BigDecimal newBeginningCredit;

    @ApiModelProperty("新本期借方发生")
    private BigDecimal newPeriodDebit;

    @ApiModelProperty("新本期贷方发生")
    private BigDecimal newPeriodCredit;

    @ApiModelProperty(value = "调整原因", required = true)
    @NotBlank(message = "调整原因不能为空")
    private String adjustmentReason;

    @ApiModelProperty("附件路径")
    private String attachmentPath;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否需要审批")
    private Boolean needApproval = true;

    @ApiModelProperty("审批人ID列表")
    private java.util.List<Long> approverIds;
}