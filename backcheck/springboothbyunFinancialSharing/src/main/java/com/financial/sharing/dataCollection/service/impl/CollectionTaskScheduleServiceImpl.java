package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dataCollection.entity.TblCollectionTask;
import com.financial.sharing.dataCollection.mapper.CollectionTaskMapper;
import com.financial.sharing.dataCollection.service.CollectionTaskScheduleService;
import com.financial.sharing.dataCollection.service.CollectionTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 归集任务调度Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class CollectionTaskScheduleServiceImpl implements CollectionTaskScheduleService {

    @Autowired
    private CollectionTaskMapper collectionTaskMapper;

    @Autowired
    private CollectionTaskService collectionTaskService;

    /**
     * 任务调度器
     */
    private TaskScheduler taskScheduler;

    /**
     * 存储正在调度的任务
     * Key: taskId, Value: ScheduledFuture
     */
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    /**
     * 初始化任务调度器
     */
    public CollectionTaskScheduleServiceImpl() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("collection-task-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.initialize();
        this.taskScheduler = scheduler;
    }

    /**
     * 应用启动时初始化所有启用的定时任务
     * 暂时禁用归集任务调度初始化 - 数据库表结构不匹配
     * TASK_TYPE 列不存在
     */
    @PostConstruct
    @Override
    public void initScheduledTasks() {
        log.info("归集任务调度初始化已禁用 - 数据库表结构不匹配");
    }

    /**
     * 应用关闭时取消所有调度任务
     */
    @PreDestroy
    public void destroy() {
        log.info("开始关闭归集任务调度...");
        
        for (Map.Entry<String, ScheduledFuture<?>> entry : scheduledTasks.entrySet()) {
            try {
                entry.getValue().cancel(false);
                log.info("取消定时任务: {}", entry.getKey());
            } catch (Exception e) {
                log.error("取消定时任务失败: {}", entry.getKey(), e);
            }
        }
        
        scheduledTasks.clear();
        log.info("归集任务调度关闭完成");
    }

    @Override
    public void scheduleTask(TblCollectionTask task) {
        // 先取消已存在的调度
        cancelTask(task.getTaskId());
        
        log.info("开始调度归集任务: {} - {}, 调度类型: {}, 调度表达式: {}", 
                task.getTaskCode(), task.getTaskName(), task.getScheduleType(), task.getScheduleExpression());
        
        try {
            // 创建任务执行的Runnable
            Runnable taskRunnable = () -> {
                long startTime = System.currentTimeMillis();
                try {
                    log.info("定时任务开始执行: {} - {}", task.getTaskCode(), task.getTaskName());
                    
                    // 调用任务执行服务
                    collectionTaskService.executeTask(task.getTaskId(), task.getOrgId());
                    
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("定时任务执行完成: {} - {}, 耗时: {}ms", 
                            task.getTaskCode(), task.getTaskName(), duration);
                    
                } catch (Exception e) {
                    log.error("定时任务执行失败: {} - {}", task.getTaskCode(), e.getMessage(), e);
                }
            };
            
            // 根据调度类型创建调度
            ScheduledFuture<?> future = null;
            String scheduleType = task.getScheduleType();

            if ("CRON".equals(scheduleType)) {
                // CRON表达式调度
                String cronExpression = task.getScheduleExpression();
                if (StringUtils.isBlank(cronExpression)) {
                    log.error("CRON表达式为空，无法调度任务: {}", task.getTaskCode());
                    return;
                }

                future = taskScheduler.schedule(taskRunnable, new CronTrigger(cronExpression));
                log.info("使用CRON表达式调度任务: {}, 表达式: {}", task.getTaskCode(), cronExpression);

            } else if ("ONCE".equals(scheduleType)) {
                // 一次性任务
                Date nextExecuteTime = task.getNextExecuteTime();
                if (nextExecuteTime == null) {
                    nextExecuteTime = new Date();
                }

                future = taskScheduler.schedule(taskRunnable, nextExecuteTime);
                log.info("调度一次性任务: {}, 执行时间: {}", task.getTaskCode(), nextExecuteTime);

            } else if ("DAILY".equals(scheduleType)) {
                // 每日任务 - 转换为CRON表达式
                LocalDateTime now = LocalDateTime.now();
                String cronExpression = String.format("0 %d %d * * ?", now.getMinute(), now.getHour());

                future = taskScheduler.schedule(taskRunnable, new CronTrigger(cronExpression));
                log.info("调度每日任务: {}, CRON表达式: {}", task.getTaskCode(), cronExpression);

            } else if ("WEEKLY".equals(scheduleType)) {
                // 每周任务 - 转换为CRON表达式
                LocalDateTime now = LocalDateTime.now();
                int dayOfWeek = now.getDayOfWeek().getValue();
                String cronExpression = String.format("0 %d %d ? * %d", now.getMinute(), now.getHour(), dayOfWeek);

                future = taskScheduler.schedule(taskRunnable, new CronTrigger(cronExpression));
                log.info("调度每周任务: {}, CRON表达式: {}", task.getTaskCode(), cronExpression);

            } else if ("MONTHLY".equals(scheduleType)) {
                // 每月任务 - 转换为CRON表达式
                LocalDateTime now = LocalDateTime.now();
                String cronExpression = String.format("0 %d %d %d * ?", now.getMinute(), now.getHour(), now.getDayOfMonth());

                future = taskScheduler.schedule(taskRunnable, new CronTrigger(cronExpression));
                log.info("调度每月任务: {}, CRON表达式: {}", task.getTaskCode(), cronExpression);

            } else {
                log.error("不支持的调度类型: {}, 任务: {}", scheduleType, task.getTaskCode());
                return;
            }

            // 保存调度任务
            if (future != null) {
                scheduledTasks.put(task.getTaskId(), future);
                log.info("归集任务调度成功: {} - {}", task.getTaskCode(), task.getTaskName());
            }

        } catch (Exception e) {
            log.error("调度归集任务失败: {} - {}", task.getTaskCode(), e.getMessage(), e);
        }
    }

    @Override
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskId);
        if (future != null) {
            future.cancel(false);
            log.info("取消归集任务调度: {}", taskId);
        }
    }

    @Override
    public boolean isScheduled(String taskId) {
        return scheduledTasks.containsKey(taskId);
    }

    @Override
    public void rescheduleTask(TblCollectionTask task) {
        log.info("重新调度归集任务: {} - {}", task.getTaskCode(), task.getTaskName());
        // 先取消旧调度
        cancelTask(task.getTaskId());
        // 重新调度
        scheduleTask(task);
    }
}
