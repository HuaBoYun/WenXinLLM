package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.InventoryCategoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 存货分类 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryCategoryMapper")
public interface InventoryCategoryMapper extends BaseMapper<InventoryCategoryEntity> {

    /**
     * 查询存货分类树
     *
     * @param tenantId 租户ID
     * @return 分类树列表
     */
    List<Map<String, Object>> selectCategoryTree(@Param("tenantId") Long tenantId);

    /**
     * 根据父级ID查询子分类列表
     *
     * @param parentId 父级ID
     * @param tenantId 租户ID
     * @return 子分类列表
     */
    List<InventoryCategoryEntity> selectByParentId(@Param("parentId") Long parentId, @Param("tenantId") Long tenantId);

    /**
     * 根据分类编码查询分类
     *
     * @param categoryCode 分类编码
     * @param tenantId 租户ID
     * @return 分类信息
     */
    InventoryCategoryEntity selectByCategoryCode(@Param("categoryCode") String categoryCode, @Param("tenantId") Long tenantId);

    /**
     * 根据分类ID查询存货列表
     *
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 存货列表
     */
    List<Map<String, Object>> selectInventoriesByCategoryId(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

    /**
     * 更新分类排序
     *
     * @param categoryId 分类ID
     * @param sortOrder 排序号
     * @return 更新数量
     */
    int updateSortOrder(@Param("categoryId") Long categoryId, @Param("sortOrder") Integer sortOrder);

    /**
     * 更新分类路径
     *
     * @param categoryId 分类ID
     * @param categoryPath 分类路径
     * @return 更新数量
     */
    int updateCategoryPath(@Param("categoryId") Long categoryId, @Param("categoryPath") String categoryPath);

    /**
     * 检查分类是否有子分类
     *
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 子分类数量
     */
    int countChildCategories(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

    /**
     * 检查分类是否有存货
     *
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 存货数量
     */
    int countInventories(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);
}

