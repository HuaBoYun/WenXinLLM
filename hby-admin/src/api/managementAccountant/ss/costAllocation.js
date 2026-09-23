import request from '@/utils/request'

// 成本分摊API基础路径
const API_BASE_PATH = '/accountant/ss/cost-allocation'

/**
 * 分页查询成本分摊列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostAllocationPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'get',
    params
  })
}

/**
 * 根据ID获取成本分摊详情
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function getCostAllocationById(id) {
  return request({
    url: `${API_BASE_PATH}/${id}`,
    method: 'get'
  })
}

/**
 * 创建成本分摊
 * @param {Object} data 成本分摊数据
 * @returns {Promise}
 */
export function createCostAllocation(data) {
  return request({
    url: API_BASE_PATH,
    method: 'post',
    data
  })
}

/**
 * 更新成本分摊
 * @param {Object} data 成本分摊数据
 * @returns {Promise}
 */
export function updateCostAllocation(data) {
  return request({
    url: API_BASE_PATH,
    method: 'put',
    data
  })
}

/**
 * 删除成本分摊
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function deleteCostAllocation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除成本分摊
 * @param {Array} ids 分摊ID列表
 * @returns {Promise}
 */
export function batchDeleteCostAllocation(ids) {
  return request({
    url: `${API_BASE_PATH}/batch`,
    method: 'delete',
    data: ids
  })
}

/**
 * 根据状态查询成本分摊列表
 * @param {String} status 分摊状态
 * @returns {Promise}
 */
export function getCostAllocationByStatus(status) {
  return request({
    url: `${API_BASE_PATH}/status/${status}`,
    method: 'get'
  })
}

/**
 * 根据类型查询成本分摊列表
 * @param {String} type 分摊类型
 * @returns {Promise}
 */
export function getCostAllocationByType(type) {
  return request({
    url: `${API_BASE_PATH}/type/${type}`,
    method: 'get'
  })
}

/**
 * 根据成本中心查询成本分摊列表
 * @param {Number} costCenterId 成本中心ID
 * @returns {Promise}
 */
export function getCostAllocationByCostCenter(costCenterId) {
  return request({
    url: `${API_BASE_PATH}/cost-center/${costCenterId}`,
    method: 'get'
  })
}

/**
 * 根据分摊方法查询成本分摊列表
 * @param {String} method 分摊方法
 * @returns {Promise}
 */
export function getCostAllocationByMethod(method) {
  return request({
    url: `${API_BASE_PATH}/method/${method}`,
    method: 'get'
  })
}

/**
 * 根据分摊周期查询成本分摊列表
 * @param {String} period 分摊周期
 * @returns {Promise}
 */
export function getCostAllocationByPeriod(period) {
  return request({
    url: `${API_BASE_PATH}/period/${period}`,
    method: 'get'
  })
}

/**
 * 根据时间范围查询成本分摊列表
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function getCostAllocationByTimeRange(startTime, endTime) {
  return request({
    url: `${API_BASE_PATH}/time-range`,
    method: 'get',
    params: { startTime, endTime }
  })
}

/**
 * 根据金额范围查询成本分摊列表
 * @param {Number} minAmount 最小金额
 * @param {Number} maxAmount 最大金额
 * @returns {Promise}
 */
export function getCostAllocationByAmountRange(minAmount, maxAmount) {
  return request({
    url: `${API_BASE_PATH}/amount-range`,
    method: 'get',
    params: { minAmount, maxAmount }
  })
}

/**
 * 根据优先级查询成本分摊列表
 * @param {Number} priority 优先级
 * @returns {Promise}
 */
export function getCostAllocationByPriority(priority) {
  return request({
    url: `${API_BASE_PATH}/priority/${priority}`,
    method: 'get'
  })
}

/**
 * 查询待分摊的成本分摊列表
 * @returns {Promise}
 */
export function getPendingAllocation() {
  return request({
    url: `${API_BASE_PATH}/pending`,
    method: 'get'
  })
}

/**
 * 查询进行中的成本分摊列表
 * @returns {Promise}
 */
