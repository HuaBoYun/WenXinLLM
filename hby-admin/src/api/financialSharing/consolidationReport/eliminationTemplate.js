import request from '@/utils/request'

/**
 * 查询抵消凭证模板列表
 */
export function getTemplateList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationTemplate/getTemplateList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询抵消凭证模板
 */
export function getTemplateById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationTemplate/getTemplateById',
    method: 'post',
    data
  })
}

/**
 * 新增抵消凭证模板
 */
export function saveTemplate(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationTemplate/saveTemplate',
    method: 'post',
    data
  })
}

/**
 * 修改抵消凭证模板
 */
export function updateTemplate(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationTemplate/updateTemplate',
    method: 'post',
    data
  })
}

/**
 * 删除抵消凭证模板
 */
export function deleteTemplate(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationTemplate/deleteTemplate',
    method: 'post',
    data
  })
}

/**
 * 更新模板状态
 */
export function updateTemplateStatus(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationTemplate/updateTemplateStatus',
    method: 'post',
    data
  })
}

