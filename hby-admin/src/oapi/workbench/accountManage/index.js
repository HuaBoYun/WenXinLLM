import { transData } from '@/utils/requestData'
import request from '@/utils/request'

// 前缀/zbgl

// 账簿管理-分页查询
export function getList(params) {
  return request({
    url: '/zbgl/accBookMgr/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    // data: JSON.stringify(params),
  })
}

// 账簿管理-获取当前登录用户选中的账套数据
export function getSelectedBookInfo(params) {
  console.log(params, 'params')
  return request({
    url: '/zbgl/accBookMgr/getSelectedBookInfo',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    // data: JSON.stringify(params),
  })
}

// 账簿管理-选择年份账套
export function checkBook(params) {
  return request({
    url: '/zbgl/accBookMgr/checkBook?bookId=' + params.bookId,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    // data: JSON.stringify(params),
  })
}
