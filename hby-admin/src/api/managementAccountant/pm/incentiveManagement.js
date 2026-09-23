import request from '@/utils/request'

// 基础API路径
const BASE_URL = '/accountant/pm/incentive-management'

/**
 * 激励管理API
 */
export default {
  // 分页查询激励管理列表
  getIncentiveManagementPage(params) {
    return request({
      url: `${BASE_URL}/page`,
      method: 'get',
      params
    })
  },

  // 根据ID查询激励管理详情
  getIncentiveManagementById(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据激励编码查询
  getByIncentiveCode(incentiveCode, tenantId) {
    return request({
      url: `${BASE_URL}/code/${incentiveCode}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据激励类型查询列表
  getByIncentiveType(incentiveType, tenantId) {
    return request({
      url: `${BASE_URL}/type/${incentiveType}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据激励状态查询列表
  getByIncentiveStatus(incentiveStatus, tenantId) {
    return request({
      url: `${BASE_URL}/status/${incentiveStatus}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据部门ID查询激励列表
  getByDeptId(targetDeptId, tenantId) {
    return request({
      url: `${BASE_URL}/dept/${targetDeptId}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据负责人ID查询激励列表
  getByOwnerId(incentiveOwnerId, tenantId) {
    return request({
      url: `${BASE_URL}/owner/${incentiveOwnerId}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据年度查询激励列表
  getByYear(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/year/${incentiveYear}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据季度查询激励列表
  getByQuarter(incentiveYear, incentiveQuarter, tenantId) {
    return request({
      url: `${BASE_URL}/quarter/${incentiveYear}/${incentiveQuarter}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 根据月份查询激励列表
  getByMonth(incentiveYear, incentiveMonth, tenantId) {
    return request({
      url: `${BASE_URL}/month/${incentiveYear}/${incentiveMonth}`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 创建激励方案
  createIncentiveScheme(data) {
    return request({
      url: BASE_URL,
      method: 'post',
      data
    })
  },

  // 更新激励方案
  updateIncentiveScheme(data) {
    return request({
      url: BASE_URL,
      method: 'put',
      data
    })
  },

  // 删除激励方案
  deleteIncentiveScheme(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}`,
      method: 'delete',
      params: { tenantId }
    })
  },

  // 批量删除激励方案
  batchDeleteIncentiveSchemes(incentiveIds, tenantId) {
    return request({
      url: `${BASE_URL}/batch`,
      method: 'delete',
      data: incentiveIds,
      params: { tenantId }
    })
  },

  // 启动激励方案
  startIncentiveScheme(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/start`,
      method: 'post',
      params: { tenantId }
    })
  },

  // 暂停激励方案
  suspendIncentiveScheme(id, reason, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/suspend`,
      method: 'post',
      params: { reason, tenantId }
    })
  },

  // 完成激励方案
  completeIncentiveScheme(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/complete`,
      method: 'post',
      params: { tenantId }
    })
  },

  // 取消激励方案
  cancelIncentiveScheme(id, reason, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/cancel`,
      method: 'post',
      params: { reason, tenantId }
    })
  },

  // 审批激励方案
  approveIncentiveScheme(id, approvalComments, approverId, approverName, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/approve`,
      method: 'post',
      params: { approvalComments, approverId, approverName, tenantId }
    })
  },

  // 拒绝激励方案
  rejectIncentiveScheme(id, approvalComments, approverId, approverName, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/reject`,
      method: 'post',
      params: { approvalComments, approverId, approverName, tenantId }
    })
  },

  // 发放激励
  distributeIncentive(id, distributionMethod, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/distribute`,
      method: 'post',
      params: { distributionMethod, tenantId }
    })
  },

  // 批量发放激励
  batchDistributeIncentives(incentiveIds, distributionMethod, tenantId) {
    return request({
      url: `${BASE_URL}/batch-distribute`,
      method: 'post',
      data: incentiveIds,
      params: { distributionMethod, tenantId }
    })
  },

  // 计算激励金额
  calculateIncentiveAmount(id, parameters, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/calculate-amount`,
      method: 'post',
      data: parameters,
      params: { tenantId }
    })
  },

  // 批量计算激励金额
  batchCalculateIncentiveAmounts(incentiveIds, parameters, tenantId) {
    return request({
      url: `${BASE_URL}/batch-calculate-amounts`,
      method: 'post',
      data: incentiveIds,
      params: { ...parameters, tenantId }
    })
  },

  // 查询待审批的激励列表
  getPendingApproval(tenantId) {
    return request({
      url: `${BASE_URL}/pending-approval`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 查询待发放的激励列表
  getPendingDistribution(tenantId) {
    return request({
      url: `${BASE_URL}/pending-distribution`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 查询需要跟进的激励列表
  getNeedFollowUp(tenantId) {
    return request({
      url: `${BASE_URL}/need-follow-up`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 查询即将到期的激励列表
  getUpcomingDeadline(deadline, tenantId) {
    return request({
      url: `${BASE_URL}/upcoming-deadline`,
      method: 'get',
      params: { deadline, tenantId }
    })
  },

  // 查询超期的激励列表
  getOverdue(currentTime, tenantId) {
    return request({
      url: `${BASE_URL}/overdue`,
      method: 'get',
      params: { currentTime, tenantId }
    })
  },

  // 添加跟进记录
  addFollowUpRecord(id, followUpRecord, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/add-follow-up`,
      method: 'post',
      params: { followUpRecord, tenantId }
    })
  },

  // 更新跟进状态
  updateFollowUpStatus(id, followUpStatus, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/update-follow-up-status`,
      method: 'post',
      params: { followUpStatus, tenantId }
    })
  },

  // 批量更新跟进状态
  batchUpdateFollowUpStatus(incentiveIds, followUpStatus, tenantId) {
    return request({
      url: `${BASE_URL}/batch-update-follow-up-status`,
      method: 'post',
      data: incentiveIds,
      params: { followUpStatus, tenantId }
    })
  },

  // 发送提醒通知
  sendReminderNotification(id, notificationType, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/send-reminder`,
      method: 'post',
      params: { notificationType, tenantId }
    })
  },

  // 批量发送提醒通知
  batchSendReminderNotifications(incentiveIds, notificationType, tenantId) {
    return request({
      url: `${BASE_URL}/batch-send-reminders`,
      method: 'post',
      data: incentiveIds,
      params: { notificationType, tenantId }
    })
  }
}

