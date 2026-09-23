import request from '@/utils/request'

/**
 * 凭证模板管理 API
 */

// 分页查询凭证模板列表
export function getVoucherTemplateList(params) {
  return request({
    url: '/cwgxAi/voucher-template/getList',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 根据ID查询凭证模板详情
export function getVoucherTemplateById(templateId) {
  return request({
    url: `/cwgxAi/voucher-template/${templateId}`,
    method: 'get'
  })
}

// 保存或更新凭证模板
export function saveOrUpdateVoucherTemplate(data) {
  return request({
    url: '/cwgxAi/voucher-template/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 删除凭证模板
export function deleteVoucherTemplate(templateId) {
  return request({
    url: `/cwgxAi/voucher-template/${templateId}`,
    method: 'delete'
  })
}

// 复制凭证模板
export function copyVoucherTemplate(templateId, data) {
  return request({
    url: `/cwgxAi/voucher-template/${templateId}/copy`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 测试凭证模板
export function testVoucherTemplate(templateId, testData) {
  return request({
    url: `/cwgxAi/voucher-template/test/${templateId}`,
    method: 'post',
    data: testData
  })
}

// 获取模板版本列表
export function getTemplateVersions(templateId) {
  return request({
    url: `/cwgxAi/voucher-template/versions/${templateId}`,
    method: 'get'
  })
}

// 导入凭证模板
export function importVoucherTemplate(fileData) {
  return request({
    url: '/cwgxAi/voucher-template/import',
    method: 'post',
    data: { fileData }
  })
}

// 导出凭证模板
export function exportVoucherTemplate(templateIds) {
  return request({
    url: '/cwgxAi/voucher-template/export',
    method: 'post',
    data: templateIds
  })
}

// 批量更新模板状态
export function batchUpdateTemplateStatus(templateIds, status) {
  return request({
    url: '/cwgxAi/voucher-template/batchUpdateStatus',
    method: 'put',
    data: templateIds,
    params: { status }
  })
}

// 获取模板类型列表
export function getTemplateTypes() {
  return request({
    url: '/cwgxAi/voucher-template/templateTypes',
    method: 'get'
  })
}