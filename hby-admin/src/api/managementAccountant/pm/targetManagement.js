import request from '@/utils/request'

/**
 * 目标管理API
 */

// 分页查询目标
export function queryTargetPage(params) {
  return request({
    url: '/accountant/pm/target-management/page',
    method: 'get',
    params
  })
}

// 创建目标
export function createTarget(data) {
  return request({
    url: '/accountant/pm/target-management',
    method: 'post',
    data
  })
}

// 更新目标
export function updateTarget(data) {
  return request({
    url: '/accountant/pm/target-management',
    method: 'put',
    data
  })
}

// 删除目标
export function deleteTarget(targetId) {
  return request({
    url: `/accountant/pm/target-management/${targetId}`,
    method: 'delete'
  })
}

// 根据ID查询目标详情
export function getTargetById(targetId) {
  return request({
    url: `/accountant/pm/target-management/${targetId}`,
    method: 'get'
  })
}

// 目标分解
export function decomposeTarget(targetId, decomposeParams) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/decompose`,
    method: 'post',
    data: decomposeParams
  })
}

// 目标跟踪
export function trackTarget(targetId, trackingData) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/track`,
    method: 'post',
    data: trackingData
  })
}

// 目标调整
export function adjustTarget(targetId, adjustParams) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/adjust`,
    method: 'post',
    data: adjustParams
  })
}

// 目标评估
export function evaluateTarget(targetId, evaluationParams) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/evaluate`,
    method: 'post',
    data: evaluationParams
  })
}

// 目标激励
export function incentiveTarget(targetId, incentiveParams) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/incentive`,
    method: 'post',
    data: incentiveParams
  })
}

// 目标协商
export function negotiateTarget(targetId, negotiationParams) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/negotiate`,
    method: 'post',
    data: negotiationParams
  })
}

// 获取目标知识
export function getTargetKnowledge(targetId, params) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/knowledge`,
    method: 'get',
    params
  })
}

// 目标数据分析
export function analyzeTarget(targetId, params) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/analysis`,
    method: 'get',
    params
  })
}

// 获取目标树
export function getTargetTree(params) {
  return request({
    url: '/accountant/pm/target-management/tree',
    method: 'get',
    params
  })
}

// 获取目标仪表板
export function getTargetDashboard(params) {
  return request({
    url: '/accountant/pm/target-management/dashboard',
    method: 'get',
    params
  })
}

// 批量操作目标
export function batchOperateTargets(batchData) {
  return request({
    url: '/accountant/pm/target-management/batch-operation',
    method: 'post',
    data: batchData
  })
}

// 导入目标
export function importTargets(importData) {
  return request({
    url: '/accountant/pm/target-management/import',
    method: 'post',
    data: importData
  })
}

// 导出目标
export function exportTargets(exportParams) {
  return request({
    url: '/accountant/pm/target-management/export',
    method: 'post',
    data: exportParams
  })
}

// 获取目标统计
export function getTargetStatistics(params) {
  return request({
    url: '/accountant/pm/target-management/statistics',
    method: 'get',
    params
  })
}

// 获取目标进度报告
export function getTargetProgressReport(params) {
  return request({
    url: '/accountant/pm/target-management/progress-report',
    method: 'get',
    params
  })
}

// 复制目标
export function copyTarget(targetId, copyParams) {
  return request({
    url: `/accountant/pm/target-management/${targetId}/copy`,
    method: 'post',
    data: copyParams
  })
}

// 获取目标模板
export function getTargetTemplates(params) {
  return request({
    url: '/accountant/pm/target-management/templates',
    method: 'get',
    params
  })
}

// 应用目标模板
export function applyTargetTemplate(templateParams) {
  return request({
    url: '/accountant/pm/target-management/templates/apply',
    method: 'post',
    data: templateParams
  })
}

// 获取目标建议
export function getTargetRecommendations(params) {
  return request({
    url: '/accountant/pm/target-management/recommendations',
    method: 'get',
    params
  })
}

// 刷新目标缓存
export function refreshTargetCache(params) {
  return request({
    url: '/accountant/pm/target-management/refresh-cache',
    method: 'post',
    params
  })
}

