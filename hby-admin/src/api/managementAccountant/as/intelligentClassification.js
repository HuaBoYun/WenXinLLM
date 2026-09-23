import request from '@/utils/request'

// ==================== 基础CRUD操作 ====================

/**
 * 创建智能分类
 */
export function createClassification(data) {
  return request({
    url: '/accountant/as/intelligent-classification/create',
    method: 'post',
    data
  })
}

/**
 * 更新智能分类
 */
export function updateClassification(data) {
  return request({
    url: '/accountant/as/intelligent-classification/update',
    method: 'put',
    data
  })
}

/**
 * 删除智能分类
 */
export function deleteClassification(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/delete/${tenantId}/${classificationId}`,
    method: 'delete'
  })
}

/**
 * 根据ID查询智能分类
 */
export function getClassificationById(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/get/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

/**
 * 根据编号查询智能分类
 */
export function getClassificationByCode(tenantId, classificationCode) {
  return request({
    url: `/accountant/as/intelligent-classification/get-by-code/${tenantId}/${classificationCode}`,
    method: 'get'
  })
}

// ==================== 分页查询操作 ====================

/**
 * 分页查询智能分类
 */
export function getClassificationPage(params) {
  return request({
    url: '/accountant/as/intelligent-classification/page',
    method: 'get',
    params
  })
}

// ==================== 分类管理操作 ====================

/**
 * 启动分类训练
 */
export function startTraining(tenantId, classificationId, trainingConfig = {}) {
  return request({
    url: `/accountant/as/intelligent-classification/start-training/${tenantId}/${classificationId}`,
    method: 'post',
    data: trainingConfig
  })
}

/**
 * 停止分类训练
 */
export function stopTraining(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/stop-training/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 部署分类模型
 */
export function deployModel(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/deploy-model/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 取消部署分类模型
 */
export function undeployModel(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/undeploy-model/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 激活分类模型
 */
export function activateClassification(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/activate/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 停用分类模型
 */
export function deactivateClassification(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/deactivate/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 测试分类模型
 */
export function testClassification(tenantId, classificationId, testData) {
  return request({
    url: `/accountant/as/intelligent-classification/test/${tenantId}/${classificationId}`,
    method: 'post',
    data: testData
  })
}

/**
 * 预测分类结果
 */
export function predictClassification(tenantId, classificationId, inputData) {
  return request({
    url: `/accountant/as/intelligent-classification/predict/${tenantId}/${classificationId}`,
    method: 'post',
    data: inputData
  })
}

// ==================== 模型管理操作 ====================

/**
 * 更新模型版本
 */
export function updateModelVersion(tenantId, classificationId, newVersion) {
  return request({
    url: `/accountant/as/intelligent-classification/update-model-version/${tenantId}/${classificationId}`,
    method: 'put',
    params: { newVersion }
  })
}

/**
 * 回滚模型版本
 */
export function rollbackModelVersion(tenantId, classificationId, targetVersion) {
  return request({
    url: `/accountant/as/intelligent-classification/rollback-model-version/${tenantId}/${classificationId}`,
    method: 'post',
    params: { targetVersion }
  })
}

/**
 * 比较模型版本
 */
export function compareModelVersions(tenantId, classificationId, version1, version2) {
  return request({
    url: `/accountant/as/intelligent-classification/compare-model-versions/${tenantId}/${classificationId}`,
    method: 'get',
    params: { version1, version2 }
  })
}

/**
 * 获取模型信息
 */
export function getModelInfo(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-model-info/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

/**
 * 更新模型配置
 */
export function updateModelConfig(tenantId, classificationId, config) {
  return request({
    url: `/accountant/as/intelligent-classification/update-model-config/${tenantId}/${classificationId}`,
    method: 'put',
    data: config
  })
}

// ==================== 性能监控操作 ====================

/**
 * 获取分类性能指标
 */
export function getPerformanceMetrics(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-performance-metrics/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

/**
 * 获取分类准确率历史
 */
export function getAccuracyHistory(tenantId, classificationId, startTime, endTime) {
  return request({
    url: `/accountant/as/intelligent-classification/get-accuracy-history/${tenantId}/${classificationId}`,
    method: 'get',
    params: { startTime, endTime }
  })
}

/**
 * 获取预测统计
 */
export function getPredictionStats(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-prediction-stats/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

/**
 * 获取资源使用情况
 */
export function getResourceUsage(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-resource-usage/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

/**
 * 监控分类健康状态
 */
export function monitorClassificationHealth(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/monitor-health/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

// ==================== 查询统计操作 ====================

/**
 * 根据分类类型查询
 */
export function getClassificationsByType(tenantId, classificationType) {
  return request({
    url: `/accountant/as/intelligent-classification/get-by-type/${tenantId}`,
    method: 'get',
    params: { classificationType }
  })
}

/**
 * 根据分类状态查询
 */
export function getClassificationsByStatus(tenantId, classificationStatus) {
  return request({
    url: `/accountant/as/intelligent-classification/get-by-status/${tenantId}`,
    method: 'get',
    params: { classificationStatus }
  })
}

/**
 * 根据分类算法查询
 */
export function getClassificationsByAlgorithm(tenantId, classificationAlgorithm) {
  return request({
    url: `/accountant/as/intelligent-classification/get-by-algorithm/${tenantId}`,
    method: 'get',
    params: { classificationAlgorithm }
  })
}

/**
 * 查询活跃的分类
 */
export function getActiveClassifications(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-active/${tenantId}`,
    method: 'get'
  })
}

