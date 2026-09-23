package com.management.accountant.mapper.pm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.pm.PmAssessmentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 考核方案配置数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface PmAssessmentPlanMapper extends BaseMapper<PmAssessmentPlan> {

    /**
     * 复制考核方案
     * 
     * @param planId 方案ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyAssessmentPlan(@Param("planId") Long planId,
                                         @Param("copyParams") Map<String, Object> copyParams);

    /**
     * 获取方案模板
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<Map<String, Object>> selectAssessmentPlanTemplates(@Param("templateType") String templateType);

    /**
     * 应用方案模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyAssessmentPlanTemplate(@Param("templateParams") Map<String, Object> templateParams);

    /**
     * 获取方案统计
     * 
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @param statisticsPeriod 统计周期
     * @return 统计结果
     */
    Map<String, Object> selectAssessmentPlanStatistics(@Param("organizationId") Long organizationId,
                                                      @Param("statisticsType") String statisticsType,
                                                      @Param("statisticsPeriod") String statisticsPeriod);

    /**
     * 批量操作方案
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateAssessmentPlans(@Param("batchData") Map<String, Object> batchData);

    /**
     * 导入方案
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importAssessmentPlans(@Param("importData") Map<String, Object> importData);

    /**
     * 导出方案
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportAssessmentPlans(@Param("exportParams") Map<String, Object> exportParams);

    /**
     * 验证方案配置
     * 
     * @param planId 方案ID
     * @return 验证结果
     */
    Map<String, Object> validateAssessmentPlan(@Param("planId") Long planId);

    /**
     * 获取方案预览
     * 
     * @param planId 方案ID
     * @return 预览结果
     */
    Map<String, Object> previewAssessmentPlan(@Param("planId") Long planId);

    /**
     * 刷新方案缓存
     * 
     * @param organizationId 组织ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    int refreshAssessmentPlanCache(@Param("organizationId") Long organizationId,
                                 @Param("cacheType") String cacheType);

    /**
     * 获取指标体系
     * 
     * @param planId 方案ID
     * @return 指标体系
     */
    Map<String, Object> selectIndicatorSystem(@Param("planId") Long planId);

    /**
     * 更新指标体系
     * 
     * @param planId 方案ID
     * @param indicatorSystem 指标体系
     * @return 更新结果
     */
    int updateIndicatorSystem(@Param("planId") Long planId,
                            @Param("indicatorSystem") Map<String, Object> indicatorSystem);

    /**
     * 获取评分标准
     * 
     * @param planId 方案ID
     * @return 评分标准
     */
    Map<String, Object> selectScoringStandards(@Param("planId") Long planId);

    /**
     * 更新评分标准
     * 
     * @param planId 方案ID
     * @param scoringStandards 评分标准
     * @return 更新结果
     */
    int updateScoringStandards(@Param("planId") Long planId,
                             @Param("scoringStandards") Map<String, Object> scoringStandards);

    /**
     * 获取考核周期配置
     * 
     * @param planId 方案ID
     * @return 周期配置
     */
    Map<String, Object> selectAssessmentCycleConfig(@Param("planId") Long planId);

    /**
     * 更新考核周期配置
     * 
     * @param planId 方案ID
     * @param cycleConfig 周期配置
     * @return 更新结果
     */
    int updateAssessmentCycleConfig(@Param("planId") Long planId,
                                  @Param("cycleConfig") Map<String, Object> cycleConfig);

    /**
     * 获取适用范围配置
     * 
     * @param planId 方案ID
     * @return 适用范围配置
     */
    Map<String, Object> selectApplicableScopeConfig(@Param("planId") Long planId);

    /**
     * 更新适用范围配置
     * 
     * @param planId 方案ID
     * @param scopeConfig 适用范围配置
     * @return 更新结果
     */
    int updateApplicableScopeConfig(@Param("planId") Long planId,
                                  @Param("scopeConfig") Map<String, Object> scopeConfig);

    /**
     * 获取结果应用配置
     * 
     * @param planId 方案ID
     * @return 结果应用配置
     */
    Map<String, Object> selectResultApplicationConfig(@Param("planId") Long planId);

    /**
     * 更新结果应用配置
     * 
     * @param planId 方案ID
     * @param applicationConfig 结果应用配置
     * @return 更新结果
     */
    int updateResultApplicationConfig(@Param("planId") Long planId,
                                    @Param("applicationConfig") Map<String, Object> applicationConfig);

    /**
     * 获取方案执行状态
     * 
     * @param planId 方案ID
     * @return 执行状态
     */
    Map<String, Object> selectAssessmentPlanExecutionStatus(@Param("planId") Long planId);

    /**
     * 获取方案参与人员
     * 
     * @param planId 方案ID
     * @return 参与人员列表
     */
    List<Map<String, Object>> selectAssessmentPlanParticipants(@Param("planId") Long planId);

    /**
     * 更新方案参与人员
     * 
     * @param planId 方案ID
     * @param participants 参与人员
     * @return 更新结果
     */
    int updateAssessmentPlanParticipants(@Param("planId") Long planId,
                                       @Param("participants") List<Map<String, Object>> participants);

    /**
     * 获取方案进度报告
     * 
     * @param planId 方案ID
     * @param reportType 报告类型
     * @return 进度报告
     */
    Map<String, Object> selectAssessmentPlanProgressReport(@Param("planId") Long planId,
                                                          @Param("reportType") String reportType);

    /**
     * 生成方案分析报告
     * 
     * @param planId 方案ID
     * @param analysisParams 分析参数
     * @return 分析报告
     */
    Map<String, Object> generateAssessmentPlanAnalysisReport(@Param("planId") Long planId,
                                                            @Param("analysisParams") Map<String, Object> analysisParams);

    /**
     * 获取方案建议
     * 
     * @param planId 方案ID
     * @param suggestionType 建议类型
     * @return 建议列表
     */
    List<Map<String, Object>> selectAssessmentPlanSuggestions(@Param("planId") Long planId,
                                                             @Param("suggestionType") String suggestionType);

    /**
     * 方案智能优化
     * 
     * @param planId 方案ID
     * @param optimizationParams 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeAssessmentPlan(@Param("planId") Long planId,
                                              @Param("optimizationParams") Map<String, Object> optimizationParams);

    /**
     * 方案效果评估
     * 
     * @param planId 方案ID
     * @param evaluationParams 评估参数
     * @return 评估结果
     */
    Map<String, Object> evaluateAssessmentPlanEffectiveness(@Param("planId") Long planId,
                                                           @Param("evaluationParams") Map<String, Object> evaluationParams);

    /**
     * 方案风险评估
     * 
     * @param planId 方案ID
     * @return 风险评估结果
     */
    Map<String, Object> assessAssessmentPlanRisk(@Param("planId") Long planId);

    /**
     * 方案合规检查
     * 
     * @param planId 方案ID
     * @return 合规检查结果
     */
    Map<String, Object> checkAssessmentPlanCompliance(@Param("planId") Long planId);

    /**
     * 获取方案配置历史
     * 
     * @param planId 方案ID
     * @param historyType 历史类型
     * @return 配置历史
     */
    List<Map<String, Object>> selectAssessmentPlanConfigHistory(@Param("planId") Long planId,
                                                               @Param("historyType") String historyType);

    /**
     * 获取方案使用统计
     * 
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @return 使用统计
     */
    Map<String, Object> selectAssessmentPlanUsageStatistics(@Param("organizationId") Long organizationId,
                                                           @Param("statisticsType") String statisticsType);

    /**
     * 获取方案效果分析
     * 
     * @param planId 方案ID
     * @param analysisType 分析类型
     * @return 效果分析
     */
    Map<String, Object> selectAssessmentPlanEffectivenessAnalysis(@Param("planId") Long planId,
                                                                 @Param("analysisType") String analysisType);

    /**
     * 获取方案对比分析
     * 
     * @param planIds 方案ID列表
     * @param comparisonType 对比类型
     * @return 对比分析结果
     */
    Map<String, Object> selectAssessmentPlanComparison(@Param("planIds") List<Long> planIds,
                                                      @Param("comparisonType") String comparisonType);

    /**
     * 获取方案趋势分析
     * 
     * @param organizationId 组织ID
     * @param trendType 趋势类型
     * @param periods 周期数
     * @return 趋势分析结果
     */
    List<Map<String, Object>> selectAssessmentPlanTrend(@Param("organizationId") Long organizationId,
                                                       @Param("trendType") String trendType,
                                                       @Param("periods") Integer periods);

    /**
     * 获取方案健康度评估
     * 
     * @param planId 方案ID
     * @return 健康度评估结果
     */
    Map<String, Object> selectAssessmentPlanHealthCheck(@Param("planId") Long planId);

    /**
     * 获取方案智能推荐
     * 
     * @param organizationId 组织ID
     * @param recommendationType 推荐类型
     * @return 智能推荐结果
     */
    List<Map<String, Object>> selectAssessmentPlanRecommendations(@Param("organizationId") Long organizationId,
                                                                 @Param("recommendationType") String recommendationType);

    /**
     * 批量更新方案状态
     * 
     * @param planIds 方案ID列表
     * @param status 状态
     * @return 更新行数
     */
    int batchUpdateAssessmentPlanStatus(@Param("planIds") List<Long> planIds,
                                      @Param("status") String status);

    /**
     * 批量更新方案配置
     * 
     * @param planIds 方案ID列表
     * @param configData 配置数据
     * @return 更新行数
     */
    int batchUpdateAssessmentPlanConfig(@Param("planIds") List<Long> planIds,
                                      @Param("configData") Map<String, Object> configData);

    /**
     * 获取方案依赖关系
     * 
     * @param planId 方案ID
     * @param dependencyType 依赖类型
     * @return 依赖关系
     */
    List<Map<String, Object>> selectAssessmentPlanDependencies(@Param("planId") Long planId,
                                                              @Param("dependencyType") String dependencyType);

    /**
     * 获取方案影响分析
     * 
     * @param planId 方案ID
     * @param impactType 影响类型
     * @return 影响分析结果
     */
    Map<String, Object> selectAssessmentPlanImpactAnalysis(@Param("planId") Long planId,
                                                          @Param("impactType") String impactType);

    /**
     * 方案自动化配置
     * 
     * @param organizationId 组织ID
     * @param automationParams 自动化参数
     * @return 配置结果
     */
    Map<String, Object> configureAssessmentPlanAutomation(@Param("organizationId") Long organizationId,
                                                         @Param("automationParams") Map<String, Object> automationParams);
}
