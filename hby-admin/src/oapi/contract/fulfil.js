/*
 * @Date: 2022-03-11 14:51:54
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-21 14:53:56
 * @FilePath: /hb-admin/src/api/contract/fulfil.js
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//变更人列表
export function getChangeContractStaffList(data) {
  return request({
    url: '/contract/getChangeContractStaffList',
    method: 'get',
    params: transData(data),
  })
}

//变更
export function changeContractStaff(data) {
  return request({
    url: '/contract/changeContractStaff',
    method: 'post',
    params: transData(data),
  })
}

/**
 * @description: 我的合同列表
 * @param {*} data
 * @return {*}
 */
export function getContractList(data) {
  return request({
    url: '/contract/contract/mineContract',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同暂停/合同变更/合同终止
 * @param {*} data
 * @return {*}
 */
export function changeContractStatus(data) {
  return request({
    url: '/contract/contract/changeContractStatus',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 文件上传
 * @param {*} data
 * @return {*}
 */
export function addFileToContract(data) {
  return request({
    url: '/contract/contract/importFile',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 附件列表
 * @param {*} data
 * @return {*}
 */
export function getFileList(data) {
  return request({
    url: '/contract/contract/showSealFileList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 附件删除
 * @param {*} data
 * @return {*}
 */
export function removeFileFromContract(data) {
  return request({
    url: '/contract/contract/deleteFile',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 执行
 * @param {*} data
 * @return {*}
 */
export function excuteContract(data) {
  return request({
    url: '/contract/contract/excuteContractId',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 比对
 * @param {*}
 * @return {*}
 */
export function compareContract(data) {
  return request({
    url: '/contract',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 履行跟踪列表
 * @param {*} data
 * @return {*}
 */
export function getTrackingContractList(data) {
  return request({
    url: '/contract/contract/performanceTracking',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 履行落实列表
 * @param {*}
 * @return {*}
 */
export function getImplementContractList(data) {
  return request({
    url: '/contract/contract/fulfillment',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 履行跟踪、履行落实-点击跟踪/落实获取子列表
 * @param {*} data
 * @return {*}
 */
export function getSubTrackingList(data) {
  return request({
    url: '/contract/contract/contractTracking',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description:  履行跟踪子列表-获取详情
 * @param {*} data
 * @return {*}
 */
export function getNodeDetail(data) {
  return request({
    url: '/contract/contract/approvalContractPlanNode',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description:  履行跟踪子列表-审批
 * @param {*} data
 * @return {*}
 */
export function trackingApproval(data) {
  return request({
    url: '/contract/contract/modifyContractNodeStatus',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 履行落实子列表-落实
 * @param {*} data
 * @return {*}
 */
export function implement(data) {
  return request({
    url: '/contract/contract/modifyContractSpNodeInfo',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 我的合同-附件下载
 * @param {*} data
 * @return {*}
 */
export function download(data) {
  return request({
    url: '/contract/downloadFtp/upload',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 我的合同-比对前置条件
 * @param {*} data
 * @return {*}
 */
export function getCompareInfo(data) {
  return request({
    url: '/contract/contractManagement/documentComparison',
    method: 'post',
    data,
  })
}

export function reloadTrack(data) {
  return request({
    url: '/contract/contract/modifyContractNodeStatus',
    method: 'post',
    data,
  })
}
