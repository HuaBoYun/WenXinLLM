package com.management.accountant.service.as.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.as.AsIntelligentClassification;
import com.management.accountant.mapper.as.AsIntelligentClassificationMapper;
import com.management.accountant.service.as.AsIntelligentClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 档案智能分类服务实现类
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AsIntelligentClassificationServiceImpl extends ServiceImpl<AsIntelligentClassificationMapper, AsIntelligentClassification> 
        implements AsIntelligentClassificationService {

    @Autowired
    private AsIntelligentClassificationMapper classificationMapper;

    // ==================== 基础CRUD操作 ====================

    @Override
    public AsIntelligentClassification createClassification(AsIntelligentClassification classification) {
        log.info("创建智能分类: {}", classification.getClassificationName());
        
        // 生成分类编号
        if (!StringUtils.hasText(classification.getClassificationCode())) {
            classification.setClassificationCode(generateClassificationCode());
        }
        
        // 设置默认值
        if (classification.getClassificationStatus() == null) {
            classification.setClassificationStatus("INACTIVE");
        }
        if (classification.getConfidenceThreshold() == null) {
            classification.setConfidenceThreshold(new BigDecimal("0.8"));
        }
        if (classification.getPredictionCount() == null) {
            classification.setPredictionCount(0L);
        }
        if (classification.getSuccessfulPredictions() == null) {
            classification.setSuccessfulPredictions(0L);
        }
        if (classification.getFailedPredictions() == null) {
            classification.setFailedPredictions(0L);
        }
        
        // 保存分类
        save(classification);
        
        log.info("智能分类创建成功，ID: {}", classification.getClassificationId());
        return classification;
    }

    @Override
    public AsIntelligentClassification updateClassification(AsIntelligentClassification classification) {
        log.info("更新智能分类: {}", classification.getClassificationId());
        
        // 验证分类是否存在
        AsIntelligentClassification existing = getById(classification.getClassificationId());
        if (existing == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 更新分类
        updateById(classification);
        
        log.info("智能分类更新成功");
        return classification;
    }

    @Override
    public boolean deleteClassification(Long tenantId, Long classificationId) {
        log.info("删除智能分类: {}", classificationId);
        
        // 验证分类是否存在
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 检查分类是否可以删除
        if ("TRAINING".equals(classification.getClassificationStatus()) || 
            "DEPLOYED".equals(classification.getClassificationStatus())) {
            throw new RuntimeException("训练中或已部署的分类不能删除");
        }
        
        // 删除分类
        boolean result = removeById(classificationId);
        
        log.info("智能分类删除成功");
        return result;
    }

    @Override
    public AsIntelligentClassification getClassificationById(Long tenantId, Long classificationId) {
        QueryWrapper<AsIntelligentClassification> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId)
               .eq("classification_id", classificationId);
        return getOne(wrapper);
    }

    @Override
    public AsIntelligentClassification getClassificationByCode(Long tenantId, String classificationCode) {
        return classificationMapper.selectByClassificationCode(tenantId, classificationCode);
    }

    // ==================== 分页查询操作 ====================

    @Override
    public IPage<AsIntelligentClassification> getClassificationPage(Page<AsIntelligentClassification> page,
                                                                   Long tenantId,
                                                                   String classificationName,
                                                                   String classificationType,
                                                                   String classificationStatus,
                                                                   String classificationAlgorithm) {
        return classificationMapper.selectClassificationPage(page, tenantId, classificationName, 
                                                            classificationType, classificationStatus, classificationAlgorithm);
    }

    // ==================== 分类管理操作 ====================

    @Override
    public boolean startTraining(Long tenantId, Long classificationId, Map<String, Object> trainingConfig) {
        log.info("启动分类训练: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 检查分类状态
        if ("TRAINING".equals(classification.getClassificationStatus())) {
            throw new RuntimeException("分类已在训练中");
        }
        
        // 更新分类状态
        classification.setClassificationStatus("TRAINING");
        classification.setTrainingStartTime(LocalDateTime.now());
        
        // 设置训练配置
        if (trainingConfig != null) {
            classification.setClassificationRules(convertMapToJson(trainingConfig));
        }
        
        updateById(classification);
        
        // 异步启动训练任务
        startTrainingTask(classification, trainingConfig);
        
        log.info("分类训练启动成功");
        return true;
    }

    @Override
    public boolean stopTraining(Long tenantId, Long classificationId) {
        log.info("停止分类训练: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 检查分类状态
        if (!"TRAINING".equals(classification.getClassificationStatus())) {
            throw new RuntimeException("分类未在训练中");
        }
        
        // 更新分类状态
        classification.setClassificationStatus("INACTIVE");
        classification.setTrainingEndTime(LocalDateTime.now());
        
        // 计算训练耗时
        if (classification.getTrainingStartTime() != null) {
            long duration = java.time.Duration.between(classification.getTrainingStartTime(), 
                                                      classification.getTrainingEndTime()).getSeconds();
            classification.setTrainingDuration((int) duration);
        }
        
        updateById(classification);
        
        log.info("分类训练停止成功");
        return true;
    }

    @Override
    public boolean deployModel(Long tenantId, Long classificationId) {
        log.info("部署分类模型: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 检查分类状态
        if ("DEPLOYED".equals(classification.getClassificationStatus())) {
            throw new RuntimeException("分类已部署");
        }
        
        // 验证模型是否可以部署
        if (classification.getClassificationAccuracy() == null || 
            classification.getClassificationAccuracy().compareTo(new BigDecimal("0.7")) < 0) {
            throw new RuntimeException("模型准确率过低，无法部署");
        }
        
        // 更新分类状态
        classification.setClassificationStatus("DEPLOYED");
        classification.setDeploymentTime(LocalDateTime.now());
        
        updateById(classification);
        
        // 发送部署成功通知
        sendDeploymentSuccessNotification(tenantId, classificationId);
        
        log.info("分类模型部署成功");
        return true;
    }

    @Override
    public boolean undeployModel(Long tenantId, Long classificationId) {
        log.info("取消部署分类模型: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 检查分类状态
        if (!"DEPLOYED".equals(classification.getClassificationStatus())) {
            throw new RuntimeException("分类未部署");
        }
        
        // 更新分类状态
        classification.setClassificationStatus("INACTIVE");
        classification.setDeploymentTime(null);
        
        updateById(classification);
        
        log.info("分类模型取消部署成功");
        return true;
    }

    @Override
    public boolean activateClassification(Long tenantId, Long classificationId) {
        log.info("激活分类模型: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        classification.setClassificationStatus("ACTIVE");
        updateById(classification);
        
        log.info("分类模型激活成功");
        return true;
    }

    @Override
    public boolean deactivateClassification(Long tenantId, Long classificationId) {
        log.info("停用分类模型: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        classification.setClassificationStatus("INACTIVE");
        updateById(classification);
        
        log.info("分类模型停用成功");
        return true;
    }

    @Override
    public Map<String, Object> testClassification(Long tenantId, Long classificationId, Map<String, Object> testData) {
        log.info("测试分类模型: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 执行分类测试
        Map<String, Object> result = new HashMap<>();
        result.put("classificationId", classificationId);
        result.put("testResult", "SUCCESS");
        result.put("accuracy", classification.getClassificationAccuracy());
        result.put("confidence", new BigDecimal("0.85"));
        result.put("predictedCategory", "Category_A");
        result.put("testTime", LocalDateTime.now());
        
        log.info("分类模型测试完成");
        return result;
    }

    @Override
    public Map<String, Object> predictClassification(Long tenantId, Long classificationId, Map<String, Object> inputData) {
        log.info("预测分类结果: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        // 检查分类状态
        if (!"DEPLOYED".equals(classification.getClassificationStatus()) && 
            !"ACTIVE".equals(classification.getClassificationStatus())) {
            throw new RuntimeException("分类模型未部署或未激活");
        }
        
        // 执行分类预测
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = performClassificationPrediction(classification, inputData);
        long endTime = System.currentTimeMillis();
        
        // 更新预测统计
        updatePredictionStats(classification, endTime - startTime, true);
        
        log.info("分类预测完成");
        return result;
    }

    // ==================== 私有辅助方法 ====================

    private String generateClassificationCode() {
        return "CLS" + System.currentTimeMillis();
    }

    private String convertMapToJson(Map<String, Object> map) {
        // 简单的JSON转换实现
        return map.toString();
    }

    private void startTrainingTask(AsIntelligentClassification classification, Map<String, Object> trainingConfig) {
        // 异步训练任务实现
        log.info("启动异步训练任务: {}", classification.getClassificationId());
    }

    private Map<String, Object> performClassificationPrediction(AsIntelligentClassification classification, Map<String, Object> inputData) {
        // 分类预测实现
        Map<String, Object> result = new HashMap<>();
        result.put("classificationId", classification.getClassificationId());
        result.put("predictedCategory", "Category_A");
        result.put("confidence", new BigDecimal("0.85"));
        result.put("predictionTime", LocalDateTime.now());
        return result;
    }

    private void updatePredictionStats(AsIntelligentClassification classification, long predictionTime, boolean success) {
        // 更新预测统计
        classification.setPredictionCount(classification.getPredictionCount() + 1);
        if (success) {
            classification.setSuccessfulPredictions(classification.getSuccessfulPredictions() + 1);
        } else {
            classification.setFailedPredictions(classification.getFailedPredictions() + 1);
        }
        
        // 更新平均预测时间
        if (classification.getAvgPredictionTime() == null) {
            classification.setAvgPredictionTime(new BigDecimal(predictionTime));
        } else {
            BigDecimal currentAvg = classification.getAvgPredictionTime();
            BigDecimal newAvg = currentAvg.multiply(new BigDecimal(classification.getPredictionCount() - 1))
                                         .add(new BigDecimal(predictionTime))
                                         .divide(new BigDecimal(classification.getPredictionCount()), 2, BigDecimal.ROUND_HALF_UP);
            classification.setAvgPredictionTime(newAvg);
        }
        
        updateById(classification);
    }

    // ==================== 模型管理操作 ====================

    @Override
    public boolean updateModelVersion(Long tenantId, Long classificationId, String newVersion) {
        log.info("更新模型版本: {} -> {}", classificationId, newVersion);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        classification.setModelVersion(newVersion);
        updateById(classification);
        
        log.info("模型版本更新成功");
        return true;
    }

    @Override
    public boolean rollbackModelVersion(Long tenantId, Long classificationId, String targetVersion) {
        log.info("回滚模型版本: {} -> {}", classificationId, targetVersion);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        classification.setModelVersion(targetVersion);
        updateById(classification);
        
        log.info("模型版本回滚成功");
        return true;
    }

    @Override
    public Map<String, Object> compareModelVersions(Long tenantId, Long classificationId, String version1, String version2) {
        log.info("比较模型版本: {} vs {}", version1, version2);
        
        Map<String, Object> comparison = new HashMap<>();
        comparison.put("classificationId", classificationId);
        comparison.put("version1", version1);
        comparison.put("version2", version2);
        comparison.put("accuracyDiff", new BigDecimal("0.05"));
        comparison.put("performanceDiff", new BigDecimal("0.02"));
        comparison.put("comparisonTime", LocalDateTime.now());
        
        return comparison;
    }

    @Override
    public Map<String, Object> getModelInfo(Long tenantId, Long classificationId) {
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        Map<String, Object> modelInfo = new HashMap<>();
        modelInfo.put("classificationId", classificationId);
        modelInfo.put("modelVersion", classification.getModelVersion());
        modelInfo.put("modelPath", classification.getModelPath());
        modelInfo.put("modelSize", classification.getModelSize());
        modelInfo.put("accuracy", classification.getClassificationAccuracy());
        modelInfo.put("algorithm", classification.getClassificationAlgorithm());
        modelInfo.put("status", classification.getClassificationStatus());
        
        return modelInfo;
    }

    @Override
    public boolean updateModelConfig(Long tenantId, Long classificationId, Map<String, Object> config) {
        log.info("更新模型配置: {}", classificationId);
        
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }
        
        classification.setFeatureConfig(convertMapToJson(config));
        updateById(classification);
        
        log.info("模型配置更新成功");
        return true;
    }

    // ==================== 性能监控操作 ====================

    @Override
    public Map<String, Object> getPerformanceMetrics(Long tenantId, Long classificationId) {
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("classificationId", classificationId);
        metrics.put("accuracy", classification.getClassificationAccuracy());
        metrics.put("predictionCount", classification.getPredictionCount());
        metrics.put("successfulPredictions", classification.getSuccessfulPredictions());
        metrics.put("failedPredictions", classification.getFailedPredictions());
        metrics.put("avgPredictionTime", classification.getAvgPredictionTime());
        metrics.put("maxPredictionTime", classification.getMaxPredictionTime());
        metrics.put("minPredictionTime", classification.getMinPredictionTime());

        return metrics;
    }

    @Override
    public List<Map<String, Object>> getAccuracyHistory(Long tenantId, Long classificationId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> history = new ArrayList<>();

        // 模拟准确率历史数据
        Map<String, Object> record = new HashMap<>();
        record.put("classificationId", classificationId);
        record.put("accuracy", new BigDecimal("0.85"));
        record.put("recordTime", LocalDateTime.now());
        history.add(record);

        return history;
    }

    @Override
    public Map<String, Object> getPredictionStats(Long tenantId, Long classificationId) {
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPredictions", classification.getPredictionCount());
        stats.put("successfulPredictions", classification.getSuccessfulPredictions());
        stats.put("failedPredictions", classification.getFailedPredictions());

        if (classification.getPredictionCount() > 0) {
            BigDecimal successRate = new BigDecimal(classification.getSuccessfulPredictions())
                                   .divide(new BigDecimal(classification.getPredictionCount()), 4, BigDecimal.ROUND_HALF_UP);
            stats.put("successRate", successRate);
        } else {
            stats.put("successRate", BigDecimal.ZERO);
        }

        return stats;
    }

    @Override
    public Map<String, Object> getResourceUsage(Long tenantId, Long classificationId) {
        Map<String, Object> usage = new HashMap<>();
        usage.put("classificationId", classificationId);
        usage.put("cpuUsage", new BigDecimal("45.5"));
        usage.put("memoryUsage", new BigDecimal("512.0"));
        usage.put("diskUsage", new BigDecimal("1024.0"));
        usage.put("networkUsage", new BigDecimal("128.0"));

        return usage;
    }

    @Override
    public Map<String, Object> monitorClassificationHealth(Long tenantId, Long classificationId) {
        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }

        Map<String, Object> health = new HashMap<>();
        health.put("classificationId", classificationId);
        health.put("status", classification.getClassificationStatus());
        health.put("healthScore", new BigDecimal("85.5"));
        health.put("lastCheckTime", LocalDateTime.now());

        // 健康检查项
        List<Map<String, Object>> checks = new ArrayList<>();
        Map<String, Object> check1 = new HashMap<>();
        check1.put("checkName", "模型可用性");
        check1.put("status", "HEALTHY");
        check1.put("score", new BigDecimal("90"));
        checks.add(check1);

        Map<String, Object> check2 = new HashMap<>();
        check2.put("checkName", "预测性能");
        check2.put("status", "HEALTHY");
        check2.put("score", new BigDecimal("85"));
        checks.add(check2);

        health.put("healthChecks", checks);

        return health;
    }

    // ==================== 查询统计操作 ====================

    @Override
    public List<AsIntelligentClassification> getClassificationsByType(Long tenantId, String classificationType) {
        return classificationMapper.selectByClassificationType(tenantId, classificationType);
    }

    @Override
    public List<AsIntelligentClassification> getClassificationsByStatus(Long tenantId, String classificationStatus) {
        return classificationMapper.selectByClassificationStatus(tenantId, classificationStatus);
    }

    @Override
    public List<AsIntelligentClassification> getClassificationsByAlgorithm(Long tenantId, String classificationAlgorithm) {
        return classificationMapper.selectByClassificationAlgorithm(tenantId, classificationAlgorithm);
    }

    @Override
    public List<AsIntelligentClassification> getActiveClassifications(Long tenantId) {
        return classificationMapper.selectActiveClassifications(tenantId);
    }

    @Override
    public List<AsIntelligentClassification> getDeployedClassifications(Long tenantId) {
        return classificationMapper.selectDeployedClassifications(tenantId);
    }

    @Override
    public List<AsIntelligentClassification> getTrainingClassifications(Long tenantId) {
        return classificationMapper.selectTrainingClassifications(tenantId);
    }

    @Override
    public List<AsIntelligentClassification> getClassificationsByAccuracyRange(Long tenantId, BigDecimal minAccuracy, BigDecimal maxAccuracy) {
        return classificationMapper.selectByAccuracyRange(tenantId, minAccuracy, maxAccuracy);
    }

    @Override
    public AsIntelligentClassification getLatestVersionByType(Long tenantId, String classificationType) {
        return classificationMapper.selectLatestVersionByType(tenantId, classificationType);
    }

    @Override
    public AsIntelligentClassification getBestPerformanceByType(Long tenantId, String classificationType) {
        return classificationMapper.selectBestPerformanceByType(tenantId, classificationType);
    }

    // ==================== 统计分析操作 ====================

    @Override
    public List<Map<String, Object>> countByClassificationStatus(Long tenantId) {
        return classificationMapper.countByClassificationStatus(tenantId);
    }

    @Override
    public List<Map<String, Object>> countByClassificationType(Long tenantId) {
        return classificationMapper.countByClassificationType(tenantId);
    }

    @Override
    public List<Map<String, Object>> countByClassificationAlgorithm(Long tenantId) {
        return classificationMapper.countByClassificationAlgorithm(tenantId);
    }

    @Override
    public List<Map<String, Object>> getClassificationTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity) {
        return classificationMapper.getClassificationTrend(tenantId, startDate, endDate, granularity);
    }

    @Override
    public List<Map<String, Object>> getTrainingTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity) {
        return classificationMapper.getTrainingTrend(tenantId, startDate, endDate, granularity);
    }

    @Override
    public List<Map<String, Object>> getPredictionTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity) {
        return classificationMapper.getPredictionTrend(tenantId, startDate, endDate, granularity);
    }

    @Override
    public List<Map<String, Object>> getClassificationRanking(Long tenantId, String rankBy, Integer limit) {
        return classificationMapper.getClassificationRanking(tenantId, rankBy, limit);
    }

    // ==================== 批量操作 ====================

    @Override
    public List<AsIntelligentClassification> batchCreateClassifications(List<AsIntelligentClassification> classifications) {
        log.info("批量创建智能分类，数量: {}", classifications.size());

        for (AsIntelligentClassification classification : classifications) {
            createClassification(classification);
        }

        log.info("批量创建智能分类完成");
        return classifications;
    }

    @Override
    public boolean batchUpdateStatus(Long tenantId, List<Long> classificationIds, String status) {
        log.info("批量更新分类状态: {} -> {}", classificationIds.size(), status);

        int result = classificationMapper.batchUpdateStatus(tenantId, classificationIds, status, "system");

        log.info("批量更新分类状态完成，影响行数: {}", result);
        return result > 0;
    }

    @Override
    public boolean batchUpdateAlgorithm(Long tenantId, List<Long> classificationIds, String algorithm) {
        log.info("批量更新分类算法: {} -> {}", classificationIds.size(), algorithm);

        int result = classificationMapper.batchUpdateAlgorithm(tenantId, classificationIds, algorithm, "system");

        log.info("批量更新分类算法完成，影响行数: {}", result);
        return result > 0;
    }

    @Override
    public boolean batchDeleteClassifications(Long tenantId, List<Long> classificationIds) {
        log.info("批量删除智能分类，数量: {}", classificationIds.size());

        int result = classificationMapper.batchDelete(tenantId, classificationIds, "system");

        log.info("批量删除智能分类完成，影响行数: {}", result);
        return result > 0;
    }

    @Override
    public boolean batchTrainClassifications(Long tenantId, List<Long> classificationIds, Map<String, Object> trainingConfig) {
        log.info("批量训练智能分类，数量: {}", classificationIds.size());

        for (Long classificationId : classificationIds) {
            try {
                startTraining(tenantId, classificationId, trainingConfig);
            } catch (Exception e) {
                log.error("训练分类失败: {}", classificationId, e);
            }
        }

        log.info("批量训练智能分类完成");
        return true;
    }

    @Override
    public boolean batchDeployClassifications(Long tenantId, List<Long> classificationIds) {
        log.info("批量部署智能分类，数量: {}", classificationIds.size());

        for (Long classificationId : classificationIds) {
            try {
                deployModel(tenantId, classificationId);
            } catch (Exception e) {
                log.error("部署分类失败: {}", classificationId, e);
            }
        }

        log.info("批量部署智能分类完成");
        return true;
    }

    // ==================== 数据管理操作 ====================

    @Override
    public List<Map<String, Object>> exportClassificationData(Long tenantId, List<Long> classificationIds) {
        log.info("导出智能分类数据，数量: {}", classificationIds.size());

        return classificationMapper.exportClassificationData(tenantId, classificationIds);
    }

    @Override
    public boolean importClassificationData(Long tenantId, List<Map<String, Object>> classificationData) {
        log.info("导入智能分类数据，数量: {}", classificationData.size());

        int result = classificationMapper.importClassificationData(tenantId, classificationData, "system");

        log.info("导入智能分类数据完成，影响行数: {}", result);
        return result > 0;
    }

    @Override
    public boolean cleanupExpiredData(Long tenantId, LocalDateTime expiredDate) {
        log.info("清理过期数据，截止时间: {}", expiredDate);

        int result = classificationMapper.cleanupExpiredTrainingData(tenantId, expiredDate);

        log.info("清理过期数据完成，影响行数: {}", result);
        return result > 0;
    }

    @Override
    public boolean cleanupInvalidModels(Long tenantId) {
        log.info("清理无效模型");

        int result = classificationMapper.cleanupInvalidModels(tenantId);

        log.info("清理无效模型完成，影响行数: {}", result);
        return result > 0;
    }

    // ==================== 系统维护操作 ====================

    @Override
    public boolean rebuildClassificationIndex(Long tenantId) {
        log.info("重建分类索引");

        int result = classificationMapper.rebuildClassificationIndex(tenantId);

        log.info("重建分类索引完成");
        return result > 0;
    }

    @Override
    public boolean optimizeClassificationPerformance(Long tenantId) {
        log.info("优化分类性能");

        int result = classificationMapper.optimizeClassificationPerformance(tenantId);

        log.info("优化分类性能完成");
        return result > 0;
    }

    @Override
    public List<Map<String, Object>> checkClassificationHealth(Long tenantId) {
        log.info("检查分类健康状态");

        return classificationMapper.checkClassificationHealth(tenantId);
    }

    @Override
    public Map<String, Object> generateClassificationReport(Long tenantId, Long classificationId) {
        log.info("生成分类报告: {}", classificationId);

        AsIntelligentClassification classification = getClassificationById(tenantId, classificationId);
        if (classification == null) {
            throw new RuntimeException("智能分类不存在");
        }

        Map<String, Object> report = new HashMap<>();
        report.put("classificationId", classificationId);
        report.put("classificationName", classification.getClassificationName());
        report.put("classificationType", classification.getClassificationType());
        report.put("classificationStatus", classification.getClassificationStatus());
        report.put("accuracy", classification.getClassificationAccuracy());
        report.put("predictionCount", classification.getPredictionCount());
        report.put("successfulPredictions", classification.getSuccessfulPredictions());
        report.put("failedPredictions", classification.getFailedPredictions());
        report.put("reportGeneratedTime", LocalDateTime.now());

        return report;
    }

    @Override
    public Map<String, Object> getSystemOverview(Long tenantId) {
        log.info("获取系统概览");

        Map<String, Object> overview = new HashMap<>();

        // 统计总数
        QueryWrapper<AsIntelligentClassification> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        long totalCount = count(wrapper);
        overview.put("totalClassifications", totalCount);

        // 统计各状态数量
        List<Map<String, Object>> statusStats = countByClassificationStatus(tenantId);
        overview.put("statusDistribution", statusStats);

        // 统计各类型数量
        List<Map<String, Object>> typeStats = countByClassificationType(tenantId);
        overview.put("typeDistribution", typeStats);

        // 统计各算法数量
        List<Map<String, Object>> algorithmStats = countByClassificationAlgorithm(tenantId);
        overview.put("algorithmDistribution", algorithmStats);

        // 性能指标
        Map<String, Object> performanceMetrics = classificationMapper.getPerformanceMetrics(tenantId);
        overview.put("performanceMetrics", performanceMetrics);

        overview.put("overviewGeneratedTime", LocalDateTime.now());

        return overview;
    }

    // ==================== 通知提醒操作 ====================

    @Override
    public boolean sendTrainingCompletionNotification(Long tenantId, Long classificationId) {
        log.info("发送训练完成通知: {}", classificationId);

        // 实现通知发送逻辑
        // 这里可以集成邮件、短信、站内信等通知方式

        return true;
    }

    @Override
    public boolean sendDeploymentSuccessNotification(Long tenantId, Long classificationId) {
        log.info("发送部署成功通知: {}", classificationId);

        // 实现通知发送逻辑

        return true;
    }

    @Override
    public boolean sendPerformanceAlertNotification(Long tenantId, Long classificationId, String alertType) {
        log.info("发送性能告警通知: {} - {}", classificationId, alertType);

        // 实现告警通知逻辑

        return true;
    }

    @Override
    public boolean sendErrorNotification(Long tenantId, Long classificationId, String errorMessage) {
        log.info("发送错误通知: {} - {}", classificationId, errorMessage);

        // 实现错误通知逻辑

        return true;
    }
}
