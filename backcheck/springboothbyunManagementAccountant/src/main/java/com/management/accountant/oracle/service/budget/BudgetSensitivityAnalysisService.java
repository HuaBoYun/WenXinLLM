package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetSensitivityAnalysis;

import java.util.List;

/**
 * 预算敏感性分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetSensitivityAnalysisService extends IService<BudgetSensitivityAnalysis> {

    /**
     * 根据预算ID查询敏感性分析列表
     * 
     * @param budgetId 预算ID
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> listByBudgetId(String budgetId);

    /**
     * 根据预算年度查询敏感性分析列表
     * 
     * @param budgetYear 预算年度
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询敏感性分析列表
     * 
     * @param organizationId 组织ID
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据敏感度等级查询敏感性分析列表
     * 
     * @param sensitivityLevel 敏感度等级
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> listBySensitivityLevel(String sensitivityLevel);

    /**
     * 根据分析状态查询敏感性分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> listByAnalysisStatus(String analysisStatus);

    /**
     * 执行敏感性分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeAnalysis(String analysisId);

    /**
     * 批量删除敏感性分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

