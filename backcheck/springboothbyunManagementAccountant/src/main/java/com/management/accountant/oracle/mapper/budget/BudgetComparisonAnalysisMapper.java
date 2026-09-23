package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetComparisonAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算对比分析Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetComparisonAnalysisMapper extends BaseMapper<BudgetComparisonAnalysis> {

    /**
     * 根据基准预算ID查询对比分析列表
     * 
     * @param baseBudgetId 基准预算ID
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> selectByBaseBudgetId(@Param("baseBudgetId") String baseBudgetId);

    /**
     * 根据对比类型查询对比分析列表
     * 
     * @param comparisonType 对比类型
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> selectByComparisonType(@Param("comparisonType") String comparisonType);

    /**
     * 根据组织ID查询对比分析列表
     * 
     * @param organizationId 组织ID
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据分析状态查询对比分析列表
     * 
     * @param analysisStatus 分析状态
     * @return 对比分析列表
     */
    List<BudgetComparisonAnalysis> selectByAnalysisStatus(@Param("analysisStatus") String analysisStatus);
}

