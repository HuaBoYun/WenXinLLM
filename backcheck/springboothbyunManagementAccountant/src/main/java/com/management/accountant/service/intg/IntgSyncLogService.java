package com.management.accountant.service.intg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.intg.IntgSyncLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 同步日志服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface IntgSyncLogService extends IService<IntgSyncLog> {

    // 基础CRUD操作

    /**
     * 创建同步日志
     */
    IntgSyncLog createSyncLog(IntgSyncLog syncLog);

    /**
     * 更新同步日志
     */
    IntgSyncLog updateSyncLog(IntgSyncLog syncLog);

    /**
     * 删除同步日志
     */
    boolean deleteSyncLog(String logId);

    /**
     * 根据ID获取同步日志
     */
    IntgSyncLog getSyncLogById(String logId);

    /**
     * 根据编码获取同步日志
     */
    IntgSyncLog getSyncLogByCode(String logCode);

    // 查询操作

    /**
     * 分页查询同步日志
     */
    IPage<IntgSyncLog> getSyncLogPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据系统配置ID查询日志列表
     */
    List<IntgSyncLog> getSyncLogsByConfigId(String configId);

    /**
     * 根据映射配置ID查询日志列表
     */
    List<IntgSyncLog> getSyncLogsByMappingId(String mappingId);

    /**
     * 根据任务ID查询日志列表
     */
    List<IntgSyncLog> getSyncLogsByTaskId(String taskId);

    /**
     * 根据批次号查询日志列表
     */
    List<IntgSyncLog> getSyncLogsByBatchNumber(String batchNumber);

    /**
     * 根据同步状态查询日志列表
     */
    List<IntgSyncLog> getSyncLogsByStatus(String syncStatus);

    /**
     * 根据执行结果查询日志列表
     */
    List<IntgSyncLog> getSyncLogsByResult(String executionResult);

    /**
     * 查询最近的日志列表
     */
    List<IntgSyncLog> getRecentSyncLogs(Integer limit);

    // 日志管理操作

    /**
     * 开始同步日志记录
     */
    IntgSyncLog startSyncLog(String configId, String mappingId, String taskId, String syncType, String syncDirection);

    /**
     * 完成同步日志记录
     */
    boolean completeSyncLog(String logId, String executionResult, Long processedRecords, 
                           Long successRecords, Long failedRecords);

    /**
     * 更新同步进度
     */
    boolean updateSyncProgress(String logId, Long processedRecords, Long successRecords, Long failedRecords);

    /**
     * 记录同步错误
     */
    boolean recordSyncError(String logId, String errorCode, String errorMessage, String errorStack);

    /**
     * 批量更新日志状态
     */
    boolean batchUpdateLogStatus(List<String> logIds, String syncStatus);

    /**
     * 批量更新归档状态
     */
    boolean batchUpdateArchiveStatus(List<String> logIds, String archiveStatus);

    // 日志分析操作

    /**
     * 获取同步趋势分析
     */
    List<Map<String, Object>> getSyncTrendAnalysis(LocalDateTime startTime, LocalDateTime endTime, String granularity);

    /**
     * 获取性能统计分析
     */
    List<Map<String, Object>> getPerformanceStats(List<String> configIds, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取错误统计分析
     */
    List<Map<String, Object>> getErrorStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取数据量统计分析
     */
    List<Map<String, Object>> getDataVolumeStats(List<String> configIds, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取系统使用统计
     */
    List<Map<String, Object>> getSystemUsageStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分析同步模式
     */
    Map<String, Object> analyzeSyncPatterns(LocalDateTime startTime, LocalDateTime endTime);

    // 统计查询操作

    /**
     * 统计日志总数
     */
    Long countSyncLogs();

    /**
     * 按同步类型统计日志数量
     */
    List<Map<String, Object>> countBySyncType();

    /**
     * 按同步方向统计日志数量
     */
    List<Map<String, Object>> countBySyncDirection();

    /**
     * 按同步状态统计日志数量
     */
    List<Map<String, Object>> countBySyncStatus();

    /**
     * 按执行结果统计日志数量
     */
    List<Map<String, Object>> countByExecutionResult();

    /**
     * 按日期统计日志数量
     */
    List<Map<String, Object>> countByDate(LocalDateTime startTime, LocalDateTime endTime, String granularity);

    /**
     * 按系统配置统计日志数量
     */
    List<Map<String, Object>> countBySystemConfig();

    // 日志查找操作

    /**
     * 查找失败的日志
     */
    List<IntgSyncLog> findFailedLogs(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查找需要重试的日志
     */
    List<IntgSyncLog> findLogsForRetry(Integer maxRetryCount);

    /**
     * 查找长时间运行的日志
     */
    List<IntgSyncLog> findLongRunningLogs(Integer thresholdMinutes);

    /**
     * 查找异常日志
     */
    List<IntgSyncLog> findAnomalousLogs(Long thresholdDuration, Double thresholdFailureRate);

    /**
     * 查找相关日志
     */
    List<IntgSyncLog> findRelatedLogs(String logId, String relationshipType);

    /**
     * 查找重复日志
     */
    List<IntgSyncLog> findDuplicateLogs(String configId, String batchNumber);

    // 日志监控操作

    /**
     * 获取实时监控数据
     */
    Map<String, Object> getRealTimeMonitoringData();

    /**
     * 获取系统健康状态
     */
    Map<String, Object> getSystemHealthStatus();

    /**
     * 获取告警信息
     */
    List<Map<String, Object>> getAlertInfo(String alertLevel, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 检测异常模式
     */
    List<Map<String, Object>> detectAnomalousPatterns(String detectionType, Map<String, Object> parameters);

    /**
     * 生成监控报告
     */
    Map<String, Object> generateMonitoringReport(LocalDateTime startTime, LocalDateTime endTime);

    // 日志重试操作

    /**
     * 重试失败的同步
     */
    Map<String, Object> retrySyncLog(String logId);

    /**
     * 批量重试失败的同步
     */
    List<Map<String, Object>> batchRetrySyncLogs(List<String> logIds);

    /**
     * 自动重试失败的同步
     */
    Map<String, Object> autoRetrySyncLogs(Integer maxRetryCount);

    /**
     * 停止重试
     */
    boolean stopRetry(String logId);

    // 日志导出操作

    /**
     * 导出同步日志
     */
    Map<String, Object> exportSyncLogs(List<String> logIds, String exportFormat);

    /**
     * 导出错误日志
     */
    Map<String, Object> exportErrorLogs(LocalDateTime startTime, LocalDateTime endTime, String exportFormat);

    /**
     * 导出统计报告
     */
    Map<String, Object> exportStatisticsReport(LocalDateTime startTime, LocalDateTime endTime, String reportType);

    /**
     * 生成日志摘要
     */
    Map<String, Object> generateLogSummary(LocalDateTime startTime, LocalDateTime endTime);

    // 数据清理操作

    /**
     * 清理过期日志
     */
    int cleanupExpiredLogs();

    /**
     * 清理归档日志
     */
    int cleanupArchivedLogs(LocalDateTime archiveTime);

    /**
     * 压缩历史日志
     */
    int compressHistoryLogs(LocalDateTime compressTime);

    /**
     * 优化日志存储
     */
    int optimizeLogStorage(String optimizationType);

    /**
     * 自动清理日志
     */
    Map<String, Object> autoCleanupLogs(Map<String, Object> cleanupRules);

    // 系统维护操作

    /**
     * 获取日志概览信息
     */
    Map<String, Object> getLogOverview();

    /**
     * 生成日志报告
     */
    List<Map<String, Object>> generateLogReport(String reportType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分析日志质量
     */
    Map<String, Object> analyzeLogQuality(String analysisType, Map<String, Object> parameters);

    /**
     * 执行日志维护任务
     */
    Map<String, Object> executeLogMaintenanceTasks(String taskType, Map<String, Object> parameters);

    /**
     * 检查日志完整性
     */
    Map<String, Object> checkLogIntegrity();

    // 日志搜索操作

    /**
     * 全文搜索日志
     */
    List<IntgSyncLog> searchLogs(String keyword, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 高级搜索日志
     */
    List<IntgSyncLog> advancedSearchLogs(Map<String, Object> searchCriteria);

    /**
     * 搜索相似日志
     */
    List<IntgSyncLog> searchSimilarLogs(String logId, Double similarityThreshold);

    /**
     * 搜索日志模式
     */
    List<Map<String, Object>> searchLogPatterns(Map<String, Object> patternCriteria);

    // 日志通知操作

    /**
     * 发送日志通知
     */
    boolean sendLogNotification(String logId, String notificationType, List<String> recipients);

    /**
     * 批量发送通知
     */
    boolean batchSendNotifications(List<String> logIds, String notificationType, List<String> recipients);

    /**
     * 设置通知规则
     */
    boolean setNotificationRules(Map<String, Object> notificationRules);

    /**
     * 获取通知历史
     */
    List<Map<String, Object>> getNotificationHistory(LocalDateTime startTime, LocalDateTime endTime);
}
