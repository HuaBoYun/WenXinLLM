/*
 * @Description: 财务共享 - 收入管理模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 收入确认管理 API ====================

/**
 * 查询收入确认列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueRecognitionList(data) {
  return request({
    url: '/cwgxAi/revenue/recognition/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 执行收入确认
 * @param {Object} data 确认参数
 * @returns {Promise}
 */
export function executeRevenueRecognition(data) {
  return request({
    url: '/cwgxAi/revenue/recognition/execute',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取收入确认详情
 * @param {String} recognitionId 确认ID
 * @returns {Promise}
 */
export function getRevenueRecognitionDetail(recognitionId) {
  return request({
    url: `/cwgxAi/revenue/recognition/${recognitionId}`,
    method: 'get'
  })
}

/**
 * 根据收入确认ID获取凭证信息
 * @param {String} recognitionId 确认ID
 * @returns {Promise}
 */
export function getRecognitionVoucher(recognitionId) {
  return request({
    url: `/cwgxAi/revenue/recognition/${recognitionId}/voucher`,
    method: 'get'
  })
}

/**
 * 撤销收入确认
 * @param {String} recognitionId 确认ID
 * @param {Object} data 撤销数据
 * @returns {Promise}
 */
export function revokeRevenueRecognition(recognitionId, data) {
  return request({
    url: `/cwgxAi/revenue/recognition/${recognitionId}/revoke`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 批量确认收入
 * @param {Object} data 批量确认参数
 * @returns {Promise}
 */
export function batchRevenueRecognition(data) {
  return request({
    url: '/cwgxAi/revenue/recognition/batch',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取收入确认统计概览
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueRecognitionStats(params) {
  return request({
    url: '/cwgxAi/revenue/recognition/stats',
    method: 'get',
    params
  })
}

// ==================== 收入分配管理 API ====================

/**
 * 获取收入分配列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueAllocationList(data) {
  return request({
    url: '/cwgxAi/revenue/allocation/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 创建收入分配
 * @param {Object} data 分配数据
 * @returns {Promise}
 */
export function createRevenueAllocation(data) {
  return request({
    url: '/cwgxAi/revenue/allocation/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 更新收入分配
 * @param {String} allocationId 分配ID
 * @param {Object} data 分配数据
 * @returns {Promise}
 */
export function updateRevenueAllocation(allocationId, data) {
  return request({
    url: `/cwgxAi/revenue/allocation/${allocationId}`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 删除收入分配
 * @param {String} allocationId 分配ID
 * @returns {Promise}
 */
export function deleteRevenueAllocation(allocationId) {
  return request({
    url: `/cwgxAi/revenue/allocation/${allocationId}`,
    method: 'delete'
  })
}

/**
 * 执行收入分配
 * @param {String} allocationId 分配ID
 * @returns {Promise}
 */
export function executeRevenueAllocation(allocationId) {
  return request({
    url: `/cwgxAi/revenue/allocation/${allocationId}/execute`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取分配规则列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAllocationRuleList(params) {
  return request({
    url: '/cwgxAi/revenue/allocation/rules',
    method: 'get',
    params
  })
}

/**
 * 获取收入分配统计
 * @returns {Promise}
 */
export function getRevenueAllocationStats() {
  return request({
    url: '/cwgxAi/revenue/allocation/stats',
    method: 'get'
  })
}

// ==================== 合同收入管理 API ====================

/**
 * 获取合同收入列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getContractRevenueList(data) {
  return request({
    url: '/cwgxAi/revenue/contract/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 创建合同收入
 * @param {Object} data 收入数据
 * @returns {Promise}
 */
export function createContractRevenue(data) {
  return request({
    url: '/cwgxAi/revenue/contract/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 更新合同收入
 * @param {String} revenueId 收入ID
 * @param {Object} data 收入数据
 * @returns {Promise}
 */
export function updateContractRevenue(revenueId, data) {
  return request({
    url: `/cwgxAi/revenue/contract/${revenueId}`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取合同收入详情
 * @param {String} revenueId 收入ID
 * @returns {Promise}
 */
export function getContractRevenueDetail(revenueId) {
  return request({
    url: `/cwgxAi/revenue/contract/${revenueId}`,
    method: 'get'
  })
}

/**
 * 识别履约义务
 * @param {String} contractId 合同ID
 * @returns {Promise}
 */
export function identifyPerformanceObligations(contractId) {
  return request({
    url: `/cwgxAi/revenue/contract/${contractId}/obligations`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新履约进度
 * @param {String} contractId 合同ID
 * @param {Object} data 进度数据
 * @returns {Promise}
 */
export function updatePerformanceProgress(contractId, data) {
  return request({
    url: `/cwgxAi/revenue/contract/${contractId}/progress`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取合同收入统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getContractRevenueStats(params) {
  return request({
    url: '/cwgxAi/revenue/contract/stats',
    method: 'get',
    params
  })
}

// ==================== 递延收入管理 API ====================

/**
 * 获取递延收入列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getDeferredRevenueList(data) {
  return request({
    url: '/cwgxAi/revenue/deferred/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 创建递延收入
 * @param {Object} data 递延收入数据
 * @returns {Promise}
 */
export function createDeferredRevenue(data) {
  return request({
    url: '/cwgxAi/revenue/deferred/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 更新递延收入
 * @param {String} deferredId 递延收入ID
 * @param {Object} data 递延收入数据
 * @returns {Promise}
 */
export function updateDeferredRevenue(deferredId, data) {
  return request({
    url: `/cwgxAi/revenue/deferred/${deferredId}`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 删除递延收入
 * @param {String} deferredId 递延收入ID
 * @returns {Promise}
 */
export function deleteDeferredRevenue(deferredId) {
  return request({
    url: `/cwgxAi/revenue/deferred/${deferredId}`,
    method: 'delete'
  })
}

/**
 * 分期确认递延收入
 * @param {String} deferredId 递延收入ID
 * @param {Object} data 确认数据
 * @returns {Promise}
 */
export function recognizeDeferredRevenue(deferredId, data) {
  return request({
    url: `/cwgxAi/revenue/deferred/${deferredId}/recognize`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取确认计划
 * @param {String} deferredId 递延收入ID
 * @returns {Promise}
 */
export function getRecognitionSchedule(deferredId) {
  return request({
    url: `/cwgxAi/revenue/deferred/${deferredId}/schedule`,
    method: 'get'
  })
}

/**
 * 更新确认计划
 * @param {String} deferredId 递延收入ID
 * @param {Object} data 计划数据
 * @returns {Promise}
 */
export function updateRecognitionSchedule(deferredId, data) {
  return request({
    url: `/cwgxAi/revenue/deferred/${deferredId}/schedule`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取递延收入详情
 * @param {String} deferredId 递延收入ID
 * @returns {Promise}
 */
export function getDeferredRevenueDetail(deferredId) {
  return request({
    url: `/cwgxAi/revenue/deferred/${deferredId}`,
    method: 'get'
  })
}

/**
 * 获取递延收入统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDeferredRevenueStats(params) {
  return request({
    url: '/cwgxAi/revenue/deferred/stats',
    method: 'get',
    params
  })
}

// ==================== 收入调整管理 API ====================

/**
 * 获取收入调整列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueAdjustmentList(data) {
  return request({
    url: '/cwgxAi/revenue/adjustment/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 创建收入调整
 * @param {Object} data 调整数据
 * @returns {Promise}
 */
export function createRevenueAdjustment(data) {
  return request({
    url: '/cwgxAi/revenue/adjustment/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 审批收入调整
 * @param {String} adjustmentId 调整ID
 * @param {Object} data 审批数据
 * @returns {Promise}
 */
export function approveRevenueAdjustment(adjustmentId, data) {
  return request({
    url: `/cwgxAi/revenue/adjustment/${adjustmentId}/approve`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 执行收入调整
 * @param {String} adjustmentId 调整ID
 * @returns {Promise}
 */
export function executeRevenueAdjustment(adjustmentId) {
  return request({
    url: `/cwgxAi/revenue/adjustment/${adjustmentId}/execute`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取调整影响分析
 * @param {String} adjustmentId 调整ID
 * @returns {Promise}
 */
export function getAdjustmentImpactAnalysis(adjustmentId) {
  return request({
    url: `/cwgxAi/revenue/adjustment/${adjustmentId}/impact`,
    method: 'get'
  })
}

/**
 * 获取收入调整统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueAdjustmentStats(params) {
  return request({
    url: '/cwgxAi/revenue/adjustment/stats',
    method: 'get',
    params
  })
}

// ==================== 收入分析管理 API ====================

/**
 * 获取收入结构分析列表（分页）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueStructureAnalysisList(data) {
  return request({
    url: '/cwgxAi/revenue/analysis/structure/getList',
    method: 'get',
    params: transData(data)
  })
}

/**
 * 获取收入结构分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueStructureAnalysis(params) {
  return request({
    url: '/cwgxAi/revenue/analysis/structure',
    method: 'get',
    params
  })
}

/**
 * 获取收入趋势分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueTrendAnalysis(params) {
  return request({
    url: '/cwgxAi/revenue/analysis/trend',
    method: 'get',
    params
  })
}

/**
 * 获取收入质量分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueQualityAnalysis(params) {
  return request({
    url: '/cwgxAi/revenue/analysis/quality',
    method: 'get',
    params
  })
}

/**
 * 获取收入预测模型
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueForecastModel(params) {
  return request({
    url: '/cwgxAi/revenue/analysis/forecast',
    method: 'get',
    params
  })
}

/**
 * 获取收入对比分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueComparisonAnalysis(params) {
  return request({
    url: '/cwgxAi/revenue/analysis/comparison',
    method: 'get',
    params
  })
}

/**
 * 获取收入贡献度分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueContributionAnalysis(params) {
  return request({
    url: '/cwgxAi/revenue/analysis/contribution',
    method: 'get',
    params
  })
}

// ==================== 收入统计管理 API ====================

/**
 * 获取收入统计概览
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueStatisticsOverview(params) {
  return request({
    url: '/cwgxAi/revenue/statistics/overview',
    method: 'get',
    params
  })
}

/**
 * 获取收入月度统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueMonthlyStatistics(params) {
  return request({
    url: '/cwgxAi/revenue/statistics/monthly',
    method: 'get',
    params
  })
}

/**
 * 获取收入年度统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueYearlyStatistics(params) {
  return request({
    url: '/cwgxAi/revenue/statistics/yearly',
    method: 'get',
    params
  })
}

/**
 * 获取收入部门统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueDepartmentStatistics(params) {
  return request({
    url: '/cwgxAi/revenue/statistics/department',
    method: 'get',
    params
  })
}

/**
 * 获取收入产品统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRevenueProductStatistics(params) {
  return request({
    url: '/cwgxAi/revenue/statistics/product',
    method: 'get',
    params
  })
}

// ==================== 收入报表管理 API ====================

/**
 * 生成收入明细报表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateRevenueDetailReport(data) {
  return request({
    url: '/cwgxAi/revenue/report/detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 生成收入汇总报表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateRevenueSummaryReport(data) {
  return request({
    url: '/cwgxAi/revenue/report/summary',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 生成收入确认报表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateRevenueRecognitionReport(data) {
  return request({
    url: '/cwgxAi/revenue/report/recognition',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 生成收入分析报表
 * @param {Object} data 报表参数
 * @returns {Promise}
 */
export function generateRevenueAnalysisReport(data) {
  return request({
    url: '/cwgxAi/revenue/report/analysis',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 导出收入数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportRevenueData(data) {
  return request({
    url: '/cwgxAi/revenue/export',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data)),
    responseType: 'blob'
  })
}

/**
 * 导入收入数据
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importRevenueData(formData) {
  return request({
    url: '/cwgxAi/revenue/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// ==================== 收入规则管理 API ====================

/**
 * 获取收入确认规则列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueRecognitionRules(data) {
  return request({
    url: '/cwgxAi/revenue/rules/recognition',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 保存收入确认规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveRevenueRecognitionRule(data) {
  return request({
    url: '/cwgxAi/revenue/rules/recognition/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 删除收入确认规则
 * @param {String} ruleId 规则ID
 * @returns {Promise}
 */
export function deleteRevenueRecognitionRule(ruleId) {
  return request({
    url: `/cwgxAi/revenue/rules/recognition/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 启用/禁用收入确认规则
 * @param {String} ruleId 规则ID
 * @param {Boolean} enabled 是否启用
 * @returns {Promise}
 */
export function toggleRevenueRecognitionRule(ruleId, enabled) {
  return request({
    url: `/cwgxAi/revenue/rules/recognition/${ruleId}/toggle`,
    method: 'put',
    params: { enabled }
  })
}

// ==================== 收入批量结账管理 API ====================

/**
 * 获取收入批量结账列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRevenueBatchSettlementList(data) {
  return request({
    url: '/cwgxAi/revenue/batch-settlement/getList',
    method: 'get',
    params: transData(data)
  })
}

/**
 * 获取收入批量结账详情
 * @param {String} settlementId 结账ID
 * @returns {Promise}
 */
export function getRevenueBatchSettlementDetail(settlementId) {
  return request({
    url: `/cwgxAi/revenue/batch-settlement/detail/${settlementId}`,
    method: 'get'
  })
}

/**
 * 执行收入批量结账
 * @param {Object} data 结账参数
 * @returns {Promise}
 */
export function doRevenueBatchSettlement(data) {
  return request({
    url: '/cwgxAi/revenue/batch-settlement/doSettle',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 取消收入批量结账
 * @param {Object} data 取消结账参数
 * @returns {Promise}
 */
export function cancelRevenueBatchSettlement(data) {
  return request({
    url: '/cwgxAi/revenue/batch-settlement/cancelSettle',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}
