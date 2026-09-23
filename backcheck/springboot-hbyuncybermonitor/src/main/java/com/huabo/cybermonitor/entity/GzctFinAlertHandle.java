package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 财务预警处置记录实体
 * 对应表：GZCT_FIN_ALERT_HANDLE
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_ALERT_HANDLE")
public class GzctFinAlertHandle extends Model<GzctFinAlertHandle> {

    @TableId(value = "HANDLE_ID", type = IdType.ASSIGN_UUID)
    private String handleId;

    @TableField("ALERT_ID")
    private String alertId;

    @TableField("HANDLE_TYPE")
    private String handleType;

    @TableField("HANDLE_DESC")
    private String handleDesc;

    @TableField("DEADLINE")
    private LocalDateTime deadline;

    @TableField("HANDLER_NAME")
    private String handlerName;

    @TableField("STATUS")
    private String status;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
