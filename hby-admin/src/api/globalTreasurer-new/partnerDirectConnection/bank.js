import request from '@/utils/request'

/**
 * 财资公共模块 - 合作伙伴直连管理 API
 */

// ==================== 银企直连日志管理 ====================
export function getBankConnectLogPage(params) {
  return request({
    url: '/qqsk/settlement/bank-connect-log/page',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getBankConnectLogDetail(logId) {
  return request({ url: `/qqsk/settlement/bank-connect-log/${logId}`, method: 'get' })
}

export function getBankConnectLogStatistics() {
  return request({ url: '/qqsk/settlement/bank-connect-log/statistics', method: 'get' })
}

export function retryBankConnectLog(logId) {
  return request({ url: `/qqsk/settlement/bank-connect-log/retry/${logId}`, method: 'post' })
}

export function exportBankConnectLog(params) {
  return request({
    url: '/qqsk/settlement/bank-connect-log/export',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function cleanBankConnectLog(days) {
  return request({ url: '/qqsk/settlement/bank-connect-log/clean', method: 'delete', params: { days } })
}

export function getInterfaceTypes() {
  return request({ url: '/qqsk/settlement/bank-connect-log/interface-types', method: 'get' })
}

// ==================== 银行直连配置 ====================
export function getBankDirectConnectionConfigPage(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection-config/page',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getBankDirectConnectionConfigDetail(id) {
  return request({ url: `/qqsk/settlement/bank-direct-connection-config/${id}`, method: 'get' })
}

export function addBankDirectConnectionConfig(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection-config',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateBankDirectConnectionConfig(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection-config',
    method: 'put',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteBankDirectConnectionConfig(id) {
  return request({ url: `/qqsk/settlement/bank-direct-connection-config/${id}`, method: 'delete' })
}

export function testBankDirectConnectionConfig(id) {
  return request({ url: `/qqsk/settlement/bank-direct-connection-config/test/${id}`, method: 'post' })
}

export function batchTestBankDirectConnectionConfig(ids) {
  // 将id数组转换为数字类型
  const idArray = Array.isArray(ids) ? ids.map(id => parseInt(id, 10)) : []
  return request({
    url: '/qqsk/settlement/bank-direct-connection-config/batch-test',
    method: 'post',
    data: { ids: idArray },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getBankDirectConnectionConfigStatistics() {
  return request({ url: '/qqsk/settlement/bank-direct-connection-config/statistics', method: 'get' })
}

export function exportBankDirectConnectionConfig(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection-config/export',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

// ==================== 银行直连管理 ====================
export function getBankDirectConnectionPage(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection/page',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getBankDirectConnectionDetail(configId) {
  return request({ url: `/qqsk/settlement/bank-direct-connection/${configId}`, method: 'get' })
}

export function addBankDirectConnection(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateBankDirectConnection(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection',
    method: 'put',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteBankDirectConnection(configId) {
  // 使用 GET 请求传递 ID，后端改为 @GetMapping("/delete/{configId}")
  return request({
    url: `/qqsk/settlement/bank-direct-connection/delete/${configId}`,
    method: 'get'
  })
}

export function batchTestBankDirectConnection() {
  return request({ url: '/qqsk/settlement/bank-direct-connection/batch-test', method: 'post' })
}

export function getBankDirectConnectionStatistics() {
  return request({ url: '/qqsk/settlement/bank-direct-connection/statistics', method: 'get' })
}

export function exportBankDirectConnection(params) {
  return request({
    url: '/qqsk/settlement/bank-direct-connection/export',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

