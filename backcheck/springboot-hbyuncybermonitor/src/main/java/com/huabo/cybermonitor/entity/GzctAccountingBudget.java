package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_BUDGET")
public class GzctAccountingBudget {

    @TableId(value = "BUDGET_ID", type = IdType.ASSIGN_UUID)
    private String budgetId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("BUDGET_YEAR")
    private String budgetYear;

    @TableField("BUDGET_TYPE")
    private String budgetType;

    @TableField("CATEGORY")
    private String category;

    @TableField("ANNUAL_BUDGET")
    private BigDecimal annualBudget;

    @TableField("EXECUTED")
    private BigDecimal executed;

    @TableField("RATE")
    private BigDecimal rate;

    @TableField("OVER_AMOUNT")
    private BigDecimal overAmount;

    @TableField("END_RUSH")
    private String endRush;

    @TableField("ALERT_LEVEL")
    private String alertLevel;

    @TableField("ALERT_STATUS")
    private String alertStatus;

    @TableField("MONTH_BUDGET")
    private BigDecimal monthBudget;

    @TableField("MONTH_ACTUAL")
    private BigDecimal monthActual;

    @TableField("MONTH_GAP")
    private BigDecimal monthGap;

    @TableField("CUM_RATE")
    private BigDecimal cumRate;

    @TableField("ADJUST_TYPE")
    private String adjustType;

    @TableField("ADJUST_AMOUNT")
    private BigDecimal adjustAmount;

    @TableField("ADJUST_REASON")
    private String adjustReason;

    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @TableField("ADJUST_COUNT")
    private Integer adjustCount;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
