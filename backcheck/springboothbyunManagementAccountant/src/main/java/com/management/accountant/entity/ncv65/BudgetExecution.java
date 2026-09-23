package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算执行监控实体类
 * 
 * @description 预算执行监控实体，支持预算执行情况的实时监控和分析
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_EXECUTION")
public class BudgetExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 执行编码
     */
    @TableField("EXECUTION_CODE")
    private String executionCode;

    /**
     * 执行名称
     */
    @TableField("EXECUTION_NAME")
    private String executionName;

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
     * 指标ID
     */
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 指标编码
     */
    @TableField("INDICATOR_CODE")
    private String indicatorCode;

    /**
     * 维度组合（JSON格式）
     */
    @TableField("DIMENSION_COMBINATION")
    private String dimensionCombination;

    /**
     * 执行期间（格式：YYYY-MM）
     */
    @TableField("EXECUTION_PERIOD")
    private String executionPeriod;

    /**
     * 期间类型：annual-年度，quarterly-季度，monthly-月度，weekly-周，daily-日
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    /**
     * 已使用金额
     */
    @TableField("USED_AMOUNT")
    private BigDecimal usedAmount;

    /**
     * 剩余金额
     */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /**
     * 执行金额
     */
    @TableField("EXECUTION_AMOUNT")
    private BigDecimal executionAmount;

    /**
     * 执行率（百分比）
     */
    @TableField("EXECUTION_RATE")
    private BigDecimal executionRate;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 差异率（百分比）
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 预测金额
     */
    @TableField("FORECAST_AMOUNT")
    private BigDecimal forecastAmount;

    /**
     * 预测执行率（百分比）
     */
    @TableField("FORECAST_EXECUTION_RATE")
    private BigDecimal forecastExecutionRate;

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
     * 执行状态：normal-正常，warning-预警，over-超支，under-不足
     */
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    /**
     * 控制状态：controlled-受控，uncontrolled-失控，monitoring-监控中
     */
    @TableField("CONTROL_STATUS")
    private String controlStatus;

    /**
     * 预警级别：none-无预警，low-低级，medium-中级，high-高级，critical-严重
     */
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    /**
     * 预警原因
     */
    @TableField("WARNING_REASON")
    private String warningReason;

    /**
     * 预警时间
     */
    @TableField("WARNING_TIME")
    private LocalDateTime warningTime;

    /**
     * 执行趋势：increasing-上升，decreasing-下降，stable-稳定，volatile-波动
     */
    @TableField("EXECUTION_TREND")
    private String executionTrend;

    /**
     * 趋势分析（JSON格式）
     */
    @TableField("TREND_ANALYSIS")
    private String trendAnalysis;

    /**
     * 执行分析（JSON格式）
     */
    @TableField("EXECUTION_ANALYSIS")
    private String executionAnalysis;

    /**
     * 风险评估（JSON格式）
     */
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    /**
     * 改进建议（JSON格式）
     */
    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    /**
     * 数据来源：manual-手工录入，import-导入，sync-同步，calculation-计算
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据来源配置（JSON格式）
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 最后更新时间
     */
    @TableField("LAST_UPDATE_TIME")
    private LocalDateTime lastUpdateTime;

    /**
     * 更新频率：realtime-实时，hourly-每小时，daily-每日，weekly-每周，monthly-每月
     */
    @TableField("UPDATE_FREQUENCY")
    private String updateFrequency;

    /**
     * 下次更新时间
     */
    @TableField("NEXT_UPDATE_TIME")
    private LocalDateTime nextUpdateTime;

    /**
     * 监控配置（JSON格式）
     */
    @TableField("MONITORING_CONFIG")
    private String monitoringConfig;

    /**
     * 通知配置（JSON格式）
     */
    @TableField("NOTIFICATION_CONFIG")
    private String notificationConfig;

    /**
     * 是否自动监控
     */
    @TableField("IS_AUTO_MONITORING")
    private Boolean isAutoMonitoring;

    /**
     * 是否自动预警
     */
    @TableField("IS_AUTO_WARNING")
    private Boolean isAutoWarning;

    /**
     * 是否自动通知
     */
    @TableField("IS_AUTO_NOTIFICATION")
    private Boolean isAutoNotification;

    /**
     * 负责人ID
     */
    @TableField("OWNER_ID")
    private String ownerId;

    /**
     * 负责人姓名
     */
    @TableField("OWNER_NAME")
    private String ownerName;

    /**
     * 监控人ID
     */
    @TableField("MONITOR_ID")
    private String monitorId;

    /**
     * 监控人姓名
     */
    @TableField("MONITOR_NAME")
    private String monitorName;

    /**
     * 关联业务ID
     */
    @TableField("BUSINESS_ID")
    private String businessId;

    /**
     * 关联业务类型
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;

    /**
     * 执行明细（JSON格式）
     */
    @TableField("EXECUTION_DETAILS")
    private String executionDetails;

    /**
     * 历史记录（JSON格式）
     */
    @TableField("HISTORY_RECORDS")
    private String historyRecords;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，suspended-暂停
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
    public static final String PERIOD_TYPE_ANNUAL = "annual";
    public static final String PERIOD_TYPE_QUARTERLY = "quarterly";
    public static final String PERIOD_TYPE_MONTHLY = "monthly";
    public static final String PERIOD_TYPE_WEEKLY = "weekly";
    public static final String PERIOD_TYPE_DAILY = "daily";

    public static final String EXECUTION_STATUS_NORMAL = "normal";
    public static final String EXECUTION_STATUS_WARNING = "warning";
    public static final String EXECUTION_STATUS_OVER = "over";
    public static final String EXECUTION_STATUS_UNDER = "under";

    public static final String CONTROL_STATUS_CONTROLLED = "controlled";
    public static final String CONTROL_STATUS_UNCONTROLLED = "uncontrolled";
    public static final String CONTROL_STATUS_MONITORING = "monitoring";

    public static final String WARNING_LEVEL_NONE = "none";
    public static final String WARNING_LEVEL_LOW = "low";
    public static final String WARNING_LEVEL_MEDIUM = "medium";
    public static final String WARNING_LEVEL_HIGH = "high";
    public static final String WARNING_LEVEL_CRITICAL = "critical";

    public static final String EXECUTION_TREND_INCREASING = "increasing";
    public static final String EXECUTION_TREND_DECREASING = "decreasing";
    public static final String EXECUTION_TREND_STABLE = "stable";
    public static final String EXECUTION_TREND_VOLATILE = "volatile";

    public static final String DATA_SOURCE_MANUAL = "manual";
    public static final String DATA_SOURCE_IMPORT = "import";
    public static final String DATA_SOURCE_SYNC = "sync";
    public static final String DATA_SOURCE_CALCULATION = "calculation";

    public static final String UPDATE_FREQUENCY_REALTIME = "realtime";
    public static final String UPDATE_FREQUENCY_HOURLY = "hourly";
    public static final String UPDATE_FREQUENCY_DAILY = "daily";
    public static final String UPDATE_FREQUENCY_WEEKLY = "weekly";
    public static final String UPDATE_FREQUENCY_MONTHLY = "monthly";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_SUSPENDED = "suspended";
}
