package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblFundPool;
import com.global.treasurer.mapper.TblFundPoolMapper;
import com.global.treasurer.service.TblFundPoolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 资金池Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblFundPoolServiceImpl implements TblFundPoolService {
    @Resource
    private TblFundPoolMapper tblFundPoolMapper;

    @Override
    public PageInfo<TblFundPool> getFundPoolPage(Integer pageNum, Integer pageSize,
                                                  String poolName, String poolType, String poolStatus) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblFundPool> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(poolName), TblFundPool::getPoolName, poolName)
               .eq(StringUtils.isNotBlank(poolType), TblFundPool::getPoolType, poolType)
               .eq(StringUtils.isNotBlank(poolStatus), TblFundPool::getStatus, poolStatus)
               .orderByDesc(TblFundPool::getCreateTime);
        List<TblFundPool> list = tblFundPoolMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblFundPool getFundPoolById(Long poolId) {
        return tblFundPoolMapper.selectById(poolId);
    }

    @Override
    public TblFundPool saveFundPool(TblFundPool fundPool) {
        // POOL_ID 为 BIGINT IDENTITY 自增，不需要手动设置
        // POOL_CODE 唯一，为空时自动生成
        if (fundPool.getPoolCode() == null || fundPool.getPoolCode().isEmpty()) {
            fundPool.setPoolCode("FP" + System.currentTimeMillis());
        }
        fundPool.setCreateTime(new Date());
        if (fundPool.getCurrency() == null) {
            fundPool.setCurrency("CNY");
        }
        if (fundPool.getTotalLimit() == null) {
            fundPool.setTotalLimit(BigDecimal.ZERO);
        }
        if (fundPool.getAvailableAmount() == null) {
            fundPool.setAvailableAmount(BigDecimal.ZERO);
        }
        if (fundPool.getUsedAmount() == null) {
            fundPool.setUsedAmount(BigDecimal.ZERO);
        }
        if (fundPool.getIsEnabled() == null) {
            fundPool.setIsEnabled(1);
        }
        if (fundPool.getStatus() == null) {
            fundPool.setStatus("ACTIVE");
        }
        tblFundPoolMapper.insertFundPool(fundPool);
        return fundPool;
    }

    @Override
    public void updateFundPool(TblFundPool fundPool) {
        fundPool.setUpdateTime(new Date());
        tblFundPoolMapper.updateById(fundPool);
    }

    @Override
    public void deleteFundPool(Long poolId) {
        tblFundPoolMapper.deleteById(poolId);
    }

    @Override
    public void batchDeleteFundPool(List<Long> poolIds) {
        for (Long poolId : poolIds) {
            deleteFundPool(poolId);
        }
    }

    @Override
    public Map<String, Object> getFundPoolStatistics() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblFundPool> wrapper = new LambdaQueryWrapper<>();
        List<TblFundPool> list = tblFundPoolMapper.selectList(wrapper);

        result.put("totalCount", list.size());
        result.put("activeCount", list.stream().filter(p -> "ACTIVE".equals(p.getStatus())).count());
        result.put("totalBalance", list.stream().map(TblFundPool::getTotalLimit)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.put("availableBalance", list.stream().map(TblFundPool::getAvailableAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        return result;
    }

    @Override
    public List<TblFundPool> getActiveFundPools() {
        LambdaQueryWrapper<TblFundPool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblFundPool::getStatus, "ACTIVE");
        return tblFundPoolMapper.selectList(wrapper);
    }
}

