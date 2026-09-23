package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 利润表数据实体类
 * 用于利润表数据存储和分析
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("INCOME_STATEMENT_DATA")
public class IncomeStatementData {

    /**
     * 利润表数据ID（主键）
     */
    @TableId(value = "INCOME_STATEMENT_ID", type = IdType.ASSIGN_UUID)
    private String incomeStatementId;

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

    // ==================== 收入类科目 ====================

    /**
     * 营业收入
     */
    private BigDecimal operatingRevenue;

    /**
     * 减：营业成本
     */
    private BigDecimal operatingCosts;

    /**
     * 税金及附加
     */
    private BigDecimal taxesAndSurcharges;

    /**
     * 销售费用
     */
    private BigDecimal sellingExpenses;

    /**
     * 管理费用
     */
    private BigDecimal administrativeExpenses;

    /**
     * 研发费用
     */
    private BigDecimal researchAndDevelopmentExpenses;

    /**
     * 财务费用
     */
    private BigDecimal financialExpenses;

    /**
     * 其中：利息费用
     */
    private BigDecimal interestExpenses;

    /**
     * 利息收入
     */
    private BigDecimal interestIncome;

    /**
     * 加：其他收益
     */
    private BigDecimal otherIncome;

    /**
     * 投资收益
     */
    private BigDecimal investmentIncome;

    /**
     * 其中：对联营企业和合营企业的投资收益
     */
    private BigDecimal investmentIncomeFromAssociatesAndJointVentures;

    /**
     * 以摊余成本计量的金融资产终止确认收益
     */
    private BigDecimal gainOnDerecognitionOfFinancialAssetsAtAmortizedCost;

    /**
     * 汇兑收益
     */
    private BigDecimal foreignExchangeGains;

    /**
     * 净敞口套期收益
     */
    private BigDecimal netHedgingGains;

    /**
     * 公允价值变动收益
     */
    private BigDecimal fairValueChangeGains;

    /**
     * 信用减值损失
     */
    private BigDecimal creditImpairmentLosses;

    /**
     * 资产减值损失
     */
    private BigDecimal assetImpairmentLosses;

    /**
     * 资产处置收益
     */
    private BigDecimal assetDisposalGains;

    // ==================== 利润类科目 ====================

    /**
     * 营业利润
     */
    private BigDecimal operatingProfit;

    /**
     * 加：营业外收入
     */
    private BigDecimal nonOperatingIncome;

    /**
     * 减：营业外支出
     */
    private BigDecimal nonOperatingExpenses;

    /**
     * 利润总额
     */
    private BigDecimal totalProfit;

    /**
     * 减：所得税费用
     */
    private BigDecimal incomeTaxExpenses;

    /**
     * 净利润
     */
    private BigDecimal netProfit;

    /**
     * （一）按经营持续性分类
     */
    private String profitClassificationByContinuity;

    /**
     * 1.持续经营净利润
     */
    private BigDecimal continuingOperationsNetProfit;

    /**
     * 2.终止经营净利润
     */
    private BigDecimal discontinuedOperationsNetProfit;

    /**
     * （二）按所有权归属分类
     */
    private String profitClassificationByOwnership;

    /**
     * 1.归属于母公司所有者的净利润
     */
    private BigDecimal netProfitAttributableToParent;

    /**
     * 2.少数股东损益
     */
    private BigDecimal minorityInterestIncome;

    // ==================== 其他综合收益 ====================

    /**
     * 其他综合收益
     */
    private BigDecimal otherComprehensiveIncome;

    /**
     * 归属母公司所有者的其他综合收益
     */
    private BigDecimal otherComprehensiveIncomeAttributableToParent;

    /**
     * （一）不能重分类进损益的其他综合收益
     */
    private BigDecimal otherComprehensiveIncomeNotReclassified;

    /**
     * 1.重新计量设定受益计划变动额
     */
    private BigDecimal remeasurementOfDefinedBenefitPlans;

    /**
     * 2.权益法下不能转损益的其他综合收益
     */
    private BigDecimal equityMethodOtherComprehensiveIncomeNotReclassified;

    /**
     * 3.其他权益工具投资公允价值变动
     */
    private BigDecimal fairValueChangeOfOtherEquityInstruments;

    /**
     * 4.企业自身信用风险公允价值变动
     */
    private BigDecimal fairValueChangeOfOwnCreditRisk;

    /**
     * （二）将重分类进损益的其他综合收益
     */
    private BigDecimal otherComprehensiveIncomeToBeReclassified;

    /**
     * 1.权益法下可转损益的其他综合收益
     */
    private BigDecimal equityMethodOtherComprehensiveIncomeToBeReclassified;

    /**
     * 2.其他债权投资公允价值变动
     */
    private BigDecimal fairValueChangeOfOtherDebtInvestments;

    /**
     * 3.金融资产重分类计入其他综合收益的金额
     */
    private BigDecimal financialAssetReclassificationToOtherComprehensiveIncome;

    /**
     * 4.其他债权投资信用减值准备
     */
    private BigDecimal creditImpairmentOfOtherDebtInvestments;

    /**
     * 5.现金流量套期储备
     */
    private BigDecimal cashFlowHedgeReserve;

    /**
     * 6.外币财务报表折算差额
     */
    private BigDecimal foreignCurrencyTranslationDifferences;

    /**
     * 归属于少数股东的其他综合收益
     */
    private BigDecimal otherComprehensiveIncomeAttributableToMinority;

    /**
     * 综合收益总额
     */
    private BigDecimal totalComprehensiveIncome;

    /**
     * 归属于母公司所有者的综合收益总额
     */
    private BigDecimal totalComprehensiveIncomeAttributableToParent;

    /**
     * 归属于少数股东的综合收益总额
     */
    private BigDecimal totalComprehensiveIncomeAttributableToMinority;

    // ==================== 每股收益 ====================

    /**
     * 基本每股收益
     */
    private BigDecimal basicEarningsPerShare;

    /**
     * 稀释每股收益
     */
    private BigDecimal dilutedEarningsPerShare;

    // ==================== 分析指标 ====================

    /**
     * 毛利率
     */
    private BigDecimal grossProfitMargin;

    /**
     * 营业利润率
     */
    private BigDecimal operatingProfitMargin;

    /**
     * 净利润率
     */
    private BigDecimal netProfitMargin;

    /**
     * 期间费用率
     */
    private BigDecimal periodExpenseRatio;

    /**
     * 销售费用率
     */
    private BigDecimal sellingExpenseRatio;

    /**
     * 管理费用率
     */
    private BigDecimal administrativeExpenseRatio;

    /**
     * 财务费用率
     */
    private BigDecimal financialExpenseRatio;

    /**
     * 研发费用率
     */
    private BigDecimal rdExpenseRatio;

    /**
     * 所得税率
     */
    private BigDecimal effectiveTaxRate;

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

}
