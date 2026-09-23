package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算审批流程实体类
 * 
 * @description 预算审批流程管理实体，支持多级审批和灵活的审批流程配置
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_APPROVAL_FLOW")
public class BudgetApprovalFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 流程编码
     */
    @TableField("FLOW_CODE")
    private String flowCode;

    /**
     * 流程名称
     */
    @TableField("FLOW_NAME")
    private String flowName;

    /**
     * 流程类型：budget_preparation-预算编制，budget_adjustment-预算调整，budget_review-预算审查
     */
    @TableField("FLOW_TYPE")
    private String flowType;

    /**
     * 流程描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 适用组织ID
     */
    @TableField("APPLICABLE_ORG_ID")
    private String applicableOrgId;

    /**
     * 适用预算类型
     */
    @TableField("APPLICABLE_BUDGET_TYPE")
    private String applicableBudgetType;

    /**
     * 流程定义（JSON格式）
     */
    @TableField("FLOW_DEFINITION")
    private String flowDefinition;

    /**
     * 审批节点配置（JSON格式）
     */
    @TableField("NODE_CONFIG")
    private String nodeConfig;

    /**
     * 审批规则配置（JSON格式）
     */
    @TableField("APPROVAL_RULES")
    private String approvalRules;

    /**
     * 超时处理策略：auto_approve-自动通过，auto_reject-自动拒绝，escalate-升级处理
     */
    @TableField("TIMEOUT_STRATEGY")
    private String timeoutStrategy;

    /**
     * 超时时间（小时）
     */
    @TableField("TIMEOUT_HOURS")
    private Integer timeoutHours;

    /**
     * 是否启用并行审批
     */
    @TableField("IS_PARALLEL_ENABLED")
    private Boolean isParallelEnabled;

    /**
     * 是否启用会签
     */
    @TableField("IS_COUNTERSIGN_ENABLED")
    private Boolean isCountersignEnabled;

    /**
     * 会签通过比例（百分比）
     */
    @TableField("COUNTERSIGN_PASS_RATE")
    private Integer countersignPassRate;

    /**
     * 是否启用委托
     */
    @TableField("IS_DELEGATE_ENABLED")
    private Boolean isDelegateEnabled;

    /**
     * 是否启用加签
     */
    @TableField("IS_ADD_SIGN_ENABLED")
    private Boolean isAddSignEnabled;

    /**
     * 是否启用退回
     */
    @TableField("IS_RETURN_ENABLED")
    private Boolean isReturnEnabled;

    /**
     * 是否启用撤回
     */
    @TableField("IS_WITHDRAW_ENABLED")
    private Boolean isWithdrawEnabled;

    /**
     * 流程版本
     */
    @TableField("FLOW_VERSION")
    private String flowVersion;

    /**
     * 是否默认流程
     */
    @TableField("IS_DEFAULT")
    private Boolean isDefault;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：draft-草稿，published-已发布，archived-已归档
     */
    @TableField("STATUS")
    private String status;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 归档时间
     */
    @TableField("ARCHIVE_TIME")
    private LocalDateTime archiveTime;

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
    public static final String FLOW_TYPE_BUDGET_PREPARATION = "budget_preparation";
    public static final String FLOW_TYPE_BUDGET_ADJUSTMENT = "budget_adjustment";
    public static final String FLOW_TYPE_BUDGET_REVIEW = "budget_review";

    public static final String TIMEOUT_STRATEGY_AUTO_APPROVE = "auto_approve";
    public static final String TIMEOUT_STRATEGY_AUTO_REJECT = "auto_reject";
    public static final String TIMEOUT_STRATEGY_ESCALATE = "escalate";

    public static final String STATUS_DRAFT = "draft";
    public static final String STATUS_PUBLISHED = "published";
    public static final String STATUS_ARCHIVED = "archived";
}
