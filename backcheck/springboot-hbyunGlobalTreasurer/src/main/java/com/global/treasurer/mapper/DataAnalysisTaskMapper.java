package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.DataAnalysisTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据分析任务Mapper接口
 * 
 * @author Global Treasurer System
 * @since 2025-09-22
 */
@Mapper
public interface DataAnalysisTaskMapper extends BaseMapper<DataAnalysisTask> {

    /**
     * 根据条件查询数据分析任务列表(支持动态条件)
     *
     * @param taskNo 任务编号(模糊查询)
     * @param taskName 任务名称(模糊查询)
     * @param taskType 任务类型(精确查询)
     * @param taskStatus 任务状态(精确查询)
     * @param orgId 组织ID(精确查询)
     * @return 数据分析任务列表
     */
    List<DataAnalysisTask> selectByCondition(@Param("taskNo") String taskNo,
                                             @Param("taskName") String taskName,
                                             @Param("taskType") String taskType,
                                             @Param("taskStatus") String taskStatus,
                                             @Param("orgId") Long orgId);

    // ==================== 基础查询方法 ====================

    /**
     * 根据任务编号查询任务
     */
    DataAnalysisTask selectByTaskNo(@Param("taskNo") String taskNo, @Param("orgId") Long orgId);

    /**
     * 根据任务类型查询任务列表
     */
    List<DataAnalysisTask> selectByTaskType(@Param("taskType") String taskType, @Param("orgId") Long orgId);

    /**
     * 根据分析类型查询任务列表
     */
    List<DataAnalysisTask> selectByAnalysisType(@Param("analysisType") String analysisType, @Param("orgId") Long orgId);

    /**
     * 根据任务状态查询任务列表
     */
    List<DataAnalysisTask> selectByTaskStatus(@Param("taskStatus") String taskStatus, @Param("orgId") Long orgId);

    /**
     * 根据调度类型查询任务列表
     */
    List<DataAnalysisTask> selectByScheduleType(@Param("scheduleType") String scheduleType, @Param("orgId") Long orgId);

    /**
     * 根据模型ID查询任务列表
     */
    List<DataAnalysisTask> selectByModelId(@Param("modelId") Long modelId, @Param("orgId") Long orgId);

    /**
     * 查询待执行任务
     */
    List<DataAnalysisTask> selectPendingTasks(@Param("orgId") Long orgId);

    /**
     * 查询运行中任务
     */
    List<DataAnalysisTask> selectRunningTasks(@Param("orgId") Long orgId);

    /**
     * 查询已完成任务
     */
    List<DataAnalysisTask> selectCompletedTasks(@Param("orgId") Long orgId);

    /**
     * 查询失败任务
     */
    List<DataAnalysisTask> selectFailedTasks(@Param("orgId") Long orgId);

    /**
     * 分页查询任务
     */
    IPage<DataAnalysisTask> selectTaskPage(Page<DataAnalysisTask> page, @Param("params") Map<String, Object> params);

    // ==================== 任务调度方法 ====================

    /**
     * 查询定时任务
     */
    List<DataAnalysisTask> selectScheduledTasks(@Param("orgId") Long orgId);

    /**
     * 查询需要执行的定时任务
     */
    List<DataAnalysisTask> selectTasksToExecute(@Param("currentTime") LocalDateTime currentTime, @Param("orgId") Long orgId);

    /**
     * 查询超时任务
     */
    List<DataAnalysisTask> selectTimeoutTasks(@Param("timeoutMinutes") Integer timeoutMinutes, @Param("orgId") Long orgId);

    /**
     * 查询长时间运行任务
     */
    List<DataAnalysisTask> selectLongRunningTasks(@Param("thresholdMinutes") Integer thresholdMinutes, @Param("orgId") Long orgId);

    // ==================== 任务状态管理方法 ====================

    /**
     * 启动任务
     */
    int startTask(@Param("taskId") Long taskId, 
                  @Param("startTime") LocalDateTime startTime, 
                  @Param("executeUser") Long executeUser);

    /**
     * 完成任务
     */
    int completeTask(@Param("taskId") Long taskId, 
                     @Param("endTime") LocalDateTime endTime,
                     @Param("executionTime") Integer executionTime,
                     @Param("analysisResult") String analysisResult,
                     @Param("resultPath") String resultPath);

