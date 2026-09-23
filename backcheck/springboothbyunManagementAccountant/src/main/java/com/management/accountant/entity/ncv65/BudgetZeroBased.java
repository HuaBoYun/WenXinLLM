package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 零基预算实体类
 * 
 * @description 零基预算管理实体，支持零基预算的配置和执行管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_ZERO_BASED")
public class BudgetZeroBased implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 零基预算编码
     */
    @TableField("ZBB_CODE")
    private String zbbCode;

    /**
     * 零基预算名称
     */
    @TableField("ZBB_NAME")
    private String zbbName;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 决策单元类型：DEPARTMENT-部门，PROJECT-项目，ACTIVITY-活动，FUNCTION-职能
     */
    @TableField("DECISION_UNIT_TYPE")
    private String decisionUnitType;

    /**
     * 决策单元ID
     */
    @TableField("DECISION_UNIT_ID")
    private String decisionUnitId;

    /**
     * 决策单元名称
     */
    @TableField("DECISION_UNIT_NAME")
    private String decisionUnitName;

    /**
     * 决策包类型：BASIC-基本包，INCREMENTAL-增量包，ALTERNATIVE-替代包
     */
    @TableField("DECISION_PACKAGE_TYPE")
    private String decisionPackageType;

    /**
     * 决策包级别：ESSENTIAL-必需，DESIRABLE-期望，OPTIONAL-可选
     */
    @TableField("DECISION_PACKAGE_LEVEL")
    private String decisionPackageLevel;

    /**
     * 优先级：HIGH-高，MEDIUM-中，LOW-低
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 优先级分数
     */
    @TableField("PRIORITY_SCORE")
    private BigDecimal priorityScore;

    /**
     * 成本效益比
     */
    @TableField("COST_BENEFIT_RATIO")
    private BigDecimal costBenefitRatio;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 最小预算金额
     */
    @TableField("MINIMUM_BUDGET_AMOUNT")
    private BigDecimal minimumBudgetAmount;

    /**
     * 最大预算金额
     */
    @TableField("MAXIMUM_BUDGET_AMOUNT")
    private BigDecimal maximumBudgetAmount;

    /**
     * 基准预算金额
     */
    @TableField("BASELINE_BUDGET_AMOUNT")
    private BigDecimal baselineBudgetAmount;

    /**
     * 增量预算金额
     */
    @TableField("INCREMENTAL_BUDGET_AMOUNT")
    private BigDecimal incrementalBudgetAmount;

    /**
     * 活动描述
     */
    @TableField("ACTIVITY_DESCRIPTION")
    private String activityDescription;

    /**
     * 目标和目的
     */
    @TableField("OBJECTIVES")
    private String objectives;

    /**
     * 预期效益
     */
    @TableField("EXPECTED_BENEFITS")
    private String expectedBenefits;

    /**
     * 关键绩效指标（JSON格式）
     */
    @TableField("KEY_PERFORMANCE_INDICATORS")
    private String keyPerformanceIndicators;

    /**
     * 资源需求（JSON格式）
     */
    @TableField("RESOURCE_REQUIREMENTS")
    private String resourceRequirements;

    /**
     * 风险评估
     */
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    /**
     * 替代方案（JSON格式）
     */
    @TableField("ALTERNATIVES")
    private String alternatives;

    /**
     * 不执行的后果
     */
    @TableField("CONSEQUENCES_OF_NOT_FUNDING")
    private String consequencesOfNotFunding;

    /**
     * 成本分析（JSON格式）
     */
    @TableField("COST_ANALYSIS")
    private String costAnalysis;

    /**
     * 效益分析（JSON格式）
     */
    @TableField("BENEFIT_ANALYSIS")
    private String benefitAnalysis;

    /**
     * 投资回报率
     */
    @TableField("RETURN_ON_INVESTMENT")
    private BigDecimal returnOnInvestment;

    /**
     * 净现值
     */
    @TableField("NET_PRESENT_VALUE")
    private BigDecimal netPresentValue;

    /**
     * 内部收益率
     */
    @TableField("INTERNAL_RATE_OF_RETURN")
    private BigDecimal internalRateOfReturn;

    /**
     * 回收期
     */
    @TableField("PAYBACK_PERIOD")
    private BigDecimal paybackPeriod;

    /**
     * 排名
     */
    @TableField("RANKING")
    private Integer ranking;

    /**
     * 累计排名分数
     */
    @TableField("CUMULATIVE_RANKING_SCORE")
    private BigDecimal cumulativeRankingScore;

    /**
     * 预算状态：DRAFT-草稿，SUBMITTED-已提交，APPROVED-已批准，REJECTED-已拒绝
     */
    @TableField("BUDGET_STATUS")
    private String budgetStatus;

    /**
     * 审批状态：PENDING-待审批，APPROVED-已批准，REJECTED-已拒绝
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 资金状态：UNFUNDED-未获资金，PARTIALLY_FUNDED-部分获资金，FULLY_FUNDED-完全获资金
     */
    @TableField("FUNDING_STATUS")
    private String fundingStatus;

    /**
     * 获得资金金额
     */
    @TableField("FUNDED_AMOUNT")
    private BigDecimal fundedAmount;

    /**
     * 资金分配日期
     */
    @TableField("FUNDING_DATE")
    private LocalDateTime fundingDate;

    /**
     * 执行状态：NOT_STARTED-未开始，IN_PROGRESS-进行中，COMPLETED-已完成，CANCELLED-已取消
     */
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    /**
     * 执行开始日期
     */
    @TableField("EXECUTION_START_DATE")
    private LocalDateTime executionStartDate;

    /**
     * 执行结束日期
     */
    @TableField("EXECUTION_END_DATE")
    private LocalDateTime executionEndDate;

    /**
     * 实际支出金额
     */
    @TableField("ACTUAL_EXPENSE_AMOUNT")
    private BigDecimal actualExpenseAmount;

    /**
     * 预算执行率
     */
    @TableField("BUDGET_EXECUTION_RATE")
    private BigDecimal budgetExecutionRate;

    /**
     * 绩效评估结果
     */
    @TableField("PERFORMANCE_EVALUATION")
    private String performanceEvaluation;

    /**
     * 绩效得分
     */
    @TableField("PERFORMANCE_SCORE")
    private BigDecimal performanceScore;

    /**
     * 负责人ID
     */
    @TableField("RESPONSIBLE_PERSON_ID")
    private String responsiblePersonId;

    /**
     * 负责人姓名
     */
    @TableField("RESPONSIBLE_PERSON_NAME")
    private String responsiblePersonName;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 审批人
     */
    @TableField("APPROVED_BY")
    private String approvedBy;

    /**
     * 审批时间
     */
    @TableField("APPROVED_TIME")
    private LocalDateTime approvedTime;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // ==================== 常量定义 ====================

    /**
     * 决策单元类型常量
     */
    public static final String DECISION_UNIT_TYPE_DEPARTMENT = "DEPARTMENT";
    public static final String DECISION_UNIT_TYPE_PROJECT = "PROJECT";
    public static final String DECISION_UNIT_TYPE_ACTIVITY = "ACTIVITY";
    public static final String DECISION_UNIT_TYPE_FUNCTION = "FUNCTION";

    /**
     * 决策包类型常量
     */
    public static final String DECISION_PACKAGE_TYPE_BASIC = "BASIC";
    public static final String DECISION_PACKAGE_TYPE_INCREMENTAL = "INCREMENTAL";
    public static final String DECISION_PACKAGE_TYPE_ALTERNATIVE = "ALTERNATIVE";

    /**
     * 决策包级别常量
     */
    public static final String DECISION_PACKAGE_LEVEL_ESSENTIAL = "ESSENTIAL";
    public static final String DECISION_PACKAGE_LEVEL_DESIRABLE = "DESIRABLE";
    public static final String DECISION_PACKAGE_LEVEL_OPTIONAL = "OPTIONAL";

    /**
     * 优先级常量
     */
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_LOW = "LOW";

    /**
     * 预算状态常量
     */
    public static final String BUDGET_STATUS_DRAFT = "DRAFT";
    public static final String BUDGET_STATUS_SUBMITTED = "SUBMITTED";
    public static final String BUDGET_STATUS_APPROVED = "APPROVED";
    public static final String BUDGET_STATUS_REJECTED = "REJECTED";

    /**
     * 审批状态常量
     */
    public static final String APPROVAL_STATUS_PENDING = "PENDING";
    public static final String APPROVAL_STATUS_APPROVED = "APPROVED";
    public static final String APPROVAL_STATUS_REJECTED = "REJECTED";

    /**
     * 资金状态常量
     */
    public static final String FUNDING_STATUS_UNFUNDED = "UNFUNDED";
    public static final String FUNDING_STATUS_PARTIALLY_FUNDED = "PARTIALLY_FUNDED";
    public static final String FUNDING_STATUS_FULLY_FUNDED = "FULLY_FUNDED";

    /**
     * 执行状态常量
     */
    public static final String EXECUTION_STATUS_NOT_STARTED = "NOT_STARTED";
    public static final String EXECUTION_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String EXECUTION_STATUS_COMPLETED = "COMPLETED";
    public static final String EXECUTION_STATUS_CANCELLED = "CANCELLED";
}
