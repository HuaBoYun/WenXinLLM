import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 智慧法务首页-获取仪表盘数据
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSmartHomeDashboard(params) {
  return request({
    url: '/fwgl/api-auth/smart-home/dashboard',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
