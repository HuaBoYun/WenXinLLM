import request from '@/utils/request'

// ==================== 常量定义 ====================

// 权限类型
export const PERMISSION_TYPES = {
  RESOURCE: 'RESOURCE',
  OPERATION: 'OPERATION',
  DATA: 'DATA',
  FUNCTION: 'FUNCTION'
}

// 权限级别
export const PERMISSION_LEVELS = {
  SYSTEM: 'SYSTEM',
  MODULE: 'MODULE',
  RESOURCE: 'RESOURCE',
  RECORD: 'RECORD'
}

// 资源类型
export const RESOURCE_TYPES = {
  ARCHIVE: 'ARCHIVE',
  DOCUMENT: 'DOCUMENT',
  FOLDER: 'FOLDER',
  CATEGORY: 'CATEGORY',
  SYSTEM: 'SYSTEM'
}

// 主体类型
export const SUBJECT_TYPES = {
  USER: 'USER',
  ROLE: 'ROLE',
  GROUP: 'GROUP',
  DEPARTMENT: 'DEPARTMENT',
  ORGANIZATION: 'ORGANIZATION'
}

// 权限状态
export const PERMISSION_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  SUSPENDED: 'SUSPENDED',
  EXPIRED: 'EXPIRED',
  REVOKED: 'REVOKED'
}

// 审批状态
export const APPROVAL_STATUS = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  CANCELLED: 'CANCELLED'
}

// 访问级别
export const ACCESS_LEVELS = {
  PUBLIC: 'PUBLIC',
  INTERNAL: 'INTERNAL',
  CONFIDENTIAL: 'CONFIDENTIAL',
  SECRET: 'SECRET',
  TOP_SECRET: 'TOP_SECRET'
}

// 风险等级
export const RISK_LEVELS = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
  CRITICAL: 'CRITICAL'
}

// 操作权限
export const OPERATIONS = {
  READ: 'READ',
  WRITE: 'write',
  DELETE: 'delete',
  EXECUTE: 'execute',
  ADMIN: 'admin'
}

// ==================== 基本CRUD操作 ====================

/**
 * 创建权限
 */
export function createPermission(data) {
  return request({
    url: '/accountant/as/archivePermission/create',
    method: 'post',
    data
  })
}

/**
 * 更新权限
 */
export function updatePermission(data) {
  return request({
    url: '/accountant/as/archivePermission/update',
    method: 'put',
    data
  })
}

/**
 * 删除权限
 */
export function deletePermission(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/delete/${permissionId}`,
    method: 'delete'
  })
}

/**
 * 获取权限详情
 */
export function getPermissionById(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/get/${permissionId}`,
    method: 'get'
  })
}

/**
 * 根据编码获取权限
 */
export function getPermissionByCode(permissionCode) {
  return request({
    url: `/accountant/as/archivePermission/getByCode/${permissionCode}`,
    method: 'get'
  })
}

/**
 * 分页查询权限
 */
