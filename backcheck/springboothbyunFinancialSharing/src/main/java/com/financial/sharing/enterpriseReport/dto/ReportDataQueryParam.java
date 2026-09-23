package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报表数据查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "ReportDataQueryParam", description = "报表数据查询参数")
public class ReportDataQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据ID")
    private String dataId;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "模板ID")
    private String templateId;

    @ApiModelProperty(value = "指标ID")
    private String indicatorId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "期间")
    private String period;

    @ApiModelProperty(value = "数据来源：MANUAL(手工)/FETCH(取数)/CALCULATION(计算)")
    private String dataSource;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

