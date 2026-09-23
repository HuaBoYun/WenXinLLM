import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 用户管理-切换公司
 * @param {*} data
 * @return {*}
 */
export function setLoginUserOrgInfo(data) {
  return request({
    url: '/setting/user/setLoginUserOrgInfo',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 授权接口-数据授权
 * @param {*} data
 * @return {*}
 */
export function grantRoleDataRight(data) {
  return request({
    url: '/setting/right/grantRoleDataRight',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 授权接口-数据授权 获取
 * @param {*} data
 * @return {*}
 */
export function getRoleDataDeptInfo(data) {
  return request({
    url: '/setting/right/getRoleDataDeptInfo',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 用户管理-我的组织
 * @param {*} data
 * @return {*}
 */
export function getLoginUserOrgRelationList(data) {
  return request({
    url: '/setting/user/getLoginUserOrgRelationList',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 用户管理-新建-所属机构
 * @param {*} data
 * @return {*}
 */
export function TreeNbkz(data) {
  return request({
    url: '/setting/findOrganizationByTreeNbkz',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 账套授权-授权列表
 * @param {*} data
 * @return {*}
 */
export function newindexs(data) {
  return request({
    url: '/setting/zt/listorg',
    method: 'post',
    data,
  })
}
/**
 * @description: 账套授权-授权详情
 * @param {*} data
 * @return {*}
 */
export function bookdetail(data) {
  return request({
    url: '/setting/zt/accbookdetail',
    method: 'post',
    data,
  })
}
/**
 * @description: 账套授权-授权
 * @param {*} data
 * @return {*}
 */
export function ztNewlist(data) {
  return request({
    url: '/setting/zt_user/newlist',
    method: 'post',
    data,
  })
}
/**
 * @description: 账套授权-授权
 * @param {*} data
 * @return {*}
 */
export function saveNewAccBookManage(data) {
  return request({
    url: '/setting/zt/saveNewAccBookManage',
    method: 'post',
    data,
  })
}
/**
 * @description: 账套授权-取消授权-用户列表
 * @param {*} data
 * @return {*}
 */
export function zbqxindexs(data) {
  return request({
    url: '/setting/user/zbqxindexs',
    method: 'post',
    data,
  })
}
/**
 * @description: 账套授权-取消授权
 * @param {*} data
 * @return {*}
 */
export function delAccBookManage(data) {
  return request({
    url: '/setting/zt/delAccBookManage',
    method: 'post',
    data,
  })
}
/**
 * @description: 权限设定-查询
 * @param {*} data
 * @return {*}
 */
export function qxsdList(data) {
  return request({
    url: '/setting/qxsd/list',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 权限设定-功能授权列表
 * @param {*} data
 * @return {*}
 */
export function qxsdFeatureList(data) {
  return request({
    url: '/setting/qxsd/mgnsq',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 权限设定-功能授权保存
 * @param {*} data
 * @return {*}
 */
export function qxsdFeatureSave(data) {
  return request({
    url: '/setting/qxsd/qx_save',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 权限设定-报表授权列表
 * @param {*} data
 * @return {*}
 */
export function qxsdTableList(data) {
  return request({
    url: '/setting/qxsd/screenRightList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 权限设定-报表授权保存
 * @param {*} data
 * @return {*}
 */
export function qxsdTableSave(data) {
  return request({
    url: '/setting/qxsd/screenRightList/qx_save',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 岗位管理-查询列表
 * @param {*} data
 * @return {*}
 */
export function jobList(data) {
  return request({
    url: '/setting/job/job_list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 岗位管理-新增
 * @param {*} data
 * @return {*}
 */
export function jobSave(data) {
  return request({
    url: '/setting/job/save',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 详情
 * @param {*} data
 * @return {*}
 */
export function userInfo(data) {
  return request({
    url: '/setting/user/info',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 岗位管理-删除
 * @param {*} data
 * @return {*}
 */
export function jobDel(data) {
  return request({
    url: '/setting/job/job_del',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 角色管理-列表
 * @param {*} data
 * @return {*}
 */
export function roleList(data) {
  return request({
    url: '/setting/role/role_list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 角色管理-新增/role/role_delW
 * @param {*} data
 * @return {*}
 */
export function roleSave(data) {
  return request({
    url: '/setting/role/save',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 角色管理-删除
 * @param {*} data
 * @return {*}
 */
export function roleDel(data) {
  return request({
    url: '/setting/role/role_del',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 角色管理-启用
 * @param {*} data
 * @return {*}
 */
export function modifyRstatus(data) {
  return request({
    url: '/setting/role/modify_rstatus',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 用户管理-新建保存
 * @param {*} data
 * @return {*}
 */
export function userSave(data) {
  return request({
    url: '/setting/user/save',
    method: 'post',
    data,
  })
}

/**
 * @description: 用户管理-重置密碼
 * @param {*} data
 * @return {*}
 */
export function userPsave(data) {
  return request({
    url: '/setting/user/xgpassword',
    method: 'post',
    data,
  })
}
/**
 * @description: 用户管理-新建
 * @param {*} data
 * @return {*}
 */
export function userNew(data) {
  return request({
    url: '/setting/user/new',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 用户管理列表-查询
 * @param {*} data
 * @return {*}
 */
export function userList(data) {
  return request({
    url: '/setting/user/list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 转发用户管理列表-查询
 * @param {*} data
 * @return {*}
 */
export function bmfzrList(data) {
  return request({
    url: '/setting/user/bmfzr_list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 分管领导转发用户管理列表-查询
 * @param {*} data
 * @return {*}
 */
export function fgldList(data) {
  return request({
    url: '/setting/user/fgld_list',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取角色菜单权限
 * @param data
 * @returns {AxiosPromise}
 */
export const getRoleRightList = (data) => {
  return request({
    url: '/setting/right/getRoleRightList',
    method: 'get',
    params: data,
  })
}

/**
 * 保存角色授权
 * @param data
 * @returns {AxiosPromise}
 */
export const grantRoleRight = (data) => {
  return request({
    url: '/setting/right/grantRoleRight',
    method: 'post',
    data,
  })
}

/**
 * 获取报表菜单
 * @param data
 * @returns {AxiosPromise}
 */
export const getRoleRightListScreen = (data) => {
  return request({
    url: '/setting/right/screenRight/getAllRightList',
    method: 'get',
    params: data,
  })
}

/**
 * 保存角色授权
 * @param data
 * @returns {AxiosPromise}
 */
export const saveRightInfoScreen = (data) => {
  return request({
    url: '/setting/right/screenRight/saveRightInfo',
    method: 'post',
    data,
  })
}
/**
 * 用户管理预览
 * @param data
 * @returns {AxiosPromise}
 */
export const qxInfo = (data) => {
  return request({
    url: '/setting/qx/qxInfo',
    method: 'post',
    data,
  })
}
