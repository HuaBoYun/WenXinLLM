import request from '@/utils/request'

// 业务规则管理 API接口 (cwgxAi版本)

export function getBusinessRuleList(query) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/list',
    method: 'get',
    params: query
  })
}

export function createBusinessRule(data) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/save',
    method: 'post',
    data
  })
}

export function updateBusinessRule(data) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/update',
    method: 'post',
    data
  })
}

export function deleteBusinessRule(id) {
  return request({
    url: `/cwgxAi/data-rules/business-rule/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteBusinessRule(ids) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/batchDelete',
    method: 'post',
    data: ids
  })
}

export function toggleBusinessRuleStatus(data) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/toggleStatus',
    method: 'post',
    data
  })
}

export function exportBusinessRule(query) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

export function getBusinessRuleById(id) {
  return request({
    url: `/cwgxAi/data-rules/business-rule/${id}`,
    method: 'get'
  })
}

export function validateRuleExpression(data) {
  return request({
    url: '/cwgxAi/data-rules/business-rule/validate',
    method: 'post',
    data
  })
}
