package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核结果表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_EVALUATION")
public class TblStateassetsPerformanceEvaluation {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("SCHEME_ID")
    private String schemeId;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("YEAR")
    private Integer year;

    @TableField("TOTAL_SCORE")
    private BigDecimal totalScore;

    @TableField("RANK_NUM")
    private Integer rankNum;

    @TableField("LEVEL")
    private String level;

    @TableField("RESULT_STATUS")
    private String resultStatus;

    @TableField("APPEAL_STATUS")
    private String appealStatus;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
