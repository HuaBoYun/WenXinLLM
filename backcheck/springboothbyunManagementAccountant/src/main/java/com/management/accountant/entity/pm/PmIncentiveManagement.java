package com.management.accountant.entity.pm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 激励管理实体类
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_pm_incentive_management")
public class PmIncentiveManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 激励ID
     */
    @TableId(value = "incentive_id", type = IdType.AUTO)
    private Long incentiveId;

    /**
     * 激励编码
     */
    @TableField("incentive_code")
    private String incentiveCode;

    /**
     * 激励标题
     */
    @TableField("incentive_title")
    private String incentiveTitle;

    /**
     * 激励类型 (PERFORMANCE:绩效激励, ACHIEVEMENT:成就激励, INNOVATION:创新激励, TEAM:团队激励, SPECIAL:专项激励)
     */
    @TableField("incentive_type")
    private String incentiveType;

    /**
     * 激励分类 (MONETARY:货币激励, NON_MONETARY:非货币激励, MIXED:混合激励)
     */
    @TableField("incentive_category")
    private String incentiveCategory;

    /**
     * 激励状态 (DRAFT:草稿, APPROVED:已审批, ACTIVE:生效中, SUSPENDED:暂停, COMPLETED:已完成, CANCELLED:已取消)
     */
    @TableField("incentive_status")
    private String incentiveStatus;

    /**
     * 激励年度
     */
    @TableField("incentive_year")
    private Integer incentiveYear;

    /**
     * 激励季度
     */
    @TableField("incentive_quarter")
    private Integer incentiveQuarter;

    /**
     * 激励月份
     */
    @TableField("incentive_month")
    private Integer incentiveMonth;

    /**
     * 激励周期 (MONTHLY:月度, QUARTERLY:季度, SEMI_ANNUAL:半年度, ANNUAL:年度, PROJECT:项目制)
     */
    @TableField("incentive_cycle")
    private String incentiveCycle;

    /**
     * 激励范围 (INDIVIDUAL:个人, DEPARTMENT:部门, COMPANY:公司, PROJECT_TEAM:项目团队)
     */
    @TableField("incentive_scope")
    private String incentiveScope;

    /**
     * 目标部门ID
     */
    @TableField("target_dept_id")
    private Long targetDeptId;

    /**
     * 目标部门名称
     */
    @TableField("target_dept_name")
    private String targetDeptName;

    /**
     * 激励负责人ID
     */
    @TableField("incentive_owner_id")
    private Long incentiveOwnerId;

    /**
     * 激励负责人姓名
     */
    @TableField("incentive_owner_name")
    private String incentiveOwnerName;

    /**
     * 激励目标
     */
    @TableField("incentive_objective")
    private String incentiveObjective;

    /**
     * 激励原则
     */
    @TableField("incentive_principles")
    private String incentivePrinciples;

    /**
     * 激励标准
     */
    @TableField("incentive_standards")
    private String incentiveStandards;

    /**
     * 激励规则
     */
    @TableField("incentive_rules")
    private String incentiveRules;

    /**
     * 计算方法 (FIXED:固定金额, PERCENTAGE:百分比, FORMULA:公式计算, TIERED:分层计算)
     */
    @TableField("calculation_method")
    private String calculationMethod;

    /**
     * 计算公式
     */
    @TableField("calculation_formula")
    private String calculationFormula;

    /**
     * 基础金额
     */
    @TableField("base_amount")
    private BigDecimal baseAmount;

    /**
     * 最小金额
     */
    @TableField("min_amount")
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    @TableField("max_amount")
    private BigDecimal maxAmount;

    /**
     * 激励系数
     */
    @TableField("incentive_coefficient")
    private BigDecimal incentiveCoefficient;

    /**
     * 权重配置
     */
    @TableField("weight_config")
    private String weightConfig;

    /**
     * 优先级 (HIGH:高, MEDIUM:中, LOW:低)
     */
    @TableField("priority_level")
    private String priorityLevel;

    /**
     * 计划开始时间
     */
    @TableField("planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 计划结束时间
     */
    @TableField("planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开始时间
     */
    @TableField("actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 申请截止时间
     */
    @TableField("application_deadline")
    private LocalDateTime applicationDeadline;

    /**
     * 发放时间
     */
    @TableField("distribution_time")
    private LocalDateTime distributionTime;

    /**
     * 参与人数
     */
    @TableField("participant_count")
    private Integer participantCount;

    /**
     * 符合条件人数
     */
    @TableField("eligible_count")
    private Integer eligibleCount;

    /**
     * 实际获得人数
     */
    @TableField("actual_recipients")
    private Integer actualRecipients;

    /**
     * 预算总额
     */
    @TableField("total_budget")
    private BigDecimal totalBudget;

    /**
     * 已使用金额
     */
    @TableField("used_amount")
    private BigDecimal usedAmount;

    /**
     * 剩余金额
     */
    @TableField("remaining_amount")
    private BigDecimal remainingAmount;

    /**
     * 平均激励金额
     */
    @TableField("average_amount")
    private BigDecimal averageAmount;

    /**
     * 最高激励金额
     */
    @TableField("max_individual_amount")
    private BigDecimal maxIndividualAmount;

    /**
     * 最低激励金额
     */
    @TableField("min_individual_amount")
    private BigDecimal minIndividualAmount;

    /**
     * 完成率
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 满意度评分
     */
    @TableField("satisfaction_score")
    private BigDecimal satisfactionScore;

    /**
     * 效果评级 (1-5星)
     */
    @TableField("effectiveness_rating")
    private Integer effectivenessRating;

    /**
     * 激励效果
     */
    @TableField("incentive_effect")
    private String incentiveEffect;

    /**
     * 改进建议
     */
    @TableField("improvement_suggestions")
    private String improvementSuggestions;

    /**
     * 激励描述
     */
    @TableField("incentive_description")
    private String incentiveDescription;

    /**
     * 申请条件
     */
    @TableField("application_conditions")
    private String applicationConditions;

    /**
     * 评审标准
     */
    @TableField("review_criteria")
    private String reviewCriteria;

    /**
     * 发放方式 (CASH:现金, TRANSFER:转账, VOUCHER:代金券, GIFT:实物, POINTS:积分)
     */
    @TableField("distribution_method")
    private String distributionMethod;

    /**
     * 发放状态 (PENDING:待发放, PROCESSING:发放中, COMPLETED:已发放, FAILED:发放失败)
     */
    @TableField("distribution_status")
    private String distributionStatus;

    /**
     * 审批状态 (PENDING:待审批, APPROVED:已审批, REJECTED:已拒绝)
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批人姓名
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("approval_comments")
    private String approvalComments;

    /**
     * 是否需要跟进 (0:否, 1:是)
     */
    @TableField("need_follow_up")
    private Integer needFollowUp;

    /**
     * 跟进状态 (PENDING:待跟进, IN_PROGRESS:跟进中, COMPLETED:已完成, CANCELLED:已取消)
     */
    @TableField("follow_up_status")
    private String followUpStatus;

    /**
     * 跟进计划
     */
    @TableField("follow_up_plan")
    private String followUpPlan;

    /**
     * 跟进截止时间
     */
    @TableField("follow_up_deadline")
    private LocalDateTime followUpDeadline;

    /**
     * 跟进记录
     */
    @TableField("follow_up_record")
    private String followUpRecord;

    /**
     * 是否已提醒 (0:否, 1:是)
     */
    @TableField("is_reminded")
    private Integer isReminded;

    /**
     * 提醒时间
     */
    @TableField("reminder_time")
    private LocalDateTime reminderTime;

    /**
     * 通知状态 (PENDING:待通知, SENT:已发送, FAILED:发送失败)
     */
    @TableField("notification_status")
    private String notificationStatus;

    /**
     * 通知时间
     */
    @TableField("notification_time")
    private LocalDateTime notificationTime;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField(value = "created_by_name", fill = FieldFill.INSERT)
    private String createdByName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField(value = "updated_by_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志 (0:未删除, 1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
