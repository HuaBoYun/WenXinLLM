package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryUnitEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 计量单位 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryUnitMapper")
public interface InventoryUnitMapper extends BaseMapper<InventoryUnitEntity> {

    /**
     * 分页查询计量单位列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectUnitPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据单位编码查询单位
     *
     * @param unitCode 单位编码
     * @param tenantId 租户ID
     * @return 单位信息
     */
    InventoryUnitEntity selectByUnitCode(@Param("unitCode") String unitCode, @Param("tenantId") Long tenantId);

    /**
     * 根据分类ID查询单位列表
     *
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 单位列表
     */
    List<InventoryUnitEntity> selectByCategoryId(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

    /**
     * 查询所有启用的单位
     *
     * @param tenantId 租户ID
     * @return 单位列表
     */
    List<InventoryUnitEntity> selectAllEnabled(@Param("tenantId") Long tenantId);
}

