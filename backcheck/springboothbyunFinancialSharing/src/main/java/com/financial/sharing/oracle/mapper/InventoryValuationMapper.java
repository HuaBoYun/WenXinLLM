package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryValuationEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 存货计价 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryValuationMapper")
public interface InventoryValuationMapper extends BaseMapper<InventoryValuationEntity> {

    /**
     * 分页查询存货计价列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectValuationPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据存货ID查询计价信息
     *
     * @param inventoryId 存货ID
     * @param tenantId 租户ID
     * @return 计价信息
     */
    InventoryValuationEntity selectByInventoryId(@Param("inventoryId") Long inventoryId, @Param("tenantId") Long tenantId);

    /**
     * 根据存货编码查询计价信息
     *
     * @param inventoryCode 存货编码
     * @param tenantId 租户ID
     * @return 计价信息
     */
    InventoryValuationEntity selectByInventoryCode(@Param("inventoryCode") String inventoryCode, @Param("tenantId") Long tenantId);

    /**
     * 根据仓库ID查询计价列表
     *
     * @param warehouseId 仓库ID
     * @param tenantId 租户ID
     * @return 计价列表
     */
    List<InventoryValuationEntity> selectByWarehouseId(@Param("warehouseId") Long warehouseId, @Param("tenantId") Long tenantId);

    /**
     * 根据计价方法查询计价列表
     *
     * @param pricingMethod 计价方法
     * @param tenantId 租户ID
     * @return 计价列表
     */
    List<InventoryValuationEntity> selectByPricingMethod(@Param("pricingMethod") Integer pricingMethod, @Param("tenantId") Long tenantId);

    /**
     * 批量更新计价方法
     *
     * @param valuationIds 计价ID列表
     * @param pricingMethod 计价方法
     * @param updaterId 更新人ID
     * @return 更新数量
     */
    int batchUpdatePricingMethod(@Param("valuationIds") List<Long> valuationIds, 
                                  @Param("pricingMethod") Integer pricingMethod,
                                  @Param("updaterId") String updaterId);

    /**
     * 批量删除计价记录
     *
     * @param valuationIds 计价ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("valuationIds") List<Long> valuationIds);

    /**
     * 计算存货成本
     *
     * @param valuationId 计价ID
     * @return 计算结果
     */
    Map<String, Object> calculateCost(@Param("valuationId") Long valuationId);

    /**
     * 获取仓库列表
     *
     * @param tenantId 租户ID
     * @return 仓库列表
     */
    List<Map<String, Object>> selectWarehouseList(@Param("tenantId") Long tenantId);

    /**
     * 统计存货计价数量
     *
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> countValuationStatistics(@Param("param") Map<String, Object> param);
}

