package com.huabo.fxgl.service;

import com.huabo.fxgl.dto.MonthlyEvaluationDTO;
import com.huabo.fxgl.dto.ReminderDTO;
import com.huabo.fxgl.dto.RiskDatabaseDTO;
import com.huabo.fxgl.dto.RiskReviewAnalysisDTO;
import com.huabo.fxgl.dto.RiskEventCountDTO;
import com.huabo.fxgl.dto.RiskMeasureStatusDTO;
import com.huabo.fxgl.dto.RiskCompletionDTO;

import java.util.List;

/**
 * 催办提醒 服务类
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
public interface IReminderService {

    /**
     * 查询未读的催办提醒列表
     *
     * @return 催办提醒列表
     */
    List<ReminderDTO> getUnreadReminderList();

    /**
     * 查询月度评估情况一览表
     *
     * @return 月度评估情况列表
     */
    List<MonthlyEvaluationDTO> getMonthlyEvaluationList();

    /**
     * 查询风险数据库一览表
     *
     * @return 风险数据库一览表列表
     */
    List<RiskDatabaseDTO> getRiskDatabaseList();

    /**
     * 查询风险审查情况分析(近12个月)
     *
     * @return 风险审查情况分析列表
     */
    List<RiskReviewAnalysisDTO> getRiskReviewAnalysis();

    /**
     * 查询风险事件数统计
     *
     * @return 风险事件数统计列表
     */
    List<RiskEventCountDTO> getRiskEventCount();

    /**
     * 查询风险措施状态统计
     *
     * @return 风险措施状态统计
     */
    RiskMeasureStatusDTO getRiskMeasureStatus();

    /**
     * 查询风险完成情况统计
     *
     * @return 风险完成情况统计列表
     */
    List<RiskCompletionDTO> getRiskCompletion();
}