/**
 * 查询已部署的分类
 */
export function getDeployedClassifications(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-deployed/${tenantId}`,
    method: 'get'
  })
}

/**
 * 查询训练中的分类
 */
export function getTrainingClassifications(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/get-training/${tenantId}`,
    method: 'get'
  })
}

/**
 * 根据准确率范围查询
 */
export function getClassificationsByAccuracyRange(tenantId, minAccuracy, maxAccuracy) {
  return request({
    url: `/accountant/as/intelligent-classification/get-by-accuracy-range/${tenantId}`,
    method: 'get',
    params: { minAccuracy, maxAccuracy }
  })
}

/**
 * 查询最新版本的分类
 */
export function getLatestVersionByType(tenantId, classificationType) {
  return request({
    url: `/accountant/as/intelligent-classification/get-latest-version/${tenantId}`,
    method: 'get',
    params: { classificationType }
  })
}

/**
 * 查询性能最佳的分类
 */
export function getBestPerformanceByType(tenantId, classificationType) {
  return request({
    url: `/accountant/as/intelligent-classification/get-best-performance/${tenantId}`,
    method: 'get',
    params: { classificationType }
  })
}

// ==================== 统计分析操作 ====================

/**
 * 统计分类状态分布
 */
export function countByClassificationStatus(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/count-by-status/${tenantId}`,
    method: 'get'
  })
}

/**
 * 统计分类类型分布
 */
export function countByClassificationType(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/count-by-type/${tenantId}`,
    method: 'get'
  })
}

/**
 * 统计分类算法分布
 */
export function countByClassificationAlgorithm(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/count-by-algorithm/${tenantId}`,
    method: 'get'
  })
}

/**
 * 获取分类趋势
 */
export function getClassificationTrend(tenantId, startDate, endDate, granularity) {
  return request({
    url: `/accountant/as/intelligent-classification/get-classification-trend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, granularity }
  })
}

/**
 * 获取训练趋势
 */
export function getTrainingTrend(tenantId, startDate, endDate, granularity) {
  return request({
    url: `/accountant/as/intelligent-classification/get-training-trend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, granularity }
  })
}

/**
 * 获取预测趋势
 */
export function getPredictionTrend(tenantId, startDate, endDate, granularity) {
  return request({
    url: `/accountant/as/intelligent-classification/get-prediction-trend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, granularity }
  })
}

