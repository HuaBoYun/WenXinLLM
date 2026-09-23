import request from '@/utils/request'

// ==================== 常量定义 ====================

// 版本类型
export const VERSION_TYPES = {
  MAJOR: 'MAJOR',
  MINOR: 'MINOR',
  PATCH: 'PATCH',
  DRAFT: 'DRAFT',
  RELEASE: 'RELEASE',
  HOTFIX: 'HOTFIX',
  BRANCH: 'BRANCH',
  TAG: 'TAG',
  MERGE: 'MERGE',
  ROLLBACK: 'ROLLBACK'
}

// 版本状态
export const VERSION_STATUS = {
  DRAFT: 'DRAFT',
  UNDER_REVIEW: 'UNDER_REVIEW',
  APPROVED: 'APPROVED',
  PUBLISHED: 'PUBLISHED',
  ARCHIVED: 'ARCHIVED',
  DEPRECATED: 'DEPRECATED',
  DELETED: 'DELETED'
}

// 变更类型
export const CHANGE_TYPES = {
  CREATE: 'CREATE',
  UPDATE: 'UPDATE',
  DELETE: 'DELETE',
  COPY: 'COPY',
  BRANCH: 'BRANCH',
  MERGE: 'MERGE',
  ROLLBACK: 'ROLLBACK',
  RESTORE: 'RESTORE'
}

// 访问级别
export const ACCESS_LEVELS = {
  PUBLIC: 'PUBLIC',
  INTERNAL: 'INTERNAL',
  CONFIDENTIAL: 'CONFIDENTIAL',
  SECRET: 'SECRET',
  TOP_SECRET: 'TOP_SECRET'
}

// 审批状态
export const APPROVAL_STATUS = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  CANCELLED: 'CANCELLED'
}

// ==================== 基础CRUD操作 ====================

/**
 * 创建文档版本
 */
export function createVersion(data) {
  return request({
    url: '/accountant/as/documentVersion/create',
    method: 'post',
    data
  })
}

/**
 * 更新文档版本
 */
export function updateVersion(data) {
  return request({
    url: '/accountant/as/documentVersion/update',
    method: 'put',
    data
  })
}

/**
 * 删除文档版本
 */
export function deleteVersion(versionId) {
  return request({
    url: `/accountant/as/documentVersion/delete/${versionId}`,
    method: 'delete'
  })
}

/**
 * 获取文档版本详情
 */
export function getVersionById(versionId) {
  return request({
    url: `/accountant/as/documentVersion/get/${versionId}`,
    method: 'get'
  })
}

/**
 * 根据版本编号获取版本信息
 */
export function getVersionByCode(versionCode) {
  return request({
    url: `/accountant/as/documentVersion/getByCode/${versionCode}`,
    method: 'get'
  })
}

/**
 * 分页查询文档版本列表
 */
export function getVersionPage(params) {
  return request({
    url: '/accountant/as/documentVersion/page',
    method: 'post',
    data: params
  })
}

// ==================== 版本管理操作 ====================

/**
 * 创建新版本
 */
export function createNewVersion(documentId, versionType, description) {
  return request({
    url: '/accountant/as/documentVersion/createNewVersion',
    method: 'post',
    params: {
      documentId,
      versionType,
      description
    }
  })
}

/**
 * 复制版本
 */
export function copyVersion(sourceVersionId, newVersionName) {
  return request({
    url: '/accountant/as/documentVersion/copyVersion',
    method: 'post',
    params: {
      sourceVersionId,
      newVersionName
    }
  })
}

/**
 * 创建分支版本
 */
export function createBranch(sourceVersionId, branchName, description) {
  return request({
    url: '/accountant/as/documentVersion/createBranch',
    method: 'post',
    params: {
      sourceVersionId,
      branchName,
      description
    }
  })
}

/**
 * 合并分支版本
 */
export function mergeBranch(sourceVersionId, targetVersionId, mergeMessage) {
  return request({
    url: '/accountant/as/documentVersion/mergeBranch',
    method: 'post',
    params: {
      sourceVersionId,
      targetVersionId,
      mergeMessage
    }
  })
}

/**
 * 创建标签版本
 */
export function createTag(versionId, tagName, description) {
  return request({
    url: '/accountant/as/documentVersion/createTag',
    method: 'post',
    params: {
      versionId,
      tagName,
      description
    }
  })
}

/**
 * 删除标签
 */
