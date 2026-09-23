import request from '@/utils/request'

// ========== 基础CRUD操作 ==========

/**
 * 分页查询金融产品列表
 */
export function getFinancialProductList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/financialProductManage/getList',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询金融产品详情
 */
export function getFinancialProductById(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getById',
    method: 'get',
    params: { ID }
  })
}

/**
 * 新增金融产品
 */
export function createFinancialProduct(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/create',
    method: 'post',
    data
  })
}

/**
 * 修改金融产品
 */
export function updateFinancialProduct(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/update',
    method: 'post',
    data
  })
}

/**
 * 删除金融产品
 */
export function deleteFinancialProduct(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/delete',
    method: 'post',
    params: { ID }
  })
}

/**
 * 批量删除金融产品
 */
export function batchDeleteFinancialProducts(IDs) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

/**
 * 启用/禁用金融产品
 */
export function updateFinancialProductStatus(ID, isEnabled) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

/**
 * 查询所有启用的金融产品
 */
export function getEnabledFinancialProducts() {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getEnabledList',
    method: 'get'
  })
}

/**
 * 校验金融产品编码唯一性
 */
export function checkFinancialProductCodeUnique(productCode, excludeId) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/checkCodeUnique',
    method: 'get',
    params: { productCode, excludeId }
  })
}

// ========== 扩展功能操作 ==========

/**
 * 复制金融产品
 */
export function copyFinancialProduct(ID, newProductCode, newProductName) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/copy',
    method: 'post',
    data: { ID, newProductCode, newProductName }
  })
}

/**
 * 导出金融产品配置
 */
export function exportFinancialProducts(params) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 导入金融产品配置
 */
export function importFinancialProducts(formData) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取金融产品统计信息
 */
export function getFinancialProductStatistics(orgId) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getStatistics',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 获取金融产品详情（包含关联信息）
 */
export function getFinancialProductDetail(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getDetail',
    method: 'get',
    params: { ID }
  })
}

/**
 * 验证金融产品是否可以删除
 */
export function validateFinancialProductDelete(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/validateDelete',
    method: 'get',
    params: { ID }
  })
}

/**
 * 获取金融产品使用情况
 */
export function getFinancialProductUsage(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getUsage',
    method: 'get',
    params: { ID }
  })
}

/**
 * 批量修改状态
 */
export function batchUpdateStatus(IDs, isEnabled) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/batchUpdateStatus',
    method: 'post',
    data: { IDs, isEnabled }
  })
}

/**
 * 产品上下架
 */
export function updateProductShelfStatus(ID, shelfStatus) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/updateShelfStatus',
    method: 'post',
    params: { ID, shelfStatus }
  })
}

/**
 * 获取产品风险等级列表
 */
export function getRiskLevelList() {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getRiskLevelList',
    method: 'get'
  })
}

/**
 * 获取产品类型列表
 */
export function getProductTypeList() {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getProductTypeList',
    method: 'get'
  })
}

/**
 * 获取币种列表
 */
export function getCurrencyList() {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getCurrencyList',
    method: 'get'
  })
}