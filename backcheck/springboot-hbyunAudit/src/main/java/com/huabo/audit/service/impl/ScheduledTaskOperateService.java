package com.huabo.audit.service.impl;

import com.huabo.audit.oracle.entity.TblUruleTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务操作服务
 * 用于管理和执行定时任务
 *
 * @author Augment Code AI
 * @date 2025-01-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskOperateService {

    private final ApplicationContext applicationContext;

    //执行公司采集对应的方法
//    @Resource
//    private GatherFinanceDateService gatherFinanceDateService;

    @Resource
    RestTemplate restTemplate;


    private final ThreadPoolTaskScheduler taskScheduler;


    private final Map<String, ScheduledFuture<?>> taskFutureMap = new ConcurrentHashMap<>();



    /**
     * 取消定时任务
     */
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = taskFutureMap.get(taskId);
        if (future != null) {
            future.cancel(true);
            taskFutureMap.remove(taskId);
        }
    }

    /**
     * 立即执行一次任务
     */
    public void runTaskOnce(Long taskId) {
        /*taskRepository.findById(taskId).ifPresent(task -> {
            try {
                Object bean = applicationContext.getBean(task.getBeanName());
                Method method = bean.getClass().getMethod(task.getMethodName(), String.class);
                method.invoke(bean, task.getParams());
            } catch (Exception e) {
                throw new RuntimeException("执行任务失败", e);
            }
        });*/
    }

    /**
     * 获取任务状态
     */
    public boolean containsKey(String taskId) {
        return taskFutureMap.containsKey(taskId);
    }

    /**
     * 调度定时任务
     *
     * @param task 任务信息
     */
    public void scheduleTask(TblUruleTask task) {
        cancelTask(task.getTaskId());
        log.info("开始调度定时任务 - 任务ID: {}, Cron表达式: {}", task.getTaskId(), task.getCornExpression());

        Runnable runnable = () -> {
            long startTime = System.currentTimeMillis();
            try {
                log.info("定时任务开始执行 - 任务ID: {}", task.getTaskId());

                /*Object bean = applicationContext.getBean(task.getBeanName());
                Method method = bean.getClass().getMethod(task.getMethodName(), String.class);
                method.invoke(bean, task);*/

//                gatherFinanceDateService.beginCornFinanceTask(task);

                // 替换原有本地方法调用为远程接口调用
                String baseUrl = "http://192.0.2.200:8787";
                String apiPath = task.getParams(); // 从数据库获取接口路径（rule/ceshi 或 rule/test）
                String fullUrl = baseUrl + "/" + apiPath;

                log.debug("发起HTTP请求 - URL: {}", fullUrl);
                // 发起HTTP请求（GET）
                String result = restTemplate.getForObject(fullUrl, String.class);

                long duration = System.currentTimeMillis() - startTime;
                log.info("定时任务执行成功 - 任务ID: {}, 耗时: {} ms", task.getTaskId(), duration);

                // 记录执行日志
                logTaskExecution(task.getTaskId(), true, "执行成功");
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                log.error("定时任务执行失败 - 任务ID: {}, 耗时: {} ms, 错误信息: {}",
                        task.getTaskId(), duration, e.getMessage(), e);

                // 记录执行日志
                logTaskExecution(task.getTaskId(), false, "执行失败: " + e.getMessage());
            }
        };

        try {
            ScheduledFuture<?> future = taskScheduler.schedule(
                    runnable,
                    new CronTrigger(task.getCornExpression())
            );

            taskFutureMap.put(task.getTaskId(), future);
            log.info("定时任务调度成功 - 任务ID: {}", task.getTaskId());
        } catch (Exception e) {
            log.error("定时任务调度失败 - 任务ID: {}, 错误信息: {}", task.getTaskId(), e.getMessage(), e);
        }
    }

    public Date getNextExecutionTime(TblUruleTask task) {
        if (task.getStatus() == 0 || !taskFutureMap.containsKey(task.getTaskId())) {
            return null;
        }

        CronTrigger trigger = new CronTrigger(task.getCornExpression());
        return trigger.nextExecutionTime(
                new SimpleTriggerContext(
                        null, null, null
                )
        );
    }

    /**
     * 记录任务执行日志
     *
     * @param taskId 任务ID
     * @param success 是否成功
     * @param message 日志消息
     */
    private void logTaskExecution(String taskId, boolean success, String message) {
        try {
            if (success) {
                log.info("任务执行日志记录 - 任务ID: {}, 状态: 成功, 消息: {}", taskId, message);
            } else {
                log.warn("任务执行日志记录 - 任务ID: {}, 状态: 失败, 消息: {}", taskId, message);
            }
            // TODO: 可以在这里添加数据库日志记录逻辑
        } catch (Exception e) {
            log.error("记录任务执行日志失败 - 任务ID: {}, 错误信息: {}", taskId, e.getMessage(), e);
        }
    }
}