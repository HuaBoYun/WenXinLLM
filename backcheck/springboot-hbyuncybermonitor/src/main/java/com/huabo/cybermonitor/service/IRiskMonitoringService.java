package com.huabo.cybermonitor.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.RiskMonitoring;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.RiskMonitoringQueryVO;

/**
 * 风险监控业务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IRiskMonitoringService extends IService<RiskMonitoring> {

    // ==================== 基础业务方法 ====================

    /**
     * 分页查询风险监控
     */
    PageResult<RiskMonitoring> selectByPage(RiskMonitoringQueryVO query);

    /**
     * 根据企业ID获取风险监控列表
     */
    List<RiskMonitoring> getByEnterpriseId(String enterpriseId);

    /**
     * 根据监控指标名称获取风险监控列表
     */
    List<RiskMonitoring> getByIndicatorName(String indicatorName);

    /**
     * 保存风险监控
     */
    boolean saveRiskMonitoring(RiskMonitoring riskMonitoring);

    /**
     * 更新风险监控
     */
    boolean updateRiskMonitoring(RiskMonitoring riskMonitoring);

    /**
     * 删除风险监控
     */
    boolean deleteRiskMonitoring(String riskMonitoringId);

    // ==================== 实时监控业务 ====================

    /**
     * 启动实时监控
     */
    boolean startRealTimeMonitoring(String enterpriseId, String indicatorName);

    /**
     * 停止实时监控
     */
    boolean stopRealTimeMonitoring(String riskMonitoringId);

    /**
     * 获取活跃监控列表
     */
    List<RiskMonitoring> getActiveMonitoring();

    /**
     * 更新监控数据
     */
    boolean updateMonitoringData(String riskMonitoringId, BigDecimal currentValue);

    /**
     * 执行监控任务
     */
    void executeMonitoringTask(String riskMonitoringId);

    /**
     * 获取需要执行的监控任务
     */
    List<RiskMonitoring> getScheduledMonitoringTasks();

    // ==================== 阈值监控业务 ====================

    /**
     * 设置监控阈值
     */
    boolean setMonitoringThresholds(String riskMonitoringId, BigDecimal warningThreshold, BigDecimal dangerThreshold);

    /**
     * 检查阈值超限
     */
    boolean checkThresholdExceeded(RiskMonitoring riskMonitoring);

    /**
     * 获取超阈值监控列表
     */
    List<RiskMonitoring> getThresholdExceededMonitoring();

    /**
     * 获取接近阈值的监控列表
     */
    List<RiskMonitoring> getNearThresholdMonitoring(BigDecimal thresholdPercentage);

    /**
     * 计算阈值达成率
     */
    BigDecimal calculateThresholdAchievementRate(String enterpriseId, String indicatorType);

    // ==================== 预警管理业务 ====================

    /**
     * 触发预警
     */
    boolean triggerAlert(String riskMonitoringId, String alertLevel, String alertMessage);

    /**
     * 确认预警
     */
    boolean confirmAlert(String riskMonitoringId, String confirmedBy, String confirmationComments);

    /**
     * 获取未确认预警列表
     */
    List<RiskMonitoring> getUnconfirmedAlerts();

    /**
     * 获取触发预警的监控列表
     */
    List<RiskMonitoring> getTriggeredAlerts();

    /**
     * 发送预警通知
     */
    boolean sendAlertNotification(RiskMonitoring riskMonitoring);

    /**
     * 获取预警统计数据
     */
    Map<String, Object> getAlertStatistics();

    // ==================== 异常检测业务 ====================

    /**
     * 执行异常检测
     */
    boolean performAnomalyDetection(String riskMonitoringId);

    /**
     * 检测数据异常
     */
    boolean detectDataAnomaly(RiskMonitoring riskMonitoring);

    /**
     * 获取异常监控列表
     */
    List<RiskMonitoring> getAnomalousMonitoring();

    /**
     * 分析异常原因
     */
    String analyzeAnomalyCause(RiskMonitoring riskMonitoring);

    /**
     * 评估异常严重程度
     */
    String assessAnomalySeverity(RiskMonitoring riskMonitoring);

    /**
     * 获取异常检测统计
     */
    Map<String, Object> getAnomalyDetectionStatistics();

    // ==================== 趋势分析业务 ====================

    /**
     * 分析指标趋势
     */
    String analyzeIndicatorTrend(String enterpriseId, String indicatorName, Integer days);

    /**
     * 计算变化率
     */
    BigDecimal calculateChangeRate(RiskMonitoring riskMonitoring);

    /**
     * 预测未来趋势
     */
    Map<String, Object> predictFutureTrend(String enterpriseId, String indicatorName, Integer forecastDays);

    /**
     * 获取趋势分析数据
     */
    List<Map<String, Object>> getTrendAnalysisData(String enterpriseId, String indicatorName, Integer days);

    /**
     * 识别趋势转折点
     */
    List<Map<String, Object>> identifyTrendTurningPoints(String enterpriseId, String indicatorName);

    // ==================== 历史数据分析 ====================

    /**
     * 计算历史统计数据
     */
    Map<String, Object> calculateHistoricalStatistics(String enterpriseId, String indicatorName);

    /**
     * 分析数据波动性
     */
    BigDecimal analyzeDataVolatility(String enterpriseId, String indicatorName);

    /**
     * 获取历史极值
     */
    Map<String, Object> getHistoricalExtremeValues(String enterpriseId, String indicatorName);

    /**
     * 计算移动平均值
     */
    BigDecimal calculateMovingAverage(String enterpriseId, String indicatorName, Integer periods);

    // ==================== 监控配置业务 ====================

    /**
     * 配置自动监控
     */
    boolean configureAutoMonitoring(String riskMonitoringId, Integer monitoringInterval, String dataUpdateFrequency);

    /**
     * 更新监控配置
     */
    boolean updateMonitoringConfiguration(RiskMonitoring riskMonitoring);

    /**
     * 获取监控配置
     */
    Map<String, Object> getMonitoringConfiguration(String riskMonitoringId);

    /**
     * 验证监控配置
     */
    boolean validateMonitoringConfiguration(RiskMonitoring riskMonitoring);

    // ==================== 处理流程业务 ====================

    /**
     * 开始处理监控事件
     */
    boolean startHandling(String riskMonitoringId, String handler, String handlingMeasures);

    /**
     * 完成处理
     */
    boolean completeHandling(String riskMonitoringId, String handlingResult, String handlingEffectiveness);

    /**
     * 获取待处理监控列表
     */
    List<RiskMonitoring> getPendingHandling();

    /**
     * 获取处理中监控列表
     */
    List<RiskMonitoring> getInHandling();

    /**
     * 评估处理效果
     */
    String evaluateHandlingEffectiveness(String riskMonitoringId);

    // ==================== 通知管理业务 ====================

    /**
     * 发送监控通知
     */
    boolean sendMonitoringNotification(String riskMonitoringId, String notificationMethod, String recipients);

    /**
     * 配置通知规则
     */
    boolean configureNotificationRules(String riskMonitoringId, String notificationMethod, String recipients);

    /**
     * 获取通知历史
     */
    List<Map<String, Object>> getNotificationHistory(String riskMonitoringId);

    /**
     * 获取通知统计
     */
    Map<String, Object> getNotificationStatistics();

    // ==================== 统计分析业务 ====================

    /**
     * 获取监控综合统计
     */
    Map<String, Object> getComprehensiveStatistics();

    /**
     * 获取企业监控概览
     */
    Map<String, Object> getEnterpriseMonitoringOverview(String enterpriseId);

    /**
     * 获取监控效果分析
     */
    List<Map<String, Object>> getMonitoringEffectivenessAnalysis();

    /**
     * 获取指标对比分析
     */
    List<Map<String, Object>> getIndicatorComparisonAnalysis(List<String> indicatorNames);

    /**
     * 获取同业对比数据
     */
    List<Map<String, Object>> getPeerComparisonData(String enterpriseId, String industryType);

    // ==================== 报告生成业务 ====================

    /**
     * 生成监控报告
     */
    Map<String, Object> generateMonitoringReport(String enterpriseId, String reportType, Integer days);

    /**
     * 生成预警报告
     */
    Map<String, Object> generateAlertReport(String enterpriseId, Integer days);

    /**
     * 生成异常分析报告
     */
    Map<String, Object> generateAnomalyAnalysisReport(String enterpriseId, Integer days);

    // ==================== 批量操作业务 ====================

    /**
     * 批量更新监控状态
     */
    boolean batchUpdateMonitoringStatus(List<String> riskMonitoringIds, String status, String updateBy);

    /**
     * 批量确认预警
     */
    boolean batchConfirmAlerts(List<String> riskMonitoringIds, String confirmedBy);

    /**
     * 批量更新阈值
     */
    boolean batchUpdateThresholds(List<String> riskMonitoringIds, BigDecimal warningThreshold, BigDecimal dangerThreshold, String updateBy);

    /**
     * 批量启动监控
     */
    boolean batchStartMonitoring(List<String> riskMonitoringIds, String updateBy);

    /**
     * 批量停止监控
     */
    boolean batchStopMonitoring(List<String> riskMonitoringIds, String updateBy);

    // ==================== 导出业务 ====================

    /**
     * 导出监控数据
     */
    List<Map<String, Object>> exportMonitoringData(RiskMonitoringQueryVO query);

    /**
     * 导出监控报告
     */
    byte[] exportMonitoringReport(String enterpriseId, String reportType, Integer days, String format);

    // ==================== 标签转换业务 ====================

    /**
     * 转换监控类型标签
     */
    String convertMonitoringTypeLabel(String monitoringType);

    /**
     * 转换监控频率标签
     */
    String convertMonitoringFrequencyLabel(String monitoringFrequency);

    /**
     * 转换监控状态标签
     */
    String convertMonitoringStatusLabel(String monitoringStatus);

    /**
     * 转换指标类型标签
     */
    String convertIndicatorTypeLabel(String indicatorType);

    /**
     * 转换预警等级标签
     */
    String convertAlertLevelLabel(String alertLevel);

    /**
     * 转换异常类型标签
     */
    String convertAnomalyTypeLabel(String anomalyType);

    /**
     * 转换处理状态标签
     */
    String convertHandlingStatusLabel(String handlingStatus);

}