// 统计分析API
export const statisticsApi = {
  // 统计激励数据
  getIncentiveStatistics(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/statistics`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 统计激励状态分布
  getIncentiveStatusDistribution(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/status-distribution`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 统计激励类型分布
  getIncentiveTypeDistribution(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/type-distribution`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 统计激励完成趋势
  getIncentiveCompletionTrend(startTime, endTime, tenantId) {
    return request({
      url: `${BASE_URL}/completion-trend`,
      method: 'get',
      params: { startTime, endTime, tenantId }
    })
  },

  // 统计激励金额分布
  getIncentiveAmountDistribution(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/amount-distribution`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 统计激励效果分布
  getIncentiveEffectivenessDistribution(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/effectiveness-distribution`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 查询激励排行榜
  getIncentiveRanking(incentiveYear, rankingType, limit, tenantId) {
    return request({
      url: `${BASE_URL}/ranking`,
      method: 'get',
      params: { incentiveYear, rankingType, limit, tenantId }
    })
  }
}

// 高级功能API
export const advancedApi = {
  // 检查激励编码是否存在
  checkIncentiveCodeExists(incentiveCode, incentiveId, tenantId) {
    return request({
      url: `${BASE_URL}/check-code-exists`,
      method: 'get',
      params: { incentiveCode, incentiveId, tenantId }
    })
  },

  // 检查时间冲突
  checkTimeConflict(incentiveId, targetDeptId, incentiveType, startTime, endTime, tenantId) {
    return request({
      url: `${BASE_URL}/check-time-conflict`,
      method: 'get',
      params: { incentiveId, targetDeptId, incentiveType, startTime, endTime, tenantId }
    })
  },

  // 计算激励总金额
  calculateTotalIncentiveAmount(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/total-amount`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 计算部门激励金额
  calculateDeptIncentiveAmount(targetDeptId, incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/dept-amount`,
      method: 'get',
      params: { targetDeptId, incentiveYear, tenantId }
    })
  },

  // 查询激励详情（包含关联信息）
  getIncentiveDetailWithRelations(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/detail-with-relations`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 查询激励历史记录
  getIncentiveHistory(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/history`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 导出激励数据
  exportIncentiveData(incentiveYear, incentiveType, incentiveStatus, targetDeptId, tenantId) {
    return request({
      url: `${BASE_URL}/export`,
      method: 'get',
      params: { incentiveYear, incentiveType, incentiveStatus, targetDeptId, tenantId }
    })
  },

  // 智能推荐激励方案
  getRecommendedIncentiveSchemes(targetDeptId, incentiveType, budgetRange, tenantId) {
    return request({
      url: `${BASE_URL}/recommended-schemes`,
      method: 'get',
      params: { targetDeptId, incentiveType, budgetRange, tenantId }
    })
  },

  // 分析激励效果
  analyzeIncentiveEffectiveness(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/analyze-effectiveness`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 生成激励报告数据
  generateIncentiveReportData(incentiveYear, reportType, tenantId) {
    return request({
      url: `${BASE_URL}/generate-report`,
      method: 'get',
      params: { incentiveYear, reportType, tenantId }
    })
  },

  // 查询激励优化建议
  getIncentiveOptimizationSuggestions(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/optimization-suggestions`,
      method: 'get',
      params: { tenantId }
    })
  },

  // 复制激励方案
  copyIncentiveScheme(id, newIncentiveTitle, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/copy`,
      method: 'post',
      params: { newIncentiveTitle, tenantId }
    })
  },

  // 保存为模板
  saveAsTemplate(id, templateName, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/save-as-template`,
      method: 'post',
      params: { templateName, tenantId }
    })
  },

  // 从模板创建激励方案
  createFromTemplate(templateId, incentiveTitle, tenantId) {
    return request({
      url: `${BASE_URL}/create-from-template`,
      method: 'post',
      params: { templateId, incentiveTitle, tenantId }
    })
  },

  // 智能分析激励趋势
  analyzeIncentiveTrends(incentiveYear, tenantId) {
    return request({
      url: `${BASE_URL}/analyze-trends`,
      method: 'get',
      params: { incentiveYear, tenantId }
    })
  },

  // 预测激励需求
  predictIncentiveNeeds(targetDeptId, targetYear, tenantId) {
    return request({
      url: `${BASE_URL}/predict-needs`,
      method: 'get',
      params: { targetDeptId, targetYear, tenantId }
    })
  },

  // 优化激励配置
  optimizeIncentiveConfiguration(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/optimize-configuration`,
      method: 'get',
      params: { tenantId }
    })
  }
}

// 快捷操作API
export const quickApi = {
  // 快速创建激励方案
  quickCreateIncentive(incentiveType, targetDeptId, totalBudget, tenantId) {
    const data = {
      incentiveType,
      targetDeptId,
      totalBudget,
      incentiveStatus: 'DRAFT',
      incentiveCode: `INC${Date.now()}`,
      incentiveTitle: `${incentiveType}激励方案`,
      tenantId
    }
    return request({
      url: BASE_URL,
      method: 'post',
      data
    })
  },

  // 快速审批通过
  quickApprove(id, approverId, approverName, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/approve`,
      method: 'post',
      params: {
        approvalComments: '快速审批通过',
        approverId,
        approverName,
        tenantId
      }
    })
  },

  // 快速发放激励
  quickDistribute(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/distribute`,
      method: 'post',
      params: { distributionMethod: 'TRANSFER', tenantId }
    })
  },

  // 快速完成激励
  quickComplete(id, tenantId) {
    return request({
      url: `${BASE_URL}/${id}/complete`,
      method: 'post',
      params: { tenantId }
    })
  }
}

