package com.management.accountant.mapper.intg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.intg.IntgInterfaceMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 接口监控 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface IntgInterfaceMonitoringMapper extends BaseMapper<IntgInterfaceMonitoring> {

    // 基础查询方法

    /**
     * 根据监控编码获取监控信息
     */
    IntgInterfaceMonitoring getByMonitorCode(@Param("monitorCode") String monitorCode, @Param("tenantId") String tenantId);

    /**
     * 根据接口名称获取监控列表
     */
    List<IntgInterfaceMonitoring> getByInterfaceName(@Param("interfaceName") String interfaceName, @Param("tenantId") String tenantId);

    /**
     * 根据接口类型获取监控列表
     */
    List<IntgInterfaceMonitoring> getByInterfaceType(@Param("interfaceType") String interfaceType, @Param("tenantId") String tenantId);

    /**
     * 根据监控类型获取监控列表
     */
    List<IntgInterfaceMonitoring> getByMonitoringType(@Param("monitoringType") String monitoringType, @Param("tenantId") String tenantId);

    /**
     * 根据监控状态获取监控列表
     */
    List<IntgInterfaceMonitoring> getByMonitoringStatus(@Param("monitoringStatus") String monitoringStatus, @Param("tenantId") String tenantId);

    /**
     * 根据API ID获取监控列表
     */
    List<IntgInterfaceMonitoring> getByApiId(@Param("apiId") String apiId, @Param("tenantId") String tenantId);

    /**
     * 根据监控分组获取监控列表
     */
    List<IntgInterfaceMonitoring> getByMonitorGroup(@Param("monitorGroup") String monitorGroup, @Param("tenantId") String tenantId);

    /**
     * 根据监控环境获取监控列表
     */
    List<IntgInterfaceMonitoring> getByMonitorEnvironment(@Param("monitorEnvironment") String monitorEnvironment, @Param("tenantId") String tenantId);

    /**
     * 获取激活的监控列表
     */
    List<IntgInterfaceMonitoring> getActiveMonitors(@Param("tenantId") String tenantId);

    /**
     * 获取需要执行的监控任务
     */
    List<IntgInterfaceMonitoring> getPendingMonitorTasks(@Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询接口监控信息
     */
    IPage<IntgInterfaceMonitoring> getMonitorPage(Page<IntgInterfaceMonitoring> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件分页查询监控
     */
    IPage<IntgInterfaceMonitoring> getMonitorPageByCondition(Page<IntgInterfaceMonitoring> page, @Param("condition") IntgInterfaceMonitoring condition);

    /**
     * 高级搜索分页查询
     */
    IPage<IntgInterfaceMonitoring> advancedSearchPage(Page<IntgInterfaceMonitoring> page, @Param("params") Map<String, Object> params);

    // 统计查询方法

    /**
     * 统计监控总数
     */
    Long countMonitors(@Param("tenantId") String tenantId);

    /**
     * 按接口类型统计数量
     */
    List<Map<String, Object>> countByInterfaceType(@Param("tenantId") String tenantId);

    /**
     * 按监控类型统计数量
     */
    List<Map<String, Object>> countByMonitoringType(@Param("tenantId") String tenantId);

    /**
     * 按监控状态统计数量
     */
    List<Map<String, Object>> countByMonitoringStatus(@Param("tenantId") String tenantId);

    /**
     * 按监控环境统计数量
     */
    List<Map<String, Object>> countByMonitorEnvironment(@Param("tenantId") String tenantId);

    /**
     * 按监控优先级统计数量
     */
    List<Map<String, Object>> countByMonitorPriority(@Param("tenantId") String tenantId);

    /**
     * 按创建时间统计数量
     */
    List<Map<String, Object>> countByCreateTime(@Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime, 
                                                @Param("tenantId") String tenantId);

    // 监控管理操作方法

    /**
     * 批量更新监控状态
     */
    int batchUpdateMonitoringStatus(@Param("monitorIds") List<String> monitorIds, 
                                   @Param("monitoringStatus") String monitoringStatus, 
                                   @Param("updatedBy") String updatedBy, 
                                   @Param("tenantId") String tenantId);

    /**
     * 批量启用监控
     */
    int batchEnableMonitors(@Param("monitorIds") List<String> monitorIds, 
                           @Param("updatedBy") String updatedBy, 
                           @Param("tenantId") String tenantId);

    /**
     * 批量暂停监控
     */
    int batchPauseMonitors(@Param("monitorIds") List<String> monitorIds, 
                          @Param("updatedBy") String updatedBy, 
                          @Param("tenantId") String tenantId);

    /**
     * 批量停止监控
     */
    int batchStopMonitors(@Param("monitorIds") List<String> monitorIds, 
                         @Param("updatedBy") String updatedBy, 
                         @Param("tenantId") String tenantId);

    /**
     * 批量更新检查间隔
     */
    int batchUpdateCheckInterval(@Param("monitorIds") List<String> monitorIds, 
                                @Param("checkInterval") Integer checkInterval,
                                @Param("updatedBy") String updatedBy, 
                                @Param("tenantId") String tenantId);

    /**
     * 批量更新告警配置
     */
    int batchUpdateAlertConfig(@Param("monitorIds") List<String> monitorIds, 
                              @Param("alertEnabled") Boolean alertEnabled,
                              @Param("updatedBy") String updatedBy, 
                              @Param("tenantId") String tenantId);

    // 监控执行方法

    /**
     * 更新最后检查时间
     */
    int updateLastCheckTime(@Param("monitorId") String monitorId, 
                           @Param("lastCheckTime") LocalDateTime lastCheckTime,
                           @Param("lastResponseTime") Long lastResponseTime,
                           @Param("lastStatusCode") Integer lastStatusCode);

    /**
     * 增加错误计数
     */
    int incrementErrorCount(@Param("monitorId") String monitorId);

    /**
     * 重置错误计数
     */
    int resetErrorCount(@Param("monitorId") String monitorId);

    /**
     * 更新监控结果
     */
    int updateMonitorResult(@Param("monitorId") String monitorId, 
                           @Param("lastCheckTime") LocalDateTime lastCheckTime,
                           @Param("lastResponseTime") Long lastResponseTime,
                           @Param("lastStatusCode") Integer lastStatusCode,
                           @Param("isSuccess") Boolean isSuccess);

    // 监控查找方法

    /**
     * 根据接口URL查找监控
     */
    List<IntgInterfaceMonitoring> findByInterfaceUrl(@Param("interfaceUrl") String interfaceUrl, @Param("tenantId") String tenantId);

    /**
     * 根据关键词搜索监控
     */
    List<IntgInterfaceMonitoring> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);

    /**
     * 根据标签搜索监控
     */
    List<IntgInterfaceMonitoring> searchByTags(@Param("tags") List<String> tags, @Param("tenantId") String tenantId);

    /**
     * 查找异常的监控
     */
    List<IntgInterfaceMonitoring> findAbnormalMonitors(@Param("tenantId") String tenantId);

    /**
     * 查找超时的监控
     */
    List<IntgInterfaceMonitoring> findTimeoutMonitors(@Param("timeoutMinutes") Integer timeoutMinutes, @Param("tenantId") String tenantId);

    /**
     * 查找高错误率的监控
     */
    List<IntgInterfaceMonitoring> findHighErrorRateMonitors(@Param("errorThreshold") Integer errorThreshold, @Param("tenantId") String tenantId);

    // 监控分析方法

    /**
     * 获取监控统计信息
     */
    Map<String, Object> getMonitorStats(@Param("monitorId") String monitorId, 
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取监控性能趋势
     */
    List<Map<String, Object>> getPerformanceTrend(@Param("monitorId") String monitorId, 
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("granularity") String granularity);

    /**
     * 获取监控可用性统计
     */
    Map<String, Object> getAvailabilityStats(@Param("monitorId") String monitorId, 
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取监控错误分析
     */
    List<Map<String, Object>> getErrorAnalysis(@Param("monitorId") String monitorId, 
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 获取监控健康度评分
     */
    Map<String, Object> getHealthScore(@Param("monitorId") String monitorId);

    /**
     * 获取监控质量评估
     */
    Map<String, Object> getQualityAssessment(@Param("monitorId") String monitorId);

    // 告警相关方法

    /**
     * 获取需要告警的监控
     */
    List<IntgInterfaceMonitoring> getAlertsToSend(@Param("tenantId") String tenantId);

    /**
     * 更新告警发送时间
     */
    int updateAlertSentTime(@Param("monitorId") String monitorId, @Param("alertSentTime") LocalDateTime alertSentTime);

    /**
     * 获取告警历史
     */
    List<Map<String, Object>> getAlertHistory(@Param("monitorId") String monitorId, 
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    // 数据清理方法

    /**
     * 清理过期的监控数据
     */
    int cleanExpiredData(@Param("expiredDate") LocalDateTime expiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理停用的监控
     */
    int cleanStoppedMonitors(@Param("stoppedDate") LocalDateTime stoppedDate, @Param("tenantId") String tenantId);

    /**
     * 清理无效的监控配置
     */
    int cleanInvalidConfigs(@Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 重建监控索引
     */
    int rebuildMonitorIndex(@Param("tenantId") String tenantId);

    /**
     * 优化监控配置
     */
    int optimizeMonitorConfigs(@Param("tenantId") String tenantId);

    /**
     * 同步监控状态
     */
    int syncMonitorStatus(@Param("tenantId") String tenantId);

    /**
     * 验证监控配置
     */
    List<Map<String, Object>> validateMonitorConfigs(@Param("tenantId") String tenantId);

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);
}
