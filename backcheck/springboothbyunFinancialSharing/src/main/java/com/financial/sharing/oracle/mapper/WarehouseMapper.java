package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.WarehouseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 仓库 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleWarehouseMapper")
public interface WarehouseMapper extends BaseMapper<WarehouseEntity> {

    /**
     * 分页查询仓库列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectWarehousePage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据仓库编码查询仓库
     *
     * @param warehouseCode 仓库编码
     * @param tenantId 租户ID
     * @return 仓库信息
     */
    WarehouseEntity selectByWarehouseCode(@Param("warehouseCode") String warehouseCode, @Param("tenantId") Long tenantId);

    /**
     * 根据仓库类型查询仓库列表
     *
     * @param warehouseType 仓库类型
     * @param tenantId 租户ID
     * @return 仓库列表
     */
    List<WarehouseEntity> selectByWarehouseType(@Param("warehouseType") String warehouseType, @Param("tenantId") Long tenantId);

    /**
     * 查询所有启用的仓库
     *
     * @param tenantId 租户ID
     * @return 仓库列表
     */
    List<WarehouseEntity> selectAllEnabled(@Param("tenantId") Long tenantId);
}

