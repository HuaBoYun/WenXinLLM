package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetInventoryTaskEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 固定资产盘点任务Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetInventoryTaskMapper extends BaseMapper<FixedAssetInventoryTaskEntity> {

    /**
     * 分页查询盘点任务列表
     * @param taskNo 任务编号
     * @param inventoryType 盘点类型
     * @param status 状态
     * @param tenantId 租户ID
     * @return 盘点任务列表
     */
    List<FixedAssetInventoryTaskEntity> selectPageList(@Param("taskNo") String taskNo,
                                                       @Param("inventoryType") String inventoryType,
                                                       @Param("status") String status,
                                                       @Param("tenantId") Long tenantId);

    /**
     * 统计盘点任务数据
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectStatistics(@Param("tenantId") Long tenantId);

    /**
     * 生成任务编号
     * @param tenantId 租户ID
     * @return 任务编号
     */
    String generateTaskNo(@Param("tenantId") Long tenantId);

    /**
     * 更新任务进度
     * @param taskId 任务ID
     * @param inventoriedCount 已盘点数量
     * @param differenceCount 差异数量
     * @param progress 进度
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int updateProgress(@Param("taskId") String taskId,
                      @Param("inventoriedCount") Integer inventoriedCount,
                      @Param("differenceCount") Integer differenceCount,
                      @Param("progress") java.math.BigDecimal progress,
                      @Param("tenantId") Long tenantId);
}

