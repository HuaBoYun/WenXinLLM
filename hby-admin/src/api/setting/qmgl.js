
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/setting/sign/getSignList',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 保存登录页
 * @param {*} data
 * @return {*}
 */
export function addInfo(data) {
  return request({
    url: '/setting/sign/saveSignInfo',
    method: 'post',
    data: transData(data),
  })
}
export function editInfo(data) {
  return request({
    url: '/setting/sign/modifySignInfo',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 修改登录页，暂不需要回显
 * @param {*} data
 * @return {*}
 */
// export function updateLoginPage(data) {
//   return request({
//     url: '/setting/loginType_modi',
//     method: 'post',
//     data: transData(data),
//   })
// }

/**
 * @description: 删除登录页
 * @param {*} data
 * @return {*}
 */
export function deleteInfo(data) {
  return request({
    url: `/setting/sign/removeSignInfo`,
    method: 'GET',
    params: data,
  })
}

export function getDefaultInfo(data) {
  return request({
    url: `/setting/sign/getSignInfo`,
    method: 'post',
    data: transData(data),
  })
}

// 用于审批的签名录入
export function getSPQMInfo(data) {
  return request({
    url: `/setting/sign/getSignNatureList`,
    method: 'post',
    data: transData(data),
  })
}

