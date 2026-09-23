import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 内部专家资源库列表
 * @param {*} data
 * @return {*}
 */
export function sjzykNbzj(data) {
  return request({
    url: '/setting/sjzyk_nbzj',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 内部专家资源库 新增/修改
 * @param {*} data
 * @return {*}
 */
export function nbzjSave(data) {
  return request({
    url: '/setting/nbzj_save',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 内部专家资源库 删除
 * @param {*} data
 * @return {*}
 */
export function nbzjDel(data) {
  return request({
    url: '/setting/nbzj_del',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 内部专家资源库 新增 获取姓名 id和start必传
 * @param {*} data
 * @return {*}
 */
export function userIndexs(data) {
  return request({
    url: '/setting/findOrganizationByTreeAll',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 外部专家资源库列表新增
 * @param {*} data
 * @return {*}
 */
export function sjzjkAdd(data) {
  return request({
    url: '/setting/sjzjk_add',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 外部专家资源库修改列表
 * @param {*} data
 * @return {*}
 */
export function sjzjkToadd(data) {
  return request({
    url: '/setting/sjzjk_to_add',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 外部专家资源库列表
 * @param {*} data
 * @return {*}
 */
export function sjzykWpzj(data) {
  return request({
    url: '/setting/sjzyk_wpzj',
    method: 'post',
    data: transData(data),
  })
}
