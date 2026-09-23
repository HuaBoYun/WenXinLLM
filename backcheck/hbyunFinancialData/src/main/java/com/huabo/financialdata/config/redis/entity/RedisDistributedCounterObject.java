package com.huabo.financialdata.config.redis.entity;

/**
 * 分布式计数器对象
 *
 * @author lee
 * @version 1.0.0
 */
public class RedisDistributedCounterObject extends RedisKeyBindingObject {

    private static final String DCOUNTER_PREFIX = preFixStart + "dcounter:";

    public RedisDistributedCounterObject(String name) {
        super(name, DCOUNTER_PREFIX);
    }
}
