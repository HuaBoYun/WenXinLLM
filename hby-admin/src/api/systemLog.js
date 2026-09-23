import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险评估-计划列表
export function getList(data) {
  return request({
    url: '/riskcontrol/pggl/pggl/riplanlist',
    method: 'get',
    params: transData(data),
  })
}

// 风险评估-计划删除
export function deletePlan(query) {
  return request({
    url: '/riskcontrol/pggl/riplandel',
    method: 'POST',
    params: transData(query),
  })
}
// 风险评估-计划详情
export function riskadd(query) {
  return request({
    url: '/riskcontrol/pggl/riskadd',
    method: 'get',
    params: transData(query),
  })
}

// 风险评估-评估任务列表
export function getRiresultList(data) {
  return request({
    url: '/riskcontrol/plan/riresultlist',
    method: 'get',
    params: transData(data),
  })
}

export function riResultItemList(data) {
  return request({
    url: '/riskcontrol/fxxt/fxpg/plan/ri_result_item_list',
    method: 'post',
    data: transData(data),
  })
}

export function riskMain(data) {
  return request({
    url: '/riskcontrol/pggl/risk_main',
    method: 'get',
    params: transData(data),
  })
}
export function riPlanInfo(query) {
  return request({
    url: '/riskcontrol/fxxt/fxyd/strategy_reply_info',
    method: 'get',
    params: transData(query),
  })
}
//风险应对审批里掉用的详情
export function riPlanSInfo(query) {
  return request({
    url: '/riskcontrol/fxxt/fxyd/strategy_reply_spinfo',
    method: 'get',
    params: transData(query),
  })
}

export function riTaskInfo(query) {
  return request({
    url: '/riskcontrol/pggl/riplaninfo',
    method: 'get',
    params: transData(query),
  })
}

export function riPlanAddPage(query) {
  return request({
    url: '/riskcontrol/pggl/riplanaddpage',
    method: 'get',
    params: transData(query),
  })
}
// 新增/编辑计划
export function riPlanAdd(data) {
  return request({
    url: '/riskcontrol/pggl/riplanadd',
    method: 'post',
    data,
    // headers: { 'content-type': 'application/x-www-form-urlencoded' },
  })
}
export function riPlanEdit(data) {
  return request({
    url: '/riskcontrol/pggl/riplanaddupdatepage',
    method: 'post',
    data: transData(data),
  })
}
// 选择风险信息
export function addRiskInfo(data) {
  return request({
    url: '/riskcontrol/pggl/pggl/riskadd',
    method: 'post',
    data: transData(data),
  })
}
// 评估结果列表查询
export function riResultQueryList(data) {
  return request({
    url: '/riskcontrol/fxxt/fxcl/riresultquerylist',
    method: 'post',
    data: transData(data),
  })
}

// 删除评估风险信息
export function delPgglByRisk(data) {
  return request({
    url: '/riskcontrol/pggl/pggl/delPgglByRisk',
    method: 'get',
    params: transData(data),
  })
}

// 保存关联附件
export function saveFile(data) {
  return request({
    url: '/riskcontrol/pggl/saveFile',
    method: 'post',
    data: transData(data),
  })
}

// 添加评估人员
export function saveUsers(data) {
  return request({
    url: '/riskcontrol/pggl/pggl/saveUsers',
    method: 'post',
    data: transData(data),
  })
}

// 设置权重列表
export function estimateUser(data) {
  return request({
    url: '/riskcontrol/pggl/estimate_user',
    method: 'get',
    params: transData(data),
  })
}

// 下发
export function down(data) {
  return request({
    url: '/riskcontrol/pggl/down',
    method: 'post',
    data: transData(data),
  })
}

// 查看已有整改
export function findByeventid(data) {
  return request({
    url: '/riskcontrol/riskEvent/findByeventid',
    method: 'post',
    data: transData(data),
  })
}

// 行业复制--选择行业
export function findOrganizationByTreeAll(data) {
  return request({
    url: '/riskcontrol/common/findOrganizationByTreeAll',
    method: 'post',
    data: transData(data),
  })
}
// 行业复制--行业

export function leftanalysishyCopy(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/leftanalysishy_copy',
    method: 'post',
    data: transData(data),
  })
}
export function listanalysishyCopy(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/listanalysishy_copy',
    method: 'post',
    data: transData(data),
  })
}

// 行业复制功能中的查看流程分析详情
export function dispaddanalysis(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/dispaddanalysis',
    method: 'post',
    data: transData(data),
  })
}
// 内规
export function innerrule(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/innerrule',
    method: 'post',
    data: transData(data),
  })
}
// 外规
export function outerrule(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/outerrule',
    method: 'post',
    data: transData(data),
  })
}
// 复制
export function toYwlcCopy(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/to_ywlc_copy',
    method: 'post',
    data: transData(data),
  })
}

