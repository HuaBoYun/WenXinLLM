import request from '@/utils/request'

/**
 * 预算数据管理API
 */

// 分页查询预算数据
export function queryBudgetDataPage(params) {
  return request({
    url: '/accountant/eps/budget-data/page',
    method: 'get',
    params
  })
}

// 创建预算数据
export function createBudgetData(data) {
  return request({
    url: '/accountant/eps/budget-data',
    method: 'post',
    data
  })
}

// 更新预算数据
export function updateBudgetData(data) {
  return request({
    url: '/accountant/eps/budget-data',
    method: 'put',
    data
  })
}

// 删除预算数据
export function deleteBudgetData(dataId) {
  return request({
    url: `/accountant/eps/budget-data/${dataId}`,
    method: 'delete'
  })
}

// 批量删除预算数据
export function batchDeleteBudgetData(dataIds) {
  return request({
    url: '/accountant/eps/budget-data/batch',
    method: 'delete',
    data: dataIds
  })
}

// 根据ID查询预算数据详情
export function getBudgetDataById(dataId) {
  return request({
    url: `/accountant/eps/budget-data/${dataId}`,
    method: 'get'
  })
}

// 根据版本查询预算数据
export function getBudgetDataByVersionId(versionId) {
  return request({
    url: `/accountant/eps/budget-data/version/${versionId}`,
    method: 'get'
  })
}

// 根据科目查询预算数据
export function getBudgetDataBySubjectId(subjectId, versionId) {
  return request({
    url: `/accountant/eps/budget-data/subject/${subjectId}`,
    method: 'get',
    params: { versionId }
  })
}

// 获取预算数据矩阵
export function getBudgetDataMatrix(params) {
  return request({
    url: '/accountant/eps/budget-data/matrix',
    method: 'get',
    params
  })
}

// 批量保存预算数据
export function batchSaveBudgetData(budgetDataList) {
  return request({
    url: '/accountant/eps/budget-data/batch-save',
    method: 'post',
    data: budgetDataList
  })
}

// 导入预算数据
export function importBudgetData(importData) {
  return request({
    url: '/accountant/eps/budget-data/import',
    method: 'post',
    data: importData
  })
}

// 导出预算数据
export function exportBudgetData(exportParams) {
  return request({
    url: '/accountant/eps/budget-data/export',
    method: 'post',
    data: exportParams
  })
}

// 计算预算数据
export function calculateBudgetData(calculateParams) {
  return request({
    url: '/accountant/eps/budget-data/calculate',
    method: 'post',
    data: calculateParams
  })
}

// 验证预算数据
export function validateBudgetData(validateParams) {
  return request({
    url: '/accountant/eps/budget-data/validate',
    method: 'post',
    data: validateParams
  })
}

// 汇总预算数据
export function summarizeBudgetData(summarizeParams) {
  return request({
    url: '/accountant/eps/budget-data/summarize',
    method: 'post',
    data: summarizeParams
  })
}

// 分解预算数据
export function decomposeBudgetData(decomposeParams) {
  return request({
    url: '/accountant/eps/budget-data/decompose',
    method: 'post',
    data: decomposeParams
  })
}

// 复制预算数据
export function copyBudgetData(copyParams) {
  return request({
    url: '/accountant/eps/budget-data/copy',
    method: 'post',
    data: copyParams
  })
}

// 锁定预算数据
export function lockBudgetData(dataId, lockReason) {
  return request({
    url: `/accountant/eps/budget-data/${dataId}/lock`,
    method: 'post',
    params: { lockReason }
  })
}

// 解锁预算数据
export function unlockBudgetData(dataId) {
  return request({
    url: `/accountant/eps/budget-data/${dataId}/unlock`,
    method: 'post'
  })
}

// 提交预算数据
export function submitBudgetData(submitParams) {
  return request({
    url: '/accountant/eps/budget-data/submit',
    method: 'post',
    data: submitParams
  })
}

// 审批预算数据
export function approveBudgetData(approveParams) {
  return request({
    url: '/accountant/eps/budget-data/approve',
    method: 'post',
    data: approveParams
  })
}

// 获取预算数据统计
export function getBudgetDataStatistics(params) {
  return request({
    url: '/accountant/eps/budget-data/statistics',
    method: 'get',
    params
  })
}

