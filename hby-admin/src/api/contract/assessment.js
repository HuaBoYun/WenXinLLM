import request from '@/utils/request'

/**
 * 项目考核API接口
 * @author 示例云开发团队
 * @since 2025-01-21
 */

// ==================== 考核管理接口 ====================

/**
 * 启动项目考核
 * @param {Object} data 考核请求参数
 * @returns {Promise} 考核结果
 */
export function startAssessment(data) {
  return request({
    url: '/contract/assessment/start',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询考核结果列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getAssessmentList(params) {
  return request({
    url: '/contract/assessment/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询指定项目的考核结果
 * @param {String} projectId 项目ID
 * @param {String} period 考核期间
 * @returns {Promise} 考核结果列表
 */
export function getAssessmentResults(projectId, period) {
  return request({
    url: `/contract/assessment/result/${projectId}`,
    method: 'get',
    params: { period }
  })
}

/**
 * 获取考核详情
 * @param {Number} assessmentId 考核记录ID
 * @returns {Promise} 考核详情
 */
export function getAssessmentDetail(assessmentId) {
  return request({
    url: `/contract/assessment/detail/${assessmentId}`,
    method: 'get'
  })
}

/**
 * 生成考核报告
 * @param {Number} assessmentId 考核记录ID
 * @param {Object} params 报告参数
 * @returns {Promise} 报告数据
 */
export function generateAssessmentReport(assessmentId, params = {}) {
  return request({
    url: `/contract/assessment/report/${assessmentId}`,
    method: 'post',
    params: {
      reportType: params.reportType || 'detailed',
      includeChart: params.includeChart !== false,
      includeSuggestions: params.includeSuggestions !== false
    }
  })
}

/**
 * 审核考核结果
 * @param {Number} assessmentId 考核记录ID
 * @param {Object} data 审核数据
 * @returns {Promise} 审核结果
 */
export function reviewAssessment(assessmentId, data) {
  return request({
    url: `/contract/assessment/review/${assessmentId}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 考核指标管理接口 ====================

/**
 * 获取指标体系
 * @param {Object} params 查询参数
 * @returns {Promise} 指标列表
 */
export function getIndicators(params = {}) {
  return request({
    url: '/contract/assessment/indicators',
    method: 'get',
    params: {
      isActive: params.isActive !== false,
      parentId: params.parentId
    }
  })
}

// 删除重复的函数定义，保留后面更完整的版本

// ==================== 考核统计分析接口 ====================

/**
 * 获取考核统计概览
 * @param {Object} params 查询参数
 * @returns {Promise} 统计数据
 */
export function getAssessmentStatistics(params = {}) {
  return request({
    url: '/contract/assessment/statistics/overview',
    method: 'get',
    params: params
  })
}

/**
 * 获取项目考核排名
 * @param {Object} params 查询参数
 * @returns {Promise} 排名列表
 */
export function getAssessmentRanking(params) {
  return request({
    url: '/contract/assessment/statistics/ranking',
    method: 'get',
    params: params
  })
}

/**
 * 获取指标分析报告
 * @param {Object} params 查询参数
 * @returns {Promise} 分析数据
 */
export function getIndicatorAnalysis(params) {
  return request({
    url: '/contract/assessment/statistics/indicators',
    method: 'get',
    params: params
  })
}

// ==================== 数据同步接口 ====================

/**
 * 批量同步考核数据
 * @param {Object} params 同步参数
 * @returns {Promise} 同步结果
 */
export function syncAssessmentData(params) {
  return request({
    url: '/contract/assessment/sync/batch',
    method: 'post',
    params: params
  })
}

// ==================== 数据导出接口 ====================

/**
 * 导出考核结果
 * @param {Object} params 导出参数
 * @returns {Promise} 导出结果
 */
export function exportAssessmentResults(params) {
  return request({
    url: '/contract/assessment/export/results',
    method: 'post',
    params: params
  })
}

/**
 * 下载导出文件
 * @param {String} fileId 文件ID
 * @returns {Promise} 文件信息
 */
export function downloadExportFile(fileId) {
  return request({
    url: `/contract/assessment/download/${fileId}`,
    method: 'get'
  })
}

// ==================== 辅助方法 ====================

/**
 * 获取考核类型选项
 * @returns {Array} 考核类型选项
 */
export function getAssessmentTypeOptions() {
  return [
    { value: 1, label: '月度考核' },
    { value: 2, label: '季度考核' },
    { value: 3, label: '年度考核' },
    { value: 4, label: '项目完成考核' }
  ]
}

/**
 * 获取考核状态选项
 * @returns {Array} 考核状态选项
 */
export function getAssessmentStatusOptions() {
  return [
    { value: 1, label: '进行中' },
    { value: 2, label: '已完成' },
    { value: 3, label: '已审核' }
  ]
}

/**
 * 获取考核等级选项
 * @returns {Array} 考核等级选项
 */
export function getAssessmentLevelOptions() {
  return [
    { value: '优秀', label: '优秀' },
    { value: '良好', label: '良好' },
    { value: '一般', label: '一般' },
    { value: '合格', label: '合格' },
    { value: '不合格', label: '不合格' }
  ]
}

/**
 * 获取考核等级颜色
 * @param {String} level 考核等级
 * @returns {String} 颜色值
 */
export function getAssessmentLevelColor(level) {
  const colorMap = {
    '优秀': '#67C23A',
    '良好': '#409EFF',
    '一般': '#E6A23C',
    '合格': '#F56C6C',
    '不合格': '#909399'
  }
  return colorMap[level] || '#909399'
}

/**
 * 格式化考核分数
 * @param {Number} score 分数
 * @returns {String} 格式化后的分数
 */
export function formatAssessmentScore(score) {
  if (score == null) return '--'
  return Number(score).toFixed(2)
}

// =====================================================
// 考核指标管理相关接口
// =====================================================

/**
 * 获取指标树
 * @returns {Promise} 指标树数据
 */
export function getIndicatorTree() {
  return request({
    url: '/contract/assessment/indicator/tree',
    method: 'post'
  })
}

/**
 * 保存指标
 * @param {Object} data 指标数据
 * @returns {Promise} 保存结果
 */
export function saveIndicator(data) {
  return request({
    url: '/contract/assessment/indicator/save',
    method: 'post',
    data: data
  })
}

/**
 * 更新指标
 * @param {Object} data 指标数据
 * @returns {Promise} 更新结果
 */
export function updateIndicator(data) {
  return request({
    url: '/contract/assessment/indicator/update',
    method: 'post',
    data: data
  })
}

/**
 * 删除指标
 * @param {Number} id 指标ID
 * @returns {Promise} 删除结果
 */
export function deleteIndicator(id) {
  return request({
    url: `/contract/assessment/indicator/delete/${id}`,
    method: 'post'
  })
}

/**
 * 更新指标状态
 * @param {Number} id 指标ID
 * @param {Number} status 状态
 * @returns {Promise} 更新结果
 */
export function updateIndicatorStatus(id, status) {
  return request({
    url: '/contract/assessment/indicator/status',
    method: 'post',
    data: { id, status }
  })
}
