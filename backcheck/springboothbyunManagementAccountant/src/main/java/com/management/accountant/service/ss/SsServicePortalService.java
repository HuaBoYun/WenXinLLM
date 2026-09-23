package com.management.accountant.service.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ss.SsServicePortal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 服务门户Service接口
 * 提供服务门户业务逻辑处理
 * 
 * @author AI Assistant
 * @since 2025-01-01
 */
public interface SsServicePortalService extends IService<SsServicePortal> {

    /**
     * 分页查询服务门户
     */
    IPage<SsServicePortal> getServicePortalPage(Map<String, Object> params);

    /**
     * 根据ID查询服务门户详情
     */
    SsServicePortal getServicePortalById(Long portalId, Long tenantId);

    /**
     * 根据门户编码查询服务门户
     */
    SsServicePortal getServicePortalByCode(String portalCode, Long tenantId);

    /**
     * 创建服务门户
     */
    boolean createServicePortal(SsServicePortal servicePortal, Long tenantId);

    /**
     * 更新服务门户
     */
    boolean updateServicePortal(SsServicePortal servicePortal, Long tenantId);

    /**
     * 删除服务门户
     */
    boolean deleteServicePortal(Long portalId, Long tenantId);

    /**
     * 批量删除服务门户
     */
    boolean batchDeleteServicePortal(List<Long> portalIds, Long tenantId);

    /**
     * 激活服务门户
     */
    boolean activateServicePortal(Long portalId, Long tenantId);

    /**
     * 停用服务门户
     */
    boolean deactivateServicePortal(Long portalId, String reason, Long tenantId);

    /**
     * 批量激活服务门户
     */
    boolean batchActivateServicePortal(List<Long> portalIds, Long tenantId);

    /**
     * 批量停用服务门户
     */
    boolean batchDeactivateServicePortal(List<Long> portalIds, String reason, Long tenantId);

    /**
     * 归档服务门户
     */
    boolean archiveServicePortal(Long portalId, String reason, Long tenantId);

    /**
     * 批量归档服务门户
     */
    boolean batchArchiveServicePortal(List<Long> portalIds, String reason, Long tenantId);

