import request from '@/utils/request'

/**
 * 智能审核API接口
 */

// 基础CRUD操作
export function getAuditPage(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/page',
    method: 'get',
    params
  })
}

export function getAuditById(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByAuditCode(auditCode, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/code/${auditCode}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByAuditType(auditType, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/type/${auditType}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByAuditStatus(auditStatus, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/status/${auditStatus}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByRiskLevel(riskLevel, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/risk/${riskLevel}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByAuditorId(auditorId, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/auditor/${auditorId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByAuditDeptId(auditDeptId, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/dept/${auditDeptId}`,
    method: 'get',
    params: { tenantId }
  })
}

export function getByTargetObject(targetObjectId, targetObjectType, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/target/${targetObjectId}/${targetObjectType}`,
    method: 'get',
    params: { tenantId }
  })
}

export function createAudit(data) {
  return request({
    url: '/accountant/ss/intelligent-audit',
    method: 'post',
    data
  })
}

export function updateAudit(data) {
  return request({
    url: '/accountant/ss/intelligent-audit',
    method: 'put',
    data
  })
}

export function deleteAudit(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}`,
    method: 'delete',
    params: { tenantId }
  })
}

export function batchDeleteAudits(auditIds, tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/batch',
    method: 'delete',
    data: auditIds,
    params: { tenantId }
  })
}

// 审核流程操作
export function startAudit(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/start`,
    method: 'post',
    params
  })
}

export function pauseAudit(id, reason, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/pause`,
    method: 'post',
    params: { reason, tenantId }
  })
}

export function resumeAudit(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/resume`,
    method: 'post',
    params: { tenantId }
  })
}

export function completeAudit(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/complete`,
    method: 'post',
    params
  })
}

export function cancelAudit(id, reason, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/cancel`,
    method: 'post',
    params: { reason, tenantId }
  })
}

export function reviewAudit(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/review`,
    method: 'post',
    params
  })
}

export function processAudit(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/process`,
    method: 'post',
    params
  })
}

// 人员分配
export function assignAuditor(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/assign`,
    method: 'post',
    params
  })
}

export function batchAssignAuditor(auditIds, params) {
  return request({
    url: '/accountant/ss/intelligent-audit/batch-assign',
    method: 'post',
    data: auditIds,
    params
  })
}

// 智能审核功能
export function executeIntelligentAudit(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/execute`,
    method: 'post',
    params: { tenantId }
  })
}

export function batchExecuteIntelligentAudit(auditIds, tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/batch-execute',
    method: 'post',
    data: auditIds,
    params: { tenantId }
  })
}

export function applyMlModel(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/apply-ml-model`,
    method: 'post',
    params
  })
}

export function detectAnomaly(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/detect-anomaly`,
    method: 'post',
    params: { tenantId }
  })
}

export function assessRisk(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/assess-risk`,
    method: 'post',
    params: { tenantId }
  })
}

export function generateWarning(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/generate-warning`,
    method: 'post',
    params
  })
}

// 跟进管理
export function addFollowUpRecord(id, followUpRecord, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/add-follow-up`,
    method: 'post',
    params: { followUpRecord, tenantId }
  })
}

export function updateFollowUpStatus(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/update-follow-up-status`,
    method: 'post',
    params
  })
}

// 查询特定状态的审核
export function getPendingAudits(tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/pending',
    method: 'get',
    params: { tenantId }
  })
}

export function getNeedReview(tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/need-review',
    method: 'get',
    params: { tenantId }
  })
}

export function getNeedFollowUp(tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/need-follow-up',
    method: 'get',
    params: { tenantId }
  })
}

export function getHighRiskAudits(tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/high-risk',
    method: 'get',
    params: { tenantId }
  })
}

export function getAnomalyAudits(tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/anomaly',
    method: 'get',
    params: { tenantId }
  })
}

export function getOverdueAudits(currentTime, tenantId) {
  return request({
    url: '/accountant/ss/intelligent-audit/overdue',
    method: 'get',
    params: { currentTime, tenantId }
  })
}

