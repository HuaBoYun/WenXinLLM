import request from '@/utils/request'

/**
 * 获取业务活跃度排名
 * @returns {Promise}
 */
export function getBusinessActivityRanking() {
  return request({
    url: '/setting/dataGrowthTrend/getBusinessActivityRanking',
    method: 'get',
  })
}
