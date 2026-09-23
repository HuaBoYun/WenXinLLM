package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetRolling;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 滚动预算Service接口
 *
 * @description 滚动预算业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetRollingService {

    /**
     * 创建滚动预算
     *
     * @param rolling 滚动预算对象
     * @return 创建后的滚动预算对象
     */
    BudgetRolling create(BudgetRolling rolling);

    /**
     * 根据ID查询滚动预算
     *
     * @param rollingId 滚动预算ID
     * @return 滚动预算对象
     */
    BudgetRolling getById(String rollingId);

    /**
     * 更新滚动预算
     *
     * @param rolling 滚动预算对象
     */
    void update(BudgetRolling rolling);

    /**
     * 删除滚动预算
     *
     * @param rollingId 滚动预算ID
     */
    void delete(String rollingId);

    /**
     * 分页查询滚动预算列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetRolling> getPage(Map<String, Object> params);

    /**
     * 执行滚动
     *
     * @param rollingId 滚动预算ID
     * @return 执行结果
     */
    Map<String, Object> executeRolling(String rollingId);

    /**
     * 滚动调整
     *
     * @param params 调整参数
     */
    void adjustRolling(Map<String, Object> params);

    /**
     * 获取滚动计划列表
     *
     * @param params 查询参数
     * @return 计划列表
     */
    Map<String, Object> getPlanList(Map<String, Object> params);

    /**
     * 获取滚动统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getStats();

    /**
     * 复制滚动计划
     *
     * @param rollingId 滚动预算ID
     * @return 复制后的滚动预算对象
     */
    BudgetRolling copy(String rollingId);

    /**
     * 暂停滚动计划
     *
     * @param rollingId 滚动预算ID
     */
    void pause(String rollingId);

    /**
     * 恢复滚动计划
     *
     * @param rollingId 滚动预算ID
     */
    void resume(String rollingId);

    /**
     * 获取滚动执行记录
     *
     * @param rollingId 滚动预算ID
     * @return 执行记录
     */
    Map<String, Object> getExecutionRecords(String rollingId);

    Map<String, Object> getRollingChartData(Map<String, Object> params);
}

