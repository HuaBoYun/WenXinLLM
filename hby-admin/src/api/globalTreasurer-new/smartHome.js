import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 全球司库首页API接口
 * @author 示例云开发团队
 * @since 2025-07-01
 */

/**
 * 获取首页聚合数据（KPI、欢迎区统计、现金流、融资进度等）
 * @param {Object} params { year }
 * @returns {Promise} 首页统计数据
 */
export function getTreasurerSmartHomeData(params) {
  return request({
    url: '/qqsk/financial/treasurerSmartHome/overview',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取现金流量统计
 * @param {Object} params { year, month }
 * @returns {Promise} 现金流统计数据
 */
export function getCashFlowStats(params) {
  return request({
    url: '/qqsk/financial/treasurerSmartHome/cashFlow',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取融资进度统计
 * @param {Object} params { year }
 * @returns {Promise} 融资进度数据
 */
export function getFinanceProgress(params) {
  return request({
    url: '/qqsk/financial/treasurerSmartHome/financeProgress',
    method: 'get',
    params: transData(params),
  })
}
