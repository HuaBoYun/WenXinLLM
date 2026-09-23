package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预算控制响应结果
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "BudgetControlResponse", description = "预算控制响应结果")
public class BudgetControlResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "控制结果：PASS(通过)/BLOCK(阻止)/WARN(警告)/APPROVE(待审批)")
    private String controlResult;

    @ApiModelProperty(value = "控制消息")
    private String controlMessage;

    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "已用金额")
    private BigDecimal usedAmount;

    @ApiModelProperty(value = "可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "执行记录ID")
    private String recordId;

    @ApiModelProperty(value = "是否需要审批")
    private Boolean needApproval;

    @ApiModelProperty(value = "审批流程ID")
    private String approvalWorkflowId;
}

