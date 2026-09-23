import request from '@/utils/request'

// API基础路径
const API_BASE_PATH = '/api/intg/system-config'

// 系统类型常量
export const SYSTEM_TYPES = {
  ERP: 'ERP',
  CRM: 'CRM',
  OA: 'OA',
  HR: 'HR',
  SCM: 'SCM',
  WMS: 'WMS',
  MES: 'MES',
  BI: 'BI',
  DATABASE: 'DATABASE',
  API: 'API',
  FILE: 'FILE',
  MESSAGE_QUEUE: 'MESSAGE_QUEUE',
  CLOUD_SERVICE: 'CLOUD_SERVICE',
  OTHER: 'OTHER'
}

// 连接类型常量
export const CONNECTION_TYPES = {
  DATABASE: 'DATABASE',
  REST_API: 'REST_API',
  SOAP_API: 'SOAP_API',
  FTP: 'FTP',
  SFTP: 'SFTP',
  HTTP: 'HTTP',
  HTTPS: 'HTTPS',
  TCP: 'TCP',
  UDP: 'UDP',
  WEBSOCKET: 'WEBSOCKET',
  MESSAGE_QUEUE: 'MESSAGE_QUEUE',
  FILE_SYSTEM: 'FILE_SYSTEM',
  CLOUD_STORAGE: 'CLOUD_STORAGE',
  OTHER: 'OTHER'
}

// 配置状态常量
export const CONFIG_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  TESTING: 'TESTING',
  ERROR: 'ERROR',
  MAINTENANCE: 'MAINTENANCE'
}

// 连接状态常量
export const CONNECTION_STATUS = {
  CONNECTED: 'CONNECTED',
  DISCONNECTED: 'DISCONNECTED',
  CONNECTING: 'CONNECTING',
  ERROR: 'ERROR',
  TIMEOUT: 'TIMEOUT'
}

// 健康状态常量
export const HEALTH_STATUS = {
  HEALTHY: 'HEALTHY',
  UNHEALTHY: 'UNHEALTHY',
  WARNING: 'WARNING',
  CRITICAL: 'CRITICAL',
  UNKNOWN: 'UNKNOWN'
}

// 基础CRUD操作

/**
 * 创建系统配置
 * @param {Object} data 系统配置数据
 * @returns {Promise}
 */
export function createSystemConfig(data) {
  return request({
    url: API_BASE_PATH,
    method: 'post',
    data
  })
}

/**
 * 更新系统配置
 * @param {String} configId 配置ID
 * @param {Object} data 系统配置数据
 * @returns {Promise}
 */
export function updateSystemConfig(configId, data) {
  return request({
    url: `${API_BASE_PATH}/${configId}`,
    method: 'put',
    data
  })
}

/**
 * 删除系统配置
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function deleteSystemConfig(configId) {
  return request({
    url: `${API_BASE_PATH}/${configId}`,
    method: 'delete'
  })
}

/**
 * 根据ID获取系统配置
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function getSystemConfigById(configId) {
  return request({
    url: `${API_BASE_PATH}/${configId}`,
    method: 'get'
  })
}

/**
 * 根据编码获取系统配置
 * @param {String} configCode 配置编码
 * @returns {Promise}
 */
export function getSystemConfigByCode(configCode) {
  return request({
    url: `${API_BASE_PATH}/code/${configCode}`,
    method: 'get'
  })
}

// 查询操作

/**
 * 分页查询系统配置
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemConfigPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'get',
    params
  })
}

/**
 * 根据系统类型查询配置列表
 * @param {String} systemType 系统类型
 * @returns {Promise}
 */
export function getSystemConfigsByType(systemType) {
  return request({
    url: `${API_BASE_PATH}/type/${systemType}`,
    method: 'get'
  })
}

/**
 * 根据连接类型查询配置列表
 * @param {String} connectionType 连接类型
 * @returns {Promise}
 */
export function getSystemConfigsByConnectionType(connectionType) {
  return request({
    url: `${API_BASE_PATH}/connection-type/${connectionType}`,
    method: 'get'
  })
}

/**
 * 根据配置状态查询配置列表
 * @param {String} configStatus 配置状态
 * @returns {Promise}
 */
export function getSystemConfigsByStatus(configStatus) {
  return request({
    url: `${API_BASE_PATH}/status/${configStatus}`,
    method: 'get'
  })
}

/**
 * 查询启用的配置列表
 * @returns {Promise}
 */
export function getEnabledSystemConfigs() {
  return request({
    url: `${API_BASE_PATH}/enabled`,
    method: 'get'
  })
}

/**
 * 查询默认配置
 * @param {String} systemType 系统类型
 * @returns {Promise}
 */
export function getDefaultSystemConfig(systemType) {
  return request({
    url: `${API_BASE_PATH}/default/${systemType}`,
    method: 'get'
  })
}

// 配置管理操作

/**
 * 测试连接配置
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function testConnection(configId) {
  return request({
    url: `${API_BASE_PATH}/${configId}/test-connection`,
    method: 'post'
  })
}

/**
 * 批量测试连接
 * @param {Array} configIds 配置ID列表
 * @returns {Promise}
 */
