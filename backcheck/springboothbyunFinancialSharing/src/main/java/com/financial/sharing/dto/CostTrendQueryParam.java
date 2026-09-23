package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 成本趋势查询参数
 */
@Data
@ApiModel("成本趋势查询参数")
public class CostTrendQueryParam {

    @ApiModelProperty("账套ID")
    private Integer bookId;

    @ApiModelProperty("租户ID")
    private Integer tenantId;

    @ApiModelProperty("开始日期(YYYY-MM)")
    private String startDate;

    @ApiModelProperty("结束日期(YYYY-MM)")
    private String endDate;

    @ApiModelProperty("维度: month/quarter/year")
    private String dimension;

    @ApiModelProperty("成本中心ID列表")
    private List<String> costCenterIds;

    @ApiModelProperty("是否对比去年同期")
    private Boolean compareFlag;
}
