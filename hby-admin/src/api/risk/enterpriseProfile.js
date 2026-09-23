/**
 * 企业画像API接口
 * @author AI Agent
 * @since 2025-01-21
 */
import request from '@/utils/request'

/**
 * 获取企业基本信息
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseInfo(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/enterpriseInfo',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取财务雷达图数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getFinancialRadarData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/financialRadar',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取趋势分析数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getTrendAnalysisData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/trendAnalysis',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取风险预警和审计面板数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRiskAuditPanelData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/riskAuditPanel',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取组织架构与人员分析数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getOrganizationAnalysisData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/organizationAnalysis',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取企业选择列表
 * @returns {Promise}
 */
export function getEnterpriseList() {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/enterpriseList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: {}
  })
}

/**
 * 刷新企业数据
 * @param {Object} data 刷新参数
 * @returns {Promise}
 */
export function refreshEnterpriseData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/refresh',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 导出企业数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportEnterpriseData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/export',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取指标预警数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getIndicatorWarnings(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/indicatorWarnings',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取企业全息画像数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseHologramData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/hologramData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 获取企业详细信息
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseDetailInfo(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/detailInfo',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}
