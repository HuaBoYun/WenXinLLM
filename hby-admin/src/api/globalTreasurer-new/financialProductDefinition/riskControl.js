/**
 * 理财产品定义模块 - 产品风控规则管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/product-risk-control'

// 分页查询产品风控规则列表
export function getRiskControlList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询产品风控规则详情
export function getRiskControlById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增产品风控规则
export function createRiskControl(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改产品风控规则
export function updateRiskControl(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除产品风控规则
export function deleteRiskControl(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除产品风控规则
export function batchDeleteRiskControl(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新产品风控规则状态
export function updateRiskControlStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的产品风控规则列表
export function getEnabledRiskControlList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 根据产品类型获取风控规则列表
export function getRiskControlByProductType(productType) {
  return request({ url: `${BASE_URL}/getByProductType`, method: 'get', params: { productType } })
}

// 检查产品风控规则编码唯一性
export function checkRiskControlCodeUnique(ruleCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { ruleCode, excludeId } })
}