export function getActiveAllocation() {
  return request({
    url: `${API_BASE_PATH}/active`,
    method: 'get'
  })
}

/**
 * 查询已完成的成本分摊列表
 * @returns {Promise}
 */
export function getCompletedAllocation() {
  return request({
    url: `${API_BASE_PATH}/completed`,
    method: 'get'
  })
}

/**
 * 查询失败的成本分摊列表
 * @returns {Promise}
 */
export function getFailedAllocation() {
  return request({
    url: `${API_BASE_PATH}/failed`,
    method: 'get'
  })
}

/**
 * 开始分摊计算
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function startAllocationCalculation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/start`,
    method: 'post'
  })
}

/**
 * 停止分摊计算
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function stopAllocationCalculation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/stop`,
    method: 'post'
  })
}

/**
 * 暂停分摊计算
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function pauseAllocationCalculation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/pause`,
    method: 'post'
  })
}

/**
 * 恢复分摊计算
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function resumeAllocationCalculation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/resume`,
    method: 'post'
  })
}

/**
 * 重新计算分摊
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function recalculateAllocation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/recalculate`,
    method: 'post'
  })
}

/**
 * 批量开始分摊计算
 * @param {Array} ids 分摊ID列表
 * @returns {Promise}
 */
export function batchStartCalculation(ids) {
  return request({
    url: `${API_BASE_PATH}/batch/start`,
    method: 'post',
    data: ids
  })
}

/**
 * 批量停止分摊计算
 * @param {Array} ids 分摊ID列表
 * @returns {Promise}
 */
export function batchStopCalculation(ids) {
  return request({
    url: `${API_BASE_PATH}/batch/stop`,
    method: 'post',
    data: ids
  })
}

/**
 * 提交审批
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function submitForApproval(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/submit-approval`,
    method: 'post'
  })
}

/**
 * 审批通过
 * @param {Number} id 分摊ID
 * @param {String} comments 审批意见
 * @returns {Promise}
 */
export function approveAllocation(id, comments) {
  return request({
    url: `${API_BASE_PATH}/${id}/approve`,
    method: 'post',
    params: { comments }
  })
}

/**
 * 审批拒绝
 * @param {Number} id 分摊ID
 * @param {String} comments 拒绝原因
 * @returns {Promise}
 */
export function rejectAllocation(id, comments) {
  return request({
    url: `${API_BASE_PATH}/${id}/reject`,
    method: 'post',
    params: { comments }
  })
}

/**
 * 批量审批通过
 * @param {Array} ids 分摊ID列表
 * @param {String} comments 审批意见
 * @returns {Promise}
 */
export function batchApprove(ids, comments) {
  return request({
    url: `${API_BASE_PATH}/batch/approve`,
    method: 'post',
    data: ids,
    params: { comments }
  })
}

/**
 * 批量审批拒绝
 * @param {Array} ids 分摊ID列表
 * @param {String} comments 拒绝原因
 * @returns {Promise}
 */
export function batchReject(ids, comments) {
  return request({
    url: `${API_BASE_PATH}/batch/reject`,
    method: 'post',
    data: ids,
    params: { comments }
  })
}

/**
 * 复制成本分摊
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function copyCostAllocation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/copy`,
    method: 'post'
  })
}

/**
 * 导入成本分摊数据
 * @param {Array} data 成本分摊数据列表
 * @returns {Promise}
 */
export function importCostAllocationData(data) {
  return request({
    url: `${API_BASE_PATH}/import`,
    method: 'post',
    data
  })
}

/**
 * 导出成本分摊数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function exportCostAllocationData(params) {
  return request({
    url: `${API_BASE_PATH}/export`,
    method: 'get',
    params
  })
}

/**
 * 生成分摊报告
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function generateAllocationReport(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/report`,
    method: 'get'
  })
}

/**
 * 生成批量分摊报告
 * @param {Array} ids 分摊ID列表
 * @returns {Promise}
 */
