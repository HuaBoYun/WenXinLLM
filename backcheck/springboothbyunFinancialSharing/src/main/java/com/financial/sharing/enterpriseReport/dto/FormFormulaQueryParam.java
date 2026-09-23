package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单公式查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "FormFormulaQueryParam", description = "表单公式查询参数")
public class FormFormulaQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公式ID")
    private String formulaId;

    @ApiModelProperty(value = "模板ID")
    private String templateId;

    @ApiModelProperty(value = "公式编码")
    private String formulaCode;

    @ApiModelProperty(value = "公式名称(模糊查询)")
    private String formulaName;

    @ApiModelProperty(value = "公式类型：CALCULATION/SUMMARY/VALIDATION/FETCH")
    private String formulaType;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

