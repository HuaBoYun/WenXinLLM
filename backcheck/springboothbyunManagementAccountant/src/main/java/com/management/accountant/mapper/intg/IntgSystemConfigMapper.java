package com.management.accountant.mapper.intg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.intg.IntgSystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ERP系统集成配置 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface IntgSystemConfigMapper extends BaseMapper<IntgSystemConfig> {

    // 基础查询方法

    /**
     * 根据配置编码查询
     */
    IntgSystemConfig getByConfigCode(@Param("configCode") String configCode, @Param("tenantId") String tenantId);

    /**
     * 根据系统类型查询配置列表
     */
    List<IntgSystemConfig> getBySystemType(@Param("systemType") String systemType, @Param("tenantId") String tenantId);

    /**
     * 根据连接类型查询配置列表
     */
    List<IntgSystemConfig> getByConnectionType(@Param("connectionType") String connectionType, @Param("tenantId") String tenantId);

    /**
     * 根据配置状态查询配置列表
     */
    List<IntgSystemConfig> getByConfigStatus(@Param("configStatus") String configStatus, @Param("tenantId") String tenantId);

    /**
     * 查询启用的配置列表
     */
    List<IntgSystemConfig> getEnabledConfigs(@Param("tenantId") String tenantId);

    /**
     * 查询默认配置
     */
    IntgSystemConfig getDefaultConfig(@Param("systemType") String systemType, @Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询配置列表
     */
    IPage<IntgSystemConfig> getConfigPage(Page<IntgSystemConfig> page, @Param("params") Map<String, Object> params);

    /**
     * 分页查询系统配置
     */
    IPage<IntgSystemConfig> getSystemConfigPage(Page<IntgSystemConfig> page, 
                                               @Param("systemType") String systemType,
                                               @Param("configStatus") String configStatus,
                                               @Param("tenantId") String tenantId);

    /**
     * 分页查询连接配置
     */
    IPage<IntgSystemConfig> getConnectionConfigPage(Page<IntgSystemConfig> page,
                                                   @Param("connectionType") String connectionType,
                                                   @Param("connectionStatus") String connectionStatus,
                                                   @Param("tenantId") String tenantId);

    // 统计查询方法

    /**
     * 统计配置总数
     */
    Long countConfigs(@Param("tenantId") String tenantId);

    /**
     * 按系统类型统计配置数量
     */
    List<Map<String, Object>> countBySystemType(@Param("tenantId") String tenantId);

    /**
     * 按连接类型统计配置数量
     */
    List<Map<String, Object>> countByConnectionType(@Param("tenantId") String tenantId);

    /**
     * 按配置状态统计配置数量
     */
    List<Map<String, Object>> countByConfigStatus(@Param("tenantId") String tenantId);

    /**
     * 按连接状态统计配置数量
     */
    List<Map<String, Object>> countByConnectionStatus(@Param("tenantId") String tenantId);

    /**
     * 按同步状态统计配置数量
     */
    List<Map<String, Object>> countBySyncStatus(@Param("tenantId") String tenantId);

    /**
     * 按健康状态统计配置数量
     */
    List<Map<String, Object>> countByHealthStatus(@Param("tenantId") String tenantId);

    // 配置管理方法

    /**
     * 批量更新配置状态
     */
    int batchUpdateConfigStatus(@Param("configIds") List<String> configIds, 
                               @Param("configStatus") String configStatus,
                               @Param("updatedBy") String updatedBy,
                               @Param("tenantId") String tenantId);

    /**
     * 批量更新连接状态
     */
    int batchUpdateConnectionStatus(@Param("configIds") List<String> configIds,
                                   @Param("connectionStatus") String connectionStatus,
                                   @Param("lastConnectionTime") LocalDateTime lastConnectionTime,
                                   @Param("updatedBy") String updatedBy,
                                   @Param("tenantId") String tenantId);

    /**
     * 批量更新同步状态
     */
    int batchUpdateSyncStatus(@Param("configIds") List<String> configIds,
                             @Param("syncStatus") String syncStatus,
                             @Param("lastSyncTime") LocalDateTime lastSyncTime,
                             @Param("updatedBy") String updatedBy,
                             @Param("tenantId") String tenantId);

    /**
     * 批量更新健康状态
     */
    int batchUpdateHealthStatus(@Param("configIds") List<String> configIds,
                               @Param("healthStatus") String healthStatus,
                               @Param("healthCheckTime") LocalDateTime healthCheckTime,
                               @Param("updatedBy") String updatedBy,
                               @Param("tenantId") String tenantId);

    /**
     * 更新令牌信息
     */
    int updateTokenInfo(@Param("configId") String configId,
                       @Param("accessToken") String accessToken,
                       @Param("refreshToken") String refreshToken,
                       @Param("tokenExpiresAt") LocalDateTime tokenExpiresAt,
                       @Param("updatedBy") String updatedBy,
                       @Param("tenantId") String tenantId);

    // 配置验证方法

    /**
     * 检查配置编码是否存在
     */
    boolean existsByConfigCode(@Param("configCode") String configCode, @Param("tenantId") String tenantId);

    /**
     * 检查配置编码是否存在（排除指定ID）
     */
    boolean existsByConfigCodeExcludeId(@Param("configCode") String configCode, 
                                       @Param("excludeId") String excludeId,
                                       @Param("tenantId") String tenantId);

    /**
     * 检查连接配置是否重复
     */
    boolean existsByConnectionConfig(@Param("connectionUrl") String connectionUrl,
                                    @Param("connectionPort") Integer connectionPort,
                                    @Param("databaseName") String databaseName,
                                    @Param("tenantId") String tenantId);

    // 配置查找方法

    /**
     * 查找相似配置
     */
    List<IntgSystemConfig> findSimilarConfigs(@Param("systemType") String systemType,
                                             @Param("connectionType") String connectionType,
                                             @Param("connectionUrl") String connectionUrl,
                                             @Param("tenantId") String tenantId);

    /**
     * 查找过期配置
     */
    List<IntgSystemConfig> findExpiredConfigs(@Param("currentTime") LocalDateTime currentTime,
                                             @Param("tenantId") String tenantId);

    /**
     * 查找需要健康检查的配置
     */
    List<IntgSystemConfig> findConfigsForHealthCheck(@Param("checkInterval") Integer checkInterval,
                                                    @Param("tenantId") String tenantId);

    /**
     * 查找需要同步的配置
     */
    List<IntgSystemConfig> findConfigsForSync(@Param("tenantId") String tenantId);

    // 配置分析方法

    /**
     * 获取配置使用统计
     */
    List<Map<String, Object>> getConfigUsageStats(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("tenantId") String tenantId);

    /**
     * 获取配置性能统计
     */
    List<Map<String, Object>> getConfigPerformanceStats(@Param("configIds") List<String> configIds,
                                                        @Param("tenantId") String tenantId);

    /**
     * 获取配置健康报告
     */
    List<Map<String, Object>> getConfigHealthReport(@Param("tenantId") String tenantId);

    /**
     * 获取配置趋势分析
     */
    List<Map<String, Object>> getConfigTrendAnalysis(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime,
                                                     @Param("granularity") String granularity,
                                                     @Param("tenantId") String tenantId);

    // 数据清理方法

    /**
     * 清理过期配置
     */
    int cleanupExpiredConfigs(@Param("expiryTime") LocalDateTime expiryTime,
                             @Param("tenantId") String tenantId);

    /**
     * 清理无效配置
     */
    int cleanupInvalidConfigs(@Param("tenantId") String tenantId);

    /**
     * 归档历史配置
     */
    int archiveHistoryConfigs(@Param("archiveTime") LocalDateTime archiveTime,
                             @Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);

    /**
     * 生成配置报告
     */
    List<Map<String, Object>> generateConfigReport(@Param("reportType") String reportType,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("tenantId") String tenantId);

    /**
     * 检查系统健康状态
     */
    Map<String, Object> checkSystemHealth(@Param("tenantId") String tenantId);

    /**
     * 评估配置质量
     */
    List<Map<String, Object>> assessConfigQuality(@Param("tenantId") String tenantId);

    /**
     * 执行系统维护任务
     */
    Map<String, Object> executeMaintenanceTasks(@Param("taskType") String taskType,
                                               @Param("parameters") Map<String, Object> parameters,
                                               @Param("tenantId") String tenantId);
}
