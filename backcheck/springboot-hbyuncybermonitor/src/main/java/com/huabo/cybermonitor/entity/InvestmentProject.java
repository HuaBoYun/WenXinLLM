package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投资项目信息实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("INVESTMENT_PROJECT")
public class InvestmentProject {

    /**
     * 投资项目ID
     */
    @TableId(value = "PROJECT_ID", type = IdType.ASSIGN_UUID)
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 投资方企业ID
     */
    private String investorEnterpriseId;

    /**
     * 投资方企业名称
     */
    private String investorEnterpriseName;

    /**
     * 被投资方企业ID
     */
    private String investeeEnterpriseId;

    /**
     * 被投资方企业名称
     */
    private String investeeEnterpriseName;

    /**
     * 投资类型
     */
    private String investmentType;

    /**
     * 投资方式
     */
    private String investmentMethod;

    /**
     * 投资行业
     */
    private String investmentIndustry;

    /**
     * 投资地区
     */
    private String investmentRegion;

    /**
     * 计划投资金额（万元）
     */
    private BigDecimal plannedInvestmentAmount;

    /**
     * 实际投资金额（万元）
     */
    private BigDecimal actualInvestmentAmount;

    /**
     * 已投资金额（万元）
     */
    private BigDecimal investedAmount;

    /**
     * 剩余投资金额（万元）
     */
    private BigDecimal remainingInvestmentAmount;

    /**
     * 投资进度（%）
     */
    private BigDecimal investmentProgress;

    /**
     * 计划持股比例（%）
     */
    private BigDecimal plannedShareholdingRatio;

    /**
     * 实际持股比例（%）
     */
    private BigDecimal actualShareholdingRatio;

    /**
     * 投资期限（年）
     */
    private Integer investmentTerm;

    /**
     * 预期收益率（%）
     */
    private BigDecimal expectedReturnRate;

    /**
     * 实际收益率（%）
     */
    private BigDecimal actualReturnRate;

    /**
     * 项目状态
     */
    private String projectStatus;

    /**
     * 投资阶段
     */
    private String investmentStage;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 风险评估结果
     */
    private String riskAssessmentResult;

    /**
     * 投资决策依据
     */
    private String investmentDecisionBasis;

    /**
     * 决策流程状态
     */
    private String decisionProcessStatus;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 审批机关
     */
    private String approvalAuthority;

    /**
     * 审批文号
     */
    private String approvalNumber;

    /**
     * 审批日期
     */
    private LocalDateTime approvalDate;

    /**
     * 项目开始日期
     */
    private LocalDateTime projectStartDate;

    /**
     * 计划完成日期
     */
    private LocalDateTime plannedCompletionDate;

    /**
     * 实际完成日期
     */
    private LocalDateTime actualCompletionDate;

    /**
     * 投资效果评估
     */
    private String investmentEffectEvaluation;

    /**
     * 是否战略投资
     */
    private Boolean isStrategicInvestment;

    /**
     * 是否关联交易
     */
    private Boolean isRelatedTransaction;

    /**
     * 关联关系描述
     */
    private String relatedRelationDescription;

    /**
     * 是否需要监管关注
     */
    private Boolean needRegulatoryAttention;

    /**
     * 监管关注原因
     */
    private String regulatoryAttentionReason;

    /**
     * 合规性检查结果
     */
    private String complianceCheckResult;

    /**
     * 项目负责人
     */
    private String projectManager;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据更新时间
     */
    private LocalDateTime dataUpdateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 投资类型常量
    public static final String INVESTMENT_TYPE_EQUITY = "EQUITY"; // 股权投资
    public static final String INVESTMENT_TYPE_DEBT = "DEBT"; // 债权投资
    public static final String INVESTMENT_TYPE_MIXED = "MIXED"; // 混合投资
    public static final String INVESTMENT_TYPE_FUND = "FUND"; // 基金投资
    public static final String INVESTMENT_TYPE_TRUST = "TRUST"; // 信托投资
    public static final String INVESTMENT_TYPE_OTHER = "OTHER"; // 其他投资
    public static final String INVESTMENT_TYPE_VENTURE = "VENTURE"; // 风险投资
    public static final String INVESTMENT_TYPE_GROWTH = "GROWTH"; // 成长投资
    public static final String INVESTMENT_TYPE_BUYOUT = "BUYOUT"; // 杠杆收购
    public static final String INVESTMENT_TYPE_MEZZANINE = "MEZZANINE"; // 夹层融资

