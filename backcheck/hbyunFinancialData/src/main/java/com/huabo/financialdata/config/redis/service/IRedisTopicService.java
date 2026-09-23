package com.huabo.financialdata.config.redis.service;


import com.huabo.financialdata.config.redis.entity.RedisSubscriptionWrapper;
import com.huabo.financialdata.config.redis.entity.RedisTopicObject;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Redis主题接口
 *
 * @author lee
 * @version 1.0.0
 */
public interface IRedisTopicService {

    boolean publish(RedisTopicObject topic, Object data);

    Thread subscribe(RedisTopicObject topic, RedisSubscriptionWrapper subscriptionWrapper, MessageListener messageListener);

    void unsubscribe(RedisTopicObject topic, RedisSubscriptionWrapper subscriptionWrapper);
}
