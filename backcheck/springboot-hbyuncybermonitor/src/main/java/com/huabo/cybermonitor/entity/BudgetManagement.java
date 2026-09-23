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
 * 预算管理实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_BUDGET_MANAGEMENT")
@Schema(name="BudgetManagement", description="预算管理实体")
public class BudgetManagement {

    @Schema(name = "预算ID")
    @TableId(value = "BUDGET_ID", type = IdType.ASSIGN_UUID)
    private String budgetId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "预算类型")
    @TableField("BUDGET_TYPE")
    private String budgetType;

    @Schema(name = "预算名称")
    @TableField("BUDGET_NAME")
    private String budgetName;

    @Schema(name = "预算描述")
    @TableField("BUDGET_DESCRIPTION")
    private String budgetDescription;

    @Schema(name = "预算年度")
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    @Schema(name = "预算期间")
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    @Schema(name = "预算开始日期")
    @TableField("BUDGET_START_DATE")
    private LocalDate budgetStartDate;

    @Schema(name = "预算结束日期")
    @TableField("BUDGET_END_DATE")
    private LocalDate budgetEndDate;

    @Schema(name = "预算状态")
    @TableField("BUDGET_STATUS")
    private String budgetStatus;

    @Schema(name = "预算版本")
    @TableField("BUDGET_VERSION")
    private String budgetVersion;

    @Schema(name = "预算总额")
    @TableField("TOTAL_BUDGET_AMOUNT")
    private BigDecimal totalBudgetAmount;

    @Schema(name = "收入预算")
    @TableField("REVENUE_BUDGET")
    private BigDecimal revenueBudget;

    @Schema(name = "支出预算")
    @TableField("EXPENSE_BUDGET")
    private BigDecimal expenseBudget;

    @Schema(name = "投资预算")
    @TableField("INVESTMENT_BUDGET")
    private BigDecimal investmentBudget;

    @Schema(name = "资金预算")
    @TableField("CASH_BUDGET")
    private BigDecimal cashBudget;

    @Schema(name = "人员预算")
    @TableField("PERSONNEL_BUDGET")
    private BigDecimal personnelBudget;

    @Schema(name = "运营预算")
    @TableField("OPERATING_BUDGET")
    private BigDecimal operatingBudget;

    @Schema(name = "资本预算")
    @TableField("CAPITAL_BUDGET")
    private BigDecimal capitalBudget;

    @Schema(name = "已执行金额")
    @TableField("EXECUTED_AMOUNT")
    private BigDecimal executedAmount;

    @Schema(name = "执行进度")
    @TableField("EXECUTION_PROGRESS")
    private BigDecimal executionProgress;

    @Schema(name = "执行率")
    @TableField("EXECUTION_RATE")
    private BigDecimal executionRate;

    @Schema(name = "剩余预算")
    @TableField("REMAINING_BUDGET")
    private BigDecimal remainingBudget;

    @Schema(name = "预算差异")
    @TableField("BUDGET_VARIANCE")
    private BigDecimal budgetVariance;

    @Schema(name = "差异率")
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    @Schema(name = "预算编制负责人")
    @TableField("BUDGET_MANAGER")
    private String budgetManager;

    @Schema(name = "预算编制部门")
    @TableField("BUDGET_DEPARTMENT")
    private String budgetDepartment;

    @Schema(name = "参与编制人员")
    @TableField("BUDGET_PARTICIPANTS")
    private String budgetParticipants;

    @Schema(name = "编制开始时间")
    @TableField("COMPILATION_START_TIME")
    private LocalDateTime compilationStartTime;

    @Schema(name = "编制结束时间")
    @TableField("COMPILATION_END_TIME")
    private LocalDateTime compilationEndTime;

    @Schema(name = "编制方法")
    @TableField("COMPILATION_METHOD")
    private String compilationMethod;

    @Schema(name = "编制依据")
    @TableField("COMPILATION_BASIS")
    private String compilationBasis;

    @Schema(name = "编制原则")
    @TableField("COMPILATION_PRINCIPLES")
    private String compilationPrinciples;

    @Schema(name = "审批状态")
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @Schema(name = "审批流程")
    @TableField("APPROVAL_PROCESS")
    private String approvalProcess;

    @Schema(name = "初审人")
    @TableField("FIRST_APPROVER")
    private String firstApprover;

    @Schema(name = "初审时间")
    @TableField("FIRST_APPROVAL_TIME")
    private LocalDateTime firstApprovalTime;

    @Schema(name = "初审意见")
    @TableField("FIRST_APPROVAL_OPINION")
    private String firstApprovalOpinion;

    @Schema(name = "终审人")
    @TableField("FINAL_APPROVER")
    private String finalApprover;

    @Schema(name = "终审时间")
    @TableField("FINAL_APPROVAL_TIME")
    private LocalDateTime finalApprovalTime;

    @Schema(name = "终审意见")
    @TableField("FINAL_APPROVAL_OPINION")
    private String finalApprovalOpinion;

    @Schema(name = "执行开始时间")
    @TableField("EXECUTION_START_TIME")
    private LocalDateTime executionStartTime;

    @Schema(name = "执行监控频率")
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

    @Schema(name = "执行质量评分")
    @TableField("EXECUTION_QUALITY_SCORE")
    private BigDecimal executionQualityScore;

    @Schema(name = "执行效率评分")
    @TableField("EXECUTION_EFFICIENCY_SCORE")
    private BigDecimal executionEfficiencyScore;

    @Schema(name = "执行合规性评分")
    @TableField("EXECUTION_COMPLIANCE_SCORE")
    private BigDecimal executionComplianceScore;

    @Schema(name = "执行创新性评分")
    @TableField("EXECUTION_INNOVATION_SCORE")
    private BigDecimal executionInnovationScore;

    @Schema(name = "调整次数")
    @TableField("ADJUSTMENT_COUNT")
    private Integer adjustmentCount;

    @Schema(name = "最后调整时间")
    @TableField("LAST_ADJUSTMENT_TIME")
    private LocalDateTime lastAdjustmentTime;

    @Schema(name = "调整原因")
    @TableField("ADJUSTMENT_REASON")
    private String adjustmentReason;

    @Schema(name = "调整内容")
    @TableField("ADJUSTMENT_CONTENT")
    private String adjustmentContent;

    @Schema(name = "调整金额")
    @TableField("ADJUSTMENT_AMOUNT")
    private BigDecimal adjustmentAmount;

    @Schema(name = "调整审批人")
    @TableField("ADJUSTMENT_APPROVER")
    private String adjustmentApprover;

    @Schema(name = "调整审批时间")
    @TableField("ADJUSTMENT_APPROVAL_TIME")
    private LocalDateTime adjustmentApprovalTime;

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

    @Schema(name = "风险评估")
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    @Schema(name = "风险等级")
    @TableField("RISK_LEVEL")
    private String riskLevel;

    @Schema(name = "风险控制措施")
    @TableField("RISK_CONTROL_MEASURES")
    private String riskControlMeasures;

    @Schema(name = "绩效目标")
    @TableField("PERFORMANCE_TARGETS")
    private String performanceTargets;

    @Schema(name = "绩效指标")
    @TableField("PERFORMANCE_INDICATORS")
    private String performanceIndicators;

    @Schema(name = "绩效评价结果")
    @TableField("PERFORMANCE_EVALUATION_RESULT")
    private String performanceEvaluationResult;

    @Schema(name = "改进建议")
    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    @Schema(name = "最佳实践")
    @TableField("BEST_PRACTICES")
    private String bestPractices;

    @Schema(name = "经验总结")
    @TableField("LESSONS_LEARNED")
    private String lessonsLearned;

    @Schema(name = "能力提升")
    @TableField("CAPABILITY_ENHANCEMENT")
    private String capabilityEnhancement;

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
