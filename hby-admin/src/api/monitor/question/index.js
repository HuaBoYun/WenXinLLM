import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//缺陷管理-新增与修改
export function bugcriidList(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/bug_criterion_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改
export function defectAdd(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_add',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-删除
export function defectDel(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_del',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-明细
export function defectDetail(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_detail',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理列表
export function defectList(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理列表
export function defectFileExport(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_file_export',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-附件列表
export function defectFileList(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_file_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-内规列表
export function innerCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/inner_common_qxwt',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-添加内规
export function addInnerCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/qxwt/add_inner_qxwt',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-新增与修改-查询内规
export function getInnerCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/inner_common_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-删除内规
export function delInnerCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/delete_qx_inner',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增与修改-外规列表
export function outerCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/outer_common_qxwt',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-新增外规
export function addOuterCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/qxwt/add_outer_qxwt',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-查询外规
export function getOuterCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/outer_common_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-删除外规
export function delOuterCommonQxwt(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/delete_qx_outer',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-查询关联缺陷
export function associateDefect(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjbb/defect_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-添加关联缺陷
export function addAssociateDefect(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/qxwt/add_defect_qxwt',
    method: 'post',
    data: transData(params),
  })
}

//缺陷管理-查询关联缺陷
export function getAssociateDefect(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/qxwt/defect_link',
    method: 'get',
    params: transData(params),
  })
}

//缺陷管理-删除关联缺陷
export function delAssociateDefect(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/delete_qx_child',
    method: 'get',
    params: transData(params),
  })
}

//底稿汇总列表
export function dgListall(params) {
  return request({
    url: '/audit/auditImplement/sjss/dg_listall',
    method: 'get',
    params: transData(params),
  })
}
//底稿汇总列表1
export function getDgListAll(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/sjss/dgAllPageList',
    method: 'get',
    params: transData(params),
  })
}

//底稿汇总--详情
export function dgDetail(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_detail',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-新增与修改
export function riskAdd(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/risk_add',
    method: 'post',
    data: transData(params),
  })
}

//风险发现-附件列表
export function getFileList(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/risk_file_list',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-风险容忍度查询
export function getRrdList(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/fxrrd_list',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-风险容忍度保存
export function addRrd(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/fxrrd_save',
    method: 'post',
    data: transData(params),
  })
}
//风险发现-风险容忍度删除
export function delRrd(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/fxrrd_del',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-删除
export function riskDel(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/risk_del',
    method: 'get',
    params: transData(params),
  })
}

//风险发现-明细
export function riskDetail(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/risk_detail',
    method: 'get',
    params: transData(params),
  })
}

//风险发现列表
export function riskList(params) {
  return request({
    url: '/monitor/cyber/YjptProblemSumController/wthz/risk_list',
    method: 'get',
    params: transData(params),
  })
}


