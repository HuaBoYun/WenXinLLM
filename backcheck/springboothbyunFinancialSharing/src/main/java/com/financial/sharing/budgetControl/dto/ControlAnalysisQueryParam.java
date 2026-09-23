package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 控制分析查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "ControlAnalysisQueryParam", description = "控制分析查询参数")
public class ControlAnalysisQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务组织ID")
    private String bizOrgId;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

    @ApiModelProperty(value = "期间")
    private String period;

    @ApiModelProperty(value = "开始期间")
    private String startPeriod;

    @ApiModelProperty(value = "结束期间")
    private String endPeriod;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "控制结果(PASS/BLOCK/WARNING)")
    private String controlResult;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

