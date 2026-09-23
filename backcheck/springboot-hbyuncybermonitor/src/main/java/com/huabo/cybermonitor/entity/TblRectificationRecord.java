package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 整改记录表实体 - 协同核查平台
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_RECTIFICATION_RECORD")
public class TblRectificationRecord {

    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_UUID)
    private String recordId;

    @TableField("TASK_ID")
    private String taskId;

    @TableField("REQUIREMENT")
    private String requirement;

    @TableField("RESPONSIBLE_USER")
    private String responsibleUser;

    @TableField("DEADLINE")
    private LocalDateTime deadline;

    @TableField("RECT_STATUS")
    private String rectStatus;

    @TableField("PROGRESS_DESC")
    private String progressDesc;

    @TableField("SUBMIT_TIME")
    private LocalDateTime submitTime;

    @TableField("VERIFY_USER")
    private String verifyUser;

    @TableField("VERIFY_TIME")
    private LocalDateTime verifyTime;

    @TableField("VERIFY_RESULT")
    private String verifyResult;

    @TableField("VERIFY_COMMENT")
    private String verifyComment;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