//计划管理-二级单位离任审计列表
export function getListDraftPlan(params) {
  return request({
    url: '/oiaudit/plan/leave/audit2L/getListDraftPlan',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-二级单位离任审计列表
export function audit2LGetList(params) {
  return request({
    url: '/oiaudit/plan/leave/audit2L/getList',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-二级机构任中立项建议表列表
export function suggestion2LGetList(params) {
  return request({
    url: '/oiaudit/plan/audit/suggestion2L/getList',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-三级单位离任审计列表
export function audit3LGetList(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/getList',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿-删除
export function jhgljhcgDelete(params) {
  return request({
    url: '/oiaudit/jhgljhcg/delete',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿-单个详情
export function jhgljhcgDetail(params) {
  return request({
    url: '/oiaudit/jhgljhcg/detail',
    method: 'get',
    params: transData(params),
    headers: {
      'jhcgid': params.jhcgid
    }
  })
}

//计划管理-计划草稿-导出数据
export function jhgljhcgExportData(params) {
  return request({
    url: '/oiaudit/jhgljhcg/exportData',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿-导入数据
export function jhgljhcgImportData(params) {
  // export function jhgljhcgImportData(data, params) {
  return request({
    url: '/oiaudit/jhgljhcg/importData',
    method: 'post',
    params: transData(params),
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    // data: transData(data),
    // params,
  })
}

//计划管理-计划草稿-列表查询
export function jhgljhcgList(params) {
  return request({
    url: '/oiaudit/jhgljhcg/list',
    method: 'get',
    params: transData(params),
  })
}
// //计划管理-计划初稿-列表查询
// export function jhgljhchugList(params) {
//   return request({
//     url: '/oiaudit/jhgljhchug/list',
//     method: 'get',
//     params: transData(params),
//   })
// }
//计划管理-选择计划
export function detailJhCg(params) {
  return request({
    url: '/oiaudit/jhgljhchug/detailJhCg',
    method: 'get',
    params: transData(params),
  })
}

export function detailJhCg2(params) {
  return request({
    url: '/oiaudit/jhgljh/detailJhCg',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿-复制
export function copyUniquecg(params) {
  return request({
    url: '/oiaudit/jhgljhcg/copyUniqueCg',
    method: 'post',
    params: transData(params),
  })
}


//计划管理-计划草稿-新增修改
export function jhgljhcgSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/jhgljhcg/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

//计划管理-计划草稿明细-删除
export function jhgljhcgmxDelete(params) {
  return request({
    url: '/oiaudit/jhgljhcgmx/delete',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿明细-单个详情
export function jhgljhcgmxDetail(params) {
  return request({
    url: '/oiaudit/jhgljhcgmx/detail',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿明细-导出
export function jhgljhcgmxExportData(params) {
  return request({
    url: '/oiaudit/jhgljhcgmx/exportData',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿明细-导入数据
export function jhgljhcgmxImportData(params) {
  // export function jhgljhcgImportData(data, params) {
  return request({
    url: '/oiaudit/jhgljhcgmx/importData',
    method: 'post',
    params: transData(params),
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    // data: transData(data),
    // params,
  })
}

//计划管理-计划草稿明细-列表查询
export function jhgljhcgmxList(params) {
  return request({
    url: '/oiaudit/jhgljhcgmx/list',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划草稿明细-新增修改
export function jhgljhcgmxSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/jhgljhcgmx/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}

//计划管理-计划初稿-删除
export function jhgljhchugDelete(params) {
  return request({
    url: '/oiaudit/jhgljhchug/delete',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划初稿-单个详情
export function jhgljhchugDetail(params) {
  return request({
    url: '/oiaudit/jhgljhchug/detail',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划初稿-列表查询
export function jhgljhchugList(params) {
  return request({
    url: '/oiaudit/jhgljhchug/list',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划初稿-新增修改
export function jhgljhchugSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/jhgljhchug/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

//计划管理-计划初稿明细-新增修改
export function jhgljhchugmxSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/jhgljhchugmx/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}



//其他中计润过计划Id乐取关联流择的数据”
export function getListForChoose(params) {
  return request({
    url: '/oiaudit/other/audit/getListForChoose',
    method: 'get',
    params: transData(params),
  })
}


//计划管理-计划-删除
export function jhgljhDelete(params) {
  return request({
    url: '/oiaudit/jhgljh/delete',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划-单个详情
export function jhgljhDetail(params) {
  return request({
    url: '/oiaudit/jhgljh/detail',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划-列表查询
export function jhgljhList(params) {
  return request({
    url: '/oiaudit/jhgljh/list',
    method: 'get',
    params: transData(params),
  })
}

//计划管理-计划-新增修改
export function jhgljhSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/jhgljh/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

//计划管理-计划明细-新增修改
export function jhgljhmxSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/jhgljhmx/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}



//审计实施-审计工作记录-删除
export function workRecordsDelete(params) {
  return request({
    url: '/oiaudit/workRecords/delete',
    method: 'get',
    params: transData(params),
  })
}

//审计实施-审计工作记录-单个详情
export function workRecordsDetail(params) {
  return request({
    url: '/oiaudit/workRecords/getRecordsById',
    method: 'get',
    params: transData(params),
  })
}

//审计实施-审计工作记录-列表查询
export function workRecordsList(params) {
  return request({
    url: '/oiaudit/workRecords/getRecordsList',
    method: 'get',
    params: transData(params),
  })
}

//审计实施-审计工作记录-新增修改
export function workRecordsSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/workRecords/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}


//计划编制---工程审计项目安排--删除
export function planArrangeDelete(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/delete',
    method: 'post',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排--上报
export function enginAuditProjectXmzsb(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/xmzsb',
    method: 'post',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排--退回
export function enginAuditProjectXmzsbth(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/xmzsbth',
    method: 'post',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排填报表-附件列表
export function enginFileList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/file_list',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排填报表-附件删除
export function enginFileDel(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/file_del',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---工程项目安排表-列表导出
export function enginAuditProjectExport(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
//计划编制---工程项目安排表-工程项目结算汇总查看列表数据 31导出
export function enginAuditProjectExportgcjs(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/exportgcjs',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
//计划编制---工程项目安排表-工程项目结算汇总查看列表数据 32导出
export function enginAuditProjectExporttzwc(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/exporttzwc',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排--删除
export function planOutArrangeDelete(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/tbgcdelete',
    method: 'DELETE',
    params: transData(params),
  })
}

//计划编制---工程审计项目安排---根据ID查询工程审计项目安排
export function planArrangeDetail(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/findById',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排---根据ID查询工程审计项目安排
export function planOutArrangeDetail(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/tbgcdetail',
    method: 'get',
    params: transData(params),
  })
}

//计划编制---工程审计项目安排---查询工程审计项目安排列表
export function planArrangeList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/findList',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---工程审计项目安排---查询工程审计项目安排列表
export function planOutArrangeList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/getgcList',
    method: 'get',
    params: transData(params),
  })
}

//计划编制---工程审计项目安排--新增/更新
export function planArrangeSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}
//计划编制---工程审计项目安排--新增/更新
export function planOutArrangeSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/tbgcsaveOrUpdate',
    method: 'post',
    data: transData(data),
  })
}
//计划编制---工程督导分工-列表
export function getddfgList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/getddfgList',
    method: 'get',
    params: transData(params),
  })
}

//项目管理---工程督导分工-详情-分工
export function ddfpJsfp_gc_save(data) {
  return request({
    url: '/oiaudit/project/implementPlan/ddfp/jsfp_gc_save',
    method: 'post',
    data: transData(data),
  })
}

//计划编制---工程--下发科室人员
export function xfksrygc(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/gcxfksry',
    method: 'post',
    data: transData(data),
  })
}
//计划编制---工程--下发组员
export function xfxmzrygc(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/gcxfxmzry',
    method: 'post',
    data: transData(data),
  })
}
//计划编制---工程审计项目安排-批量下发助审人员
export function gcxfzsrys(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/gcxfzsrys',
    method: 'post',
    data: transData(data),
  })
}
//计划编制---财务审计项目安排-批量下发助审人员
export function xfzsrys(data) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/xfzsrys',
    method: 'post',
    data: transData(data),
  })
}
//计划编制---工程督导分工--下发
export function gcfpksry(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/gcfpksry',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    data: transData(data),
  })
}
//计划编制---工程督导分工--审理科人员下发
export function gcfpslkry(data) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/fpslkry',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    data: transData(data),
  })
}

//计划编制---财务审计项目安排--删除
export function fundAuditProjectDelete(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/delete',
    method: 'post',
    params: transData(params),
  })
}
//计划编制---财务项目人员上报接口--上报
export function fundAuditProjectXmzsb(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/xmzsb',
    method: 'post',
    params: transData(params),
  })
}
//计划编制---财务审计项目安排--退回
export function fundAuditProjectXmzsbth(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/xmzsbth',
    method: 'post',
    params: transData(params),
  })
}
//计划编制---财务审计项目安排填报表-附件列表
export function fundFileList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/file_list',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---财务审计项目安排填报表-附件删除
export function fundFileDel(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/file_del',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---财务审计项目安排--删除
export function fundAuditOutProjectDelete(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/tbdelete',
    method: 'DELETE',
    data: transData(params),
  })
}
//计划编制---财务项目安排表-列表导出
export function fundAuditOutProjectExport(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
//计划编制---财务项目安排表-三级单位离任审计导出
export function fundAuditOutProjectExportsjdw(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/exportsjdw',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//计划编制---财务审计项目安排---根据ID查询工程审计项目安排
export function fundAuditProjectDetail(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/findById',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---财务审计项目安排---根据ID查询工程审计项目安排
export function fundAuditOutProjectDetail(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/tbdetail',
    method: 'get',
    params: transData(params),
  })
}

//计划编制---财务审计项目安排---查询工程审计项目安排列表
export function fundAuditProjectList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/findList',
    method: 'get',
    params: transData(params),
  })
}

//计划编制---财务审计项目安排---查询工程审计项目安排列表
export function fundAuditOutProjectList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/getList',
    method: 'get',
    params: transData(params),
  })
}
//计划编制---财务督导分工--列表
export function cwgetddfgList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/getddfgList',
    method: 'get',
    params: transData(params),
  })
}

//计划编制---财务督导分工--下发
export function cwfpddksry(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/fpddksry',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    data: transData(params),
  })
}

//计划编制---财务督导分工--审理科人员下发
export function cwfpslkry(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/fpslkry',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    data: transData(params),
  })
}

//计划编制---财务审计项目安排--新增/更新
export function fundAuditProjectSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}
//计划编制---财务审计项目安排--新增/更新
export function fundAuditOutProjectSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/tbsaveOrUpdate',
    method: 'post',

    data: transData(data),
  })
}
//计划编制---财务审计项目安排--下发科室人员
export function xfksry(data) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/xfksry',
    method: 'post',

    data: transData(data),
  })
}
//计划编制---财务审计项目安排--下发组员
export function xfxmzry(data) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/xfxmzry',
    method: 'post',

    data: transData(data),
  })
}

