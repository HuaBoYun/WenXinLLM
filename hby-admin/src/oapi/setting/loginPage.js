/*
 * @Date: 2022-02-25 11:49:58
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-18 14:24:47
 * @FilePath: /hb-admin/src/api/setting/loginPage.js
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 登录页列表
 * @param {*} data
 * @return {*}
 */
export function getLoginList(data) {
  return request({
    url: '/setting/login/page/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

/**
 * @description: 保存登录页
 * @param {*} data
 * @return {*}
 */
export function saveLoginPage(data) {
  return request({
    url: '/setting/login/page/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
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
export function deleteLoginPage(data) {
  return request({
    url: `/setting/login/page/${data.id}`,
    method: 'DELETE',
  })
}

export function getPrivewAttInfo(params) {
  return request({
    url: '/setting/fileManage/getPrivewAttInfo',
    method: 'get',
    params: transData(params),
  })
}

export function LoginPageStatus(params) {
  return request({
    url: '/setting/login/page/updateState',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

export function getDefaultLoginInfo(data) {
  return request({
    url: `/setting/login/page/${data.id}`,
    method: 'get',
    params: transData(data),
  })
}

export function saveHomePage(data) {
  return request({
    url: `/setting/home/page/saveOrUpdate`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

export function getHomePageList(data) {
  return request({
    url: `/setting/home/page/getList`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

export function getDefaultHomeInfo(data) {
  return request({
    url: `/setting/home/page/${data.id}`,
    method: 'get',
    params: transData(data),
  })
}

export function homePageStatus(data) {
  return request({
    url: `/setting/home/page/updateState`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

export function deleteHomePage(data) {
  return request({
    url: `/setting/home/page/${data.id}`,
    method: 'DELETE',
  })
}
export function getCompanyImg(data) {
  return request({
    url: `/setting/login/page/info/${data.id}`,
    method: 'GET',
  })
}
export function getHomePageListData(data) {
  return request({
    url: `/setting/home/page/auth/company`,
    method: 'GET',
    data: transData(data),
  })
}
