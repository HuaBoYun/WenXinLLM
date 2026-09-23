package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_FRAUD")
public class GzctAccountingFraud {

    @TableId(value = "FRAUD_ID", type = IdType.ASSIGN_UUID)
    private String fraudId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("FRAUD_TYPE")
    private String fraudType;

    @TableField("TOTAL_SCORE")
    private Integer totalScore;

    @TableField("PERF_SCORE")
    private Integer perfScore;

    @TableField("LEVER_SCORE")
    private Integer leverScore;

    @TableField("CLEAR_SCORE")
    private Integer clearScore;

    @TableField("RD_SCORE")
    private Integer rdScore;

    @TableField("TWOGOLD_SCORE")
    private Integer twoGoldScore;

    @TableField("ALERT_LEVEL")
    private String alertLevel;

    @TableField("CLUE_COUNT")
    private Integer clueCount;

    @TableField("REVENUE_GROWTH")
    private BigDecimal revenueGrowth;

    @TableField("INDUSTRY_AVG")
    private BigDecimal industryAvg;

    @TableField("Z_SCORE")
    private BigDecimal zScore;

    @TableField("GROSS_MARGIN")
    private BigDecimal grossMargin;

    @TableField("CASH_CONTENT")
    private BigDecimal cashContent;

    @TableField("DEC12_RATE")
    private BigDecimal dec12Rate;

    @TableField("REVENUE_ANOMALY")
    private String revenueAnomaly;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RD_AMOUNT")
    private BigDecimal rdAmount;

    @TableField("CAPITAL_AMOUNT")
    private BigDecimal capitalAmount;

    @TableField("CAPITAL_RATE")
    private BigDecimal capitalRate;

    @TableField("RD_STAFF")
    private Integer rdStaff;

    @TableField("OUTPUT_PER_PERSON")
    private BigDecimal outputPerPerson;

    @TableField("PATENTS")
    private Integer patents;

    // ========== 杠杆假字段 ==========
    @TableField("DEBT_RATIO")
    private BigDecimal debtRatio;

    @TableField("INTEREST_COVERAGE")
    private BigDecimal interestCoverage;

    @TableField("SHORT_DEBT")
    private BigDecimal shortDebt;

    @TableField("LONG_DEBT")
    private BigDecimal longDebt;

    @TableField("OFF_BALANCE_DEBT")
    private BigDecimal offBalanceDebt;

    @TableField("GUARANTEE_AMOUNT")
    private BigDecimal guaranteeAmount;

    // ========== 出清假字段 ==========
    @TableField("DISPOSAL_AMOUNT")
    private BigDecimal disposalAmount;

    @TableField("DISPOSAL_GAIN")
    private BigDecimal disposalGain;

    @TableField("DISPOSAL_RATE")
    private BigDecimal disposalRate;

    @TableField("RELATED_PARTY_DEAL")
    private BigDecimal relatedPartyDeal;

    @TableField("FAIR_VALUE_GAP")
    private BigDecimal fairValueGap;

    @TableField("TIMING_ANOMALY")
    private String timingAnomaly;

    // ========== 两金假字段 ==========
    @TableField("RECEIVABLE_GROWTH")
    private BigDecimal receivableGrowth;

    @TableField("INVENTORY_GROWTH")
    private BigDecimal inventoryGrowth;

    @TableField("RECEIVABLE_TURNOVER")
    private BigDecimal receivableTurnover;

    @TableField("INVENTORY_TURNOVER")
    private BigDecimal inventoryTurnover;

    @TableField("BAD_DEBT_RATE")
    private BigDecimal badDebtRate;

    @TableField("IMPAIRMENT_RATE")
    private BigDecimal impairmentRate;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
