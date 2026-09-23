import request from '@/utils/request'

// 产品会计属性管理API

// 获取产品会计属性列表
export function getProductAccountingAttrList(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/getList',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  })
}

// 新增产品会计属性
export function createProductAccountingAttr(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/create',
    method: 'post',
    data: data
  })
}

// 更新产品会计属性
export function updateProductAccountingAttr(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/update',
    method: 'post',
    data: data
  })
}

// 删除产品会计属性
export function deleteProductAccountingAttr(ID) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/delete',
    method: 'post',
    params: { ID }
  })
}

// 批量删除产品会计属性
export function batchDeleteProductAccountingAttrs(IDs) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

// 检查属性编码唯一性
export function checkAttrCodeUnique(attrCode, excludeId) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/checkCodeUnique',
    method: 'get',
    params: { attrCode, excludeId }
  })
}

// 获取会计科目映射
export function getAccountingSubjectMapping() {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/getSubjectMapping',
    method: 'post'
  })
}

// 导出产品会计属性
export function exportProductAccountingAttrs(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

// 导入产品会计属性
export function importProductAccountingAttrs(formData) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取产品会计属性详情
export function getProductAccountingAttrDetail(ID) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/getDetail',
    method: 'get',
    params: { ID }
  })
}

// 更新属性状态
export function updateProductAccountingAttrStatus(ID, isEnabled) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

// 获取统计信息
export function getProductAccountingAttrStatistics() {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/getStatistics',
    method: 'get'
  })
}

// 批量更新科目映射
export function batchUpdateMapping(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-accounting-attr/batchUpdateMapping',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
