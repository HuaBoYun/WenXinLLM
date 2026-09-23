package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetRelease;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算释放Service接口
 * 
 * @description 预算释放业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetReleaseService {

    /**
     * 创建释放申请
     * 
     * @param release 释放对象
     * @return 创建后的释放对象
     */
    BudgetRelease create(BudgetRelease release);

    /**
     * 根据ID查询释放
     * 
     * @param releaseId 释放ID
     * @return 释放对象
     */
    BudgetRelease getById(String releaseId);

    /**
     * 更新释放申请
     * 
     * @param release 释放对象
     */
    void update(BudgetRelease release);

    /**
     * 删除释放申请
     * 
     * @param releaseId 释放ID
     */
    void delete(String releaseId);

    /**
     * 分页查询释放列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetRelease> getPage(Map<String, Object> params);

    /**
     * 审批释放申请
     * 
     * @param params 审批参数
     */
    void approve(Map<String, Object> params);

    /**
     * 执行释放
     * 
     * @param releaseId 释放ID
     */
    void execute(String releaseId);

    /**
     * 批量释放
     *
     * @param params 批量参数
     * @return 批量结果
     */
    Map<String, Object> batchRelease(Map<String, Object> params);

    /**
     * 导出释放数据
     *
     * @param params 查询参数
     * @return 释放数据列表
     */
    java.util.List<BudgetRelease> exportData(Map<String, Object> params);

    /**
     * 获取统计信息
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 根据ID更新释放记录
     *
     * @param release 释放对象
     * @return 是否成功
     */
    boolean updateById(BudgetRelease release);
}

