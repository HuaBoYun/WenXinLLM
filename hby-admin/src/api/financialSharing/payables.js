/*
 * @Description: 财务共享 - 应付管理模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 应付单据管理 API ====================

/**
 * 分页查询应付单据列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPayableDocumentPage(data) {
  return request({
    url: '/cwgxAi/payables/document/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新应付单据
 * @param {Object} data 单据数据
 * @returns {Promise}
 */
export function saveOrUpdatePayableDocument(data) {
  return request({
    url: '/cwgxAi/payables/document/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取应付单据详情
 * @param {String} documentId 单据ID
 * @returns {Promise}
 */
export function getPayableDocumentDetail(documentId) {
  return request({
    url: `/cwgxAi/payables/document/detail/${documentId}`,
    method: 'get'
  })
}

/**
 * 删除应付单据
 * @param {String} documentId 单据ID
 * @returns {Promise}
 */
export function deletePayableDocument(documentId) {
  return request({
    url: `/cwgxAi/payables/document/${documentId}`,
    method: 'delete'
  })
}

/**
 * 审核应付单据
 * @param {String} documentId 单据ID
 * @param {Object} auditData 审核数据
 * @returns {Promise}
 */
export function auditPayableDocument(documentId, auditData) {
  return request({
    url: `/cwgxAi/payables/document/${documentId}/audit`,
    method: 'post',
    data: transData(auditData)
  })
}

// ==================== 付款管理 API ====================

/**
 * 分页查询付款单列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPaymentOrderPage(data) {
  return request({
    url: '/cwgxAi/ap/payment/order/page',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存或更新付款单
 * @param {Object} data 付款单数据
 * @returns {Promise}
 */
export function saveOrUpdatePaymentOrder(data) {
  return request({
    url: '/cwgxAi/ap/payment/order/save',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 付款核销
 * @param {Object} data 核销数据
 * @returns {Promise}
 */
export function writeOffPayment(data) {
  return request({
    url: '/cwgxAi/ap/payment/order/writeoff',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取可核销应付列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getWriteOffablePayables(params) {
  return request({
    url: '/cwgxAi/payables/payment/write-offable',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 付款申请
 * @param {Object} data 申请数据
 * @returns {Promise}
 */
export function applyPayment(data) {
  return request({
    url: '/cwgxAi/payables/payment/apply',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 付款审批
 * @param {String} paymentId 付款单ID
 * @param {Object} approvalData 审批数据
 * @returns {Promise}
 */
export function approvePayment(paymentId, approvalData) {
  return request({
    url: `/cwgxAi/payables/payment/${paymentId}/approve`,
    method: 'post',
    data: transData(approvalData)
  })
}

// ==================== 供应商管理 API ====================

/**
 * 分页查询供应商列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getSupplierPage(data) {
  return request({
    url: '/cwgxAi/payables/supplier/getList',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存或更新供应商信息
 * @param {Object} data 供应商数据
 * @returns {Promise}
 */
export function saveOrUpdateSupplier(data) {
  return request({
    url: '/cwgxAi/payables/supplier/saveOrUpdate',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取供应商应付汇总
 * @param {String} supplierId 供应商ID
 * @returns {Promise}
 */
export function getSupplierPayableSummary(supplierId) {
  return request({
    url: `/cwgxAi/payables/supplier/${supplierId}/summary`,
    method: 'get'
  })
}

/**
 * 获取供应商详情
 * @param {String} supplierId 供应商ID
 * @returns {Promise}
 */
export function getSupplierDetail(supplierId) {
  return request({
    url: `/cwgxAi/payables/supplier/detail/${supplierId}`,
    method: 'get'
  })
}

/**
 * 获取供应商信用信息
 * @param {String} supplierId 供应商ID
 * @returns {Promise}
 */
export function getSupplierCreditInfo(supplierId) {
  return request({
    url: `/cwgxAi/payables/supplier/${supplierId}/credit`,
    method: 'get'
  })
}

// ==================== 账龄分析 API ====================

/**
 * 获取应付账龄分析数据
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getPayableAgingAnalysis(params) {
  return request({
    url: '/cwgxAi/payables/aging/analysis',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取应付账龄分析明细
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPayableAgingDetail(params) {
  return request({
    url: '/cwgxAi/payables/aging/detail',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出应付账龄分析报表
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportPayableAgingAnalysis(params) {
  return request({
    url: '/cwgxAi/payables/aging/export',
    method: 'post',
    data: transData(params),
    responseType: 'blob'
  })
}

// ==================== 预付款管理 API ====================

/**
 * 分页查询预付款列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPrepaymentPage(data) {
  return request({
    url: '/cwgxAi/payables/prepayment/page',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 查询预付款详情
 * @param {String} prepaymentId 预付款ID
 * @returns {Promise}
 */
export function getPrepaymentDetail(prepaymentId) {
  return request({
    url: `/cwgxAi/payables/prepayment/${prepaymentId}`,
    method: 'get'
  })
}

/**
 * 预付款冲销
 * @param {Object} data 冲销数据 {prepaymentId, documentId, offsetAmount}
 * @returns {Promise}
 */
export function offsetPrepayment(data) {
  return request({
    url: '/cwgxAi/payables/prepayment/offset',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取可冲销预付款列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getOffsetablePrepayments(params) {
  return request({
    url: '/cwgxAi/payables/prepayment/offsetable',
    method: 'get',
    params: transData(params)
  })
}

// ==================== 统计分析 API ====================

/**
 * 获取应付统计概览
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getPayableStatistics(params = {}) {
  return request({
    url: '/cwgxAi/payables/statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取供应商应付排名
 * @param {Object} params 排名参数
 * @returns {Promise}
 */
export function getSupplierPayableRanking(params) {
  return request({
    url: '/cwgxAi/payables/statistics/supplier-ranking',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取付款效率分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getPaymentEfficiencyAnalysis(params) {
  return request({
    url: '/cwgxAi/payables/statistics/payment-efficiency',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取应付趋势分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getPayableTrendAnalysis(params) {
  return request({
    url: '/cwgxAi/payables/statistics/trend-analysis',
    method: 'get',
    params: transData(params)
  })
}

// ==================== 报表管理 API ====================

/**
 * 获取应付明细账
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPayableDetailReport(params) {
  return request({
    url: '/cwgxAi/payables/reports/detail',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取应付余额表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPayableBalanceReport(params) {
  return request({
    url: '/cwgxAi/payables/reports/balance',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取付款明细表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPaymentDetailReport(params) {
  return request({
    url: '/cwgxAi/payables/reports/payment-detail',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出应付报表
 * @param {String} reportType 报表类型
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportPayableReport(reportType, params) {
  return request({
    url: `/cwgxAi/payables/reports/export/${reportType}`,
    method: 'post',
    data: transData(params),
    responseType: 'blob'
  })
}

// ==================== 账期管理 API ====================

/**
 * 分页查询账期设置列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPaymentTermsPage(data) {
  return request({
    url: '/cwgxAi/payables/payment-terms/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新账期设置
 * @param {Object} data 账期数据
 * @returns {Promise}
 */
export function saveOrUpdatePaymentTerms(data) {
  return request({
    url: '/cwgxAi/payables/payment-terms/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取账期设置详情
 * @param {String} termsId 账期ID
 * @returns {Promise}
 */
export function getPaymentTermsDetail(termsId) {
  return request({
    url: `/cwgxAi/payables/payment-terms/${termsId}`,
    method: 'get'
  })
}

/**
 * 删除账期设置
 * @param {String} termsId 账期ID
 * @returns {Promise}
 */
export function deletePaymentTerms(termsId) {
  return request({
    url: `/cwgxAi/payables/payment-terms/${termsId}`,
    method: 'delete'
  })
}

/**
 * 批量更新账期状态
 * @param {Object} data {termsIds: [], status: 0/1}
 * @returns {Promise}
 */
export function batchUpdatePaymentTermsStatus(data) {
  return request({
    url: '/cwgxAi/payables/payment-terms/batch/status',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量删除账期
 * @param {Object} data {termsIds: []}
 * @returns {Promise}
 */
export function batchDeletePaymentTerms(data) {
  return request({
    url: '/cwgxAi/payables/payment-terms/batch/delete',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取到期提醒列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDueReminders(params) {
  return request({
    url: '/cwgxAi/payables/payment-terms/due-reminders',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取逾期统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getOverdueStatistics(params) {
  return request({
    url: '/cwgxAi/payables/payment-terms/overdue-statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 创建付款计划
 * @param {Object} data 计划数据
 * @returns {Promise}
 */
export function createPaymentPlan(data) {
  return request({
    url: '/cwgxAi/payables/payment-terms/payment-plan',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取付款计划列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPaymentPlanList(params) {
  return request({
    url: '/cwgxAi/payables/payment-terms/payment-plan/list',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 更新付款计划状态
 * @param {String} planId 计划ID
 * @param {Object} data 状态数据
 * @returns {Promise}
 */
export function updatePaymentPlanStatus(planId, data) {
  return request({
    url: `/cwgxAi/payables/payment-terms/payment-plan/${planId}/status`,
    method: 'put',
    data: transData(data)
  })
}

// ==================== 票据管理 API ====================

/**
 * 分页查询应付票据列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPayableBillsPage(data) {
  return request({
    url: '/cwgxAi/financial/payables/bills/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新应付票据
 * @param {Object} data 票据数据
 * @returns {Promise}
 */
export function saveOrUpdatePayableBill(data) {
  return request({
    url: '/cwgxAi/financial/payables/bills/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取应付票据详情
 * @param {String} billId 票据ID
 * @returns {Promise}
 */
export function getPayableBillDetail(billId) {
  return request({
    url: `/cwgxAi/financial/payables/bills/${billId}`,
    method: 'get'
  })
}

/**
 * 删除应付票据
 * @param {String} billId 票据ID
 * @returns {Promise}
 */
export function deletePayableBill(billId) {
  return request({
    url: `/cwgxAi/financial/payables/bills/${billId}`,
    method: 'delete'
  })
}

/**
 * 作废应付票据
 * @param {String} billId 票据ID
 * @param {String} remarks 作废原因
 * @returns {Promise}
 */
export function cancelPayableBill(billId, remarks) {
  return request({
    url: `/cwgxAi/financial/payables/bills/${billId}/cancel`,
    method: 'post',
    data: transData({ remarks })
  })
}

/**
 * 票据背书转让
 * @param {String} billId 票据ID
 * @param {Object} data 背书数据
 * @returns {Promise}
 */
export function endorseBill(billId, data) {
  return request({
    url: `/cwgxAi/financial/payables/bills/${billId}/endorse`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 票据兑付处理
 * @param {String} billId 票据ID
 * @param {Object} data 兑付数据
 * @returns {Promise}
 */
export function payBill(billId, data) {
  return request({
    url: `/cwgxAi/financial/payables/bills/${billId}/pay`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取背书列表
 * @returns {Promise}
 */
export function getEndorseList() {
  return request({
    url: '/cwgxAi/financial/payables/bills/endorse/list',
    method: 'get'
  })
}

/**
 * 审批背书
 * @param {String} endorseId 背书ID
 * @param {Boolean} approved 是否通过
 * @param {String} comments 审批意见
 * @returns {Promise}
 */
export function approveEndorse(endorseId, approved, comments) {
  return request({
    url: `/cwgxAi/financial/payables/bills/endorse/${endorseId}/approve`,
    method: 'post',
    data: transData({ approved, comments })
  })
}

/**
 * 撤销背书
 * @param {String} endorseId 背书ID
 * @returns {Promise}
 */
export function cancelEndorse(endorseId) {
  return request({
    url: `/cwgxAi/financial/payables/bills/endorse/${endorseId}/cancel`,
    method: 'post'
  })
}

/**
 * 获取兑付列表
 * @returns {Promise}
 */
export function getPaymentList() {
  return request({
    url: '/cwgxAi/financial/payables/bills/payment/list',
    method: 'get'
  })
}

/**
 * 确认兑付
 * @param {String} paymentId 兑付ID
 * @param {Number} actualAmount 实际到账金额
 * @returns {Promise}
 */
export function confirmPayment(paymentId, actualAmount) {
  return request({
    url: `/cwgxAi/financial/payables/bills/payment/${paymentId}/confirm`,
    method: 'post',
    data: transData({ actualAmount })
  })
}

/**
 * 获取票据到期提醒
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getBillDueReminders(params) {
  return request({
    url: '/cwgxAi/financial/payables/bills/due-reminders',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取票据统计信息
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getBillStatistics(params) {
  return request({
    url: '/cwgxAi/financial/payables/bills/statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 票据审核
 * @param {String} billId 票据ID
 * @param {Object} auditData 审核数据
 * @returns {Promise}
 */
export function auditPayableBill(billId, auditData) {
  return request({
    url: `/cwgxAi/financial/payables/bills/${billId}/audit`,
    method: 'post',
    data: transData(auditData)
  })
}

/**
 * 批量票据操作
 * @param {String} operation 操作类型
 * @param {Array} billIds 票据ID列表
 * @param {Object} data 操作数据
 * @returns {Promise}
 */
export function batchBillOperation(operation, billIds, data = {}) {
  return request({
    url: `/cwgxAi/financial/payables/bills/batch/${operation}`,
    method: 'post',
    data: transData({ billIds, ...data })
  })
}

// ==================== 核销记录管理 API ====================

/**
 * 分页查询核销记录列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getWriteOffRecordPage(data) {
  return request({
    url: '/cwgxAi/payables/write-off/page',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 查询核销记录详情
 * @param {String} writeOffId 核销ID
 * @returns {Promise}
 */
export function getWriteOffRecordDetail(writeOffId) {
  return request({
    url: `/cwgxAi/payables/write-off/${writeOffId}`,
    method: 'get'
  })
}

/**
 * 撤销核销
 * @param {Object} data 撤销数据 {writeOffId, writeOffNo}
 * @returns {Promise}
 */
export function reverseWriteOff(data) {
  return request({
    url: '/cwgxAi/payables/write-off/reverse',
    method: 'post',
    data: JSON.stringify(transData(data)),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 应付分析增强 API ====================

/**
 * 获取账龄分析数据(图表)
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAgingAnalysisChart(data) {
  return request({
    url: '/cwgxAi/financial/payables/statistics/aging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取现金流预测数据
 * @param {Object} data 预测参数
 * @returns {Promise}
 */
export function getCashFlowForecastData(data) {
  return request({
    url: '/cwgxAi/financial/payables/statistics/cash-flow-forecast',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}
