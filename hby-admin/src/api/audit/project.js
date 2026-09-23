import request from '@/utils/request'
import { transData } from '@/utils/requestData'
//项目管理列表查询
export function getNbsjProjectPageList(params) {
  return request({
    url: '/audit/projectManage/getNbsjProjectPageList',
    method: 'get',
    params: transData(params),
  })
}
//项目管理明细
export function getProjectDetail(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_detail',
    method: 'get',
    params: transData(params),
  })
}
//项目附件删除
export function deleteProjectFile(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_file_del',
    method: 'get',
    params: transData(params),
  })
}
//项目模板 查询审计模板下业务单元数量
export function seachTeamplateCnt(params) {
  return request({
    url: '/audit/auditProject/xmgl/tempele_biz_cnt',
    method: 'get',
    params: transData(params),
  })
}
// 项目管理-新增修改获取审计对象的公司树形菜单
export function getOrgTreeListByAuditObj(params) {
  return request({
    url: '/audit/projectManage/getOrgTreeListByAuditObj',
    method: 'get',
    params: transData(params),
  })
}
// 项目管理-新增修改页面获取审计计划后获取审计开始和结束时间
export function getNbsjAuditPlanDateInfo(params) {
  return request({
    url: '/audit/auditProject/getNbsjAuditPlanDateInfo',
    method: 'get',
    params: transData(params),
  })
}
// 项目管理-获取项目经理列表
export function getUserList(params) {
  return request({
    url: '/audit/auditImplement/user/user_list',
    method: 'get',
    params: transData(params),
  })
}
//获取审计模板审计指引列表
export function getNbsjTempleteList(params) {
  return request({
    url: '/audit/auditProject/getNbsjTempleteList',
    method: 'post',
    data: transData(params),
  })
}

//项目管理-新增修改页面获取审计计划集合
export function getNbsjAuditPlanListForMerge(params) {
  return request({
    url: '/audit/projectManage/getNbsjAuditPlanListForMerge',
    method: 'get',
    params: transData(params),
  })
}
//项目管理-新增修改获取审计对象的公司树形菜单
export function getOrgTreeListByAuditObjd(params) {
  return request({
    url: '/audit/projectManage/getOrgTreeListByAuditObjd',
    method: 'get',
    params: transData(params),
  })
}
//项目管理-新增修改页面获取审计类型集合
// export function getNbsjTypeListForMerge(params) {
//   return request({
//     url: '/audit/projectManage/getNbsjTypeListForMerge',
//     method: 'get',
//     params: transData(params),
//   })
// }
export function getNbsjTypeListForMerge(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjTypeAllList',
    method: 'get',
    data: transData(params),
  })
}
//项目管理-新增修改获取所选计划的计划项目
export function getPlanProjectListByPlanId(params) {
  return request({
    url: '/audit/auditProject/getPlanProjectListByPlanId',
    method: 'get',
    params: transData(params),
  })
}
//项目管理-删除
export function projectDel(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_del',
    method: 'get',
    params: transData(params),
  })
}
// 分配-选择人员保存
export function manageSave(params) {
  return request({
    url: '/audit/auditProject/xmgl/jsfp_role_manage_save',
    method: 'post',
    data: transData(params),
  })
}
//修改项目经理
export function proPmModi(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_pm_modi',
    method: 'post',
    data: transData(params),
  })
}
//项目小组列表
export function getProjectPjteamList(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_pjteam_list',
    method: 'get',
    params: transData(params),
  })
}
//启动
export function projectStart(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_pj_start',
    method: 'post',
    data: transData(params),
  })
}
//任务分配-左侧树
export function getTreeTask(params) {
  return request({
    url: '/audit/pjData/sjss/getTree',
    method: 'get',
    params: transData(params),
  })
}
//选择人员-人员列表
export function selectPerson(params) {
  return request({
    url: '/setting/baseInfo/getUserList',
    method: 'get',
    params: transData(params),
  })
}
//选择人员-左侧部门
export function findOrganizationByTreeAllss(params) {
  return request({
    url: '/setting/findOrganizationByTreeAllbm',
    method: 'get',
    params: transData(params),
  })
}
//项目管理-新增与修改
export function projectAdd(data) {
  return request({
    url: '/audit/auditProject/xmgl/project_add',
    method: 'post',
    data: transData(data),
  })
}
//项目实施
export function xmProjectPlan(params) {
  return request({
    url: '/audit/auditProject/xmgl/xmproject_plan_cycurr',
    method: 'get',
    params: transData(params),
  })
}

//项目审批
export function submitProjectApproval(params) {
  return request({
    url: '/audit/nbsjapproval/submitProjectApproval',
    method: 'post',
    data: transData(params),
  })
}
//小组信息--新增修改
export function projectPjteamList(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_pjteam_add',
    method: 'post',
    data: transData(params),
  })
}
//通过小组id，查询小组信息
export function proPjteamInfo(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_pjteam_info',
    method: 'get',
    params: transData(params),
  })
}
export function proPjteamDel(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_pjteam_del',
    method: 'get',
    params: transData(params),
  })
}
//项目分配列表
export function getProjectListXmgl(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_role_list_xmgl',
    method: 'get',
    params: transData(params),
  })
}

// 任务分配-列表
export function getProjectRwList(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_rw_list',
    method: 'get',
    params: transData(params),
  })
}
// 查询项目下的所有小组成员
export function getPjteamUsrList(params) {
  return request({
    url: '/audit/auditProject/xmgl/proj_pjteam_usr_list',
    method: 'get',
    params: transData(params),
  })
}
// 查询项目下的所有小组成员
export function getPjteamUsrList2(params) {
  return request({
    url: '/audit/auditProject/xmgl/pjteam_usr_list',
    method: 'get',
    params: transData(params),
  })
}
//项目管理-附件列表
export function projectFileList(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_file_list',
    method: 'get',
    params: transData(params),
  })
}
//项目任务分配列表查询
export function projectTaskAssign(params) {
  return request({
    url: '/audit/audit/projectManage/projectTaskAssign',
    method: 'get',
    params: transData(params),
  })
}

//项目执行一览列表
export function getProjectRunMounth(params) {
  return request({
    url: '/audit/auditProject/sjgl/project_run_month',
    method: 'get',
    params: transData(params),
  })
}

//获取项目计划列表
export function getPlanTableData(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_plan_list',
    method: 'get',
    params: transData(params),
  })
}
//获取项目计划列表
export function getQuestionTypeList(params) {
  return request({
    url: '/audit/auditProject/xmgl/question_type_list',
    method: 'get',
    params: transData(params),
  })
}
//获取项目计划列表
export function saveProjectData(params) {
  return request({
    url: '/audit/auditReady/sjzl/issueProject',
    method: 'get',
    params: transData(params),
  })
}
//项目审批 （直接已完成）
export function submitPjAprWC(params) {
  return request({
    url: '/audit/nbsjapproval/submitPjAprWC',
    method: 'post',
    data: transData(params),
  })
}
//项目管理 - 编码
export function getAutoCodeByXmgl(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByXmgl',
    method: 'get',
    params: transData(params),
  })
}
