package com.management.accountant.service;

import java.util.Map;

/**
 * 预算版本对比Service接口
 * 
 * @description 预算版本对比业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetVersionComparisonService {

    /**
     * 版本对比
     * 
     * @param params 对比参数
     * @return 对比结果
     */
    Map<String, Object> compareVersions(Map<String, Object> params);

    /**
     * 差异分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> analyzeDifference(Map<String, Object> params);

    /**
     * 变更追踪
     * 
     * @param params 追踪参数
     * @return 追踪结果
     */
    Map<String, Object> trackChanges(Map<String, Object> params);

    /**
     * 历史查询
     * 
     * @param params 查询参数
     * @return 历史记录
     */
    Map<String, Object> queryHistory(Map<String, Object> params);

    /**
     * 生成对比报告
     *
     * @param params 报告参数
     * @return 报告信息
     */
    Map<String, Object> generateComparisonReport(Map<String, Object> params);

    /**
     * 获取版本对比日志
     *
     * @param comparisonId 对比ID
     * @return 日志列表
     */
    Map<String, Object> getComparisonLogs(String comparisonId);

    /**
     * 导出版本对比结果
     *
     * @param comparisonId 对比ID
     * @return 导出结果
     */
    Map<String, Object> exportComparison(String comparisonId);

    /**
     * 删除版本对比
     *
     * @param comparisonId 对比ID
     */
    void deleteComparison(String comparisonId);

    /**
     * 复制版本对比
     *
     * @param comparisonId 对比ID
     * @return 复制结果
     */
    Map<String, Object> copyComparison(String comparisonId);

    /**
     * 合并版本
     *
     * @param params 合并参数
     * @return 合并结果
     */
    Map<String, Object> mergeVersions(Map<String, Object> params);

    /**
     * 回滚版本
     *
     * @param params 回滚参数
     * @return 回滚结果
     */
    Map<String, Object> rollbackVersion(Map<String, Object> params);

    /**
     * 获取历史版本列表
     *
     * @param params 查询参数
     * @return 历史版本列表
     */
    Map<String, Object> getHistoryVersions(Map<String, Object> params);

    /**
     * 获取版本对比列表
     *
     * @param params 查询参数
     * @return 对比列表
     */
    Map<String, Object> getComparisonList(Map<String, Object> params);

    /**
     * 获取版本对比统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getComparisonStats(Map<String, Object> params);

    /**
     * 获取可用版本列表
     *
     * @return 可用版本列表
     */
    Map<String, Object> getAvailableVersions();

    /**
     * 创建版本对比
     *
     * @param params 对比参数
     * @return 创建结果
     */
    Map<String, Object> createComparison(Map<String, Object> params);

    /**
     * 重新对比
     *
     * @param comparisonId 对比ID
     * @return 重新对比结果
     */
    Map<String, Object> recompare(String comparisonId);

    /**
     * 获取对比差异
     *
     * @param comparisonId 对比ID
     * @return 差异列表
     */
    Map<String, Object> getDifferences(String comparisonId);

    /**
     * 获取版本历史（分页）
     *
     * @param params 查询参数
     * @return 版本历史
     */
    Map<String, Object> getVersionHistory(Map<String, Object> params);
}

