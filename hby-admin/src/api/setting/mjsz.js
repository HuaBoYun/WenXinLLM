/*
 * @Date: 2022-02-25 11:51:30
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-02-25 13:50:08
 * @FilePath: /hb-admin/src/api/setting/contract.js
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 密级设置列表
 * @param {*} data
 * @return {*}
 */
export function getList(data) {
  return request({
    url: '/setting/secrect/getPageInfo',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 密级设置人员下拉
 * @param {*} data
 * @return {*}
 */

export function getSelectList(data) {
  return request({
    url: '/setting/secrect/getScopeSecrectListByType',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 密级设置功能下拉
 * @param {*} data
 * @return {*}
 */
export function getGNList(data) {
  return request({
    url: '/setting/htgl/saveflow',
    method: 'get',
    params: transData(data),
  })
}
/**
 * @description: 密级设置保存
 * @param {*} data
 * @return {*}
 */
export function addData(data) {
  return request({
    url: '/setting/secrect/saveInfo',
    method: 'POST',
    data: transData(data),
  })
}
export function editData(data) {
  return request({
    url: '/setting/secrect/modifyInfo',
    method: 'POST',
    data: transData(data),
  })
}
export function deleteData(data) {
  return request({
    url: '/setting/secrect/removeInfo',
    method: 'get',
    params: transData(data),
  })
}
export function detailData(data) {
  return request({
    url: '/setting/secrect/getDetail',
    method: 'get',
    params: transData(data),
  })
}
//系统设置里的密级下拉数据
export function getMJdata(data) {
  return request({
    url: '/setting/secrect/getSecrectListByType',
    method: 'get',
    params: transData(data),
  })
}
// 根据菜单id获取密级下拉数据
export function getMJ(data) {
  return request({
    url: '/setting/secrect/getSecrectListForRight',
    method: 'get',
    params: transData(data),
  })
}
// 审批时候需要密级
export function getSPMJ(data) {
  return request({
    url: '/setting/secrect/getSecrectListByLoginUser',
    method: 'get',
    params: transData(data),
  })
}
