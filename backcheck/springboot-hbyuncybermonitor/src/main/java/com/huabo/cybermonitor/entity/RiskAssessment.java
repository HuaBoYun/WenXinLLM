package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 风险评估实体类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_RISK_ASSESSMENT")
public class RiskAssessment {

    /**
     * 风险评估ID
     */
    @TableId(value = "RISK_ASSESSMENT_ID", type = IdType.ASSIGN_UUID)
    private String riskAssessmentId;

    /**
     * 企业ID
     */
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    /**
     * 企业名称
     */
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    /**
     * 评估名称
     */
    @TableField("ASSESSMENT_NAME")
    private String assessmentName;

    /**
     * 评估年度
     */
    @TableField("ASSESSMENT_YEAR")
    private Integer assessmentYear;

    /**
     * 评估期间
     */
    @TableField("ASSESSMENT_PERIOD")
    private Integer assessmentPeriod;

    /**
     * 评估日期
     */
    @TableField("ASSESSMENT_DATE")
    private LocalDate assessmentDate;

    /**
     * 评估类型
     */
    @TableField("ASSESSMENT_TYPE")
    private String assessmentType;

    /**
     * 评估方法
     */
    @TableField("ASSESSMENT_METHOD")
    private String assessmentMethod;

    /**
     * 评估状态
     */
    @TableField("ASSESSMENT_STATUS")
    private String assessmentStatus;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    // ==================== 综合风险评估 ====================

    /**
     * 综合风险等级
     */
    @TableField("OVERALL_RISK_LEVEL")
    private String overallRiskLevel;

    /**
     * 综合风险评分
     */
    @TableField("OVERALL_RISK_SCORE")
    private BigDecimal overallRiskScore;

    /**
     * 风险趋势
     */
    @TableField("RISK_TREND")
    private String riskTrend;

    /**
     * 风险变化幅度
     */
    @TableField("RISK_CHANGE_MAGNITUDE")
    private BigDecimal riskChangeMagnitude;

    // ==================== 财务风险评估 ====================

    /**
     * 财务风险等级
     */
    @TableField("FINANCIAL_RISK_LEVEL")
    private String financialRiskLevel;

    /**
     * 财务风险评分
     */
    @TableField("FINANCIAL_RISK_SCORE")
    private BigDecimal financialRiskScore;

    /**
     * 流动性风险评分
     */
    @TableField("LIQUIDITY_RISK_SCORE")
    private BigDecimal liquidityRiskScore;

    /**
     * 偿债能力风险评分
     */
    @TableField("SOLVENCY_RISK_SCORE")
    private BigDecimal solvencyRiskScore;

    /**
     * 盈利能力风险评分
     */
    @TableField("PROFITABILITY_RISK_SCORE")
    private BigDecimal profitabilityRiskScore;

    /**
     * 现金流风险评分
     */
    @TableField("CASH_FLOW_RISK_SCORE")
    private BigDecimal cashFlowRiskScore;

    /**
     * 财务杠杆风险评分
     */
    @TableField("LEVERAGE_RISK_SCORE")
    private BigDecimal leverageRiskScore;

    // ==================== 经营风险评估 ====================

    /**
     * 经营风险等级
     */
    @TableField("OPERATIONAL_RISK_LEVEL")
    private String operationalRiskLevel;

    /**
     * 经营风险评分
     */
    @TableField("OPERATIONAL_RISK_SCORE")
    private BigDecimal operationalRiskScore;

    /**
     * 市场风险评分
     */
    @TableField("MARKET_RISK_SCORE")
    private BigDecimal marketRiskScore;

    /**
     * 竞争风险评分
     */
    @TableField("COMPETITION_RISK_SCORE")
    private BigDecimal competitionRiskScore;

    /**
     * 技术风险评分
     */
    @TableField("TECHNOLOGY_RISK_SCORE")
    private BigDecimal technologyRiskScore;

    /**
     * 供应链风险评分
     */
    @TableField("SUPPLY_CHAIN_RISK_SCORE")
    private BigDecimal supplyChainRiskScore;

    /**
     * 客户集中度风险评分
     */
    @TableField("CUSTOMER_CONCENTRATION_RISK_SCORE")
    private BigDecimal customerConcentrationRiskScore;

    // ==================== 合规风险评估 ====================

    /**
     * 合规风险等级
     */
    @TableField("COMPLIANCE_RISK_LEVEL")
    private String complianceRiskLevel;

    /**
     * 合规风险评分
     */
    @TableField("COMPLIANCE_RISK_SCORE")
    private BigDecimal complianceRiskScore;

    /**
     * 法律风险评分
     */
    @TableField("LEGAL_RISK_SCORE")
    private BigDecimal legalRiskScore;

    /**
     * 监管风险评分
     */
    @TableField("REGULATORY_RISK_SCORE")
    private BigDecimal regulatoryRiskScore;

    /**
     * 税务风险评分
     */
    @TableField("TAX_RISK_SCORE")
    private BigDecimal taxRiskScore;

