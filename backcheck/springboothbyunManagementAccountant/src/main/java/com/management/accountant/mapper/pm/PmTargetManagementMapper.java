package com.management.accountant.mapper.pm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.pm.PmTargetManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 目标管理数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface PmTargetManagementMapper extends BaseMapper<PmTargetManagement> {

    /**
     * 目标激励
     * 
     * @param targetId 目标ID
     * @param incentiveParams 激励参数
     * @return 激励结果
     */
    Map<String, Object> incentiveTarget(@Param("targetId") Long targetId,
                                      @Param("incentiveParams") Map<String, Object> incentiveParams);

    /**
     * 目标协商
     * 
     * @param targetId 目标ID
     * @param negotiationParams 协商参数
     * @return 协商结果
     */
    Map<String, Object> negotiateTarget(@Param("targetId") Long targetId,
                                      @Param("negotiationParams") Map<String, Object> negotiationParams);

    /**
     * 获取目标知识
     * 
     * @param targetId 目标ID
     * @param knowledgeType 知识类型
     * @return 知识信息
     */
    Map<String, Object> selectTargetKnowledge(@Param("targetId") Long targetId,
                                             @Param("knowledgeType") String knowledgeType);

    /**
     * 目标数据分析
     * 
     * @param targetId 目标ID
     * @param analysisType 分析类型
     * @return 分析结果
     */
    Map<String, Object> analyzeTarget(@Param("targetId") Long targetId,
                                    @Param("analysisType") String analysisType);

    /**
     * 获取目标树
     * 
     * @param organizationId 组织ID
     * @param targetType 目标类型
     * @param targetLevel 目标级别
     * @return 目标树
     */
    List<Map<String, Object>> selectTargetTree(@Param("organizationId") Long organizationId,
                                              @Param("targetType") String targetType,
                                              @Param("targetLevel") String targetLevel);

    /**
     * 获取目标仪表板数据
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param dashboardType 仪表板类型
     * @return 仪表板数据
     */
    Map<String, Object> selectTargetDashboard(@Param("organizationId") Long organizationId,
                                             @Param("targetOwnerId") Long targetOwnerId,
                                             @Param("dashboardType") String dashboardType);

    /**
     * 获取目标图表数据
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param chartType 图表类型
     * @return 图表数据
     */
    List<Map<String, Object>> selectTargetChartData(@Param("organizationId") Long organizationId,
                                                   @Param("targetOwnerId") Long targetOwnerId,
                                                   @Param("chartType") String chartType);

    /**
     * 批量操作目标
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateTargets(@Param("batchData") Map<String, Object> batchData);

    /**
     * 导入目标
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importTargets(@Param("importData") Map<String, Object> importData);

    /**
     * 导出目标
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportTargets(@Param("exportParams") Map<String, Object> exportParams);

    /**
     * 获取目标统计
     * 
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @param statisticsPeriod 统计周期
     * @return 统计结果
     */
    Map<String, Object> selectTargetStatistics(@Param("organizationId") Long organizationId,
                                              @Param("statisticsType") String statisticsType,
                                              @Param("statisticsPeriod") String statisticsPeriod);

    /**
     * 获取目标进度报告
     * 
     * @param organizationId 组织ID
     * @param reportType 报告类型
     * @param reportPeriod 报告周期
     * @return 进度报告
     */
    Map<String, Object> selectTargetProgressReport(@Param("organizationId") Long organizationId,
                                                  @Param("reportType") String reportType,
                                                  @Param("reportPeriod") String reportPeriod);

    /**
     * 复制目标
     * 
     * @param targetId 目标ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyTarget(@Param("targetId") Long targetId,
                                 @Param("copyParams") Map<String, Object> copyParams);

    /**
     * 获取目标模板
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<Map<String, Object>> selectTargetTemplates(@Param("templateType") String templateType);

    /**
     * 应用目标模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyTargetTemplate(@Param("templateParams") Map<String, Object> templateParams);

    /**
     * 获取目标建议
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param recommendationType 建议类型
     * @return 建议列表
     */
    List<Map<String, Object>> selectTargetRecommendations(@Param("organizationId") Long organizationId,
                                                         @Param("targetOwnerId") Long targetOwnerId,
                                                         @Param("recommendationType") String recommendationType);

    /**
     * 刷新目标缓存
     * 
     * @param organizationId 组织ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    int refreshTargetCache(@Param("organizationId") Long organizationId,
                         @Param("cacheType") String cacheType);

    /**
     * 计算目标完成率
     * 
     * @param targetId 目标ID
     * @return 完成率
     */
    Map<String, Object> calculateTargetCompletionRate(@Param("targetId") Long targetId);

    /**
     * 更新目标进度
     * 
     * @param targetId 目标ID
     * @param progressData 进度数据
     * @return 更新结果
     */
    int updateTargetProgress(@Param("targetId") Long targetId,
                           @Param("progressData") Map<String, Object> progressData);

    /**
     * 获取目标风险评估
     * 
     * @param targetId 目标ID
     * @return 风险评估结果
     */
    Map<String, Object> assessTargetRisk(@Param("targetId") Long targetId);

    /**
     * 目标预警检查
     * 
     * @param targetId 目标ID
     * @return 预警信息
     */
    List<Map<String, Object>> checkTargetAlerts(@Param("targetId") Long targetId);

    /**
     * 获取目标关联信息
     * 
     * @param targetId 目标ID
     * @param relationType 关联类型
     * @return 关联信息
     */
    List<Map<String, Object>> selectTargetRelations(@Param("targetId") Long targetId,
                                                   @Param("relationType") String relationType);

    /**
     * 目标对齐检查
     * 
     * @param targetId 目标ID
     * @return 对齐检查结果
     */
    Map<String, Object> checkTargetAlignment(@Param("targetId") Long targetId);

    /**
     * 获取目标历史记录
     * 
     * @param targetId 目标ID
     * @param recordType 记录类型
     * @return 历史记录
     */
    List<Map<String, Object>> selectTargetHistory(@Param("targetId") Long targetId,
                                                 @Param("recordType") String recordType);

    /**
     * 目标智能推荐
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 智能推荐结果
     */
    Map<String, Object> intelligentTargetRecommendation(@Param("organizationId") Long organizationId,
                                                       @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 目标绩效预测
     * 
     * @param targetId 目标ID
     * @param predictionParams 预测参数
     * @return 预测结果
     */
    Map<String, Object> predictTargetPerformance(@Param("targetId") Long targetId,
                                                @Param("predictionParams") Map<String, Object> predictionParams);

    /**
     * 获取目标优化建议
     * 
     * @param targetId 目标ID
     * @return 优化建议
     */
    List<Map<String, Object>> selectTargetOptimizationSuggestions(@Param("targetId") Long targetId);

    /**
     * 目标影响分析
     * 
     * @param targetId 目标ID
     * @param impactParams 影响参数
     * @return 影响分析结果
     */
    Map<String, Object> analyzeTargetImpact(@Param("targetId") Long targetId,
                                          @Param("impactParams") Map<String, Object> impactParams);

    /**
     * 目标协同分析
     * 
     * @param targetId 目标ID
     * @return 协同分析结果
     */
    Map<String, Object> analyzeTargetCollaboration(@Param("targetId") Long targetId);

    /**
     * 目标资源分析
     * 
     * @param targetId 目标ID
     * @return 资源分析结果
     */
    Map<String, Object> analyzeTargetResources(@Param("targetId") Long targetId);

    /**
     * 目标时间分析
     * 
     * @param targetId 目标ID
     * @return 时间分析结果
     */
    Map<String, Object> analyzeTargetTimeline(@Param("targetId") Long targetId);

    /**
     * 目标质量评估
     * 
     * @param targetId 目标ID
     * @return 质量评估结果
     */
    Map<String, Object> assessTargetQuality(@Param("targetId") Long targetId);

    /**
     * 目标健康度监控
     * 
     * @param targetId 目标ID
     * @return 健康度监控结果
     */
    Map<String, Object> monitorTargetHealth(@Param("targetId") Long targetId);

    /**
     * 获取总目标数量
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 总目标数量
     */
    Long getTotalTargetsCount(@Param("organizationId") Long organizationId,
                            @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 获取已完成目标数量
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 已完成目标数量
     */
    Long getCompletedTargetsCount(@Param("organizationId") Long organizationId,
                                @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 获取正常进度目标数量
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 正常进度目标数量
     */
    Long getOnTrackTargetsCount(@Param("organizationId") Long organizationId,
                              @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 获取风险目标数量
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 风险目标数量
     */
    Long getAtRiskTargetsCount(@Param("organizationId") Long organizationId,
                             @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 获取平均完成率
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 平均完成率
     */
    BigDecimal getAverageCompletionRate(@Param("organizationId") Long organizationId,
                                      @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 批量更新目标状态
     * 
     * @param targetIds 目标ID列表
     * @param status 状态
     * @return 更新行数
     */
    int batchUpdateTargetStatus(@Param("targetIds") List<Long> targetIds,
                              @Param("status") String status);

    /**
     * 批量更新目标负责人
     * 
     * @param targetIds 目标ID列表
     * @param targetOwnerId 负责人ID
     * @param targetOwnerName 负责人姓名
     * @return 更新行数
     */
    int batchUpdateTargetOwner(@Param("targetIds") List<Long> targetIds,
                             @Param("targetOwnerId") Long targetOwnerId,
                             @Param("targetOwnerName") String targetOwnerName);

    /**
     * 获取目标趋势数据
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param trendType 趋势类型
     * @param periods 周期数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectTargetTrend(@Param("organizationId") Long organizationId,
                                               @Param("targetOwnerId") Long targetOwnerId,
                                               @Param("trendType") String trendType,
                                               @Param("periods") Integer periods);

    /**
     * 计算目标健康度分数
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 健康度分数
     */
    BigDecimal calculateTargetHealthScore(@Param("organizationId") Long organizationId,
                                        @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 生成目标执行报告
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param reportType 报告类型
     * @param reportPeriod 报告周期
     * @return 执行报告数据
     */
    Map<String, Object> generateTargetExecutionReport(@Param("organizationId") Long organizationId,
                                                     @Param("targetOwnerId") Long targetOwnerId,
                                                     @Param("reportType") String reportType,
                                                     @Param("reportPeriod") String reportPeriod);

    /**
     * 目标冲突检测
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 冲突检测结果
     */
    List<Map<String, Object>> detectTargetConflicts(@Param("organizationId") Long organizationId,
                                                   @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 目标优化建议生成
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 优化建议列表
     */
    List<Map<String, Object>> generateTargetOptimizationSuggestions(@Param("organizationId") Long organizationId,
                                                                   @Param("targetOwnerId") Long targetOwnerId);

    /**
     * 目标影响分析
     * 
     * @param targetId 目标ID
     * @param analysisParams 分析参数
     * @return 影响分析结果
     */
    Map<String, Object> analyzeTargetInfluence(@Param("targetId") Long targetId,
                                              @Param("analysisParams") Map<String, Object> analysisParams);

    /**
     * 目标自动化配置
     * 
     * @param organizationId 组织ID
     * @param automationParams 自动化参数
     * @return 配置结果
     */
    Map<String, Object> configureTargetAutomation(@Param("organizationId") Long organizationId,
                                                 @Param("automationParams") Map<String, Object> automationParams);
}
