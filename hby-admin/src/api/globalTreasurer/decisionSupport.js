import request from '@/utils/request'

// 决策支持模块API接口 - 匹配后端 qqsk-service

// ==================== 决策模型管理API ====================

// 分页查询决策模型 - 使用GET请求
export function getDecisionModelPage(params) {
  console.log('[API-决策模型] 请求URL:', '/qqsk/decisionModel/page')
  console.log('[API-决策模型] 请求参数:', params)

  return request({
    url: '/qqsk/decisionModel/page',
    method: 'get',
    params: params  // GET请求使用params参数，会自动拼接到URL上
  }).then(response => {
    console.log('[API-决策模型] 响应数据:', response)
    return response
  }).catch(error => {
    console.error('[API-决策模型] 请求失败:', error)
    throw error
  })
}

// 查询决策模型详细
export function getDecisionModel(modelId) {
  return request({
    url: `/qqsk/decisionModel/${modelId}`,
    method: 'get'
  })
}

// 新增决策模型
export function createDecisionModel(data) {
  return request({
    url: '/qqsk/decisionModel',
    method: 'post',
    data: data
  })
}

// 修改决策模型
export function updateDecisionModel(data) {
  console.log('[API-修改] ========== 开始 ==========');
  console.log('[API-修改] 完整数据:', JSON.stringify(data, null, 2));
  console.log('[API-修改] modelId:', data.modelId, '类型:', typeof data.modelId);
  console.log('[API-修改] modelName:', data.modelName);

  // 确保modelId是数字类型
  const requestData = {
    ...data,
    modelId: parseInt(data.modelId)
  };

  console.log('[API-修改] 最终发送数据:', JSON.stringify(requestData, null, 2));

  // 使用JSON格式提交，确保后端能正确接收
  return request({
    url: '/qqsk/decisionModel/update',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: requestData,
    transformRequest: [(data) => {
      // 覆盖默认的 transformRequest，确保数据不被序列化
      return JSON.stringify(data)
    }]
  })
}

// 删除决策模型
export function deleteDecisionModel(modelId) {
  return request({
    url: `/qqsk/decisionModel/${modelId}`,
    method: 'delete'
  })
}

// 批量删除决策模型
export function batchDeleteDecisionModels(modelIds) {
  return request({
    url: `/qqsk/decisionModel/${modelIds.join(',')}`,  // 路径参数
    method: 'delete'
  })
}