    /**
     * 环保风险评分
     */
    @TableField("ENVIRONMENTAL_RISK_SCORE")
    private BigDecimal environmentalRiskScore;

    /**
     * 安全风险评分
     */
    @TableField("SAFETY_RISK_SCORE")
    private BigDecimal safetyRiskScore;

    // ==================== 治理风险评估 ====================

    /**
     * 治理风险等级
     */
    @TableField("GOVERNANCE_RISK_LEVEL")
    private String governanceRiskLevel;

    /**
     * 治理风险评分
     */
    @TableField("GOVERNANCE_RISK_SCORE")
    private BigDecimal governanceRiskScore;

    /**
     * 内控风险评分
     */
    @TableField("INTERNAL_CONTROL_RISK_SCORE")
    private BigDecimal internalControlRiskScore;

    /**
     * 决策风险评分
     */
    @TableField("DECISION_RISK_SCORE")
    private BigDecimal decisionRiskScore;

    /**
     * 人员风险评分
     */
    @TableField("PERSONNEL_RISK_SCORE")
    private BigDecimal personnelRiskScore;

    /**
     * 信息系统风险评分
     */
    @TableField("IT_RISK_SCORE")
    private BigDecimal itRiskScore;

    // ==================== 外部风险评估 ====================

    /**
     * 外部风险等级
     */
    @TableField("EXTERNAL_RISK_LEVEL")
    private String externalRiskLevel;

    /**
     * 外部风险评分
     */
    @TableField("EXTERNAL_RISK_SCORE")
    private BigDecimal externalRiskScore;

    /**
     * 宏观经济风险评分
     */
    @TableField("MACROECONOMIC_RISK_SCORE")
    private BigDecimal macroeconomicRiskScore;

    /**
     * 政策风险评分
     */
    @TableField("POLICY_RISK_SCORE")
    private BigDecimal policyRiskScore;

    /**
     * 行业风险评分
     */
    @TableField("INDUSTRY_RISK_SCORE")
    private BigDecimal industryRiskScore;

    /**
     * 汇率风险评分
     */
    @TableField("EXCHANGE_RATE_RISK_SCORE")
    private BigDecimal exchangeRateRiskScore;

    /**
     * 利率风险评分
     */
    @TableField("INTEREST_RATE_RISK_SCORE")
    private BigDecimal interestRateRiskScore;

    // ==================== 风险因素分析 ====================

    /**
     * 主要风险因素
     */
    @TableField("MAJOR_RISK_FACTORS")
    private String majorRiskFactors;

    /**
     * 风险因素数量
     */
    @TableField("RISK_FACTOR_COUNT")
    private Integer riskFactorCount;

    /**
     * 高风险因素数量
     */
    @TableField("HIGH_RISK_FACTOR_COUNT")
    private Integer highRiskFactorCount;

    /**
     * 风险关联度
     */
    @TableField("RISK_CORRELATION")
    private BigDecimal riskCorrelation;

    /**
     * 风险集中度
     */
    @TableField("RISK_CONCENTRATION")
    private BigDecimal riskConcentration;

    // ==================== 风险预警信息 ====================

    /**
     * 是否触发预警
     */
    @TableField("IS_WARNING_TRIGGERED")
    private Boolean isWarningTriggered;

    /**
     * 预警等级
     */
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    /**
     * 预警类型
     */
    @TableField("WARNING_TYPE")
    private String warningType;

    /**
     * 预警原因
     */
    @TableField("WARNING_REASON")
    private String warningReason;

    /**
     * 预警触发时间
     */
    @TableField("WARNING_TRIGGER_TIME")
    private LocalDateTime warningTriggerTime;

    // ==================== 风险应对措施 ====================

    /**
     * 风险应对策略
     */
    @TableField("RISK_RESPONSE_STRATEGY")
    private String riskResponseStrategy;

    /**
     * 风险控制措施
     */
    @TableField("RISK_CONTROL_MEASURES")
    private String riskControlMeasures;

    /**
     * 应急预案
     */
    @TableField("CONTINGENCY_PLAN")
    private String contingencyPlan;

    /**
     * 责任部门
     */
    @TableField("RESPONSIBLE_DEPARTMENT")
    private String responsibleDepartment;

    /**
     * 责任人
     */
    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    /**
     * 整改期限
     */
    @TableField("RECTIFICATION_DEADLINE")
    private LocalDate rectificationDeadline;

    // ==================== 评估结果 ====================

    /**
     * 评估结论
     */
    @TableField("ASSESSMENT_CONCLUSION")
    private String assessmentConclusion;

    /**
     * 风险建议
     */
    @TableField("RISK_RECOMMENDATIONS")
    private String riskRecommendations;

    /**
     * 下次评估日期
     */
    @TableField("NEXT_ASSESSMENT_DATE")
    private LocalDate nextAssessmentDate;

    /**
     * 评估有效期
     */
    @TableField("ASSESSMENT_VALIDITY_PERIOD")
    private Integer assessmentValidityPeriod;

    // ==================== 审核信息 ====================

