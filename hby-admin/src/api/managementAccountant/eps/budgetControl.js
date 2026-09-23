import request from '@/utils/request'

/**
 * 预算控制管理API
 */

// 分页查询预算控制规则
export function queryBudgetControlPage(params) {
  return request({
    url: '/accountant/eps/budget-control/page',
    method: 'get',
    params
  })
}

// 创建预算控制规则
export function createBudgetControl(data) {
  return request({
    url: '/accountant/eps/budget-control',
    method: 'post',
    data
  })
}

// 更新预算控制规则
export function updateBudgetControl(data) {
  return request({
    url: '/accountant/eps/budget-control',
    method: 'put',
    data
  })
}

// 删除预算控制规则
export function deleteBudgetControl(controlId) {
  return request({
    url: `/accountant/eps/budget-control/${controlId}`,
    method: 'delete'
  })
}

// 根据ID查询预算控制规则详情
export function getBudgetControlById(controlId) {
  return request({
    url: `/accountant/eps/budget-control/${controlId}`,
    method: 'get'
  })
}

// 启用预算控制规则
export function enableBudgetControl(controlId) {
  return request({
    url: `/accountant/eps/budget-control/${controlId}/enable`,
    method: 'post'
  })
}

// 禁用预算控制规则
export function disableBudgetControl(controlId) {
  return request({
    url: `/accountant/eps/budget-control/${controlId}/disable`,
    method: 'post'
  })
}

// 执行预算控制检查
export function executeBudgetControlCheck(checkParams) {
  return request({
    url: '/accountant/eps/budget-control/check',
    method: 'post',
    data: checkParams
  })
}

// 获取预算控制监控数据
export function getBudgetControlMonitor(params) {
  return request({
    url: '/accountant/eps/budget-control/monitor',
    method: 'get',
    params
  })
}

// 获取预算控制预警信息
export function getBudgetControlAlerts(params) {
  return request({
    url: '/accountant/eps/budget-control/alerts',
    method: 'get',
    params
  })
}

// 处理预算控制预警
export function handleBudgetControlAlert(alertId, handleParams) {
  return request({
    url: `/accountant/eps/budget-control/alerts/${alertId}/handle`,
    method: 'post',
    data: handleParams
  })
}

// 获取预算控制仪表板
export function getBudgetControlDashboard(params) {
  return request({
    url: '/accountant/eps/budget-control/dashboard',
    method: 'get',
    params
  })
}

// 批量操作预算控制规则
export function batchOperateBudgetControl(batchData) {
  return request({
    url: '/accountant/eps/budget-control/batch-operation',
    method: 'post',
    data: batchData
  })
}

// 导入预算控制规则
export function importBudgetControl(importData) {
  return request({
    url: '/accountant/eps/budget-control/import',
    method: 'post',
    data: importData
  })
}

// 导出预算控制规则
export function exportBudgetControl(exportParams) {
  return request({
    url: '/accountant/eps/budget-control/export',
    method: 'post',
    data: exportParams
  })
}

// 获取预算控制统计
export function getBudgetControlStatistics(params) {
  return request({
    url: '/accountant/eps/budget-control/statistics',
    method: 'get',
    params
  })
}

// 获取预算控制历史
export function getBudgetControlHistory(params) {
  return request({
    url: '/accountant/eps/budget-control/history',
    method: 'get',
    params
  })
}

// 测试预算控制规则
export function testBudgetControl(testParams) {
  return request({
    url: '/accountant/eps/budget-control/test',
    method: 'post',
    data: testParams
  })
}

// 复制预算控制规则
export function copyBudgetControl(controlId, copyParams) {
  return request({
    url: `/accountant/eps/budget-control/${controlId}/copy`,
    method: 'post',
    data: copyParams
  })
}

// 获取预算控制模板
export function getBudgetControlTemplates(params) {
  return request({
    url: '/accountant/eps/budget-control/templates',
    method: 'get',
    params
  })
}

// 应用预算控制模板
export function applyBudgetControlTemplate(templateParams) {
  return request({
    url: '/accountant/eps/budget-control/templates/apply',
    method: 'post',
    data: templateParams
  })
}

