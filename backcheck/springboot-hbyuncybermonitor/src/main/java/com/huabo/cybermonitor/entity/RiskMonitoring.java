package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 风险监控实体类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_RISK_MONITORING")
public class RiskMonitoring {

    /**
     * 风险监控ID
     */
    @TableId(value = "RISK_MONITORING_ID", type = IdType.ASSIGN_UUID)
    private String riskMonitoringId;

    /**
     * 企业ID
     */
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    /**
     * 企业名称
     */
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    /**
     * 监控日期
     */
    @TableField("MONITORING_DATE")
    private LocalDate monitoringDate;

    /**
     * 监控类型
     */
    @TableField("MONITORING_TYPE")
    private String monitoringType;

    /**
     * 监控频率
     */
    @TableField("MONITORING_FREQUENCY")
    private String monitoringFrequency;

    /**
     * 监控状态
     */
    @TableField("MONITORING_STATUS")
    private String monitoringStatus;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    // ==================== 监控指标 ====================

    /**
     * 监控指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 监控指标类型
     */
    @TableField("INDICATOR_TYPE")
    private String indicatorType;

    /**
     * 当前值
     */
    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue;

    /**
     * 目标值
     */
    @TableField("TARGET_VALUE")
    private BigDecimal targetValue;

    /**
     * 预警阈值
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 危险阈值
     */
    @TableField("DANGER_THRESHOLD")
    private BigDecimal dangerThreshold;

    /**
     * 上期值
     */
    @TableField("PREVIOUS_VALUE")
    private BigDecimal previousValue;

    /**
     * 变化幅度
     */
    @TableField("CHANGE_MAGNITUDE")
    private BigDecimal changeMagnitude;

    /**
     * 变化率
     */
    @TableField("CHANGE_RATE")
    private BigDecimal changeRate;

    // ==================== 风险状态 ====================

    /**
     * 当前风险等级
     */
    @TableField("CURRENT_RISK_LEVEL")
    private String currentRiskLevel;

    /**
     * 风险评分
     */
    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    /**
     * 风险趋势
     */
    @TableField("RISK_TREND")
    private String riskTrend;

    /**
     * 是否超阈值
     */
    @TableField("IS_THRESHOLD_EXCEEDED")
    private Boolean isThresholdExceeded;

    /**
     * 超阈值类型
     */
    @TableField("THRESHOLD_EXCEEDED_TYPE")
    private String thresholdExceededType;

    /**
     * 超阈值时间
     */
    @TableField("THRESHOLD_EXCEEDED_TIME")
    private LocalDateTime thresholdExceededTime;

    // ==================== 预警信息 ====================

    /**
     * 是否触发预警
     */
    @TableField("IS_ALERT_TRIGGERED")
    private Boolean isAlertTriggered;

    /**
     * 预警等级
     */
    @TableField("ALERT_LEVEL")
    private String alertLevel;

    /**
     * 预警类型
     */
    @TableField("ALERT_TYPE")
    private String alertType;

    /**
     * 预警消息
     */
    @TableField("ALERT_MESSAGE")
    private String alertMessage;

    /**
     * 预警触发时间
     */
    @TableField("ALERT_TRIGGER_TIME")
    private LocalDateTime alertTriggerTime;

    /**
     * 预警确认状态
     */
    @TableField("ALERT_CONFIRMATION_STATUS")
    private String alertConfirmationStatus;

    /**
     * 预警确认人
     */
    @TableField("ALERT_CONFIRMED_BY")
    private String alertConfirmedBy;

    /**
     * 预警确认时间
     */
    @TableField("ALERT_CONFIRMATION_TIME")
    private LocalDateTime alertConfirmationTime;

    // ==================== 监控配置 ====================

    /**
     * 自动监控
     */
    @TableField("AUTO_MONITORING")
    private Boolean autoMonitoring;

    /**
     * 监控间隔（分钟）
     */
    @TableField("MONITORING_INTERVAL")
    private Integer monitoringInterval;

    /**
     * 数据更新频率
     */
    @TableField("DATA_UPDATE_FREQUENCY")
    private String dataUpdateFrequency;

    /**
     * 最后更新时间
     */
    @TableField("LAST_UPDATE_TIME")
    private LocalDateTime lastUpdateTime;

    /**
     * 下次监控时间
     */
    @TableField("NEXT_MONITORING_TIME")
    private LocalDateTime nextMonitoringTime;

    // ==================== 历史数据 ====================

    /**
     * 历史最高值
     */
    @TableField("HISTORICAL_MAX_VALUE")
    private BigDecimal historicalMaxValue;

    /**
     * 历史最低值
     */
    @TableField("HISTORICAL_MIN_VALUE")
    private BigDecimal historicalMinValue;

    /**
     * 历史平均值
     */
    @TableField("HISTORICAL_AVG_VALUE")
    private BigDecimal historicalAvgValue;

    /**
     * 标准差
     */
    @TableField("STANDARD_DEVIATION")
    private BigDecimal standardDeviation;

    /**
     * 变异系数
     */
    @TableField("COEFFICIENT_OF_VARIATION")
    private BigDecimal coefficientOfVariation;

    // ==================== 异常检测 ====================

    /**
     * 是否异常
     */
    @TableField("IS_ANOMALY")
    private Boolean isAnomaly;

    /**
     * 异常类型
     */
    @TableField("ANOMALY_TYPE")
    private String anomalyType;

