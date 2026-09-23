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
 * 收款单保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArReceiptSaveParam", description = "收款单保存参数")
public class ArReceiptSaveParam {

    @ApiModelProperty(value = "收款单ID（更新时必填）")
    private String receiptId;

    @ApiModelProperty(value = "收款单号")
    @Size(max = 50, message = "收款单号长度不能超过50个字符")
    private String receiptNo;

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @ApiModelProperty(value = "收款金额", required = true)
    @NotNull(message = "收款金额不能为空")
    private BigDecimal receiptAmount;

    @ApiModelProperty(value = "收款日期", required = true)
    @NotNull(message = "收款日期不能为空")
    private LocalDate receiptDate;

    @ApiModelProperty(value = "收款方式(1现金 2银行转账 3支票 4承兑汇票)", required = true)
    @NotNull(message = "收款方式不能为空")
    private Integer paymentMethod;

    @ApiModelProperty(value = "开户银行")
    @Size(max = 200, message = "开户银行长度不能超过200个字符")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    @Size(max = 50, message = "银行账号长度不能超过50个字符")
    private String bankAccount;

    @ApiModelProperty(value = "备注")
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remarks;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

