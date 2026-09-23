package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产负债表数据查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BalanceSheetDataQueryVO extends BaseVo {

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

    // ==================== 资产范围查询 ====================

    /**
     * 最小总资产
     */
    private BigDecimal minTotalAssets;

    /**
     * 最大总资产
     */
    private BigDecimal maxTotalAssets;

    /**
     * 最小流动资产
     */
    private BigDecimal minTotalCurrentAssets;

    /**
     * 最大流动资产
     */
    private BigDecimal maxTotalCurrentAssets;

    /**
     * 最小非流动资产
     */
    private BigDecimal minTotalNonCurrentAssets;

    /**
     * 最大非流动资产
     */
    private BigDecimal maxTotalNonCurrentAssets;

    /**
     * 最小货币资金
     */
    private BigDecimal minCashAndCashEquivalents;

    /**
     * 最大货币资金
     */
    private BigDecimal maxCashAndCashEquivalents;

    /**
     * 最小应收账款
     */
    private BigDecimal minAccountsReceivable;

    /**
     * 最大应收账款
     */
    private BigDecimal maxAccountsReceivable;

    /**
     * 最小存货
     */
    private BigDecimal minInventories;

    /**
     * 最大存货
     */
    private BigDecimal maxInventories;

    /**
     * 最小固定资产
     */
    private BigDecimal minFixedAssets;

    /**
     * 最大固定资产
     */
    private BigDecimal maxFixedAssets;

    /**
     * 最小无形资产
     */
    private BigDecimal minIntangibleAssets;

    /**
     * 最大无形资产
     */
    private BigDecimal maxIntangibleAssets;

    /**
     * 最小商誉
     */
    private BigDecimal minGoodwill;

    /**
     * 最大商誉
     */
    private BigDecimal maxGoodwill;

    // ==================== 负债范围查询 ====================

    /**
     * 最小总负债
     */
    private BigDecimal minTotalLiabilities;

    /**
     * 最大总负债
     */
    private BigDecimal maxTotalLiabilities;

    /**
     * 最小流动负债
     */
    private BigDecimal minTotalCurrentLiabilities;

    /**
     * 最大流动负债
     */
    private BigDecimal maxTotalCurrentLiabilities;

    /**
     * 最小非流动负债
     */
    private BigDecimal minTotalNonCurrentLiabilities;

    /**
     * 最大非流动负债
     */
    private BigDecimal maxTotalNonCurrentLiabilities;

    /**
     * 最小短期借款
     */
    private BigDecimal minShortTermBorrowings;

    /**
     * 最大短期借款
     */
    private BigDecimal maxShortTermBorrowings;

    /**
     * 最小应付账款
     */
    private BigDecimal minAccountsPayable;

    /**
     * 最大应付账款
     */
    private BigDecimal maxAccountsPayable;

    /**
     * 最小长期借款
     */
    private BigDecimal minLongTermBorrowings;

    /**
     * 最大长期借款
     */
    private BigDecimal maxLongTermBorrowings;

    /**
     * 最小应付债券
     */
    private BigDecimal minBondsPayable;

    /**
     * 最大应付债券
     */
    private BigDecimal maxBondsPayable;

    // ==================== 所有者权益范围查询 ====================

    /**
     * 最小所有者权益总额
     */
    private BigDecimal minTotalOwnersEquity;

    /**
     * 最大所有者权益总额
     */
    private BigDecimal maxTotalOwnersEquity;

    /**
     * 最小实收资本
     */
    private BigDecimal minPaidInCapital;

    /**
     * 最大实收资本
     */
    private BigDecimal maxPaidInCapital;

    /**
     * 最小资本公积
     */
    private BigDecimal minCapitalReserve;

    /**
     * 最大资本公积
     */
    private BigDecimal maxCapitalReserve;

    /**
     * 最小盈余公积
     */
    private BigDecimal minSurplusReserve;

    /**
     * 最大盈余公积
     */
    private BigDecimal maxSurplusReserve;

    /**
     * 最小未分配利润
     */
    private BigDecimal minRetainedEarnings;

    /**
     * 最大未分配利润
     */
    private BigDecimal maxRetainedEarnings;

    /**
     * 最小少数股东权益
     */
    private BigDecimal minMinorityInterests;

    /**
     * 最大少数股东权益
     */
    private BigDecimal maxMinorityInterests;

    // ==================== 财务比率范围查询 ====================

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
     * 最小权益乘数
     */
    private BigDecimal minEquityMultiplier;

    /**
     * 最大权益乘数
     */
    private BigDecimal maxEquityMultiplier;

    /**
     * 最小产权比率
     */
    private BigDecimal minDebtToEquityRatio;

    /**
     * 最大产权比率
     */
    private BigDecimal maxDebtToEquityRatio;

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
