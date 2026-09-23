import request from '@/utils/request'

// 决策支持模块API接口

// ==================== 决策模型管理API ====================

// 分页查询决策模型
export function getDecisionModelPage(params) {
  return request({
    url: '/qqsk/decision-support/models/page',
    method: 'get',
    params
  })
}

// 查询决策模型列表
export function listDecisionModels(query) {
  return request({
    url: '/qqsk/decision-support/models',
    method: 'get',
    params: query
  })
}

// 查询决策模型详细
export function getDecisionModel(modelId) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId,
    method: 'get'
  })
}

// 新增决策模型
export function createDecisionModel(data) {
  return request({
    url: '/qqsk/decision-support/models',
    method: 'post',
    data: data
  })
}

// 新增决策模型（别名）
export function addDecisionModel(data) {
  return request({
    url: '/qqsk/decision-support/models',
    method: 'post',
    data: data
  })
}

// 修改决策模型
export function updateDecisionModel(data) {
  return request({
    url: '/qqsk/decision-support/models',
    method: 'put',
    data: data
  })
}

// 删除决策模型
export function deleteDecisionModel(modelIds) {
  return request({
    url: '/qqsk/decision-support/models/' + modelIds,
    method: 'delete'
  })
}

// 删除决策模型（别名）
export function delDecisionModel(modelIds) {
  return request({
    url: '/qqsk/decision-support/models/' + modelIds,
    method: 'delete'
  })
}

// 训练决策模型
export function trainDecisionModel(modelId, trainingData) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/train',
    method: 'post',
    params: { trainingData: trainingData }
  })
}

// 验证决策模型
export function validateDecisionModel(modelId, validationData) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/validate',
    method: 'post',
    params: { validationData: validationData }
  })
}

// 审批决策模型
export function approveDecisionModel(modelId) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/approve',
    method: 'post'
  })
}

// 激活决策模型
export function activateDecisionModel(modelId) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/activate',
    method: 'post'
  })
}

// 停用决策模型
export function deactivateDecisionModel(modelId) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/deactivate',
    method: 'post'
  })
}

// 查询活跃决策模型
export function getActiveDecisionModels() {
  return request({
    url: '/qqsk/decision-support/models/active',
    method: 'get'
  })
}

// 查询需要训练的模型
export function getModelsNeedTraining() {
  return request({
    url: '/qqsk/decision-support/models/need-training',
    method: 'get'
  })
}

// 批量训练模型
export function batchTrainModels(modelIds) {
  return request({
    url: '/qqsk/decision-support/models/batch-train',
    method: 'post',
    data: modelIds
  })
}

// 模型性能评估
export function evaluateModelPerformance(modelId) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/performance',
    method: 'get'
  })
}

// 生成模型优化建议
export function generateModelOptimizationSuggestions(modelId) {
  return request({
    url: '/qqsk/decision-support/models/' + modelId + '/optimization-suggestions',
    method: 'get'
  })
}

// ==================== 数据分析任务管理API ====================

// 分页查询数据分析任务
export function getDataAnalysisTaskPage(params) {
  return request({
    url: '/qqsk/decision/analysis/task/page',
    method: 'get',
    params
  })
}

// 查询数据分析任务列表
export function listAnalysisTasks(query) {
  return request({
    url: '/qqsk/decision/analysis/task',
    method: 'get',
    params: query
  })
}

// 查询数据分析任务详细
export function getAnalysisTask(taskId) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId,
    method: 'get'
  })
}

// 根据ID查询数据分析任务
export function getDataAnalysisTask(taskId) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId,
    method: 'get'
  })
}

// 新增数据分析任务
export function addAnalysisTask(data) {
  return request({
    url: '/qqsk/decision/analysis/task',
    method: 'post',
    data: data
  })
}

// 创建数据分析任务
export function createDataAnalysisTask(data) {
  return request({
    url: '/qqsk/decision/analysis/task',
    method: 'post',
    data: data
  })
}

