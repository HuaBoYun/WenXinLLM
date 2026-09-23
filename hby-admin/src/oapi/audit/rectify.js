import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//整改方案关闭
export function closeSolution(params) {
  return request({
    url: '/audit/nbsjzg/closeSolution',
    method: 'post',
    data: transData(params),
  })
}

// 整改方案删除选择的附件
export function delSolutionAttInfo(params) {
  return request({
    url: '/audit/nbsjzg/delSolutionAttInfo',
    method: 'post',
    data: transData(params),
  })
}

// 整改落实，评价删除选择的附件
export function delRefromAttInfo(params) {
  return request({
    url: '/audit/nbsjzg/delRefromAttInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改分派-分派保存人员
export function fpSolutionRy(params) {
  return request({
    url: '/audit/nbsjzg/fpSolutionRy',
    method: 'post',
    data: transData(params),
  })
}

//整改落实-整改内容页面分页功能
export function getLsContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getLsContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改落实-获取整改落实信息
export function getReformByid(params) {
  return request({
    url: '/audit/nbsjzg/getReformByid',
    method: 'post',
    data: transData(params),
  })
}

//整改方案获取所属的附件
export function getSolutionAttInfo(params) {
  return request({
    url: '/audit/nbsjzg/getSolutionAttInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改落实，评价获取所属的附件
export function getReformAttInfo(params) {
  return request({
    url: '/audit/nbsjzg/getReformAttInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改方案查看详情信息
export function getSolutionDetail(params) {
  return request({
    url: '/audit/nbsjzg/getSolutionDetail',
    method: 'get',
    params: transData(params),
  })
}

//整改方案-添加整改内容页面分页功能
export function getZgContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getZgContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改查询列表分页功能
export function getZgcxSolutionmgmtList(params) {
  return request({
    url: '/audit/nbsjzg/getZgcxSolutionmgmtList',
    method: 'get',
    params: transData(params),
  })
}

//整改分派列表分页功能
export function getZgfpsolutionmgmtList(params) {
  return request({
    url: '/audit/nbsjzg/getZgfpsolutionmgmtList',
    method: 'get',
    params: transData(params),
  })
}

//整改跟踪列表分页功能
export function getZggzsolutionmgmtList(params) {
  return request({
    url: '/audit/nbsjzg/getZggzsolutionmgmtList',
    method: 'get',
    params: transData(params),
  })
}

//整改评价列表分页功能
export function getZgjcSolutionmgmtList(params) {
  return request({
    url: '/audit/nbsjzg/getZgjcSolutionmgmtList',
    method: 'get',
    params: transData(params),
  })
}

//整改落实列表分页功能
export function getZglsSolutionmgmtList(params) {
  return request({
    url: '/audit/nbsjzg/getZglsSolutionmgmtList',
    method: 'get',
    params: transData(params),
  })
}

//整改方案列表分页功能
export function getZgsolutionmgmtList(params) {
  return request({
    url: '/audit/nbsjzg/getZgsolutionmgmtList',
    method: 'get',
    params: transData(params),
  })
}

//整改查询-整改内容页面分页功能
export function getcxContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getcxContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改分派-整改内容页面分页功能
export function getfpContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getfpContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改跟踪-整改内容页面分页功能
export function getgzContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getgzContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改评价-整改内容页面分页功能
export function getpjContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getpjContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改评价-获取整改评价信息
export function getpjReformByid(params) {
  return request({
    url: '/audit/nbsjzg/getpjReformByid',
    method: 'post',
    data: transData(params),
  })
}

//未销号问题列表分页功能
export function getwxhContentsList(params) {
  return request({
    url: '/audit/nbsjzg/getwxhContentsList',
    method: 'get',
    params: transData(params),
  })
}

//整改方案下发整改责任人
export function issuePersonliable(params) {
  return request({
    url: '/audit/nbsjzg/issuePersonliable',
    method: 'post',
    data: transData(params),
  })
}

//整改方案新增或修改
export function mergeSolutionInfo(params) {
  return request({
    url: '/audit/nbsjzg/mergeSolutionInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改评价-退回信息
export function pjthReformInfo(params) {
  return request({
    url: '/audit/nbsjzg/pjthReformInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改落实-保存整改落实信息
export function saveReformInfo(params) {
  return request({
    url: '/audit/nbsjzg/saveReformInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改评价-保存评价信息
export function savepjReformInfo(params) {
  return request({
    url: '/audit/nbsjzg/savepjReformInfo',
    method: 'post',
    data: transData(params),
  })
}

//整改方案启动
export function startSolution(params) {
  return request({
    url: '/audit/nbsjzg/startSolution',
    method: 'post',
    data: transData(params),
  })
}

//整改落实-提交整改落实信息
export function tjReform(params) {
  return request({
    url: '/audit/nbsjzg/tjReform',
    method: 'post',
    data: transData(params),
  })
}

//整改评价-提交评价信息
export function tjpjReform(params) {
  return request({
    url: '/audit/nbsjzg/tjpjReform',
    method: 'post',
    data: transData(params),
  })
}

//未销号-再次下发
export function wxhXfSolutionRy(params) {
  return request({
    url: '/audit/nbsjzg/wxhXfSolutionRy',
    method: 'post',
    data: transData(params),
  })
}

//项目管理列表
export function projectList(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_list',
    method: 'get',
    params: transData(params),
  })
}
//项目管理列表
export function projectList1(params) {
  return request({
    url: '/audit/nbsjzg/getZgproject_list',
    method: 'get',
    params: transData(params),
  })
}

//获取问题整改结果信息
export function getzgjgReformByid(params) {
  return request({
    url: '/audit/nbsjzg/getzgjgReformByid',
    method: 'get',
    params: transData(params),
  })
}

//整改查询-根据整改内容查询结果集合
export function getzgjgReformlist(params) {
  return request({
    url: '/audit/nbsjzg/getzgjgReformlist',
    method: 'get',
    params: transData(params),
  })
}

//整改查询导出
export function exploredInfo(params) {
  return request({
    url: '/audit/nbsjzg/zgcx/exploredInfo',
    method: 'post',
    responseType: 'blob',
    data: transData(params),
  })
}

//整改台账-分页列表
export function getzlAllReformlist(params) {
  return request({
    url: '/audit/nbsjzg/getzlAllReformlist',
    method: 'get',
    params: transData(params),
  })
}
// 删除列表row
export function deleteSolution(params) {
  return request({
    url: '/audit/nbsjzg/deleteSolution',
    method: 'post',
    data: transData(params),
  })
}
// 新建整改方案编号
export function createSchemeCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByZgfa',
    method: 'post',
    data: transData(params),
  })
}
export function delZhengGaiInfo(params) {
  return request({
    url: '/audit/nbsjzg/delSolutionnr',
    method: 'post',
    data: transData(params),
  })
}
