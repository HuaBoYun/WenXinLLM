package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 执行记录查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "ExecutionRecordQueryParam", description = "执行记录查询参数")
public class ExecutionRecordQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据编号")
    private String sourceDocCode;

    @ApiModelProperty(value = "业务组织ID")
    private String bizOrgId;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

    @ApiModelProperty(value = "期间")
    private String period;

    @ApiModelProperty(value = "控制结果")
    private String controlResult;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