export function generateBatchAllocationReport(ids) {
  return request({
    url: `${API_BASE_PATH}/batch/report`,
    method: 'post',
    data: ids
  })
}

/**
 * 发送分摊通知
 * @param {Number} id 分摊ID
 * @param {String} notificationType 通知类型
 * @returns {Promise}
 */
export function sendAllocationNotification(id, notificationType) {
  return request({
    url: `${API_BASE_PATH}/${id}/notify`,
    method: 'post',
    params: { notificationType }
  })
}

/**
 * 批量发送分摊通知
 * @param {Array} ids 分摊ID列表
 * @param {String} notificationType 通知类型
 * @returns {Promise}
 */
export function batchSendNotification(ids, notificationType) {
  return request({
    url: `${API_BASE_PATH}/batch/notify`,
    method: 'post',
    data: ids,
    params: { notificationType }
  })
}

/**
 * 查询成本分摊统计信息
 * @returns {Promise}
 */
export function getCostAllocationStatistics() {
  return request({
    url: `${API_BASE_PATH}/statistics`,
    method: 'get'
  })
}

/**
 * 查询成本分摊状态分布
 * @returns {Promise}
 */
export function getAllocationStatusDistribution() {
  return request({
    url: `${API_BASE_PATH}/distribution/status`,
    method: 'get'
  })
}

/**
 * 查询成本分摊类型分布
 * @returns {Promise}
 */
export function getAllocationTypeDistribution() {
  return request({
    url: `${API_BASE_PATH}/distribution/type`,
    method: 'get'
  })
}

/**
 * 查询成本分摊方法分布
 * @returns {Promise}
 */
export function getAllocationMethodDistribution() {
  return request({
    url: `${API_BASE_PATH}/distribution/method`,
    method: 'get'
  })
}

/**
 * 查询成本分摊趋势数据
 * @param {String} startDate 开始日期
 * @param {String} endDate 结束日期
 * @returns {Promise}
 */