/**
 * 获取分类排行榜
 */
export function getClassificationRanking(tenantId, rankBy, limit = 10) {
  return request({
    url: `/accountant/as/intelligent-classification/get-ranking/${tenantId}`,
    method: 'get',
    params: { rankBy, limit }
  })
}

// ==================== 批量操作 ====================

/**
 * 批量创建分类
 */
export function batchCreateClassifications(classifications) {
  return request({
    url: '/accountant/as/intelligent-classification/batch-create',
    method: 'post',
    data: classifications
  })
}

/**
 * 批量更新分类状态
 */
export function batchUpdateStatus(tenantId, classificationIds, status) {
  return request({
    url: `/accountant/as/intelligent-classification/batch-update-status/${tenantId}`,
    method: 'put',
    data: classificationIds,
    params: { status }
  })
}

/**
 * 批量更新分类算法
 */
export function batchUpdateAlgorithm(tenantId, classificationIds, algorithm) {
  return request({
    url: `/accountant/as/intelligent-classification/batch-update-algorithm/${tenantId}`,
    method: 'put',
    data: classificationIds,
    params: { algorithm }
  })
}

/**
 * 批量删除分类
 */
export function batchDeleteClassifications(tenantId, classificationIds) {
  return request({
    url: `/accountant/as/intelligent-classification/batch-delete/${tenantId}`,
    method: 'delete',
    data: classificationIds
  })
}

/**
 * 批量训练分类
 */
export function batchTrainClassifications(tenantId, classificationIds, trainingConfig = {}) {
  return request({
    url: `/accountant/as/intelligent-classification/batch-train/${tenantId}`,
    method: 'post',
    data: classificationIds,
    params: trainingConfig
  })
}

/**
 * 批量部署分类
 */
export function batchDeployClassifications(tenantId, classificationIds) {
  return request({
    url: `/accountant/as/intelligent-classification/batch-deploy/${tenantId}`,
    method: 'post',
    data: classificationIds
  })
}

// ==================== 数据管理操作 ====================

/**
 * 导出分类数据
 */
export function exportClassificationData(tenantId, classificationIds) {
  return request({
    url: `/accountant/as/intelligent-classification/export/${tenantId}`,
    method: 'get',
    params: { classificationIds: classificationIds.join(',') }
  })
}

/**
 * 导入分类数据
 */
export function importClassificationData(tenantId, classificationData) {
  return request({
    url: `/accountant/as/intelligent-classification/import/${tenantId}`,
    method: 'post',
    data: classificationData
  })
}

/**
 * 清理过期数据
 */
export function cleanupExpiredData(tenantId, expiredDate) {
  return request({
    url: `/accountant/as/intelligent-classification/cleanup-expired/${tenantId}`,
    method: 'delete',
    params: { expiredDate }
  })
}

/**
 * 清理无效模型
 */
export function cleanupInvalidModels(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/cleanup-invalid/${tenantId}`,
    method: 'delete'
  })
}

// ==================== 系统维护操作 ====================

/**
 * 重建分类索引
 */
export function rebuildClassificationIndex(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/rebuild-index/${tenantId}`,
    method: 'post'
  })
}

/**
 * 优化分类性能
 */
export function optimizeClassificationPerformance(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/optimize-performance/${tenantId}`,
    method: 'post'
  })
}

/**
 * 检查分类健康状态
 */
export function checkClassificationHealth(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/check-health/${tenantId}`,
    method: 'get'
  })
}

/**
 * 生成分类报告
 */
