package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblLeaseAsset;
import com.global.treasurer.mapper.LeaseAssetMapper;
import com.global.treasurer.service.LeaseAssetService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 租赁资产服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@Service
public class LeaseAssetServiceImpl implements LeaseAssetService {

    private static final Logger log = LoggerFactory.getLogger(LeaseAssetServiceImpl.class);

    @Autowired
    private LeaseAssetMapper leaseAssetMapper;

    @Override
    public List<TblLeaseAsset> getAssetsByLeaseId(Long leaseId) {
        return leaseAssetMapper.selectByLeaseId(leaseId);
    }

    @Override
    public TblLeaseAsset getAssetById(Long assetId) {
        TblLeaseAsset asset = leaseAssetMapper.selectByAssetId(assetId);
        if (asset == null) {
            throw new ServiceException(404, "资产不存在");
        }
        return asset;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblLeaseAsset saveAsset(TblLeaseAsset asset) {
        if (asset.getAssetId() == null) {
            // 新增
            asset.setAssetCode("AST" + System.currentTimeMillis());
            asset.setStatus("NORMAL");
            asset.setCreatedTime(new Date());
            leaseAssetMapper.insert(asset);
            log.info("新增租赁资产成功, assetCode: {}", asset.getAssetCode());
        } else {
            // 更新
            asset.setUpdatedTime(new Date());
            leaseAssetMapper.updateById(asset);
            log.info("更新租赁资产成功, assetId: {}", asset.getAssetId());
        }
        return asset;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAsset(Long assetId) {
        leaseAssetMapper.deleteById(assetId);
        log.info("删除租赁资产成功, assetId: {}", assetId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAssets(List<Long> assetIds) {
        if (assetIds != null && !assetIds.isEmpty()) {
            leaseAssetMapper.batchDeleteByIds(assetIds);
            log.info("批量删除租赁资产成功, count: {}", assetIds.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssetsByLeaseId(Long leaseId) {
        leaseAssetMapper.deleteByLeaseId(leaseId);
        log.info("删除租赁所有资产成功, leaseId: {}", leaseId);
    }

    @Override
    public BigDecimal getTotalAssetValue(Long leaseId) {
        BigDecimal total = leaseAssetMapper.sumAssetValueByLeaseId(leaseId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Map<String, Object> getAssetStatistics(Long leaseId) {
        Map<String, Object> stats = new HashMap<>();
        List<TblLeaseAsset> assets = leaseAssetMapper.selectByLeaseId(leaseId);
        stats.put("totalCount", assets.size());
        stats.put("totalValue", getTotalAssetValue(leaseId));
        
        // 按类型统计
        Map<String, Integer> typeCount = new HashMap<>();
        for (TblLeaseAsset asset : assets) {
            String type = asset.getAssetType();
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }
        stats.put("typeDistribution", typeCount);
        
        return stats;
    }
}

