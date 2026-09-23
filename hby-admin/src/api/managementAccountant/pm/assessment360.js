import request from '@/utils/request'

/**
 * 360度评估API
 */

// 分页查询360度评估
export function query360AssessmentPage(params) {
  return request({
    url: '/accountant/pm/360-assessment/page',
    method: 'get',
    params
  })
}

// 创建360度评估
export function create360Assessment(data) {
  return request({
    url: '/accountant/pm/360-assessment',
    method: 'post',
    data
  })
}

// 更新360度评估
export function update360Assessment(data) {
  return request({
    url: '/accountant/pm/360-assessment',
    method: 'put',
    data
  })
}

// 删除360度评估
export function delete360Assessment(assessmentId) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}`,
    method: 'delete'
  })
}

// 根据ID查询360度评估详情
export function get360AssessmentById(assessmentId) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}`,
    method: 'get'
  })
}

// 启动评估
export function startAssessment(assessmentId, startParams) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/start`,
    method: 'post',
    data: startParams
  })
}

// 完成评估
export function completeAssessment(assessmentId, completeParams) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/complete`,
    method: 'post',
    data: completeParams
  })
}

// 取消评估
export function cancelAssessment(assessmentId, cancelParams) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/cancel`,
    method: 'post',
    data: cancelParams
  })
}

// 提交自评
export function submitSelfEvaluation(assessmentId, evaluationData) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/self-evaluation`,
    method: 'post',
    data: evaluationData
  })
}

// 提交上级评价
export function submitSuperiorEvaluation(assessmentId, evaluationData) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/superior-evaluation`,
    method: 'post',
    data: evaluationData
  })
}

// 提交同级评价
export function submitPeerEvaluation(assessmentId, evaluationData) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/peer-evaluation`,
    method: 'post',
    data: evaluationData
  })
}

// 提交下级评价
export function submitSubordinateEvaluation(assessmentId, evaluationData) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/subordinate-evaluation`,
    method: 'post',
    data: evaluationData
  })
}

// 提交客户评价
export function submitCustomerEvaluation(assessmentId, evaluationData) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/customer-evaluation`,
    method: 'post',
    data: evaluationData
  })
}

// 计算评估结果
export function calculateAssessmentResult(assessmentId, calculateParams) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/calculate`,
    method: 'post',
    data: calculateParams
  })
}

// 生成评估报告
export function generateAssessmentReport(assessmentId, reportParams) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/report`,
    method: 'post',
    data: reportParams
  })
}

// 获取评估统计
export function getAssessmentStatistics(params) {
  return request({
    url: '/accountant/pm/360-assessment/statistics',
    method: 'get',
    params
  })
}