export function generateClassificationReport(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/generate-report/${tenantId}/${classificationId}`,
    method: 'get'
  })
}

/**
 * 获取系统概览
 */
export function getSystemOverview(tenantId) {
  return request({
    url: `/accountant/as/intelligent-classification/system-overview/${tenantId}`,
    method: 'get'
  })
}

// ==================== 通知提醒操作 ====================

/**
 * 发送训练完成通知
 */
export function sendTrainingCompletionNotification(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/send-training-notification/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 发送部署成功通知
 */
export function sendDeploymentSuccessNotification(tenantId, classificationId) {
  return request({
    url: `/accountant/as/intelligent-classification/send-deployment-notification/${tenantId}/${classificationId}`,
    method: 'post'
  })
}

/**
 * 发送性能告警通知
 */
export function sendPerformanceAlertNotification(tenantId, classificationId, alertType) {
  return request({
    url: `/accountant/as/intelligent-classification/send-alert-notification/${tenantId}/${classificationId}`,
    method: 'post',
    params: { alertType }
  })
}

/**
 * 发送错误通知
 */
export function sendErrorNotification(tenantId, classificationId, errorMessage) {
  return request({
    url: `/accountant/as/intelligent-classification/send-error-notification/${tenantId}/${classificationId}`,
    method: 'post',
    params: { errorMessage }
  })
}

// ==================== 常量定义 ====================

// 分类类型
export const CLASSIFICATION_TYPES = {
  AUTO: 'AUTO',
  MANUAL: 'MANUAL',
  HYBRID: 'HYBRID',
  RULE_BASED: 'RULE_BASED',
  ML_BASED: 'ML_BASED'
}

// 分类状态
export const CLASSIFICATION_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  TRAINING: 'TRAINING',
  TESTING: 'TESTING',
  DEPLOYED: 'DEPLOYED'
}

// 分类算法
export const CLASSIFICATION_ALGORITHMS = {
  NAIVE_BAYES: 'NAIVE_BAYES',
  SVM: 'SVM',
  RANDOM_FOREST: 'RANDOM_FOREST',
  NEURAL_NETWORK: 'NEURAL_NETWORK',
  RULE_ENGINE: 'RULE_ENGINE'
}

// 分类类型选项
export const CLASSIFICATION_TYPE_OPTIONS = [
  { label: '自动分类', value: 'AUTO' },
  { label: '手动分类', value: 'MANUAL' },
  { label: '混合分类', value: 'HYBRID' },
  { label: '规则分类', value: 'RULE_BASED' },
  { label: '机器学习分类', value: 'ML_BASED' }
]

// 分类状态选项
export const CLASSIFICATION_STATUS_OPTIONS = [
  { label: '活跃', value: 'ACTIVE' },
  { label: '非活跃', value: 'INACTIVE' },
  { label: '训练中', value: 'TRAINING' },
  { label: '测试中', value: 'TESTING' },
  { label: '已部署', value: 'DEPLOYED' }
]

// 分类算法选项
export const CLASSIFICATION_ALGORITHM_OPTIONS = [
  { label: '朴素贝叶斯', value: 'NAIVE_BAYES' },
  { label: '支持向量机', value: 'SVM' },
  { label: '随机森林', value: 'RANDOM_FOREST' },
  { label: '神经网络', value: 'NEURAL_NETWORK' },
  { label: '规则引擎', value: 'RULE_ENGINE' }
]

// 排行榜类型选项
export const RANKING_TYPE_OPTIONS = [
  { label: '按准确率', value: 'accuracy' },
  { label: '按预测次数', value: 'prediction_count' },
  { label: '按成功率', value: 'success_rate' },
  { label: '按响应时间', value: 'response_time' },
  { label: '按创建时间', value: 'created_time' }
]

// 趋势粒度选项
export const TREND_GRANULARITY_OPTIONS = [
  { label: '按小时', value: 'hour' },
  { label: '按天', value: 'day' },
  { label: '按周', value: 'week' },
  { label: '按月', value: 'month' },
  { label: '按年', value: 'year' }
]

// ==================== 工具函数 ====================

/**
 * 获取分类类型标签
 */
export function getClassificationTypeLabel(type) {
  const option = CLASSIFICATION_TYPE_OPTIONS.find(item => item.value === type)
  return option ? option.label : type
}

/**
 * 获取分类状态标签
 */
export function getClassificationStatusLabel(status) {
  const option = CLASSIFICATION_STATUS_OPTIONS.find(item => item.value === status)
  return option ? option.label : status
}

/**
 * 获取分类算法标签
 */
export function getClassificationAlgorithmLabel(algorithm) {
  const option = CLASSIFICATION_ALGORITHM_OPTIONS.find(item => item.value === algorithm)
  return option ? option.label : algorithm
}

/**
 * 获取分类状态标签样式
 */
export function getClassificationStatusTagType(status) {
  const statusTagMap = {
    'ACTIVE': 'success',
    'INACTIVE': 'info',
    'TRAINING': 'warning',
    'TESTING': 'primary',
    'DEPLOYED': 'success'
  }
  return statusTagMap[status] || 'default'
}

/**
 * 格式化准确率显示
 */
export function formatAccuracy(accuracy) {
  if (accuracy === null || accuracy === undefined) {
    return '-'
  }
  return (accuracy * 100).toFixed(2) + '%'
}

/**
 * 格式化预测时间显示
 */
export function formatPredictionTime(time) {
  if (time === null || time === undefined) {
    return '-'
  }
  if (time < 1000) {
    return time + 'ms'
  } else {
    return (time / 1000).toFixed(2) + 's'
  }
}

/**
 * 格式化数据大小显示
 */
export function formatDataSize(size) {
  if (size === null || size === undefined) {
    return '-'
  }
  if (size < 1024) {
    return size + 'B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + 'KB'
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(2) + 'MB'
  } else {
    return (size / (1024 * 1024 * 1024)).toFixed(2) + 'GB'
  }
}

/**
 * 计算成功率
 */
export function calculateSuccessRate(successfulPredictions, totalPredictions) {
  if (!totalPredictions || totalPredictions === 0) {
    return 0
  }
  return (successfulPredictions / totalPredictions * 100).toFixed(2)
}

/**
 * 验证分类配置
 */
export function validateClassificationConfig(config) {
  const errors = []

  if (!config.classificationName) {
    errors.push('分类名称不能为空')
  }

  if (!config.classificationType) {
    errors.push('分类类型不能为空')
  }

  if (!config.classificationAlgorithm) {
    errors.push('分类算法不能为空')
  }

  if (config.confidenceThreshold !== undefined && (config.confidenceThreshold < 0 || config.confidenceThreshold > 1)) {
    errors.push('置信度阈值必须在0-1之间')
  }

  return errors
}

/**
 * 生成分类编号
 */
export function generateClassificationCode(type) {
  const timestamp = Date.now()
  const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
  return `${type}_${timestamp}_${random}`
}

// ==================== 快捷操作函数 ====================

/**
 * 快速创建自动分类
 */
export function quickCreateAutoClassification(tenantId, name, algorithm = 'RANDOM_FOREST') {
  const classificationData = {
    tenantId,
    classificationCode: generateClassificationCode('AUTO'),
    classificationName: name,
    classificationType: 'AUTO',
    classificationAlgorithm: algorithm,
    classificationStatus: 'INACTIVE',
    confidenceThreshold: 0.8,
    createdBy: 'system'
  }

  return createClassification(classificationData)
}

/**
 * 快速部署分类模型
 */
export function quickDeployClassification(tenantId, classificationId) {
  return Promise.all([
    activateClassification(tenantId, classificationId),
    deployModel(tenantId, classificationId)
  ])
}

/**
 * 快速获取分类概览
 */
export function quickGetClassificationOverview(tenantId) {
  return Promise.all([
    getSystemOverview(tenantId),
    countByClassificationStatus(tenantId),
    countByClassificationType(tenantId),
    countByClassificationAlgorithm(tenantId)
  ]).then(([overview, statusStats, typeStats, algorithmStats]) => {
    return {
      overview,
      statusStats,
      typeStats,
      algorithmStats
    }
  })
}
