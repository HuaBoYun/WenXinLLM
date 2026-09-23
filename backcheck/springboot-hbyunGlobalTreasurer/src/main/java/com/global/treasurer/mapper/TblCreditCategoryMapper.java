package com.global.treasurer.mapper;

import com.global.treasurer.entity.TblCreditCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 授信类别Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Mapper
public interface TblCreditCategoryMapper {

    /**
     * 查询所有授信类别（未删除）
     */
    List<TblCreditCategory> selectList();

    /**
     * 根据ID查询
     */
    TblCreditCategory selectById(Long categoryId);

    /**
     * 根据条件查询
     */
    List<TblCreditCategory> selectByCondition(@Param("categoryName") String categoryName,
                                              @Param("categoryCode") String categoryCode,
                                              @Param("status") String status);

    /**
     * 插入
     */
    int insert(TblCreditCategory category);

    /**
     * 更新
     */
    int updateById(TblCreditCategory category);

    /**
     * 删除（软删除）
     */
    int deleteById(Long categoryId);
}
