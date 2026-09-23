package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetTrendAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算趋势分析Mapper接口
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetTrendAnalysisMapper extends BaseMapper<BudgetTrendAnalysis> {

    List<BudgetTrendAnalysis> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    List<BudgetTrendAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId);

    List<BudgetTrendAnalysis> selectByTrendType(@Param("trendType") String trendType);

    List<BudgetTrendAnalysis> selectByAnalysisStatus(@Param("analysisStatus") String analysisStatus);

    /** 分页查询（配合 PageHelper 使用） */
    List<BudgetTrendAnalysis> selectByPage(Map<String, Object> params);

    /** 统计卡片数据 */
    Map<String, Object> selectStats();

    /** 主趋势图：按月/季/年汇总预算金额、实际金额、执行率 */
    List<Map<String, Object>> selectMainChartData(Map<String, Object> params);

    /** 增长率趋势图：按期间计算环比增长率 */
    List<Map<String, Object>> selectGrowthChartData(Map<String, Object> params);

    /** 波动率分析图：按期间计算执行率波动 */
    List<Map<String, Object>> selectVolatilityChartData(Map<String, Object> params);

    /** 预测分析图：历史执行率 + 简单线性预测 */
    List<Map<String, Object>> selectForecastChartData(Map<String, Object> params);

    /** 导出查询（不分页） */
    List<BudgetTrendAnalysis> selectForExport(Map<String, Object> params);

    /** 逻辑删除（直接更新 DEL_FLAG，绕过 @TableLogic 对 updateById 的限制） */
    void logicDeleteById(@Param("id") String id);
}

