package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAllocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算分配Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetAllocationMapper extends BaseMapper<BudgetAllocation> {

    /**
     * 根据分配编码查询分配
     */
    BudgetAllocation selectByAllocationCode(@Param("allocationCode") String allocationCode);

    /**
     * 根据源预算ID查询分配列表
     */
    List<BudgetAllocation> selectBySourceBudgetId(@Param("sourceBudgetId") String sourceBudgetId);

    /**
     * 根据目标预算ID查询分配列表
     */
    List<BudgetAllocation> selectByTargetBudgetId(@Param("targetBudgetId") String targetBudgetId);

    /**
     * 根据分配状态查询分配列表
     */
    List<BudgetAllocation> selectByAllocationStatus(@Param("allocationStatus") String allocationStatus);

    /**
     * 批量删除分配
     */
    int batchDeleteAllocations(@Param("allocationIds") List<String> allocationIds);
}

