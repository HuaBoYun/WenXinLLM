import request from '@/utils/request'

/**
 * 合作伙伴类型管理 API
 */

// 获取合作伙伴类型列表
export function getPartnerTypeList(params) {
  return request({
    url: '/qqsk/settlement/partner-type/page',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  })
}

// 获取所有启用的合作伙伴类型
export function getAllEnabledTypes() {
  return request({
    url: '/qqsk/settlement/partner-type/getAllEnabled',
    method: 'post'
  })
}

// 获取合作伙伴类型详情
export function getPartnerTypeDetail(params) {
  return request({
    url: '/qqsk/settlement/partner-type/getDetail',
    method: 'post',
    data: params
  })
}

// 新增合作伙伴类型
export function addPartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/add',
    method: 'post',
    data: params
  })
}

// 更新合作伙伴类型
export function updatePartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/update',
    method: 'post',
    data: params
  })
}

// 删除合作伙伴类型
export function deletePartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/delete',
    method: 'post',
    data: params
  })
}

// 更新类型状态
export function updateTypeStatus(params) {
  return request({
    url: '/qqsk/settlement/partner-type/toggle',
    method: 'post',
    data: params
  })
}

// 更新排序顺序
export function updateSortOrder(params) {
  return request({
    url: '/qqsk/settlement/partner-type/updateSortOrder',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  })
}

// 获取统计信息
export function getStatistics() {
  return request({
    url: '/qqsk/settlement/partner-type/getStatistics',
    method: 'post'
  })
}

// 导出类型配置
export function exportTypes(params) {
  return request({
    url: '/qqsk/settlement/partner-type/export',
    method: 'post',
    data: params
  })
}

// 批量删除合作伙伴类型
export function batchDeletePartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/batchDelete',
    method: 'post',
    data: params
  })
}

// 启用/禁用合作伙伴类型
export function togglePartnerType(params) {
  return request({
    url: '/qqsk/settlement/partner-type/toggle',
    method: 'post',
    data: params
  })
}

// 获取类型权限配置
export function getTypePermissions(params) {
  return request({
    url: '/qqsk/settlement/partner-type/getPermissions',
    method: 'post',
    data: params
  })
}

// 更新类型权限配置
export function updateTypePermissions(params) {
  return request({
    url: '/qqsk/settlement/partner-type/updatePermissions',
    method: 'post',
    data: params
  })
}

// 获取业务范围配置
export function getBusinessScope(params) {
  return request({
    url: '/qqsk/settlement/partner-type/getBusinessScope',
    method: 'post',
    data: params
  })
}

// 更新业务范围配置
export function updateBusinessScope(params) {
  return request({
    url: '/qqsk/settlement/partner-type/updateBusinessScope',
    method: 'post',
    data: params
  })
}

// 检查类型编码唯一性
export function checkTypeCodeUnique(params) {
  return request({
    url: '/qqsk/settlement/partner-type/checkCodeUnique',
    method: 'post',
    data: params
  })
}

// 复制类型配置
export function copyTypeConfig(params) {
  return request({
    url: '/qqsk/settlement/partner-type/copyConfig',
    method: 'post',
    data: params
  })
}

// 导入类型配置
export function importTypeConfig(params) {
  return request({
    url: '/qqsk/settlement/partner-type/import',
    method: 'post',
    data: params
  })
}