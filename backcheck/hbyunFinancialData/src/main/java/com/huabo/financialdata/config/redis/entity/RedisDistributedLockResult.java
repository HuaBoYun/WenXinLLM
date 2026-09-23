package com.huabo.financialdata.config.redis.entity;

/**
 * Redis 分布式锁处理返回对象
 *
 * @author lee
 * @version 1.0.0
 */
public class RedisDistributedLockResult {
    /**
     * 锁的名称
     */
    private String lockName;
    /**
     * 加锁的结果
     */
    private boolean success;
    /**
     * 锁的密钥
     */
    private String identifier;
    /**
     * 分布式锁的信息对象
     */
    private RedisDistributedLockObject dlock;

    /**
     * 构造函数
     *
     * @param lockName   锁名称
     * @param success    加锁结果
     * @param identifier 锁密钥
     * @param dlock      加锁信息
     */
    public RedisDistributedLockResult(String lockName, boolean success, String identifier, RedisDistributedLockObject dlock) {
        this.lockName = lockName;
        this.success = success;
        this.identifier = identifier;
        this.dlock = dlock;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getLockName() {
        return lockName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public RedisDistributedLockObject getDlock() {
        return dlock;
    }

    @Override
    public String toString() {
        return "RedisDistributedLockResult [success=" + success + "]";
    }
}
