import request from '@/utils/request'

/**
 * 合作伙伴风险管理 API
 */

// 获取风险评估列表
export function getRiskAssessmentList(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/page',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  })
}

// 获取风险评估详情
export function getRiskAssessmentDetail(id) {
  return request({
    url: `/qqsk/settlement/partner-risk/${id}`,
    method: 'get'
  })
}

// 新增风险评估
export function addRiskAssessment(params) {
  return request({
    url: '/qqsk/settlement/partner-risk',
    method: 'post',
    data: params
  })
}

// 更新风险评估
export function updateRiskAssessment(params) {
  return request({
    url: '/qqsk/settlement/partner-risk',
    method: 'put',
    data: params
  })
}

// 删除风险评估
export function deleteRiskAssessment(id) {
  return request({
    url: `/qqsk/settlement/partner-risk/${id}`,
    method: 'delete'
  })
}

// 获取风险统计信息
export function getRiskStatistics() {
  return request({
    url: '/qqsk/settlement/partner-risk/statistics',
    method: 'get'
  })
}

// 获取风险预警信息
export function getRiskAlerts() {
  return request({
    url: '/qqsk/settlement/partner-risk/alerts',
    method: 'get'
  })
}

// 导出风险评估报告
export function exportRiskReport(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/export',
    method: 'post',
    data: params
  })
}

// 批量风险评估
export function batchRiskAssessment(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/batch-assess',
    method: 'post',
    data: params
  })
}

// 批量删除风险评估
export function batchDeleteRiskAssessment(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/batch',
    method: 'delete',
    data: params
  })
}

// 获取风险等级列表
export function getRiskLevels() {
  return request({
    url: '/qqsk/settlement/partner-risk/risk-levels',
    method: 'get'
  })
}

// 获取评估类型列表
export function getAssessmentTypes() {
  return request({
    url: '/qqsk/settlement/partner-risk/assessment-types',
    method: 'get'
  })
}

// 获取合作伙伴列表
export function getPartners() {
  return request({
    url: '/qqsk/settlement/partner-risk/partners',
    method: 'get'
  })
}

// 更新风险状态
export function updateRiskStatus(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/update-status',
    method: 'put',
    data: params
  })
}

// 重新评估风险
export function reassessRisk(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/batch-assess',
    method: 'post',
    data: params
  })
}

// 获取风险历史记录
export function getRiskHistory(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/page',
    method: 'post',
    data: params
  })
}