// 获取预算控制建议
export function getBudgetControlRecommendations(params) {
  return request({
    url: '/accountant/eps/budget-control/recommendations',
    method: 'get',
    params
  })
}

// 优化预算控制策略
export function optimizeBudgetControlStrategy(optimizeParams) {
  return request({
    url: '/accountant/eps/budget-control/optimize',
    method: 'post',
    data: optimizeParams
  })
}

// 刷新预算控制缓存
export function refreshBudgetControlCache(params) {
  return request({
    url: '/accountant/eps/budget-control/refresh-cache',
    method: 'post',
    params
  })
}

// 预算控制快捷操作
export const budgetControlQuickActions = {
  // 快速创建金额控制规则
  quickCreateAmountControl(versionId, organizationId, subjectId, budgetAmount, controlThreshold = 90, warningThreshold = 80) {
    return createBudgetControl({
      versionId,
      organizationId,
      subjectId,
      controlName: `金额控制规则_${Date.now()}`,
      controlType: 'AMOUNT',
      controlLevel: 'STRICT',
      controlScope: 'SUBJECT',
      budgetAmount,
      controlThreshold,
      warningThreshold,
      isEnabled: 1,
      status: 'ACTIVE'
    })
  },

  // 快速创建比率控制规则
  quickCreateRatioControl(versionId, organizationId, controlThreshold = 100, warningThreshold = 90) {
    return createBudgetControl({
      versionId,
      organizationId,
      controlName: `比率控制规则_${Date.now()}`,
      controlType: 'RATE',
      controlLevel: 'WARNING',
      controlScope: 'ORGANIZATION',
      controlThreshold,
      warningThreshold,
      isEnabled: 1,
      status: 'ACTIVE'
    })
  },

  // 快速启用多个控制规则
  quickEnableMultipleControls(controlIds) {
    return batchOperateBudgetControl({
      operation: 'ENABLE',
      controlIds,
      operationParams: {}
    })
  },

  // 快速禁用多个控制规则
  quickDisableMultipleControls(controlIds) {
    return batchOperateBudgetControl({
      operation: 'DISABLE',
      controlIds,
      operationParams: {}
    })
  },

  // 快速检查预算控制
  quickBudgetCheck(versionId, organizationId, subjectId, amount) {
    return executeBudgetControlCheck({
      versionId,
      organizationId,
      subjectId,
      amount,
      operationType: 'EXPENSE'
    })
  },

  // 快速获取控制概览
  quickGetControlOverview(versionId, organizationId) {
    return getBudgetControlDashboard({
      versionId,
      organizationId,
      dashboardType: 'OVERVIEW'
    })
  },

  // 快速获取预警信息
  quickGetAlerts(versionId, organizationId) {
    return getBudgetControlAlerts({
      versionId,
      organizationId,
      alertLevel: 'HIGH',
      alertStatus: 'ACTIVE'
    })
  }
}

