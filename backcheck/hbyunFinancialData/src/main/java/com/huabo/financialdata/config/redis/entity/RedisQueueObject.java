package com.huabo.financialdata.config.redis.entity;

import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.config.log.LogFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Redis队列对象类
 *
 * @author lee
 * @version 1.0.0
 */
public class RedisQueueObject extends RedisKeyBindingObject implements Cloneable {
    private int queueType = FIFO;

    public static final int FIFO = 0;

    public static final int FILO = 1;

    private static final String REDIS_QUEUE_PREFIX = preFixStart + "queue:";

    public RedisQueueObject(String name) {
        this(name, FIFO);
    }

    public RedisQueueObject(String name, int queueType) {
        super(name, REDIS_QUEUE_PREFIX);
        this.queueType = queueType;
    }

    public int getQueueType() {
        return queueType;
    }

    public void setQueueType(int queueType) {
        this.queueType = queueType;
    }

    @Override
    public RedisQueueObject clone() {
        RedisQueueObject ret = null;
        try {
            ret = (RedisQueueObject) super.clone();
        } catch (CloneNotSupportedException e) {
            LogFactory.error("RedisQueueObject Clone 失败", new BizException("CloneNotSupportedException", e));
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RedisQueueObject) {
            RedisQueueObject obj = (RedisQueueObject) o;
            return StringUtils.equals(obj.getName(), this.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
