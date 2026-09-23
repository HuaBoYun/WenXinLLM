package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetDimensionAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算维度属性Mapper接口
 */
@Mapper
public interface BudgetDimensionAttributeMapper extends BaseMapper<BudgetDimensionAttribute> {

    /**
     * 根据维度ID查询属性列表
     */
    List<BudgetDimensionAttribute> selectByDimensionId(@Param("dimensionId") String dimensionId);
}
