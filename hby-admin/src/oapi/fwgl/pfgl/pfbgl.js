import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表查询
export function getList(params) {
  return request({
    url: '/centralaudit/api-auth/examine/topic/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 保存
export function saveOrUpdate(params, scoreTransaction) {
  return request({
    url: `/centralaudit/api-auth/examine/topic/saveOrUpdate/${scoreTransaction || 0}`,
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
    url: `/centralaudit/api-auth/examine/topic/${params.id}`,
    method: 'get',
  })
}

// 详情
export function deleteApi(params) {
  return request({
    url: `/centralaudit/api-auth/examine/topic/${params.id}`,
    method: 'delete',
  })
}

// 详情列表
export function getInfoList(params) {
  return request({
    url: `/centralaudit/api-auth/examine/topic/second/info/${params.id}`,
    method: 'get',
  })
}
