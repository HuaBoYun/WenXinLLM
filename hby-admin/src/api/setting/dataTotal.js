import request from '@/utils/request'

/**
 * 获取数据总量
 * @returns {Promise}
 */
export function getDataTotal() {
  return request({
    url: '/setting/dataGrowthTrend/getTotal',
    method: 'get',
  })
}
