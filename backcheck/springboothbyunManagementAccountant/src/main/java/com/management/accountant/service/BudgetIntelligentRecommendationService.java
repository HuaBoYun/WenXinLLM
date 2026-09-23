package com.management.accountant.service;

import java.util.Map;

/**
 * 预算智能推荐Service接口
 *
 * @description 预算智能推荐业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntelligentRecommendationService {

    /**
     * 获取预算推荐
     *
     * @param params 推荐参数
     * @return 推荐结果
     */
    Map<String, Object> getRecommendation(Map<String, Object> params);

    /**
     * 分析历史数据
     *
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> analyzeHistory(Map<String, Object> params);

    /**
     * 生成智能建议
     *
     * @param params 建议参数
     * @return 建议结果
     */
    Map<String, Object> generateSuggestions(Map<String, Object> params);

    /**
     * 预测预算趋势
     *
     * @param params 预测参数
     * @return 预测结果
     */
    Map<String, Object> predictTrend(Map<String, Object> params);

    /**
     * 优化预算分配
     *
     * @param params 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeAllocation(Map<String, Object> params);

    /**
     * 应用推荐
     *
     * @param params 应用参数
     * @return 应用结果
     */
    Map<String, Object> applyRecommendation(Map<String, Object> params);

    /**
     * 删除推荐
     *
     * @param recommendationId 推荐ID
     */
    void deleteRecommendation(String recommendationId);

    /**
     * 分享推荐
     *
     * @param params 分享参数
     * @return 分享结果
     */
    Map<String, Object> shareRecommendation(Map<String, Object> params);

    /**
     * 获取推荐反馈
     *
     * @param params 查询参数
     * @return 反馈结果
     */
    Map<String, Object> getRecommendationFeedback(Map<String, Object> params);

    /**
     * 获取智能推荐列表
     *
     * @param params 查询参数
     * @return 推荐列表
     */
    Map<String, Object> getRecommendationList(Map<String, Object> params);

    /**
     * 获取智能推荐统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getRecommendationStats(Map<String, Object> params);

    /**
     * 生成智能推荐
     *
     * @param params 生成参数
     * @return 生成结果
     */
    Map<String, Object> generateRecommendation(Map<String, Object> params);

    /**
     * 采纳推荐
     *
     * @param recommendationId 推荐ID
     * @return 采纳结果
     */
    Map<String, Object> acceptRecommendation(String recommendationId);

    /**
     * 拒绝推荐
     *
     * @param recommendationId 推荐ID
     * @return 拒绝结果
     */
    Map<String, Object> rejectRecommendation(String recommendationId);

    /**
     * 更新推荐
     * @param params 更新参数（包含 recommendationId）
     * @return 更新结果
     */
    Map<String, Object> updateRecommendation(Map<String, Object> params);

    /**
     * 获取推荐反馈（按ID）
     *
     * @param recommendationId 推荐ID
     * @return 反馈结果
     */
    Map<String, Object> getRecommendationFeedbackById(String recommendationId);

    /**
     * 训练模型
     * @param params 训练参数
     * @return 训练结果
     */
    Map<String, Object> trainModel(Map<String, Object> params);

    /**
     * 推荐分析
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> getRecommendationAnalysis(Map<String, Object> params);
}

