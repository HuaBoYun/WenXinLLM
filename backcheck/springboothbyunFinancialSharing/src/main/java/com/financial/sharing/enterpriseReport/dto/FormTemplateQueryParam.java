package com.financial.sharing.enterpriseReport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单模板查询参数
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@ApiModel(value = "FormTemplateQueryParam", description = "表单模板查询参数")
public class FormTemplateQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板ID")
    private String templateId;

    @ApiModelProperty(value = "表单组ID")
    private String groupId;

    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    @ApiModelProperty(value = "模板名称(模糊查询)")
    private String templateName;

    @ApiModelProperty(value = "模板类型：FIXED/FLOATING")
    private String templateType;

    @ApiModelProperty(value = "版本号")
    private String versionNo;

    @ApiModelProperty(value = "是否默认版本：Y/N")
    private String isDefault;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

