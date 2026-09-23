// 第三批次金融产品定义模块API接口汇总
// 包含10个页面的所有API接口定义

import request from '@/utils/request'

// ===== 1. 现金流类型管理 (已完成) =====
export * from './cashflowTypeManage'

// ===== 2. 财务类别管理 =====
export * from './financialCategoryManage'

// ===== 3. 金融产品管理 =====
export function getFinancialProductList(params) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getList',
    method: 'post',
    params
  })
}

export function getFinancialProductById(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/getById',
    method: 'get',
    params: { ID }
  })
}

export function createFinancialProduct(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/create',
    method: 'post',
    data
  })
}

export function updateFinancialProduct(data) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/update',
    method: 'post',
    data
  })
}

export function deleteFinancialProduct(ID) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/delete',
    method: 'post',
    params: { ID }
  })
}

export function batchDeleteFinancialProducts(IDs) {
  return request({
    url: '/cwgxAi/product-definition/financial-product/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

// ===== 4. 产品会计属性管理 =====
export function getProductAccountingAttrList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-accounting-attr/getList',
    method: 'post',
    params
  })
}

export function getProductAccountingAttrById(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-accounting-attr/getById',
    method: 'get',
    params: { ID }
  })
}

export function createProductAccountingAttr(data) {
  return request({
    url: '/cwgxAi/product-definition/product-accounting-attr/create',
    method: 'post',
    data
  })
}

export function updateProductAccountingAttr(data) {
  return request({
    url: '/cwgxAi/product-definition/product-accounting-attr/update',
    method: 'post',
    data
  })
}

export function deleteProductAccountingAttr(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-accounting-attr/delete',
    method: 'post',
    params: { ID }
  })
}

// ===== 5. 产品利率规则管理 =====
export function getProductInterestRuleList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/getList',
    method: 'post',
    params
  })
}

export function getProductInterestRuleById(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/getById',
    method: 'get',
    params: { ID }
  })
}

export function createProductInterestRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/create',
    method: 'post',
    data
  })
}

export function updateProductInterestRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/update',
    method: 'post',
    data
  })
}

export function deleteProductInterestRule(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-interest-rule/delete',
    method: 'post',
    params: { ID }
  })
}

// ===== 6. 产品本金规则管理 =====
export function getProductPrincipalRuleList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-principal-rule/getList',
    method: 'post',
    params
  })
}

export function getProductPrincipalRuleById(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-principal-rule/getById',
    method: 'get',
    params: { ID }
  })
}

export function createProductPrincipalRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-principal-rule/create',
    method: 'post',
    data
  })
}

export function updateProductPrincipalRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-principal-rule/update',
    method: 'post',
    data
  })
}

export function deleteProductPrincipalRule(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-principal-rule/delete',
    method: 'post',
    params: { ID }
  })
}

// ===== 7. 产品风控规则管理 =====
export function getProductRiskControlRuleList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-risk-control-rule/getList',
    method: 'post',
    params
  })
}

export function getProductRiskControlRuleById(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-risk-control-rule/getById',
    method: 'get',
    params: { ID }
  })
}

export function createProductRiskControlRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-risk-control-rule/create',
    method: 'post',
    data
  })
}

export function updateProductRiskControlRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-risk-control-rule/update',
    method: 'post',
    data
  })
}

export function deleteProductRiskControlRule(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-risk-control-rule/delete',
    method: 'post',
    params: { ID }
  })
}

// ===== 8. 产品期限规则管理 =====
export function getProductTermRuleList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-term-rule/getList',
    method: 'post',
    params
  })
}

export function getProductTermRuleById(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-term-rule/getById',
    method: 'get',
    params: { ID }
  })
}

export function createProductTermRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-term-rule/create',
    method: 'post',
    data
  })
}

export function updateProductTermRule(data) {
  return request({
    url: '/cwgxAi/product-definition/product-term-rule/update',
    method: 'post',
    data
  })
}

export function deleteProductTermRule(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-term-rule/delete',
    method: 'post',
    params: { ID }
  })
}

// ===== 9. 产品交易事件管理 =====
export function getProductTransactionEventList(params) {
  return request({
    url: '/cwgxAi/product-definition/product-transaction-event/getList',
    method: 'post',
    params
  })
}

export function getProductTransactionEventById(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-transaction-event/getById',
    method: 'get',
    params: { ID }
  })
}

export function createProductTransactionEvent(data) {
  return request({
    url: '/cwgxAi/product-definition/product-transaction-event/create',
    method: 'post',
    data
  })
}

export function updateProductTransactionEvent(data) {
  return request({
    url: '/cwgxAi/product-definition/product-transaction-event/update',
    method: 'post',
    data
  })
}

export function deleteProductTransactionEvent(ID) {
  return request({
    url: '/cwgxAi/product-definition/product-transaction-event/delete',
    method: 'post',
    params: { ID }
  })
}

// ===== 10. 交易类型管理 =====
export function getTransactionTypeList(params) {
  return request({
    url: '/cwgxAi/product-definition/transaction-type/getList',
    method: 'post',
    params
  })
}

export function getTransactionTypeById(ID) {
  return request({
    url: '/cwgxAi/product-definition/transaction-type/getById',
    method: 'get',
    params: { ID }
  })
}

export function createTransactionType(data) {
  return request({
    url: '/cwgxAi/product-definition/transaction-type/create',
    method: 'post',
    data
  })
}

export function updateTransactionType(data) {
  return request({
    url: '/cwgxAi/product-definition/transaction-type/update',
    method: 'post',
    data
  })
}

export function deleteTransactionType(ID) {
  return request({
    url: '/cwgxAi/product-definition/transaction-type/delete',
    method: 'post',
    params: { ID }
  })
}

export function batchDeleteTransactionTypes(IDs) {
  return request({
    url: '/cwgxAi/product-definition/transaction-type/batchDelete',
    method: 'post',
    params: { IDs }
  })
}