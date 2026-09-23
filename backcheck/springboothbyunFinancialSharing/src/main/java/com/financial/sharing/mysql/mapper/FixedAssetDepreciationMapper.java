package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetDepreciationEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 固定资产折旧记录Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetDepreciationMapper extends BaseMapper<FixedAssetDepreciationEntity> {

    /**
     * 分页查询折旧明细列表
     * @param assetCode 资产编码
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 折旧明细列表
     */
    List<FixedAssetDepreciationEntity> selectPageList(@Param("assetCode") String assetCode,
                                                       @Param("startPeriod") String startPeriod,
                                                       @Param("endPeriod") String endPeriod,
                                                       @Param("tenantId") Long tenantId);

    /**
     * 统计折旧数据
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param categoryId 资产类别ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectStatistics(@Param("startPeriod") String startPeriod,
                                        @Param("endPeriod") String endPeriod,
                                        @Param("categoryId") String categoryId,
                                        @Param("tenantId") Long tenantId);

    /**
     * 查询折旧趋势数据
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 趋势数据
     */
    List<Map<String, Object>> selectTrendData(@Param("startPeriod") String startPeriod,
                                              @Param("endPeriod") String endPeriod,
                                              @Param("tenantId") Long tenantId);

    /**
     * 检查期间是否已计提折旧
     * @param assetId 资产ID
     * @param period 期间
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkPeriodExists(@Param("assetId") String assetId,
                         @Param("period") String period,
                         @Param("tenantId") Long tenantId);

    /**
     * 批量插入折旧记录
     * @param list 折旧记录列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<FixedAssetDepreciationEntity> list);
}

