package com.huabo.financialdata.config.redis.entity;

/**
 * Redis主题对象
 *
 * @author lee
 * @version 1.0.0
 */
public class RedisTopicObject extends RedisKeyBindingObject {

    private static final String REDIS_TOPIC_PREFIX = preFixStart + "topic:";

    public RedisTopicObject(String name) {
        super(name, REDIS_TOPIC_PREFIX);
    }

}

