package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetMultiBookEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 多账簿资产Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetMultiBookMapper extends BaseMapper<FixedAssetMultiBookEntity> {

    /**
     * 查询资产的多账簿信息
     * @param assetId 资产ID
     * @param tenantId 租户ID
     * @return 多账簿资产列表
     */
    List<FixedAssetMultiBookEntity> selectByAssetId(@Param("assetId") String assetId,
                                                    @Param("tenantId") Long tenantId);

    /**
     * 查询指定账簿的资产信息
     * @param assetId 资产ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 多账簿资产
     */
    FixedAssetMultiBookEntity selectByAssetAndBook(@Param("assetId") String assetId,
                                                   @Param("bookId") String bookId,
                                                   @Param("tenantId") Long tenantId);
}

