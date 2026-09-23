package com.management.accountant.service.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.as.AsIntelligentClassification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案智能分类服务接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface AsIntelligentClassificationService extends IService<AsIntelligentClassification> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建智能分类
     */
    AsIntelligentClassification createClassification(AsIntelligentClassification classification);

    /**
     * 更新智能分类
     */
    AsIntelligentClassification updateClassification(AsIntelligentClassification classification);

    /**
     * 删除智能分类
     */
    boolean deleteClassification(Long tenantId, Long classificationId);

    /**
     * 根据ID查询智能分类
     */
    AsIntelligentClassification getClassificationById(Long tenantId, Long classificationId);

    /**
     * 根据编号查询智能分类
     */
    AsIntelligentClassification getClassificationByCode(Long tenantId, String classificationCode);

    // ==================== 分页查询操作 ====================

    /**
     * 分页查询智能分类
     */
    IPage<AsIntelligentClassification> getClassificationPage(Page<AsIntelligentClassification> page,
                                                            Long tenantId,
                                                            String classificationName,
                                                            String classificationType,
                                                            String classificationStatus,
                                                            String classificationAlgorithm);

    // ==================== 分类管理操作 ====================

    /**
     * 启动分类训练
     */
    boolean startTraining(Long tenantId, Long classificationId, Map<String, Object> trainingConfig);

    /**
     * 停止分类训练
     */
    boolean stopTraining(Long tenantId, Long classificationId);

    /**
     * 部署分类模型
     */
    boolean deployModel(Long tenantId, Long classificationId);

    /**
     * 取消部署分类模型
     */
    boolean undeployModel(Long tenantId, Long classificationId);

    /**
     * 激活分类模型
     */
    boolean activateClassification(Long tenantId, Long classificationId);

    /**
     * 停用分类模型
     */
    boolean deactivateClassification(Long tenantId, Long classificationId);

    /**
     * 测试分类模型
     */
    Map<String, Object> testClassification(Long tenantId, Long classificationId, Map<String, Object> testData);

    /**
     * 预测分类结果
     */
    Map<String, Object> predictClassification(Long tenantId, Long classificationId, Map<String, Object> inputData);

    // ==================== 模型管理操作 ====================

    /**
     * 更新模型版本
     */
    boolean updateModelVersion(Long tenantId, Long classificationId, String newVersion);

    /**
     * 回滚模型版本
     */
    boolean rollbackModelVersion(Long tenantId, Long classificationId, String targetVersion);

    /**
     * 比较模型版本
     */
    Map<String, Object> compareModelVersions(Long tenantId, Long classificationId, String version1, String version2);

    /**
     * 获取模型信息
     */
    Map<String, Object> getModelInfo(Long tenantId, Long classificationId);

    /**
     * 更新模型配置
     */
    boolean updateModelConfig(Long tenantId, Long classificationId, Map<String, Object> config);

    // ==================== 性能监控操作 ====================

    /**
     * 获取分类性能指标
     */
    Map<String, Object> getPerformanceMetrics(Long tenantId, Long classificationId);

    /**
     * 获取分类准确率历史
     */
    List<Map<String, Object>> getAccuracyHistory(Long tenantId, Long classificationId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取预测统计
     */
    Map<String, Object> getPredictionStats(Long tenantId, Long classificationId);

    /**
     * 获取资源使用情况
     */
    Map<String, Object> getResourceUsage(Long tenantId, Long classificationId);

    /**
     * 监控分类健康状态
     */
    Map<String, Object> monitorClassificationHealth(Long tenantId, Long classificationId);

    // ==================== 查询统计操作 ====================

    /**
     * 根据分类类型查询
     */
    List<AsIntelligentClassification> getClassificationsByType(Long tenantId, String classificationType);

    /**
     * 根据分类状态查询
     */
    List<AsIntelligentClassification> getClassificationsByStatus(Long tenantId, String classificationStatus);

    /**
     * 根据分类算法查询
     */
    List<AsIntelligentClassification> getClassificationsByAlgorithm(Long tenantId, String classificationAlgorithm);

    /**
     * 查询活跃的分类
     */
    List<AsIntelligentClassification> getActiveClassifications(Long tenantId);

    /**
     * 查询已部署的分类
     */
    List<AsIntelligentClassification> getDeployedClassifications(Long tenantId);

    /**
     * 查询训练中的分类
     */
    List<AsIntelligentClassification> getTrainingClassifications(Long tenantId);

    /**
     * 根据准确率范围查询
     */
    List<AsIntelligentClassification> getClassificationsByAccuracyRange(Long tenantId, BigDecimal minAccuracy, BigDecimal maxAccuracy);

    /**
     * 查询最新版本的分类
     */
    AsIntelligentClassification getLatestVersionByType(Long tenantId, String classificationType);

    /**
     * 查询性能最佳的分类
     */
    AsIntelligentClassification getBestPerformanceByType(Long tenantId, String classificationType);

    // ==================== 统计分析操作 ====================

    /**
     * 统计分类状态分布
     */
    List<Map<String, Object>> countByClassificationStatus(Long tenantId);

    /**
     * 统计分类类型分布
     */
    List<Map<String, Object>> countByClassificationType(Long tenantId);

    /**
     * 统计分类算法分布
     */
    List<Map<String, Object>> countByClassificationAlgorithm(Long tenantId);

    /**
     * 获取分类趋势
     */
    List<Map<String, Object>> getClassificationTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity);

    /**
     * 获取训练趋势
     */
    List<Map<String, Object>> getTrainingTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity);

    /**
     * 获取预测趋势
     */
    List<Map<String, Object>> getPredictionTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity);

    /**
     * 获取分类排行榜
     */
    List<Map<String, Object>> getClassificationRanking(Long tenantId, String rankBy, Integer limit);

    // ==================== 批量操作 ====================

    /**
     * 批量创建分类
     */
    List<AsIntelligentClassification> batchCreateClassifications(List<AsIntelligentClassification> classifications);

    /**
     * 批量更新分类状态
     */
    boolean batchUpdateStatus(Long tenantId, List<Long> classificationIds, String status);

    /**
     * 批量更新分类算法
     */
    boolean batchUpdateAlgorithm(Long tenantId, List<Long> classificationIds, String algorithm);

    /**
     * 批量删除分类
     */
    boolean batchDeleteClassifications(Long tenantId, List<Long> classificationIds);

    /**
     * 批量训练分类
     */
    boolean batchTrainClassifications(Long tenantId, List<Long> classificationIds, Map<String, Object> trainingConfig);

    /**
     * 批量部署分类
     */
    boolean batchDeployClassifications(Long tenantId, List<Long> classificationIds);

    // ==================== 数据管理操作 ====================

    /**
     * 导出分类数据
     */
    List<Map<String, Object>> exportClassificationData(Long tenantId, List<Long> classificationIds);

    /**
     * 导入分类数据
     */
    boolean importClassificationData(Long tenantId, List<Map<String, Object>> classificationData);

    /**
     * 清理过期数据
     */
    boolean cleanupExpiredData(Long tenantId, LocalDateTime expiredDate);

    /**
     * 清理无效模型
     */
    boolean cleanupInvalidModels(Long tenantId);

    // ==================== 系统维护操作 ====================

    /**
     * 重建分类索引
     */
    boolean rebuildClassificationIndex(Long tenantId);

    /**
     * 优化分类性能
     */
    boolean optimizeClassificationPerformance(Long tenantId);

    /**
     * 检查分类健康状态
     */
    List<Map<String, Object>> checkClassificationHealth(Long tenantId);

    /**
     * 生成分类报告
     */
    Map<String, Object> generateClassificationReport(Long tenantId, Long classificationId);

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(Long tenantId);

    // ==================== 通知提醒操作 ====================

    /**
     * 发送训练完成通知
     */
    boolean sendTrainingCompletionNotification(Long tenantId, Long classificationId);

    /**
     * 发送部署成功通知
     */
    boolean sendDeploymentSuccessNotification(Long tenantId, Long classificationId);

    /**
     * 发送性能告警通知
     */
    boolean sendPerformanceAlertNotification(Long tenantId, Long classificationId, String alertType);

    /**
     * 发送错误通知
     */
    boolean sendErrorNotification(Long tenantId, Long classificationId, String errorMessage);
}
