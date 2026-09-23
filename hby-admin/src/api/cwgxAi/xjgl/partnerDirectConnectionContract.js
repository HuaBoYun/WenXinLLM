import request from '@/utils/request'

// 云连接合同管理 API
const prefix = '/cwgxAi/xjgl/partnerDirectConnection/contract'

// 获取云连接合同列表
export function getCloudContractList(query) {
  return request({
    url: `${prefix}/getList`,
    method: 'post',
    data: query
  })
}

// 获取云连接合同详情
export function getCloudContractDetail(id) {
  return request({
    url: `${prefix}/getDetail`,
    method: 'post',
    data: { id }
  })
}

// 创建云连接合同
export function createCloudContract(data) {
  return request({
    url: `${prefix}/create`,
    method: 'post',
    data
  })
}

// 更新云连接合同
export function updateCloudContract(data) {
  return request({
    url: `${prefix}/update`,
    method: 'post',
    data
  })
}

// 删除云连接合同
export function deleteCloudContract(id) {
  return request({
    url: `${prefix}/delete`,
    method: 'post',
    data: { id }
  })
}

// 批量删除云连接合同
export function batchDeleteCloudContract(ids) {
  return request({
    url: `${prefix}/batchDelete`,
    method: 'post',
    data: { ids }
  })
}

// 导出云连接合同
export function exportCloudContract(query) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 导入云连接合同
export function importCloudContract(formData) {
  return request({
    url: `${prefix}/import`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取合同统计数据
export function getCloudContractStatistics() {
  return request({
    url: `${prefix}/statistics`,
    method: 'post'
  })
}

// 获取服务提供商列表
export function getServiceProviderList() {
  return request({
    url: `${prefix}/serviceProvider/getList`,
    method: 'post'
  })
}

// 获取服务类型列表
export function getServiceTypeList() {
  return request({
    url: `${prefix}/serviceType/getList`,
    method: 'post'
  })
}

// 合同续约提醒
export function getContractRenewalAlert() {
  return request({
    url: `${prefix}/renewalAlert`,
    method: 'post'
  })
}

// 合同续约操作
export function renewContract(id, renewalData) {
  return request({
    url: `${prefix}/renew`,
    method: 'post',
    data: { id, renewalData }
  })
}

// 合同终止
export function terminateContract(id, reason) {
  return request({
    url: `${prefix}/terminate`,
    method: 'post',
    data: { id, reason }
  })
}

// 合同审批
export function approveContract(id, action, comment) {
  return request({
    url: `${prefix}/approve`,
    method: 'post',
    data: { id, action, comment }
  })
}

// 上传合同文件
export function uploadContractFile(formData) {
  return request({
    url: `${prefix}/uploadFile`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 下载合同文件
export function downloadContractFile(fileId) {
  return request({
    url: `${prefix}/downloadFile`,
    method: 'post',
    data: { fileId },
    responseType: 'blob'
  })
}

// 获取合同文件列表
export function getContractFileList(contractId) {
  return request({
    url: `${prefix}/getFileList`,
    method: 'post',
    data: { contractId }
  })
}

// 删除合同文件
export function deleteContractFile(fileId) {
  return request({
    url: `${prefix}/deleteFile`,
    method: 'post',
    data: { fileId }
  })
}

// 获取合同履约记录
export function getContractPerformanceRecords(contractId) {
  return request({
    url: `${prefix}/performance/records`,
    method: 'post',
    data: { contractId }
  })
}

// 添加合同履约记录
export function addContractPerformanceRecord(data) {
  return request({
    url: `${prefix}/performance/add`,
    method: 'post',
    data
  })
}

// 启用/禁用合同
export function toggleCloudContract(id, enabled) {
  return request({
    url: `${prefix}/toggle`,
    method: 'post',
    data: { id, enabled }
  })
}

// 合同状态变更
export function changeContractStatus(id, status, reason) {
  return request({
    url: `${prefix}/changeStatus`,
    method: 'post',
    data: { id, status, reason }
  })
}