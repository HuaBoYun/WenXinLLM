import request from '@/utils/request'

/**
 * 合作伙伴档案管理 API
 */

// 获取合作伙伴档案列表
export function getPartnerList(params) {
  return request({
    url: '/cwgxAi/partner/archive/getList',
    method: 'post',
    data: params
  })
}

// 获取合作伙伴详情
export function getPartnerDetail(params) {
  return request({
    url: '/cwgxAi/partner/archive/getDetail',
    method: 'post',
    data: params
  })
}

// 新增合作伙伴
export function addPartner(params) {
  return request({
    url: '/cwgxAi/partner/archive/add',
    method: 'post',
    data: params
  })
}

// 更新合作伙伴
export function updatePartner(params) {
  return request({
    url: '/cwgxAi/partner/archive/update',
    method: 'post',
    data: params
  })
}

// 删除合作伙伴
export function deletePartner(params) {
  return request({
    url: '/cwgxAi/partner/archive/delete',
    method: 'post',
    data: params
  })
}

// 获取合作伙伴类型列表
export function getPartnerTypes() {
  return request({
    url: '/cwgxAi/partner/archive/getPartnerTypes',
    method: 'post'
  })
}

// 导出合作伙伴档案
export function exportPartners(params) {
  return request({
    url: '/cwgxAi/partner/archive/export',
    method: 'post',
    data: params
  })
}

// 获取统计信息
export function getStatistics() {
  return request({
    url: '/cwgxAi/partner/archive/getStatistics',
    method: 'post'
  })
}