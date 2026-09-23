import request from '@/utils/request'

// 结算平台模块API接口

// ==================== 待结算数据管理 ====================

// 分页查询待结算数据
export function getPendingDataPage(params) {
  return request({
    url: '/qqsk/settlement/pending-data/page',
    method: 'get',
    params
  })
}

// 根据ID查询待结算数据
export function getPendingDataById(id) {
  return request({
    url: `/qqsk/settlement/pending-data/${id}`,
    method: 'get'
  })
}

// 新增待结算数据
export function addPendingData(data) {
  return request({
    url: '/qqsk/settlement/pending-data',
    method: 'post',
    data
  })
}

// 修改待结算数据
export function updatePendingData(data) {
  return request({
    url: '/qqsk/settlement/pending-data',
    method: 'put',
    data
  })
}

// 删除待结算数据
export function deletePendingData(ids) {
  return request({
    url: `/qqsk/settlement/pending-data/${ids}`,
    method: 'delete'
  })
}

// 批量更新结算状态
export function batchUpdatePendingStatus(data) {
  return request({
    url: '/qqsk/settlement/pending-data/batch-status',
    method: 'put',
    data
  })
}

// 审批待结算数据
export function approvePendingData(data) {
  return request({
    url: '/qqsk/settlement/pending-data/approve',
    method: 'put',
    data
  })
}

// 查询高优先级待结算数据
export function getHighPriorityPending(orgId) {
  return request({
    url: '/qqsk/settlement/pending-data/high-priority',
    method: 'get',
    params: { orgId }
  })
}

// 查询逾期待结算数据
export function getOverduePending(orgId) {
  return request({
    url: '/qqsk/settlement/pending-data/overdue',
    method: 'get',
    params: { orgId }
  })
}

// 查询大额待结算数据
export function getLargeAmountPending(params) {
  return request({
    url: '/qqsk/settlement/pending-data/large-amount',
    method: 'get',
    params
  })
}

// 统计待结算数据概要
export function getPendingDataSummary(orgId) {
  return request({
    url: '/qqsk/settlement/pending-data/summary',
    method: 'get',
    params: { orgId }
  })
}

// 风险评估
export function assessPendingDataRisk(id) {
  return request({
    url: `/qqsk/settlement/pending-data/${id}/risk-assessment`,
    method: 'get'
  })
}

