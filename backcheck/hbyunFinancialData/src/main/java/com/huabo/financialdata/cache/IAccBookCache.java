package com.huabo.financialdata.cache;

import com.huabo.financialdata.entity.entity.AccBook;

import java.math.BigDecimal;

/**
 * 账套缓存
 *
 * @author lee
 * @version 1.0.0
 **/
public interface IAccBookCache {


    /**
     * 缓存用户选中账套数据
     *
     * @param staffId 用户ID
     * @param accBook 选中的账套信息
     */
    void addCacheByStaffId(BigDecimal staffId, AccBook accBook);

    /**
     * 获取用户选中账套数据
     *
     * @param staffId 用户ID
     * @return 选中的账套信息
     */
    AccBook getCacheByStaffId(BigDecimal staffId);

    /**
     * 删除用户选中账套数据
     *
     * @param staffId 用户ID
     */
    void delCacheByStaffId(BigDecimal staffId);

}
