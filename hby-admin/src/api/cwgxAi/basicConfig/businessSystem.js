import request from '@/utils/request'

// 业务系统管理 API接口 (cwgxAi版本)

/**
 * 分页查询业务系统列表
 */
export function getBusinessSystemList(params) {
  return request({
    url: '/cwgxAi/basicConfig/system/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询业务系统
 */
export function getBusinessSystem(id) {
  return request({
    url: `/cwgxAi/basicConfig/system/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建业务系统
 */
export function createBusinessSystem(data) {
  return request({
    url: '/cwgxAi/basicConfig/system/create',
    method: 'post',
    data
  })
}

/**
 * 更新业务系统
 */
export function updateBusinessSystem(data) {
  return request({
    url: '/cwgxAi/basicConfig/system/update',
    method: 'put',
    data
  })
}

/**
 * 删除业务系统
 */
export function deleteBusinessSystem(ids) {
  return request({
    url: `/cwgxAi/basicConfig/system/delete`,
    method: 'delete',
    params: { id: ids }
  })
}

/**
 * 测试系统连接
 */
export function testSystemConnection(id) {
  return request({
    url: `/cwgxAi/basicConfig/system/testConnection`,
    method: 'post',
    data: { id }
  })
}

/**
 * 同步系统状态
 */
export function syncSystemStatus() {
  return request({
    url: '/cwgxAi/basicConfig/system/syncStatus',
    method: 'post'
  })
}

/**
 * 获取系统类型选项
 */
export function getSystemTypes() {
  return request({
    url: '/cwgxAi/basicConfig/system/systemTypes',
    method: 'get'
  })
}

/**
 * 获取认证方式选项
 */
export function getAuthTypes() {
  return request({
    url: '/cwgxAi/basicConfig/system/authTypes',
    method: 'get'
  })
}

/**
 * 导出业务系统数据
 */
export function exportBusinessSystem(params) {
  return request({
    url: '/cwgxAi/basicConfig/system/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
