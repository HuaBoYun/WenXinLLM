package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetReserve;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算保留Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetReserveMapper extends BaseMapper<BudgetReserve> {

    /**
     * 根据保留编码查询保留
     */
    BudgetReserve selectByReserveCode(@Param("reserveCode") String reserveCode);

    /**
     * 根据预算ID查询保留列表
     */
    List<BudgetReserve> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据保留状态查询保留列表
     */
    List<BudgetReserve> selectByReserveStatus(@Param("reserveStatus") String reserveStatus);

    /**
     * 查询已保留的列表
     */
    List<BudgetReserve> selectReservedList();

    /**
     * 批量删除保留
     */
    int batchDeleteReserves(@Param("reserveIds") List<String> reserveIds);
}

