import request from '@/utils/request'

// 分页查询预算体系
export function getBudgetSystemPage(params) {
  return request({
    url: '/accountant/eps/budget-system/page',
    method: 'get',
    params
  })
}

// 创建预算体系
export function createBudgetSystem(data) {
  return request({
    url: '/accountant/eps/budget-system',
    method: 'post',
    data
  })
}

// 更新预算体系
export function updateBudgetSystem(data) {
  return request({
    url: '/accountant/eps/budget-system',
    method: 'put',
    data
  })
}

// 删除预算体系
export function deleteBudgetSystem(systemId) {
  return request({
    url: `/accountant/eps/budget-system/${systemId}`,
    method: 'delete'
  })
}

// 批量删除预算体系
export function batchDeleteBudgetSystem(systemIds) {
  return request({
    url: '/accountant/eps/budget-system/batch',
    method: 'delete',
    data: systemIds
  })
}

// 根据ID查询预算体系详情
export function getBudgetSystemById(systemId) {
  return request({
    url: `/accountant/eps/budget-system/${systemId}`,
    method: 'get'
  })
}

// 根据体系编码查询预算体系
export function getBudgetSystemByCode(systemCode) {
  return request({
    url: `/accountant/eps/budget-system/code/${systemCode}`,
    method: 'get'
  })
}

// 根据组织ID查询预算体系列表
export function getBudgetSystemsByOrganization(organizationId) {
  return request({
    url: `/accountant/eps/budget-system/organization/${organizationId}`,
    method: 'get'
  })
}

// 查询默认预算体系
export function getDefaultBudgetSystem(organizationId) {
  return request({
    url: `/accountant/eps/budget-system/default/${organizationId}`,
    method: 'get'
  })
}

// 设置默认预算体系
export function setDefaultBudgetSystem(systemId, organizationId) {
  return request({
    url: `/accountant/eps/budget-system/default/${systemId}/${organizationId}`,
    method: 'put'
  })
}

// 激活预算体系
export function activateBudgetSystem(systemId) {
  return request({
    url: `/accountant/eps/budget-system/activate/${systemId}`,
    method: 'put'
  })
}

// 停用预算体系
export function deactivateBudgetSystem(systemId) {
  return request({
    url: `/accountant/eps/budget-system/deactivate/${systemId}`,
    method: 'put'
  })
}

// 归档预算体系
export function archiveBudgetSystem(systemId) {
  return request({
    url: `/accountant/eps/budget-system/archive/${systemId}`,
    method: 'put'
  })
}

// 复制预算体系
export function copyBudgetSystem(data) {
  return request({
    url: '/accountant/eps/budget-system/copy',
    method: 'post',
    params: data
  })
}

// 检查体系编码是否存在
export function checkSystemCodeExists(params) {
  return request({
    url: '/accountant/eps/budget-system/check-code',
    method: 'get',
    params
  })
}

// 批量更新状态
export function batchUpdateStatus(systemIds, status, updatedBy) {
  return request({
    url: '/accountant/eps/budget-system/batch-status',
    method: 'put',
    data: systemIds,
    params: { status, updatedBy }
  })
}

// 根据预算年度查询预算体系
export function getBudgetSystemsByFiscalYear(fiscalYear, organizationId) {
  return request({
    url: `/accountant/eps/budget-system/fiscal-year/${fiscalYear}`,
    method: 'get',
    params: { organizationId }
  })
}
