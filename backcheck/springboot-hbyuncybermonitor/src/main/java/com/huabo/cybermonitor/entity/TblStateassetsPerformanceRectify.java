package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 整改任务表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_RECTIFY")
public class TblStateassetsPerformanceRectify {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("RECTIFY_CODE")
    private String rectifyCode;

    @TableField("SOURCE_TYPE")
    private String sourceType;

    @TableField("SOURCE_ID")
    private String sourceId;

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

    @TableField("RECTIFY_TITLE")
    private String rectifyTitle;

    @TableField("RECTIFY_CONTENT")
    private String rectifyContent;

    @TableField("DEADLINE")
    private LocalDate deadline;

    @TableField("RESPONSIBLE_USER")
    private String responsibleUser;

    @TableField("ACCEPT_STANDARD")
    private String acceptStandard;

    @TableField("STATUS")
    private String status;

    @TableField("LEVEL")
    private String level;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