    /**
     * 任务失败
     */
    int failTask(@Param("taskId") Long taskId, 
                 @Param("endTime") LocalDateTime endTime,
                 @Param("errorMessage") String errorMessage);

    /**
     * 取消任务
     */
    int cancelTask(@Param("taskId") Long taskId, @Param("updateUser") Long updateUser);

    /**
     * 重置任务状态
     */
    int resetTaskStatus(@Param("taskId") Long taskId, @Param("updateUser") Long updateUser);

    /**
     * 批量更新任务状态
     */
    int batchUpdateTaskStatus(@Param("taskIds") List<Long> taskIds,
                             @Param("taskStatus") String taskStatus,
                             @Param("updateUser") Long updateUser);

    /**
     * 更新任务执行信息
     */
    int updateTaskExecution(@Param("taskId") Long taskId,
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime") LocalDateTime endTime,
                           @Param("executionTime") Integer executionTime,
                           @Param("taskStatus") String taskStatus,
                           @Param("executeUser") Long executeUser);

    /**
     * 更新任务状态
     */
    int updateTaskStatus(@Param("taskId") Long taskId,
                        @Param("taskStatus") String taskStatus,
                        @Param("updateUser") Long updateUser);

    /**
     * 更新任务结果
     */
    int updateTaskResult(@Param("taskId") Long taskId,
                        @Param("analysisResult") String analysisResult,
                        @Param("resultPath") String resultPath,
                        @Param("taskStatus") String taskStatus,
                        @Param("updateUser") Long updateUser);

    /**
     * 更新任务错误信息
     */
    int updateTaskError(@Param("taskId") Long taskId,
                       @Param("errorMessage") String errorMessage,
                       @Param("taskStatus") String taskStatus,
                       @Param("updateUser") Long updateUser);

    // ==================== 统计分析方法 ====================

    /**
     * 统计任务总数
     */
    Long countTasks(@Param("orgId") Long orgId);

    /**
     * 按任务类型统计
     */
    List<Map<String, Object>> countByTaskType(@Param("orgId") Long orgId);

    /**
     * 按任务状态统计
     */
    List<Map<String, Object>> countByTaskStatus(@Param("orgId") Long orgId);

    /**
     * 按分析类型统计
     */
    List<Map<String, Object>> countByAnalysisType(@Param("orgId") Long orgId);

    /**
     * 按调度类型统计
     */
    List<Map<String, Object>> countByScheduleType(@Param("orgId") Long orgId);

    /**
     * 统计任务执行时间分布
     */
    List<Map<String, Object>> countByExecutionTimeRange(@Param("orgId") Long orgId);

    /**
     * 计算平均执行时间
     */
    Double calculateAverageExecutionTime(@Param("orgId") Long orgId);

    /**
     * 计算任务成功率
     */
    Double calculateTaskSuccessRate(@Param("orgId") Long orgId);

