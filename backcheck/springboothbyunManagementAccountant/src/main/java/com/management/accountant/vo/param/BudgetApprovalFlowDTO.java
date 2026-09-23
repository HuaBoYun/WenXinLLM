package com.management.accountant.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 预算审批流程DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算审批流程DTO")
public class BudgetApprovalFlowDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("流程ID")
    private String flowId;

    @ApiModelProperty("流程名称")
    private String flowName;

    @ApiModelProperty("流程编码")
    private String flowCode;

    @ApiModelProperty("流程类型")
    private String flowType;

    @ApiModelProperty("预算ID")
    private String budgetId;

    @ApiModelProperty("预算名称")
    private String budgetName;

    @ApiModelProperty("当前节点ID")
    private String currentNode;

    @ApiModelProperty("当前节点名称")
    private String currentNodeName;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("发起人ID")
    private String initiator;

    @ApiModelProperty("发起人姓名")
    private String initiatorName;

    @ApiModelProperty("发起时间")
    private Date initiateTime;

    @ApiModelProperty("审批结果")
    private String approvalResult;

    @ApiModelProperty("审批意见")
    private String approvalComment;

    @ApiModelProperty("流程配置")
    private String flowConfig;

    @ApiModelProperty("优先级")
    private String priority;

    @ApiModelProperty("截止日期")
    private Date dueDate;

    @ApiModelProperty("完成时间")
    private Date completeTime;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("审批节点列表")
    private List<BudgetApprovalNodeDTO> nodes;

    @ApiModelProperty("公司ID")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;
}

