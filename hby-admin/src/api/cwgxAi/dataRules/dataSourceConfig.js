import request from '@/utils/request'

// 数据源配置管理 API接口 (cwgxAi版本)

export function getDataSourceConfigList(query) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/list',
    method: 'get',
    params: query
  })
}

export function createDataSourceConfig(data) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/save',
    method: 'post',
    data
  })
}

export function updateDataSourceConfig(data) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/update',
    method: 'post',
    data
  })
}

export function deleteDataSourceConfig(id) {
  return request({
    url: `/cwgxAi/data-rules/data-source-config/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteDataSourceConfig(ids) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/batchDelete',
    method: 'post',
    data: ids
  })
}

export function toggleDataSourceConfigStatus(data) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/toggleStatus',
    method: 'post',
    data
  })
}

export function exportDataSourceConfig(query) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

export function getDataSourceConfigById(id) {
  return request({
    url: `/cwgxAi/data-rules/data-source-config/${id}`,
    method: 'get'
  })
}

export function testConnection(data) {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/testConnection',
    method: 'post',
    data
  })
}

export function getDataSourceConfigByType(sourceType) {
  return request({
    url: `/cwgxAi/data-rules/data-source-config/byType/${sourceType}`,
    method: 'get'
  })
}

export function getEnabledDataSourceConfigs() {
  return request({
    url: '/cwgxAi/data-rules/data-source-config/enabled',
    method: 'get'
  })
}
