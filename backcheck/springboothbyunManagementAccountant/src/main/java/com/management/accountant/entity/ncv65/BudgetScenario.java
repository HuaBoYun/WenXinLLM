package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算场景实体类
 * 
 * @description 预算场景管理实体，支持多场景预算分析和对比
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_SCENARIO")
public class BudgetScenario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 场景编码
     */
    @TableField("SCENARIO_CODE")
    private String scenarioCode;

    /**
     * 场景名称
     */
    @TableField("SCENARIO_NAME")
    private String scenarioName;

    /**
     * 场景简称
     */
    @TableField("SCENARIO_SHORT_NAME")
    private String scenarioShortName;

    /**
     * 场景类型：base-基准场景，optimistic-乐观场景，pessimistic-悲观场景，custom-自定义场景
     */
    @TableField("SCENARIO_TYPE")
    private String scenarioType;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 预算模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 场景描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 场景假设（JSON格式）
     */
    @TableField("SCENARIO_ASSUMPTIONS")
    private String scenarioAssumptions;

    /**
     * 关键参数（JSON格式）
     */
    @TableField("KEY_PARAMETERS")
    private String keyParameters;

    /**
     * 增长率假设（JSON格式）
     */
    @TableField("GROWTH_ASSUMPTIONS")
    private String growthAssumptions;

    /**
     * 风险因子（JSON格式）
     */
    @TableField("RISK_FACTORS")
    private String riskFactors;

    /**
     * 概率权重
     */
    @TableField("PROBABILITY_WEIGHT")
    private BigDecimal probabilityWeight;

    /**
     * 置信度
     */
    @TableField("CONFIDENCE_LEVEL")
    private BigDecimal confidenceLevel;

    /**
     * 敏感性分析配置（JSON格式）
     */
    @TableField("SENSITIVITY_CONFIG")
    private String sensitivityConfig;

    /**
     * 蒙特卡洛配置（JSON格式）
     */
    @TableField("MONTE_CARLO_CONFIG")
    private String monteCarloConfig;

    /**
     * 基准场景ID（用于对比分析）
     */
    @TableField("BASE_SCENARIO_ID")
    private String baseScenarioId;

    /**
     * 父场景ID（用于场景继承）
     */
    @TableField("PARENT_SCENARIO_ID")
    private String parentScenarioId;

    /**
     * 场景版本
     */
    @TableField("SCENARIO_VERSION")
    private String scenarioVersion;

    /**
     * 是否默认场景
     */
    @TableField("IS_DEFAULT")
    private Boolean isDefault;

    /**
     * 是否基准场景
     */
    @TableField("IS_BASELINE")
    private Boolean isBaseline;

    /**
     * 是否已发布
     */
    @TableField("IS_PUBLISHED")
    private Boolean isPublished;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    @TableField("PUBLISHED_BY")
    private String publishedBy;

    /**
     * 发布人姓名
     */
    @TableField("PUBLISHED_BY_NAME")
    private String publishedByName;

    /**
     * 是否锁定
     */
    @TableField("IS_LOCKED")
    private Boolean isLocked;

    /**
     * 锁定时间
     */
    @TableField("LOCK_TIME")
    private LocalDateTime lockTime;

    /**
     * 锁定人ID
     */
    @TableField("LOCKED_BY")
    private String lockedBy;

    /**
     * 锁定人姓名
     */
    @TableField("LOCKED_BY_NAME")
    private String lockedByName;

    /**
     * 审批状态：draft-草稿，submitted-已提交，approved-已审批，rejected-已拒绝
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

    /**
     * 计算状态：pending-待计算，calculating-计算中，completed-已完成，failed-计算失败
     */
    @TableField("CALCULATION_STATUS")
    private String calculationStatus;

    /**
     * 计算开始时间
     */
    @TableField("CALCULATION_START_TIME")
    private LocalDateTime calculationStartTime;

    /**
     * 计算结束时间
     */
    @TableField("CALCULATION_END_TIME")
    private LocalDateTime calculationEndTime;

    /**
     * 计算结果摘要（JSON格式）
     */
    @TableField("CALCULATION_SUMMARY")
    private String calculationSummary;

    /**
     * 计算错误信息
     */
    @TableField("CALCULATION_ERROR")
    private String calculationError;

    /**
     * 数据质量评分
     */
    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    /**
     * 数据完整性评分
     */
    @TableField("COMPLETENESS_SCORE")
    private BigDecimal completenessScore;

    /**
     * 一致性评分
     */
    @TableField("CONSISTENCY_SCORE")
    private BigDecimal consistencyScore;

    /**
     * 标签（JSON格式）
     */
    @TableField("TAGS")
    private String tags;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 访问权限配置（JSON格式）
     */
    @TableField("ACCESS_PERMISSIONS")
    private String accessPermissions;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRY_TIME")
    private LocalDateTime expiryTime;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，archived-已归档
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
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

    // 常量定义
    public static final String SCENARIO_TYPE_BASE = "base";
    public static final String SCENARIO_TYPE_OPTIMISTIC = "optimistic";
    public static final String SCENARIO_TYPE_PESSIMISTIC = "pessimistic";
    public static final String SCENARIO_TYPE_CUSTOM = "custom";

    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";

    public static final String CALCULATION_STATUS_PENDING = "pending";
    public static final String CALCULATION_STATUS_CALCULATING = "calculating";
    public static final String CALCULATION_STATUS_COMPLETED = "completed";
    public static final String CALCULATION_STATUS_FAILED = "failed";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_ARCHIVED = "archived";
}
