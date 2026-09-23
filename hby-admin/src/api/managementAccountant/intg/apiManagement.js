import request from '@/utils/request'

// API接口管理相关接口

// 基础CRUD操作

/**
 * 创建API管理
 * @param {Object} data API管理数据
 */
export function createApiManagement(data) {
  return request({
    url: '/api/intg/api-management/create',
    method: 'post',
    data
  })
}

/**
 * 更新API管理
 * @param {Object} data API管理数据
 */
export function updateApiManagement(data) {
  return request({
    url: '/api/intg/api-management/update',
    method: 'put',
    data
  })
}

/**
 * 删除API管理
 * @param {String} apiId API ID
 */
export function deleteApiManagement(apiId) {
  return request({
    url: `/api/intg/api-management/delete/${apiId}`,
    method: 'delete'
  })
}

/**
 * 根据ID获取API管理
 * @param {String} apiId API ID
 */
export function getApiManagementById(apiId) {
  return request({
    url: `/api/intg/api-management/get/${apiId}`,
    method: 'get'
  })
}

/**
 * 根据编码获取API管理
 * @param {String} apiCode API编码
 */
export function getApiManagementByCode(apiCode) {
  return request({
    url: `/api/intg/api-management/get-by-code/${apiCode}`,
    method: 'get'
  })
}

// 查询操作

/**
 * 分页查询API管理
 * @param {Object} params 查询参数
 */
export function getApiManagementPage(params) {
  return request({
    url: '/api/intg/api-management/page',
    method: 'get',
    params
  })
}

/**
 * 根据API类型获取列表
 * @param {String} apiType API类型
 */
export function getApiManagementsByType(apiType) {
  return request({
    url: `/api/intg/api-management/list-by-type/${apiType}`,
    method: 'get'
  })
}

/**
 * 根据API分类获取列表
 * @param {String} apiCategory API分类
 */
export function getApiManagementsByCategory(apiCategory) {
  return request({
    url: `/api/intg/api-management/list-by-category/${apiCategory}`,
    method: 'get'
  })
}

/**
 * 根据状态获取列表
 * @param {String} status 状态
 */
export function getApiManagementsByStatus(status) {
  return request({
    url: `/api/intg/api-management/list-by-status/${status}`,
    method: 'get'
  })
}

/**
 * 获取启用的API列表
 */
export function getActiveApiManagements() {
  return request({
    url: '/api/intg/api-management/list-active',
    method: 'get'
  })
}

/**
 * 获取已废弃的API列表
 */
export function getDeprecatedApiManagements() {
  return request({
    url: '/api/intg/api-management/list-deprecated',
    method: 'get'
  })
}

// API管理操作

/**
 * 启用API
 * @param {String} apiId API ID
 */
export function enableApiManagement(apiId) {
  return request({
    url: `/api/intg/api-management/enable/${apiId}`,
    method: 'put'
  })
}

/**
 * 禁用API
 * @param {String} apiId API ID
 */
export function disableApiManagement(apiId) {
  return request({
    url: `/api/intg/api-management/disable/${apiId}`,
    method: 'put'
  })
}

/**
 * 设置API为废弃
 * @param {String} apiId API ID
 * @param {String} deprecationDate 废弃日期
 * @param {String} replacementApiId 替换API ID
 */
export function deprecateApiManagement(apiId, deprecationDate, replacementApiId) {
  return request({
    url: `/api/intg/api-management/deprecate/${apiId}`,
    method: 'put',
    params: {
      deprecationDate,
      replacementApiId
    }
  })
}

/**
 * 批量更新API状态
 * @param {Array} apiIds API ID列表
 * @param {String} status 状态
 */
export function batchUpdateApiStatus(apiIds, status) {
  return request({
    url: '/api/intg/api-management/batch-update-status',
    method: 'put',
    data: {
      apiIds,
      status
    }
  })
}

/**
 * 批量启用API
 * @param {Array} apiIds API ID列表
 */
export function batchEnableApiManagements(apiIds) {
  return request({
    url: '/api/intg/api-management/batch-enable',
    method: 'put',
    data: apiIds
  })
}

/**
 * 批量禁用API
 * @param {Array} apiIds API ID列表
 */
