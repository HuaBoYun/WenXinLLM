/*
 * @Description: 财务共享 - 试算平衡模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 试算平衡核心 API ====================

/**
 * 生成试算平衡表
 * @param {Object} data 试算平衡参数
 * @returns {Promise}
 */
export function generateTrialBalance(data) {
  return request({
    url: '/cwgxAi/trial-balance/generate',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 试算平衡校验
 * @param {Object} data 试算平衡参数
 * @returns {Promise}
 */
export function checkBalance(data) {
  return request({
    url: '/cwgxAi/trial-balance/check',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 异步生成试算平衡表
 * @param {Object} data 试算平衡参数
 * @returns {Promise}
 */
export function asyncGenerateTrialBalance(data) {
  return request({
    url: '/cwgxAi/trial-balance/async',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询试算平衡进度
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getTrialBalanceProgress(taskId) {
  return request({
    url: `/cwgxAi/trial-balance/progress/${taskId}`,
    method: 'get'
  })
}

/**
 * 获取历史试算平衡记录
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTrialBalanceHistory(params) {
  return request({
    url: '/cwgxAi/trial-balance/history',
    method: 'get',
    params
  })
}

/**
 * 删除试算平衡记录
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function deleteTrialBalance(taskId) {
  return request({
    url: `/cwgxAi/trial-balance/${taskId}`,
    method: 'delete'
  })
}

// ==================== 试算平衡报告 API ====================

/**
 * 生成平衡报告
 * @param {Object} data 报告参数
 * @returns {Promise}
 */
export function generateBalanceReport(data) {
  return request({
    url: '/cwgxAi/trial-balance/report',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出试算平衡表
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportTrialBalance(data) {
  return request({
    url: '/cwgxAi/trial-balance/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 辅助查询 API ====================

/**
 * 验证试算平衡参数
 * @param {Object} data 参数数据
 * @returns {Promise}
 */
export function validateTrialBalanceParameters(data) {
  return request({
    url: '/cwgxAi/trial-balance/validate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取可用的会计期间列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAvailablePeriods(params) {
  return request({
    url: '/cwgxAi/trial-balance/periods',
    method: 'get',
    params
  })
}

/**
 * 获取试算平衡配置
 * @returns {Promise}
 */
export function getTrialBalanceConfig() {
  return request({
    url: '/cwgxAi/trial-balance/config',
    method: 'get'
  })
}

// ==================== 试算平衡参数构造函数 ====================

/**
 * 构造试算平衡参数
 * @param {Object} options 参数选项
 * @returns {Object} 试算平衡参数
 */
export function buildTrialBalanceParam(options = {}) {
  return {
    // 基础参数
    bookId: options.bookId || null,
    tenantId: options.tenantId || null,
    accountingPeriod: options.accountingPeriod || '',
    startPeriod: options.startPeriod || '',
    endPeriod: options.endPeriod || '',

    // 科目过滤参数
    subjectType: options.subjectType || 0,
    subjectLevel: options.subjectLevel || 0,
    subjectIds: options.subjectIds || [],
    excludeSubjectIds: options.excludeSubjectIds || [],

    // 数据范围参数
    includeUnposted: options.includeUnposted || false,
    includeZeroBalance: options.includeZeroBalance !== false,
    includeDetailSubjects: options.includeDetailSubjects !== false,
    includeAuxiliary: options.includeAuxiliary || false,

    // 校验参数
    checkOpeningBalance: options.checkOpeningBalance !== false,
    checkTransactionAmount: options.checkTransactionAmount !== false,
    checkClosingBalance: options.checkClosingBalance !== false,
    checkAccumulatedAmount: options.checkAccumulatedAmount || false,

    // 精度和容差
    amountPrecision: options.amountPrecision || 2,
    tolerance: options.tolerance || 0.01,
    showDifference: options.showDifference !== false,
    showBalanceRate: options.showBalanceRate !== false,

    // 报告参数
    reportFormat: options.reportFormat || 'standard',
    generateHtmlReport: options.generateHtmlReport || false,
    generateExcelReport: options.generateExcelReport !== false,
    generatePdfReport: options.generatePdfReport || false,
    reportTemplate: options.reportTemplate || 'default',

    // 验证级别
    validationLevel: options.validationLevel || 'basic',
    checkContinuity: options.checkContinuity !== false,
    checkCrossReference: options.checkCrossReference || false,
    checkLogic: options.checkLogic !== false,

    // 备注
    remark: options.remark || ''
  }
}

// ==================== 批量操作 API ====================

/**
 * 批量生成多个期间的试算平衡表
 * @param {Object} data 批量参数
 * @returns {Promise}
 */
export function batchGenerateTrialBalance(data) {
  return request({
    url: '/cwgxAi/trial-balance/batch/generate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取批量任务状态
 * @param {String} batchId 批量任务ID
 * @returns {Promise}
 */
export function getBatchTaskStatus(batchId) {
  return request({
    url: `/cwgxAi/trial-balance/batch/status/${batchId}`,
    method: 'get'
  })
}

/**
 * 取消批量任务
 * @param {String} batchId 批量任务ID
 * @returns {Promise}
 */
export function cancelBatchTask(batchId) {
  return request({
    url: `/cwgxAi/trial-balance/batch/cancel/${batchId}`,
    method: 'post'
  })
}

// ==================== 统计分析 API ====================

/**
 * 获取试算平衡统计概览
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTrialBalanceStatistics(params) {
  return request({
    url: '/cwgxAi/trial-balance/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取科目余额分布统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSubjectBalanceDistribution(params) {
  return request({
    url: '/cwgxAi/trial-balance/statistics/distribution',
    method: 'get',
    params
  })
}

/**
 * 获取试算平衡趋势数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTrialBalanceTrend(params) {
  return request({
    url: '/cwgxAi/trial-balance/statistics/trend',
    method: 'get',
    params
  })
}