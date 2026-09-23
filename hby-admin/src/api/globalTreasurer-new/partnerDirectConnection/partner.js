import request from '@/utils/request'

/**
 * 财资公共模块 - 合作伙伴管理 API
 */

// ==================== 合作伙伴档案管理 ====================
export function getPartnerArchivePage(params) {
  return request({
    url: '/qqsk/settlement/partner-archive/page',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getPartnerArchiveDetail(id) {
  return request({ url: `/qqsk/settlement/partner-archive/${id}`, method: 'get' })
}

export function addPartnerArchive(params) {
  return request({
    url: '/qqsk/settlement/partner-archive',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updatePartnerArchive(params) {
  return request({
    url: '/qqsk/settlement/partner-archive',
    method: 'put',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deletePartnerArchive(id) {
  return request({ url: `/qqsk/settlement/partner-archive/${id}`, method: 'delete' })
}

export function batchDeletePartnerArchive(ids) {
  return request({
    url: '/qqsk/settlement/partner-archive/batch',
    method: 'delete',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function togglePartnerArchive(id, isEnabled) {
  return request({
    url: '/qqsk/settlement/partner-archive/toggle',
    method: 'put',
    data: { id, isEnabled },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updatePartnerArchiveRiskLevel(id, riskLevel) {
  return request({
    url: '/qqsk/settlement/partner-archive/update-risk-level',
    method: 'put',
    data: { id, riskLevel },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updatePartnerArchiveCreditRating(id, creditRating) {
  return request({
    url: '/qqsk/settlement/partner-archive/update-credit-rating',
    method: 'put',
    data: { id, creditRating },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getPartnerArchiveStatistics() {
  return request({ url: '/qqsk/settlement/partner-archive/statistics', method: 'get' })
}

export function getPartnerTypes() {
  return request({ url: '/qqsk/settlement/partner-archive/partner-types', method: 'get' })
}

export function exportPartnerArchive(params) {
  return request({
    url: '/qqsk/settlement/partner-archive/export',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

// ==================== 合作伙伴类型管理 ====================
export function getPartnerTypePage(params) {
  return request({
    url: '/qqsk/settlement/partner-type/page',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getPartnerTypeDetail(partnerTypeId) {
  return request({
    url: '/qqsk/settlement/partner-type/getDetail',
    method: 'post',
    data: { partnerTypeId },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function addPartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updatePartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deletePartnerType(partnerTypeId) {
  return request({
    url: '/qqsk/settlement/partner-type/delete',
    method: 'post',
    data: { partnerTypeId },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchDeletePartnerType(ids) {
  return request({
    url: '/qqsk/settlement/partner-type/batchDelete',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function togglePartnerType(partnerTypeId, isEnabled) {
  return request({
    url: '/qqsk/settlement/partner-type/toggle',
    method: 'post',
    data: { partnerTypeId, isEnabled },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updatePartnerTypeSortOrder(sortList) {
  return request({
    url: '/qqsk/settlement/partner-type/updateSortOrder',
    method: 'post',
    data: { sortList },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getPartnerTypeStatistics() {
  return request({
    url: '/qqsk/settlement/partner-type/getStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportPartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/export',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getAllEnabledPartnerTypes() {
  return request({
    url: '/qqsk/settlement/partner-type/getAllEnabled',
    method: 'post',
    headers: { 'Content-Type': 'application/json' }
  })
}

// ==================== 合作伙伴风险管理 ====================
export function getPartnerRiskPage(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/page',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getPartnerRiskDetail(id) {
  return request({ url: `/qqsk/settlement/partner-risk/${id}`, method: 'get' })
}

export function addPartnerRisk(params) {
  return request({
    url: '/qqsk/settlement/partner-risk',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updatePartnerRisk(params) {
  return request({
    url: '/qqsk/settlement/partner-risk',
    method: 'put',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deletePartnerRisk(id) {
  return request({ url: `/qqsk/settlement/partner-risk/${id}`, method: 'delete' })
}

export function batchDeletePartnerRisk(ids) {
  return request({
    url: '/qqsk/settlement/partner-risk/batch',
    method: 'delete',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchAssessPartnerRisk(partners) {
  return request({
    url: '/qqsk/settlement/partner-risk/batch-assess',
    method: 'post',
    data: { partners },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getPartnerRiskStatistics() {
  return request({ url: '/qqsk/settlement/partner-risk/statistics', method: 'get' })
}

export function getPartnerRiskAlerts() {
  return request({ url: '/qqsk/settlement/partner-risk/alerts', method: 'get' })
}

export function updatePartnerRiskStatus(id, riskStatus) {
  return request({
    url: '/qqsk/settlement/partner-risk/update-status',
    method: 'put',
    data: { id, riskStatus },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportPartnerRisk(params) {
  return request({
    url: '/qqsk/settlement/partner-risk/export',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getRiskLevels() {
  return request({ url: '/qqsk/settlement/partner-risk/risk-levels', method: 'get' })
}

export function getAssessmentTypes() {
  return request({ url: '/qqsk/settlement/partner-risk/assessment-types', method: 'get' })
}

