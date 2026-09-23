package com.huabo.finance.thread;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import com.huabo.finance.entity.TblSysScheduledTask;
import com.huabo.finance.service.GatherFinanceDateService;
import com.huabo.finance.vr.TblSysScheduledTaskVr;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ScheduledTaskOperateService {

    private final ApplicationContext applicationContext;
    
    @Resource
    private GatherFinanceDateService gatherFinanceDateService;
    
    
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
    
    public void scheduleTask(TblSysScheduledTask task) {
    	cancelTask(task.getTaskId());
        Runnable runnable = () -> {
            try {
                /*Object bean = applicationContext.getBean(task.getBeanName());
                Method method = bean.getClass().getMethod(task.getMethodName(), String.class);
                method.invoke(bean, task);*/
            	
            	gatherFinanceDateService.beginCornFinanceTask(task);
                
                // 记录执行日志
                logTaskExecution(task.getTaskId(), true, "执行成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        
        ScheduledFuture<?> future = taskScheduler.schedule(
            runnable,
            new CronTrigger(task.getCornExpression())
        );
        
        taskFutureMap.put(task.getTaskId(), future);
    }
    
   public Date getNextExecutionTime(TblSysScheduledTaskVr task) {
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
    
    private void logTaskExecution(String taskId, boolean success, String message) {
        // 实现任务执行日志记录
    }
}
