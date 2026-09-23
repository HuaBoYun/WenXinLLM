package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 风险事件实体类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_RISK_INCIDENT")
public class RiskIncident {

    /**
     * 风险事件ID
     */
    @TableId(value = "RISK_INCIDENT_ID", type = IdType.ASSIGN_UUID)
    private String riskIncidentId;

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
     * 事件编号
     */
    @TableField("INCIDENT_NUMBER")
    private String incidentNumber;

    /**
     * 事件名称
     */
    @TableField("INCIDENT_NAME")
    private String incidentName;

    /**
     * 事件类型
     */
    @TableField("INCIDENT_TYPE")
    private String incidentType;

    /**
     * 事件分类
     */
    @TableField("INCIDENT_CATEGORY")
    private String incidentCategory;

    /**
     * 事件分类（自动分类结果）
     */
    @TableField("INCIDENT_CLASSIFICATION")
    private String incidentClassification;

    /**
     * 事件描述
     */
    @TableField("INCIDENT_DESCRIPTION")
    private String incidentDescription;

    /**
     * 事件状态
     */
    @TableField("INCIDENT_STATUS")
    private String incidentStatus;

    // ==================== 事件基本信息 ====================

    /**
     * 发生时间
     */
    @TableField("OCCURRENCE_TIME")
    private LocalDateTime occurrenceTime;

    /**
     * 发现时间
     */
    @TableField("DISCOVERY_TIME")
    private LocalDateTime discoveryTime;

    /**
     * 报告时间
     */
    @TableField("REPORT_TIME")
    private LocalDateTime reportTime;

    /**
     * 发生地点
     */
    @TableField("OCCURRENCE_LOCATION")
    private String occurrenceLocation;

    /**
     * 发现人员
     */
    @TableField("DISCOVERER")
    private String discoverer;

    /**
     * 报告人员
     */
    @TableField("REPORTER")
    private String reporter;

    /**
     * 事件来源
     */
    @TableField("INCIDENT_SOURCE")
    private String incidentSource;

    // ==================== 风险信息 ====================

    /**
     * 风险等级
     */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /**
     * 风险评分
     */
    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    /**
     * 影响程度
     */
    @TableField("IMPACT_LEVEL")
    private String impactLevel;

    /**
     * 发生概率
     */
    @TableField("PROBABILITY")
    private BigDecimal probability;

    /**
     * 紧急程度
     */
    @TableField("URGENCY_LEVEL")
    private String urgencyLevel;

    /**
     * 影响范围
     */
    @TableField("IMPACT_SCOPE")
    private String impactScope;

    /**
     * 影响部门
     */
    @TableField("AFFECTED_DEPARTMENTS")
    private String affectedDepartments;

    /**
     * 影响人员数量
     */
    @TableField("AFFECTED_PERSONNEL_COUNT")
    private Integer affectedPersonnelCount;

    // ==================== 损失信息 ====================

    /**
     * 直接经济损失
     */
    @TableField("DIRECT_ECONOMIC_LOSS")
    private BigDecimal directEconomicLoss;

    /**
     * 间接经济损失
     */
    @TableField("INDIRECT_ECONOMIC_LOSS")
    private BigDecimal indirectEconomicLoss;

    /**
     * 总经济损失
     */
    @TableField("TOTAL_ECONOMIC_LOSS")
    private BigDecimal totalEconomicLoss;

    /**
     * 声誉损失
     */
    @TableField("REPUTATION_LOSS")
    private String reputationLoss;

    /**
     * 机会成本
     */
    @TableField("OPPORTUNITY_COST")
    private BigDecimal opportunityCost;

    /**
     * 恢复成本
     */
    @TableField("RECOVERY_COST")
    private BigDecimal recoveryCost;

    // ==================== 原因分析 ====================

    /**
     * 根本原因
     */
    @TableField("ROOT_CAUSE")
    private String rootCause;

    /**
     * 直接原因
     */
    @TableField("IMMEDIATE_CAUSE")
    private String immediateCause;

    /**
     * 间接原因
     */
    @TableField("INDIRECT_CAUSE")
    private String indirectCause;

    /**
     * 人为因素
     */
    @TableField("HUMAN_FACTORS")
    private String humanFactors;

    /**
     * 系统因素
     */
    @TableField("SYSTEM_FACTORS")
    private String systemFactors;

    /**
     * 环境因素
     */
    @TableField("ENVIRONMENTAL_FACTORS")
    private String environmentalFactors;