// 预算控制工具函数
export const budgetControlUtils = {
  // 格式化控制状态
  formatControlStatus(status) {
    const statusMap = {
      'NORMAL': { text: '正常', color: '#67C23A' },
      'WARNING': { text: '预警', color: '#E6A23C' },
      'EXCEEDED': { text: '超支', color: '#F56C6C' },
      'BLOCKED': { text: '阻止', color: '#909399' }
    }
    return statusMap[status] || { text: status, color: '#909399' }
  },

  // 格式化控制类型
  formatControlType(type) {
    const typeMap = {
      'AMOUNT': '金额控制',
      'QUANTITY': '数量控制',
      'RATE': '比率控制',
      'BALANCE': '余额控制'
    }
    return typeMap[type] || type
  },

  // 格式化控制级别
  formatControlLevel(level) {
    const levelMap = {
      'STRICT': '严格控制',
      'WARNING': '预警控制',
      'SOFT': '软控制'
    }
    return levelMap[level] || level
  },

  // 格式化控制范围
  formatControlScope(scope) {
    const scopeMap = {
      'ORGANIZATION': '组织',
      'SUBJECT': '科目',
      'PROJECT': '项目',
      'GLOBAL': '全局'
    }
    return scopeMap[scope] || scope
  },

  // 计算使用率
  calculateUsageRate(usedAmount, budgetAmount) {
    if (!budgetAmount || budgetAmount === 0) return 0
    return ((usedAmount / budgetAmount) * 100).toFixed(2)
  },

  // 计算可用金额
  calculateAvailableAmount(budgetAmount, usedAmount) {
    return (budgetAmount - usedAmount).toFixed(2)
  },

  // 获取使用率状态
  getUsageRateStatus(usageRate, warningThreshold = 80, controlThreshold = 90) {
    if (usageRate >= controlThreshold) return 'danger'
    if (usageRate >= warningThreshold) return 'warning'
    return 'success'
  },

  // 获取使用率颜色
  getUsageRateColor(usageRate, warningThreshold = 80, controlThreshold = 90) {
    if (usageRate >= controlThreshold) return '#F56C6C'
    if (usageRate >= warningThreshold) return '#E6A23C'
    return '#67C23A'
  },

  // 格式化金额
  formatAmount(amount) {
    if (!amount) return '0.00'
    return Number(amount).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  },

  // 验证控制规则
  validateControlRule(rule) {
    const errors = []
    
    if (!rule.controlName) {
      errors.push('控制规则名称不能为空')
    }
    
    if (!rule.controlType) {
      errors.push('控制类型不能为空')
    }
    
    if (!rule.controlLevel) {
      errors.push('控制级别不能为空')
    }
    
    if (rule.controlType === 'AMOUNT' && (!rule.budgetAmount || rule.budgetAmount <= 0)) {
      errors.push('金额控制必须设置有效的预算金额')
    }
    
    if (rule.controlThreshold && (rule.controlThreshold < 0 || rule.controlThreshold > 200)) {
      errors.push('控制阈值必须在0-200之间')
    }
    
    if (rule.warningThreshold && (rule.warningThreshold < 0 || rule.warningThreshold > 200)) {
      errors.push('预警阈值必须在0-200之间')
    }
    
    if (rule.controlThreshold && rule.warningThreshold && rule.warningThreshold >= rule.controlThreshold) {
      errors.push('预警阈值必须小于控制阈值')
    }
    
    return {
      isValid: errors.length === 0,
      errors
    }
  },

  // 生成控制规则建议
  generateControlRuleSuggestions(budgetData) {
    const suggestions = []
    
    if (budgetData.totalBudget > 1000000) {
      suggestions.push({
        type: 'AMOUNT',
        title: '建议设置金额控制',
        description: '预算金额较大，建议设置严格的金额控制规则',
        priority: 'HIGH'
      })
    }
    
    if (budgetData.subjectCount > 50) {
      suggestions.push({
        type: 'RATE',
        title: '建议设置比率控制',
        description: '科目数量较多，建议设置比率控制规则',
        priority: 'MEDIUM'
      })
    }
    
    if (budgetData.organizationCount > 10) {
      suggestions.push({
        type: 'ORGANIZATION',
        title: '建议设置组织级控制',
        description: '组织数量较多，建议设置组织级控制规则',
        priority: 'MEDIUM'
      })
    }
    
    return suggestions
  },

  // 分析控制效果
  analyzeControlEffectiveness(controlData) {
    const analysis = {
      totalRules: controlData.length,
      activeRules: 0,
      effectiveRules: 0,
      violationRate: 0,
      averageUsageRate: 0
    }
    
    let totalUsageRate = 0
    let totalViolations = 0
    
    controlData.forEach(rule => {
      if (rule.isEnabled) {
        analysis.activeRules++
      }
      
      if (rule.violationCount === 0) {
        analysis.effectiveRules++
      }
      
      totalUsageRate += rule.usageRate || 0
      totalViolations += rule.violationCount || 0
    })
    
    analysis.averageUsageRate = (totalUsageRate / controlData.length).toFixed(2)
    analysis.violationRate = ((totalViolations / controlData.length) * 100).toFixed(2)
    analysis.effectiveness = ((analysis.effectiveRules / analysis.totalRules) * 100).toFixed(2)
    
    return analysis
  },

  // 生成控制报告
  generateControlReport(controlData, period) {
    const report = {
      period,
      summary: this.analyzeControlEffectiveness(controlData),
      trends: [],
      recommendations: [],
      alerts: []
    }
    
    // 生成趋势分析
    report.trends = this.analyzeTrends(controlData)
    
    // 生成建议
    report.recommendations = this.generateRecommendations(controlData)
    
    // 生成预警
    report.alerts = this.generateAlerts(controlData)
    
    return report
  },

  // 分析趋势
  analyzeTrends(controlData) {
    // TODO: 实现趋势分析逻辑
    return []
  },

  // 生成建议
  generateRecommendations(controlData) {
    // TODO: 实现建议生成逻辑
    return []
  },

  // 生成预警
  generateAlerts(controlData) {
    const alerts = []
    
    controlData.forEach(rule => {
      if (rule.usageRate >= rule.controlThreshold) {
        alerts.push({
          level: 'HIGH',
          type: 'EXCEEDED',
          message: `${rule.controlName} 已超出控制阈值`,
          rule: rule
        })
      } else if (rule.usageRate >= rule.warningThreshold) {
        alerts.push({
          level: 'MEDIUM',
          type: 'WARNING',
          message: `${rule.controlName} 已超出预警阈值`,
          rule: rule
        })
      }
    })
    
    return alerts
  }
}

