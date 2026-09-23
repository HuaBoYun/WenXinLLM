import request from '@/utils/request'

// 银行接口配置管理 API
const prefix = '/qqsk/settlement/bank-interface-config'

// 获取银行接口配置列表
export function getBankInterfaceList(query) {
  return request({
    url: `${prefix}/page`,
    method: 'get',
    params: query
  })
}

// 获取银行接口配置详情
export function getBankInterfaceDetail(id) {
  return request({
    url: `${prefix}/${id}`,
    method: 'get'
  })
}

// 创建银行接口配置
export function createBankInterface(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 更新银行接口配置
export function updateBankInterface(data) {
  return request({
    url: `${prefix}`,
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 删除银行接口配置
export function deleteBankInterface(ids) {
  return request({
    url: `${prefix}/${ids}`,
    method: 'delete'
  })
}

// 批量删除银行接口配置
export function batchDeleteBankInterface(ids) {
  return request({
    url: `${prefix}/${ids.join(',')}`,
    method: 'delete'
  })
}

// 测试银行接口连通性
export function testBankInterface(id) {
  return request({
    url: `${prefix}/${id}/test-connection`,
    method: 'post'
  })
}

// 批量测试银行接口连通性
export function batchTestBankInterface(ids) {
  return request({
    url: `${prefix}/batch-test`,
    method: 'post',
    data: { ids }
  })
}

// 导出银行接口配置
export function exportBankInterface(query) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 导入银行接口配置
export function importBankInterface(formData) {
  return request({
    url: `${prefix}/import`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取接口统计数据
export function getBankInterfaceStatistics(orgId) {
  return request({
    url: `${prefix}/summary`,
    method: 'get',
    params: { orgId }
  })
}

// 获取银行列表
export function getBankList() {
  return request({
    url: `${prefix}/available`,
    method: 'get'
  })
}

// 获取接口类型列表
export function getInterfaceTypeList() {
  return request({
    url: `${prefix}/types`,
    method: 'get'
  })
}

// 启用/禁用接口
export function toggleBankInterface(id, enabled) {
  return request({
    url: `${prefix}/${enabled ? 'enable' : 'disable'}`,
    method: 'put',
    data: { configIds: [id] }
  })
}

// 获取接口测试历史
export function getInterfaceTestHistory(query) {
  return request({
    url: `${prefix}/test-history`,
    method: 'get',
    params: query
  })
}