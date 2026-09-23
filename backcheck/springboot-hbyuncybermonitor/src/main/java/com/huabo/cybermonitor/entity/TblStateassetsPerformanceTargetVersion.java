package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 目标版本表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_TARGET_VERSION")
public class TblStateassetsPerformanceTargetVersion {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("TARGET_ID")
    private String targetId;

    @TableField("VERSION")
    private Integer version;

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

    @TableField("TIME_TARGET_VALUE")
    private BigDecimal timeTargetValue;

    @TableField("CHANGE_CONTENT")
    private String changeContent;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
