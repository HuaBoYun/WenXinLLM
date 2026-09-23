import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 获取审计首页KPI统计数据
 * @param {Object} params - { year }
 */
export function getAuditHomeKpiData(params) {
  return request({
    url: '/audit/auditSmartHome/getKpiData',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取审计生命周期节点统计
 * @param {Object} params - { year }
 */
export function getAuditLifecycleData(params) {
  return request({
    url: '/audit/auditSmartHome/getLifecycleData',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取整改跟踪进度数据
 * @param {Object} params - { year }
 */
export function getAuditRectifyProgress(params) {
  return request({
    url: '/audit/auditSmartHome/getRectifyProgress',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取审计风险预警列表
 * @param {Object} params - { pageSize }
 */
export function getAuditRiskWarningList(params) {
  return request({
    url: '/audit/auditSmartHome/getRiskWarningList',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取整改追责首页聚合数据
 * 包含：KPI、整改跟踪、整改进度、违规追责统计
 * @param {Object} params - { year }
 */
export function getRectifyHomeOverview(params) {
  return request({
    url: '/audit/rectifyHome/getOverview',
    method: 'get',
    params: transData(params),
  })
}
