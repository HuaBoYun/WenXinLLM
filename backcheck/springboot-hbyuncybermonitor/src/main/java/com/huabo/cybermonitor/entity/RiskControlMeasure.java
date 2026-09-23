package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 风险控制措施实体类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_RISK_CONTROL_MEASURE")
public class RiskControlMeasure {

    /**
     * 风险控制措施ID
     */
    @TableId(value = "CONTROL_MEASURE_ID", type = IdType.ASSIGN_UUID)
    private String controlMeasureId;

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
     * 风险评估ID
     */
    @TableField("RISK_ASSESSMENT_ID")
    private String riskAssessmentId;

    /**
     * 措施名称
     */
    @TableField("MEASURE_NAME")
    private String measureName;

    /**
     * 措施类型
     */
    @TableField("MEASURE_TYPE")
    private String measureType;

    /**
     * 措施分类
     */
    @TableField("MEASURE_CATEGORY")
    private String measureCategory;

    /**
     * 措施描述
     */
    @TableField("MEASURE_DESCRIPTION")
    private String measureDescription;

    /**
     * 措施状态
     */
    @TableField("MEASURE_STATUS")
    private String measureStatus;

    /**
     * 优先级
     */
    @TableField("PRIORITY")
    private String priority;

    // ==================== 风险信息 ====================

    /**
     * 目标风险类型
     */
    @TableField("TARGET_RISK_TYPE")
    private String targetRiskType;

    /**
     * 目标风险等级
     */
    @TableField("TARGET_RISK_LEVEL")
    private String targetRiskLevel;

    /**
     * 风险控制目标
     */
    @TableField("RISK_CONTROL_OBJECTIVE")
    private String riskControlObjective;

    /**
     * 预期风险降低程度
     */
    @TableField("EXPECTED_RISK_REDUCTION")
    private BigDecimal expectedRiskReduction;

    /**
     * 实际风险降低程度
     */
    @TableField("ACTUAL_RISK_REDUCTION")
    private BigDecimal actualRiskReduction;

    // ==================== 实施信息 ====================

    /**
     * 实施计划
     */
    @TableField("IMPLEMENTATION_PLAN")
    private String implementationPlan;

    /**
     * 实施步骤
     */
    @TableField("IMPLEMENTATION_STEPS")
    private String implementationSteps;

    /**
     * 计划开始日期
     */
    @TableField("PLANNED_START_DATE")
    private LocalDate plannedStartDate;

    /**
     * 计划完成日期
     */
    @TableField("PLANNED_COMPLETION_DATE")
    private LocalDate plannedCompletionDate;

    /**
     * 实际开始日期
     */
    @TableField("ACTUAL_START_DATE")
    private LocalDate actualStartDate;

    /**
     * 实际完成日期
     */
    @TableField("ACTUAL_COMPLETION_DATE")
    private LocalDate actualCompletionDate;

    /**
     * 实施进度
     */
    @TableField("IMPLEMENTATION_PROGRESS")
    private BigDecimal implementationProgress;

    /**
     * 实施状态
     */
    @TableField("IMPLEMENTATION_STATUS")
    private String implementationStatus;

    // ==================== 责任信息 ====================

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
     * 协助部门
     */
    @TableField("ASSISTING_DEPARTMENTS")
    private String assistingDepartments;

    /**
     * 监督人员
     */
    @TableField("SUPERVISOR")
    private String supervisor;

    /**
     * 联系方式
     */
    @TableField("CONTACT_INFO")
    private String contactInfo;

    // ==================== 资源信息 ====================

    /**
     * 所需资源
     */
    @TableField("REQUIRED_RESOURCES")
    private String requiredResources;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 实际支出
     */
    @TableField("ACTUAL_EXPENDITURE")
    private BigDecimal actualExpenditure;

    /**
     * 人力投入
     */
    @TableField("HUMAN_RESOURCES")
    private String humanResources;

    /**
     * 技术支持
     */
    @TableField("TECHNICAL_SUPPORT")
    private String technicalSupport;

