package com.management.accountant.mapper.mobile;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileAppConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动应用配置 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface MobileAppConfigMapper extends BaseMapper<MobileAppConfig> {

    // 基础查询方法

    /**
     * 根据应用编码获取应用配置
     */
    MobileAppConfig getByAppCode(@Param("appCode") String appCode, @Param("tenantId") String tenantId);

    /**
     * 根据应用类型获取应用列表
     */
    List<MobileAppConfig> getByAppType(@Param("appType") String appType, @Param("tenantId") String tenantId);

    /**
     * 根据应用平台获取应用列表
     */
    List<MobileAppConfig> getByAppPlatform(@Param("appPlatform") String appPlatform, @Param("tenantId") String tenantId);

    /**
     * 根据应用分类获取应用列表
     */
    List<MobileAppConfig> getByAppCategory(@Param("appCategory") String appCategory, @Param("tenantId") String tenantId);

    /**
     * 根据状态获取应用列表
     */
    List<MobileAppConfig> getByStatus(@Param("status") String status, @Param("tenantId") String tenantId);

    /**
     * 获取启用的应用列表
     */
    List<MobileAppConfig> getEnabledApps(@Param("tenantId") String tenantId);

    /**
     * 获取默认应用
     */
    MobileAppConfig getDefaultApp(@Param("tenantId") String tenantId);

    /**
     * 获取已发布的应用列表
     */
    List<MobileAppConfig> getPublishedApps(@Param("tenantId") String tenantId);

    /**
     * 获取需要强制更新的应用列表
     */
    List<MobileAppConfig> getForceUpdateApps(@Param("tenantId") String tenantId);

    /**
     * 获取支持离线的应用列表
     */
    List<MobileAppConfig> getOfflineSupportApps(@Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询应用配置
     */
    IPage<MobileAppConfig> getAppConfigPage(Page<MobileAppConfig> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件分页查询应用
     */
    IPage<MobileAppConfig> getAppConfigPageByCondition(Page<MobileAppConfig> page, @Param("condition") MobileAppConfig condition);

    /**
     * 高级搜索分页查询
     */
    IPage<MobileAppConfig> advancedSearchPage(Page<MobileAppConfig> page, @Param("params") Map<String, Object> params);

    // 统计查询方法

    /**
     * 统计应用总数
     */
    Long countApps(@Param("tenantId") String tenantId);

    /**
     * 按应用类型统计数量
     */
    List<Map<String, Object>> countByAppType(@Param("tenantId") String tenantId);

    /**
     * 按应用平台统计数量
     */
    List<Map<String, Object>> countByAppPlatform(@Param("tenantId") String tenantId);

    /**
     * 按应用分类统计数量
     */
    List<Map<String, Object>> countByAppCategory(@Param("tenantId") String tenantId);

    /**
     * 按状态统计数量
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") String tenantId);

    /**
     * 按创建时间统计数量
     */
    List<Map<String, Object>> countByCreateTime(@Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime, 
                                                @Param("tenantId") String tenantId);

    // 应用管理操作方法

    /**
     * 批量更新应用状态
     */
    int batchUpdateStatus(@Param("appConfigIds") List<String> appConfigIds, 
                         @Param("status") String status, 
                         @Param("updatedBy") String updatedBy, 
                         @Param("tenantId") String tenantId);

    /**
     * 批量启用应用
     */
    int batchEnableApps(@Param("appConfigIds") List<String> appConfigIds, 
                       @Param("updatedBy") String updatedBy, 
                       @Param("tenantId") String tenantId);

    /**
     * 批量禁用应用
     */
    int batchDisableApps(@Param("appConfigIds") List<String> appConfigIds, 
                        @Param("updatedBy") String updatedBy, 
                        @Param("tenantId") String tenantId);

    /**
     * 批量发布应用
     */
    int batchPublishApps(@Param("appConfigIds") List<String> appConfigIds, 
                        @Param("publishTime") LocalDateTime publishTime,
                        @Param("updatedBy") String updatedBy, 
                        @Param("tenantId") String tenantId);

    /**
     * 批量下架应用
     */
    int batchUnpublishApps(@Param("appConfigIds") List<String> appConfigIds, 
                          @Param("unpublishTime") LocalDateTime unpublishTime,
                          @Param("updatedBy") String updatedBy, 
                          @Param("tenantId") String tenantId);

    /**
     * 设置默认应用
     */
    int setDefaultApp(@Param("appConfigId") String appConfigId, 
                     @Param("updatedBy") String updatedBy, 
                     @Param("tenantId") String tenantId);

    /**
     * 取消默认应用
     */
    int unsetDefaultApp(@Param("tenantId") String tenantId);

    // 应用版本管理方法

    /**
     * 获取应用的所有版本
     */
    List<MobileAppConfig> getAppVersions(@Param("appCode") String appCode, @Param("tenantId") String tenantId);

    /**
     * 获取应用的最新版本
     */
    MobileAppConfig getLatestVersion(@Param("appCode") String appCode, @Param("tenantId") String tenantId);

    /**
     * 检查应用版本是否存在
     */
    boolean checkVersionExists(@Param("appCode") String appCode, 
                              @Param("appVersion") String appVersion, 
                              @Param("tenantId") String tenantId);

    /**
     * 更新应用下载次数
     */
    int updateDownloadCount(@Param("appConfigId") String appConfigId, @Param("increment") Long increment);

    /**
     * 更新应用安装次数
     */
    int updateInstallCount(@Param("appConfigId") String appConfigId, @Param("increment") Long increment);

    /**
     * 更新应用活跃用户数
     */
    int updateActiveUserCount(@Param("appConfigId") String appConfigId, @Param("activeUserCount") Long activeUserCount);

    /**
     * 更新应用使用时长
     */
    int updateUsageDuration(@Param("appConfigId") String appConfigId, @Param("increment") Long increment);

    /**
     * 更新应用性能指标
     */
    int updatePerformanceMetrics(@Param("appConfigId") String appConfigId, 
                                @Param("crashRate") Double crashRate,
                                @Param("startupTime") Long startupTime,
                                @Param("memoryUsage") Double memoryUsage,
                                @Param("cpuUsage") Double cpuUsage,
                                @Param("networkUsage") Long networkUsage,
                                @Param("storageUsage") Double storageUsage,
                                @Param("batteryUsage") Double batteryUsage);

    // 应用查找方法

    /**
     * 根据关键词搜索应用
     */
    List<MobileAppConfig> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);

    /**
     * 根据标签搜索应用
     */
    List<MobileAppConfig> searchByTags(@Param("tags") List<String> tags, @Param("tenantId") String tenantId);

    /**
     * 查找相似的应用
     */
    List<MobileAppConfig> findSimilarApps(@Param("appConfigId") String appConfigId, @Param("tenantId") String tenantId);

    /**
     * 查找热门应用
     */
    List<MobileAppConfig> findPopularApps(@Param("limit") Integer limit, @Param("tenantId") String tenantId);

    /**
     * 查找推荐应用
     */
    List<MobileAppConfig> findRecommendedApps(@Param("userId") String userId, 
                                             @Param("limit") Integer limit, 
                                             @Param("tenantId") String tenantId);

    // 应用分析方法

    /**
     * 获取应用使用统计
     */
    Map<String, Object> getAppUsageStats(@Param("appConfigId") String appConfigId, 
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 获取应用性能统计
     */
    Map<String, Object> getAppPerformanceStats(@Param("appConfigId") String appConfigId, 
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 获取应用用户统计
     */
    Map<String, Object> getAppUserStats(@Param("appConfigId") String appConfigId, 
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取应用评分统计
     */
    Map<String, Object> getAppRatingStats(@Param("appConfigId") String appConfigId);

    /**
     * 获取应用健康度评分
     */
    Map<String, Object> getAppHealthScore(@Param("appConfigId") String appConfigId);

    /**
     * 获取应用质量评估
     */
    Map<String, Object> getAppQualityAssessment(@Param("appConfigId") String appConfigId);

    // 应用监控方法

    /**
     * 获取应用监控数据
     */
    List<Map<String, Object>> getAppMonitoringData(@Param("appConfigId") String appConfigId, 
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("granularity") String granularity);

    /**
     * 获取应用异常记录
     */
    List<Map<String, Object>> getAppExceptionRecords(@Param("appConfigId") String appConfigId, 
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 获取应用崩溃记录
     */
    List<Map<String, Object>> getAppCrashRecords(@Param("appConfigId") String appConfigId, 
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    // 数据清理方法

    /**
     * 清理过期的应用数据
     */
    int cleanExpiredData(@Param("expiredDate") LocalDateTime expiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理已下架的应用
     */
    int cleanUnpublishedApps(@Param("unpublishedDate") LocalDateTime unpublishedDate, @Param("tenantId") String tenantId);

    /**
     * 清理无效的应用配置
     */
    int cleanInvalidConfigs(@Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 重建应用索引
     */
    int rebuildAppIndex(@Param("tenantId") String tenantId);

    /**
     * 优化应用配置
     */
    int optimizeAppConfigs(@Param("tenantId") String tenantId);

    /**
     * 同步应用状态
     */
    int syncAppStatus(@Param("tenantId") String tenantId);

    /**
     * 验证应用配置
     */
    List<Map<String, Object>> validateAppConfigs(@Param("tenantId") String tenantId);

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);
}