// 修改数据分析任务
export function updateAnalysisTask(data) {
  return request({
    url: '/qqsk/decision/analysis/task/' + data.taskId,
    method: 'put',
    data: data
  })
}

// 更新数据分析任务
export function updateDataAnalysisTask(taskId, data) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId,
    method: 'put',
    data: data
  })
}

// 删除数据分析任务
export function delAnalysisTask(taskIds) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskIds,
    method: 'delete'
  })
}

// 删除数据分析任务
export function deleteDataAnalysisTask(taskId) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId,
    method: 'delete'
  })
}

// 批量删除数据分析任务
export function batchDeleteDataAnalysisTasks(taskIds) {
  return request({
    url: '/qqsk/decision/analysis/task/batch',
    method: 'delete',
    data: taskIds
  })
}

// 执行数据分析任务
export function executeAnalysisTask(taskId, executeUser) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId + '/execute',
    method: 'post',
    params: { executeUser }
  })
}

// 执行数据分析任务
export function executeDataAnalysisTask(taskId, executeUser) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId + '/execute',
    method: 'post',
    params: { executeUser }
  })
}

// 取消数据分析任务
export function cancelAnalysisTask(taskId, updateUser) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId + '/cancel',
    method: 'post',
    params: { updateUser }
  })
}

// 取消数据分析任务
export function cancelDataAnalysisTask(taskId, updateUser) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId + '/cancel',
    method: 'post',
    params: { updateUser }
  })
}

// 重试失败任务
export function retryFailedTask(taskId, executeUser) {
  return request({
    url: '/qqsk/decision/analysis/task/' + taskId + '/retry',
    method: 'post',
    params: { executeUser }
  })
}

// 查询待执行任务
export function getPendingTasks(orgId) {
  return request({
    url: '/qqsk/decision/analysis/task/pending',
    method: 'get',
    params: { orgId }
  })
}

// 查询运行中任务
export function getRunningTasks(orgId) {
  return request({
    url: '/qqsk/decision/analysis/task/running',
    method: 'get',
    params: { orgId }
  })
}

// 查询失败任务
export function getFailedTasks(orgId) {
  return request({
    url: '/qqsk/decision/analysis/task/failed',
    method: 'get',
    params: { orgId }
  })
}

// 获取任务统计信息
export function getTaskStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/analysis/task/statistics',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

// ==================== 决策建议API（旧版本，已废弃） ====================

// 以下函数已被新版本替代，保留注释以避免混淆
// 新版本函数在文件末尾的 "决策建议管理API" 部分

// // 查询决策建议列表
// export function listDecisionRecommendations(query) {
//   return request({
//     url: '/qqsk/decision-support/recommendations',
//     method: 'get',
//     params: query
//   })
// }

// // 查询决策建议详细
// export function getDecisionRecommendation(recommendationId) {
//   return request({
//     url: '/qqsk/decision-support/recommendations/' + recommendationId,
//     method: 'get'
//   })
// }

// // 新增决策建议
// export function addDecisionRecommendation(data) {
//   return request({
//     url: '/qqsk/decision-support/recommendations',
//     method: 'post',
//     data: data
//   })
// }

// // 删除决策建议
// export function delDecisionRecommendation(recommendationIds) {
//   return request({
//     url: '/qqsk/decision-support/recommendations/' + recommendationIds,
//     method: 'delete'
//   })
// }

// 查询高优先级建议
export function getHighPriorityRecommendations() {
  return request({
    url: '/qqsk/decision-support/recommendations/high-priority',
    method: 'get'
  })
}

// 查询待审核建议
export function getPendingRecommendations() {
  return request({
    url: '/qqsk/decision-support/recommendations/pending',
    method: 'get'
  })
}

// 生成智能建议
export function generateIntelligentRecommendations(recommendationType) {
  return request({
    url: '/qqsk/decision-support/recommendations/intelligent',
    method: 'post',
    params: { recommendationType: recommendationType }
  })
}

// ==================== 综合分析API ====================

