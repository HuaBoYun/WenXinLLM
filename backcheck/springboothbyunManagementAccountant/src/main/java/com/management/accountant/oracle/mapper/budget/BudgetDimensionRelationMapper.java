package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetDimensionRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算维度关联关系Mapper接口
 */
@Mapper
public interface BudgetDimensionRelationMapper extends BaseMapper<BudgetDimensionRelation> {

    /**
     * 根据维度ID查询关联关系列表（含关联维度名称）
     */
    List<BudgetDimensionRelation> selectByDimensionId(@Param("dimensionId") String dimensionId);
}
