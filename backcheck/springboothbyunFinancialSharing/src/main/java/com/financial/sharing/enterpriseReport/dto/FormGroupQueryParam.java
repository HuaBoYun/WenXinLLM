package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单组查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "FormGroupQueryParam", description = "表单组查询参数")
public class FormGroupQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表单组ID")
    private String groupId;

    @ApiModelProperty(value = "目录ID")
    private String directoryId;

    @ApiModelProperty(value = "表单组编码")
    private String groupCode;

    @ApiModelProperty(value = "表单组名称(模糊查询)")
    private String groupName;

    @ApiModelProperty(value = "周期类型：YEAR/HALF_YEAR/QUARTER/MONTH")
    private String periodType;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

