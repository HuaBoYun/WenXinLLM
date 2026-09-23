package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 财务指标查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FinancialIndicatorsQueryVO extends BaseVo {

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 报表年度
     */
    private Integer reportYear;

    /**
     * 报表期间
     */
    private Integer reportPeriod;

    /**
     * 预警等级
     */
    private String warningLevel;

    /**
     * 数据状态
     */
    private String dataStatus;

    /**
     * 计算方法
     */
    private String calculationMethod;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 行业对比结果
     */
    private String industryComparison;

    // ==================== 盈利能力指标范围查询 ====================

    /**
     * 最小净资产收益率
     */
    private BigDecimal minReturnOnEquity;

    /**
     * 最大净资产收益率
     */
    private BigDecimal maxReturnOnEquity;

    /**
     * 最小总资产收益率
     */
    private BigDecimal minReturnOnAssets;

    /**
     * 最大总资产收益率
     */
    private BigDecimal maxReturnOnAssets;

    /**
     * 最小毛利率
     */
    private BigDecimal minGrossProfitMargin;

    /**
     * 最大毛利率
     */
    private BigDecimal maxGrossProfitMargin;

    /**
     * 最小净利润率
     */
    private BigDecimal minNetProfitMargin;

    /**
     * 最大净利润率
     */
    private BigDecimal maxNetProfitMargin;

    /**
     * 最小每股收益
     */
    private BigDecimal minEarningsPerShare;

    /**
     * 最大每股收益
     */
    private BigDecimal maxEarningsPerShare;

    // ==================== 偿债能力指标范围查询 ====================

    /**
     * 最小资产负债率
     */
    private BigDecimal minAssetLiabilityRatio;

    /**
     * 最大资产负债率
     */
    private BigDecimal maxAssetLiabilityRatio;

    /**
     * 最小流动比率
     */
    private BigDecimal minCurrentRatio;

    /**
     * 最大流动比率
     */
    private BigDecimal maxCurrentRatio;

    /**
     * 最小速动比率
     */
    private BigDecimal minQuickRatio;

    /**
     * 最大速动比率
     */
    private BigDecimal maxQuickRatio;

    /**
     * 最小利息保障倍数
     */
    private BigDecimal minInterestCoverageRatio;

    /**
     * 最大利息保障倍数
     */
    private BigDecimal maxInterestCoverageRatio;

    // ==================== 运营能力指标范围查询 ====================

    /**
     * 最小总资产周转率
     */
    private BigDecimal minTotalAssetTurnoverRatio;

    /**
     * 最大总资产周转率
     */
    private BigDecimal maxTotalAssetTurnoverRatio;

    /**
     * 最小存货周转率
     */
    private BigDecimal minInventoryTurnoverRatio;

    /**
     * 最大存货周转率
     */
    private BigDecimal maxInventoryTurnoverRatio;

    /**
     * 最小应收账款周转率
     */
    private BigDecimal minAccountsReceivableTurnoverRatio;

    /**
     * 最大应收账款周转率
     */
    private BigDecimal maxAccountsReceivableTurnoverRatio;

    // ==================== 发展能力指标范围查询 ====================

    /**
     * 最小营业收入增长率
     */
    private BigDecimal minRevenueGrowthRate;

    /**
     * 最大营业收入增长率
     */
    private BigDecimal maxRevenueGrowthRate;

    /**
     * 最小净利润增长率
     */
    private BigDecimal minNetProfitGrowthRate;

    /**
     * 最大净利润增长率
     */
    private BigDecimal maxNetProfitGrowthRate;

    /**
     * 最小总资产增长率
     */
    private BigDecimal minTotalAssetGrowthRate;

    /**
     * 最大总资产增长率
     */
    private BigDecimal maxTotalAssetGrowthRate;

    // ==================== 现金流量指标范围查询 ====================

    /**
     * 最小经营活动现金流量净额/营业收入
     */
    private BigDecimal minOperatingCashFlowToRevenue;

    /**
     * 最大经营活动现金流量净额/营业收入
     */
    private BigDecimal maxOperatingCashFlowToRevenue;

    /**
     * 最小现金流量比率
     */
    private BigDecimal minCashFlowRatio;

    /**
     * 最大现金流量比率
     */
    private BigDecimal maxCashFlowRatio;

    // ==================== 综合评价指标范围查询 ====================

    /**
     * 最小综合评分
     */
    private BigDecimal minComprehensiveScore;

    /**
     * 最大综合评分
     */
    private BigDecimal maxComprehensiveScore;

    /**
     * 最小盈利能力评分
     */
    private BigDecimal minProfitabilityScore;

    /**
     * 最大盈利能力评分
     */
    private BigDecimal maxProfitabilityScore;

    /**
     * 最小偿债能力评分
     */
    private BigDecimal minSolvencyScore;

    /**
     * 最大偿债能力评分
     */
    private BigDecimal maxSolvencyScore;

    /**
     * 最小运营能力评分
     */
    private BigDecimal minOperatingAbilityScore;

    /**
     * 最大运营能力评分
     */
    private BigDecimal maxOperatingAbilityScore;

    /**
     * 最小发展能力评分
     */
    private BigDecimal minDevelopmentAbilityScore;

    /**
     * 最大发展能力评分
     */
    private BigDecimal maxDevelopmentAbilityScore;

    // ==================== 时间范围查询 ====================

    /**
     * 开始年度
     */
    private Integer startYear;

    /**
     * 结束年度
     */
    private Integer endYear;

    /**
     * 开始期间
     */
    private Integer startPeriod;

    /**
     * 结束期间
     */
    private Integer endPeriod;

    /**
     * 开始计算日期
     */
    private String startCalculationDate;

    /**
     * 结束计算日期
     */
    private String endCalculationDate;

    /**
     * 开始审核日期
     */
    private String startAuditDate;

    /**
     * 结束审核日期
     */
    private String endAuditDate;

    // ==================== 其他查询条件 ====================

    /**
     * 计算人
     */
    private String calculatedBy;

    /**
     * 审核人
     */
    private String auditedBy;

    /**
     * 最小行业排名
     */
    private Integer minIndustryRanking;

    /**
     * 最大行业排名
     */
    private Integer maxIndustryRanking;

    /**
     * 关键字搜索
     */
    private String keyword;

}
