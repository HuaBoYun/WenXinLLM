import request from '@/utils/request'

// 数据映射配置管理 API接口

/**
 * 分页查询数据映射列表
 */
export function listDataMapping(params) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/list',
    method: 'get',
    params
  })
}

/**
 * 分页查询数据映射列表
 */
export function pageDataMapping(params) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/page',
    method: 'get',
    params
  })
}

/**
 * 搜索数据映射列表
 */
export function searchDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/search',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID查询数据映射详情
 */
export function getDataMapping(id) {
  return request({
    url: `/qqsk/financial/basicConfig/dataMappingConfig/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 删除数据映射
 */
export function delDataMapping(id) {
  return request({
    url: `/qqsk/financial/basicConfig/dataMappingConfig/${id}`,
    method: 'delete'
  })
}

/**
 * 添加数据映射
 */
export function addDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 创建数据映射(别名)
 */
export function createDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新数据映射
 */
export function updateDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取初始化数据
 */
export function getInitData() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/initData',
    method: 'get'
  })
}

/**
 * 检查映射名称唯一性
 */
export function checkMappingName(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/checkName',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 验证字段映射
 */
export function validateFieldMappings(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/validateFields',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 测试数据映射规则 (旧版本接口)
 */
export function testDataMappingOld(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/test',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取映射类型选项
 */
export function getMappingTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/mappingTypes',
    method: 'get'
  })
}

/**
 * 获取源系统选项
 */
export function getSourceSystems() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/sourceSystems',
    method: 'get'
  })
}

/**
 * 获取目标系统选项
 */
export function getTargetSystems() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/targetSystems',
    method: 'get'
  })
}

/**
 * 获取源系统字段列表
 */
export function getSourceFields(systemId) {
  return request({
    url: `/qqsk/financial/basicConfig/dataMappingConfig/sourceFields?systemId=${systemId}`,
    method: 'get'
  })
}

/**
 * 获取目标系统字段列表
 */
export function getTargetFields(systemId) {
  return request({
    url: `/qqsk/financial/basicConfig/dataMappingConfig/targetFields?systemId=${systemId}`,
    method: 'get'
  })
}

/**
 * 批量删除数据映射
 */
export function batchDeleteDataMapping(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/batch',
    method: 'delete',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取数据映射统计信息
 */
export function getDataMappingStatistics() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/statistics',
    method: 'get'
  })
}

/**
 * 批量导入数据映射
 */
export function batchImportDataMapping(file, updateUser) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/import',
    method: 'post',
    data: formData,
    params: { updateUser },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取字段类型选项
 */
export function getFieldTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/fieldTypes',
    method: 'get'
  })
}

/**
 * 获取转换规则选项
 */
export function getTransformRules() {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/transformRules',
    method: 'get'
  })
}

/**
 * 测试数据映射
 */
export function testDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/test',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分页查询数据映射列表(新版本 - 页面使用)
 */
export function getDataMappingPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/page',
    method: 'get',
    params
  })
}

/**
 * 删除数据映射(别名 - 页面使用)
 */
export function deleteDataMapping(id) {
  return request({
    url: `/qqsk/financial/basicConfig/dataMappingConfig/${id}`,
    method: 'delete'
  })
}

/**
 * 测试数据映射(别名 - 页面使用)
 */
export function testMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMappingConfig/test',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}