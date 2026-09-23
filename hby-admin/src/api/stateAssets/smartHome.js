import request from '@/utils/request'

const BASE = '/monitor/v1/smartHome'

/**
 * 获取首页全部数据（聚合接口，一次请求获取所有数据）
 */
export function getSmartHomeOverview() {
  return request({ url: `${BASE}/overview`, method: 'get' })
}

/**
 * 获取KPI数据
 */
export function getSmartHomeKpi() {
  return request({ url: `${BASE}/kpi`, method: 'get' })
}

/**
 * 获取风险预警列表
 */
export function getSmartHomeRiskWarning() {
  return request({ url: `${BASE}/riskWarning`, method: 'get' })
}

/**
 * 获取待处理事项
 */
export function getSmartHomePending() {
  return request({ url: `${BASE}/pending`, method: 'get' })
}

/**
 * 获取异常检测列表
 */
export function getSmartHomeAbnormal() {
  return request({ url: `${BASE}/abnormal`, method: 'get' })
}

/**
 * 获取领域分布统计
 */
export function getSmartHomeDomainStats() {
  return request({ url: `${BASE}/domainStats`, method: 'get' })
}

/**
 * 获取监管快报
 */
export function getSmartHomeReports() {
  return request({ url: `${BASE}/reports`, method: 'get' })
}

/**
 * 获取重点监管进度
 */
export function getSmartHomeProgress() {
  return request({ url: `${BASE}/progress`, method: 'get' })
}
