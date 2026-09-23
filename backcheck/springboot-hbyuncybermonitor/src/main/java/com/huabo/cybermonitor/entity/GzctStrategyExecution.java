package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 执行监控实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_STRATEGY_EXECUTION")
public class GzctStrategyExecution extends Model<GzctStrategyExecution> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("EXECUTION_NO")
    private String executionNo;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("EXECUTION_PHASE")
    private String executionPhase;

    @TableField("EXECUTOR")
    private String executor;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("START_DATE")
    private String startDate;

    @TableField("PLAN_END_DATE")
    private String planEndDate;

    @TableField("ACTUAL_PROGRESS")
    private Integer actualProgress;

    @TableField("PLAN_PROGRESS")
    private Integer planProgress;

    @TableField("PROGRESS_DEVIATION")
    private Integer progressDeviation;

    @TableField("BUDGET_USED")
    private BigDecimal budgetUsed;

    @TableField("COMPLETED_MILESTONES")
    private Integer completedMilestones;

    @TableField("TOTAL_MILESTONES")
    private Integer totalMilestones;

    @TableField("RISK_COUNT")
    private Integer riskCount;

    @TableField("STATUS")
    private String status;

    @TableField("LAST_UPDATE_DATE")
    private String lastUpdateDate;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
