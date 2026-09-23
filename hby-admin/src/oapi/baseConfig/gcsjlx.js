import request from '@/utils/request'

export const getList = (params) => request({
  url: '/oiaudit/project/auditType/getAuditTypePage',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getDetail = (params) => request({
  url: '/oiaudit/project/auditType/getAuditTypeById',
  method: 'get',
  params,
})

export const handleDelete = (params) => request({
  url: '/oiaudit/project/auditType/delete',
  method: 'get',
  params,
})

export const handleDeleteByNameId = (params) => request({
  url: '/oiaudit/project/auditType/deleteNameById',
  method: 'get',
  params,
})

export const addOrUpdate = (data) => request({
  url: '/oiaudit/project/auditType/saveOrUpdate',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  // baseURL: '/tempUrl',
  data,
})

export const getMethodMaintainOpts = () => request({
  url: '/oiaudit/audit/methodMaintain/getMethodMaintainList',
  method: 'get',
  // baseURL: '/tempUrl',
})