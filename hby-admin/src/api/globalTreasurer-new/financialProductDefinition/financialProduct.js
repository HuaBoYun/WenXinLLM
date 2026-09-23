/**
 * 理财产品定义模块 - 金融产品管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/financial-product'

// 分页查询金融产品列表
export function getFinancialProductList(data) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', data })
}

// 根据ID查询金融产品详情
export function getFinancialProductById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增金融产品
export function createFinancialProduct(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改金融产品
export function updateFinancialProduct(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除金融产品
export function deleteFinancialProduct(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除金融产品
export function batchDeleteFinancialProduct(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新金融产品状态
export function updateFinancialProductStatus(ID, productStatus) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, productStatus } })
}

// 更新金融产品上下架状态
export function updateFinancialProductShelfStatus(ID, shelfStatus) {
  return request({ url: `${BASE_URL}/updateShelfStatus`, method: 'post', params: { ID, shelfStatus } })
}

// 获取启用的金融产品列表
export function getEnabledFinancialProductList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 检查金融产品编码唯一性
export function checkFinancialProductCodeUnique(productCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { productCode, excludeId } })
}

// 获取金融产品统计信息
export function getFinancialProductStatistics() {
  return request({ url: `${BASE_URL}/getStatistics`, method: 'get' })
}