// 目标管理快捷操作
export const targetManagementQuickActions = {
  // 快速创建个人目标
  quickCreatePersonalTarget(targetName, targetValue, targetOwnerId, organizationId) {
    return createTarget({
      targetName,
      targetType: 'PERSONAL',
      targetLevel: 'INDIVIDUAL',
      targetCategory: 'OPERATIONAL',
      targetValue,
      targetOwnerId,
      organizationId,
      targetStatus: 'DRAFT',
      progressStatus: 'ON_TRACK',
      priority: 'MEDIUM',
      isEnabled: 1,
      isVisible: 1
    })
  },

  // 快速创建团队目标
  quickCreateTeamTarget(targetName, targetValue, targetOwnerId, organizationId) {
    return createTarget({
      targetName,
      targetType: 'TEAM',
      targetLevel: 'TEAM',
      targetCategory: 'OPERATIONAL',
      targetValue,
      targetOwnerId,
      organizationId,
      targetStatus: 'DRAFT',
      progressStatus: 'ON_TRACK',
      priority: 'HIGH',
      isEnabled: 1,
      isVisible: 1
    })
  },

  // 快速更新目标进度
  quickUpdateProgress(targetId, currentValue, trackingRemark) {
    return trackTarget(targetId, {
      currentValue,
      trackingRemark,
      trackingTime: new Date(),
      trackingUser: 'current_user'
    })
  },

  // 快速激活目标
  quickActivateTarget(targetId) {
    return adjustTarget(targetId, {
      targetStatus: 'ACTIVE',
      adjustmentReason: '目标激活',
      adjustmentUser: 'current_user'
    })
  },

  // 快速完成目标
  quickCompleteTarget(targetId) {
    return adjustTarget(targetId, {
      targetStatus: 'COMPLETED',
      progressStatus: 'ON_TRACK',
      adjustmentReason: '目标完成',
      adjustmentUser: 'current_user'
    })
  },

  // 快速获取我的目标
  quickGetMyTargets(targetOwnerId, organizationId) {
    return queryTargetPage({
      current: 1,
      size: 20,
      targetOwnerId,
      organizationId,
      targetStatus: 'ACTIVE'
    })
  },

  // 快速获取团队目标概览
  quickGetTeamOverview(organizationId) {
    return getTargetDashboard({
      organizationId,
      dashboardType: 'TEAM_OVERVIEW'
    })
  }
}

