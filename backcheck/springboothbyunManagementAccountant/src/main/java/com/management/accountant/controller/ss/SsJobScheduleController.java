package com.management.accountant.controller.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsJobSchedule;
import com.management.accountant.service.ss.SsJobScheduleService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 作业调度控制器
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ss/jobSchedule")
@Api(tags = "作业调度管理")
public class SsJobScheduleController {

    @Autowired
    private SsJobScheduleService jobScheduleService;

    /**
     * 分页查询作业调度列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询作业调度列表")
    public MyJsonBean getJobSchedulePage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("作业名称") @RequestParam(required = false) String jobName,
            @ApiParam("作业类型") @RequestParam(required = false) String jobType,
            @ApiParam("作业状态") @RequestParam(required = false) String jobStatus,
            @ApiParam("调度状态") @RequestParam(required = false) String scheduleStatus,
            @ApiParam("执行状态") @RequestParam(required = false) String executionStatus,
            @ApiParam("优先级") @RequestParam(required = false) Integer priority,
            @ApiParam("调度器ID") @RequestParam(required = false) Long schedulerId,
            @ApiParam("执行用户ID") @RequestParam(required = false) Long executorUserId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Page<SsJobSchedule> page = new Page<>(current, size);
            IPage<SsJobSchedule> result = jobScheduleService.getJobSchedulePage(page, jobName, jobType, jobStatus, 
                                                                               scheduleStatus, executionStatus, priority,
                                                                               schedulerId, executorUserId, startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询作业调度列表失败", e);
            return MyJsonBean.error("分页查询作业调度列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询作业调度详情
     */
    @GetMapping("/{jobId}")
    @ApiOperation("根据ID查询作业调度详情")
    public MyJsonBean getJobScheduleById(@ApiParam("作业ID") @PathVariable Long jobId) {
        try {
            SsJobSchedule jobSchedule = jobScheduleService.getById(jobId);
            return MyJsonBean.success(jobSchedule);
        } catch (Exception e) {
            log.error("查询作业调度详情失败", e);
            return MyJsonBean.error("查询作业调度详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据作业编码查询
     */
    @GetMapping("/code/{jobCode}")
    @ApiOperation("根据作业编码查询")
    public MyJsonBean getJobScheduleByCode(
            @ApiParam("作业编码") @PathVariable String jobCode,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            SsJobSchedule jobSchedule = jobScheduleService.getByJobCode(jobCode, tenantId);
            return MyJsonBean.success(jobSchedule);
        } catch (Exception e) {
            log.error("根据作业编码查询失败", e);
            return MyJsonBean.error("根据作业编码查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建作业调度
     */
    @PostMapping
    @ApiOperation("创建作业调度")
    public MyJsonBean createJobSchedule(@RequestBody SsJobSchedule jobSchedule) {
        try {
            boolean result = jobScheduleService.createJobSchedule(jobSchedule);
            return result ? MyJsonBean.success("创建成功") : MyJsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建作业调度失败", e);
            return MyJsonBean.error("创建作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 更新作业调度
     */
    @PutMapping
    @ApiOperation("更新作业调度")
    public MyJsonBean updateJobSchedule(@RequestBody SsJobSchedule jobSchedule) {
        try {
            boolean result = jobScheduleService.updateJobSchedule(jobSchedule);
            return result ? MyJsonBean.success("更新成功") : MyJsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新作业调度失败", e);
            return MyJsonBean.error("更新作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 删除作业调度
     */
    @DeleteMapping("/{jobId}")
    @ApiOperation("删除作业调度")
    public MyJsonBean deleteJobSchedule(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.deleteJobSchedule(jobId, tenantId);
            return result ? MyJsonBean.success("删除成功") : MyJsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除作业调度失败", e);
            return MyJsonBean.error("删除作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除作业调度
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除作业调度")
    public MyJsonBean batchDeleteJobSchedule(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchDeleteJobSchedule(jobIds, tenantId);
            return result ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除作业调度失败", e);
            return MyJsonBean.error("批量删除作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 启动作业调度
     */
    @PostMapping("/{jobId}/start")
    @ApiOperation("启动作业调度")
    public MyJsonBean startJobSchedule(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.startJobSchedule(jobId, tenantId);
            return result ? MyJsonBean.success("启动成功") : MyJsonBean.error("启动失败");
        } catch (Exception e) {
            log.error("启动作业调度失败", e);
            return MyJsonBean.error("启动作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 停止作业调度
     */
    @PostMapping("/{jobId}/stop")
    @ApiOperation("停止作业调度")
    public MyJsonBean stopJobSchedule(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.stopJobSchedule(jobId, tenantId);
            return result ? MyJsonBean.success("停止成功") : MyJsonBean.error("停止失败");
        } catch (Exception e) {
            log.error("停止作业调度失败", e);
            return MyJsonBean.error("停止作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 暂停作业调度
     */
    @PostMapping("/{jobId}/pause")
    @ApiOperation("暂停作业调度")
    public MyJsonBean pauseJobSchedule(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.pauseJobSchedule(jobId, tenantId);
            return result ? MyJsonBean.success("暂停成功") : MyJsonBean.error("暂停失败");
        } catch (Exception e) {
            log.error("暂停作业调度失败", e);
            return MyJsonBean.error("暂停作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 恢复作业调度
     */
    @PostMapping("/{jobId}/resume")
    @ApiOperation("恢复作业调度")
    public MyJsonBean resumeJobSchedule(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.resumeJobSchedule(jobId, tenantId);
            return result ? MyJsonBean.success("恢复成功") : MyJsonBean.error("恢复失败");
        } catch (Exception e) {
            log.error("恢复作业调度失败", e);
            return MyJsonBean.error("恢复作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 立即执行作业
     */
    @PostMapping("/{jobId}/execute")
    @ApiOperation("立即执行作业")
    public MyJsonBean executeJobImmediately(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.executeJobImmediately(jobId, tenantId);
            return result ? MyJsonBean.success("执行成功") : MyJsonBean.error("执行失败");
        } catch (Exception e) {
            log.error("立即执行作业失败", e);
            return MyJsonBean.error("立即执行作业失败: " + e.getMessage());
        }
    }

    /**
     * 重新调度作业
     */
    @PostMapping("/{jobId}/reschedule")
    @ApiOperation("重新调度作业")
    public MyJsonBean rescheduleJob(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("调度表达式") @RequestParam String scheduleExpression,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.rescheduleJob(jobId, scheduleExpression, tenantId);
            return result ? MyJsonBean.success("重新调度成功") : MyJsonBean.error("重新调度失败");
        } catch (Exception e) {
            log.error("重新调度作业失败", e);
            return MyJsonBean.error("重新调度作业失败: " + e.getMessage());
        }
    }

    /**
     * 批量启动作业调度
     */
    @PostMapping("/batch/start")
    @ApiOperation("批量启动作业调度")
    public MyJsonBean batchStartJobSchedule(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchStartJobSchedule(jobIds, tenantId);
            return result ? MyJsonBean.success("批量启动成功") : MyJsonBean.error("批量启动失败");
        } catch (Exception e) {
            log.error("批量启动作业调度失败", e);
            return MyJsonBean.error("批量启动作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 批量停止作业调度
     */
    @PostMapping("/batch/stop")
    @ApiOperation("批量停止作业调度")
    public MyJsonBean batchStopJobSchedule(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchStopJobSchedule(jobIds, tenantId);
            return result ? MyJsonBean.success("批量停止成功") : MyJsonBean.error("批量停止失败");
        } catch (Exception e) {
            log.error("批量停止作业调度失败", e);
            return MyJsonBean.error("批量停止作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 批量暂停作业调度
     */
    @PostMapping("/batch/pause")
    @ApiOperation("批量暂停作业调度")
    public MyJsonBean batchPauseJobSchedule(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchPauseJobSchedule(jobIds, tenantId);
            return result ? MyJsonBean.success("批量暂停成功") : MyJsonBean.error("批量暂停失败");
        } catch (Exception e) {
            log.error("批量暂停作业调度失败", e);
            return MyJsonBean.error("批量暂停作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 批量恢复作业调度
     */
    @PostMapping("/batch/resume")
    @ApiOperation("批量恢复作业调度")
    public MyJsonBean batchResumeJobSchedule(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchResumeJobSchedule(jobIds, tenantId);
            return result ? MyJsonBean.success("批量恢复成功") : MyJsonBean.error("批量恢复失败");
        } catch (Exception e) {
            log.error("批量恢复作业调度失败", e);
            return MyJsonBean.error("批量恢复作业调度失败: " + e.getMessage());
        }
    }

    /**
     * 批量分配调度器
     */
    @PostMapping("/batch/assign-scheduler")
    @ApiOperation("批量分配调度器")
    public MyJsonBean batchAssignScheduler(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("调度器ID") @RequestParam Long schedulerId,
            @ApiParam("调度器名称") @RequestParam String schedulerName,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchAssignScheduler(jobIds, schedulerId, schedulerName, tenantId);
            return result ? MyJsonBean.success("批量分配调度器成功") : MyJsonBean.error("批量分配调度器失败");
        } catch (Exception e) {
            log.error("批量分配调度器失败", e);
            return MyJsonBean.error("批量分配调度器失败: " + e.getMessage());
        }
    }

    /**
     * 批量设置优先级
     */
    @PostMapping("/batch/set-priority")
    @ApiOperation("批量设置优先级")
    public MyJsonBean batchSetPriority(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("优先级") @RequestParam Integer priority,
            @ApiParam("优先级权重") @RequestParam BigDecimal priorityWeight,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchSetPriority(jobIds, priority, priorityWeight, tenantId);
            return result ? MyJsonBean.success("批量设置优先级成功") : MyJsonBean.error("批量设置优先级失败");
        } catch (Exception e) {
            log.error("批量设置优先级失败", e);
            return MyJsonBean.error("批量设置优先级失败: " + e.getMessage());
        }
    }

    /**
     * 任务分配管理
     */
    @PostMapping("/{jobId}/assign")
    @ApiOperation("任务分配管理")
    public MyJsonBean assignJobToScheduler(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("调度器ID") @RequestParam Long schedulerId,
            @ApiParam("负载均衡策略") @RequestParam String loadBalanceStrategy,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.assignJobToScheduler(jobId, schedulerId, loadBalanceStrategy, tenantId);
            return result ? MyJsonBean.success("任务分配成功") : MyJsonBean.error("任务分配失败");
        } catch (Exception e) {
            log.error("任务分配失败", e);
            return MyJsonBean.error("任务分配失败: " + e.getMessage());
        }
    }

    /**
     * 负载均衡功能
     */
    @PostMapping("/load-balance")
    @ApiOperation("负载均衡功能")
    public MyJsonBean performLoadBalancing(
            @ApiParam("负载均衡策略") @RequestParam String strategy,
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.performLoadBalancing(strategy, jobIds, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("负载均衡失败", e);
            return MyJsonBean.error("负载均衡失败: " + e.getMessage());
        }
    }

    /**
     * 优先级管理
     */
    @PostMapping("/{jobId}/priority")
    @ApiOperation("优先级管理")
    public MyJsonBean managePriority(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("优先级") @RequestParam Integer priority,
            @ApiParam("优先级权重") @RequestParam BigDecimal weight,
            @ApiParam("原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.managePriority(jobId, priority, weight, reason, tenantId);
            return result ? MyJsonBean.success("优先级管理成功") : MyJsonBean.error("优先级管理失败");
        } catch (Exception e) {
            log.error("优先级管理失败", e);
            return MyJsonBean.error("优先级管理失败: " + e.getMessage());
        }
    }

    /**
     * SLA监控体系
     */
    @GetMapping("/sla-monitor")
    @ApiOperation("SLA监控体系")
    public MyJsonBean monitorSLA(
            @ApiParam("作业ID") @RequestParam(required = false) Long jobId,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.monitorSLA(jobId, startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("SLA监控失败", e);
            return MyJsonBean.error("SLA监控失败: " + e.getMessage());
        }
    }

    /**
     * 调度算法优化
     */
    @PostMapping("/optimize-algorithm")
    @ApiOperation("调度算法优化")
    public MyJsonBean optimizeSchedulingAlgorithm(
            @ApiParam("算法类型") @RequestParam String algorithmType,
            @ApiParam("参数配置") @RequestBody Map<String, Object> parameters,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.optimizeSchedulingAlgorithm(algorithmType, parameters, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("调度算法优化失败", e);
            return MyJsonBean.error("调度算法优化失败: " + e.getMessage());
        }
    }

    /**
     * 性能优化管理
     */
    @PostMapping("/{jobId}/optimize-performance")
    @ApiOperation("性能优化管理")
    public MyJsonBean optimizePerformance(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("优化类型") @RequestParam String optimizationType,
            @ApiParam("配置参数") @RequestBody Map<String, Object> config,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.optimizePerformance(jobId, optimizationType, config, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("性能优化失败", e);
            return MyJsonBean.error("性能优化失败: " + e.getMessage());
        }
    }

    /**
     * 调度报告分析
     */
    @GetMapping("/report")
    @ApiOperation("调度报告分析")
    public MyJsonBean generateSchedulingReport(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("报告类型") @RequestParam String reportType,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.generateSchedulingReport(startTime, endTime, reportType, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("生成调度报告失败", e);
            return MyJsonBean.error("生成调度报告失败: " + e.getMessage());
        }
    }

    /**
     * 容量规划管理
     */
    @PostMapping("/capacity-planning")
    @ApiOperation("容量规划管理")
    public MyJsonBean planCapacity(
            @ApiParam("规划类型") @RequestParam String planningType,
            @ApiParam("预测周期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime forecastPeriod,
            @ApiParam("参数配置") @RequestBody Map<String, Object> parameters,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.planCapacity(planningType, forecastPeriod, parameters, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("容量规划失败", e);
            return MyJsonBean.error("容量规划失败: " + e.getMessage());
        }
    }

    /**
     * 调度策略配置
     */
    @PostMapping("/{jobId}/configure-strategy")
    @ApiOperation("调度策略配置")
    public MyJsonBean configureSchedulingStrategy(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("策略类型") @RequestParam String strategyType,
            @ApiParam("配置参数") @RequestBody Map<String, Object> config,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.configureSchedulingStrategy(jobId, strategyType, config, tenantId);
            return result ? MyJsonBean.success("调度策略配置成功") : MyJsonBean.error("调度策略配置失败");
        } catch (Exception e) {
            log.error("调度策略配置失败", e);
            return MyJsonBean.error("调度策略配置失败: " + e.getMessage());
        }
    }

    /**
     * 查询待调度的作业列表
     */
    @GetMapping("/pending")
    @ApiOperation("查询待调度的作业列表")
    public MyJsonBean getPendingJobs(@ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsJobSchedule> result = jobScheduleService.getPendingJobs(tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询待调度作业失败", e);
            return MyJsonBean.error("查询待调度作业失败: " + e.getMessage());
        }
    }

    /**
     * 查询正在运行的作业列表
     */
    @GetMapping("/running")
    @ApiOperation("查询正在运行的作业列表")
    public MyJsonBean getRunningJobs(@ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsJobSchedule> result = jobScheduleService.getRunningJobs(tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询正在运行作业失败", e);
            return MyJsonBean.error("查询正在运行作业失败: " + e.getMessage());
        }
    }

    /**
     * 查询已完成的作业列表
     */
    @GetMapping("/completed")
    @ApiOperation("查询已完成的作业列表")
    public MyJsonBean getCompletedJobs(@ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsJobSchedule> result = jobScheduleService.getCompletedJobs(tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询已完成作业失败", e);
            return MyJsonBean.error("查询已完成作业失败: " + e.getMessage());
        }
    }

    /**
     * 查询失败的作业列表
     */
    @GetMapping("/failed")
    @ApiOperation("查询失败的作业列表")
    public MyJsonBean getFailedJobs(@ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsJobSchedule> result = jobScheduleService.getFailedJobs(tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询失败作业失败", e);
            return MyJsonBean.error("查询失败作业失败: " + e.getMessage());
        }
    }

    /**
     * 统计作业调度数据
     */
    @GetMapping("/statistics")
    @ApiOperation("统计作业调度数据")
    public MyJsonBean getJobScheduleStatistics(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.getJobScheduleStatistics(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计作业调度数据失败", e);
            return MyJsonBean.error("统计作业调度数据失败: " + e.getMessage());
        }
    }

    /**
     * 统计作业状态分布
     */
    @GetMapping("/statistics/job-status")
    @ApiOperation("统计作业状态分布")
    public MyJsonBean getJobStatusDistribution(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getJobStatusDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计作业状态分布失败", e);
            return MyJsonBean.error("统计作业状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计作业类型分布
     */
    @GetMapping("/statistics/job-type")
    @ApiOperation("统计作业类型分布")
    public MyJsonBean getJobTypeDistribution(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getJobTypeDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计作业类型分布失败", e);
            return MyJsonBean.error("统计作业类型分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计调度状态分布
     */
    @GetMapping("/statistics/schedule-status")
    @ApiOperation("统计调度状态分布")
    public MyJsonBean getScheduleStatusDistribution(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getScheduleStatusDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计调度状态分布失败", e);
            return MyJsonBean.error("统计调度状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计执行状态分布
     */
    @GetMapping("/statistics/execution-status")
    @ApiOperation("统计执行状态分布")
    public MyJsonBean getExecutionStatusDistribution(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getExecutionStatusDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计执行状态分布失败", e);
            return MyJsonBean.error("统计执行状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计优先级分布
     */
    @GetMapping("/statistics/priority")
    @ApiOperation("统计优先级分布")
    public MyJsonBean getPriorityDistribution(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getPriorityDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计优先级分布失败", e);
            return MyJsonBean.error("统计优先级分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计作业调度趋势
     */
    @GetMapping("/statistics/trend")
    @ApiOperation("统计作业调度趋势")
    public MyJsonBean getJobScheduleTrend(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getJobScheduleTrend(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计作业调度趋势失败", e);
            return MyJsonBean.error("统计作业调度趋势失败: " + e.getMessage());
        }
    }

    /**
     * 统计调度器工作负载
     */
    @GetMapping("/statistics/scheduler-workload")
    @ApiOperation("统计调度器工作负载")
    public MyJsonBean getSchedulerWorkload(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getSchedulerWorkload(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计调度器工作负载失败", e);
            return MyJsonBean.error("统计调度器工作负载失败: " + e.getMessage());
        }
    }

    /**
     * 统计执行用户工作负载
     */
    @GetMapping("/statistics/executor-workload")
    @ApiOperation("统计执行用户工作负载")
    public MyJsonBean getExecutorWorkload(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getExecutorWorkload(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计执行用户工作负载失败", e);
            return MyJsonBean.error("统计执行用户工作负载失败: " + e.getMessage());
        }
    }

    /**
     * 统计作业性能指标
     */
    @GetMapping("/statistics/performance")
    @ApiOperation("统计作业性能指标")
    public MyJsonBean getJobPerformanceMetrics(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.getJobPerformanceMetrics(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计作业性能指标失败", e);
            return MyJsonBean.error("统计作业性能指标失败: " + e.getMessage());
        }
    }

    /**
     * 统计SLA达成情况
     */
    @GetMapping("/statistics/sla")
    @ApiOperation("统计SLA达成情况")
    public MyJsonBean getSlaMetrics(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.getSlaMetrics(startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("统计SLA达成情况失败", e);
            return MyJsonBean.error("统计SLA达成情况失败: " + e.getMessage());
        }
    }

    /**
     * 查询作业调度排行榜
     */
    @GetMapping("/ranking")
    @ApiOperation("查询作业调度排行榜")
    public MyJsonBean getJobScheduleRanking(
            @ApiParam("排行类型") @RequestParam String rankingType,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.getJobScheduleRanking(rankingType, startTime, endTime, limit, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询作业调度排行榜失败", e);
            return MyJsonBean.error("查询作业调度排行榜失败: " + e.getMessage());
        }
    }

    /**
     * 导入作业调度数据
     */
    @PostMapping("/import")
    @ApiOperation("导入作业调度数据")
    public MyJsonBean importJobScheduleData(
            @ApiParam("数据列表") @RequestBody List<Map<String, Object>> dataList,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> result = jobScheduleService.importJobScheduleData(dataList, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入作业调度数据失败", e);
            return MyJsonBean.error("导入作业调度数据失败: " + e.getMessage());
        }
    }

    /**
     * 导出作业调度数据
     */
    @PostMapping("/export")
    @ApiOperation("导出作业调度数据")
    public MyJsonBean exportJobScheduleData(
            @ApiParam("查询参数") @RequestBody Map<String, Object> queryParams,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> result = jobScheduleService.exportJobScheduleData(queryParams, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出作业调度数据失败", e);
            return MyJsonBean.error("导出作业调度数据失败: " + e.getMessage());
        }
    }

    /**
     * 发送通知
     */
    @PostMapping("/{jobId}/notify")
    @ApiOperation("发送通知")
    public MyJsonBean sendNotification(
            @ApiParam("作业ID") @PathVariable Long jobId,
            @ApiParam("通知类型") @RequestParam String notificationType,
            @ApiParam("消息内容") @RequestParam String message,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.sendNotification(jobId, notificationType, message, tenantId);
            return result ? MyJsonBean.success("发送通知成功") : MyJsonBean.error("发送通知失败");
        } catch (Exception e) {
            log.error("发送通知失败", e);
            return MyJsonBean.error("发送通知失败: " + e.getMessage());
        }
    }

    /**
     * 批量发送通知
     */
    @PostMapping("/batch/notify")
    @ApiOperation("批量发送通知")
    public MyJsonBean batchSendNotification(
            @ApiParam("作业ID列表") @RequestBody List<Long> jobIds,
            @ApiParam("通知类型") @RequestParam String notificationType,
            @ApiParam("消息内容") @RequestParam String message,
            @ApiParam("租户ID") @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            boolean result = jobScheduleService.batchSendNotification(jobIds, notificationType, message, tenantId);
            return result ? MyJsonBean.success("批量发送通知成功") : MyJsonBean.error("批量发送通知失败");
        } catch (Exception e) {
            log.error("批量发送通知失败", e);
            return MyJsonBean.error("批量发送通知失败: " + e.getMessage());
        }
    }
}
