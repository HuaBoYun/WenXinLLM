package com.management.accountant.service.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ss.SsJobSchedule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 作业调度服务接口
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface SsJobScheduleService extends IService<SsJobSchedule> {

    /**
     * 分页查询作业调度列表
     */
    IPage<SsJobSchedule> getJobSchedulePage(Page<SsJobSchedule> page,
                                           String jobName,
                                           String jobType,
                                           String jobStatus,
                                           String scheduleStatus,
                                           String executionStatus,
                                           Integer priority,
                                           Long schedulerId,
                                           Long executorUserId,
                                           LocalDateTime startTime,
                                           LocalDateTime endTime,
                                           Long tenantId);

    /**
     * 根据作业编码查询
     */
    SsJobSchedule getByJobCode(String jobCode, Long tenantId);

    /**
     * 根据作业类型查询列表
     */
    List<SsJobSchedule> getByJobType(String jobType, Long tenantId);

    /**
     * 根据作业状态查询列表
     */
    List<SsJobSchedule> getByJobStatus(String jobStatus, Long tenantId);

    /**
     * 根据调度状态查询列表
     */
    List<SsJobSchedule> getByScheduleStatus(String scheduleStatus, Long tenantId);

    /**
     * 根据执行状态查询列表
     */
    List<SsJobSchedule> getByExecutionStatus(String executionStatus, Long tenantId);

    /**
     * 创建作业调度
     */
    boolean createJobSchedule(SsJobSchedule jobSchedule);

    /**
     * 更新作业调度
     */
    boolean updateJobSchedule(SsJobSchedule jobSchedule);

    /**
     * 删除作业调度
     */
    boolean deleteJobSchedule(Long jobId, Long tenantId);

    /**
     * 批量删除作业调度
     */
    boolean batchDeleteJobSchedule(List<Long> jobIds, Long tenantId);

    /**
     * 启动作业调度
     */
    boolean startJobSchedule(Long jobId, Long tenantId);

    /**
     * 停止作业调度
     */
    boolean stopJobSchedule(Long jobId, Long tenantId);

    /**
     * 暂停作业调度
     */
    boolean pauseJobSchedule(Long jobId, Long tenantId);

    /**
     * 恢复作业调度
     */
    boolean resumeJobSchedule(Long jobId, Long tenantId);

    /**
     * 立即执行作业
     */
    boolean executeJobImmediately(Long jobId, Long tenantId);

    /**
     * 重新调度作业
     */
    boolean rescheduleJob(Long jobId, String scheduleExpression, Long tenantId);

    /**
     * 批量启动作业调度
     */
    boolean batchStartJobSchedule(List<Long> jobIds, Long tenantId);

    /**
     * 批量停止作业调度
     */
    boolean batchStopJobSchedule(List<Long> jobIds, Long tenantId);

    /**
     * 批量暂停作业调度
     */
    boolean batchPauseJobSchedule(List<Long> jobIds, Long tenantId);

    /**
     * 批量恢复作业调度
     */
    boolean batchResumeJobSchedule(List<Long> jobIds, Long tenantId);

    /**
     * 批量分配调度器
     */
    boolean batchAssignScheduler(List<Long> jobIds, Long schedulerId, String schedulerName, Long tenantId);

    /**
     * 批量设置优先级
     */
    boolean batchSetPriority(List<Long> jobIds, Integer priority, BigDecimal priorityWeight, Long tenantId);

    /**
     * 任务分配管理
     */
    boolean assignJobToScheduler(Long jobId, Long schedulerId, String loadBalanceStrategy, Long tenantId);

    /**
     * 负载均衡处理
     */
    Map<String, Object> performLoadBalancing(String strategy, List<Long> jobIds, Long tenantId);

    /**
     * 优先级管理
     */
    boolean managePriority(Long jobId, Integer priority, BigDecimal weight, String reason, Long tenantId);

    /**
     * SLA监控
     */
    Map<String, Object> monitorSLA(Long jobId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 调度算法优化
     */
    Map<String, Object> optimizeSchedulingAlgorithm(String algorithmType, Map<String, Object> parameters, Long tenantId);

    /**
     * 性能优化管理
     */
    Map<String, Object> optimizePerformance(Long jobId, String optimizationType, Map<String, Object> config, Long tenantId);

    /**
     * 调度报告分析
     */
    Map<String, Object> generateSchedulingReport(LocalDateTime startTime, LocalDateTime endTime, String reportType, Long tenantId);

    /**
     * 容量规划管理
     */
    Map<String, Object> planCapacity(String planningType, LocalDateTime forecastPeriod, Map<String, Object> parameters, Long tenantId);

    /**
     * 调度策略配置
     */
    boolean configureSchedulingStrategy(Long jobId, String strategyType, Map<String, Object> config, Long tenantId);

    /**
     * 查询待调度的作业列表
     */
    List<SsJobSchedule> getPendingJobs(Long tenantId);

    /**
     * 查询正在运行的作业列表
     */
    List<SsJobSchedule> getRunningJobs(Long tenantId);

    /**
     * 查询已完成的作业列表
     */
    List<SsJobSchedule> getCompletedJobs(Long tenantId);

    /**
     * 查询失败的作业列表
     */
    List<SsJobSchedule> getFailedJobs(Long tenantId);

    /**
     * 查询超时的作业列表
     */
    List<SsJobSchedule> getTimeoutJobs(Long tenantId);

    /**
     * 查询高优先级作业列表
     */
    List<SsJobSchedule> getHighPriorityJobs(Integer minPriority, Long tenantId);

    /**
     * 查询需要重试的作业列表
     */
    List<SsJobSchedule> getRetryJobs(Long tenantId);

    /**
     * 查询即将执行的作业列表
     */
    List<SsJobSchedule> getUpcomingJobs(LocalDateTime timeWindow, Long tenantId);

    /**
     * 更新执行统计信息
     */
    boolean updateExecutionStatistics(Long jobId, Integer executionCount, Integer successCount, 
                                     Integer failureCount, Integer avgDuration, BigDecimal successRate,
                                     LocalDateTime lastExecutionTime, LocalDateTime lastSuccessTime, 
                                     LocalDateTime lastFailureTime, Long tenantId);

    /**
     * 更新下次执行时间
     */
    boolean updateNextExecutionTime(Long jobId, LocalDateTime nextExecutionTime, Long tenantId);

    /**
     * 统计作业调度数据
     */
    Map<String, Object> getJobScheduleStatistics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计作业状态分布
     */
    List<Map<String, Object>> getJobStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计作业类型分布
     */
    List<Map<String, Object>> getJobTypeDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计调度状态分布
     */
    List<Map<String, Object>> getScheduleStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计执行状态分布
     */
    List<Map<String, Object>> getExecutionStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计优先级分布
     */
    List<Map<String, Object>> getPriorityDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计作业调度趋势
     */
    List<Map<String, Object>> getJobScheduleTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计调度器工作负载
     */
    List<Map<String, Object>> getSchedulerWorkload(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计执行用户工作负载
     */
    List<Map<String, Object>> getExecutorWorkload(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计作业性能指标
     */
    Map<String, Object> getJobPerformanceMetrics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计SLA达成情况
     */
    Map<String, Object> getSlaMetrics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询作业调度排行榜
     */
    List<Map<String, Object>> getJobScheduleRanking(String rankingType, LocalDateTime startTime, LocalDateTime endTime, Integer limit, Long tenantId);

    /**
     * 检查作业编码是否存在
     */
    boolean checkJobCodeExists(String jobCode, Long jobId, Long tenantId);

    /**
     * 检查作业依赖关系
     */
    boolean checkJobDependency(Long jobId, Long dependencyJobId, Long tenantId);

    /**
     * 计算平均执行时间
     */
    BigDecimal calculateAverageExecutionTime(String jobType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 计算成功率
     */
    BigDecimal calculateSuccessRate(String jobType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询资源使用情况
     */
    Map<String, Object> getResourceUsage(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询容量规划数据
     */
    Map<String, Object> getCapacityPlanningData(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 导入作业调度数据
     */
    Map<String, Object> importJobScheduleData(List<Map<String, Object>> dataList, Long tenantId);

    /**
     * 导出作业调度数据
     */
    List<Map<String, Object>> exportJobScheduleData(Map<String, Object> queryParams, Long tenantId);

    /**
     * 生成作业调度报告
     */
    Map<String, Object> generateJobScheduleReport(String reportType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 发送通知
     */
    boolean sendNotification(Long jobId, String notificationType, String message, Long tenantId);

    /**
     * 批量发送通知
     */
    boolean batchSendNotification(List<Long> jobIds, String notificationType, String message, Long tenantId);
}
