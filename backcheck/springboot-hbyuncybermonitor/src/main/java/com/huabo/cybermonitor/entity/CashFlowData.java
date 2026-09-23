package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 现金流量表数据实体类
 * 用于现金流量表数据存储和分析
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("CASH_FLOW_DATA")
public class CashFlowData {

    /**
     * 现金流量表数据ID（主键）
     */
    @TableId(value = "CASH_FLOW_ID", type = IdType.ASSIGN_UUID)
    private String cashFlowId;

    /**
     * 财务报表ID（外键）
     */
    private String statementId;

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

    // ==================== 经营活动现金流量 ====================

    /**
     * 销售商品、提供劳务收到的现金
     */
    private BigDecimal cashReceivedFromSalesOfGoodsAndServices;

    /**
     * 客户存款和同业存放款项净增加额
     */
    private BigDecimal netIncreaseInCustomerDepositsAndInterbankDeposits;

    /**
     * 向中央银行借款净增加额
     */
    private BigDecimal netIncreaseInBorrowingsFromCentralBank;

    /**
     * 向其他金融机构拆入资金净增加额
     */
    private BigDecimal netIncreaseInBorrowingsFromOtherFinancialInstitutions;

    /**
     * 收到原保险合同保费取得的现金
     */
    private BigDecimal cashReceivedFromOriginalInsurancePremiums;

    /**
     * 收到再保业务现金净额
     */
    private BigDecimal netCashReceivedFromReinsuranceBusiness;

    /**
     * 保户储金及投资款净增加额
     */
    private BigDecimal netIncreaseInPolicyholderDepositsAndInvestments;

    /**
     * 收取利息、手续费及佣金的现金
     */
    private BigDecimal cashReceivedFromInterestFeesAndCommissions;

    /**
     * 拆入资金净增加额
     */
    private BigDecimal netIncreaseInBorrowedFunds;

    /**
     * 回购业务资金净增加额
     */
    private BigDecimal netIncreaseInRepurchaseBusinessFunds;

    /**
     * 代理买卖证券收到的现金净额
     */
    private BigDecimal netCashReceivedFromAgencyTradingSecurities;

    /**
     * 收到的税费返还
     */
    private BigDecimal taxRefundsReceived;

    /**
     * 收到其他与经营活动有关的现金
     */
    private BigDecimal otherCashReceivedFromOperatingActivities;

    /**
     * 经营活动现金流入小计
     */
    private BigDecimal totalCashInflowsFromOperatingActivities;

    /**
     * 购买商品、接受劳务支付的现金
     */
    private BigDecimal cashPaidForGoodsAndServices;

    /**
     * 客户贷款及垫款净增加额
     */
    private BigDecimal netIncreaseInCustomerLoansAndAdvances;

    /**
     * 存放中央银行和同业款项净增加额
     */
    private BigDecimal netIncreaseInDepositsWithCentralBankAndOtherBanks;

    /**
     * 支付原保险合同赔付款项的现金
     */
    private BigDecimal cashPaidForOriginalInsuranceClaims;

    /**
     * 拆出资金净增加额
     */
    private BigDecimal netIncreaseInLentFunds;

    /**
     * 支付利息、手续费及佣金的现金
     */
    private BigDecimal cashPaidForInterestFeesAndCommissions;

    /**
     * 支付保单红利的现金
     */
    private BigDecimal cashPaidForPolicyDividends;

    /**
     * 支付给职工以及为职工支付的现金
     */
    private BigDecimal cashPaidToAndOnBehalfOfEmployees;

    /**
     * 支付的各项税费
     */
    private BigDecimal taxesPaid;

    /**
     * 支付其他与经营活动有关的现金
     */
    private BigDecimal otherCashPaidForOperatingActivities;

    /**
     * 经营活动现金流出小计
     */
    private BigDecimal totalCashOutflowsFromOperatingActivities;

    /**
     * 经营活动产生的现金流量净额
     */
    private BigDecimal netCashFlowFromOperatingActivities;

    // ==================== 投资活动现金流量 ====================

    /**
     * 收回投资收到的现金
     */
    private BigDecimal cashReceivedFromDisposalOfInvestments;

    /**
     * 取得投资收益收到的现金
     */
    private BigDecimal cashReceivedFromInvestmentIncome;

    /**
     * 处置固定资产、无形资产和其他长期资产收回的现金净额
     */
    private BigDecimal netCashReceivedFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;

    /**
     * 处置子公司及其他营业单位收到的现金净额
     */
    private BigDecimal netCashReceivedFromDisposalOfSubsidiariesAndOtherBusinessUnits;

    /**
     * 收到其他与投资活动有关的现金
     */
    private BigDecimal otherCashReceivedFromInvestingActivities;

    /**
     * 投资活动现金流入小计
     */
    private BigDecimal totalCashInflowsFromInvestingActivities;

    /**
     * 购建固定资产、无形资产和其他长期资产支付的现金
     */
    private BigDecimal cashPaidForPurchaseAndConstructionOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;

    /**
     * 投资支付的现金
     */
    private BigDecimal cashPaidForInvestments;

    /**
     * 质押贷款净增加额
     */
    private BigDecimal netIncreaseInPledgedLoans;

