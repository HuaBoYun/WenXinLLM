import request from '@/utils/request'

/**
 * 合作伙伴档案管理 API
 */

// 获取合作伙伴档案列表
export function getPartnerList(params) {
  return request({
    url: '/qqsk/settlement/partner-archive/page',
    method: 'post',
    data: params
  })
}

// 获取合作伙伴详情
export function getPartnerDetail(id) {
  return request({
    url: `/qqsk/settlement/partner-archive/${id}`,
    method: 'get'
  })
}

// 新增合作伙伴
export function addPartner(params) {
  return request({
    url: '/qqsk/settlement/partner-archive',
    method: 'post',
    data: params
  })
}

// 更新合作伙伴
export function updatePartner(params) {
  return request({
    url: '/qqsk/settlement/partner-archive',
    method: 'put',
    data: params
  })
}

// 删除合作伙伴
export function deletePartner(id) {
  return request({
    url: `/qqsk/settlement/partner-archive/${id}`,
    method: 'delete'
  })
}

// 获取合作伙伴类型列表
export function getPartnerTypes() {
  return request({
    url: '/qqsk/settlement/partner-archive/partner-types',
    method: 'get'
  })
}

// 导出合作伙伴档案
export function exportPartners(params) {
  return request({
    url: '/qqsk/settlement/partner-archive/export',
    method: 'post',
    data: params
  })
}

// 获取统计信息
export function getStatistics() {
  return request({
    url: '/qqsk/settlement/partner-archive/statistics',
    method: 'get'
  })
}