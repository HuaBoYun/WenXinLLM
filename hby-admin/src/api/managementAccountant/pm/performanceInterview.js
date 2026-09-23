import request from '@/utils/request'

// API基础路径
const API_BASE_PATH = '/pm/performance-interview'

/**
 * 绩效面谈管理API
 */

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询绩效面谈
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function queryInterviewPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'get',
    params
  })
}

/**
 * 根据ID查询绩效面谈
 * @param {Number} id 面谈ID
 * @returns {Promise}
 */
export function getInterviewById(id) {
  return request({
    url: `${API_BASE_PATH}/${id}`,
    method: 'get'
  })
}

/**
 * 查询面谈详情（包含关联信息）
 * @param {Number} id 面谈ID
 * @returns {Promise}
 */
export function getInterviewDetail(id) {
  return request({
    url: `${API_BASE_PATH}/${id}/detail`,
    method: 'get'
  })
}

/**
 * 创建绩效面谈
 * @param {Object} data 面谈数据
 * @returns {Promise}
 */
export function createInterview(data) {
  return request({
    url: API_BASE_PATH,
    method: 'post',
    data
  })
}

/**
 * 更新绩效面谈
 * @param {Object} data 面谈数据
 * @returns {Promise}
 */
export function updateInterview(data) {
  return request({
    url: API_BASE_PATH,
    method: 'put',
    data
  })
}

/**
 * 删除绩效面谈
 * @param {Number} id 面谈ID
 * @returns {Promise}
 */
export function deleteInterview(id) {
  return request({
    url: `${API_BASE_PATH}/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除绩效面谈
 * @param {Array} ids 面谈ID列表
 * @returns {Promise}
 */
export function batchDeleteInterviews(ids) {
  return request({
    url: `${API_BASE_PATH}/batch`,
    method: 'delete',
    data: ids
  })
}

// ==================== 面谈流程管理 ====================

/**
 * 安排面谈
 * @param {Number} id 面谈ID
 * @param {Object} data 安排参数
 * @returns {Promise}
 */
export function scheduleInterview(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/schedule`,
    method: 'post',
    data
  })
}

/**
 * 开始面谈
 * @param {Number} id 面谈ID
 * @param {Object} data 开始参数
 * @returns {Promise}
 */
export function startInterview(id, data = {}) {
  return request({
    url: `${API_BASE_PATH}/${id}/start`,
    method: 'post',
    data
  })
}

/**
 * 完成面谈
 * @param {Number} id 面谈ID
 * @param {Object} data 完成参数
 * @returns {Promise}
 */
export function completeInterview(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/complete`,
    method: 'post',
    data
  })
}

/**
 * 取消面谈
 * @param {Number} id 面谈ID
 * @param {Object} data 取消参数
 * @returns {Promise}
 */
export function cancelInterview(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/cancel`,
    method: 'post',
    data
  })
}

/**
 * 延期面谈
 * @param {Number} id 面谈ID
 * @param {Object} data 延期参数
 * @returns {Promise}
 */
export function postponeInterview(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/postpone`,
    method: 'post',
    data
  })
}

/**
 * 批量更新面谈状态
 * @param {Array} ids 面谈ID列表
 * @param {String} status 状态
 * @param {Object} data 更新参数
 * @returns {Promise}
 */
export function batchUpdateStatus(ids, status, data = {}) {
  return request({
    url: `${API_BASE_PATH}/batch/status`,
    method: 'post',
    params: { interviewIds: ids, status },
    data
  })
}

// ==================== 面谈记录和反馈 ====================

/**
 * 保存面谈记录
 * @param {Number} id 面谈ID
 * @param {Object} data 记录参数
 * @returns {Promise}
 */
export function saveInterviewRecord(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/record`,
    method: 'post',
    data
  })
}

/**
 * 提交面谈反馈
 * @param {Number} id 面谈ID
 * @param {Object} data 反馈参数
 * @returns {Promise}
 */
export function submitFeedback(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/feedback`,
    method: 'post',
    data
  })
}

// ==================== 跟进管理 ====================

/**
 * 创建跟进计划
 * @param {Number} id 面谈ID
 * @param {Object} data 跟进参数
 * @returns {Promise}
 */
export function createFollowUpPlan(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/follow-up`,
    method: 'post',
    data
  })
}

