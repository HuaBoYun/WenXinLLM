import request from '@/utils/request'

// 市场数据管理 API接口

/**
 * 分页查询市场数据
 */
export function getMarketDataManageList(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/page',
    method: 'get',
    params
  })
}

/**
 * 创建市场数据
 */
export function createMarketDataManage(data) {
  return request({
    url: '/qqsk/treasury/marketDataManage',
    method: 'post',
    data
  })
}

/**
 * 更新市场数据
 */
export function updateMarketDataManage(data) {
  return request({
    url: '/qqsk/treasury/marketDataManage',
    method: 'put',
    data
  })
}

/**
 * 删除市场数据
 */
export function deleteMarketDataManage(id) {
  return request({
    url: `/qqsk/treasury/marketDataManage/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除市场数据
 */
export function batchDeleteMarketDataManage(ids) {
  return request({
    url: '/qqsk/treasury/marketDataManage/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 切换市场数据状态
 */
export function toggleMarketDataManageStatus(data) {
  return request({
    url: '/qqsk/treasury/marketDataManage/status',
    method: 'put',
    data
  })
}

/**
 * 导出市场数据
 */
export function exportMarketDataManage(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 同步市场数据
 */
export function syncMarketData(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/sync',
    method: 'post',
    data: params
  })
}

/**
 * 批量导入市场数据
 */
export function batchImportMarketDataManage(file, updateUser) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/treasury/marketDataManage/import',
    method: 'post',
    data: formData,
    params: { updateUser },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取实时市场数据
 */
export function getRealTimeMarketData(dataType, symbol) {
  return request({
    url: '/qqsk/treasury/marketDataManage/realTime',
    method: 'get',
    params: { dataType, symbol }
  })
}

/**
 * 获取市场数据类型选项
 */
export function getDataTypeOptions() {
  return request({
    url: '/qqsk/treasury/marketDataManage/dataTypeOptions',
    method: 'get'
  })
}

/**
 * 获取数据来源选项
 */
export function getDataSourceOptions() {
  return request({
    url: '/qqsk/treasury/marketDataManage/dataSourceOptions',
    method: 'get'
  })
}

/**
 * 获取货币类型选项
 */
export function getCurrencyOptions() {
  return request({
    url: '/qqsk/treasury/marketDataManage/currencyOptions',
    method: 'get'
  })
}

/**
 * 根据ID查询市场数据
 */
export function getMarketDataManage(id) {
  return request({
    url: `/qqsk/treasury/marketDataManage/${id}`,
    method: 'get'
  })
}

/**
 * 获取市场数据历史趋势
 */
export function getMarketDataTrend(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/trend',
    method: 'get',
    params
  })
}

/**
 * 批量更新市场数据
 */
export function batchUpdateMarketDataManage(data) {
  return request({
    url: '/qqsk/treasury/marketDataManage/batchUpdate',
    method: 'put',
    data
  })
}

/**
 * 验证市场数据
 */
export function validateMarketData(data) {
  return request({
    url: '/qqsk/treasury/marketDataManage/validate',
    method: 'post',
    data
  })
}

/**
 * 获取市场数据统计信息
 */
export function getMarketDataStatistics(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取市场数据对比分析
 */
export function getMarketDataComparison(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/comparison',
    method: 'get',
    params
  })
}

/**
 * 订阅市场数据推送
 */
export function subscribeMarketData(params) {
  return request({
    url: '/qqsk/treasury/marketDataManage/subscribe',
    method: 'post',
    data: params
  })
}

/**
 * 取消订阅市场数据推送
 */
export function unsubscribeMarketData(subscriptionId) {
  return request({
    url: `/qqsk/treasury/marketDataManage/unsubscribe/${subscriptionId}`,
    method: 'delete'
  })
}