// 复制
export function toYwlc(data) {
  return request({
    url: '/riskcontrol/ywlc/processAnalysis/to_ywlc',
    method: 'post',
    data: transData(data),
  })
}

// 内规列表
export function innerCommonQxwt(data) {
  return request({
    url: '/riskcontrol/innerrule/inner_common_qxwt',
    method: 'post',
    data: transData(data),
  })
}

// 外规列表
export function outerCommonQxwt(data) {
  return request({
    url: '/riskcontrol/outerrule/outer_common_qxwt',
    method: 'post',
    data: transData(data),
  })
}

// 保存权重比例
export function saveProportion(data) {
  return request({
    url: '/riskcontrol/pggl/save_estimate_user',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function taskModalInfo(data) {
  return request({
    url: '/riskcontrol/plan/ri_result_item_list',
    method: 'get',
    params: transData(data),
  })
}

// 评估信息
export function riskResult(data) {
  return request({
    url: '/riskcontrol/risk/fxtz/riskResult',
    method: 'get',
    params: transData(data),
  })
}

// 评估任务列表 选择等级
export function taskModalInfoProcess(data) {
  return request({
    url: '/riskcontrol/plan/ri_result_process_detail',
    method: 'get',
    params: transData(data),
  })
}

// 评估任务列表 选择等级 详细
export function taskModalInfoProcessResult(data) {
  return request({
    url: '/riskcontrol/plan/ri_assessmet_result_disp',
    method: 'get',
    params: transData(data),
  })
}

// 评估任务列表2
export function riskListRt(data) {
  return request({
    url: '/riskcontrol/fxcl/risk_list_rt',
    method: 'get',
    params: transData(data),
  })
}

// 保存评分
export function saveTask(data) {
  return request({
    url: '/riskcontrol/plan/ri_result_save',
    method: 'post',
    data: transData(data),
  })
}

// 提交评分
export function submitTask(data) {
  return request({
    url: '/riskcontrol/plan/ri_result_submit',
    method: 'post',
    data: transData(data),
  })
}

// 评估任务列表
export function downloadFile(data) {
  return request({
    url: '/riskcontrol/file/download',
    responseType: 'blob',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function getAssessmentStandardList(data) {
  return request({
    url: '/riskcontrol/fxbz/v_list',
    method: 'get',
    params: transData(data),
  })
}
// 新增风险评估标准
export function saveAssessmentStandardInfo(data) {
  return request({
    url: '/riskcontrol/fxbz/savebz',
    method: 'post',
    data: transData(data),
  })
}
// 修改风险评估标准
export function updateAssessmentStandardInfo(data) {
  return request({
    url: '/riskcontrol/fxbz/updatebz',
    method: 'post',
    data: transData(data),
  })
}
// 删除风险评估标准
export function deleteAssessmentStandardInfo(data) {
  return request({
    url: '/riskcontrol/fxbz/delete',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function getAssessmentStandardDefaultData(data) {
  return request({
    url: '/riskcontrol/fxbz/get',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function deleteAssessmentStandardList(data) {
  return request({
    url: '/riskcontrol/fxbz/delete',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function getTab1List(data) {
  return request({
    url: '/riskcontrol/fxbz/v_list_yxcd',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function getDefaultTab1Data(data) {
  return request({
    url: '/riskcontrol/fxbz/rsik_yxcd_update',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function updateTab1Data(data) {
  return request({
    url: '/riskcontrol/fxbz/rsik_yxcd_update_submit',
    method: 'post',
    data: transData(data),
  })
}

export function getTab2List(data) {
  return request({
    url: '/riskcontrol/fxbz/v_list_knx',
    method: 'get',
    params: transData(data),
  })
}

// 评估任务列表
export function getDefaultTab2Data(data) {
  return request({
    url: '/riskcontrol/fxbz/rsik_knx_update',
    method: 'get',
    params: transData(data),
  })
}
// 评估任务列表
export function updateTab2Data(data) {
  return request({
    url: '/riskcontrol/fxbz/rsik_knx_update_submit',
    method: 'post',
    data: transData(data),
  })
}

export function getTab3List(data) {
  return request({
    url: '/riskcontrol/fxbz/v_list_jb',
    method: 'get',
    params: transData(data),
  })
}

export function updateTab3Data(data) {
  return request({
    url: '/riskcontrol/fxbz/v_update_jb',
    method: 'post',
    params: transData(data),
  })
}

export function getXXYDList(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/getlist',
    method: 'get',
    params: transData(data),
  })
}
export function getXXYDCode(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/gaincode',
    method: 'post',
    data: transData(data),
  })
}
export function editXXYD(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/saveOrupdate',
    method: 'get',
    params: transData(data),
  })
}
export function XXYDDelete(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/deleteone',
    method: 'get',
    params: transData(data),
  })
}
export function getXXYDDefaultData(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/detail',
    method: 'get',
    params: transData(data),
  })
}
export function deleteXXYDFile(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/deletefj',
    method: 'get',
    params: transData(data),
  })
}
export function getXXYDDefaultFileData(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/ling/getfjList',
    method: 'get',
    params: transData(data),
  })
}


export function getTZGGList(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/notice/getlist',
    method: 'get',
    params: transData(data),
  })
}
export function getTZGGCode(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/noctice/gaincode',
    method: 'post',
    data: transData(data),
  })
}
export function editTZGG(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/notice/saveOrupdate',
    method: 'get',
    params: transData(data),
  })
}
export function TZGGDelete(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/notice/deleteone',
    method: 'get',
    params: transData(data),
  })
}
export function getTZGGDefaultData(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/notice/detail',
    method: 'get',
    params: transData(data),
  })
}
export function deleteTZGGFile(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/notice/deletefj',
    method: 'get',
    params: transData(data),
  })
}
export function getTZGGDefaultFileData(data) {
  return request({
    url: '/fwgl/api-auth/daily/management/notice/getfjList',
    method: 'get',
    params: transData(data),
  })
}