// 生成决策支持仪表盘
export function generateDecisionSupportDashboard() {
  return request({
    url: '/qqsk/decision-support/dashboard',
    method: 'get'
  })
}

// 综合数据分析
export function comprehensiveDataAnalysis(analysisParams) {
  return request({
    url: '/qqsk/decision-support/comprehensive-analysis',
    method: 'post',
    data: analysisParams
  })
}

// 智能洞察分析
export function generateIntelligentInsights() {
  return request({
    url: '/qqsk/decision-support/intelligent-insights',
    method: 'get'
  })
}

// 风险预警分析
export function analyzeRiskAlerts() {
  return request({
    url: '/qqsk/decision-support/risk-alerts',
    method: 'get'
  })
}

// 业务优化建议
export function generateBusinessOptimizationSuggestions() {
  return request({
    url: '/qqsk/decision-support/business-optimization-suggestions',
    method: 'get'
  })
}

// 生成决策支持报告
export function generateDecisionSupportReport(startDate, endDate) {
  return request({
    url: '/qqsk/decision-support/report',
    method: 'get',
    params: { startDate: startDate, endDate: endDate }
  })
}

// ==================== 系统管理API ====================

// 系统健康检查
export function performSystemHealthCheck() {
  return request({
    url: '/qqsk/decision-support/system/health-check',
    method: 'get'
  })
}

// 清理过期数据
export function cleanupExpiredData(retentionDays) {
  return request({
    url: '/qqsk/decision-support/system/cleanup',
    method: 'post',
    params: { retentionDays: retentionDays }
  })
}

// 数据备份
export function backupData(backupPath) {
  return request({
    url: '/qqsk/decision-support/system/backup',
    method: 'post',
    params: { backupPath: backupPath }
  })
}

// 系统性能监控
export function monitorSystemPerformance() {
  return request({
    url: '/qqsk/decision-support/system/performance',
    method: 'get'
  })
}

// 生成系统使用统计
export function generateSystemUsageStatistics(startDate, endDate) {
  return request({
    url: '/qqsk/decision-support/system/usage-statistics',
    method: 'get',
    params: { startDate: startDate, endDate: endDate }
  })
}

// ==================== KPI指标管理API ====================

// 分页查询KPI指标
export function getKpiIndicatorPage(params) {
  return request({
    url: '/qqsk/decision/kpi/page',
    method: 'get',
    params
  })
}

// 根据ID查询KPI指标
export function getKpiIndicator(kpiId) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}`,
    method: 'get'
  })
}

// 创建KPI指标
export function createKpiIndicator(data) {
  return request({
    url: '/qqsk/decision/kpi',
    method: 'post',
    data
  })
}

// 更新KPI指标
export function updateKpiIndicator(kpiId, data) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}`,
    method: 'put',
    data
  })
}

// 删除KPI指标
export function deleteKpiIndicator(kpiId) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}`,
    method: 'delete'
  })
}

// 批量删除KPI指标
export function batchDeleteKpiIndicators(kpiIds) {
  return request({
    url: '/qqsk/decision/kpi/batch',
    method: 'delete',
    data: kpiIds
  })
}

// 计算KPI指标值
export function calculateKpiIndicator(kpiId) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}/calculate`,
    method: 'post'
  })
}

// 批量计算KPI指标
export function batchCalculateKpiIndicators(kpiIds) {
  return request({
    url: '/qqsk/decision/kpi/batch/calculate',
    method: 'post',
    data: kpiIds
  })
}

// 更新KPI当前值
export function updateKpiCurrentValue(kpiId, currentValue) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}/current-value`,
    method: 'put',
    params: { currentValue }
  })
}

// 更新KPI状态
export function updateKpiStatus(kpiId, kpiStatus) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}/status`,
    method: 'put',
    params: { kpiStatus }
  })
}

// 根据分类查询KPI指标
export function getKpisByCategory(kpiCategory, orgId) {
  return request({
    url: `/qqsk/decision/kpi/category/${kpiCategory}`,
    method: 'get',
    params: { orgId }
  })
}

