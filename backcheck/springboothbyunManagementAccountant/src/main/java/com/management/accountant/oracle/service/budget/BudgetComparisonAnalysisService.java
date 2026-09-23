package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetComparisonAnalysis;

import java.util.List;

/**
 * 预算对比分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetComparisonAnalysisService extends IService<BudgetComparisonAnalysis> {

    /**
     * 根据基准预算ID查询对比分析列表
     * 
     * @param baseBudgetId 基准预算ID
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> listByBaseBudgetId(String baseBudgetId);

    /**
     * 根据对比类型查询对比分析列表
     * 
     * @param comparisonType 对比类型
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> listByComparisonType(String comparisonType);

    /**
     * 根据组织ID查询对比分析列表
     * 
     * @param organizationId 组织ID
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据分析状态查询对比分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> listByAnalysisStatus(String analysisStatus);

    /**
     * 执行对比分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeAnalysis(String analysisId);

    /**
     * 批量删除对比分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

