package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预算释放请求
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "BudgetReleaseRequest", description = "预算释放请求")
public class BudgetReleaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "执行ID（用于释放已占用的预算）")
    private String executionId;

    @ApiModelProperty(value = "业务组织ID")
    private String bizOrgId;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

    @ApiModelProperty(value = "期间")
    private String period;

    @ApiModelProperty(value = "释放金额")
    private BigDecimal releaseAmount;

    @ApiModelProperty(value = "释放原因")
    private String releaseReason;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据ID")
    private String sourceDocId;

    @ApiModelProperty(value = "操作用户")
    private String operateUser;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

