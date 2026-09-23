package com.management.accountant.service;

import com.management.accountant.vo.result.BudgetAnalysisInsightVO;
import com.management.accountant.vo.result.BudgetAnalysisStatsVO;

import java.util.List;
import java.util.Map;

/**
 * 预算分析统计Service接口
 * 
 * @description 预算分析统计数据服务
 * @author AI Assistant
 * @date 2025-01-30
 */
public interface BudgetAnalysisStatsService {

    /**
     * 获取预算分析统计数据
     * 
     * @return 统计数据
     */
    BudgetAnalysisStatsVO getAnalysisStats();

    /**
     * 获取分析表格数据
     * 
     * @return 表格数据
     */
    List<Map<String, Object>> getAnalysisTableData();

    /**
     * 获取分析洞察
     *
     * @param limit 限制数量
     * @return 洞察列表
     */
    List<BudgetAnalysisInsightVO> getAnalysisInsights(Integer limit);

    /**
     * 获取组织列表
     *
     * @return 组织列表
     */
    List<Map<String, Object>> getOrganizations();

    /**
     * 获取预算科目列表
     *
     * @return 预算科目列表
     */
    List<Map<String, Object>> getBudgetAccounts();

    Map<String, Object> getDashboardStats();

    Map<String, Object> getDashboardData();

    Map<String, Object> getDashboardChartData(Map<String, Object> params);

    /**
     * 执行差异分析（仪表盘快捷操作）
     */
    List<Map<String, Object>> runVarianceAnalysis(String timeRange, double threshold);

    /**
     * 保存仪表盘预警设置
     */
    void saveDashboardAlertSettings(Map<String, Object> params);

    /**
     * 保存仪表盘系统配置
     */
    void saveDashboardSysSettings(Map<String, Object> params);

    /**
     * 保存仪表盘布局配置
     */
    void saveDashboardLayout(Map<String, Object> params);

    /**
     * 获取仪表盘布局配置
     */
    Map<String, Object> getDashboardLayout();

    /**
     * 快速创建分析
     * @param params 分析参数(analysisType, dimension, startDate, endDate)
     * @return 分析结果
     */
    Map<String, Object> quickAnalysis(Map<String, Object> params);
}

