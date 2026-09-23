import request from '@/utils/request'

// 获取字段映射列表
export function getFieldMappingList(params) {
  return request({
    url: '/cwgxAi/field-mapping/list',
    method: 'post',
    data: params
  })
}

// 获取字段映射详情
export function getFieldMapping(mappingId) {
  return request({
    url: `/cwgxAi/field-mapping/${mappingId}`,
    method: 'get'
  })
}

// 保存字段映射
export function saveFieldMapping(data) {
  return request({
    url: '/cwgxAi/field-mapping/save',
    method: 'post',
    data: data
  })
}

// 更新字段映射
export function updateFieldMapping(mappingId, data) {
  return request({
    url: `/cwgxAi/field-mapping/${mappingId}`,
    method: 'put',
    data: data
  })
}

// 删除字段映射
export function deleteFieldMapping(mappingId) {
  return request({
    url: `/cwgxAi/field-mapping/${mappingId}`,
    method: 'delete'
  })
}

// 根据数据源ID查询字段映射
export function getFieldMappingByDataSourceId(dataSourceId) {
  return request({
    url: `/cwgxAi/field-mapping/datasource/${dataSourceId}`,
    method: 'get'
  })
}

// 批量操作字段映射
export function batchOperationFieldMapping(data) {
  return request({
    url: '/cwgxAi/field-mapping/batch',
    method: 'post',
    data: data
  })
}

// 导入字段映射
export function importFieldMapping(data) {
  return request({
    url: '/cwgxAi/field-mapping/import',
    method: 'post',
    data: data
  })
}

// 导出字段映射
export function exportFieldMapping(dataSourceId) {
  return request({
    url: `/cwgxAi/field-mapping/export/${dataSourceId}`,
    method: 'get'
  })
}

// 复制字段映射
export function copyFieldMapping(sourceDataSourceId, targetDataSourceId) {
  return request({
    url: '/cwgxAi/field-mapping/copy',
    method: 'post',
    params: {
      sourceDataSourceId,
      targetDataSourceId
    }
  })
}

// 更新字段映射排序
export function updateSortOrder(mappingId, sortOrder) {
  return request({
    url: '/cwgxAi/field-mapping/sort',
    method: 'post',
    params: {
      mappingId,
      sortOrder
    }
  })
}