// 统计分析
export function getAuditStatistics(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/statistics',
    method: 'get',
    params
  })
}

export function getAuditStatusDistribution(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/status-distribution',
    method: 'get',
    params
  })
}

export function getAuditTypeDistribution(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/type-distribution',
    method: 'get',
    params
  })
}

export function getRiskLevelDistribution(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/risk-distribution',
    method: 'get',
    params
  })
}

export function getAuditTrend(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/trend',
    method: 'get',
    params
  })
}

export function getAuditEfficiency(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/efficiency',
    method: 'get',
    params
  })
}

export function getAuditQuality(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/quality',
    method: 'get',
    params
  })
}

export function getAuditRanking(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/ranking',
    method: 'get',
    params
  })
}

export function getAuditorWorkload(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/auditor-workload',
    method: 'get',
    params
  })
}

export function getDeptAuditStatistics(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/dept-statistics',
    method: 'get',
    params
  })
}

// 计算和分析
export function calculateAverageAuditDuration(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/average-duration',
    method: 'get',
    params
  })
}

export function calculateAuditSuccessRate(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/success-rate',
    method: 'get',
    params
  })
}

export function generateAuditReport(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/report',
    method: 'get',
    params
  })
}

export function exportAuditData(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/export',
    method: 'get',
    params
  })
}

// 智能推荐和预测
export function recommendAuditRules(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/recommend-rules',
    method: 'get',
    params
  })
}

export function predictAuditRisk(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/predict-risk',
    method: 'get',
    params
  })
}

