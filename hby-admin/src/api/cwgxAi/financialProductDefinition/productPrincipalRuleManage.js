import request from '@/utils/request'

// 产品本金规则管理API

// 获取产品本金规则列表
export function getProductPrincipalRuleList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/getList',
    method: 'post',
    data: params
  })
}

// 新增产品本金规则
export function createProductPrincipalRule(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/create',
    method: 'post',
    data: data
  })
}

// 更新产品本金规则
export function updateProductPrincipalRule(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/update',
    method: 'post',
    data: data
  })
}

// 删除产品本金规则
export function deleteProductPrincipalRule(ruleId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/delete',
    method: 'post',
    data: { ruleId: ruleId }
  })
}

// 批量删除产品本金规则
export function batchDeleteProductPrincipalRules(ruleIds) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/batchDelete',
    method: 'post',
    data: { ruleIds: ruleIds }
  })
}

// 检查规则编码唯一性
export function checkRuleCodeUnique(ruleCode, excludeId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/checkCodeUnique',
    method: 'post',
    data: { ruleCode: ruleCode, excludeId: excludeId }
  })
}

// 本金计算
export function calculatePrincipal(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/calculate',
    method: 'post',
    data: params
  })
}

// 导出产品本金规则
export function exportProductPrincipalRules(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// 导入产品本金规则
export function importProductPrincipalRules(formData) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取产品本金规则详情
export function getProductPrincipalRuleDetail(ruleId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/getDetail',
    method: 'post',
    data: { ruleId: ruleId }
  })
}

// 更新规则状态
export function updateProductPrincipalRuleStatus(ruleId, isEnabled) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/updateStatus',
    method: 'post',
    data: { ruleId: ruleId, isEnabled: isEnabled }
  })
}

// 获取统计信息
export function getProductPrincipalRuleStatistics() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productPrincipalRuleManage/getStatistics',
    method: 'post'
  })
}