package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetAllocation;

import java.util.List;

/**
 * 预算分配Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetAllocationService extends IService<BudgetAllocation> {

    /**
     * 根据分配编码查询分配
     */
    BudgetAllocation getByAllocationCode(String allocationCode);

    /**
     * 根据源预算ID查询分配列表
     */
    List<BudgetAllocation> listBySourceBudgetId(String sourceBudgetId);

    /**
     * 根据目标预算ID查询分配列表
     */
    List<BudgetAllocation> listByTargetBudgetId(String targetBudgetId);

    /**
     * 根据分配状态查询分配列表
     */
    List<BudgetAllocation> listByAllocationStatus(String allocationStatus);

    /**
     * 批量删除分配
     */
    boolean batchDeleteAllocations(List<String> allocationIds);

    /**
     * 保存或更新分配
     */
    boolean saveOrUpdateAllocation(BudgetAllocation allocation);

    /**
     * 执行预算分配
     */
    boolean executeAllocation(String allocationId);
}

