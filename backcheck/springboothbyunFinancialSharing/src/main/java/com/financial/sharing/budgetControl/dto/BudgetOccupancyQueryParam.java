package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算占用查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "BudgetOccupancyQueryParam", description = "预算占用查询参数")
public class BudgetOccupancyQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模型ID")
    private String modelId;

    @ApiModelProperty(value = "业务组织ID")
    private String bizOrgId;

    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

    @ApiModelProperty(value = "科目名称")
    private String subjectName;

    @ApiModelProperty(value = "期间")
    private String period;

    @ApiModelProperty(value = "预算年度")
    private String budgetYear;

    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "状态：NORMAL(正常)/WARNING(预警)/EXCEEDED(超支)")
    private String status;

    @ApiModelProperty(value = "预警级别：LOW(低)/MEDIUM(中)/HIGH(高)")
    private String warningLevel;

    @ApiModelProperty(value = "开始期间")
    private String startPeriod;

    @ApiModelProperty(value = "结束期间")
    private String endPeriod;

    @ApiModelProperty(value = "占用率最小值(%)")
    private String minOccupancyRate;

    @ApiModelProperty(value = "占用率最大值(%)")
    private String maxOccupancyRate;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNumber = 1;

    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize = 10;
}

