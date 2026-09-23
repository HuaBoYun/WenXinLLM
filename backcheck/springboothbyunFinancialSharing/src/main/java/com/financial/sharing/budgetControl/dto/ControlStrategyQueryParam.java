package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 控制策略查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "ControlStrategyQueryParam", description = "控制策略查询参数")
public class ControlStrategyQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "控制类型")
    private String controlType;

    @ApiModelProperty(value = "控制级别")
    private String controlLevel;

    @ApiModelProperty(value = "控制期间")
    private String controlPeriod;

    @ApiModelProperty(value = "是否启用")
    private String isEnabled;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

