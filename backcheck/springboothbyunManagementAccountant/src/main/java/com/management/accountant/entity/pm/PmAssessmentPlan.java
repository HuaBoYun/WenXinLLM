package com.management.accountant.entity.pm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核方案配置实体
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_pm_assessment_plan")
public class PmAssessmentPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 方案ID
     */
    @TableId(value = "plan_id", type = IdType.AUTO)
    private Long planId;

    /**
     * 方案编码
     */
    @TableField("plan_code")
    private String planCode;

    /**
     * 方案名称
     */
    @TableField("plan_name")
    private String planName;

    /**
     * 方案描述
     */
    @TableField("plan_description")
    private String planDescription;

    /**
     * 方案类型(ANNUAL-年度考核/QUARTERLY-季度考核/MONTHLY-月度考核/PROJECT-项目考核)
     */
    @TableField("plan_type")
    private String planType;

    /**
     * 考核模式(INDIVIDUAL-个人考核/TEAM-团队考核/DEPARTMENT-部门考核/COMPANY-公司考核)
     */
    @TableField("assessment_mode")
    private String assessmentMode;

    /**
     * 考核周期(ANNUAL-年度/QUARTERLY-季度/MONTHLY-月度/WEEKLY-周度)
     */
    @TableField("assessment_cycle")
    private String assessmentCycle;

    /**
     * 考核年度
     */
    @TableField("assessment_year")
    private Integer assessmentYear;

    /**
     * 考核期间
     */
    @TableField("assessment_period")
    private String assessmentPeriod;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @TableField("organization_name")
    private String organizationName;

    /**
     * 适用范围(ALL-全员/DEPARTMENT-部门/POSITION-岗位/INDIVIDUAL-个人)
     */
    @TableField("applicable_scope")
    private String applicableScope;

    /**
     * 适用对象IDs(JSON格式)
     */
    @TableField("applicable_targets")
    private String applicableTargets;

    /**
     * 方案状态(DRAFT-草稿/ACTIVE-激活/PAUSED-暂停/COMPLETED-完成/CANCELLED-取消)
     */
    @TableField("plan_status")
    private String planStatus;

    /**
     * 审批状态(PENDING-待审批/APPROVED-已审批/REJECTED-已拒绝)
     */
    @TableField("approval_status")
    private String approvalStatus;

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
     * 评分方式(SCORE-分数制/GRADE-等级制/RANKING-排名制)
     */
    @TableField("scoring_method")
    private String scoringMethod;

    /**
     * 总分值
     */
    @TableField("total_score")
    private BigDecimal totalScore;

    /**
     * 及格分数
     */
    @TableField("pass_score")
    private BigDecimal passScore;

    /**
     * 优秀分数
     */
    @TableField("excellent_score")
    private BigDecimal excellentScore;

    /**
     * 等级设置(JSON格式)
     */
    @TableField("grade_settings")
    private String gradeSettings;

    /**
     * 权重配置(JSON格式)
     */
    @TableField("weight_config")
    private String weightConfig;

    /**
     * 指标体系ID
     */
    @TableField("indicator_system_id")
    private Long indicatorSystemId;

    /**
     * 指标体系名称
     */
    @TableField("indicator_system_name")
    private String indicatorSystemName;

    /**
     * 考核流程ID
     */
    @TableField("process_id")
    private Long processId;

    /**
     * 考核流程名称
     */
    @TableField("process_name")
    private String processName;

    /**
     * 流程配置(JSON格式)
     */
    @TableField("process_config")
    private String processConfig;

    /**
     * 自评权重
     */
    @TableField("self_evaluation_weight")
    private BigDecimal selfEvaluationWeight;

    /**
     * 上级评价权重
     */
    @TableField("superior_evaluation_weight")
    private BigDecimal superiorEvaluationWeight;

    /**
     * 同级评价权重
     */
    @TableField("peer_evaluation_weight")
    private BigDecimal peerEvaluationWeight;

    /**
     * 下级评价权重
     */
    @TableField("subordinate_evaluation_weight")
    private BigDecimal subordinateEvaluationWeight;

    /**
     * 客户评价权重
     */
    @TableField("customer_evaluation_weight")
    private BigDecimal customerEvaluationWeight;

    /**
     * 是否启用360度评估
     */
    @TableField("enable_360_evaluation")
    private Integer enable360Evaluation;

    /**
     * 是否启用目标考核
     */
    @TableField("enable_target_assessment")
    private Integer enableTargetAssessment;

    /**
     * 是否启用能力考核
     */
    @TableField("enable_competency_assessment")
    private Integer enableCompetencyAssessment;

    /**
     * 是否启用行为考核
     */
    @TableField("enable_behavior_assessment")
    private Integer enableBehaviorAssessment;

    /**
     * 是否启用价值观考核
     */
    @TableField("enable_value_assessment")
    private Integer enableValueAssessment;

    /**
     * 考核结果应用(PROMOTION-晋升/SALARY-薪酬/TRAINING-培训/DEVELOPMENT-发展)
     */
    @TableField("result_application")
    private String resultApplication;

    /**
     * 结果公开范围(PRIVATE-私有/DEPARTMENT-部门/COMPANY-公司)
     */
    @TableField("result_visibility")
    private String resultVisibility;

    /**
     * 申诉期限(天)
     */
    @TableField("appeal_deadline")
    private Integer appealDeadline;

    /**
     * 是否允许申诉
     */
    @TableField("allow_appeal")
    private Integer allowAppeal;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 是否默认方案
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

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
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

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
     * 是否删除(0-未删除 1-已删除)
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Integer version;
}
