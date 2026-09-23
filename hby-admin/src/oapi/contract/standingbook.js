/*
 * @Date: 2022-03-23 10:30:49
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-01 16:32:37
 * @FilePath: /hb-admin/src/api/contract/standingbook.js
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 合同台账列表
 * @param {*} data
 * @return {*}
 */
export function getStandingBookList(data) {
  return request({
    url: '/contract/contract/contractLedgerList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同台账导出
 * @param {*} data
 * @return {*}
 */
export function exportStandingBook(data) {
  return request({
    url: '/contract/contract/exploredInfo',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
  })
}

export function getStandingAllBookList(data) {
  return request({
    url: '/contract/contract/contractLedgerListAllOrg',
    method: 'post',
    data: transData(data),
  })
}

export function exportStandingAllBook(data) {
  return request({
    url: '/contract/contract/exploredOrgInfo',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
  })
}
