import request from '@/utils/request'

// 分页查询预算模板
export function getBudgetTemplatePage(params) {
  return request({
    url: '/accountant/eps/budget-template/page',
    method: 'get',
    params
  })
}

// 创建预算模板
export function createBudgetTemplate(data) {
  return request({
    url: '/accountant/eps/budget-template',
    method: 'post',
    data
  })
}

// 更新预算模板
export function updateBudgetTemplate(data) {
  return request({
    url: '/accountant/eps/budget-template',
    method: 'put',
    data
  })
}

// 删除预算模板
export function deleteBudgetTemplate(templateId) {
  return request({
    url: `/accountant/eps/budget-template/${templateId}`,
    method: 'delete'
  })
}

// 批量删除预算模板
export function batchDeleteBudgetTemplate(templateIds) {
  return request({
    url: '/accountant/eps/budget-template/batch',
    method: 'delete',
    data: templateIds
  })
}

// 根据ID查询预算模板详情
export function getBudgetTemplateById(templateId) {
  return request({
    url: `/accountant/eps/budget-template/${templateId}`,
    method: 'get'
  })
}

// 根据模板编码查询预算模板
export function getBudgetTemplateByCode(templateCode) {
  return request({
    url: `/accountant/eps/budget-template/code/${templateCode}`,
    method: 'get'
  })
}

// 根据预算体系ID查询模板列表
export function getBudgetTemplatesBySystemId(systemId) {
  return request({
    url: `/accountant/eps/budget-template/system/${systemId}`,
    method: 'get'
  })
}

// 查询默认模板
export function getDefaultBudgetTemplate(params) {
  return request({
    url: '/accountant/eps/budget-template/default',
    method: 'get',
    params
  })
}

// 设置默认模板
export function setDefaultBudgetTemplate(templateId, systemId, templateCategory) {
  return request({
    url: `/accountant/eps/budget-template/default/${templateId}`,
    method: 'put',
    params: { systemId, templateCategory }
  })
}

// 激活预算模板
export function activateBudgetTemplate(templateId) {
  return request({
    url: `/accountant/eps/budget-template/activate/${templateId}`,
    method: 'put'
  })
}

// 停用预算模板
export function deactivateBudgetTemplate(templateId) {
  return request({
    url: `/accountant/eps/budget-template/deactivate/${templateId}`,
    method: 'put'
  })
}

// 归档预算模板
export function archiveBudgetTemplate(templateId) {
  return request({
    url: `/accountant/eps/budget-template/archive/${templateId}`,
    method: 'put'
  })
}

// 复制预算模板
export function copyBudgetTemplate(data) {
  return request({
    url: '/accountant/eps/budget-template/copy',
    method: 'post',
    params: data
  })
}

// 检查模板编码是否存在
export function checkTemplateCodeExists(params) {
  return request({
    url: '/accountant/eps/budget-template/check-code',
    method: 'get',
    params
  })
}

// 批量更新状态
export function batchUpdateStatus(templateIds, status, updatedBy) {
  return request({
    url: '/accountant/eps/budget-template/batch-status',
    method: 'put',
    data: templateIds,
    params: { status, updatedBy }
  })
}

// 根据模板类型查询模板
export function getBudgetTemplatesByType(templateType, systemId) {
  return request({
    url: `/accountant/eps/budget-template/type/${templateType}`,
    method: 'get',
    params: { systemId }
  })
}

// 根据模板分类查询模板
export function getBudgetTemplatesByCategory(templateCategory, systemId) {
  return request({
    url: `/accountant/eps/budget-template/category/${templateCategory}`,
    method: 'get',
    params: { systemId }
  })
}

// 查询热门模板
export function getPopularTemplates(params) {
  return request({
    url: '/accountant/eps/budget-template/popular',
    method: 'get',
    params
  })
}

// 查询最近使用的模板
export function getRecentlyUsedTemplates(params) {
  return request({
    url: '/accountant/eps/budget-template/recent',
    method: 'get',
    params
  })
}

// 根据标签查询模板
export function getBudgetTemplatesByTags(params) {
  return request({
    url: '/accountant/eps/budget-template/tags',
    method: 'get',
    params
  })
}

// 查询共享模板
export function getSharedTemplates(params) {
  return request({
    url: '/accountant/eps/budget-template/shared',
    method: 'get',
    params
  })
}

// 查询系统模板
export function getSystemTemplates() {
  return request({
    url: '/accountant/eps/budget-template/system',
    method: 'get'
  })
}

// 导入模板
export function importTemplate(data) {
  return request({
    url: '/accountant/eps/budget-template/import',
    method: 'post',
    params: data
  })
}

// 导出模板
export function exportTemplate(templateId) {
  return request({
    url: `/accountant/eps/budget-template/export/${templateId}`,
    method: 'get'
  })
}

// 验证模板结构
export function validateTemplateStructure(templateStructure) {
  return request({
    url: '/accountant/eps/budget-template/validate',
    method: 'post',
    params: { templateStructure }
  })
}
