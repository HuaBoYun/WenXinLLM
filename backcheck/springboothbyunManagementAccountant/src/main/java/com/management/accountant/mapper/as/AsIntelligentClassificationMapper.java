package com.management.accountant.mapper.as;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsIntelligentClassification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案智能分类 Mapper 接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface AsIntelligentClassificationMapper extends BaseMapper<AsIntelligentClassification> {

    /**
     * 分页查询智能分类
     */
    IPage<AsIntelligentClassification> selectClassificationPage(Page<AsIntelligentClassification> page, 
                                                               @Param("tenantId") Long tenantId,
                                                               @Param("classificationName") String classificationName,
                                                               @Param("classificationType") String classificationType,
                                                               @Param("classificationStatus") String classificationStatus,
                                                               @Param("classificationAlgorithm") String classificationAlgorithm);

    /**
     * 根据分类编号查询
     */
    AsIntelligentClassification selectByClassificationCode(@Param("tenantId") Long tenantId, 
                                                          @Param("classificationCode") String classificationCode);

    /**
     * 根据分类类型查询
     */
    List<AsIntelligentClassification> selectByClassificationType(@Param("tenantId") Long tenantId, 
                                                                @Param("classificationType") String classificationType);

    /**
     * 根据分类状态查询
     */
    List<AsIntelligentClassification> selectByClassificationStatus(@Param("tenantId") Long tenantId, 
                                                                  @Param("classificationStatus") String classificationStatus);

    /**
     * 根据分类算法查询
     */
    List<AsIntelligentClassification> selectByClassificationAlgorithm(@Param("tenantId") Long tenantId, 
                                                                     @Param("classificationAlgorithm") String classificationAlgorithm);

    /**
     * 查询活跃的分类模型
     */
    List<AsIntelligentClassification> selectActiveClassifications(@Param("tenantId") Long tenantId);

    /**
     * 查询已部署的分类模型
     */
    List<AsIntelligentClassification> selectDeployedClassifications(@Param("tenantId") Long tenantId);

    /**
     * 查询训练中的分类模型
     */
    List<AsIntelligentClassification> selectTrainingClassifications(@Param("tenantId") Long tenantId);

    /**
     * 根据准确率范围查询
     */
    List<AsIntelligentClassification> selectByAccuracyRange(@Param("tenantId") Long tenantId,
                                                           @Param("minAccuracy") BigDecimal minAccuracy,
                                                           @Param("maxAccuracy") BigDecimal maxAccuracy);

    /**
     * 根据置信度阈值查询
     */
    List<AsIntelligentClassification> selectByConfidenceThreshold(@Param("tenantId") Long tenantId,
                                                                 @Param("confidenceThreshold") BigDecimal confidenceThreshold);

    /**
     * 根据训练时间范围查询
     */
    List<AsIntelligentClassification> selectByTrainingTimeRange(@Param("tenantId") Long tenantId,
                                                               @Param("startTime") LocalDateTime startTime,
                                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 根据部署时间范围查询
     */
    List<AsIntelligentClassification> selectByDeploymentTimeRange(@Param("tenantId") Long tenantId,
                                                                 @Param("startTime") LocalDateTime startTime,
                                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 根据预测次数范围查询
     */
    List<AsIntelligentClassification> selectByPredictionCountRange(@Param("tenantId") Long tenantId,
                                                                  @Param("minCount") Long minCount,
                                                                  @Param("maxCount") Long maxCount);

    /**
     * 根据模型版本查询
     */
    List<AsIntelligentClassification> selectByModelVersion(@Param("tenantId") Long tenantId,
                                                          @Param("modelVersion") String modelVersion);

    /**
     * 查询最新版本的分类模型
     */
    AsIntelligentClassification selectLatestVersionByType(@Param("tenantId") Long tenantId,
                                                         @Param("classificationType") String classificationType);

    /**
     * 查询性能最佳的分类模型
     */
    AsIntelligentClassification selectBestPerformanceByType(@Param("tenantId") Long tenantId,
                                                           @Param("classificationType") String classificationType);

    /**
     * 统计分类状态分布
     */
    List<Map<String, Object>> countByClassificationStatus(@Param("tenantId") Long tenantId);

    /**
     * 统计分类类型分布
     */
    List<Map<String, Object>> countByClassificationType(@Param("tenantId") Long tenantId);

    /**
     * 统计分类算法分布
     */
    List<Map<String, Object>> countByClassificationAlgorithm(@Param("tenantId") Long tenantId);

    /**
     * 统计模型版本分布
     */
    List<Map<String, Object>> countByModelVersion(@Param("tenantId") Long tenantId);

    /**
     * 统计准确率分布
     */
    List<Map<String, Object>> countByAccuracyRange(@Param("tenantId") Long tenantId);

    /**
     * 统计预测次数分布
     */
    List<Map<String, Object>> countByPredictionRange(@Param("tenantId") Long tenantId);

    /**
     * 获取分类趋势统计
     */
    List<Map<String, Object>> getClassificationTrend(@Param("tenantId") Long tenantId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate,
                                                     @Param("granularity") String granularity);

    /**
     * 获取训练趋势统计
     */
    List<Map<String, Object>> getTrainingTrend(@Param("tenantId") Long tenantId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("granularity") String granularity);

    /**
     * 获取部署趋势统计
     */
    List<Map<String, Object>> getDeploymentTrend(@Param("tenantId") Long tenantId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                @Param("granularity") String granularity);

    /**
     * 获取预测趋势统计
     */
    List<Map<String, Object>> getPredictionTrend(@Param("tenantId") Long tenantId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                @Param("granularity") String granularity);

    /**
     * 获取准确率趋势统计
     */
    List<Map<String, Object>> getAccuracyTrend(@Param("tenantId") Long tenantId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("granularity") String granularity);

    /**
     * 获取性能指标统计
     */
    Map<String, Object> getPerformanceMetrics(@Param("tenantId") Long tenantId);

    /**
     * 获取资源使用统计
     */
    Map<String, Object> getResourceUsageStats(@Param("tenantId") Long tenantId);

    /**
     * 获取错误统计
     */
    Map<String, Object> getErrorStats(@Param("tenantId") Long tenantId);

    /**
     * 获取分类排行榜
     */
    List<Map<String, Object>> getClassificationRanking(@Param("tenantId") Long tenantId,
                                                       @Param("rankBy") String rankBy,
                                                       @Param("limit") Integer limit);

    /**
     * 批量更新分类状态
     */
    int batchUpdateStatus(@Param("tenantId") Long tenantId,
                         @Param("classificationIds") List<Long> classificationIds,
                         @Param("status") String status,
                         @Param("updatedBy") String updatedBy);

    /**
     * 批量更新分类算法
     */
    int batchUpdateAlgorithm(@Param("tenantId") Long tenantId,
                            @Param("classificationIds") List<Long> classificationIds,
                            @Param("algorithm") String algorithm,
                            @Param("updatedBy") String updatedBy);

    /**
     * 批量删除分类
     */
    int batchDelete(@Param("tenantId") Long tenantId,
                   @Param("classificationIds") List<Long> classificationIds,
                   @Param("updatedBy") String updatedBy);

    /**
     * 清理过期的训练数据
     */
    int cleanupExpiredTrainingData(@Param("tenantId") Long tenantId,
                                  @Param("expiredDate") LocalDateTime expiredDate);

    /**
     * 清理无效的分类模型
     */
    int cleanupInvalidModels(@Param("tenantId") Long tenantId);

    /**
     * 重建分类索引
     */
    int rebuildClassificationIndex(@Param("tenantId") Long tenantId);

    /**
     * 优化分类性能
     */
    int optimizeClassificationPerformance(@Param("tenantId") Long tenantId);

    /**
     * 检查分类健康状态
     */
    List<Map<String, Object>> checkClassificationHealth(@Param("tenantId") Long tenantId);

    /**
     * 获取分类配置信息
     */
    Map<String, Object> getClassificationConfig(@Param("tenantId") Long tenantId,
                                               @Param("classificationId") Long classificationId);

    /**
     * 更新分类配置信息
     */
    int updateClassificationConfig(@Param("tenantId") Long tenantId,
                                  @Param("classificationId") Long classificationId,
                                  @Param("config") String config,
                                  @Param("updatedBy") String updatedBy);

    /**
     * 导出分类数据
     */
    List<Map<String, Object>> exportClassificationData(@Param("tenantId") Long tenantId,
                                                       @Param("classificationIds") List<Long> classificationIds);

    /**
     * 导入分类数据
     */
    int importClassificationData(@Param("tenantId") Long tenantId,
                                @Param("classificationData") List<Map<String, Object>> classificationData,
                                @Param("createdBy") String createdBy);
}
