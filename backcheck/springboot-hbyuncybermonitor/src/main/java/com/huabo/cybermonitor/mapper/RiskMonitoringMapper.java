package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.RiskMonitoring;
import com.huabo.cybermonitor.vo.RiskMonitoringQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 风险监控数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface RiskMonitoringMapper extends BaseMapper<RiskMonitoring> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据查询条件获取风险监控列表
     */
    List<RiskMonitoring> selectByCondition(@Param("query") RiskMonitoringQueryVO query);

    /**
     * 根据查询条件统计风险监控数量
     */
    Long countByCondition(@Param("query") RiskMonitoringQueryVO query);

    /**
     * 根据企业ID获取风险监控列表
     */
    List<RiskMonitoring> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID和监控类型获取风险监控列表
     */
    List<RiskMonitoring> selectByEnterpriseIdAndType(@Param("enterpriseId") String enterpriseId, 
                                                     @Param("monitoringType") String monitoringType);

    /**
     * 根据监控指标名称获取风险监控列表
     */
    List<RiskMonitoring> selectByIndicatorName(@Param("indicatorName") String indicatorName);

    // ==================== 实时监控查询 ====================

    /**
     * 获取活跃的风险监控列表
     */
    List<RiskMonitoring> selectActiveMonitoring();

    /**
     * 获取实时监控数据
     */
    List<RiskMonitoring> selectRealTimeMonitoring();

    /**
     * 根据监控状态获取风险监控列表
     */
    List<RiskMonitoring> selectByMonitoringStatus(@Param("monitoringStatus") String monitoringStatus);

    /**
     * 获取需要更新的监控项
     */
    List<RiskMonitoring> selectPendingUpdate();

    // ==================== 阈值监控查询 ====================

    /**
     * 获取超阈值的风险监控列表
     */
    List<RiskMonitoring> selectThresholdExceeded();

    /**
     * 根据超阈值类型获取风险监控列表
     */
    List<RiskMonitoring> selectByThresholdExceededType(@Param("thresholdExceededType") String thresholdExceededType);

    /**
     * 获取接近阈值的风险监控列表
     */
    List<RiskMonitoring> selectNearThreshold(@Param("thresholdPercentage") BigDecimal thresholdPercentage);

    /**
     * 根据当前值范围获取风险监控列表
     */
    List<RiskMonitoring> selectByCurrentValueRange(@Param("minValue") BigDecimal minValue, 
                                                   @Param("maxValue") BigDecimal maxValue);

    // ==================== 预警查询 ====================

    /**
     * 获取触发预警的风险监控列表
     */
    List<RiskMonitoring> selectTriggeredAlerts();

    /**
     * 根据预警等级获取风险监控列表
     */
    List<RiskMonitoring> selectByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 获取未确认的预警列表
     */
    List<RiskMonitoring> selectUnconfirmedAlerts();

    /**
     * 获取最近预警统计
     */
    List<Map<String, Object>> selectRecentAlertStatistics(@Param("days") Integer days);

    // ==================== 趋势分析 ====================

    /**
     * 根据风险趋势获取风险监控列表
     */
    List<RiskMonitoring> selectByRiskTrend(@Param("riskTrend") String riskTrend);

    /**
     * 获取指标趋势分析数据
     */
    List<Map<String, Object>> selectIndicatorTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("indicatorName") String indicatorName, 
                                                           @Param("days") Integer days);

    /**
     * 获取风险变化趋势统计
     */
    List<Map<String, Object>> selectRiskChangeTrendStatistics();

    /**
     * 根据变化率范围获取风险监控列表
     */
    List<RiskMonitoring> selectByChangeRateRange(@Param("minRate") BigDecimal minRate, 
                                                 @Param("maxRate") BigDecimal maxRate);

    // ==================== 异常检测查询 ====================

    /**
     * 获取异常的风险监控列表
     */
    List<RiskMonitoring> selectAnomalies();

    /**
     * 根据异常类型获取风险监控列表
     */
    List<RiskMonitoring> selectByAnomalyType(@Param("anomalyType") String anomalyType);

    /**
     * 根据异常严重程度获取风险监控列表
     */
    List<RiskMonitoring> selectByAnomalySeverity(@Param("anomalySeverity") String anomalySeverity);

    /**
     * 获取异常检测统计数据
     */
    List<Map<String, Object>> selectAnomalyDetectionStatistics();

    // ==================== 监控配置查询 ====================

    /**
     * 获取自动监控的风险监控列表
     */
    List<RiskMonitoring> selectAutoMonitoring();

    /**
     * 根据监控频率获取风险监控列表
     */
    List<RiskMonitoring> selectByMonitoringFrequency(@Param("monitoringFrequency") String monitoringFrequency);

    /**
     * 根据监控间隔范围获取风险监控列表
     */
    List<RiskMonitoring> selectByMonitoringIntervalRange(@Param("minInterval") Integer minInterval, 
                                                         @Param("maxInterval") Integer maxInterval);

    /**
     * 获取需要执行的监控任务
     */
    List<RiskMonitoring> selectScheduledMonitoringTasks();

    // ==================== 历史数据分析 ====================

    /**
     * 获取历史数据统计分析
     */
    List<Map<String, Object>> selectHistoricalDataAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("indicatorName") String indicatorName);

    /**
     * 根据标准差范围获取风险监控列表
     */
    List<RiskMonitoring> selectByStandardDeviationRange(@Param("minStdDev") BigDecimal minStdDev, 
                                                        @Param("maxStdDev") BigDecimal maxStdDev);

    /**
     * 获取变异系数分析数据
     */
    List<Map<String, Object>> selectCoefficientOfVariationAnalysis();

    /**
     * 获取历史极值分析数据
     */
    List<Map<String, Object>> selectHistoricalExtremeValueAnalysis();

    // ==================== 处理状态查询 ====================

    /**
     * 根据处理状态获取风险监控列表
     */
    List<RiskMonitoring> selectByHandlingStatus(@Param("handlingStatus") String handlingStatus);

    /**
     * 获取待处理的风险监控列表
     */
    List<RiskMonitoring> selectPendingHandling();

    /**
     * 获取处理中的风险监控列表
     */
    List<RiskMonitoring> selectInHandling();

    /**
     * 获取已完成处理的风险监控列表
     */
    List<RiskMonitoring> selectCompletedHandling();

    // ==================== 通知状态查询 ====================

    /**
     * 获取未通知的风险监控列表
     */
    List<RiskMonitoring> selectUnnotified();

    /**
     * 根据通知方式获取风险监控列表
     */
    List<RiskMonitoring> selectByNotificationMethod(@Param("notificationMethod") String notificationMethod);

    /**
     * 根据通知状态获取风险监控列表
     */
    List<RiskMonitoring> selectByNotificationStatus(@Param("notificationStatus") String notificationStatus);

    /**
     * 获取通知统计数据
     */
    List<Map<String, Object>> selectNotificationStatistics();

    // ==================== 时间维度查询 ====================

    /**
     * 根据监控日期范围获取风险监控列表
     */
    List<RiskMonitoring> selectByMonitoringDateRange(@Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);

    /**
     * 获取按小时统计的监控数据
     */
    List<Map<String, Object>> selectHourlyMonitoringStatistics(@Param("date") LocalDate date);

    /**
     * 获取按日统计的监控数据
     */
    List<Map<String, Object>> selectDailyMonitoringStatistics(@Param("startDate") LocalDate startDate, 
                                                              @Param("endDate") LocalDate endDate);

    /**
     * 获取按月统计的监控数据
     */
    List<Map<String, Object>> selectMonthlyMonitoringStatistics(@Param("year") Integer year);

    // ==================== 综合统计分析 ====================

    /**
     * 获取风险监控综合统计数据
     */
    Map<String, Object> selectComprehensiveStatistics();

    /**
     * 获取企业风险监控概览数据
     */
    Map<String, Object> selectEnterpriseMonitoringOverview(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取监控效果分析数据
     */
    List<Map<String, Object>> selectMonitoringEffectivenessAnalysis();

    /**
     * 获取监控质量分析数据
     */
    List<Map<String, Object>> selectMonitoringQualityAnalysis();

    // ==================== 对比分析 ====================

    /**
     * 获取同业监控对比数据
     */
    List<Map<String, Object>> selectPeerMonitoringComparison(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("industryType") String industryType);

    /**
     * 获取指标对比分析数据
     */
    List<Map<String, Object>> selectIndicatorComparison(@Param("indicatorNames") List<String> indicatorNames);

    // ==================== 批量操作 ====================

    /**
     * 批量更新监控状态
     */
    Integer batchUpdateMonitoringStatus(@Param("ids") List<String> ids, 
                                       @Param("status") String status, 
                                       @Param("updateBy") String updateBy);

    /**
     * 批量确认预警
     */
    Integer batchConfirmAlerts(@Param("ids") List<String> ids, 
                              @Param("confirmedBy") String confirmedBy, 
                              @Param("confirmationTime") LocalDateTime confirmationTime);

    /**
     * 批量更新阈值
     */
    Integer batchUpdateThresholds(@Param("ids") List<String> ids, 
                                 @Param("warningThreshold") BigDecimal warningThreshold, 
                                 @Param("dangerThreshold") BigDecimal dangerThreshold, 
                                 @Param("updateBy") String updateBy);

}
