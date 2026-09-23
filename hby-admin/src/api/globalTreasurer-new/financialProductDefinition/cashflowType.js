/**
 * 理财产品定义模块 - 现金流类型管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/cashflow-type'

// 分页查询现金流类型列表
export function getCashflowTypeList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询现金流类型详情
export function getCashflowTypeById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增现金流类型
export function createCashflowType(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改现金流类型
export function updateCashflowType(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除现金流类型
export function deleteCashflowType(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除现金流类型
export function batchDeleteCashflowType(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新现金流类型状态
export function updateCashflowTypeStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的现金流类型列表
export function getEnabledCashflowTypeList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 获取现金流类型树形结构
export function getCashflowTypeTree(orgId) {
  return request({ url: `${BASE_URL}/getTree`, method: 'get', params: { orgId } })
}

// 检查现金流类型编码唯一性
export function checkCashflowTypeCodeUnique(cashflowTypeCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { cashflowTypeCode, excludeId } })
}

// 根据方向获取现金流类型列表
export function getCashflowTypeByDirection(direction) {
  return request({ url: `${BASE_URL}/getByDirection`, method: 'get', params: { direction } })
}

// 根据分类获取现金流类型列表
export function getCashflowTypeByCategory(category) {
  return request({ url: `${BASE_URL}/getByCategory`, method: 'get', params: { category } })
}

