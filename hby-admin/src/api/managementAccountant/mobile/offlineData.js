import request from '@/utils/request'

// ==================== 常量定义 ====================

// 离线数据类型
export const OFFLINE_DATA_TYPES = {
  CACHE: 'CACHE',           // 缓存数据
  BACKUP: 'BACKUP',         // 备份数据
  SYNC: 'SYNC',             // 同步数据
  TEMP: 'TEMP'              // 临时数据
}

// 离线数据分类
export const OFFLINE_DATA_CATEGORIES = {
  BUSINESS: 'BUSINESS',     // 业务数据
  SYSTEM: 'SYSTEM',         // 系统数据
  USER: 'USER',             // 用户数据
  CONFIG: 'CONFIG'          // 配置数据
}

// 数据状态
export const DATA_STATUS = {
  DRAFT: 'DRAFT',           // 草稿
  ACTIVE: 'ACTIVE',         // 活跃
  INACTIVE: 'INACTIVE',     // 非活跃
  ARCHIVED: 'ARCHIVED'      // 已归档
}

// 同步状态
export const SYNC_STATUS = {
  PENDING: 'PENDING',       // 待同步
  SYNCING: 'SYNCING',       // 同步中
  COMPLETED: 'COMPLETED',   // 已完成
  FAILED: 'FAILED'          // 失败
}

// ==================== 基础CRUD操作 ====================

/**
 * 创建离线数据
 */
export function createOfflineData(data) {
  return request({
    url: '/api/mobile/offline-data/create',
    method: 'post',
    data
  })
}

/**
 * 更新离线数据
 */
export function updateOfflineData(data) {
  return request({
    url: '/api/mobile/offline-data/update',
    method: 'put',
    data
  })
}

/**
 * 删除离线数据
 */
export function deleteOfflineData(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/delete/${offlineDataId}`,
    method: 'delete'
  })
}

/**
 * 根据ID获取离线数据
 */
export function getOfflineDataById(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/get/${offlineDataId}`,
    method: 'get'
  })
}

/**
 * 根据编码获取离线数据
 */
export function getOfflineDataByCode(offlineDataCode) {
  return request({
    url: `/api/mobile/offline-data/get-by-code/${offlineDataCode}`,
    method: 'get'
  })
}

// ==================== 查询操作 ====================

/**
 * 根据应用配置ID获取离线数据列表
 */
export function getOfflineDatasByAppConfigId(appConfigId) {
  return request({
    url: `/api/mobile/offline-data/list-by-app-config/${appConfigId}`,
    method: 'get'
  })
}

/**
 * 根据类型获取离线数据列表
 */
export function getOfflineDatasByType(offlineDataType) {
  return request({
    url: `/api/mobile/offline-data/list-by-type/${offlineDataType}`,
    method: 'get'
  })
}

/**
 * 根据分类获取离线数据列表
 */
export function getOfflineDatasByCategory(offlineDataCategory) {
  return request({
    url: `/api/mobile/offline-data/list-by-category/${offlineDataCategory}`,
    method: 'get'
  })
}

/**
 * 根据用户ID获取离线数据
 */
export function getOfflineDatasByUserId(userId) {
  return request({
    url: `/api/mobile/offline-data/list-by-user/${userId}`,
    method: 'get'
  })
}

/**
 * 获取可用的离线数据
 */
export function getAvailableOfflineData() {
  return request({
    url: '/api/mobile/offline-data/list-available',
    method: 'get'
  })
}

/**
 * 获取已过期的离线数据
 */
export function getExpiredOfflineData() {
  return request({
    url: '/api/mobile/offline-data/list-expired',
    method: 'get'
  })
}

/**
 * 获取需要同步的离线数据
 */
export function getNeedSyncOfflineData() {
  return request({
    url: '/api/mobile/offline-data/list-need-sync',
    method: 'get'
  })
}

// ==================== 分页查询 ====================

/**
 * 分页查询离线数据
 */
export function getOfflineDataPage(params) {
  return request({
    url: '/api/mobile/offline-data/page',
    method: 'get',
    params
  })
}

// ==================== 离线数据管理 ====================

/**
 * 启用离线数据
 */
export function enableOfflineData(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/enable/${offlineDataId}`,
    method: 'put'
  })
}

/**
 * 禁用离线数据
 */
export function disableOfflineData(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/disable/${offlineDataId}`,
    method: 'put'
  })
}

/**
 * 批量更新数据状态
 */
export function batchUpdateDataStatus(offlineDataIds, dataStatus) {
  return request({
    url: '/api/mobile/offline-data/batch-update-data-status',
    method: 'put',
    data: {
      offlineDataIds,
      dataStatus
    }
  })
}

/**
 * 批量启用离线数据
 */
export function batchEnableOfflineData(offlineDataIds) {
  return request({
    url: '/api/mobile/offline-data/batch-enable',
    method: 'put',
    data: offlineDataIds
  })
}

/**
 * 批量禁用离线数据
 */
export function batchDisableOfflineData(offlineDataIds) {
  return request({
    url: '/api/mobile/offline-data/batch-disable',
    method: 'put',
    data: offlineDataIds
  })
}

// ==================== 数据同步管理 ====================

/**
 * 同步离线数据
 */
export function syncOfflineData(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/sync/${offlineDataId}`,
    method: 'post'
  })
}

/**
 * 批量同步离线数据
 */
export function batchSyncOfflineData(offlineDataIds) {
  return request({
    url: '/api/mobile/offline-data/batch-sync',
    method: 'post',
    data: offlineDataIds
  })
}

/**
 * 下载离线数据
 */
export function downloadOfflineData(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/download/${offlineDataId}`,
    method: 'post'
  })
}