// 自动化和通知
export function automateAuditProcess(id, tenantId) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/automate`,
    method: 'post',
    params: { tenantId }
  })
}

export function sendAuditNotification(id, params) {
  return request({
    url: `/accountant/ss/intelligent-audit/${id}/notify`,
    method: 'post',
    params
  })
}

export function batchSendAuditNotifications(auditIds, params) {
  return request({
    url: '/accountant/ss/intelligent-audit/batch-notify',
    method: 'post',
    data: auditIds,
    params
  })
}

// 验证检查
export function checkAuditCodeExists(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/check-code',
    method: 'get',
    params
  })
}

export function checkTargetObjectInProgress(params) {
  return request({
    url: '/accountant/ss/intelligent-audit/check-target-in-progress',
    method: 'get',
    params
  })
}

// 快捷操作方法
export const auditQuickActions = {
  // 快速启动审核
  quickStart: (auditId, auditorInfo, tenantId) => {
    return startAudit(auditId, {
      auditorId: auditorInfo.auditorId,
      auditorName: auditorInfo.auditorName,
      auditDeptId: auditorInfo.auditDeptId,
      auditDeptName: auditorInfo.auditDeptName,
      tenantId
    })
  },

  // 快速完成审核
  quickComplete: (auditId, result, conclusion, tenantId) => {
    return completeAudit(auditId, {
      auditResult: result,
      auditConclusion: conclusion,
      tenantId
    })
  },

  // 快速复核
  quickReview: (auditId, reviewerInfo, comments, result, tenantId) => {
    return reviewAudit(auditId, {
      reviewerId: reviewerInfo.reviewerId,
      reviewerName: reviewerInfo.reviewerName,
      reviewComments: comments,
      reviewResult: result,
      tenantId
    })
  },

  // 快速执行智能审核流程
  quickIntelligentAudit: async (auditId, tenantId) => {
    try {
      // 执行智能审核
      await executeIntelligentAudit(auditId, tenantId)
      // 异常检测
      await detectAnomaly(auditId, tenantId)
      // 风险评估
      await assessRisk(auditId, tenantId)
      return { success: true, message: '智能审核流程执行成功' }
    } catch (error) {
      return { success: false, message: '智能审核流程执行失败: ' + error.message }
    }
  },

  // 批量操作
  batchOperations: {
    // 批量启动
    batchStart: async (auditIds, auditorInfo, tenantId) => {
      const results = []
      for (const auditId of auditIds) {
        try {
          await startAudit(auditId, {
            auditorId: auditorInfo.auditorId,
            auditorName: auditorInfo.auditorName,
            auditDeptId: auditorInfo.auditDeptId,
            auditDeptName: auditorInfo.auditDeptName,
            tenantId
          })
          results.push({ auditId, success: true })
        } catch (error) {
          results.push({ auditId, success: false, error: error.message })
        }
      }
      return results
    },

    // 批量执行智能审核
    batchIntelligentAudit: (auditIds, tenantId) => {
      return batchExecuteIntelligentAudit(auditIds, tenantId)
    },

    // 批量分配审核人员
    batchAssign: (auditIds, auditorInfo, tenantId) => {
      return batchAssignAuditor(auditIds, {
        auditorId: auditorInfo.auditorId,
        auditorName: auditorInfo.auditorName,
        auditDeptId: auditorInfo.auditDeptId,
        auditDeptName: auditorInfo.auditDeptName,
        tenantId
      })
    }
  }
}

// 工具函数
export const auditUtils = {
  // 格式化审核状态
  formatAuditStatus: (status) => {
    const statusMap = {
      'DRAFT': '草稿',
      'IN_PROGRESS': '进行中',
      'PAUSED': '已暂停',
      'COMPLETED': '已完成',
      'CANCELLED': '已取消',
      'REVIEWED': '已复核'
    }
    return statusMap[status] || status
  },

  // 格式化风险等级
  formatRiskLevel: (level) => {
    const levelMap = {
      'LOW': '低风险',
      'MEDIUM': '中风险',
      'HIGH': '高风险',
      'CRITICAL': '极高风险'
    }
    return levelMap[level] || level
  },

  // 格式化审核类型
  formatAuditType: (type) => {
    const typeMap = {
      'FINANCIAL': '财务审核',
      'COMPLIANCE': '合规审核',
      'OPERATIONAL': '运营审核',
      'SECURITY': '安全审核',
      'QUALITY': '质量审核',
      'PERFORMANCE': '绩效审核'
    }
    return typeMap[type] || type
  },

  // 获取状态颜色
  getStatusColor: (status) => {
    const colorMap = {
      'DRAFT': '#909399',
      'IN_PROGRESS': '#409EFF',
      'PAUSED': '#E6A23C',
      'COMPLETED': '#67C23A',
      'CANCELLED': '#F56C6C',
      'REVIEWED': '#67C23A'
    }
    return colorMap[status] || '#909399'
  },

  // 获取风险等级颜色
  getRiskLevelColor: (level) => {
    const colorMap = {
      'LOW': '#67C23A',
      'MEDIUM': '#E6A23C',
      'HIGH': '#F56C6C',
      'CRITICAL': '#F56C6C'
    }
    return colorMap[level] || '#909399'
  },

  // 计算审核进度
  calculateProgress: (audit) => {
    if (!audit) return 0

    const statusProgress = {
      'DRAFT': 10,
      'IN_PROGRESS': 50,
      'PAUSED': 30,
      'COMPLETED': 100,
      'CANCELLED': 0,
      'REVIEWED': 100
    }

    return statusProgress[audit.auditStatus] || 0
  },

  // 验证审核数据
  validateAuditData: (auditData) => {
    const errors = []

    if (!auditData.auditTitle) {
      errors.push('审核标题不能为空')
    }

    if (!auditData.auditType) {
      errors.push('审核类型不能为空')
    }

    if (!auditData.targetObjectId) {
      errors.push('目标对象不能为空')
    }

    if (!auditData.targetObjectType) {
      errors.push('目标对象类型不能为空')
    }

    return {
      isValid: errors.length === 0,
      errors
    }
  }
}

// 默认导出
export default {
  // API方法
  getAuditPage,
  getAuditById,
  createAudit,
  updateAudit,
  deleteAudit,
  startAudit,
  completeAudit,
  reviewAudit,
  executeIntelligentAudit,
  getAuditStatistics,

  // 快捷操作
  auditQuickActions,

  // 工具函数
  auditUtils
}
