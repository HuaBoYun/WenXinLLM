import request from '@/utils/request'

/**
 * 获取新增用户数
 */
export function getNewUserCount() {
  return request({
    url: '/setting/dataGrowthTrend/getNewUserCount',
    method: 'get',
  })
}
