package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 军品任务台账实体 - 军品业务穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_TASK")
public class TblMilitaryTask {

    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("TASK_CODE")
    private String taskCode;

    @TableField("TASK_NAME")
    private String taskName;

    @TableField("TASK_TYPE")
    private String taskType;

    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    @TableField("BUDGET_COST")
    private BigDecimal budgetCost;

    @TableField("ACTUAL_COST")
    private BigDecimal actualCost;

    @TableField("DELIVERY_DEADLINE")
    private LocalDate deliveryDeadline;

    @TableField("PLAN_PROGRESS")
    private BigDecimal planProgress;

    @TableField("ACTUAL_PROGRESS")
    private BigDecimal actualProgress;

    @TableField("QUALITY_PASS_RATE")
    private BigDecimal qualityPassRate;

    @TableField("TASK_STATUS")
    private String taskStatus;

    @TableField("QUALIFICATION_ID")
    private String qualificationId;

    @TableField("QUAL_CERT_NAME")
    private String qualCertName;

    @TableField("QUAL_EXPIRY_DATE")
    private LocalDate qualExpiryDate;

    @TableField("SECURITY_LEVEL")
    private String securityLevel;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