// 导出决策模型数据
export function exportDecisionModelData(query) {
  return request({
    url: '/qqsk/decisionModel/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 训练决策模型
export function trainDecisionModel(modelId, trainingData) {
  return request({
    url: `/qqsk/decisionModel/${modelId}/train`,
    method: 'post',
    data: trainingData
  })
}

// ==================== 数据分析任务管理API ====================

// 分页查询数据分析任务
export function getDataAnalysisTaskPage(params) {
  console.log('[API-数据分析任务] 请求URL:', '/qqsk/dataAnalysisTask/page');
  console.log('[API-数据分析任务] 请求参数:', params);
  return request({
    url: '/qqsk/dataAnalysisTask/page',
    method: 'get',
    params
  }).then(response => {
    console.log('[API-数据分析任务] 响应数据:', response);
    return response;
  }).catch(error => {
    console.error('[API-数据分析任务] 请求失败:', error);
    throw error;
  });
}

// 查询数据分析任务详细
export function getDataAnalysisTask(taskId) {
  return request({
    url: `/qqsk/dataAnalysisTask/${taskId}`,
    method: 'get'
  })
}

// 新增数据分析任务
export function createDataAnalysisTask(data) {
  return request({
    url: '/qqsk/dataAnalysisTask',
    method: 'post',
    data: data
  })
}

// 修改数据分析任务
export function updateDataAnalysisTask(data) {
  console.log('[API-修改任务] ========== 开始 ==========');
  console.log('[API-修改任务] 完整数据:', JSON.stringify(data, null, 2));
  console.log('[API-修改任务] taskId:', data.taskId, '类型:', typeof data.taskId);

  // 确保taskId是数字类型
  const requestData = {
    ...data,
    taskId: parseInt(data.taskId)
  };

  console.log('[API-修改任务] 最终发送数据:', JSON.stringify(requestData, null, 2));

  // 使用JSON格式提交，确保后端能正确接收
  return request({
    url: '/qqsk/dataAnalysisTask/update',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: requestData,
    transformRequest: [(data) => {
      // 覆盖默认的 transformRequest，确保数据不被序列化
      return JSON.stringify(data)
    }]
  })
}

// 删除数据分析任务
export function deleteDataAnalysisTask(taskId) {
  return request({
    url: `/qqsk/dataAnalysisTask/${taskId}`,
    method: 'delete'
  })
}

// 批量删除数据分析任务
export function batchDeleteDataAnalysisTasks(taskIds) {
  console.log('[API-批量删除任务] ========== 开始 ==========');
  console.log('[API-批量删除任务] taskIds:', taskIds);
  console.log('[API-批量删除任务] taskIds类型:', typeof taskIds);
  console.log('[API-批量删除任务] 是否为数组:', Array.isArray(taskIds));

  if (!Array.isArray(taskIds) || taskIds.length === 0) {
    console.error('[API-批量删除任务] taskIds不是数组或为空');
    return Promise.reject({ code: 0, message: '任务ID列表不能为空' });
  }

  // 使用POST方法，taskIds作为URL参数传递
  return request({
    url: '/qqsk/dataAnalysisTask/batch-delete',
    method: 'post',
    params: {
      taskIds: taskIds.join(',')  // 将数组转换为逗号分隔的字符串
    }
  })
}

// 执行数据分析任务
export function executeDataAnalysisTask(taskId, executeUser) {
  return request({
    url: `/qqsk/dataAnalysisTask/${taskId}/execute`,
    method: 'post',
    params: { executeUser }
  })
}

// 取消数据分析任务
export function cancelDataAnalysisTask(taskId, updateUser) {
  return request({
    url: `/qqsk/dataAnalysisTask/${taskId}/cancel`,
    method: 'post',
    params: { updateUser }
  })
}

// 重试失败任务
export function retryFailedTask(taskId, executeUser) {
  return request({
    url: `/qqsk/dataAnalysisTask/${taskId}/retry`,
    method: 'post',
    params: { executeUser }
  })
}

// 导出数据分析任务
export function exportDataAnalysisTasks(params) {
  return request({
    url: '/qqsk/dataAnalysisTask/export',
    method: 'get',
    params: params,
    responseType: 'blob'
  })
}

// 查询待执行任务
export function getPendingTasks(orgId) {
  return request({
    url: '/qqsk/dataAnalysisTask/pending',
    method: 'get',
    params: { orgId }
  })
}

// 查询运行中任务
export function getRunningTasks(orgId) {
  return request({
    url: '/qqsk/dataAnalysisTask/running',
    method: 'get',
    params: { orgId }
  })
}

// 查询失败任务
export function getFailedTasks(orgId) {
  return request({
    url: '/qqsk/dataAnalysisTask/failed',
    method: 'get',
    params: { orgId }
  })
}

// 获取任务统计信息
export function getTaskStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/dataAnalysisTask/statistics',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

// ==================== KPI指标管理API ====================

// 分页查询KPI指标
export function getKpiIndicatorPage(params) {
  console.log('[API-KPI指标] 请求URL:', '/qqsk/kpiIndicator/page');
  console.log('[API-KPI指标] 请求参数:', params);
  return request({
    url: '/qqsk/kpiIndicator/page',
    method: 'get',
    params
  }).then(response => {
    console.log('[API-KPI指标] 响应数据:', response);
    return response;
  }).catch(error => {
    console.error('[API-KPI指标] 请求失败:', error);
    throw error;
  });
}

// 根据ID查询KPI指标
export function getKpiIndicator(kpiId) {
  return request({
    url: `/qqsk/kpiIndicator/${kpiId}`,
    method: 'get'
  })
}

// 创建KPI指标
export function createKpiIndicator(data) {
  return request({
    url: '/qqsk/kpiIndicator',
    method: 'post',
    data
  })
}

// 更新KPI指标
export function updateKpiIndicator(data) {
  return request({
    url: '/qqsk/kpiIndicator/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    transformRequest: [(data) => {
      return JSON.stringify(data)
    }]
  })
}

// 删除KPI指标
export function deleteKpiIndicator(kpiId) {
  return request({
    url: `/qqsk/kpiIndicator/${kpiId}`,
    method: 'delete'
  })
}

// 批量删除KPI指标
export function batchDeleteKpiIndicators(kpiIds) {
  return request({
    url: '/qqsk/kpiIndicator/batch',
    method: 'delete',
    params: {
      kpiIds: kpiIds.join(',')  // 转换为逗号分隔的字符串
    }
  })
}

// 计算KPI指标值
export function calculateKpiIndicator(kpiId) {
  return request({
    url: `/qqsk/kpiIndicator/${kpiId}/calculate`,
    method: 'post'
  })
}

// 批量计算KPI指标
export function batchCalculateKpiIndicators(kpiIds) {
  return request({
    url: '/qqsk/kpiIndicator/batch/calculate',
    method: 'post',
    data: kpiIds
  })
}

// 更新KPI当前值
export function updateKpiCurrentValue(kpiId, currentValue) {
  return request({
    url: `/qqsk/kpiIndicator/${kpiId}/current-value`,
    method: 'put',
    params: { currentValue }
  })
}

// 更新KPI状态
export function updateKpiStatus(kpiId, kpiStatus) {
  return request({
    url: `/qqsk/kpiIndicator/${kpiId}/status`,
    method: 'put',
    params: { kpiStatus }
  })
}

// 根据分类查询KPI指标
export function getKpisByCategory(kpiCategory, orgId) {
  return request({
    url: `/qqsk/kpiIndicator/category/${kpiCategory}`,
    method: 'get',
    params: { orgId }
  })
}

// 根据类型查询KPI指标
export function getKpisByType(kpiType, orgId) {
  return request({
    url: `/qqsk/kpiIndicator/type/${kpiType}`,
    method: 'get',
    params: { orgId }
  })
}

// 查询预警KPI指标
export function getWarningKpis(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/warning',
    method: 'get',
    params: { orgId }
  })
}

