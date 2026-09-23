import request from '@/utils/request'

/**
 * 考核方案配置API
 */

// 分页查询考核方案
export function queryAssessmentPlanPage(params) {
  return request({
    url: '/accountant/pm/assessment-plan/page',
    method: 'get',
    params
  })
}

// 创建考核方案
export function createAssessmentPlan(data) {
  return request({
    url: '/accountant/pm/assessment-plan',
    method: 'post',
    data
  })
}

// 更新考核方案
export function updateAssessmentPlan(data) {
  return request({
    url: '/accountant/pm/assessment-plan',
    method: 'put',
    data
  })
}

// 删除考核方案
export function deleteAssessmentPlan(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}`,
    method: 'delete'
  })
}

// 根据ID查询考核方案详情
export function getAssessmentPlanById(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}`,
    method: 'get'
  })
}

// 配置考核指标
export function configureIndicators(planId, indicatorConfig) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/indicators`,
    method: 'post',
    data: indicatorConfig
  })
}

// 设置评分规则
export function setScoringRules(planId, scoringRules) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/scoring-rules`,
    method: 'post',
    data: scoringRules
  })
}

// 配置考核流程
export function configureProcess(planId, processConfig) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/process`,
    method: 'post',
    data: processConfig
  })
}

// 设置权重配置
export function setWeightConfig(planId, weightConfig) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/weights`,
    method: 'post',
    data: weightConfig
  })
}

// 激活考核方案
export function activateAssessmentPlan(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/activate`,
    method: 'post'
  })
}

// 暂停考核方案
export function pauseAssessmentPlan(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/pause`,
    method: 'post'
  })
}

// 完成考核方案
export function completeAssessmentPlan(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/complete`,
    method: 'post'
  })
}

// 复制考核方案
export function copyAssessmentPlan(planId, copyParams) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/copy`,
    method: 'post',
    data: copyParams
  })
}

// 获取方案模板
export function getAssessmentPlanTemplates(params) {
  return request({
    url: '/accountant/pm/assessment-plan/templates',
    method: 'get',
    params
  })
}

// 应用方案模板
export function applyAssessmentPlanTemplate(templateParams) {
  return request({
    url: '/accountant/pm/assessment-plan/templates/apply',
    method: 'post',
    data: templateParams
  })
}

// 获取方案统计
export function getAssessmentPlanStatistics(params) {
  return request({
    url: '/accountant/pm/assessment-plan/statistics',
    method: 'get',
    params
  })
}

// 批量操作方案
export function batchOperateAssessmentPlans(batchData) {
  return request({
    url: '/accountant/pm/assessment-plan/batch-operation',
    method: 'post',
    data: batchData
  })
}

// 导入方案
export function importAssessmentPlans(importData) {
  return request({
    url: '/accountant/pm/assessment-plan/import',
    method: 'post',
    data: importData
  })
}

// 导出方案
export function exportAssessmentPlans(exportParams) {
  return request({
    url: '/accountant/pm/assessment-plan/export',
    method: 'post',
    data: exportParams
  })
}

