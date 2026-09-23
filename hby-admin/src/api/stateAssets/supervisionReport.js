import { transData } from '@/utils/requestData'

// 获取监管报告统计数据
export function getSupervisionReportStatistics() {
  return transData({
    url: '/api/supervision-report/statistics',
    method: 'post'
  })
}

// ==================== 报告管理 ====================

// 获取监管报告列表
export function getSupervisionReportsList(params) {
  return transData({
    url: '/api/supervision-report/list',
    method: 'post',
    data: params
  })
}

// 保存监管报告
export function saveSupervisionReport(data) {
  return transData({
    url: '/api/supervision-report/save',
    method: 'post',
    data: data
  })
}

// 删除监管报告
export function deleteSupervisionReport(id) {
  return transData({
    url: '/api/supervision-report/delete',
    method: 'post',
    data: { id }
  })
}

// 获取监管报告详情
export function getSupervisionReportDetail(id) {
  return transData({
    url: '/api/supervision-report/detail',
    method: 'post',
    data: { id }
  })
}

// 发布监管报告
export function publishSupervisionReport(id) {
  return transData({
    url: '/api/supervision-report/publish',
    method: 'post',
    data: { id }
  })
}

// 预览监管报告
export function previewSupervisionReport(id) {
  return transData({
    url: '/api/supervision-report/preview',
    method: 'post',
    data: { id }
  })
}

// 下载监管报告
export function downloadSupervisionReport(id, format) {
  return transData({
    url: '/api/supervision-report/download',
    method: 'post',
    data: { id, format }
  })
}

// 智能生成报告
export function generateSupervisionReport(generateConfig) {
  return transData({
    url: '/api/supervision-report/generate',
    method: 'post',
    data: generateConfig
  })
}

// 导出监管报告数据
export function exportSupervisionReports(params) {
  return transData({
    url: '/api/supervision-report/export',
    method: 'post',
    data: params
  })
}

// 批量操作监管报告
export function batchOperateSupervisionReports(operation, ids) {
  return transData({
    url: '/api/supervision-report/batch-operate',
    method: 'post',
    data: { operation, ids }
  })
}

// 获取报告审核历史
export function getReportAuditHistory(id) {
  return transData({
    url: '/api/supervision-report/audit-history',
    method: 'post',
    data: { id }
  })
}

// 提交报告审核
export function submitReportAudit(id, auditData) {
  return transData({
    url: '/api/supervision-report/submit-audit',
    method: 'post',
    data: { id, auditData }
  })
}

// 审核报告
export function auditReport(id, auditResult) {
  return transData({
    url: '/api/supervision-report/audit',
    method: 'post',
    data: { id, auditResult }
  })
}

// ==================== 报告模板管理 ====================

// 获取报告模板列表
export function getReportTemplatesList(params) {
  return transData({
    url: '/api/supervision-report/templates/list',
    method: 'post',
    data: params
  })
}

// 保存报告模板
export function saveReportTemplate(data) {
  return transData({
    url: '/api/supervision-report/templates/save',
    method: 'post',
    data: data
  })
}

// 删除报告模板
export function deleteReportTemplate(id) {
  return transData({
    url: '/api/supervision-report/templates/delete',
    method: 'post',
    data: { id }
  })
}

// 获取报告模板详情
export function getReportTemplateDetail(id) {
  return transData({
    url: '/api/supervision-report/templates/detail',
    method: 'post',
    data: { id }
  })
}

// 使用报告模板
export function useReportTemplate(id, reportData) {
  return transData({
    url: '/api/supervision-report/templates/use',
    method: 'post',
    data: { id, reportData }
  })
}

// 复制报告模板
export function copyReportTemplate(id, newName) {
  return transData({
    url: '/api/supervision-report/templates/copy',
    method: 'post',
    data: { id, newName }
  })
}

// 导出报告模板
export function exportReportTemplates(params) {
  return transData({
    url: '/api/supervision-report/templates/export',
    method: 'post',
    data: params
  })
}

