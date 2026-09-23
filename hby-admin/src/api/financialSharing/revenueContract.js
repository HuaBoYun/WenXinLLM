/*
 * @Description: 财务共享 - 收入合同模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 收入合同管理 API ====================

/**
 * 分页查询收入合同列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueContractPage(data) {
  return request({
    url: '/cwgxAi/transaction/contract/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 保存或更新收入合同
 * @param {Object} data 合同数据
 * @returns {Promise}
 */
export function saveOrUpdateRevenueContract(data) {
  return request({
    url: '/cwgxAi/transaction/contract/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取收入合同详情
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getRevenueContractById(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}`,
    method: 'get'
  })
}

/**
 * 删除收入合同
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function deleteRevenueContract(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}`,
    method: 'delete'
  })
}

/**
 * 批量删除收入合同
 * @param {Array} contractIds 合同ID列表
 * @returns {Promise}
 */
export function batchDeleteRevenueContracts(contractIds) {
  return request({
    url: '/cwgxAi/transaction/contract/batch',
    method: 'delete',
    data: contractIds
  })
}

/**
 * 更新合同状态
 * @param {Number} contractId 合同ID
 * @param {Number} contractStatus 合同状态
 * @returns {Promise}
 */
export function updateContractStatus(contractId, contractStatus) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}/status`,
    method: 'put',
    params: { contractStatus }
  })
}

/**
 * 批量更新合同状态
 * @param {Array} contractIds 合同ID列表
 * @param {Number} contractStatus 合同状态
 * @returns {Promise}
 */
export function batchUpdateContractStatus(contractIds, contractStatus) {
  return request({
    url: '/cwgxAi/transaction/contract/batch/status',
    method: 'put',
    data: contractIds,
    params: { contractStatus }
  })
}

// ==================== 合同履约管理 API ====================

/**
 * 合同履约处理
 * @param {Object} data 履约数据
 * @returns {Promise}
 */
export function processContractPerformance(data) {
  return request({
    url: '/cwgxAi/transaction/contract/performance',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取合同履约记录
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getContractPerformanceList(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/performance/${contractId}`,
    method: 'get'
  })
}

/**
 * 获取履约详情
 * @param {String} performanceId 履约ID
 * @returns {Promise}
 */
export function getContractPerformanceById(performanceId) {
  return request({
    url: `/cwgxAi/transaction/contract/performance/detail/${performanceId}`,
    method: 'get'
  })
}

/**
 * 更新履约状态
 * @param {String} performanceId 履约ID
 * @param {String} performanceStatus 履约状态
 * @returns {Promise}
 */
export function updatePerformanceStatus(performanceId, performanceStatus) {
  return request({
    url: `/cwgxAi/transaction/contract/performance/${performanceId}/status`,
    method: 'put',
    params: { performanceStatus }
  })
}

/**
 * 删除履约记录
 * @param {String} performanceId 履约ID
 * @returns {Promise}
 */
export function deleteContractPerformance(performanceId) {
  return request({
    url: `/cwgxAi/transaction/contract/performance/${performanceId}`,
    method: 'delete'
  })
}

// ==================== 合同统计分析 API ====================

/**
 * 获取合同统计信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractStatistics(params) {
  return request({
    url: '/cwgxAi/transaction/contract/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取合同收入确认情况
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getContractRevenueRecognition(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/revenue-recognition/${contractId}`,
    method: 'get'
  })
}

/**
 * 获取合同执行进度
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getContractExecutionProgress(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/execution-progress/${contractId}`,
    method: 'get'
  })
}

/**
 * 获取合同风险评估
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getContractRiskAssessment(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/risk-assessment/${contractId}`,
    method: 'get'
  })
}

/**
 * 获取合同盈利分析
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getContractProfitabilityAnalysis(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/profitability-analysis/${contractId}`,
    method: 'get'
  })
}

// ==================== 合同报表 API ====================

/**
 * 获取合同收入报表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractRevenueReport(params) {
  return request({
    url: '/cwgxAi/transaction/contract/revenue-report',
    method: 'get',
    params
  })
}

/**
 * 获取合同执行报表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractExecutionReport(params) {
  return request({
    url: '/cwgxAi/transaction/contract/execution-report',
    method: 'get',
    params
  })
}

/**
 * 获取合同到期提醒报表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractExpiryReminderReport(params) {
  return request({
    url: '/cwgxAi/transaction/contract/expiry-reminder-report',
    method: 'get',
    params
  })
}

/**
 * 获取合同履约报表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractPerformanceReport(params) {
  return request({
    url: '/cwgxAi/transaction/contract/performance-report',
    method: 'get',
    params
  })
}

// ==================== 合同数据导入导出 API ====================

/**
 * 导出合同数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportContractData(data) {
  return request({
    url: '/cwgxAi/transaction/contract/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导入合同数据
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importContractData(formData) {
  return request({
    url: '/cwgxAi/transaction/contract/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载合同数据导入模板
 * @returns {Promise}
 */
export function downloadContractImportTemplate() {
  return request({
    url: '/cwgxAi/transaction/contract/import-template',
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 合同审批流程 API ====================

/**
 * 提交合同审批
 * @param {Number} contractId 合同ID
 * @param {Object} data 审批数据
 * @returns {Promise}
 */
export function submitContractApproval(contractId, data) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}/submit-approval`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 审批合同
 * @param {Number} contractId 合同ID
 * @param {Object} data 审批数据
 * @returns {Promise}
 */
export function approveContract(contractId, data) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}/approve`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 拒绝合同
 * @param {Number} contractId 合同ID
 * @param {Object} data 拒绝数据
 * @returns {Promise}
 */
export function rejectContract(contractId, data) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}/reject`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 撤回合同审批
 * @param {Number} contractId 合同ID
 * @param {String} reason 撤回原因
 * @returns {Promise}
 */
export function withdrawContractApproval(contractId, reason) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}/withdraw`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 获取合同审批历史
 * @param {Number} contractId 合同ID
 * @returns {Promise}
 */
export function getContractApprovalHistory(contractId) {
  return request({
    url: `/cwgxAi/transaction/contract/${contractId}/approval-history`,
    method: 'get'
  })
}

// ==================== 合同模板管理 API ====================

/**
 * 获取合同模板列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractTemplateList(params) {
  return request({
    url: '/cwgxAi/transaction/contract/template/list',
    method: 'get',
    params
  })
}

/**
 * 保存合同模板
 * @param {Object} data 模板数据
 * @returns {Promise}
 */
export function saveContractTemplate(data) {
  return request({
    url: '/cwgxAi/transaction/contract/template/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除合同模板
 * @param {Number} templateId 模板ID
 * @returns {Promise}
 */
export function deleteContractTemplate(templateId) {
  return request({
    url: `/cwgxAi/transaction/contract/template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 根据模板创建合同
 * @param {Number} templateId 模板ID
 * @param {Object} data 合同数据
 * @returns {Promise}
 */
export function createContractFromTemplate(templateId, data) {
  return request({
    url: `/cwgxAi/transaction/contract/template/${templateId}/create-contract`,
    method: 'post',
    data: transData(data)
  })
}
