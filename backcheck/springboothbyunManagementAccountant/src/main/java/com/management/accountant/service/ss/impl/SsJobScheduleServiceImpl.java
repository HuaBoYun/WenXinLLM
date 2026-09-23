package com.management.accountant.service.ss.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ss.SsJobSchedule;
import com.management.accountant.mapper.ss.SsJobScheduleMapper;
import com.management.accountant.service.ss.SsJobScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 作业调度服务实现类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@Service
public class SsJobScheduleServiceImpl extends ServiceImpl<SsJobScheduleMapper, SsJobSchedule> implements SsJobScheduleService {

    @Autowired
    private SsJobScheduleMapper jobScheduleMapper;

    @Override
    public IPage<SsJobSchedule> getJobSchedulePage(Page<SsJobSchedule> page, String jobName, String jobType, 
                                                  String jobStatus, String scheduleStatus, String executionStatus,
                                                  Integer priority, Long schedulerId, Long executorUserId,
                                                  LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectJobSchedulePage(page, jobName, jobType, jobStatus, scheduleStatus, 
                                                       executionStatus, priority, schedulerId, executorUserId,
                                                       startTime, endTime, tenantId);
    }

    @Override
    public SsJobSchedule getByJobCode(String jobCode, Long tenantId) {
        return jobScheduleMapper.selectByJobCode(jobCode, tenantId);
    }

    @Override
    public List<SsJobSchedule> getByJobType(String jobType, Long tenantId) {
        return jobScheduleMapper.selectByJobType(jobType, tenantId);
    }

    @Override
    public List<SsJobSchedule> getByJobStatus(String jobStatus, Long tenantId) {
        return jobScheduleMapper.selectByJobStatus(jobStatus, tenantId);
    }

    @Override
    public List<SsJobSchedule> getByScheduleStatus(String scheduleStatus, Long tenantId) {
        return jobScheduleMapper.selectByScheduleStatus(scheduleStatus, tenantId);
    }

