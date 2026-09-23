import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 供应商监控主列表

// 缺陷管理左侧树
export function getFlawTreeData(params) {
  return request({
    url: '/riskcontrol/nbkz/comm/findOrganizationByTreeAllss',
    method: 'get',
    params: transData(params),
  })
}
// 缺陷管理列表
export function getFlawList(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_list',
    method: 'get',
    params: transData(params),
  })
}
//缺陷管理新增
export function addFlaw(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_add',
    method: 'post',
    data: transData(params),
  })
}
//缺陷管理删除
export function deleteFlaw(params) {
  return request({
    url: `/riskcontrol//pggl/qxgl/defect_del`,
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-导出
export function exportFlaw(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_file_export',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改
export function bugcriidList(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/bug_criterion_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改
export function defectAdd(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_add',
    method: 'post',
    data: transData(params),
  })
}

//附件删除
export function deleteFieldById(params) {
  return request({
    url: '/riskcontrol/delete',
    method: 'post',
    data: transData(params),
  })
}

//附件  下载
export function download(params) {
  return request({
    url: '/riskcontrol/download',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-删除
export function defectDel(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_del',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-明细
export function defectDetail(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/bug_criterion_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理列表
export function defectList(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理 - 导出
export function defectFileExport(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_file_export',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-附件列表
export function defectFileList(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_file_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-内规列表
export function innerCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/inner_common_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-添加内规
export function addInnerCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/inner_common_qxwt',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-新增与修改-查询内规
export function getInnerCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/inner_common_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-删除内规
export function delInnerCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/delete_qx_inner',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-外规列表
export function outerCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/outer_common_qxwt',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增外规
export function addOuterCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/add_outer_qxwt',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-查询外规
export function getOuterCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/outer_common_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-删除外规
export function delOuterCommonQxwt(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/delete_qx_outer',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-查询关联缺陷
export function associateDefect(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-添加关联缺陷
export function addAssociateDefect(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/add_defect_qxwt',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-查询关联缺陷
export function getAssociateDefect(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/defect_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-删除关联缺陷
export function delAssociateDefect(params) {
  return request({
    url: '/riskcontrol/pggl/qxgl/delete_qx_child',
    method: 'get',
    params: transData(params),
  })
}

// 底稿汇总列表
export function getDraftList(params) {
  return request({
    url: '/riskcontrol/nbkz/nkgj/dg_index',
    method: 'get',
    params: transData(params),
  })
}

//底稿汇总 详细
export function dgDetail(params) {
  return request({
    url: '/riskcontrol/nbkz/nkgj/detial_all',
    method: 'get',
    params: transData(params),
  })
}

// 风险发现列表
export function getRiskList(params) {
  return request({
    url: '/riskcontrol/nbkz/wthz/risk_index',
    method: 'get',
    params: transData(params),
  })
}

// 底稿汇总列表
export function dgAllPageList(params) {
  return request({
    url: '/riskcontrol/nbkz/nkgj/project_standard_list_all',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-新增与修改
export function riskAdd(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/risk_add',
    method: 'post',
    data: transData(params),
  })
}

//风险发现-附件列表
export function getFileList(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/risk_file_list',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-附件删除
export function riskFileDel(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/risk_file_del',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-风险容忍度查询
export function getRrdList(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/fxrrd_list',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-风险容忍度保存
export function addRrd(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/fxrrd_save',
    method: 'post',
    data: transData(params),
  })
}
//风险发现-风险容忍度删除
export function delRrd(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/fxrrd_del',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-删除
export function riskDel(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/risk_del',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-明细
export function riskDetail(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/risk_detail',
    method: 'get',
    params: transData(params),
  })
}

//风险发现列表
export function riskList(params) {
  return request({
    url: '/riskcontrol/pggl/fxfx/risk_list',
    method: 'get',
    params: transData(params),
  })
}