// ==================== 搜索功能 ====================

/**
 * 根据关键词搜索离线数据
 */
export function searchOfflineDatasByKeyword(keyword) {
  return request({
    url: '/api/mobile/offline-data/search-by-keyword',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 根据标签搜索离线数据
 */
export function searchOfflineDatasByTags(tags) {
  return request({
    url: '/api/mobile/offline-data/search-by-tags',
    method: 'post',
    data: tags
  })
}

// ==================== 验证功能 ====================

/**
 * 验证离线数据
 */
export function validateOfflineData(offlineData) {
  return request({
    url: '/api/mobile/offline-data/validate',
    method: 'post',
    data: offlineData
  })
}

/**
 * 测试离线数据连接
 */
export function testOfflineDataConnection(offlineDataId) {
  return request({
    url: `/api/mobile/offline-data/test-connection/${offlineDataId}`,
    method: 'post'
  })
}

// ==================== 统计分析 ====================

/**
 * 统计离线数据总数
 */
export function countOfflineData() {
  return request({
    url: '/api/mobile/offline-data/count',
    method: 'get'
  })
}

/**
 * 根据类型统计离线数据
 */
export function countOfflineDataByType(offlineDataType) {
  return request({
    url: '/api/mobile/offline-data/count-by-type',
    method: 'get',
    params: { offlineDataType }
  })
}

/**
 * 根据状态统计离线数据
 */
export function countOfflineDataByStatus(status) {
  return request({
    url: '/api/mobile/offline-data/count-by-status',
    method: 'get',
    params: { status }
  })
}

/**
 * 获取系统概览
 */
export function getOfflineDataSystemOverview() {
  return request({
    url: '/api/mobile/offline-data/system-overview',
    method: 'get'
  })
}

// ==================== 工具函数 ====================

/**
 * 格式化离线数据类型
 */
export function formatOfflineDataType(type) {
  const typeMap = {
    CACHE: '缓存数据',
    BACKUP: '备份数据',
    SYNC: '同步数据',
    TEMP: '临时数据'
  }
  return typeMap[type] || type
}

/**
 * 格式化离线数据分类
 */
export function formatOfflineDataCategory(category) {
  const categoryMap = {
    BUSINESS: '业务数据',
    SYSTEM: '系统数据',
    USER: '用户数据',
    CONFIG: '配置数据'
  }
  return categoryMap[category] || category
}

/**
 * 格式化数据状态
 */
export function formatDataStatus(status) {
  const statusMap = {
    DRAFT: '草稿',
    ACTIVE: '活跃',
    INACTIVE: '非活跃',
    ARCHIVED: '已归档'
  }
  return statusMap[status] || status
}

/**
 * 格式化同步状态
 */
export function formatSyncStatus(status) {
  const statusMap = {
    PENDING: '待同步',
    SYNCING: '同步中',
    COMPLETED: '已完成',
    FAILED: '失败'
  }
  return statusMap[status] || status
}

/**
 * 获取离线数据类型标签类型
 */
export function getOfflineDataTypeTagType(type) {
  const typeMap = {
    CACHE: 'primary',
    BACKUP: 'success',
    SYNC: 'warning',
    TEMP: 'info'
  }
  return typeMap[type] || 'default'
}

/**
 * 获取数据状态标签类型
 */
export function getDataStatusTagType(status) {
  const statusMap = {
    DRAFT: 'info',
    ACTIVE: 'success',
    INACTIVE: 'warning',
    ARCHIVED: 'danger'
  }
  return statusMap[status] || 'default'
}

/**
 * 获取同步状态标签类型
 */
export function getSyncStatusTagType(status) {
  const statusMap = {
    PENDING: 'info',
    SYNCING: 'warning',
    COMPLETED: 'success',
    FAILED: 'danger'
  }
  return statusMap[status] || 'default'
}

// ==================== 快捷操作 ====================

/**
 * 快速创建缓存数据
 */
export function quickCreateCacheData(name, content) {
  const offlineData = {
    offlineDataName: name,
    offlineDataType: 'CACHE',
    offlineDataCategory: 'BUSINESS',
    dataContent: content,
    dataStatus: 'ACTIVE',
    syncStatus: 'PENDING',
    isAvailable: true,
    isExpired: false
  }
  return createOfflineData(offlineData)
}

/**
 * 快速启用离线数据
 */
export function quickEnableOfflineData(offlineDataIds) {
  if (Array.isArray(offlineDataIds)) {
    return batchEnableOfflineData(offlineDataIds)
  } else {
    return enableOfflineData(offlineDataIds)
  }
}

/**
 * 快速禁用离线数据
 */
export function quickDisableOfflineData(offlineDataIds) {
  if (Array.isArray(offlineDataIds)) {
    return batchDisableOfflineData(offlineDataIds)
  } else {
    return disableOfflineData(offlineDataIds)
  }
}

/**
 * 获取离线数据选项
 */
export function getOfflineDataOptions() {
  return {
    types: Object.keys(OFFLINE_DATA_TYPES).map(key => ({
      value: key,
      label: formatOfflineDataType(key)
    })),
    categories: Object.keys(OFFLINE_DATA_CATEGORIES).map(key => ({
      value: key,
      label: formatOfflineDataCategory(key)
    })),
    dataStatuses: Object.keys(DATA_STATUS).map(key => ({
      value: key,
      label: formatDataStatus(key)
    })),
    syncStatuses: Object.keys(SYNC_STATUS).map(key => ({
      value: key,
      label: formatSyncStatus(key)
    }))
  }
}
