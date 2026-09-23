package com.management.accountant.service.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.as.AsDocumentVersion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 文档版本管理服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
public interface AsDocumentVersionService extends IService<AsDocumentVersion> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建文档版本
     */
    AsDocumentVersion createVersion(AsDocumentVersion version);

    /**
     * 更新文档版本
     */
    AsDocumentVersion updateVersion(AsDocumentVersion version);

    /**
     * 删除文档版本
     */
    boolean deleteVersion(String tenantId, String versionId);

    /**
     * 根据ID获取版本信息
     */
    AsDocumentVersion getVersionById(String tenantId, String versionId);

    /**
     * 根据版本编号获取版本信息
     */
    AsDocumentVersion getVersionByCode(String tenantId, String versionCode);

    /**
     * 分页查询版本列表
     */
    IPage<AsDocumentVersion> getVersionPage(String tenantId, Integer current, Integer size, Map<String, Object> params);

    // ==================== 版本管理操作 ====================

    /**
     * 创建新版本
     */
    AsDocumentVersion createNewVersion(String tenantId, String documentId, String versionType, String description, String userId);

    /**
     * 复制版本
     */
    AsDocumentVersion copyVersion(String tenantId, String sourceVersionId, String newVersionName, String userId);

    /**
     * 创建分支版本
     */
    AsDocumentVersion createBranch(String tenantId, String sourceVersionId, String branchName, String description, String userId);

    /**
     * 合并分支版本
     */
    AsDocumentVersion mergeBranch(String tenantId, String sourceVersionId, String targetVersionId, String mergeMessage, String userId);

    /**
     * 创建标签版本
     */
    AsDocumentVersion createTag(String tenantId, String versionId, String tagName, String description, String userId);

    /**
     * 删除标签
     */
    boolean deleteTag(String tenantId, String versionId, String tagName, String userId);

    /**
     * 回滚到指定版本
     */
    AsDocumentVersion rollbackToVersion(String tenantId, String documentId, String targetVersionId, String rollbackReason, String userId);

    /**
     * 设置当前版本
     */
    boolean setCurrentVersion(String tenantId, String documentId, String versionId, String userId);

    /**
     * 设置默认版本
     */
    boolean setDefaultVersion(String tenantId, String documentId, String versionId, String userId);

    // ==================== 版本状态管理 ====================

    /**
     * 提交版本审批
     */
    boolean submitForApproval(String tenantId, String versionId, String approvalProcessId, String userId);

    /**
     * 审批版本
     */
    boolean approveVersion(String tenantId, String versionId, String approvalComment, String approverId);

    /**
     * 拒绝版本
     */
    boolean rejectVersion(String tenantId, String versionId, String rejectReason, String approverId);

    /**
     * 发布版本
     */
    boolean publishVersion(String tenantId, String versionId, String userId);

    /**
     * 撤销发布
     */
    boolean unpublishVersion(String tenantId, String versionId, String reason, String userId);

    /**
     * 锁定版本
     */
    boolean lockVersion(String tenantId, String versionId, String lockReason, String userId);

    /**
     * 解锁版本
     */
    boolean unlockVersion(String tenantId, String versionId, String userId);

    /**
     * 归档版本
     */
    boolean archiveVersion(String tenantId, String versionId, String archiveReason, String userId);

    /**
     * 恢复版本
     */
    boolean restoreVersion(String tenantId, String versionId, String userId);

    // ==================== 版本查询操作 ====================

    /**
     * 获取文档的所有版本
     */
    List<AsDocumentVersion> getDocumentVersions(String tenantId, String documentId);

    /**
     * 获取当前版本
     */
    AsDocumentVersion getCurrentVersion(String tenantId, String documentId);

    /**
     * 获取默认版本
     */
    AsDocumentVersion getDefaultVersion(String tenantId, String documentId);

    /**
     * 获取最新版本
     */
    AsDocumentVersion getLatestVersion(String tenantId, String documentId);

    /**
     * 获取版本历史
     */
    List<AsDocumentVersion> getVersionHistory(String tenantId, String documentId, Integer limit);

    /**
     * 获取版本树结构
     */
    List<AsDocumentVersion> getVersionTree(String tenantId, String documentId);

    /**
     * 获取子版本列表
     */
    List<AsDocumentVersion> getChildVersions(String tenantId, String parentVersionId);

    /**
     * 获取父版本信息
     */
    AsDocumentVersion getParentVersion(String tenantId, String versionId);

    /**
     * 根据分支名称获取版本列表
     */
    List<AsDocumentVersion> getVersionsByBranch(String tenantId, String branchName);

    /**
     * 根据标签名称获取版本列表
     */
    List<AsDocumentVersion> getVersionsByTag(String tenantId, String tagName);

    /**
     * 根据版本类型获取版本列表
     */
    List<AsDocumentVersion> getVersionsByType(String tenantId, String versionType);

    /**
     * 根据版本状态获取版本列表
     */
    List<AsDocumentVersion> getVersionsByStatus(String tenantId, String versionStatus);

    // ==================== 版本对比操作 ====================

    /**
     * 比较两个版本
     */
    Map<String, Object> compareVersions(String tenantId, String sourceVersionId, String targetVersionId);

    /**
     * 获取版本差异
     */
    Map<String, Object> getVersionDiff(String tenantId, String sourceVersionId, String targetVersionId);

    /**
     * 获取版本变更历史
     */
    List<Map<String, Object>> getVersionChangeHistory(String tenantId, String versionId);

    /**
     * 生成版本对比报告
     */
    Map<String, Object> generateComparisonReport(String tenantId, List<String> versionIds, String reportFormat);

    // ==================== 权限管理操作 ====================

    /**
     * 检查版本访问权限
     */
    boolean checkVersionAccess(String tenantId, String versionId, String userId, String permission);

    /**
     * 获取版本权限列表
     */
    List<Map<String, Object>> getVersionPermissions(String tenantId, String versionId);

    /**
     * 设置版本权限
     */
    boolean setVersionPermission(String tenantId, String versionId, String userId, String permission, String granterId);

    /**
     * 删除版本权限
     */
    boolean removeVersionPermission(String tenantId, String versionId, String userId, String permission, String removerId);

    /**
     * 批量设置权限
     */
    boolean batchSetPermissions(String tenantId, List<String> versionIds, Map<String, String> permissions, String userId);

    /**
     * 继承父版本权限
     */
    boolean inheritParentPermissions(String tenantId, String versionId, String parentVersionId, String userId);

    // ==================== 批量操作 ====================

    /**
     * 批量创建版本
     */
    List<AsDocumentVersion> batchCreateVersions(String tenantId, List<AsDocumentVersion> versions, String userId);

    /**
     * 批量更新版本状态
     */
    boolean batchUpdateStatus(String tenantId, List<String> versionIds, String status, String userId);

    /**
     * 批量删除版本
     */
    boolean batchDeleteVersions(String tenantId, List<String> versionIds, String userId);

    /**
     * 批量归档版本
     */
    boolean batchArchiveVersions(String tenantId, List<String> versionIds, String userId);

    /**
     * 批量发布版本
     */
    boolean batchPublishVersions(String tenantId, List<String> versionIds, String userId);

    /**
     * 批量锁定版本
     */
    boolean batchLockVersions(String tenantId, List<String> versionIds, String lockReason, String userId);

    /**
     * 批量解锁版本
     */
    boolean batchUnlockVersions(String tenantId, List<String> versionIds, String userId);

    // ==================== 统计分析操作 ====================

    /**
     * 统计版本数量
     */
    Long countVersions(String tenantId);

    /**
     * 按状态统计版本数量
     */
    List<Map<String, Object>> countByStatus(String tenantId);

    /**
     * 按类型统计版本数量
     */
    List<Map<String, Object>> countByType(String tenantId);

    /**
     * 按文档类型统计版本数量
     */
    List<Map<String, Object>> countByDocumentType(String tenantId);

    /**
     * 获取版本趋势数据
     */
    List<Map<String, Object>> getVersionTrend(String tenantId, LocalDateTime startTime, LocalDateTime endTime, String granularity);

    /**
     * 获取用户活动统计
     */
    List<Map<String, Object>> getUserActivityStats(String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取热门文档排行
     */
    List<Map<String, Object>> getPopularDocuments(String tenantId, Integer limit);

    /**
     * 获取活跃用户排行
     */
    List<Map<String, Object>> getActiveUsers(String tenantId, Integer limit);

    /**
     * 获取存储使用情况
     */
    Map<String, Object> getStorageUsage(String tenantId);

    // ==================== 数据管理操作 ====================

    /**
     * 导出版本数据
     */
    List<Map<String, Object>> exportVersionData(String tenantId, List<String> versionIds, String exportFormat);

    /**
     * 导入版本数据
     */
    List<AsDocumentVersion> importVersionData(String tenantId, String importData, String importFormat, String userId);

    /**
     * 清理过期版本
     */
    int cleanupExpiredVersions(String tenantId, Integer retentionDays);

    /**
     * 清理无效版本
     */
    int cleanupInvalidVersions(String tenantId);

    /**
     * 优化版本存储
     */
    boolean optimizeVersionStorage(String tenantId);

    /**
     * 备份版本数据
     */
    boolean backupVersionData(String tenantId, List<String> versionIds, String backupLocation);

    /**
     * 恢复版本数据
     */
    boolean restoreVersionData(String tenantId, String backupLocation, String userId);

    // ==================== 系统维护操作 ====================

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(String tenantId);

    /**
     * 生成版本报告
     */
    Map<String, Object> generateVersionReport(String tenantId, String reportType, Map<String, Object> params);

    /**
     * 检查系统健康状态
     */
    Map<String, Object> checkSystemHealth(String tenantId);

    /**
     * 获取版本质量评估
     */
    Map<String, Object> getVersionQualityAssessment(String tenantId, String versionId);

    /**
     * 执行版本维护任务
     */
    boolean executeMaintenanceTask(String tenantId, String taskType, Map<String, Object> taskParams);

    /**
     * 获取维护任务状态
     */
    Map<String, Object> getMaintenanceTaskStatus(String tenantId, String taskId);

    // ==================== 通知操作 ====================

    /**
     * 发送版本通知
     */
    boolean sendVersionNotification(String tenantId, String versionId, String notificationType, List<String> recipients, String message);

    /**
     * 订阅版本更新通知
     */
    boolean subscribeVersionUpdates(String tenantId, String documentId, String userId, String notificationMethod);

    /**
     * 取消订阅版本更新通知
     */
    boolean unsubscribeVersionUpdates(String tenantId, String documentId, String userId);

    /**
     * 获取版本通知历史
     */
    List<Map<String, Object>> getVersionNotificationHistory(String tenantId, String versionId);
}
