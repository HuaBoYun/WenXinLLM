import request from '@/utils/request'

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询绩效校准
 */
export function queryCalibrationPage(params) {
  return request({
    url: '/accountant/pm/performance-calibration/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询绩效校准
 */
export function getCalibrationById(id) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}`,
    method: 'get'
  })
}

/**
 * 查询校准详情（包含关联信息）
 */
export function getCalibrationDetail(id) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/detail`,
    method: 'get'
  })
}

/**
 * 创建绩效校准
 */
export function createCalibration(data) {
  return request({
    url: '/accountant/pm/performance-calibration',
    method: 'post',
    data
  })
}

/**
 * 更新绩效校准
 */
export function updateCalibration(data) {
  return request({
    url: '/accountant/pm/performance-calibration',
    method: 'put',
    data
  })
}

/**
 * 删除绩效校准
 */
export function deleteCalibration(id) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除绩效校准
 */
export function batchDeleteCalibrations(calibrationIds) {
  return request({
    url: '/accountant/pm/performance-calibration/batch',
    method: 'delete',
    data: calibrationIds
  })
}

// ==================== 校准流程管理 ====================

/**
 * 开始校准
 */
export function startCalibration(id, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/start`,
    method: 'post',
    data
  })
}

/**
 * 完成校准
 */
export function completeCalibration(id, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/complete`,
    method: 'post',
    data
  })
}

/**
 * 取消校准
 */
export function cancelCalibration(id, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/cancel`,
    method: 'post',
    data
  })
}

/**
 * 批量更新校准状态
 */
export function batchUpdateStatus(calibrationIds, status, data) {
  return request({
    url: '/accountant/pm/performance-calibration/batch/status',
    method: 'post',
    params: {
      calibrationIds: calibrationIds.join(','),
      status
    },
    data
  })
}

// ==================== 校准记录和跟进 ====================

/**
 * 保存校准记录
 */
export function saveCalibrationRecord(id, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/record`,
    method: 'post',
    data
  })
}

/**
 * 创建跟进计划
 */
export function createFollowUpPlan(id, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/follow-up`,
    method: 'post',
    data
  })
}

/**
 * 更新跟进状态
 */
export function updateFollowUpStatus(id, followUpStatus, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/follow-up/status`,
    method: 'put',
    params: { followUpStatus },
    data
  })
}

/**
 * 批量更新跟进状态
 */
export function batchUpdateFollowUpStatus(calibrationIds, followUpStatus, data) {
  return request({
    url: '/accountant/pm/performance-calibration/batch/follow-up/status',
    method: 'post',
    params: {
      calibrationIds: calibrationIds.join(','),
      followUpStatus
    },
    data
  })
}

// ==================== 查询接口 ====================

/**
 * 根据校准负责人查询校准列表
 */
export function getCalibrationsByOwner(ownerId, status, limit = 10) {
  return request({
    url: `/accountant/pm/performance-calibration/owner/${ownerId}`,
    method: 'get',
    params: { status, limit }
  })
}

/**
 * 根据部门查询校准列表
 */
export function getCalibrationsByDept(deptId, status, limit = 10) {
  return request({
    url: `/accountant/pm/performance-calibration/department/${deptId}`,
    method: 'get',
    params: { status, limit }
  })
}

/**
 * 根据校准类型查询校准列表
 */
export function getCalibrationsByType(type, calibrationYear, limit = 10) {
  return request({
    url: `/accountant/pm/performance-calibration/type/${type}`,
    method: 'get',
    params: { calibrationYear, limit }
  })
}

/**
 * 查询待跟进的校准
 */
export function getPendingFollowUpCalibrations(deadline, limit = 10) {
  return request({
    url: '/accountant/pm/performance-calibration/pending-follow-up',
    method: 'get',
    params: { deadline, limit }
  })
}

/**
 * 查询即将到期的校准
 */
export function getUpcomingCalibrations(deadline, limit = 10) {
  return request({
    url: '/accountant/pm/performance-calibration/upcoming',
    method: 'get',
    params: { deadline, limit }
  })
}

