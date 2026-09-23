import request from '@/utils/request'

/**
 * 服务门户API接口封装
 * 提供完整的服务门户管理功能
 */

// 基础CRUD操作
export function getServicePortalPage(params) {
  return request({
    url: '/accountant/ss/servicePortal/page',
    method: 'get',
    params
  })
}

export function getServicePortalById(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getServicePortalByCode(portalCode, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/code/${portalCode}`,
    method: 'get',
    params: { tenantId }
  })
}

export function createServicePortal(data, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal',
    method: 'post',
    data,
    params: { tenantId }
  })
}

export function updateServicePortal(data, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal',
    method: 'put',
    data,
    params: { tenantId }
  })
}

export function deleteServicePortal(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}`,
    method: 'delete',
    params: { tenantId }
  })
}

export function batchDeleteServicePortal(portalIds, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/batch',
    method: 'delete',
    data: portalIds,
    params: { tenantId }
  })
}

// 门户状态管理
export function activateServicePortal(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/activate`,
    method: 'post',
    params: { tenantId }
  })
}

export function deactivateServicePortal(portalId, reason, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/deactivate`,
    method: 'post',
    params: { reason, tenantId }
  })
}

export function batchActivateServicePortal(portalIds, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/batch/activate',
    method: 'post',
    data: portalIds,
    params: { tenantId }
  })
}

export function batchDeactivateServicePortal(portalIds, reason, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/batch/deactivate',
    method: 'post',
    data: portalIds,
    params: { reason, tenantId }
  })
}

export function archiveServicePortal(portalId, reason, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/archive`,
    method: 'post',
    params: { reason, tenantId }
  })
}

export function batchArchiveServicePortal(portalIds, reason, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/batch/archive',
    method: 'post',
    data: portalIds,
    params: { reason, tenantId }
  })
}

// 维护管理
export function startMaintenance(portalId, reason, startTime, endTime, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/maintenance/start`,
    method: 'post',
    params: { reason, startTime, endTime, tenantId }
  })
}

export function endMaintenance(portalId, result, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/maintenance/end`,
    method: 'post',
    params: { result, tenantId }
  })
}

// 配置管理
export function configurePortal(portalId, configType, configData, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/configure`,
    method: 'post',
    data: configData,
    params: { configType, tenantId }
  })
}

export function resetPortalConfig(portalId, configType, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/config/reset`,
    method: 'post',
    params: { configType, tenantId }
  })
}

export function copyPortalConfig(sourcePortalId, targetPortalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${sourcePortalId}/config/copy/${targetPortalId}`,
    method: 'post',
    params: { tenantId }
  })
}

export function setDefaultPortal(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/setDefault`,
    method: 'post',
    params: { tenantId }
  })
}

export function setPortalPermissions(portalId, permissions, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/permissions`,
    method: 'post',
    data: permissions,
    params: { tenantId }
  })
}

// 访问统计
export function updateAccessStats(portalId, userId, userName, accessIp, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/access`,
    method: 'post',
    params: { userId, userName, accessIp, tenantId }
  })
}

// 监控和检查
export function performHealthCheck(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/healthCheck`,
    method: 'get',
    params: { tenantId }
  })
}

export function monitorPortalPerformance(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/monitor`,
    method: 'get',
    params: { tenantId }
  })
}

// 备份和恢复
export function backupPortal(portalId, backupType, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/backup`,
    method: 'post',
    params: { backupType, tenantId }
  })
}

export function restorePortal(portalId, backupId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/restore`,
    method: 'post',
    params: { backupId, tenantId }
  })
}

// 优化和测试
export function optimizePortal(portalId, optimizationConfig, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/optimize`,
    method: 'post',
    data: optimizationConfig,
    params: { tenantId }
  })
}

export function performSecurityScan(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/securityScan`,
    method: 'get',
    params: { tenantId }
  })
}

export function performIntegrationTest(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/integrationTest`,
    method: 'get',
    params: { tenantId }
  })
}

