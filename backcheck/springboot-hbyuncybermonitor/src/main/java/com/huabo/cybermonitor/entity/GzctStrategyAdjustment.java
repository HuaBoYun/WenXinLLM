package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 战略调整实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_STRATEGY_ADJUSTMENT")
public class GzctStrategyAdjustment extends Model<GzctStrategyAdjustment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ADJUSTMENT_NO")
    private String adjustmentNo;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("ADJUSTMENT_TYPE")
    private String adjustmentType;

    @TableField("APPLICANT")
    private String applicant;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("ADJUSTMENT_REASON")
    private String adjustmentReason;

    @TableField("ORIGINAL_TARGET")
    private String originalTarget;

    @TableField("ADJUSTED_TARGET")
    private String adjustedTarget;

    @TableField("IMPACT_LEVEL")
    private String impactLevel;

    @TableField("URGENCY_LEVEL")
    private String urgencyLevel;

    @TableField("BUDGET_IMPACT")
    private BigDecimal budgetImpact;

    @TableField("TIME_IMPACT")
    private Integer timeImpact;

    @TableField("APPROVER")
    private String approver;

    @TableField("APPLICATION_DATE")
    private String applicationDate;

    @TableField("STATUS")
    private String status;

    @TableField("COMPLETION_DATE")
    private String completionDate;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
