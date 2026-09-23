package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务报表数据实体 - 财务穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCE_STATEMENT")
public class TblFinanceStatement {

    @TableId(value = "STATEMENT_ID", type = IdType.ASSIGN_UUID)
    private String statementId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("TOTAL_ASSETS")
    private BigDecimal totalAssets;

    @TableField("TOTAL_LIABILITIES")
    private BigDecimal totalLiabilities;

    @TableField("NET_ASSETS")
    private BigDecimal netAssets;

    @TableField("REVENUE")
    private BigDecimal revenue;

    @TableField("NET_PROFIT")
    private BigDecimal netProfit;

    @TableField("OPERATING_CASH")
    private BigDecimal operatingCash;

    @TableField("DEBT_RATIO")
    private BigDecimal debtRatio;

    @TableField("ROE")
    private BigDecimal roe;

    @TableField("ROA")
    private BigDecimal roa;

    @TableField("CURRENT_RATIO")
    private BigDecimal currentRatio;

    @TableField("RECEIVABLE_DAYS")
    private BigDecimal receivableDays;

    @TableField("INVENTORY_DAYS")
    private BigDecimal inventoryDays;

    @TableField("ADMIN_EXPENSE_RATE")
    private BigDecimal adminExpenseRate;

    @TableField("SALES_EXPENSE_RATE")
    private BigDecimal salesExpenseRate;

    @TableField("IS_CONSOLIDATED")
    private String isConsolidated;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

