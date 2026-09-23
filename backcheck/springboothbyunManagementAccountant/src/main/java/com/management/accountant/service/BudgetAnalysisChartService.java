package com.management.accountant.service;

import java.util.List;
import java.util.Map;

/**
 * 预算分析图表 Service 接口
 */
public interface BudgetAnalysisChartService {

    /** 获取图表统计数据 */
    Map<String, Object> getChartStats();

    /** 获取图表类型列表（含使用统计） */
    List<Map<String, Object>> getChartTypes();

    /** 获取图表库列表 */
    List<Map<String, Object>> getChartLibrary();

    /** 分页查询图表 */
    Map<String, Object> getChartPage(Map<String, Object> params);

    /** 创建图表 */
    Map<String, Object> createChart(Map<String, Object> data);

    /** 更新图表 */
    Map<String, Object> updateChart(Map<String, Object> data);

    /** 删除图表 */
    void deleteChart(String chartId);

    /** 获取图表详情 */
    Map<String, Object> getChartDetail(String chartId);

    /** 获取图表数据（用于渲染ECharts） */
    Map<String, Object> getChartData(Map<String, Object> params);
}
