import request from '@/utils/request'

// 汇率配置管理 API接口

/**
 * 分页查询汇率配置
 */
export function getExchangeRateConfigList(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询汇率配置
 */
export function getExchangeRateConfig(id) {
  return request({
    url: `/qqsk/treasury/exchangeRateConfig/${id}`,
    method: 'get'
  })
}

/**
 * 创建汇率配置
 */
export function createExchangeRateConfig(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig',
    method: 'post',
    data
  })
}

/**
 * 更新汇率配置
 */
export function updateExchangeRateConfig(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig',
    method: 'put',
    data
  })
}

/**
 * 删除汇率配置
 */
export function deleteExchangeRateConfig(id) {
  return request({
    url: `/qqsk/treasury/exchangeRateConfig/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除汇率配置
 */
export function batchDeleteExchangeRateConfig(ids) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 切换汇率配置状态
 */
export function toggleExchangeRateConfigStatus(data) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/status',
    method: 'put',
    data
  })
}

/**
 * 导出汇率配置
 */
export function exportExchangeRateConfig(params) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取货币类型选项
 */
export function getCurrencyOptions() {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/currencyOptions',
    method: 'get'
  })
}

/**
 * 获取汇率类型选项
 */
export function getRateTypeOptions() {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/rateTypeOptions',
    method: 'get'
  })
}

/**
 * 获取更新频率选项
 */
export function getUpdateFrequencyOptions() {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/updateFrequencyOptions',
    method: 'get'
  })
}

/**
 * 同步汇率数据
 */
export function syncExchangeRate(configId, updateUser) {
  return request({
    url: `/qqsk/treasury/exchangeRateConfig/${configId}/sync`,
    method: 'post',
    params: { updateUser }
  })
}

/**
 * 获取实时汇率
 */
export function getRealTimeExchangeRate(baseCurrency, targetCurrency) {
  return request({
    url: '/qqsk/treasury/exchangeRateConfig/realTimeRate',
    method: 'get',
    params: { baseCurrency, targetCurrency }
  })
}