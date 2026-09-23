package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预算审批流程配置表
 * 对应表：tbl_eps_approval_config
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_approval_config")
public class EpsApprovalConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID，主键
     */
    @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 配置名称
     */
    @TableField("config_name")
    private String configName;

    /**
     * 审批类型：BUDGET-预算审批/ADJUSTMENT-调整审批/TRANSFER-调拨审批
     */
    @TableField("approval_type")
    private String approvalType;

    /**
     * 触发条件，JSON格式存储
     */
    @TableField("trigger_conditions")
    private String triggerConditions;

    /**
     * 审批流程，JSON格式存储
     */
    @TableField("approval_flow")
    private String approvalFlow;

    /**
     * 审批人配置，JSON格式存储
     */
    @TableField("approver_config")
    private String approverConfig;

    /**
     * 升级规则，JSON格式存储
     */
    @TableField("escalation_rules")
    private String escalationRules;

    /**
     * 通知配置，JSON格式存储
     */
    @TableField("notification_config")
    private String notificationConfig;

    /**
     * SLA配置，JSON格式存储
     */
    @TableField("sla_config")
    private String slaConfig;

    /**
     * 是否并行审批：0-否/1-是
     */
    @TableField("parallel_approval")
    private Integer parallelApproval;

    /**
     * 自动审批规则，JSON格式存储
     */
    @TableField("auto_approval_rules")
    private String autoApprovalRules;

    /**
     * 拒绝处理，JSON格式存储
     */
    @TableField("rejection_handling")
    private String rejectionHandling;

    /**
     * 是否激活：0-否/1-是
     */
    @TableField("is_active")
    private Integer isActive;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

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
}
