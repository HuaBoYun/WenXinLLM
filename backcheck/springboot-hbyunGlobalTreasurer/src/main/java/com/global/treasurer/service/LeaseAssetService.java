package com.global.treasurer.service;

import com.global.treasurer.entity.TblLeaseAsset;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 租赁资产服务接口
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
public interface LeaseAssetService {

    /**
     * 根据租赁ID获取资产列表
     */
    List<TblLeaseAsset> getAssetsByLeaseId(Long leaseId);

    /**
     * 根据资产ID获取资产详情
     */
    TblLeaseAsset getAssetById(Long assetId);

    /**
     * 保存资产（新增或更新）
     */
    TblLeaseAsset saveAsset(TblLeaseAsset asset);

    /**
     * 删除资产
     */
    void deleteAsset(Long assetId);

    /**
     * 批量删除资产
     */
    void batchDeleteAssets(List<Long> assetIds);

    /**
     * 根据租赁ID删除所有资产
     */
    void deleteAssetsByLeaseId(Long leaseId);

    /**
     * 获取租赁资产总值
     */
    BigDecimal getTotalAssetValue(Long leaseId);

    /**
     * 获取资产统计信息
     */
    Map<String, Object> getAssetStatistics(Long leaseId);
}

