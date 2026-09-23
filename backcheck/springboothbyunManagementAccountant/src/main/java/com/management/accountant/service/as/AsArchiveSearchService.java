package com.management.accountant.service.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.as.AsArchiveSearch;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案检索服务接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface AsArchiveSearchService extends IService<AsArchiveSearch> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建档案检索
     */
    AsArchiveSearch createArchiveSearch(AsArchiveSearch archiveSearch);

    /**
     * 更新档案检索
     */
    AsArchiveSearch updateArchiveSearch(AsArchiveSearch archiveSearch);

    /**
     * 删除档案检索
     */
    boolean deleteArchiveSearch(Long tenantId, Long searchId);

    /**
     * 根据ID查询档案检索
     */
    AsArchiveSearch getArchiveSearchById(Long tenantId, Long searchId);

    /**
     * 根据编号查询档案检索
     */
    AsArchiveSearch getArchiveSearchByCode(Long tenantId, String searchCode);

    /**
     * 分页查询档案检索
     */
    IPage<AsArchiveSearch> getArchiveSearchPage(Page<AsArchiveSearch> page, Long tenantId, 
                                               String searchName, String searchType, 
                                               String searchStatus, String searchEngine);

    // ==================== 检索管理操作 ====================

    /**
     * 启动检索服务
     */
    boolean startSearchService(Long tenantId, Long searchId);

    /**
     * 停止检索服务
     */
    boolean stopSearchService(Long tenantId, Long searchId);

    /**
     * 重启检索服务
     */
    boolean restartSearchService(Long tenantId, Long searchId);

    /**
     * 执行检索
     */
    Map<String, Object> executeSearch(Long tenantId, Long searchId, Map<String, Object> searchParams);

    /**
     * 全文检索
     */
    Map<String, Object> fullTextSearch(Long tenantId, Long searchId, String keywords, Map<String, Object> filters);

    /**
     * 语义检索
     */
    Map<String, Object> semanticSearch(Long tenantId, Long searchId, String query, Map<String, Object> context);

    /**
     * 图像检索
     */
    Map<String, Object> imageSearch(Long tenantId, Long searchId, String imageData, Map<String, Object> options);

    /**
     * 语音检索
     */
    Map<String, Object> voiceSearch(Long tenantId, Long searchId, String audioData, Map<String, Object> options);

    /**
     * 混合检索
     */
    Map<String, Object> hybridSearch(Long tenantId, Long searchId, Map<String, Object> multiModalData);

    // ==================== 索引管理操作 ====================

    /**
     * 创建索引
     */
    boolean createIndex(Long tenantId, Long searchId, Map<String, Object> indexConfig);

    /**
     * 重建索引
     */
    boolean rebuildIndex(Long tenantId, Long searchId);

    /**
     * 优化索引
     */
    boolean optimizeIndex(Long tenantId, Long searchId);

    /**
     * 删除索引
     */
    boolean deleteIndex(Long tenantId, Long searchId);

    /**
     * 更新索引配置
     */
    boolean updateIndexConfig(Long tenantId, Long searchId, Map<String, Object> indexConfig);

    /**
     * 获取索引状态
     */
    Map<String, Object> getIndexStatus(Long tenantId, Long searchId);

    /**
     * 获取索引统计
     */
    Map<String, Object> getIndexStatistics(Long tenantId, Long searchId);

    // ==================== OCR处理操作 ====================

    /**
     * 启动OCR处理
     */
    boolean startOcrProcessing(Long tenantId, Long searchId, Map<String, Object> ocrConfig);

    /**
     * 停止OCR处理
     */
    boolean stopOcrProcessing(Long tenantId, Long searchId);

    /**
     * 执行OCR识别
     */
    Map<String, Object> executeOcrRecognition(Long tenantId, Long searchId, String documentPath);

    /**
     * 批量OCR处理
     */
    Map<String, Object> batchOcrProcessing(Long tenantId, Long searchId, List<String> documentPaths);

    /**
     * 获取OCR处理结果
     */
    Map<String, Object> getOcrResults(Long tenantId, Long searchId, String taskId);

    /**
     * 更新OCR配置
     */
    boolean updateOcrConfig(Long tenantId, Long searchId, Map<String, Object> ocrConfig);

    // ==================== 语义检索操作 ====================

    /**
     * 初始化语义模型
     */
    boolean initializeSemanticModel(Long tenantId, Long searchId, Map<String, Object> modelConfig);

    /**
     * 更新语义模型
     */
    boolean updateSemanticModel(Long tenantId, Long searchId, String modelVersion);

    /**
     * 训练语义模型
     */
    boolean trainSemanticModel(Long tenantId, Long searchId, Map<String, Object> trainingData);

    /**
     * 语义相似度计算
     */
    Map<String, Object> calculateSemanticSimilarity(Long tenantId, Long searchId, String text1, String text2);

    /**
     * 语义向量化
     */
    Map<String, Object> semanticVectorization(Long tenantId, Long searchId, String text);

    // ==================== 推荐系统操作 ====================

    /**
     * 启动推荐系统
     */
    boolean startRecommendationSystem(Long tenantId, Long searchId, Map<String, Object> recommendConfig);

    /**
     * 停止推荐系统
     */
    boolean stopRecommendationSystem(Long tenantId, Long searchId);

    /**
     * 获取推荐结果
     */
    Map<String, Object> getRecommendations(Long tenantId, Long searchId, String userId, Map<String, Object> context);

    /**
     * 更新推荐算法
     */
    boolean updateRecommendationAlgorithm(Long tenantId, Long searchId, String algorithm);

    /**
     * 训练推荐模型
     */
    boolean trainRecommendationModel(Long tenantId, Long searchId, Map<String, Object> trainingData);

    // ==================== 性能优化操作 ====================

    /**
     * 启动性能优化
     */
    boolean startPerformanceOptimization(Long tenantId, Long searchId);

    /**
     * 停止性能优化
     */
    boolean stopPerformanceOptimization(Long tenantId, Long searchId);

    /**
     * 配置缓存策略
     */
    boolean configureCacheStrategy(Long tenantId, Long searchId, Map<String, Object> cacheConfig);

    /**
     * 清理缓存
     */
    boolean clearCache(Long tenantId, Long searchId);

    /**
     * 预热缓存
     */
    boolean warmupCache(Long tenantId, Long searchId, List<String> keywords);

    // ==================== 查询统计操作 ====================

    /**
     * 根据检索类型查询
     */
    List<AsArchiveSearch> getArchiveSearchesByType(Long tenantId, String searchType);

    /**
     * 根据检索状态查询
     */
    List<AsArchiveSearch> getArchiveSearchesByStatus(Long tenantId, String searchStatus);

    /**
     * 根据检索引擎查询
     */
    List<AsArchiveSearch> getArchiveSearchesByEngine(Long tenantId, String searchEngine);

    /**
     * 查询活跃的检索配置
     */
    List<AsArchiveSearch> getActiveArchiveSearches(Long tenantId);

    /**
     * 查询正在索引的检索配置
     */
    List<AsArchiveSearch> getIndexingArchiveSearches(Long tenantId);

    /**
     * 根据准确率范围查询
     */
    List<AsArchiveSearch> getArchiveSearchesByAccuracyRange(Long tenantId, BigDecimal minAccuracy, BigDecimal maxAccuracy);

    /**
     * 根据响应时间范围查询
     */
    List<AsArchiveSearch> getArchiveSearchesByResponseTimeRange(Long tenantId, Long minTime, Long maxTime);

    /**
     * 查询最新版本的检索配置
     */
    AsArchiveSearch getLatestVersionByType(Long tenantId, String searchType);

    /**
     * 查询性能最佳的检索配置
     */
    AsArchiveSearch getBestPerformanceByType(Long tenantId, String searchType);

    // ==================== 统计分析操作 ====================

    /**
     * 统计检索状态分布
     */
    List<Map<String, Object>> countBySearchStatus(Long tenantId);

    /**
     * 统计检索类型分布
     */
    List<Map<String, Object>> countBySearchType(Long tenantId);

    /**
     * 统计检索引擎分布
     */
    List<Map<String, Object>> countBySearchEngine(Long tenantId);

    /**
     * 获取检索趋势
     */
    List<Map<String, Object>> getSearchTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity);

    /**
     * 获取索引趋势
     */
    List<Map<String, Object>> getIndexTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity);

    /**
     * 获取性能趋势
     */
    List<Map<String, Object>> getPerformanceTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String granularity);

    /**
     * 获取检索排行榜
     */
    List<Map<String, Object>> getSearchRanking(Long tenantId, String rankBy, Integer limit);

    /**
     * 获取热门关键词
     */
    List<Map<String, Object>> getPopularKeywords(Long tenantId, Integer limit);

    /**
     * 获取用户行为分析
     */
    List<Map<String, Object>> getUserBehaviorAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    // ==================== 性能监控操作 ====================

    /**
     * 获取性能指标
     */
    Map<String, Object> getPerformanceMetrics(Long tenantId, Long searchId);

    /**
     * 获取准确率历史
     */
    List<Map<String, Object>> getAccuracyHistory(Long tenantId, Long searchId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取响应时间统计
     */
    Map<String, Object> getResponseTimeStats(Long tenantId, Long searchId);

    /**
     * 获取缓存命中率统计
     */
    Map<String, Object> getCacheHitRateStats(Long tenantId, Long searchId);

    /**
     * 监控检索健康状态
     */
    Map<String, Object> monitorSearchHealth(Long tenantId, Long searchId);

    // ==================== 批量操作 ====================

    /**
     * 批量创建检索配置
     */
    List<AsArchiveSearch> batchCreateArchiveSearches(List<AsArchiveSearch> archiveSearches);

    /**
     * 批量更新检索状态
     */
    boolean batchUpdateStatus(Long tenantId, List<Long> searchIds, String status);

    /**
     * 批量更新检索引擎
     */
    boolean batchUpdateEngine(Long tenantId, List<Long> searchIds, String engine);

    /**
     * 批量删除检索配置
     */
    boolean batchDeleteArchiveSearches(Long tenantId, List<Long> searchIds);

    /**
     * 批量重建索引
     */
    boolean batchRebuildIndex(Long tenantId, List<Long> searchIds);

    /**
     * 批量优化索引
     */
    boolean batchOptimizeIndex(Long tenantId, List<Long> searchIds);

    // ==================== 数据管理操作 ====================

    /**
     * 导出检索数据
     */
    List<Map<String, Object>> exportArchiveSearchData(Long tenantId, List<Long> searchIds);

    /**
     * 导入检索数据
     */
    boolean importArchiveSearchData(Long tenantId, List<Map<String, Object>> searchData);

    /**
     * 清理过期数据
     */
    boolean cleanupExpiredData(Long tenantId, LocalDateTime expiredDate);

    /**
     * 清理无效索引
     */
    boolean cleanupInvalidIndexes(Long tenantId);

    // ==================== 系统维护操作 ====================

    /**
     * 重建检索索引
     */
    boolean rebuildSearchIndex(Long tenantId);

    /**
     * 优化检索性能
     */
    boolean optimizeSearchPerformance(Long tenantId);

    /**
     * 检查检索健康状态
     */
    List<Map<String, Object>> checkSearchHealth(Long tenantId);

    /**
     * 生成检索报告
     */
    Map<String, Object> generateSearchReport(Long tenantId, Long searchId);

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(Long tenantId);

    // ==================== 通知提醒操作 ====================

    /**
     * 发送索引完成通知
     */
    boolean sendIndexCompletionNotification(Long tenantId, Long searchId);

    /**
     * 发送性能告警通知
     */
    boolean sendPerformanceAlertNotification(Long tenantId, Long searchId, String alertType);

    /**
     * 发送错误通知
     */
    boolean sendErrorNotification(Long tenantId, Long searchId, String errorMessage);

    /**
     * 发送系统维护通知
     */
    boolean sendMaintenanceNotification(Long tenantId, String maintenanceType);
}
