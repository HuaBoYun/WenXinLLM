package com.management.accountant.entity.ts;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 税务筹划实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ts_tax_planning")
public class TsTaxPlanning implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 筹划ID
     */
    @TableId(value = "planning_id", type = IdType.AUTO)
    private Long planningId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 筹划编号
     */
    @TableField("planning_code")
    private String planningCode;

    /**
     * 筹划名称
     */
    @TableField("planning_name")
    private String planningName;

    /**
     * 筹划类型
     */
    @TableField("planning_type")
    private String planningType;

    /**
     * 筹划状态
     */
    @TableField("planning_status")
    private String planningStatus;

    /**
     * 筹划优先级
     */
    @TableField("priority")
    private String priority;

    /**
     * 筹划描述
     */
    @TableField("planning_description")
    private String planningDescription;

    /**
     * 筹划目标
     */
    @TableField("planning_objective")
    private String planningObjective;

    /**
     * 筹划方案
     */
    @TableField("planning_scheme")
    private String planningScheme;

    /**
     * 筹划策略
     */
    @TableField("planning_strategy")
    private String planningStrategy;

    /**
     * 税种类型
     */
    @TableField("tax_type")
    private String taxType;

    /**
     * 适用税率
     */
    @TableField("applicable_tax_rate")
    private BigDecimal applicableTaxRate;

    /**
     * 筹划前税负
     */
    @TableField("tax_burden_before")
    private BigDecimal taxBurdenBefore;

    /**
     * 筹划后税负
     */
    @TableField("tax_burden_after")
    private BigDecimal taxBurdenAfter;

    /**
     * 节税金额
     */
    @TableField("tax_saving_amount")
    private BigDecimal taxSavingAmount;

    /**
     * 节税比例
     */
    @TableField("tax_saving_rate")
    private BigDecimal taxSavingRate;

    /**
     * 筹划成本
     */
    @TableField("planning_cost")
    private BigDecimal planningCost;

    /**
     * 净收益
     */
    @TableField("net_benefit")
    private BigDecimal netBenefit;

    /**
     * 投资回报率
     */
    @TableField("roi")
    private BigDecimal roi;

    /**
     * 风险等级
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 风险评分
     */
    @TableField("risk_score")
    private BigDecimal riskScore;

    /**
     * 风险描述
     */
    @TableField("risk_description")
    private String riskDescription;

    /**
     * 合规性评估
     */
    @TableField("compliance_assessment")
    private String complianceAssessment;

    /**
     * 合规性等级
     */
    @TableField("compliance_level")
    private String complianceLevel;

    /**
     * 法律依据
     */
    @TableField("legal_basis")
    private String legalBasis;

    /**
     * 政策依据
     */
    @TableField("policy_basis")
    private String policyBasis;

    /**
     * 实施条件
     */
    @TableField("implementation_conditions")
    private String implementationConditions;

    /**
     * 实施步骤
     */
    @TableField("implementation_steps")
    private String implementationSteps;

    /**
     * 实施时间表
     */
    @TableField("implementation_timeline")
    private String implementationTimeline;

    /**
     * 责任人
     */
    @TableField("responsible_person")
    private String responsiblePerson;

    /**
     * 责任部门
     */
    @TableField("responsible_department")
    private String responsibleDepartment;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 计划完成时间
     */
    @TableField("planned_completion_time")
    private LocalDateTime plannedCompletionTime;

    /**
     * 实际完成时间
     */
    @TableField("actual_completion_time")
    private LocalDateTime actualCompletionTime;

    /**
     * 执行进度
     */
    @TableField("execution_progress")
    private BigDecimal executionProgress;

    /**
     * 执行状态
     */
    @TableField("execution_status")
    private String executionStatus;

    /**
     * 执行结果
     */
    @TableField("execution_result")
    private String executionResult;

    /**
     * 效果评估
     */
    @TableField("effectiveness_evaluation")
    private String effectivenessEvaluation;

    /**
     * 实际节税金额
     */
    @TableField("actual_tax_saving")
    private BigDecimal actualTaxSaving;

    /**
     * 实际投资回报率
     */
    @TableField("actual_roi")
    private BigDecimal actualRoi;

    /**
     * 监控指标
     */
    @TableField("monitoring_indicators")
    private String monitoringIndicators;

    /**
     * 监控频率
     */
    @TableField("monitoring_frequency")
    private String monitoringFrequency;

    /**
     * 预警阈值
     */
    @TableField("alert_threshold")
    private BigDecimal alertThreshold;

    /**
     * 调整建议
     */
    @TableField("adjustment_suggestions")
    private String adjustmentSuggestions;

    /**
     * 后续计划
     */
    @TableField("follow_up_plan")
    private String followUpPlan;

    /**
     * 关联项目
     */
    @TableField("related_projects")
    private String relatedProjects;

    /**
     * 关联合同
     */
    @TableField("related_contracts")
    private String relatedContracts;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
