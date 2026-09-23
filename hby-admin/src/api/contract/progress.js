import request from '@/utils/request'

/**
 * 进度管理API接口
 * 基于达梦数据库 project_progress 表结构
 * @author 示例云开发团队
 * @since 2025-01-21
 */

// ==================== 进度管理 ====================

/**
 * 分页查询进度记录列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProgressPage(params) {
  return request({
    url: '/contract/progress/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID查询进度记录详情
 * @param {String} id 进度记录ID
 * @returns {Promise} 进度详情
 */
export function getProgressById(id) {
  return request({
    url: `/contract/progress/${id}`,
    method: 'get'
  })
}

/**
 * 新增进度记录
 * @param {Object} data 进度数据
 * @returns {Promise} 创建结果
 */
export function createProgress(data) {
  return request({
    url: '/contract/progress/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 修改进度记录
 * @param {String} id 进度记录ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateProgress(id, data) {
  return request({
    url: `/contract/progress/update/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除进度记录
 * @param {String} id 进度记录ID
 * @returns {Promise} 删除结果
 */
export function deleteProgress(id) {
  return request({
    url: `/contract/progress/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除进度记录
 * @param {Array} ids 进度记录ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteProgress(ids) {
  return request({
    url: '/contract/progress/batch',
    method: 'delete',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据项目ID查询进度记录列表
 * @param {String} projectId 项目ID
 * @returns {Promise} 进度列表
 */
export function getProgressByProjectId(projectId) {
  return request({
    url: `/contract/progress/project/${projectId}`,
    method: 'get'
  })
}

/**
 * 根据进度类型查询进度记录列表
 * @param {Number} progressType 进度类型
 * @returns {Promise} 进度列表
 */
export function getProgressByType(progressType) {
  return request({
    url: `/contract/progress/type/${progressType}`,
    method: 'get'
  })
}

/**
 * 根据进度状态查询进度记录列表
 * @param {Number} progressStatus 进度状态
 * @returns {Promise} 进度列表
 */
export function getProgressByStatus(progressStatus) {
  return request({
    url: `/contract/progress/status/${progressStatus}`,
    method: 'get'
  })
}

/**
 * 根据负责人ID查询进度记录列表
 * @param {String} managerId 负责人ID
 * @returns {Promise} 进度列表
 */
export function getProgressByManagerId(managerId) {
  return request({
    url: `/contract/progress/manager/${managerId}`,
    method: 'get'
  })
}

/**
 * 查询里程碑进度记录列表
 * @param {String} projectId 项目ID
 * @returns {Promise} 里程碑列表
 */
export function getMilestones(projectId) {
  return request({
    url: `/contract/progress/milestones/${projectId}`,
    method: 'get'
  })
}

/**
 * 查询关键路径进度记录列表
 * @param {String} projectId 项目ID
 * @returns {Promise} 关键路径列表
 */
export function getCriticalPath(projectId) {
  return request({
    url: `/contract/progress/critical-path/${projectId}`,
    method: 'get'
  })
}

/**
 * 根据项目ID统计总体进度
 * @param {String} projectId 项目ID
 * @returns {Promise} 总体进度
 */
export function calculateOverallProgress(projectId) {
  return request({
    url: `/contract/progress/overall-progress/${projectId}`,
    method: 'get'
  })
}

/**
 * 查询延期的进度记录列表
 * @returns {Promise} 延期进度列表
 */
export function getDelayedProgress() {
  return request({
    url: '/contract/progress/delayed',
    method: 'get'
  })
}

/**
 * 查询提前完成的进度记录列表
 * @returns {Promise} 提前完成进度列表
 */
export function getEarlyCompletedProgress() {
  return request({
    url: '/contract/progress/early-completed',
    method: 'get'
  })
}

/**
 * 查询高优先级进度记录列表
 * @returns {Promise} 高优先级进度列表
 */
export function getHighPriorityProgress() {
  return request({
    url: '/contract/progress/high-priority',
    method: 'get'
  })
}

/**
 * 查询即将到期的进度记录列表
 * @param {Number} days 天数
 * @returns {Promise} 即将到期进度列表
 */
export function getUpcomingDeadlines(days = 7) {
  return request({
    url: '/contract/progress/upcoming-deadlines',
    method: 'get',
    params: { days }
  })
}

/**
 * 批量更新进度状态
 * @param {Array} ids 进度ID列表
 * @param {Number} progressStatus 新状态
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function batchUpdateProgressStatus(ids, progressStatus, updateBy) {
  return request({
    url: '/contract/progress/batch-status',
    method: 'put',
    data: ids,
    params: { progressStatus, updateBy },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量更新实际进度
 * @param {Array} ids 进度ID列表
 * @param {Number} actualProgress 实际进度
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function batchUpdateActualProgress(ids, actualProgress, updateBy) {
  return request({
    url: '/contract/progress/batch-actual-progress',
    method: 'put',
    data: ids,
    params: { actualProgress, updateBy },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目进度树形结构
 * @param {String} projectId 项目ID
 * @returns {Promise} 进度树
 */
export function getProgressTree(projectId) {
  return request({
    url: `/contract/progress/tree/${projectId}`,
    method: 'get'
  })
}

/**
 * 更新进度
 * @param {String} id 进度记录ID
 * @param {Number} actualProgress 实际进度
 * @param {String} completionDescription 完成情况说明
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function updateProgressStatus(id, actualProgress, completionDescription, updateBy) {
  return request({
    url: `/contract/progress/update-progress/${id}`,
    method: 'put',
    params: { actualProgress, completionDescription, updateBy }
  })
}

/**
 * 开始进度
 * @param {String} id 进度记录ID
 * @param {Date} actualStartTime 实际开始时间
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function startProgress(id, actualStartTime, updateBy) {
  return request({
    url: `/contract/progress/start/${id}`,
    method: 'post',
    params: { actualStartTime, updateBy }
  })
}

/**
 * 完成进度
 * @param {String} id 进度记录ID
 * @param {Date} actualEndTime 实际结束时间
 * @param {String} completionDescription 完成情况说明
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function completeProgress(id, actualEndTime, completionDescription, updateBy) {
  return request({
    url: `/contract/progress/complete/${id}`,
    method: 'post',
    params: { actualEndTime, completionDescription, updateBy }
  })
}

/**
 * 暂停进度
 * @param {String} id 进度记录ID
 * @param {String} reason 暂停原因
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function pauseProgress(id, reason, updateBy) {
  return request({
    url: `/contract/progress/pause/${id}`,
    method: 'post',
    params: { reason, updateBy }
  })
}

/**
 * 恢复进度
 * @param {String} id 进度记录ID
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function resumeProgress(id, updateBy) {
  return request({
    url: `/contract/progress/resume/${id}`,
    method: 'post',
    params: { updateBy }
  })
}

/**
 * 取消进度
 * @param {String} id 进度记录ID
 * @param {String} reason 取消原因
 * @param {String} updateBy 更新人
 * @returns {Promise} 更新结果
 */
export function cancelProgress(id, reason, updateBy) {
  return request({
    url: `/contract/progress/cancel/${id}`,
    method: 'post',
    params: { reason, updateBy }
  })
}

/**
 * 根据关键词搜索进度记录
 * @param {String} keyword 关键词
 * @param {Number} limit 限制数量
 * @returns {Promise} 进度列表
 */
export function searchProgress(keyword, limit = 10) {
  return request({
    url: '/contract/progress/search',
    method: 'get',
    params: { keyword, limit }
  })
}

/**
 * 查询进度统计信息
 * @returns {Promise} 统计信息
 */
export function getProgressStatistics() {
  return request({
    url: '/contract/progress/statistics',
    method: 'get'
  })
}

/**
 * 获取项目进度报告
 * @param {String} projectId 项目ID
 * @returns {Promise} 进度报告
 */
export function getProjectProgressReport(projectId) {
  return request({
    url: `/contract/progress/project-report/${projectId}`,
    method: 'get'
  })
}

/**
 * 生成进度编号
 * @returns {Promise} 进度编号
 */
export function generateProgressNo() {
  return request({
    url: '/contract/progress/generate-progress-no',
    method: 'get'
  })
}