// 获取预算数据变更历史
export function getBudgetDataHistory(dataId) {
  return request({
    url: `/accountant/eps/budget-data/${dataId}/history`,
    method: 'get'
  })
}

// 批量操作预算数据
export function batchOperateBudgetData(batchData) {
  return request({
    url: '/accountant/eps/budget-data/batch-operation',
    method: 'post',
    data: batchData
  })
}

// 获取预算数据配置
export function getBudgetDataConfiguration(versionId) {
  return request({
    url: '/accountant/eps/budget-data/configuration',
    method: 'get',
    params: { versionId }
  })
}

// 保存预算数据配置
export function saveBudgetDataConfiguration(versionId, configuration) {
  return request({
    url: '/accountant/eps/budget-data/configuration',
    method: 'post',
    data: { versionId, configuration }
  })
}

// 获取预算数据权限
export function getBudgetDataPermissions(dataId, userId) {
  return request({
    url: '/accountant/eps/budget-data/permissions',
    method: 'get',
    params: { dataId, userId }
  })
}

// 设置预算数据权限
export function setBudgetDataPermissions(dataId, permissionData) {
  return request({
    url: '/accountant/eps/budget-data/permissions',
    method: 'post',
    data: { dataId, ...permissionData }
  })
}

// 获取预算数据模板
export function getBudgetDataTemplate(versionId) {
  return request({
    url: '/accountant/eps/budget-data/template',
    method: 'get',
    params: { versionId }
  })
}

// 应用预算数据模板
export function applyBudgetDataTemplate(versionId, templateData) {
  return request({
    url: '/accountant/eps/budget-data/template',
    method: 'post',
    data: { versionId, templateData }
  })
}

// 获取预算数据差异
export function getBudgetDataDifferences(sourceVersionId, targetVersionId) {
  return request({
    url: '/accountant/eps/budget-data/differences',
    method: 'get',
    params: { sourceVersionId, targetVersionId }
  })
}

// 同步预算数据
export function syncBudgetData(sourceVersionId, targetVersionId, syncParams) {
  return request({
    url: '/accountant/eps/budget-data/sync',
    method: 'post',
    data: { sourceVersionId, targetVersionId, ...syncParams }
  })
}

// 合并预算数据
export function mergeBudgetData(sourceVersionIds, targetVersionId, mergeParams) {
  return request({
    url: '/accountant/eps/budget-data/merge',
    method: 'post',
    data: { sourceVersionIds, targetVersionId, ...mergeParams }
  })
}

// 拆分预算数据
export function splitBudgetData(sourceVersionId, splitParams) {
  return request({
    url: '/accountant/eps/budget-data/split',
    method: 'post',
    data: { sourceVersionId, ...splitParams }
  })
}

// 获取预算数据趋势
export function getBudgetDataTrend(versionId, subjectId, organizationId) {
  return request({
    url: '/accountant/eps/budget-data/trend',
    method: 'get',
    params: { versionId, subjectId, organizationId }
  })
}

// 获取预算数据预测
export function forecastBudgetData(versionId, forecastParams) {
  return request({
    url: '/accountant/eps/budget-data/forecast',
    method: 'post',
    data: { versionId, ...forecastParams }
  })
}

// 获取预算数据异常
export function getBudgetDataAnomalies(versionId, anomalyParams) {
  return request({
    url: '/accountant/eps/budget-data/anomalies',
    method: 'get',
    params: { versionId, ...anomalyParams }
  })
}

// 修复预算数据
export function repairBudgetData(versionId, repairParams) {
  return request({
    url: '/accountant/eps/budget-data/repair',
    method: 'post',
    data: { versionId, ...repairParams }
  })
}

// 获取预算数据性能统计
export function getBudgetDataPerformanceStatistics(versionId, startDate, endDate) {
  return request({
    url: '/accountant/eps/budget-data/performance-statistics',
    method: 'get',
    params: { versionId, startDate, endDate }
  })
}

// 优化预算数据
export function optimizeBudgetData(versionId, optimizeParams) {
  return request({
    url: '/accountant/eps/budget-data/optimize',
    method: 'post',
    data: { versionId, ...optimizeParams }
  })
}

