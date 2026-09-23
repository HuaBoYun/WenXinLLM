/*
 * @Date: 2024-01-01 10:00:00
 * @LastEditors: AI Assistant
 * @LastEditTime: 2024-01-01 10:00:00
 * @FilePath: /hby-admin/src/api/financialSharing/reports.js
 * @Description: 报表分析模块API接口
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 财务报表 API ====================

/**
 * 获取财务报表列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFinancialReportsList(params) {
  return request({
    url: '/cwgxAi/reports/financialReports',
    method: 'get',
    params
  })
}

/**
 * 生成资产负债表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateBalanceSheet(data) {
  return request({
    url: '/cwgxAi/reports/balanceSheet',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成利润表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateIncomeStatement(data) {
  return request({
    url: '/cwgxAi/reports/incomeStatement',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成现金流量表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateCashFlowStatement(data) {
  return request({
    url: '/cwgxAi/reports/cashFlowStatement',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取资产负债表详情
 * @param {String} reportId 报表ID
 * @returns {Promise}
 */
export function getBalanceSheetDetail(reportId) {
  return request({
    url: `/cwgxAi/reports/balanceSheet/${reportId}`,
    method: 'get'
  })
}

/**
 * 获取利润表详情
 * @param {String} reportId 报表ID
 * @returns {Promise}
 */
export function getIncomeStatementDetail(reportId) {
  return request({
    url: `/cwgxAi/reports/incomeStatement/${reportId}`,
    method: 'get'
  })
}

/**
 * 获取现金流量表详情
 * @param {String} reportId 报表ID
 * @returns {Promise}
 */
export function getCashFlowStatementDetail(reportId) {
  return request({
    url: `/cwgxAi/reports/cashFlowStatement/${reportId}`,
    method: 'get'
  })
}

/**
 * 删除财务报表
 * @param {String} reportId 报表ID
 * @returns {Promise}
 */
export function deleteFinancialReport(reportId) {
  return request({
    url: `/cwgxAi/reports/financialReport/${reportId}`,
    method: 'delete'
  })
}

// ==================== 业务分析 API ====================

/**
 * 获取业务分析数据
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getBusinessAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/businessAnalysis',
    method: 'get',
    params
  })
}

/**
 * 获取成本分析数据
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getCostAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/costAnalysis',
    method: 'get',
    params
  })
}

/**
 * 获取收入分析数据
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getRevenueAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/revenueAnalysis',
    method: 'get',
    params
  })
}

/**
 * 获取财务指标分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getFinancialIndicators(params) {
  return request({
    url: '/cwgxAi/reports/financialIndicators',
    method: 'get',
    params
  })
}

/**
 * 获取经营分析数据
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getOperationalAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/operationalAnalysis',
    method: 'get',
    params
  })
}

/**
 * 获取盈利能力分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getProfitabilityAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/profitabilityAnalysis',
    method: 'get',
    params
  })
}

/**
 * 获取偿债能力分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getSolvencyAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/solvencyAnalysis',
    method: 'get',
    params
  })
}

/**
 * 获取营运能力分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getOperatingCapabilityAnalysis(params) {
  return request({
    url: '/cwgxAi/reports/operatingCapabilityAnalysis',
    method: 'get',
    params
  })
}

// ==================== 数据可视化 API ====================

/**
 * 获取仪表板列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDashboardList(params) {
  return request({
    url: '/cwgxAi/reports/dashboards',
    method: 'get',
    params
  })
}

/**
 * 获取仪表板详情
 * @param {String} dashboardId 仪表板ID
 * @returns {Promise}
 */
export function getDashboardDetail(dashboardId) {
  return request({
    url: `/cwgxAi/reports/dashboard/${dashboardId}`,
    method: 'get'
  })
}

/**
 * 创建仪表板
 * @param {Object} data 仪表板数据
 * @returns {Promise}
 */
