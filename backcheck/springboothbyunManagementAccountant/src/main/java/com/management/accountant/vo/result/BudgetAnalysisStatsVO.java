package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算分析统计数据VO
 * 
 * @description 预算分析首页统计数据
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算分析统计数据")
public class BudgetAnalysisStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分析报告总数")
    private Integer totalReports = 0;

    @ApiModelProperty("本月新增报告数")
    private Integer monthlyReports = 0;

    @ApiModelProperty("差异分析数")
    private Integer varianceAnalysis = 0;

    @ApiModelProperty("趋势分析数")
    private Integer trendAnalysis = 0;

    @ApiModelProperty("对比分析数")
    private Integer comparisonAnalysis = 0;

    @ApiModelProperty("预测分析数")
    private Integer forecastAnalysis = 0;

    @ApiModelProperty("平均预算执行率(%)")
    private Double avgExecutionRate = 0.0;

    @ApiModelProperty("平均预算偏差率(%)")
    private Double avgVarianceRate = 0.0;
}

