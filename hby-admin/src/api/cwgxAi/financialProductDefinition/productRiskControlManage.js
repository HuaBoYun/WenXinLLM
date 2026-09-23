import request from '@/utils/request'

// 产品风控管理API

export function getProductRiskControlList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/getList',
    method: 'post',
    data: params
  })
}

export function createProductRiskControl(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/create',
    method: 'post',
    data: data
  })
}

export function updateProductRiskControl(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/update',
    method: 'post',
    data: data
  })
}

export function deleteProductRiskControl(riskControlId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/delete',
    method: 'post',
    data: { riskControlId: riskControlId }
  })
}

export function exportProductRiskControls(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

export function getProductRiskControlStatistics() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/getStatistics',
    method: 'post'
  })
}

export function getRiskAlertList() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productRiskControlManage/getRiskAlertList',
    method: 'post'
  })
}