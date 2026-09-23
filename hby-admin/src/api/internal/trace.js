import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价立项-主页查询
export function getTraceList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_list',
    method: 'get',
    params: transData(params),
  })
}

//评价管理-评价跟踪-跟踪
export function getProjTask(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_task',
    method: 'get',
    params: transData(params),
  })
}

//评价跟踪-跟踪-点击评价对象(评价人列表)
export function getProjPerson(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_person',
    method: 'get',
    params: transData(params),
  })
}
