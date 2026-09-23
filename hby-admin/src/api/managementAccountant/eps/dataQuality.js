import request from '@/utils/request'

// ==================== 常量定义 ====================

// 数据质量类型
export const DATA_QUALITY_TYPES = {
  COMPLETENESS: 'COMPLETENESS', // 完整性
  ACCURACY: 'ACCURACY', // 准确性
  CONSISTENCY: 'CONSISTENCY', // 一致性
  VALIDITY: 'VALIDITY', // 有效性
  UNIQUENESS: 'UNIQUENESS', // 唯一性
  TIMELINESS: 'TIMELINESS' // 及时性
}

// 数据质量分类
export const DATA_QUALITY_CATEGORIES = {
  DATA: 'DATA', // 数据质量
  PROCESS: 'PROCESS', // 流程质量
  SYSTEM: 'SYSTEM' // 系统质量
}

// 检查状态
export const CHECK_STATUS = {
  PENDING: 'PENDING', // 待检查
  CHECKING: 'CHECKING', // 检查中
  COMPLETED: 'COMPLETED', // 已完成
  FAILED: 'FAILED' // 检查失败
}

// 检查结果
export const CHECK_RESULT = {
  PASS: 'PASS', // 通过
  FAIL: 'FAIL', // 失败
  WARNING: 'WARNING' // 警告
}

// 质量等级
export const QUALITY_LEVEL = {
  EXCELLENT: 'EXCELLENT', // 优秀
  GOOD: 'GOOD', // 良好
  FAIR: 'FAIR', // 一般
  POOR: 'POOR' // 较差
}

// 修复状态
export const FIX_STATUS = {
  PENDING: 'PENDING', // 待修复
  FIXING: 'FIXING', // 修复中
  FIXED: 'FIXED', // 已修复
  IGNORED: 'IGNORED' // 已忽略
}

// ==================== API接口函数 ====================

// 基础CRUD操作
export function createDataQuality(data) {
  return request({
    url: '/api/eps/data-quality/create',
    method: 'post',
    data
  })
}

export function updateDataQuality(data) {
  return request({
    url: '/api/eps/data-quality/update',
    method: 'put',
    data
  })
}

export function deleteDataQuality(qualityId) {
  return request({
    url: `/api/eps/data-quality/delete/${qualityId}`,
    method: 'delete'
  })
}

export function getDataQualityById(qualityId) {
  return request({
    url: `/api/eps/data-quality/get/${qualityId}`,
    method: 'get'
  })
}

export function getDataQualityByCode(qualityCode) {
  return request({
    url: `/api/eps/data-quality/get-by-code/${qualityCode}`,
    method: 'get'
  })
}

// 查询操作
export function getDataQualityByType(qualityType) {
  return request({
    url: `/api/eps/data-quality/list-by-type/${qualityType}`,
    method: 'get'
  })
}

export function getDataQualityByCategory(qualityCategory) {
  return request({
    url: `/api/eps/data-quality/list-by-category/${qualityCategory}`,
    method: 'get'
  })
}

export function getDataQualityByModule(qualityModule) {
  return request({
    url: `/api/eps/data-quality/list-by-module/${qualityModule}`,
    method: 'get'
  })
}

export function getDataQualityByDataSourceId(dataSourceId) {
  return request({
    url: `/api/eps/data-quality/list-by-data-source/${dataSourceId}`,
    method: 'get'
  })
}

export function getDataQualityByCheckStatus(checkStatus) {
  return request({
    url: `/api/eps/data-quality/list-by-check-status/${checkStatus}`,
    method: 'get'
  })
}

export function getDataQualityByCheckResult(checkResult) {
  return request({
    url: `/api/eps/data-quality/list-by-check-result/${checkResult}`,
    method: 'get'
  })
}

export function getPendingCheckDataQuality() {
  return request({
    url: '/api/eps/data-quality/list-pending-check',
    method: 'get'
  })
}

export function getFailedCheckDataQuality() {
  return request({
    url: '/api/eps/data-quality/list-failed-check',
    method: 'get'
  })
}

