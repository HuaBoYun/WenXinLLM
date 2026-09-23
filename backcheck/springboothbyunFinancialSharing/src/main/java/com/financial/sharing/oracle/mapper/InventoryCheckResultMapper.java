package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.InventoryCheckResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 盘点结果 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryCheckResultMapper")
public interface InventoryCheckResultMapper extends BaseMapper<InventoryCheckResultEntity> {

    /**
     * 根据盘点ID查询结果列表
     *
     * @param checkId 盘点ID
     * @return 结果列表
     */
    List<InventoryCheckResultEntity> selectByCheckId(@Param("checkId") Long checkId);

    /**
     * 根据存货ID查询结果列表
     *
     * @param inventoryId 存货ID
     * @param tenantId 租户ID
     * @return 结果列表
     */
    List<InventoryCheckResultEntity> selectByInventoryId(@Param("inventoryId") Long inventoryId, @Param("tenantId") Long tenantId);

    /**
     * 根据盘点结果查询结果列表
     *
     * @param checkResult 盘点结果
     * @param checkId 盘点ID
     * @return 结果列表
     */
    List<InventoryCheckResultEntity> selectByCheckResult(@Param("checkResult") String checkResult, @Param("checkId") Long checkId);

    /**
     * 根据处理状态查询结果列表
     *
     * @param processStatus 处理状态
     * @param checkId 盘点ID
     * @return 结果列表
     */
    List<InventoryCheckResultEntity> selectByProcessStatus(@Param("processStatus") Integer processStatus, @Param("checkId") Long checkId);

    /**
     * 批量插入盘点结果
     *
     * @param resultList 结果列表
     * @return 插入数量
     */
    int batchInsert(@Param("resultList") List<InventoryCheckResultEntity> resultList);

    /**
     * 根据盘点ID删除结果
     *
     * @param checkId 盘点ID
     * @return 删除数量
     */
    int deleteByCheckId(@Param("checkId") Long checkId);

    /**
     * 处理盘点差异
     *
     * @param resultId 结果ID
     * @param processStatus 处理状态
     * @param processRemark 处理备注
     * @param processorId 处理人ID
     * @return 更新数量
     */
    int processDifference(@Param("resultId") Long resultId,
                         @Param("processStatus") Integer processStatus,
                         @Param("processRemark") String processRemark,
                         @Param("processorId") String processorId);

    /**
     * 统计盘点结果
     *
     * @param checkId 盘点ID
     * @return 统计结果
     */
    Map<String, Object> countResultStatistics(@Param("checkId") Long checkId);
}

