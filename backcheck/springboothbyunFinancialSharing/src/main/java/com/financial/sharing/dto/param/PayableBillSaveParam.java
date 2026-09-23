package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应付票据保存参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付票据保存参数")
public class PayableBillSaveParam {

    @ApiModelProperty("票据ID(新增时为空)")
    private String billId;

    @ApiModelProperty("票据号")
    private String billNo;

    @NotBlank(message = "票据类型不能为空")
    @ApiModelProperty(value = "票据类型(bank_acceptance/commercial_acceptance/check/promissory_note)", required = true)
    private String billType;

    @NotBlank(message = "供应商ID不能为空")
    @ApiModelProperty(value = "供应商ID", required = true)
    private String supplierId;

    @NotNull(message = "票据金额不能为空")
    @ApiModelProperty(value = "票据金额", required = true)
    private BigDecimal amount;

    @NotNull(message = "出票日期不能为空")
    @ApiModelProperty(value = "出票日期", required = true)
    private LocalDate issueDate;

    @NotNull(message = "到期日期不能为空")
    @ApiModelProperty(value = "到期日期", required = true)
    private LocalDate dueDate;

    @ApiModelProperty("承兑银行")
    private String bankName;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("出票人")
    private String drawer;

    @ApiModelProperty("收款人")
    private String payee;

    @ApiModelProperty("票据描述")
    private String description;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建人ID")
    private String createBy;

    @ApiModelProperty("状态")
    private String status;
}

