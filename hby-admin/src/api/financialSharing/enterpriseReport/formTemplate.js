import request from '@/utils/request'

/**
 * 查询表单模板列表
 */
export function getFormTemplateList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/getList',
    method: 'post',
    data
  })
}

/**
 * 查询表单模板详情
 */
export function getFormTemplateDetail(templateId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/detail',
    method: 'post',
    data: { templateId }
  })
}

/**
 * 保存表单模板
 */
export function saveFormTemplate(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/save',
    method: 'post',
    data
  })
}

/**
 * 删除表单模板
 */
export function deleteFormTemplate(templateId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/delete',
    method: 'post',
    data: { templateId }
  })
}

/**
 * 设置默认版本
 */
export function setDefaultVersion(templateId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/setDefaultVersion',
    method: 'post',
    data: { templateId }
  })
}

/**
 * 复制表单模板
 */
export function copyFormTemplate(templateId, newTemplateName, newVersionNo) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/copy',
    method: 'post',
    data: { templateId, newTemplateName, newVersionNo }
  })
}

/**
 * 根据表单组ID查询表单模板列表
 */
export function getFormTemplateListByGroupId(groupId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formTemplate/getListByGroupId',
    method: 'post',
    data: { groupId }
  })
}

