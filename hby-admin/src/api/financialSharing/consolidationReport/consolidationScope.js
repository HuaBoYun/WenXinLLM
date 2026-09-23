import request from '@/utils/request'

/**
 * 查询合并范围配置列表
 */
export function getScopeList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/getScopeList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询合并范围配置
 */
export function getScopeById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/getScopeById',
    method: 'post',
    data
  })
}

/**
 * 新增合并范围配置
 */
export function saveScope(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/saveScope',
    method: 'post',
    data
  })
}

/**
 * 修改合并范围配置
 */
export function updateScope(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/updateScope',
    method: 'post',
    data
  })
}

/**
 * 删除合并范围配置
 */
export function deleteScope(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/deleteScope',
    method: 'post',
    data
  })
}

/**
 * 批量保存合并范围配置
 */
export function batchSaveScope(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/batchSaveScope',
    method: 'post',
    data
  })
}

/**
 * 更新范围配置状态
 */
export function updateScopeStatus(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationScope/updateScopeStatus',
    method: 'post',
    data
  })
}