// 查询临界KPI指标
export function getCriticalKpis(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/critical',
    method: 'get',
    params: { orgId }
  })
}

// 获取KPI统计信息
export function getKpiStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/kpiIndicator/statistics',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

// 获取KPI类型分布
export function getKpiTypeDistribution(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/distribution/type',
    method: 'get',
    params: { orgId }
  })
}

// 获取KPI状态分布
export function getKpiStatusDistribution(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/distribution/status',
    method: 'get',
    params: { orgId }
  })
}

// 获取KPI目标完成情况
export function getKpiTargetCompletion(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/target-completion',
    method: 'get',
    params: { orgId }
  })
}

// 查询KPI历史数据
export function getKpiHistory(kpiId, startDate, endDate) {
  return request({
    url: `/qqsk/kpiIndicator/${kpiId}/history`,
    method: 'get',
    params: { startDate, endDate }
  })
}

// 查询KPI排行榜
export function getKpiRanking(kpiType, orgId, limit) {
  return request({
    url: '/qqsk/kpiIndicator/ranking',
    method: 'get',
    params: { kpiType, orgId, limit }
  })
}

// 查询KPI预警统计
export function getKpiWarningStatistics(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/warning-statistics',
    method: 'get',
    params: { orgId }
  })
}

// 导出KPI指标数据
export function exportKpiData(params) {
  return request({
    url: '/qqsk/kpiIndicator/export',
    method: 'get',
    params: params,
    responseType: 'blob'
  })
}

