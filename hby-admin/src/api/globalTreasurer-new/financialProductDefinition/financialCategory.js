/**
 * 理财产品定义模块 - 金融分类管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/financial-category'

// 分页查询金融分类列表
export function getFinancialCategoryList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询金融分类详情
export function getFinancialCategoryById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增金融分类
export function createFinancialCategory(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改金融分类
export function updateFinancialCategory(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除金融分类
export function deleteFinancialCategory(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除金融分类
export function batchDeleteFinancialCategory(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新金融分类状态
export function updateFinancialCategoryStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的金融分类列表
export function getEnabledFinancialCategoryList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 获取金融分类树形结构
export function getFinancialCategoryTree(orgId) {
  return request({ url: `${BASE_URL}/getTree`, method: 'get', params: { orgId } })
}

// 检查金融分类编码唯一性
export function checkFinancialCategoryCodeUnique(categoryCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { categoryCode, excludeId } })
}

// 根据父ID获取子分类列表
export function getFinancialCategoryByParentId(parentId) {
  return request({ url: `${BASE_URL}/getByParentId`, method: 'get', params: { parentId } })
}

// 金融分类排序
export function sortFinancialCategories(list) {
  return request({ url: `${BASE_URL}/sort`, method: 'post', data: list })
}

// 导出金融分类配置
export function exportFinancialCategories(params) {
  return request({ url: `${BASE_URL}/export`, method: 'post', params, responseType: 'blob' })
}