    /**
     * 评估人员
     */
    @TableField("ASSESSOR")
    private String assessor;

    /**
     * 审核人员
     */
    @TableField("REVIEWER")
    private String reviewer;

    /**
     * 审核状态
     */
    @TableField("REVIEW_STATUS")
    private String reviewStatus;

    /**
     * 审核意见
     */
    @TableField("REVIEW_COMMENTS")
    private String reviewComments;

    /**
     * 审核时间
     */
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;

    /**
     * 批准人员
     */
    @TableField("APPROVER")
    private String approver;

    /**
     * 批准时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    // ==================== 系统字段 ====================

    /**
     * 备注
     */
    @TableField("REMARKS")
    private String remarks;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("DELETED")
    @TableLogic
    private Boolean deleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Long version;

    // ==================== 常量定义 ====================

    /**
     * 评估类型常量
     */
    public static final String ASSESSMENT_TYPE_COMPREHENSIVE = "COMPREHENSIVE"; // 综合评估
    public static final String ASSESSMENT_TYPE_FINANCIAL = "FINANCIAL"; // 财务风险评估
    public static final String ASSESSMENT_TYPE_OPERATIONAL = "OPERATIONAL"; // 经营风险评估
    public static final String ASSESSMENT_TYPE_COMPLIANCE = "COMPLIANCE"; // 合规风险评估
    public static final String ASSESSMENT_TYPE_GOVERNANCE = "GOVERNANCE"; // 治理风险评估
    public static final String ASSESSMENT_TYPE_EXTERNAL = "EXTERNAL"; // 外部风险评估
    public static final String ASSESSMENT_TYPE_SPECIAL = "SPECIAL"; // 专项评估

    /**
     * 评估方法常量
     */
    public static final String ASSESSMENT_METHOD_QUANTITATIVE = "QUANTITATIVE"; // 定量评估
    public static final String ASSESSMENT_METHOD_QUALITATIVE = "QUALITATIVE"; // 定性评估
    public static final String ASSESSMENT_METHOD_MIXED = "MIXED"; // 混合评估
    public static final String ASSESSMENT_METHOD_MODEL = "MODEL"; // 模型评估
    public static final String ASSESSMENT_METHOD_EXPERT = "EXPERT"; // 专家评估

    /**
     * 评估状态常量
     */
    public static final String ASSESSMENT_STATUS_DRAFT = "DRAFT"; // 草稿
    public static final String ASSESSMENT_STATUS_IN_PROGRESS = "IN_PROGRESS"; // 评估中
    public static final String ASSESSMENT_STATUS_COMPLETED = "COMPLETED"; // 已完成
    public static final String ASSESSMENT_STATUS_REVIEWED = "REVIEWED"; // 已审核
    public static final String ASSESSMENT_STATUS_APPROVED = "APPROVED"; // 已批准
    public static final String ASSESSMENT_STATUS_REJECTED = "REJECTED"; // 已拒绝

    /**
     * 风险等级常量
     */
    public static final String RISK_LEVEL_VERY_LOW = "VERY_LOW"; // 极低风险
    public static final String RISK_LEVEL_LOW = "LOW"; // 低风险
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM"; // 中等风险
    public static final String RISK_LEVEL_HIGH = "HIGH"; // 高风险
    public static final String RISK_LEVEL_VERY_HIGH = "VERY_HIGH"; // 极高风险
    public static final String RISK_LEVEL_CRITICAL = "CRITICAL"; // 临界风险

    /**
     * 风险趋势常量
     */
    public static final String RISK_TREND_DECREASING = "DECREASING"; // 下降趋势
    public static final String RISK_TREND_STABLE = "STABLE"; // 稳定趋势
    public static final String RISK_TREND_INCREASING = "INCREASING"; // 上升趋势
    public static final String RISK_TREND_VOLATILE = "VOLATILE"; // 波动趋势

    /**
     * 预警等级常量
     */
    public static final String WARNING_LEVEL_GREEN = "GREEN"; // 绿色预警
    public static final String WARNING_LEVEL_YELLOW = "YELLOW"; // 黄色预警
    public static final String WARNING_LEVEL_ORANGE = "ORANGE"; // 橙色预警
    public static final String WARNING_LEVEL_RED = "RED"; // 红色预警

    /**
     * 风险应对策略常量
     */
    public static final String RESPONSE_STRATEGY_ACCEPT = "ACCEPT"; // 接受风险
    public static final String RESPONSE_STRATEGY_AVOID = "AVOID"; // 规避风险
    public static final String RESPONSE_STRATEGY_MITIGATE = "MITIGATE"; // 缓解风险
    public static final String RESPONSE_STRATEGY_TRANSFER = "TRANSFER"; // 转移风险
    public static final String RESPONSE_STRATEGY_MONITOR = "MONITOR"; // 监控风险

    // ==================== 手动getter方法 ====================

    public String getRiskAssessmentId() {
        return this.riskAssessmentId;
    }

    public void setRiskAssessmentId(String riskAssessmentId) {
        this.riskAssessmentId = riskAssessmentId;
    }

}