export function getCostAllocationTrend(startDate, endDate) {
  return request({
    url: `${API_BASE_PATH}/trend`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 查询成本分摊排行榜
 * @param {String} rankType 排行类型
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getCostAllocationRanking(rankType, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/ranking`,
    method: 'get',
    params: { rankType, limit }
  })
}

/**
 * 查询成本分摊效率分析
 * @returns {Promise}
 */
export function getAllocationEfficiencyAnalysis() {
  return request({
    url: `${API_BASE_PATH}/analysis/efficiency`,
    method: 'get'
  })
}

/**
 * 查询成本分摊质量分析
 * @returns {Promise}
 */
export function getAllocationQualityAnalysis() {
  return request({
    url: `${API_BASE_PATH}/analysis/quality`,
    method: 'get'
  })
}

/**
 * 查询成本分摊成本分析
 * @returns {Promise}
 */
export function getAllocationCostAnalysis() {
  return request({
    url: `${API_BASE_PATH}/analysis/cost`,
    method: 'get'
  })
}

/**
 * 查询成本分摊风险分析
 * @returns {Promise}
 */
export function getAllocationRiskAnalysis() {
  return request({
    url: `${API_BASE_PATH}/analysis/risk`,
    method: 'get'
  })
}

/**
 * 清理过期数据
 * @param {Number} expiredDays 过期天数
 * @returns {Promise}
 */
export function cleanExpiredData(expiredDays = 30) {
  return request({
    url: `${API_BASE_PATH}/clean`,
    method: 'delete',
    params: { expiredDays }
  })
}

/**
 * 检查编码是否存在
 * @param {String} allocationCode 分摊编码
 * @param {Number} allocationId 分摊ID
 * @returns {Promise}
 */
export function checkCodeExists(allocationCode, allocationId) {
  return request({
    url: `${API_BASE_PATH}/check/code`,
    method: 'get',
    params: { allocationCode, allocationId }
  })
}

/**
 * 检查名称是否存在
 * @param {String} allocationName 分摊名称
 * @param {Number} allocationId 分摊ID
 * @returns {Promise}
 */
export function checkNameExists(allocationName, allocationId) {
  return request({
    url: `${API_BASE_PATH}/check/name`,
    method: 'get',
    params: { allocationName, allocationId }
  })
}

/**
 * 执行分摊计算
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function executeAllocationCalculation(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/calculate`,
    method: 'post'
  })
}

/**
 * 分摊结果分析
 * @param {Number} id 分摊ID
 * @returns {Promise}
 */
export function analyzeAllocationResult(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/analyze`,
    method: 'get'
  })
}

// 快捷操作函数
export const costAllocationQuickActions = {
  // 快速创建草稿分摊
  createDraft: (name, type, method) => {
    return createCostAllocation({
      allocationName: name,
      allocationType: type,
      allocationMethod: method,
      allocationStatus: 'DRAFT',
      priority: 5
    })
  },

  // 快速启动分摊
  quickStart: async (id) => {
    await submitForApproval(id)
    return startAllocationCalculation(id)
  },

  // 快速停止分摊
  quickStop: (id) => {
    return stopAllocationCalculation(id)
  },

  // 快速审批通过
  quickApprove: (id, comments = '审批通过') => {
    return approveAllocation(id, comments)
  },

  // 快速拒绝
  quickReject: (id, comments = '审批拒绝') => {
    return rejectAllocation(id, comments)
  }
}

// 工具函数
export const costAllocationUtils = {
  // 格式化分摊状态
  formatAllocationStatus: (status) => {
    const statusMap = {
      'DRAFT': '草稿',
      'ACTIVE': '活跃',
      'CALCULATING': '计算中',
      'COMPLETED': '已完成',
      'SUSPENDED': '已暂停',
      'CANCELLED': '已取消'
    }
    return statusMap[status] || status
  },

  // 格式化分摊类型
  formatAllocationType: (type) => {
    const typeMap = {
      'DIRECT': '直接分摊',
      'INDIRECT': '间接分摊',
      'STEP': '阶梯分摊',
      'RECIPROCAL': '交互分摊',
      'ACTIVITY': '作业分摊',
      'VALUE': '价值分摊'
    }
    return typeMap[type] || type
  },

  // 格式化分摊方法
  formatAllocationMethod: (method) => {
    const methodMap = {
      'EQUAL': '平均分摊',
      'WEIGHTED': '加权分摊',
      'PROPORTIONAL': '比例分摊',
      'ACTIVITY_BASED': '作业成本分摊',
      'STANDARD': '标准分摊',
      'ACTUAL': '实际分摊'
    }
    return methodMap[method] || method
  },

  // 格式化计算状态
  formatCalculationStatus: (status) => {
    const statusMap = {
      'PENDING': '待计算',
      'RUNNING': '计算中',
      'COMPLETED': '已完成',
      'FAILED': '计算失败',
      'CANCELLED': '已取消',
      'PAUSED': '已暂停'
    }
    return statusMap[status] || status
  },

  // 格式化审批状态
  formatApprovalStatus: (status) => {
    const statusMap = {
      'PENDING': '待审批',
      'APPROVED': '已审批',
      'REJECTED': '已拒绝',
      'CANCELLED': '已取消'
    }
    return statusMap[status] || status
  },

  // 格式化分摊周期
  formatAllocationPeriod: (period) => {
    const periodMap = {
      'DAILY': '日',
      'WEEKLY': '周',
      'MONTHLY': '月',
      'QUARTERLY': '季',
      'YEARLY': '年',
      'CUSTOM': '自定义'
    }
    return periodMap[period] || period
  },

  // 计算分摊进度
  calculateProgress: (completedCount, targetCount) => {
    if (!targetCount || targetCount === 0) return 0
    return Math.round((completedCount || 0) / targetCount * 100)
  },

  // 格式化金额
  formatAmount: (amount) => {
    if (!amount) return '0.00'
    return Number(amount).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  },

  // 格式化百分比
  formatPercentage: (percentage) => {
    if (!percentage) return '0.00%'
    return Number(percentage).toFixed(2) + '%'
  }
}
