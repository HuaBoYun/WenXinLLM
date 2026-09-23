package com.hbfk.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisConnectionKeeper {

	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // 每1分钟执行一次心跳
    @Scheduled(fixedRate = 60000)
    public void keepAlive() {
    	try {
            redisTemplate.execute(RedisConnection::ping);
        } catch (Exception e) {
            resetRedisConnection();
        }
    }
    
    private void resetRedisConnection() {
        if (redisTemplate.getConnectionFactory() instanceof LettuceConnectionFactory) {
            ((LettuceConnectionFactory) redisTemplate.getConnectionFactory()).resetConnection();
        }
    }
}
