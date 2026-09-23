package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算调整实体类
 * 
 * @description 预算调整管理实体，支持预算数据的调整申请、审批和执行
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_ADJUSTMENT")
public class BudgetAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 调整编码
     */
    @TableField("ADJUSTMENT_CODE")
    private String adjustmentCode;

    /**
     * 调整名称
     */
    @TableField("ADJUSTMENT_NAME")
    private String adjustmentName;

    /**
     * 调整简称
     */
    @TableField("ADJUSTMENT_SHORT_NAME")
    private String adjustmentShortName;

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
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 调整类型：increase-增加，decrease-减少，transfer-调拨，reallocation-重新分配
     */
    @TableField("ADJUSTMENT_TYPE")
    private String adjustmentType;

    /**
     * 调整分类：budget-预算调整，forecast-预测调整，plan-计划调整
     */
    @TableField("ADJUSTMENT_CATEGORY")
    private String adjustmentCategory;

    /**
     * 调整级别：urgent-紧急，high-高，normal-普通，low-低
     */
    @TableField("ADJUSTMENT_LEVEL")
    private String adjustmentLevel;

    /**
     * 调整原因
     */
    @TableField("ADJUSTMENT_REASON")
    private String adjustmentReason;

    /**
     * 调整说明
     */
    @TableField("ADJUSTMENT_DESCRIPTION")
    private String adjustmentDescription;

    /**
     * 调整依据（JSON格式）
     */
    @TableField("ADJUSTMENT_BASIS")
    private String adjustmentBasis;

    /**
     * 调整范围（JSON格式）
     */
    @TableField("ADJUSTMENT_SCOPE")
    private String adjustmentScope;

    /**
     * 调整金额
     */
    @TableField("ADJUSTMENT_AMOUNT")
    private BigDecimal adjustmentAmount;

    /**
     * 调整前金额
     */
    @TableField("BEFORE_AMOUNT")
    private BigDecimal beforeAmount;

    /**
     * 调整后金额
     */
    @TableField("AFTER_AMOUNT")
    private BigDecimal afterAmount;

    /**
     * 调整比例（百分比）
     */
    @TableField("ADJUSTMENT_RATE")
    private BigDecimal adjustmentRate;

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
     * 调整期间（格式：YYYY-MM）
     */
    @TableField("ADJUSTMENT_PERIOD")
    private String adjustmentPeriod;

    /**
     * 期间类型：annual-年度，quarterly-季度，monthly-月度
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

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
     * 申请人ID
     */
    @TableField("APPLICANT_ID")
    private String applicantId;

    /**
     * 申请人姓名
     */
    @TableField("APPLICANT_NAME")
    private String applicantName;

    /**
     * 申请时间
     */
    @TableField("APPLICATION_TIME")
    private LocalDateTime applicationTime;

    /**
     * 申请部门ID
     */
    @TableField("APPLICATION_DEPT_ID")
    private String applicationDeptId;

    /**
     * 申请部门名称
     */
    @TableField("APPLICATION_DEPT_NAME")
    private String applicationDeptName;

    /**
     * 审批状态：draft-草稿，submitted-已提交，reviewing-审批中，approved-已审批，rejected-已拒绝，cancelled-已取消
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

    /**
     * 当前审批节点ID
     */
    @TableField("CURRENT_NODE_ID")
    private String currentNodeId;

    /**
     * 当前审批人ID
     */
    @TableField("CURRENT_APPROVER_ID")
    private String currentApproverId;

    /**
     * 当前审批人姓名
     */
    @TableField("CURRENT_APPROVER_NAME")
    private String currentApproverName;

    /**
     * 审批开始时间
     */
    @TableField("APPROVAL_START_TIME")
    private LocalDateTime approvalStartTime;

    /**
     * 审批结束时间
     */
    @TableField("APPROVAL_END_TIME")
    private LocalDateTime approvalEndTime;

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
     * 影响分析（JSON格式）
     */
    @TableField("IMPACT_ANALYSIS")
    private String impactAnalysis;

    /**
     * 风险评估（JSON格式）
     */
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    /**
     * 关联调整ID列表（JSON格式）
     */
    @TableField("RELATED_ADJUSTMENTS")
    private String relatedAdjustments;

    /**
     * 调整明细（JSON格式）
     */
    @TableField("ADJUSTMENT_DETAILS")
    private String adjustmentDetails;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 是否紧急调整
     */
    @TableField("IS_URGENT")
    private Boolean isUrgent;

    /**
     * 是否批量调整
     */
    @TableField("IS_BATCH")
    private Boolean isBatch;

    /**
     * 是否自动执行
     */
    @TableField("IS_AUTO_EXECUTE")
    private Boolean isAutoExecute;

    /**
     * 是否需要回滚
     */
    @TableField("IS_ROLLBACK_REQUIRED")
    private Boolean isRollbackRequired;

    /**
     * 回滚信息（JSON格式）
     */
    @TableField("ROLLBACK_INFO")
    private String rollbackInfo;

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
    public static final String ADJUSTMENT_TYPE_INCREASE = "increase";
    public static final String ADJUSTMENT_TYPE_DECREASE = "decrease";
    public static final String ADJUSTMENT_TYPE_TRANSFER = "transfer";
    public static final String ADJUSTMENT_TYPE_REALLOCATION = "reallocation";

    public static final String ADJUSTMENT_CATEGORY_BUDGET = "budget";
    public static final String ADJUSTMENT_CATEGORY_FORECAST = "forecast";
    public static final String ADJUSTMENT_CATEGORY_PLAN = "plan";

    public static final String ADJUSTMENT_LEVEL_URGENT = "urgent";
    public static final String ADJUSTMENT_LEVEL_HIGH = "high";
    public static final String ADJUSTMENT_LEVEL_NORMAL = "normal";
    public static final String ADJUSTMENT_LEVEL_LOW = "low";

    public static final String PERIOD_TYPE_ANNUAL = "annual";
    public static final String PERIOD_TYPE_QUARTERLY = "quarterly";
    public static final String PERIOD_TYPE_MONTHLY = "monthly";

    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_REVIEWING = "reviewing";
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
