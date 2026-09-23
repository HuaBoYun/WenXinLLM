package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcCashflowCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 现金流分类Mapper接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Repository
public interface TcCashflowCategoryMapper extends Mapper<TcCashflowCategory> {

    /**
     * 根据父级ID查询子分类
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<TcCashflowCategory> selectByParentId(@Param("parentId") String parentId);

    /**
     * 查询根级分类
     * @return 根级分类列表
     */
    List<TcCashflowCategory> selectRootCategories();

    /**
     * 根据业务类型查询分类
     * @param businessType 业务类型
     * @return 分类列表
     */
    List<TcCashflowCategory> selectByBusinessType(@Param("businessType") String businessType);

    /**
     * 根据分类编码查询分类
     * @param categoryCode 分类编码
     * @return 分类
     */
    TcCashflowCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 根据分类层级查询分类
     * @param categoryLevel 分类层级
     * @return 分类列表
     */
    List<TcCashflowCategory> selectByCategoryLevel(@Param("categoryLevel") Integer categoryLevel);

    /**
     * 查询分类树结构
     * @return 分类树列表
     */
    List<TcCashflowCategory> selectCategoryTree();

    /**
     * 根据父级ID统计子分类数量
     * @param parentId 父级ID
     * @return 子分类数量
     */
    int countByParentId(@Param("parentId") String parentId);

    /**
     * 批量插入分类
     * @param categories 分类列表
     * @return 插入数量
     */
    int batchInsert(@Param("categories") List<TcCashflowCategory> categories);

    /**
     * 更新分类路径
     * @param id 分类ID
     * @param categoryPath 分类路径
     * @return 更新数量
     */
    int updateCategoryPath(@Param("id") String id, @Param("categoryPath") String categoryPath);
}