/**
 * 查询逾期的校准
 */
export function getOverdueCalibrations(limit = 10) {
  return request({
    url: '/accountant/pm/performance-calibration/overdue',
    method: 'get',
    params: { limit }
  })
}

// ==================== 统计分析 ====================

/**
 * 统计校准数据
 */
export function getCalibrationStatistics(params) {
  return request({
    url: '/accountant/pm/performance-calibration/statistics',
    method: 'get',
    params
  })
}

/**
 * 统计校准状态分布
 */
export function getCalibrationStatusDistribution(calibrationYear, targetDeptId) {
  return request({
    url: '/accountant/pm/performance-calibration/statistics/status-distribution',
    method: 'get',
    params: { calibrationYear, targetDeptId }
  })
}

/**
 * 统计校准类型分布
 */
export function getCalibrationTypeDistribution(calibrationYear, targetDeptId) {
  return request({
    url: '/accountant/pm/performance-calibration/statistics/type-distribution',
    method: 'get',
    params: { calibrationYear, targetDeptId }
  })
}

/**
 * 统计校准完成趋势
 */
export function getCalibrationCompletionTrend(startTime, endTime, targetDeptId) {
  return request({
    url: '/accountant/pm/performance-calibration/statistics/completion-trend',
    method: 'get',
    params: { startTime, endTime, targetDeptId }
  })
}

/**
 * 统计校准效果分布
 */
export function getEffectivenessDistribution(calibrationYear, targetDeptId) {
  return request({
    url: '/accountant/pm/performance-calibration/statistics/effectiveness-distribution',
    method: 'get',
    params: { calibrationYear, targetDeptId }
  })
}

/**
 * 查询校准排行榜
 */
export function getCalibrationRanking(calibrationYear, rankType, limit = 10) {
  return request({
    url: '/accountant/pm/performance-calibration/ranking',
    method: 'get',
    params: { calibrationYear, rankType, limit }
  })
}

// ==================== 辅助功能 ====================

/**
 * 检查校准时间冲突
 */
export function checkTimeConflict(calibrationOwnerId, startTime, endTime, excludeId) {
  return request({
    url: '/accountant/pm/performance-calibration/check-conflict',
    method: 'get',
    params: { calibrationOwnerId, startTime, endTime, excludeId }
  })
}

/**
 * 查询可用校准负责人
 */
export function getAvailableOwners(targetDeptId, calibrationType, limit = 10) {
  return request({
    url: '/accountant/pm/performance-calibration/available-owners',
    method: 'get',
    params: { targetDeptId, calibrationType, limit }
  })
}

/**
 * 查询校准提醒列表
 */
export function getCalibrationReminders(reminderTime, limit = 10) {
  return request({
    url: '/accountant/pm/performance-calibration/reminders',
    method: 'get',
    params: { reminderTime, limit }
  })
}

/**
 * 智能推荐校准时间
 */
export function recommendCalibrationTimes(calibrationOwnerId, participantIds, duration, preferredDates) {
  return request({
    url: '/accountant/pm/performance-calibration/recommend-times',
    method: 'get',
    params: {
      calibrationOwnerId,
      participantIds: participantIds.join(','),
      duration,
      preferredDates: preferredDates ? preferredDates.join(',') : undefined
    }
  })
}

// ==================== 高级功能 ====================

/**
 * 生成校准报告
 */
export function generateCalibrationReport(id, params) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/report`,
    method: 'post',
    data: params
  })
}

/**
 * 导出校准数据
 */
export function exportCalibrationData(params) {
  return request({
    url: '/accountant/pm/performance-calibration/export',
    method: 'post',
    data: params
  })
}

/**
 * 导入校准数据
 */
export function importCalibrationData(importData, params) {
  return request({
    url: '/accountant/pm/performance-calibration/import',
    method: 'post',
    data: importData,
    params
  })
}

/**
 * 复制校准
 */
export function copyCalibration(id, params) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/copy`,
    method: 'post',
    data: params
  })
}

/**
 * 批量创建校准
 */
