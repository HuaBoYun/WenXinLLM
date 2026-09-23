package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 控制规则查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "ControlRuleQueryParam", description = "控制规则查询参数")
public class ControlRuleQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "控制类型：RIGID(刚性)/FLEXIBLE(弹性)/WARNING(预警)")
    private String controlType;

    @ApiModelProperty(value = "控制级别：SUBJECT(科目)/ORG(组织)/PROJECT(项目)/CUSTOM(自定义)")
    private String controlLevel;

    @ApiModelProperty(value = "是否启用：Y/N")
    private String isEnabled;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

