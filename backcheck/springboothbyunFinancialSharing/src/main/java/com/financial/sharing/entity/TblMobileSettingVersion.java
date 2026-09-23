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
 * 配置版本表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_MOBILE_SETTING_VERSION")
@ApiModel(value = "TblMobileSettingVersion", description = "配置版本表")
public class TblMobileSettingVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "VERSION_ID")
    @ApiModelProperty(value = "版本ID")
    private String versionId;

    @TableField("SETTING_ID")
    @ApiModelProperty(value = "设置ID")
    private String settingId;

    @TableField("VERSION_NUMBER")
    @ApiModelProperty(value = "版本号")
    private String versionNumber;

    @TableField("VERSION_DATA")
    @ApiModelProperty(value = "版本数据(JSON格式)")
    private String versionData;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