// 预算控制常量
export const budgetControlConstants = {
  // 控制类型
  CONTROL_TYPES: [
    { value: 'AMOUNT', label: '金额控制' },
    { value: 'QUANTITY', label: '数量控制' },
    { value: 'RATE', label: '比率控制' },
    { value: 'BALANCE', label: '余额控制' }
  ],

  // 控制级别
  CONTROL_LEVELS: [
    { value: 'STRICT', label: '严格控制' },
    { value: 'WARNING', label: '预警控制' },
    { value: 'SOFT', label: '软控制' }
  ],

  // 控制范围
  CONTROL_SCOPES: [
    { value: 'ORGANIZATION', label: '组织' },
    { value: 'SUBJECT', label: '科目' },
    { value: 'PROJECT', label: '项目' },
    { value: 'GLOBAL', label: '全局' }
  ],

  // 控制状态
  CONTROL_STATUSES: [
    { value: 'NORMAL', label: '正常' },
    { value: 'WARNING', label: '预警' },
    { value: 'EXCEEDED', label: '超支' },
    { value: 'BLOCKED', label: '阻止' }
  ],

  // 控制周期
  CONTROL_PERIODS: [
    { value: 'DAILY', label: '每日' },
    { value: 'WEEKLY', label: '每周' },
    { value: 'MONTHLY', label: '每月' },
    { value: 'QUARTERLY', label: '每季度' },
    { value: 'YEARLY', label: '每年' }
  ],

  // 预警级别
  ALERT_LEVELS: [
    { value: 'LOW', label: '低' },
    { value: 'MEDIUM', label: '中' },
    { value: 'HIGH', label: '高' },
    { value: 'CRITICAL', label: '严重' }
  ]
}

export default {
  queryBudgetControlPage,
  createBudgetControl,
  updateBudgetControl,
  deleteBudgetControl,
  getBudgetControlById,
  enableBudgetControl,
  disableBudgetControl,
  executeBudgetControlCheck,
  getBudgetControlMonitor,
  getBudgetControlAlerts,
  handleBudgetControlAlert,
  getBudgetControlDashboard,
  batchOperateBudgetControl,
  importBudgetControl,
  exportBudgetControl,
  getBudgetControlStatistics,
  getBudgetControlHistory,
  testBudgetControl,
  copyBudgetControl,
  getBudgetControlTemplates,
  applyBudgetControlTemplate,
  getBudgetControlRecommendations,
  optimizeBudgetControlStrategy,
  refreshBudgetControlCache,
  budgetControlQuickActions,
  budgetControlUtils,
  budgetControlConstants
}
