import request from '@/utils/request'

/**
 * 风险评估API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// 为了保持与planning.js的兼容性，导出一个contractRequest别名
export const contractRequest = request

// ==================== 相对方信息管理 ====================

/**
 * 创建相对方信息
 * @param {Object} data 相对方信息
 * @returns {Promise} 创建结果
 */
export function createCounterpart(data) {
  return request({
    url: '/contract/counterpart/info/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询相对方信息列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getCounterpartList(params) {
  return request({
    url: '/contract/counterpart/info/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 搜索相对方信息（用于下拉选择）- 使用原有相对方模块
 * @param {String} keyword 搜索关键词
 * @param {Number} limit 限制数量
 * @returns {Promise} 相对方列表
 */
export function searchCounterpartOptions(keyword, limit = 20) {
  const { transData } = require('@/utils/requestData')
  return request({
    url: '/contract/oppositePartyMaintenance',
    method: 'post',
    headers: {
      'token': localStorage.getItem('token') || ''
    },
    data: transData({
      budgetname: keyword,
      pageNumber: 1,
      pageSize: limit,
      flowId: 'HTGL001', // 相对方管理流程ID
      staffId: ''
    })
  })
}

/**
 * 获取相对方详情
 * @param {String} id 相对方ID
 * @returns {Promise} 相对方详情
 */
export function getCounterpartById(id) {
  return request({
    url: `/contract/counterpart/info/${id}`,
    method: 'get'
  })
}

/**
 * 更新相对方信息
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateCounterpart(data) {
  return request({
    url: '/contract/counterpart/info/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除相对方信息
 * @param {String} id 相对方ID
 * @returns {Promise} 删除结果
 */
export function deleteCounterpart(id) {
  return request({
    url: `/contract/counterpart/info/${id}`,
    method: 'delete'
  })
}

// ==================== 风险评估管理 ====================

/**
 * 创建风险评估
 * @param {Object} data 风险评估数据
 * @returns {Promise} 创建结果
 */
export function createRiskAssessment(data) {
  return request({
    url: '/contract/riskAssessment/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询风险评估列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getRiskAssessmentList(params) {
  return request({
    url: '/contract/riskAssessment/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取风险评估详情
 * @param {String} id 评估ID
 * @returns {Promise} 评估详情
 */
export function getRiskAssessmentById(id) {
  return request({
    url: `/contract/riskAssessment/${id}`,
    method: 'get'
  })
}

/**
 * 更新风险评估
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateRiskAssessment(data) {
  return request({
    url: '/contract/riskAssessment/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 风险评估审批
 * @param {Object} data 审批数据（包含id）
 * @returns {Promise} 审批结果
 */
export function approveRiskAssessment(data) {
  return request({
    url: '/contract/riskAssessment/approve',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除风险评估
 * @param {String|Number} id 评估ID
 * @returns {Promise} 删除结果
 */
export function deleteRiskAssessment(id) {
  return request({
    url: `/contract/riskAssessment/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除风险评估
 * @param {Array} ids 评估ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteRiskAssessment(ids) {
  return request({
    url: '/contract/riskAssessment/batchDelete',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 风险预警管理接口 ====================

// 获取风险预警列表
export function getRiskWarningList(params) {
  return request({
    url: '/contract/riskWarning/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 获取风险预警统计信息
export function getRiskWarningStatistics() {
  return request({
    url: '/contract/riskWarning/statistics',
    method: 'get'
  })
}

// 获取高风险项目列表
export function getHighRiskProjects() {
  return request({
    url: '/contract/riskWarning/highRiskProjects',
    method: 'get'
  })
}

// 获取风险趋势分析
export function getRiskTrendAnalysis(months = 12) {
  return request({
    url: '/contract/riskWarning/trendAnalysis',
    method: 'get',
    params: { months }
  })
}

// 执行风险自动评估
export function executeAutoRiskAssessment(projectId) {
  return request({
    url: '/contract/riskWarning/autoAssessment',
    method: 'post',
    params: { projectId }
  })
}

// 获取相对方风险评估
export function getCounterpartRiskAssessment(counterpartId) {
  return request({
    url: `/contract/riskWarning/counterpartRisk/${counterpartId}`,
    method: 'get'
  })
}

// 生成风险评估报告
export function generateRiskAssessmentReport(assessmentId) {
  return request({
    url: `/contract/riskWarning/generateReport/${assessmentId}`,
    method: 'post'
  })
}

// 获取风险预警配置
export function getRiskWarningConfig() {
  return request({
    url: '/contract/riskWarning/warningConfig',
    method: 'get'
  })
}

// 更新风险预警配置
export function updateRiskWarningConfig(config) {
  return request({
    url: '/contract/riskWarning/warningConfig',
    method: 'post',
    data: config,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 处理风险预警
export function handleRiskWarning(warningId, action, remarks) {
  return request({
    url: '/contract/riskWarning/handleWarning',
    method: 'post',
    params: { warningId, action, remarks }
  })
}

// 获取风险评估模板
export function getRiskAssessmentTemplate(assessmentType) {
  return request({
    url: `/contract/riskWarning/assessmentTemplate/${assessmentType}`,
    method: 'get'
  })
}

// 导出风险评估报告
export function exportRiskAssessmentReportFile(assessmentId) {
  return request({
    url: `/contract/riskWarning/exportReport/${assessmentId}`,
    method: 'get'
  })
}

// 获取风险缓解建议
export function getRiskMitigationSuggestions(riskCategory, riskLevel) {
  return request({
    url: '/contract/riskWarning/mitigationSuggestions',
    method: 'get',
    params: { riskCategory, riskLevel }
  })
}

// 智能风险识别
export function intelligentRiskIdentification(projectId) {
  return request({
    url: '/contract/riskWarning/intelligentIdentification',
    method: 'post',
    params: { projectId }
  })
}

// 风险评分计算
export function calculateRiskScore(assessmentId) {
  return request({
    url: '/contract/riskWarning/calculateScore',
    method: 'post',
    params: { assessmentId }
  })
}

// 风险监控
export function monitorProjectRisk(projectId) {
  return request({
    url: '/contract/riskWarning/monitor',
    method: 'get',
    params: { projectId }
  })
}

// 生成风险热力图数据
export function generateRiskHeatmapData() {
  return request({
    url: '/contract/riskWarning/heatmapData',
    method: 'get'
  })
}

// 获取风险预警历史记录
export function getRiskWarningHistory(projectId, days = 30) {
  return request({
    url: '/contract/riskWarning/warningHistory',
    method: 'get',
    params: { projectId, days }
  })
}

// 风险评估质量检查
export function checkAssessmentQuality(assessmentId) {
  return request({
    url: '/contract/riskWarning/qualityCheck',
    method: 'post',
    params: { assessmentId }
  })
}

/**
 * 获取风险评估详情
 * @param {String} id 评估ID
 * @returns {Promise} 评估详情
 */
export function getRiskAssessmentDetail(id) {
  return request({
    url: `/contract/riskAssessment/${id}`,
    method: 'get'
  })
}