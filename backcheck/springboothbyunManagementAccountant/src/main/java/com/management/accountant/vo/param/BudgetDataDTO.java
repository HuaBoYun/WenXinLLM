package com.management.accountant.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算数据DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算数据DTO")
public class BudgetDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据ID")
    private String dataId;

    @ApiModelProperty("预算ID")
    private String budgetId;

    @ApiModelProperty("预算名称")
    private String budgetName;

    @ApiModelProperty("维度值(JSON)")
    private String dimensionValues;

    @ApiModelProperty("指标ID")
    private String indicatorId;

    @ApiModelProperty("指标名称")
    private String indicatorName;

    @ApiModelProperty("指标编码")
    private String indicatorCode;

    @ApiModelProperty("期间ID")
    private String periodId;

    @ApiModelProperty("期间名称")
    private String periodName;

    @ApiModelProperty("期间类型")
    private String periodType;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("数量")
    private BigDecimal quantity;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty("数据来源")
    private String dataSource;

    @ApiModelProperty("版本ID")
    private String versionId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("审批状态")
    private String approvalStatus;

    @ApiModelProperty("是否锁定")
    private Integer isLocked;

    @ApiModelProperty("是否计算值")
    private Integer isCalculated;

    @ApiModelProperty("计算公式")
    private String formula;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("公司ID")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;
}

