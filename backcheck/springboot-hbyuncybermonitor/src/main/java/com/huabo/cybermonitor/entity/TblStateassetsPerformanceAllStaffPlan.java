package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 全员绩效计划表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_ALL_STAFF_PLAN")
public class TblStateassetsPerformanceAllStaffPlan {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("DEPT_ID")
    private String deptId;

    @TableField("USER_ID")
    private String userId;

    @TableField("YEAR")
    private Integer year;

    @TableField("PERIOD_TYPE")
    private String periodType;

    @TableField("PERIOD_VALUE")
    private String periodValue;

    @TableField("INDEX_ID")
    private String indexId;

    @TableField("TARGET_VALUE")
    private BigDecimal targetValue;

    @TableField("WEIGHT")
    private BigDecimal weight;

    @TableField("SCORE_RULE")
    private String scoreRule;

    @TableField("STATUS")
    private String status;

    @TableField("VERSION")
    private Integer version;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
