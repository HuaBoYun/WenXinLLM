import request from '@/utils/request'

// 资金集中模块API接口

// ==================== 资金池管理 ====================

/**
 * 分页查询资金池
 */
export function getFundPoolPage(params) {
  return request({
    url: '/qqsk/fund-concentration/fund-pool/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询资金池
 */
export function getFundPool(poolId) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}`,
    method: 'get'
  })
}

/**
 * 创建资金池
 */
export function createFundPool(data) {
  return request({
    url: '/qqsk/fund-concentration/fund-pool',
    method: 'post',
    data
  })
}

/**
 * 更新资金池
 */
export function updateFundPool(data) {
  return request({
    url: '/qqsk/fund-concentration/fund-pool',
    method: 'put',
    data
  })
}

/**
 * 删除资金池
 */
export function deleteFundPool(poolId) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}`,
    method: 'delete'
  })
}

/**
 * 批量删除资金池
 */
export function batchDeleteFundPool(poolIds) {
  return request({
    url: '/qqsk/fund-concentration/fund-pool/batch',
    method: 'delete',
    params: { poolIds: poolIds.join(',') }
  })
}

/**
 * 启用/禁用资金池
 */
export function toggleFundPoolStatus(poolId, status) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 获取资金池成员列表
 */
export function getFundPoolMembers(poolId, params) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}/members`,
    method: 'get',
    params
  })
}

/**
 * 添加资金池成员
 */
export function addFundPoolMember(poolId, data) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}/members`,
    method: 'post',
    data
  })
}

/**
 * 移除资金池成员
 */
export function removeFundPoolMember(poolId, memberId) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}/members/${memberId}`,
    method: 'delete'
  })
}

/**
 * 获取资金池统计信息
 */
export function getFundPoolStatistics(poolId) {
  return request({
    url: `/qqsk/fund-concentration/fund-pool/${poolId}/statistics`,
    method: 'get'
  })
}

// ==================== 资金归集管理 ====================

/**
 * 分页查询资金归集记录
 */
export function getFundCollectionPage(params) {
  return request({
    url: '/qqsk/fund-concentration/collection/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询资金归集记录
 */
export function getFundCollection(collectionId) {
  return request({
    url: `/qqsk/fund-concentration/collection/${collectionId}`,
    method: 'get'
  })
}

/**
 * 创建资金归集
 */
export function createFundCollection(data) {
  return request({
    url: '/qqsk/fund-concentration/collection',
    method: 'post',
    data
  })
}

/**
 * 更新资金归集
 */
export function updateFundCollection(data) {
  return request({
    url: '/qqsk/fund-concentration/collection',
    method: 'put',
    data
  })
}

/**
 * 删除资金归集记录
 */
export function deleteFundCollection(collectionId) {
  return request({
    url: `/qqsk/fund-concentration/collection/${collectionId}`,
    method: 'delete'
  })
}

/**
 * 执行资金归集
 */
export function executeFundCollection(collectionId) {
  return request({
    url: `/qqsk/fund-concentration/collection/${collectionId}/execute`,
    method: 'post'
  })
}

/**
 * 批量执行资金归集
 */
export function batchExecuteFundCollection(collectionIds) {
  return request({
    url: '/qqsk/fund-concentration/collection/batch-execute',
    method: 'post',
    data: collectionIds
  })
}

/**
 * 取消资金归集
 */
export function cancelFundCollection(collectionId, reason) {
  return request({
    url: `/qqsk/fund-concentration/collection/${collectionId}/cancel`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 获取归集规则列表
 */
export function getCollectionRules(params) {
  return request({
    url: '/qqsk/fund-concentration/collection-rules',
    method: 'get',
    params
  })
}

/**
 * 创建归集规则
 */
export function createCollectionRule(data) {
  return request({
    url: '/qqsk/fund-concentration/collection-rules',
    method: 'post',
    data
  })
}

/**
 * 更新归集规则
 */
export function updateCollectionRule(data) {
  return request({
    url: '/qqsk/fund-concentration/collection-rules',
    method: 'put',
    data
  })
}

/**
 * 删除归集规则
 */
export function deleteCollectionRule(ruleId) {
  return request({
    url: `/qqsk/fund-concentration/collection-rules/${ruleId}`,
    method: 'delete'
  })
}

// ==================== 资金下拨管理 ====================

/**
 * 分页查询资金下拨记录
 */
export function getFundAllocationPage(params) {
  return request({
    url: '/qqsk/fund-concentration/allocation/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询资金下拨记录
 */
export function getFundAllocation(allocationId) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}`,
    method: 'get'
  })
}

