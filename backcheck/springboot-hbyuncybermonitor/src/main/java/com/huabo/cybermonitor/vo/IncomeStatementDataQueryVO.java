package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 利润表数据查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IncomeStatementDataQueryVO extends BaseVo {

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
     * 数据状态
     */
    private String dataStatus;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 审核状态
     */
    private String auditStatus;

    // ==================== 收入范围查询 ====================

    /**
     * 最小营业收入
     */
    private BigDecimal minOperatingRevenue;

    /**
     * 最大营业收入
     */
    private BigDecimal maxOperatingRevenue;

    /**
     * 最小营业成本
     */
    private BigDecimal minOperatingCosts;

    /**
     * 最大营业成本
     */
    private BigDecimal maxOperatingCosts;

    /**
     * 最小销售费用
     */
    private BigDecimal minSellingExpenses;

    /**
     * 最大销售费用
     */
    private BigDecimal maxSellingExpenses;

    /**
     * 最小管理费用
     */
    private BigDecimal minAdministrativeExpenses;

    /**
     * 最大管理费用
     */
    private BigDecimal maxAdministrativeExpenses;

    /**
     * 最小研发费用
     */
    private BigDecimal minResearchAndDevelopmentExpenses;

    /**
     * 最大研发费用
     */
    private BigDecimal maxResearchAndDevelopmentExpenses;

    /**
     * 最小财务费用
     */
    private BigDecimal minFinancialExpenses;

    /**
     * 最大财务费用
     */
    private BigDecimal maxFinancialExpenses;

    /**
     * 最小投资收益
     */
    private BigDecimal minInvestmentIncome;

    /**
     * 最大投资收益
     */
    private BigDecimal maxInvestmentIncome;

    /**
     * 最小公允价值变动收益
     */
    private BigDecimal minFairValueChangeGains;

    /**
     * 最大公允价值变动收益
     */
    private BigDecimal maxFairValueChangeGains;

    /**
     * 最小资产减值损失
     */
    private BigDecimal minAssetImpairmentLosses;

    /**
     * 最大资产减值损失
     */
    private BigDecimal maxAssetImpairmentLosses;

    // ==================== 利润范围查询 ====================

    /**
     * 最小营业利润
     */
    private BigDecimal minOperatingProfit;

    /**
     * 最大营业利润
     */
    private BigDecimal maxOperatingProfit;

    /**
     * 最小利润总额
     */
    private BigDecimal minTotalProfit;

    /**
     * 最大利润总额
     */
    private BigDecimal maxTotalProfit;

    /**
     * 最小净利润
     */
    private BigDecimal minNetProfit;

    /**
     * 最大净利润
     */
    private BigDecimal maxNetProfit;

    /**
     * 最小归属于母公司所有者的净利润
     */
    private BigDecimal minNetProfitAttributableToParent;

    /**
     * 最大归属于母公司所有者的净利润
     */
    private BigDecimal maxNetProfitAttributableToParent;

    /**
     * 最小少数股东损益
     */
    private BigDecimal minMinorityInterestIncome;

    /**
     * 最大少数股东损益
     */
    private BigDecimal maxMinorityInterestIncome;

    /**
     * 最小所得税费用
     */
    private BigDecimal minIncomeTaxExpenses;

    /**
     * 最大所得税费用
     */
    private BigDecimal maxIncomeTaxExpenses;

    // ==================== 每股收益范围查询 ====================

    /**
     * 最小基本每股收益
     */
    private BigDecimal minBasicEarningsPerShare;

    /**
     * 最大基本每股收益
     */
    private BigDecimal maxBasicEarningsPerShare;

    /**
     * 最小稀释每股收益
     */
    private BigDecimal minDilutedEarningsPerShare;

    /**
     * 最大稀释每股收益
     */
    private BigDecimal maxDilutedEarningsPerShare;

    // ==================== 其他综合收益范围查询 ====================

    /**
     * 最小其他综合收益
     */
    private BigDecimal minOtherComprehensiveIncome;

    /**
     * 最大其他综合收益
     */
    private BigDecimal maxOtherComprehensiveIncome;

    /**
     * 最小综合收益总额
     */
    private BigDecimal minTotalComprehensiveIncome;

    /**
     * 最大综合收益总额
     */
    private BigDecimal maxTotalComprehensiveIncome;

    // ==================== 财务比率范围查询 ====================

    /**
     * 最小毛利率
     */
    private BigDecimal minGrossProfitMargin;

    /**
     * 最大毛利率
     */
    private BigDecimal maxGrossProfitMargin;

    /**
     * 最小营业利润率
     */
    private BigDecimal minOperatingProfitMargin;

    /**
     * 最大营业利润率
     */
    private BigDecimal maxOperatingProfitMargin;

    /**
     * 最小净利润率
     */
    private BigDecimal minNetProfitMargin;

    /**
     * 最大净利润率
     */
    private BigDecimal maxNetProfitMargin;

    /**
     * 最小期间费用率
     */
    private BigDecimal minPeriodExpenseRatio;

    /**
     * 最大期间费用率
     */
    private BigDecimal maxPeriodExpenseRatio;

    /**
     * 最小销售费用率
     */
    private BigDecimal minSellingExpenseRatio;

    /**
     * 最大销售费用率
     */
    private BigDecimal maxSellingExpenseRatio;

    /**
     * 最小管理费用率
     */
    private BigDecimal minAdministrativeExpenseRatio;

    /**
     * 最大管理费用率
     */
    private BigDecimal maxAdministrativeExpenseRatio;

    /**
     * 最小财务费用率
     */
    private BigDecimal minFinancialExpenseRatio;

    /**
     * 最大财务费用率
     */
    private BigDecimal maxFinancialExpenseRatio;

    /**
     * 最小研发费用率
     */
    private BigDecimal minRdExpenseRatio;

    /**
     * 最大研发费用率
     */
    private BigDecimal maxRdExpenseRatio;

    /**
     * 最小所得税率
     */
    private BigDecimal minEffectiveTaxRate;

    /**
     * 最大所得税率
     */
    private BigDecimal maxEffectiveTaxRate;

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
     * 开始审核日期
     */
    private String startAuditDate;

    /**
     * 结束审核日期
     */
    private String endAuditDate;

    // ==================== 其他查询条件 ====================

    /**
     * 审核人
     */
    private String auditedBy;

    /**
     * 关键字搜索
     */
    private String keyword;

}
