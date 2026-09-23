package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单目录查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "FormDirectoryQueryParam", description = "表单目录查询参数")
public class FormDirectoryQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "目录ID")
    private String directoryId;

    @ApiModelProperty(value = "目录编码")
    private String directoryCode;

    @ApiModelProperty(value = "目录名称(模糊查询)")
    private String directoryName;

    @ApiModelProperty(value = "父目录ID")
    private String parentDirectoryId;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

