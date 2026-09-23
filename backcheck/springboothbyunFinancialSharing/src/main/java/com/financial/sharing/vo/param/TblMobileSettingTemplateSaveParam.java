package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动设置模板保存参数
 *
 * @author Financial Sharing System
 * @since 2026-02-10
 */
@Data
@ApiModel(value = "TblMobileSettingTemplateSaveParam", description = "移动设置模板保存参数")
public class TblMobileSettingTemplateSaveParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板ID（新增时为null）")
    private String templateId;

    @ApiModelProperty(value = "关联的设置ID", required = true)
    private String settingId;

    @ApiModelProperty(value = "模板名称", required = true)
    private String templateName;

    @ApiModelProperty(value = "模板数据(JSON格式)", required = true)
    private String templateData;

    @ApiModelProperty(value = "适用平台", required = true)
    private String platform;

    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @ApiModelProperty(value = "模板描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;
}

