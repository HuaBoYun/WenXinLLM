package com.financial.sharing.service.cache;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 余额缓存服务接口
 * 用于缓存科目余额数据，提高查询性能
 *
 * @author system
 * @since 2024-12-19
 */
public interface BalanceCacheService {

    /**
     * 缓存科目余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectId 科目ID
     * @param balance 余额
     */
    void cacheSubjectBalance(Long bookId, String period, Long subjectId, BigDecimal balance);

    /**
     * 批量缓存科目余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param balances 科目余额映射
     */
    void batchCacheSubjectBalances(Long bookId, String period, Map<Long, BigDecimal> balances);

    /**
     * 获取科目余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectId 科目ID
     * @return 余额
     */
    BigDecimal getSubjectBalance(Long bookId, String period, Long subjectId);

    /**
     * 获取多个科目的余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectIds 科目ID列表
     * @return 科目余额映射
     */
    Map<Long, BigDecimal> getSubjectBalances(Long bookId, String period, List<Long> subjectIds);

    /**
     * 获取账簿期间的所有科目余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @return 科目余额映射
     */
    Map<Long, BigDecimal> getAllSubjectBalances(Long bookId, String period);

    /**
     * 清除科目余额缓存
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectId 科目ID
     */
    void evictSubjectBalance(Long bookId, String period, Long subjectId);

    /**
     * 清除账簿期间的所有科目余额缓存
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     */
    void evictBalance(Long bookId, String period);

    /**
     * 清除账簿的所有余额缓存
     *
     * @param bookId 账簿ID
     */
    void evictAllBalance(Long bookId);

    /**
     * 检查科目余额是否已缓存
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectId 科目ID
     * @return 是否已缓存
     */
    boolean isBalanceCached(Long bookId, String period, Long subjectId);

    /**
     * 预热缓存
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     */
    void warmUpCache(Long bookId, String period);

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计
     */
    Map<String, Object> getCacheStatistics();

    /**
     * 获取缓存命中率
     *
     * @return 命中率
     */
    Double getCacheHitRate();

    /**
     * 缓存试算平衡结果
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param result 试算平衡结果
     */
    void cacheTrialBalanceResult(Long bookId, String period, Object result);

    /**
     * 获取试算平衡结果
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @return 试算平衡结果
     */
    Object getTrialBalanceResult(Long bookId, String period);

    /**
     * 清除试算平衡缓存
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     */
    void evictTrialBalance(Long bookId, String period);

    /**
     * 设置缓存过期时间
     *
     * @param timeout 过期时间（秒）
     */
    void setCacheTimeout(int timeout);

    /**
     * 设置最大缓存条目数
     *
     * @param maxSize 最大条目数
     */
    void setMaxCacheSize(int maxSize);

    /**
     * 刷新缓存配置
     */
    void refreshCacheConfig();
}