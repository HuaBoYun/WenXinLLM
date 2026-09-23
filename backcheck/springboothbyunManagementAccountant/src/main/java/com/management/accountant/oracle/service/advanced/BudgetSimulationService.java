package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.BudgetSimulation;

import java.util.List;
import java.util.Map;

/**
 * 预算模拟Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetSimulationService {

    /**
     * 查询预算模拟列表
     */
    List<BudgetSimulation> selectList(Map<String, Object> params);

    /**
     * 分页查询预算模拟列表
     */
    Page<BudgetSimulation> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询预算模拟
     */
    BudgetSimulation selectById(String simulationId);

    /**
     * 新增预算模拟
     */
    boolean insert(BudgetSimulation simulation);

    /**
     * 修改预算模拟
     */
    boolean update(BudgetSimulation simulation);

    /**
     * 删除预算模拟
     */
    boolean deleteById(String simulationId);

    /**
     * 执行模拟
     */
    boolean executeSimulation(String simulationId);

    /**
     * 获取模拟结果
     */
    Map<String, Object> getSimulationResult(String simulationId);
}

