import request from '@/utils/request'

export const getList = (params) => request({
  url: '/oiaudit/sjssSjnr/list',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getDetail = (params) => request({
  url: '/oiaudit/sjssSjnr/detail',
  method: 'get',
  params,
})

export const handleDelete = (params) => request({
  url: '/oiaudit/sjssSjnr/delete',
  method: 'get',
  params,
})

export const addOrUpdate = (data) => request({
  url: '/oiaudit/sjssSjnr/saveOrUpdate',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  data,
})

export const getScSingleDetail = (params) => request({
  url: '/oiaudit/audit/myTask/Review/getRecordsById',
  method: 'get',
  params,
})

export const getMyTaskReviewList = (params) => request({
  url: '/oiaudit/audit/myTask/Review/getMyTaskReviewList',
  method: 'get',
  params,
})

export const handleDeleteAnnex = (params) => request({
  url: '/oiaudit/audit/myTask/Review/deleteFileAttach',
  method: 'get',
  params,
})

export const handleDeleteSc = (params) => request({
  url: '/oiaudit/audit/myTask/Review/delete',
  method: 'get',
  params,
})

export const addOrUpdateSc = (data) => request({
  url: '/oiaudit/audit/myTask/Review/saveOrUpdate',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  data,
})

export const addOrUpdateScList = (data) => request({
  url: '/oiaudit/audit/myTask/Review/saveOrUpdateList',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  // baseURL: '/tempUrl',
  data,
})




export const getDdtaskTemplateById = (params) => request({
  url: '/oiaudit/sjdd/ddtaskTemplateById',
  method: 'get',
  params,
})

export const saveOrUpdateList = (data) => request({
  url: '/oiaudit/sjdd/saveOrUpdateList',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  // baseURL: '/tempUrl',
  data,
})