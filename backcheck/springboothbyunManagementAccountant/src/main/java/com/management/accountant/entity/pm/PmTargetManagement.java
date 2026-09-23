package com.management.accountant.entity.pm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 目标管理实体
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_pm_target_management")
public class PmTargetManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 目标ID，主键
     */
    @TableId(value = "target_id", type = IdType.AUTO)
    private Long targetId;

    /**
     * 目标编码
     */
    @TableField("target_code")
    private String targetCode;

    /**
     * 目标名称
     */
    @TableField("target_name")
    private String targetName;

    /**
     * 目标描述
     */
    @TableField("target_description")
    private String targetDescription;

    /**
     * 目标类型：STRATEGIC-战略目标/OPERATIONAL-运营目标/PERSONAL-个人目标/TEAM-团队目标
     */
    @TableField("target_type")
    private String targetType;

    /**
     * 目标级别：COMPANY-公司级/DEPARTMENT-部门级/TEAM-团队级/INDIVIDUAL-个人级
     */
    @TableField("target_level")
    private String targetLevel;

    /**
     * 目标分类：FINANCIAL-财务类/CUSTOMER-客户类/PROCESS-流程类/LEARNING-学习成长类
     */
    @TableField("target_category")
    private String targetCategory;

    /**
     * 父目标ID
     */
    @TableField("parent_target_id")
    private Long parentTargetId;

    /**
     * 目标路径
     */
    @TableField("target_path")
    private String targetPath;

    /**
     * 目标层级
     */
    @TableField("target_hierarchy")
    private Integer targetHierarchy;

    /**
     * 所属组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 所属组织名称
     */
    @TableField("organization_name")
    private String organizationName;

    /**
     * 目标负责人ID
     */
    @TableField("target_owner_id")
    private Long targetOwnerId;

    /**
     * 目标负责人姓名
     */
    @TableField("target_owner_name")
    private String targetOwnerName;

    /**
     * 目标参与人员，JSON格式存储
     */
    @TableField("target_participants")
    private String targetParticipants;

    /**
     * 目标指标类型：QUANTITATIVE-定量指标/QUALITATIVE-定性指标
     */
    @TableField("indicator_type")
    private String indicatorType;

    /**
     * 目标值
     */
    @TableField("target_value")
    private BigDecimal targetValue;

    /**
     * 目标单位
     */
    @TableField("target_unit")
    private String targetUnit;

    /**
     * 基准值
     */
    @TableField("baseline_value")
    private BigDecimal baselineValue;

    /**
     * 当前值
     */
    @TableField("current_value")
    private BigDecimal currentValue;

    /**
     * 完成率（百分比）
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 目标权重（百分比）
     */
    @TableField("target_weight")
    private BigDecimal targetWeight;

    /**
     * 计算方式：CUMULATIVE-累计/AVERAGE-平均/LATEST-最新值
     */
    @TableField("calculation_method")
    private String calculationMethod;

    /**
     * 数据来源：MANUAL-手工录入/SYSTEM-系统自动/IMPORT-导入
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 更新频率：DAILY-每日/WEEKLY-每周/MONTHLY-每月/QUARTERLY-每季度
     */
    @TableField("update_frequency")
    private String updateFrequency;

    /**
     * 目标开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 目标结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 目标周期：ANNUAL-年度/QUARTERLY-季度/MONTHLY-月度/WEEKLY-周度
     */
    @TableField("target_period")
    private String targetPeriod;

    /**
     * 目标状态：DRAFT-草稿/ACTIVE-激活/PAUSED-暂停/COMPLETED-完成/CANCELLED-取消
     */
    @TableField("target_status")
    private String targetStatus;

    /**
     * 进度状态：ON_TRACK-正常/AT_RISK-风险/OFF_TRACK-偏离
     */
    @TableField("progress_status")
    private String progressStatus;

    /**
     * 优先级：HIGH-高/MEDIUM-中/LOW-低
     */
    @TableField("priority")
    private String priority;

    /**
     * 关键结果，JSON格式存储
     */
    @TableField("key_results")
    private String keyResults;

    /**
     * 行动计划，JSON格式存储
     */
    @TableField("action_plans")
    private String actionPlans;

    /**
     * 里程碑，JSON格式存储
     */
    @TableField("milestones")
    private String milestones;

    /**
     * 风险因素，JSON格式存储
     */
    @TableField("risk_factors")
    private String riskFactors;

    /**
     * 支持资源，JSON格式存储
     */
    @TableField("support_resources")
    private String supportResources;

    /**
     * 评估标准
     */
    @TableField("evaluation_criteria")
    private String evaluationCriteria;

    /**
     * 激励方案，JSON格式存储
     */
    @TableField("incentive_scheme")
    private String incentiveScheme;

    /**
     * 协商记录，JSON格式存储
     */
    @TableField("negotiation_records")
    private String negotiationRecords;

    /**
     * 调整历史，JSON格式存储
     */
    @TableField("adjustment_history")
    private String adjustmentHistory;

    /**
     * 跟踪记录，JSON格式存储
     */
    @TableField("tracking_records")
    private String trackingRecords;

    /**
     * 评估结果，JSON格式存储
     */
    @TableField("evaluation_results")
    private String evaluationResults;

    /**
     * 知识库链接，JSON格式存储
     */
    @TableField("knowledge_links")
    private String knowledgeLinks;

    /**
     * 数据分析结果，JSON格式存储
     */
    @TableField("analysis_results")
    private String analysisResults;

    /**
     * 最后更新时间
     */
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 最后评估时间
     */
    @TableField("last_evaluation_time")
    private LocalDateTime lastEvaluationTime;

    /**
     * 下次评估时间
     */
    @TableField("next_evaluation_time")
    private LocalDateTime nextEvaluationTime;

    /**
     * 提醒设置，JSON格式存储
     */
    @TableField("reminder_settings")
    private String reminderSettings;

    /**
     * 是否启用：0-否/1-是
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 是否关键目标：0-否/1-是
     */
    @TableField("is_key_target")
    private Integer isKeyTarget;

    /**
     * 是否可见：0-否/1-是
     */
    @TableField("is_visible")
    private Integer isVisible;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 扩展属性，JSON格式存储
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField("created_by_name")
    private String createdByName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField("updated_by_name")
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;
}
