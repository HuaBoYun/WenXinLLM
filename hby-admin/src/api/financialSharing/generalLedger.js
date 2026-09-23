/*
 * @Description: 财务共享 - 总账模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 总账余额管理 API ====================

/**
 * 分页查询总账余额
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getGeneralLedgerBalancePage(data) {
  // 参数名称映射：前端 -> 后端
  const mappedData = {
    pageNo: data.pageNumber || 1,
    pageSize: data.pageSize || 10,
    subjectCode: data.subjectCode,
    subjectName: data.subjectName,
    accountingPeriod: data.period,  // period -> accountingPeriod
    balanceDirection: data.balanceDirection,
    subjectType: data.subjectCategory  // subjectCategory -> subjectType
  }
  return request({
    url: '/cwgxAi/general-ledger/balance/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(mappedData))
  })
}

/**
 * 获取科目余额详情
 * @param {String} subjectCode 科目编码
 * @param {String} period 期间
 * @returns {Promise}
 */
export function getSubjectBalanceDetail(subjectCode, period) {
  return request({
    url: '/cwgxAi/general-ledger/balance/detail',
    method: 'get',
    params: { subjectCode, period }
  })
}

/**
 * 获取科目余额趋势
 * @param {String} subjectCode 科目编码
 * @param {String} startPeriod 开始期间
 * @param {String} endPeriod 结束期间
 * @returns {Promise}
 */
export function getSubjectBalanceTrend(subjectCode, startPeriod, endPeriod) {
  return request({
    url: '/cwgxAi/general-ledger/balance/trend',
    method: 'get',
    params: { subjectCode, startPeriod, endPeriod }
  })
}

/**
 * 重新计算科目余额
 * @param {Object} data 计算参数
 * @returns {Promise}
 */
