package com.huabo.financialdata.config.redis.service.impl;


import com.huabo.financialdata.config.redis.entity.RedisKeyBindingObject;

/**
 * 通用缓存实现类
 *
 * @author lee
 * @version 1.0.0
 */

public class RedisCacheServiceImpl extends RedisKeyBindingObject {

    private static final String CACHE_PREFIX = preFixStart + "cache:";

    public RedisCacheServiceImpl(String name) {
        super(name, CACHE_PREFIX);
    }
}
