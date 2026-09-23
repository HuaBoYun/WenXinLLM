import request from '@/utils/request'

export const getList = (params) => request({
  url: '/oiaudit/project/auditTemplate/getAuditTemplatePage',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getDetail = (params) => request({
  url: '/oiaudit/project/auditTemplate/getTemplateById',
  method: 'get',
  params,
})

export const getSubTemplate = (params) => request({
  url: '/oiaudit/project/auditTemplate/previewTemplateById',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getSubTemplateNext = (params) => request({
  url: '/oiaudit/project/auditTemplate/mytaskTemplateById',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const handleDelete = (params) => request({
  url: '/oiaudit/project/auditTemplate/delete',
  method: 'get',
  params,
})

export const addOrUpdate = (data) => request({
  url: '/oiaudit/project/auditTemplate/saveOrUpdate',
  method: 'post',
  // baseURL: '/tempUrl',
  data,
})

export const getTypeOpts = () => request({
  url: '/oiaudit/project/auditType/getAuditTypeList',
  method: 'get',
  // baseURL: '/tempUrl',
})