package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 战略规划实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_STRATEGY_PLANNING")
public class GzctStrategyPlanning extends Model<GzctStrategyPlanning> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("PLAN_NO")
    private String planNo;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("PLAN_TYPE")
    private String planType;

    @TableField("MANAGER")
    private String manager;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("START_DATE")
    private String startDate;

    @TableField("END_DATE")
    private String endDate;

    @TableField("PROGRESS")
    private Integer progress;

    @TableField("BUDGET")
    private BigDecimal budget;

    @TableField("PRIORITY")
    private String priority;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("STATUS")
    private String status;

    @TableField("COMPLETION_DATE")
    private String completionDate;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
