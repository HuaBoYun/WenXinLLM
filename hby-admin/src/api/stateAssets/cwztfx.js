import request from '@/utils/request'

const BASE = '/monitor/v1/supervision/financial/cwztfx'

/**
 * 财务综合总览（cwzl.vue 使用）
 * @param {string} year  年份，如 "2024"
 * @param {string} month 月份，"0" 表示全年
 */
export function getCwzlOverview(params) {
  return request({ url: `${BASE}/overview`, method: 'get', params })
}

/**
 * 利润分析（lrfx.vue 使用）
 */
export function getLrfxData(params) {
  return request({ url: `${BASE}/profit`, method: 'get', params })
}

/**
 * 收入分析（srfx.vue 使用）
 */
export function getSrfxData(params) {
  return request({ url: `${BASE}/revenue`, method: 'get', params })
}

/**
 * 成本费用分析（cbfyfx.vue 使用）
 */
export function getCbfyfxData(params) {
  return request({ url: `${BASE}/cost`, method: 'get', params })
}

/**
 * 资产负债分析（zcfzfx.vue 使用）
 */
export function getZcfzfxData(params) {
  return request({ url: `${BASE}/balance`, method: 'get', params })
}

/**
 * 应收应付分析（ysyfzkfx.vue 使用）
 */
export function getYsyfzkfxData(params) {
  return request({ url: `${BASE}/receivable`, method: 'get', params })
}