// 预算数据快捷操作
export const budgetDataQuickActions = {
  // 快速创建空白数据
  createBlankData(versionId, subjectId, organizationId, budgetPeriod) {
    return createBudgetData({
      versionId,
      subjectId,
      organizationId,
      budgetPeriod,
      budgetAmount: 0,
      actualAmount: 0,
      varianceAmount: 0,
      dataStatus: 'DRAFT'
    })
  },

  // 快速复制数据
  quickCopyData(sourceDataId, targetParams) {
    return copyBudgetData({
      sourceDataId,
      ...targetParams
    })
  },

  // 快速锁定多个数据
  quickLockMultiple(dataIds, lockReason) {
    return batchOperateBudgetData({
      operation: 'LOCK',
      dataIds,
      lockReason
    })
  },

  // 快速解锁多个数据
  quickUnlockMultiple(dataIds) {
    return batchOperateBudgetData({
      operation: 'UNLOCK',
      dataIds
    })
  },

  // 快速提交多个数据
  quickSubmitMultiple(dataIds, submitComment) {
    return batchOperateBudgetData({
      operation: 'SUBMIT',
      dataIds,
      submitComment
    })
  },

  // 快速审批多个数据
  quickApproveMultiple(dataIds, approveComment) {
    return batchOperateBudgetData({
      operation: 'APPROVE',
      dataIds,
      approveComment
    })
  },

  // 快速拒绝多个数据
  quickRejectMultiple(dataIds, rejectReason) {
    return batchOperateBudgetData({
      operation: 'REJECT',
      dataIds,
      rejectReason
    })
  }
}

// 预算数据工具函数
export const budgetDataUtils = {
  // 格式化金额
  formatAmount(amount) {
    if (!amount) return '0.00'
    return Number(amount).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  },

  // 计算差异率
  calculateVarianceRate(budgetAmount, actualAmount) {
    if (!budgetAmount || budgetAmount === 0) return 0
    const variance = budgetAmount - actualAmount
    return ((variance / budgetAmount) * 100).toFixed(2)
  },

  // 获取状态显示文本
  getStatusText(status) {
    const statusMap = {
      'DRAFT': '草稿',
      'SUBMITTED': '已提交',
      'APPROVED': '已审批',
      'REJECTED': '已拒绝',
      'LOCKED': '已锁定'
    }
    return statusMap[status] || status
  },

  // 获取状态颜色
  getStatusColor(status) {
    const colorMap = {
      'DRAFT': '#909399',
      'SUBMITTED': '#E6A23C',
      'APPROVED': '#67C23A',
      'REJECTED': '#F56C6C',
      'LOCKED': '#F56C6C'
    }
    return colorMap[status] || '#909399'
  },

  // 验证数据完整性
  validateDataIntegrity(data) {
    const errors = []
    
    if (!data.versionId) errors.push('版本ID不能为空')
    if (!data.subjectId) errors.push('科目ID不能为空')
    if (!data.organizationId) errors.push('组织ID不能为空')
    if (!data.budgetPeriod) errors.push('预算期间不能为空')
    
    if (data.budgetAmount !== undefined && data.budgetAmount < 0) {
      errors.push('预算金额不能为负数')
    }
    
    if (data.actualAmount !== undefined && data.actualAmount < 0) {
      errors.push('实际金额不能为负数')
    }
    
    return {
      isValid: errors.length === 0,
      errors
    }
  }
}

export default {
  queryBudgetDataPage,
  createBudgetData,
  updateBudgetData,
  deleteBudgetData,
  batchDeleteBudgetData,
  getBudgetDataById,
  getBudgetDataByVersionId,
  getBudgetDataBySubjectId,
  getBudgetDataMatrix,
  batchSaveBudgetData,
  importBudgetData,
  exportBudgetData,
  calculateBudgetData,
  validateBudgetData,
  summarizeBudgetData,
  decomposeBudgetData,
  copyBudgetData,
  lockBudgetData,
  unlockBudgetData,
  submitBudgetData,
  approveBudgetData,
  getBudgetDataStatistics,
  getBudgetDataHistory,
  batchOperateBudgetData,
  budgetDataQuickActions,
  budgetDataUtils
}