/**
 * 更新跟进状态
 * @param {Number} id 面谈ID
 * @param {String} status 跟进状态
 * @param {Object} data 更新参数
 * @returns {Promise}
 */
export function updateFollowUpStatus(id, status, data = {}) {
  return request({
    url: `${API_BASE_PATH}/${id}/follow-up/status`,
    method: 'put',
    params: { followUpStatus: status },
    data
  })
}

/**
 * 批量更新跟进状态
 * @param {Array} ids 面谈ID列表
 * @param {String} status 跟进状态
 * @param {Object} data 更新参数
 * @returns {Promise}
 */
export function batchUpdateFollowUpStatus(ids, status, data = {}) {
  return request({
    url: `${API_BASE_PATH}/batch/follow-up/status`,
    method: 'post',
    params: { interviewIds: ids, followUpStatus: status },
    data
  })
}

// ==================== 查询接口 ====================

/**
 * 根据被面谈人查询面谈列表
 * @param {Number} intervieweeId 被面谈人ID
 * @param {String} status 状态
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getInterviewsByInterviewee(intervieweeId, status, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/interviewee/${intervieweeId}`,
    method: 'get',
    params: { status, limit }
  })
}

/**
 * 根据面谈官查询面谈列表
 * @param {Number} interviewerId 面谈官ID
 * @param {String} status 状态
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getInterviewsByInterviewer(interviewerId, status, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/interviewer/${interviewerId}`,
    method: 'get',
    params: { status, limit }
  })
}

/**
 * 根据部门查询面谈列表
 * @param {Number} deptId 部门ID
 * @param {String} status 状态
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getInterviewsByDept(deptId, status, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/department/${deptId}`,
    method: 'get',
    params: { status, limit }
  })
}

/**
 * 查询待跟进的面谈
 * @param {String} deadline 截止时间
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getPendingFollowUpInterviews(deadline, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/pending-follow-up`,
    method: 'get',
    params: { deadline, limit }
  })
}

/**
 * 查询即将到期的面谈
 * @param {String} deadline 截止时间
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getUpcomingInterviews(deadline, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/upcoming`,
    method: 'get',
    params: { deadline, limit }
  })
}

/**
 * 查询逾期的面谈
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getOverdueInterviews(limit = 10) {
  return request({
    url: `${API_BASE_PATH}/overdue`,
    method: 'get',
    params: { limit }
  })
}

// ==================== 统计分析 ====================

/**
 * 统计面谈数据
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getInterviewStatistics(params) {
  return request({
    url: `${API_BASE_PATH}/statistics`,
    method: 'get',
    params
  })
}

/**
 * 统计面谈状态分布
 * @param {Number} year 年度
 * @param {Number} deptId 部门ID
 * @returns {Promise}
 */
export function getInterviewStatusDistribution(year, deptId) {
  return request({
    url: `${API_BASE_PATH}/statistics/status-distribution`,
    method: 'get',
    params: { year, deptId }
  })
}

/**
 * 统计面谈类型分布
 * @param {Number} year 年度
 * @param {Number} deptId 部门ID
 * @returns {Promise}
 */
export function getInterviewTypeDistribution(year, deptId) {
  return request({
    url: `${API_BASE_PATH}/statistics/type-distribution`,
    method: 'get',
    params: { year, deptId }
  })
}

/**
 * 统计面谈完成趋势
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {Number} deptId 部门ID
 * @returns {Promise}
 */
export function getInterviewCompletionTrend(startTime, endTime, deptId) {
  return request({
    url: `${API_BASE_PATH}/statistics/completion-trend`,
    method: 'get',
    params: { startTime, endTime, deptId }
  })
}

/**
 * 统计面谈满意度分布
 * @param {Number} year 年度
 * @param {Number} deptId 部门ID
 * @returns {Promise}
 */
export function getSatisfactionDistribution(year, deptId) {
  return request({
    url: `${API_BASE_PATH}/statistics/satisfaction-distribution`,
    method: 'get',
    params: { year, deptId }
  })
}

