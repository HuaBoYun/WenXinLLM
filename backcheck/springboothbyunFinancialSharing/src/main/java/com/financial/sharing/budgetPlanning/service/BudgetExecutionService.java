package com.financial.sharing.budgetPlanning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.budgetPlanning.dto.BudgetExecutionQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetExecution;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 预算执行分析Service接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetExecutionService extends IService<TblBudgetExecution> {

    /**
     * 查询预算执行列表（分页）
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetExecution> getExecutionList(BudgetExecutionQueryParam param);

    /**
     * 根据ID查询预算执行
     * 
     * @param executionId 执行ID
     * @return 执行记录
     */
    TblBudgetExecution getExecutionById(String executionId);

    /**
     * 查询预算执行统计
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getExecutionStatistics(BudgetExecutionQueryParam param);

    /**
     * 查询预算执行趋势
     * 
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> getExecutionTrend(BudgetExecutionQueryParam param);

    /**
     * 查询预算执行预警列表
     * 
     * @param param 查询参数
     * @return 预警列表
     */
    PageInfo<TblBudgetExecution> getExecutionWarnings(BudgetExecutionQueryParam param);

    /**
     * 刷新预算执行数据
     * 
     * @param param 查询参数
     * @return 刷新结果
     */
    Map<String, Object> refreshExecutionData(BudgetExecutionQueryParam param);

    /**
     * 计算预算执行指标
     * 
     * @param execution 执行记录
     */
    void calculateExecutionMetrics(TblBudgetExecution execution);
}

