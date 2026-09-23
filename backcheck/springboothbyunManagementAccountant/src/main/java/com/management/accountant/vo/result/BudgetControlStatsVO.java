package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算控制统计数据VO
 * 
 * @description 预算控制首页统计数据
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算控制统计数据")
public class BudgetControlStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("控制规则总数")
    private Integer totalRules = 0;

    @ApiModelProperty("启用规则数")
    private Integer activeRules = 0;

    @ApiModelProperty("预警记录总数")
    private Integer totalWarnings = 0;

    @ApiModelProperty("未处理预警数")
    private Integer pendingWarnings = 0;

    @ApiModelProperty("预算执行总数")
    private Integer totalExecutions = 0;

    @ApiModelProperty("超预算执行数")
    private Integer overBudgetExecutions = 0;

    @ApiModelProperty("冻结预算数")
    private Integer frozenBudgets = 0;

    @ApiModelProperty("预算限额数")
    private Integer budgetLimits = 0;

    @ApiModelProperty("控制有效率(%)")
    private Double controlEffectiveness = 0.0;

    @ApiModelProperty("预警响应率(%)")
    private Double warningResponseRate = 0.0;
}

