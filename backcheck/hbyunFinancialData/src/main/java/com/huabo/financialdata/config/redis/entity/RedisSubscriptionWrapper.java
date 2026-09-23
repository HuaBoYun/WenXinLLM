package com.huabo.financialdata.config.redis.entity;


import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.Subscription;

/**
 * RedisSubscriptionWrapper
 *
 * @author lee
 * @version 1.0.0
 */

public class RedisSubscriptionWrapper {
    private RedisConnection connection;

    public RedisSubscriptionWrapper() {
    }

    public RedisSubscriptionWrapper(RedisConnection connection) {
        wrapSubscribedConnection(connection);
    }

    public void wrapSubscribedConnection(RedisConnection connection) {
        this.connection = connection;
    }

    public Subscription getSubscription() {
        return this.connection != null ? this.connection.getSubscription() : null;
    }
}
