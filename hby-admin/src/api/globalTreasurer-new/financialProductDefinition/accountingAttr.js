/**
 * 理财产品定义模块 - 产品核算属性管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/product-accounting-attr'

// 分页查询产品核算属性列表
export function getAccountingAttrList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询产品核算属性详情
export function getAccountingAttrById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增产品核算属性
export function createAccountingAttr(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改产品核算属性
export function updateAccountingAttr(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除产品核算属性
export function deleteAccountingAttr(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除产品核算属性
export function batchDeleteAccountingAttr(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新产品核算属性状态
export function updateAccountingAttrStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的产品核算属性列表
export function getEnabledAccountingAttrList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 根据产品类型获取核算属性列表
export function getAccountingAttrByProductType(productType) {
  return request({ url: `${BASE_URL}/getByProductType`, method: 'get', params: { productType } })
}

// 检查产品核算属性编码唯一性
export function checkAccountingAttrCodeUnique(attrCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { attrCode, excludeId } })
}

