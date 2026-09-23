import request from '@/utils/request'

/**
 * 授信额度管理API
 */

// 授信额度分页查询
export function getCreditLimitPage(data) {
  return request({
    url: '/qqsk/financing/credit-limit/page',
    method: 'post',
    data
  })
}

// 授信额度详情查询
export function getCreditLimitDetail(id) {
  return request({
    url: `/qqsk/financing/credit-limit/${id}`,
    method: 'get'
  })
}

// 新增授信额度
export function addCreditLimit(data) {
  return request({
    url: '/qqsk/financing/credit-limit/add',
    method: 'post',
    data
  })
}

// 调整授信额度
export function adjustCreditLimit(data) {
  return request({
    url: '/qqsk/financing/credit-limit/adjust',
    method: 'put',
    data
  })
}

// 冻结授信额度
export function freezeCreditLimit(id, reason) {
  return request({
    url: `/qqsk/financing/credit-limit/freeze/${id}`,
    method: 'put',
    params: { reason }
  })
}

// 解冻授信额度
export function unfreezeCreditLimit(id) {
  return request({
    url: `/qqsk/financing/credit-limit/unfreeze/${id}`,
    method: 'put'
  })
}

