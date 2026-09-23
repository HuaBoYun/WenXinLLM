/**
 * 理财产品定义模块 - 产品本金规则管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/principal-rule'

// 分页查询产品本金规则列表
export function getPrincipalRuleList(params) {
  return request({
    url: `${BASE_URL}/getList`,
    method: 'get',
    params
  })
}

// 根据ID查询产品本金规则详情
export function getPrincipalRuleById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增产品本金规则
export function createPrincipalRule(data) {
  return request({
    url: `${BASE_URL}/create`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 修改产品本金规则
export function updatePrincipalRule(data) {
  return request({
    url: `${BASE_URL}/update`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 删除产品本金规则
export function deletePrincipalRule(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除产品本金规则
export function batchDeletePrincipalRule(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新产品本金规则状态
export function updatePrincipalRuleStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的产品本金规则列表
export function getEnabledPrincipalRuleList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 根据产品类型获取本金规则列表
export function getPrincipalRuleByProductType(productType) {
  return request({ url: `${BASE_URL}/getByProductType`, method: 'get', params: { productType } })
}

// 检查产品本金规则编码唯一性
export function checkPrincipalRuleCodeUnique(ruleCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { ruleCode, excludeId } })
}

// 本金计算
export function calculatePrincipal(data) {
  return request({
    url: `${BASE_URL}/calculate`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

