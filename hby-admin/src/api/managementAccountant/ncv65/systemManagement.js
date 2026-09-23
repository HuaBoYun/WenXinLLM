/**
 * NCV65全面预算系统 - 系统管理API
 * 
 * @description 系统管理功能API接口，包含用户管理、权限管理、日志管理、配置管理等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module systemManagement
 */

import request from '@/utils/request'

// ==================== 用户管理API ====================

/**
 * 创建用户
 * @param {Object} data 用户数据
 * @returns {Promise} 请求结果
 */
export function createUser(data) {
  return request({
    url: '/accountant/system/user',
    method: 'post',
    data
  })
}

/**
 * 查询用户详情
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function getUser(userId) {
  return request({
    url: `/accountant/system/user/${userId}`,
    method: 'get'
  })
}

/**
 * 更新用户
 * @param {String} userId 用户ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateUser(userId, data) {
  return request({
    url: `/accountant/system/user/${userId}`,
    method: 'put',
    data
  })
}

/**
 * 删除用户
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function deleteUser(userId) {
  return request({
    url: `/accountant/system/user/${userId}`,
    method: 'delete'
  })
}

/**
 * 分页查询用户列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getUserPage(current, size, params) {
  return request({
    url: '/accountant/system/user/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 重置用户密码
 * @param {String} userId 用户ID
 * @param {String} newPassword 新密�? * @returns {Promise} 请求结果
 */
export function resetUserPassword(userId, newPassword) {
  return request({
    url: `/accountant/system/user/${userId}/reset-password`,
    method: 'post',
    data: { newPassword }
  })
}

/**
 * 启用/禁用用户
 * @param {String} userId 用户ID
 * @param {Boolean} enabled 是否启用
 * @returns {Promise} 请求结果
 */
export function toggleUserStatus(userId, enabled) {
  return request({
    url: `/accountant/system/user/${userId}/toggle-status`,
    method: 'post',
    data: { enabled }
  })
}

// ==================== 角色管理API ====================

/**
 * 创建角色
 * @param {Object} data 角色数据
 * @returns {Promise} 请求结果
 */
export function createRole(data) {
  return request({
    url: '/accountant/system/role',
    method: 'post',
    data
  })
}

/**
 * 查询角色详情
 * @param {String} roleId 角色ID
 * @returns {Promise} 请求结果
 */
export function getRole(roleId) {
  return request({
    url: `/accountant/system/role/${roleId}`,
    method: 'get'
  })
}

/**
 * 更新角色
 * @param {String} roleId 角色ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateRole(roleId, data) {
  return request({
    url: `/accountant/system/role/${roleId}`,
    method: 'put',
    data
  })
}

/**
 * 删除角色
 * @param {String} roleId 角色ID
 * @returns {Promise} 请求结果
 */
export function deleteRole(roleId) {
  return request({
    url: `/accountant/system/role/${roleId}`,
    method: 'delete'
  })
}

/**
 * 分页查询角色列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getRolePage(current, size, params) {
  return request({
    url: '/accountant/system/role/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 获取角色权限列表
 * @param {String} roleId 角色ID
 * @returns {Promise} 请求结果
 */
export function getRolePermissions(roleId) {
  return request({
    url: `/accountant/system/role/${roleId}/permissions`,
    method: 'get'
  })
}

/**
 * 分配角色权限
 * @param {String} roleId 角色ID
 * @param {Array} permissionIds 权限ID数组
 * @returns {Promise} 请求结果
 */
export function assignRolePermissions(roleId, permissionIds) {
  return request({
    url: `/accountant/system/role/${roleId}/permissions`,
    method: 'post',
    data: { permissionIds }
  })
}

// ==================== 权限管理API ====================

/**
 * 创建权限
 * @param {Object} data 权限数据
 * @returns {Promise} 请求结果
 */
export function createPermission(data) {
  return request({
    url: '/accountant/system/permission',
    method: 'post',
    data
  })
}

/**
 * 查询权限详情
 * @param {String} permissionId 权限ID
 * @returns {Promise} 请求结果
 */
export function getPermission(permissionId) {
  return request({
    url: `/accountant/system/permission/${permissionId}`,
    method: 'get'
  })
}

/**
 * 更新权限
 * @param {String} permissionId 权限ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updatePermission(permissionId, data) {
  return request({
    url: `/accountant/system/permission/${permissionId}`,
    method: 'put',
    data
  })
}

/**
 * 删除权限
 * @param {String} permissionId 权限ID
 * @returns {Promise} 请求结果
 */
export function deletePermission(permissionId) {
  return request({
    url: `/accountant/system/permission/${permissionId}`,
    method: 'delete'
  })
}

/**
 * 分页查询权限列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getPermissionPage(current, size, params) {
  return request({
    url: '/accountant/system/permission/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 获取权限树结�? * @returns {Promise} 请求结果
 */
export function getPermissionTree() {
  return request({
    url: '/accountant/system/permission/tree',
    method: 'get'
  })
}

// ==================== 日志管理API ====================

/**
 * 分页查询操作日志
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getOperationLogPage(current, size, params) {
  return request({
    url: '/accountant/system/log/operation/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 查询操作日志详情
 * @param {String} logId 日志ID
 * @returns {Promise} 请求结果
 */
export function getOperationLogDetail(logId) {
  return request({
    url: `/accountant/system/log/operation/${logId}`,
    method: 'get'
  })
}

/**
 * 分页查询登录日志
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getLoginLogPage(current, size, params) {
  return request({
    url: '/accountant/system/log/login/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 分页查询系统日志
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getSystemLogPage(current, size, params) {
  return request({
    url: '/accountant/system/log/system/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 导出日志
 * @param {Object} params 导出参数
 * @returns {Promise} 请求结果
 */
export function exportLogs(params) {
  return request({
    url: '/accountant/system/log/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ==================== 配置管理API ====================

/**
 * 获取系统配置
 * @param {String} configKey 配置�? * @returns {Promise} 请求结果
 */
export function getSystemConfig(configKey) {
  return request({
    url: `/accountant/system/config/${configKey}`,
    method: 'get'
  })
}

/**
 * 更新系统配置
 * @param {String} configKey 配置�? * @param {Object} data 配置数据
 * @returns {Promise} 请求结果
 */
export function updateSystemConfig(configKey, data) {
  return request({
    url: `/accountant/system/config/${configKey}`,
    method: 'put',
    data
  })
}

/**
 * 分页查询系统配置列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getSystemConfigPage(current, size, params) {
  return request({
    url: '/accountant/system/config/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 批量更新系统配置
 * @param {Object} configs 配置对象
 * @returns {Promise} 请求结果
 */
export function batchUpdateSystemConfig(configs) {
  return request({
    url: '/accountant/system/config/batch',
    method: 'put',
    data: configs
  })
}

/**
 * 重置系统配置
 * @param {String} configKey 配置�? * @returns {Promise} 请求结果
 */
export function resetSystemConfig(configKey) {
  return request({
    url: `/accountant/system/config/${configKey}/reset`,
    method: 'post'
  })
}
