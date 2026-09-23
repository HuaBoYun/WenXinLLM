package com.hbfk.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
public class ThreadPoolConfiguration {

	@Bean(name = "synExecutor")
	@Primary
	public ThreadPoolTaskExecutor taskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(5);
		// 设置最大线程数
		executor.setMaxPoolSize(10);
		// 设置队列容量
		executor.setQueueCapacity(5000);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("mission-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}
	
	@Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // 设置线程池大小
        scheduler.setThreadNamePrefix("scheduled-task-");  // 线程名前缀
        scheduler.setAwaitTerminationSeconds(60);  // 等待终止时间(秒)
        scheduler.setWaitForTasksToCompleteOnShutdown(true);  // 关闭时等待任务完成
        scheduler.setRemoveOnCancelPolicy(true);  // 取消后立即移除任务
        return scheduler;
    }
	
	@Bean
    public ExecutorService threadPool() {
        // 核心5线程，最大20线程，队列无限（实际控制并发在服务层）
        return new ThreadPoolExecutor(5, 20, 
                                    60L, TimeUnit.SECONDS,
                                    new SynchronousQueue<>());
    }
	
	@Bean
	public Semaphore concurrentSemaphore() {
		// 控制并发数为5的信号量
	    return new Semaphore(5);
	}

}