// 工具函数
export const utils = {
  // 格式化激励状态
  formatIncentiveStatus(status) {
    const statusMap = {
      'DRAFT': '草稿',
      'APPROVED': '已审批',
      'ACTIVE': '生效中',
      'SUSPENDED': '暂停',
      'COMPLETED': '已完成',
      'CANCELLED': '已取消'
    }
    return statusMap[status] || status
  },

  // 格式化激励类型
  formatIncentiveType(type) {
    const typeMap = {
      'PERFORMANCE': '绩效激励',
      'ACHIEVEMENT': '成就激励',
      'INNOVATION': '创新激励',
      'TEAM': '团队激励',
      'SPECIAL': '专项激励'
    }
    return typeMap[type] || type
  },

  // 格式化激励分类
  formatIncentiveCategory(category) {
    const categoryMap = {
      'MONETARY': '货币激励',
      'NON_MONETARY': '非货币激励',
      'MIXED': '混合激励'
    }
    return categoryMap[category] || category
  },

  // 格式化发放方式
  formatDistributionMethod(method) {
    const methodMap = {
      'CASH': '现金',
      'TRANSFER': '转账',
      'VOUCHER': '代金券',
      'GIFT': '实物',
      'POINTS': '积分'
    }
    return methodMap[method] || method
  },

  // 获取状态颜色
  getStatusColor(status) {
    const colorMap = {
      'DRAFT': 'info',
      'APPROVED': 'success',
      'ACTIVE': 'primary',
      'SUSPENDED': 'warning',
      'COMPLETED': 'success',
      'CANCELLED': 'danger'
    }
    return colorMap[status] || 'info'
  },

  // 验证激励数据
  validateIncentiveData(data) {
    const errors = []

    if (!data.incentiveTitle) {
      errors.push('激励标题不能为空')
    }

    if (!data.incentiveType) {
      errors.push('激励类型不能为空')
    }

    if (!data.targetDeptId) {
      errors.push('目标部门不能为空')
    }

    if (!data.totalBudget || data.totalBudget <= 0) {
      errors.push('预算总额必须大于0')
    }

    if (data.plannedStartTime && data.plannedEndTime) {
      if (new Date(data.plannedStartTime) >= new Date(data.plannedEndTime)) {
        errors.push('开始时间必须早于结束时间')
      }
    }

    return errors
  },

  // 计算激励进度
  calculateIncentiveProgress(incentive) {
    if (!incentive.plannedStartTime || !incentive.plannedEndTime) {
      return 0
    }

    const now = new Date()
    const startTime = new Date(incentive.plannedStartTime)
    const endTime = new Date(incentive.plannedEndTime)

    if (now < startTime) {
      return 0
    }

    if (now > endTime) {
      return 100
    }

    const totalDuration = endTime.getTime() - startTime.getTime()
    const elapsedDuration = now.getTime() - startTime.getTime()

    return Math.round((elapsedDuration / totalDuration) * 100)
  }
}
