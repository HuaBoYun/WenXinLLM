package com.huabo.financialdata.config.redis.service;


import com.huabo.financialdata.config.redis.entity.RedisQueueObject;

import java.util.concurrent.ExecutorService;


/**
 * Redis队列服务接口
 *
 * @author lee
 * @version 1.0.0
 */
public interface IRedisQueueService {

    long llen(RedisQueueObject queue);

    long push(RedisQueueObject queue, Object msg);

    <T> T pop(RedisQueueObject queue, Class<T> tpl);

    ExecutorService listen(RedisQueueObject queue, Class tpl, int concurrent, IRedisMessageListener l);

}
