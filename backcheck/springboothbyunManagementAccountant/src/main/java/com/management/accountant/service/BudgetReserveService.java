package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetReserve;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算保留Service接口
 * 
 * @description 预算保留业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetReserveService {

    /**
     * 创建保留申请
     * 
     * @param reserve 保留对象
     * @return 创建后的保留对象
     */
    BudgetReserve create(BudgetReserve reserve);

    /**
     * 根据ID查询保留
     * 
     * @param reserveId 保留ID
     * @return 保留对象
     */
    BudgetReserve getById(String reserveId);

    /**
     * 更新保留申请
     * 
     * @param reserve 保留对象
     */
    void update(BudgetReserve reserve);

    /**
     * 删除保留申请
     * 
     * @param reserveId 保留ID
     */
    void delete(String reserveId);

    /**
     * 分页查询保留列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetReserve> getPage(Map<String, Object> params);

    /**
     * 审批保留申请
     * 
     * @param params 审批参数
     */
    void approve(Map<String, Object> params);

    /**
     * 执行保留
     * 
     * @param reserveId 保留ID
     */
    void execute(String reserveId);

    /**
     * 批量保留
     *
     * @param params 批量参数
     * @return 批量结果
     */
    Map<String, Object> batchReserve(Map<String, Object> params);

    /**
     * 导出保留数据
     *
     * @param params 查询参数
     * @return 保留数据列表
     */
    java.util.List<BudgetReserve> exportData(Map<String, Object> params);

    /**
     * 获取统计信息
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 根据条件查询保留列表
     *
     * @param wrapper 查询条件
     * @return 保留列表
     */
    java.util.List<BudgetReserve> list(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetReserve> wrapper);

    /**
     * 根据ID更新保留记录
     *
     * @param reserve 保留对象
     */
    void updateById(BudgetReserve reserve);
}

