package com.management.accountant.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetRolling;
import org.apache.ibatis.annotations.Mapper;

/**
 * 滚动预算Mapper接口
 * 
 * @description 滚动预算数据访问层
 * @author AI Assistant
 * @date 2025-01-04
 */
@Mapper
public interface BudgetRollingMapper extends BaseMapper<BudgetRolling> {
}

