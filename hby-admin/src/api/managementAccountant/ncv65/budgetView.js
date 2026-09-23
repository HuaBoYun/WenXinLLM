/**
 * NCV65全面预算系统 - 预算查阅API
 * 
 * @description 预算查阅功能API接口，包含预算查阅、版本查询、预算桌面等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module budgetView
 */

import request from '@/utils/request'

// ==================== 预算查阅权限API ====================

/**
 * 创建预算查阅权限
 * @param {Object} data 权限数据
 * @returns {Promise} 请求结果
 */
export function createBudgetViewPermission(data) {
  return request({
    url: '/glkj/accountant/budget/view/permission',
    method: 'post',
    data
  })
}

/**
 * 查询预算查阅权限详情
 * @param {String} permissionId 权限ID
 * @returns {Promise} 请求结果
 */
export function getBudgetViewPermission(permissionId) {
  return request({
    url: `/glkj/accountant/budget/view/permission/${permissionId}`,
    method: 'get'
  })
}

/**
 * 更新预算查阅权限
 * @param {String} permissionId 权限ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetViewPermission(permissionId, data) {
  return request({
    url: `/glkj/accountant/budget/view/permission/${permissionId}`,
    method: 'put',
    data
  })
}

/**
 * 删除预算查阅权限
 * @param {String} permissionId 权限ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetViewPermission(permissionId) {
  return request({
    url: `/glkj/accountant/budget/view/permission/${permissionId}`,
    method: 'delete'
  })
}

/**
 * 分页查询预算查阅权限列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetViewPermissionPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/view/permission/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 检查用户查阅权�?
 * @param {String} userId 用户ID
 * @param {String} budgetId 预算ID
 * @param {String} viewType 查阅类型
 * @returns {Promise} 请求结果
 */
export function checkUserViewPermission(userId, budgetId, viewType) {
  return request({
    url: '/glkj/accountant/budget/view/permission/check',
    method: 'get',
    params: { userId, budgetId, viewType }
  })
}

/**
 * 获取用户可查阅的预算列表
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function getUserViewableBudgets(userId) {
  return request({
    url: `/glkj/accountant/budget/view/permission/user/${userId}/budgets`,
    method: 'get'
  })
}

// ==================== 预算查阅API ====================

/**
 * 查阅预算任务
 * @param {String} taskId 任务ID
 * @param {Object} params 查阅参数
 * @returns {Promise} 请求结果
 */
export function viewBudgetTask(taskId, params) {
  return request({
    url: `/glkj/accountant/budget/view/task/${taskId}`,
    method: 'get',
    params
  })
}

/**
 * 查阅预算数据
 * @param {String} dataId 数据ID
 * @param {Object} params 查阅参数
 * @returns {Promise} 请求结果
 */
export function viewBudgetData(dataId, params) {
  return request({
    url: `/glkj/accountant/budget/view/data/${dataId}`,
    method: 'get',
    params
  })
}

/**
 * 查阅预算报表
 * @param {String} reportId 报表ID
 * @param {Object} params 查阅参数
 * @returns {Promise} 请求结果
 */
export function viewBudgetReport(reportId, params) {
  return request({
    url: `/glkj/accountant/budget/view/report/${reportId}`,
    method: 'get',
    params
  })
}

/**
 * 查阅预算分析
 * @param {String} analysisId 分析ID
 * @param {Object} params 查阅参数
 * @returns {Promise} 请求结果
 */
export function viewBudgetAnalysis(analysisId, params) {
  return request({
    url: `/glkj/accountant/budget/view/analysis/${analysisId}`,
    method: 'get',
    params
  })
}

/**
 * 分页查询可查阅的预算任务
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getViewableBudgetTaskPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/view/task/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 分页查询可查阅的预算数据
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getViewableBudgetDataPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/view/data/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

// ==================== 预算查阅记录API ====================

/**
 * 记录预算查阅日志
 * @param {Object} data 查阅日志数据
 * @returns {Promise} 请求结果
 */
export function logBudgetView(data) {
  return request({
    url: '/glkj/accountant/budget/view/log',
    method: 'post',
    data
  })
}

/**
 * 分页查询预算查阅记录
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetViewLogPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/view/log/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 查询预算查阅记录详情
 * @param {String} logId 日志ID
 * @returns {Promise} 请求结果
 */
