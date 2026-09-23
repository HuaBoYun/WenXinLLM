package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 预算控制请求参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "BudgetControlRequest", description = "预算控制请求参数")
public class BudgetControlRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "来源系统", required = true)
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据类型")
    private String sourceDocType;

    @ApiModelProperty(value = "来源单据ID", required = true)
    private String sourceDocId;

    @ApiModelProperty(value = "来源单据编号")
    private String sourceDocCode;

    @ApiModelProperty(value = "业务组织ID", required = true)
    private String bizOrgId;

    @ApiModelProperty(value = "科目编码", required = true)
    private String subjectCode;

    @ApiModelProperty(value = "期间", required = true)
    private String period;

    @ApiModelProperty(value = "维度值（key-value对）")
    private Map<String, String> dimensionValues;

    @ApiModelProperty(value = "申请金额", required = true)
    private BigDecimal applyAmount;

    @ApiModelProperty(value = "操作类型：OCCUPY(占用)/RELEASE(释放)/TRANSFER(转移)", required = true)
    private String operationType;

    @ApiModelProperty(value = "组织ID", required = true)
    private String orgId;

    @ApiModelProperty(value = "操作用户")
    private String operateUser;
}

