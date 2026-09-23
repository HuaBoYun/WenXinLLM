package com.huabo.financialdata.config.redis.service;


import com.huabo.financialdata.config.redis.entity.RedisDistributedLockObject;
import com.huabo.financialdata.config.redis.entity.RedisDistributedLockResult;

/**
 * @author lee
 * @version 1.0.0
 */
public interface IRedisDistributedLockService {

    /**
     * Redis分布式加锁
     * 加锁后，将自动以锁定的名称作为Key在Redis存一个随机UUID，作为解锁时的钥匙
     *
     * @param dlock 分布锁对象
     * @return
     * @since 2019年3月14日 下午2:57:17
     */
    RedisDistributedLockResult acquireLock(RedisDistributedLockObject dlock);

    /**
     * Redis分布式加锁
     * 获取锁失败后会再在指定时间内自旋获取锁
     *
     * @param dlock       锁对象
     * @param spinSeconds 自旋时间
     * @return
     */
    RedisDistributedLockResult acquireLock(RedisDistributedLockObject dlock, Long spinSeconds);

    /**
     * Redis分布式解锁
     * 如原未获得锁，调用该方法将不会起作用
     * 如已获得锁，调用该方法解锁时将要验证锁对应的随机key值，保证安全性。
     *
     * @param result
     */
    void unlock(RedisDistributedLockResult result);
}
