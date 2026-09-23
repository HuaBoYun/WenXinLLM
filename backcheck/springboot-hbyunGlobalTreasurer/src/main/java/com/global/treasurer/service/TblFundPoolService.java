package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblFundPool;

import java.util.List;
import java.util.Map;

/**
 * 资金池Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblFundPoolService {

    /**
     * 分页查询资金池列表
     */
    PageInfo<TblFundPool> getFundPoolPage(Integer pageNum, Integer pageSize, 
                                          String poolName, String poolType, String poolStatus);

    /**
     * 根据ID查询资金池
     */
    TblFundPool getFundPoolById(Long poolId);

    /**
     * 保存资金池
     */
    TblFundPool saveFundPool(TblFundPool fundPool);

    /**
     * 更新资金池
     */
    void updateFundPool(TblFundPool fundPool);

    /**
     * 删除资金池
     */
    void deleteFundPool(Long poolId);

    /**
     * 批量删除资金池
     */
    void batchDeleteFundPool(List<Long> poolIds);

    /**
     * 获取资金池统计信息
     */
    Map<String, Object> getFundPoolStatistics();

    /**
     * 获取所有启用的资金池
     */
    List<TblFundPool> getActiveFundPools();
}

