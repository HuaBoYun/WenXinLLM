import request from '@/utils/request'
import { transData } from '@/utils/requestData'
export const getList = (params) => request({
  url: '/oiaudit/audit/MyManuscript/getMyManuscriptPage',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getRecordsListByMyDraft = (params) => request({
  url: '/oiaudit/audit/workRecords/getRecordsListByMyDraft',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})
export const delWorkRecordRela = (params) => request({
  url: '/oiaudit/audit/MyManuscript/delWorkRecordRela',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getAllList = (params) => request({
  url: '/oiaudit/audit/MyManuscript/getMyManuscriptListByTypeId',
  method: 'get',
  // baseURL: '/tempUrl',
  params,
})

export const getDetail = (params) => request({
  url: '/oiaudit/audit/MyManuscript/getMyManuscriptById',
  method: 'get',
  params,
})
export const tipList = (params) => request({
  url: '/oiaudit/xmqd/getTipList',
  method: 'get',
  params,
})

export const handleDelete = (params) => request({
  url: '/oiaudit/audit/MyManuscript/delete',
  method: 'get',
  params,
})

export const addOrUpdate = (data) => request({
  url: '/oiaudit/audit/MyManuscript/saveOrUpdate',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  data,
})

export const handleDeleteSub = (params) => request({
  url: '/oiaudit/audit/MyManuscript/deleteMyManuVerify',
  method: 'get',
  params,
})

//我的底稿-导出
export function myDraftExport(params) {
  return request({
    url: '/oiaudit/audit/MyManuscript/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//我的底稿-导出
export function myDraftExportWord(params) {
  return request({
    url: '/oiaudit/audit/MyManuscript/exportWord',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//风险月度评估上报提醒
export function remindMonthlyEvaluation(params) {
  return request({
    url: '/riskcontrol/monthlyEvaluation/remindMonthlyEvaluation',
    method: 'post',
    data: transData(params),
  })
}

