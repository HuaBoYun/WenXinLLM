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
 * 配置模板表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_MOBILE_SETTING_TEMPLATE")
@ApiModel(value = "TblMobileSettingTemplate", description = "配置模板表")
public class TblMobileSettingTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "TEMPLATE_ID")
    @ApiModelProperty(value = "模板ID")
    private String templateId;

    @TableField("TEMPLATE_NAME")
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @TableField("TEMPLATE_DATA")
    @ApiModelProperty(value = "模板数据(JSON格式)")
    private String templateData;

    @TableField("PLATFORM")
    @ApiModelProperty(value = "适用平台")
    private String platform;

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

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "模板描述")
    private String description;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
