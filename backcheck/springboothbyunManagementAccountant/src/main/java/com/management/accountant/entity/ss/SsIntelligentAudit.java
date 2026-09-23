package com.management.accountant.entity.ss;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 智能审核实体类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ss_intelligent_audit")
public class SsIntelligentAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核ID
     */
    @TableId(value = "audit_id", type = IdType.AUTO)
    private Long auditId;

    /**
     * 审核编码
     */
    @TableField("audit_code")
    private String auditCode;

    /**
     * 审核标题
     */
    @TableField("audit_title")
    private String auditTitle;

    /**
     * 审核类型
     */
    @TableField("audit_type")
    private String auditType;

    /**
     * 审核分类
     */
    @TableField("audit_category")
    private String auditCategory;

    /**
     * 审核状态
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 审核优先级
     */
    @TableField("priority_level")
    private String priorityLevel;

    /**
     * 目标对象ID
     */
    @TableField("target_object_id")
    private Long targetObjectId;

    /**
     * 目标对象类型
     */
    @TableField("target_object_type")
    private String targetObjectType;

    /**
     * 目标对象名称
     */
    @TableField("target_object_name")
    private String targetObjectName;

    /**
     * 审核规则ID
     */
    @TableField("audit_rule_id")
    private Long auditRuleId;

    /**
     * 审核规则名称
     */
    @TableField("audit_rule_name")
    private String auditRuleName;

    /**
     * 审核规则版本
     */
    @TableField("rule_version")
    private String ruleVersion;

    /**
     * 审核引擎类型
     */
    @TableField("engine_type")
    private String engineType;

    /**
     * 审核算法
     */
    @TableField("audit_algorithm")
    private String auditAlgorithm;

    /**
     * 审核参数配置
     */
    @TableField("audit_parameters")
    private String auditParameters;

    /**
     * 审核阈值
     */
    @TableField("audit_threshold")
    private BigDecimal auditThreshold;

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
     * 置信度
     */
    @TableField("confidence_level")
    private BigDecimal confidenceLevel;

    /**
     * 审核结果
     */
    @TableField("audit_result")
    private String auditResult;

    /**
     * 审核结论
     */
    @TableField("audit_conclusion")
    private String auditConclusion;

    /**
     * 异常类型
     */
    @TableField("anomaly_type")
    private String anomalyType;

    /**
     * 异常描述
     */
    @TableField("anomaly_description")
    private String anomalyDescription;

    /**
     * 异常严重程度
     */
    @TableField("anomaly_severity")
    private String anomalySeverity;

    /**
     * 预警级别
     */
    @TableField("warning_level")
    private String warningLevel;

    /**
     * 预警消息
     */
    @TableField("warning_message")
    private String warningMessage;

    /**
     * 建议措施
     */
    @TableField("recommended_actions")
    private String recommendedActions;

    /**
     * 审核开始时间
     */
    @TableField("audit_start_time")
    private LocalDateTime auditStartTime;

    /**
     * 审核结束时间
     */
    @TableField("audit_end_time")
    private LocalDateTime auditEndTime;

    /**
     * 审核耗时(秒)
     */
    @TableField("audit_duration")
    private Integer auditDuration;

    /**
     * 审核人员ID
     */
    @TableField("auditor_id")
    private Long auditorId;

    /**
     * 审核人员姓名
     */
    @TableField("auditor_name")
    private String auditorName;

    /**
     * 审核部门ID
     */
    @TableField("audit_dept_id")
    private Long auditDeptId;

    /**
     * 审核部门名称
     */
    @TableField("audit_dept_name")
    private String auditDeptName;

    /**
     * 复核人员ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 复核人员姓名
     */
    @TableField("reviewer_name")
    private String reviewerName;

    /**
     * 复核时间
     */
    @TableField("review_time")
    private LocalDateTime reviewTime;

    /**
     * 复核意见
     */
    @TableField("review_comments")
    private String reviewComments;

    /**
     * 复核结果
     */
    @TableField("review_result")
    private String reviewResult;

    /**
     * 处理状态
     */
    @TableField("processing_status")
    private String processingStatus;

    /**
     * 处理人员ID
     */
    @TableField("processor_id")
    private Long processorId;

    /**
     * 处理人员姓名
     */
    @TableField("processor_name")
    private String processorName;

    /**
     * 处理时间
     */
    @TableField("processing_time")
    private LocalDateTime processingTime;

    /**
     * 处理措施
     */
    @TableField("processing_actions")
    private String processingActions;

    /**
     * 处理结果
     */
    @TableField("processing_result")
    private String processingResult;

    /**
     * 跟进状态
     */
    @TableField("follow_up_status")
    private String followUpStatus;

    /**
     * 跟进记录
     */
    @TableField("follow_up_records")
    private String followUpRecords;

    /**
     * 下次跟进时间
     */
    @TableField("next_follow_up_time")
    private LocalDateTime nextFollowUpTime;

    /**
     * 质量评分
     */
    @TableField("quality_score")
    private BigDecimal qualityScore;

    /**
     * 准确率
     */
    @TableField("accuracy_rate")
    private BigDecimal accuracyRate;

    /**
     * 效率评分
     */
    @TableField("efficiency_score")
    private BigDecimal efficiencyScore;

    /**
     * 满意度评分
     */
    @TableField("satisfaction_score")
    private BigDecimal satisfactionScore;

    /**
     * 机器学习模型ID
     */
    @TableField("ml_model_id")
    private Long mlModelId;

    /**
     * 机器学习模型版本
     */
    @TableField("ml_model_version")
    private String mlModelVersion;

    /**
     * 训练数据集ID
     */
    @TableField("training_dataset_id")
    private Long trainingDatasetId;

    /**
     * 特征向量
     */
    @TableField("feature_vector")
    private String featureVector;

    /**
     * 预测概率
     */
    @TableField("prediction_probability")
    private BigDecimal predictionProbability;

    /**
     * 数据源信息
     */
    @TableField("data_source_info")
    private String dataSourceInfo;

    /**
     * 审核日志
     */
    @TableField("audit_log")
    private String auditLog;

    /**
     * 扩展属性
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

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
     * 逻辑删除标志
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;
}
