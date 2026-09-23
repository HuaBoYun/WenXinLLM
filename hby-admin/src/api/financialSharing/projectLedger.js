/*
 * @Description: 财务共享 - 财务项目台账模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 财务项目管理 API ====================

/**
 * 分页查询财务项目列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getProjectLedgerPage(data) {
  return request({
    url: '/cwgxAi/transaction/project/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新财务项目
 * @param {Object} data 项目数据
 * @returns {Promise}
 */
export function saveOrUpdateProjectLedger(data) {
  return request({
    url: '/cwgxAi/transaction/project/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取财务项目详情
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectLedgerById(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/${projectId}`,
    method: 'get'
  })
}

/**
 * 删除财务项目
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function deleteProjectLedger(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/${projectId}`,
    method: 'delete'
  })
}

/**
 * 批量删除财务项目
 * @param {Array} projectIds 项目ID列表
 * @returns {Promise}
 */
export function batchDeleteProjectLedgers(projectIds) {
  return request({
    url: '/cwgxAi/transaction/project/batch',
    method: 'delete',
    data: projectIds
  })
}

// ==================== 项目数据收集 API ====================

/**
 * 项目数据收集
 * @param {Object} data 收集参数
 * @returns {Promise}
 */
export function collectProjectData(data) {
  return request({
    url: '/cwgxAi/transaction/project/collect',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取项目数据收集状态
 * @param {String} collectId 收集任务ID
 * @returns {Promise}
 */
export function getCollectStatus(collectId) {
  return request({
    url: `/cwgxAi/transaction/project/collect/status/${collectId}`,
    method: 'get'
  })
}

/**
 * 停止项目数据收集
 * @param {String} collectId 收集任务ID
 * @returns {Promise}
 */
export function stopProjectDataCollect(collectId) {
  return request({
    url: `/cwgxAi/transaction/project/collect/stop/${collectId}`,
    method: 'post'
  })
}

/**
 * 重新启动项目数据收集
 * @param {String} collectId 收集任务ID
 * @returns {Promise}
 */
export function restartProjectDataCollect(collectId) {
  return request({
    url: `/cwgxAi/transaction/project/collect/restart/${collectId}`,
    method: 'post'
  })
}

/**
 * 获取项目数据收集日志
 * @param {String} collectId 收集任务ID
 * @returns {Promise}
 */
export function getProjectDataCollectLogs(collectId) {
  return request({
    url: `/cwgxAi/transaction/project/collect/logs/${collectId}`,
    method: 'get'
  })
}

// ==================== 项目统计分析 API ====================

/**
 * 获取项目统计信息
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectStatistics(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/statistics/${projectId}`,
    method: 'get'
  })
}

/**
 * 获取项目预算执行情况
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectBudgetExecution(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/budget-execution/${projectId}`,
    method: 'get'
  })
}

/**
 * 获取项目成本分析
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectCostAnalysis(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/cost-analysis/${projectId}`,
    method: 'get'
  })
}

/**
 * 获取项目收入分析
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectRevenueAnalysis(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/revenue-analysis/${projectId}`,
    method: 'get'
  })
}

/**
 * 获取项目利润分析
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectProfitAnalysis(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/profit-analysis/${projectId}`,
    method: 'get'
  })
}

/**
 * 获取项目现金流分析
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectCashFlowAnalysis(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/cash-flow-analysis/${projectId}`,
    method: 'get'
  })
}

// ==================== 项目报表 API ====================

/**
 * 获取项目财务报表
 * @param {Number} projectId 项目ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProjectFinancialReport(projectId, params) {
  return request({
    url: `/cwgxAi/transaction/project/financial-report/${projectId}`,
    method: 'get',
    params
  })
}

/**
 * 获取项目成本报表
 * @param {Number} projectId 项目ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProjectCostReport(projectId, params) {
  return request({
    url: `/cwgxAi/transaction/project/cost-report/${projectId}`,
    method: 'get',
    params
  })
}

/**
 * 获取项目收入报表
 * @param {Number} projectId 项目ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProjectRevenueReport(projectId, params) {
  return request({
    url: `/cwgxAi/transaction/project/revenue-report/${projectId}`,
    method: 'get',
    params
  })
}

/**
 * 获取项目预算报表
 * @param {Number} projectId 项目ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProjectBudgetReport(projectId, params) {
  return request({
    url: `/cwgxAi/transaction/project/budget-report/${projectId}`,
    method: 'get',
    params
  })
}

// ==================== 项目数据导入导出 API ====================

/**
 * 导出项目数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportProjectData(data) {
  return request({
    url: '/cwgxAi/transaction/project/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导入项目数据
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importProjectData(formData) {
  return request({
    url: '/cwgxAi/transaction/project/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载项目数据导入模板
 * @returns {Promise}
 */
export function downloadProjectImportTemplate() {
  return request({
    url: '/cwgxAi/transaction/project/import-template',
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 项目配置管理 API ====================

/**
 * 获取项目配置
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectConfig(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/config/${projectId}`,
    method: 'get'
  })
}

/**
 * 保存项目配置
 * @param {Number} projectId 项目ID
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function saveProjectConfig(projectId, data) {
  return request({
    url: `/cwgxAi/transaction/project/config/${projectId}`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 重置项目配置
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function resetProjectConfig(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/config/${projectId}/reset`,
    method: 'post'
  })
}

// ==================== 项目权限管理 API ====================

/**
 * 获取项目权限列表
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectPermissions(projectId) {
  return request({
    url: `/cwgxAi/transaction/project/permissions/${projectId}`,
    method: 'get'
  })
}

/**
 * 保存项目权限
 * @param {Number} projectId 项目ID
 * @param {Object} data 权限数据
 * @returns {Promise}
 */
export function saveProjectPermissions(projectId, data) {
  return request({
    url: `/cwgxAi/transaction/project/permissions/${projectId}`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 检查项目权限
 * @param {Number} projectId 项目ID
 * @param {String} permission 权限代码
 * @returns {Promise}
 */
export function checkProjectPermission(projectId, permission) {
  return request({
    url: `/cwgxAi/transaction/project/permissions/${projectId}/check`,
    method: 'get',
    params: { permission }
  })
}
