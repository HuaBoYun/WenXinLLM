import request from '@/utils/request'

export function getProductInterestRuleList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/getList',
    method: 'post',
    params
  })
}

export function createProductInterestRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/create',
    method: 'post',
    data
  })
}

export function updateProductInterestRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/update',
    method: 'post',
    data
  })
}

export function deleteProductInterestRule(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/delete',
    method: 'post',
    params: { ID }
  })
}

export function exportProductInterestRules(params) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}