/**
 * 查询面谈排行榜
 * @param {Number} year 年度
 * @param {String} rankType 排行类型
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getInterviewRanking(year, rankType, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/ranking`,
    method: 'get',
    params: { year, rankType, limit }
  })
}

// ==================== 辅助功能 ====================

/**
 * 检查面谈时间冲突
 * @param {Number} interviewerId 面谈官ID
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {Number} excludeId 排除的面谈ID
 * @returns {Promise}
 */
export function checkTimeConflict(interviewerId, startTime, endTime, excludeId) {
  return request({
    url: `${API_BASE_PATH}/check-conflict`,
    method: 'get',
    params: { interviewerId, startTime, endTime, excludeId }
  })
}

/**
 * 查询可用面谈官
 * @param {Number} deptId 部门ID
 * @param {String} interviewType 面谈类型
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getAvailableInterviewers(deptId, interviewType, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/available-interviewers`,
    method: 'get',
    params: { deptId, interviewType, limit }
  })
}

/**
 * 查询面谈提醒列表
 * @param {String} reminderTime 提醒时间
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getInterviewReminders(reminderTime, limit = 10) {
  return request({
    url: `${API_BASE_PATH}/reminders`,
    method: 'get',
    params: { reminderTime, limit }
  })
}

/**
 * 智能推荐面谈时间
 * @param {Number} interviewerId 面谈官ID
 * @param {Number} intervieweeId 被面谈人ID
 * @param {Number} duration 面谈时长
 * @param {Array} preferredDates 偏好日期
 * @returns {Promise}
 */
export function recommendInterviewTimes(interviewerId, intervieweeId, duration, preferredDates) {
  return request({
    url: `${API_BASE_PATH}/recommend-times`,
    method: 'get',
    params: { interviewerId, intervieweeId, duration, preferredDates }
  })
}

// ==================== 报告和导入导出 ====================

/**
 * 生成面谈报告
 * @param {Number} id 面谈ID
 * @param {Object} data 报告参数
 * @returns {Promise}
 */