export function getNeedFixDataQuality() {
  return request({
    url: '/api/eps/data-quality/list-need-fix',
    method: 'get'
  })
}

// 分页查询
export function getDataQualityPage(params) {
  return request({
    url: '/api/eps/data-quality/page',
    method: 'get',
    params
  })
}

// 数据质量检查操作
export function executeDataQualityCheck(qualityId) {
  return request({
    url: `/api/eps/data-quality/execute-check/${qualityId}`,
    method: 'post'
  })
}

export function batchExecuteDataQualityCheck(qualityIds) {
  return request({
    url: '/api/eps/data-quality/batch-execute-check',
    method: 'post',
    data: qualityIds
  })
}

export function autoExecuteDataQualityCheck() {
  return request({
    url: '/api/eps/data-quality/auto-execute-check',
    method: 'post'
  })
}

// 数据质量修复操作
export function fixDataQualityIssue(qualityId) {
  return request({
    url: `/api/eps/data-quality/fix-issue/${qualityId}`,
    method: 'post'
  })
}

export function batchFixDataQualityIssue(qualityIds) {
  return request({
    url: '/api/eps/data-quality/batch-fix-issue',
    method: 'post',
    data: qualityIds
  })
}

export function manualFixDataQualityIssue(qualityId, fixSolution) {
  return request({
    url: `/api/eps/data-quality/manual-fix-issue/${qualityId}`,
    method: 'post',
    params: { fixSolution }
  })
}

export function ignoreDataQualityIssue(qualityId, reason) {
  return request({
    url: `/api/eps/data-quality/ignore-issue/${qualityId}`,
    method: 'post',
    params: { reason }
  })
}

// 批量操作
export function batchEnableDataQuality(qualityIds) {
  return request({
    url: '/api/eps/data-quality/batch-enable',
    method: 'put',
    data: qualityIds
  })
}

export function batchDisableDataQuality(qualityIds) {
  return request({
    url: '/api/eps/data-quality/batch-disable',
    method: 'put',
    data: qualityIds
  })
}

export function batchDeleteDataQuality(qualityIds) {
  return request({
    url: '/api/eps/data-quality/batch-delete',
    method: 'delete',
    data: qualityIds
  })
}

// 搜索功能
export function searchDataQualityByKeyword(keyword) {
  return request({
    url: '/api/eps/data-quality/search-by-keyword',
    method: 'get',
    params: { keyword }
  })
}

export function searchDataQualityByTags(tags) {
  return request({
    url: '/api/eps/data-quality/search-by-tags',
    method: 'post',
    data: tags
  })
}

export function findSimilarDataQuality(qualityId) {
  return request({
    url: `/api/eps/data-quality/find-similar/${qualityId}`,
    method: 'get'
  })
}

export function findPopularDataQuality(limit = 10) {
  return request({
    url: '/api/eps/data-quality/find-popular',
    method: 'get',
    params: { limit }
  })
}

export function findHighRiskDataQuality() {
  return request({
    url: '/api/eps/data-quality/find-high-risk',
    method: 'get'
  })
}

// 验证功能
export function validateDataQuality(data) {
  return request({
    url: '/api/eps/data-quality/validate',
    method: 'post',
    data
  })
}

export function testDataQualityConnection(qualityId) {
  return request({
    url: `/api/eps/data-quality/test-connection/${qualityId}`,
    method: 'post'
  })
}

// 统计分析功能
export function countDataQuality() {
  return request({
    url: '/api/eps/data-quality/count',
    method: 'get'
  })
}

export function countDataQualityByType(qualityType) {
  return request({
    url: '/api/eps/data-quality/count-by-type',
    method: 'get',
    params: { qualityType }
  })
}

export function countDataQualityByCheckStatus(checkStatus) {
  return request({
    url: '/api/eps/data-quality/count-by-status',
    method: 'get',
    params: { checkStatus }
  })
}

export function getDataQualitySystemOverview() {
  return request({
    url: '/api/eps/data-quality/system-overview',
    method: 'get'
  })
}

// ==================== 工具函数 ====================

