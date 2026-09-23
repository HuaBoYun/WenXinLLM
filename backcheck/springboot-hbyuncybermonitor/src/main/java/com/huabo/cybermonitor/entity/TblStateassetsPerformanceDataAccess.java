package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据接入看板状态表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_DATA_ACCESS")
public class TblStateassetsPerformanceDataAccess {

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

    @TableField("ACCESS_STATUS")
    private String accessStatus;

    @TableField("ACCESS_TYPE")
    private String accessType;

    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    @TableField("SOURCE_TABLE")
    private String sourceTable;

    @TableField("SOURCE_FIELD")
    private String sourceField;

    @TableField("TASK_ID")
    private String taskId;

    @TableField("TASK_STATUS")
    private String taskStatus;

    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @TableField("QUALITY_ISSUES")
    private String qualityIssues;

    @TableField("LAST_ACCESS_TIME")
    private LocalDateTime lastAccessTime;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
