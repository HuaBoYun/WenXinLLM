package com.financial.sharing.config;

import com.financial.sharing.util.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法ID生成器配置类
 * 将SnowflakeIdWorker注册为Spring Bean，供全局使用
 * 
 * @author Financial Sharing System
 * @since 2026-01-27
 */
@Configuration
public class SnowflakeIdConfig {

    /**
     * 创建SnowflakeIdWorker Bean
     * workerId和datacenterId可以根据实际部署环境进行配置
     * 
     * @return SnowflakeIdWorker实例
     */
    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        // 使用固定的workerId=1和datacenterId=1
        // 在分布式环境中，应该为每个实例配置不同的workerId
        return new SnowflakeIdWorker(1, 1);
    }
}