/**
 * 创建资金下拨申请
 */
export function createFundAllocation(data) {
  return request({
    url: '/qqsk/fund-concentration/allocation',
    method: 'post',
    data
  })
}

/**
 * 更新资金下拨申请
 */
export function updateFundAllocation(data) {
  return request({
    url: '/qqsk/fund-concentration/allocation',
    method: 'put',
    data
  })
}

/**
 * 删除资金下拨记录
 */
export function deleteFundAllocation(allocationId) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}`,
    method: 'delete'
  })
}

/**
 * 提交资金下拨申请
 */
export function submitFundAllocation(allocationId) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}/submit`,
    method: 'post'
  })
}

/**
 * 审批资金下拨申请
 */
export function approveFundAllocation(allocationId, data) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}/approve`,
    method: 'post',
    data
  })
}

/**
 * 拒绝资金下拨申请
 */
export function rejectFundAllocation(allocationId, reason) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}/reject`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 执行资金下拨
 */
export function executeFundAllocation(allocationId) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}/execute`,
    method: 'post'
  })
}

/**
 * 取消资金下拨
 */
export function cancelFundAllocation(allocationId, reason) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}/cancel`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 重试资金下拨
 */
export function retryFundAllocation(allocationId) {
  return request({
    url: `/qqsk/fund-concentration/allocation/${allocationId}/retry`,
    method: 'post'
  })
}

// ==================== 内部借贷管理 ====================

/**
 * 分页查询内部借贷记录
 */
export function getInternalLoanPage(params) {
  return request({
    url: '/qqsk/fund-concentration/internal-loan/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询内部借贷记录
 */
export function getInternalLoan(loanId) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}`,
    method: 'get'
  })
}

/**
 * 创建内部借贷申请
 */
export function createInternalLoan(data) {
  return request({
    url: '/qqsk/fund-concentration/internal-loan',
    method: 'post',
    data
  })
}

/**
 * 更新内部借贷申请
 */
export function updateInternalLoan(data) {
  return request({
    url: '/qqsk/fund-concentration/internal-loan',
    method: 'put',
    data
  })
}

/**
 * 删除内部借贷记录
 */
export function deleteInternalLoan(loanId) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}`,
    method: 'delete'
  })
}

/**
 * 取消内部借贷申请
 */
export function cancelInternalLoan(loanId) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}/cancel`,
    method: 'post'
  })
}

/**
 * 提交内部借贷申请
 */
export function submitInternalLoan(loanId) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}/submit`,
    method: 'post'
  })
}

/**
 * 审批内部借贷申请
 */
export function approveInternalLoan(loanId, data) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}/approve`,
    method: 'post',
    data
  })
}

/**
 * 放款
 */
export function disburseLoan(loanId) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}/disburse`,
    method: 'post'
  })
}

/**
 * 还款
 */
