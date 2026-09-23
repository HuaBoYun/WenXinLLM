package com.management.accountant.service;

import java.util.Map;

/**
 * 预算场景分析Service接口
 *
 * @description 预算场景分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetScenarioAnalysisService {

    /** 分页查询场景分析列表 */
    Map<String, Object> getPage(Map<String, Object> params);

    /** 创建场景分析（写入数据库） */
    Map<String, Object> createScenario(Map<String, Object> params);

    /** 更新场景分析 */
    Map<String, Object> updateScenario(Map<String, Object> params);

    /** 删除场景分析 */
    void deleteScenario(String id);

    /** 复制场景分析 */
    Map<String, Object> copyScenario(String id);

    /** 执行场景分析 */
    Map<String, Object> executeScenarioAnalysis(Map<String, Object> params);

    /** 对比场景 */
    Map<String, Object> compareScenarios(Map<String, Object> params);

    /** 获取统计数据 */
    Map<String, Object> getScenarioStats();

    /** 获取图表数据 */
    Map<String, Object> getScenarioChartData(Map<String, Object> params);

    /** 获取组织选项列表 */
    Object getOrganizations();
}

