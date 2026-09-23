package com.management.accountant.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetMultiCurrency;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预算多币种管理Mapper接口
 * 
 * @description 预算多币种管理数据访问层
 * @author AI Assistant
 * @date 2025-01-04
 */
@Mapper
public interface BudgetMultiCurrencyMapper extends BaseMapper<BudgetMultiCurrency> {
}