// 验证方案配置
export function validateAssessmentPlan(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/validate`,
    method: 'post'
  })
}

// 获取方案预览
export function previewAssessmentPlan(planId) {
  return request({
    url: `/accountant/pm/assessment-plan/${planId}/preview`,
    method: 'get'
  })
}

// 刷新方案缓存
export function refreshAssessmentPlanCache(params) {
  return request({
    url: '/accountant/pm/assessment-plan/refresh-cache',
    method: 'post',
    params
  })
}

// 考核方案配置快捷操作
export const assessmentPlanQuickActions = {
  // 快速创建年度考核方案
  quickCreateAnnualPlan(planName, organizationId, assessmentYear) {
    return createAssessmentPlan({
      planName,
      planType: 'ANNUAL',
      assessmentMode: 'INDIVIDUAL',
      assessmentCycle: 'ANNUAL',
      assessmentYear,
      organizationId,
      planStatus: 'DRAFT',
      approvalStatus: 'PENDING',
      scoringMethod: 'SCORE',
      totalScore: 100,
      passScore: 60,
      excellentScore: 90,
      isEnabled: 1
    })
  },

  // 快速创建季度考核方案
  quickCreateQuarterlyPlan(planName, organizationId, assessmentYear) {
    return createAssessmentPlan({
      planName,
      planType: 'QUARTERLY',
      assessmentMode: 'INDIVIDUAL',
      assessmentCycle: 'QUARTERLY',
      assessmentYear,
      organizationId,
      planStatus: 'DRAFT',
      approvalStatus: 'PENDING',
      scoringMethod: 'SCORE',
      totalScore: 100,
      passScore: 60,
      excellentScore: 90,
      isEnabled: 1
    })
  },

  // 快速创建团队考核方案
  quickCreateTeamPlan(planName, organizationId, assessmentYear) {
    return createAssessmentPlan({
      planName,
      planType: 'ANNUAL',
      assessmentMode: 'TEAM',
      assessmentCycle: 'ANNUAL',
      assessmentYear,
      organizationId,
      planStatus: 'DRAFT',
      approvalStatus: 'PENDING',
      scoringMethod: 'SCORE',
      totalScore: 100,
      passScore: 60,
      excellentScore: 90,
      isEnabled: 1
    })
  },

  // 快速配置360度评估权重
  quickSet360Weights(planId) {
    return setWeightConfig(planId, {
      selfEvaluationWeight: 20,
      superiorEvaluationWeight: 40,
      peerEvaluationWeight: 20,
      subordinateEvaluationWeight: 10,
      customerEvaluationWeight: 10
    })
  },

  // 快速配置标准评分规则
  quickSetStandardScoring(planId) {
    return setScoringRules(planId, {
      scoringMethod: 'SCORE',
      totalScore: 100,
      passScore: 60,
      excellentScore: 90,
      gradeSettings: {
        'A': { min: 90, max: 100, label: '优秀' },
        'B': { min: 80, max: 89, label: '良好' },
        'C': { min: 70, max: 79, label: '合格' },
        'D': { min: 60, max: 69, label: '基本合格' },
        'E': { min: 0, max: 59, label: '不合格' }
      }
    })
  },

  // 快速获取我的考核方案
  quickGetMyPlans(organizationId) {
    return queryAssessmentPlanPage({
      current: 1,
      size: 20,
      organizationId,
      planStatus: 'ACTIVE'
    })
  },

  // 快速获取方案概览
  quickGetPlanOverview(organizationId) {
    return getAssessmentPlanStatistics({
      organizationId,
      statisticsType: 'OVERVIEW'
    })
  }
}

// 考核方案配置工具函数
export const assessmentPlanUtils = {
  // 格式化方案状态
  formatPlanStatus(status) {
    const statusMap = {
      'DRAFT': { text: '草稿', color: '#909399' },
      'ACTIVE': { text: '激活', color: '#409EFF' },
      'PAUSED': { text: '暂停', color: '#E6A23C' },
      'COMPLETED': { text: '完成', color: '#67C23A' },
      'CANCELLED': { text: '取消', color: '#F56C6C' }
    }
    return statusMap[status] || { text: status, color: '#909399' }
  },

  // 格式化审批状态
  formatApprovalStatus(status) {
    const statusMap = {
      'PENDING': { text: '待审批', color: '#E6A23C' },
      'APPROVED': { text: '已审批', color: '#67C23A' },
      'REJECTED': { text: '已拒绝', color: '#F56C6C' }
    }
    return statusMap[status] || { text: status, color: '#909399' }
  },

  // 格式化方案类型
  formatPlanType(type) {
    const typeMap = {
      'ANNUAL': '年度考核',
      'QUARTERLY': '季度考核',
      'MONTHLY': '月度考核',
      'PROJECT': '项目考核'
    }
    return typeMap[type] || type
  },

  // 格式化考核模式
  formatAssessmentMode(mode) {
    const modeMap = {
      'INDIVIDUAL': '个人考核',
      'TEAM': '团队考核',
      'DEPARTMENT': '部门考核',
      'COMPANY': '公司考核'
    }
    return modeMap[mode] || mode
  },

  // 格式化考核周期
  formatAssessmentCycle(cycle) {
    const cycleMap = {
      'ANNUAL': '年度',
      'QUARTERLY': '季度',
      'MONTHLY': '月度',
      'WEEKLY': '周度'
    }
    return cycleMap[cycle] || cycle
  },

  // 格式化评分方式
  formatScoringMethod(method) {
    const methodMap = {
      'SCORE': '分数制',
      'GRADE': '等级制',
      'RANKING': '排名制'
    }
    return methodMap[method] || method
  },

  // 格式化适用范围
  formatApplicableScope(scope) {
    const scopeMap = {
      'ALL': '全员',
      'DEPARTMENT': '部门',
      'POSITION': '岗位',
      'INDIVIDUAL': '个人'
    }
    return scopeMap[scope] || scope
  },

  // 计算权重总和
  calculateTotalWeight(weights) {
    let total = 0
    Object.values(weights).forEach(weight => {
      if (typeof weight === 'number') {
        total += weight
      }
    })
    return total
  },

  // 验证权重配置
  validateWeights(weights) {
    const total = this.calculateTotalWeight(weights)
    return {
      isValid: total === 100,
      total,
      message: total === 100 ? '权重配置正确' : `权重总和为${total}%，应为100%`
    }
  },

  // 验证方案配置
  validatePlanConfig(plan) {
    const errors = []
    
    if (!plan.planName) {
      errors.push('方案名称不能为空')
    }
    
    if (!plan.planType) {
      errors.push('方案类型不能为空')
    }
    
    if (!plan.assessmentMode) {
      errors.push('考核模式不能为空')
    }
    
    if (!plan.organizationId) {
      errors.push('组织不能为空')
    }
    
    if (plan.startTime && plan.endTime && new Date(plan.startTime) >= new Date(plan.endTime)) {
      errors.push('开始时间必须早于结束时间')
    }
    
    if (plan.totalScore && plan.totalScore <= 0) {
      errors.push('总分必须大于0')
    }
    
    if (plan.passScore && plan.excellentScore && plan.passScore >= plan.excellentScore) {
      errors.push('及格分数必须小于优秀分数')
    }
    
    return {
      isValid: errors.length === 0,
      errors
    }
  },

  // 生成方案建议
  generatePlanSuggestions(planData) {
    const suggestions = []
    
    if (!planData.indicatorSystemId) {
      suggestions.push({
        type: 'INDICATOR',
        title: '建议配置指标体系',
        description: '完善的指标体系有助于准确评估绩效',
        priority: 'HIGH'
      })
    }
    
    if (!planData.processId) {
      suggestions.push({
        type: 'PROCESS',
        title: '建议配置考核流程',
        description: '标准化的流程确保考核的公平性',
        priority: 'HIGH'
      })
    }
    
    if (!planData.weightConfig) {
      suggestions.push({
        type: 'WEIGHT',
        title: '建议设置评估权重',
        description: '合理的权重分配提高评估准确性',
        priority: 'MEDIUM'
      })
    }
    
    if (!planData.enable360Evaluation) {
      suggestions.push({
        type: '360_EVALUATION',
        title: '建议启用360度评估',
        description: '多角度评估提供更全面的绩效反馈',
        priority: 'MEDIUM'
      })
    }
    
    return suggestions
  },

  // 分析方案健康度
  analyzePlanHealth(plan) {
    const health = {
      score: 100,
      level: 'EXCELLENT',
      issues: []
    }
    
    // 检查基础配置
    if (!plan.indicatorSystemId) {
      health.score -= 20
      health.issues.push('缺少指标体系配置')
    }
    
    if (!plan.processId) {
      health.score -= 15
      health.issues.push('缺少流程配置')
    }
    
    if (!plan.weightConfig) {
      health.score -= 15
      health.issues.push('缺少权重配置')
    }
    
    // 检查时间配置
    if (!plan.startTime || !plan.endTime) {
      health.score -= 10
      health.issues.push('缺少时间配置')
    }
    
    // 检查适用范围
    if (!plan.applicableScope || !plan.applicableTargets) {
      health.score -= 10
      health.issues.push('适用范围配置不完整')
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

  // 生成方案报告
  generatePlanReport(plans, period) {
    const report = {
      period,
      summary: {
        totalPlans: plans.length,
        activePlans: 0,
        completedPlans: 0,
        draftPlans: 0,
        pausedPlans: 0
      },
      trends: [],
      recommendations: []
    }
    
    plans.forEach(plan => {
      switch (plan.planStatus) {
        case 'ACTIVE':
          report.summary.activePlans++
          break
        case 'COMPLETED':
          report.summary.completedPlans++
          break
        case 'DRAFT':
          report.summary.draftPlans++
          break
        case 'PAUSED':
          report.summary.pausedPlans++
          break
      }
    })
    
    // 生成建议
    if (report.summary.draftPlans > report.summary.activePlans) {
      report.recommendations.push('建议激活更多草稿方案以提高考核覆盖率')
    }
    
    if (report.summary.pausedPlans > 0) {
      report.recommendations.push('关注暂停的方案，及时恢复或调整')
    }
    
    if (report.summary.activePlans === 0) {
      report.recommendations.push('当前没有激活的考核方案，建议尽快启动考核')
    }
    
    return report
  }
}

// 考核方案配置常量
export const assessmentPlanConstants = {
  // 方案类型
  PLAN_TYPES: [
    { value: 'ANNUAL', label: '年度考核' },
    { value: 'QUARTERLY', label: '季度考核' },
    { value: 'MONTHLY', label: '月度考核' },
    { value: 'PROJECT', label: '项目考核' }
  ],

  // 考核模式
  ASSESSMENT_MODES: [
    { value: 'INDIVIDUAL', label: '个人考核' },
    { value: 'TEAM', label: '团队考核' },
    { value: 'DEPARTMENT', label: '部门考核' },
    { value: 'COMPANY', label: '公司考核' }
  ],

  // 考核周期
  ASSESSMENT_CYCLES: [
    { value: 'ANNUAL', label: '年度' },
    { value: 'QUARTERLY', label: '季度' },
    { value: 'MONTHLY', label: '月度' },
    { value: 'WEEKLY', label: '周度' }
  ],

  // 方案状态
  PLAN_STATUSES: [
    { value: 'DRAFT', label: '草稿' },
    { value: 'ACTIVE', label: '激活' },
    { value: 'PAUSED', label: '暂停' },
    { value: 'COMPLETED', label: '完成' },
    { value: 'CANCELLED', label: '取消' }
  ],

  // 审批状态
  APPROVAL_STATUSES: [
    { value: 'PENDING', label: '待审批' },
    { value: 'APPROVED', label: '已审批' },
    { value: 'REJECTED', label: '已拒绝' }
  ],

  // 评分方式
  SCORING_METHODS: [
    { value: 'SCORE', label: '分数制' },
    { value: 'GRADE', label: '等级制' },
    { value: 'RANKING', label: '排名制' }
  ],

  // 适用范围
  APPLICABLE_SCOPES: [
    { value: 'ALL', label: '全员' },
    { value: 'DEPARTMENT', label: '部门' },
    { value: 'POSITION', label: '岗位' },
    { value: 'INDIVIDUAL', label: '个人' }
  ],

  // 结果应用
  RESULT_APPLICATIONS: [
    { value: 'PROMOTION', label: '晋升' },
    { value: 'SALARY', label: '薪酬' },
    { value: 'TRAINING', label: '培训' },
    { value: 'DEVELOPMENT', label: '发展' }
  ],

  // 结果公开范围
  RESULT_VISIBILITIES: [
    { value: 'PRIVATE', label: '私有' },
    { value: 'DEPARTMENT', label: '部门' },
    { value: 'COMPANY', label: '公司' }
  ]
}

export default {
  queryAssessmentPlanPage,
  createAssessmentPlan,
  updateAssessmentPlan,
  deleteAssessmentPlan,
  getAssessmentPlanById,
  configureIndicators,
  setScoringRules,
  configureProcess,
  setWeightConfig,
  activateAssessmentPlan,
  pauseAssessmentPlan,
  completeAssessmentPlan,
  copyAssessmentPlan,
  getAssessmentPlanTemplates,
  applyAssessmentPlanTemplate,
  getAssessmentPlanStatistics,
  batchOperateAssessmentPlans,
  importAssessmentPlans,
  exportAssessmentPlans,
  validateAssessmentPlan,
  previewAssessmentPlan,
  refreshAssessmentPlanCache,
  assessmentPlanQuickActions,
  assessmentPlanUtils,
  assessmentPlanConstants
}
