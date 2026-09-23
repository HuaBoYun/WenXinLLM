package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 财务报表主表实体类
 * 用于财务数据穿透分析、财务风险识别、财务绩效评价、财务合规监管
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FINANCIAL_STATEMENT")
public class FinancialStatement {

    /**
     * 财务报表ID（主键）
     */
    @TableId(value = "STATEMENT_ID", type = IdType.ASSIGN_UUID)
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
     * 报表类型
     */
    private String statementType;

    /**
     * 报表期间类型
     */
    private String periodType;

    /**
     * 报表年度
     */
    private Integer reportYear;

    /**
     * 报表期间
     */
    private Integer reportPeriod;

    /**
     * 报表开始日期
     */
    private LocalDate startDate;

    /**
     * 报表结束日期
     */
    private LocalDate endDate;

    /**
     * 报表状态
     */
    private String statementStatus;

    /**
     * 审计状态
     */
    private String auditStatus;

    /**
     * 审计机构
     */
    private String auditFirm;

    /**
     * 审计意见
     */
    private String auditOpinion;

    /**
     * 审计日期
     */
    private LocalDate auditDate;

    /**
     * 编制单位
     */
    private String preparingUnit;

    /**
     * 编制人
     */
    private String preparedBy;

    /**
     * 编制日期
     */
    private LocalDate preparedDate;

    /**
     * 复核人
     */
    private String reviewedBy;

    /**
     * 复核日期
     */
    private LocalDate reviewedDate;

    /**
     * 批准人
     */
    private String approvedBy;

    /**
     * 批准日期
     */
    private LocalDate approvedDate;

    /**
     * 报送状态
     */
    private String submissionStatus;

    /**
     * 报送日期
     */
    private LocalDate submissionDate;

    /**
     * 报送机构
     */
    private String submissionOrganization;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据质量等级
     */
    private String dataQualityLevel;

    /**
     * 数据完整性评分
     */
    private BigDecimal dataCompletenessScore;

    /**
     * 数据准确性评分
     */
    private BigDecimal dataAccuracyScore;

    /**
     * 数据及时性评分
     */
    private BigDecimal dataTimelinessScore;

    /**
     * 数据一致性评分
     */
    private BigDecimal dataConsistencyScore;

    /**
     * 是否合并报表
     */
    private Boolean isConsolidated;

    /**
     * 合并范围说明
     */
    private String consolidationScope;

    /**
     * 会计准则
     */
    private String accountingStandards;

    /**
     * 货币单位
     */
    private String currency;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 重要会计政策变更
     */
    private String accountingPolicyChanges;

    /**
     * 重要会计估计变更
     */
    private String accountingEstimateChanges;

    /**
     * 前期差错更正
     */
    private String priorPeriodAdjustments;

    /**
     * 是否需要监管关注
     */
    private Boolean needRegulatoryAttention;

    /**
     * 监管关注原因
     */
    private String regulatoryAttentionReason;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 风险因素
     */
    private String riskFactors;

    /**
     * 异常标识
     */
    private Boolean hasAnomalies;

    /**
     * 异常描述
     */
    private String anomalyDescription;

    /**
     * 质量检查结果
     */
    private String qualityCheckResult;

    /**
     * 质量检查人
     */
    private String qualityCheckedBy;

    /**
     * 质量检查日期
     */
    private LocalDate qualityCheckDate;

    /**
     * 合规检查结果
     */
    private String complianceCheckResult;

    /**
     * 合规检查人
     */
    private String complianceCheckedBy;

    /**
     * 合规检查日期
     */
    private LocalDate complianceCheckDate;

    /**
     * 附件文件
     */
    private String attachmentFiles;

    /**
     * 备注说明
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
     * 报表类型常量
     */
    public static final String STATEMENT_TYPE_BALANCE_SHEET = "BALANCE_SHEET";           // 资产负债表
    public static final String STATEMENT_TYPE_INCOME_STATEMENT = "INCOME_STATEMENT";     // 利润表
    public static final String STATEMENT_TYPE_CASH_FLOW = "CASH_FLOW";                   // 现金流量表
    public static final String STATEMENT_TYPE_EQUITY_CHANGE = "EQUITY_CHANGE";           // 所有者权益变动表
    public static final String STATEMENT_TYPE_NOTES = "NOTES";                           // 财务报表附注
    public static final String STATEMENT_TYPE_CONSOLIDATED = "CONSOLIDATED";             // 合并报表

    /**
     * 报表期间类型常量
     */
    public static final String PERIOD_TYPE_ANNUAL = "ANNUAL";                            // 年报
    public static final String PERIOD_TYPE_SEMI_ANNUAL = "SEMI_ANNUAL";                  // 半年报
    public static final String PERIOD_TYPE_QUARTERLY = "QUARTERLY";                      // 季报
    public static final String PERIOD_TYPE_MONTHLY = "MONTHLY";                          // 月报

