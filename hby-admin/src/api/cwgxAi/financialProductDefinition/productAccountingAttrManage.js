import request from '@/utils/request'

// 产品会计属性管理API

// 获取产品会计属性列表
export function getProductAccountingAttrList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/getList',
    method: 'post',
    data: params
  })
}

// 新增产品会计属性
export function createProductAccountingAttr(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/create',
    method: 'post',
    data: data
  })
}

// 更新产品会计属性
export function updateProductAccountingAttr(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/update',
    method: 'post',
    data: data
  })
}

// 删除产品会计属性
export function deleteProductAccountingAttr(attrId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/delete',
    method: 'post',
    data: { attrId: attrId }
  })
}

// 批量删除产品会计属性
export function batchDeleteProductAccountingAttrs(attrIds) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/batchDelete',
    method: 'post',
    data: { attrIds: attrIds }
  })
}

// 检查属性编码唯一性
export function checkAttrCodeUnique(attrCode, excludeId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/checkCodeUnique',
    method: 'post',
    data: { attrCode: attrCode, excludeId: excludeId }
  })
}

// 获取会计科目映射
export function getAccountingSubjectMapping() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/getSubjectMapping',
    method: 'post'
  })
}

// 导出产品会计属性
export function exportProductAccountingAttrs(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// 导入产品会计属性
export function importProductAccountingAttrs(formData) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取产品会计属性详情
export function getProductAccountingAttrDetail(attrId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/getDetail',
    method: 'post',
    data: { attrId: attrId }
  })
}

// 更新属性状态
export function updateProductAccountingAttrStatus(attrId, isEnabled) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/updateStatus',
    method: 'post',
    data: { attrId: attrId, isEnabled: isEnabled }
  })
}

// 获取统计信息
export function getProductAccountingAttrStatistics() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productAccountingAttrManage/getStatistics',
    method: 'post'
  })
}