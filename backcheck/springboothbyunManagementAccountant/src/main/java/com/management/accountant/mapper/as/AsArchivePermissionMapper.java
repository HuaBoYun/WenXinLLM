package com.management.accountant.mapper.as;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsArchivePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案权限管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface AsArchivePermissionMapper extends BaseMapper<AsArchivePermission> {

    // ==================== 基本查询方法 ====================

    /**
     * 根据权限编码查询权限
     */
    AsArchivePermission getByPermissionCode(@Param("tenantId") String tenantId, @Param("permissionCode") String permissionCode);

    /**
     * 根据权限名称查询权限
     */
    List<AsArchivePermission> getByPermissionName(@Param("tenantId") String tenantId, @Param("permissionName") String permissionName);

    /**
     * 根据权限类型查询权限
     */
    List<AsArchivePermission> getByPermissionType(@Param("tenantId") String tenantId, @Param("permissionType") String permissionType);

    /**
     * 根据权限级别查询权限
     */
    List<AsArchivePermission> getByPermissionLevel(@Param("tenantId") String tenantId, @Param("permissionLevel") String permissionLevel);

    /**
     * 根据资源类型查询权限
     */
    List<AsArchivePermission> getByResourceType(@Param("tenantId") String tenantId, @Param("resourceType") String resourceType);

    /**
     * 根据资源ID查询权限
     */
    List<AsArchivePermission> getByResourceId(@Param("tenantId") String tenantId, @Param("resourceId") String resourceId);

    /**
     * 根据主体类型查询权限
     */
    List<AsArchivePermission> getBySubjectType(@Param("tenantId") String tenantId, @Param("subjectType") String subjectType);

    /**
     * 根据主体ID查询权限
     */
    List<AsArchivePermission> getBySubjectId(@Param("tenantId") String tenantId, @Param("subjectId") String subjectId);

    /**
     * 根据权限状态查询权限
     */
    List<AsArchivePermission> getByPermissionStatus(@Param("tenantId") String tenantId, @Param("permissionStatus") String permissionStatus);

    /**
     * 根据审批状态查询权限
     */
    List<AsArchivePermission> getByApprovalStatus(@Param("tenantId") String tenantId, @Param("approvalStatus") String approvalStatus);

    // ==================== 分页查询方法 ====================

    /**
     * 分页查询权限列表
     */
    IPage<AsArchivePermission> getPermissionPage(Page<AsArchivePermission> page, @Param("tenantId") String tenantId, @Param("params") Map<String, Object> params);

    /**
     * 分页查询用户权限
     */
    IPage<AsArchivePermission> getUserPermissionPage(Page<AsArchivePermission> page, @Param("tenantId") String tenantId, @Param("userId") String userId, @Param("params") Map<String, Object> params);

    /**
     * 分页查询角色权限
     */
    IPage<AsArchivePermission> getRolePermissionPage(Page<AsArchivePermission> page, @Param("tenantId") String tenantId, @Param("roleId") String roleId, @Param("params") Map<String, Object> params);

    /**
     * 分页查询资源权限
     */
    IPage<AsArchivePermission> getResourcePermissionPage(Page<AsArchivePermission> page, @Param("tenantId") String tenantId, @Param("resourceId") String resourceId, @Param("params") Map<String, Object> params);

    /**
     * 分页查询待审批权限
     */
    IPage<AsArchivePermission> getPendingApprovalPage(Page<AsArchivePermission> page, @Param("tenantId") String tenantId, @Param("approverId") String approverId);

    /**
     * 分页查询过期权限
     */
    IPage<AsArchivePermission> getExpiredPermissionPage(Page<AsArchivePermission> page, @Param("tenantId") String tenantId, @Param("currentTime") LocalDateTime currentTime);

    // ==================== 统计查询方法 ====================

    /**
     * 统计权限总数
     */
    Long countPermissions(@Param("tenantId") String tenantId);

    /**
     * 按权限类型统计
     */
    List<Map<String, Object>> countByPermissionType(@Param("tenantId") String tenantId);

    /**
     * 按权限级别统计
     */
    List<Map<String, Object>> countByPermissionLevel(@Param("tenantId") String tenantId);

    /**
     * 按权限状态统计
     */
    List<Map<String, Object>> countByPermissionStatus(@Param("tenantId") String tenantId);

    /**
     * 按主体类型统计
     */
    List<Map<String, Object>> countBySubjectType(@Param("tenantId") String tenantId);

    /**
     * 按资源类型统计
     */
    List<Map<String, Object>> countByResourceType(@Param("tenantId") String tenantId);

    /**
     * 按访问级别统计
     */
    List<Map<String, Object>> countByAccessLevel(@Param("tenantId") String tenantId);

    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> countByRiskLevel(@Param("tenantId") String tenantId);

    /**
     * 统计用户权限数量
     */
    Long countUserPermissions(@Param("tenantId") String tenantId, @Param("userId") String userId);

    /**
     * 统计角色权限数量
     */
    Long countRolePermissions(@Param("tenantId") String tenantId, @Param("roleId") String roleId);

    // ==================== 权限验证方法 ====================

    /**
     * 检查用户权限
     */
    Boolean checkUserPermission(@Param("tenantId") String tenantId, @Param("userId") String userId, @Param("resourceType") String resourceType, @Param("resourceId") String resourceId, @Param("operation") String operation);

    /**
     * 检查角色权限
     */
    Boolean checkRolePermission(@Param("tenantId") String tenantId, @Param("roleId") String roleId, @Param("resourceType") String resourceType, @Param("resourceId") String resourceId, @Param("operation") String operation);

    /**
     * 获取用户有效权限
     */
    List<AsArchivePermission> getUserEffectivePermissions(@Param("tenantId") String tenantId, @Param("userId") String userId, @Param("resourceType") String resourceType);

    /**
     * 获取角色有效权限
     */
    List<AsArchivePermission> getRoleEffectivePermissions(@Param("tenantId") String tenantId, @Param("roleId") String roleId, @Param("resourceType") String resourceType);

    /**
     * 获取资源权限列表
     */
    List<AsArchivePermission> getResourcePermissions(@Param("tenantId") String tenantId, @Param("resourceType") String resourceType, @Param("resourceId") String resourceId);

    // ==================== 权限管理方法 ====================

    /**
     * 授予权限
     */
    int grantPermission(@Param("tenantId") String tenantId, @Param("permission") AsArchivePermission permission);

    /**
     * 撤销权限
     */
    int revokePermission(@Param("tenantId") String tenantId, @Param("permissionId") Long permissionId, @Param("revokedBy") String revokedBy);

    /**
     * 更新权限状态
     */
    int updatePermissionStatus(@Param("tenantId") String tenantId, @Param("permissionId") Long permissionId, @Param("status") String status, @Param("updatedBy") String updatedBy);

    /**
     * 批量更新权限状态
     */
    int batchUpdatePermissionStatus(@Param("tenantId") String tenantId, @Param("permissionIds") List<Long> permissionIds, @Param("status") String status, @Param("updatedBy") String updatedBy);

    /**
     * 继承父权限
     */
    int inheritParentPermissions(@Param("tenantId") String tenantId, @Param("parentPermissionId") Long parentPermissionId, @Param("childSubjectId") String childSubjectId, @Param("createdBy") String createdBy);

    /**
     * 委托权限
     */
    int delegatePermission(@Param("tenantId") String tenantId, @Param("permissionId") Long permissionId, @Param("delegatorId") String delegatorId, @Param("delegateId") String delegateId, @Param("effectiveTime") LocalDateTime effectiveTime, @Param("expiryTime") LocalDateTime expiryTime);

    // ==================== 权限层次方法 ====================

    /**
     * 获取父权限
     */
    AsArchivePermission getParentPermission(@Param("tenantId") String tenantId, @Param("permissionId") Long permissionId);

    /**
     * 获取子权限列表
     */
    List<AsArchivePermission> getChildPermissions(@Param("tenantId") String tenantId, @Param("parentPermissionId") Long parentPermissionId);

    /**
     * 获取权限树结构
     */
    List<AsArchivePermission> getPermissionTree(@Param("tenantId") String tenantId, @Param("rootPermissionId") Long rootPermissionId);

    /**
     * 获取权限路径
     */
    List<AsArchivePermission> getPermissionPath(@Param("tenantId") String tenantId, @Param("permissionId") Long permissionId);

    // ==================== 批量操作方法 ====================

    /**
     * 批量创建权限
     */
    int batchInsertPermissions(@Param("tenantId") String tenantId, @Param("permissions") List<AsArchivePermission> permissions);

    /**
     * 批量删除权限
     */
    int batchDeletePermissions(@Param("tenantId") String tenantId, @Param("permissionIds") List<Long> permissionIds, @Param("deletedBy") String deletedBy);

    /**
     * 批量授予权限
     */
    int batchGrantPermissions(@Param("tenantId") String tenantId, @Param("subjectId") String subjectId, @Param("permissionIds") List<Long> permissionIds, @Param("grantedBy") String grantedBy);

    /**
     * 批量撤销权限
     */
    int batchRevokePermissions(@Param("tenantId") String tenantId, @Param("subjectId") String subjectId, @Param("permissionIds") List<Long> permissionIds, @Param("revokedBy") String revokedBy);

    // ==================== 数据管理方法 ====================

    /**
     * 清理过期权限
     */
    int cleanupExpiredPermissions(@Param("tenantId") String tenantId, @Param("currentTime") LocalDateTime currentTime);

    /**
     * 清理无效权限
     */
    int cleanupInvalidPermissions(@Param("tenantId") String tenantId);

    /**
     * 优化权限存储
     */
    int optimizePermissionStorage(@Param("tenantId") String tenantId);

    /**
     * 备份权限数据
     */
    int backupPermissionData(@Param("tenantId") String tenantId, @Param("backupPath") String backupPath);

    /**
     * 恢复权限数据
     */
    int restorePermissionData(@Param("tenantId") String tenantId, @Param("backupPath") String backupPath);

    // ==================== 系统维护方法 ====================

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);

    /**
     * 生成权限报告
     */
    Map<String, Object> generatePermissionReport(@Param("tenantId") String tenantId, @Param("reportType") String reportType, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 检查系统健康
     */
    Map<String, Object> checkSystemHealth(@Param("tenantId") String tenantId);

    /**
     * 权限质量评估
     */
    Map<String, Object> assessPermissionQuality(@Param("tenantId") String tenantId);

    /**
     * 执行维护任务
     */
    int executeMaintenanceTask(@Param("tenantId") String tenantId, @Param("taskType") String taskType, @Param("taskParams") Map<String, Object> taskParams);

    /**
     * 获取维护任务状态
     */
    Map<String, Object> getMaintenanceTaskStatus(@Param("tenantId") String tenantId, @Param("taskId") String taskId);
}