// 目标管理工具函数
export const targetManagementUtils = {
  // 格式化目标状态
  formatTargetStatus(status) {
    const statusMap = {
      'DRAFT': { text: '草稿', color: '#909399' },
      'ACTIVE': { text: '激活', color: '#409EFF' },
      'PAUSED': { text: '暂停', color: '#E6A23C' },
      'COMPLETED': { text: '完成', color: '#67C23A' },
      'CANCELLED': { text: '取消', color: '#F56C6C' }
    }
    return statusMap[status] || { text: status, color: '#909399' }
  },

  // 格式化进度状态
  formatProgressStatus(status) {
    const statusMap = {
      'ON_TRACK': { text: '正常', color: '#67C23A' },
      'AT_RISK': { text: '风险', color: '#E6A23C' },
      'OFF_TRACK': { text: '偏离', color: '#F56C6C' }
    }
    return statusMap[status] || { text: status, color: '#909399' }
  },

  // 格式化目标类型
  formatTargetType(type) {
    const typeMap = {
      'STRATEGIC': '战略目标',
      'OPERATIONAL': '运营目标',
      'PERSONAL': '个人目标',
      'TEAM': '团队目标'
    }
    return typeMap[type] || type
  },

  // 格式化目标级别
  formatTargetLevel(level) {
    const levelMap = {
      'COMPANY': '公司级',
      'DEPARTMENT': '部门级',
      'TEAM': '团队级',
      'INDIVIDUAL': '个人级'
    }
    return levelMap[level] || level
  },

  // 格式化目标分类
  formatTargetCategory(category) {
    const categoryMap = {
      'FINANCIAL': '财务类',
      'CUSTOMER': '客户类',
      'PROCESS': '流程类',
      'LEARNING': '学习成长类'
    }
    return categoryMap[category] || category
  },

  // 格式化优先级
  formatPriority(priority) {
    const priorityMap = {
      'HIGH': { text: '高', color: '#F56C6C' },
      'MEDIUM': { text: '中', color: '#E6A23C' },
      'LOW': { text: '低', color: '#409EFF' }
    }
    return priorityMap[priority] || { text: priority, color: '#909399' }
  },

  // 计算完成率
  calculateCompletionRate(currentValue, targetValue) {
    if (!targetValue || targetValue === 0) return 0
    return ((currentValue / targetValue) * 100).toFixed(2)
  },

  // 获取完成率状态
  getCompletionRateStatus(completionRate) {
    if (completionRate >= 100) return 'success'
    if (completionRate >= 80) return 'warning'
    if (completionRate >= 60) return 'info'
    return 'danger'
  },

  // 获取完成率颜色
  getCompletionRateColor(completionRate) {
    if (completionRate >= 100) return '#67C23A'
    if (completionRate >= 80) return '#E6A23C'
    if (completionRate >= 60) return '#409EFF'
    return '#F56C6C'
  },

  // 格式化目标值
  formatTargetValue(value, unit) {
    if (!value) return '0'
    const formattedValue = Number(value).toLocaleString('zh-CN', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 2
    })
    return unit ? `${formattedValue} ${unit}` : formattedValue
  },

  // 验证目标数据
  validateTargetData(target) {
    const errors = []
    
    if (!target.targetName) {
      errors.push('目标名称不能为空')
    }
    
    if (!target.targetType) {
      errors.push('目标类型不能为空')
    }
    
    if (!target.targetLevel) {
      errors.push('目标级别不能为空')
    }
    
    if (!target.targetOwnerId) {
      errors.push('目标负责人不能为空')
    }
    
    if (target.targetValue && target.targetValue <= 0) {
      errors.push('目标值必须大于0')
    }
    
    if (target.targetWeight && (target.targetWeight < 0 || target.targetWeight > 100)) {
      errors.push('目标权重必须在0-100之间')
    }
    
    if (target.startTime && target.endTime && new Date(target.startTime) >= new Date(target.endTime)) {
      errors.push('开始时间必须早于结束时间')
    }
    
    return {
      isValid: errors.length === 0,
      errors
    }
  },

  // 生成目标建议
  generateTargetSuggestions(targetData) {
    const suggestions = []
    
    if (!targetData.targetValue) {
      suggestions.push({
        type: 'VALUE',
        title: '建议设置目标值',
        description: '设置明确的目标值有助于跟踪进度',
        priority: 'HIGH'
      })
    }
    
    if (!targetData.endTime) {
      suggestions.push({
        type: 'TIMELINE',
        title: '建议设置截止时间',
        description: '明确的时间节点有助于目标管理',
        priority: 'MEDIUM'
      })
    }
    
    if (!targetData.targetWeight) {
      suggestions.push({
        type: 'WEIGHT',
        title: '建议设置目标权重',
        description: '权重设置有助于优先级管理',
        priority: 'MEDIUM'
      })
    }
    
    return suggestions
  },

  // 分析目标健康度
  analyzeTargetHealth(target) {
    const health = {
      score: 100,
      level: 'EXCELLENT',
      issues: []
    }
    
    // 检查完成率
    if (target.completionRate < 50) {
      health.score -= 30
      health.issues.push('完成率偏低')
    }
    
    // 检查进度状态
    if (target.progressStatus === 'OFF_TRACK') {
      health.score -= 25
      health.issues.push('进度偏离')
    } else if (target.progressStatus === 'AT_RISK') {
      health.score -= 15
      health.issues.push('存在风险')
    }
    
    // 检查时间进度
    if (target.startTime && target.endTime) {
      const now = new Date()
      const start = new Date(target.startTime)
      const end = new Date(target.endTime)
      const totalTime = end - start
      const elapsedTime = now - start
      const timeProgress = (elapsedTime / totalTime) * 100
      
      if (timeProgress > target.completionRate + 20) {
        health.score -= 20
        health.issues.push('时间进度超前于完成进度')
      }
    }
    
    // 确定健康等级
    if (health.score >= 90) {
      health.level = 'EXCELLENT'
    } else if (health.score >= 80) {
      health.level = 'GOOD'
    } else if (health.score >= 70) {
      health.level = 'FAIR'
    } else if (health.score >= 60) {
      health.level = 'POOR'
    } else {
      health.level = 'CRITICAL'
    }
    
    return health
  },

  // 生成目标报告
  generateTargetReport(targets, period) {
    const report = {
      period,
      summary: {
        totalTargets: targets.length,
        completedTargets: 0,
        onTrackTargets: 0,
        atRiskTargets: 0,
        offTrackTargets: 0,
        averageCompletionRate: 0
      },
      trends: [],
      recommendations: []
    }
    
    let totalCompletionRate = 0
    
    targets.forEach(target => {
      if (target.targetStatus === 'COMPLETED') {
        report.summary.completedTargets++
      }
      
      switch (target.progressStatus) {
        case 'ON_TRACK':
          report.summary.onTrackTargets++
          break
        case 'AT_RISK':
          report.summary.atRiskTargets++
          break
        case 'OFF_TRACK':
          report.summary.offTrackTargets++
          break
      }
      
      totalCompletionRate += target.completionRate || 0
    })
    
    report.summary.averageCompletionRate = (totalCompletionRate / targets.length).toFixed(2)
    
    // 生成建议
    if (report.summary.atRiskTargets > 0) {
      report.recommendations.push('关注风险目标，及时采取措施')
    }
    
    if (report.summary.offTrackTargets > 0) {
      report.recommendations.push('重新评估偏离目标的可行性')
    }
    
    if (report.summary.averageCompletionRate < 70) {
      report.recommendations.push('整体完成率偏低，建议加强执行力度')
    }
    
    return report
  }
}

