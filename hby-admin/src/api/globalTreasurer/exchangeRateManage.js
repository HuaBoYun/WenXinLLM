import request from '@/utils/request'

// 汇率管理 API接口

/**
 * 分页查询汇率数据
 */
export function getExchangeRateManageList(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/page',
    method: 'get',
    params
  })
}

/**
 * 创建汇率数据
 */
export function createExchangeRateManage(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage',
    method: 'post',
    data
  })
}

/**
 * 更新汇率数据
 */
export function updateExchangeRateManage(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage',
    method: 'put',
    data
  })
}

/**
 * 删除汇率数据
 */
export function deleteExchangeRateManage(id) {
  return request({
    url: `/qqsk/treasury/exchangeRateManage/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除汇率数据
 */
export function batchDeleteExchangeRateManage(ids) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 切换汇率数据状态
 */
export function toggleExchangeRateManageStatus(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/status',
    method: 'put',
    data
  })
}

/**
 * 导出汇率数据
 */
export function exportExchangeRateManage(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量导入汇率数据
 */
export function batchImportExchangeRateManage(file, updateUser) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/treasury/exchangeRateManage/import',
    method: 'post',
    data: formData,
    params: { updateUser },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 货币转换计算
 */
export function convertCurrency(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/convert',
    method: 'get',
    params
  })
}

/**
 * 获取实时汇率
 */
export function getRealTimeExchangeRate(baseCurrency, targetCurrency, rateType) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/realTime',
    method: 'get',
    params: { baseCurrency, targetCurrency, rateType }
  })
}

/**
 * 同步汇率数据
 */
export function syncExchangeRateData(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/sync',
    method: 'post',
    data: params
  })
}

/**
 * 获取货币类型选项
 */
export function getCurrencyOptions() {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/currencyOptions',
    method: 'get'
  })
}

/**
 * 获取汇率类型选项
 */
export function getRateTypeOptions() {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/rateTypeOptions',
    method: 'get'
  })
}

/**
 * 获取数据来源选项
 */
export function getDataSourceOptions() {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/dataSourceOptions',
    method: 'get'
  })
}

/**
 * 根据ID查询汇率数据
 */
export function getExchangeRateManage(id) {
  return request({
    url: `/qqsk/treasury/exchangeRateManage/${id}`,
    method: 'get'
  })
}

/**
 * 获取汇率历史趋势数据
 */
export function getExchangeRateTrend(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/trend',
    method: 'get',
    params
  })
}

/**
 * 批量更新汇率数据
 */
export function batchUpdateExchangeRateManage(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/batchUpdate',
    method: 'put',
    data
  })
}

/**
 * 验证汇率数据
 */
export function validateExchangeRateData(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/validate',
    method: 'post',
    data
  })
}

/**
 * 获取汇率统计信息
 */
export function getExchangeRateStatistics(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateManage/statistics',
    method: 'get',
    params
  })
}