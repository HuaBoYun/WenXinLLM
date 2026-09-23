import request from '@/utils/request'

/**
 * 查询股权信息列表
 */
export function getEquityList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/getEquityList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询股权信息
 */
export function getEquityById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/getEquityById',
    method: 'post',
    data
  })
}

/**
 * 新增股权信息
 */
export function saveEquity(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/saveEquity',
    method: 'post',
    data
  })
}

/**
 * 修改股权信息
 */
export function updateEquity(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/updateEquity',
    method: 'post',
    data
  })
}

/**
 * 删除股权信息
 */
export function deleteEquity(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/deleteEquity',
    method: 'post',
    data
  })
}

/**
 * 更新股权信息状态
 */
export function updateEquityStatus(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/updateEquityStatus',
    method: 'post',
    data
  })
}

/**
 * 根据模型ID查询股权信息列表
 */
export function getEquityListByModelId(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityInfo/getEquityListByModelId',
    method: 'post',
    data
  })
}

