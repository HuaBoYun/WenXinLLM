import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//列表查询
export function getHjtzList(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/getList',
    method: 'get',
    params: transData(params),
  })
}
//列表删除
export function deleteHjtz(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/deleteone',
    method: 'post',
    data: transData(params),
  })
}
//下发
export function hjtzxfry(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/hjtzxfry',
    method: 'post',
    data: transData(params),
  })
}
//详情
export function getHjtz(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/getone',
    method: 'get',
    params: transData(params),
  })
}
//保存
export function saveHjtz(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}
//附件列表
export function getattList(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/getattList',
    method: 'get',
    params: transData(params),
  })
}
//附件删除
export function deleteHjtzatt(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/deleteatt',
    method: 'post',
    data: transData(params),
  })
}

//理论研讨通知- 下发人员
export function dxfryLlyytz(params) {
  return request({
    url: '/oiaudit/xmpy/hjtz/hjtzxfry',
    method: 'post',
    data: transData(params),
  })
}