export function batchDisableApiManagements(apiIds) {
  return request({
    url: '/api/intg/api-management/batch-disable',
    method: 'put',
    data: apiIds
  })
}

// API版本管理

/**
 * 获取API的所有版本
 * @param {String} apiCode API编码
 */
export function getApiVersions(apiCode) {
  return request({
    url: `/api/intg/api-management/versions/${apiCode}`,
    method: 'get'
  })
}

/**
 * 获取API的最新版本
 * @param {String} apiCode API编码
 */
export function getLatestApiVersion(apiCode) {
  return request({
    url: `/api/intg/api-management/latest-version/${apiCode}`,
    method: 'get'
  })
}

/**
 * 创建API新版本
 * @param {String} baseApiId 基础API ID
 * @param {String} newVersion 新版本号
 */
export function createApiVersion(baseApiId, newVersion) {
  return request({
    url: '/api/intg/api-management/create-version',
    method: 'post',
    params: {
      baseApiId,
      newVersion
    }
  })
}

/**
 * 发布API版本
 * @param {String} apiId API ID
 */
export function publishApiVersion(apiId) {
  return request({
    url: `/api/intg/api-management/publish-version/${apiId}`,
    method: 'put'
  })
}

// API查找操作

/**
 * 根据关键词搜索API
 * @param {String} keyword 关键词
 */
