import { transData } from '@/utils/requestData'

// 获取智能AI统计数据
export function getIntelligentAIStatistics() {
  return transData({
    url: '/api/intelligent-ai/statistics',
    method: 'post'
  })
}

// ==================== AI模型管理 ====================

// 获取AI模型列表
export function getAIModelsList(params) {
  return transData({
    url: '/api/intelligent-ai/models/list',
    method: 'post',
    data: params
  })
}

// 保存AI模型
export function saveAIModel(data) {
  return transData({
    url: '/api/intelligent-ai/models/save',
    method: 'post',
    data: data
  })
}

// 删除AI模型
export function deleteAIModel(id) {
  return transData({
    url: '/api/intelligent-ai/models/delete',
    method: 'post',
    data: { id }
  })
}

// 获取AI模型详情
export function getAIModelDetail(id) {
  return transData({
    url: '/api/intelligent-ai/models/detail',
    method: 'post',
    data: { id }
  })
}

// 部署AI模型
export function deployAIModel(id) {
  return transData({
    url: '/api/intelligent-ai/models/deploy',
    method: 'post',
    data: { id }
  })
}

// 测试AI模型
export function testAIModel(id, testData) {
  return transData({
    url: '/api/intelligent-ai/models/test',
    method: 'post',
    data: { id, testData }
  })
}

// 启动模型训练
export function startModelTraining(modelId, trainingConfig) {
  return transData({
    url: '/api/intelligent-ai/models/train',
    method: 'post',
    data: { modelId, trainingConfig }
  })
}

// 获取可用模型列表
export function getAvailableModels() {
  return transData({
    url: '/api/intelligent-ai/models/available',
    method: 'post'
  })
}

// 导出AI模型数据
export function exportAIModels(params) {
  return transData({
    url: '/api/intelligent-ai/models/export',
    method: 'post',
    data: params
  })
}

// ==================== 智能预测分析 ====================

// 获取预测结果列表
export function getPredictionsList(params) {
  return transData({
    url: '/api/intelligent-ai/predictions/list',
    method: 'post',
    data: params
  })
}

// 开始预测分析
export function startPrediction(predictionConfig) {
  return transData({
    url: '/api/intelligent-ai/predictions/start',
    method: 'post',
    data: predictionConfig
  })
}

// 批量预测分析
export function batchPrediction(batchConfig) {
  return transData({
    url: '/api/intelligent-ai/predictions/batch',
    method: 'post',
    data: batchConfig
  })
}

// 获取预测详情
export function getPredictionDetail(id) {
  return transData({
    url: '/api/intelligent-ai/predictions/detail',
    method: 'post',
    data: { id }
  })
}

// 分析预测结果
export function analyzePrediction(id) {
  return transData({
    url: '/api/intelligent-ai/predictions/analyze',
    method: 'post',
    data: { id }
  })
}

// 获取预测图表数据
export function getPredictionChartData(params) {
  return transData({
    url: '/api/intelligent-ai/predictions/chart-data',
    method: 'post',
    data: params
  })
}

// ==================== 智能推荐系统 ====================

// 获取推荐结果列表
export function getRecommendationsList(params) {
  return transData({
    url: '/api/intelligent-ai/recommendations/list',
    method: 'post',
    data: params
  })
}

// 生成智能推荐
export function generateRecommendations(recommendationConfig) {
  return transData({
    url: '/api/intelligent-ai/recommendations/generate',
    method: 'post',
    data: recommendationConfig
  })
}

// 刷新推荐结果
export function refreshRecommendations(params) {
  return transData({
    url: '/api/intelligent-ai/recommendations/refresh',
    method: 'post',
    data: params
  })
}

// 获取推荐详情
export function getRecommendationDetail(id) {
  return transData({
    url: '/api/intelligent-ai/recommendations/detail',
    method: 'post',
    data: { id }
  })
}

// 采纳推荐
export function applyRecommendation(id, applyData) {
  return transData({
    url: '/api/intelligent-ai/recommendations/apply',
    method: 'post',
    data: { id, applyData }
  })
}

// 推荐反馈
export function feedbackRecommendation(id, feedback) {
  return transData({
    url: '/api/intelligent-ai/recommendations/feedback',
    method: 'post',
    data: { id, feedback }
  })
}

// 获取推荐配置
export function getRecommendationConfig() {
  return transData({
    url: '/api/intelligent-ai/recommendations/config',
    method: 'post'
  })
}

// 保存推荐配置
export function saveRecommendationConfig(config) {
  return transData({
    url: '/api/intelligent-ai/recommendations/config/save',
    method: 'post',
    data: config
  })
}

// ==================== 模型训练监控 ====================

// 获取训练任务列表
export function getTrainingTasksList(params) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/list',
    method: 'post',
    data: params
  })
}

// 创建训练任务
export function createTrainingTask(taskData) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/create',
    method: 'post',
    data: taskData
  })
}

// 保存训练任务（新增或更新）
export function saveTrainingTask(taskData) {
  return transData({
    url: taskData.id ? '/api/intelligent-ai/training/tasks/update' : '/api/intelligent-ai/training/tasks/create',
    method: 'post',
    data: taskData
  })
}

// 获取训练任务详情
export function getTrainingTaskDetail(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/detail',
    method: 'post',
    data: { id }
  })
}

