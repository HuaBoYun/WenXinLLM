package com.huabo.finance.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

	@Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setThreadNamePrefix("GatherDate-"); // 线程名前缀
        executor.initialize();
        return executor;
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
