import request from '@/utils/request'

/**
 * 合作伙伴类型管理 API (cwgxAi版本)
 */

// 获取合作伙伴类型列表
export function getPartnerTypeList(params) {
  return request({
    url: '/cwgxAi/partner/type/getList',
    method: 'post',
    data: params
  })
}

// 获取所有启用的合作伙伴类型
export function getAllEnabledTypes() {
  return request({
    url: '/cwgxAi/partner/type/getAllEnabled',
    method: 'post'
  })
}

// 获取合作伙伴类型详情
export function getPartnerTypeDetail(params) {
  return request({
    url: '/cwgxAi/partner/type/getDetail',
    method: 'post',
    data: params
  })
}

// 新增合作伙伴类型
export function addPartnerType(params) {
  return request({
    url: '/cwgxAi/partner/type/add',
    method: 'post',
    data: params
  })
}

// 更新合作伙伴类型
export function updatePartnerType(params) {
  return request({
    url: '/cwgxAi/partner/type/update',
    method: 'post',
    data: params
  })
}

// 删除合作伙伴类型
export function deletePartnerType(params) {
  return request({
    url: '/cwgxAi/partner/type/delete',
    method: 'post',
    data: params
  })
}

// 更新类型状态
export function updateTypeStatus(params) {
  return request({
    url: '/cwgxAi/partner/type/updateStatus',
    method: 'post',
    data: params
  })
}

// 更新排序顺序
export function updateSortOrder(params) {
  return request({
    url: '/cwgxAi/partner/type/updateSortOrder',
    method: 'post',
    data: params
  })
}

// 获取统计信息
export function getStatistics() {
  return request({
    url: '/cwgxAi/partner/type/getStatistics',
    method: 'post'
  })
}

// 导出数据
export function exportData(data) {
  return request({
    url: '/cwgxAi/partner/type/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

// 批量删除
export function batchDelete(data) {
  return request({
    url: '/cwgxAi/partner/type/batchDelete',
    method: 'post',
    data
  })
}

// 切换状态
export function toggleStatus(data) {
  return request({
    url: '/cwgxAi/partner/type/toggle',
    method: 'post',
    data
  })
}

// 获取权限
export function getPermissions(params) {
  return request({
    url: '/cwgxAi/partner/type/getPermissions',
    method: 'post',
    data: params
  })
}

// 更新权限
export function updatePermissions(data) {
  return request({
    url: '/cwgxAi/partner/type/updatePermissions',
    method: 'post',
    data
  })
}

// 获取业务范围
export function getBusinessScope(params) {
  return request({
    url: '/cwgxAi/partner/type/getBusinessScope',
    method: 'post',
    data: params
  })
}

// 更新业务范围
export function updateBusinessScope(data) {
  return request({
    url: '/cwgxAi/partner/type/updateBusinessScope',
    method: 'post',
    data
  })
}

// 检查编码唯一性
export function checkCodeUnique(data) {
  return request({
    url: '/cwgxAi/partner/type/checkCodeUnique',
    method: 'post',
    data
  })
}

// 复制配置
export function copyConfig(data) {
  return request({
    url: '/cwgxAi/partner/type/copyConfig',
    method: 'post',
    data
  })
}

// 导入数据
export function importData(data) {
  return request({
    url: '/cwgxAi/partner/type/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