    /**
     * 外部支持
     */
    @TableField("EXTERNAL_SUPPORT")
    private String externalSupport;

    // ==================== 效果评估 ====================

    /**
     * 评估标准
     */
    @TableField("EVALUATION_CRITERIA")
    private String evaluationCriteria;

    /**
     * 评估方法
     */
    @TableField("EVALUATION_METHOD")
    private String evaluationMethod;

    /**
     * 评估周期
     */
    @TableField("EVALUATION_CYCLE")
    private String evaluationCycle;

    /**
     * 最后评估日期
     */
    @TableField("LAST_EVALUATION_DATE")
    private LocalDate lastEvaluationDate;

    /**
     * 下次评估日期
     */
    @TableField("NEXT_EVALUATION_DATE")
    private LocalDate nextEvaluationDate;

    /**
     * 效果评分
     */
    @TableField("EFFECTIVENESS_SCORE")
    private BigDecimal effectivenessScore;

    /**
     * 效果等级
     */
    @TableField("EFFECTIVENESS_LEVEL")
    private String effectivenessLevel;

    /**
     * 评估结果
     */
    @TableField("EVALUATION_RESULT")
    private String evaluationResult;

    // ==================== 监控信息 ====================

    /**
     * 是否需要监控
     */
    @TableField("REQUIRES_MONITORING")
    private Boolean requiresMonitoring;

    /**
     * 监控指标
     */
    @TableField("MONITORING_INDICATORS")
    private String monitoringIndicators;

    /**
     * 监控频率
     */
    @TableField("MONITORING_FREQUENCY")
    private String monitoringFrequency;

    /**
     * 监控责任人
     */
    @TableField("MONITORING_RESPONSIBLE_PERSON")
    private String monitoringResponsiblePerson;

    /**
     * 最后监控时间
     */
    @TableField("LAST_MONITORING_TIME")
    private LocalDateTime lastMonitoringTime;

    /**
     * 监控状态
     */
    @TableField("MONITORING_STATUS")
    private String monitoringStatus;

    // ==================== 风险事件关联 ====================

    /**
     * 关联风险事件ID
     */
    @TableField("RELATED_RISK_EVENT_ID")
    private String relatedRiskEventId;

    /**
     * 触发条件
     */
    @TableField("TRIGGER_CONDITIONS")
    private String triggerConditions;

    /**
     * 是否自动触发
     */
    @TableField("AUTO_TRIGGER")
    private Boolean autoTrigger;

    /**
     * 触发时间
     */
    @TableField("TRIGGER_TIME")
    private LocalDateTime triggerTime;

    /**
     * 触发原因
     */
    @TableField("TRIGGER_REASON")
    private String triggerReason;

    // ==================== 持续改进 ====================

    /**
     * 改进建议
     */
    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    /**
     * 经验教训
     */
    @TableField("LESSONS_LEARNED")
    private String lessonsLearned;

    /**
     * 最佳实践
     */
    @TableField("BEST_PRACTICES")
    private String bestPractices;

    /**
     * 知识库链接
     */
    @TableField("KNOWLEDGE_BASE_LINK")
    private String knowledgeBaseLink;

    /**
     * 培训需求
     */
    @TableField("TRAINING_REQUIREMENTS")
    private String trainingRequirements;

    // ==================== 审核信息 ====================

    /**
     * 审核状态
     */
    @TableField("REVIEW_STATUS")
    private String reviewStatus;

    /**
     * 审核人员
     */
    @TableField("REVIEWER")
    private String reviewer;

    /**
     * 审核时间
     */
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;

    /**
     * 审核意见
     */
    @TableField("REVIEW_COMMENTS")
    private String reviewComments;

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

