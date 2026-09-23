package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.BudgetOptimization;

import java.util.List;
import java.util.Map;

public interface BudgetOptimizationService {
    List<BudgetOptimization> selectList(Map<String, Object> params);
    Page<BudgetOptimization> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    BudgetOptimization selectById(String optimizationId);
    boolean insert(BudgetOptimization optimization);
    boolean update(BudgetOptimization optimization);
    boolean deleteById(String optimizationId);
    boolean runOptimization(String optimizationId);
    boolean stopOptimization(String optimizationId);
    boolean applyOptimization(String optimizationId);
    boolean copyOptimization(String optimizationId);
    Map<String, Object> getStats();
}
