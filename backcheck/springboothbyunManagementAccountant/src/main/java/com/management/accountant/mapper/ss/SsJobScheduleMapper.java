package com.management.accountant.mapper.ss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsJobSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 作业调度Mapper接口
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface SsJobScheduleMapper extends BaseMapper<SsJobSchedule> {

    /**
     * 分页查询作业调度列表
     */
    IPage<SsJobSchedule> selectJobSchedulePage(Page<SsJobSchedule> page,
                                               @Param("jobName") String jobName,
                                               @Param("jobType") String jobType,
                                               @Param("jobStatus") String jobStatus,
                                               @Param("scheduleStatus") String scheduleStatus,
                                               @Param("executionStatus") String executionStatus,
                                               @Param("priority") Integer priority,
                                               @Param("schedulerId") Long schedulerId,
                                               @Param("executorUserId") Long executorUserId,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime,
                                               @Param("tenantId") Long tenantId);

    /**
     * 根据作业编码查询
     */
    SsJobSchedule selectByJobCode(@Param("jobCode") String jobCode, @Param("tenantId") Long tenantId);

    /**
     * 根据作业类型查询列表
     */
    List<SsJobSchedule> selectByJobType(@Param("jobType") String jobType, @Param("tenantId") Long tenantId);

    /**
     * 根据作业状态查询列表
     */
    List<SsJobSchedule> selectByJobStatus(@Param("jobStatus") String jobStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据调度状态查询列表
     */
    List<SsJobSchedule> selectByScheduleStatus(@Param("scheduleStatus") String scheduleStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据执行状态查询列表
     */
    List<SsJobSchedule> selectByExecutionStatus(@Param("executionStatus") String executionStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据优先级查询列表
     */
    List<SsJobSchedule> selectByPriority(@Param("priority") Integer priority, @Param("tenantId") Long tenantId);

    /**
     * 根据调度器ID查询列表
     */
    List<SsJobSchedule> selectBySchedulerId(@Param("schedulerId") Long schedulerId, @Param("tenantId") Long tenantId);

    /**
     * 根据执行用户ID查询列表
     */
    List<SsJobSchedule> selectByExecutorUserId(@Param("executorUserId") Long executorUserId, @Param("tenantId") Long tenantId);

    /**
     * 根据依赖作业查询列表
     */
    List<SsJobSchedule> selectByDependencyJobs(@Param("dependencyJobId") Long dependencyJobId, @Param("tenantId") Long tenantId);

    /**
     * 查询待调度的作业列表
     */
    List<SsJobSchedule> selectPendingJobs(@Param("tenantId") Long tenantId);

    /**
     * 查询正在运行的作业列表
     */
    List<SsJobSchedule> selectRunningJobs(@Param("tenantId") Long tenantId);

    /**
     * 查询已完成的作业列表
     */
    List<SsJobSchedule> selectCompletedJobs(@Param("tenantId") Long tenantId);

    /**
     * 查询失败的作业列表
     */
    List<SsJobSchedule> selectFailedJobs(@Param("tenantId") Long tenantId);

    /**
     * 查询超时的作业列表
     */
    List<SsJobSchedule> selectTimeoutJobs(@Param("currentTime") LocalDateTime currentTime, @Param("tenantId") Long tenantId);

    /**
     * 查询高优先级作业列表
     */
    List<SsJobSchedule> selectHighPriorityJobs(@Param("minPriority") Integer minPriority, @Param("tenantId") Long tenantId);

    /**
     * 查询需要重试的作业列表
     */
    List<SsJobSchedule> selectRetryJobs(@Param("tenantId") Long tenantId);

    /**
     * 查询即将执行的作业列表
     */
    List<SsJobSchedule> selectUpcomingJobs(@Param("timeWindow") LocalDateTime timeWindow, @Param("tenantId") Long tenantId);

    /**
     * 批量更新作业状态
     */
    int batchUpdateJobStatus(@Param("jobIds") List<Long> jobIds,
                            @Param("jobStatus") String jobStatus,
                            @Param("updatedBy") String updatedBy,
                            @Param("updatedTime") LocalDateTime updatedTime,
                            @Param("tenantId") Long tenantId);

    /**
     * 批量更新调度状态
     */
    int batchUpdateScheduleStatus(@Param("jobIds") List<Long> jobIds,
                                 @Param("scheduleStatus") String scheduleStatus,
                                 @Param("updatedBy") String updatedBy,
                                 @Param("updatedTime") LocalDateTime updatedTime,
                                 @Param("tenantId") Long tenantId);

    /**
     * 批量更新执行状态
     */
    int batchUpdateExecutionStatus(@Param("jobIds") List<Long> jobIds,
                                  @Param("executionStatus") String executionStatus,
                                  @Param("updatedBy") String updatedBy,
                                  @Param("updatedTime") LocalDateTime updatedTime,
                                  @Param("tenantId") Long tenantId);

    /**
     * 批量分配调度器
     */
    int batchAssignScheduler(@Param("jobIds") List<Long> jobIds,
                            @Param("schedulerId") Long schedulerId,
                            @Param("schedulerName") String schedulerName,
                            @Param("updatedBy") String updatedBy,
                            @Param("updatedTime") LocalDateTime updatedTime,
                            @Param("tenantId") Long tenantId);

    /**
     * 批量设置优先级
     */
    int batchSetPriority(@Param("jobIds") List<Long> jobIds,
                        @Param("priority") Integer priority,
                        @Param("priorityWeight") BigDecimal priorityWeight,
                        @Param("updatedBy") String updatedBy,
                        @Param("updatedTime") LocalDateTime updatedTime,
                        @Param("tenantId") Long tenantId);

    /**
     * 更新执行统计信息
     */
    int updateExecutionStatistics(@Param("jobId") Long jobId,
                                 @Param("executionCount") Integer executionCount,
                                 @Param("successCount") Integer successCount,
                                 @Param("failureCount") Integer failureCount,
                                 @Param("avgDuration") Integer avgDuration,
                                 @Param("successRate") BigDecimal successRate,
                                 @Param("lastExecutionTime") LocalDateTime lastExecutionTime,
                                 @Param("lastSuccessTime") LocalDateTime lastSuccessTime,
                                 @Param("lastFailureTime") LocalDateTime lastFailureTime,
                                 @Param("tenantId") Long tenantId);

    /**
     * 更新下次执行时间
     */
    int updateNextExecutionTime(@Param("jobId") Long jobId,
                               @Param("nextExecutionTime") LocalDateTime nextExecutionTime,
                               @Param("tenantId") Long tenantId);

    /**
     * 统计作业调度数据
     */
    Map<String, Object> selectJobScheduleStatistics(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("tenantId") Long tenantId);

    /**
     * 统计作业状态分布
     */
    List<Map<String, Object>> selectJobStatusDistribution(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime,
                                                          @Param("tenantId") Long tenantId);

    /**
     * 统计作业类型分布
     */
    List<Map<String, Object>> selectJobTypeDistribution(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime,
                                                        @Param("tenantId") Long tenantId);

    /**
     * 统计调度状态分布
     */
    List<Map<String, Object>> selectScheduleStatusDistribution(@Param("startTime") LocalDateTime startTime,
                                                               @Param("endTime") LocalDateTime endTime,
                                                               @Param("tenantId") Long tenantId);

    /**
     * 统计执行状态分布
     */
    List<Map<String, Object>> selectExecutionStatusDistribution(@Param("startTime") LocalDateTime startTime,
                                                                @Param("endTime") LocalDateTime endTime,
                                                                @Param("tenantId") Long tenantId);

    /**
     * 统计优先级分布
     */
    List<Map<String, Object>> selectPriorityDistribution(@Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime,
                                                         @Param("tenantId") Long tenantId);

    /**
     * 统计作业调度趋势
     */
    List<Map<String, Object>> selectJobScheduleTrend(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime,
                                                     @Param("tenantId") Long tenantId);

    /**
     * 统计调度器工作负载
     */
    List<Map<String, Object>> selectSchedulerWorkload(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("tenantId") Long tenantId);

    /**
     * 统计执行用户工作负载
     */
    List<Map<String, Object>> selectExecutorWorkload(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime,
                                                     @Param("tenantId") Long tenantId);

    /**
     * 统计作业性能指标
     */
    Map<String, Object> selectJobPerformanceMetrics(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("tenantId") Long tenantId);

    /**
     * 统计SLA达成情况
     */
    Map<String, Object> selectSlaMetrics(@Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime,
                                        @Param("tenantId") Long tenantId);

    /**
     * 查询作业调度排行榜
     */
    List<Map<String, Object>> selectJobScheduleRanking(@Param("rankingType") String rankingType,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime,
                                                       @Param("limit") Integer limit,
                                                       @Param("tenantId") Long tenantId);

    /**
     * 检查作业编码是否存在
     */
    int checkJobCodeExists(@Param("jobCode") String jobCode, @Param("jobId") Long jobId, @Param("tenantId") Long tenantId);

    /**
     * 检查作业依赖关系
     */
    int checkJobDependency(@Param("jobId") Long jobId, @Param("dependencyJobId") Long dependencyJobId, @Param("tenantId") Long tenantId);

    /**
     * 计算平均执行时间
     */
    BigDecimal calculateAverageExecutionTime(@Param("jobType") String jobType,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime,
                                           @Param("tenantId") Long tenantId);

    /**
     * 计算成功率
     */
    BigDecimal calculateSuccessRate(@Param("jobType") String jobType,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  @Param("tenantId") Long tenantId);

    /**
     * 查询资源使用情况
     */
    Map<String, Object> selectResourceUsage(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime,
                                           @Param("tenantId") Long tenantId);

    /**
     * 查询容量规划数据
     */
    Map<String, Object> selectCapacityPlanningData(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("tenantId") Long tenantId);
}
