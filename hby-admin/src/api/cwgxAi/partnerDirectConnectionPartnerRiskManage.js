import request from '@/utils/request'

/**
 * 合作伙伴风险管理 API
 */

// 获取风险评估列表
export function getRiskAssessmentList(params) {
  return request({
    url: '/cwgxAi/partner/risk/getList',
    method: 'post',
    data: params
  })
}

// 获取风险评估详情
export function getRiskAssessmentDetail(params) {
  return request({
    url: '/cwgxAi/partner/risk/getDetail',
    method: 'post',
    data: params
  })
}

// 新增风险评估
export function addRiskAssessment(params) {
  return request({
    url: '/cwgxAi/partner/risk/add',
    method: 'post',
    data: params
  })
}

// 更新风险评估
export function updateRiskAssessment(params) {
  return request({
    url: '/cwgxAi/partner/risk/update',
    method: 'post',
    data: params
  })
}

// 删除风险评估
export function deleteRiskAssessment(params) {
  return request({
    url: '/cwgxAi/partner/risk/delete',
    method: 'post',
    data: params
  })
}

// 获取风险统计信息
export function getRiskStatistics() {
  return request({
    url: '/cwgxAi/partner/risk/getStatistics',
    method: 'post'
  })
}

// 获取风险预警信息
export function getRiskAlerts() {
  return request({
    url: '/cwgxAi/partner/risk/getRiskAlerts',
    method: 'post'
  })
}

// 导出风险评估报告
export function exportRiskReport(params) {
  return request({
    url: '/cwgxAi/partner/risk/exportReport',
    method: 'post',
    data: params
  })
}

// 批量风险评估
export function batchRiskAssessment(params) {
  return request({
    url: '/cwgxAi/partner/risk/batchAssess',
    method: 'post',
    data: params
  })
}

// 批量删除风险评估
export function batchDeleteRiskAssessment(params) {
  return request({
    url: '/cwgxAi/partner/risk/batchDelete',
    method: 'post',
    data: params
  })
}

// 获取风险等级列表
export function getRiskLevels() {
  return request({
    url: '/cwgxAi/partner/risk/getRiskLevels',
    method: 'post'
  })
}

// 获取评估类型列表
export function getAssessmentTypes() {
  return request({
    url: '/cwgxAi/partner/risk/getAssessmentTypes',
    method: 'post'
  })
}

// 获取合作伙伴列表
export function getPartners() {
  return request({
    url: '/cwgxAi/partner/risk/getPartners',
    method: 'post'
  })
}

// 更新风险状态
export function updateRiskStatus(params) {
  return request({
    url: '/cwgxAi/partner/risk/updateRiskStatus',
    method: 'post',
    data: params
  })
}

// 重新评估风险
export function reassessRisk(params) {
  return request({
    url: '/cwgxAi/partner/risk/reassess',
    method: 'post',
    data: params
  })
}

// 获取风险历史记录
export function getRiskHistory(params) {
  return request({
    url: '/cwgxAi/partner/risk/getHistory',
    method: 'post',
    data: params
  })
}