    /**
     * 报表状态常量
     */
    public static final String STATEMENT_STATUS_DRAFT = "DRAFT";                         // 草稿
    public static final String STATEMENT_STATUS_PREPARED = "PREPARED";                   // 已编制
    public static final String STATEMENT_STATUS_REVIEWED = "REVIEWED";                   // 已复核
    public static final String STATEMENT_STATUS_APPROVED = "APPROVED";                   // 已批准
    public static final String STATEMENT_STATUS_SUBMITTED = "SUBMITTED";                 // 已报送
    public static final String STATEMENT_STATUS_PUBLISHED = "PUBLISHED";                 // 已发布

    /**
     * 审计状态常量
     */
    public static final String AUDIT_STATUS_NOT_AUDITED = "NOT_AUDITED";                 // 未审计
    public static final String AUDIT_STATUS_IN_PROGRESS = "IN_PROGRESS";                 // 审计中
    public static final String AUDIT_STATUS_COMPLETED = "COMPLETED";                     // 审计完成
    public static final String AUDIT_STATUS_QUALIFIED = "QUALIFIED";                     // 审计合格
    public static final String AUDIT_STATUS_UNQUALIFIED = "UNQUALIFIED";                 // 审计不合格

    /**
     * 审计意见常量
     */
    public static final String AUDIT_OPINION_UNQUALIFIED = "UNQUALIFIED";                // 无保留意见
    public static final String AUDIT_OPINION_QUALIFIED = "QUALIFIED";                    // 保留意见
    public static final String AUDIT_OPINION_ADVERSE = "ADVERSE";                        // 否定意见
    public static final String AUDIT_OPINION_DISCLAIMER = "DISCLAIMER";                  // 无法表示意见

    /**
     * 报送状态常量
     */
    public static final String SUBMISSION_STATUS_NOT_SUBMITTED = "NOT_SUBMITTED";        // 未报送
    public static final String SUBMISSION_STATUS_SUBMITTED = "SUBMITTED";                // 已报送
    public static final String SUBMISSION_STATUS_ACCEPTED = "ACCEPTED";                  // 已接收
    public static final String SUBMISSION_STATUS_REJECTED = "REJECTED";                  // 已拒绝
    public static final String SUBMISSION_STATUS_UNDER_REVIEW = "UNDER_REVIEW";          // 审核中

    /**
     * 数据质量等级常量
     */
    public static final String DATA_QUALITY_EXCELLENT = "EXCELLENT";                     // 优秀
    public static final String DATA_QUALITY_GOOD = "GOOD";                               // 良好
    public static final String DATA_QUALITY_AVERAGE = "AVERAGE";                         // 一般
    public static final String DATA_QUALITY_POOR = "POOR";                               // 较差
    public static final String DATA_QUALITY_BAD = "BAD";                                 // 很差
    public static final String DATA_QUALITY_LEVEL_EXCELLENT = "EXCELLENT";               // 优秀
    public static final String DATA_QUALITY_LEVEL_GOOD = "GOOD";                         // 良好
    public static final String DATA_QUALITY_LEVEL_AVERAGE = "AVERAGE";                   // 一般
    public static final String DATA_QUALITY_LEVEL_POOR = "POOR";                         // 较差
    public static final String DATA_QUALITY_LEVEL_BAD = "BAD";                           // 很差

    /**
     * 风险等级常量
     */
    public static final String RISK_LEVEL_LOW = "LOW";                                   // 低风险
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM";                             // 中风险
    public static final String RISK_LEVEL_HIGH = "HIGH";                                 // 高风险
    public static final String RISK_LEVEL_CRITICAL = "CRITICAL";                         // 极高风险

    /**
     * 会计准则常量
     */
    public static final String ACCOUNTING_STANDARDS_CAS = "CAS";                         // 企业会计准则
    public static final String ACCOUNTING_STANDARDS_IFRS = "IFRS";                       // 国际财务报告准则
    public static final String ACCOUNTING_STANDARDS_GAAP = "GAAP";                       // 美国通用会计准则
    public static final String ACCOUNTING_STANDARDS_OTHER = "OTHER";                     // 其他准则

    /**
     * 货币单位常量
     */
    public static final String CURRENCY_CNY = "CNY";                                     // 人民币
    public static final String CURRENCY_USD = "USD";                                     // 美元
    public static final String CURRENCY_EUR = "EUR";                                     // 欧元
    public static final String CURRENCY_HKD = "HKD";                                     // 港币

}
