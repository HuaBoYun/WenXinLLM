package com.management.accountant.mapper.intg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.intg.IntgSyncLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 同步日志 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface IntgSyncLogMapper extends BaseMapper<IntgSyncLog> {

    // 基础查询方法

    /**
     * 根据日志编码查询
     */
    IntgSyncLog getByLogCode(@Param("logCode") String logCode, @Param("tenantId") String tenantId);

    /**
     * 根据系统配置ID查询日志列表
     */
    List<IntgSyncLog> getByConfigId(@Param("configId") String configId, @Param("tenantId") String tenantId);

    /**
     * 根据映射配置ID查询日志列表
     */
    List<IntgSyncLog> getByMappingId(@Param("mappingId") String mappingId, @Param("tenantId") String tenantId);

    /**
     * 根据任务ID查询日志列表
     */
    List<IntgSyncLog> getByTaskId(@Param("taskId") String taskId, @Param("tenantId") String tenantId);

    /**
     * 根据批次号查询日志列表
     */
    List<IntgSyncLog> getByBatchNumber(@Param("batchNumber") String batchNumber, @Param("tenantId") String tenantId);

    /**
     * 根据同步状态查询日志列表
     */
    List<IntgSyncLog> getBySyncStatus(@Param("syncStatus") String syncStatus, @Param("tenantId") String tenantId);

    /**
     * 根据执行结果查询日志列表
     */
    List<IntgSyncLog> getByExecutionResult(@Param("executionResult") String executionResult, @Param("tenantId") String tenantId);

    /**
     * 查询最近的日志列表
     */
    List<IntgSyncLog> getRecentLogs(@Param("limit") Integer limit, @Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询日志列表
     */
    IPage<IntgSyncLog> getLogPage(Page<IntgSyncLog> page, @Param("params") Map<String, Object> params);

    /**
     * 分页查询系统日志
     */
    IPage<IntgSyncLog> getSystemLogPage(Page<IntgSyncLog> page,
                                       @Param("configId") String configId,
                                       @Param("syncStatus") String syncStatus,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("tenantId") String tenantId);

    /**
     * 分页查询错误日志
     */
    IPage<IntgSyncLog> getErrorLogPage(Page<IntgSyncLog> page,
                                      @Param("errorCode") String errorCode,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("tenantId") String tenantId);

    // 统计查询方法

    /**
     * 统计日志总数
     */
    Long countLogs(@Param("tenantId") String tenantId);

    /**
     * 按同步类型统计日志数量
     */
    List<Map<String, Object>> countBySyncType(@Param("tenantId") String tenantId);

    /**
     * 按同步方向统计日志数量
     */
    List<Map<String, Object>> countBySyncDirection(@Param("tenantId") String tenantId);

    /**
     * 按同步状态统计日志数量
     */
    List<Map<String, Object>> countBySyncStatus(@Param("tenantId") String tenantId);

    /**
     * 按执行结果统计日志数量
     */
    List<Map<String, Object>> countByExecutionResult(@Param("tenantId") String tenantId);

    /**
     * 按日期统计日志数量
     */
    List<Map<String, Object>> countByDate(@Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime,
                                         @Param("granularity") String granularity,
                                         @Param("tenantId") String tenantId);

    /**
     * 按系统配置统计日志数量
     */
    List<Map<String, Object>> countBySystemConfig(@Param("tenantId") String tenantId);

    // 日志分析方法

    /**
     * 获取同步趋势分析
     */
    List<Map<String, Object>> getSyncTrendAnalysis(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("granularity") String granularity,
                                                  @Param("tenantId") String tenantId);

    /**
     * 获取性能统计分析
     */
    List<Map<String, Object>> getPerformanceStats(@Param("configIds") List<String> configIds,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("tenantId") String tenantId);

    /**
     * 获取错误统计分析
     */
    List<Map<String, Object>> getErrorStats(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime,
                                           @Param("tenantId") String tenantId);

    /**
     * 获取数据量统计分析
     */
    List<Map<String, Object>> getDataVolumeStats(@Param("configIds") List<String> configIds,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("tenantId") String tenantId);

    /**
     * 获取系统使用统计
     */
    List<Map<String, Object>> getSystemUsageStats(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("tenantId") String tenantId);

    // 日志管理方法

    /**
     * 批量更新日志状态
     */
    int batchUpdateLogStatus(@Param("logIds") List<String> logIds,
                            @Param("syncStatus") String syncStatus,
                            @Param("updatedBy") String updatedBy,
                            @Param("tenantId") String tenantId);

    /**
     * 批量更新归档状态
     */
    int batchUpdateArchiveStatus(@Param("logIds") List<String> logIds,
                                @Param("archiveStatus") String archiveStatus,
                                @Param("archiveTime") LocalDateTime archiveTime,
                                @Param("updatedBy") String updatedBy,
                                @Param("tenantId") String tenantId);

    /**
     * 更新日志执行结果
     */
    int updateExecutionResult(@Param("logId") String logId,
                             @Param("executionResult") String executionResult,
                             @Param("endTime") LocalDateTime endTime,
                             @Param("executionDuration") Long executionDuration,
                             @Param("processedRecords") Long processedRecords,
                             @Param("successRecords") Long successRecords,
                             @Param("failedRecords") Long failedRecords,
                             @Param("tenantId") String tenantId);

    /**
     * 更新日志错误信息
     */
    int updateErrorInfo(@Param("logId") String logId,
                       @Param("errorCode") String errorCode,
                       @Param("errorMessage") String errorMessage,
                       @Param("errorStack") String errorStack,
                       @Param("tenantId") String tenantId);

    // 日志查找方法

    /**
     * 查找失败的日志
     */
    List<IntgSyncLog> findFailedLogs(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("tenantId") String tenantId);

    /**
     * 查找需要重试的日志
     */
    List<IntgSyncLog> findLogsForRetry(@Param("maxRetryCount") Integer maxRetryCount,
                                      @Param("tenantId") String tenantId);

    /**
     * 查找长时间运行的日志
     */
    List<IntgSyncLog> findLongRunningLogs(@Param("thresholdMinutes") Integer thresholdMinutes,
                                         @Param("tenantId") String tenantId);

    /**
     * 查找异常日志
     */
    List<IntgSyncLog> findAnomalousLogs(@Param("thresholdDuration") Long thresholdDuration,
                                       @Param("thresholdFailureRate") Double thresholdFailureRate,
                                       @Param("tenantId") String tenantId);

    /**
     * 查找相关日志
     */
    List<IntgSyncLog> findRelatedLogs(@Param("logId") String logId,
                                     @Param("relationshipType") String relationshipType,
                                     @Param("tenantId") String tenantId);

    // 日志监控方法

    /**
     * 获取实时监控数据
     */
    Map<String, Object> getRealTimeMonitoringData(@Param("tenantId") String tenantId);

    /**
     * 获取系统健康状态
     */
    Map<String, Object> getSystemHealthStatus(@Param("tenantId") String tenantId);

    /**
     * 获取告警信息
     */
    List<Map<String, Object>> getAlertInfo(@Param("alertLevel") String alertLevel,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,
                                          @Param("tenantId") String tenantId);

    /**
     * 检测异常模式
     */
    List<Map<String, Object>> detectAnomalousPatterns(@Param("detectionType") String detectionType,
                                                      @Param("parameters") Map<String, Object> parameters,
                                                      @Param("tenantId") String tenantId);

    // 数据清理方法

    /**
     * 清理过期日志
     */
    int cleanupExpiredLogs(@Param("expiryTime") LocalDateTime expiryTime,
                          @Param("tenantId") String tenantId);

    /**
     * 清理归档日志
     */
    int cleanupArchivedLogs(@Param("archiveTime") LocalDateTime archiveTime,
                           @Param("tenantId") String tenantId);

    /**
     * 压缩历史日志
     */
    int compressHistoryLogs(@Param("compressTime") LocalDateTime compressTime,
                           @Param("tenantId") String tenantId);

    /**
     * 优化日志存储
     */
    int optimizeLogStorage(@Param("optimizationType") String optimizationType,
                          @Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 获取日志概览信息
     */
    Map<String, Object> getLogOverview(@Param("tenantId") String tenantId);

    /**
     * 生成日志报告
     */
    List<Map<String, Object>> generateLogReport(@Param("reportType") String reportType,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime,
                                               @Param("tenantId") String tenantId);

    /**
     * 分析日志质量
     */
    Map<String, Object> analyzeLogQuality(@Param("analysisType") String analysisType,
                                         @Param("parameters") Map<String, Object> parameters,
                                         @Param("tenantId") String tenantId);

    /**
     * 执行日志维护任务
     */
    Map<String, Object> executeLogMaintenanceTasks(@Param("taskType") String taskType,
                                                   @Param("parameters") Map<String, Object> parameters,
                                                   @Param("tenantId") String tenantId);
}
