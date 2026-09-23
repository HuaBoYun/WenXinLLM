package com.huabo.finance.thread;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppShutdownConfig {

	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
    @PreDestroy
    public void destroy() {
        taskExecutor.shutdown();
    }
    
}
