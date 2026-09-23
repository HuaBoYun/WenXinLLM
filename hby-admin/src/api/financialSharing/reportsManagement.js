/*
 * @Description: 财务共享 - 成本管理分析API
 * @Author: 星光问心AI大模型
 * @Date: 2025-12-30
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 成本管理分析 API ====================

/**
 * 获取管理概览统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getManagementOverview(params) {
  return request({
    url: '/cwgxAi/reports/management/getOverview',
    method: 'get',
    params
  })
}

/**
 * 获取成本趋势分析数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getManagementCostTrend(data) {
  return request({
    url: '/cwgxAi/reports/management/getCostTrend',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取预算执行分析数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getManagementBudgetExecution(data) {
  return request({
    url: '/cwgxAi/reports/management/getBudgetExecution',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取异常预警列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getManagementAlerts(params) {
  return request({
    url: '/cwgxAi/reports/management/getAlerts',
    method: 'get',
    params
  })
}

/**
 * 标记预警为已处理
 * @param {String} alertId 预警ID
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function resolveManagementAlert(alertId, data) {
  return request({
    url: `/cwgxAi/reports/management/alert/${alertId}/resolve`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量标记预警为已处理
 * @param {Object} data 批量处理数据
 * @returns {Promise}
 */
export function batchResolveManagementAlerts(data) {
  return request({
    url: '/cwgxAi/reports/management/alerts/batchResolve',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 导出管理分析报告
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportManagementReport(data) {
  return request({
    url: '/cwgxAi/reports/management/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}
