package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetCategoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 固定资产类别Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetCategoryMapper extends BaseMapper<FixedAssetCategoryEntity> {

    /**
     * 查询类别树形列表
     * @param tenantId 租户ID
     * @return 类别列表
     */
    List<FixedAssetCategoryEntity> selectTreeList(@Param("tenantId") Long tenantId);

    /**
     * 检查类别编码是否存在
     * @param categoryCode 类别编码
     * @param categoryId 类别ID（排除自己）
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkCategoryCodeExists(@Param("categoryCode") String categoryCode,
                                @Param("categoryId") String categoryId,
                                @Param("tenantId") Long tenantId);

    /**
     * 查询子类别数量
     * @param parentId 父类别ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int countChildren(@Param("parentId") String parentId,
                     @Param("tenantId") Long tenantId);

    /**
     * 查询类别下的资产数量
     * @param categoryId 类别ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int countAssets(@Param("categoryId") String categoryId,
                   @Param("tenantId") Long tenantId);
}

