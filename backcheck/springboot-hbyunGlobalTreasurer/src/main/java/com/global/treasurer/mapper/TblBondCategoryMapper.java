package com.global.treasurer.mapper;

import com.global.treasurer.entity.TblBondCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 债券类别Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Mapper
public interface TblBondCategoryMapper {

    /**
     * 查询所有债券类别（未删除）
     */
    List<TblBondCategory> selectList();

    /**
     * 根据ID查询
     */
    TblBondCategory selectById(Long categoryId);

    /**
     * 根据条件查询
     */
    List<TblBondCategory> selectByCondition(@Param("categoryName") String categoryName,
                                             @Param("categoryCode") String categoryCode,
                                             @Param("status") String status);

    /**
     * 插入
     */
    int insert(TblBondCategory category);

    /**
     * 更新
     */
    int updateById(TblBondCategory category);

    /**
     * 删除（软删除）
     */
    int deleteById(Long categoryId);
}
