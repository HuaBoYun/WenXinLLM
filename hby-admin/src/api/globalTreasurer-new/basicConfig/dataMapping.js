import request from '@/utils/request'

/**
 * 财资公共模块 - 数据映射配置 API
 */

// ==================== 数据映射配置管理 ====================

/**
 * 分页查询数据映射列表
 * @param {Object} params - 查询参数
 * @param {Number} params.page - 页码
 * @param {Number} params.pageSize - 每页数量
 * @param {String} params.mappingName - 映射名称
 * @param {String} params.sourceField - 源字段
 * @param {String} params.targetField - 目标字段
 * @param {String} params.status - 状态
 */
export function getDataMappingPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 获取数据映射详情
 * @param {Number|String} id - 映射ID
 */
export function getDataMappingDetail(id) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 新增数据映射
 * @param {Object} data - 数据映射配置
 */
export function createDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 更新数据映射
 * @param {Object} data - 数据映射配置
 */
export function updateDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/update',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 删除数据映射
 * @param {Number|String} id - 映射ID
 */
export function deleteDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/delete',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 测试映射规则
 * @param {Object} data - 测试参数
 * @param {Number|String} data.id - 映射ID
 * @param {Object} data.testData - 测试数据
 */
export function testMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/testMapping',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出映射配置
 * @param {Object} params - 查询参数
 */
export function exportDataMapping(params) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量导入映射配置
 * @param {FormData} formData - 文件表单数据
 */
export function importDataMapping(formData) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 根据系统ID获取映射列表
 * @param {Number|String} systemId - 系统ID
 */
export function getDataMappingBySystemId(systemId) {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/getBySystemId',
    method: 'get',
    params: { systemId }
  })
}

/**
 * 刷新数据映射配置
 * 从源系统获取最新字段信息,更新映射配置中的字段列表,标记失效的映射规则
 * @returns {Promise} 刷新结果
 */
export function refreshDataMapping() {
  return request({
    url: '/qqsk/financial/basicConfig/mapping/refresh',
    method: 'get'
  })
}
