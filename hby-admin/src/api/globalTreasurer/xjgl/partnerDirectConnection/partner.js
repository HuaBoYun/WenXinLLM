import request from '@/utils/request'

// 合作伙伴档案管理 API
const prefix = '/qqsk/settlement/partner-archive'
const typePrefix = '/qqsk/settlement/partner-type'

// 获取合作伙伴档案列表
export function getPartnerArchiveList(query) {
  return request({
    url: `${prefix}/page`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: query
  })
}

// 获取合作伙伴档案详情
export function getPartnerArchiveDetail(id) {
  return request({
    url: `${prefix}/${id}`,
    method: 'get'
  })
}

// 创建合作伙伴档案
export function createPartnerArchive(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 更新合作伙伴档案
export function updatePartnerArchive(data) {
  return request({
    url: `${prefix}`,
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 删除合作伙伴档案
export function deletePartnerArchive(id) {
  return request({
    url: `${prefix}/${id}`,
    method: 'delete'
  })
}

// 批量删除合作伙伴档案
export function batchDeletePartnerArchive(ids) {
  return request({
    url: `${prefix}/batch`,
    method: 'delete',
    data: { ids }
  })
}

// 导出合作伙伴档案
export function exportPartnerArchive(query) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 导入合作伙伴档案
export function importPartnerArchive(formData) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取合作伙伴统计数据
export function getPartnerArchiveStatistics() {
  return request({
    url: `${prefix}/statistics`,
    method: 'get'
  })
}

// 获取伙伴类型列表
export function getPartnerTypeList() {
  return request({
    url: `${typePrefix}/getAllEnabled`,
    method: 'post'
  })
}

// 获取伙伴类型详情
export function getPartnerTypeDetail(id) {
  return request({
    url: `${typePrefix}/getDetail`,
    method: 'post',
    data: { partnerTypeId: id }
  })
}

// 创建伙伴类型
export function createPartnerType(data) {
  return request({
    url: `${typePrefix}/add`,
    method: 'post',
    data
  })
}

// 更新伙伴类型
export function updatePartnerType(data) {
  return request({
    url: `${typePrefix}/update`,
    method: 'post',
    data
  })
}

// 删除伙伴类型
export function deletePartnerType(id) {
  return request({
    url: `${typePrefix}/delete`,
    method: 'post',
    data: { partnerTypeId: id }
  })
}

// 启用/禁用合作伙伴
export function togglePartnerArchive(id, enabled) {
  return request({
    url: `${prefix}/toggle`,
    method: 'put',
    data: { id, isEnabled: enabled }
  })
}

// 合作伙伴状态变更
export function changePartnerStatus(id, status, reason) {
  return request({
    url: `${prefix}/toggle`,
    method: 'put',
    data: { id, status: status ? '1' : '0' }
  })
}

// 更新风险等级
export function updatePartnerRiskLevel(id, riskLevel, reason) {
  return request({
    url: `${prefix}/update-risk-level`,
    method: 'put',
    data: { id, riskLevel }
  })
}

// 更新信用评级
export function updatePartnerCreditRating(id, creditRating, reason) {
  return request({
    url: `${prefix}/update-credit-rating`,
    method: 'put',
    data: { id, creditRating }
  })
}

// 获取合作伙伴历史记录
export function getPartnerHistoryRecords(query) {
  return request({
    url: `${prefix}/page`,
    method: 'post',
    data: query
  })
}

// 添加合作伙伴历史记录
export function addPartnerHistoryRecord(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    data
  })
}

// 上传合作伙伴档案文件
export function uploadPartnerArchiveFile(formData) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 下载合作伙伴档案文件
export function downloadPartnerArchiveFile(fileId) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    data: { fileId },
    responseType: 'blob'
  })
}

// 获取合作伙伴档案文件列表
export function getPartnerArchiveFileList(partnerId) {
  return request({
    url: `${prefix}/${partnerId}`,
    method: 'get'
  })
}

// 删除合作伙伴档案文件
export function deletePartnerArchiveFile(fileId) {
  return request({
    url: `${prefix}/${fileId}`,
    method: 'delete'
  })
}

// 获取合作伙伴风险评估记录
export function getPartnerRiskAssessmentRecords(partnerId) {
  return request({
    url: `${prefix}/${partnerId}`,
    method: 'get'
  })
}

// 添加合作伙伴风险评估记录
export function addPartnerRiskAssessmentRecord(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    data
  })
}

// 获取合作伙伴联系方式列表
export function getPartnerContactList(partnerId) {
  return request({
    url: `${prefix}/${partnerId}`,
    method: 'get'
  })
}

// 添加合作伙伴联系方式
export function addPartnerContact(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    data
  })
}

// 更新合作伙伴联系方式
export function updatePartnerContact(data) {
  return request({
    url: `${prefix}`,
    method: 'put',
    data
  })
}

// 删除合作伙伴联系方式
export function deletePartnerContact(id) {
  return request({
    url: `${prefix}/${id}`,
    method: 'delete'
  })
}

// 获取合作伙伴银行账户列表
export function getPartnerBankAccountList(partnerId) {
  return request({
    url: `${prefix}/${partnerId}`,
    method: 'get'
  })
}

// 添加合作伙伴银行账户
export function addPartnerBankAccount(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    data
  })
}

// 更新合作伙伴银行账户
export function updatePartnerBankAccount(data) {
  return request({
    url: `${prefix}`,
    method: 'put',
    data
  })
}

// 删除合作伙伴银行账户
export function deletePartnerBankAccount(id) {
  return request({
    url: `${prefix}/${id}`,
    method: 'delete'
  })
}