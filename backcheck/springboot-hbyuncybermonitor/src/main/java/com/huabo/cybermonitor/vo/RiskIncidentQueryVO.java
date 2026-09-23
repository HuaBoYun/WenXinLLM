package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 风险事件查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskIncidentQueryVO extends BaseVo {

    /**
     * 风险事件ID
     */
    private String riskIncidentId;

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 事件编号
     */
    private String incidentNumber;

    /**
     * 事件名称
     */
    private String incidentName;

    /**
     * 事件类型
     */
    private String incidentType;

    /**
     * 事件分类
     */
    private String incidentCategory;

    /**
     * 事件状态
     */
    private String incidentStatus;

    // ==================== 事件时间查询 ====================

    /**
     * 发生时间开始
     */
    private LocalDateTime occurrenceTimeStart;

    /**
     * 发生时间结束
     */
    private LocalDateTime occurrenceTimeEnd;

    /**
     * 发现时间开始
     */
    private LocalDateTime discoveryTimeStart;

    /**
     * 发现时间结束
     */
    private LocalDateTime discoveryTimeEnd;

    /**
     * 报告时间开始
     */
    private LocalDateTime reportTimeStart;

    /**
     * 报告时间结束
     */
    private LocalDateTime reportTimeEnd;

    /**
     * 发生地点
     */
    private String occurrenceLocation;

    /**
     * 发现人员
     */
    private String discoverer;

    /**
     * 报告人员
     */
    private String reporter;

    /**
     * 事件来源
     */
    private String incidentSource;

    // ==================== 风险信息查询 ====================

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 风险评分最小值
     */
    private BigDecimal riskScoreMin;

    /**
     * 风险评分最大值
     */
    private BigDecimal riskScoreMax;

    /**
     * 影响程度
     */
    private String impactLevel;

    /**
     * 发生概率最小值
     */
    private BigDecimal probabilityMin;

    /**
     * 发生概率最大值
     */
    private BigDecimal probabilityMax;

    /**
     * 紧急程度
     */
    private String urgencyLevel;

    /**
     * 影响范围
     */
    private String impactScope;

    /**
     * 影响人员数量最小值
     */
    private Integer affectedPersonnelCountMin;

    /**
     * 影响人员数量最大值
     */
    private Integer affectedPersonnelCountMax;

    // ==================== 损失信息查询 ====================

    /**
     * 直接经济损失最小值
     */
    private BigDecimal directEconomicLossMin;

    /**
     * 直接经济损失最大值
     */
    private BigDecimal directEconomicLossMax;

    /**
     * 间接经济损失最小值
     */
    private BigDecimal indirectEconomicLossMin;

    /**
     * 间接经济损失最大值
     */
    private BigDecimal indirectEconomicLossMax;

    /**
     * 总经济损失最小值
     */
    private BigDecimal totalEconomicLossMin;

    /**
     * 总经济损失最大值
     */
    private BigDecimal totalEconomicLossMax;

    /**
     * 机会成本最小值
     */
    private BigDecimal opportunityCostMin;

    /**
     * 机会成本最大值
     */
    private BigDecimal opportunityCostMax;

    /**
     * 恢复成本最小值
     */
    private BigDecimal recoveryCostMin;

    /**
     * 恢复成本最大值
     */
    private BigDecimal recoveryCostMax;

    // ==================== 应急响应查询 ====================

    /**
     * 应急响应等级
     */
    private String emergencyResponseLevel;

    /**
     * 应急响应开始时间开始
     */
    private LocalDateTime emergencyResponseStartTimeStart;

    /**
     * 应急响应开始时间结束
     */
    private LocalDateTime emergencyResponseStartTimeEnd;

    /**
     * 应急响应结束时间开始
     */
    private LocalDateTime emergencyResponseEndTimeStart;

    /**
     * 应急响应结束时间结束
     */
    private LocalDateTime emergencyResponseEndTimeEnd;

    /**
     * 应急响应效果
     */
    private String emergencyResponseEffectiveness;

    // ==================== 处理信息查询 ====================

    /**
     * 处理状态
     */
    private String handlingStatus;

    /**
     * 处理负责人
     */
    private String handlingResponsiblePerson;

    /**
     * 处理开始时间开始
     */
    private LocalDateTime handlingStartTimeStart;

    /**
     * 处理开始时间结束
     */
    private LocalDateTime handlingStartTimeEnd;

    /**
     * 处理完成时间开始
     */
    private LocalDateTime handlingCompletionTimeStart;

    /**
     * 处理完成时间结束
     */
    private LocalDateTime handlingCompletionTimeEnd;

    /**
     * 处理效果评估
     */
    private String handlingEffectivenessEvaluation;

    // ==================== 预防措施查询 ====================

    /**
     * 措施实施期限开始
     */
    private LocalDate measuresImplementationDeadlineStart;

    /**
     * 措施实施期限结束
     */
    private LocalDate measuresImplementationDeadlineEnd;

    /**
     * 措施责任人
     */
    private String measuresResponsiblePerson;

    /**
     * 措施实施状态
     */
    private String measuresImplementationStatus;

    // ==================== 监管报告查询 ====================

    /**
     * 是否需要监管报告
     */
    private Boolean requiresRegulatoryReport;

    /**
     * 监管报告状态
     */
    private String regulatoryReportStatus;

    /**
     * 监管报告时间开始
     */
    private LocalDateTime regulatoryReportTimeStart;

    /**
     * 监管报告时间结束
     */
    private LocalDateTime regulatoryReportTimeEnd;

    /**
     * 监管机构
     */
    private String regulatoryAuthority;

    // ==================== 关联信息查询 ====================

    /**
     * 关联风险评估ID
     */
    private String relatedRiskAssessmentId;

    /**
     * 关联控制措施ID
     */
    private String relatedControlMeasureId;

    /**
     * 父事件ID
     */
    private String parentIncidentId;

    /**
     * 是否重复事件
     */
    private Boolean isRecurringIncident;

    /**
     * 重复事件次数最小值
     */
    private Integer recurrenceCountMin;

    /**
     * 重复事件次数最大值
     */
    private Integer recurrenceCountMax;

    // ==================== 时间范围查询 ====================

    /**
     * 创建时间开始
     */

    /**
     */

    /**
     * 更新时间开始
     */
    private LocalDateTime updateTimeStart;

    /**
     * 更新时间结束
     */
    private LocalDateTime updateTimeEnd;

    // ==================== 关键字查询 ====================

    /**
     * 关键字搜索（支持企业名称、事件名称、事件描述等字段模糊查询）
     */
    private String keyword;

    /**
     * 事件描述关键字
     */
    private String incidentDescriptionKeyword;

    /**
     * 根本原因关键字
     */
    private String rootCauseKeyword;

    /**
     * 处理措施关键字
     */
    private String handlingMeasuresKeyword;

    /**
     * 预防措施关键字
     */
    private String preventiveMeasuresKeyword;

    /**
     * 经验教训关键字
     */
    private String lessonsLearnedKeyword;

    // ==================== 排序字段 ====================

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection;

    // ==================== 统计查询 ====================

    /**
     * 是否需要统计信息
     */
    private Boolean needStatistics;

    /**
     * 统计维度（按类型、按等级、按状态等）
     */
    private String statisticsDimension;

    /**
     * 分组字段
     */
    private String groupBy;

    // ==================== 导出查询 ====================

    /**
     * 是否导出查询
     */
    private Boolean isExport;

    /**
     * 导出格式
     */
    private String exportFormat;

    /**
     * 导出字段列表
     */
    private String exportFields;

    // ==================== 高级查询 ====================

    /**
     * 是否为重大事件（总经济损失超过阈值或影响程度为重大及以上）
     */
    private Boolean isMajorIncident;

    /**
     * 是否已完成处理
     */
    private Boolean isHandlingCompleted;

    /**
     * 是否超期处理（处理完成时间超过预期）
     */
    private Boolean isOverdueHandling;

    /**
     * 是否有经济损失
     */
    private Boolean hasEconomicLoss;

    /**
     * 是否触发应急响应
     */
    private Boolean hasEmergencyResponse;

}
