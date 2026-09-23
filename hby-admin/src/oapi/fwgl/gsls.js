import request from '@/utils/request'
import { transData } from '@/utils/requestData'
export const uploadApi = '/fwgl/api-auth/fileManage/upload'

//预览
export function getPrivewAttInfo(params) {
  return request({
    url: '/fwgl/api-auth/fileManage/getPrivewAttInfo',
    method: 'get',
    params: transData(params),
  })
}

// 执业申请-列表-查询
export function getZYSQList(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/personnel/get-all',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//执业申请-新增/更新
export function addZYSQ(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/apply/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执业申请-详情
export function getZYSQDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/apply/${params.id}`,
    method: 'get',
  })
}

// 执业申请-列表-删除
export function deleteZYSQList(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/apply/${params.id}`,
    method: 'delete',
  })
}

//执业申请-简历-新增/更新
export function addZYSQjl(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/apply/ext/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执业申请-列表-删除
export function deleteZYSQjl(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/apply/ext/${params.id}`,
    method: 'delete',
  })
}

// 人员台账-列表-查询
export function getRYTZList(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/personnel/get-all',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执行活动-列表-查询
export function getZXHDList(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/activity/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执行活动-列表-删除
export function deleteZXHDList(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/activity/${params.id}`,
    method: 'delete',
  })
}

//执行活动-新增/更新
export function addZXHD(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/activity/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执行活动-详情
export function getZXHDDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/activity/${params.id}`,
    method: 'get',
  })
}

// 执行考核-列表-查询
export function getZXKHList(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/examine/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执行考核-列表-删除
export function deleteZXKHList(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/examine/${params.id}`,
    method: 'delete',
  })
}

//执行考核-新增/更新
export function addZXKH(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/examine/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 执行考核-详情
export function getZXKHDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/practice/examine/${params.id}`,
    method: 'get',
  })
}

//执业申请-导出
export function exportZYSQ(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/apply/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//人员台账-导出
export function exportRYTZ(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/personnel/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//执业活动-导出
export function exportZYHD(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer//practice/download-expressA',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//执业考核-导出
export function exportZYKH(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/examine/download-expressbb',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

export function getZDDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/legal/review/institution/audit/ext/${params.id}`,
    method: 'get',
    params,
  })
}

//会议管理-消息推送
export function pushRCGLInfo(params) {
  return request({
    url: `/fwgl/api-auth/daily/management/sendMetting/${params.id}`,
    method: 'post',
    data: params,
  })
}