//验证评估计划的风险分配情况
export function riskPlanSituation(data) {
  return request({
    url: '/riskcontrol/pggl/check_assplan',
    method: 'post',
    data: transData(data),
  })
}

//风险评估编号
export function getRiskpgbzNo(data) {
  return request({
    url: '/riskcontrol//fxbz/get_riskpgbz_no',
    method: 'get',
    params: transData(data),
  })
}

//集团评估计划列表
export function getGroupPlanlist(data) {
  return request({
    url: '/riskcontrol/groupPlan/getGroupPlanlist',
    method: 'get',
    params: transData(data),
  })
}
// 集团评估计划-删除
export function riplandel(query) {
  return request({
    url: '/riskcontrol/groupPlan/riplandel',
    method: 'POST',
    params: transData(query),
  })
}
// 集团评估计划-详情
export function riplaninfo(query) {
  return request({
    url: '/riskcontrol/groupPlan/riplaninfo',
    method: 'get',
    params: transData(query),
  })
}
// 集团评估计划-新增
export function riplanadd(params) {
  return request({
    url: `/riskcontrol/groupPlan/riplanadd?assId=${params.assId}&&reporteds=${params.reporteds}&&removeReporteds=${params.removeReporteds}&&startDate=${params.startDate}&&endDate=${params.endDate}`,
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 集团评估计划-下发
export function toIssued(data) {
  return request({
    url: '/riskcontrol/groupPlan/toIssued',
    method: 'post',
    params: transData(data),
  })
}
// 集团跟进结果-列表
export function getGroupPlanResultList(data) {
  return request({
    url: '/riskcontrol/groupPlan/getGroupPlanResultList',
    method: 'get',
    params: transData(data),
  })
}
// 集团评估结果列表查询
export function groupQueryList(data) {
  return request({
    url: '/riskcontrol/fxxt/fxcl/groupQueryList',
    method: 'get',
    params: transData(data),
  })
}

// 系统日志-日志台账
export function getLogBusList(data) {
  return request({
    url: '/rzgl/log/bus/list',
    method: 'POST',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//系统日志，异常日志详情接口
export function getLogBusDetail(params) {
  return request({
    url: '/rzgl/log/bus/detail',
    method: 'GET',
    params: transData(params),
  })
}

// 系统日志-登录日志
export function getLoginLogList(data) {
  return request({
    url: '/rzgl/log/bus/list/login',
    method: 'POST',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

// 系统日志--删除
export function deleteLoginLog(data) {
  return request({
    url: '/rzgl/log/bus/batch/del',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

// 系统日志--登录删除
export function deleteLoginLog2(data) {
  return request({
    url: '/rzgl/log/bus/login/batch/del',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

// 系统日志-异常日志
export function getErrorLogList(data) {
  return request({
    url: '/rzgl/log/bus/list/error',
    method: 'POST',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

//评估列表
export function resultProcessDetailMon(data) {
  return request({
    url: '/riskcontrol/plan/resultProcessDetailMon',
    method: 'get',
    params: transData(data),
  })
}
//评估计划查询
export function riplanTrackList(data) {
  return request({
    url: '/riskcontrol//pggl/pggl/riplanTrackList',
    method: 'get',
    params: transData(data),
  })
}

//风险评估跟踪查看详情接口
export function trackItemList(data) {
  return request({
    url: '/riskcontrol/pggl/plan/trackItemList',
    method: 'get',
    params: transData(data),
  })
}

// 登录日志-导出
export function exportLog(data) {
  return request({
    url: '/rzgl/log/bus/list/login/export',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    responseType: 'blob',
  })
}

// 系统日志-导出
export function exportSystemLog(data) {
  return request({
    url: '/rzgl/log/bus/export',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    responseType: 'blob',
  })
}

// 异常日志-导出
export function exportErrorLog(data) {
  return request({
    url: '/rzgl/log/bus/export/error',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    responseType: 'blob',
  })
}
