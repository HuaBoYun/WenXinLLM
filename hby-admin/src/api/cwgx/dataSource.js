import request from '@/utils/request'

// 获取数据源配置列表
export function getDataSourceList(params) {
  return request({
    url: '/cwgxAi/data-source/config/list',
    method: 'post',
    data: params
  })
}

// 获取数据源配置详情
export function getDataSourceConfig(configId) {
  return request({
    url: `/cwgxAi/data-source/config/${configId}`,
    method: 'get'
  })
}

// 保存数据源配置
export function saveDataSourceConfig(data) {
  return request({
    url: '/cwgxAi/data-source/config/save',
    method: 'post',
    data: data
  })
}

// 更新数据源配置
export function updateDataSourceConfig(configId, data) {
  return request({
    url: `/cwgxAi/data-source/config/${configId}`,
    method: 'put',
    data: data
  })
}

// 删除数据源配置
export function deleteDataSourceConfig(configId) {
  return request({
    url: `/cwgxAi/data-source/config/${configId}`,
    method: 'delete'
  })
}

// 测试数据源连接
export function testDataSourceConnection(data) {
  return request({
    url: '/cwgxAi/data-source/config/test',
    method: 'post',
    data: data
  })
}

// 预览数据源数据
export function previewDataSourceData(data) {
  return request({
    url: '/cwgxAi/data-source/config/preview',
    method: 'post',
    data: data
  })
}

// 获取支持的数据源类型
export function getDataSourceTypes() {
  return request({
    url: '/cwgxAi/data-source/types',
    method: 'get'
  })
}

// 批量操作数据源
export function batchOperationDataSource(data) {
  return request({
    url: '/cwgxAi/data-source/config/batch',
    method: 'post',
    data: data
  })
}

// 导入数据源配置
export function importDataSourceConfig(data) {
  return request({
    url: '/cwgxAi/data-source/import',
    method: 'post',
    data: data
  })
}

// 导出数据源配置
export function exportDataSourceConfig(data) {
  return request({
    url: '/cwgxAi/data-source/export',
    method: 'post',
    data: data
  })
}