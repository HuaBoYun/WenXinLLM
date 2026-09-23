import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/** 分页查询指标控制配置 */
export function getControlConfigList(data) {
  return request({
    url: '/riskcontrol/indicatorControl/list',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: JSON.stringify(transData(data))
  })
}

/** 保存指标控制配置 */
export function saveControlConfig(data) {
  return request({
    url: '/riskcontrol/indicatorControl/save',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: transData(data)
  })
}

/** 删除指标控制配置 */
export function deleteControlConfig(id) {
  return request({
    url: `/riskcontrol/indicatorControl/delete/${id}`,
    method: 'post',
    data: transData({})
  })
}

/** 根据页面ID查询控制配置（拦截器调用） */
export function getControlByRightId(rightId, operationType) {
  return request({
    url: '/riskcontrol/indicatorControl/getByRightId',
    method: 'post',
    params: { rightId, operationType }
  })
}

/** 执行指标校验 */
export function validateIndicator(data) {
  return request({
    url: '/riskcontrol/indicatorControl/validate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: transData(data)
  })
}

/**
 * 页面树复用现有接口，无需新建：
 * import { getAuthList } from '@/api/setting/auths'
 * import { getModuleList } from '@/api/setting/system'
 */

