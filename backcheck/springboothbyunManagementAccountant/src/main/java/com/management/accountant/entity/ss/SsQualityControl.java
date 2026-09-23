package com.management.accountant.entity.ss;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量管控实体类
 * 
 * @author AI Assistant
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ss_quality_control")
public class SsQualityControl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 质量管控ID
     */
    @TableId(value = "quality_id", type = IdType.AUTO)
    private Long qualityId;

    /**
     * 质量管控编码
     */
    @TableField("quality_code")
    private String qualityCode;

    /**
     * 质量管控名称
     */
    @TableField("quality_name")
    private String qualityName;

    /**
     * 质量管控描述
     */
    @TableField("quality_description")
    private String qualityDescription;

    /**
     * 质量管控类型
     */
    @TableField("quality_type")
    private String qualityType;

    /**
     * 质量管控分类
     */
    @TableField("quality_category")
    private String qualityCategory;

    /**
     * 质量管控状态
     */
    @TableField("quality_status")
    private String qualityStatus;

    /**
     * 质量等级
     */
    @TableField("quality_level")
    private String qualityLevel;

    /**
     * 质量评分
     */
    @TableField("quality_score")
    private BigDecimal qualityScore;

    /**
     * 质量标准ID
     */
    @TableField("standard_id")
    private Long standardId;

    /**
     * 质量标准名称
     */
    @TableField("standard_name")
    private String standardName;

    /**
     * 质量标准版本
     */
    @TableField("standard_version")
    private String standardVersion;

    /**
     * 检测规则ID
     */
    @TableField("detection_rule_id")
    private Long detectionRuleId;

    /**
     * 检测规则名称
     */
    @TableField("detection_rule_name")
    private String detectionRuleName;

    /**
     * 检测方式
     */
    @TableField("detection_method")
    private String detectionMethod;

    /**
     * 检测频率
     */
    @TableField("detection_frequency")
    private String detectionFrequency;

    /**
     * 检测周期
     */
    @TableField("detection_cycle")
    private Integer detectionCycle;

    /**
     * 抽检比例
     */
    @TableField("sampling_ratio")
    private BigDecimal samplingRatio;

    /**
     * 抽检数量
     */
    @TableField("sampling_quantity")
    private Integer samplingQuantity;

    /**
     * 检测结果
     */
    @TableField("detection_result")
    private String detectionResult;

    /**
     * 检测状态
     */
    @TableField("detection_status")
    private String detectionStatus;

    /**
     * 合格率
     */
    @TableField("pass_rate")
    private BigDecimal passRate;

    /**
     * 不合格数量
     */
    @TableField("fail_quantity")
    private Integer failQuantity;

    /**
     * 缺陷类型
     */
    @TableField("defect_type")
    private String defectType;

    /**
     * 缺陷等级
     */
    @TableField("defect_level")
    private String defectLevel;

    /**
     * 缺陷描述
     */
    @TableField("defect_description")
    private String defectDescription;

    /**
     * 改进措施
     */
    @TableField("improvement_measures")
    private String improvementMeasures;

    /**
     * 改进状态
     */
    @TableField("improvement_status")
    private String improvementStatus;

    /**
     * 改进效果
     */
    @TableField("improvement_effect")
    private String improvementEffect;

    /**
     * 质量成本
     */
    @TableField("quality_cost")
    private BigDecimal qualityCost;

    /**
     * 预防成本
     */
    @TableField("prevention_cost")
    private BigDecimal preventionCost;

    /**
     * 评价成本
     */
    @TableField("appraisal_cost")
    private BigDecimal appraisalCost;

    /**
     * 内部失效成本
     */
    @TableField("internal_failure_cost")
    private BigDecimal internalFailureCost;

    /**
     * 外部失效成本
     */
    @TableField("external_failure_cost")
    private BigDecimal externalFailureCost;

    /**
     * 质量培训ID
     */
    @TableField("training_id")
    private Long trainingId;

    /**
     * 质量培训名称
     */
    @TableField("training_name")
    private String trainingName;

    /**
     * 培训状态
     */
    @TableField("training_status")
    private String trainingStatus;

    /**
     * 培训完成率
     */
    @TableField("training_completion_rate")
    private BigDecimal trainingCompletionRate;

    /**
     * 负责人ID
     */
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    /**
     * 负责人姓名
     */
    @TableField("responsible_person_name")
    private String responsiblePersonName;

    /**
     * 检测人员ID
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 检测人员姓名
     */
    @TableField("inspector_name")
    private String inspectorName;

    /**
     * 审核人员ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核人员姓名
     */
    @TableField("reviewer_name")
    private String reviewerName;

    /**
     * 检测开始时间
     */
    @TableField("detection_start_time")
    private LocalDateTime detectionStartTime;

    /**
     * 检测结束时间
     */
    @TableField("detection_end_time")
    private LocalDateTime detectionEndTime;

    /**
     * 检测耗时(分钟)
     */
    @TableField("detection_duration")
    private Integer detectionDuration;

    /**
     * 下次检测时间
     */
    @TableField("next_detection_time")
    private LocalDateTime nextDetectionTime;

    /**
     * 最后检测时间
     */
    @TableField("last_detection_time")
    private LocalDateTime lastDetectionTime;

    /**
     * 检测次数
     */
    @TableField("detection_count")
    private Integer detectionCount;

    /**
     * 通过次数
     */
    @TableField("pass_count")
    private Integer passCount;

    /**
     * 失败次数
     */
    @TableField("fail_count")
    private Integer failCount;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 优先级权重
     */
    @TableField("priority_weight")
    private BigDecimal priorityWeight;

    /**
     * 风险等级
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 风险评估
     */
    @TableField("risk_assessment")
    private String riskAssessment;

    /**
     * 质量目标
     */
    @TableField("quality_target")
    private String qualityTarget;

    /**
     * 质量指标
     */
    @TableField("quality_indicators")
    private String qualityIndicators;

    /**
     * 质量要求
     */
    @TableField("quality_requirements")
    private String qualityRequirements;

    /**
     * 质量计划
     */
    @TableField("quality_plan")
    private String qualityPlan;

    /**
     * 质量报告
     */
    @TableField("quality_report")
    private String qualityReport;

    /**
     * 自动化检测配置
     */
    @TableField("automation_config")
    private String automationConfig;

    /**
     * 通知配置
     */
    @TableField("notification_config")
    private String notificationConfig;

    /**
     * 告警配置
     */
    @TableField("alert_config")
    private String alertConfig;

    /**
     * 扩展属性
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

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
}