export function batchCreateCalibrations(calibrations, params) {
  return request({
    url: '/accountant/pm/performance-calibration/batch/create',
    method: 'post',
    data: calibrations,
    params
  })
}

/**
 * 发送校准通知
 */
export function sendCalibrationNotification(id, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/notification`,
    method: 'post',
    data
  })
}

/**
 * 批量发送通知
 */
export function batchSendNotifications(calibrationIds, data) {
  return request({
    url: '/accountant/pm/performance-calibration/batch/notification',
    method: 'post',
    params: { calibrationIds: calibrationIds.join(',') },
    data
  })
}

/**
 * 校准流程控制
 */
export function controlCalibrationProcess(id, action, data) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/control`,
    method: 'post',
    params: { action },
    data
  })
}

/**
 * 智能校准分析
 */
export function intelligentCalibrationAnalysis(id, params) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/intelligent-analysis`,
    method: 'post',
    data: params
  })
}

/**
 * 获取校准优化建议
 */
export function getCalibrationOptimizationSuggestions(id) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/optimization-suggestions`,
    method: 'get'
  })
}

/**
 * 评估校准效果
 */
export function evaluateCalibrationEffectiveness(id, params) {
  return request({
    url: `/accountant/pm/performance-calibration/${id}/effectiveness-evaluation`,
    method: 'post',
    data: params
  })
}

/**
 * 数据验证
 */
export function validateCalibrationData(data) {
  return request({
    url: '/accountant/pm/performance-calibration/validate',
    method: 'post',
    data
  })
}

// ==================== 快捷操作 ====================

/**
 * 快速创建校准
 */
export function quickCreateCalibration(calibrationTitle, calibrationType, targetDeptId, calibrationOwnerId) {
  const data = {
    calibrationTitle,
    calibrationType,
    targetDeptId,
    calibrationOwnerId,
    calibrationStatus: 'PLANNED',
    priorityLevel: 'MEDIUM',
    calibrationYear: new Date().getFullYear()
  }
  return createCalibration(data)
}

/**
 * 快速开始校准
 */
export function quickStartCalibration(id) {
  return startCalibration(id, {
    calibrationMethod: 'MEETING',
    calibrationLocation: '会议室'
  })
}

/**
 * 快速完成校准
 */
export function quickCompleteCalibration(id, calibrationSummary) {
  return completeCalibration(id, {
    calibrationSummary,
    effectivenessRating: 4,
    needFollowUp: false
  })
}

/**
 * 快速取消校准
 */
export function quickCancelCalibration(id, cancelReason) {
  return cancelCalibration(id, {
    cancelReason
  })
}

// ==================== 工具函数 ====================

/**
 * 格式化校准状态
 */
