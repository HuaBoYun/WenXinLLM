package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryMasterEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 存货主表 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryMasterMapper")
public interface InventoryMasterMapper extends BaseMapper<InventoryMasterEntity> {

    /**
     * 分页查询存货列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectInventoryPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据存货编码查询存货
     *
     * @param inventoryCode 存货编码
     * @param tenantId 租户ID
     * @return 存货信息
     */
    InventoryMasterEntity selectByInventoryCode(@Param("inventoryCode") String inventoryCode, @Param("tenantId") Long tenantId);

    /**
     * 根据分类ID查询存货列表
     *
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 存货列表
     */
    List<InventoryMasterEntity> selectByCategoryId(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

    /**
     * 根据仓库ID查询存货列表
     *
     * @param warehouseId 仓库ID
     * @param tenantId 租户ID
     * @return 存货列表
     */
    List<InventoryMasterEntity> selectByWarehouseId(@Param("warehouseId") Long warehouseId, @Param("tenantId") Long tenantId);

    /**
     * 查询所有启用的存货
     *
     * @param tenantId 租户ID
     * @return 存货列表
     */
    List<InventoryMasterEntity> selectAllEnabled(@Param("tenantId") Long tenantId);
}

