import request from '@/utils/request'

// 合作伙伴档案管理 API
const prefix = '/cwgxAi/xjgl/partnerDirectConnection/partner'

// 获取合作伙伴档案列表
export function getPartnerArchiveList(query) {
  return request({
    url: `${prefix}/archive/getList`,
    method: 'post',
    data: query
  })
}

// 获取合作伙伴档案详情
export function getPartnerArchiveDetail(id) {
  return request({
    url: `${prefix}/archive/getDetail`,
    method: 'post',
    data: { id }
  })
}

// 创建合作伙伴档案
export function createPartnerArchive(data) {
  return request({
    url: `${prefix}/archive/create`,
    method: 'post',
    data
  })
}

// 更新合作伙伴档案
export function updatePartnerArchive(data) {
  return request({
    url: `${prefix}/archive/update`,
    method: 'post',
    data
  })
}

// 删除合作伙伴档案
export function deletePartnerArchive(id) {
  return request({
    url: `${prefix}/archive/delete`,
    method: 'post',
    data: { id }
  })
}

// 批量删除合作伙伴档案
export function batchDeletePartnerArchive(ids) {
  return request({
    url: `${prefix}/archive/batchDelete`,
    method: 'post',
    data: { ids }
  })
}

// 导出合作伙伴档案
export function exportPartnerArchive(query) {
  return request({
    url: `${prefix}/archive/export`,
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 导入合作伙伴档案
export function importPartnerArchive(formData) {
  return request({
    url: `${prefix}/archive/import`,
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
    url: `${prefix}/archive/statistics`,
    method: 'post'
  })
}

// 获取伙伴类型列表
export function getPartnerTypeList() {
  return request({
    url: `${prefix}/type/getList`,
    method: 'post'
  })
}

// 获取伙伴类型详情
export function getPartnerTypeDetail(id) {
  return request({
    url: `${prefix}/type/getDetail`,
    method: 'post',
    data: { id }
  })
}

// 创建伙伴类型
export function createPartnerType(data) {
  return request({
    url: `${prefix}/type/create`,
    method: 'post',
    data
  })
}

// 更新伙伴类型
export function updatePartnerType(data) {
  return request({
    url: `${prefix}/type/update`,
    method: 'post',
    data
  })
}

// 删除伙伴类型
export function deletePartnerType(id) {
  return request({
    url: `${prefix}/type/delete`,
    method: 'post',
    data: { id }
  })
}

// 启用/禁用合作伙伴
export function togglePartnerArchive(id, enabled) {
  return request({
    url: `${prefix}/archive/toggle`,
    method: 'post',
    data: { id, enabled }
  })
}

// 合作伙伴状态变更
export function changePartnerStatus(id, status, reason) {
  return request({
    url: `${prefix}/archive/changeStatus`,
    method: 'post',
    data: { id, status, reason }
  })
}

// 更新风险等级
export function updatePartnerRiskLevel(id, riskLevel, reason) {
  return request({
    url: `${prefix}/archive/updateRiskLevel`,
    method: 'post',
    data: { id, riskLevel, reason }
  })
}

// 更新信用评级
export function updatePartnerCreditRating(id, creditRating, reason) {
  return request({
    url: `${prefix}/archive/updateCreditRating`,
    method: 'post',
    data: { id, creditRating, reason }
  })
}

// 获取合作伙伴历史记录
export function getPartnerHistoryRecords(query) {
  return request({
    url: `${prefix}/archive/history/records`,
    method: 'post',
    data: query
  })
}

// 添加合作伙伴历史记录
export function addPartnerHistoryRecord(data) {
  return request({
    url: `${prefix}/archive/history/add`,
    method: 'post',
    data
  })
}

// 上传合作伙伴档案文件
export function uploadPartnerArchiveFile(formData) {
  return request({
    url: `${prefix}/archive/uploadFile`,
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
    url: `${prefix}/archive/downloadFile`,
    method: 'post',
    data: { fileId },
    responseType: 'blob'
  })
}

// 获取合作伙伴档案文件列表
export function getPartnerArchiveFileList(partnerId) {
  return request({
    url: `${prefix}/archive/getFileList`,
    method: 'post',
    data: { partnerId }
  })
}

// 删除合作伙伴档案文件
export function deletePartnerArchiveFile(fileId) {
  return request({
    url: `${prefix}/archive/deleteFile`,
    method: 'post',
    data: { fileId }
  })
}

// 获取合作伙伴风险评估记录
export function getPartnerRiskAssessmentRecords(partnerId) {
  return request({
    url: `${prefix}/archive/riskAssessment/records`,
    method: 'post',
    data: { partnerId }
  })
}

// 添加合作伙伴风险评估记录
export function addPartnerRiskAssessmentRecord(data) {
  return request({
    url: `${prefix}/archive/riskAssessment/add`,
    method: 'post',
    data
  })
}

// 获取合作伙伴联系方式列表
export function getPartnerContactList(partnerId) {
  return request({
    url: `${prefix}/archive/contact/getList`,
    method: 'post',
    data: { partnerId }
  })
}

// 添加合作伙伴联系方式
export function addPartnerContact(data) {
  return request({
    url: `${prefix}/archive/contact/add`,
    method: 'post',
    data
  })
}

// 更新合作伙伴联系方式
export function updatePartnerContact(data) {
  return request({
    url: `${prefix}/archive/contact/update`,
    method: 'post',
    data
  })
}

// 删除合作伙伴联系方式
export function deletePartnerContact(id) {
  return request({
    url: `${prefix}/archive/contact/delete`,
    method: 'post',
    data: { id }
  })
}

// 获取合作伙伴银行账户列表
export function getPartnerBankAccountList(partnerId) {
  return request({
    url: `${prefix}/archive/bankAccount/getList`,
    method: 'post',
    data: { partnerId }
  })
}

// 添加合作伙伴银行账户
export function addPartnerBankAccount(data) {
  return request({
    url: `${prefix}/archive/bankAccount/add`,
    method: 'post',
    data
  })
}

// 更新合作伙伴银行账户
export function updatePartnerBankAccount(data) {
  return request({
    url: `${prefix}/archive/bankAccount/update`,
    method: 'post',
    data
  })
}

// 删除合作伙伴银行账户
export function deletePartnerBankAccount(id) {
  return request({
    url: `${prefix}/archive/bankAccount/delete`,
    method: 'post',
    data: { id }
  })
}