// 获取评估分析
export function getAssessmentAnalysis(assessmentId, params) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/analysis`,
    method: 'get',
    params
  })
}

// 批量操作评估
export function batchOperateAssessments(batchData) {
  return request({
    url: '/accountant/pm/360-assessment/batch-operation',
    method: 'post',
    data: batchData
  })
}

// 导入评估数据
export function importAssessments(importData) {
  return request({
    url: '/accountant/pm/360-assessment/import',
    method: 'post',
    data: importData
  })
}

// 导出评估数据
export function exportAssessments(exportParams) {
  return request({
    url: '/accountant/pm/360-assessment/export',
    method: 'post',
    data: exportParams
  })
}

// 复制评估
export function copyAssessment(assessmentId, copyParams) {
  return request({
    url: `/accountant/pm/360-assessment/${assessmentId}/copy`,
    method: 'post',
    data: copyParams
  })
}

// 获取评估模板
export function getAssessmentTemplates(params) {
  return request({
    url: '/accountant/pm/360-assessment/templates',
    method: 'get',
    params
  })
}

// 应用评估模板
export function applyAssessmentTemplate(templateParams) {
  return request({
    url: '/accountant/pm/360-assessment/templates/apply',
    method: 'post',
    data: templateParams
  })
}

// 刷新评估缓存
export function refreshAssessmentCache(params) {
  return request({
    url: '/accountant/pm/360-assessment/refresh-cache',
    method: 'post',
    params
  })
}

// 360度评估快捷操作
export const assessment360QuickActions = {
  // 快速创建年度评估
  quickCreateAnnualAssessment(assessedUserId, assessedUserName, organizationId) {
    return create360Assessment({
      assessedUserId,
      assessedUserName,
      assessmentType: 'ANNUAL',
      assessmentYear: new Date().getFullYear(),
      assessmentStatus: 'DRAFT',
      organizationId,
      assessmentName: `${assessedUserName}年度360度评估`,
      selfWeight: 0.2,
      superiorWeight: 0.4,
      peerWeight: 0.2,
      subordinateWeight: 0.1,
      customerWeight: 0.1,
      isEnabled: 1,
      isVisible: 1
    })
  },

  // 快速创建季度评估
  quickCreateQuarterlyAssessment(assessedUserId, assessedUserName, organizationId, quarter) {
    return create360Assessment({
      assessedUserId,
      assessedUserName,
      assessmentType: 'QUARTERLY',
      assessmentYear: new Date().getFullYear(),
      assessmentQuarter: quarter,
      assessmentStatus: 'DRAFT',
      organizationId,
      assessmentName: `${assessedUserName}Q${quarter}季度360度评估`,
      selfWeight: 0.3,
      superiorWeight: 0.4,
      peerWeight: 0.2,
      subordinateWeight: 0.1,
      customerWeight: 0.0,
      isEnabled: 1,
      isVisible: 1
    })
  },

  // 快速提交评价
  quickSubmitEvaluation(assessmentId, evaluationType, score, feedback) {
    const evaluationData = {
      score,
      feedback,
      evaluationTime: new Date(),
      evaluator: 'current_user'
    }

    switch (evaluationType) {
      case 'SELF':
        return submitSelfEvaluation(assessmentId, evaluationData)
      case 'SUPERIOR':
        return submitSuperiorEvaluation(assessmentId, evaluationData)
      case 'PEER':
        return submitPeerEvaluation(assessmentId, evaluationData)
      case 'SUBORDINATE':
        return submitSubordinateEvaluation(assessmentId, evaluationData)
      case 'CUSTOMER':
        return submitCustomerEvaluation(assessmentId, evaluationData)
      default:
        throw new Error('不支持的评价类型')
    }
  },

  // 快速启动评估
  quickStartAssessment(assessmentId) {
    return startAssessment(assessmentId, {
      startReason: '评估启动',
      notifyParticipants: true,
      startUser: 'current_user'
    })
  },

  // 快速完成评估
  quickCompleteAssessment(assessmentId) {
    return completeAssessment(assessmentId, {
      completeReason: '评估完成',
      generateReport: true,
      completeUser: 'current_user'
    })
  },

  // 快速获取我的评估任务
  quickGetMyAssessmentTasks(userId) {
    return query360AssessmentPage({
      current: 1,
      size: 20,
      assessedUserId: userId,
      assessmentStatus: 'ONGOING'
    })
  },

  // 快速获取待评估列表
  quickGetPendingEvaluations(userId) {
    return query360AssessmentPage({
      current: 1,
      size: 20,
      evaluatorId: userId,
      evaluationStatus: 'PENDING'
    })
  }
}

// 360度评估工具函数
export const assessment360Utils = {
  // 格式化评估状态
  formatAssessmentStatus(status) {
    const statusMap = {
      'DRAFT': { text: '草稿', color: '#909399' },
      'ONGOING': { text: '进行中', color: '#409EFF' },
      'COMPLETED': { text: '已完成', color: '#67C23A' },
      'CANCELLED': { text: '已取消', color: '#F56C6C' }
    }
    return statusMap[status] || { text: status, color: '#909399' }
  },

  // 格式化评估类型
  formatAssessmentType(type) {
    const typeMap = {
      'ANNUAL': '年度评估',
      'QUARTERLY': '季度评估',
      'MONTHLY': '月度评估',
      'PROJECT': '项目评估'
    }
    return typeMap[type] || type
  },

  // 格式化评估等级
  formatAssessmentGrade(grade) {
    const gradeMap = {
      'EXCELLENT': { text: '优秀', color: '#67C23A' },
      'GOOD': { text: '良好', color: '#409EFF' },
      'FAIR': { text: '一般', color: '#E6A23C' },
      'POOR': { text: '较差', color: '#F56C6C' }
    }
    return gradeMap[grade] || { text: grade, color: '#909399' }
  },

  // 计算评估完成率
  calculateCompletionRate(assessment) {
    let completedCount = 0
    let totalCount = 0

    if (assessment.selfWeight > 0) {
      totalCount++
      if (assessment.selfScore) completedCount++
    }
    if (assessment.superiorWeight > 0) {
      totalCount++
      if (assessment.superiorScore) completedCount++
    }
    if (assessment.peerWeight > 0) {
      totalCount++
      if (assessment.peerScore) completedCount++
    }
    if (assessment.subordinateWeight > 0) {
      totalCount++
      if (assessment.subordinateScore) completedCount++
    }
    if (assessment.customerWeight > 0) {
      totalCount++
      if (assessment.customerScore) completedCount++
    }

    return totalCount > 0 ? ((completedCount / totalCount) * 100).toFixed(2) : 0
  },

  // 获取评估进度状态
  getProgressStatus(completionRate) {
    if (completionRate >= 100) return 'success'
    if (completionRate >= 80) return 'warning'
    if (completionRate >= 50) return 'info'
    return 'danger'
  },

  // 验证评估数据
  validateAssessmentData(assessment) {
    const errors = []

    if (!assessment.assessmentName) {
      errors.push('评估名称不能为空')
    }

    if (!assessment.assessedUserId) {
      errors.push('被评估人不能为空')
    }

    if (!assessment.assessmentType) {
      errors.push('评估类型不能为空')
    }

    if (!assessment.startTime || !assessment.endTime) {
      errors.push('评估时间不能为空')
    }

    if (assessment.startTime && assessment.endTime && 
        new Date(assessment.startTime) >= new Date(assessment.endTime)) {
      errors.push('开始时间必须早于结束时间')
    }

    // 验证权重总和
    const totalWeight = (assessment.selfWeight || 0) + 
                       (assessment.superiorWeight || 0) + 
                       (assessment.peerWeight || 0) + 
                       (assessment.subordinateWeight || 0) + 
                       (assessment.customerWeight || 0)
    
    if (Math.abs(totalWeight - 1) > 0.01) {
      errors.push('权重总和必须等于1')
    }

    return {
      isValid: errors.length === 0,
      errors
    }
  },

  // 生成评估建议
  generateAssessmentSuggestions(assessment) {
    const suggestions = []

    if (!assessment.assessmentDescription) {
      suggestions.push({
        type: 'DESCRIPTION',
        title: '建议添加评估描述',
        description: '详细的评估描述有助于参与者理解评估目标',
        priority: 'MEDIUM'
      })
    }

    if (!assessment.assessmentCriteria) {
      suggestions.push({
        type: 'CRITERIA',
        title: '建议设置评估标准',
        description: '明确的评估标准有助于提高评估的客观性',
        priority: 'HIGH'
      })
    }

    const completionRate = this.calculateCompletionRate(assessment)
    if (completionRate < 50) {
      suggestions.push({
        type: 'PROGRESS',
        title: '评估进度偏慢',
        description: '建议跟进评估进度，确保按时完成',
        priority: 'HIGH'
      })
    }

    return suggestions
  },

  // 分析评估健康度
  analyzeAssessmentHealth(assessment) {
    const health = {
      score: 100,
      level: 'EXCELLENT',
      issues: []
    }

    // 检查完成率
    const completionRate = this.calculateCompletionRate(assessment)
    if (completionRate < 50) {
      health.score -= 30
      health.issues.push('完成率偏低')
    }

    // 检查时间进度
    if (assessment.startTime && assessment.endTime) {
      const now = new Date()
      const start = new Date(assessment.startTime)
      const end = new Date(assessment.endTime)
      const totalTime = end - start
      const elapsedTime = now - start
      const timeProgress = (elapsedTime / totalTime) * 100

      if (timeProgress > completionRate + 20) {
        health.score -= 25
        health.issues.push('时间进度超前于完成进度')
      }
    }

    // 检查参与度
    if (assessment.participantCount && assessment.completedCount) {
      const participationRate = (assessment.completedCount / assessment.participantCount) * 100
      if (participationRate < 70) {
        health.score -= 20
        health.issues.push('参与度偏低')
      }
    }

    // 确定健康等级
    if (health.score >= 90) {
      health.level = 'EXCELLENT'
    } else if (health.score >= 80) {
      health.level = 'GOOD'
    } else if (health.score >= 70) {
      health.level = 'FAIR'
    } else {
      health.level = 'POOR'
    }

    return health
  }
}

// 360度评估常量
export const assessment360Constants = {
  // 评估类型
  ASSESSMENT_TYPES: [
    { value: 'ANNUAL', label: '年度评估' },
    { value: 'QUARTERLY', label: '季度评估' },
    { value: 'MONTHLY', label: '月度评估' },
    { value: 'PROJECT', label: '项目评估' }
  ],

  // 评估状态
  ASSESSMENT_STATUSES: [
    { value: 'DRAFT', label: '草稿' },
    { value: 'ONGOING', label: '进行中' },
    { value: 'COMPLETED', label: '已完成' },
    { value: 'CANCELLED', label: '已取消' }
  ],

  // 评估等级
  ASSESSMENT_GRADES: [
    { value: 'EXCELLENT', label: '优秀' },
    { value: 'GOOD', label: '良好' },
    { value: 'FAIR', label: '一般' },
    { value: 'POOR', label: '较差' }
  ],

  // 评价维度
  EVALUATION_DIMENSIONS: [
    { value: 'SELF', label: '自评', weight: 0.2 },
    { value: 'SUPERIOR', label: '上级评价', weight: 0.4 },
    { value: 'PEER', label: '同级评价', weight: 0.2 },
    { value: 'SUBORDINATE', label: '下级评价', weight: 0.1 },
    { value: 'CUSTOMER', label: '客户评价', weight: 0.1 }
  ]
}

export default {
  query360AssessmentPage,
  create360Assessment,
  update360Assessment,
  delete360Assessment,
  get360AssessmentById,
  startAssessment,
  completeAssessment,
  cancelAssessment,
  submitSelfEvaluation,
  submitSuperiorEvaluation,
  submitPeerEvaluation,
  submitSubordinateEvaluation,
  submitCustomerEvaluation,
  calculateAssessmentResult,
  generateAssessmentReport,
  getAssessmentStatistics,
  getAssessmentAnalysis,
  batchOperateAssessments,
  importAssessments,
  exportAssessments,
  copyAssessment,
  getAssessmentTemplates,
  applyAssessmentTemplate,
  refreshAssessmentCache,
  assessment360QuickActions,
  assessment360Utils,
  assessment360Constants
}
