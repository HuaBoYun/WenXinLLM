package com.management.accountant.mapper.mobile;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileDeviceManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动设备管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface MobileDeviceManagementMapper extends BaseMapper<MobileDeviceManagement> {

    // 基础查询方法

    /**
     * 根据设备编码获取设备信息
     */
    MobileDeviceManagement getByDeviceCode(@Param("deviceCode") String deviceCode, @Param("tenantId") String tenantId);

    /**
     * 根据设备UUID获取设备信息
     */
    MobileDeviceManagement getByDeviceUuid(@Param("deviceUuid") String deviceUuid, @Param("tenantId") String tenantId);

    /**
     * 根据设备IMEI获取设备信息
     */
    MobileDeviceManagement getByDeviceImei(@Param("deviceImei") String deviceImei, @Param("tenantId") String tenantId);

    /**
     * 根据设备类型获取设备列表
     */
    List<MobileDeviceManagement> getByDeviceType(@Param("deviceType") String deviceType, @Param("tenantId") String tenantId);

    /**
     * 根据设备平台获取设备列表
     */
    List<MobileDeviceManagement> getByDevicePlatform(@Param("devicePlatform") String devicePlatform, @Param("tenantId") String tenantId);

    /**
     * 根据设备品牌获取设备列表
     */
    List<MobileDeviceManagement> getByDeviceBrand(@Param("deviceBrand") String deviceBrand, @Param("tenantId") String tenantId);

    /**
     * 根据设备状态获取设备列表
     */
    List<MobileDeviceManagement> getByDeviceStatus(@Param("deviceStatus") String deviceStatus, @Param("tenantId") String tenantId);

    /**
     * 根据健康状态获取设备列表
     */
    List<MobileDeviceManagement> getByHealthStatus(@Param("healthStatus") String healthStatus, @Param("tenantId") String tenantId);

    /**
     * 根据合规状态获取设备列表
     */
    List<MobileDeviceManagement> getByComplianceStatus(@Param("complianceStatus") String complianceStatus, @Param("tenantId") String tenantId);

    /**
     * 根据安全状态获取设备列表
     */
    List<MobileDeviceManagement> getBySecurityStatus(@Param("securityStatus") String securityStatus, @Param("tenantId") String tenantId);

    /**
     * 根据用户ID获取设备列表
     */
    List<MobileDeviceManagement> getByDeviceUserId(@Param("deviceUserId") String deviceUserId, @Param("tenantId") String tenantId);

    /**
     * 根据组织ID获取设备列表
     */
    List<MobileDeviceManagement> getByOrganizationId(@Param("organizationId") String organizationId, @Param("tenantId") String tenantId);

    /**
     * 根据部门ID获取设备列表
     */
    List<MobileDeviceManagement> getByDepartmentId(@Param("departmentId") String departmentId, @Param("tenantId") String tenantId);

    /**
     * 获取在线设备列表
     */
    List<MobileDeviceManagement> getOnlineDevices(@Param("tenantId") String tenantId);

    /**
     * 获取离线设备列表
     */
    List<MobileDeviceManagement> getOfflineDevices(@Param("tenantId") String tenantId);

    /**
     * 获取已注册设备列表
     */
    List<MobileDeviceManagement> getRegisteredDevices(@Param("tenantId") String tenantId);

    /**
     * 获取已激活设备列表
     */
    List<MobileDeviceManagement> getActivatedDevices(@Param("tenantId") String tenantId);

    /**
     * 获取已越狱/Root设备列表
     */
    List<MobileDeviceManagement> getJailbrokenDevices(@Param("tenantId") String tenantId);

    /**
     * 获取支持MDM的设备列表
     */
    List<MobileDeviceManagement> getMdmSupportedDevices(@Param("tenantId") String tenantId);

    /**
     * 获取启用MDM的设备列表
     */
    List<MobileDeviceManagement> getMdmEnabledDevices(@Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询设备管理
     */
    IPage<MobileDeviceManagement> getDeviceManagementPage(Page<MobileDeviceManagement> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件分页查询设备
     */
    IPage<MobileDeviceManagement> getDeviceManagementPageByCondition(Page<MobileDeviceManagement> page, @Param("condition") MobileDeviceManagement condition);

    /**
     * 高级搜索分页查询
     */
    IPage<MobileDeviceManagement> advancedSearchPage(Page<MobileDeviceManagement> page, @Param("params") Map<String, Object> params);

    // 统计查询方法

    /**
     * 统计设备总数
     */
    Long countDevices(@Param("tenantId") String tenantId);

    /**
     * 按设备类型统计数量
     */
    List<Map<String, Object>> countByDeviceType(@Param("tenantId") String tenantId);

    /**
     * 按设备平台统计数量
     */
    List<Map<String, Object>> countByDevicePlatform(@Param("tenantId") String tenantId);

    /**
     * 按设备品牌统计数量
     */
    List<Map<String, Object>> countByDeviceBrand(@Param("tenantId") String tenantId);

    /**
     * 按设备状态统计数量
     */
    List<Map<String, Object>> countByDeviceStatus(@Param("tenantId") String tenantId);

    /**
     * 按健康状态统计数量
     */
    List<Map<String, Object>> countByHealthStatus(@Param("tenantId") String tenantId);

    /**
     * 按合规状态统计数量
     */
    List<Map<String, Object>> countByComplianceStatus(@Param("tenantId") String tenantId);

    /**
     * 按安全状态统计数量
     */
    List<Map<String, Object>> countBySecurityStatus(@Param("tenantId") String tenantId);

    /**
     * 按组织统计数量
     */
    List<Map<String, Object>> countByOrganization(@Param("tenantId") String tenantId);

    /**
     * 按部门统计数量
     */
    List<Map<String, Object>> countByDepartment(@Param("tenantId") String tenantId);

    /**
     * 按注册时间统计数量
     */
    List<Map<String, Object>> countByRegistrationTime(@Param("startTime") LocalDateTime startTime, 
                                                      @Param("endTime") LocalDateTime endTime, 
                                                      @Param("tenantId") String tenantId);

    // 设备管理操作方法

    /**
     * 批量更新设备状态
     */
    int batchUpdateDeviceStatus(@Param("deviceIds") List<String> deviceIds, 
                               @Param("deviceStatus") String deviceStatus, 
                               @Param("updatedBy") String updatedBy, 
                               @Param("tenantId") String tenantId);

    /**
     * 批量注册设备
     */
    int batchRegisterDevices(@Param("deviceIds") List<String> deviceIds, 
                            @Param("registrationTime") LocalDateTime registrationTime,
                            @Param("updatedBy") String updatedBy, 
                            @Param("tenantId") String tenantId);

    /**
     * 批量激活设备
     */
    int batchActivateDevices(@Param("deviceIds") List<String> deviceIds, 
                            @Param("activationTime") LocalDateTime activationTime,
                            @Param("updatedBy") String updatedBy, 
                            @Param("tenantId") String tenantId);

    /**
     * 批量锁定设备
     */
    int batchLockDevices(@Param("deviceIds") List<String> deviceIds, 
                        @Param("updatedBy") String updatedBy, 
                        @Param("tenantId") String tenantId);

    /**
     * 批量解锁设备
     */
    int batchUnlockDevices(@Param("deviceIds") List<String> deviceIds, 
                          @Param("updatedBy") String updatedBy, 
                          @Param("tenantId") String tenantId);

    /**
     * 批量启用MDM
     */
    int batchEnableMdm(@Param("deviceIds") List<String> deviceIds, 
                      @Param("updatedBy") String updatedBy, 
                      @Param("tenantId") String tenantId);

    /**
     * 批量禁用MDM
     */
    int batchDisableMdm(@Param("deviceIds") List<String> deviceIds, 
                       @Param("updatedBy") String updatedBy, 
                       @Param("tenantId") String tenantId);

    /**
     * 批量退役设备
     */
    int batchRetireDevices(@Param("deviceIds") List<String> deviceIds, 
                          @Param("updatedBy") String updatedBy, 
                          @Param("tenantId") String tenantId);

    // 设备监控方法

    /**
     * 更新设备在线状态
     */
    int updateOnlineStatus(@Param("deviceId") String deviceId, 
                          @Param("deviceStatus") String deviceStatus,
                          @Param("lastOnlineTime") LocalDateTime lastOnlineTime);

    /**
     * 更新设备同步时间
     */
    int updateLastSyncTime(@Param("deviceId") String deviceId, @Param("lastSyncTime") LocalDateTime lastSyncTime);

    /**
     * 更新设备备份时间
     */
    int updateLastBackupTime(@Param("deviceId") String deviceId, @Param("lastBackupTime") LocalDateTime lastBackupTime);

    /**
     * 更新设备更新时间
     */
    int updateLastUpdateTime(@Param("deviceId") String deviceId, @Param("lastUpdateTime") LocalDateTime lastUpdateTime);

    /**
     * 更新设备使用统计
     */
    int updateUsageStats(@Param("deviceId") String deviceId, 
                        @Param("usageCount") Long usageCount,
                        @Param("usageDuration") Long usageDuration);

    /**
     * 更新设备错误统计
     */
    int updateErrorStats(@Param("deviceId") String deviceId, 
                        @Param("errorCount") Long errorCount,
                        @Param("crashCount") Long crashCount,
                        @Param("restartCount") Long restartCount);

    /**
     * 更新设备性能指标
     */
    int updatePerformanceMetrics(@Param("deviceId") String deviceId, 
                                @Param("avgResponseTime") Long avgResponseTime,
                                @Param("avgMemoryUsage") Double avgMemoryUsage,
                                @Param("avgCpuUsage") Double avgCpuUsage,
                                @Param("avgNetworkUsage") Long avgNetworkUsage,
                                @Param("avgBatteryUsage") Double avgBatteryUsage);

    // 设备查找方法

    /**
     * 根据关键词搜索设备
     */
    List<MobileDeviceManagement> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);

    /**
     * 查找相似的设备
     */
    List<MobileDeviceManagement> findSimilarDevices(@Param("deviceId") String deviceId, @Param("tenantId") String tenantId);

    /**
     * 查找异常设备
     */
    List<MobileDeviceManagement> findAbnormalDevices(@Param("tenantId") String tenantId);

    /**
     * 查找高风险设备
     */
    List<MobileDeviceManagement> findHighRiskDevices(@Param("tenantId") String tenantId);

    /**
     * 查找需要更新的设备
     */
    List<MobileDeviceManagement> findDevicesNeedingUpdate(@Param("tenantId") String tenantId);

    /**
     * 查找需要备份的设备
     */
    List<MobileDeviceManagement> findDevicesNeedingBackup(@Param("tenantId") String tenantId);

    /**
     * 查找长时间未使用的设备
     */
    List<MobileDeviceManagement> findInactiveDevices(@Param("inactiveDays") Integer inactiveDays, @Param("tenantId") String tenantId);

    // 设备分析方法

    /**
     * 获取设备使用统计
     */
    Map<String, Object> getDeviceUsageStats(@Param("deviceId") String deviceId, 
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备性能统计
     */
    Map<String, Object> getDevicePerformanceStats(@Param("deviceId") String deviceId, 
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备健康度评分
     */
    Map<String, Object> getDeviceHealthScore(@Param("deviceId") String deviceId);

    /**
     * 获取设备安全评估
     */
    Map<String, Object> getDeviceSecurityAssessment(@Param("deviceId") String deviceId);

    /**
     * 获取设备合规检查
     */
    Map<String, Object> getDeviceComplianceCheck(@Param("deviceId") String deviceId);

    /**
     * 生成设备报告
     */
    Map<String, Object> generateDeviceReport(@Param("deviceId") String deviceId, 
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    // 设备监控数据方法

    /**
     * 获取设备监控数据
     */
    List<Map<String, Object>> getDeviceMonitoringData(@Param("deviceId") String deviceId, 
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("granularity") String granularity);

    /**
     * 获取设备告警记录
     */
    List<Map<String, Object>> getDeviceAlertRecords(@Param("deviceId") String deviceId, 
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备事件记录
     */
    List<Map<String, Object>> getDeviceEventRecords(@Param("deviceId") String deviceId, 
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);

    // 数据清理方法

    /**
     * 清理过期的设备数据
     */
    int cleanExpiredData(@Param("expiredDate") LocalDateTime expiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理已退役的设备
     */
    int cleanRetiredDevices(@Param("retiredDate") LocalDateTime retiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理无效的设备记录
     */
    int cleanInvalidDevices(@Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 重建设备索引
     */
    int rebuildDeviceIndex(@Param("tenantId") String tenantId);

    /**
     * 优化设备数据
     */
    int optimizeDeviceData(@Param("tenantId") String tenantId);

    /**
     * 同步设备状态
     */
    int syncDeviceStatus(@Param("tenantId") String tenantId);

    /**
     * 验证设备数据
     */
    List<Map<String, Object>> validateDeviceData(@Param("tenantId") String tenantId);

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);
}