export function batchTestConnection(configIds) {
  return request({
    url: `${API_BASE_PATH}/batch-test-connection`,
    method: 'post',
    data: configIds
  })
}

/**
 * 启用系统配置
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function enableSystemConfig(configId) {
  return request({
    url: `${API_BASE_PATH}/${configId}/enable`,
    method: 'put'
  })
}

/**
 * 禁用系统配置
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function disableSystemConfig(configId) {
  return request({
    url: `${API_BASE_PATH}/${configId}/disable`,
    method: 'put'
  })
}

/**
 * 批量更新配置状态
 * @param {Array} configIds 配置ID列表
 * @param {String} configStatus 配置状态
 * @returns {Promise}
 */
export function batchUpdateConfigStatus(configIds, configStatus) {
  return request({
    url: `${API_BASE_PATH}/batch-update-status`,
    method: 'put',
    params: {
      configIds: configIds.join(','),
      configStatus
    }
  })
}

// 健康检查操作

/**
 * 执行健康检查
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function performHealthCheck(configId) {
  return request({
    url: `${API_BASE_PATH}/${configId}/health-check`,
    method: 'post'
  })
}

/**
 * 批量健康检查
 * @param {Array} configIds 配置ID列表
 * @returns {Promise}
 */
export function batchPerformHealthCheck(configIds) {
  return request({
    url: `${API_BASE_PATH}/batch-health-check`,
    method: 'post',
    data: configIds
  })
}

/**
 * 获取系统健康报告
 * @returns {Promise}
 */
export function getSystemHealthReport() {
  return request({
    url: `${API_BASE_PATH}/health-report`,
    method: 'get'
  })
}

// 统计分析操作

/**
 * 统计配置总数
 * @returns {Promise}
 */
export function countSystemConfigs() {
  return request({
    url: `${API_BASE_PATH}/count`,
    method: 'get'
  })
}

/**
 * 按系统类型统计配置数量
 * @returns {Promise}
 */
export function countBySystemType() {
  return request({
    url: `${API_BASE_PATH}/count/by-system-type`,
    method: 'get'
  })
}

/**
 * 按连接类型统计配置数量
 * @returns {Promise}
 */
export function countByConnectionType() {
  return request({
    url: `${API_BASE_PATH}/count/by-connection-type`,
    method: 'get'
  })
}

/**
 * 按配置状态统计配置数量
 * @returns {Promise}
 */
export function countByConfigStatus() {
  return request({
    url: `${API_BASE_PATH}/count/by-config-status`,
    method: 'get'
  })
}

/**
 * 按连接状态统计配置数量
 * @returns {Promise}
 */
export function countByConnectionStatus() {
  return request({
    url: `${API_BASE_PATH}/count/by-connection-status`,
    method: 'get'
  })
}

/**
 * 按健康状态统计配置数量
 * @returns {Promise}
 */
export function countByHealthStatus() {
  return request({
    url: `${API_BASE_PATH}/count/by-health-status`,
    method: 'get'
  })
}

// 系统维护操作

/**
 * 获取系统概览信息
 * @returns {Promise}
 */
export function getSystemOverview() {
  return request({
    url: `${API_BASE_PATH}/overview`,
    method: 'get'
  })
}

/**
 * 生成配置报告
 * @param {String} reportType 报告类型
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function generateConfigReport(reportType, startTime, endTime) {
  return request({
    url: `${API_BASE_PATH}/report`,
    method: 'get',
    params: {
      reportType,
      startTime,
      endTime
    }
  })
}

/**
 * 检查系统健康状态
 * @returns {Promise}
 */
export function checkSystemHealth() {
  return request({
    url: `${API_BASE_PATH}/check-health`,
    method: 'get'
  })
}

/**
 * 评估配置质量
 * @returns {Promise}
 */
export function assessConfigQuality() {
  return request({
    url: `${API_BASE_PATH}/assess-quality`,
    method: 'get'
  })
}

// 快捷操作

/**
 * 快速创建数据库配置
 * @param {Object} config 配置信息
 * @returns {Promise}
 */
export function quickCreateDatabaseConfig(config) {
  const data = {
    ...config,
    systemType: SYSTEM_TYPES.DATABASE,
    connectionType: CONNECTION_TYPES.DATABASE,
    configStatus: CONFIG_STATUS.ACTIVE
  }
  return createSystemConfig(data)
}

/**
 * 快速创建API配置
 * @param {Object} config 配置信息
 * @returns {Promise}
 */
export function quickCreateApiConfig(config) {
  const data = {
    ...config,
    systemType: SYSTEM_TYPES.API,
    connectionType: CONNECTION_TYPES.REST_API,
    configStatus: CONFIG_STATUS.ACTIVE
  }
  return createSystemConfig(data)
}

// 工具函数

/**
 * 格式化系统类型显示名称
 * @param {String} systemType 系统类型
 * @returns {String}
 */