// 格式化数据质量类型
export function formatDataQualityType(type) {
  const typeMap = {
    COMPLETENESS: '完整性',
    ACCURACY: '准确性',
    CONSISTENCY: '一致性',
    VALIDITY: '有效性',
    UNIQUENESS: '唯一性',
    TIMELINESS: '及时性'
  }
  return typeMap[type] || type
}

// 格式化数据质量分类
export function formatDataQualityCategory(category) {
  const categoryMap = {
    DATA: '数据质量',
    PROCESS: '流程质量',
    SYSTEM: '系统质量'
  }
  return categoryMap[category] || category
}

// 格式化检查状态
export function formatCheckStatus(status) {
  const statusMap = {
    PENDING: '待检查',
    CHECKING: '检查中',
    COMPLETED: '已完成',
    FAILED: '检查失败'
  }
  return statusMap[status] || status
}

// 格式化检查结果
export function formatCheckResult(result) {
  const resultMap = {
    PASS: '通过',
    FAIL: '失败',
    WARNING: '警告'
  }
  return resultMap[result] || result
}

// 格式化质量等级
export function formatQualityLevel(level) {
  const levelMap = {
    EXCELLENT: '优秀',
    GOOD: '良好',
    FAIR: '一般',
    POOR: '较差'
  }
  return levelMap[level] || level
}

// 格式化修复状态
export function formatFixStatus(status) {
  const statusMap = {
    PENDING: '待修复',
    FIXING: '修复中',
    FIXED: '已修复',
    IGNORED: '已忽略'
  }
  return statusMap[status] || status
}

// 获取检查状态标签类型
export function getCheckStatusTagType(status) {
  const typeMap = {
    PENDING: 'info',
    CHECKING: 'warning',
    COMPLETED: 'success',
    FAILED: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取检查结果标签类型
export function getCheckResultTagType(result) {
  const typeMap = {
    PASS: 'success',
    FAIL: 'danger',
    WARNING: 'warning'
  }
  return typeMap[result] || 'info'
}

// 获取质量等级标签类型
export function getQualityLevelTagType(level) {
  const typeMap = {
    EXCELLENT: 'success',
    GOOD: 'primary',
    FAIR: 'warning',
    POOR: 'danger'
  }
  return typeMap[level] || 'info'
}

// 获取修复状态标签类型
export function getFixStatusTagType(status) {
  const typeMap = {
    PENDING: 'info',
    FIXING: 'warning',
    FIXED: 'success',
    IGNORED: 'info'
  }
  return typeMap[status] || 'info'
}

// ==================== 快捷操作 ====================

// 快速创建数据质量
export function quickCreateDataQuality(qualityName, qualityType, dataSourceId) {
  const data = {
    qualityCode: `DQ_${Date.now()}`,
    qualityName,
    qualityType,
    qualityCategory: 'DATA',
    dataSourceId,
    checkStatus: 'PENDING',
    status: 'ACTIVE',
    autoFix: false,
    sendNotification: true
  }
  return createDataQuality(data)
}

// 快速启用数据质量
export function quickEnableDataQuality(qualityId) {
  return batchEnableDataQuality([qualityId])
}

// 快速禁用数据质量
export function quickDisableDataQuality(qualityId) {
  return batchDisableDataQuality([qualityId])
}

// 获取数据质量选项
export function getDataQualityOptions() {
  return {
    qualityTypes: Object.keys(DATA_QUALITY_TYPES).map(key => ({
      value: key,
      label: formatDataQualityType(key)
    })),
    qualityCategories: Object.keys(DATA_QUALITY_CATEGORIES).map(key => ({
      value: key,
      label: formatDataQualityCategory(key)
    })),
    checkStatuses: Object.keys(CHECK_STATUS).map(key => ({
      value: key,
      label: formatCheckStatus(key)
    })),
    checkResults: Object.keys(CHECK_RESULT).map(key => ({
      value: key,
      label: formatCheckResult(key)
    })),
    qualityLevels: Object.keys(QUALITY_LEVEL).map(key => ({
      value: key,
      label: formatQualityLevel(key)
    })),
    fixStatuses: Object.keys(FIX_STATUS).map(key => ({
      value: key,
      label: formatFixStatus(key)
    }))
  }
}
