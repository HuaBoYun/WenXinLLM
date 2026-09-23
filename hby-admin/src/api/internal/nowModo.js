import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//公告维护-列表
export function getList(params) {
  return request({
    url: '/nkhg/xxbz/getHomepage_List',
    method: 'get',
    params: transData(params),
  })
}
//公告维护-详情
export function getDetail(params) {
  return request({
    url: '/nkhg/xxbz/getDetail',
    method: 'get',
    params: transData(params),
  })
}

//公告维护-新增或更新
export function saveOrUpdate(data) {
  return request({
    url: '/nkhg/xxbz/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
//公告维护-删除
export function remove(params) {
  return request({
    url: '/nkhg/xxbz/remove',
    method: 'post',
    params: transData(params),
  })
}

//删除附件
export function deleteAttachment(params) {
  return request({
    url: '/nkhg/xxbz/deleteAttachment',
    method: 'post',
    params: transData(params),
  })
}

