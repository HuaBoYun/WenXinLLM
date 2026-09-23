package com.huabo.contract.service;

import com.github.pagehelper.PageInfo;
import com.huabo.contract.vo.RiskWarningQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 风险预警服务接口
 * 提供智能风险识别和预警功能
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface RiskWarningService {

    /**
     * 获取风险预警列表
     *
     * @param param 查询参数
     * @return 预警列表
     */
    PageInfo<Map<String, Object>> getRiskWarningList(RiskWarningQueryParam param);

    /**
     * 获取风险预警统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getRiskWarningStatistics();

    /**
     * 获取高风险项目列表
     *
     * @return 高风险项目列表
     */
    List<Map<String, Object>> getHighRiskProjects();

    /**
     * 获取风险趋势分析
     *
     * @param months 分析月数
     * @return 趋势数据
     */
    Map<String, Object> getRiskTrendAnalysis(Integer months);

    /**
     * 执行风险自动评估
     *
     * @param projectId 项目ID，为空时评估所有项目
     * @return 评估结果
     */
    Map<String, Object> executeAutoRiskAssessment(Long projectId);

    /**
     * 获取相对方风险评估
     *
     * @param counterpartId 相对方ID
     * @return 风险评估信息
     */
    Map<String, Object> getCounterpartRiskAssessment(Long counterpartId);

    /**
     * 生成风险评估报告
     *
     * @param assessmentId 评估ID
     * @return 报告内容
     */
    Map<String, Object> generateRiskAssessmentReport(Long assessmentId);

    /**
     * 获取风险预警配置
     *
     * @return 配置信息
     */
    Map<String, Object> getRiskWarningConfig();

    /**
     * 更新风险预警配置
     *
     * @param config 配置参数
     * @return 更新结果
     */
    boolean updateRiskWarningConfig(Map<String, Object> config);

    /**
     * 处理风险预警
     *
     * @param warningId 预警ID
     * @param action 处理动作
     * @param remarks 处理备注
     * @return 处理结果
     */
    boolean handleRiskWarning(Long warningId, Integer action, String remarks);

    /**
     * 获取风险评估模板
     *
     * @param assessmentType 评估类型
     * @return 评估模板
     */
    Map<String, Object> getRiskAssessmentTemplate(Integer assessmentType);

    /**
     * 导出风险评估报告
     *
     * @param assessmentId 评估ID
     * @return 导出结果
     */
    Map<String, Object> exportRiskAssessmentReport(Long assessmentId);

    /**
     * 智能风险识别
     * 基于历史数据和规则引擎识别潜在风险
     *
     * @param projectId 项目ID
     * @return 识别结果
     */
    Map<String, Object> intelligentRiskIdentification(Long projectId);

    /**
     * 风险评分计算
     * 根据多维度指标计算综合风险评分
     *
     * @param assessmentId 评估ID
     * @return 评分结果
     */
    Map<String, Object> calculateRiskScore(Long assessmentId);

    /**
     * 风险预警推送
     * 向相关人员推送风险预警信息
     *
     * @param warningType 预警类型
     * @param projectId 项目ID
     * @param message 预警消息
     * @return 推送结果
     */
    boolean pushRiskWarning(String warningType, Long projectId, String message);

    /**
     * 获取风险缓解建议
     * 基于风险类型和严重程度提供缓解建议
     *
     * @param riskCategory 风险类别
     * @param riskLevel 风险等级
     * @return 缓解建议
     */
    List<String> getRiskMitigationSuggestions(Integer riskCategory, Integer riskLevel);

    /**
     * 风险监控
     * 持续监控项目风险状态变化
     *
     * @param projectId 项目ID
     * @return 监控结果
     */
    Map<String, Object> monitorProjectRisk(Long projectId);

    /**
     * 生成风险热力图数据
     * 用于可视化展示风险分布
     *
     * @return 热力图数据
     */
    Map<String, Object> generateRiskHeatmapData();

    /**
     * 获取风险预警历史记录
     *
     * @param projectId 项目ID
     * @param days 查询天数
     * @return 历史记录
     */
    List<Map<String, Object>> getRiskWarningHistory(Long projectId, Integer days);

    /**
     * 风险评估质量检查
     * 检查风险评估的完整性和准确性
     *
     * @param assessmentId 评估ID
     * @return 检查结果
     */
    Map<String, Object> checkAssessmentQuality(Long assessmentId);

    /**
     * 批量风险评估
     * 对多个项目进行批量风险评估
     *
     * @param projectIds 项目ID列表
     * @return 评估结果
     */
    Map<String, Object> batchRiskAssessment(List<Long> projectIds);

    /**
     * 获取风险评估对比分析
     * 对比不同时期或不同项目的风险评估结果
     *
     * @param assessmentIds 评估ID列表
     * @return 对比分析结果
     */
    Map<String, Object> compareRiskAssessments(List<Long> assessmentIds);

    /**
     * 风险预测分析
     * 基于历史数据预测未来风险趋势
     *
     * @param projectId 项目ID
     * @param predictDays 预测天数
     * @return 预测结果
     */
    Map<String, Object> predictRiskTrend(Long projectId, Integer predictDays);

    /**
     * 获取行业风险基准
     * 获取同行业项目的风险基准数据
     *
     * @param industryType 行业类型
     * @return 基准数据
     */
    Map<String, Object> getIndustryRiskBenchmark(String industryType);

    /**
     * 风险评估审核
     * 对风险评估结果进行审核
     *
     * @param assessmentId 评估ID
     * @param reviewerId 审核人ID
     * @param reviewComments 审核意见
     * @param approved 是否通过
     * @return 审核结果
     */
    boolean reviewRiskAssessment(Long assessmentId, Long reviewerId, String reviewComments, Boolean approved);

    /**
     * 获取风险评估审核历史
     *
     * @param assessmentId 评估ID
     * @return 审核历史
     */
    List<Map<String, Object>> getAssessmentReviewHistory(Long assessmentId);

    /**
     * 风险评估模型训练
     * 基于历史数据训练风险评估模型
     *
     * @return 训练结果
     */
    Map<String, Object> trainRiskAssessmentModel();

    /**
     * 获取风险评估准确性统计
     * 统计风险评估的准确性指标
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 准确性统计
     */
    Map<String, Object> getAssessmentAccuracyStats(String startDate, String endDate);
}