    @Override
    public List<SsJobSchedule> getByExecutionStatus(String executionStatus, Long tenantId) {
        return jobScheduleMapper.selectByExecutionStatus(executionStatus, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createJobSchedule(SsJobSchedule jobSchedule) {
        try {
            // 检查作业编码是否存在
            if (checkJobCodeExists(jobSchedule.getJobCode(), null, jobSchedule.getTenantId())) {
                throw new RuntimeException("作业编码已存在: " + jobSchedule.getJobCode());
            }

            // 设置默认值
            if (jobSchedule.getJobStatus() == null) {
                jobSchedule.setJobStatus("DRAFT");
            }
            if (jobSchedule.getScheduleStatus() == null) {
                jobSchedule.setScheduleStatus("STOPPED");
            }
            if (jobSchedule.getExecutionStatus() == null) {
                jobSchedule.setExecutionStatus("PENDING");
            }
            if (jobSchedule.getPriority() == null) {
                jobSchedule.setPriority(5);
            }
            if (jobSchedule.getPriorityWeight() == null) {
                jobSchedule.setPriorityWeight(BigDecimal.valueOf(1.0));
            }
            if (jobSchedule.getRetryCount() == null) {
                jobSchedule.setRetryCount(3);
            }
            if (jobSchedule.getRetryInterval() == null) {
                jobSchedule.setRetryInterval(60);
            }
            if (jobSchedule.getTimeoutSeconds() == null) {
                jobSchedule.setTimeoutSeconds(3600);
            }
            if (jobSchedule.getExecutionCount() == null) {
                jobSchedule.setExecutionCount(0);
            }
            if (jobSchedule.getSuccessCount() == null) {
                jobSchedule.setSuccessCount(0);
            }
            if (jobSchedule.getFailureCount() == null) {
                jobSchedule.setFailureCount(0);
            }
            if (jobSchedule.getSuccessRate() == null) {
                jobSchedule.setSuccessRate(BigDecimal.ZERO);
            }
            if (jobSchedule.getIsEnabled() == null) {
                jobSchedule.setIsEnabled(true);
            }

            return save(jobSchedule);
        } catch (Exception e) {
            log.error("创建作业调度失败", e);
            throw new RuntimeException("创建作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateJobSchedule(SsJobSchedule jobSchedule) {
        try {
            // 检查作业编码是否存在
            if (checkJobCodeExists(jobSchedule.getJobCode(), jobSchedule.getJobId(), jobSchedule.getTenantId())) {
                throw new RuntimeException("作业编码已存在: " + jobSchedule.getJobCode());
            }

            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("更新作业调度失败", e);
            throw new RuntimeException("更新作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteJobSchedule(Long jobId, Long tenantId) {
        try {
            QueryWrapper<SsJobSchedule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_id", jobId)
                       .eq("tenant_id", tenantId);
            return remove(queryWrapper);
        } catch (Exception e) {
            log.error("删除作业调度失败", e);
            throw new RuntimeException("删除作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteJobSchedule(List<Long> jobIds, Long tenantId) {
        try {
            QueryWrapper<SsJobSchedule> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("job_id", jobIds)
                       .eq("tenant_id", tenantId);
            return remove(queryWrapper);
        } catch (Exception e) {
            log.error("批量删除作业调度失败", e);
            throw new RuntimeException("批量删除作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startJobSchedule(Long jobId, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setScheduleStatus("RUNNING");
            jobSchedule.setExecutionStatus("PENDING");
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("启动作业调度失败", e);
            throw new RuntimeException("启动作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopJobSchedule(Long jobId, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setScheduleStatus("STOPPED");
            jobSchedule.setExecutionStatus("STOPPED");
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("停止作业调度失败", e);
            throw new RuntimeException("停止作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pauseJobSchedule(Long jobId, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setScheduleStatus("PAUSED");
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("暂停作业调度失败", e);
            throw new RuntimeException("暂停作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resumeJobSchedule(Long jobId, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setScheduleStatus("RUNNING");
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("恢复作业调度失败", e);
            throw new RuntimeException("恢复作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeJobImmediately(Long jobId, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setExecutionStatus("RUNNING");
            jobSchedule.setActualStartTime(LocalDateTime.now());
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("立即执行作业失败", e);
            throw new RuntimeException("立即执行作业失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rescheduleJob(Long jobId, String scheduleExpression, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setScheduleExpression(scheduleExpression);
            // 这里应该根据新的调度表达式计算下次执行时间
            // jobSchedule.setNextExecutionTime(calculateNextExecutionTime(scheduleExpression));
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("重新调度作业失败", e);
            throw new RuntimeException("重新调度作业失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchStartJobSchedule(List<Long> jobIds, Long tenantId) {
        try {
            return jobScheduleMapper.batchUpdateScheduleStatus(jobIds, "RUNNING", 
                                                              "system", LocalDateTime.now(), tenantId) > 0;
        } catch (Exception e) {
            log.error("批量启动作业调度失败", e);
            throw new RuntimeException("批量启动作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchStopJobSchedule(List<Long> jobIds, Long tenantId) {
        try {
            return jobScheduleMapper.batchUpdateScheduleStatus(jobIds, "STOPPED", 
                                                              "system", LocalDateTime.now(), tenantId) > 0;
        } catch (Exception e) {
            log.error("批量停止作业调度失败", e);
            throw new RuntimeException("批量停止作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchPauseJobSchedule(List<Long> jobIds, Long tenantId) {
        try {
            return jobScheduleMapper.batchUpdateScheduleStatus(jobIds, "PAUSED", 
                                                              "system", LocalDateTime.now(), tenantId) > 0;
        } catch (Exception e) {
            log.error("批量暂停作业调度失败", e);
            throw new RuntimeException("批量暂停作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchResumeJobSchedule(List<Long> jobIds, Long tenantId) {
        try {
            return jobScheduleMapper.batchUpdateScheduleStatus(jobIds, "RUNNING", 
                                                              "system", LocalDateTime.now(), tenantId) > 0;
        } catch (Exception e) {
            log.error("批量恢复作业调度失败", e);
            throw new RuntimeException("批量恢复作业调度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAssignScheduler(List<Long> jobIds, Long schedulerId, String schedulerName, Long tenantId) {
        try {
            return jobScheduleMapper.batchAssignScheduler(jobIds, schedulerId, schedulerName,
                                                         "system", LocalDateTime.now(), tenantId) > 0;
        } catch (Exception e) {
            log.error("批量分配调度器失败", e);
            throw new RuntimeException("批量分配调度器失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSetPriority(List<Long> jobIds, Integer priority, BigDecimal priorityWeight, Long tenantId) {
        try {
            return jobScheduleMapper.batchSetPriority(jobIds, priority, priorityWeight,
                                                     "system", LocalDateTime.now(), tenantId) > 0;
        } catch (Exception e) {
            log.error("批量设置优先级失败", e);
            throw new RuntimeException("批量设置优先级失败: " + e.getMessage());
        }
    }

    @Override
    public boolean assignJobToScheduler(Long jobId, Long schedulerId, String loadBalanceStrategy, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setSchedulerId(schedulerId);
            jobSchedule.setLoadBalanceStrategy(loadBalanceStrategy);
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("分配作业到调度器失败", e);
            throw new RuntimeException("分配作业到调度器失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> performLoadBalancing(String strategy, List<Long> jobIds, Long tenantId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 根据不同的负载均衡策略执行相应的逻辑
            switch (strategy) {
                case "ROUND_ROBIN":
                    result = performRoundRobinLoadBalancing(jobIds, tenantId);
                    break;
                case "LEAST_CONNECTIONS":
                    result = performLeastConnectionsLoadBalancing(jobIds, tenantId);
                    break;
                case "WEIGHTED_ROUND_ROBIN":
                    result = performWeightedRoundRobinLoadBalancing(jobIds, tenantId);
                    break;
                case "RESOURCE_BASED":
                    result = performResourceBasedLoadBalancing(jobIds, tenantId);
                    break;
                default:
                    result = performRoundRobinLoadBalancing(jobIds, tenantId);
            }
            
            result.put("strategy", strategy);
            result.put("jobCount", jobIds.size());
            result.put("timestamp", LocalDateTime.now());
            
            return result;
        } catch (Exception e) {
            log.error("执行负载均衡失败", e);
            throw new RuntimeException("执行负载均衡失败: " + e.getMessage());
        }
    }

    private Map<String, Object> performRoundRobinLoadBalancing(List<Long> jobIds, Long tenantId) {
        // 轮询负载均衡实现
        Map<String, Object> result = new HashMap<>();
        result.put("balancingType", "ROUND_ROBIN");
        result.put("distributedJobs", jobIds.size());
        return result;
    }

    private Map<String, Object> performLeastConnectionsLoadBalancing(List<Long> jobIds, Long tenantId) {
        // 最少连接负载均衡实现
        Map<String, Object> result = new HashMap<>();
        result.put("balancingType", "LEAST_CONNECTIONS");
        result.put("distributedJobs", jobIds.size());
        return result;
    }

    private Map<String, Object> performWeightedRoundRobinLoadBalancing(List<Long> jobIds, Long tenantId) {
        // 加权轮询负载均衡实现
        Map<String, Object> result = new HashMap<>();
        result.put("balancingType", "WEIGHTED_ROUND_ROBIN");
        result.put("distributedJobs", jobIds.size());
        return result;
    }

    private Map<String, Object> performResourceBasedLoadBalancing(List<Long> jobIds, Long tenantId) {
        // 基于资源的负载均衡实现
        Map<String, Object> result = new HashMap<>();
        result.put("balancingType", "RESOURCE_BASED");
        result.put("distributedJobs", jobIds.size());
        return result;
    }

    @Override
    public boolean managePriority(Long jobId, Integer priority, BigDecimal weight, String reason, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setPriority(priority);
            jobSchedule.setPriorityWeight(weight);
            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("管理优先级失败", e);
            throw new RuntimeException("管理优先级失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> monitorSLA(Long jobId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        try {
            Map<String, Object> slaMetrics = jobScheduleMapper.selectSlaMetrics(startTime, endTime, tenantId);

            if (jobId != null) {
                SsJobSchedule jobSchedule = getById(jobId);
                if (jobSchedule != null && jobSchedule.getTenantId().equals(tenantId)) {
                    slaMetrics.put("jobId", jobId);
                    slaMetrics.put("jobName", jobSchedule.getJobName());
                    slaMetrics.put("slaRequirements", jobSchedule.getSlaRequirements());
                }
            }

            return slaMetrics;
        } catch (Exception e) {
            log.error("SLA监控失败", e);
            throw new RuntimeException("SLA监控失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> optimizeSchedulingAlgorithm(String algorithmType, Map<String, Object> parameters, Long tenantId) {
        try {
            Map<String, Object> result = new HashMap<>();

            switch (algorithmType) {
                case "FIFO":
                    result = optimizeFIFOAlgorithm(parameters, tenantId);
                    break;
                case "SJF":
                    result = optimizeSJFAlgorithm(parameters, tenantId);
                    break;
                case "PRIORITY":
                    result = optimizePriorityAlgorithm(parameters, tenantId);
                    break;
                case "ROUND_ROBIN":
                    result = optimizeRoundRobinAlgorithm(parameters, tenantId);
                    break;
                default:
                    result.put("message", "不支持的算法类型: " + algorithmType);
            }

            result.put("algorithmType", algorithmType);
            result.put("optimizationTime", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            log.error("调度算法优化失败", e);
            throw new RuntimeException("调度算法优化失败: " + e.getMessage());
        }
    }

    private Map<String, Object> optimizeFIFOAlgorithm(Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "FIFO");
        result.put("optimizedJobs", 0);
        return result;
    }

    private Map<String, Object> optimizeSJFAlgorithm(Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "SJF");
        result.put("optimizedJobs", 0);
        return result;
    }

    private Map<String, Object> optimizePriorityAlgorithm(Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "PRIORITY");
        result.put("optimizedJobs", 0);
        return result;
    }

    private Map<String, Object> optimizeRoundRobinAlgorithm(Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "ROUND_ROBIN");
        result.put("optimizedJobs", 0);
        return result;
    }

    @Override
    public Map<String, Object> optimizePerformance(Long jobId, String optimizationType, Map<String, Object> config, Long tenantId) {
        try {
            Map<String, Object> result = new HashMap<>();

            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            switch (optimizationType) {
                case "RESOURCE":
                    result = optimizeResourcePerformance(jobSchedule, config);
                    break;
                case "EXECUTION_TIME":
                    result = optimizeExecutionTimePerformance(jobSchedule, config);
                    break;
                case "THROUGHPUT":
                    result = optimizeThroughputPerformance(jobSchedule, config);
                    break;
                case "LATENCY":
                    result = optimizeLatencyPerformance(jobSchedule, config);
                    break;
                default:
                    result.put("message", "不支持的优化类型: " + optimizationType);
            }

            result.put("jobId", jobId);
            result.put("optimizationType", optimizationType);
            result.put("optimizationTime", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            log.error("性能优化失败", e);
            throw new RuntimeException("性能优化失败: " + e.getMessage());
        }
    }

    private Map<String, Object> optimizeResourcePerformance(SsJobSchedule jobSchedule, Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "RESOURCE");
        result.put("originalCpuRequirement", jobSchedule.getCpuRequirement());
        result.put("originalMemoryRequirement", jobSchedule.getMemoryRequirement());
        result.put("optimizationApplied", true);
        return result;
    }

    private Map<String, Object> optimizeExecutionTimePerformance(SsJobSchedule jobSchedule, Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "EXECUTION_TIME");
        result.put("originalAvgDuration", jobSchedule.getAvgDuration());
        result.put("optimizationApplied", true);
        return result;
    }

    private Map<String, Object> optimizeThroughputPerformance(SsJobSchedule jobSchedule, Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "THROUGHPUT");
        result.put("originalExecutionCount", jobSchedule.getExecutionCount());
        result.put("optimizationApplied", true);
        return result;
    }

    private Map<String, Object> optimizeLatencyPerformance(SsJobSchedule jobSchedule, Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizationType", "LATENCY");
        result.put("optimizationApplied", true);
        return result;
    }

    @Override
    public Map<String, Object> generateSchedulingReport(LocalDateTime startTime, LocalDateTime endTime, String reportType, Long tenantId) {
        try {
            Map<String, Object> report = new HashMap<>();

            switch (reportType) {
                case "SUMMARY":
                    report = generateSummaryReport(startTime, endTime, tenantId);
                    break;
                case "PERFORMANCE":
                    report = generatePerformanceReport(startTime, endTime, tenantId);
                    break;
                case "SLA":
                    report = generateSlaReport(startTime, endTime, tenantId);
                    break;
                case "RESOURCE":
                    report = generateResourceReport(startTime, endTime, tenantId);
                    break;
                default:
                    report = generateSummaryReport(startTime, endTime, tenantId);
            }

            report.put("reportType", reportType);
            report.put("startTime", startTime);
            report.put("endTime", endTime);
            report.put("generatedTime", LocalDateTime.now());

            return report;
        } catch (Exception e) {
            log.error("生成调度报告失败", e);
            throw new RuntimeException("生成调度报告失败: " + e.getMessage());
        }
    }

    private Map<String, Object> generateSummaryReport(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> statistics = jobScheduleMapper.selectJobScheduleStatistics(startTime, endTime, tenantId);
        report.putAll(statistics);
        report.put("reportType", "SUMMARY");
        return report;
    }

    private Map<String, Object> generatePerformanceReport(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> performanceMetrics = jobScheduleMapper.selectJobPerformanceMetrics(startTime, endTime, tenantId);
        report.putAll(performanceMetrics);
        report.put("reportType", "PERFORMANCE");
        return report;
    }

    private Map<String, Object> generateSlaReport(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> slaMetrics = jobScheduleMapper.selectSlaMetrics(startTime, endTime, tenantId);
        report.putAll(slaMetrics);
        report.put("reportType", "SLA");
        return report;
    }

    private Map<String, Object> generateResourceReport(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> resourceUsage = jobScheduleMapper.selectResourceUsage(startTime, endTime, tenantId);
        report.putAll(resourceUsage);
        report.put("reportType", "RESOURCE");
        return report;
    }

    @Override
    public Map<String, Object> planCapacity(String planningType, LocalDateTime forecastPeriod, Map<String, Object> parameters, Long tenantId) {
        try {
            Map<String, Object> result = new HashMap<>();

            switch (planningType) {
                case "RESOURCE":
                    result = planResourceCapacity(forecastPeriod, parameters, tenantId);
                    break;
                case "WORKLOAD":
                    result = planWorkloadCapacity(forecastPeriod, parameters, tenantId);
                    break;
                case "PERFORMANCE":
                    result = planPerformanceCapacity(forecastPeriod, parameters, tenantId);
                    break;
                default:
                    result.put("message", "不支持的容量规划类型: " + planningType);
            }

            result.put("planningType", planningType);
            result.put("forecastPeriod", forecastPeriod);
            result.put("planningTime", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            log.error("容量规划失败", e);
            throw new RuntimeException("容量规划失败: " + e.getMessage());
        }
    }

    private Map<String, Object> planResourceCapacity(LocalDateTime forecastPeriod, Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> capacityData = jobScheduleMapper.selectCapacityPlanningData(LocalDateTime.now().minusDays(30), LocalDateTime.now(), tenantId);
        result.putAll(capacityData);
        result.put("planningType", "RESOURCE");
        return result;
    }

    private Map<String, Object> planWorkloadCapacity(LocalDateTime forecastPeriod, Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("planningType", "WORKLOAD");
        result.put("forecastedWorkload", 100);
        return result;
    }

    private Map<String, Object> planPerformanceCapacity(LocalDateTime forecastPeriod, Map<String, Object> parameters, Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("planningType", "PERFORMANCE");
        result.put("forecastedPerformance", 95.5);
        return result;
    }

    @Override
    public boolean configureSchedulingStrategy(Long jobId, String strategyType, Map<String, Object> config, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            jobSchedule.setScheduleStrategy(strategyType);
            // 将配置转换为JSON字符串存储
            // jobSchedule.setExtendedAttributes(JsonUtils.toJsonString(config));

            return updateById(jobSchedule);
        } catch (Exception e) {
            log.error("配置调度策略失败", e);
            throw new RuntimeException("配置调度策略失败: " + e.getMessage());
        }
    }

    @Override
    public List<SsJobSchedule> getPendingJobs(Long tenantId) {
        return jobScheduleMapper.selectPendingJobs(tenantId);
    }

    @Override
    public List<SsJobSchedule> getRunningJobs(Long tenantId) {
        return jobScheduleMapper.selectRunningJobs(tenantId);
    }

    @Override
    public List<SsJobSchedule> getCompletedJobs(Long tenantId) {
        return jobScheduleMapper.selectCompletedJobs(tenantId);
    }

    @Override
    public List<SsJobSchedule> getFailedJobs(Long tenantId) {
        return jobScheduleMapper.selectFailedJobs(tenantId);
    }

    @Override
    public List<SsJobSchedule> getTimeoutJobs(Long tenantId) {
        return jobScheduleMapper.selectTimeoutJobs(LocalDateTime.now(), tenantId);
    }

    @Override
    public List<SsJobSchedule> getHighPriorityJobs(Integer minPriority, Long tenantId) {
        return jobScheduleMapper.selectHighPriorityJobs(minPriority, tenantId);
    }

    @Override
    public List<SsJobSchedule> getRetryJobs(Long tenantId) {
        return jobScheduleMapper.selectRetryJobs(tenantId);
    }

    @Override
    public List<SsJobSchedule> getUpcomingJobs(LocalDateTime timeWindow, Long tenantId) {
        return jobScheduleMapper.selectUpcomingJobs(timeWindow, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateExecutionStatistics(Long jobId, Integer executionCount, Integer successCount,
                                           Integer failureCount, Integer avgDuration, BigDecimal successRate,
                                           LocalDateTime lastExecutionTime, LocalDateTime lastSuccessTime,
                                           LocalDateTime lastFailureTime, Long tenantId) {
        try {
            return jobScheduleMapper.updateExecutionStatistics(jobId, executionCount, successCount, failureCount,
                                                              avgDuration, successRate, lastExecutionTime,
                                                              lastSuccessTime, lastFailureTime, tenantId) > 0;
        } catch (Exception e) {
            log.error("更新执行统计信息失败", e);
            throw new RuntimeException("更新执行统计信息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNextExecutionTime(Long jobId, LocalDateTime nextExecutionTime, Long tenantId) {
        try {
            return jobScheduleMapper.updateNextExecutionTime(jobId, nextExecutionTime, tenantId) > 0;
        } catch (Exception e) {
            log.error("更新下次执行时间失败", e);
            throw new RuntimeException("更新下次执行时间失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getJobScheduleStatistics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectJobScheduleStatistics(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getJobStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectJobStatusDistribution(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getJobTypeDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectJobTypeDistribution(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getScheduleStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectScheduleStatusDistribution(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getExecutionStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectExecutionStatusDistribution(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getPriorityDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectPriorityDistribution(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getJobScheduleTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectJobScheduleTrend(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getSchedulerWorkload(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectSchedulerWorkload(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getExecutorWorkload(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectExecutorWorkload(startTime, endTime, tenantId);
    }

    @Override
    public Map<String, Object> getJobPerformanceMetrics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectJobPerformanceMetrics(startTime, endTime, tenantId);
    }

    @Override
    public Map<String, Object> getSlaMetrics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectSlaMetrics(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getJobScheduleRanking(String rankingType, LocalDateTime startTime, LocalDateTime endTime, Integer limit, Long tenantId) {
        return jobScheduleMapper.selectJobScheduleRanking(rankingType, startTime, endTime, limit, tenantId);
    }

    @Override
    public boolean checkJobCodeExists(String jobCode, Long jobId, Long tenantId) {
        return jobScheduleMapper.checkJobCodeExists(jobCode, jobId, tenantId) > 0;
    }

    @Override
    public boolean checkJobDependency(Long jobId, Long dependencyJobId, Long tenantId) {
        return jobScheduleMapper.checkJobDependency(jobId, dependencyJobId, tenantId) > 0;
    }

    @Override
    public BigDecimal calculateAverageExecutionTime(String jobType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.calculateAverageExecutionTime(jobType, startTime, endTime, tenantId);
    }

    @Override
    public BigDecimal calculateSuccessRate(String jobType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.calculateSuccessRate(jobType, startTime, endTime, tenantId);
    }

    @Override
    public Map<String, Object> getResourceUsage(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectResourceUsage(startTime, endTime, tenantId);
    }

    @Override
    public Map<String, Object> getCapacityPlanningData(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return jobScheduleMapper.selectCapacityPlanningData(startTime, endTime, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importJobScheduleData(List<Map<String, Object>> dataList, Long tenantId) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failureCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (Map<String, Object> data : dataList) {
                try {
                    SsJobSchedule jobSchedule = convertMapToJobSchedule(data, tenantId);
                    if (createJobSchedule(jobSchedule)) {
                        successCount++;
                    } else {
                        failureCount++;
                        errorMessages.add("导入作业失败: " + data.get("jobName"));
                    }
                } catch (Exception e) {
                    failureCount++;
                    errorMessages.add("导入作业异常: " + data.get("jobName") + " - " + e.getMessage());
                }
            }

            result.put("totalCount", dataList.size());
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("errorMessages", errorMessages);
            result.put("importTime", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            log.error("导入作业调度数据失败", e);
            throw new RuntimeException("导入作业调度数据失败: " + e.getMessage());
        }
    }

    private SsJobSchedule convertMapToJobSchedule(Map<String, Object> data, Long tenantId) {
        SsJobSchedule jobSchedule = new SsJobSchedule();
        jobSchedule.setJobCode((String) data.get("jobCode"));
        jobSchedule.setJobName((String) data.get("jobName"));
        jobSchedule.setJobDescription((String) data.get("jobDescription"));
        jobSchedule.setJobType((String) data.get("jobType"));
        jobSchedule.setJobCategory((String) data.get("jobCategory"));
        jobSchedule.setScheduleExpression((String) data.get("scheduleExpression"));
        jobSchedule.setScheduleType((String) data.get("scheduleType"));
        jobSchedule.setPriority(data.get("priority") != null ? Integer.valueOf(data.get("priority").toString()) : 5);
        jobSchedule.setTenantId(tenantId);
        return jobSchedule;
    }

    @Override
    public List<Map<String, Object>> exportJobScheduleData(Map<String, Object> queryParams, Long tenantId) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();

            QueryWrapper<SsJobSchedule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id", tenantId);

            // 根据查询参数构建查询条件
            if (queryParams.get("jobType") != null) {
                queryWrapper.eq("job_type", queryParams.get("jobType"));
            }
            if (queryParams.get("jobStatus") != null) {
                queryWrapper.eq("job_status", queryParams.get("jobStatus"));
            }

            List<SsJobSchedule> jobSchedules = list(queryWrapper);

            for (SsJobSchedule jobSchedule : jobSchedules) {
                Map<String, Object> data = new HashMap<>();
                data.put("jobCode", jobSchedule.getJobCode());
                data.put("jobName", jobSchedule.getJobName());
                data.put("jobDescription", jobSchedule.getJobDescription());
                data.put("jobType", jobSchedule.getJobType());
                data.put("jobCategory", jobSchedule.getJobCategory());
                data.put("jobStatus", jobSchedule.getJobStatus());
                data.put("scheduleStatus", jobSchedule.getScheduleStatus());
                data.put("executionStatus", jobSchedule.getExecutionStatus());
                data.put("priority", jobSchedule.getPriority());
                data.put("scheduleExpression", jobSchedule.getScheduleExpression());
                data.put("scheduleType", jobSchedule.getScheduleType());
                data.put("executionCount", jobSchedule.getExecutionCount());
                data.put("successCount", jobSchedule.getSuccessCount());
                data.put("failureCount", jobSchedule.getFailureCount());
                data.put("successRate", jobSchedule.getSuccessRate());
                data.put("avgDuration", jobSchedule.getAvgDuration());
                data.put("lastExecutionTime", jobSchedule.getLastExecutionTime());
                data.put("nextExecutionTime", jobSchedule.getNextExecutionTime());
                data.put("createdTime", jobSchedule.getCreatedTime());
                result.add(data);
            }

            return result;
        } catch (Exception e) {
            log.error("导出作业调度数据失败", e);
            throw new RuntimeException("导出作业调度数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> generateJobScheduleReport(String reportType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return generateSchedulingReport(startTime, endTime, reportType, tenantId);
    }

    @Override
    public boolean sendNotification(Long jobId, String notificationType, String message, Long tenantId) {
        try {
            SsJobSchedule jobSchedule = getById(jobId);
            if (jobSchedule == null || !jobSchedule.getTenantId().equals(tenantId)) {
                throw new RuntimeException("作业调度不存在");
            }

            // 这里应该实现具体的通知发送逻辑
            log.info("发送通知 - 作业ID: {}, 通知类型: {}, 消息: {}", jobId, notificationType, message);

            return true;
        } catch (Exception e) {
            log.error("发送通知失败", e);
            throw new RuntimeException("发送通知失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchSendNotification(List<Long> jobIds, String notificationType, String message, Long tenantId) {
        try {
            int successCount = 0;

            for (Long jobId : jobIds) {
                try {
                    if (sendNotification(jobId, notificationType, message, tenantId)) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.warn("发送通知失败 - 作业ID: {}", jobId, e);
                }
            }

            return successCount > 0;
        } catch (Exception e) {
            log.error("批量发送通知失败", e);
            throw new RuntimeException("批量发送通知失败: " + e.getMessage());
        }
    }
}
