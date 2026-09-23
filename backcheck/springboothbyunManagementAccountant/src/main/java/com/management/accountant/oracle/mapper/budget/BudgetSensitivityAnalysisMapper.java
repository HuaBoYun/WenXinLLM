package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetSensitivityAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算敏感性分析Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetSensitivityAnalysisMapper extends BaseMapper<BudgetSensitivityAnalysis> {

    /**
     * 根据预算ID查询敏感性分析列表
     * 
     * @param budgetId 预算ID
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据预算年度查询敏感性分析列表
     * 
     * @param budgetYear 预算年度
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据组织ID查询敏感性分析列表
     * 
     * @param organizationId 组织ID
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据敏感度等级查询敏感性分析列表
     * 
     * @param sensitivityLevel 敏感度等级
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> selectBySensitivityLevel(@Param("sensitivityLevel") String sensitivityLevel);

    /**
     * 根据分析状态查询敏感性分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 敏感性分析列表
     */
    List<BudgetSensitivityAnalysis> selectByAnalysisStatus(@Param("analysisStatus") String analysisStatus);
}