export function searchApiManagementsByKeyword(keyword) {
  return request({
    url: '/api/intg/api-management/search-by-keyword',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 根据标签搜索API
 * @param {Array} tags 标签列表
 */
export function searchApiManagementsByTags(tags) {
  return request({
    url: '/api/intg/api-management/search-by-tags',
    method: 'post',
    data: tags
  })
}

// API验证操作

/**
 * 验证API配置
 * @param {Object} apiManagement API管理数据
 */
export function validateApiConfiguration(apiManagement) {
  return request({
    url: '/api/intg/api-management/validate-configuration',
    method: 'post',
    data: apiManagement
  })
}

/**
 * 测试API连接
 * @param {String} apiId API ID
 */
export function testApiConnection(apiId) {
  return request({
    url: `/api/intg/api-management/test-connection/${apiId}`,
    method: 'post'
  })
}

// 统计分析操作

/**
 * 统计API总数
 */
export function countApiManagements() {
  return request({
    url: '/api/intg/api-management/count',
    method: 'get'
  })
}

/**
 * 按API类型统计数量
 */
export function countByApiType() {
  return request({
    url: '/api/intg/api-management/count-by-type',
    method: 'get'
  })
}

/**
 * 按状态统计数量
 */
export function countByStatus() {
  return request({
    url: '/api/intg/api-management/count-by-status',
    method: 'get'
  })
}

/**
 * 获取系统概览信息
 */
export function getSystemOverview() {
  return request({
    url: '/api/intg/api-management/system-overview',
    method: 'get'
  })
}

// 常量定义

// API类型
export const API_TYPES = {
  REST: 'REST',
  SOAP: 'SOAP',
  GRAPHQL: 'GraphQL',
  RPC: 'RPC'
}

// API分类
export const API_CATEGORIES = {
  BUSINESS: 'BUSINESS',
  SYSTEM: 'SYSTEM',
  INTEGRATION: 'INTEGRATION'
}

// HTTP方法
export const HTTP_METHODS = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE',
  PATCH: 'PATCH'
}

// API状态
export const API_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  TESTING: 'TESTING'
}

// 请求/响应格式
export const DATA_FORMATS = {
  JSON: 'JSON',
  XML: 'XML',
  FORM: 'FORM',
  MULTIPART: 'MULTIPART',
  TEXT: 'TEXT',
  BINARY: 'BINARY'
}

// 日志级别
export const LOG_LEVELS = {
  DEBUG: 'DEBUG',
  INFO: 'INFO',
  WARN: 'WARN',
  ERROR: 'ERROR'
}

// 安全级别
export const SECURITY_LEVELS = {
  PUBLIC: 'PUBLIC',
  INTERNAL: 'INTERNAL',
  CONFIDENTIAL: 'CONFIDENTIAL',
  SECRET: 'SECRET',
  TOP_SECRET: 'TOP_SECRET'
}

// 优先级
export const PRIORITIES = {
  HIGH: 'HIGH',
  MEDIUM: 'MEDIUM',
  LOW: 'LOW'
}

// 工具函数

/**
 * 格式化API类型显示文本
 * @param {String} apiType API类型
 */
export function formatApiType(apiType) {
  const typeMap = {
    'REST': 'REST API',
    'SOAP': 'SOAP API',
    'GRAPHQL': 'GraphQL API',
    'RPC': 'RPC API'
  }
  return typeMap[apiType] || apiType
}

/**
 * 格式化API分类显示文本
 * @param {String} apiCategory API分类
 */
export function formatApiCategory(apiCategory) {
  const categoryMap = {
    'BUSINESS': '业务接口',
    'SYSTEM': '系统接口',
    'INTEGRATION': '集成接口'
  }
  return categoryMap[apiCategory] || apiCategory
}

/**
 * 格式化API状态显示文本
 * @param {String} status 状态
 */
export function formatApiStatus(status) {
  const statusMap = {
    'ACTIVE': '激活',
    'INACTIVE': '停用',
    'TESTING': '测试中'
  }
  return statusMap[status] || status
}

/**
 * 获取API状态对应的标签类型
 * @param {String} status 状态
 */
export function getApiStatusTagType(status) {
  const tagTypeMap = {
    'ACTIVE': 'success',
    'INACTIVE': 'danger',
    'TESTING': 'warning'
  }
  return tagTypeMap[status] || 'info'
}

/**
 * 格式化安全级别显示文本
 * @param {String} securityLevel 安全级别
 */
export function formatSecurityLevel(securityLevel) {
  const levelMap = {
    'PUBLIC': '公开',
    'INTERNAL': '内部',
    'CONFIDENTIAL': '机密',
    'SECRET': '秘密',
    'TOP_SECRET': '绝密'
  }
  return levelMap[securityLevel] || securityLevel
}

/**
 * 获取安全级别对应的标签类型
 * @param {String} securityLevel 安全级别
 */
export function getSecurityLevelTagType(securityLevel) {
  const tagTypeMap = {
    'PUBLIC': 'success',
    'INTERNAL': 'primary',
    'CONFIDENTIAL': 'warning',
    'SECRET': 'danger',
    'TOP_SECRET': 'danger'
  }
  return tagTypeMap[securityLevel] || 'info'
}

// 快捷操作

/**
 * 快速创建REST API
 * @param {Object} basicInfo 基础信息
 */
export function quickCreateRestApi(basicInfo) {
  const apiData = {
    ...basicInfo,
    apiType: 'REST',
    requestFormat: 'JSON',
    responseFormat: 'JSON',
    authenticationRequired: false,
    monitoringEnabled: true,
    loggingLevel: 'INFO',
    status: 'TESTING'
  }
  return createApiManagement(apiData)
}

/**
 * 快速启用多个API
 * @param {Array} apiIds API ID列表
 */
export function quickEnableApis(apiIds) {
  return batchEnableApiManagements(apiIds)
}

/**
 * 快速禁用多个API
 * @param {Array} apiIds API ID列表
 */
export function quickDisableApis(apiIds) {
  return batchDisableApiManagements(apiIds)
}

/**
 * 获取API管理选项数据
 */
export function getApiManagementOptions() {
  return {
    apiTypes: Object.keys(API_TYPES).map(key => ({
      value: key,
      label: formatApiType(key)
    })),
    apiCategories: Object.keys(API_CATEGORIES).map(key => ({
      value: key,
      label: formatApiCategory(key)
    })),
    httpMethods: Object.keys(HTTP_METHODS).map(key => ({
      value: key,
      label: key
    })),
    apiStatuses: Object.keys(API_STATUS).map(key => ({
      value: key,
      label: formatApiStatus(key)
    })),
    dataFormats: Object.keys(DATA_FORMATS).map(key => ({
      value: key,
      label: key
    })),
    logLevels: Object.keys(LOG_LEVELS).map(key => ({
      value: key,
      label: key
    })),
    securityLevels: Object.keys(SECURITY_LEVELS).map(key => ({
      value: key,
      label: formatSecurityLevel(key)
    })),
    priorities: Object.keys(PRIORITIES).map(key => ({
      value: key,
      label: key
    }))
  }
}
