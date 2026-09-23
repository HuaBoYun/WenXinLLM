package com.management.accountant.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算调整DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算调整DTO")
public class BudgetAdjustmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("调整ID")
    private String adjustmentId;

    @ApiModelProperty("调整名称")
    private String adjustmentName;

    @ApiModelProperty("调整编码")
    private String adjustmentCode;

    @ApiModelProperty("调整类型")
    private String adjustmentType;

    @ApiModelProperty("预算ID")
    private String budgetId;

    @ApiModelProperty("预算名称")
    private String budgetName;

    @ApiModelProperty("调整对象类型")
    private String targetType;

    @ApiModelProperty("调整对象ID")
    private String targetId;

    @ApiModelProperty("调整对象名称")
    private String targetName;

    @ApiModelProperty("调整前金额")
    private BigDecimal beforeAmount;

    @ApiModelProperty("调整后金额")
    private BigDecimal afterAmount;

    @ApiModelProperty("调整金额")
    private BigDecimal adjustmentAmount;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("调整原因")
    private String reason;

    @ApiModelProperty("调整依据")
    private String basis;

    @ApiModelProperty("影响分析")
    private String impactAnalysis;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("审批状态")
    private String approvalStatus;

    @ApiModelProperty("审批流程ID")
    private String approvalFlowId;

    @ApiModelProperty("申请人ID")
    private String applicant;

    @ApiModelProperty("申请人姓名")
    private String applicantName;

    @ApiModelProperty("申请时间")
    private Date applyTime;

    @ApiModelProperty("审批人ID")
    private String approver;

    @ApiModelProperty("审批人姓名")
    private String approverName;

    @ApiModelProperty("审批时间")
    private Date approvalTime;

    @ApiModelProperty("审批意见")
    private String approvalComment;

    @ApiModelProperty("生效日期")
    private Date effectiveDate;

    @ApiModelProperty("是否已生效")
    private Integer isEffective;

    @ApiModelProperty("附件信息")
    private String attachments;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("公司ID")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;
}

