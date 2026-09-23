package com.management.accountant.mapper.as;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsDocumentVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 文档版本管理 Mapper 接口
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@Mapper
public interface AsDocumentVersionMapper extends BaseMapper<AsDocumentVersion> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据版本编号查询版本信息
     */
    AsDocumentVersion selectByVersionCode(@Param("tenantId") String tenantId, @Param("versionCode") String versionCode);

    /**
     * 根据文档ID查询版本列表
     */
    List<AsDocumentVersion> selectByDocumentId(@Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 根据版本类型查询版本列表
     */
    List<AsDocumentVersion> selectByVersionType(@Param("tenantId") String tenantId, @Param("versionType") String versionType);

    /**
     * 根据版本状态查询版本列表
     */
    List<AsDocumentVersion> selectByVersionStatus(@Param("tenantId") String tenantId, @Param("versionStatus") String versionStatus);

    /**
     * 根据分支名称查询版本列表
     */
    List<AsDocumentVersion> selectByBranchName(@Param("tenantId") String tenantId, @Param("branchName") String branchName);

    /**
     * 根据标签名称查询版本列表
     */
    List<AsDocumentVersion> selectByTagName(@Param("tenantId") String tenantId, @Param("tagName") String tagName);

    /**
     * 查询当前版本
     */
    AsDocumentVersion selectCurrentVersion(@Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 查询默认版本
     */
    AsDocumentVersion selectDefaultVersion(@Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 查询最新版本
     */
    AsDocumentVersion selectLatestVersion(@Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 查询父版本信息
     */
    AsDocumentVersion selectParentVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId);

    /**
     * 查询子版本列表
     */
    List<AsDocumentVersion> selectChildVersions(@Param("tenantId") String tenantId, @Param("parentVersionId") String parentVersionId);

    /**
     * 查询版本树结构
     */
    List<AsDocumentVersion> selectVersionTree(@Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 查询版本历史
     */
    List<AsDocumentVersion> selectVersionHistory(@Param("tenantId") String tenantId, @Param("documentId") String documentId, @Param("limit") Integer limit);

    // ==================== 分页查询方法 ====================

    /**
     * 分页查询版本列表
     */
    IPage<AsDocumentVersion> selectVersionPage(Page<AsDocumentVersion> page, @Param("tenantId") String tenantId, @Param("params") Map<String, Object> params);

    /**
     * 分页查询文档版本
     */
    IPage<AsDocumentVersion> selectDocumentVersionPage(Page<AsDocumentVersion> page, @Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 分页查询待审批版本
     */
    IPage<AsDocumentVersion> selectPendingApprovalPage(Page<AsDocumentVersion> page, @Param("tenantId") String tenantId, @Param("approverId") String approverId);

    /**
     * 分页查询已发布版本
     */
    IPage<AsDocumentVersion> selectPublishedVersionPage(Page<AsDocumentVersion> page, @Param("tenantId") String tenantId, @Param("params") Map<String, Object> params);

    /**
     * 分页查询归档版本
     */
    IPage<AsDocumentVersion> selectArchivedVersionPage(Page<AsDocumentVersion> page, @Param("tenantId") String tenantId, @Param("params") Map<String, Object> params);

    // ==================== 统计查询方法 ====================

    /**
     * 统计版本总数
     */
    Long countVersions(@Param("tenantId") String tenantId);

    /**
     * 按状态统计版本数量
     */
    List<Map<String, Object>> countByVersionStatus(@Param("tenantId") String tenantId);

    /**
     * 按类型统计版本数量
     */
    List<Map<String, Object>> countByVersionType(@Param("tenantId") String tenantId);

    /**
     * 按文档类型统计版本数量
     */
    List<Map<String, Object>> countByDocumentType(@Param("tenantId") String tenantId);

    /**
     * 按创建时间统计版本数量
     */
    List<Map<String, Object>> countByCreateTime(@Param("tenantId") String tenantId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计文档版本数量
     */
    Long countDocumentVersions(@Param("tenantId") String tenantId, @Param("documentId") String documentId);

    /**
     * 统计用户创建的版本数量
     */
    Long countUserVersions(@Param("tenantId") String tenantId, @Param("userId") String userId);

    /**
     * 统计部门版本数量
     */
    Long countDepartmentVersions(@Param("tenantId") String tenantId, @Param("departmentId") String departmentId);

    /**
     * 统计项目版本数量
     */
    Long countProjectVersions(@Param("tenantId") String tenantId, @Param("projectId") String projectId);

    // ==================== 版本管理方法 ====================

    /**
     * 创建版本分支
     */
    int createBranch(@Param("tenantId") String tenantId, @Param("sourceVersionId") String sourceVersionId, @Param("branchName") String branchName, @Param("userId") String userId);

    /**
     * 合并版本分支
     */
    int mergeBranch(@Param("tenantId") String tenantId, @Param("sourceVersionId") String sourceVersionId, @Param("targetVersionId") String targetVersionId, @Param("userId") String userId);

    /**
     * 创建版本标签
     */
    int createTag(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("tagName") String tagName, @Param("userId") String userId);

    /**
     * 删除版本标签
     */
    int deleteTag(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("tagName") String tagName);

    /**
     * 设置当前版本
     */
    int setCurrentVersion(@Param("tenantId") String tenantId, @Param("documentId") String documentId, @Param("versionId") String versionId);

    /**
     * 设置默认版本
     */
    int setDefaultVersion(@Param("tenantId") String tenantId, @Param("documentId") String documentId, @Param("versionId") String versionId);

    /**
     * 锁定版本
     */
    int lockVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId);

    /**
     * 解锁版本
     */
    int unlockVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId);

    /**
     * 发布版本
     */
    int publishVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId);

    /**
     * 撤销发布
     */
    int unpublishVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId);

    /**
     * 归档版本
     */
    int archiveVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId);

    /**
     * 恢复版本
     */
    int restoreVersion(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId);

    // ==================== 版本对比方法 ====================

    /**
     * 获取版本差异
     */
    Map<String, Object> getVersionDiff(@Param("tenantId") String tenantId, @Param("sourceVersionId") String sourceVersionId, @Param("targetVersionId") String targetVersionId);

    /**
     * 比较版本内容
     */
    Map<String, Object> compareVersions(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds);

    /**
     * 获取版本变更历史
     */
    List<Map<String, Object>> getVersionChangeHistory(@Param("tenantId") String tenantId, @Param("versionId") String versionId);

    // ==================== 权限管理方法 ====================

    /**
     * 检查版本访问权限
     */
    Boolean checkVersionAccess(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId, @Param("permission") String permission);

    /**
     * 获取版本权限列表
     */
    List<Map<String, Object>> getVersionPermissions(@Param("tenantId") String tenantId, @Param("versionId") String versionId);

    /**
     * 设置版本权限
     */
    int setVersionPermission(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId, @Param("permission") String permission);

    /**
     * 删除版本权限
     */
    int removeVersionPermission(@Param("tenantId") String tenantId, @Param("versionId") String versionId, @Param("userId") String userId, @Param("permission") String permission);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新版本状态
     */
    int batchUpdateStatus(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds, @Param("status") String status, @Param("userId") String userId);

    /**
     * 批量删除版本
     */
    int batchDeleteVersions(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds, @Param("userId") String userId);

    /**
     * 批量归档版本
     */
    int batchArchiveVersions(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds, @Param("userId") String userId);

    /**
     * 批量发布版本
     */
    int batchPublishVersions(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds, @Param("userId") String userId);

    /**
     * 批量设置权限
     */
    int batchSetPermissions(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds, @Param("permissions") Map<String, String> permissions, @Param("userId") String userId);

    // ==================== 数据管理方法 ====================

    /**
     * 导出版本数据
     */
    List<Map<String, Object>> exportVersionData(@Param("tenantId") String tenantId, @Param("versionIds") List<String> versionIds);

    /**
     * 清理过期版本
     */
    int cleanupExpiredVersions(@Param("tenantId") String tenantId, @Param("retentionDays") Integer retentionDays);

    /**
     * 清理无效版本
     */
    int cleanupInvalidVersions(@Param("tenantId") String tenantId);

    /**
     * 优化版本存储
     */
    int optimizeVersionStorage(@Param("tenantId") String tenantId);

    // ==================== 系统维护方法 ====================

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);

    /**
     * 生成版本报告
     */
    Map<String, Object> generateVersionReport(@Param("tenantId") String tenantId, @Param("reportType") String reportType, @Param("params") Map<String, Object> params);

    /**
     * 检查系统健康状态
     */
    Map<String, Object> checkSystemHealth(@Param("tenantId") String tenantId);

    /**
     * 获取版本统计信息
     */
    Map<String, Object> getVersionStatistics(@Param("tenantId") String tenantId, @Param("timeRange") String timeRange);

    /**
     * 获取存储使用情况
     */
    Map<String, Object> getStorageUsage(@Param("tenantId") String tenantId);

    /**
     * 获取用户活动统计
     */
    List<Map<String, Object>> getUserActivityStats(@Param("tenantId") String tenantId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取版本趋势数据
     */
    List<Map<String, Object>> getVersionTrend(@Param("tenantId") String tenantId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("granularity") String granularity);

    /**
     * 获取热门文档排行
     */
    List<Map<String, Object>> getPopularDocuments(@Param("tenantId") String tenantId, @Param("limit") Integer limit);

    /**
     * 获取活跃用户排行
     */
    List<Map<String, Object>> getActiveUsers(@Param("tenantId") String tenantId, @Param("limit") Integer limit);

    /**
     * 获取版本质量评估
     */
    Map<String, Object> getVersionQualityAssessment(@Param("tenantId") String tenantId, @Param("versionId") String versionId);
}
