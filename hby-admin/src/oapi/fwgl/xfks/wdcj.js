import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 学法考试-我的成绩-查询列表
export function queryScore(params) {
  return request({
    url: '/fwgl/exam/api/user/exam/my-paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 学法考试-我的成绩-考试错题-查询列表
export function queryWrongTopic(params) {
  return request({
    url: '/fwgl/exam/api/user/wrong-book/paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 学法考试-我的成绩-考试错题-批量删除
export function deleteWrongTopic(params) {
  return request({
    url: '/fwgl/exam/api/user/wrong-book/delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 学法考试-我的成绩-错题训练-下一题
export function nextQu(params) {
  return request({
    url: '/fwgl/exam/api/user/wrong-book/next',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 学法考试-我的成绩-错题训练-下一题详情
export function fetchDetail(params) {
  return request({
    url: '/fwgl/exam/api/qu/qu/detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