export function deleteTag(versionId, tagName) {
  return request({
    url: '/accountant/as/documentVersion/deleteTag',
    method: 'delete',
    params: {
      versionId,
      tagName
    }
  })
}

/**
 * 回滚到指定版本
 */
export function rollbackToVersion(documentId, targetVersionId, rollbackReason) {
  return request({
    url: '/accountant/as/documentVersion/rollbackToVersion',
    method: 'post',
    params: {
      documentId,
      targetVersionId,
      rollbackReason
    }
  })
}

/**
 * 设置当前版本
 */
export function setCurrentVersion(documentId, versionId) {
  return request({
    url: '/accountant/as/documentVersion/setCurrentVersion',
    method: 'post',
    params: {
      documentId,
      versionId
    }
  })
}

/**
 * 设置默认版本
 */
export function setDefaultVersion(documentId, versionId) {
  return request({
    url: '/accountant/as/documentVersion/setDefaultVersion',
    method: 'post',
    params: {
      documentId,
      versionId
    }
  })
}

// ==================== 版本状态管理 ====================

/**
 * 提交版本审批
 */
export function submitForApproval(versionId, approvalProcessId) {
  return request({
    url: '/accountant/as/documentVersion/submitForApproval',
    method: 'post',
    params: {
      versionId,
      approvalProcessId
    }
  })
}

/**
 * 审批版本
 */
export function approveVersion(versionId, approvalComment) {
  return request({
    url: '/accountant/as/documentVersion/approveVersion',
    method: 'post',
    params: {
      versionId,
      approvalComment
    }
  })
}

/**
 * 拒绝版本
 */
export function rejectVersion(versionId, rejectReason) {
  return request({
    url: '/accountant/as/documentVersion/rejectVersion',
    method: 'post',
    params: {
      versionId,
      rejectReason
    }
  })
}

/**
 * 发布版本
 */
export function publishVersion(versionId) {
  return request({
    url: '/accountant/as/documentVersion/publishVersion',
    method: 'post',
    params: {
      versionId
    }
  })
}

/**
 * 撤销发布
 */
export function unpublishVersion(versionId, reason) {
  return request({
    url: '/accountant/as/documentVersion/unpublishVersion',
    method: 'post',
    params: {
      versionId,
      reason
    }
  })
}

/**
 * 锁定版本
 */
export function lockVersion(versionId, lockReason) {
  return request({
    url: '/accountant/as/documentVersion/lockVersion',
    method: 'post',
    params: {
      versionId,
      lockReason
    }
  })
}

/**
 * 解锁版本
 */
export function unlockVersion(versionId) {
  return request({
    url: '/accountant/as/documentVersion/unlockVersion',
    method: 'post',
    params: {
      versionId
    }
  })
}

/**
 * 归档版本
 */
export function archiveVersion(versionId, archiveReason) {
  return request({
    url: '/accountant/as/documentVersion/archiveVersion',
    method: 'post',
    params: {
      versionId,
      archiveReason
    }
  })
}

/**
 * 恢复版本
 */
export function restoreVersion(versionId) {
  return request({
    url: '/accountant/as/documentVersion/restoreVersion',
    method: 'post',
    params: {
      versionId
    }
  })
}

// ==================== 版本查询操作 ====================

/**
 * 获取文档的所有版本
 */
export function getDocumentVersions(documentId) {
  return request({
    url: `/accountant/as/documentVersion/getDocumentVersions/${documentId}`,
    method: 'get'
  })
}

/**
 * 获取当前版本
 */
export function getCurrentVersion(documentId) {
  return request({
    url: `/accountant/as/documentVersion/getCurrentVersion/${documentId}`,
    method: 'get'
  })
}

/**
 * 获取默认版本
 */
export function getDefaultVersion(documentId) {
  return request({
    url: `/accountant/as/documentVersion/getDefaultVersion/${documentId}`,
    method: 'get'
  })
}

/**
 * 获取最新版本
 */
export function getLatestVersion(documentId) {
  return request({
    url: `/accountant/as/documentVersion/getLatestVersion/${documentId}`,
    method: 'get'
  })
}

/**
 * 获取版本历史
 */
export function getVersionHistory(documentId, limit = 10) {
  return request({
    url: `/accountant/as/documentVersion/getVersionHistory/${documentId}`,
    method: 'get',
    params: {
      limit
    }
  })
}

