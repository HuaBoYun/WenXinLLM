import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 风险评估API接口
 * 国资国企穿透式监管系统 - 风险穿透管控模块 - 风险评估功能
 */

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询风险评估列表
 */
export function getRiskAssessmentList(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID获取风险评估详情
 */
export function getRiskAssessmentById(id) {
  return request({
    url: `/monitor/v1/supervision/risk/assessment/${id}`,
    method: 'get'
  })
}

/**
 * 新增风险评估
 */
export function addRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/add',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新风险评估
 */
export function updateRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/update',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除风险评估
 */
export function deleteRiskAssessment(id) {
  return request({
    url: `/monitor/v1/supervision/risk/assessment/${id}`,
    method: 'delete'
  })
}

// ==================== 风险评估核心业务 ====================

/**
 * 执行风险评估
 */
export function performRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/perform',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算综合风险评分
 */
export function calculateOverallRiskScore(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/calculate-score',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取企业风险评估列表
 */
export function getEnterpriseRiskAssessments(enterpriseId) {
  return request({
    url: `/monitor/v1/supervision/risk/assessment/enterprise/${enterpriseId}`,
    method: 'get'
  })
}

/**
 * 获取最新风险评估
 */
export function getLatestRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/latest',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 风险预警管理 ====================

/**
 * 检查风险预警
 */
export function checkRiskWarning(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/check-warning',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 触发风险预警
 */
export function triggerRiskWarning(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/trigger-warning',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取触发预警的风险评估列表
 */
export function getTriggeredWarnings() {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/warnings',
    method: 'get'
  })
}

/**
 * 获取预警统计数据
 */
export function getWarningStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/warning-statistics',
    method: 'get'
  })
}

// ==================== 风险因素分析 ====================

/**
 * 识别风险因素
 */
export function identifyRiskFactors(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/identify-factors',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分析风险关联度
 */
export function analyzeRiskCorrelation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/analyze-correlation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算风险集中度
 */
export function calculateRiskConcentration(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/calculate-concentration',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估风险影响程度
 */
export function assessRiskImpact(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/assess-impact',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 风险应对策略 ====================

/**
 * 制定风险应对策略
 */
export function developRiskResponseStrategy(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/develop-strategy',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成风险控制措施建议
 */
export function generateControlMeasureRecommendations(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/generate-recommendations',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估风险应对效果
 */
export function evaluateResponseEffectiveness(assessmentId) {
  return request({
    url: `/monitor/v1/supervision/risk/assessment/evaluate-effectiveness/${assessmentId}`,
    method: 'get'
  })
}

// ==================== 审核流程 ====================

/**
 * 提交审核
 */
export function submitForReview(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/submit-review',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 审核风险评估
 */
export function reviewRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/review',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批准风险评估
 */
export function approveRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/approve',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取待审核风险评估列表
 */
export function getPendingReview() {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/pending-review',
    method: 'get'
  })
}

// ==================== 统计分析 ====================

/**
 * 获取风险等级分布统计
 */
export function getRiskLevelDistribution() {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/risk-level-distribution',
    method: 'get'
  })
}

/**
 * 获取风险趋势分析
 */
export function getRiskTrendAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/trend-analysis',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取行业风险对比
 */
export function getIndustryRiskComparison(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/industry-comparison',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取综合统计数据
 */
export function getComprehensiveStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/comprehensive-statistics',
    method: 'get'
  })
}

/**
 * 获取企业风险概览
 */
export function getEnterpriseRiskOverview(enterpriseId) {
  return request({
    url: `/monitor/v1/supervision/risk/assessment/enterprise-overview/${enterpriseId}`,
    method: 'get'
  })
}

// ==================== 报告生成 ====================

/**
 * 生成风险评估报告
 */
export function generateAssessmentReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/generate-report',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成风险分析报告
 */
export function generateRiskAnalysisReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/generate-analysis-report',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成预警报告
 */
export function generateWarningReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/generate-warning-report',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 批量操作 ====================

/**
 * 批量执行风险评估
 */
export function batchPerformAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/batch-perform',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量更新评估状态
 */
export function batchUpdateStatus(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/batch-update-status',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量审核风险评估
 */
export function batchReview(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/batch-review',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量删除风险评估
 */
export function batchDelete(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/batch-delete',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 导出功能 ====================

/**
 * 导出风险评估数据
 */
export function exportAssessmentData(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/export-data',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出风险评估报告
 */
export function exportAssessmentReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/assessment/export-report',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}
