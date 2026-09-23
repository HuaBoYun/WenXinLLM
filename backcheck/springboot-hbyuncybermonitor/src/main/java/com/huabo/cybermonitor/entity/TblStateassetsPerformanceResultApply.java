package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结果应用表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_RESULT_APPLY")
public class TblStateassetsPerformanceResultApply {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("EVALUATION_ID")
    private String evaluationId;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("YEAR")
    private Integer year;

    @TableField("APPLY_TYPE")
    private String applyType;

    @TableField("APPLY_CONTENT")
    private String applyContent;

    @TableField("SALARY_AMOUNT")
    private BigDecimal salaryAmount;

    @TableField("BONUS_POOL_AMOUNT")
    private BigDecimal bonusPoolAmount;

    @TableField("APPOINTMENT_SUGGESTION")
    private String appointmentSuggestion;

    @TableField("RESOURCE_ALLOCATION")
    private String resourceAllocation;

    @TableField("RECTIFY_REQUIREMENT")
    private String rectifyRequirement;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
