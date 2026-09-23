package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算分配实体类
 * 
 * @description 预算分配管理实体，支持预算资源的分配、调配和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_ALLOCATION")
public class BudgetAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分配编码
     */
    @TableField("ALLOCATION_CODE")
    private String allocationCode;

    /**
     * 分配名称
     */
    @TableField("ALLOCATION_NAME")
    private String allocationName;

    /**
     * 分配简称
     */
    @TableField("ALLOCATION_SHORT_NAME")
    private String allocationShortName;

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
     * 源组织ID
     */
    @TableField("SOURCE_ORGANIZATION_ID")
    private String sourceOrganizationId;

    /**
     * 源组织名称
     */
    @TableField("SOURCE_ORGANIZATION_NAME")
    private String sourceOrganizationName;

    /**
     * 目标组织ID
     */
    @TableField("TARGET_ORGANIZATION_ID")
    private String targetOrganizationId;

    /**
     * 目标组织名称
     */
    @TableField("TARGET_ORGANIZATION_NAME")
    private String targetOrganizationName;

    /**
     * 指标ID
     */
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 指标编码
     */
    @TableField("INDICATOR_CODE")
    private String indicatorCode;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 维度组合（JSON格式）
     */
    @TableField("DIMENSION_COMBINATION")
    private String dimensionCombination;

    /**
     * 分配类型：initial-初始分配，adjustment-调整分配，reallocation-重新分配，transfer-转移分配
     */
    @TableField("ALLOCATION_TYPE")
    private String allocationType;

    /**
     * 分配方式：manual-手工分配，automatic-自动分配，formula-公式分配，proportion-比例分配
     */
    @TableField("ALLOCATION_METHOD")
    private String allocationMethod;

    /**
     * 分配策略：equal-平均分配，weighted-权重分配，performance-绩效分配，historical-历史分配
     */
    @TableField("ALLOCATION_STRATEGY")
    private String allocationStrategy;

    /**
     * 分配期间（格式：YYYY-MM）
     */
    @TableField("ALLOCATION_PERIOD")
    private String allocationPeriod;

    /**
     * 期间类型：annual-年度，quarterly-季度，monthly-月度
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 分配金额
     */
    @TableField("ALLOCATION_AMOUNT")
    private BigDecimal allocationAmount;

    /**
     * 原始金额
     */
    @TableField("ORIGINAL_AMOUNT")
    private BigDecimal originalAmount;

    /**
     * 分配比例（百分比）
     */
    @TableField("ALLOCATION_RATIO")
    private BigDecimal allocationRatio;

    /**
     * 权重系数
     */
    @TableField("WEIGHT_FACTOR")
    private BigDecimal weightFactor;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 汇率
     */
    @TableField("EXCHANGE_RATE")
    private BigDecimal exchangeRate;

    /**
     * 分配公式
     */
    @TableField("ALLOCATION_FORMULA")
    private String allocationFormula;

    /**
     * 分配规则（JSON格式）
     */
    @TableField("ALLOCATION_RULES")
    private String allocationRules;

    /**
     * 分配条件（JSON格式）
     */
    @TableField("ALLOCATION_CONDITIONS")
    private String allocationConditions;

    /**
     * 分配参数（JSON格式）
     */
    @TableField("ALLOCATION_PARAMETERS")
    private String allocationParameters;

    /**
     * 分配依据
     */
    @TableField("ALLOCATION_BASIS")
    private String allocationBasis;

    /**
     * 分配说明
     */
    @TableField("ALLOCATION_DESCRIPTION")
    private String allocationDescription;

    /**
     * 分配原因
     */
    @TableField("ALLOCATION_REASON")
    private String allocationReason;

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
     * 分配人ID
     */
    @TableField("ALLOCATOR_ID")
    private String allocatorId;

    /**
     * 分配人姓名
     */
    @TableField("ALLOCATOR_NAME")
    private String allocatorName;

    /**
     * 分配时间
     */
    @TableField("ALLOCATION_TIME")
    private LocalDateTime allocationTime;

    /**
     * 分配部门ID
     */
    @TableField("ALLOCATION_DEPT_ID")
    private String allocationDeptId;

    /**
     * 分配部门名称
     */
    @TableField("ALLOCATION_DEPT_NAME")
    private String allocationDeptName;

    /**
     * 审批状态：draft-草稿，submitted-已提交，approved-已审批，rejected-已拒绝，cancelled-已取消
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
     * 执行状态：pending-待执行，executing-执行中，completed-已完成，failed-执行失败
     */
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    /**
     * 执行人ID
     */
    @TableField("EXECUTOR_ID")
    private String executorId;

    /**
     * 执行人姓名
     */
    @TableField("EXECUTOR_NAME")
    private String executorName;

    /**
     * 执行时间
     */
    @TableField("EXECUTION_TIME")
    private LocalDateTime executionTime;

    /**
     * 执行结果
     */
    @TableField("EXECUTION_RESULT")
    private String executionResult;

    /**
     * 执行错误信息
     */
    @TableField("EXECUTION_ERROR")
    private String executionError;

    /**
     * 分配明细（JSON格式）
     */
    @TableField("ALLOCATION_DETAILS")
    private String allocationDetails;

    /**
     * 影响分析（JSON格式）
     */
    @TableField("IMPACT_ANALYSIS")
    private String impactAnalysis;

    /**
     * 关联分配ID列表（JSON格式）
     */
    @TableField("RELATED_ALLOCATIONS")
    private String relatedAllocations;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 是否自动分配
     */
    @TableField("IS_AUTO_ALLOCATION")
    private Boolean isAutoAllocation;

    /**
     * 是否批量分配
     */
    @TableField("IS_BATCH_ALLOCATION")
    private Boolean isBatchAllocation;

    /**
     * 是否可撤销
     */
    @TableField("IS_REVOCABLE")
    private Boolean isRevocable;

    /**
     * 撤销信息（JSON格式）
     */
    @TableField("REVOCATION_INFO")
    private String revocationInfo;

    /**
     * 通知配置（JSON格式）
     */
    @TableField("NOTIFICATION_CONFIG")
    private String notificationConfig;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，cancelled-已取消
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
    public static final String ALLOCATION_TYPE_INITIAL = "initial";
    public static final String ALLOCATION_TYPE_ADJUSTMENT = "adjustment";
    public static final String ALLOCATION_TYPE_REALLOCATION = "reallocation";
    public static final String ALLOCATION_TYPE_TRANSFER = "transfer";

    public static final String ALLOCATION_METHOD_MANUAL = "manual";
    public static final String ALLOCATION_METHOD_AUTOMATIC = "automatic";
    public static final String ALLOCATION_METHOD_FORMULA = "formula";
    public static final String ALLOCATION_METHOD_PROPORTION = "proportion";

    public static final String ALLOCATION_STRATEGY_EQUAL = "equal";
    public static final String ALLOCATION_STRATEGY_WEIGHTED = "weighted";
    public static final String ALLOCATION_STRATEGY_PERFORMANCE = "performance";
    public static final String ALLOCATION_STRATEGY_HISTORICAL = "historical";

    public static final String PERIOD_TYPE_ANNUAL = "annual";
    public static final String PERIOD_TYPE_QUARTERLY = "quarterly";
    public static final String PERIOD_TYPE_MONTHLY = "monthly";

    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";
    public static final String APPROVAL_STATUS_CANCELLED = "cancelled";

    public static final String EXECUTION_STATUS_PENDING = "pending";
    public static final String EXECUTION_STATUS_EXECUTING = "executing";
    public static final String EXECUTION_STATUS_COMPLETED = "completed";
    public static final String EXECUTION_STATUS_FAILED = "failed";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_CANCELLED = "cancelled";
}