// 导出待结算数据
export function exportPendingData(params) {
  return request({
    url: '/qqsk/settlement/pending-data/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 结算处理记录管理 ====================

// 分页查询处理记录
export function getProcessingRecordPage(params) {
  return request({
    url: '/qqsk/settlement/processing-record/page',
    method: 'get',
    params
  })
}

// 根据ID查询处理记录
export function getProcessingRecordById(id) {
  return request({
    url: `/qqsk/settlement/processing-record/${id}`,
    method: 'get'
  })
}

// 新增处理记录
export function addProcessingRecord(data) {
  return request({
    url: '/qqsk/settlement/processing-record',
    method: 'post',
    data
  })
}

// 修改处理记录
export function updateProcessingRecord(data) {
  return request({
    url: '/qqsk/settlement/processing-record',
    method: 'put',
    data
  })
}

// 删除处理记录
export function deleteProcessingRecord(ids) {
  return request({
    url: `/qqsk/settlement/processing-record/${ids}`,
    method: 'delete'
  })
}

// 查询需要重试的记录
export function getRetryableRecords(orgId) {
  return request({
    url: '/qqsk/settlement/processing-record/retryable',
    method: 'get',
    params: { orgId }
  })
}

// 查询超时记录
export function getTimeoutRecords(params) {
  return request({
    url: '/qqsk/settlement/processing-record/timeout',
    method: 'get',
    params
  })
}

// 统计处理记录概要
export function getProcessingSummary(params) {
  return request({
    url: '/qqsk/settlement/processing-record/summary',
    method: 'get',
    params
  })
}

// 重试处理
export function retryProcessing(id) {
  return request({
    url: `/qqsk/settlement/processing-record/${id}/retry`,
    method: 'post'
  })
}

// ==================== 银企联配置管理 ====================

// 分页查询银企联配置
export function getBankInterfaceConfigPage(params) {
  return request({
    url: '/qqsk/settlement/bank-interface-config/page',
    method: 'get',
    params
  })
}

// 根据ID查询银企联配置
export function getBankInterfaceConfigById(id) {
  return request({
    url: `/qqsk/settlement/bank-interface-config/${id}`,
    method: 'get'
  })
}

// 新增银企联配置
export function addBankInterfaceConfig(data) {
  return request({
    url: '/qqsk/settlement/bank-interface-config',
    method: 'post',
    data
  })
}

// 修改银企联配置
export function updateBankInterfaceConfig(data) {
  return request({
    url: '/qqsk/settlement/bank-interface-config',
    method: 'put',
    data
  })
}

// 删除银企联配置
export function deleteBankInterfaceConfig(ids) {
  return request({
    url: `/qqsk/settlement/bank-interface-config/${ids}`,
    method: 'delete'
  })
}

// 启用配置
export function enableBankInterfaceConfig(ids) {
  return request({
    url: '/qqsk/settlement/bank-interface-config/enable',
    method: 'put',
    data: { configIds: ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

// 禁用配置
export function disableBankInterfaceConfig(ids) {
  return request({
    url: '/qqsk/settlement/bank-interface-config/disable',
    method: 'put',
    data: { configIds: ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

// 测试连接
export function testBankInterfaceConnection(id) {
  return request({
    url: `/qqsk/settlement/bank-interface-config/${id}/test-connection`,
    method: 'post'
  })
}

// 健康检查
export function healthCheckBankInterface(id) {
  return request({
    url: `/qqsk/settlement/bank-interface-config/${id}/health-check`,
    method: 'post'
  })
}

// 查询可用配置
export function getAvailableBankConfigs(params) {
  return request({
    url: '/qqsk/settlement/bank-interface-config/available',
    method: 'get',
    params
  })
}

// 统计配置概要
export function getBankConfigSummary(orgId) {
  return request({
    url: '/qqsk/settlement/bank-interface-config/summary',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 结算监控管理 ====================

// 分页查询监控数据
export function getMonitoringPage(params) {
  return request({
    url: '/qqsk/settlement/monitoring/page',
    method: 'get',
    params
  })
}

// 根据ID查询监控数据
export function getMonitoringById(id) {
  return request({
    url: `/qqsk/settlement/monitoring/${id}`,
    method: 'get'
  })
}

// 新增监控数据
export function addMonitoring(data) {
  return request({
    url: '/qqsk/settlement/monitoring',
    method: 'post',
    data
  })
}

// 修改监控数据
export function updateMonitoring(data) {
  return request({
    url: '/qqsk/settlement/monitoring',
    method: 'put',
    data
  })
}

// 删除监控数据
export function deleteMonitoring(ids) {
  return request({
    url: `/qqsk/settlement/monitoring/${ids}`,
    method: 'delete'
  })
}

// 查询告警记录
export function getAlerts(orgId) {
  return request({
    url: '/qqsk/settlement/monitoring/alerts',
    method: 'get',
    params: { orgId }
  })
}

// 查询严重告警
export function getCriticalAlerts(orgId) {
  return request({
    url: '/qqsk/settlement/monitoring/critical-alerts',
    method: 'get',
    params: { orgId }
  })
}

// 查询系统健康状态
export function getSystemHealthStatus(orgId) {
  return request({
    url: '/qqsk/settlement/monitoring/health-status',
    method: 'get',
    params: { orgId }
  })
}

// 查询监控仪表板数据
export function getDashboardData(orgId) {
  return request({
    url: '/qqsk/settlement/monitoring/dashboard',
    method: 'get',
    params: { orgId }
  })
}

// 查询监控趋势
export function getMonitoringTrend(params) {
  return request({
    url: '/qqsk/settlement/monitoring/trend',
    method: 'get',
    params
  })
}

// 发送告警
export function sendAlert(id) {
  return request({
    url: `/qqsk/settlement/monitoring/${id}/send-alert`,
    method: 'post'
  })
}

// ==================== 结算批次管理 ====================

// 分页查询结算批次
export function getBatchPage(params) {
  return request({
    url: '/qqsk/settlement/batch/page',
    method: 'get',
    params
  })
}

// 根据ID查询结算批次
export function getBatchById(id) {
  return request({
    url: `/qqsk/settlement/batch/${id}`,
    method: 'get'
  })
}

// 新增结算批次
export function addBatch(data) {
  return request({
    url: '/qqsk/settlement/batch',
    method: 'post',
    data
  })
}

// 修改结算批次
export function updateBatch(data) {
  return request({
    url: '/qqsk/settlement/batch',
    method: 'put',
    data
  })
}

// 删除结算批次
export function deleteBatch(ids) {
  return request({
    url: `/qqsk/settlement/batch/${ids}`,
    method: 'delete'
  })
}

// 启动批次
export function startBatch(id) {
  return request({
    url: `/qqsk/settlement/batch/${id}/start`,
    method: 'post'
  })
}

// 停止批次
export function stopBatch(id) {
  return request({
    url: `/qqsk/settlement/batch/${id}/stop`,
    method: 'post'
  })
}

// 重试批次
export function retryBatch(id) {
  return request({
    url: `/qqsk/settlement/batch/${id}/retry`,
    method: 'post'
  })
}

// 查询可执行批次
export function getExecutableBatches(orgId) {
  return request({
    url: '/qqsk/settlement/batch/executable',
    method: 'get',
    params: { orgId }
  })
}

// 统计批次概要
export function getBatchSummary(params) {
  return request({
    url: '/qqsk/settlement/batch/summary',
    method: 'get',
    params
  })
}

// 批次性能分析
export function analyzeBatchPerformance(params) {
  return request({
    url: '/qqsk/settlement/batch/performance-analysis',
    method: 'get',
    params
  })
}

// 优化建议
export function getBatchOptimizationSuggestions(orgId) {
  return request({
    url: '/qqsk/settlement/batch/optimization-suggestions',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 结算规则管理 ====================

// 分页查询结算规则
export function getRulePage(params) {
  return request({
    url: '/qqsk/settlement/rule/page',
    method: 'get',
    params
  })
}

// 根据ID查询结算规则
export function getRuleById(id) {
  return request({
    url: `/qqsk/settlement/rule/${id}`,
    method: 'get'
  })
}

// 新增结算规则
export function addRule(data) {
  return request({
    url: '/qqsk/settlement/rule',
    method: 'post',
    data
  })
}

// 修改结算规则
export function updateRule(data) {
  return request({
    url: '/qqsk/settlement/rule',
    method: 'put',
    data
  })
}

// 删除结算规则
export function deleteRule(ids) {
  return request({
    url: `/qqsk/settlement/rule/${ids}`,
    method: 'delete'
  })
}

// 启用规则
export function enableRule(ids) {
  return request({
    url: '/qqsk/settlement/rule/enable',
    method: 'put',
    data: { ruleIds: ids }
  })
}

// 禁用规则
export function disableRule(ids) {
  return request({
    url: '/qqsk/settlement/rule/disable',
    method: 'put',
    data: { ruleIds: ids }
  })
}

// 查询适用规则
export function getApplicableRules(params) {
  return request({
    url: '/qqsk/settlement/rule/applicable',
    method: 'get',
    params
  })
}

// 测试规则
export function testRule(data) {
  return request({
    url: '/qqsk/settlement/rule/test',
    method: 'post',
    data
  })
}

// 规则执行统计
export function getRuleExecutionStats(orgId) {
  return request({
    url: '/qqsk/settlement/rule/execution-stats',
    method: 'get',
    params: { orgId }
  })
}

// 规则冲突检测
export function detectRuleConflicts(orgId) {
  return request({
    url: '/qqsk/settlement/rule/conflict-detection',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 结算异常管理 ====================

// 分页查询结算异常
export function getExceptionPage(params) {
  return request({
    url: '/qqsk/settlement/exception/page',
    method: 'get',
    params
  })
}

// 根据ID查询结算异常
export function getExceptionById(id) {
  return request({
    url: `/qqsk/settlement/exception/${id}`,
    method: 'get'
  })
}

// 新增结算异常
export function addException(data) {
  return request({
    url: '/qqsk/settlement/exception',
    method: 'post',
    data
  })
}

// 修改结算异常
export function updateException(data) {
  return request({
    url: '/qqsk/settlement/exception',
    method: 'put',
    data
  })
}

// 删除结算异常
export function deleteException(ids) {
  return request({
    url: `/qqsk/settlement/exception/${ids}`,
    method: 'delete'
  })
}

// 分配异常
export function assignException(data) {
  return request({
    url: '/qqsk/settlement/exception/assign',
    method: 'put',
    data
  })
}

// 解决异常
export function resolveException(data) {
  return request({
    url: '/qqsk/settlement/exception/resolve',
    method: 'put',
    data
  })
}

// 升级异常
export function escalateException(data) {
  return request({
    url: '/qqsk/settlement/exception/escalate',
    method: 'put',
    data
  })
}

// 查询未解决异常
export function getUnresolvedExceptions(orgId) {
  return request({
    url: '/qqsk/settlement/exception/unresolved',
    method: 'get',
    params: { orgId }
  })
}

// 查询严重异常
export function getCriticalExceptions(orgId) {
  return request({
    url: '/qqsk/settlement/exception/critical',
    method: 'get',
    params: { orgId }
  })
}

// 统计异常概要
export function getExceptionSummary(params) {
  return request({
    url: '/qqsk/settlement/exception/summary',
    method: 'get',
    params
  })
}

// 异常趋势分析
export function getExceptionTrend(params) {
  return request({
    url: '/qqsk/settlement/exception/trend',
    method: 'get',
    params
  })
}

// 重复异常模式分析
export function getRecurringPatterns(params) {
  return request({
    url: '/qqsk/settlement/exception/recurring-patterns',
    method: 'get',
    params
  })
}