/**
 * 获取版本树结构
 */
export function getVersionTree(documentId) {
  return request({
    url: `/accountant/as/documentVersion/getVersionTree/${documentId}`,
    method: 'get'
  })
}

/**
 * 获取子版本列表
 */
export function getChildVersions(parentVersionId) {
  return request({
    url: `/accountant/as/documentVersion/getChildVersions/${parentVersionId}`,
    method: 'get'
  })
}

/**
 * 获取父版本信息
 */
export function getParentVersion(versionId) {
  return request({
    url: `/accountant/as/documentVersion/getParentVersion/${versionId}`,
    method: 'get'
  })
}

/**
 * 根据分支名称获取版本列表
 */
export function getVersionsByBranch(branchName) {
  return request({
    url: `/accountant/as/documentVersion/getVersionsByBranch/${branchName}`,
    method: 'get'
  })
}

/**
 * 根据标签名称获取版本列表
 */
export function getVersionsByTag(tagName) {
  return request({
    url: `/accountant/as/documentVersion/getVersionsByTag/${tagName}`,
    method: 'get'
  })
}

/**
 * 根据版本类型获取版本列表
 */
export function getVersionsByType(versionType) {
  return request({
    url: `/accountant/as/documentVersion/getVersionsByType/${versionType}`,
    method: 'get'
  })
}

/**
 * 根据版本状态获取版本列表
 */
export function getVersionsByStatus(versionStatus) {
  return request({
    url: `/accountant/as/documentVersion/getVersionsByStatus/${versionStatus}`,
    method: 'get'
  })
}

// ==================== 版本对比操作 ====================

/**
 * 比较两个版本
 */
export function compareVersions(sourceVersionId, targetVersionId) {
  return request({
    url: '/accountant/as/documentVersion/compareVersions',
    method: 'post',
    params: {
      sourceVersionId,
      targetVersionId
    }
  })
}

/**
 * 获取版本差异
 */
export function getVersionDiff(sourceVersionId, targetVersionId) {
  return request({
    url: '/accountant/as/documentVersion/getVersionDiff',
    method: 'post',
    params: {
      sourceVersionId,
      targetVersionId
    }
  })
}

/**
 * 获取版本变更历史
 */
export function getVersionChangeHistory(versionId) {
  return request({
    url: `/accountant/as/documentVersion/getVersionChangeHistory/${versionId}`,
    method: 'get'
  })
}

/**
 * 生成版本对比报告
 */
export function generateComparisonReport(versionIds, reportFormat) {
  return request({
    url: '/accountant/as/documentVersion/generateComparisonReport',
    method: 'post',
    data: versionIds,
    params: {
      reportFormat
    }
  })
}

// ==================== 权限管理操作 ====================

/**
 * 检查版本访问权限
 */
export function checkVersionAccess(versionId, permission) {
  return request({
    url: '/accountant/as/documentVersion/checkVersionAccess',
    method: 'get',
    params: {
      versionId,
      permission
    }
  })
}

/**
 * 获取版本权限列表
 */
export function getVersionPermissions(versionId) {
  return request({
    url: `/accountant/as/documentVersion/getVersionPermissions/${versionId}`,
    method: 'get'
  })
}

/**
 * 设置版本权限
 */
export function setVersionPermission(versionId, targetUserId, permission) {
  return request({
    url: '/accountant/as/documentVersion/setVersionPermission',
    method: 'post',
    params: {
      versionId,
      targetUserId,
      permission
    }
  })
}

/**
 * 删除版本权限
 */
export function removeVersionPermission(versionId, targetUserId, permission) {
  return request({
    url: '/accountant/as/documentVersion/removeVersionPermission',
    method: 'delete',
    params: {
      versionId,
      targetUserId,
      permission
    }
  })
}

/**
 * 批量设置权限
 */
export function batchSetPermissions(versionIds, permissions) {
  return request({
    url: '/accountant/as/documentVersion/batchSetPermissions',
    method: 'post',
    data: {
      versionIds,
      permissions
    }
  })
}

/**
 * 继承父版本权限
 */
export function inheritParentPermissions(versionId, parentVersionId) {
  return request({
    url: '/accountant/as/documentVersion/inheritParentPermissions',
    method: 'post',
    params: {
      versionId,
      parentVersionId
    }
  })
}

// ==================== 批量操作 ====================

/**
 * 批量创建版本
 */
