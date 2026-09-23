package com.management.accountant.mapper.ss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsServicePortal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 服务门户Mapper接口
 * 提供服务门户数据访问功能
 * 
 * @author AI Assistant
 * @since 2025-01-01
 */
@Mapper
public interface SsServicePortalMapper extends BaseMapper<SsServicePortal> {

    /**
     * 分页查询服务门户
     */
    IPage<SsServicePortal> selectServicePortalPage(Page<SsServicePortal> page, @Param("params") Map<String, Object> params);

    /**
     * 根据门户编码查询服务门户
     */
    SsServicePortal selectByPortalCode(@Param("portalCode") String portalCode, @Param("tenantId") Long tenantId);

    /**
     * 根据门户类型查询服务门户列表
     */
    List<SsServicePortal> selectByPortalType(@Param("portalType") String portalType, @Param("tenantId") Long tenantId);

    /**
     * 根据门户状态查询服务门户列表
     */
    List<SsServicePortal> selectByPortalStatus(@Param("portalStatus") String portalStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据所有者查询服务门户列表
     */
    List<SsServicePortal> selectByOwner(@Param("ownerId") Long ownerId, @Param("tenantId") Long tenantId);

    /**
     * 根据管理员查询服务门户列表
     */
    List<SsServicePortal> selectByAdmin(@Param("adminId") Long adminId, @Param("tenantId") Long tenantId);

    /**
     * 根据部门查询服务门户列表
     */
    List<SsServicePortal> selectByDepartment(@Param("departmentId") Long departmentId, @Param("tenantId") Long tenantId);

    /**
     * 根据公司查询服务门户列表
     */
    List<SsServicePortal> selectByCompany(@Param("companyId") Long companyId, @Param("tenantId") Long tenantId);

    /**
     * 查询活跃的服务门户
     */
    List<SsServicePortal> selectActivePortals(@Param("tenantId") Long tenantId);

    /**
     * 查询非活跃的服务门户
     */
    List<SsServicePortal> selectInactivePortals(@Param("tenantId") Long tenantId);

    /**
     * 查询维护中的服务门户
     */
    List<SsServicePortal> selectMaintenancePortals(@Param("tenantId") Long tenantId);

    /**
     * 查询已归档的服务门户
     */
    List<SsServicePortal> selectArchivedPortals(@Param("tenantId") Long tenantId);

    /**
     * 查询默认门户
     */
    List<SsServicePortal> selectDefaultPortals(@Param("tenantId") Long tenantId);

    /**
     * 查询公开门户
     */
    List<SsServicePortal> selectPublicPortals(@Param("tenantId") Long tenantId);

    /**
     * 查询可定制门户
     */
    List<SsServicePortal> selectCustomizablePortals(@Param("tenantId") Long tenantId);

    /**
     * 查询高访问量门户
     */
    List<SsServicePortal> selectHighTrafficPortals(@Param("limit") Integer limit, @Param("tenantId") Long tenantId);

    /**
     * 查询最近访问的门户
     */
    List<SsServicePortal> selectRecentlyAccessedPortals(@Param("limit") Integer limit, @Param("tenantId") Long tenantId);

    /**
     * 查询需要维护的门户
     */
    List<SsServicePortal> selectPortalsNeedMaintenance(@Param("tenantId") Long tenantId);

    /**
     * 查询过期的门户
     */
    List<SsServicePortal> selectExpiredPortals(@Param("tenantId") Long tenantId);

    /**
     * 查询低使用率门户
     */
    List<SsServicePortal> selectLowUsagePortals(@Param("threshold") Long threshold, @Param("tenantId") Long tenantId);

    /**
     * 批量更新门户状态
     */
    int batchUpdatePortalStatus(@Param("portalIds") List<Long> portalIds, @Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 批量激活门户
     */
    int batchActivatePortals(@Param("portalIds") List<Long> portalIds, @Param("tenantId") Long tenantId);

    /**
     * 批量停用门户
     */
    int batchDeactivatePortals(@Param("portalIds") List<Long> portalIds, @Param("reason") String reason, @Param("tenantId") Long tenantId);

    /**
     * 批量归档门户
     */
    int batchArchivePortals(@Param("portalIds") List<Long> portalIds, @Param("tenantId") Long tenantId);

    /**
     * 更新门户访问统计
     */
    int updatePortalAccessStats(@Param("portalId") Long portalId, @Param("userId") Long userId, @Param("userName") String userName, @Param("accessIp") String accessIp, @Param("tenantId") Long tenantId);

    /**
     * 更新门户配置
     */
    int updatePortalConfig(@Param("portalId") Long portalId, @Param("configType") String configType, @Param("configData") String configData, @Param("tenantId") Long tenantId);

    /**
     * 重置门户配置
     */
    int resetPortalConfig(@Param("portalId") Long portalId, @Param("configType") String configType, @Param("tenantId") Long tenantId);

    /**
     * 复制门户配置
     */
    int copyPortalConfig(@Param("sourcePortalId") Long sourcePortalId, @Param("targetPortalId") Long targetPortalId, @Param("tenantId") Long tenantId);

    /**
     * 检查门户编码是否存在
     */
    int checkPortalCodeExists(@Param("portalCode") String portalCode, @Param("portalId") Long portalId, @Param("tenantId") Long tenantId);

    /**
     * 检查门户名称是否存在
     */
    int checkPortalNameExists(@Param("portalName") String portalName, @Param("portalId") Long portalId, @Param("tenantId") Long tenantId);

    /**
     * 统计门户总数
     */
    Long countTotalPortals(@Param("tenantId") Long tenantId);

    /**
     * 统计活跃门户数
     */
    Long countActivePortals(@Param("tenantId") Long tenantId);

    /**
     * 统计非活跃门户数
     */
    Long countInactivePortals(@Param("tenantId") Long tenantId);

    /**
     * 统计维护中门户数
     */
    Long countMaintenancePortals(@Param("tenantId") Long tenantId);

    /**
     * 统计已归档门户数
     */
    Long countArchivedPortals(@Param("tenantId") Long tenantId);

    /**
     * 统计门户类型分布
     */
    List<Map<String, Object>> countPortalTypeDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计门户状态分布
     */
    List<Map<String, Object>> countPortalStatusDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计门户分类分布
     */
    List<Map<String, Object>> countPortalCategoryDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计门户访问量排行
     */
    List<Map<String, Object>> getPortalAccessRanking(@Param("limit") Integer limit, @Param("tenantId") Long tenantId);

    /**
     * 统计门户使用趋势
     */
    List<Map<String, Object>> getPortalUsageTrend(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计门户性能指标
     */
    List<Map<String, Object>> getPortalPerformanceMetrics(@Param("tenantId") Long tenantId);

    /**
     * 统计门户用户反馈
     */
    List<Map<String, Object>> getPortalUserFeedback(@Param("tenantId") Long tenantId);

    /**
     * 统计门户安全事件
     */
    List<Map<String, Object>> getPortalSecurityEvents(@Param("tenantId") Long tenantId);

    /**
     * 统计门户集成状态
     */
    List<Map<String, Object>> getPortalIntegrationStatus(@Param("tenantId") Long tenantId);

    /**
     * 统计门户备份状态
     */
    List<Map<String, Object>> getPortalBackupStatus(@Param("tenantId") Long tenantId);

    /**
     * 统计门户监控状态
     */
    List<Map<String, Object>> getPortalMonitoringStatus(@Param("tenantId") Long tenantId);

    /**
     * 查询门户访问日志
     */
    List<Map<String, Object>> getPortalAccessLogs(@Param("portalId") Long portalId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询门户配置历史
     */
    List<Map<String, Object>> getPortalConfigHistory(@Param("portalId") Long portalId, @Param("tenantId") Long tenantId);

    /**
     * 查询门户维护记录
     */
    List<Map<String, Object>> getPortalMaintenanceRecords(@Param("portalId") Long portalId, @Param("tenantId") Long tenantId);

    /**
     * 查询门户告警信息
     */
    List<Map<String, Object>> getPortalAlerts(@Param("alertType") String alertType, @Param("tenantId") Long tenantId);

    /**
     * 查询门户待处理事项
     */
    List<Map<String, Object>> getPortalPendingItems(@Param("tenantId") Long tenantId);

    /**
     * 查询门户健康检查结果
     */
    List<Map<String, Object>> getPortalHealthCheckResults(@Param("tenantId") Long tenantId);

    /**
     * 查询门户资源使用情况
     */
    List<Map<String, Object>> getPortalResourceUsage(@Param("tenantId") Long tenantId);

    /**
     * 查询门户优化建议
     */
    List<Map<String, Object>> getPortalOptimizationSuggestions(@Param("tenantId") Long tenantId);
}
