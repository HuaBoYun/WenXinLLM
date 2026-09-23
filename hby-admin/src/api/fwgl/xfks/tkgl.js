import request from '@/utils/request'
import { transData } from '@/utils/requestData'



export function getTkglList(params) {
  return request({
    url: '/fwgl/exam/api/repo/paging',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}


// 添加或修改
export function saveWarehouse(params) {
  return request({
    url: '/fwgl/exam/api/repo/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 批量删除
export function deleteWarehouse(params) {
  return request({
    url: '/fwgl/exam/api/repo/delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 查找详情
export function detailWarehouse(params) {
  return request({
    url: '/fwgl/exam/api/repo/detail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

