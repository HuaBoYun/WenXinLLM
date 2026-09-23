/*
 * @Date: 2022-02-25 13:55:04
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-03-01 16:44:37
 * @FilePath: /hb-admin/src/api/setting/infoSearch.js
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 信息查询-个人查询
 * @param {*}
 * @return {*}
 */
export function getPersonalList(data) {
  return request({
    url: '/setting/select_user',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 信息查询-费用标准
 * @param {*}
 * @return {*}
 */
export function getCostStandard(data) {
  return request({
    url: '/setting/price_cost',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 信息查询-统计费用
 * @param {*}
 * @return {*}
 */
export function getCostStatistics(data) {
  return request({
    url: '/setting/price_company',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 信息查询-统计费用明细
 * @param {*}
 * @return {*}
 */
export function getCostStatisticsDetail(data) {
  return request({
    url: '/setting/findQueryRecodeDetail',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 信息查询-充值记录
 * @param {*}
 * @return {*}
 */
export function getChargeRecord(data) {
  return request({
    url: '/setting/deposit_money',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 信息查询-充值
 * @param {*} data
 * @return {*}
 */
export function saveCharge(data) {
  return request({
    url: '/setting/deposit_money',
    method: 'post',
    data: transData(data),
  })
}
