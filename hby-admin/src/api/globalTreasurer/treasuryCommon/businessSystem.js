import request from '@/utils/request'

/**
 * 业务系统注册管理API接口
 */

// 分页查询业务系统注册列表
export function getBusinessSystemList(params) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/system/list',
    method: 'get',
    params
  })
}

// 根据ID查询业务系统注册信息
export function getBusinessSystemById(id) {
  return request({
    url: `/qqsk/financial/treasury-common/business-system/${id}`,
    method: 'get'
  })
}

// 根据系统编码查询业务系统注册信息
export function getBusinessSystemByCode(systemCode) {
  return request({
    url: `/qqsk/financial/treasury-common/business-system/byCode/${systemCode}`,
    method: 'get'
  })
}

// 保存或更新业务系统注册信息
export function saveOrUpdateBusinessSystem(data) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/saveOrUpdate',
    method: 'post',
    data
  })
}

// 删除业务系统注册信息
export function deleteBusinessSystem(id) {
  return request({
    url: `/qqsk/financial/treasury-common/business-system/${id}`,
    method: 'delete'
  })
}

// 批量删除业务系统注册信息
export function batchDeleteBusinessSystems(ids) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/batchDelete',
    method: 'post',
    data: ids
  })
}

// 更新系统状态
export function updateBusinessSystemStatus(data) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/updateStatus',
    method: 'post',
    data
  })
}

// 获取所有激活状态的业务系统
export function getActiveBusinessSystems() {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/active',
    method: 'get'
  })
}

// 获取业务系统统计信息
export function getBusinessSystemStatistics() {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/statistics',
    method: 'get'
  })
}

// 测试系统连接
export function testSystemConnection(data) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/testConnection',
    method: 'post',
    data
  })
}

// 导出业务系统配置
export function exportBusinessSystemConfig(params) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/export',
    method: 'get',
    params
  })
}

// 导入业务系统配置
export function importBusinessSystemConfig(data) {
  return request({
    url: '/qqsk/financial/treasury-common/business-system/import',
    method: 'post',
    data
  })
}

// 系统类型选项
export const systemTypeOptions = [
  { label: '核心系统', value: 'CORE' },
  { label: '支持系统', value: 'SUPPORT' },
  { label: '外部系统', value: 'EXTERNAL' }
]

// 系统状态选项
export const systemStatusOptions = [
  { label: '激活', value: 'ACTIVE' },
  { label: '停用', value: 'INACTIVE' },
  { label: '维护中', value: 'MAINTENANCE' }
]

// 获取系统类型标签
export function getSystemTypeLabel(type) {
  const typeMap = {
    'CORE': '核心系统',
    'SUPPORT': '支持系统',
    'EXTERNAL': '外部系统'
  }
  return typeMap[type] || type
}

// 获取系统类型标签样式
export function getSystemTypeTag(type) {
  const typeMap = {
    'CORE': 'danger',
    'SUPPORT': 'warning',
    'EXTERNAL': 'info'
  }
  return typeMap[type] || 'info'
}

// 获取系统状态标签
export function getSystemStatusLabel(status) {
  const statusMap = {
    'ACTIVE': '激活',
    'INACTIVE': '停用',
    'MAINTENANCE': '维护中'
  }
  return statusMap[status] || status
}

// 获取系统状态标签样式
export function getSystemStatusTag(status) {
  const statusMap = {
    'ACTIVE': 'success',
    'INACTIVE': 'info',
    'MAINTENANCE': 'warning'
  }
  return statusMap[status] || 'info'
}