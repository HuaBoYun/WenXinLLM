package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FINANCIAL_INDICATORS")
public class FinancialIndicators {

    // ==================== 常量定义 ====================

    // 数据状态常量
    public static final String DATA_STATUS_CALCULATING = "CALCULATING";
    public static final String DATA_STATUS_CALCULATED = "CALCULATED";
    public static final String DATA_STATUS_APPROVED = "APPROVED";
    public static final String DATA_STATUS_PUBLISHED = "PUBLISHED";

    // 计算方式常量
    public static final String CALCULATION_METHOD_AUTO = "AUTO";
    public static final String CALCULATION_METHOD_MANUAL = "MANUAL";
    public static final String CALCULATION_METHOD_MIXED = "MIXED";

    // 审核状态常量
    public static final String AUDIT_STATUS_PENDING = "PENDING";
    public static final String AUDIT_STATUS_APPROVED = "APPROVED";
    public static final String AUDIT_STATUS_REJECTED = "REJECTED";

    // 行业对比常量
    public static final String INDUSTRY_COMPARISON_EXCELLENT = "EXCELLENT";
    public static final String INDUSTRY_COMPARISON_GOOD = "GOOD";
    public static final String INDUSTRY_COMPARISON_AVERAGE = "AVERAGE";
    public static final String INDUSTRY_COMPARISON_BELOW = "BELOW";
    public static final String INDUSTRY_COMPARISON_POOR = "POOR";

    // 指标类型常量
    public static final String INDICATOR_TYPE_PROFITABILITY = "PROFITABILITY";
    public static final String INDICATOR_TYPE_SOLVENCY = "SOLVENCY";
    public static final String INDICATOR_TYPE_OPERATING = "OPERATING";
    public static final String INDICATOR_TYPE_DEVELOPMENT = "DEVELOPMENT";
    public static final String INDICATOR_TYPE_CASH_FLOW = "CASH_FLOW";

    // 质量等级常量
    public static final String QUALITY_LEVEL_EXCELLENT = "EXCELLENT";
    public static final String QUALITY_LEVEL_GOOD = "GOOD";
    public static final String QUALITY_LEVEL_AVERAGE = "AVERAGE";
    public static final String QUALITY_LEVEL_POOR = "POOR";
    public static final String QUALITY_LEVEL_BAD = "BAD";

    @TableId(value = "INDICATOR_ID", type = IdType.ASSIGN_UUID)
    private String indicatorId;

    @TableField("STATEMENT_ID")
    private String statementId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("REPORT_YEAR")
    private Integer reportYear;

    @TableField("REPORT_PERIOD")
    private Integer reportPeriod;

    @TableField("CALCULATION_DATE")
    private LocalDateTime calculationDate;

    @TableField("RETURN_ON_EQUITY")
    private BigDecimal returnOnEquity;

    @TableField("RETURN_ON_ASSETS")
    private BigDecimal returnOnAssets;

    @TableField("GROSS_PROFIT_MARGIN")
    private BigDecimal grossProfitMargin;

    @TableField("OPERATING_PROFIT_MARGIN")
    private BigDecimal operatingProfitMargin;

    @TableField("NET_PROFIT_MARGIN")
    private BigDecimal netProfitMargin;

    @TableField("ASSET_LIABILITY_RATIO")
    private BigDecimal assetLiabilityRatio;

    @TableField("CURRENT_RATIO")
    private BigDecimal currentRatio;

    @TableField("QUICK_RATIO")
    private BigDecimal quickRatio;

    @TableField("TOTAL_ASSET_TURNOVER_RATIO")
    private BigDecimal totalAssetTurnoverRatio;

    @TableField("INVENTORY_TURNOVER_RATIO")
    private BigDecimal inventoryTurnoverRatio;

    @TableField("ACCOUNTS_RECEIVABLE_TURNOVER_RATIO")
    private BigDecimal accountsReceivableTurnoverRatio;

    @TableField("INVENTORY_TURNOVER_DAYS")
    private BigDecimal inventoryTurnoverDays;

    @TableField("ACCOUNTS_RECEIVABLE_TURNOVER_DAYS")
    private BigDecimal accountsReceivableTurnoverDays;

