package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核目标表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_TARGET")
public class TblStateassetsPerformanceTarget {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("INDEX_ID")
    private String indexId;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("YEAR")
    private Integer year;

    @TableField("PERIOD_TYPE")
    private String periodType;

    @TableField("PERIOD_VALUE")
    private String periodValue;

    @TableField("TARGET_VALUE")
    private BigDecimal targetValue;

    @TableField("TIME_PROGRESS")
    private BigDecimal timeProgress;

    @TableField("TIME_TARGET_VALUE")
    private BigDecimal timeTargetValue;

    @TableField("VERSION")
    private Integer version;

    @TableField("LOCK_STATUS")
    private String lockStatus;

    @TableField("NEGOTIATE_STATUS")
    private String negotiateStatus;

    @TableField("PARENT_ID")
    private String parentId;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