export function batchCreateVersions(versions) {
  return request({
    url: '/accountant/as/documentVersion/batchCreateVersions',
    method: 'post',
    data: versions
  })
}

/**
 * 批量更新版本状态
 */
export function batchUpdateStatus(versionIds, status) {
  return request({
    url: '/accountant/as/documentVersion/batchUpdateStatus',
    method: 'post',
    data: versionIds,
    params: {
      status
    }
  })
}

/**
 * 批量删除版本
 */
export function batchDeleteVersions(versionIds) {
  return request({
    url: '/accountant/as/documentVersion/batchDeleteVersions',
    method: 'post',
    data: versionIds
  })
}

/**
 * 批量归档版本
 */
export function batchArchiveVersions(versionIds) {
  return request({
    url: '/accountant/as/documentVersion/batchArchiveVersions',
    method: 'post',
    data: versionIds
  })
}

/**
 * 批量发布版本
 */
export function batchPublishVersions(versionIds) {
  return request({
    url: '/accountant/as/documentVersion/batchPublishVersions',
    method: 'post',
    data: versionIds
  })
}

/**
 * 批量锁定版本
 */
export function batchLockVersions(versionIds, lockReason) {
  return request({
    url: '/accountant/as/documentVersion/batchLockVersions',
    method: 'post',
    data: versionIds,
    params: {
      lockReason
    }
  })
}

/**
 * 批量解锁版本
 */
export function batchUnlockVersions(versionIds) {
  return request({
    url: '/accountant/as/documentVersion/batchUnlockVersions',
    method: 'post',
    data: versionIds
  })
}

// ==================== 统计分析操作 ====================

/**
 * 统计版本数量
 */
export function countVersions() {
  return request({
    url: '/accountant/as/documentVersion/countVersions',
    method: 'get'
  })
}

/**
 * 按状态统计版本数量
 */
export function countByStatus() {
  return request({
    url: '/accountant/as/documentVersion/countByStatus',
    method: 'get'
  })
}

/**
 * 按类型统计版本数量
 */
export function countByType() {
  return request({
    url: '/accountant/as/documentVersion/countByType',
    method: 'get'
  })
}

/**
 * 按文档类型统计版本数量
 */
export function countByDocumentType() {
  return request({
    url: '/accountant/as/documentVersion/countByDocumentType',
    method: 'get'
  })
}

/**
 * 获取版本趋势数据
 */
export function getVersionTrend(startTime, endTime, granularity = 'day') {
  return request({
    url: '/accountant/as/documentVersion/getVersionTrend',
    method: 'get',
    params: {
      startTime,
      endTime,
      granularity
    }
  })
}

/**
 * 获取用户活动统计
 */
export function getUserActivityStats(startTime, endTime) {
  return request({
    url: '/accountant/as/documentVersion/getUserActivityStats',
    method: 'get',
    params: {
      startTime,
      endTime
    }
  })
}

/**
 * 获取热门文档排行
 */
export function getPopularDocuments(limit = 10) {
  return request({
    url: '/accountant/as/documentVersion/getPopularDocuments',
    method: 'get',
    params: {
      limit
    }
  })
}

/**
 * 获取活跃用户排行
 */
export function getActiveUsers(limit = 10) {
  return request({
    url: '/accountant/as/documentVersion/getActiveUsers',
    method: 'get',
    params: {
      limit
    }
  })
}

/**
 * 获取存储使用情况
 */
export function getStorageUsage() {
  return request({
    url: '/accountant/as/documentVersion/getStorageUsage',
    method: 'get'
  })
}

// ==================== 数据管理操作 ====================

/**
 * 导出版本数据
 */
export function exportVersionData(versionIds, exportFormat) {
  return request({
    url: '/accountant/as/documentVersion/exportVersionData',
    method: 'post',
    data: versionIds,
    params: {
      exportFormat
    }
  })
}

/**
 * 导入版本数据
 */
export function importVersionData(importData, importFormat) {
  return request({
    url: '/accountant/as/documentVersion/importVersionData',
    method: 'post',
    params: {
      importData,
      importFormat
    }
  })
}

/**
 * 清理过期版本
 */
export function cleanupExpiredVersions(retentionDays) {
  return request({
    url: '/accountant/as/documentVersion/cleanupExpiredVersions',
    method: 'post',
    params: {
      retentionDays
    }
  })
}

/**
 * 清理无效版本
 */
