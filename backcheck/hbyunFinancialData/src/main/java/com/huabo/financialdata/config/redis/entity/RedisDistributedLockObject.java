package com.huabo.financialdata.config.redis.entity;

import java.util.UUID;

/**
 * 分布式锁对象
 *
 * @author lee
 * @version 1.0.0
 */
public class RedisDistributedLockObject extends RedisKeyBindingObject {
    /**
     * 过期时间值，单位秒
     */
    private long selfReleaseExpired;
    /**
     * 默认过期时间，默认值为0L
     */
    private static final long DEFAULT_EXPIRED = 0L;
    /**
     * 锁的Key值前缀
     */
    private static final String DLOCK_PREFIX = preFixStart + "dlock:";
    /**
     * 随机生成的锁的密钥
     */
    private final String identifier = UUID.randomUUID().toString();

    /**
     * 构造函数
     *
     * @param name 锁的名称
     */
    public RedisDistributedLockObject(String name) {
        this(name, DEFAULT_EXPIRED);
    }

    /**
     * 构造函数
     *
     * @param name    锁的名称
     * @param expired key的过期时间
     */
    public RedisDistributedLockObject(String name, long expired) {
        super(name, DLOCK_PREFIX);
        this.selfReleaseExpired = expired;
    }

    /**
     * 获取key的过期时间值，单位为秒
     *
     * @return selfReleaseExpired 过期时间值，单位秒
     */
    public long getSelfReleaseExpired() {
        return selfReleaseExpired;
    }

    /**
     * 设置Key的过期时间值，单位为秒
     *
     * @param selfReleaseExpired 过期时间值
     */
    public void setSelfReleaseExpired(long selfReleaseExpired) {
        this.selfReleaseExpired = selfReleaseExpired;
    }

    /**
     * 获取分布锁密钥<br/>
     * 该方法考虑安全性为protect，只允许同个包下访问
     *
     * @return
     */
    public String getIdentifier() {
        return identifier;
    }

}
