package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetScenarioAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算场景分析Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetScenarioAnalysisMapper extends BaseMapper<BudgetScenarioAnalysis> {

    /**
     * 根据预算ID查询场景分析列表
     * 
     * @param budgetId 预算ID
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据预算年度查询场景分析列表
     * 
     * @param budgetYear 预算年度
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据组织ID查询场景分析列表
     * 
     * @param organizationId 组织ID
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据场景类型查询场景分析列表
     * 
     * @param scenarioType 场景类型
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> selectByScenarioType(@Param("scenarioType") String scenarioType);

    /**
     * 根据分析状态查询场景分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 场景分析列表
     */
    List<BudgetScenarioAnalysis> selectByAnalysisStatus(@Param("analysisStatus") String analysisStatus);
}

