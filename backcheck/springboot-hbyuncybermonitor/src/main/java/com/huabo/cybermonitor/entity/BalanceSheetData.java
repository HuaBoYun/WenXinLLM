package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产负债表数据实体类
 * 用于资产负债表数据存储和分析
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BALANCE_SHEET_DATA")
public class BalanceSheetData {

    /**
     * 资产负债表数据ID（主键）
     */
    @TableId(value = "BALANCE_SHEET_ID", type = IdType.ASSIGN_UUID)
    private String balanceSheetId;

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

    // ==================== 资产类科目 ====================

    /**
     * 流动资产合计
     */
    private BigDecimal totalCurrentAssets;

    /**
     * 货币资金
     */
    private BigDecimal cashAndCashEquivalents;

    /**
     * 交易性金融资产
     */
    private BigDecimal tradingFinancialAssets;

    /**
     * 衍生金融资产
     */
    private BigDecimal derivativeFinancialAssets;

    /**
     * 应收票据
     */
    private BigDecimal notesReceivable;

    /**
     * 应收账款
     */
    private BigDecimal accountsReceivable;

    /**
     * 应收款项融资
     */
    private BigDecimal receivablesFinancing;

    /**
     * 预付款项
     */
    private BigDecimal prepayments;

    /**
     * 其他应收款
     */
    private BigDecimal otherReceivables;

    /**
     * 存货
     */
    private BigDecimal inventories;

    /**
     * 合同资产
     */
    private BigDecimal contractAssets;

    /**
     * 持有待售资产
     */
    private BigDecimal assetsHeldForSale;

    /**
     * 一年内到期的非流动资产
     */
    private BigDecimal nonCurrentAssetsDueWithinOneYear;

    /**
     * 其他流动资产
     */
    private BigDecimal otherCurrentAssets;

    /**
     * 非流动资产合计
     */
    private BigDecimal totalNonCurrentAssets;

    /**
     * 债权投资
     */
    private BigDecimal debtInvestments;

    /**
     * 其他债权投资
     */
    private BigDecimal otherDebtInvestments;

    /**
     * 长期应收款
     */
    private BigDecimal longTermReceivables;

    /**
     * 长期股权投资
     */
    private BigDecimal longTermEquityInvestments;

    /**
     * 其他权益工具投资
     */
    private BigDecimal otherEquityInstrumentInvestments;

    /**
     * 其他非流动金融资产
     */
    private BigDecimal otherNonCurrentFinancialAssets;

    /**
     * 投资性房地产
     */
    private BigDecimal investmentProperties;

    /**
     * 固定资产
     */
    private BigDecimal fixedAssets;

    /**
     * 在建工程
     */
    private BigDecimal constructionInProgress;

    /**
     * 生产性生物资产
     */
    private BigDecimal productiveBiologicalAssets;

    /**
     * 油气资产
     */
    private BigDecimal oilAndGasAssets;

    /**
     * 使用权资产
     */
    private BigDecimal rightOfUseAssets;

    /**
     * 无形资产
     */
    private BigDecimal intangibleAssets;

    /**
     * 开发支出
     */
    private BigDecimal developmentExpenditures;

    /**
     * 商誉
     */
    private BigDecimal goodwill;

    /**
     * 长期待摊费用
     */
    private BigDecimal longTermPrepaidExpenses;

    /**
     * 递延所得税资产
     */
    private BigDecimal deferredTaxAssets;

    /**
     * 其他非流动资产
     */
    private BigDecimal otherNonCurrentAssets;

    /**
     * 资产总计
     */
    private BigDecimal totalAssets;

    // ==================== 负债类科目 ====================

    /**
     * 流动负债合计
     */
    private BigDecimal totalCurrentLiabilities;

    /**
     * 短期借款
     */
    private BigDecimal shortTermBorrowings;

    /**
     * 交易性金融负债
     */
    private BigDecimal tradingFinancialLiabilities;

    /**
     * 衍生金融负债
     */
    private BigDecimal derivativeFinancialLiabilities;

    /**
     * 应付票据
     */
    private BigDecimal notesPayable;

    /**
     * 应付账款
     */
    private BigDecimal accountsPayable;

    /**
     * 预收款项
     */
    private BigDecimal advancesFromCustomers;

    /**
     * 合同负债
     */
    private BigDecimal contractLiabilities;

    /**
     * 应付职工薪酬
     */
    private BigDecimal employeeBenefitsPayable;

    /**
     * 应交税费
     */
    private BigDecimal taxesPayable;

    /**
     * 其他应付款
     */
    private BigDecimal otherPayables;

    /**
     * 持有待售负债
     */
    private BigDecimal liabilitiesHeldForSale;

    /**
     * 一年内到期的非流动负债
     */
    private BigDecimal nonCurrentLiabilitiesDueWithinOneYear;

    /**
     * 其他流动负债
     */
    private BigDecimal otherCurrentLiabilities;

    /**
     * 非流动负债合计
     */
    private BigDecimal totalNonCurrentLiabilities;

    /**
     * 长期借款
     */
    private BigDecimal longTermBorrowings;

    /**
     * 应付债券
     */
    private BigDecimal bondsPayable;

    /**
     * 租赁负债
     */
    private BigDecimal leaseLiabilities;

    /**
     * 长期应付款
     */
    private BigDecimal longTermPayables;

    /**
     * 长期应付职工薪酬
     */
    private BigDecimal longTermEmployeeBenefitsPayable;

    /**
     * 预计负债
     */
    private BigDecimal provisionsForLiabilities;

    /**
     * 递延收益
     */
    private BigDecimal deferredRevenue;

    /**
     * 递延所得税负债
     */
    private BigDecimal deferredTaxLiabilities;

    /**
     * 其他非流动负债
     */
    private BigDecimal otherNonCurrentLiabilities;

    /**
     * 负债合计
     */
    private BigDecimal totalLiabilities;

    // ==================== 所有者权益类科目 ====================

    /**
     * 所有者权益合计
     */
    private BigDecimal totalOwnersEquity;

    /**
     * 实收资本（股本）
     */
    private BigDecimal paidInCapital;

    /**
     * 其他权益工具
     */
    private BigDecimal otherEquityInstruments;

    /**
     * 资本公积
     */
    private BigDecimal capitalReserve;

    /**
     * 减：库存股
     */
    private BigDecimal treasuryStock;

    /**
     * 其他综合收益
     */
    private BigDecimal otherComprehensiveIncome;

    /**
     * 专项储备
     */
    private BigDecimal specialReserves;

    /**
     * 盈余公积
     */
    private BigDecimal surplusReserve;

    /**
     * 未分配利润
     */
    private BigDecimal retainedEarnings;

    /**
     * 归属于母公司所有者权益合计
     */
    private BigDecimal totalEquityAttributableToParent;

    /**
     * 少数股东权益
     */
    private BigDecimal minorityInterests;

    /**
     * 负债和所有者权益总计
     */
    private BigDecimal totalLiabilitiesAndOwnersEquity;

    // ==================== 分析指标 ====================

    /**
     * 资产负债率
     */
    private BigDecimal assetLiabilityRatio;

    /**
     * 流动比率
     */
    private BigDecimal currentRatio;

    /**
     * 速动比率
     */
    private BigDecimal quickRatio;

    /**
     * 权益乘数
     */
    private BigDecimal equityMultiplier;

    /**
     * 产权比率
     */
    private BigDecimal debtToEquityRatio;

    /**
     * 有形净值债务率
     */
    private BigDecimal tangibleNetWorthDebtRatio;

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
