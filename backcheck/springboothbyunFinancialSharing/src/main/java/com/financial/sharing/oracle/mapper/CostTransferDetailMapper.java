package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.CostTransferDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 成本结转明细 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleCostTransferDetailMapper")
public interface CostTransferDetailMapper extends BaseMapper<CostTransferDetailEntity> {

    /**
     * 根据结转ID查询明细列表
     *
     * @param transferId 结转ID
     * @return 明细列表
     */
    List<CostTransferDetailEntity> selectByTransferId(@Param("transferId") Long transferId);

    /**
     * 根据存货ID查询明细列表
     *
     * @param inventoryId 存货ID
     * @param tenantId 租户ID
     * @return 明细列表
     */
    List<CostTransferDetailEntity> selectByInventoryId(@Param("inventoryId") Long inventoryId, @Param("tenantId") Long tenantId);

    /**
     * 批量插入明细
     *
     * @param detailList 明细列表
     * @return 插入数量
     */
    int batchInsert(@Param("detailList") List<CostTransferDetailEntity> detailList);

    /**
     * 根据结转ID删除明细
     *
     * @param transferId 结转ID
     * @return 删除数量
     */
    int deleteByTransferId(@Param("transferId") Long transferId);

    /**
     * 统计结转明细
     *
     * @param transferId 结转ID
     * @return 统计结果
     */
    Map<String, Object> countDetailStatistics(@Param("transferId") Long transferId);
}

