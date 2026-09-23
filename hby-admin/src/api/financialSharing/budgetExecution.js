/**
 * 预算执行分析API
 * @author hbyun
 * @date 2026-02-02
 */
import request from '@/utils/request'

/**
 * 查询预算执行列表
 * @param {Object} data 查询参数
 */
export function getExecutionList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetExecution/getExecutionList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 根据ID查询预算执行
 * @param {String} executionId 执行ID
 */
export function getExecutionById(executionId) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetExecution/getExecutionById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { executionId }
  })
}

/**
 * 查询预算执行统计
 * @param {Object} data 查询参数
 */
export function getExecutionStatistics(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetExecution/getExecutionStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 查询预算执行趋势
 * @param {Object} data 查询参数
 */
export function getExecutionTrend(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetExecution/getExecutionTrend',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 查询预算执行预警列表
 * @param {Object} data 查询参数
 */
export function getExecutionWarnings(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetExecution/getExecutionWarnings',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 刷新预算执行数据
 * @param {Object} data 查询参数
 */
export function refreshExecutionData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetExecution/refreshExecutionData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