// 用户反馈
export function collectUserFeedback(portalId, feedback, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/feedback`,
    method: 'post',
    data: feedback,
    params: { tenantId }
  })
}

// 分析和推荐
export function analyzePortalUsage(portalId, startTime, endTime, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/usage/analyze`,
    method: 'get',
    params: { startTime, endTime, tenantId }
  })
}

export function recommendPortals(userId, limit, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/recommend',
    method: 'get',
    params: { userId, limit, tenantId }
  })
}

export function searchPortals(keyword, filters, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/search',
    method: 'get',
    params: { keyword, ...filters, tenantId }
  })
}

// 查询方法
export function getPortalsByType(portalType, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/type/${portalType}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalsByStatus(portalStatus, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/status/${portalStatus}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalsByOwner(ownerId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/owner/${ownerId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalsByAdmin(adminId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/admin/${adminId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalsByDepartment(departmentId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/department/${departmentId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalsByCompany(companyId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/company/${companyId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getActivePortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/active',
    method: 'get',
    params: { tenantId }
  })
}

export function getInactivePortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/inactive',
    method: 'get',
    params: { tenantId }
  })
}

export function getMaintenancePortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/maintenance',
    method: 'get',
    params: { tenantId }
  })
}

export function getArchivedPortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/archived',
    method: 'get',
    params: { tenantId }
  })
}

export function getDefaultPortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/default',
    method: 'get',
    params: { tenantId }
  })
}

export function getPublicPortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/public',
    method: 'get',
    params: { tenantId }
  })
}

export function getCustomizablePortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/customizable',
    method: 'get',
    params: { tenantId }
  })
}

export function getHighTrafficPortals(limit, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/highTraffic',
    method: 'get',
    params: { limit, tenantId }
  })
}

export function getRecentlyAccessedPortals(limit, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/recentlyAccessed',
    method: 'get',
    params: { limit, tenantId }
  })
}

export function getPortalsNeedMaintenance(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/needMaintenance',
    method: 'get',
    params: { tenantId }
  })
}

export function getExpiredPortals(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/expired',
    method: 'get',
    params: { tenantId }
  })
}

export function getLowUsagePortals(threshold, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/lowUsage',
    method: 'get',
    params: { threshold, tenantId }
  })
}

// 验证方法
export function checkPortalCodeExists(portalCode, portalId, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/check/code',
    method: 'get',
    params: { portalCode, portalId, tenantId }
  })
}

export function checkPortalNameExists(portalName, portalId, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/check/name',
    method: 'get',
    params: { portalName, portalId, tenantId }
  })
}

// 统计分析接口
export function getStatistics(startTime, endTime, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics',
    method: 'get',
    params: { startTime, endTime, tenantId }
  })
}

export function getPortalTypeDistribution(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/typeDistribution',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalStatusDistribution(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/statusDistribution',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalCategoryDistribution(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/categoryDistribution',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalAccessRanking(limit, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/accessRanking',
    method: 'get',
    params: { limit, tenantId }
  })
}

export function getPortalUsageTrend(startTime, endTime, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/usageTrend',
    method: 'get',
    params: { startTime, endTime, tenantId }
  })
}

export function getPortalPerformanceMetrics(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/performanceMetrics',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalUserFeedback(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/userFeedback',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalSecurityEvents(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/securityEvents',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalIntegrationStatus(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/integrationStatus',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalBackupStatus(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/backupStatus',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalMonitoringStatus(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/statistics/monitoringStatus',
    method: 'get',
    params: { tenantId }
  })
}

