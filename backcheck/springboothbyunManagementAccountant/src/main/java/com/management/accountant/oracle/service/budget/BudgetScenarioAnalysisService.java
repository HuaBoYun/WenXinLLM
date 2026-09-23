package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetScenarioAnalysis;

import java.util.List;

/**
 * 预算场景分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetScenarioAnalysisService extends IService<BudgetScenarioAnalysis> {

    /**
     * 根据预算ID查询场景分析列表
     * 
     * @param budgetId 预算ID
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> listByBudgetId(String budgetId);

    /**
     * 根据预算年度查询场景分析列表
     * 
     * @param budgetYear 预算年度
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询场景分析列表
     * 
     * @param organizationId 组织ID
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据场景类型查询场景分析列表
     * 
     * @param scenarioType 场景类型
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> listByScenarioType(String scenarioType);

    /**
     * 根据分析状态查询场景分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> listByAnalysisStatus(String analysisStatus);

    /**
     * 执行场景分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeAnalysis(String analysisId);

    /**
     * 批量删除场景分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