// 根据类型查询KPI指标
export function getKpisByType(kpiType, orgId) {
  return request({
    url: `/qqsk/decision/kpi/type/${kpiType}`,
    method: 'get',
    params: { orgId }
  })
}

// 查询预警KPI指标
export function getWarningKpis(orgId) {
  return request({
    url: '/qqsk/decision/kpi/warning',
    method: 'get',
    params: { orgId }
  })
}

// 查询临界KPI指标
export function getCriticalKpis(orgId) {
  return request({
    url: '/qqsk/decision/kpi/critical',
    method: 'get',
    params: { orgId }
  })
}

// 获取KPI统计信息
export function getKpiStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/kpi/statistics',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

// 获取KPI类型分布
export function getKpiTypeDistribution(orgId) {
  return request({
    url: '/qqsk/decision/kpi/distribution/type',
    method: 'get',
    params: { orgId }
  })
}

// 获取KPI状态分布
export function getKpiStatusDistribution(orgId) {
  return request({
    url: '/qqsk/decision/kpi/distribution/status',
    method: 'get',
    params: { orgId }
  })
}

// 获取KPI目标完成情况
export function getKpiTargetCompletion(orgId) {
  return request({
    url: '/qqsk/decision/kpi/target-completion',
    method: 'get',
    params: { orgId }
  })
}

// 查询KPI历史数据
export function getKpiHistory(kpiId, startDate, endDate) {
  return request({
    url: `/qqsk/decision/kpi/${kpiId}/history`,
    method: 'get',
    params: { startDate, endDate }
  })
}

// 查询KPI排行榜
export function getKpiRanking(kpiType, orgId, limit) {
  return request({
    url: '/qqsk/decision/kpi/ranking',
    method: 'get',
    params: { kpiType, orgId, limit }
  })
}

// 查询KPI预警统计
export function getKpiWarningStatistics(orgId) {
  return request({
    url: '/qqsk/decision/kpi/warning-statistics',
    method: 'get',
    params: { orgId }
  })
}

// 导出KPI指标数据
export function exportKpiData(params) {
  return request({
    url: '/qqsk/decision/kpi/export',
    method: 'get',
    params
  })
}

// 系统健康检查
export function performKpiHealthCheck(orgId) {
  return request({
    url: '/qqsk/decision/kpi/health-check',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 预测分析管理API ====================

// 分页查询预测分析
export function getPredictiveAnalysisPage(query) {
  return request({
    url: '/qqsk/decision/predictive/page',
    method: 'get',
    params: query
  })
}

// 根据ID查询预测分析
export function getPredictiveAnalysisById(analysisId) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}`,
    method: 'get'
  })
}

// 创建预测分析
export function createPredictiveAnalysis(data) {
  return request({
    url: '/qqsk/decision/predictive',
    method: 'post',
    data: data
  })
}

// 更新预测分析
export function updatePredictiveAnalysis(analysisId, data) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}`,
    method: 'put',
    data: data
  })
}

// 删除预测分析
export function deletePredictiveAnalysis(analysisId) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}`,
    method: 'delete'
  })
}

// 批量删除预测分析
export function deletePredictiveAnalyses(analysisIds) {
  return request({
    url: '/qqsk/decision/predictive/batch',
    method: 'delete',
    data: analysisIds
  })
}

// 执行预测分析
export function executePredictiveAnalysis(analysisId, executeUser) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}/execute`,
    method: 'post',
    params: { executeUser }
  })
}

// 批量执行预测分析
export function batchExecutePredictiveAnalysis(analysisIds, executeUser) {
  return request({
    url: '/qqsk/decision/predictive/batch/execute',
    method: 'post',
    data: analysisIds,
    params: { executeUser }
  })
}

// 取消预测分析
export function cancelPredictiveAnalysis(analysisId, cancelUser) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}/cancel`,
    method: 'post',
    params: { cancelUser }
  })
}

// 重试失败的预测分析
export function retryFailedPredictiveAnalysis(analysisId, retryUser) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}/retry`,
    method: 'post',
    params: { retryUser }
  })
}

