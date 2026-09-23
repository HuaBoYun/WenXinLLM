package com.management.accountant.service.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.pm.PmAssessmentPlan;

import java.util.List;
import java.util.Map;

/**
 * 考核方案配置服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface PmAssessmentPlanService {

    /**
     * 分页查询考核方案
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param organizationId 组织ID
     * @param planType 方案类型
     * @param assessmentMode 考核模式
     * @param planStatus 方案状态
     * @param assessmentYear 考核年度
     * @param keyword 关键词
     * @return 分页结果
     */
    IPage<PmAssessmentPlan> queryAssessmentPlanPage(Long current, Long size, Long organizationId,
                                                   String planType, String assessmentMode, String planStatus,
                                                   Integer assessmentYear, String keyword);

    /**
     * 创建考核方案
     * 
     * @param assessmentPlan 考核方案信息
     * @return 创建结果
     */
    boolean createAssessmentPlan(PmAssessmentPlan assessmentPlan);

    /**
     * 更新考核方案
     * 
     * @param assessmentPlan 考核方案信息
     * @return 更新结果
     */
    boolean updateAssessmentPlan(PmAssessmentPlan assessmentPlan);

    /**
     * 删除考核方案
     * 
     * @param planId 方案ID
     * @return 删除结果
     */
    boolean deleteAssessmentPlan(Long planId);

    /**
     * 根据ID查询考核方案
     * 
     * @param planId 方案ID
     * @return 考核方案信息
     */
    PmAssessmentPlan getAssessmentPlanById(Long planId);

    /**
     * 配置考核指标
     * 
     * @param planId 方案ID
     * @param indicatorConfig 指标配置
     * @return 配置结果
     */
    Map<String, Object> configureIndicators(Long planId, Map<String, Object> indicatorConfig);

    /**
     * 设置评分规则
     * 
     * @param planId 方案ID
     * @param scoringRules 评分规则
     * @return 设置结果
     */
    boolean setScoringRules(Long planId, Map<String, Object> scoringRules);

    /**
     * 配置考核流程
     * 
     * @param planId 方案ID
     * @param processConfig 流程配置
     * @return 配置结果
     */
    Map<String, Object> configureProcess(Long planId, Map<String, Object> processConfig);

    /**
     * 设置权重配置
     * 
     * @param planId 方案ID
     * @param weightConfig 权重配置
     * @return 设置结果
     */
    boolean setWeightConfig(Long planId, Map<String, Object> weightConfig);

    /**
     * 激活考核方案
     * 
     * @param planId 方案ID
     * @return 激活结果
     */
    boolean activateAssessmentPlan(Long planId);

    /**
     * 暂停考核方案
     * 
     * @param planId 方案ID
     * @return 暂停结果
     */
    boolean pauseAssessmentPlan(Long planId);

    /**
     * 完成考核方案
     * 
     * @param planId 方案ID
     * @return 完成结果
     */
    boolean completeAssessmentPlan(Long planId);

    /**
     * 复制考核方案
     * 
     * @param planId 方案ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyAssessmentPlan(Long planId, Map<String, Object> copyParams);

    /**
     * 获取方案模板
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<Map<String, Object>> getAssessmentPlanTemplates(String templateType);

    /**
     * 应用方案模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyAssessmentPlanTemplate(Map<String, Object> templateParams);

    /**
     * 获取方案统计
     * 
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @param statisticsPeriod 统计周期
     * @return 统计结果
     */
    Map<String, Object> getAssessmentPlanStatistics(Long organizationId, String statisticsType, String statisticsPeriod);

    /**
     * 批量操作方案
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateAssessmentPlans(Map<String, Object> batchData);

    /**
     * 导入方案
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importAssessmentPlans(Map<String, Object> importData);

    /**
     * 导出方案
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportAssessmentPlans(Map<String, Object> exportParams);

    /**
     * 验证方案配置
     * 
     * @param planId 方案ID
     * @return 验证结果
     */
    Map<String, Object> validateAssessmentPlan(Long planId);

    /**
     * 获取方案预览
     * 
     * @param planId 方案ID
     * @return 预览结果
     */
    Map<String, Object> previewAssessmentPlan(Long planId);

    /**
     * 刷新方案缓存
     * 
     * @param organizationId 组织ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    boolean refreshAssessmentPlanCache(Long organizationId, String cacheType);

    /**
     * 获取指标体系
     * 
     * @param planId 方案ID
     * @return 指标体系
     */
    Map<String, Object> getIndicatorSystem(Long planId);

    /**
     * 设置指标体系
     * 
     * @param planId 方案ID
     * @param indicatorSystem 指标体系
     * @return 设置结果
     */
    boolean setIndicatorSystem(Long planId, Map<String, Object> indicatorSystem);

    /**
     * 获取评分标准
     * 
     * @param planId 方案ID
     * @return 评分标准
     */
    Map<String, Object> getScoringStandards(Long planId);

    /**
     * 设置评分标准
     * 
     * @param planId 方案ID
     * @param scoringStandards 评分标准
     * @return 设置结果
     */
    boolean setScoringStandards(Long planId, Map<String, Object> scoringStandards);

    /**
     * 获取考核周期配置
     * 
     * @param planId 方案ID
     * @return 周期配置
     */
    Map<String, Object> getAssessmentCycleConfig(Long planId);

    /**
     * 设置考核周期配置
     * 
     * @param planId 方案ID
     * @param cycleConfig 周期配置
     * @return 设置结果
     */
    boolean setAssessmentCycleConfig(Long planId, Map<String, Object> cycleConfig);

    /**
     * 获取适用范围配置
     * 
     * @param planId 方案ID
     * @return 适用范围配置
     */
    Map<String, Object> getApplicableScopeConfig(Long planId);

    /**
     * 设置适用范围配置
     * 
     * @param planId 方案ID
     * @param scopeConfig 适用范围配置
     * @return 设置结果
     */
    boolean setApplicableScopeConfig(Long planId, Map<String, Object> scopeConfig);

    /**
     * 获取结果应用配置
     * 
     * @param planId 方案ID
     * @return 结果应用配置
     */
    Map<String, Object> getResultApplicationConfig(Long planId);

    /**
     * 设置结果应用配置
     * 
     * @param planId 方案ID
     * @param applicationConfig 结果应用配置
     * @return 设置结果
     */
    boolean setResultApplicationConfig(Long planId, Map<String, Object> applicationConfig);

    /**
     * 获取方案执行状态
     * 
     * @param planId 方案ID
     * @return 执行状态
     */
    Map<String, Object> getAssessmentPlanExecutionStatus(Long planId);

    /**
     * 获取方案参与人员
     * 
     * @param planId 方案ID
     * @return 参与人员列表
     */
    List<Map<String, Object>> getAssessmentPlanParticipants(Long planId);

    /**
     * 设置方案参与人员
     * 
     * @param planId 方案ID
     * @param participants 参与人员
     * @return 设置结果
     */
    boolean setAssessmentPlanParticipants(Long planId, List<Map<String, Object>> participants);

    /**
     * 获取方案进度报告
     * 
     * @param planId 方案ID
     * @param reportType 报告类型
     * @return 进度报告
     */
    Map<String, Object> getAssessmentPlanProgressReport(Long planId, String reportType);

    /**
     * 生成方案分析报告
     * 
     * @param planId 方案ID
     * @param analysisParams 分析参数
     * @return 分析报告
     */
    Map<String, Object> generateAssessmentPlanAnalysisReport(Long planId, Map<String, Object> analysisParams);

    /**
     * 获取方案建议
     * 
     * @param planId 方案ID
     * @param suggestionType 建议类型
     * @return 建议列表
     */
    List<Map<String, Object>> getAssessmentPlanSuggestions(Long planId, String suggestionType);

    /**
     * 方案智能优化
     * 
     * @param planId 方案ID
     * @param optimizationParams 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeAssessmentPlan(Long planId, Map<String, Object> optimizationParams);

    /**
     * 方案效果评估
     * 
     * @param planId 方案ID
     * @param evaluationParams 评估参数
     * @return 评估结果
     */
    Map<String, Object> evaluateAssessmentPlanEffectiveness(Long planId, Map<String, Object> evaluationParams);

    /**
     * 方案风险评估
     * 
     * @param planId 方案ID
     * @return 风险评估结果
     */
    Map<String, Object> assessAssessmentPlanRisk(Long planId);

    /**
     * 方案合规检查
     * 
     * @param planId 方案ID
     * @return 合规检查结果
     */
    Map<String, Object> checkAssessmentPlanCompliance(Long planId);
}
