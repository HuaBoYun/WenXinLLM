package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算定时备份计划实体
 */
@Data
@TableName("TBL_BUDGET_BACKUP_SCHEDULE")
public class BudgetBackupSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "SCHEDULE_ID", type = IdType.ASSIGN_UUID)
    private String scheduleId;

    @TableField("SCHEDULE_NAME")
    private String scheduleName;

    @TableField("SCHEDULE_TYPE")
    private String scheduleType;

    @TableField("CRON_EXPRESSION")
    private String cronExpression;

    @TableField("BACKUP_TYPE")
    private String backupType;

    @TableField("BACKUP_SCOPE")
    private String backupScope;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @TableField("LAST_EXECUTE_TIME")
    private Date lastExecuteTime;

    @TableField("NEXT_EXECUTE_TIME")
    private Date nextExecuteTime;

    @TableField("EXECUTE_COUNT")
    private Integer executeCount;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATOR_ID")
    private String creatorId;

    @TableField("CREATOR_NAME")
    private String creatorName;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("IS_DELETED")
    private Integer isDeleted;
}