export function cleanupInvalidVersions() {
  return request({
    url: '/accountant/as/documentVersion/cleanupInvalidVersions',
    method: 'post'
  })
}

/**
 * 优化版本存储
 */
export function optimizeVersionStorage() {
  return request({
    url: '/accountant/as/documentVersion/optimizeVersionStorage',
    method: 'post'
  })
}

/**
 * 备份版本数据
 */
export function backupVersionData(versionIds, backupLocation) {
  return request({
    url: '/accountant/as/documentVersion/backupVersionData',
    method: 'post',
    data: versionIds,
    params: {
      backupLocation
    }
  })
}

/**
 * 恢复版本数据
 */
export function restoreVersionData(backupLocation) {
  return request({
    url: '/accountant/as/documentVersion/restoreVersionData',
    method: 'post',
    params: {
      backupLocation
    }
  })
}

// ==================== 系统维护操作 ====================

/**
 * 获取系统概览
 */
export function getSystemOverview() {
  return request({
    url: '/accountant/as/documentVersion/getSystemOverview',
    method: 'get'
  })
}

/**
 * 生成版本报告
 */
export function generateVersionReport(reportType, params) {
  return request({
    url: '/accountant/as/documentVersion/generateVersionReport',
    method: 'post',
    data: params,
    params: {
      reportType
    }
  })
}

/**
 * 检查系统健康状态
 */
export function checkSystemHealth() {
  return request({
    url: '/accountant/as/documentVersion/checkSystemHealth',
    method: 'get'
  })
}

/**
 * 获取版本质量评估
 */
export function getVersionQualityAssessment(versionId) {
  return request({
    url: `/accountant/as/documentVersion/getVersionQualityAssessment/${versionId}`,
    method: 'get'
  })
}

/**
 * 执行版本维护任务
 */
export function executeMaintenanceTask(taskType, taskParams) {
  return request({
    url: '/accountant/as/documentVersion/executeMaintenanceTask',
    method: 'post',
    data: taskParams,
    params: {
      taskType
    }
  })
}

/**
 * 获取维护任务状态
 */
export function getMaintenanceTaskStatus(taskId) {
  return request({
    url: `/accountant/as/documentVersion/getMaintenanceTaskStatus/${taskId}`,
    method: 'get'
  })
}

// ==================== 通知操作 ====================

/**
 * 发送版本通知
 */
export function sendVersionNotification(versionId, notificationType, recipients, message) {
  return request({
    url: '/accountant/as/documentVersion/sendVersionNotification',
    method: 'post',
    data: recipients,
    params: {
      versionId,
      notificationType,
      message
    }
  })
}

/**
 * 订阅版本更新通知
 */
export function subscribeVersionUpdates(documentId, notificationMethod) {
  return request({
    url: '/accountant/as/documentVersion/subscribeVersionUpdates',
    method: 'post',
    params: {
      documentId,
      notificationMethod
    }
  })
}

/**
 * 取消订阅版本更新通知
 */
export function unsubscribeVersionUpdates(documentId) {
  return request({
    url: '/accountant/as/documentVersion/unsubscribeVersionUpdates',
    method: 'post',
    params: {
      documentId
    }
  })
}

/**
 * 获取版本通知历史
 */
export function getVersionNotificationHistory(versionId) {
  return request({
    url: `/accountant/as/documentVersion/getVersionNotificationHistory/${versionId}`,
    method: 'get'
  })
}

// ==================== 工具函数 ====================

/**
 * 格式化版本号
 */
export function formatVersionNumber(versionNumber) {
  if (!versionNumber) return '-'
  return versionNumber
}

/**
 * 格式化版本状态
 */
export function formatVersionStatus(status) {
  const statusMap = {
    [VERSION_STATUS.DRAFT]: '草稿',
    [VERSION_STATUS.UNDER_REVIEW]: '审核中',
    [VERSION_STATUS.APPROVED]: '已审批',
    [VERSION_STATUS.PUBLISHED]: '已发布',
    [VERSION_STATUS.ARCHIVED]: '已归档',
    [VERSION_STATUS.DEPRECATED]: '已废弃',
    [VERSION_STATUS.DELETED]: '已删除'
  }
  return statusMap[status] || status
}

/**
 * 格式化版本类型
 */
