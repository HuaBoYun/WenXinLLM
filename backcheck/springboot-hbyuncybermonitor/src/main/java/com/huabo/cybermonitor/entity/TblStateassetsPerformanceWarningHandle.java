package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 预警处置表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_WARNING_HANDLE")
public class TblStateassetsPerformanceWarningHandle {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("WARNING_ID")
    private String warningId;

    @TableField("HANDLE_TYPE")
    private String handleType;

    @TableField("HANDLE_CONTENT")
    private String handleContent;

    @TableField("HANDLE_RESULT")
    private String handleResult;

    @TableField("HANDLE_OPINION")
    private String handleOpinion;

    @TableField("ATTACHMENT_URL")
    private String attachmentUrl;

    @TableField("HANDLE_USER")
    private String handleUser;

    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
