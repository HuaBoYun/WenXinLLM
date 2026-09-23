import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//理论研讨通知- 列表查询
export function getLlyttzList(params) {
  return request({
    url: '/oiaudit/yttz/getList',
    method: 'get',
    params: transData(params),
  })
}

//理论研讨通知- 保存或修改
export function saveLlyytzData(params) {
  return request({
    url: '/oiaudit/yttz/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}

//理论研讨通知- 查询详情
export function getLlyttzDetails(params) {
  return request({
    url: '/oiaudit/yttz/getone',
    method: 'get',
    params: transData(params),
  })
}

//理论研讨通知- 查询附件列表
export function getLlyttzAttList(params) {
  return request({
    url: '/oiaudit/yttz/getattList',
    method: 'get',
    params: transData(params),
  })
}

//理论研讨通知- 删除附件
export function deleteLlyytzatt(params) {
  return request({
    url: '/oiaudit/yttz/deleteatt',
    method: 'post',
    data: transData(params),
  })
}

// 理论研讨通知- 删除
export function deleteLlyytz(params) {
  return request({
    url: '/oiaudit/yttz/deleteone',
    method: 'post',
    data: transData(params),
  })
}

//理论研讨通知- 下发人员
export function dxfryLlyytz(params) {
  return request({
    url: '/oiaudit/yttz/xfry',
    method: 'post',
    data: transData(params),
  })
}

//理论研究上报-填报列表
export function getLlyjsbList(params) {
  return request({
    url: '/oiaudit/yjsb/gettdList',
    method: 'get',
    params: transData(params),
  })
}

//理论研究上报-添加或修改填报信息
export function saveLlyjsbData(params) {
  return request({
    url: '/oiaudit/yjsb/tbsaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//理论研究上报-子表保存或修改
export function saveLlyjsbzbData(params) {
  return request({
    url: '/oiaudit/yjsb/saveOrupdatezb',
    method: 'post',
    data: transData(params),
  })
}

//理论研究上报-子表列表查询
export function getLlyjsbzbList(params) {
  return request({
    url: '/oiaudit/yjsb/getList',
    method: 'get',
    params: transData(params),
  })
}

//理论研究上报-根据ID获取填报详细信息
export function getLlyjsbData(params) {
  return request({
    url: '/oiaudit/yjsb/tbdetail',
    method: 'get',
    params: transData(params),
  })
}

//理论研究上报- 子表查询详情 
export function getLlyjsbzbData(params) {
  return request({
    url: '/oiaudit/yjsb/getone',
    method: 'get',
    params: transData(params),
  })
}

//理论研究上报-删除填报信息
export function deleteLlyjsb(params) {
  return request({
    url: '/oiaudit/yjsb/tbdelete',
    method: 'delete',
    data: transData(params),
  })
}

//理论研究台账- 列表查询
export function getLlyjtzList(params) {
  return request({
    url: '/oiaudit/yatz/getList',
    method: 'get',
    params: transData(params),
  })
}

//理论研究台账-导出
export function llyjtzExport(params) {
  return request({
    url: '/oiaudit/yatz/exportt',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//论文上报- 列表查询
export function getLwsbList(params) {
  return request({
    url: '/oiaudit/lwsb/getList',
    method: 'get',
    params: transData(params),
  })
}

//论文上报- 保存或修改
export function saveLwsbData(params) {
  return request({
    url: '/oiaudit/lwsb/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}

//论文上报- 查询详情
export function getLwsbData(params) {
  return request({
    url: '/oiaudit/lwsb/getone',
    method: 'get',
    params: transData(params),
  })
}

//论文上报- 查询附件列表
export function getLwsbzbList(params) {
  return request({
    url: '/oiaudit/lwsb/getattList',
    method: 'get',
    params: transData(params),
  })
}

//论文上报- 删除附件
export function deleteLwsbatt(params) {
  return request({
    url: '/oiaudit/lwsb/deleteatt',
    method: 'post',
    data: transData(params),
  })
}

//论文上报- 删除
export function deleteLwsb(params) {
  return request({
    url: '/oiaudit/lwsb/deleteone',
    method: 'post',
    data: transData(params),
  })
}

//论文上报- 上报
export function lwsbXfry(params) {
  return request({
    url: '/oiaudit/lwsb/xfry',
    method: 'post',
    data: transData(params),
  })
}

//论文台账- 列表查询
export function getLwtzList(params) {
  return request({
    url: '/oiaudit/lwtz/gettzList',
    method: 'get',
    params: transData(params),
  })
}

//论文台账-导出
export function lwtzExport(params) {
  return request({
    url: '/oiaudit/lwtz/exportt',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//论文台账-退回
export function returnThlw(params) {
  return request({
    url: 'oiaudit/lwsb/thlw',
    method: 'post',
    params: transData(params),
  })
}

//论文汇总排名表- 列表查询
export function getLwhzpmbList(params) {
  return request({
    url: '/oiaudit/lwpx/getzpxList',
    method: 'get',
    params: transData(params),
  })
}

//论文排序-填报列表
export function getLwpxList(params) {
  return request({
    url: '/oiaudit/lwpx/gettdList',
    method: 'get',
    params: transData(params),
  })
}

//论文排序-添加或修改填报信息
export function saveLwpxData(params) {
  return request({
    url: '/oiaudit/lwpx/tbsaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//论文排序- 选择论文上报列表查询
export function getLwpxSelectList(params) {
  return request({
    url: '/oiaudit/lwpx/gettzList',
    method: 'get',
    params: transData(params),
  })
}

//论文排序-根据ID获取填报详细信息
export function getLwpxData(params) {
  return request({
    url: '/oiaudit/lwpx/tbdetail',
    method: 'get',
    params: transData(params),
  })
}

//论文排序- 子表论文上报列表查询
export function getLwpxzbList(params) {
  return request({
    url: '/oiaudit/lwpx/getList',
    method: 'get',
    params: transData(params),
  })
}
//论文排序- 导出
export function lwpxExportList(params) {
  return request({
    url: '/oiaudit/lwpx/exportList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//论文排序- 查询打分集合
export function getLwpxfsList(params) {
  return request({
    url: '/oiaudit/lwpx/getfsList',
    method: 'get',
    params: transData(params),
  })
}

//论文排序-添加或修改分数信息
export function fssaveOrUpdate(params) {
  return request({
    url: '/oiaudit/lwpx/fssaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//论文排序-删除填报信息
export function deleteLwpx(params) {
  return request({
    url: '/oiaudit/lwpx/tbdelete',
    method: 'delete',
    data: transData(params),
  })
}
//论文排序-子表删除填报信息
export function deletezbLwpx(params) {
  return request({
    url: '/oiaudit/lwpx/deletezb',
    method: 'post',
    data: transData(params),
  })
}
//论文排序-提交审批验证
export function yzpfLwpx(params) {
  return request({
    url: '/oiaudit/lwpx/yzpf',
    method: 'get',
    params: transData(params),
  })
}

// 论文获奖名单- 列表查询
export function getLwhjmdList(params) {
  return request({
    url: '/oiaudit/hjmd/getList',
    method: 'get',
    params: transData(params),
  })
}

//论文获奖规则- 填报列表查询
export function getLwhjgzList(params) {
  return request({
    url: '/oiaudit/hjgz/getList',
    method: 'get',
    params: transData(params),
  })
}

//论文获奖规则-添加或修改填报信息
export function saveLwhjgzData(params) {
  return request({
    url: '/oiaudit/hjgz/tbsaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//论文获奖规则-根据年验证规则是否存在
export function isAddYear(params) {
  return request({
    url: '/oiaudit/hjgz/yzyear',
    method: 'get',
    params: transData(params),
  })
}

//论文获奖规则-根据ID获取填报详细信息
export function getLwhjgzData(params) {
  return request({
    url: '/oiaudit/hjgz/tbdetail',
    method: 'get',
    params: transData(params),
  })
}

//论文获奖规则-删除填报信息
export function deleteLwhjgz(params) {
  return request({
    url: '/oiaudit/hjgz/tbdelete',
    method: 'delete',
    data: transData(params),
  })
}

//论文评优-获奖通知

//列表查询
export function getHjtzList(params) {
  return request({
    url: '/oiaudit/hjtz/getList',
    method: 'get',
    params: transData(params),
  })
}
//列表删除
export function deleteHjtz(params) {
  return request({
    url: '/oiaudit//hjtz/deleteone',
    method: 'post',
    data: transData(params),
  })
}
//下发
export function hjtzxfry(params) {
  return request({
    url: '/oiaudit//hjtz/hjtzxfry',
    method: 'post',
    data: transData(params),
  })
}
//详情
export function getHjtz(params) {
  return request({
    url: '/oiaudit/hjtz/getone',
    method: 'get',
    params: transData(params),
  })
}
//保存
export function saveHjtz(params) {
  return request({
    url: '/oiaudit/hjtz/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}
//附件列表
export function getattList(params) {
  return request({
    url: '/oiaudit//hjtz/getattList',
    method: 'get',
    params: transData(params),
  })
}
//附件删除
export function deleteHjtzatt(params) {
  return request({
    url: '/oiaudit/hjtz/deleteatt',
    method: 'post',
    data: transData(params),
  })
}
