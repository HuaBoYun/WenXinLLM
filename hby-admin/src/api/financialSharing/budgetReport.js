/**
 * 预算报表查询API
 * @author hbyun
 * @date 2026-02-02
 */
import request from '@/utils/request'

/**
 * 查询报表配置列表
 * @param {Object} data 查询参数
 */
export function getReportList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/getReportList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 根据ID查询报表配置
 * @param {String} reportId 报表ID
 */
export function getReportById(reportId) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/getReportById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { reportId }
  })
}

/**
 * 创建报表配置
 * @param {Object} data 报表配置
 */
export function createReport(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/createReport',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 更新报表配置
 * @param {Object} data 报表配置
 */
export function updateReport(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/updateReport',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 删除报表配置
 * @param {Array} reportIds 报表ID列表
 */
export function deleteReports(reportIds) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/deleteReports',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { reportIds }
  })
}

/**
 * 查询预算明细数据
 * @param {Object} data 查询参数
 */
export function queryDetailData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/queryDetailData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 查询预算汇总数据
 * @param {Object} data 查询参数
 */
export function querySummaryData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/querySummaryData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 查询预算对比数据
 * @param {Object} data 查询参数
 */
export function queryCompareData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/queryCompareData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 查询预算趋势数据
 * @param {Object} data 查询参数
 */
export function queryTrendData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/queryTrendData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 导出报表数据
 * @param {Object} data 查询参数
 */
export function exportData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetReport/exportData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

