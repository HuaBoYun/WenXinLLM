package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算工作流实体类
 * 
 * @description 预算工作流管理实体，支持预算审批流程的定义和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_WORKFLOW")
public class BudgetWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 工作流编码
     */
    @TableField("WORKFLOW_CODE")
    private String workflowCode;

    /**
     * 工作流名称
     */
    @TableField("WORKFLOW_NAME")
    private String workflowName;

    /**
     * 工作流类型：BUDGET_PREPARATION-预算编制，BUDGET_ADJUSTMENT-预算调整，BUDGET_ALLOCATION-预算分配，BUDGET_APPROVAL-预算审批
     */
    @TableField("WORKFLOW_TYPE")
    private String workflowType;

    /**
     * 工作流分类：STANDARD-标准流程，CUSTOM-自定义流程，EMERGENCY-紧急流程
     */
    @TableField("WORKFLOW_CATEGORY")
    private String workflowCategory;

    /**
     * 工作流版本
     */
    @TableField("WORKFLOW_VERSION")
    private String workflowVersion;

    /**
     * 工作流状态：DRAFT-草稿，ACTIVE-激活，INACTIVE-停用，ARCHIVED-归档
     */
    @TableField("WORKFLOW_STATUS")
    private String workflowStatus;

    /**
     * 是否默认工作流
     */
    @TableField("IS_DEFAULT")
    private Boolean isDefault;

    /**
     * 适用组织ID
     */
    @TableField("APPLICABLE_ORGANIZATION_ID")
    private String applicableOrganizationId;

    /**
     * 适用范围：ALL-全部，SPECIFIC-指定组织，ROLE-指定角色
     */
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    /**
     * 适用条件
     */
    @TableField("APPLICABLE_CONDITIONS")
    private String applicableConditions;

    /**
     * 工作流定义（JSON格式）
     */
    @TableField("WORKFLOW_DEFINITION")
    private String workflowDefinition;

    /**
     * 节点配置（JSON格式）
     */
    @TableField("NODE_CONFIG")
    private String nodeConfig;

    /**
     * 路由规则（JSON格式）
     */
    @TableField("ROUTING_RULES")
    private String routingRules;

    /**
     * 审批规则（JSON格式）
     */
    @TableField("APPROVAL_RULES")
    private String approvalRules;

    /**
     * 通知规则（JSON格式）
     */
    @TableField("NOTIFICATION_RULES")
    private String notificationRules;

    /**
     * 超时处理规则（JSON格式）
     */
    @TableField("TIMEOUT_RULES")
    private String timeoutRules;

    /**
     * 异常处理规则（JSON格式）
     */
    @TableField("EXCEPTION_RULES")
    private String exceptionRules;

    /**
     * 权限配置（JSON格式）
     */
    @TableField("PERMISSION_CONFIG")
    private String permissionConfig;

    /**
     * 表单配置（JSON格式）
     */
    @TableField("FORM_CONFIG")
    private String formConfig;

    /**
     * 字段配置（JSON格式）
     */
    @TableField("FIELD_CONFIG")
    private String fieldConfig;

    /**
     * 按钮配置（JSON格式）
     */
    @TableField("BUTTON_CONFIG")
    private String buttonConfig;

    /**
     * 显示配置（JSON格式）
     */
    @TableField("DISPLAY_CONFIG")
    private String displayConfig;

    /**
     * 启动条件
     */
    @TableField("START_CONDITIONS")
    private String startConditions;

    /**
     * 结束条件
     */
    @TableField("END_CONDITIONS")
    private String endConditions;

    /**
     * 优先级：HIGH-高，MEDIUM-中，LOW-低
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 预计处理时间（小时）
     */
    @TableField("ESTIMATED_PROCESSING_TIME")
    private Integer estimatedProcessingTime;

    /**
     * 最大处理时间（小时）
     */
    @TableField("MAX_PROCESSING_TIME")
    private Integer maxProcessingTime;

    /**
     * 自动提醒时间（小时）
     */
    @TableField("AUTO_REMINDER_TIME")
    private Integer autoReminderTime;

    /**
     * 工作流描述
     */
    @TableField("WORKFLOW_DESCRIPTION")
    private String workflowDescription;

    /**
     * 使用说明
     */
    @TableField("USAGE_INSTRUCTIONS")
    private String usageInstructions;

    /**
     * 变更日志
     */
    @TableField("CHANGE_LOG")
    private String changeLog;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDateTime expiryDate;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 审批人
     */
    @TableField("APPROVED_BY")
    private String approvedBy;

    /**
     * 审批时间
     */
    @TableField("APPROVED_TIME")
    private LocalDateTime approvedTime;

    /**
     * 发布人
     */
    @TableField("PUBLISHED_BY")
    private String publishedBy;

    /**
     * 发布时间
     */
    @TableField("PUBLISHED_TIME")
    private LocalDateTime publishedTime;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
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

    // ==================== 常量定义 ====================

    /**
     * 工作流类型常量
     */
    public static final String WORKFLOW_TYPE_BUDGET_PREPARATION = "BUDGET_PREPARATION";
    public static final String WORKFLOW_TYPE_BUDGET_ADJUSTMENT = "BUDGET_ADJUSTMENT";
    public static final String WORKFLOW_TYPE_BUDGET_ALLOCATION = "BUDGET_ALLOCATION";
    public static final String WORKFLOW_TYPE_BUDGET_APPROVAL = "BUDGET_APPROVAL";

    /**
     * 工作流分类常量
     */
    public static final String WORKFLOW_CATEGORY_STANDARD = "STANDARD";
    public static final String WORKFLOW_CATEGORY_CUSTOM = "CUSTOM";
    public static final String WORKFLOW_CATEGORY_EMERGENCY = "EMERGENCY";

    /**
     * 工作流状态常量
     */
    public static final String WORKFLOW_STATUS_DRAFT = "DRAFT";
    public static final String WORKFLOW_STATUS_ACTIVE = "ACTIVE";
    public static final String WORKFLOW_STATUS_INACTIVE = "INACTIVE";
    public static final String WORKFLOW_STATUS_ARCHIVED = "ARCHIVED";

    /**
     * 适用范围常量
     */
    public static final String APPLICABLE_SCOPE_ALL = "ALL";
    public static final String APPLICABLE_SCOPE_SPECIFIC = "SPECIFIC";
    public static final String APPLICABLE_SCOPE_ROLE = "ROLE";

    /**
     * 优先级常量
     */
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_LOW = "LOW";
}
