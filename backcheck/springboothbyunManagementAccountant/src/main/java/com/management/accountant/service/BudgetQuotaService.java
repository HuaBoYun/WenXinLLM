package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetQuota;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算配额Service接口
 * 
 * @description 预算配额业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetQuotaService {

    /**
     * 创建预算配额
     * 
     * @param quota 配额对象
     * @return 创建后的配额对象
     */
    BudgetQuota create(BudgetQuota quota);

    /**
     * 根据ID查询配额
     * 
     * @param quotaId 配额ID
     * @return 配额对象
     */
    BudgetQuota getById(String quotaId);

    /**
     * 更新预算配额
     * 
     * @param quota 配额对象
     */
    void update(BudgetQuota quota);

    /**
     * 删除预算配额
     * 
     * @param quotaId 配额ID
     */
    void delete(String quotaId);

    /**
     * 分页查询配额列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetQuota> getPage(Map<String, Object> params);

    /**
     * 启用配额
     * 
     * @param quotaId 配额ID
     */
    void enable(String quotaId);

    /**
     * 停用配额
     * 
     * @param quotaId 配额ID
     */
    void disable(String quotaId);

    /**
     * 分配配额
     * 
     * @param params 分配参数
     * @return 分配结果
     */
    Map<String, Object> allocate(Map<String, Object> params);

    /**
     * 调整配额
     *
     * @param params 调整参数
     */
    void adjust(Map<String, Object> params);

    /**
     * 批量调整配额
     *
     * @param params 批量调整参数
     * @return 批量调整结果
     */
    Map<String, Object> batchAdjust(Map<String, Object> params);

    /**
     * 导出配额数据
     *
     * @param params 查询参数
     * @return 配额数据列表
     */
    java.util.List<BudgetQuota> exportData(Map<String, Object> params);

    /**
     * 获取统计信息
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 根据条件查询配额列表
     *
     * @param wrapper 查询条件
     * @return 配额列表
     */
    java.util.List<BudgetQuota> list(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetQuota> wrapper);
}

