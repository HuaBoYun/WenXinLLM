/*
 * @Date: 2022-02-25 14:02:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-26 18:33:14
 * @FilePath: /hb-admin/src/api/setting/dataGather.js
 */

import request from '@/utils/request'
// import { transData } from '@/utils/requestData'

/**
 * @description: 数据采集-列表
 * @param {*} data
 * @return {*}
 */
export function getDataGatherList(data) {
  return request({
    url: '/setting/show/acquisition',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集-采集
 * @param {*} data
 * @return {*}
 */
export function startGather(data) {
  return request({
    url: '/setting/startAcquisition',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集策略-列表
 * @param {*} data
 * @return {*}
 */
export function getStrategyList(data) {
  return request({
    url: '/setting/zt/getAcInfo',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集策略-保存
 * @param {*} data
 * @return {*}
 */
export function saveStrategy(data) {
  return request({
    url: '/setting/zt/saveAvTimeInfo',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集策略-查询
 * @param {*} data
 * @return {*}
 */
// export function strategyQuery(data) {
//   return request({
//     url: '/setting/zt/sjcj_modi',
//     method: 'post',
//     data: transData(data),
//   })
// }

/**
 * @description: 数据采集策略-修改
 * @param {*} data
 * @return {*}
 */
export function updateStrategy(data) {
  return request({
    url: '/setting/zt/modifyInfo',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集策略-删除
 * @param {*} data
 * @return {*}
 */
export function deleteStrategy(data) {
  return request({
    url: '/setting/zt/removeAvTimeInfo',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集配置-列表
 * @param {*} data
 * @return {*}
 */
export function getConfigList(data) {
  return request({
    url: '/setting/zt/sjcj_list',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集配置-新增
 * @param {*} data
 * @return {*}
 */
export function addConfig(data) {
  return request({
    url: '/setting/zt/sjcj_save',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集配置-获取数据库列表
 * @param {*} data
 * @return {*}
 */
export function getDbVersion(data) {
  return request({
    url: '/setting/financial_version_show',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集配置-删除
 * @param {*} data
 * @return {*}
 */
export function deleteConfig(data) {
  return request({
    url: '/setting/zt/sjcj_del',
    method: 'post',
    data,
  })
}

/**
 * @description: 数据采集配置-删除
 * @param {*} data
 * @return {*}
 */
export function download(data) {
  return request({
    url: '/setting/mb/download',
    method: 'get',
    params: data,
    responseType: 'blob',
  })
}