export function recalculateSubjectBalance(data) {
  return request({
    url: '/cwgxAi/general-ledger/balance/recalculate',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 明细账管理 API ====================

/**
 * 分页查询明细账
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getDetailLedgerPage(data) {
  // 参数名称映射：前端 -> 后端
  const mappedData = {
    pageNo: data.pageNumber || 1,
    pageSize: data.pageSize || 20,
    subjectCode: data.subjectCode,
    startDate: data.startDate,
    endDate: data.endDate,
    voucherNo: data.voucherNo,
    summary: data.summary,
    minAmount: data.minAmount,
    maxAmount: data.maxAmount,
    auxiliaryType: data.auxiliaryType
  }
  return request({
    url: '/cwgxAi/general-ledger/detail/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(mappedData))
  })
}

/**
 * 获取科目明细账
 * @param {String} subjectCode 科目编码
 * @param {String} period 期间
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSubjectDetailLedger(subjectCode, period, params) {
  return request({
    url: '/cwgxAi/general-ledger/detail/subject',
    method: 'get',
    params: { subjectCode, period, ...params }
  })
}

/**
 * 导出明细账
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportDetailLedger(data) {
  return request({
    url: '/cwgxAi/general-ledger/detail/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 试算平衡表 API ====================

/**
 * 获取试算平衡表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTrialBalance(params) {
  return request({
    url: '/cwgxAi/general-ledger/trial-balance',
    method: 'get',
    params
  })
}

/**
 * 获取试算平衡表汇总
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTrialBalanceSummary(params) {
  return request({
    url: '/cwgxAi/general-ledger/trial-balance/summary',
    method: 'get',
    params
  })
}

/**
 * 导出试算平衡表
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportTrialBalance(data) {
  return request({
    url: '/cwgxAi/general-ledger/trial-balance/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 期末处理 API ====================

/**
 * 获取期末处理任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPeriodEndTaskList(params) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/tasks',
    method: 'get',
    params
  })
}

/**
 * 创建期末处理任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function createPeriodEndTask(data) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/tasks',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行期末处理任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function executePeriodEndTask(taskId) {
  return request({
    url: `/cwgxAi/general-ledger/period-end/tasks/${taskId}/execute`,
    method: 'post'
  })
}

/**
 * 获取期末处理任务状态
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getPeriodEndTaskStatus(taskId) {
  return request({
    url: `/cwgxAi/general-ledger/period-end/tasks/${taskId}/status`,
    method: 'get'
  })
}

/**
 * 停止期末处理任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function stopPeriodEndTask(taskId) {
  return request({
    url: `/cwgxAi/general-ledger/period-end/tasks/${taskId}/stop`,
    method: 'post'
  })
}

// ==================== 损益结转 API ====================

/**
 * 执行损益结转
 * @param {Object} data 结转参数
 * @returns {Promise}
 */
export function executeProfitLossCarryForward(data) {
  return request({
    url: '/cwgxAi/general-ledger/profit-loss/carry-forward',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取损益结转预览
 * @param {Object} params 预览参数
 * @returns {Promise}
 */
export function getProfitLossCarryForwardPreview(params) {
  return request({
    url: '/cwgxAi/general-ledger/profit-loss/preview',
    method: 'get',
    params
  })
}

/**
 * 获取损益结转历史
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProfitLossCarryForwardHistory(params) {
  return request({
    url: '/cwgxAi/general-ledger/profit-loss/history',
    method: 'get',
    params
  })
}

/**
 * 反结转损益
 * @param {String} carryForwardId 结转ID
 * @param {String} reason 反结转原因
 * @returns {Promise}
 */
export function reverseProfitLossCarryForward(carryForwardId, reason) {
  return request({
    url: `/cwgxAi/general-ledger/profit-loss/reverse/${carryForwardId}`,
    method: 'post',
    data: { reason }
  })
}

// ==================== 期初余额 API ====================

/**
 * 分页查询期初余额
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getOpeningBalancePage(data) {
  return request({
    url: '/cwgxAi/general-ledger/opening-balance/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新期初余额
 * @param {Object} data 余额数据
 * @returns {Promise}
 */
export function saveOrUpdateOpeningBalance(data) {
  return request({
    url: '/cwgxAi/general-ledger/opening-balance/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量导入期初余额
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importOpeningBalance(formData) {
  return request({
    url: '/cwgxAi/general-ledger/opening-balance/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出期初余额
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportOpeningBalance(data) {
  return request({
    url: '/cwgxAi/general-ledger/opening-balance/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 下载期初余额模板
 * @returns {Promise}
 */
export function downloadOpeningBalanceTemplate() {
  return request({
    url: '/cwgxAi/general-ledger/opening-balance/template',
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 汇总账簿 API ====================

/**
 * 获取汇总账簿列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSummaryBookList(params) {
  return request({
    url: '/cwgxAi/general-ledger/summary-book/list',
    method: 'get',
    params
  })
}

/**
 * 创建汇总账簿
 * @param {Object} data 账簿数据
 * @returns {Promise}
 */
export function createSummaryBook(data) {
  return request({
    url: '/cwgxAi/general-ledger/summary-book',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新汇总账簿
 * @param {String} bookId 账簿ID
 * @param {Object} data 账簿数据
 * @returns {Promise}
 */
export function updateSummaryBook(bookId, data) {
  return request({
    url: `/cwgxAi/general-ledger/summary-book/${bookId}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除汇总账簿
 * @param {String} bookId 账簿ID
 * @returns {Promise}
 */
export function deleteSummaryBook(bookId) {
  return request({
    url: `/cwgxAi/general-ledger/summary-book/${bookId}`,
    method: 'delete'
  })
}

/**
 * 执行账簿汇总
 * @param {String} bookId 账簿ID
 * @param {Object} data 汇总参数
 * @returns {Promise}
 */
export function executeSummaryBook(bookId, data) {
  return request({
    url: `/cwgxAi/general-ledger/summary-book/${bookId}/execute`,
    method: 'post',
    data: transData(data)
  })
}

// ==================== 总账报表 API ====================

/**
 * 获取总账报表列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getGeneralLedgerReportList(params) {
  return request({
    url: '/cwgxAi/general-ledger/reports',
    method: 'get',
    params
  })
}

/**
 * 生成总账报表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateGeneralLedgerReport(data) {
  return request({
    url: '/cwgxAi/general-ledger/reports/generate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取报表数据
 * @param {String} reportId 报表ID
 * @returns {Promise}
 */
export function getGeneralLedgerReportData(reportId) {
  return request({
    url: `/cwgxAi/general-ledger/reports/${reportId}/data`,
    method: 'get'
  })
}

/**
 * 导出总账报表
 * @param {String} reportId 报表ID
 * @param {Object} exportOptions 导出选项
 * @returns {Promise}
 */
export function exportGeneralLedgerReport(reportId, exportOptions) {
  return request({
    url: `/cwgxAi/general-ledger/reports/${reportId}/export`,
    method: 'post',
    data: transData(exportOptions),
    responseType: 'blob'
  })
}

// ==================== 总账统计 API ====================

/**
 * 获取总账统计信息
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getGeneralLedgerStatistics(params) {
  return request({
    url: '/cwgxAi/general-ledger/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取科目统计分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getSubjectStatisticsAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/statistics/subject-analysis',
    method: 'get',
    params
  })
}

/**
 * 获取期间对比分析
 * @param {Object} params 对比参数
 * @returns {Promise}
 */
export function getPeriodComparisonAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/statistics/period-comparison',
    method: 'get',
    params
  })
}

// ==================== 总账查询 API ====================

/**
 * 分页查询总账数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getGeneralLedgerQueryPage(data) {
  return request({
    url: '/cwgxAi/general-ledger/query/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取科目总账查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSubjectLedgerQuery(params) {
  return request({
    url: '/cwgxAi/general-ledger/query/subject',
    method: 'get',
    params
  })
}

/**
 * 获取多栏式总账查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMultiColumnLedgerQuery(params) {
  return request({
    url: '/cwgxAi/general-ledger/query/multi-column',
    method: 'get',
    params
  })
}

/**
 * 获取总账汇总查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getLedgerSummaryQuery(params) {
  return request({
    url: '/cwgxAi/general-ledger/query/summary',
    method: 'get',
    params
  })
}

/**
 * 获取辅助核算总账查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAuxiliaryLedgerQuery(params) {
  return request({
    url: '/cwgxAi/general-ledger/query/auxiliary',
    method: 'get',
    params
  })
}

/**
 * 导出总账查询结果
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportLedgerQuery(data) {
  return request({
    url: '/cwgxAi/general-ledger/query/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 获取总账查询统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getLedgerQueryStatistics(params) {
  return request({
    url: '/cwgxAi/general-ledger/query/statistics',
    method: 'get',
    params
  })
}

// ==================== 期末处理扩展 API ====================

/**
 * 获取期末处理检查项
 * @param {Object} params 检查参数
 * @returns {Promise}
 */
export function getPeriodEndCheckItems(params) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/check-items',
    method: 'get',
    params
  })
}

/**
 * 执行期末检查
 * @param {Object} data 检查数据
 * @returns {Promise}
 */
export function executePeriodEndCheck(data) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/check',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取期末处理进度
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getPeriodEndProgress(taskId) {
  return request({
    url: `/cwgxAi/general-ledger/period-end/progress/${taskId}`,
    method: 'get'
  })
}

/**
 * 执行期末结账
 * @param {Object} data 结账数据
 * @returns {Promise}
 */
export function executePeriodEndClosing(data) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/closing',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 反结账操作
 * @param {Object} data 反结账数据
 * @returns {Promise}
 */
export function reversePeriodEndClosing(data) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/reverse-closing',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取结账历史
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPeriodEndClosingHistory(params) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/closing-history',
    method: 'get',
    params
  })
}

/**
 * 执行自动转账
 * @param {Object} data 转账数据
 * @returns {Promise}
 */
export function executeAutoTransfer(data) {
  return request({
    url: '/cwgxAi/general-ledger/period-end/auto-transfer',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 总账分析扩展 API ====================

/**
 * 获取科目余额分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getSubjectBalanceAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/subject-balance',
    method: 'get',
    params
  })
}

/**
 * 获取科目发生额分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getSubjectOccurrenceAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/subject-occurrence',
    method: 'get',
    params
  })
}

/**
 * 获取账龄分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getAccountAgeAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/account-age',
    method: 'get',
    params
  })
}

/**
 * 获取异常数据分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getAbnormalDataAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/abnormal-data',
    method: 'get',
    params
  })
}

/**
 * 获取趋势分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getTrendAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/trend',
    method: 'get',
    params
  })
}

/**
 * 获取对比分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getComparisonAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/comparison',
    method: 'get',
    params
  })
}

/**
 * 获取财务指标分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getFinancialRatioAnalysis(params) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/financial-ratio',
    method: 'get',
    params
  })
}

/**
 * 生成分析报告
 * @param {Object} data 报告参数
 * @returns {Promise}
 */
export function generateAnalysisReport(data) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/generate-report',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 导出分析结果
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportAnalysisResult(data) {
  return request({
    url: '/cwgxAi/general-ledger/analysis/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}