// 系统健康检查
export function performKpiHealthCheck(orgId) {
  return request({
    url: '/qqsk/kpiIndicator/health-check',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 预测分析管理API ====================

// 分页查询预测分析
export function getPredictiveAnalysisPage(params) {
  console.log('[API-预测分析] 请求URL:', '/qqsk/financial/decision/predictive/page');
  console.log('[API-预测分析] 请求参数:', params);
  return request({
    url: '/qqsk/financial/decision/predictive/page',
    method: 'get',
    params  // GET请求使用params参数，会自动拼接到URL上
  }).then(response => {
    console.log('[API-预测分析] 响应数据:', response);
    return response;
  }).catch(error => {
    console.error('[API-预测分析] 请求失败:', error);
    throw error;
  });
}

// 根据ID查询预测分析
export function getPredictiveAnalysisById(analysisId) {
  return request({
    url: `/qqsk/financial/decision/predictive/${analysisId}`,
    method: 'get'
  })
}

// 创建预测分析
export function createPredictiveAnalysis(data) {
  return request({
    url: '/qqsk/financial/decision/predictive/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 更新预测分析
export function updatePredictiveAnalysis(analysisId, data) {
  const requestData = {
    ...data,
    analysisId: parseInt(analysisId)
  }
  return request({
    url: '/qqsk/predictiveAnalysis/update',
    method: 'post',
    data: requestData,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    transformRequest: [(data) => {
      return JSON.stringify(data)
    }]
  })
}

// 删除预测分析
export function deletePredictiveAnalysis(analysisId) {
  return request({
    url: '/qqsk/financial/decision/predictive/delete',
    method: 'post',
    data: { analysisId },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 批量删除预测分析
export function deletePredictiveAnalyses(analysisIds) {
  return request({
    url: '/qqsk/financial/decision/predictive/batch',
    method: 'delete',
    params: { analysisIds: analysisIds.join(',') },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 执行预测分析
export function executePredictiveAnalysis(analysisId, executeUser) {
  return request({
    url: `/qqsk/financial/decision/predictive/${analysisId}/execute`,
    method: 'post',
    params: { executeUser },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 批量执行预测分析
export function batchExecutePredictiveAnalysis(analysisIds, executeUser) {
  return request({
    url: '/qqsk/financial/decision/predictive/batch/execute',
    method: 'post',
    data: analysisIds,
    params: { executeUser },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 取消预测分析
export function cancelPredictiveAnalysis(analysisId, cancelUser) {
  return request({
    url: `/qqsk/financial/decision/predictive/${analysisId}/cancel`,
    method: 'post',
    params: { cancelUser },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 重试失败的预测分析
export function retryFailedPredictiveAnalysis(analysisId, retryUser) {
  return request({
    url: `/qqsk/financial/decision/predictive/${analysisId}/retry`,
    method: 'post',
    params: { retryUser },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 决策建议管理API ====================

// 分页查询决策建议
export function getDecisionRecommendationPage(params) {
  console.log('=== 分页查询调试信息 ===');
  console.log('原始params:', params);

  // 只发送后端需要的字段，过滤掉null和undefined值
  const cleanParams = {
    pageNo: params.pageNo || 1,  // 修复：使用pageNo而不是pageNum
    pageSize: params.pageSize || 10
  };

  // 添加可选参数（只有非空时才添加）
  if (params.recommendationNo != null && params.recommendationNo !== '') {
    cleanParams.recommendationNo = params.recommendationNo;
  }
  if (params.recommendationName != null && params.recommendationName !== '') {
    cleanParams.recommendationName = params.recommendationName;
  }
  if (params.recommendationType != null && params.recommendationType !== '') {
    cleanParams.recommendationType = params.recommendationType;
  }
  if (params.recommendationStatus != null && params.recommendationStatus !== '') {
    cleanParams.recommendationStatus = params.recommendationStatus;
  }
  if (params.priority != null && params.priority !== '') {
    cleanParams.priority = params.priority;
  }

  console.log('清理后的params:', cleanParams);

  const requestConfig = {
    url: '/qqsk/recommendation/page',
    method: 'get',
    params: cleanParams
  };
  console.log('axios请求配置:', requestConfig);
  return request(requestConfig);
}

// 根据ID查询决策建议
export function getDecisionRecommendationById(recommendationId) {
  console.log('=== API层调试信息 ===');
  console.log('getDecisionRecommendationById接收到的recommendationId:', recommendationId);
  const requestConfig = {
    url: '/qqsk/recommendation/detail',
    method: 'get',
    params: {
      recommendationId: recommendationId
    }
  };
  console.log('axios请求配置:', requestConfig);
  return request(requestConfig);
}

// 创建决策建议
export function createDecisionRecommendation(data) {
  // 将数据转换为URL编码格式
  const formData = new FormData()
  for (const key in data) {
    if (data[key] !== null && data[key] !== undefined && data[key] !== '') {
      formData.append(key, data[key])
    }
  }
  return request({
    url: '/qqsk/recommendation/create',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    transformRequest: [(data) => {
      return data
    }]
  })
}

// 更新决策建议
export function updateDecisionRecommendation(recommendationId, data) {
  // 将数据转换为FormData格式
  const formData = new FormData()
  const requestData = {
    ...data,
    recommendationId: recommendationId
  }

  // 排除不需要更新的时间字段（避免类型转换错误）
  const excludedFields = [
    'createTime',
    'updateTime',
    'approvalDate',
    'reviewDate',
    'implementationDate'
  ]

  for (const key in requestData) {
    // 跳过排除的字段
    if (excludedFields.includes(key)) {
      console.log(`跳过字段: ${key} = ${requestData[key]}`)
      continue
    }
    // 跳过空值
    if (requestData[key] !== null && requestData[key] !== undefined && requestData[key] !== '') {
      formData.append(key, requestData[key])
    }
  }

  console.log('=== 更新决策建议 ===')
  console.log('原始数据:', data)
  console.log('排除字段:', excludedFields)
  console.log('FormData内容:', Array.from(formData.entries()))

  return request({
    url: '/qqsk/recommendation/update',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    transformRequest: [(data) => {
      return data
    }]
  })
}

// 删除决策建议
export function deleteDecisionRecommendation(recommendationId) {
  // 使用FormData格式，与后端@RequestParam配合
  const formData = new FormData()
  formData.append('recommendationId', recommendationId)

  console.log('=== 删除决策建议 ===')
  console.log('recommendationId:', recommendationId)
  console.log('FormData内容:', Array.from(formData.entries()))

  return request({
    url: '/qqsk/recommendation/delete',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    transformRequest: [(data) => {
      return data
    }]
  })
}

// 批量删除决策建议（循环调用单个删除）
export function deleteDecisionRecommendations(recommendationIds) {
  // 由于后端没有批量删除接口，前端循环调用单个删除
  const promises = recommendationIds.map(id => deleteDecisionRecommendation(id))
  return Promise.all(promises)
}

// ==================== 额外方法（用于统计分析等） ====================

// 预测分析额外方法
export function updateActualResult(analysisId, actualResult, updateUser) {
  return request({
    url: `/qqsk/financial/decision/predictive/${analysisId}/actual-result`,
    method: 'put',
    params: { actualResult, updateUser }
  })
}

export function calculatePredictionAccuracy(analysisId, calculateUser) {
  return request({
    url: `/qqsk/financial/decision/predictive/${analysisId}/calculate-accuracy`,
    method: 'post',
    params: { calculateUser }
  })
}

export function getPredictiveAnalysisStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/financial/decision/predictive/statistics',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

export function getPredictionTypeDistribution(orgId) {
  return request({
    url: '/qqsk/financial/decision/predictive/type-distribution',
    method: 'get',
    params: { orgId }
  })
}

export function getAnalysisStatusDistribution(orgId) {
  return request({
    url: '/qqsk/financial/decision/predictive/status-distribution',
    method: 'get',
    params: { orgId }
  })
}

export function getPredictionTrend(orgId, predictionType, startDate, endDate) {
  return request({
    url: '/qqsk/financial/decision/predictive/trend',
    method: 'get',
    params: { orgId, predictionType, startDate, endDate }
  })
}

export function getPredictionPerformance(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/financial/decision/predictive/performance',
    method: 'get',
    params: { orgId, startDate, endDate }
  })
}

export function getAccuracyRanking(orgId, limit) {
  return request({
    url: '/qqsk/financial/decision/predictive/accuracy-ranking',
    method: 'get',
    params: { orgId, limit }
  })
}

export function getAbnormalPredictions(orgId) {
  return request({
    url: '/qqsk/financial/decision/predictive/abnormal',
    method: 'get',
    params: { orgId }
  })
}

export function performPredictiveAnalysisHealthCheck(orgId) {
  return request({
    url: '/qqsk/financial/decision/predictive/health-check',
    method: 'get',
    params: { orgId }
  })
}

export function exportPredictiveAnalysisData(query) {
  return request({
    url: '/qqsk/financial/decision/predictive/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 决策建议额外方法
export function reviewDecisionRecommendation(recommendationId, reviewOpinion, reviewUser) {
  return request({
    url: `/qqsk/recommendation/${recommendationId}/review`,
    method: 'post',
    params: {
      reviewOpinion: reviewOpinion,
      reviewUser: reviewUser
    }
  })
}

export function approveDecisionRecommendation(recommendationId, reviewOpinion, reviewUser) {
  return request({
    url: `/qqsk/recommendation/${recommendationId}/approve`,
    method: 'post',
    params: {
      reviewOpinion: reviewOpinion,
      reviewUser: reviewUser
    }
  })
}

export function rejectDecisionRecommendation(recommendationId, reviewOpinion, reviewUser) {
  return request({
    url: `/qqsk/recommendation/${recommendationId}/reject`,
    method: 'post',
    params: {
      reviewOpinion: reviewOpinion,
      reviewUser: reviewUser
    }
  })
}

export function implementDecisionRecommendation(recommendationId, implementationResult, implementUser) {
  return request({
    url: `/qqsk/recommendation/${recommendationId}/implement`,
    method: 'post',
    params: {
      implementationResult: implementationResult,
      implementUser: implementUser
    }
  })
}

export function batchReviewDecisionRecommendations(recommendationIds, recommendationStatus, reviewUser) {
  return request({
    url: '/qqsk/recommendation/batch-review',
    method: 'post',
    data: recommendationIds,
    params: {
      recommendationStatus: recommendationStatus,
      reviewUser: reviewUser
    }
  })
}

export function batchImplementDecisionRecommendations(recommendationIds, implementUser) {
  return request({
    url: '/qqsk/recommendation/batch-implement',
    method: 'post',
    data: recommendationIds,
    params: {
      implementUser: implementUser
    }
  })
}

export function updateImplementationResult(recommendationId, implementationResult, updateUser) {
  return request({
    url: `/qqsk/recommendation/${recommendationId}/implementation-result`,
    method: 'put',
    params: {
      implementationResult: implementationResult,
      updateUser: updateUser
    }
  })
}

export function getDecisionRecommendationStatistics(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/recommendation/statistics',
    method: 'get',
    params: {
      orgId: orgId,
      startDate: startDate,
      endDate: endDate
    }
  })
}

export function getRecommendationTypeDistribution(orgId) {
  return request({
    url: '/qqsk/recommendation/type-distribution',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function getRecommendationStatusDistribution(orgId) {
  return request({
    url: '/qqsk/recommendation/status-distribution',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function getPriorityDistribution(orgId) {
  return request({
    url: '/qqsk/recommendation/priority-distribution',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function getConfidenceStatistics(orgId) {
  return request({
    url: '/qqsk/recommendation/confidence-statistics',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function getRecommendationTrend(orgId, recommendationType, startDate, endDate) {
  return request({
    url: '/qqsk/recommendation/trend',
    method: 'get',
    params: {
      orgId: orgId,
      recommendationType: recommendationType,
      startDate: startDate,
      endDate: endDate
    }
  })
}

export function getRecommendationEffectiveness(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/recommendation/effectiveness',
    method: 'get',
    params: {
      orgId: orgId,
      startDate: startDate,
      endDate: endDate
    }
  })
}

export function getConfidenceRanking(orgId, limit) {
  return request({
    url: '/qqsk/recommendation/confidence-ranking',
    method: 'get',
    params: {
      orgId: orgId,
      limit: limit
    }
  })
}

export function getImplementationRateStatistics(orgId) {
  return request({
    url: '/qqsk/recommendation/implementation-rate',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function getRecommendationQualityAssessment(orgId, startDate, endDate) {
  return request({
    url: '/qqsk/recommendation/quality-assessment',
    method: 'get',
    params: {
      orgId: orgId,
      startDate: startDate,
      endDate: endDate
    }
  })
}

export function getAbnormalRecommendations(orgId) {
  return request({
    url: '/qqsk/recommendation/abnormal',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function performDecisionRecommendationHealthCheck(orgId) {
  return request({
    url: '/qqsk/recommendation/health-check',
    method: 'get',
    params: {
      orgId: orgId
    }
  })
}

export function exportDecisionRecommendationData(query) {
  return request({
    url: '/qqsk/recommendation/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
