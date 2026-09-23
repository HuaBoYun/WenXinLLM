package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报告调度实体
 */
@Data
@TableName("TBL_REPORT_SCHEDULE")
public class ReportScheduleEntity {

    @TableId(type = IdType.AUTO)
    @TableField("SCHEDULE_ID")
    private Long scheduleId;

    @TableField("SCHEDULE_NAME")
    private String scheduleName;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("TEMPLATE_ID")
    private String templateId;

    @TableField("CRON_EXPRESSION")
    private String cronExpression;

    @TableField("SCHEDULE_TYPE")
    private String scheduleType; // DAILY, WEEKLY, MONTHLY, CUSTOM

    @TableField("PARAMETERS")
    private String parameters;

    @TableField("OUTPUT_FORMAT")
    private String outputFormat;

    @TableField("DISTRIBUTION_CONFIG")
    private String distributionConfig;

    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    @TableField("LAST_RUN_TIME")
    private LocalDateTime lastRunTime;

    @TableField("NEXT_RUN_TIME")
    private LocalDateTime nextRunTime;

    @TableField("RUN_COUNT")
    private Integer runCount;

    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("CREATE_USER")
    private Long createUser;

    @TableField("UPDATE_USER")
    private Long updateUser;
}