package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动设置表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_MOBILE_SETTING")
@ApiModel(value = "TblMobileSetting", description = "移动设置表")
public class TblMobileSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "SETTING_ID")
    @ApiModelProperty(value = "设置ID")
    private String settingId;

    @TableField("SETTING_CODE")
    @ApiModelProperty(value = "设置编码")
    private String settingCode;

    @TableField("SETTING_NAME")
    @ApiModelProperty(value = "设置名称")
    private String settingName;

    @TableField("SETTING_TYPE")
    @ApiModelProperty(value = "设置类型(UI-界面设置,FUNCTION-功能设置,PERMISSION-权限设置,NOTIFICATION-推送设置,SECURITY-安全设置)")
    private String settingType;

    @TableField("PLATFORM")
    @ApiModelProperty(value = "适用平台(IOS,ANDROID,WECHAT,ALIPAY,ALL)")
    private String platform;

    @TableField("SETTING_VALUE")
    @ApiModelProperty(value = "设置值")
    private String settingValue;

    @TableField("DEFAULT_VALUE")
    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @TableField("VERSION")
    @ApiModelProperty(value = "版本")
    private String version;

    @TableField("PRIORITY")
    @ApiModelProperty(value = "优先级(1-100)")
    private Integer priority;

    @TableField("IS_REQUIRED")
    @ApiModelProperty(value = "是否必需(0-否,1-是)")
    private Integer isRequired;

    @TableField("IS_USER_EDITABLE")
    @ApiModelProperty(value = "用户可修改(0-否,1-是)")
    private Integer isUserEditable;

    @TableField("EFFECTIVE_CONDITION")
    @ApiModelProperty(value = "生效条件")
    private String effectiveCondition;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("LAST_SYNC_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后同步时间")
    private LocalDateTime lastSyncTime;

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "设置描述")
    private String description;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
