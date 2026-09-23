package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算规则实体类
 * 
 * @description 预算规则管理实体，支持预算业务规则的定义和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_RULE")
public class BudgetRule implements Serializable {

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
     * 规则类型：VALIDATION-验证规则，CALCULATION-计算规则，CONTROL-控制规则，APPROVAL-审批规则
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 规则分类：BUDGET_PREPARATION-预算编制，BUDGET_EXECUTION-预算执行，BUDGET_ANALYSIS-预算分析
     */
    @TableField("RULE_CATEGORY")
    private String ruleCategory;

    /**
     * 规则级别：SYSTEM-系统级，ORGANIZATION-组织级，USER-用户级
     */
    @TableField("RULE_LEVEL")
    private String ruleLevel;

    /**
     * 规则状态：ACTIVE-激活，INACTIVE-停用，DRAFT-草稿，ARCHIVED-归档
     */
    @TableField("RULE_STATUS")
    private String ruleStatus;

    /**
     * 规则优先级：HIGH-高，MEDIUM-中，LOW-低
     */
    @TableField("RULE_PRIORITY")
    private String rulePriority;

    /**
     * 适用组织ID
     */
    @TableField("APPLICABLE_ORGANIZATION_ID")
    private String applicableOrganizationId;

    /**
     * 适用范围：ALL-全部，SPECIFIC-指定范围，CONDITIONAL-条件适用
     */
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    /**
     * 适用条件（JSON格式）
     */
    @TableField("APPLICABLE_CONDITIONS")
    private String applicableConditions;

    /**
     * 规则表达式
     */
    @TableField("RULE_EXPRESSION")
    private String ruleExpression;

    /**
     * 规则脚本
     */
    @TableField("RULE_SCRIPT")
    private String ruleScript;

    /**
     * 规则参数（JSON格式）
     */
    @TableField("RULE_PARAMETERS")
    private String ruleParameters;

    /**
     * 规则配置（JSON格式）
     */
    @TableField("RULE_CONFIG")
    private String ruleConfig;

    /**
     * 执行顺序
     */
    @TableField("EXECUTION_ORDER")
    private Integer executionOrder;

    /**
     * 是否必须执行
     */
    @TableField("IS_MANDATORY")
    private Boolean isMandatory;

    /**
     * 是否可跳过
     */
    @TableField("IS_SKIPPABLE")
    private Boolean isSkippable;

    /**
     * 错误处理方式：STOP-停止执行，WARNING-警告继续，IGNORE-忽略错误
     */
    @TableField("ERROR_HANDLING")
    private String errorHandling;

    /**
     * 错误消息模板
     */
    @TableField("ERROR_MESSAGE_TEMPLATE")
    private String errorMessageTemplate;

    /**
     * 成功消息模板
     */
    @TableField("SUCCESS_MESSAGE_TEMPLATE")
    private String successMessageTemplate;

    /**
     * 阈值设置（JSON格式）
     */
    @TableField("THRESHOLD_SETTINGS")
    private String thresholdSettings;

    /**
     * 预警阈值
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 控制阈值
     */
    @TableField("CONTROL_THRESHOLD")
    private BigDecimal controlThreshold;

    /**
     * 触发条件
     */
    @TableField("TRIGGER_CONDITIONS")
    private String triggerConditions;

    /**
     * 触发事件
     */
    @TableField("TRIGGER_EVENTS")
    private String triggerEvents;

    /**
     * 执行动作（JSON格式）
     */
    @TableField("EXECUTION_ACTIONS")
    private String executionActions;

    /**
     * 通知设置（JSON格式）
     */
    @TableField("NOTIFICATION_SETTINGS")
    private String notificationSettings;

    /**
     * 日志设置（JSON格式）
     */
    @TableField("LOG_SETTINGS")
    private String logSettings;

    /**
     * 规则描述
     */
    @TableField("RULE_DESCRIPTION")
    private String ruleDescription;

    /**
     * 使用说明
     */
    @TableField("USAGE_INSTRUCTIONS")
    private String usageInstructions;

    /**
     * 示例说明
     */
    @TableField("EXAMPLE_DESCRIPTION")
    private String exampleDescription;

    /**
     * 变更历史
     */
    @TableField("CHANGE_HISTORY")
    private String changeHistory;

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
     * 最后执行时间
     */
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    /**
     * 执行次数
     */
    @TableField("EXECUTION_COUNT")
    private Integer executionCount;

    /**
     * 成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 平均执行时间（毫秒）
     */
    @TableField("AVERAGE_EXECUTION_TIME")
    private Long averageExecutionTime;

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
     * 规则类型常量
     */
    public static final String RULE_TYPE_VALIDATION = "VALIDATION";
    public static final String RULE_TYPE_CALCULATION = "CALCULATION";
    public static final String RULE_TYPE_CONTROL = "CONTROL";
    public static final String RULE_TYPE_APPROVAL = "APPROVAL";

    /**
     * 规则分类常量
     */
    public static final String RULE_CATEGORY_BUDGET_PREPARATION = "BUDGET_PREPARATION";
    public static final String RULE_CATEGORY_BUDGET_EXECUTION = "BUDGET_EXECUTION";
    public static final String RULE_CATEGORY_BUDGET_ANALYSIS = "BUDGET_ANALYSIS";

    /**
     * 规则级别常量
     */
    public static final String RULE_LEVEL_SYSTEM = "SYSTEM";
    public static final String RULE_LEVEL_ORGANIZATION = "ORGANIZATION";
    public static final String RULE_LEVEL_USER = "USER";

    /**
     * 规则状态常量
     */
    public static final String RULE_STATUS_ACTIVE = "ACTIVE";
    public static final String RULE_STATUS_INACTIVE = "INACTIVE";
    public static final String RULE_STATUS_DRAFT = "DRAFT";
    public static final String RULE_STATUS_ARCHIVED = "ARCHIVED";

    /**
     * 规则优先级常量
     */
    public static final String RULE_PRIORITY_HIGH = "HIGH";
    public static final String RULE_PRIORITY_MEDIUM = "MEDIUM";
    public static final String RULE_PRIORITY_LOW = "LOW";

    /**
     * 适用范围常量
     */
    public static final String APPLICABLE_SCOPE_ALL = "ALL";
    public static final String APPLICABLE_SCOPE_SPECIFIC = "SPECIFIC";
    public static final String APPLICABLE_SCOPE_CONDITIONAL = "CONDITIONAL";

    /**
     * 错误处理方式常量
     */
    public static final String ERROR_HANDLING_STOP = "STOP";
    public static final String ERROR_HANDLING_WARNING = "WARNING";
    public static final String ERROR_HANDLING_IGNORE = "IGNORE";
}