// 停止训练任务
export function stopTrainingTask(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/stop',
    method: 'post',
    data: { id }
  })
}

// 启动训练
export function startTraining(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/start',
    method: 'post',
    data: { id }
  })
}

// 停止训练
export function stopTraining(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/stop',
    method: 'post',
    data: { id }
  })
}

// 继续训练任务
export function resumeTrainingTask(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/resume',
    method: 'post',
    data: { id }
  })
}

// 删除训练任务
export function deleteTrainingTask(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/delete',
    method: 'post',
    data: { id }
  })
}

// 获取训练进度
export function getTrainingProgress(id) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/progress',
    method: 'post',
    data: { id }
  })
}

// 获取训练日志
export function getTrainingLogs(id, params) {
  return transData({
    url: '/api/intelligent-ai/training/tasks/logs',
    method: 'post',
    data: { id, ...params }
  })
}

// ==================== 训练数据管理 ====================

// 获取训练数据列表
export function getTrainingDataList(params) {
  return transData({
    url: '/api/intelligent-ai/training/data/list',
    method: 'post',
    data: params
  })
}

// 上传训练数据
export function uploadTrainingData(formData) {
  return transData({
    url: '/api/intelligent-ai/training/data/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 删除训练数据
export function deleteTrainingData(id) {
  return transData({
    url: '/api/intelligent-ai/training/data/delete',
    method: 'post',
    data: { id }
  })
}

// 验证训练数据
export function validateTrainingData(id) {
  return transData({
    url: '/api/intelligent-ai/training/data/validate',
    method: 'post',
    data: { id }
  })
}

// 预处理训练数据
export function preprocessTrainingData(id, preprocessConfig) {
  return transData({
    url: '/api/intelligent-ai/training/data/preprocess',
    method: 'post',
    data: { id, preprocessConfig }
  })
}

// ==================== 高级分析功能 ====================

// 获取分析模型列表
export function getAnalysisModelsList(params) {
  return transData({
    url: '/api/intelligent-ai/analysis/models/list',
    method: 'post',
    data: params
  })
}

// 执行高级分析
export function executeAdvancedAnalysis(analysisConfig) {
  return transData({
    url: '/api/intelligent-ai/analysis/execute',
    method: 'post',
    data: analysisConfig
  })
}

// 获取分析结果
export function getAnalysisResults(params) {
  return transData({
    url: '/api/intelligent-ai/analysis/results',
    method: 'post',
    data: params
  })
}

// 保存分析结果
export function saveAnalysisResult(resultData) {
  return transData({
    url: '/api/intelligent-ai/analysis/results/save',
    method: 'post',
    data: resultData
  })
}

// 导出分析结果
export function exportAnalysisResults(params) {
  return transData({
    url: '/api/intelligent-ai/analysis/results/export',
    method: 'post',
    data: params
  })
}

// ==================== 决策支持分析 ====================

// 获取决策支持分析列表
export function getDecisionSupportAnalysisList(params) {
  return transData({
    url: '/api/intelligent-ai/decision-support/list',
    method: 'post',
    data: params
  })
}

// 创建决策支持分析
export function createDecisionSupportAnalysis(analysisData) {
  return transData({
    url: '/api/intelligent-ai/decision-support/create',
    method: 'post',
    data: analysisData
  })
}

// 获取决策支持分析详情
export function getDecisionSupportAnalysisDetail(id) {
  return transData({
    url: '/api/intelligent-ai/decision-support/detail',
    method: 'post',
    data: { id }
  })
}

// 执行决策支持分析
export function executeDecisionSupportAnalysis(id) {
  return transData({
    url: '/api/intelligent-ai/decision-support/execute',
    method: 'post',
    data: { id }
  })
}

// 获取决策方案评估
export function getDecisionSchemeEvaluation(params) {
  return transData({
    url: '/api/intelligent-ai/decision-support/scheme-evaluation',
    method: 'post',
    data: params
  })
}

// 保存决策方案评估
export function saveDecisionSchemeEvaluation(evaluationData) {
  return transData({
    url: '/api/intelligent-ai/decision-support/scheme-evaluation/save',
    method: 'post',
    data: evaluationData
  })
}

// ==================== 系统配置 ====================

// 获取AI系统配置
export function getAISystemConfig() {
  return transData({
    url: '/api/intelligent-ai/system/config',
    method: 'post'
  })
}

// 保存AI系统配置
export function saveAISystemConfig(config) {
  return transData({
    url: '/api/intelligent-ai/system/config/save',
    method: 'post',
    data: config
  })
}

// 获取AI服务状态
export function getAIServiceStatus() {
  return transData({
    url: '/api/intelligent-ai/system/status',
    method: 'post'
  })
}

// 重启AI服务
export function restartAIService() {
  return transData({
    url: '/api/intelligent-ai/system/restart',
    method: 'post'
  })
}

// 获取AI系统日志
export function getAISystemLogs(params) {
  return transData({
    url: '/api/intelligent-ai/system/logs',
    method: 'post',
    data: params
  })
}

// 清理AI系统缓存
export function clearAISystemCache() {
  return transData({
    url: '/api/intelligent-ai/system/cache/clear',
    method: 'post'
  })
}
