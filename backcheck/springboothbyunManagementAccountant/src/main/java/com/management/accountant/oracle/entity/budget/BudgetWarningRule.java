package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 预算预警规则实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_WARNING_RULE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetWarningRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警规则ID (主键)
     * 前端字段: id / warningRuleId
     */
    @TableId(value = "WARNING_RULE_ID", type = IdType.ASSIGN_UUID)
    @JsonProperty("id")
    @JsonAlias({"id", "warningRuleId"})
    private String warningRuleId;

    /**
     * 规则编码
     * 前端字段: warningCode
     */
    @TableField("RULE_CODE")
    @ExcelField(title = "规则编码", sort = 10, width = 4000)
    @JsonProperty("warningCode")
    @JsonAlias({"warningCode", "ruleCode"})
    private String ruleCode;

    /**
     * 规则名称
     * 前端字段: warningName
     */
    @TableField("RULE_NAME")
    @ExcelField(title = "规则名称", sort = 20, width = 6000)
    @JsonProperty("warningName")
    @JsonAlias({"warningName", "ruleName"})
    private String ruleName;

    /**
     * 预警类型 (BUDGET_EXCEED/EXECUTION_LOW/VARIANCE_HIGH/TIME_OVERDUE/APPROVAL_DELAY/CUSTOM)
     */
    @TableField("WARNING_TYPE")
    @ExcelField(title = "预警类型", sort = 30, width = 3000)
    private String warningType;

    /**
     * 阈值（存数值字符串）
     * 前端字段: thresholdValue
     */
    @TableField("THRESHOLD")
    @ExcelField(title = "阈值", sort = 40, width = 3000)
    @JsonProperty("thresholdValue")
    @JsonAlias({"thresholdValue", "threshold"})
    private String threshold;

    /**
     * 比较类型 (GT/LT/EQ/GTE/LTE)
     */
    @TableField("COMPARE_TYPE")
    @ExcelField(title = "比较类型", sort = 50, width = 3000)
    private String compareType;

    /**
     * 预警级别 (HIGH/MEDIUM/LOW/CRITICAL)
     */
    @TableField("WARNING_LEVEL")
    @ExcelField(title = "预警级别", sort = 60, width = 3000)
    private String warningLevel;

    /**
     * 预警状态 (ACTIVE-活跃 INACTIVE-停用 TRIGGERED-已触发)
     */
    @TableField("WARNING_STATUS")
    @JsonProperty("warningStatus")
    private String warningStatus;

    /**
     * 处理状态 (PENDING-待处理 PROCESSING-处理中 RESOLVED-已解决 IGNORED-已忽略)
     */
    @TableField("PROCESS_STATUS")
    @JsonProperty("processStatus")
    private String processStatus;

    /**
     * 当前执行值
     */
    @TableField("CURRENT_VALUE")
    @JsonProperty("currentValue")
    private java.math.BigDecimal currentValue;

    /**
     * 超出率(%)
     */
    @TableField("EXCEED_RATE")
    @JsonProperty("exceedRate")
    private java.math.BigDecimal exceedRate;

    /**
     * 最后触发时间
     */
    @TableField("LAST_TRIGGER_TIME")
    @JsonProperty("lastTriggerTime")
    private Date lastTriggerTime;

    /**
     * 通知方式，逗号分隔字符串（EMAIL,SMS,SYSTEM,WECHAT）
     * 前端用 notificationMethods (List<String>)，序列化/反序列化时转换
     * 此字段用于数据库存储，前端交互由 notificationMethods 承接
     */
    @TableField("NOTIFICATION_TYPE")
    @ExcelField(title = "通知方式", sort = 70, width = 4000)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String notificationType;

    /**
     * 通知方式列表（前端交互字段，不持久化到独立列）
     * 读写时自动与 notificationType 互转
     */
    @TableField(exist = false)
    @JsonProperty("notificationMethods")
    @JsonAlias({"notificationMethods"})
    private List<String> notificationMethods;

    /**
     * 通知用户ID列表（逗号分隔字符串，数据库存储）
     * 此字段仅用于数据库存储，不参与JSON序列化
     */
    @TableField("NOTIFICATION_USERS")
    @JsonIgnore
    private String notificationUsers;

    /**
     * 通知用户ID列表（前端交互字段，不持久化到独立列）
     */
    @TableField(exist = false)
    @JsonProperty("notificationUsers")
    @JsonAlias({"notificationUsers"})
    private List<Object> notificationUserList;

    /**
     * 适用范围
     */
    @TableField("APPLICABLE_SCOPE")
    @ExcelField(title = "适用范围", sort = 80, width = 4000)
    private String applicableScope;

    /**
     * 是否启用 (1:启用 0:停用)
     * 前端字段: isActive (boolean)
     */
    @TableField("IS_ENABLED")
    @ExcelField(title = "是否启用", sort = 90, width = 2500)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer isEnabled;

    /**
     * 前端 isActive 字段（boolean），序列化/反序列化时与 isEnabled 互转
     */
    @TableField(exist = false)
    @JsonProperty("isActive")
    @JsonAlias({"isActive"})
    private Boolean isActive;

    /**
     * 规则描述
     * 前端字段: warningDescription
     */
    @TableField("DESCRIPTION")
    @ExcelField(title = "规则描述", sort = 100, width = 8000)
    @JsonProperty("warningDescription")
    @JsonAlias({"warningDescription", "description"})
    private String description;

    /**
     * 阈值单位（前端字段 thresholdUnit，存入 REMARK 字段）
     */
    @TableField(exist = false)
    @JsonProperty("thresholdUnit")
    @JsonAlias({"thresholdUnit"})
    private String thresholdUnit;

    /**
     * 预警条件列表（前端字段 warningConditions，JSON序列化存入 APPLICABLE_SCOPE 字段）
     */
    @TableField(exist = false)
    @JsonProperty("warningConditions")
    @JsonAlias({"warningConditions"})
    private List<Object> warningConditions;

    /**
     * 自动处理（前端字段 autoProcess，不持久化）
     */
    @TableField(exist = false)
    @JsonProperty("autoProcess")
    @JsonAlias({"autoProcess"})
    private Boolean autoProcess;

    /**
     * 记录触发日志（前端字段 logTrigger，不持久化）
     */
    @TableField(exist = false)
    @JsonProperty("logTrigger")
    @JsonAlias({"logTrigger"})
    private Boolean logTrigger;

    /**
     * 备注（用于存储 thresholdUnit 等扩展信息）
     */
    @TableField("REMARK")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(title = "创建时间", sort = 110, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    @TableLogic
    @TableField("DEL_FLAG")
    private Integer delFlag;
}

