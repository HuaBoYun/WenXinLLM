import request from '@/utils/request'

/**
 * 获取用户活跃度
 */
export function getUserActivity() {
  return request({
    url: '/setting/dataGrowthTrend/getUserActivity',
    method: 'get',
  })
}
