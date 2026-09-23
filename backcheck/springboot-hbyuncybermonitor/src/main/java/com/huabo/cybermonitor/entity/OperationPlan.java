package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 经营计划管理实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("OPERATION_PLAN")
@Schema(name="OperationPlan", description="经营计划管理实体")
public class OperationPlan {

    @Schema(name = "计划ID")
    @TableId(value = "PLAN_ID", type = IdType.ASSIGN_UUID)
    private String planId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "计划类型")
    @TableField("PLAN_TYPE")
    private String planType;

    @Schema(name = "计划名称")
    @TableField("PLAN_NAME")
    private String planName;

    @Schema(name = "计划描述")
    @TableField("PLAN_DESCRIPTION")
    private String planDescription;

    @Schema(name = "计划年度")
    @TableField("PLAN_YEAR")
    private Integer planYear;

    @Schema(name = "计划期间")
    @TableField("PLAN_PERIOD")
    private String planPeriod;

    @Schema(name = "计划开始日期")
    @TableField("PLAN_START_DATE")
    private LocalDate planStartDate;

    @Schema(name = "计划结束日期")
    @TableField("PLAN_END_DATE")
    private LocalDate planEndDate;

    @Schema(name = "计划状态")
    @TableField("PLAN_STATUS")
    private String planStatus;

    @Schema(name = "计划版本")
    @TableField("PLAN_VERSION")
    private String planVersion;

    @Schema(name = "战略目标")
    @TableField("STRATEGIC_OBJECTIVES")
    private String strategicObjectives;

    @Schema(name = "经营目标")
    @TableField("OPERATIONAL_OBJECTIVES")
    private String operationalObjectives;

    @Schema(name = "财务目标")
    @TableField("FINANCIAL_OBJECTIVES")
    private String financialObjectives;

    @Schema(name = "管理目标")
    @TableField("MANAGEMENT_OBJECTIVES")
    private String managementObjectives;

    @Schema(name = "收入计划")
    @TableField("REVENUE_PLAN")
    private BigDecimal revenuePlan;

    @Schema(name = "成本计划")
    @TableField("COST_PLAN")
    private BigDecimal costPlan;

    @Schema(name = "利润计划")
    @TableField("PROFIT_PLAN")
    private BigDecimal profitPlan;

    @Schema(name = "投资计划")
    @TableField("INVESTMENT_PLAN")
    private BigDecimal investmentPlan;

    @Schema(name = "资金计划")
    @TableField("FUNDING_PLAN")
    private BigDecimal fundingPlan;

    @Schema(name = "市场目标")
    @TableField("MARKET_TARGETS")
    private String marketTargets;

    @Schema(name = "产品目标")
    @TableField("PRODUCT_TARGETS")
    private String productTargets;

    @Schema(name = "客户目标")
    @TableField("CUSTOMER_TARGETS")
    private String customerTargets;

    @Schema(name = "质量目标")
    @TableField("QUALITY_TARGETS")
    private String qualityTargets;

    @Schema(name = "创新目标")
    @TableField("INNOVATION_TARGETS")
    private String innovationTargets;

    @Schema(name = "人力资源目标")
    @TableField("HR_TARGETS")
    private String hrTargets;

    @Schema(name = "技术发展目标")
    @TableField("TECHNOLOGY_TARGETS")
    private String technologyTargets;

    @Schema(name = "可持续发展目标")
    @TableField("SUSTAINABILITY_TARGETS")
    private String sustainabilityTargets;

    @Schema(name = "计划制定负责人")
    @TableField("PLAN_MANAGER")
    private String planManager;

    @Schema(name = "计划制定部门")
    @TableField("PLAN_DEPARTMENT")
    private String planDepartment;

    @Schema(name = "参与制定人员")
    @TableField("PLAN_PARTICIPANTS")
    private String planParticipants;

    @Schema(name = "制定开始时间")
    @TableField("FORMULATION_START_TIME")
    private LocalDateTime formulationStartTime;

    @Schema(name = "制定结束时间")
    @TableField("FORMULATION_END_TIME")
    private LocalDateTime formulationEndTime;

    @Schema(name = "制定方法")
    @TableField("FORMULATION_METHOD")
    private String formulationMethod;

    @Schema(name = "制定依据")
    @TableField("FORMULATION_BASIS")
    private String formulationBasis;

    @Schema(name = "制定原则")
    @TableField("FORMULATION_PRINCIPLES")
    private String formulationPrinciples;

    @Schema(name = "制定进度")
    @TableField("FORMULATION_PROGRESS")
    private BigDecimal formulationProgress;

    @Schema(name = "审批状态")
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @Schema(name = "初审人")
    @TableField("FIRST_APPROVER")
    private String firstApprover;

    @Schema(name = "初审时间")
    @TableField("FIRST_APPROVAL_TIME")
    private LocalDateTime firstApprovalTime;

    @Schema(name = "初审意见")
    @TableField("FIRST_APPROVAL_OPINION")
    private String firstApprovalOpinion;

    @Schema(name = "内部审批")
    @TableField("INTERNAL_APPROVAL")
    private String internalApproval;

    @Schema(name = "外部审批")
    @TableField("EXTERNAL_APPROVAL")
    private String externalApproval;

    @Schema(name = "合规审查")
    @TableField("COMPLIANCE_REVIEW")
    private String complianceReview;

    @Schema(name = "风险评估")
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    @Schema(name = "审批人")
    @TableField("APPROVER")
    private String approver;

    @Schema(name = "审批时间")
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    @Schema(name = "审批意见")
    @TableField("APPROVAL_OPINION")
    private String approvalOpinion;

    @Schema(name = "发布状态")
    @TableField("PUBLICATION_STATUS")
    private String publicationStatus;

    @Schema(name = "发布时间")
    @TableField("PUBLICATION_TIME")
    private LocalDateTime publicationTime;

    @Schema(name = "任务分解")
    @TableField("TASK_BREAKDOWN")
    private String taskBreakdown;

    @Schema(name = "责任落实")
    @TableField("RESPONSIBILITY_ASSIGNMENT")
    private String responsibilityAssignment;

    @Schema(name = "执行启动时间")
    @TableField("EXECUTION_START_TIME")
    private LocalDateTime executionStartTime;

    @Schema(name = "执行状态")
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    @Schema(name = "执行进度")
    @TableField("EXECUTION_PROGRESS")
    private BigDecimal executionProgress;

    @Schema(name = "完成情况")
    @TableField("COMPLETION_STATUS")
    private String completionStatus;

    @Schema(name = "关键指标达成")
    @TableField("KEY_INDICATOR_ACHIEVEMENT")
    private String keyIndicatorAchievement;

    @Schema(name = "执行偏差")
    @TableField("EXECUTION_DEVIATION")
    private String executionDeviation;

    @Schema(name = "偏差分析")
    @TableField("DEVIATION_ANALYSIS")
    private String deviationAnalysis;

    @Schema(name = "执行质量")
    @TableField("EXECUTION_QUALITY")
    private String executionQuality;

    @Schema(name = "执行效率")
    @TableField("EXECUTION_EFFICIENCY")
    private String executionEfficiency;

    @Schema(name = "执行效果")
    @TableField("EXECUTION_EFFECTIVENESS")
    private String executionEffectiveness;

    @Schema(name = "执行创新")
    @TableField("EXECUTION_INNOVATION")
    private String executionInnovation;

    @Schema(name = "问题识别")
    @TableField("PROBLEM_IDENTIFICATION")
    private String problemIdentification;

    @Schema(name = "原因分析")
    @TableField("CAUSE_ANALYSIS")
    private String causeAnalysis;

    @Schema(name = "解决方案")
    @TableField("SOLUTIONS")
    private String solutions;

    @Schema(name = "效果验证")
    @TableField("EFFECTIVENESS_VERIFICATION")
    private String effectivenessVerification;

    @Schema(name = "成功经验")
    @TableField("SUCCESS_EXPERIENCES")
    private String successExperiences;

    @Schema(name = "失败教训")
    @TableField("FAILURE_LESSONS")
    private String failureLessons;

    @Schema(name = "改进建议")
    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    @Schema(name = "能力提升")
    @TableField("CAPABILITY_ENHANCEMENT")
    private String capabilityEnhancement;

    @Schema(name = "效果评估")
    @TableField("EFFECTIVENESS_EVALUATION")
    private String effectivenessEvaluation;

    @Schema(name = "调整需求")
    @TableField("ADJUSTMENT_REQUIREMENTS")
    private String adjustmentRequirements;

    @Schema(name = "内部需求")
    @TableField("INTERNAL_REQUIREMENTS")
    private String internalRequirements;

    @Schema(name = "外部需求")
    @TableField("EXTERNAL_REQUIREMENTS")
    private String externalRequirements;

    @Schema(name = "政策变化")
    @TableField("POLICY_CHANGES")
    private String policyChanges;

    @Schema(name = "市场变化")
    @TableField("MARKET_CHANGES")
    private String marketChanges;

    @Schema(name = "调整目标")
    @TableField("ADJUSTMENT_OBJECTIVES")
    private String adjustmentObjectives;

    @Schema(name = "调整内容")
    @TableField("ADJUSTMENT_CONTENT")
    private String adjustmentContent;

    @Schema(name = "调整措施")
    @TableField("ADJUSTMENT_MEASURES")
    private String adjustmentMeasures;

    @Schema(name = "调整时间")
    @TableField("ADJUSTMENT_TIME")
    private LocalDateTime adjustmentTime;

    @Schema(name = "调整审批")
    @TableField("ADJUSTMENT_APPROVAL")
    private String adjustmentApproval;

    @Schema(name = "调整实施")
    @TableField("ADJUSTMENT_IMPLEMENTATION")
    private String adjustmentImplementation;

    @Schema(name = "调整监控")
    @TableField("ADJUSTMENT_MONITORING")
    private String adjustmentMonitoring;

    @Schema(name = "调整评估")
    @TableField("ADJUSTMENT_EVALUATION")
    private String adjustmentEvaluation;

    @Schema(name = "调整合理性")
    @TableField("ADJUSTMENT_RATIONALITY")
    private String adjustmentRationality;

    @Schema(name = "调整及时性")
    @TableField("ADJUSTMENT_TIMELINESS")
    private String adjustmentTimeliness;

    @Schema(name = "调整有效性")
    @TableField("ADJUSTMENT_EFFECTIVENESS")
    private String adjustmentEffectiveness;

    @Schema(name = "调整创新性")
    @TableField("ADJUSTMENT_INNOVATION")
    private String adjustmentInnovation;

    @Schema(name = "监控频率")
    @TableField("MONITORING_FREQUENCY")
    private String monitoringFrequency;

    @Schema(name = "最后监控时间")
    @TableField("LAST_MONITORING_TIME")
    private LocalDateTime lastMonitoringTime;

    @Schema(name = "下次监控时间")
    @TableField("NEXT_MONITORING_TIME")
    private LocalDateTime nextMonitoringTime;

    @Schema(name = "监控负责人")
    @TableField("MONITORING_MANAGER")
    private String monitoringManager;

    @Schema(name = "相关附件")
    @TableField("RELATED_ATTACHMENTS")
    private String relatedAttachments;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "扩展字段1")
    @TableField("EXT_FIELD1")
    private String extField1;

    @Schema(name = "扩展字段2")
    @TableField("EXT_FIELD2")
    private String extField2;

    @Schema(name = "扩展字段3")
    @TableField("EXT_FIELD3")
    private String extField3;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志")
    @TableField("DEL_FLAG")
    private String delFlag;
}
