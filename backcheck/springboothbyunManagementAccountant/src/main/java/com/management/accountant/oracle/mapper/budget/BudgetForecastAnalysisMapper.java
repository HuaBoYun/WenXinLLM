package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetForecastAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算预测分析Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetForecastAnalysisMapper extends BaseMapper<BudgetForecastAnalysis> {

    /** 分页查询（配合 PageHelper 使用） */
    List<BudgetForecastAnalysis> selectByPage(Map<String, Object> params);

    /** 统计卡片数据 */
    Map<String, Object> selectStats(Map<String, Object> params);

    /** 预测趋势图数据 */
    List<Map<String, Object>> selectTrendChartData(Map<String, Object> params);

    /** 模型性能图数据 */
    List<Map<String, Object>> selectModelPerformanceData(Map<String, Object> params);

    /** 预测误差分析图数据 */
    List<Map<String, Object>> selectErrorAnalysisData(Map<String, Object> params);

    /** 残差分析图数据 */
    List<Map<String, Object>> selectResidualData(Map<String, Object> params);

    /** 导出查询（不分页） */
    List<BudgetForecastAnalysis> selectForExport(Map<String, Object> params);

    /** 逻辑删除 */
    void logicDeleteById(@Param("id") String id);
}

