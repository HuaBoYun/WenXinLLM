package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 控制策略配置DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "ControlStrategyConfigDTO", description = "控制策略配置DTO")
public class ControlStrategyConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "控制类型")
    private String controlType;

    @ApiModelProperty(value = "控制级别")
    private String controlLevel;

    @ApiModelProperty(value = "控制维度配置")
    private List<DimensionConfig> dimensionConfigs;

    @ApiModelProperty(value = "控制期间")
    private String controlPeriod;

    @ApiModelProperty(value = "阈值类型")
    private String thresholdType;

    @ApiModelProperty(value = "阈值")
    private BigDecimal thresholdValue;

    @ApiModelProperty(value = "预警比例")
    private BigDecimal warningRatio;

    @ApiModelProperty(value = "控制动作")
    private String controlAction;

    @ApiModelProperty(value = "审批流程ID")
    private String approvalWorkflowId;

    @ApiModelProperty(value = "例外用户列表")
    private List<String> exceptionUsers;

    @ApiModelProperty(value = "是否启用")
    private String isEnabled;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    /**
     * 维度配置内部类
     */
    @Data
    @ApiModel(value = "DimensionConfig", description = "维度配置")
    public static class DimensionConfig implements Serializable {
        
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "维度编码")
        private String dimensionCode;

        @ApiModelProperty(value = "维度名称")
        private String dimensionName;

        @ApiModelProperty(value = "维度类型")
        private String dimensionType;

        @ApiModelProperty(value = "是否必填")
        private Boolean required;

        @ApiModelProperty(value = "控制方式: GROUP(组控制)/ACCUMULATE(累计控制)/CARRYFORWARD(年内顺延)")
        private String controlMethod;
    }
}

