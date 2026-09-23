package com.management.accountant.service.mobile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.mobile.MobileAppConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动应用配置服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface MobileAppConfigService extends IService<MobileAppConfig> {

    // CRUD操作

    /**
     * 创建应用配置
     */
    boolean createAppConfig(MobileAppConfig appConfig);

    /**
     * 更新应用配置
     */
    boolean updateAppConfig(MobileAppConfig appConfig);

    /**
     * 删除应用配置
     */
    boolean deleteAppConfig(String appConfigId);

    /**
     * 根据ID获取应用配置
     */
    MobileAppConfig getAppConfigById(String appConfigId);

    /**
     * 根据应用编码获取应用配置
     */
    MobileAppConfig getAppConfigByCode(String appCode);

    // 查询操作

    /**
     * 根据应用类型获取应用列表
     */
    List<MobileAppConfig> getAppConfigsByType(String appType);

    /**
     * 根据应用平台获取应用列表
     */
    List<MobileAppConfig> getAppConfigsByPlatform(String appPlatform);

    /**
     * 根据应用分类获取应用列表
     */
    List<MobileAppConfig> getAppConfigsByCategory(String appCategory);

    /**
     * 根据状态获取应用列表
     */
    List<MobileAppConfig> getAppConfigsByStatus(String status);

    /**
     * 获取启用的应用列表
     */
    List<MobileAppConfig> getEnabledAppConfigs();

    /**
     * 获取默认应用
     */
    MobileAppConfig getDefaultAppConfig();

    /**
     * 获取已发布的应用列表
     */
    List<MobileAppConfig> getPublishedAppConfigs();

    /**
     * 获取需要强制更新的应用列表
     */
    List<MobileAppConfig> getForceUpdateAppConfigs();

    /**
     * 获取支持离线的应用列表
     */
    List<MobileAppConfig> getOfflineSupportAppConfigs();

    // 分页查询操作

    /**
     * 分页查询应用配置
     */
    IPage<MobileAppConfig> getAppConfigPage(Page<MobileAppConfig> page, Map<String, Object> params);

    /**
     * 根据条件分页查询应用
     */
    IPage<MobileAppConfig> getAppConfigPageByCondition(Page<MobileAppConfig> page, MobileAppConfig condition);

    /**
     * 高级搜索分页查询
     */
    IPage<MobileAppConfig> advancedSearchPage(Page<MobileAppConfig> page, Map<String, Object> params);

    // 应用管理操作

    /**
     * 启用应用
     */
    boolean enableAppConfig(String appConfigId);

    /**
     * 禁用应用
     */
    boolean disableAppConfig(String appConfigId);

    /**
     * 发布应用
     */
    boolean publishAppConfig(String appConfigId);

    /**
     * 下架应用
     */
    boolean unpublishAppConfig(String appConfigId);

    /**
     * 设置默认应用
     */
    boolean setDefaultAppConfig(String appConfigId);

    /**
     * 批量更新应用状态
     */
    boolean batchUpdateStatus(List<String> appConfigIds, String status);

    /**
     * 批量启用应用
     */
    boolean batchEnableAppConfigs(List<String> appConfigIds);

    /**
     * 批量禁用应用
     */
    boolean batchDisableAppConfigs(List<String> appConfigIds);

    /**
     * 批量发布应用
     */
    boolean batchPublishAppConfigs(List<String> appConfigIds);

    /**
     * 批量下架应用
     */
    boolean batchUnpublishAppConfigs(List<String> appConfigIds);

    // 应用版本管理

    /**
     * 获取应用的所有版本
     */
    List<MobileAppConfig> getAppConfigVersions(String appCode);

    /**
     * 获取应用的最新版本
     */
    MobileAppConfig getLatestAppConfigVersion(String appCode);

    /**
     * 创建应用版本
     */
    boolean createAppConfigVersion(String appConfigId, String newVersion);

    /**
     * 发布应用版本
     */
    boolean publishAppConfigVersion(String appConfigId);

    /**
     * 回滚应用版本
     */
    boolean rollbackAppConfigVersion(String appConfigId, String targetVersion);

    // 应用搜索操作

    /**
     * 根据关键词搜索应用
     */
    List<MobileAppConfig> searchAppConfigsByKeyword(String keyword);

    /**
     * 根据标签搜索应用
     */
    List<MobileAppConfig> searchAppConfigsByTags(List<String> tags);

    /**
     * 查找相似的应用
     */
    List<MobileAppConfig> findSimilarAppConfigs(String appConfigId);

    /**
     * 查找热门应用
     */
    List<MobileAppConfig> findPopularAppConfigs(Integer limit);

    /**
     * 查找推荐应用
     */
    List<MobileAppConfig> findRecommendedAppConfigs(String userId, Integer limit);

    // 应用验证操作

    /**
     * 验证应用配置
     */
    Map<String, Object> validateAppConfig(MobileAppConfig appConfig);

    /**
     * 验证应用配置JSON
     */
    Map<String, Object> validateAppConfigJson(String appConfigJson);

    /**
     * 测试应用连接
     */
    Map<String, Object> testAppConfigConnection(String appConfigId);

    /**
     * 测试应用功能
     */
    Map<String, Object> testAppConfigFunction(String appConfigId);

    // 应用分析操作

    /**
     * 获取应用使用统计
     */
    Map<String, Object> getAppConfigUsageStats(String appConfigId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取应用性能统计
     */
    Map<String, Object> getAppConfigPerformanceStats(String appConfigId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取应用用户统计
     */
    Map<String, Object> getAppConfigUserStats(String appConfigId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取应用评分统计
     */
    Map<String, Object> getAppConfigRatingStats(String appConfigId);

    /**
     * 获取应用健康度评分
     */
    Map<String, Object> getAppConfigHealthScore(String appConfigId);

    /**
     * 获取应用质量评估
     */
    Map<String, Object> getAppConfigQualityAssessment(String appConfigId);

    /**
     * 生成应用报告
     */
    Map<String, Object> generateAppConfigReport(String appConfigId, LocalDateTime startTime, LocalDateTime endTime);

    // 统计操作

    /**
     * 统计应用总数
     */
    Long countAppConfigs();

    /**
     * 按应用类型统计数量
     */
    List<Map<String, Object>> countAppConfigsByType();

    /**
     * 按应用平台统计数量
     */
    List<Map<String, Object>> countAppConfigsByPlatform();

    /**
     * 按应用分类统计数量
     */
    List<Map<String, Object>> countAppConfigsByCategory();

    /**
     * 按状态统计数量
     */
    List<Map<String, Object>> countAppConfigsByStatus();

    /**
     * 按创建时间统计数量
     */
    List<Map<String, Object>> countAppConfigsByCreateTime(LocalDateTime startTime, LocalDateTime endTime);

    // 导入导出操作

    /**
     * 导入应用配置
     */
    Map<String, Object> importAppConfigs(List<MobileAppConfig> appConfigs);

    /**
     * 导出应用配置
     */
    List<MobileAppConfig> exportAppConfigs(List<String> appConfigIds);

    /**
     * 导出应用配置模板
     */
    Map<String, Object> exportAppConfigTemplate();

    // 数据清理操作

    /**
     * 清理过期的应用数据
     */
    int cleanExpiredAppConfigData(LocalDateTime expiredDate);

    /**
     * 清理已下架的应用
     */
    int cleanUnpublishedAppConfigs(LocalDateTime unpublishedDate);

    /**
     * 清理无效的应用配置
     */
    int cleanInvalidAppConfigs();

    // 系统维护操作

    /**
     * 重建应用索引
     */
    boolean rebuildAppConfigIndex();

    /**
     * 优化应用配置
     */
    boolean optimizeAppConfigs();

    /**
     * 同步应用状态
     */
    boolean syncAppConfigStatus();

    /**
     * 验证应用配置
     */
    List<Map<String, Object>> validateAppConfigs();

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview();
}
