package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 可视化分析查询参数
 */
@Data
@ApiModel("可视化分析查询参数")
public class VisualizationQueryParam {

    @ApiModelProperty("账套ID")
    private Integer bookId;

    @ApiModelProperty("租户ID")
    private Integer tenantId;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("维度")
    private String dimension;

    @ApiModelProperty("包含的图表列表")
    private List<String> includeCharts;
}
