import request from '@/utils/request'

// ========== 基础CRUD操作 ==========

/**
 * 分页查询现金流类型列表
 */
export function getCashflowTypeList(params) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getList',
    method: 'post',
    params
  })
}

/**
 * 根据ID查询现金流类型详情
 */
export function getCashflowTypeById(ID) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getById',
    method: 'get',
    params: { ID }
  })
}

/**
 * 新增现金流类型
 */
export function createCashflowType(data) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/create',
    method: 'post',
    data
  })
}

/**
 * 修改现金流类型
 */
export function updateCashflowType(data) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/update',
    method: 'post',
    data
  })
}

/**
 * 删除现金流类型
 */
export function deleteCashflowType(ID) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/delete',
    method: 'post',
    params: { ID }
  })
}

/**
 * 批量删除现金流类型
 */
export function batchDeleteCashflowTypes(IDs) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

/**
 * 启用/禁用现金流类型
 */
export function updateCashflowTypeStatus(ID, isEnabled) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

/**
 * 查询所有启用的现金流类型
 */
export function getEnabledCashflowTypes() {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getEnabledList',
    method: 'get'
  })
}

/**
 * 校验现金流类型编码唯一性
 */
export function checkCashflowTypeCodeUnique(cashflowTypeCode, excludeId) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/checkCodeUnique',
    method: 'get',
    params: { cashflowTypeCode, excludeId }
  })
}

// ========== 扩展功能操作 ==========

/**
 * 获取现金流类型树形结构
 */
export function getCashflowTypeTree(orgId) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getTree',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据父级ID查询子级现金流类型
 */
export function getCashflowTypesByParentId(parentId, orgId) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getByParentId',
    method: 'get',
    params: { parentId, orgId }
  })
}

/**
 * 现金流类型排序
 */
export function sortCashflowTypes(data) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/sort',
    method: 'post',
    data
  })
}

/**
 * 导出现金流类型配置
 */
export function exportCashflowTypes(params) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 导入现金流类型配置
 */
export function importCashflowTypes(formData) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取现金流类型统计信息
 */
export function getCashflowTypeStatistics(orgId) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getStatistics',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 复制现金流类型
 */
export function copyCashflowType(ID, newCashflowTypeCode, newCashflowTypeName) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/copy',
    method: 'post',
    data: { ID, newCashflowTypeCode, newCashflowTypeName }
  })
}

/**
 * 批量修改状态
 */
export function batchUpdateStatus(IDs, isEnabled) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/batchUpdateStatus',
    method: 'post',
    data: { IDs, isEnabled }
  })
}

/**
 * 获取现金流类型详情（包含关联信息）
 */
export function getCashflowTypeDetail(ID) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getDetail',
    method: 'get',
    params: { ID }
  })
}

/**
 * 验证现金流类型是否可以删除
 */
export function validateCashflowTypeDelete(ID) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/validateDelete',
    method: 'get',
    params: { ID }
  })
}

/**
 * 获取现金流类型使用情况
 */
export function getCashflowTypeUsage(ID) {
  return request({
    url: '/qqsk/financial/product-definition/cashflow-type/getUsage',
    method: 'get',
    params: { ID }
  })
}