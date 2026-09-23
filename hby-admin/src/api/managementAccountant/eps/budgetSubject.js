import request from '@/utils/request'

// 预算科目管理API

/**
 * 分页查询预算科目
 */
export function queryBudgetSubjectPage(params) {
  return request({
    url: '/accountant/eps/budget-subject/page',
    method: 'get',
    params
  })
}

/**
 * 创建预算科目
 */
export function createBudgetSubject(data) {
  return request({
    url: '/accountant/eps/budget-subject',
    method: 'post',
    data
  })
}

/**
 * 更新预算科目
 */
export function updateBudgetSubject(data) {
  return request({
    url: '/accountant/eps/budget-subject',
    method: 'put',
    data
  })
}

/**
 * 删除预算科目
 */
export function deleteBudgetSubject(subjectId) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}`,
    method: 'delete'
  })
}

/**
 * 批量删除预算科目
 */
export function batchDeleteBudgetSubjects(subjectIds) {
  return request({
    url: '/accountant/eps/budget-subject/batch',
    method: 'delete',
    data: subjectIds
  })
}

/**
 * 根据ID查询预算科目详情
 */
export function getBudgetSubjectById(subjectId) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}`,
    method: 'get'
  })
}

/**
 * 根据编码查询预算科目
 */
export function getBudgetSubjectByCode(subjectCode) {
  return request({
    url: `/accountant/eps/budget-subject/code/${subjectCode}`,
    method: 'get'
  })
}

/**
 * 获取预算科目树
 */
export function getBudgetSubjectTree(params) {
  return request({
    url: '/accountant/eps/budget-subject/tree',
    method: 'get',
    params
  })
}

/**
 * 根据预算体系查询科目列表
 */
export function getBudgetSubjectsBySystemId(systemId) {
  return request({
    url: `/accountant/eps/budget-subject/system/${systemId}`,
    method: 'get'
  })
}

/**
 * 根据科目类型查询科目列表
 */
export function getBudgetSubjectsByType(subjectType, systemId) {
  return request({
    url: `/accountant/eps/budget-subject/type/${subjectType}`,
    method: 'get',
    params: { systemId }
  })
}

/**
 * 获取子科目列表
 */
export function getChildBudgetSubjects(parentSubjectId) {
  return request({
    url: `/accountant/eps/budget-subject/${parentSubjectId}/children`,
    method: 'get'
  })
}

/**
 * 移动预算科目
 */
export function moveBudgetSubject(subjectId, targetParentId, targetPosition) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}/move`,
    method: 'post',
    params: {
      targetParentId,
      targetPosition
    }
  })
}

/**
 * 复制预算科目
 */
export function copyBudgetSubject(subjectId, params) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}/copy`,
    method: 'post',
    params
  })
}

/**
 * 启用预算科目
 */
export function enableBudgetSubject(subjectId) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}/enable`,
    method: 'post'
  })
}

/**
 * 禁用预算科目
 */
export function disableBudgetSubject(subjectId) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}/disable`,
    method: 'post'
  })
}

/**
 * 获取科目路径
 */
export function getBudgetSubjectPath(subjectId) {
  return request({
    url: `/accountant/eps/budget-subject/${subjectId}/path`,
    method: 'get'
  })
}

/**
 * 验证科目编码
 */
export function validateSubjectCode(subjectCode, excludeId) {
  return request({
    url: '/accountant/eps/budget-subject/validate-code',
    method: 'get',
    params: {
      subjectCode,
      excludeId
    }
  })
}

/**
 * 获取科目统计
 */
export function getBudgetSubjectStatistics(params) {
  return request({
    url: '/accountant/eps/budget-subject/statistics',
    method: 'get',
    params
  })
}

/**
 * 导入预算科目
 */
export function importBudgetSubjects(data) {
  return request({
    url: '/accountant/eps/budget-subject/import',
    method: 'post',
    data
  })
}

/**
 * 导出预算科目
 */
export function exportBudgetSubjects(data) {
  return request({
    url: '/accountant/eps/budget-subject/export',
    method: 'post',
    data
  })
}

/**
 * 批量操作科目
 */
export function batchOperateSubjects(data) {
  return request({
    url: '/accountant/eps/budget-subject/batch-operation',
    method: 'post',
    data
  })
}
