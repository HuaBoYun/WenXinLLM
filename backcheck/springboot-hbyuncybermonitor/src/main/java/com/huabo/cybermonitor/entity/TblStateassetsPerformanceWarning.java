package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预警结果表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_WARNING")
public class TblStateassetsPerformanceWarning {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("WARNING_CODE")
    private String warningCode;

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

    @TableField("WARNING_LEVEL")
    private String warningLevel;

    @TableField("WARNING_TYPE")
    private String warningType;

    @TableField("TRIGGERED_RULES")
    private String triggeredRules;

    @TableField("WARNING_DESC")
    private String warningDesc;

    @TableField("ACTUAL_VALUE")
    private BigDecimal actualValue;

    @TableField("TARGET_VALUE")
    private BigDecimal targetValue;

    @TableField("COMPLETION_RATE")
    private BigDecimal completionRate;

    @TableField("HANDLE_STATUS")
    private String handleStatus;

    @TableField("HANDLE_USER")
    private String handleUser;

    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
