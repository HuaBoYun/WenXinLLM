package com.management.accountant.service;

import java.util.Map;

/**
 * 预算风险评估Service接口
 * 
 * @description 预算风险评估业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetRiskAssessmentService {

    /**
     * 风险识别
     * 
     * @param params 识别参数
     * @return 风险列表
     */
    Map<String, Object> identifyRisks(Map<String, Object> params);

    /**
     * 风险评估
     * 
     * @param params 评估参数
     * @return 评估结果
     */
    Map<String, Object> assessRisk(Map<String, Object> params);

    /**
     * 风险预警
     * 
     * @param params 预警参数
     * @return 预警信息
     */
    Map<String, Object> riskAlert(Map<String, Object> params);

    /**
     * 风险应对
     * 
     * @param params 应对参数
     * @return 应对方案
     */
    Map<String, Object> respondToRisk(Map<String, Object> params);

    /**
     * 风险监控
     *
     * @param params 监控参数
     * @return 监控信息
     */
    Map<String, Object> monitorRisk(Map<String, Object> params);

    /**
     * 获取风险评估列表
     *
     * @param params 查询参数
     * @return 评估列表
     */
    Map<String, Object> getRiskAssessmentList(Map<String, Object> params);

    /**
     * 获取风险评估统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getRiskAssessmentStats(Map<String, Object> params);

    /**
     * 导出风险评估
     *
     * @param assessmentId 评估ID
     * @return 导出结果
     */
    Map<String, Object> exportRiskAssessment(String assessmentId);

    /**
     * 复制风险评估
     *
     * @param assessmentId 评估ID
     * @return 复制结果
     */
    Map<String, Object> copyRiskAssessment(String assessmentId);

    /**
     * 创建风险评估
     *
     * @param params 评估参数
     * @return 创建结果
     */
    Map<String, Object> createRiskAssessment(Map<String, Object> params);

    /**
     * 更新风险评估
     *
     * @param params 评估参数
     * @return 更新结果
     */
    Map<String, Object> updateRiskAssessment(Map<String, Object> params);

    /**
     * 删除风险评估
     *
     * @param assessmentId 评估ID
     */
    void deleteRiskAssessment(String assessmentId);
}

