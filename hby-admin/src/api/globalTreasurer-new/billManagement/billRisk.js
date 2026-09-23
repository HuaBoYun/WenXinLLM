/**
 * 票据风险管理 API
 * @module api/globalTreasurer-new/billManagement/billRisk
 */
import request from '@/utils/request'

/**
 * 分页查询票据风险列表
 * @param {Object} params - 查询参数
 */
export function getBillRiskPage(params) {
  return request({
    url: '/qqsk/bill/risk/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据风险详情
 * @param {string} assessmentId - 评估ID
 */
export function getBillRiskDetail(assessmentId) {
  return request({
    url: `/qqsk/bill/risk/detail/${assessmentId}`,
    method: 'post'
  })
}

/**
 * 创建风险评估
 * @param {Object} data - 评估数据
 */
export function createBillRiskAssessment(data) {
  return request({
    url: '/qqsk/bill/risk/save',
    method: 'post',
    data
  })
}

/**
 * 更新风险评估
 * @param {Object} data - 评估数据
 */
export function updateBillRiskAssessment(data) {
  return request({
    url: '/qqsk/bill/risk/update',
    method: 'post',
    data
  })
}

/**
 * 删除风险评估
 * @param {Array} assessmentIds - 评估ID数组
 */
export function deleteBillRiskAssessment(assessmentIds) {
  return request({
    url: '/qqsk/bill/risk/delete',
    method: 'post',
    data: { assessmentIds }
  })
}

/**
 * 获取风险预警列表
 * @param {Object} params - 查询参数
 */
export function getBillRiskAlerts(params) {
  return request({
    url: '/qqsk/bill/risk/alerts',
    method: 'post',
    data: params
  })
}

/**
 * 处置风险
 * @param {Object} data - 处置数据
 */
export function disposeBillRisk(data) {
  return request({
    url: '/qqsk/bill/risk/dispose',
    method: 'post',
    data
  })
}

/**
 * 标记预警为已处理
 * @param {Object} data - 预警数据
 */
export function markAlertResolved(data) {
  return request({
    url: '/qqsk/bill/risk/alert/resolve',
    method: 'post',
    data
  })
}

/**
 * 导出风险报告
 * @param {Object} params - 导出参数
 */
export function exportBillRisk(params) {
  return request({
    url: '/qqsk/bill/risk/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取风险统计数据
 * @param {Object} params - 统计参数
 */
export function getBillRiskStatistics(params) {
  return request({
    url: '/qqsk/bill/risk/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 获取风险趋势数据
 * @param {Object} params - 查询参数
 */
export function getRiskTrendData(params) {
  return request({
    url: '/qqsk/bill/risk/trend',
    method: 'post',
    data: params
  })
}

/**
 * 获取风险分布数据
 * @param {Object} params - 查询参数
 */
export function getRiskDistributionData(params) {
  return request({
    url: '/qqsk/bill/risk/distribution',
    method: 'post',
    data: params
  })
}

/**
 * 获取评估历史记录
 * @param {string|number} identifier - 票据号码或评估ID
 */
export function getRiskAssessmentHistory(identifier) {
  return request({
    url: `/qqsk/bill/risk/history/${identifier}`,
    method: 'post'
  })
}

