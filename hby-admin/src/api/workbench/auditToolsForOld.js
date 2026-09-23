import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//审计模板-审计类型列表
export function getSjlxList(params, headers) {
  return request({
    url: '/oiaudit/nbsjworkSpace/getNbsjTypeListPage',
    method: 'post',
    data: transData(params),
  })
}

//审计模板-列表查询
export function getNbsjTempletePageList(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/getNbsjTempletePageList',
    method: 'post',
    data: transData(params),
  })
}

//审计模板-新增、修改
export function mergeNbsjTemplete(params, headers) {
  return request({
    url: '/oiaudit/nbsjworkSpace/mergeNbsjTemplete',
    method: 'post',
    data: transData(params),
    headers: { orgids: params.orgids },
  })
}

//审计模板-查询单个信息
export function selectTempleteInfo(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/selectTempleteInfo',
    method: 'post',
    headers: { templeteId: params },
  })
}

//审计模板-新增修改获取审计对象的公司树形菜单
export function getOrgTreeListByAuditObj(params) {
  return request({
    url: '/oiaudit/auditProject/getOrgTreeListByAuditObj',
    method: 'get',
    params: transData(params),
  })
}

//审计模板-模板复制功能
export function copyTemplete(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/copyTemplete',
    method: 'post',
    headers: params,
  })
}

//审计模板-删除
export function deleteTempleteInfo(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/deleteTempleteInfo',
    method: 'post',
    headers: { templeteId: params },
  })
}

//审计模板-修改状态
export function updateTempleteStatus(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/updateTempleteStatus',
    method: 'post',
    headers: { templeteId: params },
  })
}

//审计类型获取列表
export function getNbsjTypeListPage(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/getNbsjTypeListPage',
    method: 'post',
    data: transData(params),
  })
}

//审计类型删除
export function delNbsjType(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjtype/sjtype_delete',
    method: 'get',
    params: transData(params),
  })
}

//审计类型新增及修改
export function getSjtypeSave(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjtype/sjtype_save',
    method: 'post',
    data: transData(params),
  })
}
//缺陷查询 获取列表
// export function getSjtypeSave(params) {
//   return request({
//     url: '/audit/nbsjworkSpace/gzdg/def_quexian_update',
//     method: 'post',
//     params: transData(params),
//   })
// }

//获取法律规章列表
export function getOutList(params) {
  return request({
    url: '/audit/nbsjworkSpace/gkzk/mag/out_list',
    method: 'get',
    params: transData(params),
  })
}

//删除法律规章
export function delOutList(params) {
  return request({
    url: '/audit/nbsjworkSpace/gkzk/mag/delete_out',
    method: 'get',
    params: transData(params),
  })
}

//导出法律法规
export function exportOutFile(params) {
  return request({
    url: '/audit/nbsjworkSpace/expOuterRuleFile',
    method: 'get',
    // params: transData(params),
    responseType: 'blob',
    headers: { id: params },
  })
}

//添加法律规章
export function addOutList(params) {
  return request({
    url: '/audit/nbsjworkSpace/gkzk/mag/save_modify_out',
    method: 'post',
    data: transData(params),
  })
}

//查询法律规章详情
export function getOutDetails(params) {
  return request({
    url: '/audit/nbsjworkSpace/gkzk/mag/modify_out',
    method: 'get',
    params: transData(params),
  })
}

//获取管理制度列表
export function getInnerRulePageList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getInnerRulePageList',
    method: 'post',
    data: transData(params),
  })
}
//管理制度查询单个信息
export function selectInnerRuleInfo(params) {
  return request({
    url: '/audit/nbsjworkSpace/selectInnerRuleInfo',
    method: 'post',
    headers: { innerid: params },
  })
}
//管理制度新增修改
export function mergeInnerRule(params) {
  return request({
    url: '/audit/nbsjworkSpace/mergeInnerRule',
    method: 'post',
    params: transData(params),
  })
}
//获取指引模板列表页
export function getNbsjZyTempletePageList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjZyTempletePageList',
    method: 'post',
    data: transData(params),
  })
}

