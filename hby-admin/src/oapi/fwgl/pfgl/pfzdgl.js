import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表查询
export function getList(params) {
  return request({
    url: '/centralaudit/api-auth/examine/topic/first/getList',
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
    url: '/centralaudit/api-auth/examine/topic/first/saveOrUpdate',
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
    url: `/centralaudit/api-auth/examine/topic/first/${params.id}`,
    method: 'get',
  })
}

// 详情
export function deleteApi(params) {
  return request({
    url: `/centralaudit/api-auth/examine/topic/first/${params.id}`,
    method: 'delete',
  })
}
