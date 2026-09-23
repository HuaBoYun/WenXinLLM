package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核完成值表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_ACTUAL")
public class TblStateassetsPerformanceActual {

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

    @TableField("ACTUAL_VALUE")
    private BigDecimal actualValue;

    @TableField("CUMULATIVE_VALUE")
    private BigDecimal cumulativeValue;

    @TableField("COMPLETION_RATE")
    private BigDecimal completionRate;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    @TableField("SOURCE_BILL_NO")
    private String sourceBillNo;

    @TableField("SOURCE_BILL_TYPE")
    private String sourceBillType;

    @TableField("TASK_ID")
    private String taskId;

    @TableField("FILL_USER")
    private String fillUser;

    @TableField("FILL_TIME")
    private LocalDateTime fillTime;

    @TableField("AUDIT_STATUS")
    private String auditStatus;

    @TableField("AUDIT_USER")
    private String auditUser;

    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @TableField("AUDIT_OPINION")
    private String auditOpinion;

    @TableField("REPORT_STATUS")
    private String reportStatus;

    @TableField("LOCK_STATUS")
    private String lockStatus;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
