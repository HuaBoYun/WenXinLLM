package com.management.accountant.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算审批节点DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算审批节点DTO")
public class BudgetApprovalNodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("节点ID")
    private String nodeId;

    @ApiModelProperty("流程ID")
    private String flowId;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("节点编码")
    private String nodeCode;

    @ApiModelProperty("节点类型")
    private String nodeType;

    @ApiModelProperty("审批人列表(JSON)")
    private String approvers;

    @ApiModelProperty("审批方式")
    private String approvalMode;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("审批结果")
    private String approvalResult;

    @ApiModelProperty("审批意见")
    private String approvalComment;

    @ApiModelProperty("审批时间")
    private Date approvalTime;

    @ApiModelProperty("排序序号")
    private Integer orderNum;

    @ApiModelProperty("节点配置")
    private String nodeConfig;

    @ApiModelProperty("超时小时数")
    private Integer timeoutHours;

    @ApiModelProperty("描述")
    private String description;
}

