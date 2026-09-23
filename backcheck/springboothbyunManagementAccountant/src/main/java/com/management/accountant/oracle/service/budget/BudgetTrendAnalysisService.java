package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetTrendAnalysis;

import java.util.List;

/**
 * 预算趋势分析Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetTrendAnalysisService extends IService<BudgetTrendAnalysis> {

    /**
     * 根据预算年度查询趋势分析列表
     * 
     * @param budgetYear 预算年度
     * @return 趋势分析列表
     */
    List<BudgetTrendAnalysis> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询趋势分析列表
     * 
     * @param organizationId 组织ID
     * @return 趋势分析列表
     */
    List<BudgetTrendAnalysis> listByOrganizationId(String organizationId);

    /**
     * 根据趋势类型查询趋势分析列表
     * 
     * @param trendType 趋势类型
     * @return 趋势分析列表
     */
    List<BudgetTrendAnalysis> listByTrendType(String trendType);

    /**
     * 根据分析状态查询趋势分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 趋势分析列表
     */
    List<BudgetTrendAnalysis> listByAnalysisStatus(String analysisStatus);

    /**
     * 执行趋势分析
     * 
     * @param analysisId 分析ID
     * @return 是否成功
     */
    boolean executeAnalysis(String analysisId);

    /**
     * 批量删除趋势分析
     * 
     * @param analysisIds 分析ID列表
     * @return 是否成功
     */
    boolean batchDeleteAnalyses(List<String> analysisIds);
}