    // 投资方式常量
    public static final String INVESTMENT_METHOD_CASH = "CASH"; // 现金投资
    public static final String INVESTMENT_METHOD_ASSET = "ASSET"; // 资产投资
    public static final String INVESTMENT_METHOD_EQUITY_SWAP = "EQUITY_SWAP"; // 股权置换
    public static final String INVESTMENT_METHOD_DEBT_EQUITY_SWAP = "DEBT_EQUITY_SWAP"; // 债转股
    public static final String INVESTMENT_METHOD_MERGER = "MERGER"; // 并购
    public static final String INVESTMENT_METHOD_JOINT_VENTURE = "JOINT_VENTURE"; // 合资
    public static final String INVESTMENT_METHOD_ACQUISITION = "ACQUISITION"; // 收购

    // 项目状态常量
    public static final String PROJECT_STATUS_PLANNING = "PLANNING"; // 规划中
    public static final String PROJECT_STATUS_APPROVED = "APPROVED"; // 已批准
    public static final String PROJECT_STATUS_EXECUTING = "EXECUTING"; // 执行中
    public static final String PROJECT_STATUS_COMPLETED = "COMPLETED"; // 已完成
    public static final String PROJECT_STATUS_SUSPENDED = "SUSPENDED"; // 暂停
    public static final String PROJECT_STATUS_CANCELLED = "CANCELLED"; // 已取消
    public static final String PROJECT_STATUS_FAILED = "FAILED"; // 失败

    // 投资阶段常量
    public static final String INVESTMENT_STAGE_SEED = "SEED"; // 种子期
    public static final String INVESTMENT_STAGE_STARTUP = "STARTUP"; // 初创期
    public static final String INVESTMENT_STAGE_GROWTH = "GROWTH"; // 成长期
    public static final String INVESTMENT_STAGE_EXPANSION = "EXPANSION"; // 扩张期
    public static final String INVESTMENT_STAGE_MATURE = "MATURE"; // 成熟期
    public static final String INVESTMENT_STAGE_RESTRUCTURING = "RESTRUCTURING"; // 重组期
    public static final String INVESTMENT_STAGE_DECLINE = "DECLINE"; // 衰退期

    // 风险等级常量
    public static final String RISK_LEVEL_LOW = "LOW"; // 低风险
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM"; // 中风险
    public static final String RISK_LEVEL_HIGH = "HIGH"; // 高风险
    public static final String RISK_LEVEL_CRITICAL = "CRITICAL"; // 严重风险

    // 决策流程状态常量
    public static final String DECISION_PROCESS_STATUS_INITIATED = "INITIATED"; // 已发起
    public static final String DECISION_PROCESS_STATUS_REVIEWING = "REVIEWING"; // 审查中
    public static final String DECISION_PROCESS_STATUS_APPROVED = "APPROVED"; // 已批准
    public static final String DECISION_PROCESS_STATUS_REJECTED = "REJECTED"; // 已拒绝
    public static final String DECISION_PROCESS_STATUS_SUSPENDED = "SUSPENDED"; // 已暂停
    public static final String DECISION_PROCESS_STATUS_INITIAL = "INITIAL"; // 初始
    public static final String DECISION_PROCESS_STATUS_DUE_DILIGENCE = "DUE_DILIGENCE"; // 尽职调查
    public static final String DECISION_PROCESS_STATUS_COMMITTEE_REVIEW = "COMMITTEE_REVIEW"; // 委员会审查
    public static final String DECISION_PROCESS_STATUS_BOARD_APPROVAL = "BOARD_APPROVAL"; // 董事会批准
    public static final String DECISION_PROCESS_STATUS_FINAL_APPROVAL = "FINAL_APPROVAL"; // 最终批准

    // 审批状态常量
    public static final String APPROVAL_STATUS_PENDING = "PENDING"; // 待审批
    public static final String APPROVAL_STATUS_APPROVED = "APPROVED"; // 已审批
    public static final String APPROVAL_STATUS_REJECTED = "REJECTED"; // 已拒绝
    public static final String APPROVAL_STATUS_CANCELLED = "CANCELLED"; // 已取消
    public static final String APPROVAL_STATUS_EXPIRED = "EXPIRED"; // 已过期
}