//行业知识库-列表页
export function getOtherarticlePageList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getOtherarticlePageList',
    method: 'post',
    data: transData(params),
  })
}
//行业知识库-新增、修改
export function mergeOtherarticle(params) {
  return request({
    url: '/audit/nbsjworkSpace/mergeOtherarticle',
    method: 'post',
    data: transData(params),
  })
}
//审计经验库-列表页
export function getNbsjjyTempletePageList(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/getNbsjjyTempletePageList',
    method: 'post',
    data: transData(params),
  })
}
//缺陷标准-列表页
export function getNbsjBugCriterionPageList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjBugCriterionPageList',
    method: 'post',
    data: transData(params),
  })
}
//行业知识库-查询单个信息
export function selectOtherarticleInfo(params) {
  return request({
    url: '/audit/nbsjworkSpace/selectOtherarticleInfo',
    method: 'post',
    // params: transData(params),
    headers: { othartid: params },
  })
}
//缺陷标准-新增及修改
export function quexianSave(params) {
  return request({
    url: '/audit/nbsjworkSpace/gzdg/def_quexian_save',
    method: 'post',
    data: transData(params),
  })
}
//缺陷标准-删除
export function quexianDel(params) {
  return request({
    url: '/audit/nbsjworkSpace/gzdg/def_quexian_del',
    method: 'post',
    data: transData(params),
  })
}
//管理制度-删除
export function deleteInnerRuleInfo(params) {
  return request({
    url: '/audit/nbsjworkSpace/deleteInnerRuleInfo',
    method: 'post',
    // params: transData(params),
    headers: { innerid: params },
  })
}

//管理制度-导出
export function exportInnerRuleInfo(params) {
  return request({
    url: '/audit/nbsjworkSpace/expInnerRuleFile',
    method: 'get',
    responseType: 'blob',
    // params: transData(params),
    headers: { id: params },
  })
}

//审计通知书办理
export function handleHanlde(params) {
  return request({
    url: '/audit/nbsjapproval/getTblAdvicenoteApprovalInfo',
    method: 'get',
    params: transData(params),
    headers: { innerid: params },
  })
}

//审计通知书办理按钮驳回或者同意
export function handleButtonClick(params) {
  return request({
    url: '/audit/nbsjapproval/dealTblAdvicenoteApporval',
    method: 'post',
    data: transData(params),
    headers: { innerid: params },
  })
}

//审计报告办理
export function handleReport(params) {
  return request({
    url: '/audit/nbsjapproval/getReportFhApprovalInfo',
    method: 'get',
    params: transData(params),
    headers: { innerid: params },
  })
}

//审计报告办理
export function handleReportButtonClick(params) {
  return request({
    url: '/audit/nbsjapproval/dealReportFhApporvalInfo',
    method: 'post',
    data: transData(params),
    headers: { innerid: params },
  })
}

//审计模板目录-左侧树结构
export function findNbsjTargetTree(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/findNbsjTargetTree',
    method: 'post',
    headers: transData(params),
  })
}

//审计模板目录-左侧树结构2
export function findNbsjTargetTree2(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/findNbsjTargetTree2',
    method: 'post',
    headers: transData(params),
  })
}

//审计模板目录-右侧
export function defCatListZy(params) {
  return request({
    url: '/oiaudit/auditReady/sjyj/def_cat_list_zy',
    method: 'get',
    params: transData(params),
  })
}

//审计模板目录-新增、修改
export function mergeTblNbsjTarget(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/mergeTblNbsjTarget',
    method: 'post',
    data: transData(params),
  })
}

//审计模板目录-查询单个信息
export function selectNbsjTargetInfo(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/selectNbsjTargetInfo',
    method: 'post',
    headers: transData(params),
  })
}

//审计模板目录-删除
export function deleteNbsjTarget(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/deleteNbsjTarget',
    method: 'post',
    headers: transData(params),
  })
}

//审计指引-新增、修改
export function mergeTblNbsjAuditprogram(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/mergeTblNbsjAuditprogram',
    method: 'post',
    data: transData(params),
  })
}

//审计指引-删除
export function deleteNbsjAuditprogram(params) {
  return request({
    url: '/oiaudit/nbsjworkSpace/deleteNbsjAuditprogram',
    method: 'post',
    headers: transData(params),
  })
}
//审计问题类型- 列表查询
export function getNbsjQuestionTypeListPage(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjQuestionTypeListPage',
    method: 'post',
    data: transData(params),
  })
}
//审计问题类型- 新增/修改
export function sjwttype_save(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjwttype/sjwttype_save',
    method: 'post',
    data: transData(params),
  })
}
//审计问题类型- 删除
export function sjwttype_del(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjwttype/sjwttype_del',
    method: 'get',
    params: transData(params),
  })
}

