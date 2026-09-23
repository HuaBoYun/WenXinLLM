package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 现金流量表数据查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CashFlowDataQueryVO extends BaseVo {

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

    // ==================== 经营活动现金流量范围查询 ====================

    /**
     * 最小销售商品、提供劳务收到的现金
     */
    private BigDecimal minCashReceivedFromSalesOfGoodsAndServices;

    /**
     * 最大销售商品、提供劳务收到的现金
     */
    private BigDecimal maxCashReceivedFromSalesOfGoodsAndServices;

    /**
     * 最小收到的税费返还
     */
    private BigDecimal minTaxRefundsReceived;

    /**
     * 最大收到的税费返还
     */
    private BigDecimal maxTaxRefundsReceived;

    /**
     * 最小收到其他与经营活动有关的现金
     */
    private BigDecimal minOtherCashReceivedFromOperatingActivities;

    /**
     * 最大收到其他与经营活动有关的现金
     */
    private BigDecimal maxOtherCashReceivedFromOperatingActivities;

    /**
     * 最小经营活动现金流入小计
     */
    private BigDecimal minTotalCashInflowsFromOperatingActivities;

    /**
     * 最大经营活动现金流入小计
     */
    private BigDecimal maxTotalCashInflowsFromOperatingActivities;

    /**
     * 最小购买商品、接受劳务支付的现金
     */
    private BigDecimal minCashPaidForGoodsAndServices;

    /**
     * 最大购买商品、接受劳务支付的现金
     */
    private BigDecimal maxCashPaidForGoodsAndServices;

    /**
     * 最小支付给职工以及为职工支付的现金
     */
    private BigDecimal minCashPaidToAndOnBehalfOfEmployees;

    /**
     * 最大支付给职工以及为职工支付的现金
     */
    private BigDecimal maxCashPaidToAndOnBehalfOfEmployees;

    /**
     * 最小支付的各项税费
     */
    private BigDecimal minTaxesPaid;

    /**
     * 最大支付的各项税费
     */
    private BigDecimal maxTaxesPaid;

    /**
     * 最小支付其他与经营活动有关的现金
     */
    private BigDecimal minOtherCashPaidForOperatingActivities;

    /**
     * 最大支付其他与经营活动有关的现金
     */
    private BigDecimal maxOtherCashPaidForOperatingActivities;

    /**
     * 最小经营活动现金流出小计
     */
    private BigDecimal minTotalCashOutflowsFromOperatingActivities;

    /**
     * 最大经营活动现金流出小计
     */
    private BigDecimal maxTotalCashOutflowsFromOperatingActivities;

    /**
     * 最小经营活动产生的现金流量净额
     */
    private BigDecimal minNetCashFlowFromOperatingActivities;

    /**
     * 最大经营活动产生的现金流量净额
     */
    private BigDecimal maxNetCashFlowFromOperatingActivities;

    // ==================== 投资活动现金流量范围查询 ====================

    /**
     * 最小收回投资收到的现金
     */
    private BigDecimal minCashReceivedFromDisposalOfInvestments;

    /**
     * 最大收回投资收到的现金
     */
    private BigDecimal maxCashReceivedFromDisposalOfInvestments;

    /**
     * 最小取得投资收益收到的现金
     */
    private BigDecimal minCashReceivedFromInvestmentIncome;

    /**
     * 最大取得投资收益收到的现金
     */
    private BigDecimal maxCashReceivedFromInvestmentIncome;

    /**
     * 最小处置固定资产、无形资产和其他长期资产收回的现金净额
     */
    private BigDecimal minNetCashReceivedFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;

    /**
     * 最大处置固定资产、无形资产和其他长期资产收回的现金净额
     */
    private BigDecimal maxNetCashReceivedFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;

    /**
     * 最小投资活动现金流入小计
     */
    private BigDecimal minTotalCashInflowsFromInvestingActivities;

    /**
     * 最大投资活动现金流入小计
     */
    private BigDecimal maxTotalCashInflowsFromInvestingActivities;

    /**
     * 最小购建固定资产、无形资产和其他长期资产支付的现金
     */
    private BigDecimal minCashPaidForPurchaseAndConstructionOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;

    /**
     * 最大购建固定资产、无形资产和其他长期资产支付的现金
     */
    private BigDecimal maxCashPaidForPurchaseAndConstructionOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;

    /**
     * 最小投资支付的现金
     */
    private BigDecimal minCashPaidForInvestments;

    /**
     * 最大投资支付的现金
     */
    private BigDecimal maxCashPaidForInvestments;

    /**
     * 最小投资活动现金流出小计
     */
    private BigDecimal minTotalCashOutflowsFromInvestingActivities;

    /**
     * 最大投资活动现金流出小计
     */
    private BigDecimal maxTotalCashOutflowsFromInvestingActivities;

    /**
     * 最小投资活动产生的现金流量净额
     */
    private BigDecimal minNetCashFlowFromInvestingActivities;

    /**
     * 最大投资活动产生的现金流量净额
     */
    private BigDecimal maxNetCashFlowFromInvestingActivities;

    // ==================== 筹资活动现金流量范围查询 ====================

    /**
     * 最小吸收投资收到的现金
     */
    private BigDecimal minCashReceivedFromCapitalContributions;

    /**
     * 最大吸收投资收到的现金
     */
    private BigDecimal maxCashReceivedFromCapitalContributions;

    /**
     * 最小取得借款收到的现金
     */
    private BigDecimal minCashReceivedFromBorrowings;

    /**
     * 最大取得借款收到的现金
     */
    private BigDecimal maxCashReceivedFromBorrowings;

    /**
     * 最小筹资活动现金流入小计
     */
    private BigDecimal minTotalCashInflowsFromFinancingActivities;

    /**
     * 最大筹资活动现金流入小计
     */
    private BigDecimal maxTotalCashInflowsFromFinancingActivities;

    /**
     * 最小偿还债务支付的现金
     */
    private BigDecimal minCashPaidForRepaymentOfBorrowings;

    /**
     * 最大偿还债务支付的现金
     */
    private BigDecimal maxCashPaidForRepaymentOfBorrowings;

    /**
     * 最小分配股利、利润或偿付利息支付的现金
     */
    private BigDecimal minCashPaidForDividendsProfitDistributionOrInterestPayments;

    /**
     * 最大分配股利、利润或偿付利息支付的现金
     */
    private BigDecimal maxCashPaidForDividendsProfitDistributionOrInterestPayments;

    /**
     * 最小筹资活动现金流出小计
     */
    private BigDecimal minTotalCashOutflowsFromFinancingActivities;

    /**
     * 最大筹资活动现金流出小计
     */
    private BigDecimal maxTotalCashOutflowsFromFinancingActivities;

    /**
     * 最小筹资活动产生的现金流量净额
     */
    private BigDecimal minNetCashFlowFromFinancingActivities;

    /**
     * 最大筹资活动产生的现金流量净额
     */
    private BigDecimal maxNetCashFlowFromFinancingActivities;

    // ==================== 现金净变动范围查询 ====================

    /**
     * 最小现金及现金等价物净增加额
     */
    private BigDecimal minNetIncreaseInCashAndCashEquivalents;

    /**
     * 最大现金及现金等价物净增加额
     */
    private BigDecimal maxNetIncreaseInCashAndCashEquivalents;

    /**
     * 最小期初现金及现金等价物余额
     */
    private BigDecimal minCashAndCashEquivalentsAtBeginningOfPeriod;

    /**
     * 最大期初现金及现金等价物余额
     */
    private BigDecimal maxCashAndCashEquivalentsAtBeginningOfPeriod;

    /**
     * 最小期末现金及现金等价物余额
     */
    private BigDecimal minCashAndCashEquivalentsAtEndOfPeriod;

    /**
     * 最大期末现金及现金等价物余额
     */
    private BigDecimal maxCashAndCashEquivalentsAtEndOfPeriod;

    // ==================== 现金流量比率范围查询 ====================

    /**
     * 最小经营活动现金流量净额/营业收入
     */
    private BigDecimal minOperatingCashFlowToRevenue;

    /**
     * 最大经营活动现金流量净额/营业收入
     */
    private BigDecimal maxOperatingCashFlowToRevenue;

    /**
     * 最小经营活动现金流量净额/净利润
     */
    private BigDecimal minOperatingCashFlowToNetProfit;

    /**
     * 最大经营活动现金流量净额/净利润
     */
    private BigDecimal maxOperatingCashFlowToNetProfit;

    /**
     * 最小现金流量比率
     */
    private BigDecimal minCashFlowRatio;

    /**
     * 最大现金流量比率
     */
    private BigDecimal maxCashFlowRatio;

    /**
     * 最小现金债务总额比
     */
    private BigDecimal minCashToTotalDebtRatio;

    /**
     * 最大现金债务总额比
     */
    private BigDecimal maxCashToTotalDebtRatio;

    /**
     * 最小现金再投资比率
     */
    private BigDecimal minCashReinvestmentRatio;

    /**
     * 最大现金再投资比率
     */
    private BigDecimal maxCashReinvestmentRatio;

    /**
     * 最小现金流量充足率
     */
    private BigDecimal minCashFlowAdequacyRatio;

    /**
     * 最大现金流量充足率
     */
    private BigDecimal maxCashFlowAdequacyRatio;

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