// 目标管理常量
export const targetManagementConstants = {
  // 目标类型
  TARGET_TYPES: [
    { value: 'STRATEGIC', label: '战略目标' },
    { value: 'OPERATIONAL', label: '运营目标' },
    { value: 'PERSONAL', label: '个人目标' },
    { value: 'TEAM', label: '团队目标' }
  ],

  // 目标级别
  TARGET_LEVELS: [
    { value: 'COMPANY', label: '公司级' },
    { value: 'DEPARTMENT', label: '部门级' },
    { value: 'TEAM', label: '团队级' },
    { value: 'INDIVIDUAL', label: '个人级' }
  ],

  // 目标分类
  TARGET_CATEGORIES: [
    { value: 'FINANCIAL', label: '财务类' },
    { value: 'CUSTOMER', label: '客户类' },
    { value: 'PROCESS', label: '流程类' },
    { value: 'LEARNING', label: '学习成长类' }
  ],

  // 目标状态
  TARGET_STATUSES: [
    { value: 'DRAFT', label: '草稿' },
    { value: 'ACTIVE', label: '激活' },
    { value: 'PAUSED', label: '暂停' },
    { value: 'COMPLETED', label: '完成' },
    { value: 'CANCELLED', label: '取消' }
  ],

  // 进度状态
  PROGRESS_STATUSES: [
    { value: 'ON_TRACK', label: '正常' },
    { value: 'AT_RISK', label: '风险' },
    { value: 'OFF_TRACK', label: '偏离' }
  ],

  // 优先级
  PRIORITIES: [
    { value: 'HIGH', label: '高' },
    { value: 'MEDIUM', label: '中' },
    { value: 'LOW', label: '低' }
  ],

  // 目标周期
  TARGET_PERIODS: [
    { value: 'ANNUAL', label: '年度' },
    { value: 'QUARTERLY', label: '季度' },
    { value: 'MONTHLY', label: '月度' },
    { value: 'WEEKLY', label: '周度' }
  ]
}

export default {
  queryTargetPage,
  createTarget,
  updateTarget,
  deleteTarget,
  getTargetById,
  decomposeTarget,
  trackTarget,
  adjustTarget,
  evaluateTarget,
  incentiveTarget,
  negotiateTarget,
  getTargetKnowledge,
  analyzeTarget,
  getTargetTree,
  getTargetDashboard,
  batchOperateTargets,
  importTargets,
  exportTargets,
  getTargetStatistics,
  getTargetProgressReport,
  copyTarget,
  getTargetTemplates,
  applyTargetTemplate,
  getTargetRecommendations,
  refreshTargetCache,
  targetManagementQuickActions,
  targetManagementUtils,
  targetManagementConstants
}