// 统计类型维护-新增、修改
export function statisticsSave(params) {
  return request({
    url: '/audit/nbsjworkSpace/tjlx/stattype_save',
    method: 'post',
    data: transData(params),
  })
}

// 统计类型维护-列表
export function statisticsList(params) {
  return request({
    url: '/audit/nbsjworkSpace/tjlx/getNbsjStatTypeListPage',
    method: 'post',
    data: transData(params),
  })
}

// 统计类型维护-删除
export function statisticsDelete(params) {
  return request({
    url: '/audit/nbsjworkSpace/tjlx/stattype_del',
    method: 'get',
    params: transData(params),
  })
}
// 管理制度自动编号
export function createManageCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByGlzd',
    method: 'post',
    data: transData(params),
  })
}
// 审计模板自动编号
export function createAuditCode(params) {
  return request({
    url: '/oiaudit/auditPlan/getAutoCodeBySjmb',
    method: 'post',
    data: transData(params),
  })
}
// 审计经验库自动编号
export function createExperienceCode(params) {
  return request({
    url: '/oiaudit/auditPlan/getAutoCodeBySjzy',
    method: 'post',
    data: transData(params),
  })
}
// 审计模板库主列表
export function getSJMBKList(params) {
  return request({
    url: '/audit/audit/mb/getmbList',
    method: 'get',
    params: transData(params),
  })
}
// 审计模板库附件删除
export function deleteSjmbkFile(params) {
  return request({
    url: '/audit/audit/mb/delMbAttInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计模板库新增
export function sjmbkAdd(params) {
  return request({
    url: '/audit/audit/mb/mergembInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计模板库删除
export function sjmbkDelete(params) {
  return request({
    url: '/audit/audit/mb/delMbInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计模板库详情
export function getSjmbkInfo(params) {
  return request({
    url: '/audit/audit/mb/getmbDetail',
    method: 'get',
    params: transData(params),
  })
}
// 审计模板库附件详情
export function sjmbkFileList(params) {
  return request({
    url: '/audit/audit/mb/getMbnAttInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计经验库列表
export function getSJJYKList(params) {
  return request({
    url: '/audit/audit/jyk/getjykList',
    method: 'get',
    params: transData(params),
  })
}
// 审计经验库附件删除
export function deleteSjjykFile(params) {
  return request({
    url: '/audit/audit/jyk/deljykAttInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计经验库新增
export function sjjykAdd(params) {
  return request({
    url: '/audit/audit/jyk/mergejykInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计经验库删除
export function sjjykDelete(params) {
  return request({
    url: '/audit/audit/jyk/deljykInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计经验库详情
export function getSjjykInfo(params) {
  return request({
    url: '/audit/audit/jyk/getjykDetail',
    method: 'get',
    params: transData(params),
  })
}
// 审计经验库附件详情
export function sjjykFileList(params) {
  return request({
    url: '/audit/audit/jyk/getjykAttInfo',
    method: 'post',
    data: transData(params),
  })
}
// 审计经验库自动编码
export function createSJJYKCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByNewSjjyk',
    method: 'post',
    data: transData(params),
  })
}
// 审计模板库自动编码
export function createSJMBKCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByNewSjmb',
    method: 'post',
    data: transData(params),
  })
}

//审计类型维护菜单列表
export function getNbsjTypeOfList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjTypeListPage',
    method: 'post',
    data: transData(params),
  })
}
//审计类型维护菜单新增
export function saveNbsjTypeOf(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjtype/sjtype_save',
    method: 'post',
    data: transData(params),
  })
}
//审计类型维护菜单编辑
export function modifyNbsjTypeOf(params) {
  return request({
    url: '/audit/nbsjworkSpace/modifyNbsjTypeOf',
    method: 'post',
    data: transData(params),
  })
}
//审计类型维护菜单详情 
export function getNbsjTypeOfInfo(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjTypeOfInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计类型维护菜单删除
export function removeNbsjTypeOf(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjtype/sjtype_delete',
    method: 'get',
    params: transData(params),
  })
}

//工作台规章制度树
export function getInnerRuleType(params) {
  return request({
    url: '/audit/nbsjworkSpace/getInnerRuleType',
    method: 'post',
    data: transData(params),
  })
}