export function formatSystemTypeName(systemType) {
  const typeNames = {
    [SYSTEM_TYPES.ERP]: 'ERP系统',
    [SYSTEM_TYPES.CRM]: 'CRM系统',
    [SYSTEM_TYPES.OA]: 'OA系统',
    [SYSTEM_TYPES.HR]: 'HR系统',
    [SYSTEM_TYPES.SCM]: 'SCM系统',
    [SYSTEM_TYPES.WMS]: 'WMS系统',
    [SYSTEM_TYPES.MES]: 'MES系统',
    [SYSTEM_TYPES.BI]: 'BI系统',
    [SYSTEM_TYPES.DATABASE]: '数据库',
    [SYSTEM_TYPES.API]: 'API接口',
    [SYSTEM_TYPES.FILE]: '文件系统',
    [SYSTEM_TYPES.MESSAGE_QUEUE]: '消息队列',
    [SYSTEM_TYPES.CLOUD_SERVICE]: '云服务',
    [SYSTEM_TYPES.OTHER]: '其他'
  }
  return typeNames[systemType] || systemType
}

/**
 * 格式化连接类型显示名称
 * @param {String} connectionType 连接类型
 * @returns {String}
 */
export function formatConnectionTypeName(connectionType) {
  const typeNames = {
    [CONNECTION_TYPES.DATABASE]: '数据库连接',
    [CONNECTION_TYPES.REST_API]: 'REST API',
    [CONNECTION_TYPES.SOAP_API]: 'SOAP API',
    [CONNECTION_TYPES.FTP]: 'FTP',
    [CONNECTION_TYPES.SFTP]: 'SFTP',
    [CONNECTION_TYPES.HTTP]: 'HTTP',
    [CONNECTION_TYPES.HTTPS]: 'HTTPS',
    [CONNECTION_TYPES.TCP]: 'TCP',
    [CONNECTION_TYPES.UDP]: 'UDP',
    [CONNECTION_TYPES.WEBSOCKET]: 'WebSocket',
    [CONNECTION_TYPES.MESSAGE_QUEUE]: '消息队列',
    [CONNECTION_TYPES.FILE_SYSTEM]: '文件系统',
    [CONNECTION_TYPES.CLOUD_STORAGE]: '云存储',
    [CONNECTION_TYPES.OTHER]: '其他'
  }
  return typeNames[connectionType] || connectionType
}

/**
 * 格式化状态显示名称
 * @param {String} status 状态值
 * @param {String} type 状态类型 (config/connection/health)
 * @returns {String}
 */
export function formatStatusName(status, type = 'config') {
  const statusNames = {
    config: {
      [CONFIG_STATUS.ACTIVE]: '启用',
      [CONFIG_STATUS.INACTIVE]: '禁用',
      [CONFIG_STATUS.TESTING]: '测试中',
      [CONFIG_STATUS.ERROR]: '错误',
      [CONFIG_STATUS.MAINTENANCE]: '维护中'
    },
    connection: {
      [CONNECTION_STATUS.CONNECTED]: '已连接',
      [CONNECTION_STATUS.DISCONNECTED]: '未连接',
      [CONNECTION_STATUS.CONNECTING]: '连接中',
      [CONNECTION_STATUS.ERROR]: '连接错误',
      [CONNECTION_STATUS.TIMEOUT]: '连接超时'
    },
    health: {
      [HEALTH_STATUS.HEALTHY]: '健康',
      [HEALTH_STATUS.UNHEALTHY]: '不健康',
      [HEALTH_STATUS.WARNING]: '警告',
      [HEALTH_STATUS.CRITICAL]: '严重',
      [HEALTH_STATUS.UNKNOWN]: '未知'
    }
  }
  return statusNames[type]?.[status] || status
}

export default {
  // 常量
  SYSTEM_TYPES,
  CONNECTION_TYPES,
  CONFIG_STATUS,
  CONNECTION_STATUS,
  HEALTH_STATUS,
  
  // CRUD操作
  createSystemConfig,
  updateSystemConfig,
  deleteSystemConfig,
  getSystemConfigById,
  getSystemConfigByCode,
  
  // 查询操作
  getSystemConfigPage,
  getSystemConfigsByType,
  getSystemConfigsByConnectionType,
  getSystemConfigsByStatus,
  getEnabledSystemConfigs,
  getDefaultSystemConfig,
  
  // 配置管理
  testConnection,
  batchTestConnection,
  enableSystemConfig,
  disableSystemConfig,
  batchUpdateConfigStatus,
  
  // 健康检查
  performHealthCheck,
  batchPerformHealthCheck,
  getSystemHealthReport,
  
  // 统计分析
  countSystemConfigs,
  countBySystemType,
  countByConnectionType,
  countByConfigStatus,
  countByConnectionStatus,
  countByHealthStatus,
  
  // 系统维护
  getSystemOverview,
  generateConfigReport,
  checkSystemHealth,
  assessConfigQuality,
  
  // 快捷操作
  quickCreateDatabaseConfig,
  quickCreateApiConfig,
  
  // 工具函数
  formatSystemTypeName,
  formatConnectionTypeName,
  formatStatusName
}
