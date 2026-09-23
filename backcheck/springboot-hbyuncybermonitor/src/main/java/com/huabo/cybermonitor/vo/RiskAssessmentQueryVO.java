package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 风险评估查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskAssessmentQueryVO extends BaseVo {

    /**
     * 风险评估ID
     */
    private String riskAssessmentId;

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 评估年度
     */
    private Integer assessmentYear;

    /**
     * 评估期间
     */
    private Integer assessmentPeriod;

    /**
     * 评估日期开始
     */
    private LocalDate assessmentDateStart;

    /**
     * 评估日期结束
     */
    private LocalDate assessmentDateEnd;

    /**
     * 评估类型
     */
    private String assessmentType;

    /**
     * 评估方法
     */
    private String assessmentMethod;

    /**
     * 评估状态
     */
    private String assessmentStatus;

    /**
     * 数据来源
     */
    private String dataSource;

    // ==================== 风险等级查询 ====================

    /**
     * 综合风险等级
     */
    private String overallRiskLevel;

    /**
     * 财务风险等级
     */
    private String financialRiskLevel;

    /**
     * 经营风险等级
     */
    private String operationalRiskLevel;

    /**
     * 合规风险等级
     */
    private String complianceRiskLevel;

    /**
     * 治理风险等级
     */
    private String governanceRiskLevel;

    /**
     * 外部风险等级
     */
    private String externalRiskLevel;

    // ==================== 风险评分范围查询 ====================

    /**
     * 综合风险评分最小值
     */
    private BigDecimal overallRiskScoreMin;

    /**
     * 综合风险评分最大值
     */
    private BigDecimal overallRiskScoreMax;

    /**
     * 财务风险评分最小值
     */
    private BigDecimal financialRiskScoreMin;

    /**
     * 财务风险评分最大值
     */
    private BigDecimal financialRiskScoreMax;

    /**
     * 经营风险评分最小值
     */
    private BigDecimal operationalRiskScoreMin;

    /**
     * 经营风险评分最大值
     */
    private BigDecimal operationalRiskScoreMax;

    // ==================== 预警查询 ====================

    /**
     * 是否触发预警
     */
    private Boolean isWarningTriggered;

    /**
     * 预警等级
     */
    private String warningLevel;

    /**
     * 预警类型
     */
    private String warningType;

    /**
     * 预警触发时间开始
     */
    private LocalDateTime warningTriggerTimeStart;

    /**
     * 预警触发时间结束
     */
    private LocalDateTime warningTriggerTimeEnd;

    // ==================== 风险趋势查询 ====================

    /**
     * 风险趋势
     */
    private String riskTrend;

    /**
     * 风险变化幅度最小值
     */
    private BigDecimal riskChangeMagnitudeMin;

    /**
     * 风险变化幅度最大值
     */
    private BigDecimal riskChangeMagnitudeMax;

    // ==================== 风险因素查询 ====================

    /**
     * 风险因素数量最小值
     */
    private Integer riskFactorCountMin;

    /**
     * 风险因素数量最大值
     */
    private Integer riskFactorCountMax;

    /**
     * 高风险因素数量最小值
     */
    private Integer highRiskFactorCountMin;

    /**
     * 高风险因素数量最大值
     */
    private Integer highRiskFactorCountMax;

    /**
     * 风险关联度最小值
     */
    private BigDecimal riskCorrelationMin;

    /**
     * 风险关联度最大值
     */
    private BigDecimal riskCorrelationMax;

    /**
     * 风险集中度最小值
     */
    private BigDecimal riskConcentrationMin;

    /**
     * 风险集中度最大值
     */
    private BigDecimal riskConcentrationMax;

    // ==================== 应对策略查询 ====================

    /**
     * 风险应对策略
     */
    private String riskResponseStrategy;

    /**
     * 责任部门
     */
    private String responsibleDepartment;

    /**
     * 责任人
     */
    private String responsiblePerson;

    /**
     * 整改期限开始
     */
    private LocalDate rectificationDeadlineStart;

    /**
     * 整改期限结束
     */
    private LocalDate rectificationDeadlineEnd;

    // ==================== 评估结果查询 ====================

    /**
     * 下次评估日期开始
     */
    private LocalDate nextAssessmentDateStart;

    /**
     * 下次评估日期结束
     */
    private LocalDate nextAssessmentDateEnd;

    /**
     * 评估有效期最小值
     */
    private Integer assessmentValidityPeriodMin;

    /**
     * 评估有效期最大值
     */
    private Integer assessmentValidityPeriodMax;

    // ==================== 审核信息查询 ====================

    /**
     * 评估人员
     */
    private String assessor;

    /**
     * 审核人员
     */
    private String reviewer;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 审核时间开始
     */
    private LocalDateTime reviewTimeStart;

    /**
     * 审核时间结束
     */
    private LocalDateTime reviewTimeEnd;

    /**
     * 批准人员
     */
    private String approver;

    /**
     * 批准时间开始
     */
    private LocalDateTime approvalTimeStart;

    /**
     * 批准时间结束
     */
    private LocalDateTime approvalTimeEnd;

    // ==================== 时间范围查询 ====================

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
     * 关键字搜索（支持企业名称、评估结论、风险建议等字段模糊查询）
     */
    private String keyword;

    /**
     * 主要风险因素关键字
     */
    private String riskFactorsKeyword;

    /**
     * 评估结论关键字
     */
    private String conclusionKeyword;

    /**
     * 风险建议关键字
     */
    private String recommendationsKeyword;

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
     * 统计维度（按年度、按类型、按等级等）
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

}
