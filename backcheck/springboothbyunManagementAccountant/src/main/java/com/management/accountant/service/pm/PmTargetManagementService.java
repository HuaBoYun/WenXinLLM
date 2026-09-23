package com.management.accountant.service.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.pm.PmTargetManagement;

import java.util.List;
import java.util.Map;

/**
 * 目标管理服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface PmTargetManagementService {

    /**
     * 分页查询目标
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param organizationId 组织ID
     * @param targetType 目标类型
     * @param targetLevel 目标级别
     * @param targetStatus 目标状态
     * @param targetOwnerId 负责人ID
     * @param keyword 关键词
     * @return 分页结果
     */
    IPage<PmTargetManagement> queryTargetPage(Long current, Long size, Long organizationId,
                                             String targetType, String targetLevel, String targetStatus,
                                             Long targetOwnerId, String keyword);

    /**
     * 创建目标
     * 
     * @param target 目标信息
     * @return 创建结果
     */
    boolean createTarget(PmTargetManagement target);

    /**
     * 更新目标
     * 
     * @param target 目标信息
     * @return 更新结果
     */
    boolean updateTarget(PmTargetManagement target);

    /**
     * 删除目标
     * 
     * @param targetId 目标ID
     * @return 删除结果
     */
    boolean deleteTarget(Long targetId);

    /**
     * 根据ID查询目标
     * 
     * @param targetId 目标ID
     * @return 目标信息
     */
    PmTargetManagement getTargetById(Long targetId);

    /**
     * 目标分解
     * 
     * @param targetId 目标ID
     * @param decomposeParams 分解参数
     * @return 分解结果
     */
    Map<String, Object> decomposeTarget(Long targetId, Map<String, Object> decomposeParams);

    /**
     * 目标跟踪
     * 
     * @param targetId 目标ID
     * @param trackingData 跟踪数据
     * @return 跟踪结果
     */
    Map<String, Object> trackTarget(Long targetId, Map<String, Object> trackingData);

    /**
     * 目标调整
     * 
     * @param targetId 目标ID
     * @param adjustParams 调整参数
     * @return 调整结果
     */
    boolean adjustTarget(Long targetId, Map<String, Object> adjustParams);

    /**
     * 目标评估
     * 
     * @param targetId 目标ID
     * @param evaluationParams 评估参数
     * @return 评估结果
     */
    Map<String, Object> evaluateTarget(Long targetId, Map<String, Object> evaluationParams);

    /**
     * 目标激励
     * 
     * @param targetId 目标ID
     * @param incentiveParams 激励参数
     * @return 激励结果
     */
    Map<String, Object> incentiveTarget(Long targetId, Map<String, Object> incentiveParams);

    /**
     * 目标协商
     * 
     * @param targetId 目标ID
     * @param negotiationParams 协商参数
     * @return 协商结果
     */
    Map<String, Object> negotiateTarget(Long targetId, Map<String, Object> negotiationParams);

    /**
     * 获取目标知识
     * 
     * @param targetId 目标ID
     * @param knowledgeType 知识类型
     * @return 知识信息
     */
    Map<String, Object> getTargetKnowledge(Long targetId, String knowledgeType);

    /**
     * 目标数据分析
     * 
     * @param targetId 目标ID
     * @param analysisType 分析类型
     * @return 分析结果
     */
    Map<String, Object> analyzeTarget(Long targetId, String analysisType);

    /**
     * 获取目标树
     * 
     * @param organizationId 组织ID
     * @param targetType 目标类型
     * @param targetLevel 目标级别
     * @return 目标树
     */
    List<Map<String, Object>> getTargetTree(Long organizationId, String targetType, String targetLevel);

    /**
     * 获取目标仪表板
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param dashboardType 仪表板类型
     * @return 仪表板数据
     */
    Map<String, Object> getTargetDashboard(Long organizationId, Long targetOwnerId, String dashboardType);

    /**
     * 批量操作目标
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateTargets(Map<String, Object> batchData);

    /**
     * 导入目标
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importTargets(Map<String, Object> importData);

    /**
     * 导出目标
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportTargets(Map<String, Object> exportParams);

    /**
     * 获取目标统计
     * 
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @param statisticsPeriod 统计周期
     * @return 统计结果
     */
    Map<String, Object> getTargetStatistics(Long organizationId, String statisticsType, String statisticsPeriod);

    /**
     * 获取目标进度报告
     * 
     * @param organizationId 组织ID
     * @param reportType 报告类型
     * @param reportPeriod 报告周期
     * @return 进度报告
     */
    Map<String, Object> getTargetProgressReport(Long organizationId, String reportType, String reportPeriod);

    /**
     * 复制目标
     * 
     * @param targetId 目标ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyTarget(Long targetId, Map<String, Object> copyParams);

    /**
     * 获取目标模板
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<Map<String, Object>> getTargetTemplates(String templateType);

    /**
     * 应用目标模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyTargetTemplate(Map<String, Object> templateParams);

    /**
     * 获取目标建议
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @param recommendationType 建议类型
     * @return 建议列表
     */
    List<Map<String, Object>> getTargetRecommendations(Long organizationId, Long targetOwnerId, String recommendationType);

    /**
     * 刷新目标缓存
     * 
     * @param organizationId 组织ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    boolean refreshTargetCache(Long organizationId, String cacheType);

    /**
     * 计算目标完成率
     * 
     * @param targetId 目标ID
     * @return 完成率
     */
    Map<String, Object> calculateTargetCompletionRate(Long targetId);

    /**
     * 更新目标进度
     * 
     * @param targetId 目标ID
     * @param progressData 进度数据
     * @return 更新结果
     */
    boolean updateTargetProgress(Long targetId, Map<String, Object> progressData);

    /**
     * 获取目标风险评估
     * 
     * @param targetId 目标ID
     * @return 风险评估结果
     */
    Map<String, Object> assessTargetRisk(Long targetId);

    /**
     * 目标预警检查
     * 
     * @param targetId 目标ID
     * @return 预警信息
     */
    List<Map<String, Object>> checkTargetAlerts(Long targetId);

    /**
     * 获取目标关联信息
     * 
     * @param targetId 目标ID
     * @param relationType 关联类型
     * @return 关联信息
     */
    List<Map<String, Object>> getTargetRelations(Long targetId, String relationType);

    /**
     * 目标对齐检查
     * 
     * @param targetId 目标ID
     * @return 对齐检查结果
     */
    Map<String, Object> checkTargetAlignment(Long targetId);

    /**
     * 获取目标历史记录
     * 
     * @param targetId 目标ID
     * @param recordType 记录类型
     * @return 历史记录
     */
    List<Map<String, Object>> getTargetHistory(Long targetId, String recordType);

    /**
     * 目标智能推荐
     * 
     * @param organizationId 组织ID
     * @param targetOwnerId 负责人ID
     * @return 智能推荐结果
     */
    Map<String, Object> intelligentTargetRecommendation(Long organizationId, Long targetOwnerId);

    /**
     * 目标绩效预测
     * 
     * @param targetId 目标ID
     * @param predictionParams 预测参数
     * @return 预测结果
     */
    Map<String, Object> predictTargetPerformance(Long targetId, Map<String, Object> predictionParams);

    /**
     * 目标优化建议
     * 
     * @param targetId 目标ID
     * @return 优化建议
     */
    List<Map<String, Object>> getTargetOptimizationSuggestions(Long targetId);

    /**
     * 目标影响分析
     * 
     * @param targetId 目标ID
     * @param impactParams 影响参数
     * @return 影响分析结果
     */
    Map<String, Object> analyzeTargetImpact(Long targetId, Map<String, Object> impactParams);

    /**
     * 目标协同分析
     * 
     * @param targetId 目标ID
     * @return 协同分析结果
     */
    Map<String, Object> analyzeTargetCollaboration(Long targetId);

    /**
     * 目标资源分析
     * 
     * @param targetId 目标ID
     * @return 资源分析结果
     */
    Map<String, Object> analyzeTargetResources(Long targetId);

    /**
     * 目标时间分析
     * 
     * @param targetId 目标ID
     * @return 时间分析结果
     */
    Map<String, Object> analyzeTargetTimeline(Long targetId);

    /**
     * 目标质量评估
     * 
     * @param targetId 目标ID
     * @return 质量评估结果
     */
    Map<String, Object> assessTargetQuality(Long targetId);

    /**
     * 目标健康度监控
     * 
     * @param targetId 目标ID
     * @return 健康度监控结果
     */
    Map<String, Object> monitorTargetHealth(Long targetId);
}
