package com.huabo.financialdata.config.redis.service;


import com.huabo.financialdata.config.redis.entity.RedisDistributedCounterObject;

/**
 * @author lee
 * @version 1.0.0
 */
public interface IRedisDistributedCounterService {
    /**
     * 获取当前记数
     *
     * @param instance 计数对象
     * @return
     */
    long getCurrent(RedisDistributedCounterObject instance);

    /**
     * 每次以 变量 val增加缓存中的值，当Key不存时，默认以0+val存储
     *
     * @param dcounter 计数器对象
     * @param val      幅度值
     * @return
     */

    long increase(RedisDistributedCounterObject dcounter, int val);

    /**
     * 每次以 变量 val减缓存中的值，当Key不存时，默认以0-val存储
     *
     * @param dcounter 计数器对象
     * @param val      幅度值
     * @return
     */
    long decrease(RedisDistributedCounterObject dcounter, int val);

    /**
     * 删除当前计数器的缓存
     *
     * @param dcounter 计数器对象
     */
    void delete(RedisDistributedCounterObject dcounter);
}
