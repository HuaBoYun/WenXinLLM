import request from '@/utils/request'

/**
 * 获取核心功能使用率
 * @returns {Promise}
 */
export function getCoreFunctionUsage() {
  return request({
    url: '/setting/dataGrowthTrend/getCoreFunctionUsage',
    method: 'get',
  })
}
