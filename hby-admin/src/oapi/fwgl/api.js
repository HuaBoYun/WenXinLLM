import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function fetchApi(urlData, params, noRest) {
  const fetchData = {
    url: urlData.url,
    method: urlData.method,
  }

  if (urlData.method === 'post') {
    fetchData.headers = { 'Content-Type': 'application/json;charset=UTF-8' }
    fetchData.data = JSON.stringify(transData(params))
  }

  if (urlData.method === 'get' || urlData.method === 'delete') {
    fetchData.url = noRest ? urlData.url : urlData.url + '/' + params.id
    fetchData.data = params
  }

  if (noRest) {
    fetchData.responseType = 'blob'
  }

  return request(fetchData)
}

// 上传下载文件
export const uploadApi = '/fwgl/api-auth/fileManage/upload'
export const downloadApi = '/fwgl/api-auth/fileManage/download'

export function download(params) {
  return request({
    url: '/fwgl/api-auth/fileManage/download',
    method: 'get',
    data: params,
    params: params,
    responseType: 'blob',
  })
}

/** 日程管理-会议管理接口 **/
export const dailyManagementConference = {
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/daily/management/conference/getList',
    method: 'post',
  },
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/daily/management/conference/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/daily/management/conference',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/daily/management/conference',
    method: 'delete',
  },
  exportDate: {
    //会议管理
    url: '/fwgl/api-auth/daily/management/conference/download-express',
    method: 'get',
  },
}

/** 日程管理-其他文件报送 **/
export const dailyManagementOther = {
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/daily/management/other/file/getList',
    method: 'post',
  },
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/daily/management/other/file/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/daily/management/other/file',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/daily/management/other/file',
    method: 'delete',
  },
  exportData: {
    //其他文件报送
    url: '/fwgl/api-auth/daily/management/other/file/download-express',
    method: 'get',
  },
}

/** 法律审核-审核台账列表 **/
export const legalReviewAudit = {
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/legal/review/audit/get-all',
    method: 'post',
  },
  exportData: {
    //审核台账导出
    url: '/fwgl/api-auth/legal/review/audit/download-express',
    method: 'get',
  },
}

/** 法律审核-制度审核/经营事项审核 **/
export const legalReviewInstitution = {
  addZJ: {
    // 制度新增，编辑
    url: '/fwgl/api-auth/legal/review/institution/audi/ext/saveOrUpdate',
    method: 'post',
  },
  getZJDetail: {
    // 制度详情
    url: `/fwgl/api-auth/legal/review/institution/audit/ext`,
    method: 'get',
  },
  doZJDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/review/institution/audit/ext',
    method: 'delete',
  },
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/legal/review/institution/audit/getList',
    method: 'post',
  },
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/review/institution/audit/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/review/institution/audit',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/review/institution/audit',
    method: 'delete',
  },
  exportZdJyData: {
    //制度审核/经营事项审核导出
    url: '/fwgl/api-auth/legal/review/institution/audit/download-express',
    method: 'get',
  },
}

/** 法律服务-常年/专项法律服务 **/
export const legalService = {
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/legal/service/getList',
    method: 'post',
  },
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service',
    method: 'delete',
  },
  exportData: {
    url: '/fwgl/api-auth/legal/service/work/report/download-express',
    method: 'get',
  },
}

/** 法律审核-常年/专项法律服务-工作报告表/服务登记 **/
export const legalServiceWorkReport = {
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/work/report/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service/work/report',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service/work/report',
    method: 'delete',
  },
}

/** 法律审核-常年/专项法律服务-工作记录 **/
export const legalServiceWorkRecord = {
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/work/record/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service/work/record',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service/work/record',
    method: 'delete',
  },
}

/** 法律审核-常年/专项法律服务-考核表 **/
export const legalServiceExamine = {
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/examine/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service/examine',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service/examine',
    method: 'delete',
  },
}

/** 法律审核-常年/专项法律服务-评分表 **/
export const legalServiceGrade = {
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/evaluate/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service/evaluate',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service/evaluate',
    method: 'delete',
  },
}

/** 法律审核-常年/专项法律服务-律师信息 **/
export const legalServiceLawyer = {
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/lawyer/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service/lawyer',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service/lawyer',
    method: 'delete',
  },
}

export const legalServiceQuestions = {
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/exam/api/paper/paper/paging',
    method: 'post',
  },
  saveOrUpdate: {
    // 保存更新
    url: '/fwgl/api-auth/legal/service/lawyer/saveOrUpdate',
    method: 'post',
  },
  detail: {
    // 详情
    url: '/fwgl/api-auth/legal/service/lawyer',
    method: 'get',
  },
  doDelete: {
    // 删除
    url: '/fwgl/api-auth/legal/service/lawyer',
    method: 'delete',
  },
}

/** 法律服务-台账 **/
export const legalServiceTotal = {
  getList: {
    // 列表查询
    url: '/fwgl/api-auth/legal/service/examine/getList',
    method: 'post',
  },
}