export function formatCalibrationStatus(status) {
  const statusMap = {
    'PLANNED': '计划中',
    'ONGOING': '进行中',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

/**
 * 格式化校准类型
 */
export function formatCalibrationType(type) {
  const typeMap = {
    'ANNUAL': '年度校准',
    'QUARTERLY': '季度校准',
    'MONTHLY': '月度校准',
    'PROJECT': '项目校准',
    'SPECIAL': '专项校准'
  }
  return typeMap[type] || type
}

/**
 * 格式化优先级
 */
export function formatPriorityLevel(level) {
  const levelMap = {
    'HIGH': '高',
    'MEDIUM': '中',
    'LOW': '低'
  }
  return levelMap[level] || level
}

/**
 * 格式化跟进状态
 */
export function formatFollowUpStatus(status) {
  const statusMap = {
    'PENDING': '待跟进',
    'IN_PROGRESS': '跟进中',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

/**
 * 获取状态颜色
 */
export function getStatusColor(status) {
  const colorMap = {
    'PLANNED': 'info',
    'ONGOING': 'warning',
    'COMPLETED': 'success',
    'CANCELLED': 'danger'
  }
  return colorMap[status] || 'info'
}

/**
 * 获取优先级颜色
 */
export function getPriorityColor(level) {
  const colorMap = {
    'HIGH': 'danger',
    'MEDIUM': 'warning',
    'LOW': 'info'
  }
  return colorMap[level] || 'info'
}

/**
 * 校验校准数据
 */
export function validateCalibrationForm(form) {
  const errors = []
  
  if (!form.calibrationTitle) {
    errors.push('校准标题不能为空')
  }
  if (!form.calibrationType) {
    errors.push('校准类型不能为空')
  }
  if (!form.calibrationOwnerId) {
    errors.push('校准负责人不能为空')
  }
  if (!form.targetDeptId) {
    errors.push('目标部门不能为空')
  }
  if (form.plannedStartTime && form.plannedEndTime) {
    if (new Date(form.plannedStartTime) >= new Date(form.plannedEndTime)) {
      errors.push('开始时间不能晚于结束时间')
    }
  }
  
  return {
    valid: errors.length === 0,
    errors
  }
}

/**
 * 计算校准进度
 */
export function calculateCalibrationProgress(calibration) {
  if (calibration.calibrationStatus === 'COMPLETED') {
    return 100
  } else if (calibration.calibrationStatus === 'ONGOING') {
    return calibration.completionRate || 50
  } else if (calibration.calibrationStatus === 'PLANNED') {
    return 0
  } else {
    return 0
  }
}

/**
 * 判断是否逾期
 */
export function isOverdue(calibration) {
  if (calibration.calibrationStatus === 'COMPLETED' || calibration.calibrationStatus === 'CANCELLED') {
    return false
  }
  if (calibration.plannedEndTime) {
    return new Date() > new Date(calibration.plannedEndTime)
  }
  return false
}

/**
 * 获取校准持续时间
 */
export function getCalibrationDuration(calibration) {
  if (calibration.actualStartTime && calibration.actualEndTime) {
    const start = new Date(calibration.actualStartTime)
    const end = new Date(calibration.actualEndTime)
    const duration = Math.floor((end - start) / (1000 * 60)) // 分钟
    if (duration < 60) {
      return `${duration}分钟`
    } else {
      const hours = Math.floor(duration / 60)
      const minutes = duration % 60
      return `${hours}小时${minutes}分钟`
    }
  }
  return '-'
}

export default {
  // 基础CRUD
  queryCalibrationPage,
  getCalibrationById,
  getCalibrationDetail,
  createCalibration,
  updateCalibration,
  deleteCalibration,
  batchDeleteCalibrations,
  
  // 流程管理
  startCalibration,
  completeCalibration,
  cancelCalibration,
  batchUpdateStatus,
  
  // 记录跟进
  saveCalibrationRecord,
  createFollowUpPlan,
  updateFollowUpStatus,
  batchUpdateFollowUpStatus,
  
  // 查询接口
  getCalibrationsByOwner,
  getCalibrationsByDept,
  getCalibrationsByType,
  getPendingFollowUpCalibrations,
  getUpcomingCalibrations,
  getOverdueCalibrations,
  
  // 统计分析
  getCalibrationStatistics,
  getCalibrationStatusDistribution,
  getCalibrationTypeDistribution,
  getCalibrationCompletionTrend,
  getEffectivenessDistribution,
  getCalibrationRanking,
  
  // 辅助功能
  checkTimeConflict,
  getAvailableOwners,
  getCalibrationReminders,
  recommendCalibrationTimes,
  
  // 高级功能
  generateCalibrationReport,
  exportCalibrationData,
  importCalibrationData,
  copyCalibration,
  batchCreateCalibrations,
  sendCalibrationNotification,
  batchSendNotifications,
  controlCalibrationProcess,
  intelligentCalibrationAnalysis,
  getCalibrationOptimizationSuggestions,
  evaluateCalibrationEffectiveness,
  validateCalibrationData,
  
  // 快捷操作
  quickCreateCalibration,
  quickStartCalibration,
  quickCompleteCalibration,
  quickCancelCalibration,
  
  // 工具函数
  formatCalibrationStatus,
  formatCalibrationType,
  formatPriorityLevel,
  formatFollowUpStatus,
  getStatusColor,
  getPriorityColor,
  validateCalibrationForm,
  calculateCalibrationProgress,
  isOverdue,
  getCalibrationDuration
}
