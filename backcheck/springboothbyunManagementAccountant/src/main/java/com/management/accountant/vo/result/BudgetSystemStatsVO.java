package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算体系统计数据VO
 * 
 * @description 预算体系管理首页统计数据
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算体系统计数据")
public class BudgetSystemStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组织单元总数")
    private Integer organizationCount = 0;

    @ApiModelProperty("活跃组织数")
    private Integer activeOrgCount = 0;

    @ApiModelProperty("组织增长率(%)")
    private Double orgGrowth = 0.0;

    @ApiModelProperty("预算维度总数")
    private Integer dimensionCount = 0;

    @ApiModelProperty("启用维度数")
    private Integer activeDimCount = 0;

    @ApiModelProperty("维度使用率(%)")
    private Double dimUsageRate = 0.0;

    @ApiModelProperty("预算指标总数")
    private Integer indicatorCount = 0;

    @ApiModelProperty("KPI指标数")
    private Integer kpiCount = 0;

    @ApiModelProperty("指标增长率(%)")
    private Double indicatorGrowth = 0.0;

    @ApiModelProperty("预算模型总数")
    private Integer modelCount = 0;

    @ApiModelProperty("运行中模型数")
    private Integer activeModelCount = 0;

    @ApiModelProperty("模型效率(%)")
    private Double modelEfficiency = 0.0;
}

