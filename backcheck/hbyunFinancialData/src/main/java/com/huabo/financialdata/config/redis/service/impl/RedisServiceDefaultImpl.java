package com.huabo.financialdata.config.redis.service.impl;


import com.huabo.financialdata.config.constant.HbConstants;
import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.config.log.LogFactory;
import com.huabo.financialdata.config.redis.entity.*;
import com.huabo.financialdata.config.redis.serialize.ISerializer;
import com.huabo.financialdata.config.redis.serialize.JSONSerializer;
import com.huabo.financialdata.config.redis.serialize.SerializeException;
import com.huabo.financialdata.config.redis.service.IRedisMessageListener;
import com.huabo.financialdata.config.redis.service.IRedisService;
import com.huabo.financialdata.config.redis.tracer.ProcessContext;
import com.huabo.financialdata.config.redis.tracer.ThreadLocalProcessTracer;
import com.huabo.financialdata.config.redis.tracer.TracerRootAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.Pool;
import redis.clients.jedis.util.SafeEncoder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Redis功能实现类
 *
 * @author lee
 * @version 1.0.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisServiceDefaultImpl implements IRedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceDefaultImpl.class);
    /**
     * 服务名
     */
    private String name;
    /**
     * redisTemplate对象
     */
    private RedisTemplate redisTemplate;
    /**
     * jedis边接池
     */
    @SuppressWarnings("unused")
    private Pool<Jedis> pool;
    /**
     * pool最大值
     */
    @SuppressWarnings("unused")
    private int poolMaxTotal = -1;

    /**
     * 预列化对象，默认值JSONSerializer
     */
    private ISerializer serialize;
    /**
     * 开启Redis的命名空间
     */
    private boolean prefixEnabled = false;

    /**
     * RedisKey的命名空间，当prefixEnabled 为true,且当前命名空间不为空时将开启命名空间处理
     */
    private RedisKeyNamespace keyNamespace = null;

    /**
     * 存储字符集
     */
    private String charset;

    /**
     * 构造函数
     */
    public RedisServiceDefaultImpl(String name, RedisTemplate redisTemplate, boolean prefixEnabled) {
        this(name, redisTemplate, null, prefixEnabled);
    }

    /**
     * 构造函数
     */
    public RedisServiceDefaultImpl(String name, RedisTemplate redisTemplate, RedisKeyNamespace keyNamespace, boolean prefixEnabled) {
        this(name, redisTemplate, keyNamespace, prefixEnabled, null);

    }

    public RedisServiceDefaultImpl(String name, RedisTemplate redisTemplate, RedisKeyNamespace keyNamespace, boolean prefixEnabled, String charset) {
        this(name, redisTemplate, keyNamespace, prefixEnabled, charset, null);
    }

    public RedisServiceDefaultImpl(String name, RedisTemplate redisTemplate, RedisKeyNamespace keyNamespace, boolean prefixEnabled, String charset, ISerializer serialize) {
        this.setName(name);
        this.redisTemplate = redisTemplate;
        this.keyNamespace = keyNamespace;
        this.prefixEnabled = prefixEnabled;
        this.setCharset(charset);
        this.setRedisSerializer(serialize);
        this.initPool4Metrics();

    }

    @Override
    public void set(String key, Object value) {
        set(key, value, 0L);
    }

    @Override
    public void set(final String key, final Object value, final long expire) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    if (value != null) {
                        byte[] keyBytes = str2Bytes(cache.getRedisKey());
                        byte[] val = serialize.serialize(value);
                        if (expire > 0) {
                            redisConnection.setEx(keyBytes, expire, val);
                        } else {
                            redisConnection.set(keyBytes, val);
                        }
                    }
                    return 1L;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis set 失败", new BizException("redis set exception", e));
        }
    }

    @Override
    public boolean setNx(String key, Object value, long expire) {
        boolean ret = false;
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            ret = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    String tmpKey = cache.getRedisKey();
                    boolean flag = connection.setNX(str2Bytes(tmpKey), serialize.serialize(value));
                    if (flag) {
                        connection.expire(str2Bytes(tmpKey), expire);
                    }
                    return flag;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis lock 失败", new BizException("redis lock exception", e));
        }
        return ret;
    }

    @Override
    public String get(final String key) {
        return get(key, String.class);
    }

    @Override
    public <T> T get(final String key, final Class<T> tpl) {
        RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        return doGet(cache.getRedisKey(), tpl);
    }

    @Override
    public <T> T getSso(final String key, final Class<T> tpl) {
        RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        return doGetSso(cache.getRedisKey(), tpl);
    }

    @Override
    public <T> List<T> get(final List<String> keys, final Class<T> tpl) {
        return doGet(keys, tpl);
    }

    @Override
    public <T> List<T> getList(final String key, final Class<T> tpl) {
        RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        return doGetList(cache.getRedisKey(), tpl);
    }

    @Override
    public <T> T getSet(final String key, final Object value, final long expire, final Class<T> tpl) {
        return doGetSet(key, value, expire, tpl);
    }

    private <T> T doGetSet(final String key, final Object value, final long expire, final Class<T> tpl) {
        T resutl = null;
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            resutl = (T) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] val = null;
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    try {
                        val = serialize.serialize(value);
                    } catch (SerializeException e) {
                        throw new RedisServiceException(e);
                    }
                    if (val != null) {
                        byte[] ret = redisConnection.getSet(keyBytes, val);
                        if (expire > 0) {
                            redisConnection.expire(keyBytes, expire);
                        }
                        return serialize.deserialize(ret, tpl);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis getSet 失败", new BizException("redis getSet exception", e));
        }
        return resutl;
    }

    public byte[] getSet(final String key, final Object value, final long expire) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public byte[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] val = null;
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    try {
                        val = serialize.serialize(value);
                    } catch (SerializeException e) {
                        throw new RedisServiceException(e);
                    }
                    if (val != null) {
                        byte[] ret = redisConnection.getSet(keyBytes, val);
                        if (expire > 0) {
                            redisConnection.expire(keyBytes, expire);
                        }
                        return ret;
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis getSet 失败", new BizException("redis getSet exception", e));
        }
        return null;
    }

    /**
     * @param
     * @return
     * @author wf.shu
     * @version 192.0.2.200
     * @since 2019-06-14
     */
    private <T> List<T> doGet(final List<String> keys, final Class<T> tpl) {
        List<T> ret = null;
        try {
            ret = (List<T>) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    List list = new LinkedList();
                    for (String key : keys) {
                        byte[] value = redisConnection.get(str2Bytes(key));
                        if (null == value) {
                            continue;
                        }
                        list.add(serialize.deserialize(value, tpl));
                    }
                    return list;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis get 失败", e);
        }
        return ret;
    }

    private <T> T doGetSso(final String key, final Class<T> tpl) {
        T ret = null;
        try {
            ret = (T) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] value = redisConnection.get(str2Bytes(key));
                    return serialize.deserialize(value, tpl);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis get 失败", key);
        }
        return ret;
    }

    private <T> T doGet(final String key, final Class<T> tpl) {
        T ret = null;
        try {
            ret = (T) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] value = redisConnection.get(str2Bytes(key));
                    return serialize.deserialize(value, tpl);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis get 失败", new BizException("redis get exception", e));
        }
        return ret;
    }

    private <T> List<T> doGetList(final String key, final Class<T> tpl) {
        List<T> ret = null;
        try {
            ret = (List<T>) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] value = redisConnection.get(str2Bytes(key));
                    if (value != null) {
                        try {
                            return serialize.deserializeList(value, tpl);
                        } catch (SerializeException e) {
                            throw new RedisServiceException(e);
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis getList 失败", new BizException("redis getList exception", e));
        }
        return ret;
    }

    @Override
    public String[] keys(final String pattern) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(pattern);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        String[] ret = null;
        try {
            ret = (String[]) redisTemplate.execute(new RedisCallback() {
                @Override
                public String[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    Set<byte[]> keysSet = redisConnection.keys(str2Bytes(cache.getRedisKey()));
                    String[] ret = new String[keysSet.size()];
                    int idx = 0;
                    try {
                        for (byte[] k : keysSet) {
                            ret[idx++] = new String(k, getCharset());
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RedisServiceException(e);
                    }

                    return ret;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis keys 失败", new BizException("redis keys exception", e));
        }
        return ret;
    }

    @Override
    public boolean exists(final String key) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        boolean ret = false;
        try {
            ret = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.exists(str2Bytes(cache.getRedisKey()));
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis exists 失败", new BizException("redis exists exception", e));
        }
        return ret;
    }

    @Override
    public void delete(final String... keys) {
        for (String key : keys) {
            RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
            cache.setPrefixEnabled(this.isPrefixEnabled());
            cache.setKeyNamespace(this.keyNamespace);
            doDelete(cache.getRedisKey());
        }
    }

    private void doDelete(final String... keys) {
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    for (int i = 0; i < keys.length; i++) {
//                        long start = System.nanoTime();
                        connection.del(str2Bytes(keys[i]));
                    }
                    return 1L;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis delete 失败", new BizException("redis delete exception", e));
        }
    }

    @Override
    public void expire(final String key, final long expire) {
        RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        doExpire(cache.getRedisKey(), expire);
    }

    @Override
    public boolean expireKey(final String key, final long expire) {
        RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        return doExpire(cache.getRedisKey(), expire);
    }

    public boolean doExpire(final String key, final long expire) {
        boolean result = false;
        if (expire <= 0) {
            delete(key);
        }
        try {
            result = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.expire(str2Bytes(key), expire);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public long getCurrent(final RedisDistributedCounterObject dcounter) {
        dcounter.setPrefixEnabled(this.isPrefixEnabled());
        dcounter.setKeyNamespace(this.keyNamespace);
        String s = doGet(dcounter.getRedisKey(), String.class);

        if (s == null) {
            return 0L;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public long increase(final RedisDistributedCounterObject dcounter, final int val) {
        dcounter.setPrefixEnabled(this.isPrefixEnabled());
        dcounter.setKeyNamespace(this.keyNamespace);
        long ret = 0L;
        try {
            ret = (Long) redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.incrBy(str2Bytes(dcounter.getRedisKey()), val);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis increase 失败", new BizException("redis increase exception", e));
        }
        return ret;
    }

    @Override
    public long decrease(final RedisDistributedCounterObject dcounter, final int val) {
        dcounter.setPrefixEnabled(this.isPrefixEnabled());
        dcounter.setKeyNamespace(this.keyNamespace);
        long ret = 0L;
        try {
            ret = (Long) redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.decrBy(str2Bytes(dcounter.getRedisKey()), val);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis decrease 失败", new BizException("redis decrease exception", e));
        }
        return ret;
    }

    @Override
    public void delete(RedisDistributedCounterObject dcounter) {
        dcounter.setPrefixEnabled(this.isPrefixEnabled());
        dcounter.setKeyNamespace(this.keyNamespace);
        doDelete(dcounter.getRedisKey());
    }

    public boolean lock(final RedisDistributedLockObject dlock) {
        dlock.setPrefixEnabled(this.isPrefixEnabled());
        dlock.setKeyNamespace(this.keyNamespace);
        final String key = dlock.getRedisKey();
        boolean ret = false;

        try {
            ret = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                    boolean flag = connection.setNX(str2Bytes(key), str2Bytes(dlock.getIdentifier()));
//
//                    if (flag && dlock.getSelfReleaseExpired() > 0) {
//                        connection.expire(str2Bytes(key), dlock.getSelfReleaseExpired());
//                    }
//
//                    return flag;
                    Object obj = connection.execute("set", str2Bytes(key), str2Bytes(dlock.getIdentifier()), SafeEncoder.encode("NX"), SafeEncoder.encode("EX"), Protocol.toByteArray(dlock.getSelfReleaseExpired()));
                    return obj != null;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis lock 失败", new BizException("redis lock exception", e));
        }
        return ret;
    }

    public boolean lock(final RedisDistributedLockObject dlock, Long spinSeconds) {
        dlock.setPrefixEnabled(this.isPrefixEnabled());
        dlock.setKeyNamespace(this.keyNamespace);
        final String key = dlock.getRedisKey();
        boolean ret = false;
        try {
            ret = (Boolean) redisTemplate.execute((RedisCallback) connection -> {
                boolean flag = false;
                try {
                    flag = connection.setNX(str2Bytes(key), str2Bytes(dlock.getIdentifier()));

                    if (!flag) {
                        final long deadline = System.currentTimeMillis() + (spinSeconds * 1000);
                        for (; ; ) {
                            long timeout = deadline - System.currentTimeMillis();
                            if (flag || timeout <= 0L) {
                                break;
                            } else {
                                TimeUnit.MILLISECONDS.sleep(100);
                            }
                            flag = connection.setNX(str2Bytes(key), str2Bytes(dlock.getIdentifier()));
                        }
                    }

                    if (flag && dlock.getSelfReleaseExpired() > 0) {
                        connection.expire(str2Bytes(key), dlock.getSelfReleaseExpired());
                    }
                } catch (Exception e) {
                    logger.error("Redis lock 失败", new BizException("redis lock exception", e));
                }
                return flag;
            });
        } catch (Exception e) {
            logger.error("Redis lock 失败", new BizException("redis lock exception", e));
        }
        return ret;
    }

    public void unlock(RedisDistributedLockObject dlock) {
        dlock.setPrefixEnabled(this.isPrefixEnabled());
        dlock.setKeyNamespace(this.keyNamespace);
        doDelete(dlock.getRedisKey());
    }

    @Override
    public RedisDistributedLockResult acquireLock(RedisDistributedLockObject dlock) {
        boolean success = lock(dlock);
        return new RedisDistributedLockResult(dlock.getRedisKey(), success, dlock.getIdentifier(), dlock);
    }

    @Override
    public RedisDistributedLockResult acquireLock(RedisDistributedLockObject dlock, Long spinSeconds) {
        boolean success = lock(dlock, spinSeconds);
        return new RedisDistributedLockResult(dlock.getRedisKey(), success, dlock.getIdentifier(), dlock);
    }

    @Override
    public void unlock(final RedisDistributedLockResult result) {
        final RedisDistributedLockObject dlock = result.getDlock();
        dlock.setPrefixEnabled(this.isPrefixEnabled());
        dlock.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    if (result.isSuccess()) {
                        final String lockName = result.getLockName();
                        final byte[] lockNameBytes = str2Bytes(lockName);
                        String identifierInRedis = null;
                        try {
                            identifierInRedis = new String(connection.get(lockNameBytes), getCharset());
                        } catch (UnsupportedEncodingException e) {
                            throw new RedisServiceException(e);
                        }
                        if (result.getIdentifier().equals(identifierInRedis)) {
                            connection.del(lockNameBytes);
                        }
                    }
                    return 0L;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis unlock 失败", new BizException("redis unlock exception", e));
        }
    }

    @Override
    public boolean publish(final RedisTopicObject topic, final Object data) {
        topic.setPrefixEnabled(this.isPrefixEnabled());
        topic.setKeyNamespace(this.keyNamespace);
        boolean ret = false;

        try {
            ret = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    String key = topic.getRedisKey();
                    byte[] value = null;
                    try {
                        value = serialize.serialize(data);
                    } catch (SerializeException e) {
                        throw new RedisServiceException(e);
                    }

                    if (value != null) {
                        connection.publish(str2Bytes(key), value);
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis publish 失败", new BizException("redis publish exception", e));
        }
        return ret;
    }

    /**
     * Redis Topic订阅实现
     * <pre>
     *     此处需要定义消息监听处理实现类，即MessageListener处理类。同时启动一个线程监听消息
     * </pre>
     *
     * @param topic
     * @param subscriptionWrapper
     * @param messageListener
     * @return
     * @author wf.shu
     * @version 192.0.2.200
     * @since 2019-04-15
     */
    @Override
    public Thread subscribe(final RedisTopicObject topic, final RedisSubscriptionWrapper subscriptionWrapper, final MessageListener messageListener) {
        topic.setPrefixEnabled(this.isPrefixEnabled());
        topic.setKeyNamespace(this.keyNamespace);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    redisTemplate.execute(new RedisCallback() {
                        @Override
                        public Long doInRedis(RedisConnection connection) throws DataAccessException {
                            if (subscriptionWrapper != null) {
                                subscriptionWrapper.wrapSubscribedConnection(connection);
                            }
                            connection.subscribe(messageListener, str2Bytes(topic.getRedisKey()));
                            return 0L;
                        }
                    });
                } catch (Exception e) {
                    LogFactory.error("Redis subscribe 失败", new BizException("redis subscribe exception", e));
                }
            }
        });
        thread.start();
        return thread;
    }

    /**
     * 取消订阅
     *
     * @param topic
     * @param subscriptionWrapper
     * @return
     * @author wf.shu
     * @version 192.0.2.200
     * @since 2019-04-15
     */
    @Override
    public void unsubscribe(final RedisTopicObject topic, final RedisSubscriptionWrapper subscriptionWrapper) {
        topic.setPrefixEnabled(this.isPrefixEnabled());
        topic.setKeyNamespace(this.keyNamespace);
        if (subscriptionWrapper == null || subscriptionWrapper.getSubscription() == null) {
            return;
        }
        if (subscriptionWrapper.getSubscription().isAlive()) {
            subscriptionWrapper.getSubscription().unsubscribe(str2Bytes(topic.getRedisKey()));
        } else {
            LogFactory.info("subscription of queue [" + topic.getName() + "] is closed, check your codes whether there is a duplicated un-subscription of queue [" + topic.getName() + "]!");
        }
    }

    public long llen(final RedisQueueObject queue) {
        queue.setPrefixEnabled(this.isPrefixEnabled());
        queue.setKeyNamespace(this.keyNamespace);
        long ret = -1L;

        try {
            ret = (Long) redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    String key = queue.getRedisKey();
                    return connection.lLen(str2Bytes(key));
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis llen 失败", new BizException("redis llen exception", e));
        }
        return ret;
    }

    public long push(final RedisQueueObject queue, final Object msg) {
        queue.setPrefixEnabled(this.isPrefixEnabled());
        queue.setKeyNamespace(this.keyNamespace);
        long ret = -1L;
        final RedisInstructionData.Queue queueData = new RedisInstructionData.Queue(queue, ret);
        try {
            ret = (Long) redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    String key = queue.getRedisKey();
                    byte[] msgBytes = null;
                    byte[] keyBytes = str2Bytes(key);
                    try {
                        msgBytes = serialize.serialize(msg);
                    } catch (SerializeException e) {
                        throw new RedisServiceException(e);
                    }

                    if (msgBytes != null) {
                        long remaining = connection.lPush(keyBytes, msgBytes);
                        queueData.setRemaining(remaining);
                        return remaining;
                    }

                    return -1L;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis push 失败", new BizException("redis push exception ", e));
        }
        return ret;
    }

    public <T> T pop(final RedisQueueObject queue, final Class<T> tpl) {
        queue.setPrefixEnabled(this.isPrefixEnabled());
        queue.setKeyNamespace(this.keyNamespace);
        T ret = null;
        try {
            ret = this.doPop(queue, tpl);
        } catch (Exception e) {
            LogFactory.error("Redis pop 失败", new BizException("redis pop exception", e));
        }
        return ret;
    }


    public ExecutorService listen(final RedisQueueObject queue, final Class tpl, int concurrent, final IRedisMessageListener l) {
        queue.setPrefixEnabled(this.isPrefixEnabled());
        queue.setKeyNamespace(this.keyNamespace);
        final int MIN = 1;
        final int MAX = 50;

        concurrent = concurrent < MIN ? MIN : concurrent;
        if (concurrent > MAX) {
            logger.warn("add [" + concurrent + "] listeners to redis queue [" + queue.getName() + "]");
        }

        ExecutorService executor = Executors.newFixedThreadPool(concurrent);
        final long PAUSE = 200;
        for (int i = 0; i < concurrent; i++) {
            executor.execute(new Thread() {
                @Override
                public void run() {
                    final String listenerId = getShortName(l.getClass().getName()) + "[" + this.getId() + "]";
                    while (true) {
                        try {
                            Object msg = null;
                            try {
                                msg = doPop(queue, tpl);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                                Thread.sleep(5000);
                            }
                            if (msg == null) {
                                Thread.sleep(PAUSE);
                                continue;
                            }
                            ThreadLocalProcessTracer tracer = ThreadLocalProcessTracer.get();
                            tracer.setRootAttribute(TracerRootAttributes.TYPE, "REDIS-MQ");
                            tracer.setRootAttribute(TracerRootAttributes.MQ_TARGET_NAME, "QUEUE");
                            tracer.setRootAttribute(TracerRootAttributes.MQ_TARGET_NAME, queue.getName());
                            tracer.setRootAttribute(TracerRootAttributes.MQ_LISTENER, listenerId);
                            tracer.beginTrace();
                            ProcessContext processContext = tracer.join(ProcessContext.Type.RedisMessageConsumer, "process-message");
                            try {
                                l.onMessage(queue, msg);
                            } catch (Exception e) {
                                tracer.setRootAttribute(TracerRootAttributes.ERROR, e.getMessage());
                                tracer.setError(e);
                                throw e;
                            } finally {
                                processContext.stop();
                                tracer.stopTrace();
                                tracer.log("REDIS LISTEN");
                                ThreadLocalProcessTracer.clean();
                            }
                        } catch (Exception e) {
                            logger.error("listening redis queue [" + queue.getName() + "], error: " + e.getMessage(), e);
                        }
                    }
                }
            });
        }
        return executor;
    }


    public void setRedisSerializer(ISerializer redisSerializer) {
        if (redisSerializer == null) {
            redisSerializer = new JSONSerializer();
        }
        this.serialize = redisSerializer;
    }

    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("invalid redisTemplate");
        }

        this.redisTemplate = redisTemplate;
    }

    private String getShortName(String longName) {
        for (int i = 0; i < longName.length(); i++) {
            char c = longName.charAt(i);
            if (Character.isUpperCase(c)) {
                return longName.substring(i);
            }
        }

        return longName;
    }

    private <T> T doPop(final RedisQueueObject queue, final Class<T> tpl) throws Exception {
        final String key = queue.getRedisKey();
        T ret = (T) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] value = null;
                try {
                    if (queue.getQueueType() == RedisQueueObject.FILO) {
                        value = redisConnection.lPop(str2Bytes(key));
                    } else {
                        value = redisConnection.rPop(str2Bytes(key));
                    }
                } catch (Exception e) {
                    if (e.getCause() instanceof SocketTimeoutException) {
                        return null;
                    }
                    if (e.getMessage().indexOf("SocketTimeoutException") >= 0) {
                        return null;
                    }
                    logger.error(e.getMessage(), e);
                }
                if (value != null) {
                    try {
                        T realValue = serialize.deserialize(value, tpl);
                        return realValue;
                    } catch (SerializeException e) {
                        throw new RedisServiceException(e);
                    }
                }
                return null;
            }
        });

        return ret;
    }


    private void initPool4Metrics() {
        try {
            JedisConnectionFactory cf = (JedisConnectionFactory) (this.getRedisTemplate().getConnectionFactory());
            Field poolField = JedisConnectionFactory.class.getDeclaredField("pool");
            poolField.setAccessible(true);
            poolMaxTotal = cf.getPoolConfig().getMaxTotal();
            this.pool = (Pool<Jedis>) poolField.get(cf);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private byte[] str2Bytes(String key) {
        try {
            return key.getBytes(this.getCharset());
        } catch (UnsupportedEncodingException e) {
            LogFactory.error("字符转Byter失败", new BizException("UnsupportedEncodingException", e));
        }
        return new byte[0];
    }

    //ADD BY WF.SHU @2019-11-28-----START

    @Override
    public void hSet(final String key, final String field, final Object value) {
        this.hSet(key, field, value, 0L);
    }

    @Override
    public void hSet(final String key, final String field, final Object value, final long expire) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    byte[] fieldBytes = str2Bytes(field);
                    byte[] val = serialize.serialize(value);
                    boolean result = redisConnection.hSet(keyBytes, fieldBytes, val);
                    if (expire > 0 && result) {
                        redisConnection.expire(keyBytes, expire);
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            LogFactory.error("redis hSet exception", new BizException("redis hSet exception", e));
        }
    }

    @Override
    public void hMset(final String key, final Map<String, Object> values) {
        this.hMset(key, values, 0L);
    }

    @Override
    public void hMset(final String key, final Map<String, Object> values, final long expire) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    if (null != values) {
                        Map<byte[], byte[]> hashes = new HashMap<byte[], byte[]>(values.size());
                        for (String keyTmp : values.keySet()) {
                            byte[] keyTmpBytes = str2Bytes(keyTmp);
                            byte[] val = serialize.serialize(values.get(keyTmp));
                            hashes.put(keyTmpBytes, val);
                        }
                        byte[] keyBytes = str2Bytes(cache.getRedisKey());
                        redisConnection.hMSet(keyBytes, hashes);
                        if (expire > 0) {
                            redisConnection.expire(keyBytes, expire);
                        }
                    }
                    return 1L;
                }
            });
        } catch (Exception e) {
            LogFactory.error("redis hMset exception", new BizException("redis hSet exception", e));
        }
    }

    @Override
    public boolean hSetNx(String key, final String fieldKey, Object value) {
        return this.hSetNx(key, fieldKey, value, 0L);
    }

    @Override
    public boolean hSetNx(String key, final String fieldKey, Object value, long expire) {
        boolean ret = false;
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            ret = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    byte[] field = str2Bytes(fieldKey);
                    byte[] val = serialize.serialize(value);
                    boolean flag = connection.hSetNX(keyBytes, field, val);
                    if (flag && expire > 0) {
                        connection.expire(keyBytes, expire);
                    }
                    return flag;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis lock 失败", new BizException("redis hSetNx exception", e));
        }
        return ret;
    }

    @Override
    public boolean hDel(String key, final String fieldKey) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    byte[] field = str2Bytes(fieldKey);
                    return connection.hDel(keyBytes, field);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis hDel 失败", new BizException("redis hDel exception", e));
            return false;
        }
        return true;
    }

    @Override
    public boolean hMDel(String key, final List<String> fieldKeys) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    byte[][] fieldBytesArray = (byte[][]) Array.newInstance(byte[].class, fieldKeys.size());
                    int i = 0;
                    for (String fieldKey : fieldKeys) {
                        byte[] fieldBytes = str2Bytes(fieldKey);
                        fieldBytesArray[i] = fieldBytes;
                        i++;
                    }
                    return connection.hDel(keyBytes, fieldBytesArray);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis hDel 失败", new BizException("redis hDel exception", e));
            return false;
        }
        return true;
    }

    @Override
    public <T> T hGet(String key, String field, Class<T> clazz) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        return doHGet(cache.getRedisKey(), field, clazz);
    }

    @Override
    public <T> List<T> hMGet(String key, List<String> fields, Class<T> clazz) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);

        return doHMGet(cache.getRedisKey(), fields, clazz);
    }

    @Override
    public <T> List<T> hMGet(String key, Class<T> clazz) {
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        return doHMGet(cache.getRedisKey(), clazz);
    }

    @Override
    public <T> List<T> pipelined(List<String> keys, Class<T> tpl) {
        List<T> ret = new ArrayList<>();
        try {
            List<byte[]> valueList = redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    //业务操作
                    for (String key : keys) {
                        byte[] lastKeyBytes = str2Bytes(key);
                        connection.get(lastKeyBytes);
                    }
                    return null;
                }

            }, null);

            for (byte[] obj : valueList) {
                if (obj == null) {
                    ret.add(null);
                    continue;
                }
                ret.add(serialize.deserialize(obj, tpl));
            }

        } catch (Exception e) {
            LogFactory.error("Redis pipelined 失败", new BizException("redis pipelined exception", e));
        }
        return ret;
    }

    @Override
    public boolean hExist(final String key, final String fieldKey) {
        boolean ret = false;
        final RedisCacheServiceImpl cache = new RedisCacheServiceImpl(key);
        cache.setPrefixEnabled(this.isPrefixEnabled());
        cache.setKeyNamespace(this.keyNamespace);
        try {
            ret = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyBytes = str2Bytes(cache.getRedisKey());
                    byte[] fieldBytes = str2Bytes(fieldKey);
                    return connection.hExists(keyBytes, fieldBytes);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis hExist 失败", new BizException("redis hExist exception", e));
        }
        return ret;
    }

    /**
     * @param
     * @return
     * @author wf.shu
     * @version 192.0.2.200
     * @since 2019-06-14
     */
    private <T> T doHGet(String key, String field, Class<T> clazz) {
        T ret = null;
        try {
            ret = (T) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    byte[] keyBytes = str2Bytes(key);
                    byte[] fieldBytes = str2Bytes(field);
                    byte[] value = redisConnection.hGet(keyBytes, fieldBytes);
                    return serialize.deserialize(value, clazz);
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis doHGet 失败", new BizException("redis doHGet exception", e));
        }
        return ret;
    }

    private <T> List<T> doHMGet(String key, List<String> fields, Class<T> clazz) {
        List<T> result = null;
        try {
            result = (List<T>) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    List<T> ret = new ArrayList<T>();
                    byte[] keyBytes = str2Bytes(key);
                    byte[][] fieldBytesArray = (byte[][]) Array.newInstance(byte[].class, fields.size());
                    int i = 0;
                    for (String field : fields) {
                        byte[] fieldBytes = str2Bytes(field);
                        fieldBytesArray[i] = fieldBytes;
                        i++;
                    }
                    List<byte[]> retBytes = redisConnection.hMGet(keyBytes, fieldBytesArray);
                    for (byte[] b : retBytes) {
                        if (null != b) {
                            ret.add(serialize.deserialize(b, clazz));
                        }
                    }
                    return ret;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis doHGet 失败", new BizException("redis doHGet exception", e));
        }
        return result;
    }

    private <T> List<T> doHMGet(String key, Class<T> clazz) {
        List<T> result = null;
        try {
            result = (List<T>) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    List<T> ret = new ArrayList<T>();
                    byte[] keyBytes = str2Bytes(key);
                    List<byte[]> retBytes = redisConnection.hVals(keyBytes);
                    if (null != retBytes) {
                        for (byte[] b : retBytes) {
                            if (null != b) {
                                ret.add(serialize.deserialize(b, clazz));
                            }
                        }
                    }
                    return ret;
                }
            });
        } catch (Exception e) {
            LogFactory.error("Redis doHGet 失败", new BizException("redis doHGet exception", e));
        }
        return result;
    }


    //ADD BY WF.SHU @2019-11-28-----END


    //===============================================  getter && setter  =========================================================

    /**
     * setter : set name
     */
    public void setName(String name) {
        if (name == null || !StringUtils.hasLength(name)) {
            name = "defaultRedisService";
        }
        this.name = name;
    }

    /**
     * getter : get name
     */
    public String getName() {
        return this.name;
    }

    public void setKeyNamespace(RedisKeyNamespace keyNamespace) {
        this.keyNamespace = keyNamespace;
    }

    public RedisKeyNamespace getKeyNamespace() {
        return this.keyNamespace;
    }

    public String getCharset() {
        if (null == this.charset) {
            this.charset = HbConstants.DEFAULT_CHARSET;
        }
        return this.charset;
    }

    public void setCharset(String charset) {
        if (null == charset) {
            this.charset = HbConstants.DEFAULT_CHARSET;
        } else {
            this.charset = charset;
        }
    }

    public boolean isPrefixEnabled() {
        return prefixEnabled;
    }

    public void setPrefixEnabled(boolean prefixEnabled) {
        this.prefixEnabled = prefixEnabled;
    }
}