    /**
     * 管理因素
     */
    @TableField("MANAGEMENT_FACTORS")
    private String managementFactors;

    // ==================== 应急响应 ====================

    /**
     * 应急响应等级
     */
    @TableField("EMERGENCY_RESPONSE_LEVEL")
    private String emergencyResponseLevel;

    /**
     * 应急响应开始时间
     */
    @TableField("EMERGENCY_RESPONSE_START_TIME")
    private LocalDateTime emergencyResponseStartTime;

    /**
     * 应急响应结束时间
     */
    @TableField("EMERGENCY_RESPONSE_END_TIME")
    private LocalDateTime emergencyResponseEndTime;

    /**
     * 应急响应措施
     */
    @TableField("EMERGENCY_RESPONSE_MEASURES")
    private String emergencyResponseMeasures;

    /**
     * 应急响应团队
     */
    @TableField("EMERGENCY_RESPONSE_TEAM")
    private String emergencyResponseTeam;

    /**
     * 应急响应效果
     */
    @TableField("EMERGENCY_RESPONSE_EFFECTIVENESS")
    private String emergencyResponseEffectiveness;

    // ==================== 处理信息 ====================

    /**
     * 处理状态
     */
    @TableField("HANDLING_STATUS")
    private String handlingStatus;

    /**
     * 处理负责人
     */
    @TableField("HANDLING_RESPONSIBLE_PERSON")
    private String handlingResponsiblePerson;

    /**
     * 处理团队
     */
    @TableField("HANDLING_TEAM")
    private String handlingTeam;

    /**
     * 处理开始时间
     */
    @TableField("HANDLING_START_TIME")
    private LocalDateTime handlingStartTime;

    /**
     * 处理完成时间
     */
    @TableField("HANDLING_COMPLETION_TIME")
    private LocalDateTime handlingCompletionTime;

    /**
     * 处理措施
     */
    @TableField("HANDLING_MEASURES")
    private String handlingMeasures;

    /**
     * 处理结果
     */
    @TableField("HANDLING_RESULT")
    private String handlingResult;

    /**
     * 处理效果评估
     */
    @TableField("HANDLING_EFFECTIVENESS_EVALUATION")
    private String handlingEffectivenessEvaluation;

    // ==================== 预防措施 ====================

    /**
     * 预防措施
     */
    @TableField("PREVENTIVE_MEASURES")
    private String preventiveMeasures;

    /**
     * 纠正措施
     */
    @TableField("CORRECTIVE_MEASURES")
    private String correctiveMeasures;

    /**
     * 改进措施
     */
    @TableField("IMPROVEMENT_MEASURES")
    private String improvementMeasures;

    /**
     * 措施实施期限
     */
    @TableField("MEASURES_IMPLEMENTATION_DEADLINE")
    private LocalDate measuresImplementationDeadline;

    /**
     * 措施责任人
     */
    @TableField("MEASURES_RESPONSIBLE_PERSON")
    private String measuresResponsiblePerson;

    /**
     * 措施实施状态
     */
    @TableField("MEASURES_IMPLEMENTATION_STATUS")
    private String measuresImplementationStatus;

    // ==================== 关联信息 ====================

    /**
     * 关联风险评估ID
     */
    @TableField("RELATED_RISK_ASSESSMENT_ID")
    private String relatedRiskAssessmentId;

    /**
     * 关联控制措施ID
     */
    @TableField("RELATED_CONTROL_MEASURE_ID")
    private String relatedControlMeasureId;

    /**
     * 父事件ID
     */
    @TableField("PARENT_INCIDENT_ID")
    private String parentIncidentId;

    /**
     * 是否重复事件
     */
    @TableField("IS_RECURRING_INCIDENT")
    private Boolean isRecurringIncident;