    /**
     * 取得子公司及其他营业单位支付的现金净额
     */
    private BigDecimal netCashPaidForAcquisitionOfSubsidiariesAndOtherBusinessUnits;

    /**
     * 支付其他与投资活动有关的现金
     */
    private BigDecimal otherCashPaidForInvestingActivities;

    /**
     * 投资活动现金流出小计
     */
    private BigDecimal totalCashOutflowsFromInvestingActivities;

    /**
     * 投资活动产生的现金流量净额
     */
    private BigDecimal netCashFlowFromInvestingActivities;

    // ==================== 筹资活动现金流量 ====================

    /**
     * 吸收投资收到的现金
     */
    private BigDecimal cashReceivedFromCapitalContributions;

    /**
     * 其中：子公司吸收少数股东投资收到的现金
     */
    private BigDecimal cashReceivedFromMinorityShareholdersOfSubsidiaries;

    /**
     * 取得借款收到的现金
     */
    private BigDecimal cashReceivedFromBorrowings;

    /**
     * 收到其他与筹资活动有关的现金
     */
    private BigDecimal otherCashReceivedFromFinancingActivities;

    /**
     * 筹资活动现金流入小计
     */
    private BigDecimal totalCashInflowsFromFinancingActivities;

    /**
     * 偿还债务支付的现金
     */
    private BigDecimal cashPaidForRepaymentOfBorrowings;

    /**
     * 分配股利、利润或偿付利息支付的现金
     */
    private BigDecimal cashPaidForDividendsProfitDistributionOrInterestPayments;

    /**
     * 其中：子公司支付给少数股东的股利、利润
     */
    private BigDecimal dividendsAndProfitsPaidToMinorityShareholdersOfSubsidiaries;

    /**
     * 支付其他与筹资活动有关的现金
     */
    private BigDecimal otherCashPaidForFinancingActivities;

    /**
     * 筹资活动现金流出小计
     */
    private BigDecimal totalCashOutflowsFromFinancingActivities;

    /**
     * 筹资活动产生的现金流量净额
     */
    private BigDecimal netCashFlowFromFinancingActivities;

    // ==================== 汇率变动及现金净变动 ====================

    /**
     * 汇率变动对现金及现金等价物的影响
     */
    private BigDecimal effectOfForeignExchangeRateChangesOnCashAndCashEquivalents;

    /**
     * 现金及现金等价物净增加额
     */
    private BigDecimal netIncreaseInCashAndCashEquivalents;

    /**
     * 加：期初现金及现金等价物余额
     */
    private BigDecimal cashAndCashEquivalentsAtBeginningOfPeriod;

    /**
     * 期末现金及现金等价物余额
     */
    private BigDecimal cashAndCashEquivalentsAtEndOfPeriod;

    // ==================== 分析指标 ====================

    /**
     * 经营活动现金流量净额/营业收入
     */
    private BigDecimal operatingCashFlowToRevenue;

    /**
     * 经营活动现金流量净额/净利润
     */
    private BigDecimal operatingCashFlowToNetProfit;

    /**
     * 现金流量比率
     */
    private BigDecimal cashFlowRatio;

    /**
     * 现金债务总额比
     */
    private BigDecimal cashToTotalDebtRatio;

    /**
     * 现金再投资比率
     */
    private BigDecimal cashReinvestmentRatio;

    /**
     * 现金流量充足率
     */
    private BigDecimal cashFlowAdequacyRatio;

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

    /**
     * 审核人
     */
    private String auditedBy;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除标识
     */
    private Boolean deleted;

    // ==================== 常量定义 ====================

    /**
     * 数据状态常量
     */
    public static final String DATA_STATUS_DRAFT = "DRAFT";                              // 草稿
    public static final String DATA_STATUS_SUBMITTED = "SUBMITTED";                      // 已提交
    public static final String DATA_STATUS_APPROVED = "APPROVED";                        // 已审批
    public static final String DATA_STATUS_PUBLISHED = "PUBLISHED";                      // 已发布
    public static final String DATA_STATUS_CONFIRMED = "CONFIRMED";                      // 已确认
    public static final String DATA_STATUS_AUDITED = "AUDITED";                          // 已审计

    /**
     * 数据来源常量
     */
    public static final String DATA_SOURCE_MANUAL = "MANUAL";                            // 手工录入
    public static final String DATA_SOURCE_IMPORT = "IMPORT";                            // 导入
    public static final String DATA_SOURCE_INTERFACE = "INTERFACE";                      // 接口获取
    public static final String DATA_SOURCE_SYSTEM = "SYSTEM";                            // 系统生成

    /**
     * 审核状态常量
     */
    public static final String AUDIT_STATUS_PENDING = "PENDING";                         // 待审核
    public static final String AUDIT_STATUS_APPROVED = "APPROVED";                       // 已审核
    public static final String AUDIT_STATUS_REJECTED = "REJECTED";                       // 已拒绝
    public static final String AUDIT_STATUS_NOT_AUDITED = "NOT_AUDITED";                 // 未审计
    public static final String AUDIT_STATUS_IN_PROGRESS = "IN_PROGRESS";                 // 审计中
    public static final String AUDIT_STATUS_COMPLETED = "COMPLETED";                     // 审计完成

}
