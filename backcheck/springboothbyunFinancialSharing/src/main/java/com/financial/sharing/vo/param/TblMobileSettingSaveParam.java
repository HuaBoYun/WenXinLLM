package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 移动设置保存参数
 */
@Data
@ApiModel("移动设置保存参数")
public class TblMobileSettingSaveParam {

    @ApiModelProperty("设置ID")
    private String settingId;

    @ApiModelProperty(value = "设置编码", required = true)
    @NotBlank(message = "设置编码不能为空")
    private String settingCode;

    @ApiModelProperty(value = "设置名称", required = true)
    @NotBlank(message = "设置名称不能为空")
    private String settingName;

    @ApiModelProperty(value = "设置类型", required = true)
    @NotBlank(message = "设置类型不能为空")
    private String settingType;

    @ApiModelProperty(value = "适用平台", required = true)
    @NotBlank(message = "适用平台不能为空")
    private String platform;

    @ApiModelProperty("设置值")
    private String settingValue;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("版本")
    private String version;

    @ApiModelProperty("优先级(1-100)")
    private Integer priority;

    @ApiModelProperty("是否必需(0-否,1-是)")
    private Integer isRequired;

    @ApiModelProperty("用户可修改(0-否,1-是)")
    private Integer isUserEditable;

    @ApiModelProperty("生效条件")
    private String effectiveCondition;

    @ApiModelProperty("是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @ApiModelProperty("设置描述")
    private String description;

    @ApiModelProperty("备注")
    private String remark;
}

