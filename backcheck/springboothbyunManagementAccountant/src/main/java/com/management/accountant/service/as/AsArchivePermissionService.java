package com.management.accountant.service.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.as.AsArchivePermission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案权限管理服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface AsArchivePermissionService extends IService<AsArchivePermission> {

    // ==================== 基本CRUD操作 ====================

    /**
     * 创建权限
     */
    boolean createPermission(String tenantId, AsArchivePermission permission, String createdBy);

    /**
     * 更新权限
     */
    boolean updatePermission(String tenantId, AsArchivePermission permission, String updatedBy);

    /**
     * 删除权限
     */
    boolean deletePermission(String tenantId, Long permissionId, String deletedBy);

    /**
     * 根据ID获取权限
     */
    AsArchivePermission getPermissionById(String tenantId, Long permissionId);

    /**
     * 根据编码获取权限
     */
    AsArchivePermission getPermissionByCode(String tenantId, String permissionCode);

    /**
     * 分页查询权限
     */
    IPage<AsArchivePermission> getPermissionPage(String tenantId, Page<AsArchivePermission> page, Map<String, Object> params);

    // ==================== 权限验证操作 ====================

    /**
     * 检查用户权限
     */
    boolean checkUserPermission(String tenantId, String userId, String resourceType, String resourceId, String operation);

    /**
     * 检查角色权限
     */
    boolean checkRolePermission(String tenantId, String roleId, String resourceType, String resourceId, String operation);

    /**
     * 获取用户有效权限
     */
    List<AsArchivePermission> getUserEffectivePermissions(String tenantId, String userId, String resourceType);

    /**
     * 获取角色有效权限
     */
    List<AsArchivePermission> getRoleEffectivePermissions(String tenantId, String roleId, String resourceType);

    /**
     * 获取资源权限列表
     */
    List<AsArchivePermission> getResourcePermissions(String tenantId, String resourceType, String resourceId);

    // ==================== 权限管理操作 ====================

    /**
     * 授予权限
     */
    boolean grantPermission(String tenantId, String subjectType, String subjectId, String resourceType, String resourceId, String operations, String grantedBy);

    /**
     * 撤销权限
     */
    boolean revokePermission(String tenantId, Long permissionId, String revokedBy);

    /**
     * 批量授予权限
     */
    boolean batchGrantPermissions(String tenantId, String subjectType, String subjectId, List<Map<String, Object>> permissions, String grantedBy);

    /**
     * 批量撤销权限
     */
    boolean batchRevokePermissions(String tenantId, String subjectType, String subjectId, List<Long> permissionIds, String revokedBy);

    /**
     * 更新权限状态
     */
    boolean updatePermissionStatus(String tenantId, Long permissionId, String status, String updatedBy);

    /**
     * 批量更新权限状态
     */
    boolean batchUpdatePermissionStatus(String tenantId, List<Long> permissionIds, String status, String updatedBy);

    // ==================== 权限继承和委托 ====================

    /**
     * 继承父权限
     */
    boolean inheritParentPermissions(String tenantId, Long parentPermissionId, String childSubjectId, String createdBy);

    /**
     * 委托权限
     */
    boolean delegatePermission(String tenantId, Long permissionId, String delegatorId, String delegateId, LocalDateTime effectiveTime, LocalDateTime expiryTime);

    /**
     * 取消权限委托
     */
    boolean cancelPermissionDelegation(String tenantId, Long permissionId, String cancelledBy);

    /**
     * 获取委托权限列表
     */
    List<AsArchivePermission> getDelegatedPermissions(String tenantId, String delegateId);

    // ==================== 权限层次管理 ====================

    /**
     * 获取父权限
     */
    AsArchivePermission getParentPermission(String tenantId, Long permissionId);

    /**
     * 获取子权限列表
     */
    List<AsArchivePermission> getChildPermissions(String tenantId, Long parentPermissionId);

    /**
     * 获取权限树结构
     */
    List<AsArchivePermission> getPermissionTree(String tenantId, Long rootPermissionId);

    /**
     * 获取权限路径
     */
    List<AsArchivePermission> getPermissionPath(String tenantId, Long permissionId);

    // ==================== 权限审批操作 ====================

    /**
     * 提交权限审批
     */
    boolean submitPermissionApproval(String tenantId, Long permissionId, String submitterId);

    /**
     * 审批权限
     */
    boolean approvePermission(String tenantId, Long permissionId, String approverId, String approvalComment);

    /**
     * 拒绝权限
     */
    boolean rejectPermission(String tenantId, Long permissionId, String approverId, String rejectionReason);

    /**
     * 取消权限审批
     */
    boolean cancelPermissionApproval(String tenantId, Long permissionId, String cancelledBy);

    /**
     * 获取待审批权限列表
     */
    IPage<AsArchivePermission> getPendingApprovalPage(String tenantId, String approverId, Page<AsArchivePermission> page);

    // ==================== 权限监控和审计 ====================

    /**
     * 记录权限使用
     */
    boolean recordPermissionUsage(String tenantId, Long permissionId, String userId, String operation, String accessIp, String deviceInfo);

    /**
     * 获取权限使用统计
     */
    Map<String, Object> getPermissionUsageStatistics(String tenantId, Long permissionId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户权限使用历史
     */
    List<Map<String, Object>> getUserPermissionUsageHistory(String tenantId, String userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 检测异常权限使用
     */
    List<Map<String, Object>> detectAbnormalPermissionUsage(String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 生成权限审计报告
     */
    Map<String, Object> generatePermissionAuditReport(String tenantId, String reportType, LocalDateTime startTime, LocalDateTime endTime);

    // ==================== 权限风险管理 ====================

    /**
     * 评估权限风险
     */
    Map<String, Object> assessPermissionRisk(String tenantId, Long permissionId);

    /**
     * 批量评估权限风险
     */
    List<Map<String, Object>> batchAssessPermissionRisk(String tenantId, List<Long> permissionIds);

    /**
     * 获取高风险权限列表
     */
    List<AsArchivePermission> getHighRiskPermissions(String tenantId, String riskLevel);

    /**
     * 更新权限风险等级
     */
    boolean updatePermissionRiskLevel(String tenantId, Long permissionId, String riskLevel, Integer riskScore, String updatedBy);

    /**
     * 处理权限风险
     */
    boolean handlePermissionRisk(String tenantId, Long permissionId, String handlingAction, String handlingComment, String handledBy);

    // ==================== 权限合规管理 ====================

    /**
     * 检查权限合规性
     */
    Map<String, Object> checkPermissionCompliance(String tenantId, Long permissionId);

    /**
     * 批量检查权限合规性
     */
    List<Map<String, Object>> batchCheckPermissionCompliance(String tenantId, List<Long> permissionIds);

    /**
     * 获取不合规权限列表
     */
    List<AsArchivePermission> getNonCompliantPermissions(String tenantId);

    /**
     * 更新权限合规状态
     */
    boolean updatePermissionComplianceStatus(String tenantId, Long permissionId, String complianceStatus, String updatedBy);

    /**
     * 生成合规报告
     */
    Map<String, Object> generateComplianceReport(String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    // ==================== 统计分析操作 ====================

    /**
     * 统计权限总数
     */
    Long countPermissions(String tenantId);

    /**
     * 按权限类型统计
     */
    List<Map<String, Object>> countByPermissionType(String tenantId);

    /**
     * 按权限级别统计
     */
    List<Map<String, Object>> countByPermissionLevel(String tenantId);

    /**
     * 按权限状态统计
     */
    List<Map<String, Object>> countByPermissionStatus(String tenantId);

    /**
     * 按主体类型统计
     */
    List<Map<String, Object>> countBySubjectType(String tenantId);

    /**
     * 按资源类型统计
     */
    List<Map<String, Object>> countByResourceType(String tenantId);

    /**
     * 按访问级别统计
     */
    List<Map<String, Object>> countByAccessLevel(String tenantId);

    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> countByRiskLevel(String tenantId);

    /**
     * 获取权限趋势分析
     */
    List<Map<String, Object>> getPermissionTrend(String tenantId, LocalDateTime startTime, LocalDateTime endTime, String granularity);

    /**
     * 获取用户活动统计
     */
    List<Map<String, Object>> getUserActivityStatistics(String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取热门权限排行
     */
    List<Map<String, Object>> getPopularPermissions(String tenantId, Integer limit);

    /**
     * 获取活跃用户排行
     */
    List<Map<String, Object>> getActiveUsers(String tenantId, Integer limit);

    // ==================== 数据管理操作 ====================

    /**
     * 导出权限数据
     */
    String exportPermissionData(String tenantId, Map<String, Object> params, String exportFormat);

    /**
     * 导入权限数据
     */
    Map<String, Object> importPermissionData(String tenantId, String filePath, String importMode, String importedBy);

    /**
     * 清理过期权限
     */
    int cleanupExpiredPermissions(String tenantId);

    /**
     * 清理无效权限
     */
    int cleanupInvalidPermissions(String tenantId);

    /**
     * 优化权限存储
     */
    boolean optimizePermissionStorage(String tenantId);

    /**
     * 备份权限数据
     */
    boolean backupPermissionData(String tenantId, String backupPath);

    /**
     * 恢复权限数据
     */
    boolean restorePermissionData(String tenantId, String backupPath);

    // ==================== 系统维护操作 ====================

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(String tenantId);

    /**
     * 生成权限报告
     */
    Map<String, Object> generatePermissionReport(String tenantId, String reportType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 检查系统健康
     */
    Map<String, Object> checkSystemHealth(String tenantId);

    /**
     * 权限质量评估
     */
    Map<String, Object> assessPermissionQuality(String tenantId);

    /**
     * 执行维护任务
     */
    boolean executeMaintenanceTask(String tenantId, String taskType, Map<String, Object> taskParams);

    /**
     * 获取维护任务状态
     */
    Map<String, Object> getMaintenanceTaskStatus(String tenantId, String taskId);

    // ==================== 通知操作 ====================

    /**
     * 发送权限通知
     */
    boolean sendPermissionNotification(String tenantId, String notificationType, String recipientId, Map<String, Object> notificationData);

    /**
     * 订阅权限更新通知
     */
    boolean subscribePermissionUpdates(String tenantId, String userId, String subscriptionType);

    /**
     * 取消订阅权限更新通知
     */
    boolean unsubscribePermissionUpdates(String tenantId, String userId, String subscriptionType);

    /**
     * 获取权限通知历史
     */
    List<Map<String, Object>> getPermissionNotificationHistory(String tenantId, String userId, LocalDateTime startTime, LocalDateTime endTime);
}
