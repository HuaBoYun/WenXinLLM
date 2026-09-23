import request from '@/utils/request'

// ========== 基础CRUD操作 ==========

/**
 * 分页查询财务类别列表
 */
export function getFinancialCategoryList(params) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/getList',
    method: 'post',
    params
  })
}

/**
 * 根据ID查询财务类别详情
 */
export function getFinancialCategoryById(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/getById',
    method: 'get',
    params: { ID }
  })
}

/**
 * 新增财务类别
 */
export function createFinancialCategory(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/create',
    method: 'post',
    data
  })
}

/**
 * 修改财务类别
 */
export function updateFinancialCategory(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/update',
    method: 'post',
    data
  })
}

/**
 * 删除财务类别
 */
export function deleteFinancialCategory(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/delete',
    method: 'post',
    params: { ID }
  })
}

/**
 * 批量删除财务类别
 */
export function batchDeleteFinancialCategories(IDs) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

/**
 * 启用/禁用财务类别
 */
export function updateFinancialCategoryStatus(ID, isEnabled) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

/**
 * 查询所有启用的财务类别
 */
export function getEnabledFinancialCategories() {
  return request({
    url: '/cwgxAi/product-definition/financial-category/getEnabledList',
    method: 'get'
  })
}

/**
 * 校验财务类别编码唯一性
 */
export function checkFinancialCategoryCodeUnique(categoryCode, excludeId) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/checkCodeUnique',
    method: 'get',
    params: { categoryCode, excludeId }
  })
}

// ========== 扩展功能操作 ==========

/**
 * 获取财务类别树形结构
 */
export function getFinancialCategoryTree(orgId) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/getTree',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据父级ID查询子级财务类别
 */
export function getFinancialCategoriesByParentId(parentId, orgId) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/getByParentId',
    method: 'get',
    params: { parentId, orgId }
  })
}

/**
 * 财务类别排序
 */
export function sortFinancialCategories(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/sort',
    method: 'post',
    data
  })
}

/**
 * 导出财务类别配置
 */
export function exportFinancialCategories(params) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 导入财务类别配置
 */
export function importFinancialCategories(formData) {
  return request({
    url: '/cwgxAi/product-definition/financial-category/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}