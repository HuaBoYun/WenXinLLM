package com.management.accountant.service.intg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.intg.IntgSystemConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ERP系统集成配置服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface IntgSystemConfigService extends IService<IntgSystemConfig> {

    // 基础CRUD操作

    /**
     * 创建系统配置
     */
    IntgSystemConfig createSystemConfig(IntgSystemConfig systemConfig);

    /**
     * 更新系统配置
     */
    IntgSystemConfig updateSystemConfig(IntgSystemConfig systemConfig);

    /**
     * 删除系统配置
     */
    boolean deleteSystemConfig(String configId);

    /**
     * 根据ID获取系统配置
     */
    IntgSystemConfig getSystemConfigById(String configId);

    /**
     * 根据编码获取系统配置
     */
    IntgSystemConfig getSystemConfigByCode(String configCode);

    // 查询操作

    /**
     * 分页查询系统配置
     */
    IPage<IntgSystemConfig> getSystemConfigPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据系统类型查询配置列表
     */
    List<IntgSystemConfig> getSystemConfigsByType(String systemType);

    /**
     * 根据连接类型查询配置列表
     */
    List<IntgSystemConfig> getSystemConfigsByConnectionType(String connectionType);

    /**
     * 根据配置状态查询配置列表
     */
    List<IntgSystemConfig> getSystemConfigsByStatus(String configStatus);

    /**
     * 查询启用的配置列表
     */
    List<IntgSystemConfig> getEnabledSystemConfigs();

    /**
     * 查询默认配置
     */
    IntgSystemConfig getDefaultSystemConfig(String systemType);

    // 配置管理操作

    /**
     * 测试连接配置
     */
    Map<String, Object> testConnection(String configId);

    /**
     * 批量测试连接
     */
    List<Map<String, Object>> batchTestConnection(List<String> configIds);

    /**
     * 启用系统配置
     */
    boolean enableSystemConfig(String configId);

    /**
     * 禁用系统配置
     */
    boolean disableSystemConfig(String configId);

    /**
     * 批量更新配置状态
     */
    boolean batchUpdateConfigStatus(List<String> configIds, String configStatus);

    /**
     * 批量更新连接状态
     */
    boolean batchUpdateConnectionStatus(List<String> configIds, String connectionStatus);

    /**
     * 更新令牌信息
     */
    boolean updateTokenInfo(String configId, String accessToken, String refreshToken, LocalDateTime tokenExpiresAt);

    /**
     * 刷新访问令牌
     */
    Map<String, Object> refreshAccessToken(String configId);

    // 配置验证操作

    /**
     * 验证配置信息
     */
    Map<String, Object> validateSystemConfig(IntgSystemConfig systemConfig);

    /**
     * 检查配置编码是否存在
     */
    boolean existsByConfigCode(String configCode);

    /**
     * 检查配置编码是否存在（排除指定ID）
     */
    boolean existsByConfigCodeExcludeId(String configCode, String excludeId);

    /**
     * 检查连接配置是否重复
     */
    boolean existsByConnectionConfig(String connectionUrl, Integer connectionPort, String databaseName);

    // 配置查找操作

    /**
     * 查找相似配置
     */
    List<IntgSystemConfig> findSimilarConfigs(String systemType, String connectionType, String connectionUrl);

    /**
     * 查找过期配置
     */
    List<IntgSystemConfig> findExpiredConfigs();

    /**
     * 查找需要健康检查的配置
     */
    List<IntgSystemConfig> findConfigsForHealthCheck(Integer checkInterval);

    /**
     * 查找需要同步的配置
     */
    List<IntgSystemConfig> findConfigsForSync();

    // 健康检查操作

    /**
     * 执行健康检查
     */
    Map<String, Object> performHealthCheck(String configId);

    /**
     * 批量健康检查
     */
    List<Map<String, Object>> batchPerformHealthCheck(List<String> configIds);

    /**
     * 更新健康状态
     */
    boolean updateHealthStatus(String configId, String healthStatus, Map<String, Object> healthCheckResult);

    /**
     * 获取系统健康报告
     */
    Map<String, Object> getSystemHealthReport();

    // 统计分析操作

    /**
     * 统计配置总数
     */
    Long countSystemConfigs();

    /**
     * 按系统类型统计配置数量
     */
    List<Map<String, Object>> countBySystemType();

    /**
     * 按连接类型统计配置数量
     */
    List<Map<String, Object>> countByConnectionType();

    /**
     * 按配置状态统计配置数量
     */
    List<Map<String, Object>> countByConfigStatus();

    /**
     * 按连接状态统计配置数量
     */
    List<Map<String, Object>> countByConnectionStatus();

    /**
     * 按健康状态统计配置数量
     */
    List<Map<String, Object>> countByHealthStatus();

    /**
     * 获取配置使用统计
     */
    List<Map<String, Object>> getConfigUsageStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取配置性能统计
     */
    List<Map<String, Object>> getConfigPerformanceStats(List<String> configIds);

    /**
     * 获取配置趋势分析
     */
    List<Map<String, Object>> getConfigTrendAnalysis(LocalDateTime startTime, LocalDateTime endTime, String granularity);

    // 配置优化操作

    /**
     * 优化配置性能
     */
    Map<String, Object> optimizeConfigPerformance(String configId);

    /**
     * 批量优化配置
     */
    List<Map<String, Object>> batchOptimizeConfigs(List<String> configIds);

    /**
     * 分析配置瓶颈
     */
    Map<String, Object> analyzeConfigBottlenecks(String configId);

    /**
     * 生成优化建议
     */
    List<Map<String, Object>> generateOptimizationSuggestions(String configId);

    // 配置备份恢复操作

    /**
     * 备份配置
     */
    Map<String, Object> backupSystemConfig(String configId);

    /**
     * 批量备份配置
     */
    Map<String, Object> batchBackupSystemConfigs(List<String> configIds);

    /**
     * 恢复配置
     */
    boolean restoreSystemConfig(String configId, String backupId);

    /**
     * 导出配置
     */
    Map<String, Object> exportSystemConfigs(List<String> configIds, String exportFormat);

    /**
     * 导入配置
     */
    Map<String, Object> importSystemConfigs(String importData, String importFormat);

    // 配置模板操作

    /**
     * 创建配置模板
     */
    IntgSystemConfig createConfigTemplate(IntgSystemConfig templateConfig);

    /**
     * 应用配置模板
     */
    IntgSystemConfig applyConfigTemplate(String templateId, Map<String, Object> parameters);

    /**
     * 获取配置模板列表
     */
    List<IntgSystemConfig> getConfigTemplates();

    /**
     * 删除配置模板
     */
    boolean deleteConfigTemplate(String templateId);

    // 数据清理操作

    /**
     * 清理过期配置
     */
    int cleanupExpiredConfigs();

    /**
     * 清理无效配置
     */
    int cleanupInvalidConfigs();

    /**
     * 归档历史配置
     */
    int archiveHistoryConfigs(LocalDateTime archiveTime);

    /**
     * 压缩配置数据
     */
    int compressConfigData();

    // 系统维护操作

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview();

    /**
     * 生成配置报告
     */
    List<Map<String, Object>> generateConfigReport(String reportType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 检查系统健康状态
     */
    Map<String, Object> checkSystemHealth();

    /**
     * 评估配置质量
     */
    List<Map<String, Object>> assessConfigQuality();

    /**
     * 执行系统维护任务
     */
    Map<String, Object> executeMaintenanceTasks(String taskType, Map<String, Object> parameters);

    // 监控告警操作

    /**
     * 设置监控规则
     */
    boolean setMonitoringRules(String configId, Map<String, Object> monitoringRules);

    /**
     * 获取监控数据
     */
    Map<String, Object> getMonitoringData(String configId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取告警信息
     */
    List<Map<String, Object>> getAlertInfo(String configId, String alertLevel);

    /**
     * 处理告警
     */
    boolean handleAlert(String alertId, String action, Map<String, Object> parameters);

    // 安全操作

    /**
     * 加密敏感信息
     */
    String encryptSensitiveInfo(String plainText);

    /**
     * 解密敏感信息
     */
    String decryptSensitiveInfo(String encryptedText);

    /**
     * 验证访问权限
     */
    boolean validateAccessPermission(String configId, String userId, String operation);

    /**
     * 记录操作日志
     */
    boolean logOperation(String configId, String operation, String userId, Map<String, Object> operationDetails);
}
