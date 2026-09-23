package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应付票据VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付票据VO")
public class PayableBillVO {

    @ApiModelProperty("票据ID")
    private String billId;

    @ApiModelProperty("票据号")
    private String billNo;

    @ApiModelProperty("票据类型")
    private String billType;

    @ApiModelProperty("票据类型名称")
    private String billTypeName;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("票据金额")
    private BigDecimal amount;

    @ApiModelProperty("出票日期")
    private LocalDate issueDate;

    @ApiModelProperty("到期日期")
    private LocalDate dueDate;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态名称")
    private String statusName;

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

    @ApiModelProperty("审核状态")
    private Integer auditStatus;

    @ApiModelProperty("审核状态名称")
    private String auditStatusName;

    @ApiModelProperty("剩余天数")
    private Integer remainingDays;

    @ApiModelProperty("是否到期")
    private Boolean isDue;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}

