package com.management.accountant.oracle.service.advanced;

import com.management.accountant.oracle.entity.advanced.BudgetOptimizationResult;

import java.util.List;

/**
 * 预算优化结果 Service
 */
public interface BudgetOptimizationResultService {

    /**
     * 根据优化任务ID查询结果列表
     */
    List<BudgetOptimizationResult> listByOptimizationId(String optimizationId);

    /**
     * 保存优化结果（批量）
     */
    boolean saveBatch(List<BudgetOptimizationResult> results);

    /**
     * 删除某任务的所有结果（逻辑删除）
     */
    boolean deleteByOptimizationId(String optimizationId);
}
