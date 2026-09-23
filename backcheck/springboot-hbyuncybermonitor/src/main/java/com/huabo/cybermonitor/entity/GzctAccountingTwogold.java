package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_TWOGOLD")
public class GzctAccountingTwogold {

    @TableId(value = "TWOGOLD_ID", type = IdType.ASSIGN_UUID)
    private String twogoldId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("RECORD_MONTH")
    private String recordMonth;

    @TableField("DATA_TYPE")
    private String dataType;

    @TableField("RECEIVABLE")
    private BigDecimal receivable;

    @TableField("INVENTORY")
    private BigDecimal inventory;

    @TableField("TOTAL")
    private BigDecimal total;

    @TableField("REVENUE_RATE")
    private BigDecimal revenueRate;

    @TableField("TARGET_REDUCE")
    private BigDecimal targetReduce;

    @TableField("REDUCE_RATE")
    private BigDecimal reduceRate;

    @TableField("RISK_FLAG")
    private String riskFlag;

    @TableField("CUSTOMER_NAME")
    private String customerName;

    @TableField("WITHIN_1Y")
    private BigDecimal within1y;

    @TableField("Y1TO2")
    private BigDecimal y1to2;

    @TableField("Y2TO3")
    private BigDecimal y2to3;

    @TableField("OVER_3Y")
    private BigDecimal over3y;

    @TableField("OVER_3Y_RATE")
    private BigDecimal over3yRate;

    @TableField("BAD_DEBT")
    private BigDecimal badDebt;

    @TableField("RISK")
    private String risk;

    @TableField("CATEGORY")
    private String category;

    @TableField("BOOK_VALUE")
    private BigDecimal bookValue;

    @TableField("IMPAIRMENT")
    private BigDecimal impairment;

    @TableField("SLOW")
    private BigDecimal slow;

    @TableField("SLOW_RATE")
    private BigDecimal slowRate;

    @TableField("TURNOVER")
    private BigDecimal turnover;

    @TableField("FRAUD_TYPE")
    private String fraudType;

    @TableField("RULE_DESC")
    private String ruleDesc;

    @TableField("RISK_SCORE")
    private Integer riskScore;

    @TableField("ALERT_LEVEL_TG")
    private String alertLevelTg;

    @TableField("FOUND_TIME")
    private LocalDate foundTime;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
