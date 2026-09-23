import request from '@/utils/request'

// 银行接口配置管理 API
const prefix = '/cwgxAi/xjgl/partnerDirectConnection/bank'

// 获取银行接口配置列表
export function getBankInterfaceList(query) {
  return request({
    url: `${prefix}/interface/getList`,
    method: 'post',
    data: query
  })
}

// 获取银行接口配置详情
export function getBankInterfaceDetail(id) {
  return request({
    url: `${prefix}/interface/getDetail`,
    method: 'post',
    data: { id }
  })
}

// 创建银行接口配置
export function createBankInterface(data) {
  return request({
    url: `${prefix}/interface/create`,
    method: 'post',
    data
  })
}

// 更新银行接口配置
export function updateBankInterface(data) {
  return request({
    url: `${prefix}/interface/update`,
    method: 'post',
    data
  })
}

// 删除银行接口配置
export function deleteBankInterface(id) {
  return request({
    url: `${prefix}/interface/delete`,
    method: 'post',
    data: { id }
  })
}

// 批量删除银行接口配置
export function batchDeleteBankInterface(ids) {
  return request({
    url: `${prefix}/interface/batchDelete`,
    method: 'post',
    data: { ids }
  })
}

// 测试银行接口连通性
export function testBankInterface(id) {
  return request({
    url: `${prefix}/interface/test`,
    method: 'post',
    data: { id }
  })
}

// 批量测试银行接口连通性
export function batchTestBankInterface(ids) {
  return request({
    url: `${prefix}/interface/batchTest`,
    method: 'post',
    data: { ids }
  })
}

// 导出银行接口配置
export function exportBankInterface(query) {
  return request({
    url: `${prefix}/interface/export`,
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 导入银行接口配置
export function importBankInterface(formData) {
  return request({
    url: `${prefix}/interface/import`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取接口统计数据
export function getBankInterfaceStatistics() {
  return request({
    url: `${prefix}/interface/statistics`,
    method: 'post'
  })
}

// 获取银行列表
export function getBankList() {
  return request({
    url: `${prefix}/bank/getList`,
    method: 'post'
  })
}

// 获取接口类型列表
export function getInterfaceTypeList() {
  return request({
    url: `${prefix}/interfaceType/getList`,
    method: 'post'
  })
}

// 启用/禁用接口
export function toggleBankInterface(id, enabled) {
  return request({
    url: `${prefix}/interface/toggle`,
    method: 'post',
    data: { id, enabled }
  })
}

// 获取接口测试历史
export function getInterfaceTestHistory(query) {
  return request({
    url: `${prefix}/interface/testHistory`,
    method: 'post',
    data: query
  })
}