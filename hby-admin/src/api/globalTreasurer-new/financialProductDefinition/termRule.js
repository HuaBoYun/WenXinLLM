/**
 * 理财产品定义模块 - 产品期限规则管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/term-rule'

// 分页查询产品期限规则列表
export function getTermRuleList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询产品期限规则详情
export function getTermRuleById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增产品期限规则
export function createTermRule(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改产品期限规则
export function updateTermRule(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除产品期限规则
export function deleteTermRule(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除产品期限规则
export function batchDeleteTermRule(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新产品期限规则状态
export function updateTermRuleStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的产品期限规则列表
export function getEnabledTermRuleList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 根据产品类型获取期限规则列表
export function getTermRuleByProductType(productType) {
  return request({ url: `${BASE_URL}/getByProductType`, method: 'get', params: { productType } })
}

// 检查产品期限规则编码唯一性
export function checkTermRuleCodeUnique(ruleCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { ruleCode, excludeId } })
}

