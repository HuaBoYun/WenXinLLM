package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 金融分类Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblFinancialCategoryMapper extends BaseMapper<TblFinancialCategory> {

    List<TblFinancialCategory> selectByParentId(@Param("parentId") Long parentId, @Param("orgId") Long orgId);

    List<TblFinancialCategory> selectEnabledList(@Param("orgId") Long orgId);

    int checkCodeUnique(@Param("categoryCode") String categoryCode, @Param("excludeId") Long excludeId);

    int batchUpdateSort(@Param("list") List<TblFinancialCategory> list);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("updateBy") String updateBy);

    int countUsage(@Param("categoryId") Long categoryId);
}