    /**
     * 批准意见
     */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

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
     * 措施类型常量
     */
    public static final String MEASURE_TYPE_PREVENTIVE = "PREVENTIVE"; // 预防性措施
    public static final String MEASURE_TYPE_DETECTIVE = "DETECTIVE"; // 检测性措施
    public static final String MEASURE_TYPE_CORRECTIVE = "CORRECTIVE"; // 纠正性措施
    public static final String MEASURE_TYPE_COMPENSATING = "COMPENSATING"; // 补偿性措施
    public static final String MEASURE_TYPE_DIRECTIVE = "DIRECTIVE"; // 指导性措施

    /**
     * 措施分类常量
     */
    public static final String CATEGORY_POLICY = "POLICY"; // 政策措施
    public static final String CATEGORY_PROCEDURE = "PROCEDURE"; // 程序措施
    public static final String CATEGORY_TECHNOLOGY = "TECHNOLOGY"; // 技术措施
    public static final String CATEGORY_TRAINING = "TRAINING"; // 培训措施
    public static final String CATEGORY_MONITORING = "MONITORING"; // 监控措施
    public static final String CATEGORY_ORGANIZATIONAL = "ORGANIZATIONAL"; // 组织措施

    /**
     * 措施状态常量
     */
    public static final String STATUS_PLANNED = "PLANNED"; // 计划中
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS"; // 实施中
    public static final String STATUS_COMPLETED = "COMPLETED"; // 已完成
    public static final String STATUS_SUSPENDED = "SUSPENDED"; // 暂停
    public static final String STATUS_CANCELLED = "CANCELLED"; // 已取消
    public static final String STATUS_FAILED = "FAILED"; // 失败
    public static final String MEASURE_STATUS_DRAFT = "DRAFT"; // 草稿
    public static final String MEASURE_STATUS_APPROVED = "APPROVED"; // 已批准
    public static final String MEASURE_STATUS_ACTIVE = "ACTIVE"; // 生效中
    public static final String MEASURE_STATUS_SUSPENDED = "SUSPENDED"; // 已暂停
    public static final String MEASURE_STATUS_TERMINATED = "TERMINATED"; // 已终止
    public static final String MEASURE_STATUS_COMPLETED = "COMPLETED"; // 已完成

    /**
     * 实施状态常量
     */
    public static final String IMPLEMENTATION_STATUS_NOT_STARTED = "NOT_STARTED"; // 未开始
    public static final String IMPLEMENTATION_STATUS_IN_PROGRESS = "IN_PROGRESS"; // 实施中
    public static final String IMPLEMENTATION_STATUS_COMPLETED = "COMPLETED"; // 已完成
    public static final String IMPLEMENTATION_STATUS_SUSPENDED = "SUSPENDED"; // 已暂停

    /**
     * 优先级常量
     */
    public static final String PRIORITY_CRITICAL = "CRITICAL"; // 关键
    public static final String PRIORITY_URGENT = "URGENT"; // 紧急
    public static final String PRIORITY_HIGH = "HIGH"; // 高
    public static final String PRIORITY_MEDIUM = "MEDIUM"; // 中
    public static final String PRIORITY_LOW = "LOW"; // 低

    /**
     * 效果等级常量
     */
    public static final String EFFECTIVENESS_EXCELLENT = "EXCELLENT"; // 优秀
    public static final String EFFECTIVENESS_GOOD = "GOOD"; // 良好
    public static final String EFFECTIVENESS_SATISFACTORY = "SATISFACTORY"; // 满意
    public static final String EFFECTIVENESS_POOR = "POOR"; // 较差
    public static final String EFFECTIVENESS_INEFFECTIVE = "INEFFECTIVE"; // 无效
    public static final String EFFECTIVENESS_LEVEL_EXCELLENT = "EXCELLENT"; // 优秀
    public static final String EFFECTIVENESS_LEVEL_GOOD = "GOOD"; // 良好
    public static final String EFFECTIVENESS_LEVEL_FAIR = "FAIR"; // 一般
    public static final String EFFECTIVENESS_LEVEL_POOR = "POOR"; // 较差
    public static final String EFFECTIVENESS_LEVEL_VERY_POOR = "VERY_POOR"; // 很差

}
