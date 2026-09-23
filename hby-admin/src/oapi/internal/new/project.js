import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价立项-主页查询
export function getProjectList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_initiate',
    method: 'get',
    params: transData(params),
  })
}
//评价立项删除
export function deleteProject(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_delete',
    method: 'post',
    data: transData(params),
  })
}
//评价立项新增评价负责人弹框
export function userList(params) {
  return request({
    url: '/nkhg/nbkz/pjlx/list',
    method: 'get',
    params: transData(params),
  })
}
//评价立项新增评价模板弹框
export function templateList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_tmplLocation',
    method: 'get',
    params: transData(params),
  })
}
//评价立项新增
export function projectAdd(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/add',
    method: 'post',
    data: transData(params),
  })
}
//评价立项修改回填接口
export function getProjectDefaultInfo(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_modify',
    method: 'get',
    params: transData(params),
  })
}
//评价立项修改保存接口
export function projectUpdate(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_update',
    method: 'post',
    data: transData(params),
  })
}
//评价立项列表查看评价模板
export function getTemplateModalTableDate(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_diap',
    method: 'get',
    params: transData(params),
  })
}
//评价立项列表点击授权按钮弹出列表
export function getAuthorizationModalData(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_org',
    method: 'get',
    params: transData(params),
  })
}
//评价立项授权弹框点击授权按钮弹出列表
export function getAuthorization2ModalData(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_modify',
    method: 'get',
    params: transData(params),
  })
}
//评价立项授权弹框参评人弹框列表
export function ZZUserList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/setting_user_list',
    method: 'get',
    params: transData(params),
  })
}
//评价立项授权弹框主评人保存
export function saveZPRInfo(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/savezupingUsers',
    method: 'post',
    data: transData(params),
  })
}
//评价立项授权弹框参评人保存
export function saveCPRInfo(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/savecanpingUsers',
    method: 'post',
    data: transData(params),
  })
}
//评价立项授权弹框参评人评分列表
export function ccScoreList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_assignor',
    method: 'get',
    params: transData(params),
  })
}
//评价立项授权弹框参评人评分弹框保存
export function saveCCScoreInfo(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/SaveAssignor',
    method: 'post',
    data: transData(params),
  })
}
//评价立项自动编号
export function createProjectCode(params) {
  return request({
    url: '/hgglext/csfa/code/findAutoNumber',
    method: 'get',
    params: transData(params),
  })
}
//评价立项启动
export function startProject(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/start',
    method: 'post',
    data: transData(params),
  })
}
// 评价立项-评价人适用性修改
export function updateShiYongXing(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/updateShiYongXing',
    method: 'get',
    params: transData(params),
  })
}

// 业务编号自动创建 通用方法
export const createBusinessNo = (params) => {
  return request({
    url: '/nkhg/nbkz/code/findAutoNumberByChoice',
    method: 'get',
    params: transData(params),
  })
}

// 风险编号自动创建 riskcontrol/nbkz/code/findNumberLevelNexidByParent
export const createLevelByParent = (params) => {
  return request({
    url: '/riskcontrol/nbkz/code/findNumberLevelNexidByParent',
    method: 'get',
    params: transData(params),
  })
}
//评价立项-参评人管理列表-删除参评人
export const deletecanpingUsers = (params) => {
  return request({
    url: `/nkhg/nbkz/pjgl/deletecanpingUsers?z=${params}`,
    method: 'delete',
  })
}
//评价立项-主评人/参评人-左侧部门树
export const leftTree = (params) => {
  return request({
    url: '/nkhg/nbkz/pjgl/tree',
    method: 'get',
    params: transData(params),
  })
}

//项目信息字段编辑页变活接口
export function getDefaultRenderData(params) {
  return request({
    url: `/setting/customize/preview/details`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//项目信息字段变活接口
export function getDefaultListRenderData(params) {
  return request({
    url: `/setting/customize/preview/list`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
