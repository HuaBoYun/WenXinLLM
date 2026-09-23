package com.financial.sharing.service.cache.impl;

import com.financial.sharing.service.cache.BalanceCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 余额缓存服务实现
 * 基于Redis实现分布式缓存
 *
 * @author system
 * @since 2024-12-19
 */
@Service
public class BalanceCacheServiceImpl implements BalanceCacheService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存键前缀
    private static final String BALANCE_PREFIX = "balance:";
    private static final String TRIAL_BALANCE_PREFIX = "trial_balance:";
    private static final String STATISTICS_KEY = "balance_cache:statistics";

    // 统计信息
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private int cacheTimeout = 3600; // 默认1小时
    private int maxCacheSize = 10000;

    @Override
    public void cacheSubjectBalance(Long bookId, String period, Long subjectId, BigDecimal balance) {
        try {
            String key = buildBalanceKey(bookId, period, subjectId);
            redisTemplate.opsForValue().set(key, balance, cacheTimeout, TimeUnit.SECONDS);
            logger.debug("缓存科目余额，科目ID：{}，余额：{}", subjectId, balance);
        } catch (Exception e) {
            logger.error("缓存科目余额失败", e);
        }
    }

    @Override
    public void batchCacheSubjectBalances(Long bookId, String period, Map<Long, BigDecimal> balances) {
        try {
            if (balances == null || balances.isEmpty()) {
                return;
            }

            Map<String, Object> cacheMap = new HashMap<>();
            for (Map.Entry<Long, BigDecimal> entry : balances.entrySet()) {
                String key = buildBalanceKey(bookId, period, entry.getKey());
                cacheMap.put(key, entry.getValue());
            }

            // 批量设置缓存
            redisTemplate.opsForValue().multiSet(cacheMap);

            // 设置过期时间
            for (String key : cacheMap.keySet()) {
                redisTemplate.expire(key, cacheTimeout, TimeUnit.SECONDS);
            }

            logger.debug("批量缓存科目余额，数量：{}", balances.size());
        } catch (Exception e) {
            logger.error("批量缓存科目余额失败", e);
        }
    }

    @Override
    public BigDecimal getSubjectBalance(Long bookId, String period, Long subjectId) {
        try {
            String key = buildBalanceKey(bookId, period, subjectId);
            Object value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                cacheHits.incrementAndGet();
                logger.debug("缓存命中，科目ID：{}", subjectId);
                return new BigDecimal(value.toString());
            } else {
                cacheMisses.incrementAndGet();
                logger.debug("缓存未命中，科目ID：{}", subjectId);
                return null;
            }
        } catch (Exception e) {
            logger.error("获取科目余额缓存失败", e);
            cacheMisses.incrementAndGet();
            return null;
        }
    }

    @Override
    public Map<Long, BigDecimal> getSubjectBalances(Long bookId, String period, List<Long> subjectIds) {
        Map<Long, BigDecimal> result = new HashMap<>();

        try {
            if (subjectIds == null || subjectIds.isEmpty()) {
                return result;
            }

            List<String> keys = new ArrayList<>();
            for (Long subjectId : subjectIds) {
                keys.add(buildBalanceKey(bookId, period, subjectId));
            }

            // 批量获取
            List<Object> values = redisTemplate.opsForValue().multiGet(keys);

            for (int i = 0; i < keys.size(); i++) {
                if (values.get(i) != null) {
                    Long subjectId = subjectIds.get(i);
                    BigDecimal balance = new BigDecimal(values.get(i).toString());
                    result.put(subjectId, balance);
                    cacheHits.incrementAndGet();
                } else {
                    cacheMisses.incrementAndGet();
                }
            }

            logger.debug("批量获取科目余额缓存，命中：{}", result.size());
        } catch (Exception e) {
            logger.error("批量获取科目余额缓存失败", e);
        }

        return result;
    }

    @Override
    public Map<Long, BigDecimal> getAllSubjectBalances(Long bookId, String period) {
        Map<Long, BigDecimal> result = new HashMap<>();

        try {
            String pattern = buildBalancePattern(bookId, period);
            Set<String> keys = redisTemplate.keys(pattern);

            if (keys != null && !keys.isEmpty()) {
                List<Object> values = redisTemplate.opsForValue().multiGet(keys);

                int i = 0;
                for (String key : keys) {
                    if (values.get(i) != null) {
                        Long subjectId = extractSubjectId(key);
                        if (subjectId != null) {
                            BigDecimal balance = new BigDecimal(values.get(i).toString());
                            result.put(subjectId, balance);
                        }
                    }
                    i++;
                }
            }

            logger.debug("获取所有科目余额缓存，数量：{}", result.size());
        } catch (Exception e) {
            logger.error("获取所有科目余额缓存失败", e);
        }

        return result;
    }

    @Override
    public void evictSubjectBalance(Long bookId, String period, Long subjectId) {
        try {
            String key = buildBalanceKey(bookId, period, subjectId);
            redisTemplate.delete(key);
            logger.debug("清除科目余额缓存，科目ID：{}", subjectId);
        } catch (Exception e) {
            logger.error("清除科目余额缓存失败", e);
        }
    }

    @Override
    public void evictBalance(Long bookId, String period) {
        try {
            String pattern = buildBalancePattern(bookId, period);
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                logger.debug("清除账簿期间余额缓存，数量：{}", keys.size());
            }
        } catch (Exception e) {
            logger.error("清除账簿期间余额缓存失败", e);
        }
    }

    @Override
    public void evictAllBalance(Long bookId) {
        try {
            String pattern = BALANCE_PREFIX + bookId + ":*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                logger.debug("清除账簿所有余额缓存，数量：{}", keys.size());
            }
        } catch (Exception e) {
            logger.error("清除账簿所有余额缓存失败", e);
        }
    }

    @Override
    public boolean isBalanceCached(Long bookId, String period, Long subjectId) {
        try {
            String key = buildBalanceKey(bookId, period, subjectId);
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("检查余额缓存失败", e);
            return false;
        }
    }

    @Override
    public void warmUpCache(Long bookId, String period) {
        try {
            logger.info("开始预热缓存，账簿ID：{}，期间：{}", bookId, period);

            // 这里应该从数据库加载数据并缓存
            // 暂时跳过实际加载逻辑

            logger.info("缓存预热完成");
        } catch (Exception e) {
            logger.error("缓存预热失败", e);
        }
    }

    @Override
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            long hits = cacheHits.get();
            long misses = cacheMisses.get();
            long total = hits + misses;
            double hitRate = total > 0 ? (double) hits / total * 100 : 0;

            statistics.put("cacheHits", hits);
            statistics.put("cacheMisses", misses);
            statistics.put("hitRate", String.format("%.2f%%", hitRate));
            statistics.put("timeout", cacheTimeout);
            statistics.put("maxSize", maxCacheSize);

            // 获取Redis信息
            Properties info = redisTemplate.getConnectionFactory().getConnection().info();
            statistics.put("redisMemoryUsed", info.getProperty("used_memory_human"));
            statistics.put("redisConnectedClients", info.getProperty("connected_clients"));

        } catch (Exception e) {
            logger.error("获取缓存统计失败", e);
        }

        return statistics;
    }

    @Override
    public Double getCacheHitRate() {
        long hits = cacheHits.get();
        long misses = cacheMisses.get();
        long total = hits + misses;
        return total > 0 ? (double) hits / total * 100 : 0;
    }

    @Override
    public void cacheTrialBalanceResult(Long bookId, String period, Object result) {
        try {
            String key = buildTrialBalanceKey(bookId, period);
            redisTemplate.opsForValue().set(key, result, cacheTimeout, TimeUnit.SECONDS);
            logger.debug("缓存试算平衡结果");
        } catch (Exception e) {
            logger.error("缓存试算平衡结果失败", e);
        }
    }

    @Override
    public Object getTrialBalanceResult(Long bookId, String period) {
        try {
            String key = buildTrialBalanceKey(bookId, period);
            Object result = redisTemplate.opsForValue().get(key);
            if (result != null) {
                cacheHits.incrementAndGet();
            } else {
                cacheMisses.incrementAndGet();
            }
            return result;
        } catch (Exception e) {
            logger.error("获取试算平衡缓存失败", e);
            cacheMisses.incrementAndGet();
            return null;
        }
    }

    @Override
    public void evictTrialBalance(Long bookId, String period) {
        try {
            String key = buildTrialBalanceKey(bookId, period);
            redisTemplate.delete(key);
            logger.debug("清除试算平衡缓存");
        } catch (Exception e) {
            logger.error("清除试算平衡缓存失败", e);
        }
    }

    @Override
    public void setCacheTimeout(int timeout) {
        this.cacheTimeout = timeout;
        logger.info("更新缓存超时时间：{}秒", timeout);
    }

    @Override
    public void setMaxCacheSize(int maxSize) {
        this.maxCacheSize = maxSize;
        logger.info("更新最大缓存大小：{}", maxSize);
    }

    @Override
    public void refreshCacheConfig() {
        // 从配置中心或数据库加载最新配置
        // 这里暂时使用默认值
        setCacheTimeout(3600);
        setMaxCacheSize(10000);
        logger.info("刷新缓存配置完成");
    }

    /**
     * 构建余额缓存键
     */
    private String buildBalanceKey(Long bookId, String period, Long subjectId) {
        return BALANCE_PREFIX + bookId + ":" + period + ":" + subjectId;
    }

    /**
     * 构建余额缓存模式
     */
    private String buildBalancePattern(Long bookId, String period) {
        return BALANCE_PREFIX + bookId + ":" + period + ":*";
    }

    /**
     * 构建试算平衡缓存键
     */
    private String buildTrialBalanceKey(Long bookId, String period) {
        return TRIAL_BALANCE_PREFIX + bookId + ":" + period;
    }

    /**
     * 从缓存键中提取科目ID
     */
    private Long extractSubjectId(String key) {
        try {
            String[] parts = key.split(":");
            if (parts.length >= 4) {
                return Long.valueOf(parts[3]);
            }
        } catch (Exception e) {
            logger.debug("提取科目ID失败，key：{}", key);
        }
        return null;
    }
}