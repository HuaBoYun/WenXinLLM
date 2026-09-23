package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcFinancialCategory;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 金融业务分类表 Mapper接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
public interface TcFinancialCategoryMapper extends Mapper<TcFinancialCategory> {

    /**
     * 根据父级ID查询子分类
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<TcFinancialCategory> selectByParentId(@Param("parentId") String parentId);

    /**
     * 根据分类层级查询分类
     * @param categoryLevel 分类层级
     * @return 分类列表
     */
    List<TcFinancialCategory> selectByCategoryLevel(@Param("categoryLevel") Integer categoryLevel);

    /**
     * 根据分类编码查询分类
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    TcFinancialCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 查询分类树结构
     * @param parentId 父级ID
     * @param status 状态
     * @return 分类树列表
     */
    List<TcFinancialCategory> selectCategoryTree(@Param("parentId") String parentId, @Param("status") String status);

    /**
     * 检查分类编码是否存在
     * @param categoryCode 分类编码
     * @param excludeId 排除的ID
     * @return 数量
     */
    int checkCategoryCodeExists(@Param("categoryCode") String categoryCode, @Param("excludeId") String excludeId);

    /**
     * 更新分类路径
     * @param id 分类ID
     * @param categoryPath 分类路径
     * @return 更新数量
     */
    int updateCategoryPath(@Param("id") String id, @Param("categoryPath") String categoryPath);

    /**
     * 批量更新子分类路径
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @return 更新数量
     */
    int batchUpdateChildrenPath(@Param("oldPath") String oldPath, @Param("newPath") String newPath);
}
