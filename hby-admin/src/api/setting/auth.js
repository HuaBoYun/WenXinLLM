import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import store from '@/store'
const token = store.getters['user/token']
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
 * @description: 用户管理-导出
 * @param {*} data
 * @return {*}
 */
export function exportUserInfoList(data) {
  return request({
    url: '/setting/baseInfo/exportUserInfoList',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
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
    url: '/setting/baseInfo/getUserList',
    // url: '/setting/user/list',
    method: 'get',
    params: transData(data),
  })
}
/**
 * @description: 用户管理列表-查询(新接口 2025-5-14)
 * @param {*} data
 * @return {*}
 */
export function getUserListALL(data) {
  return request({
    url: '/setting/baseInfo/getUserListALL',
    method: 'get',
    params: transData(data),
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
    data: transData(data),
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

//角色管理-公司授权
export const rqorg = (data) => {
  return request({
    url: '/setting/role/rqorg',
    method: 'post',
    data,
  })
}
// 角色管理-授权用户列表
export const getroleUserList = (data) => {
  return request({
    url: '/setting/role/getroleUserList',
    method: 'get',
    params: data,
  })
}

//角色管理-公司授权
export const getCompanyList = (data) => {
  return request({
    url: '/setting/role/getroleOrgList',
    method: 'get',
    params: data,
  })
}
// 角色管理-被取消授权用户列表
export const getGrantSystemRightStaffList = (data) => {
  return request({
    url: '/setting/right/getGrantSystemRightStaffList',
    method: 'get',
    params: data,
  })
}
/**
 * @description: 用户管理-重置密码
 * @param {*} data
 * @return {*}
 */
export function userReset(data) {
  return request({
    url: '/setting/user/psave',
    method: 'post',
    data,
  })
}

export const delrqorg = (data) => {
  return request({
    url: '/setting/role/delrqorg',
    method: 'post',
    params: data,
  })
}

//角色管理-公司授权 列表
export const getCompanyListToGrantRole = (data) => {
  return request({
    url: '/setting/role/getCompanyListToGrantRole',
    method: 'get',
    params: data,
  })
}
//角色管理-用户授权
export const rquser = (data) => {
  return request({
    url: '/setting/role/rquser',
    method: 'post',
    data,
  })
}
//角色管理-取消用户授权
export const qxuser = (data) => {
  return request({
    url: '/setting/role/qxuser',
    method: 'post',
    data,
  })
}



//角色取消数据授权-查询该角色已授权的部门信息
export const getGrantRoleDataDeptInfo = (data) => {
  return request({
    url: '/setting/right/getGrantRoleDataDeptInfo',
    method: 'get',
    params: data,
  })
}

//角色取消数据授权-取消角色数据授权
export const removeDataRight = (data) => {
  return request({
    url: '/setting/right/removeDataRight',
    method: 'post',
    data,
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
 * @description: 文库新增
 * @param {*} data
 * @return {*}
 */
export function addWK(data) {
  return request({
    url: '/setting/zsgx/zswkaveOrUpdate',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 文库删除附件
 * @param {*} data
 * @return {*}
 */
export function deleteWKFile(data) {
  return request({
    url: '/setting/zsgx/deleteatt',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 文库列表
 * @param {*} data
 * @return {*}
 */
export function wkList(data) {
  return request({
    url: '/setting/zsgx/getzswk',
    method: 'get',
    params: transData(data),
  })
}
/**
 * @description: 文库列表删除
 * @param {*} data
 * @return {*}
 */
export function deleteWKList(data) {
  return request({
    url: '/setting/zsgx/deleteZswk',
    method: 'POST',
    data: transData(data),
  })
}
/**
 * @description: 文库预览
 * @param {*} data
 * @return {*}
 */
export function previewWK(data) {
  return request({
    url: '/setting/zsgx/addcxcount',
    method: 'POST',
    data: transData(data),
  })
}

/**
 * @description: 法律新增
 * @param {*} data
 * @return {*}
 */
export function addFL(data) {
  return request({
    url: '/setting/zsgx/flwksaveOrUpdate',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本库列表
 * @param {*} data
 * @return {*}
 */
export function htfbkList(data) {
  return request({
    url: '/setting/zsgx/gethtfblist',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 合同范本库新增
 * @param {*} data
 * @return {*}
 */
export function addHtfbk(data) {
  return request({
    url: '/setting/zsgx/htfbsaveOrUpdate',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 法律文库左侧搜索项
 * @param {*} data
 * @return {*}
 */
export function searchList(data) {
  return request({
    url: '/setting/zsgx/getBytypelist',
    method: 'get',
    params: transData(data),
  })
}
/**
 * @description: 法律文库列表
 * @param {*} data
 * @return {*}
 */
export function lawList(data) {
  return request({
    url: '/setting/zsgx/getflwklist',
    method: 'get',
    params: transData(data),
  })
}


//工作移交-工作移交获取分页列表
export function transferWorkList(data) {
  return request({
    url: '/setting/user/transfer/workList',
    method: 'get',
    params: transData(data),
  })
}

//工作移交-工作移交获取分页列表
export function transferMenger(data) {
  return request({
    url: '/setting/user/transfer/menger',
    method: 'POST',
    data: transData(data),
  })
}

//工作移交-根据用户主键获取其所有的兼职部门
export function getUserPartDept(data) {
  return request({
    url: '/setting/user/getUserPartDept',
    method: 'get',
    params: transData(data),
  })
}

//工作移交-工作移交详情接口
export function transferDetail(data) {
  return request({
    url: '/setting/user/transfer/detail',
    method: 'get',
    params: transData(data),
  })
}

//工作移交-工作移交启用弃用
export function transferEnableStatus(data) {
  return request({
    url: '/setting/user/transfer/enableStatus',
    method: 'POST',
    data: transData(data),
  })
}

// 查询当前是否有流程
export function getFlowList(data) {
  return request({
    url: '/setting/aurecord/confirm/verifyOperation',
    method: 'get',
    params: transData(data),
  })
}

// 系统信息流程-查询
export function getSystemInfo(data) {
  return request({
    url: '/setting/aurecord/confirm/detail',
    method: 'get',
    params: transData(data),
  })
}

// 退出登录，更新在线人数
export function updateOnlineUser(data) {
  return request({
    url: '/setting/login/logout ',
    method: 'post',
    data: transData(data),
    headers: {
      token: token
    }
  })
}

// 获取当前登录用户人数和版本
export function getRefreshInfo(data) {
  return request({
    url: '/setting/baseInfo/getRefreshInfo',
    method: 'get',
    params: transData(data),
  })
}