//计划编制-分管领导汇总-删除
export function fgldhzDelete(params) {
  return request({
    url: '/oiaudit/fgldhz/delete',
    method: 'get',
    params: transData(params),
  })
}

//计划编制-分管领导汇总-单个详情
export function fgldhzDetail(params) {
  return request({
    url: '/oiaudit/fgldhz/detail',
    method: 'get',
    params: transData(params),
  })
}

// 计划编制-分管领导汇总-导出
export function fgldhzExportList(params) {
  return request({
    url: '/oiaudit/fgldhz/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//计划编制-分管领导汇总-下发
export function fgldhzXf(data) {
  return request({
    url: '/oiaudit/fgldhz/xf',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data,
  })
}

//计划编制-分管领导汇总-列表查询
export function fgldhzList(params) {
  return request({
    url: '/oiaudit/fgldhz/list',
    method: 'get',
    params: transData(params),
  })
}

//计划编制-分管领导汇总-新增修改
export function fgldhzSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/fgldhz/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

//计划编制-审计立项建议通知-列表查询
export function proposalNoticeList(params) {
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/getList',
    method: 'get',
    params: transData(params),
  })
}

//计划编制-审计立项建议通知-新增修改
export function proposalNoticeUpdate(params) {
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

//计划编制-审计立项建议通知-查询详情
export function proposalNoticeDetail(params) {
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/detail',
    method: 'get',
    params: transData(params),
  })
}
//计划编制-审计立项建议通知-查询详情
export function proposalNoticeDelete(params) {
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/delete',
    method: 'delete',
    params: transData(params),
  })
}

//计划编制-审计立项建议通知-下发
export function sjlxjytzXf(data) {
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/xf',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data,
  })
}

//计划编制-审计立项建议通知-通过审计立项建议通知id 查询分发人员
export function proposalNoticeGetDistributeList(params) {
  return request({
    url: '/oiaudit/audit/proposalNotice/getDistributeList',
    method: 'get',
    params: transData(params),
  })
}
