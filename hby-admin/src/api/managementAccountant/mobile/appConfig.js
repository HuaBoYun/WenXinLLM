import request from '@/utils/request'

// 移动应用配置API

// 应用类型常量
export const APP_TYPES = {
  NATIVE: 'NATIVE',
  HYBRID: 'HYBRID',
  WEB: 'WEB',
  PWA: 'PWA'
}

// 应用平台常量
export const APP_PLATFORMS = {
  ANDROID: 'ANDROID',
  IOS: 'IOS',
  WINDOWS: 'WINDOWS',
  ALL: 'ALL'
}

// 应用分类常量
export const APP_CATEGORIES = {
  BUSINESS: 'BUSINESS',
  TOOL: 'TOOL',
  GAME: 'GAME',
  EDUCATION: 'EDUCATION'
}

// 应用状态常量
export const APP_STATUS = {
  DRAFT: 'DRAFT',
  PUBLISHED: 'PUBLISHED',
  UNPUBLISHED: 'UNPUBLISHED',
  ARCHIVED: 'ARCHIVED'
}

// CRUD操作

/**
 * 创建应用配置
 */
export function createAppConfig(data) {
  return request({
    url: '/api/mobile/app-config/create',
    method: 'post',
    data
  })
}

/**
 * 更新应用配置
 */
export function updateAppConfig(data) {
  return request({
    url: '/api/mobile/app-config/update',
    method: 'put',
    data
  })
}

/**
 * 删除应用配置
 */
export function deleteAppConfig(appConfigId) {
  return request({
    url: `/api/mobile/app-config/delete/${appConfigId}`,
    method: 'delete'
  })
}

/**
 * 根据ID获取应用配置
 */
export function getAppConfigById(appConfigId) {
  return request({
    url: `/api/mobile/app-config/get/${appConfigId}`,
    method: 'get'
  })
}

/**
 * 根据应用编码获取应用配置
 */
export function getAppConfigByCode(appCode) {
  return request({
    url: `/api/mobile/app-config/get-by-code/${appCode}`,
    method: 'get'
  })
}

// 查询操作

/**
 * 根据应用类型获取应用列表
 */
export function getAppConfigsByType(appType) {
  return request({
    url: `/api/mobile/app-config/list-by-type/${appType}`,
    method: 'get'
  })
}

/**
 * 根据应用平台获取应用列表
 */
export function getAppConfigsByPlatform(appPlatform) {
  return request({
    url: `/api/mobile/app-config/list-by-platform/${appPlatform}`,
    method: 'get'
  })
}

/**
 * 根据应用分类获取应用列表
 */
export function getAppConfigsByCategory(appCategory) {
  return request({
    url: `/api/mobile/app-config/list-by-category/${appCategory}`,
    method: 'get'
  })
}

/**
 * 根据状态获取应用列表
 */
export function getAppConfigsByStatus(status) {
  return request({
    url: `/api/mobile/app-config/list-by-status/${status}`,
    method: 'get'
  })
}

/**
 * 获取启用的应用列表
 */
export function getEnabledAppConfigs() {
  return request({
    url: '/api/mobile/app-config/list-enabled',
    method: 'get'
  })
}

/**
 * 获取默认应用
 */
export function getDefaultAppConfig() {
  return request({
    url: '/api/mobile/app-config/get-default',
    method: 'get'
  })
}

/**
 * 获取已发布的应用列表
 */
export function getPublishedAppConfigs() {
  return request({
    url: '/api/mobile/app-config/list-published',
    method: 'get'
  })
}

// 分页查询操作

/**
 * 分页查询应用配置
 */
export function getAppConfigPage(params) {
  return request({
    url: '/api/mobile/app-config/page',
    method: 'get',
    params
  })
}

// 应用管理操作

/**
 * 启用应用
 */
export function enableAppConfig(appConfigId) {
  return request({
    url: `/api/mobile/app-config/enable/${appConfigId}`,
    method: 'put'
  })
}

/**
 * 禁用应用
 */
export function disableAppConfig(appConfigId) {
  return request({
    url: `/api/mobile/app-config/disable/${appConfigId}`,
    method: 'put'
  })
}

/**
 * 发布应用
 */
export function publishAppConfig(appConfigId) {
  return request({
    url: `/api/mobile/app-config/publish/${appConfigId}`,
    method: 'put'
  })
}

/**
 * 下架应用
 */
export function unpublishAppConfig(appConfigId) {
  return request({
    url: `/api/mobile/app-config/unpublish/${appConfigId}`,
    method: 'put'
  })
}

/**
 * 设置默认应用
 */
export function setDefaultAppConfig(appConfigId) {
  return request({
    url: `/api/mobile/app-config/set-default/${appConfigId}`,
    method: 'put'
  })
}

/**
 * 批量更新应用状态
 */
export function batchUpdateAppConfigStatus(appConfigIds, status) {
  return request({
    url: '/api/mobile/app-config/batch-update-status',
    method: 'put',
    data: {
      appConfigIds,
      status
    }
  })
}

/**
 * 批量启用应用
 */
export function batchEnableAppConfigs(appConfigIds) {
  return request({
    url: '/api/mobile/app-config/batch-enable',
    method: 'put',
    data: appConfigIds
  })
}

/**
 * 批量禁用应用
 */
export function batchDisableAppConfigs(appConfigIds) {
  return request({
    url: '/api/mobile/app-config/batch-disable',
    method: 'put',
    data: appConfigIds
  })
}

// 应用版本管理

/**
 * 获取应用的所有版本
 */
export function getAppConfigVersions(appCode) {
  return request({
    url: `/api/mobile/app-config/versions/${appCode}`,
    method: 'get'
  })
}

/**
 * 获取应用的最新版本
 */
