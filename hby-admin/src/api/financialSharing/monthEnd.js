/*
 * @Description: 财务共享 - 智能月结模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 月结任务管理 API ====================

/**
 * 分页查询月结任务列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getMonthEndTaskPage(data) {
  return request({
    url: '/cwgxAi/monthend/task/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 创建月结任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function createMonthEndTask(data) {
  return request({
    url: '/cwgxAi/monthend/task/create',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行月结任务
 * @param {Object} data 执行参数
 * @returns {Promise}
 */
export function executeMonthEndTask(data) {
  return request({
    url: '/cwgxAi/monthend/task/execute',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 查询任务执行进度
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function getTaskProgress(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/progress/${taskId}`,
    method: 'get'
  })
}

/**
 * 暂停月结任务
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function pauseMonthEndTask(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/pause/${taskId}`,
    method: 'post'
  })
}

/**
 * 恢复月结任务
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function resumeMonthEndTask(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/resume/${taskId}`,
    method: 'post'
  })
}

/**
 * 取消月结任务
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function cancelMonthEndTask(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/cancel/${taskId}`,
    method: 'post'
  })
}

/**
 * 删除月结任务
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function deleteMonthEndTask(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/${taskId}`,
    method: 'delete'
  })
}

/**
 * 获取月结任务详情
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function getMonthEndTaskById(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/${taskId}`,
    method: 'get'
  })
}

/**
 * 获取月结模板列表
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getMonthEndTemplateList(bookId, tenantId) {
  return request({
    url: '/cwgxAi/monthend/template/list',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 保存月结模板
 * @param {Object} data 模板数据
 * @returns {Promise}
 */
export function saveMonthEndTemplate(data) {
  return request({
    url: '/cwgxAi/monthend/template/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除月结模板
 * @param {Number} templateId 模板ID
 * @returns {Promise}
 */
export function deleteMonthEndTemplate(templateId) {
  return request({
    url: `/cwgxAi/monthend/template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 获取月结日志
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function getMonthEndTaskLogs(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/logs/${taskId}`,
    method: 'get'
  })
}

// ==================== 月结步骤管理 API ====================

/**
 * 获取月结步骤列表
 * @param {Number} taskId 任务ID
 * @returns {Promise}
 */
export function getMonthEndSteps(taskId) {
  return request({
    url: `/cwgxAi/monthend/task/steps/${taskId}`,
    method: 'get'
  })
}

/**
 * 执行单个月结步骤
 * @param {Number} taskId 任务ID
 * @param {Number} stepId 步骤ID
 * @returns {Promise}
 */
export function executeMonthEndStep(taskId, stepId) {
  return request({
    url: `/cwgxAi/monthend/task/${taskId}/step/${stepId}/execute`,
    method: 'post'
  })
}

/**
 * 跳过月结步骤
 * @param {Number} taskId 任务ID
 * @param {Number} stepId 步骤ID
 * @param {String} reason 跳过原因
 * @returns {Promise}
 */
export function skipMonthEndStep(taskId, stepId, reason) {
  return request({
    url: `/cwgxAi/monthend/task/${taskId}/step/${stepId}/skip`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 重新执行月结步骤
 * @param {Number} taskId 任务ID
 * @param {Number} stepId 步骤ID
 * @returns {Promise}
 */
export function retryMonthEndStep(taskId, stepId) {
  return request({
    url: `/cwgxAi/monthend/task/${taskId}/step/${stepId}/retry`,
    method: 'post'
  })
}

// ==================== 月结检查 API ====================

/**
 * 执行月结前检查
 * @param {Object} data 检查参数
 * @returns {Promise}
 */
export function executePreMonthEndCheck(data) {
  return request({
    url: '/cwgxAi/monthend/check/pre',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行月结后检查
 * @param {Object} data 检查参数
 * @returns {Promise}
 */
export function executePostMonthEndCheck(data) {
  return request({
    url: '/cwgxAi/monthend/check/post',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取检查结果
 * @param {String} checkId 检查ID
 * @returns {Promise}
 */
export function getMonthEndCheckResult(checkId) {
  return request({
    url: `/cwgxAi/monthend/check/result/${checkId}`,
    method: 'get'
  })
}

// ==================== 月结统计 API ====================

/**
 * 获取月结任务统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMonthEndTaskStatistics(params) {
  return request({
    url: '/cwgxAi/monthend/statistics/task',
    method: 'get',
    params
  })
}

/**
 * 获取月结执行时长统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMonthEndDurationStatistics(params) {
  return request({
    url: '/cwgxAi/monthend/statistics/duration',
    method: 'get',
    params
  })
}

/**
 * 获取月结成功率统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMonthEndSuccessRateStatistics(params) {
  return request({
    url: '/cwgxAi/monthend/statistics/success-rate',
    method: 'get',
    params
  })
}

// ==================== 月结配置 API ====================

/**
 * 获取月结配置
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getMonthEndConfig(bookId, tenantId) {
  return request({
    url: '/cwgxAi/monthend/config',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 保存月结配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function saveMonthEndConfig(data) {
  return request({
    url: '/cwgxAi/monthend/config',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 重置月结配置
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function resetMonthEndConfig(bookId, tenantId) {
  return request({
    url: '/cwgxAi/monthend/config/reset',
    method: 'post',
    params: { bookId, tenantId }
  })
}