export function generateInterviewReport(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/report`,
    method: 'post',
    data
  })
}

/**
 * 导出面谈数据
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportInterviewData(params) {
  return request({
    url: `${API_BASE_PATH}/export`,
    method: 'get',
    params
  })
}

/**
 * 导入面谈数据
 * @param {Array} data 导入数据
 * @param {Object} params 导入参数
 * @returns {Promise}
 */
export function importInterviewData(data, params) {
  return request({
    url: `${API_BASE_PATH}/import`,
    method: 'post',
    data,
    params
  })
}

/**
 * 复制面谈
 * @param {Number} id 面谈ID
 * @param {Object} data 复制参数
 * @returns {Promise}
 */
export function copyInterview(id, data = {}) {
  return request({
    url: `${API_BASE_PATH}/${id}/copy`,
    method: 'post',
    data
  })
}

/**
 * 批量创建面谈
 * @param {Array} data 面谈列表
 * @param {Object} params 批量参数
 * @returns {Promise}
 */
export function batchCreateInterviews(data, params = {}) {
  return request({
    url: `${API_BASE_PATH}/batch`,
    method: 'post',
    data,
    params
  })
}

// ==================== 通知功能 ====================

/**
 * 发送面谈通知
 * @param {Number} id 面谈ID
 * @param {Object} data 通知参数
 * @returns {Promise}
 */
export function sendInterviewNotification(id, data) {
  return request({
    url: `${API_BASE_PATH}/${id}/notification`,
    method: 'post',
    data
  })
}

/**
 * 批量发送面谈通知
 * @param {Array} ids 面谈ID列表
 * @param {Object} data 通知参数
 * @returns {Promise}
 */
export function batchSendNotifications(ids, data) {
  return request({
    url: `${API_BASE_PATH}/batch/notification`,
    method: 'post',
    params: { interviewIds: ids },
    data
  })
}

// ==================== 快捷操作 ====================

/**
 * 快速创建面谈
 * @param {Object} basicInfo 基本信息
 * @returns {Promise}
 */
export function quickCreateInterview(basicInfo) {
  const interviewData = {
    interviewTitle: basicInfo.title,
    interviewType: basicInfo.type || 'QUARTERLY',
    intervieweeId: basicInfo.intervieweeId,
    intervieweeName: basicInfo.intervieweeName,
    interviewerId: basicInfo.interviewerId,
    interviewerName: basicInfo.interviewerName,
    interviewYear: new Date().getFullYear(),
    interviewStatus: 'PLANNED',
    priorityLevel: basicInfo.priority || 'MEDIUM',
    ...basicInfo
  }
  return createInterview(interviewData)
}

/**
 * 快速安排面谈
 * @param {Number} id 面谈ID
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {String} location 地点
 * @param {String} method 方式
 * @returns {Promise}
 */
export function quickScheduleInterview(id, startTime, endTime, location, method = 'FACE_TO_FACE') {
  return scheduleInterview(id, {
    plannedStartTime: startTime,
    plannedEndTime: endTime,
    interviewLocation: location,
    interviewMethod: method
  })
}

/**
 * 快速完成面谈
 * @param {Number} id 面谈ID
 * @param {Object} summary 面谈总结
 * @returns {Promise}
 */
export function quickCompleteInterview(id, summary) {
  return completeInterview(id, {
    interviewSummary: summary.summary,
    performanceSummary: summary.performance,
    keyAchievements: summary.achievements,
    improvementAreas: summary.improvements,
    developmentGoals: summary.goals,
    satisfactionRating: summary.rating || 4,
    needFollowUp: summary.needFollowUp || false,
    followUpPlan: summary.followUpPlan,
    followUpDeadline: summary.followUpDeadline
  })
}

// ==================== 常量定义 ====================

// 面谈状态
export const INTERVIEW_STATUS = {
  PLANNED: 'PLANNED',
  SCHEDULED: 'SCHEDULED',
  ONGOING: 'ONGOING',
  COMPLETED: 'COMPLETED',
  CANCELLED: 'CANCELLED',
  POSTPONED: 'POSTPONED'
}

// 面谈类型
export const INTERVIEW_TYPE = {
  ANNUAL: 'ANNUAL',
  QUARTERLY: 'QUARTERLY',
  MONTHLY: 'MONTHLY',
  PROJECT: 'PROJECT',
  SPECIAL: 'SPECIAL'
}

// 面谈方式
export const INTERVIEW_METHOD = {
  FACE_TO_FACE: 'FACE_TO_FACE',
  VIDEO: 'VIDEO',
  PHONE: 'PHONE',
  ONLINE: 'ONLINE'
}

// 优先级
export const PRIORITY_LEVEL = {
  HIGH: 'HIGH',
  MEDIUM: 'MEDIUM',
  LOW: 'LOW'
}

// 跟进状态
export const FOLLOW_UP_STATUS = {
  PENDING: 'PENDING',
  IN_PROGRESS: 'IN_PROGRESS',
  COMPLETED: 'COMPLETED'
}

// 绩效等级
export const PERFORMANCE_GRADE = {
  EXCELLENT: 'EXCELLENT',
  GOOD: 'GOOD',
  FAIR: 'FAIR',
  POOR: 'POOR'
}

// ==================== 工具函数 ====================

/**
 * 格式化面谈状态
 * @param {String} status 状态
 * @returns {String}
 */
export function formatInterviewStatus(status) {
  const statusMap = {
    [INTERVIEW_STATUS.PLANNED]: '已计划',
    [INTERVIEW_STATUS.SCHEDULED]: '已安排',
    [INTERVIEW_STATUS.ONGOING]: '进行中',
    [INTERVIEW_STATUS.COMPLETED]: '已完成',
    [INTERVIEW_STATUS.CANCELLED]: '已取消',
    [INTERVIEW_STATUS.POSTPONED]: '已延期'
  }
  return statusMap[status] || status
}

/**
 * 格式化面谈类型
 * @param {String} type 类型
 * @returns {String}
 */
export function formatInterviewType(type) {
  const typeMap = {
    [INTERVIEW_TYPE.ANNUAL]: '年度面谈',
    [INTERVIEW_TYPE.QUARTERLY]: '季度面谈',
    [INTERVIEW_TYPE.MONTHLY]: '月度面谈',
    [INTERVIEW_TYPE.PROJECT]: '项目面谈',
    [INTERVIEW_TYPE.SPECIAL]: '专项面谈'
  }
  return typeMap[type] || type
}

/**
 * 格式化面谈方式
 * @param {String} method 方式
 * @returns {String}
 */
export function formatInterviewMethod(method) {
  const methodMap = {
    [INTERVIEW_METHOD.FACE_TO_FACE]: '面对面',
    [INTERVIEW_METHOD.VIDEO]: '视频',
    [INTERVIEW_METHOD.PHONE]: '电话',
    [INTERVIEW_METHOD.ONLINE]: '在线'
  }
  return methodMap[method] || method
}

/**
 * 格式化优先级
 * @param {String} priority 优先级
 * @returns {String}
 */
export function formatPriorityLevel(priority) {
  const priorityMap = {
    [PRIORITY_LEVEL.HIGH]: '高',
    [PRIORITY_LEVEL.MEDIUM]: '中',
    [PRIORITY_LEVEL.LOW]: '低'
  }
  return priorityMap[priority] || priority
}

/**
 * 格式化跟进状态
 * @param {String} status 状态
 * @returns {String}
 */
export function formatFollowUpStatus(status) {
  const statusMap = {
    [FOLLOW_UP_STATUS.PENDING]: '待跟进',
    [FOLLOW_UP_STATUS.IN_PROGRESS]: '跟进中',
    [FOLLOW_UP_STATUS.COMPLETED]: '已完成'
  }
  return statusMap[status] || status
}

/**
 * 格式化绩效等级
 * @param {String} grade 等级
 * @returns {String}
 */
export function formatPerformanceGrade(grade) {
  const gradeMap = {
    [PERFORMANCE_GRADE.EXCELLENT]: '优秀',
    [PERFORMANCE_GRADE.GOOD]: '良好',
    [PERFORMANCE_GRADE.FAIR]: '一般',
    [PERFORMANCE_GRADE.POOR]: '较差'
  }
  return gradeMap[grade] || grade
}

/**
 * 获取状态标签类型
 * @param {String} status 状态
 * @returns {String}
 */
export function getStatusTagType(status) {
  const tagMap = {
    [INTERVIEW_STATUS.PLANNED]: 'info',
    [INTERVIEW_STATUS.SCHEDULED]: 'primary',
    [INTERVIEW_STATUS.ONGOING]: 'warning',
    [INTERVIEW_STATUS.COMPLETED]: 'success',
    [INTERVIEW_STATUS.CANCELLED]: 'danger',
    [INTERVIEW_STATUS.POSTPONED]: 'warning'
  }
  return tagMap[status] || ''
}

/**
 * 获取优先级标签类型
 * @param {String} priority 优先级
 * @returns {String}
 */
export function getPriorityTagType(priority) {
  const tagMap = {
    [PRIORITY_LEVEL.HIGH]: 'danger',
    [PRIORITY_LEVEL.MEDIUM]: 'warning',
    [PRIORITY_LEVEL.LOW]: 'info'
  }
  return tagMap[priority] || ''
}

/**
 * 获取绩效等级标签类型
 * @param {String} grade 等级
 * @returns {String}
 */
export function getGradeTagType(grade) {
  const tagMap = {
    [PERFORMANCE_GRADE.EXCELLENT]: 'success',
    [PERFORMANCE_GRADE.GOOD]: 'primary',
    [PERFORMANCE_GRADE.FAIR]: 'warning',
    [PERFORMANCE_GRADE.POOR]: 'danger'
  }
  return tagMap[grade] || ''
}

/**
 * 验证面谈数据
 * @param {Object} data 面谈数据
 * @returns {Object} 验证结果
 */
export function validateInterviewData(data) {
  const errors = []

  if (!data.interviewTitle) {
    errors.push('面谈标题不能为空')
  }

  if (!data.interviewType) {
    errors.push('面谈类型不能为空')
  }

  if (!data.intervieweeId) {
    errors.push('被面谈人不能为空')
  }

  if (!data.interviewerId) {
    errors.push('面谈官不能为空')
  }

  if (data.plannedStartTime && data.plannedEndTime) {
    if (new Date(data.plannedStartTime) >= new Date(data.plannedEndTime)) {
      errors.push('开始时间必须早于结束时间')
    }
  }

  return {
    valid: errors.length === 0,
    errors
  }
}
