package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算控制规则实体类
 * 
 * @description 预算控制规则管理实体，支持7类控制规则
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_CONTROL_RULE")
public class BudgetControlRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 规则类型：single_indicator-单指标，multi_indicator-多指标，flexible-弹性，flexible_budget-弹性预算，rolling-滚动，hierarchical-分级，warning-预警
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 规则分类
     */
    @TableField("RULE_CATEGORY")
    private String ruleCategory;

    /**
     * 规则描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 适用组织ID列表（JSON格式）
     */
    @TableField("ORGANIZATION_IDS")
    private String organizationIds;

    /**
     * 适用指标ID列表（JSON格式）
     */
    @TableField("INDICATOR_IDS")
    private String indicatorIds;

    /**
     * 控制条件（JSON格式）
     */
    @TableField("CONTROL_CONDITIONS")
    private String controlConditions;

    /**
     * 控制阈值
     */
    @TableField("CONTROL_THRESHOLD")
    private BigDecimal controlThreshold;

    /**
     * 控制阈值类型：amount-金额，percentage-百分比
     */
    @TableField("THRESHOLD_TYPE")
    private String thresholdType;

    /**
     * 控制动作：block-阻止，warn-警告，approve-需审批
     */
    @TableField("CONTROL_ACTION")
    private String controlAction;

    /**
     * 控制级别：strict-严格，normal-普通，loose-宽松
     */
    @TableField("CONTROL_LEVEL")
    private String controlLevel;

    /**
     * 控制时点：before-事前，during-事中，after-事后
     */
    @TableField("CONTROL_TIMING")
    private String controlTiming;

    /**
     * 控制范围：period-期间，cumulative-累计，annual-年度
     */
    @TableField("CONTROL_SCOPE")
    private String controlScope;

    /**
     * 预警阈值
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 预警级别：low-低，medium-中，high-高，critical-严重
     */
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    /**
     * 预警消息模板
     */
    @TableField("WARNING_MESSAGE_TEMPLATE")
    private String warningMessageTemplate;

    /**
     * 通知人员ID列表（JSON格式）
     */
    @TableField("NOTIFICATION_USER_IDS")
    private String notificationUserIds;

    /**
     * 通知方式：email-邮件，sms-短信，system-系统消息
     */
    @TableField("NOTIFICATION_METHODS")
    private String notificationMethods;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

    /**
     * 规则公式
     */
    @TableField("RULE_FORMULA")
    private String ruleFormula;

    /**
     * 规则参数（JSON格式）
     */
    @TableField("RULE_PARAMETERS")
    private String ruleParameters;

    /**
     * 执行顺序
     */
    @TableField("EXECUTION_ORDER")
    private Integer executionOrder;

    /**
     * 优先级：high-高，medium-中，low-低
     */
    @TableField("PRIORITY")
    private String priority;

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
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 规则状态：draft-草稿，active-激活，inactive-停用，expired-已过期
     */
    @TableField("RULE_STATUS")
    private String ruleStatus;

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

    // 规则类型常量
    public static final String RULE_TYPE_SINGLE_INDICATOR = "single_indicator";
    public static final String RULE_TYPE_MULTI_INDICATOR = "multi_indicator";
    public static final String RULE_TYPE_FLEXIBLE = "flexible";
    public static final String RULE_TYPE_FLEXIBLE_BUDGET = "flexible_budget";
    public static final String RULE_TYPE_ROLLING = "rolling";
    public static final String RULE_TYPE_HIERARCHICAL = "hierarchical";
    public static final String RULE_TYPE_WARNING = "warning";

    // 阈值类型常量
    public static final String THRESHOLD_TYPE_AMOUNT = "amount";
    public static final String THRESHOLD_TYPE_PERCENTAGE = "percentage";

    // 控制动作常量
    public static final String CONTROL_ACTION_BLOCK = "block";
    public static final String CONTROL_ACTION_WARN = "warn";
    public static final String CONTROL_ACTION_APPROVE = "approve";

    // 控制级别常量
    public static final String CONTROL_LEVEL_STRICT = "strict";
    public static final String CONTROL_LEVEL_NORMAL = "normal";
    public static final String CONTROL_LEVEL_LOOSE = "loose";

    // 控制时点常量
    public static final String CONTROL_TIMING_BEFORE = "before";
    public static final String CONTROL_TIMING_DURING = "during";
    public static final String CONTROL_TIMING_AFTER = "after";

    // 控制范围常量
    public static final String CONTROL_SCOPE_PERIOD = "period";
    public static final String CONTROL_SCOPE_CUMULATIVE = "cumulative";
    public static final String CONTROL_SCOPE_ANNUAL = "annual";

    // 预警级别常量
    public static final String WARNING_LEVEL_LOW = "low";
    public static final String WARNING_LEVEL_MEDIUM = "medium";
    public static final String WARNING_LEVEL_HIGH = "high";
    public static final String WARNING_LEVEL_CRITICAL = "critical";

    // 优先级常量
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_LOW = "low";

    // 规则状态常量
    public static final String RULE_STATUS_DRAFT = "draft";
    public static final String RULE_STATUS_ACTIVE = "active";
    public static final String RULE_STATUS_INACTIVE = "inactive";
    public static final String RULE_STATUS_EXPIRED = "expired";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
