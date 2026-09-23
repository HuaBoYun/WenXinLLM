package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算任务实体类
 * 
 * @description 预算任务管理实体，支持预算编制任务管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_TASK")
public class BudgetTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 任务编码
     */
    @TableField("TASK_CODE")
    private String taskCode;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 任务类型：budget_preparation-预算编制，budget_adjustment-预算调整，budget_review-预算审查
     */
    @TableField("TASK_TYPE")
    private String taskType;

    /**
     * 任务描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 预算期间
     */
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 预算模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 任务状态：draft-草稿，published-已发布，in_progress-进行中，completed-已完成，cancelled-已取消
     */
    @TableField("TASK_STATUS")
    private String taskStatus;

    /**
     * 优先级：high-高，medium-中，low-低
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 计划开始时间
     */
    @TableField("PLANNED_START_TIME")
    private LocalDateTime plannedStartTime;

    /**
     * 计划结束时间
     */
    @TableField("PLANNED_END_TIME")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开始时间
     */
    @TableField("ACTUAL_START_TIME")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("ACTUAL_END_TIME")
    private LocalDateTime actualEndTime;

    /**
     * 任务负责人ID
     */
    @TableField("OWNER_ID")
    private String ownerId;

    /**
     * 任务负责人姓名
     */
    @TableField("OWNER_NAME")
    private String ownerName;

    /**
     * 参与人员ID列表（JSON格式）
     */
    @TableField("PARTICIPANT_IDS")
    private String participantIds;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

    /**
     * 当前审批节点
     */
    @TableField("CURRENT_NODE")
    private String currentNode;

    /**
     * 审批状态：pending-待审批，approved-已审批，rejected-已拒绝
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 完成进度（百分比）
     */
    @TableField("PROGRESS")
    private Integer progress;

    /**
     * 是否启用催报
     */
    @TableField("IS_REMINDER_ENABLED")
    private Boolean isReminderEnabled;

    /**
     * 催报策略ID
     */
    @TableField("REMINDER_STRATEGY_ID")
    private String reminderStrategyId;

    /**
     * 任务配置（JSON格式）
     */
    @TableField("TASK_CONFIG")
    private String taskConfig;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用
     */
    @TableField("STATUS")
    private String status;

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
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

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

    // 任务类型常量
    public static final String TASK_TYPE_BUDGET_PREPARATION = "budget_preparation";
    public static final String TASK_TYPE_BUDGET_ADJUSTMENT = "budget_adjustment";
    public static final String TASK_TYPE_BUDGET_REVIEW = "budget_review";

    // 任务状态常量
    public static final String TASK_STATUS_DRAFT = "draft";
    public static final String TASK_STATUS_PUBLISHED = "published";
    public static final String TASK_STATUS_IN_PROGRESS = "in_progress";
    public static final String TASK_STATUS_COMPLETED = "completed";
    public static final String TASK_STATUS_CANCELLED = "cancelled";

    // 优先级常量
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_LOW = "low";

    // 审批状态常量
    public static final String APPROVAL_STATUS_PENDING = "pending";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
