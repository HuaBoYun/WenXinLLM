import request from '@/utils/request'

/**
 * 固定资产卡片管理API
 */

// 获取资产卡片列表
export function getAssetCardList(params) {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards',
    method: 'get',
    params
  })
}

// 获取资产卡片详情
export function getAssetCardById(assetId) {
  return request({
    url: `/cwgxAi/financial/fixed-assets/assetCards/${assetId}`,
    method: 'get'
  })
}

// 新增资产卡片
export function createAssetCard(data) {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

// 更新资产卡片
export function updateAssetCard(assetId, data) {
  return request({
    url: `/cwgxAi/financial/fixed-assets/assetCards/${assetId}`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

// 删除资产卡片
export function deleteAssetCard(assetId) {
  return request({
    url: `/cwgxAi/financial/fixed-assets/assetCards/${assetId}`,
    method: 'delete'
  })
}

// 批量删除资产卡片
export function batchDeleteAssetCard(assetIds) {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards/batch-delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: { assetIds }
  })
}

// 获取资产汇总信息
export function getAssetSummary() {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards/summary',
    method: 'get'
  })
}

// 按类别统计资产
export function getAssetByCategory() {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards/by-category',
    method: 'get'
  })
}

// 检查资产编码是否存在
export function checkAssetCodeExists(assetCode, assetId) {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards/check-code',
    method: 'get',
    params: { assetCode, assetId }
  })
}

// 导出资产卡片
export function exportAssetCard(params) {
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 导入资产卡片
export function importAssetCard(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/cwgxAi/financial/fixed-assets/assetCards/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

