import request from '@/utils/request'

// 利率管理 API接口

/**
 * 分页查询利率数据
 */
export function getInterestRateManageList(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/page',
    method: 'get',
    params
  })
}

/**
 * 创建利率数据
 */
export function createInterestRateManage(data) {
  return request({
    url: '/qqsk/treasury/interestRateManage',
    method: 'post',
    data
  })
}

/**
 * 更新利率数据
 */
export function updateInterestRateManage(data) {
  return request({
    url: '/qqsk/treasury/interestRateManage',
    method: 'put',
    data
  })
}

/**
 * 删除利率数据
 */
export function deleteInterestRateManage(id) {
  return request({
    url: `/qqsk/treasury/interestRateManage/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除利率数据
 */
export function batchDeleteInterestRateManage(ids) {
  return request({
    url: '/qqsk/treasury/interestRateManage/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 切换利率数据状态
 */
export function toggleInterestRateManageStatus(data) {
  return request({
    url: '/qqsk/treasury/interestRateManage/status',
    method: 'put',
    data
  })
}

/**
 * 导出利率数据
 */
export function exportInterestRateManage(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量导入利率数据
 */
export function batchImportInterestRateManage(file, updateUser) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/treasury/interestRateManage/import',
    method: 'post',
    data: formData,
    params: { updateUser },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取实时利率
 */
export function getRealTimeInterestRate(rateType, currency, term) {
  return request({
    url: '/qqsk/treasury/interestRateManage/realTime',
    method: 'get',
    params: { rateType, currency, term }
  })
}

/**
 * 同步利率数据
 */
export function syncInterestRateData(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/sync',
    method: 'post',
    data: params
  })
}

/**
 * 获取利率类型选项
 */
export function getRateTypeOptions() {
  return request({
    url: '/qqsk/treasury/interestRateManage/rateTypeOptions',
    method: 'get'
  })
}

/**
 * 获取货币类型选项
 */
export function getCurrencyOptions() {
  return request({
    url: '/qqsk/treasury/interestRateManage/currencyOptions',
    method: 'get'
  })
}

/**
 * 获取期限类型选项
 */
export function getTermTypeOptions() {
  return request({
    url: '/qqsk/treasury/interestRateManage/termTypeOptions',
    method: 'get'
  })
}

/**
 * 获取数据来源选项
 */
export function getDataSourceOptions() {
  return request({
    url: '/qqsk/treasury/interestRateManage/dataSourceOptions',
    method: 'get'
  })
}

/**
 * 根据ID查询利率数据
 */
export function getInterestRateManage(id) {
  return request({
    url: `/qqsk/treasury/interestRateManage/${id}`,
    method: 'get'
  })
}

/**
 * 获取利率历史趋势数据
 */
export function getInterestRateTrend(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/trend',
    method: 'get',
    params
  })
}

/**
 * 批量更新利率数据
 */
export function batchUpdateInterestRateManage(data) {
  return request({
    url: '/qqsk/treasury/interestRateManage/batchUpdate',
    method: 'put',
    data
  })
}

/**
 * 验证利率数据
 */
export function validateInterestRateData(data) {
  return request({
    url: '/qqsk/treasury/interestRateManage/validate',
    method: 'post',
    data
  })
}

/**
 * 获取利率统计信息
 */
export function getInterestRateStatistics(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取基准利率
 */
export function getBenchmarkRates(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/benchmark',
    method: 'get',
    params
  })
}

/**
 * 计算利率
 */
export function calculateInterestRate(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/calculate',
    method: 'get',
    params
  })
}

/**
 * 获取利率对比数据
 */
export function getInterestRateComparison(params) {
  return request({
    url: '/qqsk/treasury/interestRateManage/comparison',
    method: 'get',
    params
  })
}