export function repayLoan(loanId, data) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}/repay`,
    method: 'post',
    data
  })
}

/**
 * 计算利息
 */
export function calculateInterest(loanId, params) {
  return request({
    url: `/qqsk/fund-concentration/internal-loan/${loanId}/interest`,
    method: 'get',
    params
  })
}

// ==================== 资金监控管理 ====================

/**
 * 获取资金监控仪表盘数据
 */
export function getFundMonitoringDashboard(params) {
  return request({
    url: '/qqsk/fund-concentration/monitoring/dashboard',
    method: 'get',
    params
  })
}

/**
 * 获取资金流向分析
 */
export function getFundFlowAnalysis(params) {
  return request({
    url: '/qqsk/fund-concentration/monitoring/flow-analysis',
    method: 'get',
    params
  })
}

/**
 * 获取余额监控数据
 */
export function getBalanceMonitoring(params) {
  return request({
    url: '/qqsk/fund-concentration/monitoring/balance',
    method: 'get',
    params
  })
}

/**
 * 获取预警信息
 */
export function getAlertList(params) {
  return request({
    url: '/qqsk/fund-concentration/monitoring/alerts',
    method: 'get',
    params
  })
}

/**
 * 处理预警
 */
export function handleAlert(alertId, data) {
  return request({
    url: `/qqsk/fund-concentration/monitoring/alerts/${alertId}/handle`,
    method: 'post',
    data
  })
}

/**
 * 设置监控规则
 */
export function setMonitoringRule(data) {
  return request({
    url: '/qqsk/fund-concentration/monitoring/rules',
    method: 'post',
    data
  })
}

/**
 * 获取监控规则列表
 */
export function getMonitoringRules(params) {
  return request({
    url: '/qqsk/fund-concentration/monitoring/rules',
    method: 'get',
    params
  })
}

// ==================== 境外资金管理 ====================

/**
 * 分页查询境外资金池
 */
export function getOverseasFundPoolPage(params) {
  return request({
    url: '/qqsk/fund-concentration/overseas/fund-pool/page',
    method: 'get',
    params
  })
}

/**
 * 创建境外资金池
 */
export function createOverseasFundPool(data) {
  return request({
    url: '/qqsk/fund-concentration/overseas/fund-pool',
    method: 'post',
    data
  })
}

/**
 * 境外资金归集
 */
export function overseasFundCollection(data) {
  return request({
    url: '/qqsk/fund-concentration/overseas/collection',
    method: 'post',
    data
  })
}

/**
 * 虚拟资金池管理
 */
export function getVirtualFundPoolPage(params) {
  return request({
    url: '/qqsk/fund-concentration/virtual-pool/page',
    method: 'get',
    params
  })
}

/**
 * 创建虚拟资金池
 */
export function createVirtualFundPool(data) {
  return request({
    url: '/qqsk/fund-concentration/virtual-pool',
    method: 'post',
    data
  })
}

// ==================== 内部账户管理 ====================

/**
 * 分页查询内部账户
 */
export function getInternalAccountPage(params) {
  return request({
    url: '/qqsk/fund-concentration/internal-account/page',
    method: 'get',
    params
  })
}

/**
 * 创建内部账户
 */
export function createInternalAccount(data) {
  return request({
    url: '/qqsk/fund-concentration/internal-account',
    method: 'post',
    data
  })
}

/**
 * 销户内部账户
 */
export function closeInternalAccount(accountId, reason) {
  return request({
    url: `/qqsk/fund-concentration/internal-account/${accountId}/close`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 内部账户利息管理
 */
export function manageInternalAccountInterest(accountId, data) {
  return request({
    url: `/qqsk/fund-concentration/internal-account/${accountId}/interest`,
    method: 'post',
    data
  })
}

/**
 * 代理结算
 */
export function proxySettlement(data) {
  return request({
    url: '/qqsk/fund-concentration/internal-account/proxy-settlement',
    method: 'post',
    data
  })
}

// ==================== 报表和统计 ====================

/**
 * 获取资金集中统计报表
 */
export function getFundConcentrationReport(params) {
  return request({
    url: '/qqsk/fund-concentration/report/statistics',
    method: 'get',
    params
  })
}

/**
 * 导出资金集中报表
 */
export function exportFundConcentrationReport(params) {
  return request({
    url: '/qqsk/fund-concentration/report/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取资金效率分析
 */
export function getFundEfficiencyAnalysis(params) {
  return request({
    url: '/qqsk/fund-concentration/analysis/efficiency',
    method: 'get',
    params
  })
}

/**
 * 获取成本收益分析
 */
export function getCostBenefitAnalysis(params) {
  return request({
    url: '/qqsk/fund-concentration/analysis/cost-benefit',
    method: 'get',
    params
  })
}

// ==================== 缺失的API函数 ====================

/**
 * 资金池API集合
 */
export const fundPoolApi = {
  /**
   * 获取资金池列表
   * @param {Object} params 查询参数
   */
  getList(params) {
    return request({
      url: '/qqsk/fund-concentration/fund-pool/list',
      method: 'get',
      params
    })
  },

  /**
   * 获取资金池详情
   * @param {Number} poolId 资金池ID
   */
  getDetail(poolId) {
    return request({
      url: `/qqsk/fund-concentration/fund-pool/${poolId}`,
      method: 'get'
    })
  },

  /**
   * 创建资金池
   * @param {Object} data 资金池数据
   */
  create(data) {
    return request({
      url: '/qqsk/fund-concentration/fund-pool',
      method: 'post',
      data
    })
  },

  /**
   * 更新资金池
   * @param {Object} data 资金池数据
   */
  update(data) {
    return request({
      url: '/qqsk/fund-concentration/fund-pool',
      method: 'put',
      data
    })
  },

  /**
   * 删除资金池
   * @param {Number} poolId 资金池ID
   */
  delete(poolId) {
    return request({
      url: `/qqsk/fund-concentration/fund-pool/${poolId}`,
      method: 'delete'
    })
  }
}

// ==================== 归集策略配置 ====================

/**
 * 分页查询归集策略
 */
export function getFundConcentrationStrategyPage(params) {
  return request({
    url: '/qqsk/fund-concentration/strategy/page',
    method: 'get',
    params
  })
}

/**
 * 创建归集策略
 */
export function createFundConcentrationStrategy(data) {
  return request({
    url: '/qqsk/fund-concentration/strategy',
    method: 'post',
    data
  })
}

/**
 * 更新归集策略
 */
export function updateFundConcentrationStrategy(data) {
  return request({
    url: '/qqsk/fund-concentration/strategy',
    method: 'put',
    data
  })
}

/**
 * 启用归集策略
 */
export function enableConcentrationStrategy(strategyId) {
  return request({
    url: `/qqsk/fund-concentration/strategy/${strategyId}/enable`,
    method: 'put'
  })
}

/**
 * 停用归集策略
 */
export function disableConcentrationStrategy(strategyId) {
  return request({
    url: `/qqsk/fund-concentration/strategy/${strategyId}/disable`,
    method: 'put'
  })
}

/**
 * 测试归集策略
 */
export function testConcentrationStrategy(strategyId) {
  return request({
    url: `/qqsk/fund-concentration/strategy/${strategyId}/test`,
    method: 'post'
  })
}

/**
 * 批量启用归集策略
 */
export function batchEnableConcentrationStrategy(data) {
  return request({
    url: '/qqsk/fund-concentration/strategy/batch-enable',
    method: 'put',
    data
  })
}

/**
 * 删除归集策略
 */
export function deleteConcentrationStrategy(strategyId) {
  return request({
    url: `/qqsk/fund-concentration/strategy/${strategyId}`,
    method: 'delete'
  })
}

// ==================== 归集计划管理 ====================

/**
 * 分页查询归集计划
 */
export function getConcentrationPlanPage(params) {
  return request({
    url: '/qqsk/fund-concentration/plan/page',
    method: 'get',
    params
  })
}

/**
 * 创建归集计划
 */
export function createConcentrationPlan(data) {
  return request({
    url: '/qqsk/fund-concentration/plan',
    method: 'post',
    data
  })
}

/**
 * 更新归集计划
 */
export function updateConcentrationPlan(data) {
  return request({
    url: '/qqsk/fund-concentration/plan',
    method: 'put',
    data
  })
}

/**
 * 执行归集计划
 */
export function executeConcentrationPlan(planId) {
  return request({
    url: `/qqsk/fund-concentration/plan/${planId}/execute`,
    method: 'post'
  })
}

/**
 * 暂停归集计划
 */
export function pauseConcentrationPlan(planId) {
  return request({
    url: `/qqsk/fund-concentration/plan/${planId}/pause`,
    method: 'put'
  })
}

/**
 * 取消归集计划
 */
export function cancelConcentrationPlan(planId) {
  return request({
    url: `/qqsk/fund-concentration/plan/${planId}/cancel`,
    method: 'put'
  })
}

/**
 * 批量执行归集计划
 */
export function batchExecuteConcentrationPlan(data) {
  return request({
    url: '/qqsk/fund-concentration/plan/batch-execute',
    method: 'post',
    params: { planIds: data },
    paramsSerializer: params => {
      return params.planIds.map(id => `planIds=${encodeURIComponent(id)}`).join('&')
    }
  })
}

/**
 * 获取归集计划执行明细
 */
export function getConcentrationPlanDetails(planId) {
  return request({
    url: `/qqsk/fund-concentration/plan/${planId}/details`,
    method: 'get'
  })
}

// ==================== 归集执行监控 ====================

/**
 * 分页查询执行监控
 */
export function getExecutionMonitorPage(params) {
  return request({
    url: '/qqsk/fund-concentration/monitor/page',
    method: 'get',
    params
  })
}

/**
 * 暂停执行任务
 */
export function pauseExecution(taskId) {
  return request({
    url: `/qqsk/fund-concentration/monitor/${taskId}/pause`,
    method: 'put'
  })
}

/**
 * 恢复执行任务
 */
export function resumeExecution(taskId) {
  return request({
    url: `/qqsk/fund-concentration/monitor/${taskId}/resume`,
    method: 'put'
  })
}

/**
 * 重试执行任务
 */
export function retryExecution(taskId) {
  return request({
    url: `/qqsk/fund-concentration/monitor/${taskId}/retry`,
    method: 'post'
  })
}

/**
 * 获取执行日志
 */
export function getExecutionLogs(taskId, params) {
  return request({
    url: `/qqsk/fund-concentration/monitor/${taskId}/logs`,
    method: 'get',
    params
  })
}

/**
 * 获取系统告警
 */
export function getSystemAlerts(params) {
  return request({
    url: '/qqsk/fund-concentration/monitor/alerts',
    method: 'get',
    params
  })
}

/**
 * 获取监控统计数据
 */
export function getMonitorStatistics() {
  return request({
    url: '/qqsk/fund-concentration/monitor/statistics',
    method: 'get'
  })
}

/**
 * 批量标记告警为已处理
 */
export function batchHandleAlerts(alertIds) {
  return request({
    url: '/qqsk/fund-concentration/monitor/alerts/batch-handle',
    method: 'post',
    data: alertIds
  })
}

// ==================== 异常处理管理 ====================

/**
 * 分页查询异常处理
 */
export function getExceptionPage(params) {
  return request({
    url: '/qqsk/fund-concentration/exception-handling/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询异常处理
 */
export function getExceptionById(exceptionId) {
  return request({
    url: `/qqsk/fund-concentration/exception-handling/${exceptionId}`,
    method: 'get'
  })
}

/**
 * 创建异常记录
 */
export function createException(data) {
  // 字段名映射：前端字段名 -> 后端实体类字段名
  const mappedData = {
    exceptionId: data.exceptionId,
    exceptionNo: data.exceptionNo,
    exceptionType: data.exceptionType,
    exceptionLevel: data.exceptionLevel,
    sourceType: data.sourceType,
    sourceId: data.sourceId,
    sourceNo: data.sourceNo,
    poolId: data.poolId,
    companyId: data.companyId,
    companyName: data.companyName,
    exceptionTitle: data.exceptionTitle,
    exceptionDesc: data.exceptionDesc,
    exceptionTime: data.exceptionTime,
    exceptionStatus: data.exceptionStatus || 'PENDING',
    handleBy: data.handleBy,
    handleResult: data.handleResult,
    handleMethod: data.handleMethod,
    handleTime: data.handleTime,
    retryCount: data.retryCount,
    maxRetry: data.maxRetry,
    nextRetryTime: data.nextRetryTime,
    remark: data.remark,
    createBy: data.createBy,
    updateBy: data.updateBy
  }
  return request({
    url: '/qqsk/fund-concentration/exception-handling',
    method: 'post',
    data: mappedData
  })
}

/**
 * 处理异常
 */
export function handleException(exceptionId, handleMethod, handleResult) {
  return request({
    url: `/qqsk/fund-concentration/exception-handling/${exceptionId}/handle`,
    method: 'post',
    params: { handleMethod, handleResult }
  })
}

/**
 * 重试异常
 */
export function retryException(exceptionId) {
  return request({
    url: `/qqsk/fund-concentration/exception-handling/${exceptionId}/retry`,
    method: 'post'
  })
}

/**
 * 关闭异常
 */
export function closeException(exceptionId, closeRemark) {
  return request({
    url: `/qqsk/fund-concentration/exception-handling/${exceptionId}/close`,
    method: 'put',
    params: { closeRemark }
  })
}

/**
 * 获取异常统计
 */
export function getExceptionStatistics() {
  return request({
    url: '/qqsk/fund-concentration/exception-handling/statistics',
    method: 'get'
  })
}

// ==================== 报表统计管理 ====================

/**
 * 分页查询报表统计
 */
export function getReportStatisticsPage(params) {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询报表统计
 */
export function getReportStatisticsById(reportId) {
  return request({
    url: `/qqsk/fund-concentration/report-statistics/${reportId}`,
    method: 'get'
  })
}

/**
 * 创建报表统计
 */
export function createReportStatistics(data) {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/create',
    method: 'post',
    data
  })
}

/**
 * 更新报表统计
 */
export function updateReportStatistics(data) {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/update',
    method: 'put',
    data
  })
}

/**
 * 删除报表统计
 */
export function deleteReportStatistics(reportId) {
  return request({
    url: `/qqsk/fund-concentration/report-statistics/${reportId}`,
    method: 'delete'
  })
}

/**
 * 生成报表
 */
export function generateReport(reportType, reportPeriod, startDate, endDate) {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/generate',
    method: 'post',
    params: { reportType, reportPeriod, startDate, endDate }
  })
}

/**
 * 分析报表
 */
export function analyzeReport(reportId) {
  return request({
    url: `/qqsk/fund-concentration/report-statistics/${reportId}/analyze`,
    method: 'get'
  })
}

/**
 * 获取报表类型列表
 */
export function getReportTypes() {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/types',
    method: 'get'
  })
}

// ==================== 监管分析管理 ====================

/**
 * 分页查询监管分析
 */
export function getRegulatoryAnalysisPage(params) {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询监管分析
 */
export function getRegulatoryAnalysisById(analysisId) {
  return request({
    url: `/qqsk/fund-concentration/regulatory-analysis/${analysisId}`,
    method: 'get'
  })
}

/**
 * 创建监管分析
 */
export function createRegulatoryAnalysis(data) {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    transformRequest: [function (data) {
      return JSON.stringify(data)
    }]
  })
}

/**
 * 更新监管分析
 */
export function updateRegulatoryAnalysis(data) {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis',
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    transformRequest: [function (data) {
      return JSON.stringify(data)
    }]
  })
}

/**
 * 删除监管分析
 */
export function deleteRegulatoryAnalysis(analysisId) {
  return request({
    url: `/qqsk/fund-concentration/regulatory-analysis/${analysisId}`,
    method: 'delete'
  })
}

/**
 * 执行监管分析
 */
export function executeRegulatoryAnalysis(analysisType, startDate, endDate) {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/execute',
    method: 'post',
    params: { analysisType, startDate, endDate }
  })
}

/**
 * 生成监管报告
 */
export function generateRegulatoryReport(analysisId, reportFormat) {
  return request({
    url: `/qqsk/fund-concentration/regulatory-analysis/${analysisId}/generate-report`,
    method: 'post',
    params: { reportFormat },
    responseType: 'blob'
  })
}

/**
 * 获取合规统计
 */
export function getComplianceStatistics() {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/compliance-statistics',
    method: 'get'
  })
}

/**
 * 获取风险等级分布
 */
export function getRiskDistribution() {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/risk-distribution',
    method: 'get'
  })
}

/**
 * 获取分析类型列表
 */
export function getAnalysisTypes() {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/types',
    method: 'get'
  })
}

/**
 * 获取概览统计数据
 */
export function getRegulatoryOverviewStatistics() {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/overview-statistics',
    method: 'get'
  })
}

// ==================== 统计数据API ====================

/**
 * 获取资金归集统计数据
 */
export function getConcentrationStatistics() {
  return request({
    url: '/qqsk/fund-concentration/statistics',
    method: 'get'
  })
}

/**
 * 获取资金下拨统计数据
 */
export function getAllocationStatistics() {
  return request({
    url: '/qqsk/fund-allocation/statistics',
    method: 'get'
  })
}

/**
 * 获取内部借贷统计数据
 */
export function getLendingStatistics() {
  return request({
    url: '/qqsk/internal-loan/statistics',
    method: 'get'
  })
}

/**
 * 获取告警统计数据
 */
export function getAlertStatistics() {
  return request({
    url: '/qqsk/fund-alert/statistics',
    method: 'get'
  })
}

// ==================== 批量删除 & 导出 ====================

/**
 * 批量删除归集策略
 */
export function batchDeleteConcentrationStrategy(strategyIds) {
  return request({
    url: '/qqsk/fund-concentration/strategy/batch',
    method: 'delete',
    params: { strategyIds },
    paramsSerializer: params => {
      return params.strategyIds.map(id => `strategyIds=${encodeURIComponent(id)}`).join('&')
    }
  })
}

/**
 * 导出归集策略配置
 */
export function exportConcentrationStrategy(params) {
  return request({
    url: '/qqsk/fund-concentration/strategy/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除归集计划
 */
export function batchDeleteConcentrationPlan(planIds) {
  return request({
    url: '/qqsk/fund-concentration/plan/batch',
    method: 'delete',
    params: { planIds },
    paramsSerializer: params => {
      return params.planIds.map(id => `planIds=${encodeURIComponent(id)}`).join('&')
    }
  })
}

/**
 * 删除归集计划
 */
export function deleteConcentrationPlan(planId) {
  return request({
    url: `/qqsk/fund-concentration/plan/${planId}`,
    method: 'delete'
  })
}

/**
 * 导出归集计划
 */
export function exportConcentrationPlan(params) {
  return request({
    url: '/qqsk/fund-concentration/plan/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出执行监控报告
 */
export function exportExecutionMonitor(params) {
  return request({
    url: '/qqsk/fund-concentration/monitor/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除报表统计
 */
export function batchDeleteReportStatistics(reportIds) {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/batch',
    method: 'delete',
    params: { reportIds },
    paramsSerializer: params => {
      return params.reportIds.map(id => `reportIds=${encodeURIComponent(id)}`).join('&')
    }
  })
}

/**
 * 导出报表统计
 */
export function exportReportStatistics(params) {
  return request({
    url: '/qqsk/fund-concentration/report-statistics/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 更新异常记录
 */
export function updateException(data) {
  // 字段名映射：前端字段名 -> 后端实体类字段名
  const mappedData = {
    exceptionId: data.exceptionId,
    exceptionNo: data.exceptionNo,
    exceptionType: data.exceptionType,
    exceptionLevel: data.exceptionLevel,
    sourceType: data.sourceType,
    sourceId: data.sourceId,
    sourceNo: data.sourceNo,
    poolId: data.poolId,
    companyId: data.companyId,
    companyName: data.companyName,
    exceptionTitle: data.exceptionTitle,
    exceptionDesc: data.exceptionDesc,
    exceptionTime: data.exceptionTime,
    exceptionStatus: data.exceptionStatus,
    handleBy: data.handleBy,
    handleResult: data.handleResult,
    handleMethod: data.handleMethod,
    handleTime: data.handleTime,
    retryCount: data.retryCount,
    maxRetry: data.maxRetry,
    nextRetryTime: data.nextRetryTime,
    remark: data.remark,
    updateBy: data.updateBy
  }
  return request({
    url: '/qqsk/fund-concentration/exception-handling/update',
    method: 'put',
    data: mappedData
  })
}

/**
 * 删除异常记录
 */
export function deleteException(exceptionId) {
  return request({
    url: `/qqsk/fund-concentration/exception-handling/${exceptionId}`,
    method: 'delete'
  })
}

/**
 * 批量删除异常记录
 */
export function batchDeleteException(exceptionIds) {
  // 使用 POST 请求通过 body 传递数据，避免 URL 编码问题
  return request({
    url: '/qqsk/fund-concentration/exception-handling/batch',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: exceptionIds
  })
}

/**
 * 导出异常处理记录
 */
export function exportException(params) {
  return request({
    url: '/qqsk/fund-concentration/exception-handling/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除监管分析
 */
export function batchDeleteRegulatoryAnalysis(analysisIds) {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/batch',
    method: 'delete',
    params: { analysisIds },
    paramsSerializer: params => {
      return params.analysisIds.map(id => `analysisIds=${encodeURIComponent(id)}`).join('&')
    }
  })
}

/**
 * 导出监管分析
 */
export function exportRegulatoryAnalysis(params) {
  return request({
    url: '/qqsk/fund-concentration/regulatory-analysis/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
