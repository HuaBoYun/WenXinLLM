package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAnalysisChart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算分析图表 Mapper
 */
@Mapper
public interface BudgetAnalysisChartMapper extends BaseMapper<BudgetAnalysisChart> {

    /** 统计各类型图表数量 */
    Map<String, Object> selectChartStats();

    /** 按类型分组统计 */
    List<Map<String, Object>> selectChartTypeStats();

    /** 分页查询（带筛选） */
    List<BudgetAnalysisChart> selectChartPage(Map<String, Object> params);

    /** 分页查询总数 */
    int selectChartPageCount(Map<String, Object> params);
}
