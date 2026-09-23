import request from '@/utils/request'

// 产品期限规则管理API

export function getProductTermRuleList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/getList',
    method: 'post',
    data: params
  })
}

export function createProductTermRule(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/create',
    method: 'post',
    data: data
  })
}

export function updateProductTermRule(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/update',
    method: 'post',
    data: data
  })
}

export function deleteProductTermRule(ruleId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/delete',
    method: 'post',
    data: { ruleId: ruleId }
  })
}

export function exportProductTermRules(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

export function getProductTermRuleStatistics() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/getStatistics',
    method: 'post'
  })
}

export function calculateTerm(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTermRuleManage/calculate',
    method: 'post',
    data: params
  })
}