// 更新实际结果
export function updateActualResult(analysisId, actualResult, updateUser) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}/actual-result`,
    method: 'put',
    params: { actualResult, updateUser }
  })
}

// 计算预测准确率
export function calculatePredictionAccuracy(analysisId, calculateUser) {
  return request({
    url: `/qqsk/decision/predictive/${analysisId}/calculate-accuracy`,
    method: 'post',
    params: { calculateUser }
  })
}

// 获取预测分析统计信息
export function getPredictiveAnalysisStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/predictive/statistics',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

// 获取预测类型分布
export function getPredictionTypeDistribution(orgId) {
  return request({
    url: '/qqsk/decision/predictive/type-distribution',
    method: 'get',
    params: { orgId }
  })
}

// 获取分析状态分布
export function getAnalysisStatusDistribution(orgId) {
  return request({
    url: '/qqsk/decision/predictive/status-distribution',
    method: 'get',
    params: { orgId }
  })
}

// 获取预测趋势分析
export function getPredictionTrend(orgId, predictionType, startDate, endDate) {
  return request({
    url: '/qqsk/decision/predictive/trend',
    method: 'get',
    params: { orgId, predictionType, startDate, endDate }
  })
}

// 获取预测性能分析
export function getPredictionPerformance(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/predictive/performance',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

// 查询预测准确率排行
export function getAccuracyRanking(orgId, limit) {
  return request({
    url: '/qqsk/decision/predictive/accuracy-ranking',
    method: 'get',
    params: { orgId, limit }
  })
}

// 查询异常预测数据
export function getAbnormalPredictions(orgId) {
  return request({
    url: '/qqsk/decision/predictive/abnormal',
    method: 'get',
    params: { orgId }
  })
}

// 预测分析健康检查
export function performPredictiveAnalysisHealthCheck(orgId) {
  return request({
    url: '/qqsk/decision/predictive/health-check',
    method: 'get',
    params: { orgId }
  })
}

// 导出预测分析数据
export function exportPredictiveAnalysisData(query) {
  return request({
    url: '/qqsk/decision/predictive/export',
    method: 'get',
    params: query
  })
}

// ==================== 决策建议管理API ====================

// 分页查询决策建议
export function getDecisionRecommendationPage(query) {
  return request({
    url: '/qqsk/decision/recommendation/page',
    method: 'get',
    params: query
  })
}

// 根据ID查询决策建议
export function getDecisionRecommendationById(params) {
  return request({
    url: '/qqsk/recommendation/detail',
    method: 'get',
    params
  })
}

// 创建决策建议
export function createDecisionRecommendation(data) {
  return request({
    url: '/qqsk/decision/recommendation',
    method: 'post',
    data: data
  })
}

// 更新决策建议
export function updateDecisionRecommendation(recommendationId, data) {
  const requestData = {
    ...data,
    recommendationId: recommendationId
  }
  return request({
    url: '/qqsk/recommendation/update',
    method: 'post',
    data: requestData
  })
}

// 删除决策建议
export function deleteDecisionRecommendation(recommendationId) {
  return request({
    url: '/qqsk/recommendation/delete',
    method: 'post',
    data: { recommendationId }
  })
}

// 批量删除决策建议
export function deleteDecisionRecommendations(recommendationIds) {
  // 由于后端没有批量删除接口，前端循环调用单个删除
  const promises = recommendationIds.map(id => deleteDecisionRecommendation(id))
  return Promise.all(promises)
}

// 审核决策建议
export function reviewDecisionRecommendation(recommendationId, reviewOpinion, reviewUser) {
  return request({
    url: `/qqsk/decision/recommendation/${recommendationId}/review`,
    method: 'post',
    params: {
      reviewOpinion: reviewOpinion,
      reviewUser: reviewUser
    }
  })
}

// 批准决策建议
export function approveDecisionRecommendation(recommendationId, reviewOpinion, reviewUser) {
  return request({
    url: `/qqsk/decision/recommendation/${recommendationId}/approve`,
    method: 'post',
    params: {
      reviewOpinion: reviewOpinion,
      reviewUser: reviewUser
    }
  })
}

// 拒绝决策建议
export function rejectDecisionRecommendation(recommendationId, reviewOpinion, reviewUser) {
  return request({
    url: `/qqsk/decision/recommendation/${recommendationId}/reject`,
    method: 'post',
    params: {
      reviewOpinion: reviewOpinion,
      reviewUser: reviewUser
    }
  })
}

// 实施决策建议
export function implementDecisionRecommendation(recommendationId, implementationResult, implementUser) {
  return request({
    url: `/qqsk/decision/recommendation/${recommendationId}/implement`,
    method: 'post',
    params: {
      implementationResult: implementationResult,
      implementUser: implementUser
    }
  })
}

// 批量审核决策建议
export function batchReviewDecisionRecommendations(recommendationIds, recommendationStatus, reviewUser) {
  return request({
    url: '/qqsk/decision/recommendation/batch-review',
    method: 'post',
    data: recommendationIds,
    params: {
      recommendationStatus: recommendationStatus,
      reviewUser: reviewUser
    }
  })
}

// 批量实施决策建议
export function batchImplementDecisionRecommendations(recommendationIds, implementUser) {
  return request({
    url: '/qqsk/decision/recommendation/batch-implement',
    method: 'post',
    data: recommendationIds,
    params: {
      implementUser: implementUser
    }
  })
}

// 更新实施结果
export function updateImplementationResult(recommendationId, implementationResult, updateUser) {
  return request({
    url: `/qqsk/decision/recommendation/${recommendationId}/implementation-result`,
    method: 'put',
    params: {
      implementationResult: implementationResult,
      updateUser: updateUser
    }
  })
}

// 获取决策建议统计信息
export function getDecisionRecommendationStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/recommendation/statistics',
    method: 'get',
    params: {
      orgId: orgId,
      startDate: startDate,
      endDate: endDate
    }
  })
}

// 获取建议类型分布
export function getRecommendationTypeDistribution(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/type-distribution',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 获取建议状态分布
export function getRecommendationStatusDistribution(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/status-distribution',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 获取优先级分布
export function getPriorityDistribution(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/priority-distribution',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 获取置信度统计
export function getConfidenceStatistics(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/confidence-statistics',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 获取建议趋势分析
export function getRecommendationTrend(orgId, recommendationType, startDate, endDate) {
  return request({
    url: '/qqsk/decision/recommendation/trend',
    method: 'get',
    params: {
      orgId: orgId,
      recommendationType: recommendationType,
      startDate: startDate,
      endDate: endDate
    }
  })
}

// 获取建议执行效果分析
export function getRecommendationEffectiveness(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/recommendation/effectiveness',
    method: 'get',
    params: {
      orgId: orgId,
      startDate: startDate,
      endDate: endDate
    }
  })
}

// 查询建议置信度排行
export function getConfidenceRanking(orgId, limit) {
  return request({
    url: '/qqsk/decision/recommendation/confidence-ranking',
    method: 'get',
    params: {
      orgId: orgId,
      limit: limit
    }
  })
}

// 查询建议实施率统计
export function getImplementationRateStatistics(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/implementation-rate',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 查询建议质量评估
export function getRecommendationQualityAssessment(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/decision/recommendation/quality-assessment',
    method: 'get',
    params: {
      orgId: orgId,
      startDate: startDate,
      endDate: endDate
    }
  })
}

// 查询建议异常数据
export function getAbnormalRecommendations(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/abnormal',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 决策建议健康检查
export function performDecisionRecommendationHealthCheck(orgId) {
  return request({
    url: '/qqsk/decision/recommendation/health-check',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

// 导出决策建议数据
export function exportDecisionRecommendationData(query) {
  return request({
    url: '/qqsk/decision/recommendation/export',
    method: 'get',
    params: query
  })
}
