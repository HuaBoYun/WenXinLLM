package com.huabo.financialdata.config.redis.service;


import com.huabo.financialdata.config.redis.entity.RedisKeyBindingObject;

/**
 * Redis队列监听接口
 *
 * @author lee
 * @version 1.0.0
 */
public interface IRedisMessageListener {

    /**
     * 监听消息
     *
     * @param source  源
     * @param message 消息
     */
    void onMessage(RedisKeyBindingObject source, Object message);
}
