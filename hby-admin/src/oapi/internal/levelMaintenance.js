import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//等级维护-等级维护列表-内容页面分页功能
export function getLevelList(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/level_list',
    method: 'get',
    params: transData(params),
  })
}

//等级维护-通过等级主键查询
export function getLevelListById(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/level_findByid',
    method: 'get',
    params: transData(params),
  })
}

//等级维护-新增等级
export function levelAdd(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/level_add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
//等级维护-修改等级
export function levelEdit(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/level_modify',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
//等级维护-删除等级
export function levelDelete(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/level_delete',
    method: 'delete',
    params: transData(params),
  })
}
