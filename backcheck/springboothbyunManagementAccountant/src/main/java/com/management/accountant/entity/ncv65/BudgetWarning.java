package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算预警实体类
 * 
 * @description 预算预警管理实体，支持多级预警和智能预警
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_WARNING")
public class BudgetWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 预警编码
     */
    @TableField("WARNING_CODE")
    private String warningCode;

    /**
     * 预警标题
     */
    @TableField("WARNING_TITLE")
    private String warningTitle;

    /**
     * 预警类型：budget_exceed-预算超支，variance-差异预警，trend-趋势预警，intelligent-智能预警
     */
    @TableField("WARNING_TYPE")
    private String warningType;

    /**
     * 预警级别：low-低，medium-中，high-高，critical-严重
     */
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    /**
     * 预警描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 预警消息
     */
    @TableField("WARNING_MESSAGE")
    private String warningMessage;

    /**
     * 控制规则ID
     */
    @TableField("CONTROL_RULE_ID")
    private String controlRuleId;

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
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 指标ID
     */
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

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
     * 维度值（JSON格式）
     */
    @TableField("DIMENSION_VALUES")
    private String dimensionValues;

    /**
     * 预算值
     */
    @TableField("BUDGET_VALUE")
    private BigDecimal budgetValue;

    /**
     * 实际值
     */
    @TableField("ACTUAL_VALUE")
    private BigDecimal actualValue;

    /**
     * 差异值
     */
    @TableField("VARIANCE_VALUE")
    private BigDecimal varianceValue;

    /**
     * 差异率（百分比）
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 阈值
     */
    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    /**
     * 超出金额
     */
    @TableField("EXCEED_AMOUNT")
    private BigDecimal exceedAmount;

    /**
     * 超出比例（百分比）
     */
    @TableField("EXCEED_RATE")
    private BigDecimal exceedRate;

    /**
     * 预警时间
     */
    @TableField("WARNING_TIME")
    private LocalDateTime warningTime;

    /**
     * 预警状态：new-新建，processing-处理中，resolved-已解决，ignored-已忽略
     */
    @TableField("WARNING_STATUS")
    private String warningStatus;

    /**
     * 处理人ID
     */
    @TableField("HANDLER_ID")
    private String handlerId;

    /**
     * 处理人姓名
     */
    @TableField("HANDLER_NAME")
    private String handlerName;

    /**
     * 处理时间
     */
    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    /**
     * 处理意见
     */
    @TableField("HANDLE_COMMENT")
    private String handleComment;

    /**
     * 解决方案
     */
    @TableField("SOLUTION")
    private String solution;

    /**
     * 是否已通知
     */
    @TableField("IS_NOTIFIED")
    private Boolean isNotified;

    /**
     * 通知时间
     */
    @TableField("NOTIFY_TIME")
    private LocalDateTime notifyTime;

    /**
     * 通知人员ID列表（JSON格式）
     */
    @TableField("NOTIFY_USER_IDS")
    private String notifyUserIds;

    /**
     * 通知方式：email-邮件，sms-短信，system-系统消息
     */
    @TableField("NOTIFY_METHODS")
    private String notifyMethods;

    /**
     * 优先级：high-高，medium-中，low-低
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 预警来源：system-系统自动，manual-手工创建
     */
    @TableField("WARNING_SOURCE")
    private String warningSource;

    /**
     * 关联数据ID
     */
    @TableField("RELATED_DATA_ID")
    private String relatedDataId;

    /**
     * 关联数据类型
     */
    @TableField("RELATED_DATA_TYPE")
    private String relatedDataType;

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

    // 预警类型常量
    public static final String WARNING_TYPE_BUDGET_EXCEED = "budget_exceed";
    public static final String WARNING_TYPE_VARIANCE = "variance";
    public static final String WARNING_TYPE_TREND = "trend";
    public static final String WARNING_TYPE_INTELLIGENT = "intelligent";

    // 预警级别常量
    public static final String WARNING_LEVEL_LOW = "low";
    public static final String WARNING_LEVEL_MEDIUM = "medium";
    public static final String WARNING_LEVEL_HIGH = "high";
    public static final String WARNING_LEVEL_CRITICAL = "critical";

    // 预警状态常量
    public static final String WARNING_STATUS_NEW = "new";
    public static final String WARNING_STATUS_PROCESSING = "processing";
    public static final String WARNING_STATUS_RESOLVED = "resolved";
    public static final String WARNING_STATUS_IGNORED = "ignored";

    // 优先级常量
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_LOW = "low";

    // 预警来源常量
    public static final String WARNING_SOURCE_SYSTEM = "system";
    public static final String WARNING_SOURCE_MANUAL = "manual";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
