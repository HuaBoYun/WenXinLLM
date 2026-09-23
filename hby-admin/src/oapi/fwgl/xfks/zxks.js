import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 学法考试-在线考试列表
export function onlinePaging(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/online-paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 检测进行中的考试
export function checkProcess(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/check-process',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 考试详情
export function processDetail(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 创建考试-开始考试
export function createPaper(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/create-paper',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 开始考试详情
export function paperDetail(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/paper-detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 填充答案
export function fillAnswer(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/fill-answer',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 填充答案
export function handExam(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/hand-exam',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 试题详情
export function quDetail(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/qu-detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
