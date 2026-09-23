package com.management.accountant.mapper.as;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsArchiveSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案检索Mapper接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface AsArchiveSearchMapper extends BaseMapper<AsArchiveSearch> {

    // ==================== 基础查询方法 ====================

    /**
     * 分页查询档案检索
     */
    IPage<AsArchiveSearch> selectSearchPage(Page<AsArchiveSearch> page, 
                                           @Param("tenantId") Long tenantId,
                                           @Param("searchName") String searchName,
                                           @Param("searchType") String searchType,
                                           @Param("searchStatus") String searchStatus,
                                           @Param("searchEngine") String searchEngine);

    /**
     * 根据检索编号查询
     */
    AsArchiveSearch selectBySearchCode(@Param("tenantId") Long tenantId, @Param("searchCode") String searchCode);

    /**
     * 根据检索类型查询
     */
    List<AsArchiveSearch> selectBySearchType(@Param("tenantId") Long tenantId, @Param("searchType") String searchType);

    /**
     * 根据检索状态查询
     */
    List<AsArchiveSearch> selectBySearchStatus(@Param("tenantId") Long tenantId, @Param("searchStatus") String searchStatus);

    /**
     * 根据检索引擎查询
     */
    List<AsArchiveSearch> selectBySearchEngine(@Param("tenantId") Long tenantId, @Param("searchEngine") String searchEngine);

    /**
     * 查询活跃的检索配置
     */
    List<AsArchiveSearch> selectActiveSearches(@Param("tenantId") Long tenantId);

    /**
     * 查询正在索引的检索配置
     */
    List<AsArchiveSearch> selectIndexingSearches(@Param("tenantId") Long tenantId);

    /**
     * 根据准确率范围查询
     */
    List<AsArchiveSearch> selectByAccuracyRange(@Param("tenantId") Long tenantId, 
                                               @Param("minAccuracy") BigDecimal minAccuracy, 
                                               @Param("maxAccuracy") BigDecimal maxAccuracy);

    /**
     * 根据响应时间范围查询
     */
    List<AsArchiveSearch> selectByResponseTimeRange(@Param("tenantId") Long tenantId, 
                                                   @Param("minTime") Long minTime, 
                                                   @Param("maxTime") Long maxTime);

    /**
     * 查询最新版本的检索配置
     */
    AsArchiveSearch selectLatestVersionByType(@Param("tenantId") Long tenantId, @Param("searchType") String searchType);

    /**
     * 查询性能最佳的检索配置
     */
    AsArchiveSearch selectBestPerformanceByType(@Param("tenantId") Long tenantId, @Param("searchType") String searchType);

    // ==================== 统计分析方法 ====================

    /**
     * 统计检索状态分布
     */
    List<Map<String, Object>> countBySearchStatus(@Param("tenantId") Long tenantId);

    /**
     * 统计检索类型分布
     */
    List<Map<String, Object>> countBySearchType(@Param("tenantId") Long tenantId);

    /**
     * 统计检索引擎分布
     */
    List<Map<String, Object>> countBySearchEngine(@Param("tenantId") Long tenantId);

    /**
     * 获取检索趋势数据
     */
    List<Map<String, Object>> getSearchTrend(@Param("tenantId") Long tenantId, 
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate, 
                                            @Param("granularity") String granularity);

    /**
     * 获取索引趋势数据
     */
    List<Map<String, Object>> getIndexTrend(@Param("tenantId") Long tenantId, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate, 
                                           @Param("granularity") String granularity);

    /**
     * 获取性能趋势数据
     */
    List<Map<String, Object>> getPerformanceTrend(@Param("tenantId") Long tenantId, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate, 
                                                 @Param("granularity") String granularity);

    /**
     * 获取检索排行榜
     */
    List<Map<String, Object>> getSearchRanking(@Param("tenantId") Long tenantId, 
                                              @Param("rankBy") String rankBy, 
                                              @Param("limit") Integer limit);

    /**
     * 获取热门关键词统计
     */
    List<Map<String, Object>> getPopularKeywords(@Param("tenantId") Long tenantId, 
                                                @Param("limit") Integer limit);

    /**
     * 获取用户行为分析
     */
    List<Map<String, Object>> getUserBehaviorAnalysis(@Param("tenantId") Long tenantId, 
                                                     @Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);

    // ==================== 性能监控方法 ====================

    /**
     * 获取性能指标
     */
    Map<String, Object> getPerformanceMetrics(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 获取准确率历史
     */
    List<Map<String, Object>> getAccuracyHistory(@Param("tenantId") Long tenantId, 
                                                @Param("searchId") Long searchId, 
                                                @Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 获取响应时间统计
     */
    Map<String, Object> getResponseTimeStats(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 获取索引状态信息
     */
    Map<String, Object> getIndexStatus(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 获取缓存命中率统计
     */
    Map<String, Object> getCacheHitRateStats(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 监控检索健康状态
     */
    Map<String, Object> monitorSearchHealth(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新检索状态
     */
    int batchUpdateStatus(@Param("tenantId") Long tenantId, 
                         @Param("searchIds") List<Long> searchIds, 
                         @Param("status") String status, 
                         @Param("updatedBy") String updatedBy);

    /**
     * 批量更新检索引擎
     */
    int batchUpdateEngine(@Param("tenantId") Long tenantId, 
                         @Param("searchIds") List<Long> searchIds, 
                         @Param("engine") String engine, 
                         @Param("updatedBy") String updatedBy);

    /**
     * 批量删除检索配置
     */
    int batchDeleteSearches(@Param("tenantId") Long tenantId, 
                           @Param("searchIds") List<Long> searchIds, 
                           @Param("updatedBy") String updatedBy);

    /**
     * 批量重建索引
     */
    int batchRebuildIndex(@Param("tenantId") Long tenantId, 
                         @Param("searchIds") List<Long> searchIds, 
                         @Param("updatedBy") String updatedBy);

    /**
     * 批量优化索引
     */
    int batchOptimizeIndex(@Param("tenantId") Long tenantId, 
                          @Param("searchIds") List<Long> searchIds, 
                          @Param("updatedBy") String updatedBy);

    // ==================== 数据管理方法 ====================

    /**
     * 导出检索数据
     */
    List<Map<String, Object>> exportSearchData(@Param("tenantId") Long tenantId, 
                                              @Param("searchIds") List<Long> searchIds);

    /**
     * 导入检索数据
     */
    int importSearchData(@Param("tenantId") Long tenantId, 
                        @Param("searchData") List<Map<String, Object>> searchData, 
                        @Param("createdBy") String createdBy);

    /**
     * 清理过期检索历史
     */
    int cleanupExpiredHistory(@Param("tenantId") Long tenantId, @Param("expiredDate") LocalDateTime expiredDate);

    /**
     * 清理无效索引
     */
    int cleanupInvalidIndexes(@Param("tenantId") Long tenantId);

    // ==================== 系统维护方法 ====================

    /**
     * 重建检索索引
     */
    int rebuildSearchIndex(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 优化检索性能
     */
    int optimizeSearchPerformance(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 检查检索健康状态
     */
    List<Map<String, Object>> checkSearchHealth(@Param("tenantId") Long tenantId);

    /**
     * 生成检索报告
     */
    Map<String, Object> generateSearchReport(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") Long tenantId);

    // ==================== OCR相关方法 ====================

    /**
     * 获取OCR处理统计
     */
    Map<String, Object> getOcrProcessingStats(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 更新OCR配置
     */
    int updateOcrConfig(@Param("tenantId") Long tenantId, 
                       @Param("searchId") Long searchId, 
                       @Param("ocrConfig") String ocrConfig, 
                       @Param("updatedBy") String updatedBy);

    // ==================== 语义检索相关方法 ====================

    /**
     * 获取语义检索统计
     */
    Map<String, Object> getSemanticSearchStats(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 更新语义模型版本
     */
    int updateSemanticModelVersion(@Param("tenantId") Long tenantId, 
                                  @Param("searchId") Long searchId, 
                                  @Param("modelVersion") String modelVersion, 
                                  @Param("updatedBy") String updatedBy);

    // ==================== 推荐系统相关方法 ====================

    /**
     * 获取推荐系统统计
     */
    Map<String, Object> getRecommendationStats(@Param("tenantId") Long tenantId, @Param("searchId") Long searchId);

    /**
     * 更新推荐算法
     */
    int updateRecommendationAlgorithm(@Param("tenantId") Long tenantId, 
                                     @Param("searchId") Long searchId, 
                                     @Param("algorithm") String algorithm, 
                                     @Param("updatedBy") String updatedBy);
}
