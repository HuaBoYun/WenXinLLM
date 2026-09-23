/*
 * @Description: 财务共享 - 会计规则管理 API
 * @Author: system
 * @Date: 2024-12-19
 */

import request from '@/utils/request'

// ================================
// 确认规则管理 API
// ================================

/**
 * 获取确认规则分页列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRecognitionRulePage(params) {
  return request({
    url: '/cwgxAi/rule/recognition/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取确认规则详情
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function getRecognitionRuleById(id) {
  return request({
    url: `/cwgxAi/rule/recognition/${id}`,
    method: 'get'
  })
}

/**
 * 保存确认规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveRecognitionRule(data) {
  return request({
    url: '/cwgxAi/rule/recognition',
    method: 'post',
    data
  })
}

/**
 * 更新确认规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function updateRecognitionRule(data) {
  return request({
    url: '/cwgxAi/rule/recognition',
    method: 'put',
    data
  })
}

/**
 * 删除确认规则
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function deleteRecognitionRule(id) {
  return request({
    url: `/cwgxAi/rule/recognition/${id}`,
    method: 'delete'
  })
}

/**
 * 更新确认规则状态
 * @param {Object} data 状态更新数据
 * @returns {Promise}
 */
export function updateRecognitionRuleStatus(data) {
  return request({
    url: '/cwgxAi/rule/recognition/status',
    method: 'put',
    data
  })
}

// ================================
// 计量规则管理 API
// ================================

/**
 * 获取计量规则分页列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMeasurementRulePage(params) {
  return request({
    url: '/cwgxAi/rule/measurement/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取计量规则详情
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function getMeasurementRuleById(id) {
  return request({
    url: `/cwgxAi/rule/measurement/${id}`,
    method: 'get'
  })
}

/**
 * 保存计量规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveMeasurementRule(data) {
  return request({
    url: '/cwgxAi/rule/measurement',
    method: 'post',
    data
  })
}

/**
 * 更新计量规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function updateMeasurementRule(data) {
  return request({
    url: '/cwgxAi/rule/measurement',
    method: 'put',
    data
  })
}

/**
 * 删除计量规则
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function deleteMeasurementRule(id) {
  return request({
    url: `/cwgxAi/rule/measurement/${id}`,
    method: 'delete'
  })
}

// ================================
// 凭证规则管理 API
// ================================

/**
 * 获取凭证规则分页列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getVoucherRulePage(params) {
  return request({
    url: '/cwgxAi/rule/voucher/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取凭证规则详情
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function getVoucherRuleById(id) {
  return request({
    url: `/cwgxAi/rule/voucher/${id}`,
    method: 'get'
  })
}

/**
 * 保存凭证规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveVoucherRule(data) {
  return request({
    url: '/cwgxAi/rule/voucher',
    method: 'post',
    data
  })
}

/**
 * 更新凭证规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function updateVoucherRule(data) {
  return request({
    url: '/cwgxAi/rule/voucher',
    method: 'put',
    data
  })
}

/**
 * 删除凭证规则
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function deleteVoucherRule(id) {
  return request({
    url: `/cwgxAi/rule/voucher/${id}`,
    method: 'delete'
  })
}

/**
 * 根据业务类型获取凭证模板
 * @param {String} businessType 业务类型
 * @returns {Promise}
 */
export function getVoucherTemplateByBusinessType(businessType) {
  return request({
    url: `/cwgxAi/rule/voucher/template/${businessType}`,
    method: 'get'
  })
}

// ================================
// 过账规则管理 API
// ================================

/**
 * 获取过账规则分页列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPostingRulePage(params) {
  return request({
    url: '/cwgxAi/rule/posting/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取过账规则详情
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function getPostingRuleById(id) {
  return request({
    url: `/cwgxAi/rule/posting/${id}`,
    method: 'get'
  })
}

/**
 * 保存过账规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function savePostingRule(data) {
  return request({
    url: '/cwgxAi/rule/posting',
    method: 'post',
    data
  })
}

/**
 * 更新过账规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function updatePostingRule(data) {
  return request({
    url: '/cwgxAi/rule/posting',
    method: 'put',
    data
  })
}

/**
 * 删除过账规则
 * @param {String} id 规则ID
 * @returns {Promise}
 */
export function deletePostingRule(id) {
  return request({
    url: `/cwgxAi/rule/posting/${id}`,
    method: 'delete'
  })
}

// ================================
// 规则引擎 API
// ================================

/**
 * 测试规则执行
 * @param {Object} data 测试数据
 * @returns {Promise}
 */
export function testRuleExecution(data) {
  return request({
    url: '/cwgxAi/rule/engine/test',
    method: 'post',
    data
  })
}

/**
 * 批量执行规则
 * @param {Object} data 执行数据
 * @returns {Promise}
 */
export function batchExecuteRules(data) {
  return request({
    url: '/cwgxAi/rule/engine/batch-execute',
    method: 'post',
    data
  })
}

/**
 * 获取规则执行日志
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRuleExecutionLog(params) {
  return request({
    url: '/cwgxAi/rule/engine/execution-log',
    method: 'get',
    params
  })
}
