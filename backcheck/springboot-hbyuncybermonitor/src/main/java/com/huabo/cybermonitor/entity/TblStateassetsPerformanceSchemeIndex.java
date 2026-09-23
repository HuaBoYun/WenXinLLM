package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 方案指标关联表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_SCHEME_INDEX")
public class TblStateassetsPerformanceSchemeIndex {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("SCHEME_ID")
    private String schemeId;

    @TableField("INDEX_ID")
    private String indexId;

    @TableField("WEIGHT")
    private BigDecimal weight;

    @TableField("SCORE_RULE")
    private String scoreRule;

    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
