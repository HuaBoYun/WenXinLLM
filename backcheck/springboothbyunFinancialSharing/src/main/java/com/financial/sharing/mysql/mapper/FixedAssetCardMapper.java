package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetCardEntity;
import com.financial.sharing.vo.param.FixedAssetCardQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 固定资产卡片Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetCardMapper extends BaseMapper<FixedAssetCardEntity> {

    /**
     * 分页查询资产卡片列表
     * @param param 查询参数
     * @return 资产卡片列表
     */
    List<FixedAssetCardEntity> selectPageList(@Param("param") FixedAssetCardQueryParam param);

    /**
     * 查询资产卡片详情
     * @param assetId 资产ID
     * @return 资产卡片详情
     */
    FixedAssetCardEntity selectDetailById(@Param("assetId") String assetId);

    /**
     * 统计资产汇总信息
     * @param tenantId 租户ID
     * @return 汇总信息
     */
    Map<String, Object> selectAssetSummary(@Param("tenantId") Long tenantId);

    /**
     * 按类别统计资产
     * @param tenantId 租户ID
     * @return 统计列表
     */
    List<Map<String, Object>> selectAssetByCategory(@Param("tenantId") Long tenantId);
}

