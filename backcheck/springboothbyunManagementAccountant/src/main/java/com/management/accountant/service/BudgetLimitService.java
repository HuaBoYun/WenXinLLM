package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetLimit;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算限额Service接口
 * 
 * @description 预算限额业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetLimitService {

    /**
     * 创建预算限额
     * 
     * @param limit 限额对象
     * @return 创建后的限额对象
     */
    BudgetLimit create(BudgetLimit limit);

    /**
     * 根据ID查询限额
     * 
     * @param limitId 限额ID
     * @return 限额对象
     */
    BudgetLimit getById(String limitId);

    /**
     * 更新预算限额
     * 
     * @param limit 限额对象
     */
    void update(BudgetLimit limit);

    /**
     * 删除预算限额
     * 
     * @param limitId 限额ID
     */
    void delete(String limitId);

    /**
     * 分页查询限额列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetLimit> getPage(Map<String, Object> params);

    /**
     * 启用限额
     * 
     * @param limitId 限额ID
     */
    void enable(String limitId);

    /**
     * 停用限额
     * 
     * @param limitId 限额ID
     */
    void disable(String limitId);

    /**
     * 检查限额
     *
     * @param params 检查参数
     * @return 检查结果
     */
    Map<String, Object> checkLimit(Map<String, Object> params);

    /**
     * 批量启用限额
     *
     * @param params 批量启用参数
     * @return 批量启用结果
     */
    Map<String, Object> batchEnable(Map<String, Object> params);

    /**
     * 批量停用限额
     *
     * @param params 批量停用参数
     * @return 批量停用结果
     */
    Map<String, Object> batchDisable(Map<String, Object> params);

    /**
     * 导出限额数据
     *
     * @param params 查询参数
     * @return 限额数据列表
     */
    java.util.List<BudgetLimit> exportData(Map<String, Object> params);

    /**
     * 获取统计信息
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 根据条件查询限额列表
     *
     * @param wrapper 查询条件
     * @return 限额列表
     */
    java.util.List<BudgetLimit> list(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetLimit> wrapper);

    /**
     * 根据ID更新限额
     *
     * @param limit 限额对象
     * @return 是否成功
     */
    boolean updateById(BudgetLimit limit);
}