export function createDashboard(data) {
  return request({
    url: '/cwgxAi/reports/dashboard',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新仪表板
 * @param {String} dashboardId 仪表板ID
 * @param {Object} data 仪表板数据
 * @returns {Promise}
 */
export function updateDashboard(dashboardId, data) {
  return request({
    url: `/cwgxAi/reports/dashboard/${dashboardId}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除仪表板
 * @param {String} dashboardId 仪表板ID
 * @returns {Promise}
 */
export function deleteDashboard(dashboardId) {
  return request({
    url: `/cwgxAi/reports/dashboard/${dashboardId}`,
    method: 'delete'
  })
}

/**
 * 获取图表数据
 * @param {Object} params 图表参数
 * @returns {Promise}
 */
export function getChartData(params) {
  return request({
    url: '/cwgxAi/reports/chartData',
    method: 'get',
    params
  })
}

/**
 * 获取趋势图数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTrendChartData(params) {
  return request({
    url: '/cwgxAi/reports/trendChart',
    method: 'get',
    params
  })
}

/**
 * 获取结构分析图数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getStructureChartData(params) {
  return request({
    url: '/cwgxAi/reports/structureChart',
    method: 'get',
    params
  })
}

/**
 * 获取对比分析图数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getComparisonChartData(params) {
  return request({
    url: '/cwgxAi/reports/comparisonChart',
    method: 'get',
    params
  })
}

// ==================== 报表管理 API ====================

/**
 * 获取报表模板列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportTemplateList(params) {
  return request({
    url: '/cwgxAi/reports/templates',
    method: 'get',
    params
  })
}

/**
 * 获取报表模板详情
 * @param {String} templateId 模板ID
 * @returns {Promise}
 */
export function getReportTemplateDetail(templateId) {
  return request({
    url: `/cwgxAi/reports/template/${templateId}`,
    method: 'get'
  })
}

/**
 * 创建报表模板
 * @param {Object} data 模板数据
 * @returns {Promise}
 */
export function createReportTemplate(data) {
  return request({
    url: '/cwgxAi/reports/template',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新报表模板
 * @param {String} templateId 模板ID
 * @param {Object} data 模板数据
 * @returns {Promise}
 */
export function updateReportTemplate(templateId, data) {
  return request({
    url: `/cwgxAi/reports/template/${templateId}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除报表模板
 * @param {String} templateId 模板ID
 * @returns {Promise}
 */
export function deleteReportTemplate(templateId) {
  return request({
    url: `/cwgxAi/reports/template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 获取定时任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getScheduledTaskList(params) {
  return request({
    url: '/cwgxAi/reports/scheduledTasks',
    method: 'get',
    params
  })
}

/**
 * 创建定时任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function createScheduledTask(data) {
  return request({
    url: '/cwgxAi/reports/scheduledTask',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新定时任务
 * @param {String} taskId 任务ID
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function updateScheduledTask(taskId, data) {
  return request({
    url: `/cwgxAi/reports/scheduledTask/${taskId}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除定时任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function deleteScheduledTask(taskId) {
  return request({
    url: `/cwgxAi/reports/scheduledTask/${taskId}`,
    method: 'delete'
  })
}

/**
 * 启用/禁用定时任务
 * @param {String} taskId 任务ID
 * @param {Boolean} enabled 是否启用
 * @returns {Promise}
 */
export function toggleScheduledTask(taskId, enabled) {
  return request({
    url: `/cwgxAi/reports/scheduledTask/${taskId}/toggle`,
    method: 'put',
    data: transData({ enabled })
  })
}

/**
 * 手动执行定时任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function executeScheduledTask(taskId) {
  return request({
    url: `/cwgxAi/reports/scheduledTask/${taskId}/execute`,
    method: 'post'
  })
}

// ==================== 报表导出和分发 API ====================

/**
 * 导出报表
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportReport(data) {
  return request({
    url: '/cwgxAi/reports/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 批量导出报表
 * @param {Object} data 批量导出参数
 * @returns {Promise}
 */
export function batchExportReports(data) {
  return request({
    url: '/cwgxAi/reports/batchExport',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 获取报表分发列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportDistributionList(params) {
  return request({
    url: '/cwgxAi/reports/distributions',
    method: 'get',
    params
  })
}

/**
 * 创建报表分发
 * @param {Object} data 分发数据
 * @returns {Promise}
 */
export function createReportDistribution(data) {
  return request({
    url: '/cwgxAi/reports/distribution',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新报表分发
 * @param {String} distributionId 分发ID
 * @param {Object} data 分发数据
 * @returns {Promise}
 */
export function updateReportDistribution(distributionId, data) {
  return request({
    url: `/cwgxAi/reports/distribution/${distributionId}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除报表分发
 * @param {String} distributionId 分发ID
 * @returns {Promise}
 */
export function deleteReportDistribution(distributionId) {
  return request({
    url: `/cwgxAi/reports/distribution/${distributionId}`,
    method: 'delete'
  })
}

// ==================== 统计和监控 API ====================

/**
 * 获取报表统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportStatistics(params) {
  return request({
    url: '/cwgxAi/reports/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取报表使用情况
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportUsage(params) {
  return request({
    url: '/cwgxAi/reports/usage',
    method: 'get',
    params
  })
}

/**
 * 获取报表性能监控
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportPerformance(params) {
  return request({
    url: '/cwgxAi/reports/performance',
    method: 'get',
    params
  })
}

/**
 * 获取报表错误日志
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportErrorLogs(params) {
  return request({
    url: '/cwgxAi/reports/errorLogs',
    method: 'get',
    params
  })
}
