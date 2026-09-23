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
    url: '/oiaudit/auditProject/getNbsjTempleteList',
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
    url: '/oiaudit/nbsjworkSpace/getNbsjTypeListPage',
    method: 'post',
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
// 分配-选择人员保存 其他
export function manageSave(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/jsfp_role_manage_save',
    method: 'post',
    data: transData(params),
  })
}

// 分配-选择人员保存 
export function manageSave2(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/jsfp_gc_save',
    method: 'post',
    data: transData(params),
  })
}

// 分配-选择人员保存 - 类型31单选
export function getGcxmjsListByhzcf(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getGcxmjsListByhzcf',
    method: 'get',
    params: transData(params),
  })
}

// 分配-选择人员保存 - 类型32单选
export function getJsxmtzListByhzcf(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getJsxmtzListByhzcf',
    method: 'get',
    params: transData(params),
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
    url: '/oiaudit/project/implementPlan/proj_pj_start',
    method: 'post',
    data: transData(params),
  })
}
//任务分配-左侧树
export function getTreeTask(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getTree',
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
    url: '/audit/auditProject/htdl/findOrganizationByTreeAllss',
    method: 'post',
    data: transData(params),
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
    url: '/oiaudit/project/implementPlan/getRwfpList',
    method: 'get',
    params: transData(params),
  })
}

