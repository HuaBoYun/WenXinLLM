import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// ==================== 综合统计 ====================

/**
 * 获取战略管理综合统计数据
 */
export function getStrategyStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/statistics',
    method: 'get'
  })
}

// ==================== 战略规划 (Planning) ====================

export function getPlanningList(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getPlanningById(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/planning/${id}`,
    method: 'get'
  })
}

export function addPlanning(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function updatePlanning(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function deletePlanning(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/planning/${id}`,
    method: 'delete'
  })
}

export function batchDeletePlanning(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getPlanningStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/statistics',
    method: 'get'
  })
}

// ==================== 执行监控 (Execution) ====================

export function getExecutionList(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/execution/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getExecutionById(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/execution/${id}`,
    method: 'get'
  })
}

export function addExecution(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/execution/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function updateExecution(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/execution/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function deleteExecution(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/execution/${id}`,
    method: 'delete'
  })
}

export function batchDeleteExecution(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/execution/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getExecutionStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/execution/statistics',
    method: 'get'
  })
}

// ==================== 绩效评价 (Performance) ====================

export function getPerformanceList(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/performance/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getPerformanceById(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/performance/${id}`,
    method: 'get'
  })
}

export function addPerformance(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/performance/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function updatePerformance(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/performance/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function deletePerformance(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/performance/${id}`,
    method: 'delete'
  })
}

export function batchDeletePerformance(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/performance/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getPerformanceStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/performance/statistics',
    method: 'get'
  })
}

// ==================== 战略调整 (Adjustment) ====================

export function getAdjustmentList(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/adjustment/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getAdjustmentById(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/adjustment/${id}`,
    method: 'get'
  })
}

export function addAdjustment(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/adjustment/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function updateAdjustment(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/adjustment/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function deleteAdjustment(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/adjustment/${id}`,
    method: 'delete'
  })
}

export function batchDeleteAdjustment(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/adjustment/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getAdjustmentStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/adjustment/statistics',
    method: 'get'
  })
}

// ==================== 环境分析 (Environment) ====================

export function getEnvironmentList(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/environment/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getEnvironmentById(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/environment/${id}`,
    method: 'get'
  })
}

export function addEnvironment(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/environment/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function updateEnvironment(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/environment/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function deleteEnvironment(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/environment/${id}`,
    method: 'delete'
  })
}

export function batchDeleteEnvironment(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/environment/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getEnvironmentStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/environment/statistics',
    method: 'get'
  })
}

// ==================== 竞争分析 (Competition) ====================

export function getCompetitionList(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/competition/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getCompetitionById(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/competition/${id}`,
    method: 'get'
  })
}

export function addCompetition(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/competition/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function updateCompetition(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/competition/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function deleteCompetition(id) {
  return request({
    url: `/monitor/v1/enterprise/strategy/competition/${id}`,
    method: 'delete'
  })
}

export function batchDeleteCompetition(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/competition/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

export function getCompetitionStatistics() {
  return request({
    url: '/monitor/v1/enterprise/strategy/competition/statistics',
    method: 'get'
  })
}

// ==================== 辅助接口 ====================

/**
 * 获取所有战略规划名称（用于下拉选择）
 */
export function getPlanningAll() {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/all',
    method: 'get'
  })
}

/**
 * 批量审批战略规划
 */
export function batchApprovePlanning(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/planning/batch/approve',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 执行绩效评估
 */
export function evaluatePerformance(data) {
  return request({
    url: '/monitor/v1/enterprise/strategy/performance/evaluate',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}