    /**
     * 重复事件次数
     */
    @TableField("RECURRENCE_COUNT")
    private Integer recurrenceCount;

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
     * 事件类型常量
     */
    public static final String INCIDENT_TYPE_FINANCIAL = "FINANCIAL"; // 财务风险事件
    public static final String INCIDENT_TYPE_OPERATIONAL = "OPERATIONAL"; // 经营风险事件
    public static final String INCIDENT_TYPE_COMPLIANCE = "COMPLIANCE"; // 合规风险事件
    public static final String INCIDENT_TYPE_GOVERNANCE = "GOVERNANCE"; // 治理风险事件
    public static final String INCIDENT_TYPE_EXTERNAL = "EXTERNAL"; // 外部风险事件
    public static final String INCIDENT_TYPE_TECHNOLOGY = "TECHNOLOGY"; // 技术风险事件
    public static final String INCIDENT_TYPE_NATURAL = "NATURAL"; // 自然风险事件
    public static final String INCIDENT_TYPE_REPUTATION = "REPUTATION"; // 声誉风险事件
    public static final String INCIDENT_TYPE_STRATEGIC = "STRATEGIC"; // 战略风险事件
    public static final String INCIDENT_TYPE_ENVIRONMENTAL = "ENVIRONMENTAL"; // 环境风险事件
    public static final String INCIDENT_TYPE_SECURITY = "SECURITY"; // 安全风险事件

    /**
     * 事件状态常量
     */
    public static final String STATUS_REPORTED = "REPORTED"; // 已报告
    public static final String STATUS_INVESTIGATING = "INVESTIGATING"; // 调查中
    public static final String STATUS_HANDLING = "HANDLING"; // 处理中
    public static final String STATUS_RESOLVED = "RESOLVED"; // 已解决
    public static final String STATUS_CLOSED = "CLOSED"; // 已关闭
    public static final String STATUS_ESCALATED = "ESCALATED"; // 已升级
    public static final String INCIDENT_STATUS_REPORTED = "REPORTED"; // 已报告
    public static final String INCIDENT_STATUS_CONFIRMED = "CONFIRMED"; // 已确认
    public static final String INCIDENT_STATUS_INVESTIGATING = "INVESTIGATING"; // 调查中
    public static final String INCIDENT_STATUS_HANDLING = "HANDLING"; // 处理中
    public static final String INCIDENT_STATUS_RESOLVED = "RESOLVED"; // 已解决
    public static final String INCIDENT_STATUS_CLOSED = "CLOSED"; // 已关闭

    /**
     * 风险等级常量
     */
    public static final String RISK_LEVEL_VERY_LOW = "VERY_LOW"; // 极低
    public static final String RISK_LEVEL_LOW = "LOW"; // 低
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM"; // 中
    public static final String RISK_LEVEL_HIGH = "HIGH"; // 高
    public static final String RISK_LEVEL_VERY_HIGH = "VERY_HIGH"; // 极高
    public static final String RISK_LEVEL_CRITICAL = "CRITICAL"; // 临界

    /**
     * 影响程度常量
     */
    public static final String IMPACT_LEVEL_MINIMAL = "MINIMAL"; // 最小
    public static final String IMPACT_LEVEL_MINOR = "MINOR"; // 轻微
    public static final String IMPACT_LEVEL_MODERATE = "MODERATE"; // 中等
    public static final String IMPACT_LEVEL_MAJOR = "MAJOR"; // 重大
    public static final String IMPACT_LEVEL_SEVERE = "SEVERE"; // 严重
    public static final String IMPACT_LEVEL_CATASTROPHIC = "CATASTROPHIC"; // 灾难性

    /**
     * 紧急程度常量
     */
    public static final String URGENCY_LEVEL_LOW = "LOW"; // 低
    public static final String URGENCY_LEVEL_MEDIUM = "MEDIUM"; // 中
    public static final String URGENCY_LEVEL_HIGH = "HIGH"; // 高
    public static final String URGENCY_LEVEL_CRITICAL = "CRITICAL"; // 关键
    public static final String URGENCY_LEVEL_URGENT = "URGENT"; // 紧急

    /**
     * 处理状态常量
     */
    public static final String HANDLING_STATUS_NOT_STARTED = "NOT_STARTED"; // 未开始
    public static final String HANDLING_STATUS_IN_PROGRESS = "IN_PROGRESS"; // 处理中
    public static final String HANDLING_STATUS_COMPLETED = "COMPLETED"; // 已完成
    public static final String HANDLING_STATUS_SUSPENDED = "SUSPENDED"; // 已暂停

    /**
     * 影响程度常量（扩展）
     */
    public static final String IMPACT_LEVEL_VERY_HIGH = "VERY_HIGH"; // 极高
    public static final String IMPACT_LEVEL_HIGH = "HIGH"; // 高
    public static final String IMPACT_LEVEL_MEDIUM = "MEDIUM"; // 中
    public static final String IMPACT_LEVEL_LOW = "LOW"; // 低
    public static final String IMPACT_LEVEL_VERY_LOW = "VERY_LOW"; // 极低

}
