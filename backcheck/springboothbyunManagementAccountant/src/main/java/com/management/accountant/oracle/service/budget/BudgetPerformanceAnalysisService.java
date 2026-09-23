package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetPerformanceAnalysis;

import java.util.List;

/**
 * 预算绩效分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetPerformanceAnalysisService extends IService<BudgetPerformanceAnalysis> {

    /**
     * 根据预算ID查询绩效分析列表
     * 
     * @param budgetId 预算ID
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> listByBudgetId(String budgetId);

    /**
     * 根据预算年度查询绩效分析列表
     * 
     * @param budgetYear 预算年度
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询绩效分析列表
     * 
     * @param organizationId 组织ID
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据绩效等级查询绩效分析列表
     * 
     * @param performanceLevel 绩效等级
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> listByPerformanceLevel(String performanceLevel);

    /**
     * 根据分析状态查询绩效分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> listByAnalysisStatus(String analysisStatus);

    /**
     * 执行绩效分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeAnalysis(String analysisId);

    /**
     * 批量删除绩效分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

