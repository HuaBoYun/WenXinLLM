package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblLeaseAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租赁资产Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@Mapper
public interface LeaseAssetMapper extends BaseMapper<TblLeaseAsset> {

    /**
     * 根据租赁ID查询资产列表
     */
    List<TblLeaseAsset> selectByLeaseId(@Param("leaseId") Long leaseId);

    /**
     * 根据资产ID查询资产详情
     */
    TblLeaseAsset selectByAssetId(@Param("assetId") Long assetId);

    /**
     * 批量删除资产
     */
    int batchDeleteByIds(@Param("assetIds") List<Long> assetIds);

    /**
     * 根据租赁ID删除所有资产
     */
    int deleteByLeaseId(@Param("leaseId") Long leaseId);

    /**
     * 统计租赁资产总值
     */
    java.math.BigDecimal sumAssetValueByLeaseId(@Param("leaseId") Long leaseId);
}

