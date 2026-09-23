package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报表任务查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "ReportTaskQueryParam", description = "报表任务查询参数")
public class ReportTaskQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "表单组ID")
    private String groupId;

    @ApiModelProperty(value = "任务编码")
    private String taskCode;

    @ApiModelProperty(value = "任务名称(模糊查询)")
    private String taskName;

    @ApiModelProperty(value = "周期类型：YEAR/HALF_YEAR/QUARTER/MONTH")
    private String periodType;

    @ApiModelProperty(value = "状态：DRAFT(草稿)/PUBLISHED(已发布)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

