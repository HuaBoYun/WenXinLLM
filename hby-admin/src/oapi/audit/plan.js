import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getLxZxsjHzChooseList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/getLxZxsjHzChooseList',
    method: 'get',
    params: transData(params),
  })
}

export function getEvaluationList(params) {
  return request({
    url: '/oiaudit/auditProject/getEvaluationList',
    method: 'get',
    params: transData(params),
  })
}
export function getLeaveAuditTwoLevel(params) {
  return request({
    url: '/oiaudit/auditProject/getLeaveAuditTwoLevel',
    method: 'get',
    params: transData(params),
  })
}
export function getRzsjmxListDraftPlan(params) {
  return request({
    url: '/oiaudit/auditProject/getRzsjmxListDraftPlan',
    method: 'get',
    params: transData(params),
  })
}
export function getJhGlhzList(params) {
  return request({
    url: '/oiaudit/auditProject/getJhGlhzList',
    method: 'get',
    params: transData(params),
  })
}

export function getOtherAuditList(params) {
  return request({
    url: '/oiaudit/auditProject/getOtherAuditList',
    method: 'get',
    params: transData(params),
  })
}

export function getLxZxsjHzDetailList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/getLxZxsjHzDetailList',
    method: 'get',
    params: transData(params),
  })
}

export function setLxZxsjHzShowList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/setLxZxsjHzShowList',
    method: 'post',
    params: transData(params),
  })
}
//三级 下发 list
export function getDetailDistributeList(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/getDetailDistributeList',
    method: 'get',
    params: transData(params),
  })
}
//三级 填报 list
export function getDistributeReceiveList(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/getDistributeReceiveList',
    method: 'get',
    params: transData(params),
  })
}


//三级 下发
export function saveDistributionPerson(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/saveDistributionPerson',
    method: 'post',
    data: transData(params),
  })
}

//序号接口
export function getNumber(params) {
  return request({
    url: '/oiaudit/plan/leave/audit2L/getAutoNo',
    method: 'get',
    params: transData(params),
  })
}

//三级单位离任审计季度
export function lrjjzesqJdList(params) {
  return request({
    url: '/oiaudit/quarter/getList',
    method: 'get',
    params: transData(params),
  })
}

