package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetExecution;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算执行分析Service接口
 * 
 * @description 预算执行分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetExecutionService {

    /**
     * 创建执行记录
     * 
     * @param execution 执行对象
     * @return 创建后的执行对象
     */
    BudgetExecution create(BudgetExecution execution);

    /**
     * 更新执行记录
     *
     * @param execution 执行对象
     * @return 更新后的执行对象
     */
    BudgetExecution update(BudgetExecution execution);

    /**
     * 根据ID查询执行记录
     * 
     * @param executionId 执行ID
     * @return 执行对象
     */
    BudgetExecution getById(String executionId);

    /**
     * 分页查询执行列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetExecution> getPage(Map<String, Object> params);

    /**
     * 获取执行进度分析
     * 
     * @param params 分析参数
     * @return 进度分析结果
     */
    Map<String, Object> getProgressAnalysis(Map<String, Object> params);

    /**
     * 获取执行偏差分析
     * 
     * @param params 分析参数
     * @return 偏差分析结果
     */
    Map<String, Object> getVarianceAnalysis(Map<String, Object> params);

    /**
     * 获取执行趋势分析
     * 
     * @param params 分析参数
     * @return 趋势分析结果
     */
    Map<String, Object> getTrendAnalysis(Map<String, Object> params);

    /**
     * 获取执行统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 生成执行报表
     * 
     * @param params 报表参数
     * @return 报表数据
     */
    Map<String, Object> generateReport(Map<String, Object> params);

    /**
     * 获取预算执行率
     * 
     * @param budgetId 预算ID
     * @return 执行率
     */
    Map<String, Object> getExecutionRate(String budgetId);

    /**
     * 获取部门执行情况
     * 
     * @param departmentId 部门ID
     * @return 执行情况
     */
    Map<String, Object> getDepartmentExecution(String departmentId);

    Map<String, Object> getExecutionStats();

    Map<String, Object> getExecutionChartData(Map<String, Object> params);

    Map<String, Object> getExecutionRanking();

    Map<String, Object> getImprovementSuggestions();

    java.util.List<BudgetExecution> list();

    java.util.List<BudgetExecution> list(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetExecution> wrapper);

    java.util.List<BudgetExecution> listByIds(java.util.Collection<String> ids);
}

