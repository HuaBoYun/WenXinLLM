import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表查询
export function getList(params) {
  return request({
    url: '/fwgl/api-auth/examine/topic/second/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 保存
export function saveOrUpdate(params) {
  return request({
    url: '/fwgl/api-auth/examine/topic/second/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 详情
export function getDetail(params) {
  return request({
    url: `/fwgl/api-auth/examine/topic/second/${params.id}`,
    method: 'get',
  })
}

// 详情
export function deleteApi(params) {
  return request({
    url: `/fwgl/api-auth/examine/topic/second/${params.id}`,
    method: 'delete',
  })
}

// 导出评分项列表
export function exportTopic(params) {
  return request({
    url: '/fwgl/api-auth/examine/topic/second/export',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    responseType: 'blob',
  })
}