export function getPermissionPage(current, size, params) {
  return request({
    url: '/accountant/as/archivePermission/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

// ==================== 权限验证操作 ====================

/**
 * 检查用户权限
 */
export function checkUserPermission(userId, resourceType, resourceId, operation) {
  return request({
    url: '/accountant/as/archivePermission/checkUserPermission',
    method: 'get',
    params: { userId, resourceType, resourceId, operation }
  })
}

/**
 * 检查角色权限
 */
export function checkRolePermission(roleId, resourceType, resourceId, operation) {
  return request({
    url: '/accountant/as/archivePermission/checkRolePermission',
    method: 'get',
    params: { roleId, resourceType, resourceId, operation }
  })
}

/**
 * 获取用户有效权限
 */
export function getUserEffectivePermissions(userId, resourceType) {
  return request({
    url: '/accountant/as/archivePermission/getUserEffectivePermissions',
    method: 'get',
    params: { userId, resourceType }
  })
}

/**
 * 获取角色有效权限
 */
export function getRoleEffectivePermissions(roleId, resourceType) {
  return request({
    url: '/accountant/as/archivePermission/getRoleEffectivePermissions',
    method: 'get',
    params: { roleId, resourceType }
  })
}

/**
 * 获取资源权限列表
 */
export function getResourcePermissions(resourceType, resourceId) {
  return request({
    url: '/accountant/as/archivePermission/getResourcePermissions',
    method: 'get',
    params: { resourceType, resourceId }
  })
}

// ==================== 权限管理操作 ====================

/**
 * 授予权限
 */
export function grantPermission(subjectType, subjectId, resourceType, resourceId, operations) {
  return request({
    url: '/accountant/as/archivePermission/grant',
    method: 'post',
    params: { subjectType, subjectId, resourceType, resourceId, operations }
  })
}

/**
 * 撤销权限
 */
export function revokePermission(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/revoke/${permissionId}`,
    method: 'post'
  })
}

/**
 * 批量授予权限
 */
export function batchGrantPermissions(subjectType, subjectId, permissions) {
  return request({
    url: '/accountant/as/archivePermission/batchGrant',
    method: 'post',
    params: { subjectType, subjectId },
    data: permissions
  })
}

/**
 * 批量撤销权限
 */
export function batchRevokePermissions(subjectType, subjectId, permissionIds) {
  return request({
    url: '/accountant/as/archivePermission/batchRevoke',
    method: 'post',
    params: { subjectType, subjectId },
    data: permissionIds
  })
}

/**
 * 更新权限状态
 */
export function updatePermissionStatus(permissionId, status) {
  return request({
    url: `/accountant/as/archivePermission/updateStatus/${permissionId}`,
    method: 'put',
    params: { status }
  })
}

/**
 * 批量更新权限状态
 */
export function batchUpdatePermissionStatus(permissionIds, status) {
  return request({
    url: '/accountant/as/archivePermission/batchUpdateStatus',
    method: 'put',
    params: { status },
    data: permissionIds
  })
}

// ==================== 权限继承和委托 ====================

/**
 * 继承父权限
 */
export function inheritParentPermissions(parentPermissionId, childSubjectId) {
  return request({
    url: '/accountant/as/archivePermission/inherit',
    method: 'post',
    params: { parentPermissionId, childSubjectId }
  })
}

/**
 * 委托权限
 */
export function delegatePermission(permissionId, delegatorId, delegateId, effectiveTime, expiryTime) {
  return request({
    url: '/accountant/as/archivePermission/delegate',
    method: 'post',
    params: { permissionId, delegatorId, delegateId, effectiveTime, expiryTime }
  })
}

/**
 * 取消权限委托
 */
export function cancelPermissionDelegation(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/cancelDelegation/${permissionId}`,
    method: 'post'
  })
}

/**
 * 获取委托权限列表
 */
export function getDelegatedPermissions(delegateId) {
  return request({
    url: '/accountant/as/archivePermission/getDelegatedPermissions',
    method: 'get',
    params: { delegateId }
  })
}

// ==================== 权限层次管理 ====================

/**
 * 获取父权限
 */
export function getParentPermission(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/getParent/${permissionId}`,
    method: 'get'
  })
}

/**
 * 获取子权限列表
 */
export function getChildPermissions(parentPermissionId) {
  return request({
    url: `/accountant/as/archivePermission/getChildren/${parentPermissionId}`,
    method: 'get'
  })
}

/**
 * 获取权限树结构
 */
export function getPermissionTree(rootPermissionId) {
  return request({
    url: `/accountant/as/archivePermission/getTree/${rootPermissionId}`,
    method: 'get'
  })
}

/**
 * 获取权限路径
 */
export function getPermissionPath(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/getPath/${permissionId}`,
    method: 'get'
  })
}

// ==================== 权限审批操作 ====================

/**
 * 提交权限审批
 */
