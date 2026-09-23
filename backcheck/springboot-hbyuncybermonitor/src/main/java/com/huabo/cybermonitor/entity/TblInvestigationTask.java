package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 核查任务表实体 - 协同核查平台
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVESTIGATION_TASK")
public class TblInvestigationTask {

    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    @TableField("TASK_CODE")
    private String taskCode;

    @TableField("WARNING_ID")
    private String warningId;

    @TableField("DOMAIN_TYPE")
    private String domainType;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("TASK_TITLE")
    private String taskTitle;

    @TableField("TASK_DESC")
    private String taskDesc;

    @TableField("TASK_STATUS")
    private String taskStatus;

    @TableField("ASSIGN_USER")
    private String assignUser;

    @TableField("ASSIGN_TIME")
    private LocalDateTime assignTime;

    @TableField("DEADLINE")
    private LocalDateTime deadline;

    @TableField("CONCLUSION")
    private String conclusion;

    @TableField("CONCLUSION_DESC")
    private String conclusionDesc;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

