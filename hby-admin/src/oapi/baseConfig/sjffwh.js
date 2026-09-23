import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export const getList = (params) => request({
  url: '/oiaudit/audit/methodMaintain/getMethodMaintainPage',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getDetail = (params) => request({
  url: '/oiaudit/audit/methodMaintain/getMethodMaintainById',
  method: 'get',
  params,
})

export const handleDelete = (params) => request({
  url: '/oiaudit/audit/methodMaintain/delete',
  method: 'get',
  params,
})

export const addOrUpdate = (data) => request({
  url: '/oiaudit/audit/methodMaintain/saveOrUpdate',
  method: 'post',
  data,
})