    /**
     * 查询任务统计信息
     */
    Map<String, Object> selectTaskStatistics(@Param("orgId") Long orgId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    // ==================== 任务性能分析方法 ====================

    /**
     * 查询执行时间最长的任务
     */
    List<DataAnalysisTask> selectLongestExecutionTasks(@Param("orgId") Long orgId, @Param("limit") Integer limit);

    /**
     * 查询执行时间最短的任务
     */
    List<DataAnalysisTask> selectShortestExecutionTasks(@Param("orgId") Long orgId, @Param("limit") Integer limit);

    /**
     * 查询高频执行任务
     */
    List<Map<String, Object>> selectHighFrequencyTasks(@Param("orgId") Long orgId, @Param("limit") Integer limit);

    /**
     * 分析任务执行趋势
     */
    List<Map<String, Object>> analyzeTaskExecutionTrend(@Param("orgId") Long orgId, 
                                                        @Param("days") Integer days);

    // ==================== 任务监控方法 ====================

    /**
     * 查询任务执行统计
     */
    Map<String, Object> selectTaskExecutionStatistics(@Param("orgId") Long orgId);

    /**
     * 查询任务健康状态
     */
    List<Map<String, Object>> selectTaskHealthStatus(@Param("orgId") Long orgId);

    /**
     * 生成任务监控报告
     */
    Map<String, Object> generateTaskMonitoringReport(@Param("orgId") Long orgId);

    /**
     * 查询任务异常情况
     */
    List<Map<String, Object>> selectTaskAnomalies(@Param("orgId") Long orgId);

    // ==================== 任务依赖管理方法 ====================

    /**
     * 查询任务依赖关系
     */
    List<Map<String, Object>> selectTaskDependencies(@Param("taskId") Long taskId);

    /**
     * 查询依赖此任务的其他任务
     */
    List<DataAnalysisTask> selectDependentTasks(@Param("taskId") Long taskId);

    /**
     * 检查任务依赖是否满足
     */
    Boolean checkTaskDependenciesMet(@Param("taskId") Long taskId);

    // ==================== 任务资源管理方法 ====================

    /**
     * 查询资源使用情况
     */
    List<Map<String, Object>> selectResourceUsage(@Param("orgId") Long orgId);

    /**
     * 查询高资源消耗任务
     */
    List<DataAnalysisTask> selectHighResourceTasks(@Param("orgId") Long orgId, @Param("limit") Integer limit);

    /**
     * 优化任务资源分配
     */
    List<Map<String, Object>> optimizeTaskResourceAllocation(@Param("orgId") Long orgId);

    // ==================== 任务结果管理方法 ====================

    /**
     * 查询有结果的任务
     */
    List<DataAnalysisTask> selectTasksWithResults(@Param("orgId") Long orgId);

    /**
     * 查询无结果的任务
     */
    List<DataAnalysisTask> selectTasksWithoutResults(@Param("orgId") Long orgId);

    /**
     * 清理过期任务结果
     */
    int cleanupExpiredTaskResults(@Param("orgId") Long orgId, @Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 备份任务结果
     */
    int backupTaskResults(@Param("taskIds") List<Long> taskIds, @Param("backupPath") String backupPath);

    // ==================== 任务搜索方法 ====================

    /**
     * 全文搜索任务
     */
    List<DataAnalysisTask> searchTasks(@Param("keyword") String keyword, @Param("orgId") Long orgId);

    /**
     * 按数据源搜索任务
     */
    List<DataAnalysisTask> selectByDataSource(@Param("dataSource") String dataSource, @Param("orgId") Long orgId);

    /**
     * 按执行用户搜索任务
     */
    List<DataAnalysisTask> selectByExecuteUser(@Param("executeUser") Long executeUser, @Param("orgId") Long orgId);

    // ==================== 任务推荐方法 ====================

    /**
     * 推荐相似任务
     */
    List<DataAnalysisTask> recommendSimilarTasks(@Param("taskId") Long taskId, @Param("limit") Integer limit);

    /**
     * 推荐优化任务
     */
    List<Map<String, Object>> recommendTaskOptimizations(@Param("orgId") Long orgId);

    // ==================== 任务模板方法 ====================

    /**
     * 查询任务模板
     */
    List<Map<String, Object>> selectTaskTemplates(@Param("taskType") String taskType, @Param("orgId") Long orgId);

    /**
     * 从模板创建任务
     */
    int createTaskFromTemplate(@Param("templateId") Long templateId, 
                              @Param("taskName") String taskName,
                              @Param("createUser") Long createUser);

    // ==================== 任务审计方法 ====================

    /**
     * 记录任务操作日志
     */
    int insertTaskOperationLog(@Param("taskId") Long taskId, 
                              @Param("operation") String operation,
                              @Param("operationDetails") String operationDetails,
                              @Param("operateUser") Long operateUser);

    /**
     * 查询任务操作历史
     */
    List<Map<String, Object>> selectTaskOperationHistory(@Param("taskId") Long taskId);

    /**
     * 查询任务执行历史
     */
    List<Map<String, Object>> selectTaskExecutionHistory(@Param("taskId") Long taskId);

    // ==================== 辅助方法 ====================

    /**
     * 检查任务编号唯一性
     */
    Boolean checkTaskNoUniqueness(@Param("taskNo") String taskNo, @Param("orgId") Long orgId);

    /**
     * 生成任务编号
     */
    String generateTaskNo(@Param("taskType") String taskType, @Param("orgId") Long orgId);

    /**
     * 检查任务是否可以删除
     */
    Boolean checkTaskCanDelete(@Param("taskId") Long taskId);

    /**
     * 验证Cron表达式
     */
    Boolean validateCronExpression(@Param("cronExpression") String cronExpression);
}
