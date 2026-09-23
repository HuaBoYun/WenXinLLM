package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetInventoryResultEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 固定资产盘点结果Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetInventoryResultMapper extends BaseMapper<FixedAssetInventoryResultEntity> {

    /**
     * 查询盘点结果列表
     * @param taskId 任务ID
     * @param assetCode 资产编码
     * @param inventoryResult 盘点结果
     * @param handleStatus 处理状态
     * @param tenantId 租户ID
     * @return 盘点结果列表
     */
    List<FixedAssetInventoryResultEntity> selectPageList(@Param("taskId") String taskId,
                                                         @Param("assetCode") String assetCode,
                                                         @Param("inventoryResult") String inventoryResult,
                                                         @Param("handleStatus") String handleStatus,
                                                         @Param("tenantId") Long tenantId);

    /**
     * 批量插入盘点结果
     * @param list 盘点结果列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<FixedAssetInventoryResultEntity> list);

    /**
     * 统计盘点结果
     * @param taskId 任务ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    java.util.Map<String, Object> selectResultStatistics(@Param("taskId") String taskId,
                                                         @Param("tenantId") Long tenantId);
}