export function formatVersionType(type) {
  const typeMap = {
    [VERSION_TYPES.MAJOR]: '主版本',
    [VERSION_TYPES.MINOR]: '次版本',
    [VERSION_TYPES.PATCH]: '补丁版本',
    [VERSION_TYPES.DRAFT]: '草稿版本',
    [VERSION_TYPES.RELEASE]: '发布版本',
    [VERSION_TYPES.HOTFIX]: '热修复版本',
    [VERSION_TYPES.BRANCH]: '分支版本',
    [VERSION_TYPES.TAG]: '标签版本',
    [VERSION_TYPES.MERGE]: '合并版本',
    [VERSION_TYPES.ROLLBACK]: '回滚版本'
  }
  return typeMap[type] || type
}

/**
 * 格式化文件大小
 */
export function formatFileSize(size) {
  if (!size || size === 0) return '0 B'

  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let index = 0
  let fileSize = size

  while (fileSize >= 1024 && index < units.length - 1) {
    fileSize /= 1024
    index++
  }

  return `${fileSize.toFixed(2)} ${units[index]}`
}

/**
 * 格式化访问级别
 */
export function formatAccessLevel(level) {
  const levelMap = {
    [ACCESS_LEVELS.PUBLIC]: '公开',
    [ACCESS_LEVELS.INTERNAL]: '内部',
    [ACCESS_LEVELS.CONFIDENTIAL]: '机密',
    [ACCESS_LEVELS.SECRET]: '秘密',
    [ACCESS_LEVELS.TOP_SECRET]: '绝密'
  }
  return levelMap[level] || level
}

/**
 * 计算版本差异
 */
export function calculateVersionDiff(sourceVersion, targetVersion) {
  const diff = {
    added: [],
    modified: [],
    deleted: []
  }

  // 这里可以实现具体的版本差异计算逻辑
  // 比较两个版本的字段差异

  return diff
}

/**
 * 获取版本状态颜色
 */
export function getVersionStatusColor(status) {
  const colorMap = {
    [VERSION_STATUS.DRAFT]: '#909399',
    [VERSION_STATUS.UNDER_REVIEW]: '#E6A23C',
    [VERSION_STATUS.APPROVED]: '#67C23A',
    [VERSION_STATUS.PUBLISHED]: '#409EFF',
    [VERSION_STATUS.ARCHIVED]: '#909399',
    [VERSION_STATUS.DEPRECATED]: '#F56C6C',
    [VERSION_STATUS.DELETED]: '#F56C6C'
  }
  return colorMap[status] || '#909399'
}

/**
 * 获取版本类型图标
 */
export function getVersionTypeIcon(type) {
  const iconMap = {
    [VERSION_TYPES.MAJOR]: 'el-icon-star-on',
    [VERSION_TYPES.MINOR]: 'el-icon-star-off',
    [VERSION_TYPES.PATCH]: 'el-icon-circle-plus',
    [VERSION_TYPES.DRAFT]: 'el-icon-edit',
    [VERSION_TYPES.RELEASE]: 'el-icon-upload',
    [VERSION_TYPES.HOTFIX]: 'el-icon-warning',
    [VERSION_TYPES.BRANCH]: 'el-icon-share',
    [VERSION_TYPES.TAG]: 'el-icon-price-tag',
    [VERSION_TYPES.MERGE]: 'el-icon-connection',
    [VERSION_TYPES.ROLLBACK]: 'el-icon-refresh-left'
  }
  return iconMap[type] || 'el-icon-document'
}

// ==================== 快捷操作 ====================

/**
 * 快速创建主版本
 */
export function quickCreateMajorVersion(documentId, description) {
  return createNewVersion(documentId, VERSION_TYPES.MAJOR, description)
}

/**
 * 快速创建次版本
 */
export function quickCreateMinorVersion(documentId, description) {
  return createNewVersion(documentId, VERSION_TYPES.MINOR, description)
}

/**
 * 快速创建补丁版本
 */
export function quickCreatePatchVersion(documentId, description) {
  return createNewVersion(documentId, VERSION_TYPES.PATCH, description)
}

/**
 * 快速发布版本
 */
export function quickPublishVersion(versionId) {
  return publishVersion(versionId)
}

/**
 * 快速归档版本
 */
export function quickArchiveVersion(versionId, reason = '自动归档') {
  return archiveVersion(versionId, reason)
}

/**
 * 快速锁定版本
 */
export function quickLockVersion(versionId, reason = '版本锁定') {
  return lockVersion(versionId, reason)
}
