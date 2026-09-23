package com.global.treasurer.config;

import com.hbfk.util.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法ID生成器配置
 */
@Configuration
public class SnowflakeIdConfig {
    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker();
    }
}
