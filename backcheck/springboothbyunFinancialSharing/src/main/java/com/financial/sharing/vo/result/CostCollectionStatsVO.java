package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 成本归集统计视图对象
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本归集统计视图对象")
public class CostCollectionStatsVO {

    @ApiModelProperty("直接成本总额")
    private BigDecimal directCost;

    @ApiModelProperty("间接成本总额")
    private BigDecimal indirectCost;

    @ApiModelProperty("制造费用总额")
    private BigDecimal manufacturingCost;

    @ApiModelProperty("总成本")
    private BigDecimal totalCost;

    @ApiModelProperty("本月归集数量")
    private Integer monthlyCollectionCount;

    @ApiModelProperty("本月归集金额")
    private BigDecimal monthlyCollectionAmount;

    @ApiModelProperty("待归集数量")
    private Integer pendingCollectionCount;

    @ApiModelProperty("待归集金额")
    private BigDecimal pendingCollectionAmount;

    @ApiModelProperty("已归集数量")
    private Integer completedCollectionCount;

    @ApiModelProperty("已归集金额")
    private BigDecimal completedCollectionAmount;

    @ApiModelProperty("已审核数量")
    private Integer auditedCollectionCount;

    @ApiModelProperty("已审核金额")
    private BigDecimal auditedCollectionAmount;
}