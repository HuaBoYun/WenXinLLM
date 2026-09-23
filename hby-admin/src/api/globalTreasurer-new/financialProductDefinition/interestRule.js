/**
 * 理财产品定义模块 - 产品利息规则管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/product-interest-rule'

// 分页查询产品利息规则列表
export function getInterestRuleList(params) {
  return request({
    url: `${BASE_URL}/getList`,
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

// 根据ID查询产品利息规则详情
export function getInterestRuleById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增产品利息规则
export function createInterestRule(data) {
  return request({
    url: `${BASE_URL}/create`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 修改产品利息规则
export function updateInterestRule(data) {
  return request({
    url: `${BASE_URL}/update`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 删除产品利息规则
export function deleteInterestRule(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除产品利息规则
export function batchDeleteInterestRule(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新产品利息规则状态
export function updateInterestRuleStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的产品利息规则列表
export function getEnabledInterestRuleList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 根据产品类型获取利息规则列表
export function getInterestRuleByProductType(productType) {
  return request({ url: `${BASE_URL}/getByProductType`, method: 'get', params: { productType } })
}

// 检查产品利息规则编码唯一性
export function checkInterestRuleCodeUnique(ruleCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { ruleCode, excludeId } })
}


