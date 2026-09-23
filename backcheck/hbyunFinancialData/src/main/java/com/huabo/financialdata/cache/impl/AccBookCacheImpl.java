package com.huabo.financialdata.cache.impl;

import com.huabo.financialdata.cache.IAccBookCache;
import com.huabo.financialdata.config.constant.HbConstants;
import com.huabo.financialdata.config.redis.service.IRedisService;
import com.huabo.financialdata.entity.entity.AccBook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 账套缓存实现
 *
 * @author lee
 * @version 1.0.0
 **/
@Service(value = "accBookCacheImpl")
public class AccBookCacheImpl implements IAccBookCache {

    private static final String ACC_BOOK_INFO = "ACCBOOK:";

    @Resource
    private IRedisService redisService;

    /**
     * 缓存用户选中账套数据
     *
     * @param staffId 员工ID
     * @param accBook 账簿信息
     */
    @Override
    public void addCacheByStaffId(BigDecimal staffId, AccBook accBook) {
        // 构建键值对的键
        String key = ACC_BOOK_INFO + "USER:SELECTED:INFO:" + staffId;
        // 将键值对添加到缓存中，并设置缓存过期时间为30天
        redisService.set(key, accBook, HbConstants.CACHE_EXPIRE_30D);
    }


    /**
     * 获取用户选中账套数据
     * @param staffId 员工ID
     * @return 缓存中的AccBook对象，如果不存在则返回null
     */
    @Override
    public AccBook getCacheByStaffId(BigDecimal staffId) {
        // 构建Redis键值
        String key = ACC_BOOK_INFO + "USER:SELECTED:INFO:" + staffId;
        // 从Redis缓存中获取AccBook对象
        return redisService.get(key, AccBook.class);
    }


    /**
     * 删除用户选中账套数据
     * @param staffId 员工ID
     */
    @Override
    public void delCacheByStaffId(BigDecimal staffId) {
        // 构建Redis键值
        String key = ACC_BOOK_INFO + "USER:SELECTED:INFO:" + staffId;
        // 从Redis缓存中删除AccBook对象
        redisService.delete(key);
    }
}
