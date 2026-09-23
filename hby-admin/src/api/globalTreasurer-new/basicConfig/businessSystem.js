import request from '@/utils/request'

/**
 * 财资公共模块 - 业务系统注册 API
 */

// ==================== 业务系统注册管理 ====================

/**
 * 分页查询业务系统列表
 * @param {Object} params - 查询参数
 * @param {Number} params.page - 页码
 * @param {Number} params.pageSize - 每页数量
 * @param {String} params.systemCode - 系统编码
 * @param {String} params.systemName - 系统名称
 * @param {String} params.systemType - 系统类型
 * @param {String} params.connectionStatus - 连接状态
 * @param {String} params.status - 状态
 */
export function getBusinessSystemPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/system/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 获取业务系统详情
 * @param {Number|String} id - 系统ID
 */
export function getBusinessSystemDetail(id) {
  return request({
    url: '/qqsk/financial/basicConfig/system/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 新增业务系统
 * @param {Object} data - 业务系统数据
 */
export function createBusinessSystem(data) {
  return request({
    url: '/qqsk/financial/basicConfig/system/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 更新业务系统
 * @param {Object} data - 业务系统数据
 */
export function updateBusinessSystem(data) {
  return request({
    url: '/qqsk/financial/basicConfig/system/update',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 删除业务系统
 * @param {Number|String} id - 系统ID
 */
export function deleteBusinessSystem(id) {
  return request({
    url: '/qqsk/financial/basicConfig/system/delete',
    method: 'delete',
    params: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 测试系统连接
 * @param {Object} data - 连接测试参数
 * @param {Number|String} data.id - 系统ID
 * @param {String} data.apiUrl - 接口地址
 * @param {String} data.authType - 认证方式
 * @param {Object} data.authConfig - 认证配置
 */
export function testSystemConnection(data) {
  return request({
    url: '/qqsk/financial/basicConfig/system/testConnection',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 同步系统状态
 * @param {Number|String} id - 系统ID
 */
export function syncSystemStatus(id) {
  return request({
    url: '/qqsk/financial/basicConfig/system/syncStatus',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出业务系统数据
 * @param {Object} params - 查询参数
 */
export function exportBusinessSystem(params) {
  return request({
    url: '/qqsk/financial/basicConfig/system/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量导入业务系统
 * @param {FormData} formData - 文件表单数据
 */
export function importBusinessSystem(formData) {
  return request({
    url: '/qqsk/financial/basicConfig/system/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 获取系统类型下拉数据
 */
export function getSystemTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/system/getSystemTypes',
    method: 'get'
  })
}

/**
 * 获取认证方式下拉数据
 */
export function getAuthTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/system/getAuthTypes',
    method: 'get'
  })
}
