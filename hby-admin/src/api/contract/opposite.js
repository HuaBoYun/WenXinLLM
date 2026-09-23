import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//相对方黑名单管理列表
export function BlackList(data) {
  return request({
    url: '/contract/oppsitePartyBlackList',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 相对方维护列表
 * @param {*}
 * @return {*}
 */

export function getOppositeList(data) {
  return request({
    url: '/contract/oppositePartyMaintenance',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护详情
 * @param {*}
 * @return {*}
 */

export function getOpposite(data) {
  return request({
    url: '/contract/findOppsiteInfo',
    method: 'post',
    data,
  })
}

//相对方管理-黑名单管理-取消黑名单
export function removeBlackList(data) {
  return request({
    url: '/contract/removeBlackList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-检查状态
 * @param {*}
 * @return {*}
 */

export function checkStatus(data) {
  return request({
    url: '/contract/getProjectrBudgetStatue',
    method: 'post',
    data: transData(data),
  })
}

//相对方管理-黑名单管理-导出
export function blacklistExport(data) {
  return request({
    url: '/contract/cwgl/blacklist_export',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
  })
}

/**
 * @description: 相对方维护-新增
 * @param {*}
 * @return {*}
 */

export function saveOpposite(data) {
  return request({
    url: '/contract/saveOppositeParty',
    method: 'post',
    data: transData(data),
  })
}
//相对方预警列表页面
export function oppsiteWarningList(data) {
  return request({
    url: '/contract/oppsiteWarningList',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 相对方维护-修改
 * @param {*}
 * @return {*}
 */

export function updateOpposite(data) {
  return request({
    url: '/contract/oppositePartyMenger',
    method: 'post',
    data: transData(data),
  })
}
//相对方预警-合同签订数量
export function contractByList(data) {
  return request({
    url: '/contract/contract/contractByList',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 相对方维护-加载相对方新增页面前置条件
 * @param {*}
 * @return {*}
 */

export function getPersonalData(data) {
  return request({
    url: '/contract/oppositePartyToAddPage',
    method: 'post',
    data: transData(data),
  })
}
//相对方预警-异常履约数量
export function abnormalPerformance(data) {
  return request({
    url: '/contract/contract/abnormalPerformance',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 相对方维护-提交审批
 * @param {*}
 * @return {*}
 */
export function approveOpposite(data) {
  return request({
    url: '/contract/submitOppsitePartApproval',
    method: 'post',
    data: transData(data),
  })
}
//相对方管理-相对方名称查看
export function addOppsiteParty(data) {
  return request({
    url: '/contract/addOppsitePartyBlackList',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 相对方维护-加入黑名单
 * @param {*}
 * @return {*}
 */
export function addToBlackList(data) {
  return request({
    url: '/contract/saveOppsitePartyBlack',
    method: 'post',
    data: transData(data),
  })
}

// /**
//  * @description: 相对方维护-删除
//  * @param {*}
//  * @return {*}
//  */
// export function deleteOpposite(data) {
//   return request({
//     url: '/contract/removeOppsiteParty',
//     method: 'post',
//     data: transData(data),
//   })
// }
/**
 * @description: 相对方维护-删除 ---无 审批版本
 * @param {*}
 * @return {*}
 */
export function deleteOpposite(data) {
  return request({
    url: '/contract/removeOppsitePartyNoLc',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-银行账户列表
 * @param {*}
 * @return {*}
 */
export function getBankList(data) {
  return request({
    url: '/contract/contract/counterpartManageList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-银行账户新增、修改
 * @param {*}
 * @return {*}
 */
export function saveBank(data) {
  return request({
    url: '/contract/contract/counterpartBankSave',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-银行账户启用、弃用
 * @param {*}
 * @return {*}
 */
export function updateBankStatus(data) {
  return request({
    url: '/contract/contract/changeBankInfoStatus',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-银行账户删除
 * @param {*}
 * @return {*}
 */
export function deleteBank(data) {
  return request({
    url: '/contract/contract/counterpartBankRemove',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方监控-列表
 * @param {*}
 * @return {*}
 */
export function getMonitorList(data) {
  return request({
    url: '/contract/fxyj/fxyj_list',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方监控-新增修改分组
 * @param {*} data
 * @return {*}
 */
export function saveTeam(data) {
  return request({
    url: '/contract/saveteam',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方监控-分组列表
 * @param {*} data
 * @return {*}
 */
export function getTeamList(data) {
  return request({
    url: '/contract/riskwarning/main',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方监控-新增公司
 * @param {*} data
 * @return {*}
 */
export function saveCompany(data) {
  return request({
    url: '/contract/savecompany',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-办理信息查看
 * @param {*} data
 * @return {*}
 */
export function viewDealInfoForSeal(data) {
  return request({
    url: '/contract/viewOppsiteProcessInfo',
    method: 'get',
    data: transData(data),
  })
}

/**
 * @description: 相对方维护-办理信息查看
 * @param {*} data
 * @return {*}
 */
export function removeOppsiteFile(data) {
  return request({
    url: '/contract/removeOppsiteFile',
    method: 'post',
    data: transData(data),
  })
}

//黑名单获取详细
export function getBlackCounterPartInfo(data) {
  return request({
    url: '/contract/getOppoRelaInfo',
    method: 'get',
    params: transData(data),
  })
}
//获取相对方信息
export function getXDFtableData(data) {
  return request({
    url: '/setting/api/search',
    method: 'get',
    params: transData(data),
  })
}
//同步相对方信息
export function synchronization(data) {
  return request({
    url: '/contract/contractPro/htxm/tbxm',
    method: 'get',
    params: transData(data),
  })
}