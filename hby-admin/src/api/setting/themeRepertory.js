import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 主题仓库一级菜单列表/主题展示列表
 * @param {*} data
 * @return {*}
 */
export function reportlist(data) {
  return request({
    url: '/setting/reportlist',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 主题仓库一级菜单列表/主题展示新增
 * @param {*} data
 * @return {*}
 */
export function addReportMenu(data) {
  return request({
    url: '/setting/addReportMenu',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 主题仓库一级菜单列表/主题展示删除
 * @param {*} data
 * @return {*}
 */
export function pagedel(data) {
  return request({
    url: '/setting/page_del',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库取消模块下发获取所选择的主体的模块列表
 * @param {*} data
 * @return {*}
 */
export function cancelModuleList(data) {
  return request({
    url: '/setting/cancelModuleList',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 主题仓库取消模块下数据
 * @param {*} data
 * @return {*}
 */
export function cancelBiModule(data) {
  return request({
    url: '/setting/cancelBiModule',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 主题仓库一级菜单列表/主题/模块下发列表
 * @param {*} data
 * @return {*}
 */
export function moduleDistributioList(data) {
  return request({
    url: '/setting/moduleDistributio',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 主题仓库一级菜单列表/主题/下发列表
 * @param {*} data
 * @return {*}
 */
export function themeUsersList(data) {
  return request({
    url: '/setting/user/list/two',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库用户下发取消-获取用户集合
 * @param {*} data
 * @return {*}
 */
export function getListByThemeHouse(data) {
  return request({
    url: '/setting/user/getListByThemeHouse',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 题仓库取消用户下数据
 * @param {*} data
 * @return {*}
 */
export function cancelBiStaff(data) {
  return request({
    url: '/setting/cancelBiStaff',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库一级菜单列表/主题/确认模块下发
 * @param {*} data
 * @return {*}
 */
export function moduleDistributioSave(data) {
  return request({
    url: '/setting/chooseRithgId/disPageId',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库一级菜单列表/主题/确认模块下发
 * @param {*} data
 * @return {*}
 */
export function moduleDistributioSaveTwo(data) {
  return request({
    url: '/setting/chooseRithgId/disPageIdTwo',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库一级菜单列表/主题/确认下发
 * @param {*} data
 * @return {*}
 */
export function themeUserSave(data) {
  return request({
    url: '/setting/issudeUser',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库-风险分类列表
 * @param {*} data
 * @return {*}
 */
export function selectallrisk(data) {
  return request({
    url: '/setting/selectallrisk',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 主题仓库-风险分类-删除
 * @param {*} data
 * @return {*}
 */
export function removeRiskClass(data) {
  return request({
    url: '/setting/removeRiskClass',
    method: 'post',
    data: transData(data),
  })
}

export function addRiskClass(data) {
  return request({
    url: '/setting/addRiskClass',
    method: 'post',
    data: transData(data),
  })
}

export function modifyRiskClass(data) {
  return request({
    url: '/setting/addRiskClass',
    method: 'post',
    data: transData(data),
  })
}

export function ssfx(data) {
  return request({
    url: '/setting/yinMai/paikeSingLogin ',
    method: 'get',
    params: transData(data),
  })
}
