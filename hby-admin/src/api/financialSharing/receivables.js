/*
 * @Description: 财务共享 - 应收管理模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 应收单据管理 API ====================

/**
 * 分页查询应收单据列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getReceivableDocumentPage(data) {
  return request({
    url: '/cwgxAi/receivables/document/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新应收单据
 * @param {Object} data 单据数据
 * @returns {Promise}
 */
export function saveOrUpdateReceivableDocument(data) {
  return request({
    url: '/cwgxAi/receivables/document/saveOrUpdate',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取应收单据详情
 * @param {String} documentId 单据ID
 * @returns {Promise}
 */
export function getReceivableDocumentDetail(documentId) {
  return request({
    url: `/cwgxAi/receivables/document/${documentId}`,
    method: 'get'
  })
}

/**
 * 删除应收单据
 * @param {String} documentId 单据ID
 * @returns {Promise}
 */
export function deleteReceivableDocument(documentId) {
  return request({
    url: `/cwgxAi/receivables/document/${documentId}`,
    method: 'delete'
  })
}

/**
 * 审核应收单据
 * @param {String} documentId 单据ID
 * @param {Object} auditData 审核数据
 * @returns {Promise}
 */
export function auditReceivableDocument(documentId, auditData) {
  return request({
    url: `/cwgxAi/receivables/document/${documentId}/audit`,
    method: 'post',
    data: transData(auditData)
  })
}

// ==================== 收款管理 API ====================

/**
 * 分页查询收款单列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPaymentReceiptPage(data) {
  return request({
    url: '/cwgxAi/receivables/payment/getList',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存或更新收款单
 * @param {Object} data 收款单数据
 * @returns {Promise}
 */
export function saveOrUpdatePaymentReceipt(data) {
  return request({
    url: '/cwgxAi/receivables/payment/saveOrUpdate',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 收款核销
 * @param {Object} data 核销数据
 * @returns {Promise}
 */
export function writeOffPayment(data) {
  return request({
    url: '/cwgxAi/receivables/payment/write-off',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量核销收款单
 * @param {Object} data 批量核销数据 { receiptIds: [], operatorId: '', remark: '' }
 * @returns {Promise}
 */
export function batchWriteOffPayment(data) {
  return request({
    url: '/cwgxAi/ar/receipt/batchWriteOff',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 确认收款
 * @param {String} receiptId 收款单ID
 * @param {Object} data 确认数据 { confirmAmount, confirmBy }
 * @returns {Promise}
 */
export function confirmPaymentReceipt(receiptId, data) {
  return request({
    url: `/cwgxAi/ar/receipt/confirm/${receiptId}`,
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取可核销应收列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getWriteOffableReceivables(params) {
  return request({
    url: '/cwgxAi/receivables/payment/write-offable',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 分页查询核销记录列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getWriteOffPage(data) {
  return request({
    url: '/cwgxAi/ar/writeOff/page',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分页查询预收款列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAdvanceReceiptPage(data) {
  return request({
    url: '/cwgxAi/ar/advanceReceipt/page',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 客户管理 API ====================

/**
 * 分页查询客户列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCustomerPage(data) {
  return request({
    url: '/cwgxAi/receivables/customer/getList',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存或更新客户信息
 * @param {Object} data 客户数据
 * @returns {Promise}
 */
export function saveOrUpdateCustomer(data) {
  return request({
    url: '/cwgxAi/receivables/customer/saveOrUpdate',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取客户详情
 * @param {String} customerId 客户ID
 * @returns {Promise}
 */
export function getCustomerDetail(customerId) {
  return request({
    url: `/cwgxAi/receivables/customer/detail/${customerId}`,
    method: 'get'
  })
}

/**
 * 获取客户应收汇总
 * @param {String} customerId 客户ID
 * @returns {Promise}
 */
export function getCustomerReceivableSummary(customerId) {
  return request({
    url: `/cwgxAi/receivables/customer/${customerId}/summary`,
    method: 'get'
  })
}

/**
 * 获取客户信用信息
 * @param {String} customerId 客户ID
 * @returns {Promise}
 */
export function getCustomerCreditInfo(customerId) {
  return request({
    url: `/cwgxAi/receivables/customer/${customerId}/credit`,
    method: 'get'
  })
}

// ==================== 账龄分析 API ====================

/**
 * 获取账龄分析数据（按账龄区间统计）
 * @param {Object} params 分析参数 { analysisDate, tenantId }
 * @returns {Promise}
 */
export function getAgingAnalysis(params) {
  return request({
    url: '/cwgxAi/receivables/aging/analysis',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取账龄分析明细
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAgingAnalysisDetail(data) {
  return request({
    url: '/cwgxAi/receivables/aging/details',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取逾期统计
 * @param {Object} params { analysisDate, tenantId }
 * @returns {Promise}
 */
export function getOverdueStatistics(params) {
  return request({
    url: '/cwgxAi/receivables/aging/overdueStatistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取风险评估
 * @param {Object} params { analysisDate, tenantId }
 * @returns {Promise}
 */
export function getRiskAssessment(params) {
  return request({
    url: '/cwgxAi/receivables/aging/riskAssessment',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 按客户统计账龄
 * @param {Object} params { analysisDate, tenantId }
 * @returns {Promise}
 */
export function getAgingByCustomer(params) {
  return request({
    url: '/cwgxAi/receivables/aging/byCustomer',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 查询账龄趋势
 * @param {Object} params { startDate, endDate, tenantId }
 * @returns {Promise}
 */
export function getAgingTrend(params) {
  return request({
    url: '/cwgxAi/receivables/aging/trend',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 生成账龄快照
 * @param {Object} data { analysisDate, tenantId }
 * @returns {Promise}
 */
export function generateAgingSnapshot(data) {
  return request({
    url: '/cwgxAi/receivables/aging/generateSnapshot',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 查询最新快照日期
 * @param {Object} params { tenantId }
 * @returns {Promise}
 */
export function getLatestSnapshotDate(params) {
  return request({
    url: '/cwgxAi/receivables/aging/latestSnapshotDate',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出账龄分析报表
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportAgingAnalysis(data) {
  return request({
    url: '/cwgxAi/receivables/aging/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 坏账管理 API ====================

/**
 * 分页查询坏账列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBadDebtPage(data) {
  return request({
    url: '/cwgxAi/receivables/bad-debt/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计提坏账准备
 * @param {Object} data 计提数据
 * @returns {Promise}
 */
export function provideBadDebt(data) {
  return request({
    url: '/cwgxAi/receivables/bad-debt/provide',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 坏账核销
 * @param {Object} data 核销数据
 * @returns {Promise}
 */
export function writeOffBadDebt(data) {
  return request({
    url: '/cwgxAi/receivables/bad-debt/write-off',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分页查询坏账准备列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBadDebtProvisionPage(data) {
  return request({
    url: '/cwgxAi/ar/badDebt/provision/page',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分页查询坏账核销列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBadDebtWriteOffPage(data) {
  return request({
    url: '/cwgxAi/ar/badDebt/writeOff/page',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分页查询坏账回收列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBadDebtRecoveryPage(data) {
  return request({
    url: '/cwgxAi/ar/badDebt/recovery/page',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 统计分析 API ====================

/**
 * 获取应收统计概览
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getReceivableStatistics(params = {}) {
  return request({
    url: '/cwgxAi/receivables/statistics',
    method: 'get',
    params: transData(params)
  })
}

// ==================== 应收分析 API ====================

/**
 * 获取应收总览数据
 * @param {Object} params 查询参数 { tenantId }
 * @returns {Promise}
 */
export function getReceivableOverview(params) {
  return request({
    url: '/cwgxAi/ar/analysis/overview',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取应收结构分析
 * @param {Object} params 查询参数 { tenantId }
 * @returns {Promise}
 */
export function getReceivableStructureAnalysis(params) {
  return request({
    url: '/cwgxAi/ar/analysis/structure',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取收款效率分析
 * @param {Object} params 查询参数 { tenantId, startDate, endDate }
 * @returns {Promise}
 */
export function getCollectionEfficiency(params) {
  return request({
    url: '/cwgxAi/ar/analysis/efficiency',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取客户排名数据
 * @param {Object} params 查询参数 { tenantId, rankingType, startDate, endDate, topN }
 * @returns {Promise}
 */
export function getCustomerRankingData(params) {
  return request({
    url: '/cwgxAi/ar/analysis/ranking',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取客户应收排名
 * @param {Object} params 排名参数
 * @returns {Promise}
 */
export function getCustomerReceivableRanking(params) {
  return request({
    url: '/cwgxAi/receivables/statistics/customer-ranking',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取回款效率分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getCollectionEfficiencyAnalysis(params) {
  return request({
    url: '/cwgxAi/receivables/statistics/collection-efficiency',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取应收趋势分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getReceivableTrendAnalysis(params) {
  return request({
    url: '/cwgxAi/receivables/statistics/trend-analysis',
    method: 'get',
    params: transData(params)
  })
}

// ==================== 报表管理 API ====================

/**
 * 获取应收明细账
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReceivableDetailReport(params) {
  return request({
    url: '/cwgxAi/receivables/reports/detail',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取应收余额表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReceivableBalanceReport(params) {
  return request({
    url: '/cwgxAi/receivables/reports/balance',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出应收报表
 * @param {String} reportType 报表类型
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportReceivableReport(reportType, params) {
  return request({
    url: `/cwgxAi/receivables/reports/export/${reportType}`,
    method: 'post',
    data: transData(params),
    responseType: 'blob'
  })
}
