package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算控制表
 * 对应表：tbl_eps_budget_control
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_control")
public class EpsBudgetControl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 控制ID，主键
     */
    @TableId(value = "control_id", type = IdType.AUTO)
    private Long controlId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 预算版本ID
     */
    @TableField("version_id")
    private Long versionId;

    /**
     * 控制规则名称
     */
    @TableField("control_name")
    private String controlName;

    /**
     * 控制类型：AMOUNT-金额控制/QUANTITY-数量控制/RATE-比率控制/BALANCE-余额控制
     */
    @TableField("control_type")
    private String controlType;

    /**
     * 控制级别：STRICT-严格控制/WARNING-预警控制/SOFT-软控制
     */
    @TableField("control_level")
    private String controlLevel;

    /**
     * 控制范围：ORGANIZATION-组织/SUBJECT-科目/PROJECT-项目/GLOBAL-全局
     */
    @TableField("control_scope")
    private String controlScope;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 预算科目ID
     */
    @TableField("subject_id")
    private Long subjectId;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 控制维度，JSON格式存储
     */
    @TableField("control_dimensions")
    private String controlDimensions;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private BigDecimal budgetAmount;

    /**
     * 已用金额
     */
    @TableField("used_amount")
    private BigDecimal usedAmount;

    /**
     * 可用金额
     */
    @TableField("available_amount")
    private BigDecimal availableAmount;

    /**
     * 预警阈值（百分比）
     */
    @TableField("warning_threshold")
    private BigDecimal warningThreshold;

    /**
     * 控制阈值（百分比）
     */
    @TableField("control_threshold")
    private BigDecimal controlThreshold;

    /**
     * 当前使用率（百分比）
     */
    @TableField("usage_rate")
    private BigDecimal usageRate;

    /**
     * 控制状态：NORMAL-正常/WARNING-预警/EXCEEDED-超支/BLOCKED-阻止
     */
    @TableField("control_status")
    private String controlStatus;

    /**
     * 控制消息
     */
    @TableField("control_message")
    private String controlMessage;

    /**
     * 是否启用：0-否/1-是
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 是否实时控制：0-否/1-是
     */
    @TableField("real_time_control")
    private Integer realTimeControl;

    /**
     * 控制周期：DAILY-每日/WEEKLY-每周/MONTHLY-每月/QUARTERLY-每季度/YEARLY-每年
     */
    @TableField("control_period")
    private String controlPeriod;

    /**
     * 控制规则，JSON格式存储
     */
    @TableField("control_rules")
    private String controlRules;

    /**
     * 例外规则，JSON格式存储
     */
    @TableField("exception_rules")
    private String exceptionRules;

    /**
     * 审批规则，JSON格式存储
     */
    @TableField("approval_rules")
    private String approvalRules;

    /**
     * 通知规则，JSON格式存储
     */
    @TableField("notification_rules")
    private String notificationRules;

    /**
     * 升级规则，JSON格式存储
     */
    @TableField("escalation_rules")
    private String escalationRules;

    /**
     * 最后检查时间
     */
    @TableField("last_check_time")
    private LocalDateTime lastCheckTime;

    /**
     * 下次检查时间
     */
    @TableField("next_check_time")
    private LocalDateTime nextCheckTime;

    /**
     * 检查频率（分钟）
     */
    @TableField("check_frequency")
    private Integer checkFrequency;

    /**
     * 违规次数
     */
    @TableField("violation_count")
    private Integer violationCount;

    /**
     * 最后违规时间
     */
    @TableField("last_violation_time")
    private LocalDateTime lastViolationTime;

    /**
     * 违规记录，JSON格式存储
     */
    @TableField("violation_records")
    private String violationRecords;

    /**
     * 控制日志，JSON格式存储
     */
    @TableField("control_logs")
    private String controlLogs;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：DRAFT-草稿/ACTIVE-激活/INACTIVE-停用/ARCHIVED-归档
     */
    @TableField("status")
    private String status;

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
