import request from '@/utils/request'
import qs from 'qs'

// 衍生品模块API接口

// ==================== 远期交易管理 ====================

/**
 * 分页查询远期交易
 */
export function getForwardTransactionPage(params) {
  return request({
    url: '/qqsk/derivatives/forward/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询远期交易
 */
export function getForwardTransaction(transactionId) {
  return request({
    url: `/qqsk/derivatives/forward/${transactionId}`,
    method: 'get'
  })
}

/**
 * 创建远期合约
 */
export function createForwardContract(data) {
  return request({
    url: '/qqsk/derivatives/forward',
    method: 'post',
    data
  })
}

/**
 * 更新远期合约
 */
export function updateForwardContract(data) {
  return request({
    url: '/qqsk/derivatives/forward',
    method: 'put',
    data
  })
}

/**
 * 删除远期交易
 */
export function deleteForwardTransaction(transactionIds) {
  return request({
    url: '/qqsk/derivatives/forward/delete',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: JSON.stringify(Array.isArray(transactionIds) ? transactionIds : [transactionIds])
  })
}

export function batchValuateForwardContract(transactionIds) {
  return request({
    url: '/qqsk/derivatives/forward/valuation/batch',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: JSON.stringify(transactionIds && transactionIds.length ? transactionIds : [])
  })
}

export function exportForwardTransaction(params) {
  return request({
    url: '/qqsk/derivatives/forward/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 执行远期合约
 */
export function executeForwardContract(transactionId, data) {
  return request({
    url: `/qqsk/derivatives/forward/${transactionId}/execute`,
    method: 'post',
    data
  })
}

/**
 * 取消远期合约
 */
export function cancelForwardContract(transactionId) {
  return request({
    url: `/qqsk/derivatives/forward/${transactionId}/cancel`,
    method: 'post'
  })
}

/**
 * 远期合约交割
 */
export function settleForwardContract(transactionId, data) {
  return request({
    url: `/qqsk/derivatives/forward/${transactionId}/settle`,
    method: 'post',
    data
  })
}

/**
 * 远期合约估值
 */
export function valuateForwardContract(transactionId, params) {
  return request({
    url: `/qqsk/derivatives/forward/${transactionId}/valuation`,
    method: 'get',
    params
  })
}

/**
 * 获取远期汇率报价
 */
export function getForwardRates(params) {
  return request({
    url: '/qqsk/derivatives/forward/rates',
    method: 'get',
    params
  })
}

// ==================== 期权交易管理 ====================

/**
 * 分页查询期权交易
 */
export function getOptionTransactionPage(params) {
  return request({
    url: '/qqsk/derivatives/option/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询期权交易
 */
export function getOptionTransaction(transactionId) {
  return request({
    url: `/qqsk/derivatives/option/${transactionId}`,
    method: 'get'
  })
}

/**
 * 创建期权合约
 */
export function createOptionContract(data) {
  return request({
    url: '/qqsk/derivatives/option',
    method: 'post',
    data
  })
}

/**
 * 更新期权合约
 */
export function updateOptionContract(data) {
  return request({
    url: '/qqsk/derivatives/option',
    method: 'put',
    data
  })
}

/**
 * 删除期权交易
 */
export function deleteOptionTransaction(transactionIds) {
  const ids = (Array.isArray(transactionIds) ? transactionIds : [transactionIds]).filter(id => id != null && id !== '')
  return request({
    url: '/qqsk/derivatives/option/delete',
    method: 'post',
    params: { transactionIds: ids },
    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
  })
}




/**
 * 期权定价模型计算
 */
export function calculateOptionPrice(data) {
  return request({
    url: '/qqsk/derivatives/option/pricing',
    method: 'post',
    data
  })
}

/**
 * 期权行权管理
 */
export function exerciseOption(transactionId, data) {
  return request({
    url: `/qqsk/derivatives/option/${transactionId}/exercise`,
    method: 'post',
    data
  })
}

/**
 * 期权到期处理
 */
export function expireOption(transactionId) {
  return request({
    url: `/qqsk/derivatives/option/${transactionId}/expire`,
    method: 'post'
  })
}

/**
 * 期权取消
 */
export function cancelOption(transactionId, reason) {
  return request({
    url: `/qqsk/derivatives/option/${transactionId}/cancel`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 期权希腊字母计算
 */
export function calculateGreeks(transactionId, params) {
  return request({
    url: `/qqsk/derivatives/option/${transactionId}/greeks`,
    method: 'get',
    params
  })
}

// ==================== 期货交易管理 ====================

/**
 * 分页查询期货交易
 */
export function getFuturesTransactionPage(params) {
  return request({
    url: '/qqsk/derivatives/futures/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询期货交易
 */
export function getFuturesTransaction(transactionId) {
  return request({
    url: `/qqsk/derivatives/futures/${transactionId}`,
    method: 'get'
  })
}

/**
 * 创建期货合约
 */
export function createFuturesContract(data) {
  return request({
    url: '/qqsk/derivatives/futures',
    method: 'post',
    data
  })
}

/**
 * 更新期货合约
 */
export function updateFuturesContract(data) {
  return request({
    url: '/qqsk/derivatives/futures',
    method: 'put',
    data
  })
}

/**
 * 删除期货交易
 */
export function deleteFuturesTransaction(transactionIds) {
  const ids = Array.isArray(transactionIds) ? transactionIds : [transactionIds]
  // 将数组转换为逗号分隔的字符串
  return request({
    url: '/qqsk/derivatives/futures/delete',
    method: 'post',
    data: { ids: ids.join(',') }
  })
}

/**
 * 取消期货合约
 */
export function cancelFuturesContract(transactionId) {
  return request({
    url: `/qqsk/derivatives/futures/${transactionId}/cancel`,
    method: 'post'
  })
}

/**
 * 期货保证金管理
 */
export function manageFuturesMargin(transactionId, data) {
  return request({
    url: `/qqsk/derivatives/futures/${transactionId}/margin`,
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 获取期货保证金信息
 */
export function getFuturesMarginInfo(transactionId) {
  return request({
    url: `/qqsk/derivatives/futures/${transactionId}/margin`,
    method: 'get'
  })
}

/**
 * 期货交割管理
 */
export function settleFuturesContract(transactionId, data) {
  return request({
    url: `/qqsk/derivatives/futures/${transactionId}/settle`,
    method: 'post',
    data
  })
}

/**
 * 期货每日结算
 */
export function dailySettlement(transactionId, data) {
  const url = transactionId ? `/qqsk/derivatives/futures/${transactionId}/dailySettlement` : '/qqsk/derivatives/futures/dailySettlement'
  return request({
    url: url,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// ==================== 掉期交易管理 ====================

/**
 * 分页查询掉期交易
 */
export function getSwapTransactionPage(params) {
  return request({
    url: '/qqsk/derivatives/swap/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询掉期交易
 */
export function getSwapTransaction(transactionId) {
  return request({
    url: `/qqsk/derivatives/swap/${transactionId}`,
    method: 'get'
  })
}

/**
 * 创建利率掉期
 */
export function createInterestRateSwap(data) {
  return request({
    url: '/qqsk/derivatives/swap/interest-rate',
    method: 'post',
    data
  })
}

/**
 * 创建货币掉期
 */
export function createCurrencySwap(data) {
  return request({
    url: '/qqsk/derivatives/swap/currency',
    method: 'post',
    data
  })
}

/**
 * 更新掉期交易
 */
export function updateSwapTransaction(data) {
  return request({
    url: '/qqsk/derivatives/swap',
    method: 'put',
    data
  })
}

/**
 * 删除掉期交易
 */
export function deleteSwapTransaction(transactionIds) {
  return request({
    url: '/qqsk/derivatives/swap/delete',
    method: 'post',
    data: Array.isArray(transactionIds) ? transactionIds : [transactionIds]
  })
}

/**
 * 取消掉期交易
 */
export function cancelSwapTransaction(transactionId) {
  return request({
    url: `/qqsk/derivatives/swap/${transactionId}/cancel`,
    method: 'post'
  })
}

/**
 * 掉期估值计算
 */
export function valuateSwap(transactionId, params) {
  return request({
    url: `/qqsk/derivatives/swap/${transactionId}/valuation`,
    method: 'post',
    params
  })
}

/**
 * 掉期现金流计算
 */
export function calculateSwapCashFlow(transactionId) {
  return request({
    url: `/qqsk/derivatives/swap/cashflow/${transactionId}`,
    method: 'get'
  })
}

/**
 * 掉期支付处理
 */
export function processSwapPayment(data) {
  return request({
    url: `/qqsk/derivatives/swap/payment`,
    method: 'post',
    params: data
  })
}

/**
 * 终止掉期交易
 */
export function terminateSwapTransaction(transactionId) {
  return request({
    url: `/qqsk/derivatives/swap/${transactionId}/terminate`,
    method: 'post'
  })
}

// ==================== 衍生品监控管理 ====================

/**
 * 获取衍生品监控仪表盘
 */
export function getDerivativesMonitoringDashboard(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/dashboard',
    method: 'get',
    params
  })
}

/**
 * 获取持仓监控数据
 */
export function getPositionMonitoring(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/position',
    method: 'get',
    params
  })
}

/**
 * 获取风险监控数据
 */
export function getRiskMonitoring(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/risk',
    method: 'get',
    params
  })
}

/**
 * 衍生品报表分析
 */
export function getDerivativesReportAnalysis(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/report-analysis',
    method: 'get',
    params
  })
}

/**
 * 获取市场数据
 */
export function getMarketData(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/market-data',
    method: 'get',
    params
  })
}

/**
 * 设置监控预警（新增）
 */
export function setMonitoringAlert(data) {
  return request({
    url: '/qqsk/derivatives/monitoring/alert',
    method: 'post',
    data
  })
}

/**
 * 修改监控预警
 */
export function updateMonitoringAlert(data) {
  return request({
    url: '/qqsk/derivatives/monitoring/alert',
    method: 'put',
    data
  })
}

/**
 * 删除监控预警
 */
export function deleteMonitoringAlert(alertIds) {
  const ids = Array.isArray(alertIds) ? alertIds : [alertIds]
  return request({
    url: `/qqsk/derivatives/monitoring/alert/${ids.join(',')}`,
    method: 'delete'
  })
}

/**
 * 处理预警
 */
export function processMonitoringAlert(alertId) {
  return request({
    url: `/qqsk/derivatives/monitoring/alert/${alertId}/process`,
    method: 'post'
  })
}

/**
 * 忽略预警
 */
export function ignoreMonitoringAlert(alertId) {
  return request({
    url: `/qqsk/derivatives/monitoring/alert/${alertId}/ignore`,
    method: 'post'
  })
}

/**
 * 获取监控预警列表
 */
export function getMonitoringAlerts(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/alert',
    method: 'get',
    params
  })
}

/**
 * 导出持仓监控数据
 */
export function exportMonitoringPosition(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/position/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出预警管理数据
 */
export function exportMonitoringAlert(params) {
  return request({
    url: '/qqsk/derivatives/monitoring/alert/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 衍生品风险管理 ====================

/**
 * 衍生品限额管理
 */
export function getDerivativesLimits(params) {
  return request({
    url: '/qqsk/derivatives/risk/limits',
    method: 'get',
    params
  })
}

/**
 * 设置衍生品限额
 */
export function setDerivativesLimit(data) {
  return request({
    url: '/qqsk/derivatives/risk/limits',
    method: 'post',
    data
  })
}

/**
 * 更新衍生品限额
 */
export function updateDerivativesLimit(data) {
  return request({
    url: '/qqsk/derivatives/risk/limits',
    method: 'put',
    data
  })
}

/**
 * 风险评估计算
 */
export function assessDerivativesRisk(data) {
  return request({
    url: '/qqsk/derivatives/risk/assessment',
    method: 'post',
    data
  })
}

/**
 * VaR计算
 */
export function calculateVaR(data) {
  return request({
    url: '/qqsk/derivatives/risk/var',
    method: 'post',
    data
  })
}

/**
 * 压力测试
 */
export function stressTest(data) {
  return request({
    url: '/qqsk/derivatives/risk/stress-test',
    method: 'post',
    data
  })
}

/**
 * 合规检查
 */
export function complianceCheck(params) {
  return request({
    url: '/qqsk/derivatives/risk/compliance-check',
    method: 'get',
    params
  })
}

/**
 * 获取合规检查结果
 */
export function getComplianceCheckResults(params) {
  return request({
    url: '/qqsk/derivatives/risk/compliance-results',
    method: 'get',
    params
  })
}

// ==================== 报表和导出 ====================

/**
 * 导出衍生品持仓报表
 */
export function exportPositionReport(params) {
  return request({
    url: '/qqsk/derivatives/export/position',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出衍生品损益报表
 */
export function exportPnLReport(params) {
  return request({
    url: '/qqsk/derivatives/export/pnl',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出风险报表
 */
export function exportRiskReport(params) {
  return request({
    url: '/qqsk/derivatives/export/risk',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 生成衍生品报告
 */
export function generateDerivativesReport(params) {
  return request({
    url: '/qqsk/derivatives/report/generate',
    method: 'post',
    data: params
  })
}

/**
 * 获取衍生品报告列表
 */
export function getDerivativesReports(params) {
  return request({
    url: '/qqsk/derivatives/report/list',
    method: 'get',
    params
  })
}

/**
 * 下载衍生品报告
 */
export function downloadDerivativesReport(reportId) {
  return request({
    url: `/qqsk/derivatives/report/${reportId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 通用下载函数
 */
export function download(url, params, filename) {
  return request({
    url: `/qqsk/${url}`,
    method: 'get',
    params,
    responseType: 'blob'
  }).then(response => {
    if (!response) return
    const blob = new Blob([response])
    const link = document.createElement('a')
    link.href = window.URL.createObjectURL(blob)
    link.download = filename
    link.click()
    window.URL.revokeObjectURL(link.href)
  })
}


// ==================== 估值管理 ====================

export function getValuationPositions() {
  return request({ url: '/qqsk/derivatives/valuation/positions', method: 'get' })
}

export function getValuationSummary() {
  return request({ url: '/qqsk/derivatives/valuation/summary', method: 'get' })
}

export function getValuationHistory() {
  return request({ url: '/qqsk/derivatives/valuation/history', method: 'get' })
}

export function batchValuation() {
  return request({ url: '/qqsk/derivatives/valuation/batch', method: 'post' })
}

export function singleValuation(data) {
  return request({ url: '/qqsk/derivatives/valuation/single', method: 'post', params: data })
}

export function revaluate(id) {
  return request({ url: `/qqsk/derivatives/valuation/revaluate/${id}`, method: 'put' })
}


// ==================== 报表分析 ====================

export function getReportList() {
  return request({ url: '/qqsk/derivatives/report/list', method: 'get' })
}

export function addReport(data) {
  return request({ url: '/qqsk/derivatives/report/add', method: 'post', params: data })
}

export function getReportDetail(id) {
  return request({ url: `/qqsk/derivatives/report/detail/${id}`, method: 'get' })
}

export function editReport(id, data) {
  return request({ url: `/qqsk/derivatives/report/edit/${id}`, method: 'put', params: data })
}

export function deleteReport(id) {
  return request({ url: `/qqsk/derivatives/report/delete/${id}`, method: 'delete' })
}

export function batchDeleteReport(ids) {
  return request({ url: '/qqsk/derivatives/report/batchDelete', method: 'post', params: { ids } })
}

export function getTemplateList() {
  return request({ url: '/qqsk/derivatives/report/templateList', method: 'get' })
}

export function addTemplate(data) {
  return request({ url: '/qqsk/derivatives/report/addTemplate', method: 'post', params: data })
}

export function editTemplate(id, data) {
  return request({ url: `/qqsk/derivatives/report/editTemplate/${id}`, method: 'put', params: data })
}

export function deleteTemplate(id) {
  return request({ url: `/qqsk/derivatives/report/deleteTemplate/${id}`, method: 'delete' })
}

export function getReportChartData() {
  return request({ url: '/qqsk/derivatives/report/chartData', method: 'get' })
}
