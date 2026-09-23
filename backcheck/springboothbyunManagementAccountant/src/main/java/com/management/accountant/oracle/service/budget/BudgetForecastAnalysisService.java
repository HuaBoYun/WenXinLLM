package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetForecastAnalysis;

import java.util.List;

/**
 * 预算预测分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetForecastAnalysisService extends IService<BudgetForecastAnalysis> {

    /**
     * 根据预算ID查询预测分析列表
     * 
     * @param budgetId 预算ID
     * @return 预测分析列表
     */
    List<BudgetForecastAnalysis> listByBudgetId(String budgetId);

    /**
     * 根据预算年度查询预测分析列表
     * 
     * @param budgetYear 预算年度
     * @return 预测分析列表
     */
    List<BudgetForecastAnalysis> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询预测分析列表
     * 
     * @param organizationId 组织ID
     * @return 预测分析列表
     */
    List<BudgetForecastAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据预测方法查询预测分析列表
     * 
     * @param forecastMethod 预测方法
     * @return 预测分析列表
     */
    List<BudgetForecastAnalysis> listByForecastMethod(String forecastMethod);

    /**
     * 根据预测状态查询预测分析列表
     * 
     * @param forecastStatus 预测状态
     * @return 预测分析列表
     */
    List<BudgetForecastAnalysis> listByForecastStatus(String forecastStatus);

    /**
     * 执行预测分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeForecast(String analysisId);

    /**
     * 批量删除预测分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

