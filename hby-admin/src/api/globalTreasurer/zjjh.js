import request from '@/utils/request'

// 资金计划管理API

/**
 * 分页查询资金计划
 * @param {Object} params 查询参数
 */
export function getFundPlanPage(params) {
  return request({
    url: '/qqsk/fund/plan/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询资金计划
 * @param {Number} planId 计划ID
 */
export function getFundPlan(planId) {
  return request({
    url: `/qqsk/fund/plan/${planId}`,
    method: 'get'
  })
}

/**
 * 根据计划编号查询资金计划
 * @param {String} planNo 计划编号
 * @param {Number} orgId 组织ID
 */
export function getFundPlanByNo(planNo, orgId) {
  return request({
    url: `/qqsk/fund/plan/no/${planNo}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 创建资金计划
 * @param {Object} data 计划数据
 */
export function createFundPlan(data) {
  return request({
    url: '/qqsk/fund/plan',
    method: 'post',
    data
  })
}

/**
 * 更新资金计划
 * @param {Object} data 计划数据
 */
export function updateFundPlan(data) {
  return request({
    url: '/qqsk/fund/plan',
    method: 'put',
    data
  })
}

/**
 * 删除资金计划
 * @param {Number} planId 计划ID
 */
export function deleteFundPlan(planId) {
  return request({
    url: `/qqsk/fund/plan/${planId}`,
    method: 'delete'
  })
}

/**
 * 提交资金计划
 * @param {Number} planId 计划ID
 * @param {Number} updateUser 更新用户
 */
export function submitFundPlan(planId, updateUser) {
  return request({
    url: `/qqsk/fund/plan/${planId}/submit`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 审批资金计划
 * @param {Number} planId 计划ID
 * @param {Boolean} approved 是否通过
 * @param {Number} approvalUser 审批用户
 * @param {String} approvalOpinion 审批意见
 */
export function approveFundPlan(planId, approved, approvalUser, approvalOpinion) {
  return request({
    url: `/qqsk/fund/plan/${planId}/approve`,
    method: 'put',
    params: { approved, approvalUser, approvalOpinion }
  })
}

/**
 * 执行资金计划
 * @param {Number} planId 计划ID
 * @param {Number} executeUser 执行用户
 */
export function executeFundPlan(planId, executeUser) {
  return request({
    url: `/qqsk/fund/plan/${planId}/execute`,
    method: 'put',
    params: { executeUser }
  })
}

/**
 * 完成资金计划
 * @param {Number} planId 计划ID
 * @param {Number} updateUser 更新用户
 */
export function completeFundPlan(planId, updateUser) {
  return request({
    url: `/qqsk/fund/plan/${planId}/complete`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 取消资金计划
 * @param {Number} planId 计划ID
 * @param {Number} updateUser 更新用户
 */
export function cancelFundPlan(planId, updateUser) {
  return request({
    url: `/qqsk/fund/plan/${planId}/cancel`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 根据计划状态查询资金计划
 * @param {String} planStatus 计划状态
 * @param {Number} orgId 组织ID
 */
export function getFundPlansByStatus(planStatus, orgId) {
  return request({
    url: `/qqsk/fund/plan/status/${planStatus}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据计划类型查询资金计划
 * @param {String} planType 计划类型
 * @param {Number} orgId 组织ID
 */
export function getFundPlansByType(planType, orgId) {
  return request({
    url: `/qqsk/fund/plan/type/${planType}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据日期范围查询资金计划
 * @param {String} startDate 开始日期
 * @param {String} endDate 结束日期
 * @param {Number} orgId 组织ID
 */
export function getFundPlansByDateRange(startDate, endDate, orgId) {
  return request({
    url: '/qqsk/fund/plan/date-range',
    method: 'get',
    params: { startDate, endDate, orgId }
  })
}

/**
 * 获取待审批的资金计划列表
 * @param {Number} orgId 组织ID
 */
export function getPendingFundPlans(orgId) {
  return request({
    url: '/qqsk/fund/plan/pending',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 获取执行中的资金计划列表
 * @param {Number} orgId 组织ID
 */
export function getExecutingFundPlans(orgId) {
  return request({
    url: '/qqsk/fund/plan/executing',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 生成资金计划编号
 * @param {Number} orgId 组织ID
 */
export function generateFundPlanNo(orgId) {
  return request({
    url: '/qqsk/fund/plan/generate-no',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 导入资金计划
 * @param {Array} plans 计划列表
 * @param {Number} userId 用户ID
 */
export function importFundPlans(plans, userId) {
  return request({
    url: '/qqsk/fund/plan/import',
    method: 'post',
    data: plans,
    params: { userId }
  })
}

/**
 * 导出资金计划
 * @param {Object} queryParam 查询参数
 * @param {Number} orgId 组织ID
 */
export function exportFundPlans(queryParam, orgId) {
  return request({
    url: '/qqsk/fund/plan/export',
    method: 'get',
    params: { ...queryParam, orgId }
  })
}

// 资金计划明细管理API

/**
 * 分页查询资金计划明细
 * @param {Object} params 查询参数
 */
export function getFundPlanDetailPage(params) {
  return request({
    url: '/qqsk/fund/plan-detail/page',
    method: 'get',
    params
  })
}

/**
 * 根据计划ID查询明细列表
 * @param {Number} planId 计划ID
 */
export function getFundPlanDetailsByPlan(planId) {
  return request({
    url: `/qqsk/fund/plan-detail/plan/${planId}`,
    method: 'get'
  })
}

/**
 * 创建资金计划明细
 * @param {Object} data 明细数据
 */
export function createFundPlanDetail(data) {
  return request({
    url: '/qqsk/fund/plan-detail',
    method: 'post',
    data
  })
}

/**
 * 更新资金计划明细
 * @param {Object} data 明细数据
 */
export function updateFundPlanDetail(data) {
  return request({
    url: '/qqsk/fund/plan-detail',
    method: 'put',
    data
  })
}

/**
 * 删除资金计划明细
 * @param {Number} detailId 明细ID
 */
export function deleteFundPlanDetail(detailId) {
  return request({
    url: `/qqsk/fund/plan-detail/${detailId}`,
    method: 'delete'
  })
}

/**
 * 执行资金计划明细
 * @param {Number} detailId 明细ID
 * @param {Object} data 执行数据（actualAmount, actualDate）
 */
export function executeFundPlanDetail(detailId, data) {
  return request({
    url: `/qqsk/fund/plan-detail/${detailId}/execute`,
    method: 'put',
    data
  })
}

/**
 * 调整资金计划明细
 * @param {Number} detailId 明细ID
 * @param {Object} data 调整数据（plannedAmount, plannedDate, remark）
 */
export function adjustFundPlanDetail(detailId, data) {
  return request({
    url: `/qqsk/fund/plan-detail/${detailId}/adjust`,
    method: 'put',
    data
  })
}

/**
 * 批量创建资金计划明细
 * @param {Array} details 明细列表
 */
export function batchCreateFundPlanDetails(details) {
  return request({
    url: '/qqsk/fund/plan-detail/batch',
    method: 'post',
    data: details
  })
}

/**
 * 计算计划明细差异
 * @param {Number} detailId 明细ID
 */
export function calculatePlanDetailVariance(detailId) {
  return request({
    url: `/qqsk/fund/plan-detail/${detailId}/variance`,
    method: 'get'
  })
}

// 资金预测管理API

/**
 * 分页查询资金预测
 * @param {Object} params 查询参数
 */
export function getFundForecastPage(params) {
  return request({
    url: '/qqsk/fund/forecast/page',
    method: 'get',
    params
  })
}

/**
 * 创建资金预测
 * @param {Object} data 预测数据
 */
export function createFundForecast(data) {
  return request({
    url: '/qqsk/fund/forecast',
    method: 'post',
    data
  })
}

/**
 * 更新资金预测
 * @param {Object} data 预测数据
 */
export function updateFundForecast(data) {
  return request({
    url: '/qqsk/fund/forecast',
    method: 'put',
    data
  })
}

/**
 * 删除资金预测
 * @param {Number} forecastId 预测ID
 */
export function deleteFundForecast(forecastId) {
  return request({
    url: `/qqsk/fund/forecast/${forecastId}`,
    method: 'delete'
  })
}

/**
 * 执行资金预测
 * @param {Number} forecastId 预测ID
 */
export function executeFundForecast(forecastId) {
  return request({
    url: `/qqsk/fund/forecast/${forecastId}/execute`,
    method: 'post'
  })
}

/**
 * 计算预测准确率
 * @param {Number} forecastId 预测ID
 */
export function calculateForecastAccuracy(forecastId) {
  return request({
    url: `/qqsk/fund/forecast/${forecastId}/accuracy`,
    method: 'get'
  })
}

// 资金计划模板管理API

/**
 * 分页查询资金计划模板
 * @param {Object} params 查询参数
 */
export function getFundPlanTemplatePage(params) {
  return request({
    url: '/qqsk/fund/template/page',
    method: 'get',
    params
  })
}

/**
 * 创建资金计划模板
 * @param {Object} data 模板数据
 */
export function createFundPlanTemplate(data) {
  return request({
    url: '/qqsk/fund/template',
    method: 'post',
    data
  })
}

/**
 * 根据模板创建资金计划
 * @param {Number} templateId 模板ID
 * @param {Object} planData 计划数据
 */
export function createFundPlanFromTemplate(templateId, planData) {
  return request({
    url: `/qqsk/fund/template/${templateId}/create-plan`,
    method: 'post',
    data: planData
  })
}

/**
 * 获取默认模板
 * @param {String} planType 计划类型
 * @param {Number} orgId 组织ID
 */
export function getDefaultTemplate(planType, orgId) {
  return request({
    url: '/qqsk/fund/template/default',
    method: 'get',
    params: { planType, orgId }
  })
}

/**
 * 更新资金计划模板
 * @param {Object} data 模板数据
 */
export function updateFundPlanTemplate(data) {
  return request({
    url: '/qqsk/fund/template',
    method: 'put',
    data
  })
}

/**
 * 删除资金计划模板
 * @param {Number} templateId 模板ID
 */
export function deleteFundPlanTemplate(templateId) {
  return request({
    url: `/qqsk/fund/template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 获取资金计划模板汇总信息
 * @param {Number} orgId 组织ID
 */
export function getFundPlanTemplateSummary(orgId) {
  return request({
    url: '/qqsk/fund/template/summary',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 激活资金计划模板
 * @param {Number} templateId 模板ID
 */
export function activateFundPlanTemplate(templateId) {
  return request({
    url: `/qqsk/fund/template/${templateId}/activate`,
    method: 'put'
  })
}

/**
 * 停用资金计划模板
 * @param {Number} templateId 模板ID
 */
export function deactivateFundPlanTemplate(templateId) {
  return request({
    url: `/qqsk/fund/template/${templateId}/deactivate`,
    method: 'put'
  })
}

/**
 * 使用资金计划模板
 * @param {Number} templateId 模板ID
 * @param {Object} planData 计划数据
 */
export function useFundPlanTemplate(templateId, planData) {
  return request({
    url: `/qqsk/fund/template/${templateId}/use`,
    method: 'post',
    data: planData
  })
}

// 资金计划审批相关API
/**
 * 审批计划
 * @param {Number} planId 计划ID
 * @param {Boolean} approved 是否通过
 * @param {String} opinion 审批意见
 */
export function approvePlan(planId, opinion) {
  return request({
    url: `/qqsk/fund/plan/${planId}/approve`,
    method: 'put',
    params: { approved: true, approvalOpinion: opinion }
  })
}

/**
 * 拒绝计划
 * @param {Number} planId 计划ID
 * @param {String} reason 拒绝原因
 */
export function rejectPlan(planId, reason) {
  return request({
    url: `/qqsk/fund/plan/${planId}/approve`,
    method: 'put',
    params: { approved: false, approvalOpinion: reason }
  })
}

/**
 * 提交计划
 * @param {Number} planId 计划ID
 */
export function submitPlan(planId) {
  return request({
    url: `/qqsk/fund/plan/${planId}/submit`,
    method: 'put'
  })
}

/**
 * 执行计划
 * @param {Number} planId 计划ID
 */
export function executePlan(planId) {
  return request({
    url: `/qqsk/fund/plan/${planId}/execute`,
    method: 'put'
  })
}

/**
 * 完成计划
 * @param {Number} planId 计划ID
 */
export function completePlan(planId) {
  return request({
    url: `/qqsk/fund/plan/${planId}/complete`,
    method: 'put'
  })
}

/**
 * 取消计划
 * @param {Number} planId 计划ID
 */
export function cancelPlan(planId) {
  return request({
    url: `/qqsk/fund/plan/${planId}/cancel`,
    method: 'put'
  })
}

// 资金计划调整相关API
/**
 * 分页查询资金计划调整
 * @param {Object} params 查询参数
 */
export function getFundPlanAdjustmentPage(params) {
  return request({
    url: '/qqsk/fund/plan-adjustment/page',
    method: 'get',
    params
  })
}

/**
 * 获取资金计划调整汇总
 * @param {Object} params 查询参数
 */
export function getFundPlanAdjustmentSummary(params) {
  return request({
    url: '/qqsk/fund/plan-adjustment/summary',
    method: 'get',
    params
  })
}

/**
 * 审批资金计划调整
 * @param {Number} adjustmentId 调整ID
 * @param {Boolean} approved 是否通过
 * @param {String} opinion 审批意见
 */
export function approveFundPlanAdjustment(adjustmentId, approved, opinion) {
  return request({
    url: `/qqsk/fund/plan-adjustment/${adjustmentId}/approve`,
    method: 'put',
    data: { approved, opinion }
  })
}

/**
 * 拒绝资金计划调整
 * @param {Number} adjustmentId 调整ID
 * @param {String} reason 拒绝原因
 */
export function rejectFundPlanAdjustment(adjustmentId, reason) {
  return request({
    url: `/qqsk/fund/plan-adjustment/${adjustmentId}/reject`,
    method: 'put',
    data: { reason }
  })
}

/**
 * 删除资金计划调整
 * @param {Number} adjustmentId 调整ID
 */
export function deleteFundPlanAdjustment(adjustmentId) {
  return request({
    url: `/qqsk/fund/plan-adjustment/${adjustmentId}`,
    method: 'delete'
  })
}

// 资金计划分析相关API
/**
 * 分页查询资金计划分析
 * @param {Object} params 查询参数
 */
export function getFundPlanAnalysisPage(params) {
  return request({
    url: '/qqsk/fund/plan-analysis/page',
    method: 'get',
    params
  })
}

/**
 * 获取资金计划分析汇总
 * @param {Object} params 查询参数
 */
export function getFundPlanAnalysisSummary(params) {
  return request({
    url: '/qqsk/fund/plan-analysis/summary',
    method: 'get',
    params
  })
}

/**
 * 更新资金计划分析
 * @param {Object} data 分析数据
 */
export function updateFundPlanAnalysis(data) {
  return request({
    url: '/qqsk/fund/plan-analysis',
    method: 'put',
    data
  })
}

/**
 * 删除资金计划分析
 * @param {Number} analysisId 分析ID
 */
export function deleteFundPlanAnalysis(analysisId) {
  return request({
    url: `/qqsk/fund/plan-analysis/${analysisId}`,
    method: 'delete'
  })
}

// 资金计划执行相关API
/**
 * 分页查询资金计划执行
 * @param {Object} params 查询参数
 */
export function getFundPlanExecutionPage(params) {
  return request({
    url: '/qqsk/fund/plan-execution/page',
    method: 'get',
    params
  })
}

/**
 * 获取资金计划执行汇总
 * @param {Object} params 查询参数
 */
export function getFundPlanExecutionSummary(params) {
  return request({
    url: '/qqsk/fund/plan-execution/summary',
    method: 'get',
    params
  })
}

/**
 * 开始资金计划执行
 * @param {Number} executionId 执行ID
 */
export function startFundPlanExecution(executionId) {
  return request({
    url: `/qqsk/fund/plan-execution/${executionId}/start`,
    method: 'put'
  })
}

/**
 * 暂停资金计划执行
 * @param {Number} executionId 执行ID
 */
export function pauseFundPlanExecution(executionId) {
  return request({
    url: `/qqsk/fund/plan-execution/${executionId}/pause`,
    method: 'put'
  })
}

/**
 * 完成资金计划执行
 * @param {Number} executionId 执行ID
 */
export function completeFundPlanExecution(executionId) {
  return request({
    url: `/qqsk/fund/plan-execution/${executionId}/complete`,
    method: 'put'
  })
}

/**
 * 取消资金计划执行
 * @param {Number} executionId 执行ID
 */
export function cancelFundPlanExecution(executionId) {
  return request({
    url: `/qqsk/fund/plan-execution/${executionId}/cancel`,
    method: 'put'
  })
}

/**
 * 更新资金计划执行
 * @param {Object} data 执行数据
 */
export function updateFundPlanExecution(data) {
  return request({
    url: '/qqsk/fund/plan-execution',
    method: 'put',
    data
  })
}

/**
 * 删除资金计划执行
 * @param {Number} executionId 执行ID
 */
export function deleteFundPlanExecution(executionId) {
  return request({
    url: `/qqsk/fund/plan-execution/${executionId}`,
    method: 'delete'
  })
}

// 资金计划明细汇总API
/**
 * 获取资金计划明细汇总
 * @param {Object} params 查询参数
 */
export function getFundPlanDetailSummary(params) {
  return request({
    url: '/qqsk/fund/plan-detail/summary',
    method: 'get',
    params
  })
}

// 资金预测汇总API
/**
 * 获取资金预测汇总
 * @param {Object} params 查询参数
 */
export function getFundForecastSummary(params) {
  return request({
    url: '/qqsk/fund/forecast/summary',
    method: 'get',
    params
  })
}

/**
 * 获取资金预测准确率分析
 * @param {Object} params 查询参数
 */
export function getFundForecastAccuracyAnalysis(params) {
  return request({
    url: '/qqsk/fund/forecast/accuracy-analysis',
    method: 'get',
    params
  })
}

/**
 * 获取资金预测趋势分析
 * @param {Object} params 查询参数
 */
export function getFundForecastTrendAnalysis(params) {
  return request({
    url: '/qqsk/fund/forecast/trend-analysis',
    method: 'get',
    params
  })
}

/**
 * 资金计划调整创建
 * @param {Object} data 调整数据
 */
export function createFundPlanAdjustment(data) {
  return request({
    url: '/qqsk/fund/plan-adjustment',
    method: 'post',
    data
  })
}

/**
 * 资金计划调整更新
 * @param {Object} data 调整数据
 */
export function updateFundPlanAdjustment(data) {
  return request({
    url: '/qqsk/fund/plan-adjustment',
    method: 'put',
    data
  })
}

/**
 * 资金计划分析创建
 * @param {Object} data 分析数据
 */
export function createFundPlanAnalysis(data) {
  return request({
    url: '/qqsk/fund/plan-analysis',
    method: 'post',
    data
  })
}

/**
 * 验证资金计划分析
 * @param {Object} data 验证数据
 */
export function validateFundPlanAnalysis(data) {
  return request({
    url: '/qqsk/fund/plan-analysis/validate',
    method: 'put',
    data
  })
}

/**
 * 资金计划执行创建
 * @param {Object} data 执行数据
 */
export function createFundPlanExecution(data) {
  return request({
    url: '/qqsk/fund/plan-execution',
    method: 'post',
    data
  })
}

/**
 * 资金计划明细差异分析
 * @param {Object} params 查询参数
 */
export function getFundPlanDetailVarianceAnalysis(params) {
  return request({
    url: '/qqsk/fund/plan-detail/variance-analysis',
    method: 'get',
    params
  })
}

/**
 * 资金计划明细执行分析
 * @param {Object} params 查询参数
 */
export function getFundPlanDetailExecutionAnalysis(params) {
  return request({
    url: '/qqsk/fund/plan-detail/execution-analysis',
    method: 'get',
    params
  })
}
