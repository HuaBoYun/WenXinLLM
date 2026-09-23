package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 风险监控查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskMonitoringQueryVO extends BaseVo {

    /**
     * 风险监控ID
     */
    private String riskMonitoringId;

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 监控日期开始
     */
    private LocalDate monitoringDateStart;

    /**
     * 监控日期结束
     */
    private LocalDate monitoringDateEnd;

    /**
     * 监控类型
     */
    private String monitoringType;

    /**
     * 监控频率
     */
    private String monitoringFrequency;

    /**
     * 监控状态
     */
    private String monitoringStatus;

    /**
     * 数据来源
     */
    private String dataSource;

    // ==================== 监控指标查询 ====================

    /**
     * 监控指标名称
     */
    private String indicatorName;

    /**
     * 监控指标类型
     */
    private String indicatorType;

    /**
     * 当前值最小值
     */
    private BigDecimal currentValueMin;

    /**
     * 当前值最大值
     */
    private BigDecimal currentValueMax;

    /**
     * 目标值最小值
     */
    private BigDecimal targetValueMin;

    /**
     * 目标值最大值
     */
    private BigDecimal targetValueMax;

    /**
     * 预警阈值最小值
     */
    private BigDecimal warningThresholdMin;

    /**
     * 预警阈值最大值
     */
    private BigDecimal warningThresholdMax;

    /**
     * 危险阈值最小值
     */
    private BigDecimal dangerThresholdMin;

    /**
     * 危险阈值最大值
     */
    private BigDecimal dangerThresholdMax;

    // ==================== 变化分析查询 ====================

    /**
     * 变化幅度最小值
     */
    private BigDecimal changeMagnitudeMin;

    /**
     * 变化幅度最大值
     */
    private BigDecimal changeMagnitudeMax;

    /**
     * 变化率最小值
     */
    private BigDecimal changeRateMin;

    /**
     * 变化率最大值
     */
    private BigDecimal changeRateMax;

    // ==================== 风险状态查询 ====================

    /**
     * 当前风险等级
     */
    private String currentRiskLevel;

    /**
     * 风险评分最小值
     */
    private BigDecimal riskScoreMin;

    /**
     * 风险评分最大值
     */
    private BigDecimal riskScoreMax;

    /**
     * 风险趋势
     */
    private String riskTrend;

    /**
     * 是否超阈值
     */
    private Boolean isThresholdExceeded;

    /**
     * 超阈值类型
     */
    private String thresholdExceededType;

    /**
     * 超阈值时间开始
     */
    private LocalDateTime thresholdExceededTimeStart;

    /**
     * 超阈值时间结束
     */
    private LocalDateTime thresholdExceededTimeEnd;

    // ==================== 预警信息查询 ====================

    /**
     * 是否触发预警
     */
    private Boolean isAlertTriggered;

    /**
     * 预警等级
     */
    private String alertLevel;

    /**
     * 预警类型
     */
    private String alertType;

    /**
     * 预警触发时间开始
     */
    private LocalDateTime alertTriggerTimeStart;

    /**
     * 预警触发时间结束
     */
    private LocalDateTime alertTriggerTimeEnd;

    /**
     * 预警确认状态
     */
    private String alertConfirmationStatus;

    /**
     * 预警确认人
     */
    private String alertConfirmedBy;

    /**
     * 预警确认时间开始
     */
    private LocalDateTime alertConfirmationTimeStart;

    /**
     * 预警确认时间结束
     */
    private LocalDateTime alertConfirmationTimeEnd;

    // ==================== 监控配置查询 ====================

    /**
     * 自动监控
     */
    private Boolean autoMonitoring;

    /**
     * 监控间隔最小值（分钟）
     */
    private Integer monitoringIntervalMin;

    /**
     * 监控间隔最大值（分钟）
     */
    private Integer monitoringIntervalMax;

    /**
     * 数据更新频率
     */
    private String dataUpdateFrequency;

    /**
     * 最后更新时间开始
     */
    private LocalDateTime lastUpdateTimeStart;

    /**
     * 最后更新时间结束
     */
    private LocalDateTime lastUpdateTimeEnd;

    /**
     * 下次监控时间开始
     */
    private LocalDateTime nextMonitoringTimeStart;

    /**
     * 下次监控时间结束
     */
    private LocalDateTime nextMonitoringTimeEnd;

    // ==================== 历史数据查询 ====================

    /**
     * 历史最高值最小值
     */
    private BigDecimal historicalMaxValueMin;

    /**
     * 历史最高值最大值
     */
    private BigDecimal historicalMaxValueMax;

    /**
     * 历史最低值最小值
     */
    private BigDecimal historicalMinValueMin;

    /**
     * 历史最低值最大值
     */
    private BigDecimal historicalMinValueMax;

    /**
     * 历史平均值最小值
     */
    private BigDecimal historicalAvgValueMin;

    /**
     * 历史平均值最大值
     */
    private BigDecimal historicalAvgValueMax;

    /**
     * 标准差最小值
     */
    private BigDecimal standardDeviationMin;

    /**
     * 标准差最大值
     */
    private BigDecimal standardDeviationMax;

    // ==================== 异常检测查询 ====================

    /**
     * 是否异常
     */
    private Boolean isAnomaly;

    /**
     * 异常类型
     */
    private String anomalyType;

    /**
     * 异常检测时间开始
     */
    private LocalDateTime anomalyDetectionTimeStart;

    /**
     * 异常检测时间结束
     */
    private LocalDateTime anomalyDetectionTimeEnd;

    /**
     * 异常严重程度
     */
    private String anomalySeverity;

    /**
     * 异常影响范围
     */
    private String anomalyImpactScope;

    // ==================== 处理信息查询 ====================

    /**
     * 处理状态
     */
    private String handlingStatus;

    /**
     * 处理人员
     */
    private String handler;

    /**
     * 处理开始时间开始
     */
    private LocalDateTime handlingStartTimeStart;

    /**
     * 处理开始时间结束
     */
    private LocalDateTime handlingStartTimeEnd;

    /**
     * 处理完成时间开始
     */
    private LocalDateTime handlingCompletionTimeStart;

    /**
     * 处理完成时间结束
     */
    private LocalDateTime handlingCompletionTimeEnd;

    /**
     * 处理效果评估
     */
    private String handlingEffectiveness;

    // ==================== 通知信息查询 ====================

    /**
     * 是否已通知
     */
    private Boolean isNotified;

    /**
     * 通知方式
     */
    private String notificationMethod;

    /**
     * 通知时间开始
     */
    private LocalDateTime notificationTimeStart;

    /**
     * 通知时间结束
     */
    private LocalDateTime notificationTimeEnd;

    /**
     * 通知状态
     */
    private String notificationStatus;

    // ==================== 时间范围查询 ====================

    /**
     * 更新时间开始
     */
    private LocalDateTime updateTimeStart;

    /**
     * 更新时间结束
     */
    private LocalDateTime updateTimeEnd;

    // ==================== 关键字查询 ====================

    /**
     * 关键字搜索（支持企业名称、指标名称、预警消息等字段模糊查询）
     */
    private String keyword;

    /**
     * 预警消息关键字
     */
    private String alertMessageKeyword;

    /**
     * 异常描述关键字
     */
    private String anomalyDescriptionKeyword;

    /**
     * 处理措施关键字
     */
    private String handlingMeasuresKeyword;

    // ==================== 排序字段 ====================

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection;

    // ==================== 统计查询 ====================

    /**
     * 是否需要统计信息
     */
    private Boolean needStatistics;

    /**
     * 统计维度（按类型、按等级、按状态等）
     */
    private String statisticsDimension;

    /**
     * 分组字段
     */
    private String groupBy;

    // ==================== 导出查询 ====================

    /**
     * 是否导出查询
     */
    private Boolean isExport;

    /**
     * 导出格式
     */
    private String exportFormat;

    /**
     * 导出字段列表
     */
    private String exportFields;

}
