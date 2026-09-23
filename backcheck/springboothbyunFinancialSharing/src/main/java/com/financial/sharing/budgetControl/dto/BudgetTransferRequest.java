package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预算转移请求
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "BudgetTransferRequest", description = "预算转移请求")
public class BudgetTransferRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "源组织ID")
    private String fromOrgId;

    @ApiModelProperty(value = "源科目编码")
    private String fromSubjectCode;

    @ApiModelProperty(value = "源期间")
    private String fromPeriod;

    @ApiModelProperty(value = "目标组织ID")
    private String toOrgId;

    @ApiModelProperty(value = "目标科目编码")
    private String toSubjectCode;

    @ApiModelProperty(value = "目标期间")
    private String toPeriod;

    @ApiModelProperty(value = "转移金额")
    private BigDecimal transferAmount;

    @ApiModelProperty(value = "转移原因")
    private String transferReason;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据ID")
    private String sourceDocId;

    @ApiModelProperty(value = "操作用户")
    private String operateUser;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

