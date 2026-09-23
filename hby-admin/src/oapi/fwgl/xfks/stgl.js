import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getTopicList(params) {
  return request({
    url: '/fwgl/exam/api/qu/qu/paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 添加或修改
export function saveTopic(params) {
  return request({
    url: '/fwgl/exam/api/qu/qu/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 批量删除
export function deleteTopic(params) {
  return request({
    url: '/fwgl/exam/api/qu/qu/delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 详情
export function detailTopic(params) {
  return request({
    url: '/fwgl/exam/api/qu/qu/detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 题库列表
export function repoList(params) {
  return request({
    url: '/fwgl/exam/api/repo/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify(transData(params)),
  })
}