export function submitPermissionApproval(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/submitApproval/${permissionId}`,
    method: 'post'
  })
}

/**
 * 审批权限
 */
export function approvePermission(permissionId, approvalComment) {
  return request({
    url: `/accountant/as/archivePermission/approve/${permissionId}`,
    method: 'post',
    params: { approvalComment }
  })
}

/**
 * 拒绝权限
 */
export function rejectPermission(permissionId, rejectionReason) {
  return request({
    url: `/accountant/as/archivePermission/reject/${permissionId}`,
    method: 'post',
    params: { rejectionReason }
  })
}

/**
 * 取消权限审批
 */
export function cancelPermissionApproval(permissionId) {
  return request({
    url: `/accountant/as/archivePermission/cancelApproval/${permissionId}`,
    method: 'post'
  })
}

/**
 * 分页查询待审批权限
 */
export function getPendingApprovalPage(current, size) {
  return request({
    url: '/accountant/as/archivePermission/pendingApprovalPage',
    method: 'post',
    params: { current, size }
  })
}

// ==================== 统计分析操作 ====================

/**
 * 统计权限总数
 */
export function countPermissions() {
  return request({
    url: '/accountant/as/archivePermission/statistics/count',
    method: 'get'
  })
}

/**
 * 按权限类型统计
 */
export function countByPermissionType() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countByType',
    method: 'get'
  })
}

/**
 * 按权限级别统计
 */
export function countByPermissionLevel() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countByLevel',
    method: 'get'
  })
}

/**
 * 按权限状态统计
 */
export function countByPermissionStatus() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countByStatus',
    method: 'get'
  })
}

/**
 * 按主体类型统计
 */
export function countBySubjectType() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countBySubjectType',
    method: 'get'
  })
}

/**
 * 按资源类型统计
 */
export function countByResourceType() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countByResourceType',
    method: 'get'
  })
}

/**
 * 按访问级别统计
 */
export function countByAccessLevel() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countByAccessLevel',
    method: 'get'
  })
}

/**
 * 按风险等级统计
 */
export function countByRiskLevel() {
  return request({
    url: '/accountant/as/archivePermission/statistics/countByRiskLevel',
    method: 'get'
  })
}

/**
 * 获取权限趋势分析
 */
export function getPermissionTrend(startTime, endTime, granularity = 'day') {
  return request({
    url: '/accountant/as/archivePermission/statistics/trend',
    method: 'get',
    params: { startTime, endTime, granularity }
  })
}

/**
 * 获取用户活动统计
 */
export function getUserActivityStatistics(startTime, endTime) {
  return request({
    url: '/accountant/as/archivePermission/statistics/userActivity',
    method: 'get',
    params: { startTime, endTime }
  })
}

/**
 * 获取热门权限排行
 */
export function getPopularPermissions(limit = 10) {
  return request({
    url: '/accountant/as/archivePermission/statistics/popularPermissions',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取活跃用户排行
 */
export function getActiveUsers(limit = 10) {
  return request({
    url: '/accountant/as/archivePermission/statistics/activeUsers',
    method: 'get',
    params: { limit }
  })
}

// ==================== 系统维护操作 ====================

/**
 * 获取系统概览
 */
export function getSystemOverview() {
  return request({
    url: '/accountant/as/archivePermission/system/overview',
    method: 'get'
  })
}

/**
 * 生成权限报告
 */
export function generatePermissionReport(reportType, startTime, endTime) {
  return request({
    url: '/accountant/as/archivePermission/system/generateReport',
    method: 'post',
    params: { reportType, startTime, endTime }
  })
}

/**
 * 检查系统健康
 */
export function checkSystemHealth() {
  return request({
    url: '/accountant/as/archivePermission/system/health',
    method: 'get'
  })
}

/**
 * 权限质量评估
 */
export function assessPermissionQuality() {
  return request({
    url: '/accountant/as/archivePermission/system/assessQuality',
    method: 'get'
  })
}

/**
 * 执行维护任务
 */
export function executeMaintenanceTask(taskType, taskParams) {
  return request({
    url: '/accountant/as/archivePermission/system/maintenance',
    method: 'post',
    params: { taskType },
    data: taskParams
  })
}

/**
 * 获取维护任务状态
 */
export function getMaintenanceTaskStatus(taskId) {
  return request({
    url: `/accountant/as/archivePermission/system/maintenanceStatus/${taskId}`,
    method: 'get'
  })
}

/**
 * 清理过期权限
 */
export function cleanupExpiredPermissions() {
  return request({
    url: '/accountant/as/archivePermission/system/cleanupExpired',
    method: 'post'
  })
}

/**
 * 清理无效权限
 */
export function cleanupInvalidPermissions() {
  return request({
    url: '/accountant/as/archivePermission/system/cleanupInvalid',
    method: 'post'
  })
}

/**
 * 优化权限存储
 */
export function optimizePermissionStorage() {
  return request({
    url: '/accountant/as/archivePermission/system/optimize',
    method: 'post'
  })
}

/**
 * 备份权限数据
 */
export function backupPermissionData(backupPath) {
  return request({
    url: '/accountant/as/archivePermission/system/backup',
    method: 'post',
    params: { backupPath }
  })
}

/**
 * 恢复权限数据
 */
export function restorePermissionData(backupPath) {
  return request({
    url: '/accountant/as/archivePermission/system/restore',
    method: 'post',
    params: { backupPath }
  })
}

// ==================== 工具函数 ====================

/**
 * 格式化权限类型
 */
export function formatPermissionType(type) {
  const typeMap = {
    RESOURCE: '资源权限',
    OPERATION: '操作权限',
    DATA: '数据权限',
    FUNCTION: '功能权限'
  }
  return typeMap[type] || type
}

/**
 * 格式化权限级别
 */
export function formatPermissionLevel(level) {
  const levelMap = {
    SYSTEM: '系统级',
    MODULE: '模块级',
    RESOURCE: '资源级',
    RECORD: '记录级'
  }
  return levelMap[level] || level
}

/**
 * 格式化权限状态
 */
export function formatPermissionStatus(status) {
  const statusMap = {
    ACTIVE: '激活',
    INACTIVE: '停用',
    SUSPENDED: '暂停',
    EXPIRED: '过期',
    REVOKED: '撤销'
  }
  return statusMap[status] || status
}

/**
 * 格式化审批状态
 */
export function formatApprovalStatus(status) {
  const statusMap = {
    PENDING: '待审批',
    APPROVED: '已审批',
    REJECTED: '已拒绝',
    CANCELLED: '已取消'
  }
  return statusMap[status] || status
}

/**
 * 格式化访问级别
 */
export function formatAccessLevel(level) {
  const levelMap = {
    PUBLIC: '公开',
    INTERNAL: '内部',
    CONFIDENTIAL: '机密',
    SECRET: '秘密',
    TOP_SECRET: '绝密'
  }
  return levelMap[level] || level
}

/**
 * 格式化风险等级
 */
export function formatRiskLevel(level) {
  const levelMap = {
    LOW: '低风险',
    MEDIUM: '中风险',
    HIGH: '高风险',
    CRITICAL: '严重风险'
  }
  return levelMap[level] || level
}

/**
 * 格式化主体类型
 */
export function formatSubjectType(type) {
  const typeMap = {
    USER: '用户',
    ROLE: '角色',
    GROUP: '用户组',
    DEPARTMENT: '部门',
    ORGANIZATION: '组织'
  }
  return typeMap[type] || type
}

/**
 * 格式化资源类型
 */
export function formatResourceType(type) {
  const typeMap = {
    ARCHIVE: '档案',
    DOCUMENT: '文档',
    FOLDER: '文件夹',
    CATEGORY: '分类',
    SYSTEM: '系统'
  }
  return typeMap[type] || type
}

/**
 * 获取权限状态颜色
 */
export function getPermissionStatusColor(status) {
  const colorMap = {
    ACTIVE: 'success',
    INACTIVE: 'info',
    SUSPENDED: 'warning',
    EXPIRED: 'danger',
    REVOKED: 'danger'
  }
  return colorMap[status] || 'info'
}

/**
 * 获取风险等级颜色
 */
export function getRiskLevelColor(level) {
  const colorMap = {
    LOW: 'success',
    MEDIUM: 'warning',
    HIGH: 'danger',
    CRITICAL: 'danger'
  }
  return colorMap[level] || 'info'
}

/**
 * 获取访问级别颜色
 */
export function getAccessLevelColor(level) {
  const colorMap = {
    PUBLIC: 'success',
    INTERNAL: 'primary',
    CONFIDENTIAL: 'warning',
    SECRET: 'danger',
    TOP_SECRET: 'danger'
  }
  return colorMap[level] || 'info'
}

// ==================== 快捷操作 ====================

/**
 * 快速创建用户权限
 */
export function quickCreateUserPermission(userId, resourceType, resourceId, operations) {
  return grantPermission('USER', userId, resourceType, resourceId, operations)
}

/**
 * 快速创建角色权限
 */
export function quickCreateRolePermission(roleId, resourceType, resourceId, operations) {
  return grantPermission('ROLE', roleId, resourceType, resourceId, operations)
}

/**
 * 快速检查用户是否有读权限
 */
export function quickCheckUserReadPermission(userId, resourceType, resourceId) {
  return checkUserPermission(userId, resourceType, resourceId, 'read')
}

/**
 * 快速检查用户是否有写权限
 */
export function quickCheckUserWritePermission(userId, resourceType, resourceId) {
  return checkUserPermission(userId, resourceType, resourceId, 'write')
}

/**
 * 快速检查用户是否有删除权限
 */
export function quickCheckUserDeletePermission(userId, resourceType, resourceId) {
  return checkUserPermission(userId, resourceType, resourceId, 'delete')
}

/**
 * 快速激活权限
 */
export function quickActivatePermission(permissionId) {
  return updatePermissionStatus(permissionId, 'ACTIVE')
}

/**
 * 快速停用权限
 */
export function quickDeactivatePermission(permissionId) {
  return updatePermissionStatus(permissionId, 'INACTIVE')
}

/**
 * 快速暂停权限
 */
export function quickSuspendPermission(permissionId) {
  return updatePermissionStatus(permissionId, 'SUSPENDED')
}

/**
 * 快速撤销权限
 */
export function quickRevokePermission(permissionId) {
  return updatePermissionStatus(permissionId, 'REVOKED')
}
