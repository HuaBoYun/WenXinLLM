package com.huabo.monitor.service;

import com.huabo.monitor.util.JsonBean;

import java.util.Map;

/**
 * 大屏展示数据 Service
 */
public interface ScreenDisplayService {

    /**
     * 各部门评价项目数统计
     * @return 返回各部门的评价项目数量统计结果
     */
    JsonBean<Map<String, Object>> getDepartmentProjectCount();

    /**
     * 控制有效性数据查询
     * @return 返回控制有效性数据列表
     */
    JsonBean<Map<String, Object>> getControlEffectivenessData();

    /**
     * 各部门评价项目数占比统计
     * @return 返回各部门的评价项目数量占比
     */
    JsonBean<Map<String, Object>> getDepartmentProjectRatio();

    /**
     * 缺陷类型分布统计
     * @return 返回各缺陷类型的数量分布
     */
    JsonBean<Map<String, Object>> getDefectTypeDistribution();

    /**
     * 各单位缺陷数量对比分析
     * @return 返回各单位的缺陷数量对比数据
     */
    JsonBean<Map<String, Object>> getDepartmentDefectComparison();

    /**
     * 本年缺陷项目趋势分析
     * @return 返回近12个月的缺陷数量趋势数据
     */
    JsonBean<Map<String, Object>> getDefectTrendAnalysis();

    /**
     * 缺陷属性分布统计
     * @return 返回各缺陷属性的数量分布
     */
    JsonBean<Map<String, Object>> getDefectPropertyDistribution();
}

