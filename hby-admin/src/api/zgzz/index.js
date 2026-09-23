import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//上传决策文件附件列表
export function saveReportMeetFileR(params) {
  return request({
    url: '/audit/zgzz/saveReportMeetFileR',
    method: 'post',
    data: transData(params),
  })
}
//上传决策文件附件列表
export function getReportMeetFileList(params) {
  return request({
    url: '/audit/zgzz/getReportMeetFileList',
    method: 'get',
    params: transData(params),
  })
}

//删除决策文件附件
export function deleteReportMeetFile(params) {
  return request({
    url: '/audit/zgzz/deleteReportMeetFile',
    method: 'post',
    data: transData(params),
  })
}
// 整改落实-详情
export function getRectificationImplDetailInfo(params) {
  return request({
    url: '/audit/zgzz/getRectificationImplDetailInfo',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单-列表查询
export function getIssuesList(params) {
  return request({
    url: '/audit/zgzz/getIssuesList',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单-新增与修改
export function saveIssues(params) {
  return request({
    url: '/audit/zgzz/saveIssues',
    method: 'post',
    data: transData(params),
  })
}

// 整改清单-获取审计问题接口
export function summaryAuditList(params) {
  return request({
    url: '/audit/zgzz/summaryAuditList',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单-获取内控问题接口
export function problemLedgerList(params) {
  return request({
    url: '/audit/zgzz/problemLedgerList',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单-获取详情接口
export function getIssuesDetail(params) {
  return request({
    url: '/audit/zgzz/getIssuesDetail',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单-列表导出接口
export function exportIssuesList(params) {
  return request({
    url: '/audit/zgzz/exportIssuesList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 整改清单-列表删除
export function delIssues(params) {
  return request({
    url: '/audit/zgzz/delIssues',
    method: 'post',
    data: transData(params),
  })
}
// 整改清单-关闭
export function closeIssues(params) {
  return request({
    url: '/audit/zgzz/modifyIssuesStatus',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单-获取被审计/评价对象列表接口
export function getAuditedObjectList(params) {
  return request({
    url: '/audit/zgzz/auditedObjectList',
    method: 'get',
    params: transData(params),
  })
}

// 整改方案-列表查询
export function getRectificationPlanList(params) {
  return request({
    url: '/audit/zgzz/getRectificationPlanList',
    method: 'get',
    params: transData(params),
  })
}

// 整改方案-新增与修改
export function saveRectificationPlan(params) {
  return request({
    url: '/audit/zgzz/saveRectificationPlan',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(params),
  })
}

// 整改方案-获取详情接口
export function getRectificationPlanDetail(params) {
  return request({
    url: '/audit/zgzz/getRectificationPlanDetail',
    method: 'get',
    params: transData(params),
  })
}

// 整改方案-获取项目列表接口
export function getSolutionProjectList(params) {
  return request({
    url: '/audit/zgzz/getSolutionProjectList',
    method: 'get',
    params: transData(params),
  })
}

// 整改方案-根据项目获取整改内容清单
export function getAfterProjectlssues(params) {
  return request({
    url: '/audit/zgzz/getAfterProjectIssues',
    method: 'get',
    params: transData(params),
  })
}

// 整改方案-列表导出接口
export function exportRectificationPlanLedger(params) {
  return request({
    url: '/audit/zgzz/exportRectificationPlanLedger',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 整改方案-列表删除
export function delRectification(params) {
  return request({
    url: '/audit/zgzz/delRectification',
    method: 'post',
    data: transData(params),
  })
}

// 整改方案-删除附件
export function delRectificationFile(params) {
  return request({
    url: '/audit/zgzz/delRectificationFile',
    method: 'post',
    data: transData(params),
  })
}

// 整改方案-删除关联的整改清单
export function delRectificationIssues(params) {
  return request({
    url: '/audit/zgzz/delRectificationIssues',
    method: 'post',
    data: transData(params),
  })
}

// 整改方案-获取关联整改清单所有相关数据详情
export function getIssuesAllDetailInfo(params) {
  return request({
    url: '/audit/zgzz/getIssuesAllDetailInfo',
    method: 'get',
    params: transData(params),
  })
}

// 整改方案-保存整改方案与整改清单关系业务表中的数据
export function saveIssuesRelaPlan(params) {
  return request({
    url: '/audit/zgzz/saveIssuesRelaPlan',
    method: 'post',
    data: transData(params),
  })
}

// 整改方案-整改内容编辑-删除附件
export function delPlanIssuesFile(params) {
  return request({
    url: '/audit/zgzz/delPlanIssuesFile',
    method: 'post',
    data: transData(params),
  })
}

// 整改分派-列表查询
export function getRectificationAllocationList(params) {
  return request({
    url: '/audit/zgzz/getRectificationAllocationList',
    method: 'get',
    params: transData(params),
  })
}

// 整改分派 ,整改评价-列表页获取整改清单列表分页
export function getRectificationAllocationIssuesList(params) {
  return request({
    url: '/audit/zgzz/getRectificationAllocationIssuesList',
    method: 'get',
    // responseType: 'blob',
    params: transData(params),
  })
}

// 整改分派 选择整改落实人 后 保存整改落实人信息
export function saveIssuesRelaImpementer(params) {
  return request({
    url: '/audit/zgzz/saveIssuesRelaImpementer',
    method: 'post',
    data: transData(params),
  })
}

// 整改落实-转发人员列表
export function zgzzGetzfUserlist(params) {
  return request({
    url: '/audit/zgzz/getzfUserlist',
    method: 'get',
    params: transData(params),
  })
}

// 整改落实-列表分页查询
export function getMyRectificationList(params) {
  return request({
    url: '/audit/zgzz/getMyRectificationList',
    method: 'get',
    params: transData(params),
  })
}
// 整改落实台账-列表分页查询
export function getMyRectificationLedgerList(params) {
  return request({
    url: '/audit/zgzz/getRecitfiacationImpleLedger',
    method: 'get',
    params: transData(params),
  })
}

// 整改落实-保存落实信息
export function saveRectificationImpl(params) {
  return request({
    url: '/audit/zgzz/saveRectificationImpl',
    method: 'post',
    data: transData(params),
  })
}

// 整改落实-删除附件关联
export function removeRectificationImplAtt(params) {
  return request({
    url: '/audit/zgzz/removeRectificationImplAtt',
    method: 'post',
    data: transData(params),
  })
}

// 后续整改-保存整改落实信息
export function saveHxRectificationImpl(params) {
  return request({
    url: '/audit/zgzz/saveHxRectificationImpl',
    method: 'post',
    data: transData(params),
  })
}

// 后续整改-列表分页查询
export function zgzzGethxzgList(params) {
  return request({
    url: '/audit/zgzz/gethxzgList',
    method: 'get',
    params: transData(params),
  })
}

// 整改评价-分页查询
export function getRectificationValuation(params) {
  return request({
    url: '/audit/zgzz/getRectificationValuation',
    method: 'get',
    params: transData(params),
  })
}

// 整改评价-获取评价详情
export function getZgzzeEvaluationDetail(params) {
  return request({
    url: '/audit/zgzz/getZgzzeEvaluationDetail',
    method: 'get',
    params: transData(params),
  })
}

// 整改评价-保存落实信息
export function saveRectificationValuation(params) {
  return request({
    url: '/audit/zgzz/saveRectificationValuation',
    method: 'post',
    data: transData(params),
  })
}

// 整改评价-删除附件关联
export function removeRectiValuaAttRela(params) {
  return request({
    url: '/audit/zgzz/removeRectiValuaAttRela',
    method: 'post',
    data: transData(params),
  })
}

// 整改评价-全部评价完成后整改完成操作
export function completeRectificationEval(params) {
  return request({
    url: '/audit/zgzz/completeRectificationEval',
    method: 'post',
    data: transData(params),
  })
}

// 整改台账-列表查询
export function getRectificationPlanLedger(params) {
  return request({
    url: '/audit/zgzz/getRectificationPlanLedger',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单台账-列表查询
export function getRectificationIssuesLedgetList(params) {
  return request({
    url: '/audit/zgzz/getRectificationIssuesLedgetList',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单台账-列表导出接口
export function exportIssuesLedgetList(params) {
  return request({
    url: '/audit/zgzz/exportIssuesLedgetList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 整改报告-列表查询
export function getReportList(params) {
  return request({
    url: '/audit/zgzz/getReportList',
    method: 'get',
    params: transData(params),
  })
}
// 整改报告-根据报告类型查询方案
export function getRectificationPlanByReportType(params) {
  return request({
    url: '/audit/zgzz/getRectificationPlanByReportType',
    method: 'get',
    params: transData(params),
  })
}

// 整改报告-新增与修改
export function saveReport(params) {
  return request({
    url: '/audit/zgzz/saveReport',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    data: transData(params),
  })
}

// 整改报告-删除
export function delReport(params) {
  return request({
    url: '/audit/zgzz/delReport',
    method: 'post',
    data: transData(params),
  })
}
// 整改报告-附件删除
export function delReportFile(params) {
  return request({
    url: '/audit/zgzz/delReportFile',
    method: 'post',
    data: transData(params),
  })
}

// 整改报告-获取详情接口
export function getReportDetail(params) {
  return request({
    url: '/audit/zgzz/getReportDetail',
    method: 'get',
    params: transData(params),
  })
}

// 未销号问题-列表查询
export function getUnresolvedIssuesList(params) {
  return request({
    url: '/audit/zgzz/getUnresolvedIssuesList',
    method: 'get',
    params: transData(params),
  })
}

// 未销号问题-再次发起整改
export function issuesRectificationAgain(params) {
  return request({
    url: '/audit/zgzz/issuesRectificationAgain',
    method: 'get',
    params: transData(params),
  })
}

// 整改报告-选择方案后获取整改清单内容信息
export function getRectificationIssuesListByReportType(params) {
  return request({
    url: '/audit/zgzz/getRectificationIssuesListByReportType',
    method: 'get',
    params: transData(params),
  })
}

// 整改报告-整改方案报告-整改清单-详情
export function getIssuesAllDetailInfoByPlanIssuesId(params) {
  return request({
    url: '/audit/zgzz/getIssuesAllDetailInfoByPlanIssuesId',
    method: 'get',
    params: transData(params),
  })
}

// 整改清单导出
export function exportZGList(params) {
  return request({
    url: '/audit/zgzz/exportMyRectificationList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 责任清单导出
export function exportZRList(params) {
  return request({
    url: '/audit/zgzz/exportMyRectificationResponseList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 问题清单导出
export function exportWTList(params) {
  return request({
    url: '/audit/zgzz/exportMyRectificationIssuesList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//列表
export function getBeforeList(params) {
  return request({
    url: '/audit/zgzz/getBeforeList',
    method: 'get',
    params: transData(params),
  })
}
//批量删除
export function deleteBeforeList(params) {
  return request({
    url: '/audit/zgzz/deleteBeforeList',
    method: 'get',
    params: transData(params),
  })
}
//编号自动生成格式 ：编号-公司代码-年度-四位流水号
export function findAutoNumber(params) {
  return request({
    url: '/audit/zgzz/code/findAutoNumber',
    method: 'get',
    params: transData(params),
  })
}

export function exportWTQD(params) {
  return request({
    url: '/audit/zgzz/exportBeforeIssuesList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

export function exportZRQD(params) {
  return request({
    url: '/audit/zgzz/exportBeforeResponseList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

export function exportZGQD(params) {
  return request({
    url: '/audit/zgzz/exportBeforeZgList',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