    /**
     * 异常描述
     */
    @TableField("ANOMALY_DESCRIPTION")
    private String anomalyDescription;

    /**
     * 异常检测时间
     */
    @TableField("ANOMALY_DETECTION_TIME")
    private LocalDateTime anomalyDetectionTime;

    /**
     * 异常严重程度
     */
    @TableField("ANOMALY_SEVERITY")
    private String anomalySeverity;

    /**
     * 异常影响范围
     */
    @TableField("ANOMALY_IMPACT_SCOPE")
    private String anomalyImpactScope;

    // ==================== 处理信息 ====================

    /**
     * 处理状态
     */
    @TableField("HANDLING_STATUS")
    private String handlingStatus;

    /**
     * 处理人员
     */
    @TableField("HANDLER")
    private String handler;

    /**
     * 处理开始时间
     */
    @TableField("HANDLING_START_TIME")
    private LocalDateTime handlingStartTime;

    /**
     * 处理完成时间
     */
    @TableField("HANDLING_COMPLETION_TIME")
    private LocalDateTime handlingCompletionTime;

    /**
     * 处理措施
     */
    @TableField("HANDLING_MEASURES")
    private String handlingMeasures;

    /**
     * 处理结果
     */
    @TableField("HANDLING_RESULT")
    private String handlingResult;

    /**
     * 处理效果评估
     */
    @TableField("HANDLING_EFFECTIVENESS")
    private String handlingEffectiveness;

    // ==================== 通知信息 ====================

    /**
     * 是否已通知
     */
    @TableField("IS_NOTIFIED")
    private Boolean isNotified;

    /**
     * 通知方式
     */
    @TableField("NOTIFICATION_METHOD")
    private String notificationMethod;

    /**
     * 通知对象
     */
    @TableField("NOTIFICATION_RECIPIENTS")
    private String notificationRecipients;

    /**
     * 通知时间
     */
    @TableField("NOTIFICATION_TIME")
    private LocalDateTime notificationTime;

    /**
     * 通知内容
     */
    @TableField("NOTIFICATION_CONTENT")
    private String notificationContent;

    /**
     * 通知状态
     */
    @TableField("NOTIFICATION_STATUS")
    private String notificationStatus;

    // ==================== 系统字段 ====================

    /**
     * 备注
     */
    @TableField("REMARKS")
    private String remarks;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("DELETED")
    @TableLogic
    private Boolean deleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Long version;

    // ==================== 常量定义 ====================

    /**
     * 监控类型常量
     */
    public static final String MONITORING_TYPE_REAL_TIME = "REAL_TIME"; // 实时监控
    public static final String MONITORING_TYPE_PERIODIC = "PERIODIC"; // 定期监控
    public static final String MONITORING_TYPE_EVENT_DRIVEN = "EVENT_DRIVEN"; // 事件驱动监控
    public static final String MONITORING_TYPE_THRESHOLD = "THRESHOLD"; // 阈值监控
    public static final String MONITORING_TYPE_TREND = "TREND"; // 趋势监控

    /**
     * 监控频率常量
     */
    public static final String FREQUENCY_CONTINUOUS = "CONTINUOUS"; // 连续监控
    public static final String FREQUENCY_HOURLY = "HOURLY"; // 每小时
    public static final String FREQUENCY_DAILY = "DAILY"; // 每日
    public static final String FREQUENCY_WEEKLY = "WEEKLY"; // 每周
    public static final String FREQUENCY_MONTHLY = "MONTHLY"; // 每月
    public static final String FREQUENCY_QUARTERLY = "QUARTERLY"; // 每季度

    /**
     * 监控状态常量
     */
    public static final String STATUS_ACTIVE = "ACTIVE"; // 活跃
    public static final String STATUS_INACTIVE = "INACTIVE"; // 非活跃
    public static final String STATUS_SUSPENDED = "SUSPENDED"; // 暂停
    public static final String STATUS_ERROR = "ERROR"; // 错误

    /**
     * 指标类型常量
     */
    public static final String INDICATOR_TYPE_FINANCIAL = "FINANCIAL"; // 财务指标
    public static final String INDICATOR_TYPE_OPERATIONAL = "OPERATIONAL"; // 经营指标
    public static final String INDICATOR_TYPE_COMPLIANCE = "COMPLIANCE"; // 合规指标
    public static final String INDICATOR_TYPE_GOVERNANCE = "GOVERNANCE"; // 治理指标
    public static final String INDICATOR_TYPE_EXTERNAL = "EXTERNAL"; // 外部指标

    /**
     * 预警等级常量
     */
    public static final String ALERT_LEVEL_INFO = "INFO"; // 信息
    public static final String ALERT_LEVEL_WARNING = "WARNING"; // 警告
    public static final String ALERT_LEVEL_CRITICAL = "CRITICAL"; // 严重
    public static final String ALERT_LEVEL_EMERGENCY = "EMERGENCY"; // 紧急

    /**
     * 异常类型常量
     */
    public static final String ANOMALY_TYPE_SPIKE = "SPIKE"; // 突增
    public static final String ANOMALY_TYPE_DROP = "DROP"; // 突降
    public static final String ANOMALY_TYPE_TREND_CHANGE = "TREND_CHANGE"; // 趋势变化
    public static final String ANOMALY_TYPE_OUTLIER = "OUTLIER"; // 离群值
    public static final String ANOMALY_TYPE_PATTERN_BREAK = "PATTERN_BREAK"; // 模式中断

}
