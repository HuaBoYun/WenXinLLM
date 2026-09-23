package com.management.accountant.service;

import java.util.Map;

/**
 * 预算高级报表Service接口
 * 
 * @description 预算高级报表业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetAdvancedReportService {

    /**
     * 创建自定义报表
     * 
     * @param params 报表参数
     * @return 报表信息
     */
    Map<String, Object> createCustomReport(Map<String, Object> params);

    /**
     * 生成可视化报表
     * 
     * @param params 可视化参数
     * @return 可视化结果
     */
    Map<String, Object> generateVisualization(Map<String, Object> params);

    /**
     * 创建交互式报表
     * 
     * @param params 报表参数
     * @return 报表信息
     */
    Map<String, Object> createInteractiveReport(Map<String, Object> params);

    /**
     * 导出报表
     * 
     * @param params 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportReport(Map<String, Object> params);

    /**
     * 订阅报表
     * 
     * @param params 订阅参数
     * @return 订阅信息
     */
    Map<String, Object> subscribeReport(Map<String, Object> params);

    /**
     * 报表模板管理
     *
     * @param params 模板参数
     * @return 模板信息
     */
    Map<String, Object> manageTemplate(Map<String, Object> params);

    /**
     * 获取高级报表列表
     *
     * @param params 查询参数
     * @return 报表列表
     */
    Map<String, Object> getReportList(Map<String, Object> params);

    /**
     * 获取高级报表统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getReportStats(Map<String, Object> params);

    /**
     * 创建高级报表
     *
     * @param params 报表参数
     * @return 创建结果
     */
    Map<String, Object> createReport(Map<String, Object> params);

    /**
     * 更新高级报表
     *
     * @param reportId 报表ID
     * @param params 报表参数
     */
    void updateReport(String reportId, Map<String, Object> params);

    /**
     * 删除高级报表
     *
     * @param reportId 报表ID
     */
    void deleteReport(String reportId);

    /**
     * 生成高级报表
     *
     * @param reportId 报表ID
     * @return 生成结果
     */
    Map<String, Object> generateReport(String reportId);

    /**
     * 下载生成的报表
     *
     * @param reportId 报表ID
     * @return 下载结果
     */
    Map<String, Object> downloadReport(String reportId);

    /**
     * 复制高级报表
     *
     * @param reportId 报表ID
     * @return 复制结果
     */
    Map<String, Object> copyReport(String reportId);

    /**
     * 导出报表配置
     *
     * @param reportId 报表ID
     * @return 导出结果
     */
    Map<String, Object> exportConfig(String reportId);

    /**
     * 获取报表订阅者
     *
     * @param reportId 报表ID
     * @return 订阅者列表
     */
    Map<String, Object> getSubscribers(String reportId);

    /**
     * 获取报表生成历史
     *
     * @param reportId 报表ID
     * @return 生成历史
     */
    Map<String, Object> getGenerationHistory(String reportId);
}

