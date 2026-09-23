package com.huabo.financialdata.config.redis.service;

/**
 * Redis通用接口
 * 调用Redis服务时都通过该接口实现
 * 目前支持普通的set/get/计数/分布锁的实现，其他功能不提供支持
 *
 * @author lee
 * @version 1.0.0
 */
public interface IRedisService extends IRedisCacheService,
        IRedisDistributedCounterService, IRedisDistributedLockService, IRedisTopicService {
}