    @TableField("CASH_CONVERSION_CYCLE")
    private BigDecimal cashConversionCycle;

    @TableField("REVENUE_GROWTH_RATE")
    private BigDecimal revenueGrowthRate;

    @TableField("NET_PROFIT_GROWTH_RATE")
    private BigDecimal netProfitGrowthRate;

    @TableField("TOTAL_ASSET_GROWTH_RATE")
    private BigDecimal totalAssetGrowthRate;

    @TableField("OPERATING_CASH_FLOW_TO_REVENUE")
    private BigDecimal operatingCashFlowToRevenue;

    @TableField("OPERATING_CASH_FLOW_TO_NET_PROFIT")
    private BigDecimal operatingCashFlowToNetProfit;

    @TableField("COMPREHENSIVE_SCORE")
    private BigDecimal comprehensiveScore;

    @TableField("PROFITABILITY_SCORE")
    private BigDecimal profitabilityScore;

    @TableField("SOLVENCY_SCORE")
    private BigDecimal solvencyScore;

    @TableField("OPERATING_ABILITY_SCORE")
    private BigDecimal operatingAbilityScore;

    @TableField("DEVELOPMENT_ABILITY_SCORE")
    private BigDecimal developmentAbilityScore;

    @TableField("CASH_FLOW_SCORE")
    private BigDecimal cashFlowScore;

    @TableField("RISK_CONTROL_SCORE")
    private BigDecimal riskControlScore;

    @TableField("WARNING_LEVEL")
    private String warningLevel;

    @TableField("WARNING_REASON")
    private String warningReason;

    @TableField("DATA_STATUS")
    private String dataStatus;

    @TableField("REMARKS")
    private String remarks;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("VERSION")
    private Integer version;

    // ==================== 盈利能力扩展字段 ====================

    @TableField("RETURN_ON_INVESTED_CAPITAL")
    private BigDecimal returnOnInvestedCapital;

    @TableField("EARNINGS_PER_SHARE")
    private BigDecimal earningsPerShare;

    // ==================== 偿债能力扩展字段 ====================

    @TableField("CASH_RATIO")
    private BigDecimal cashRatio;

    @TableField("DEBT_TO_EQUITY_RATIO")
    private BigDecimal debtToEquityRatio;

    @TableField("EQUITY_MULTIPLIER")
    private BigDecimal equityMultiplier;

    @TableField("INTEREST_COVERAGE_RATIO")
    private BigDecimal interestCoverageRatio;

    @TableField("CASH_FLOW_INTEREST_COVERAGE_RATIO")
    private BigDecimal cashFlowInterestCoverageRatio;

    // ==================== 营运能力扩展字段 ====================

    @TableField("ACCOUNTS_PAYABLE_TURNOVER_RATIO")
    private BigDecimal accountsPayableTurnoverRatio;

    @TableField("WORKING_CAPITAL_TURNOVER_RATIO")
    private BigDecimal workingCapitalTurnoverRatio;

    @TableField("FIXED_ASSET_TURNOVER_RATIO")
    private BigDecimal fixedAssetTurnoverRatio;

    @TableField("CURRENT_ASSET_TURNOVER_RATIO")
    private BigDecimal currentAssetTurnoverRatio;

    // ==================== 发展能力扩展字段 ====================

    @TableField("NET_ASSET_GROWTH_RATE")
    private BigDecimal netAssetGrowthRate;

    @TableField("SUSTAINABLE_GROWTH_RATE")
    private BigDecimal sustainableGrowthRate;

    @TableField("EPS_GROWTH_RATE")
    private BigDecimal epsGrowthRate;

    // ==================== 现金流量扩展字段 ====================

    @TableField("CASH_FLOW_RATIO")
    private BigDecimal cashFlowRatio;

    @TableField("CASH_FLOW_ADEQUACY_RATIO")
    private BigDecimal cashFlowAdequacyRatio;

    @TableField("CASH_REINVESTMENT_RATIO")
    private BigDecimal cashReinvestmentRatio;

    @TableField("CASH_TO_TOTAL_DEBT_RATIO")
    private BigDecimal cashToTotalDebtRatio;

    @TableField("DELETED")
    private String deleted;
}
