/*
 * @Description: 财务共享 - 成本可视化分析API
 * @Author: 星光问心AI大模型
 * @Date: 2025-12-30
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 成本可视化分析 API ====================

/**
 * 获取可视化综合数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getVisualizationData(data) {
  return request({
    url: '/cwgxAi/reports/visualization/getData',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取成本构成数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostCompositionData(params) {
  return request({
    url: '/cwgxAi/reports/visualization/costComposition',
    method: 'get',
    params
  })
}

/**
 * 获取成本趋势数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getVisualizationCostTrend(data) {
  return request({
    url: '/cwgxAi/reports/visualization/costTrend',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取部门成本对比数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getDeptComparisonData(data) {
  return request({
    url: '/cwgxAi/reports/visualization/deptComparison',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取产品成本数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getProductCostData(data) {
  return request({
    url: '/cwgxAi/reports/visualization/productCost',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取成本中心雷达图数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getCenterRadarData(data) {
  return request({
    url: '/cwgxAi/reports/visualization/centerRadar',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 导出可视化报告
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportVisualizationReport(data) {
  return request({
    url: '/cwgxAi/reports/visualization/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 获取成本中心选项(用于雷达图筛选)
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getVisualizationCenterOptions(params) {
  return request({
    url: '/cwgxAi/cost/center/getOptions',
    method: 'get',
    params
  })
}