// 任务分配-列表
export function getProjectRwList(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/check_list_my_all',
    method: 'get',
    params: transData(params),
  })
}
// 查询项目下的所有小组成员
export function getPjteamUsrList(params) {
  return request({
    url: '/oiaudit/project/implementPlan/getteams',
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

// 项目管理---审计项目表--删除
export function xmglsjxmbDelete(data) {
  return request({
    url: '/oiaudit/xmglsjxmb/delete',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审计项目表---单个详情
export function xmglsjxmbDetail(data) {
  return request({
    url: '/oiaudit/xmglsjxmb/detail',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审计项目表--获取工作记录列表
export function xmglsjxmbList(data) {
  return request({
    url: '/oiaudit/xmglsjxmb/list',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审计项目表--新增/更新
export function xmglsjxmbSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/xmglsjxmb/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---审计项目表--下发
export function xmglsjxmbXf(params) {
  return request({
    url: '/oiaudit/xmglsjxmb/xf',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---审前调查报告--删除
export function xmglsqdcbgDelete(data) {
  return request({
    url: '/oiaudit/xmglsqdcbg/delete',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审前调查报告---单个详情
export function xmglsqdcbgDetail(data) {
  return request({
    url: '/oiaudit/xmglsqdcbg/detail',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审前调查报告--获取工作记录列表
export function xmglsqdcbgList(data) {
  return request({
    url: '/oiaudit/xmglsqdcbg/list',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审前调查报告--新增/更新
export function xmglsqdcbgSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/xmglsqdcbg/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---工作方案--删除
export function xmglgzfaDelete(data) {
  return request({
    url: '/oiaudit/xmglgzfa/delete',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---工作方案---单个详情
export function xmglgzfaDetail(data) {
  return request({
    url: '/oiaudit/xmglgzfa/detail',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---工作方案--获取工作记录列表
export function xmglgzfaList(data) {
  return request({
    url: '/oiaudit/xmglgzfa/list',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---工作方案--新增/更新
export function xmglgzfaSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/xmglgzfa/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---工作方案--下发
export function xmglgzfaXf(params) {
  return request({
    url: '/oiaudit/xmglgzfa/xf',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---审计项目制度--删除
export function xmglsjxmzdDelete(data) {
  return request({
    url: '/oiaudit/xmglsjxmzd/delete',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审计项目制度---单个详情
export function xmglsjxmzdDetail(data) {
  return request({
    url: '/oiaudit/xmglsjxmzd/detail',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审计项目制度--获取工作记录列表
export function xmglsjxmzdList(data) {
  return request({
    url: '/oiaudit/xmglsjxmzd/list',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---审计项目制度--新增/更新
export function xmglsjxmzdSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/xmglsjxmzd/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---审计项目制度--下发
export function xmglsjxmzdXf(params) {
  return request({
    url: '/oiaudit/xmglsjxmzd/xf',
    method: 'post',
    params: transData(params),
  })
}

// 项目管理---实施方案--删除
export function implementPlanDelete(data) {
  return request({
    url: '/oiaudit/project/implementPlan/delete',
    method: 'delete',
    params: transData(data),
  })
}

// 项目管理---实施方案---单个详情
export function implementPlanDetail(data) {
  return request({
    url: '/oiaudit/project/implementPlan/detail',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---实施方案--获取工作记录列表
export function implementPlanList(data) {
  return request({
    url: '/oiaudit/project/implementPlan/getList',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---实施方案--获取新建项目
export function implementPlanProject(data) {
  return request({
    url: '/oiaudit/project/implementPlan/curr_ss_project',
    method: 'get',
    params: transData(data),
  })
}

// 审计报告定稿选择审计项目接口
export function sjbgdgGetImplPlanList(data) {
  return request({
    url: '/oiaudit/sjbgdg/getImplPlanList',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理---实施方案--新增/更新
export function implementPlanSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/project/implementPlan/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

// 项目管理---实施方案--项目实施
export function implementPlanCycurr(data) {
  return request({
    url: '/oiaudit/project/implementPlan/xmproject_plan_cycurr',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理-分工
export function fpzyksry(data) {
  return request({
    url: '/oiaudit/project/implementPlan/fpzyksry',
    method: 'post',
    data: transData(data),
  })
}

// 项目管理启动-删除
export function xmglqdDelete(data) {
  return request({
    url: '/oiaudit/xmqd/deleteone',
    method: 'post',
    data: transData(data),
  })
}

// 项目管理启动---单个详情
export function xmglqdDetail(data) {
  return request({
    url: '/oiaudit/xmqd/getone',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理启动--获取工作记录列表
export function xmglqdList(data) {
  return request({
    url: '/oiaudit/xmqd/getList',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理启动--获取工作记录列表
export function xmglqdNewList(data) {
  return request({
    url: '/oiaudit/xmqd/getqdList',
    method: 'get',
    params: transData(data),
  })
}

// 项目管理启动--新增/更新
export function xmglqdSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/xmqd/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}
// 项目管理启动--新增/更新
export function xmglqddeleteAtt(params) {
  return request({
    url: '/oiaudit/xmqd/deleteatt',
    method: 'post',
    data: transData(params),
  })
}

// 项目管理启动--查询附件
export function xmglqGetFile(data) {
  return request({
    url: `/oiaudit/xmqd/getattList`,
    method: 'get',
    params: transData(data),
  })
}

// 项目管理启动--启动
export function xmglqdqd(params) {
  return request({
    url: '/oiaudit/xmqd/qdproject',
    method: 'post',
    data: transData(params),
  })
}
// 项目管理启动--终止
export function xmglqdStop(params) {
  return request({
    url: '/oiaudit/xmqd/tzproject',
    method: 'post',
    data: transData(params),
  })
}

export function planingList(params) {
  return request({
    url: '/oiaudit/project/implementPlan/getList',
    method: 'get',
    params: transData(params),
  })
}


//我的任务新左侧树
export function getNewTree(params) {
  return request({
    url: '/oiaudit/project/implementPlan/myWork/getTree',
    method: 'get',
    params: transData(params),
  })
}
//我的任务旧左侧树
export function myWorkGetTreeOld(params) {
  return request({
    url: '/audit/pjData/myWork/getTree',
    method: 'get',
    params: transData(params),
  })
}
//我的任务新左侧树
export function myWorkGetTree(params) {
  return request({
    url: '/oiaudit/pjData/myWork/getTree',
    method: 'get',
    params: transData(params),
  })
}
//我的任务清单列表(new)
export function myTaskList(params) {
  return request({
    url: '/oiaudit/project/implementPlan/sjss/check_list_my',
    method: 'get',
    params: transData(params),
  })
}
//计划初稿-三级离任弹窗
export function getSJmodalList(params) {
  return request({
    url: '/oiaudit/jhgljhcg/getSjdwlrsjSbListByhz',
    method: 'get',
    params: transData(params),
  })
}
//计划初稿-三级离任弹窗
export function getSjdwlrsjSbListByhz(params) {
  return request({
    url: '/oiaudit/jhgljhchug/getSjdwlrsjSbListByhz',
    method: 'get',
    params: transData(params),
  })
}

//计划初稿-三级离任弹窗
export function getSjdwlrsjSbListByzg(params) {
  return request({
    url: '/oiaudit/jhgljh/getSjdwlrsjSbListByhz',
    method: 'get',
    params: transData(params),
  })
}

//计划初稿-工程项目结算弹窗
export function getGCJSmodalList(params) {
  return request({
    url: '/oiaudit/jhgljhcg/getGcxmjsListByhz',
    method: 'get',
    params: transData(params),
  })
}

//计划初稿-工程项目结算弹窗
export function getGcxmjsListByhz(params) {
  return request({
    url: '/oiaudit/jhgljhchug/getGcxmjsListByhz',
    method: 'get',
    params: transData(params),
  })
}

//计划初稿-工程项目结算弹窗
export function getGcxmjsListByzg(params) {
  return request({
    url: '/oiaudit/jhgljh/getGcxmjsListByhz',
    method: 'get',
    params: transData(params),
  })
}

//计划初稿-建设项目投资弹窗
export function getJSTZmodalList(params) {
  return request({
    url: '/oiaudit/jhgljhcg/getJsxmtzListByhz',
    method: 'get',
    params: transData(params),
  })
}
//计划初稿-建设项目投资弹窗
export function getJsxmtzListByhz(params) {
  return request({
    url: '/oiaudit/jhgljhchug/getJsxmtzListByhz',
    method: 'get',
    params: transData(params),
  })
}

//计划初稿-建设项目投资弹窗
export function getJsxmtzListByzg(params) {
  return request({
    url: '/oiaudit/jhgljh/getJsxmtzListByhz',
    method: 'get',
    params: transData(params),
  })
}

//获取当前实施项目
export function getCurrSsProject(params) {
  return request({
    url: '/oiaudit/project/implementPlan/curr_ss_project',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-工程项目结算汇总查看列表数据 31
export function getgcmyrwList(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getgcmyrwList',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-建设项目投资完成情况 32
export function getJsxmmyrwlist(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getJsxmmyrwlist',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-工程项目结算汇总查看列表数据 31
export function getgcmyrwListnew(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getGcxmjsListByhz',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-建设项目投资完成情况 32
export function getJsxmmyrwlistnew(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getJsxmtzListByhz',
    method: 'get',
    params: transData(params),
  })
}

//实施方案--财务项目列表
export function cwxzfindList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/fundAuditProject/xzfindList',
    method: 'get',
    params: transData(params),
  })
}

//实施方案--工程项目列表
export function gcxzfindList(params) {
  return request({
    url: '/oiaudit/audit/planArrange/enginAuditProject/xzfindList',
    method: 'get',
    params: transData(params),
  })
}
//实施方案--选择审计模板判断
export function projectyzmb(params) {
  return request({
    url: '/oiaudit/project/implementPlan/project/yzmb',
    method: 'get',
    params: transData(params),
  })
}