//修改密码
export function modifyUserPassword(params) {
  return request({
    url: '/setting/user/modifyUserPassWord',
    method: 'post',
    data: transData(params),
  })
}
//计划管理列表
export function planManagePageList(params) {
  return request({
    url: '/audit/audit/planManageController/planManagePageList',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理列表
export function getAuditPlanPageList(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanPageList',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理，通过计划主键获取计划项目集合
export function getPlanProjectListByPlanId(params) {
  return request({
    url: '/audit/auditPlan/getProjectListByWspJhw',
    method: 'get',
    params: transData(params),
  })
}
// export function getPlanProjectListByPlanId(params) {
//   return request({
//     url: '/audit/auditPlan/getPlanProjectListByPlanId',
//     method: 'get',
//     params: transData(params),
//   })
// }
//审计计划管理-根据计划主键查找计划信息
export function getAuditPlanViewDetail(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanViewDetail',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理，根据审计计划主键获取审计计划的附件
export function getAuditPlanAttInfo(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanAttInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理-根据计划主键删除计划信息
export function deleteAuditPlanByPlanId(params) {
  return request({
    url: '/audit/auditPlan/deleteAuditPlanByPlanId',
    method: 'get',
    params: transData(params),
  })
}
//审计计划中计划项目新增或修改
export function mergePlanProjectManageInfoList(params) {
  return request({
    url: '/audit/auditPlan/mergePlanProjectManageInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划上层表单提交
export function mergeAuditPlanInfo(params) {
  return request({
    url: '/audit/auditPlan/mergeAuditPlanInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理 提交审批
export function submitAuditPlanApproval(params) {
  return request({
    url: '/audit/nbsjapproval/submitAuditPlanApproval',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理 删除单条
export function removePlanProjectInfo(params) {
  return request({
    url: '/audit/auditPlan/removePlanProjectInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理-根据附件主键删除附件接口
export function deleteFileById(params) {
  return request({
    url: '/audit/auditPlan/deleteFileById',
    method: 'get',
    params: transData(params),
  })
}
//审计计划查看
export function auditPlanListLook(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanViewInfo',
    method: 'get',
    params: transData(params),
  })
}
//获取审计计划详情
export function auditPlanViewDetail(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanViewDetail',
    method: 'get',
    params: transData(params),
  })
}

//新增年度计划
export function createPlan(params) {
  return request({
    url: '/audit/auditPlan/addAutoPlan',
    method: 'post',
    data: transData(params),
  })
}

// 以下 计划管理接口 >>>

// 建设项目投资完成情况 list
export function jsxmtzwcqkList(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqk/list',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目投资完成情况 删除
export function jsxmtzwcqkDel(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqk/delete',
    method: 'get',
    params: transData(params),
  })
}

// 建设项目投资完成情况 list
export function jsxmtzwcqkDetail(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqk/detail',
    method: 'get',
    params: transData(params),
  })
}

// 建设项目完成情况选择列表查询接口
export function jsxmjbqkJswcgetlist(params) {
  return request({
    url: '/oiaudit/jsxmjbqk/jswcgetlist',
    method: 'get',
    params: transData(params),
  })
}


// 建设项目投资完成情况 保存
export function jsxmtzwcqkUpdate(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqk/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

// 建设项目投资完成情况 明细保存
export function jsxmtzwcqkmxUpdate(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqkfymx/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
// 建设项目投资完成情况 明细详情
export function jsxmtzwcqkfymxDetail(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqkfymx/detail',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目投资完成情况删除
export function jsxmtzwcqkDelete(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqkfymx/delete',
    method: 'get',
    params: transData(params),
  })
}
// 三级单位离任审计
export function sjdwlrsjlrList(params) {
  // list
  return request({
    url: '/oiaudit/plan/leave/audit3L/getjdList',
    method: 'get',
    params: transData(params),
  })
}


export function sjdwlrsjlrExportData(params) {
  // 导出
  return request({
    url: '/oiaudit/plan/leave/audit3L/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

export function sjdwlrsjlrDistributeFund(params) {
  // 下发
  return request({
    url: '/oiaudit/plan/leave/audit3L/distributeFund',
    method: 'get',
    params: transData(params),
  })
}

export function sjdwlrsjlrDetail(params) {
  // detail
  return request({
    url: '/oiaudit/plan/leave/audit3L/detail',
    method: 'get',
    params: transData(params),
  })
}

export function sjdwlrsjlrUpdate(params) {
  // edit
  return request({
    url: '/oiaudit/plan/leave/audit3L/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

export function sjdwlrsjlrDelete(params) {
  // delete
  return request({
    url: '/oiaudit/plan/leave/audit3L/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 二级单位离任审计
export function ejdwlrsjList(params) {
  // list
  return request({
    url: '/oiaudit/plan/leave/audit2L/getList',
    method: 'get',
    params: transData(params),
  })
}

export function ejdwlrsjDetail(params) {
  // detail
  return request({
    url: '/oiaudit/plan/leave/audit2L/detail',
    method: 'get',
    params: transData(params),
  })
}

export function ejdwlrsjUpdate(params) {
  // edit
  return request({
    url: '/oiaudit/plan/leave/audit2L/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

export function ejdwlrsjDelete(params) {
  // delete
  return request({
    url: '/oiaudit/plan/leave/audit2L/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 计划管理 - 二级单位及成员离任审计 - 导出
export function lrjyjlrsjExportList(params) { // export
  return request({
    url: '/oiaudit/plan/leave/audit2L/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//计划管理 - 二级单位及成员离任审计 - 下发
export function lrjyjlrsjXf(data) {
  return request({
    url: '/oiaudit/plan/leave/audit2L/xf',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data,
  })
}

// 未委托及预计离任
export function wwtjyjlrList(params) {
  // list
  return request({
    url: '/oiaudit/plan/leave/expect/getList',
    method: 'get',
    params: transData(params),
  })
}

export function wwtjyjlrDetail(params) {
  // detail
  return request({
    url: '/oiaudit/plan/leave/expect/detail',
    method: 'get',
    params: transData(params),
  })
}

export function wwtjyjlrUpdate(params) {
  // add edit
  return request({
    url: '/oiaudit/plan/leave/expect/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

export function wwtjyjlrDelete(params) {
  // delete
  return request({
    url: '/oiaudit/plan/leave/expect/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 计划管理 - 未委托及预计离任 - 导出
export function wwtjyjlrExportList(params) { // export
  return request({
    url: '/oiaudit/plan/leave/expect/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//计划管理 - 未委托及预计离任 - 下发
export function wwtjyjlrXf(data) {
  return request({
    url: '/oiaudit/plan/leave/expect/xf',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data,
  })
}

// 二级机构任中立建议表
export function ejjgrzlxjyList(params) {
  // list
  return request({
    url: '/oiaudit/plan/audit/suggestion2L/getList',
    method: 'get',
    params: transData(params),
  })
}

export function ejjgrzlxjyDetail(params) {
  // detail
  return request({
    url: '/oiaudit/plan/audit/suggestion2L/detail',
    method: 'get',
    params: transData(params),
  })
}

export function ejjgrzlxjyUpdate(params) {
  // edit
  return request({
    url: '/oiaudit/plan/audit/suggestion2L/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

export function ejjgrzlxjyDelete(params) {
  // delete
  return request({
    url: '/oiaudit/plan/audit/suggestion2L/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 工程专项排序表
export function gczxpxbList(params) {
  // list
  return request({
    url: '/oiaudit/plan/project/sort/getList',
    method: 'get',
    params: transData(params),
  })
}

export function gczxpxbDetail(params) { // detail
  return request({
    url: '/oiaudit/plan/project/sort/detail',
    method: 'get',
    params: transData(params),
  })
}

export function gczxpxbUpdate(params) {
  // edit
  return request({
    url: '/oiaudit/plan/project/sort/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

export function gczxpxbDelete(params) {
  // delete
  return request({
    url: '/oiaudit/plan/project/sort/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 财务专项排序表
export function cwzxpxbList(params) {
  // list
  return request({
    url: '/oiaudit/plan/finance/sort/getList',
    method: 'get',
    params: transData(params),
  })
}

export function cwzxpxbListDetail(params) { // detail
  return request({
    url: '/oiaudit/plan/finance/sort/detail',
    method: 'get',
    params: transData(params),
  })
}

export function cwzxpxbUpdate(params) {
  // edit
  return request({
    url: '/oiaudit/plan/finance/sort/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

export function cwzxpxbDelete(params) {
  // delete
  return request({
    url: '/oiaudit/plan/finance/sort/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 计划管理，计划需求
export function jhxqList(params) {
  // list
  return request({
    url: '/oiaudit/jhgljhxq/list',
    method: 'get',
    params: transData(params),
  })
}

export function jhxqUpdate(params) {
  // edit
  return request({
    url: '/oiaudit/jhgljhxq/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

export function jhxqDelete(params) {
  // delete
  return request({
    url: '/oiaudit/jhgljhxq/delete',
    method: 'get',
    params: transData(params),
  })
}

export function jhxqImport(params) {
  // import
  return request({
    url: '/oiaudit/jhgljhxq/importData',
    method: 'post',
    data: transData(params),
  })
}

export function jhxqExport(params) {
  // export
  return request({
    url: '/oiaudit/jhgljhxq/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

export function jhxSubmit(params) {
  // 提交 审批
  return request({
    url: '/oiaudit/jhgljhxq/submit',
    method: 'post',
    data: transData(params),
  })
}

// 计划管理 - 工程项目验收计划
export function engineeringProjectExaminePlanList(params) { // list
  return request({
    url: '/oiaudit/gcxmjgysjh/list',
    method: 'get',
    params: transData(params),
  })
}
// 计划管理 - 新工程项目验收计划
export function getxzListPlan(params) { // list
  return request({
    url: '/oiaudit/gcxmjgysjh/getxzListPlan',
    method: 'get',
    params: transData(params),
  })
}

// 计划管理 - 工程项目验收计划 - 导出
export function exportList(params) { // export
  return request({
    url: '/oiaudit/gcxmjgysjh/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

export function engineeringProjectExaminePlanDetail(params) { // detail
  return request({
    url: '/oiaudit/gcxmjgysjh/detail',
    method: 'get',
    params: transData(params),
  })
}

export function engineeringProjectExaminePlanUpdate(params) { // update
  return request({
    url: '/oiaudit/gcxmjgysjh/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

export function engineeringProjectExaminePlanDelete(params) { // delete
  return request({
    url: '/oiaudit/gcxmjgysjh/delete',
    method: 'get',
    params: transData(params),
  })
}

// 计划管理 - 工程项目造价中间表
export function engineeringCostCenterTableList(params) { // list
  return request({
    url: '/oiaudit/gcxmzjzjb/list',
    method: 'get',
    params: transData(params),
  })
}
// 计划管理 - 工程项目造价中间表
export function planAnalysis(params) { // list
  return request({
    url: '/oiaudit/auditProject/planAnalysis',
    method: 'get',
    params: transData(params),
  })
}

export function auditSituationOverview(params) { // list
  return request({
    url: '/oiaudit/auditControlAnalysis/auditSituationOverview',
    method: 'get',
    params: transData(params),
  })
}

export function getReviewStatusList(params) { // list
  return request({
    url: '/oiaudit/project/implementPlan/getReviewStatusList',
    method: 'get',
    params: transData(params),
  })
}

export function getplanAnalysisXmList(params) { // list
  return request({
    url: '/oiaudit/auditProject/getplanAnalysisXmList',
    method: 'get',
    params: transData(params),
  })
}

export function engineeringCostCenterTableDetail(params) { // detail
  return request({
    url: '/oiaudit/gcxmzjzjb/detail',
    method: 'get',
    params: transData(params),
  })
}

//序号接口
export function gcxmzjZjbNo(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/getAutoNo',
    method: 'get',
    params: transData(params),
  })
}

//序号接口
export function getAutoNo(params) {
  return request({
    url: '/oiaudit/gcxmjgysjh/getAutoNo',
    method: 'get',
    params: transData(params),
  })
}


export function engineeringCostCenterTableUpdate(params) { // update
  return request({
    url: '/oiaudit/gcxmzjzjb/saveOrUpdate',
    method: 'post',
    // headers: {
    //   "Content-Type": "application/json;charset=utf-8"
    // },
    data: transData(params),
  })
}

export function engineeringCostCenterTableDelete(params) { // delete
  return request({
    url: '/oiaudit/gcxmzjzjb/delete',
    method: 'get',
    params: transData(params),
  })
}

export function engineeringCostCenterTableExport(params) { // export
  return request({
    url: '/oiaudit/gcxmzjzjb/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 计划管理 - 工程项目基本情况（竣工结算申请）
export function engineeringBasicInformationList(params) { // list
  return request({
    url: '/oiaudit/jsxmjbqk/list',
    method: 'get',
    params: transData(params),
  })
}

export function engineeringBasicInformationDetail(params) { // detail
  return request({
    url: '/oiaudit/jsxmjbqk/detail',
    method: 'get',
    params: transData(params),
  })
}

export function engineeringBasicInformationUpdate(params) { // update
  return request({
    url: '/oiaudit/jsxmjbqk/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

export function engineeringBasicInformationDelete(params) { // delete
  return request({
    url: '/oiaudit/jsxmjbqk/delete',
    method: 'get',
    params: transData(params),
  })
}
export function flVerify(params) { // flVerify
  return request({
    url: '/oiaudit/jsxmjbqk/flVerify',
    method: 'post',
    data: transData(params),
  })
}

export function projectSuggestionDetail(params) { // delete
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/detail',
    method: 'get',
    params: transData(params),
  })
}

export function projectSuggestionSaveOrUpdate(params) { // update
  return request({
    url: '/oiaudit/plan/projectSuggestion/notice/saveOrUpdate',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

export function exportJsdwxmjshzList(params) { // update
  return request({
    url: '/oiaudit/gcxmzj/statistical/selectTblYqnsGcxmzjJsdwStatisticalList',
    method: 'post',
    data: transData(params),
    // headers: {
    //   'Content-Type': 'application/json',
    // },
  })
}

export function exportSgdwxmjshzList(params) { // update
  return request({
    url: '/oiaudit/gcxmzj/statistical/selectTblYqnsGcxmzjSgdwStatisticalList',
    method: 'post',
    data: transData(params),
    // headers: {
    // 	'Content-Type': 'application/json',
    // },
  })
}
export function exportNwbdwxmjshzList(params) { // update
  return request({
    url: '/oiaudit/gcxmzj/statistical/selectTblYqnsGcxmzjNwbStatisticalList',
    method: 'post',
    data: transData(params),
    // headers: {
    // 	'Content-Type': 'application/json',
    // },
  })
}
export function exportEwhzjcsbList(params) { // update
  return request({
    url: '/oiaudit/gcxmzj/statistical/selectTblYqnsGcxmzjSampleStatisticalList',
    method: 'post',
    data: transData(params),
    // headers: {
    // 	'Content-Type': 'application/json',
    // },
  })
}
export function exportSjdwlrshhzList(params) { // update
  return request({
    url: '/oiaudit/plan/leave/audit3L/selectLeaveAudit3LSummary',
    method: 'get',
    params: transData(params),
    // headers: {
    //   'Content-Type': 'application/json',
    // },
  })
}

//检测金额
export function jsxmtzwcqkCheckMoney(params) {
  return request({
    url: '/oiaudit/jsxmtzwcqk/checkMoney',
    method: 'get',
    params: transData(params),
  })
}

//三级离任审计保存
export function lrjjzrsqSave(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/jdsaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
//三级离任审计保存
export function lrjjzrsqDetail(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/jddetail',
    method: 'get',
    params: transData(params),
  })
}
//三级离任审计保存
export function lrjjzrsqdelete(params) {
  return request({
    url: '/oiaudit/plan/leave/audit3L/jddelete',
    method: 'delete',
    params: transData(params),
  })
}

export function sjdwlrsjlrListDetail(params) {
  // detail
  return request({
    url: '/oiaudit/plan/leave/audit3L/getList',
    method: 'get',
    params: transData(params),
  })
}

export function lxjyzypgBaseSave(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/tbsaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
export function lxjyzypgBaseDetail(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/tbdetail',
    method: 'get',
    params: transData(params),
  })
}
export function getLxjyzypgBaseRelateList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/getEvaluationList',
    method: 'get',
    params: transData(params),
  })
}
export function deleteLxjyzypgBaseRelateList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/delete',
    method: 'delete',
    params: transData(params),
  })
}
export function rzsjmxBaseSave(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/tbsaveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
export function rzsjmxBaseDetail(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/tbdetail',
    method: 'get',
    params: transData(params),
  })
}
export function rzsjmxBaseRelateListDetail(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/getList',
    method: 'get',
    params: transData(params),
  })
}
export function rzsjmxBaseRelateListDelete(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 建设项目投资完成情况
export function jsxmtzwcqkhzHzList(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmtzwcqkhz/hz/list',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目投资完成情况详情
export function jsxmtzwcqkhzHzDetail(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmtzwcqkhz/detail',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目投资完成情况删除
export function jsxmtzwcqkhzHzDelete(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmtzwcqkhz/delete',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目投资完成情况新增修改
export function saveOrUpdate(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmtzwcqkhz/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

// 建设项目基本情况汇总
export function jsxmtzwcqkhzHzHzList(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmjbqkhz/hz/list',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目基本情况汇总删除
export function jsxmjbqkHzDelete(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmjbqkhz/delete',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目基本情况汇总详情
export function jsxmjbqkHzDetail(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmjbqkhz/detail',
    method: 'get',
    params: transData(params),
  })
}
// 建设项目基本情况汇总保存
export function jsxmjbqkHzSaveOrUpdate(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmjbqkhz/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
// 工程建设项目竣工决算的列表查询接口
export function getJsxmtzhzList(params) {
  // detail
  return request({
    url: '/oiaudit/jsxmtzwcqkhz/getJsxmtzhzList',
    method: 'get',
    params: transData(params),
  })
}

// 计划备案列表
export function getPlanFilingList(params) {
  // detail
  return request({
    url: '/oiaudit/audit/plan/getPlanFilingList',
    method: 'get',
    params: transData(params),
  })
}
// 计划备案删除
export function getPlanFilingDel(params) {
  // detail
  return request({
    url: '/oiaudit/audit/plan/delete',
    method: 'get',
    params: transData(params),
  })
}
// 计划备案详情
export function getPlanFilingDetail(params) {
  // detail
  return request({
    url: '/oiaudit/audit/plan/detail',
    method: 'get',
    params: transData(params),
  })
}
// 计划备案新增修改
export function getPlanFilingSave(params) {
  // detail
  return request({
    url: '/oiaudit/audit/plan/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//任务分配-工程项目结算汇总查看列表数据 31
export function getgcsjxmapbView1(params) {
  // detail
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getGcxmjsListByhz',
    method: 'get',
    params: transData(params),
  })
}

//任务分配-工程项目结算汇总查看列表数据 32
export function getgcsjxmapbView2(params) {
  // detail
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getJsxmtzListByhz',
    method: 'get',
    params: transData(params),
  })
}

//任务分配-工程财务结算汇总查看列表数据 23
export function getSjdwlrsjSbListByhz(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getSjdwlrsjSbListByhz',
    method: 'get',
    params: transData(params),
  })
}