export function getLatestAppConfigVersion(appCode) {
  return request({
    url: `/api/mobile/app-config/latest-version/${appCode}`,
    method: 'get'
  })
}

/**
 * 创建应用版本
 */
export function createAppConfigVersion(appConfigId, newVersion) {
  return request({
    url: '/api/mobile/app-config/create-version',
    method: 'post',
    data: {
      appConfigId,
      newVersion
    }
  })
}

/**
 * 发布应用版本
 */
export function publishAppConfigVersion(appConfigId) {
  return request({
    url: `/api/mobile/app-config/publish-version/${appConfigId}`,
    method: 'put'
  })
}

// 应用搜索操作

/**
 * 根据关键词搜索应用
 */
export function searchAppConfigsByKeyword(keyword) {
  return request({
    url: '/api/mobile/app-config/search-by-keyword',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 根据标签搜索应用
 */
export function searchAppConfigsByTags(tags) {
  return request({
    url: '/api/mobile/app-config/search-by-tags',
    method: 'post',
    data: tags
  })
}

// 应用验证操作

/**
 * 验证应用配置
 */
export function validateAppConfig(appConfig) {
  return request({
    url: '/api/mobile/app-config/validate-config',
    method: 'post',
    data: appConfig
  })
}

/**
 * 测试应用连接
 */
export function testAppConfigConnection(appConfigId) {
  return request({
    url: `/api/mobile/app-config/test-connection/${appConfigId}`,
    method: 'post'
  })
}

// 统计操作

/**
 * 统计应用总数
 */
export function countAppConfigs() {
  return request({
    url: '/api/mobile/app-config/count',
    method: 'get'
  })
}

/**
 * 按应用类型统计数量
 */
export function countAppConfigsByType() {
  return request({
    url: '/api/mobile/app-config/count-by-type',
    method: 'get'
  })
}

/**
 * 按状态统计数量
 */
export function countAppConfigsByStatus() {
  return request({
    url: '/api/mobile/app-config/count-by-status',
    method: 'get'
  })
}

/**
 * 获取系统概览信息
 */
export function getAppConfigSystemOverview() {
  return request({
    url: '/api/mobile/app-config/system-overview',
    method: 'get'
  })
}

// 工具函数

/**
 * 格式化应用类型
 */
export function formatAppType(appType) {
  const typeMap = {
    NATIVE: '原生应用',
    HYBRID: '混合应用',
    WEB: 'Web应用',
    PWA: '渐进式Web应用'
  }
  return typeMap[appType] || appType
}

/**
 * 格式化应用平台
 */
export function formatAppPlatform(appPlatform) {
  const platformMap = {
    ANDROID: '安卓',
    IOS: '苹果',
    WINDOWS: 'Windows',
    ALL: '全平台'
  }
  return platformMap[appPlatform] || appPlatform
}

/**
 * 格式化应用分类
 */
export function formatAppCategory(appCategory) {
  const categoryMap = {
    BUSINESS: '业务应用',
    TOOL: '工具应用',
    GAME: '游戏应用',
    EDUCATION: '教育应用'
  }
  return categoryMap[appCategory] || appCategory
}

/**
 * 格式化应用状态
 */
export function formatAppStatus(status) {
  const statusMap = {
    DRAFT: '草稿',
    PUBLISHED: '已发布',
    UNPUBLISHED: '已下架',
    ARCHIVED: '已归档'
  }
  return statusMap[status] || status
}

/**
 * 获取应用类型标签类型
 */
export function getAppTypeTagType(appType) {
  const typeMap = {
    NATIVE: 'success',
    HYBRID: 'warning',
    WEB: 'info',
    PWA: 'primary'
  }
  return typeMap[appType] || 'default'
}

/**
 * 获取应用平台标签类型
 */
export function getAppPlatformTagType(appPlatform) {
  const platformMap = {
    ANDROID: 'success',
    IOS: 'primary',
    WINDOWS: 'info',
    ALL: 'warning'
  }
  return platformMap[appPlatform] || 'default'
}

/**
 * 获取应用状态标签类型
 */
export function getAppStatusTagType(status) {
  const statusMap = {
    DRAFT: 'info',
    PUBLISHED: 'success',
    UNPUBLISHED: 'warning',
    ARCHIVED: 'danger'
  }
  return statusMap[status] || 'default'
}

// 快捷操作

/**
 * 快速创建原生应用
 */
export function quickCreateNativeApp(appName, appPlatform) {
  const appConfig = {
    appName,
    appCode: `NATIVE_${Date.now()}`,
    appType: 'NATIVE',
    appPlatform,
    appCategory: 'BUSINESS',
    appVersion: '1.0.0',
    isEnabled: true,
    isDefault: false,
    isForceUpdate: false,
    isOfflineSupport: false,
    status: 'DRAFT'
  }
  return createAppConfig(appConfig)
}

/**
 * 快速启用多个应用
 */
export function quickEnableApps(appConfigIds) {
  return batchEnableAppConfigs(appConfigIds)
}

/**
 * 快速禁用多个应用
 */
export function quickDisableApps(appConfigIds) {
  return batchDisableAppConfigs(appConfigIds)
}

/**
 * 获取应用配置选项
 */
export function getAppConfigOptions() {
  return {
    appTypes: Object.keys(APP_TYPES).map(key => ({
      value: key,
      label: formatAppType(key)
    })),
    appPlatforms: Object.keys(APP_PLATFORMS).map(key => ({
      value: key,
      label: formatAppPlatform(key)
    })),
    appCategories: Object.keys(APP_CATEGORIES).map(key => ({
      value: key,
      label: formatAppCategory(key)
    })),
    appStatus: Object.keys(APP_STATUS).map(key => ({
      value: key,
      label: formatAppStatus(key)
    }))
  }
}