// 日志和记录接口
export function getPortalAccessLogs(portalId, startTime, endTime, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/accessLogs`,
    method: 'get',
    params: { startTime, endTime, tenantId }
  })
}

export function getPortalConfigHistory(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/configHistory`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalMaintenanceRecords(portalId, tenantId) {
  return request({
    url: `/accountant/ss/servicePortal/${portalId}/maintenanceRecords`,
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalAlerts(alertType, tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/alerts',
    method: 'get',
    params: { alertType, tenantId }
  })
}

export function getPortalPendingItems(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/pendingItems',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalHealthCheckResults(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/healthCheckResults',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalResourceUsage(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/resourceUsage',
    method: 'get',
    params: { tenantId }
  })
}

export function getPortalOptimizationSuggestions(tenantId) {
  return request({
    url: '/accountant/ss/servicePortal/optimizationSuggestions',
    method: 'get',
    params: { tenantId }
  })
}

// 快捷操作
export const quickActions = {
  // 快速激活门户
  quickActivate: (portalIds, tenantId) => batchActivateServicePortal(portalIds, tenantId),

  // 快速停用门户
  quickDeactivate: (portalIds, reason, tenantId) => batchDeactivateServicePortal(portalIds, reason, tenantId),

  // 快速归档门户
  quickArchive: (portalIds, reason, tenantId) => batchArchiveServicePortal(portalIds, reason, tenantId),

  // 快速健康检查
  quickHealthCheck: (portalIds, tenantId) => {
    return Promise.all(portalIds.map(id => performHealthCheck(id, tenantId)))
  },

  // 快速性能监控
  quickPerformanceMonitor: (portalIds, tenantId) => {
    return Promise.all(portalIds.map(id => monitorPortalPerformance(id, tenantId)))
  },

  // 快速备份
  quickBackup: (portalIds, backupType, tenantId) => {
    return Promise.all(portalIds.map(id => backupPortal(id, backupType, tenantId)))
  }
}

// 工具函数
export const utils = {
  // 格式化门户状态
  formatPortalStatus: (status) => {
    const statusMap = {
      'ACTIVE': { text: '活跃', color: 'success' },
      'INACTIVE': { text: '非活跃', color: 'info' },
      'MAINTENANCE': { text: '维护中', color: 'warning' },
      'ARCHIVED': { text: '已归档', color: 'danger' }
    }
    return statusMap[status] || { text: status, color: 'default' }
  },

  // 格式化门户类型
  formatPortalType: (type) => {
    const typeMap = {
      'EMPLOYEE': '员工门户',
      'CUSTOMER': '客户门户',
      'PARTNER': '合作伙伴门户',
      'ADMIN': '管理门户'
    }
    return typeMap[type] || type
  },

  // 格式化访问量
  formatAccessCount: (count) => {
    if (count >= 1000000) {
      return (count / 1000000).toFixed(1) + 'M'
    } else if (count >= 1000) {
      return (count / 1000).toFixed(1) + 'K'
    }
    return count.toString()
  },

  // 计算门户健康度
  calculateHealthScore: (portal) => {
    let score = 100

    // 根据状态扣分
    if (portal.portalStatus === 'INACTIVE') score -= 30
    if (portal.portalStatus === 'MAINTENANCE') score -= 20
    if (portal.portalStatus === 'ARCHIVED') score -= 50

    // 根据访问量加分
    if (portal.accessCount > 10000) score += 10
    else if (portal.accessCount > 1000) score += 5

    // 根据最后访问时间扣分
    const daysSinceLastAccess = portal.lastAccessTime ?
      Math.floor((new Date() - new Date(portal.lastAccessTime)) / (1000 * 60 * 60 * 24)) : 999
    if (daysSinceLastAccess > 30) score -= 20
    else if (daysSinceLastAccess > 7) score -= 10

    return Math.max(0, Math.min(100, score))
  },

  // 生成门户URL
  generatePortalUrl: (portal) => {
    if (portal.portalUrl) return portal.portalUrl
    return `/portal/${portal.portalCode || portal.portalId}`
  },

  // 验证门户配置
  validatePortalConfig: (portal) => {
    const errors = []

    if (!portal.portalName) errors.push('门户名称不能为空')
    if (!portal.portalCode) errors.push('门户编码不能为空')
    if (!portal.portalType) errors.push('门户类型不能为空')
    if (!portal.portalOwnerId) errors.push('门户所有者不能为空')

    return {
      isValid: errors.length === 0,
      errors
    }
  }
}
