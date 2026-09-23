/**
 * 管理会计智慧首页 API
 * @module api/managementAccountant/smartHome
 */
import request from '@/utils/request'

/**
 * 获取管理会计首页汇总数据（欢迎区统计 + KPI卡片 + 链路节点徽标）
 * @returns {Promise}
 */
export function getSmartHomeData() {
  return request({
    url: '/glkj/accountant/smartHome/getData',
    method: 'get',
  })
}
