package com.huabo.financialdata.config.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author lee
 */
@Configuration
@EnableScheduling
public class RedisClientHeart {

    /**
     * 是否启用的自动注册
     */
    @Value("${huabo.redis.heart:false}")
    private boolean enter;

    @Resource
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/10 * * * * *")
    public void timer() {
        if (enter) {
            redisTemplate.opsForValue().get("heartbeat");
        }
    }

}
