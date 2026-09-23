package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算限额Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetLimitMapper extends BaseMapper<BudgetLimit> {

    /**
     * 根据限额编码查询限额
     */
    BudgetLimit selectByLimitCode(@Param("limitCode") String limitCode);

    /**
     * 根据预算ID查询限额列表
     */
    List<BudgetLimit> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据限额状态查询限额列表
     */
    List<BudgetLimit> selectByLimitStatus(@Param("limitStatus") String limitStatus);

    /**
     * 查询启用的限额列表
     */
    List<BudgetLimit> selectEnabledLimits();

    /**
     * 批量删除限额
     */
    int batchDeleteLimits(@Param("limitIds") List<String> limitIds);
}

