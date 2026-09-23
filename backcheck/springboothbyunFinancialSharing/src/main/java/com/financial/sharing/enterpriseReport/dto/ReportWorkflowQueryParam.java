package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报表工作流查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "ReportWorkflowQueryParam", description = "报表工作流查询参数")
public class ReportWorkflowQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程ID")
    private String workflowId;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "流程编码")
    private String workflowCode;

    @ApiModelProperty(value = "流程名称(模糊查询)")
    private String workflowName;

    @ApiModelProperty(value = "流程类型：PREPARE(编制)/APPROVE(审批)")
    private String workflowType;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