export function getBudgetViewLogDetail(logId) {
  return request({
    url: `/glkj/accountant/budget/view/log/${logId}`,
    method: 'get'
  })
}

/**
 * 获取用户查阅统计
 * @param {String} userId 用户ID
 * @param {Object} params 统计参数
 * @returns {Promise} 请求结果
 */
export function getUserViewStatistics(userId, params) {
  return request({
    url: `/glkj/accountant/budget/view/statistics/user/${userId}`,
    method: 'get',
    params
  })
}

// ==================== 版本管理API ====================

/**
 * 创建版本管理记录
 * @param {Object} data 版本数据
 * @returns {Promise} 请求结果
 */
export function createVersionManagement(data) {
  return request({
    url: '/glkj/accountant/budget/version',
    method: 'post',
    data
  })
}

/**
 * 查询版本管理详情
 * @param {String} versionId 版本ID
 * @returns {Promise} 请求结果
 */
export function getVersionManagement(versionId) {
  return request({
    url: `/glkj/accountant/budget/version/${versionId}`,
    method: 'get'
  })
}

/**
 * 更新版本管理记录
 * @param {String} versionId 版本ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateVersionManagement(versionId, data) {
  return request({
    url: `/glkj/accountant/budget/version/${versionId}`,
    method: 'put',
    data
  })
}

/**
 * 删除版本管理记录
 * @param {String} versionId 版本ID
 * @returns {Promise} 请求结果
 */
export function deleteVersionManagement(versionId) {
  return request({
    url: `/glkj/accountant/budget/version/${versionId}`,
    method: 'delete'
  })
}

/**
 * 分页查询版本管理列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getVersionManagementPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/version/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 版本对比
 * @param {String} sourceVersionId 源版本ID
 * @param {String} targetVersionId 目标版本ID
 * @returns {Promise} 请求结果
 */
export function compareVersions(sourceVersionId, targetVersionId) {
  return request({
    url: '/glkj/accountant/budget/version/compare',
    method: 'post',
    data: { sourceVersionId, targetVersionId }
  })
}

/**
 * 版本回滚
 * @param {String} versionId 版本ID
 * @returns {Promise} 请求结果
 */
export function rollbackVersion(versionId) {
  return request({
    url: `/glkj/accountant/budget/version/${versionId}/rollback`,
    method: 'post'
  })
}

// ==================== 预算桌面API ====================

/**
 * 获取预算桌面配置
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function getDesktopConfig(userId) {
  return request({
    url: `/glkj/accountant/budget/desktop/config/${userId}`,
    method: 'get'
  })
}

/**
 * 更新预算桌面配置
 * @param {String} userId 用户ID
 * @param {Object} data 配置数据
 * @returns {Promise} 请求结果
 */
export function updateDesktopConfig(userId, data) {
  return request({
    url: `/glkj/accountant/budget/desktop/config/${userId}`,
    method: 'put',
    data
  })
}

/**
 * 获取预算桌面数据
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function getDesktopData(userId) {
  return request({
    url: `/glkj/accountant/budget/desktop/data/${userId}`,
    method: 'get'
  })
}

/**
 * 获取预算桌面统计
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function getDesktopStatistics(userId) {
  return request({
    url: `/glkj/accountant/budget/desktop/statistics/${userId}`,
    method: 'get'
  })
}

/**
 * 获取预算桌面快捷操作
 * @param {String} userId 用户ID
 * @returns {Promise} 请求结果
 */
export function getDesktopQuickActions(userId) {
  return request({
    url: `/glkj/accountant/budget/desktop/quick-actions/${userId}`,
    method: 'get'
  })
}

/**
 * 添加预算桌面快捷操作
 * @param {String} userId 用户ID
 * @param {Object} action 快捷操作数据
 * @returns {Promise} 请求结果
 */
export function addDesktopQuickAction(userId, action) {
  return request({
    url: `/glkj/accountant/budget/desktop/quick-actions/${userId}`,
    method: 'post',
    data: action
  })
}

/**
 * 删除预算桌面快捷操作
 * @param {String} userId 用户ID
 * @param {String} actionId 操作ID
 * @returns {Promise} 请求结果
 */
export function removeDesktopQuickAction(userId, actionId) {
  return request({
    url: `/glkj/accountant/budget/desktop/quick-actions/${userId}/${actionId}`,
    method: 'delete'
  })
}
