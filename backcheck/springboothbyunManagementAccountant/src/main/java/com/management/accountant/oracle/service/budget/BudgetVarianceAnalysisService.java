package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetVarianceAnalysis;

import java.util.List;

/**
 * 预算差异分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetVarianceAnalysisService extends IService<BudgetVarianceAnalysis> {

    /**
     * 根据预算ID查询差异分析列表
     * 
     * @param budgetId 预算ID
     * @return 差异分析列表
     */
    List<BudgetVarianceAnalysis> listByBudgetId(String budgetId);

    /**
     * 根据预算年度查询差异分析列表
     * 
     * @param budgetYear 预算年度
     * @return 差异分析列表
     */
    List<BudgetVarianceAnalysis> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询差异分析列表
     * 
     * @param organizationId 组织ID
     * @return 差异分析列表
     */
    List<BudgetVarianceAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据差异类型查询差异分析列表
     * 
     * @param varianceType 差异类型
     * @return 差异分析列表
     */
    List<BudgetVarianceAnalysis> listByVarianceType(String varianceType);

    /**
     * 根据分析状态查询差异分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 差异分析列表
     */
    List<BudgetVarianceAnalysis> listByAnalysisStatus(String analysisStatus);

    /**
     * 执行差异分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeAnalysis(String analysisId);

    /**
     * 批量删除差异分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

