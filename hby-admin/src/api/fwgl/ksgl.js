import request from '@/utils/request'
import { transData } from '@/utils/requestData'
export const uploadApi = '/fwgl/api-auth/fileManage/upload'

// 考试管理-列表-查询
export function getKsglList(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 规划管理-新增-题库总数
export function getTKList(params) {
  return request({
    url: '/fwgl/exam/api/repo/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 规划管理-新增-保存
export function saveData(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 规划管理-新增-保存
export function getDetail(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 规划管理-新增-保存
export function getKSDetailList(params) {
  return request({
    url: '/fwgl/exam/api/user/exam/paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 考试管理-人员-查看明细
export function getPersonExamDetail(params) {
  return request({
    url: '/fwgl/exam/api/paper/paper/paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 考试管理-列表-删除
export function deleteList(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 考试管理-下发选择人员
export function sendPerson(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/examStaff',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 考试管理-下发选择人员
export function getKSRYList(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/ExamStaffInfo',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}


export function getDefaultId(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/examStaffList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//删除
export function setExamStaffDelete(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/ExamStaffDelete',
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//启动
export function setExamUpdate(params) {
  return request({
    url: '/fwgl/exam/api/exam/exam/ExamUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}