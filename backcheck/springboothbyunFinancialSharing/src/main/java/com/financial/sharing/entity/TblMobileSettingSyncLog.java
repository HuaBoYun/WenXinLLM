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
 * 配置同步日志表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_MOBILE_SETTING_SYNC_LOG")
@ApiModel(value = "TblMobileSettingSyncLog", description = "配置同步日志表")
public class TblMobileSettingSyncLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOG_ID")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @TableField("SETTING_ID")
    @ApiModelProperty(value = "设置ID")
    private String settingId;

    @TableField("SYNC_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    @TableField("SYNC_STATUS")
    @ApiModelProperty(value = "同步状态(SUCCESS-成功,FAILED-失败,PARTIAL-部分成功)")
    private String syncStatus;

    @TableField("PLATFORM")
    @ApiModelProperty(value = "同步平台")
    private String platform;

    @TableField("DEVICE_COUNT")
    @ApiModelProperty(value = "设备总数")
    private Integer deviceCount;

    @TableField("SUCCESS_COUNT")
    @ApiModelProperty(value = "成功数量")
    private Integer successCount;

    @TableField("FAIL_COUNT")
    @ApiModelProperty(value = "失败数量")
    private Integer failCount;

    @TableField("ERROR_MESSAGE")
    @ApiModelProperty(value = "错误消息")
    private String errorMessage;

    @TableField("SYNC_USER")
    @ApiModelProperty(value = "同步人")
    private String syncUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
