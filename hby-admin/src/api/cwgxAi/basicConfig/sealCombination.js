import request from '@/utils/request'

// 印鉴组合配置管理 API接口 (cwgxAi版本)

/**
 * 分页查询印鉴组合
 */
export function getSealCombinationList(params) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/list',
    method: 'get',
    params
  })
}

/**
 * 获取印鉴组合详情
 */
export function getSealCombinationDetail(id) {
  return request({
    url: `/cwgxAi/basicConfig/sealCombination/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建印鉴组合
 */
export function createSealCombination(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/create',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴组合
 */
export function updateSealCombination(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/update',
    method: 'post',
    data
  })
}

/**
 * 删除印鉴组合
 */
export function deleteSealCombination(id) {
  return request({
    url: `/cwgxAi/basicConfig/sealCombination/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除印鉴组合
 */
export function batchDeleteSealCombination(ids) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/batchDelete',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取组合类型选项
 */
export function getCombinationTypes() {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/combinationTypes',
    method: 'get'
  })
}

/**
 * 获取业务类型选项
 */
export function getBusinessTypes() {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/businessTypes',
    method: 'get'
  })
}

/**
 * 获取权限级别选项
 */
export function getAuthorityLevels() {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/authorityLevels',
    method: 'get'
  })
}

/**
 * 获取印鉴组合统计数据
 */
export function getSealCombinationStatistics() {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/statistics',
    method: 'get'
  })
}

/**
 * 获取可选印鉴列表
 */
export function getAvailableSeals() {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/availableSeals',
    method: 'get'
  })
}

/**
 * 更新印鉴组合状态
 */
export function updateSealCombinationStatus(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/updateStatus',
    method: 'post',
    data
  })
}

/**
 * 检查组合编码唯一性
 */
export function checkCombinationCodeUnique(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/checkCode',
    method: 'post',
    data
  })
}
