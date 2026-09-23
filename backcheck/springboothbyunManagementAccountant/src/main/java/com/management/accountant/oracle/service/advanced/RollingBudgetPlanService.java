package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.RollingBudgetPlan;

import java.util.List;
import java.util.Map;

/**
 * 滚动预算计划Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface RollingBudgetPlanService {

    /**
     * 查询滚动预算计划列表
     */
    List<RollingBudgetPlan> selectList(Map<String, Object> params);

    /**
     * 分页查询滚动预算计划列表
     */
    Page<RollingBudgetPlan> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询滚动预算计划
     */
    RollingBudgetPlan selectById(String planId);

    /**
     * 新增滚动预算计划
     */
    boolean insert(RollingBudgetPlan plan);

    /**
     * 修改滚动预算计划
     */
    boolean update(RollingBudgetPlan plan);

    /**
     * 删除滚动预算计划
     */
    boolean deleteById(String planId);

    /**
     * 执行滚动预算
     */
    boolean executePlan(String planId);

    /**
     * 激活计划
     */
    boolean activatePlan(String planId);

    /**
     * 暂停计划
     */
    boolean pausePlan(String planId);

    /**
     * 根据滚动类型查询计划
     */
    List<RollingBudgetPlan> selectByRollingType(String rollingType);
}

