/*
 * @Date: 2022-02-25 11:51:30
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-02-25 13:50:08
 * @FilePath: /hb-admin/src/api/setting/contract.js
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 合同流程列表
 * @param {*} data
 * @return {*}
 */
export function getContractList(data) {
  return request({
    url: '/setting/contract/typeOfContractList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同管理根据类型-关联流程
 * @param {*} data
 * @return {*}
 */

export function associateProcess(data) {
  return request({
    url: '/setting/process/htgl/setting',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同管理-保存关联流程
 * @param {*} data
 * @return {*}
 */
export function saveAssociatedProcess(data) {
  return request({
    url: '/setting/htgl/saveflow',
    method: 'post',
    data: transData(data),
  })
}
