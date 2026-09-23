import request from '@/utils/request'

// 产品本金规则管理API

// 获取产品本金规则列表
export function getProductPrincipalRuleList(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/getList',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

// 新增产品本金规则
export function createProductPrincipalRule(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/create',
    method: 'post',
    data
  })
}

// 更新产品本金规则
export function updateProductPrincipalRule(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/update',
    method: 'post',
    data
  })
}

// 删除产品本金规则
export function deleteProductPrincipalRule(ID) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/delete',
    method: 'post',
    params: { ID }
  })
}

// 批量删除产品本金规则
export function batchDeleteProductPrincipalRules(IDs) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

// 检查规则编码唯一性
export function checkRuleCodeUnique(ruleCode, excludeId) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/checkCodeUnique',
    method: 'get',
    params: { ruleCode, excludeId }
  })
}

// 本金计算
export function calculatePrincipal(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/calculate',
    method: 'post',
    data: params
  })
}

// 导出产品本金规则
export function exportProductPrincipalRules(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

// 导入产品本金规则
export function importProductPrincipalRules(formData) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取产品本金规则详情
export function getProductPrincipalRuleDetail(ID) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/getDetail',
    method: 'get',
    params: { ID }
  })
}

// 更新规则状态
export function updateProductPrincipalRuleStatus(ID, isEnabled) {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

// 获取统计信息
export function getProductPrincipalRuleStatistics() {
  return request({
    url: '/qqsk/financial/product-definition/product-principal-rule/getStatistics',
    method: 'get'
  })
}