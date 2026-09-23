import request from '@/utils/request'

/**
 * 按指标汇总数据
 */
export function aggregateByIndicator(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataAnalysis/aggregateByIndicator',
    method: 'post',
    data
  })
}

/**
 * 按组织汇总数据
 */
export function aggregateByOrg(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataAnalysis/aggregateByOrg',
    method: 'post',
    data
  })
}

/**
 * 按期间汇总数据
 */
export function aggregateByPeriod(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataAnalysis/aggregateByPeriod',
    method: 'post',
    data
  })
}

/**
 * 期间对比分析
 */
export function comparePeriod(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataAnalysis/comparePeriod',
    method: 'post',
    data
  })
}

/**
 * 多维度数据查询
 */
export function multiDimensionQuery(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataAnalysis/multiDimensionQuery',
    method: 'post',
    data
  })
}

/**
 * 数据趋势分析
 */
export function trendAnalysis(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataAnalysis/trendAnalysis',
    method: 'post',
    data
  })
}

