package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetPerformanceAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算绩效分析Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetPerformanceAnalysisMapper extends BaseMapper<BudgetPerformanceAnalysis> {

    /**
     * 根据预算ID查询绩效分析列表
     * 
     * @param budgetId 预算ID
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据预算年度查询绩效分析列表
     * 
     * @param budgetYear 预算年度
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据组织ID查询绩效分析列表
     * 
     * @param organizationId 组织ID
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据绩效等级查询绩效分析列表
     * 
     * @param performanceLevel 绩效等级
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> selectByPerformanceLevel(@Param("performanceLevel") String performanceLevel);

    /**
     * 根据分析状态查询绩效分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 绩效分析列表
     */
    List<BudgetPerformanceAnalysis> selectByAnalysisStatus(@Param("analysisStatus") String analysisStatus);
}