// 导入报告模板
export function importReportTemplates(formData) {
  return transData({
    url: '/api/supervision-report/templates/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取模板分类
export function getTemplateCategories() {
  return transData({
    url: '/api/supervision-report/templates/categories',
    method: 'post'
  })
}

// 保存模板分类
export function saveTemplateCategory(categoryData) {
  return transData({
    url: '/api/supervision-report/templates/categories/save',
    method: 'post',
    data: categoryData
  })
}

// ==================== 报告分发管理 ====================

// 获取分发记录列表
export function getDistributionRecords(params) {
  return transData({
    url: '/api/supervision-report/distribution/records',
    method: 'post',
    data: params
  })
}

// 开始分发报告
export function startReportDistribution(distributionConfig) {
  return transData({
    url: '/api/supervision-report/distribution/start',
    method: 'post',
    data: distributionConfig
  })
}

// 设置定时分发
export function scheduleReportDistribution(scheduleConfig) {
  return transData({
    url: '/api/supervision-report/distribution/schedule',
    method: 'post',
    data: scheduleConfig
  })
}

// 获取分发详情
export function getDistributionDetail(id) {
  return transData({
    url: '/api/supervision-report/distribution/detail',
    method: 'post',
    data: { id }
  })
}

// 重试分发
export function retryDistribution(id) {
  return transData({
    url: '/api/supervision-report/distribution/retry',
    method: 'post',
    data: { id }
  })
}

// 取消分发
export function cancelDistribution(id) {
  return transData({
    url: '/api/supervision-report/distribution/cancel',
    method: 'post',
    data: { id }
  })
}

// 获取分发配置
export function getDistributionConfig() {
  return transData({
    url: '/api/supervision-report/distribution/config',
    method: 'post'
  })
}

// 保存分发配置
export function saveDistributionConfig(config) {
  return transData({
    url: '/api/supervision-report/distribution/config/save',
    method: 'post',
    data: config
  })
}

// 获取分发对象列表
export function getDistributionTargets(params) {
  return transData({
    url: '/api/supervision-report/distribution/targets',
    method: 'post',
    data: params
  })
}

// 保存分发对象
export function saveDistributionTarget(targetData) {
  return transData({
    url: '/api/supervision-report/distribution/targets/save',
    method: 'post',
    data: targetData
  })
}

// ==================== 报告统计分析 ====================

// 获取报告统计数据
export function getReportStatistics(params) {
  return transData({
    url: '/api/supervision-report/statistics/data',
    method: 'post',
    data: params
  })
}

// 获取报告类型分布
export function getReportTypeDistribution(params) {
  return transData({
    url: '/api/supervision-report/statistics/type-distribution',
    method: 'post',
    data: params
  })
}

// 获取报告生成趋势
export function getReportGenerationTrend(params) {
  return transData({
    url: '/api/supervision-report/statistics/generation-trend',
    method: 'post',
    data: params
  })
}

// 获取报告下载统计
export function getReportDownloadStats(params) {
  return transData({
    url: '/api/supervision-report/statistics/download-stats',
    method: 'post',
    data: params
  })
}

// 获取报告质量评分
export function getReportQualityScore(params) {
  return transData({
    url: '/api/supervision-report/statistics/quality-score',
    method: 'post',
    data: params
  })
}

// 获取报告使用情况
export function getReportUsageStats(params) {
  return transData({
    url: '/api/supervision-report/statistics/usage-stats',
    method: 'post',
    data: params
  })
}

// ==================== 报告生成配置 ====================

// 获取报告生成配置
export function getReportGenerationConfig() {
  return transData({
    url: '/api/supervision-report/generation/config',
    method: 'post'
  })
}

// 保存报告生成配置
export function saveReportGenerationConfig(config) {
  return transData({
    url: '/api/supervision-report/generation/config/save',
    method: 'post',
    data: config
  })
}

// 获取数据源配置
export function getDataSourceConfig() {
  return transData({
    url: '/api/supervision-report/generation/data-source-config',
    method: 'post'
  })
}

// 保存数据源配置
export function saveDataSourceConfig(config) {
  return transData({
    url: '/api/supervision-report/generation/data-source-config/save',
    method: 'post',
    data: config
  })
}

// 测试数据源连接
export function testDataSourceConnection(config) {
  return transData({
    url: '/api/supervision-report/generation/test-data-source',
    method: 'post',
    data: config
  })
}

// 获取报告字段配置
export function getReportFieldConfig(templateId) {
  return transData({
    url: '/api/supervision-report/generation/field-config',
    method: 'post',
    data: { templateId }
  })
}

// 保存报告字段配置
export function saveReportFieldConfig(config) {
  return transData({
    url: '/api/supervision-report/generation/field-config/save',
    method: 'post',
    data: config
  })
}

// ==================== 报告版本管理 ====================

// 获取报告版本列表
export function getReportVersions(reportId) {
  return transData({
    url: '/api/supervision-report/versions/list',
    method: 'post',
    data: { reportId }
  })
}

// 创建报告版本
export function createReportVersion(reportId, versionData) {
  return transData({
    url: '/api/supervision-report/versions/create',
    method: 'post',
    data: { reportId, versionData }
  })
}

// 比较报告版本
export function compareReportVersions(version1Id, version2Id) {
  return transData({
    url: '/api/supervision-report/versions/compare',
    method: 'post',
    data: { version1Id, version2Id }
  })
}

// 恢复报告版本
export function restoreReportVersion(versionId) {
  return transData({
    url: '/api/supervision-report/versions/restore',
    method: 'post',
    data: { versionId }
  })
}

// 删除报告版本
export function deleteReportVersion(versionId) {
  return transData({
    url: '/api/supervision-report/versions/delete',
    method: 'post',
    data: { versionId }
  })
}

// ==================== 报告评论和反馈 ====================

// 获取报告评论列表
export function getReportComments(reportId, params) {
  return transData({
    url: '/api/supervision-report/comments/list',
    method: 'post',
    data: { reportId, ...params }
  })
}

// 添加报告评论
export function addReportComment(reportId, comment) {
  return transData({
    url: '/api/supervision-report/comments/add',
    method: 'post',
    data: { reportId, comment }
  })
}

// 回复报告评论
export function replyReportComment(commentId, reply) {
  return transData({
    url: '/api/supervision-report/comments/reply',
    method: 'post',
    data: { commentId, reply }
  })
}

// 删除报告评论
export function deleteReportComment(commentId) {
  return transData({
    url: '/api/supervision-report/comments/delete',
    method: 'post',
    data: { commentId }
  })
}

// 获取报告反馈
export function getReportFeedback(reportId, params) {
  return transData({
    url: '/api/supervision-report/feedback/list',
    method: 'post',
    data: { reportId, ...params }
  })
}

// 提交报告反馈
export function submitReportFeedback(reportId, feedback) {
  return transData({
    url: '/api/supervision-report/feedback/submit',
    method: 'post',
    data: { reportId, feedback }
  })
}

// 处理报告反馈
export function handleReportFeedback(feedbackId, handleResult) {
  return transData({
    url: '/api/supervision-report/feedback/handle',
    method: 'post',
    data: { feedbackId, handleResult }
  })
}
