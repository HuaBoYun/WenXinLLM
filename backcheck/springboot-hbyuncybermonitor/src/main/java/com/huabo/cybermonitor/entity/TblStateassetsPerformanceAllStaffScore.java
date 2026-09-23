package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 全员绩效评分表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_ALL_STAFF_SCORE")
public class TblStateassetsPerformanceAllStaffScore {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("PLAN_ID")
    private String planId;

    @TableField("SCORE_TYPE")
    private String scoreType;

    @TableField("SCORER_ID")
    private String scorerId;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField("SCORE_COMMENT")
    private String scoreComment;

    @TableField("ATTACHMENT_URL")
    private String attachmentUrl;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