    /**
     * 启动维护模式
     */
    boolean startMaintenance(Long portalId, String reason, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 结束维护模式
     */
    boolean endMaintenance(Long portalId, String result, Long tenantId);

    /**
     * 配置门户
     */
    boolean configurePortal(Long portalId, String configType, Map<String, Object> configData, Long tenantId);

    /**
     * 重置门户配置
     */
    boolean resetPortalConfig(Long portalId, String configType, Long tenantId);

    /**
     * 复制门户配置
     */
    boolean copyPortalConfig(Long sourcePortalId, Long targetPortalId, Long tenantId);

    /**
     * 设置默认门户
     */
    boolean setDefaultPortal(Long portalId, Long tenantId);

    /**
     * 设置门户权限
     */
    boolean setPortalPermissions(Long portalId, Map<String, Object> permissions, Long tenantId);

    /**
     * 更新门户访问统计
     */
    boolean updateAccessStats(Long portalId, Long userId, String userName, String accessIp, Long tenantId);

    /**
     * 门户健康检查
     */
    Map<String, Object> performHealthCheck(Long portalId, Long tenantId);

    /**
     * 门户性能监控
     */
    Map<String, Object> monitorPortalPerformance(Long portalId, Long tenantId);

    /**
     * 门户备份
     */
    boolean backupPortal(Long portalId, String backupType, Long tenantId);

    /**
     * 门户恢复
     */
    boolean restorePortal(Long portalId, String backupId, Long tenantId);

    /**
     * 门户优化
     */
    boolean optimizePortal(Long portalId, Map<String, Object> optimizationConfig, Long tenantId);

    /**
     * 门户安全扫描
     */
    Map<String, Object> performSecurityScan(Long portalId, Long tenantId);

    /**
     * 门户集成测试
     */
    Map<String, Object> performIntegrationTest(Long portalId, Long tenantId);

    /**
     * 门户用户反馈收集
     */
    boolean collectUserFeedback(Long portalId, Map<String, Object> feedback, Long tenantId);

    /**
     * 门户使用分析
     */
    Map<String, Object> analyzePortalUsage(Long portalId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 门户推荐
     */
    List<SsServicePortal> recommendPortals(Long userId, Integer limit, Long tenantId);

    /**
     * 门户搜索
     */
    List<SsServicePortal> searchPortals(String keyword, Map<String, Object> filters, Long tenantId);

    /**
     * 根据门户类型查询
     */
    List<SsServicePortal> getPortalsByType(String portalType, Long tenantId);

    /**
     * 根据门户状态查询
     */
    List<SsServicePortal> getPortalsByStatus(String portalStatus, Long tenantId);

    /**
     * 根据所有者查询
     */
    List<SsServicePortal> getPortalsByOwner(Long ownerId, Long tenantId);

    /**
     * 根据管理员查询
     */
    List<SsServicePortal> getPortalsByAdmin(Long adminId, Long tenantId);

    /**
     * 根据部门查询
     */
    List<SsServicePortal> getPortalsByDepartment(Long departmentId, Long tenantId);

    /**
     * 根据公司查询
     */
    List<SsServicePortal> getPortalsByCompany(Long companyId, Long tenantId);

    /**
     * 查询活跃门户
     */
    List<SsServicePortal> getActivePortals(Long tenantId);

    /**
     * 查询非活跃门户
     */
    List<SsServicePortal> getInactivePortals(Long tenantId);

    /**
     * 查询维护中门户
     */
    List<SsServicePortal> getMaintenancePortals(Long tenantId);

    /**
     * 查询已归档门户
     */
    List<SsServicePortal> getArchivedPortals(Long tenantId);

    /**
     * 查询默认门户
     */
    List<SsServicePortal> getDefaultPortals(Long tenantId);

    /**
     * 查询公开门户
     */
    List<SsServicePortal> getPublicPortals(Long tenantId);

    /**
     * 查询可定制门户
     */
    List<SsServicePortal> getCustomizablePortals(Long tenantId);

    /**
     * 查询高访问量门户
     */
    List<SsServicePortal> getHighTrafficPortals(Integer limit, Long tenantId);

    /**
     * 查询最近访问门户
     */
    List<SsServicePortal> getRecentlyAccessedPortals(Integer limit, Long tenantId);

    /**
     * 查询需要维护的门户
     */
    List<SsServicePortal> getPortalsNeedMaintenance(Long tenantId);

    /**
     * 查询过期门户
     */
    List<SsServicePortal> getExpiredPortals(Long tenantId);

    /**
     * 查询低使用率门户
     */
    List<SsServicePortal> getLowUsagePortals(Long threshold, Long tenantId);

    /**
     * 检查门户编码是否存在
     */
    boolean checkPortalCodeExists(String portalCode, Long portalId, Long tenantId);

    /**
     * 检查门户名称是否存在
     */
    boolean checkPortalNameExists(String portalName, Long portalId, Long tenantId);

    /**
     * 查询统计信息
     */
    Map<String, Object> getStatistics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询门户类型分布
     */
    List<Map<String, Object>> getPortalTypeDistribution(Long tenantId);

    /**
     * 查询门户状态分布
     */
    List<Map<String, Object>> getPortalStatusDistribution(Long tenantId);

    /**
     * 查询门户分类分布
     */
    List<Map<String, Object>> getPortalCategoryDistribution(Long tenantId);

    /**
     * 查询门户访问量排行
     */
    List<Map<String, Object>> getPortalAccessRanking(Integer limit, Long tenantId);

    /**
     * 查询门户使用趋势
     */
    List<Map<String, Object>> getPortalUsageTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询门户性能指标
     */
    List<Map<String, Object>> getPortalPerformanceMetrics(Long tenantId);

    /**
     * 查询门户用户反馈
     */
    List<Map<String, Object>> getPortalUserFeedback(Long tenantId);

    /**
     * 查询门户安全事件
     */
    List<Map<String, Object>> getPortalSecurityEvents(Long tenantId);

    /**
     * 查询门户集成状态
     */
    List<Map<String, Object>> getPortalIntegrationStatus(Long tenantId);

    /**
     * 查询门户备份状态
     */
    List<Map<String, Object>> getPortalBackupStatus(Long tenantId);

    /**
     * 查询门户监控状态
     */
    List<Map<String, Object>> getPortalMonitoringStatus(Long tenantId);

    /**
     * 查询门户访问日志
     */
    List<Map<String, Object>> getPortalAccessLogs(Long portalId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询门户配置历史
     */
    List<Map<String, Object>> getPortalConfigHistory(Long portalId, Long tenantId);

    /**
     * 查询门户维护记录
     */
    List<Map<String, Object>> getPortalMaintenanceRecords(Long portalId, Long tenantId);

    /**
     * 查询门户告警信息
     */
    List<Map<String, Object>> getPortalAlerts(String alertType, Long tenantId);

    /**
     * 查询门户待处理事项
     */
    List<Map<String, Object>> getPortalPendingItems(Long tenantId);

    /**
     * 查询门户健康检查结果
     */
    List<Map<String, Object>> getPortalHealthCheckResults(Long tenantId);

    /**
     * 查询门户资源使用情况
     */
    List<Map<String, Object>> getPortalResourceUsage(Long tenantId);

    /**
     * 查询门户优化建议
     */
    List<Map<String, Object>> getPortalOptimizationSuggestions(Long tenantId);
}
