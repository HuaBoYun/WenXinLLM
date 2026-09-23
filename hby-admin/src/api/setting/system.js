/*
 * @Date: 2022-02-25 14:22:56
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-19 16:18:16
 * @FilePath: /hb-admin/src/api/setting/system.js
 */
import request from '@/utils/request'
import qs from 'qs'

/**
 * @description: 系统配置-业务创建新增
 * @param {*} data
 * @return {*}
 */
export function addBusinessTree(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/add_proces_save',
    method: 'post',
    data,
  })
}
// 流程设计列表 复制
export const copyWorkFlowFormInfo = (data) => {
  return request({
    url: '/setting/ymWrok/copyFlowInfo',
    method: 'post',
    data,
  })
}

// 下发
export function saveDistribution(data) {
  return request({
    url: '/setting/distribution/saveDistribution',
    method: 'post',
    data,
  })
}
/**
 * @description: 系统配置-业务创建修改
 * @param {*} data
 * @return {*}
 */
export function updateBusinessTree(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/process_update',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建修改获取详情
 * @param {*} data
 * @return {*}
 */
export function getBusinessTreeDetail(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/process_modify',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建启用弃用
 * @param {*} data
 * @return {*}
 */
export function switchFiringStatus(data) {
  return request({
    url: '/setting/ywlc/changeFiringStatus',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建删除
 * @param {*} data
 * @return {*}
 */
export function deleteBusinessTree(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/del_process',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单获取流程分类选项
 * @param {*} data
 * @return {*}
 */
export function getFlowTypeOptions(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/lcfl',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单获取负责人右侧列表
 * @param {*} data
 * @return {*}
 */
export function getExecutorOptionsList(data) {
  return request({
    url: '/setting/nbkz/user/list',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单保存
 * @param {*} data
 * @return {*}
 */
export function saveBusinessProcess(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/process_analysis_add_save',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单修改获取详情
 * @param {*} data
 * @return {*}
 */
export function getBusinessProcessDetail(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/analysis_list_add_modify',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里内外规列表
 * @param {*} data
 * @return {*}
 */
export function getInternalExternalList(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/ioanalysis',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里内规选择列表
 * @param {*} data
 * @return {*}
 */
export function getInternalChooseList(data) {
  return request({
    url: '/setting/common/inner_common',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里内规新增
 * @param {*} data
 * @return {*}
 */
export function addInternal(data) {
  return request({
    url: '/setting/save_flow_inner',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里内规删除
 * @param {*} data
 * @return {*}
 */
export function deleteInternal(data) {
  return request({
    url: '/setting/delete_flow_inner',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里外规选择列表
 * @param {*} data
 * @return {*}
 */
export function getExternalChooseList(data) {
  return request({
    url: '/setting/common/common_outer',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里外规新增
 * @param {*} data
 * @return {*}
 */
export function addExternal(data) {
  return request({
    url: '/setting/save_flow_outer',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-业务创建-右侧表单里外规删除
 * @param {*} data
 * @return {*}
 */
export function deleteExternal(data) {
  return request({
    url: '/setting/delete_flow_outer',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}
/**
 * @description: 系统配置-业务流程设置-左侧菜单
 * @param {*} data
 * @return {*}
 */
export function getBusinessTree(data) {
  return request({
    url: '/setting/from/processAnalysis/leftanalysis',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务流程设置-右侧列表
 * @param {*} data
 * @return {*}
 */
export function getBusinessList(data) {
  return request({
    url: '/setting/from/processAnalysis/listanalysis',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务流程设置-复制到行业列表
 * @param {*} data
 * @return {*}
 */
export function copyToIndustry(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/to_leftanalysishy',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务流程设置-复制到行业列表保存
 * @param {*} data
 * @return {*}
 */
export function saveCopyToIndustry(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/to_ywlcbefore',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务流程设置-从行业复制列表
 * @param {*} data
 * @return {*}
 */
export function copyFromIndustry(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/listanalysishy_copy',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务流程设置-从行业复制保存
 * @param {*} data
 * @return {*}
 */
export function saveCopyFromIndustry(data) {
  return request({
    url: '/setting/ywlc/processAnalysis/to_ywlc_copybefore',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务模块保存
 * @param {*} data
 * @return {*}
 */
export function businessTreeItemSave(data) {
  return request({
    url: '/setting/flowmodule/saveFlowModule',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义列表
 * @param {*} data
 * @return {*}
 */
export function getFlowList(data) {
  return request({
    url: '/setting/process/setting',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义添加
 * @param {*} data
 * @return {*}
 */
export function saveFlow(data) {
  return request({
    url: '/setting/processSave',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义删除
 * @param {*} data
 * @return {*}
 */
export function deleteFlow(data) {
  return request({
    url: '/setting/processdelete',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义-选择所属部门
 * @param {*} data
 * @return {*}
 */
export function getTreeLevel(data) {
  return request({
    url: '/setting/findOrganizationByTreeNbkz',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-列表
 * @param {*} data
 * @return {*}
 */
export function getFlowListForView(data) {
  return request({
    url: '/setting/definition_list',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-添加流程
 * @param {*} data
 * @return {*}
 */
export function saveFlowForView(data) {
  return request({
    url: '/setting/adddeploymentzip',
    method: 'post',
    data,
    headers: { 'content-type': 'multipart/form-data' },
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-删除流程
 * @param {*} data
 * @return {*}
 */
export function deleteFlowForView(data) {
  return request({
    url: '/setting/definition_delete',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-流程实例列表
 * @param {*} data
 * @return {*}
 */
export function getViewInstanceList(data) {
  return request({
    url: '/setting/processInstance',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-流程实例列表-跟踪图片
 * @param {*} data
 * @return {*}
 */
export function getViewInstanceDiagram(data) {
  return request({
    url: '/setting/process/picture',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-历史流程列表
 * @param {*} data
 * @return {*}
 */
export function getViewHistoryList(data) {
  return request({
    url: '/setting/historyProcess',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程定义-查看流程定义-历史流程删除
 * @param {*} data
 * @return {*}
 */
export function deleteViewHistory(data) {
  return request({
    url: '/setting/history_delete',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-合同流程设置列表
 * @param {*} data
 * @return {*}d
 */
export function getContractSettingList(data) {
  return request({
    url: '/setting/contract/typeOfContractList',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-合同关联流程
 * @param {*} data
 * @return {*}
 */
export function getContractAssociateWithFlow(data) {
  return request({
    url: '/setting/process/htgl/setting',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-合同关联流程保存
 * @param {*} data
 * @return {*}
 */
export function saveAssociateContract(data) {
  return request({
    url: '/setting/htgl/saveflow',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-业务关联流程保存
 * @param {*} data
 * @return {*}
 */
export function saveAssociateBusiness(data) {
  return request({
    url: '/setting/ywlc/saveflow',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程版本管理左侧tree
 * @param {*} data
 * @return {*}
 */
export function getFlowManageTree(data) {
  return request({
    url: '/setting/ywlc/version_left',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程版本管理列表
 * @param {*} data
 * @return {*}
 */
export function getFlowManageList(data) {
  return request({
    url: '/setting/nbkz/ywlc/list_version',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-流程版本管理-历史版本列表
 * @param {*} data
 * @return {*}
 */
export function getFlowManageListHistory(data) {
  return request({
    url: '/setting/ywlc/list_single_version',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-编号设置-列表
 * @param {*} data
 * @return {*}
 */
export function getSnSettingList(data) {
  return request({
    url: '/setting/number_list',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-编号设置-左侧tree
 * @param {*} data
 * @return {*}
 */
export function getSnSettingTree(data) {
  return request({
    url: '/setting/numberTree',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-编号设置-修改
 * @param {*} data
 * @return {*}
 */
export function updateSnSetting(data) {
  return request({
    url: '/setting/updateNumber',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-列表
 * @param {*}
 * @return {*}
 */
export function getModelList(data) {
  return request({
    url: '/setting/flowModuleList',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 系统配置-模块创建-选择流程-创建左侧菜单
 * @param {*} data
 * @return {*}
 */
export function selectBusinessTree(data) {
  return request({
    url: '/setting/flowModule/leftanalysis',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-下发选择tree
 * @param {*} data
 * @return {*}
 */
export function getIssueModuleTree(data) {
  return request({
    url: '/setting/flowModule/gettreeorg',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-下发
 * @param {*} data
 * @return {*}
 */
export function issueModule(data) {
  return request({
    url: '/setting/flowmodule/saveFlowOrganizationRelation',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-取消下发选择列表
 * @param {*} data
 * @return {*}
 */
export function getCancelIssueModuleList(data) {
  return request({
    url: '/setting/flowModule/cancelOrganizationList',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-取消下发
 * @param {*} data
 * @return {*}
 */
export function cancelIssueModule(data) {
  return request({
    url: '/setting/flowModule/removeOrgModuleRelation',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-启用、弃用
 * @param {*} data
 * @return {*}
 */
export function updateModuleStatus(data) {
  return request({
    url: '/setting/flowmodule/modifyFlowModuleStatus',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-新增
 * @param {*} data
 * @return {*}
 */
export function addModule(data) {
  return request({
    url: '/setting/flowmodule/saveFlowModule',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-模块创建-修改
 * @param {*} data
 * @return {*}
 */
export function updateModule(data) {
  return request({
    url: '/setting/flowmodule/modifyFlowModule',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-模块创建-创建、修改时选择流程
 * @param {*} data
 * @return {*}
 */
export function getFlows(data) {
  return request({
    url: '/setting/flowModule/listanalysis',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-修改时删除流程
 * @param {*} data
 * @return {*}
 */
export function deleteFlowOfModule(data) {
  return request({
    url: '/setting/flowmodule/removeModelFlowRelation',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 系统配置-模块创建-前往新增？
 * @param {*} data
 * @return {*}
 */
export function moduleAdd(data) {
  return request({
    url: '/setting/gotoFlowModule/insert',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-模块创建-前往修改？
 * @param {*} data
 * @return {*}
 */
export function moduleUpdate(data) {
  return request({
    url: '/setting/gotoFlowModule/goToModify',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-菜单设置-系统权限tree
 * @param {*} data
 * @return {*}
 */
export function getMenuSettingTree(data) {
  return request({
    url: '/setting/qxsd/mgnsqlist',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-菜单设置-当前页面列表
 * @param {*} data
 * @return {*}
 */
export function getMenuSettingList(data) {
  return request({
    url: '/setting/pri_list',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-菜单设置-保存
 * @param {*} data
 * @return {*}
 */
export function saveMenuSetting(data) {
  return request({
    url: '/setting/module_save',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-菜单设置-删除
 * @param {*} data
 * @return {*}
 */
export function deleteMenuSetting(data) {
  return request({
    url: '/setting/module_del',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-菜单设置-跳转到修改页面？
 * @param {*} data
 * @return {*}
 */
// export function menuSettingUpdate(data) {
//   return request({
//     url: '/setting/module_modify',
//     method: 'post',
//     data: transData(data),
//   })
// }

/**
 * @description: 系统配置-菜单设置-启用、弃用
 * @param {*} data
 * @return {*}
 */
export function updateMenuSettingStatus(data) {
  return request({
    url: '/setting/module_xgstatus',
    method: 'post',
    data,
  })
}

/**
 * @description: 系统配置-数据初始化-列表
 * @param {*} data
 * @return {*}
 */
export function getDataInitialList(data) {
  return request({
    url: '/setting/org/inihy_list',
    method: 'post',
    data,
  })
}

// 工作流设计、审批列表-获取所有工作流信息
export function getAllFlowList(data) {
  return request({
    url: '/setting/ymWrok/getWorFlowList',
    method: 'get',
    params: data,
  })
}
// 工作流设计、审批列表-获取所有工作流信息保存
export function saveAllNewFlowList(data) {
  return request({
    url: '/setting/ymWrok/startYmWorkFlow',
    method: 'post',
    data: data,
  })
}

//tableId and id 查详细
export const getFlowPkInfo = (data) => {
  return request({
    url: '/setting/ymWrok/getFlowPkInfo',
    method: 'get',
    params: data,
  })
}

//删除
export const removeWorkFlowFormInfo = (data) => {
  return request({
    url: '/setting/ymWrok/removeWorkFlowFormInfo',
    method: 'post',
    data,
  })
}

//删除
export const removeWorkFlowContractType = (data) => {
  return request({
    url: '/setting/ymWrok/removeWorkFlowContractType',
    method: 'post',
    data,
  })
}

// 我的待办、我发起的-提交审批
export const submitByYmWork = (data) => {
  return request({
    url: '/setting/ymWrok/submit',
    method: 'get',
    params: data,
  })
}

//提交审批 - 选择流程
export const candidates = (data) => {
  return request({
    url: '/setting/ymWrok/candidates',
    method: 'get',
    params: data,
  })
}
// 单点登陆  jnfd 表单设计
export const SSOToJNFD = (data) => {
  return request({
    url: '/setting/yinMai/singleSign',
    method: 'post',
    data,
  })
}

export const copyContractTypeFlow = (data) => {
  return request({
    url: '/setting/ymWrok/copyContractTypeFlow',
    method: 'post',
    data,
  })
}
export const startContractTypeFlow = (data) => {
  return request({
    url: '/setting/ymWrok/startContractTypeFlow',
    method: 'post',
    data,
  })
}

export const getContractTypeFlowList = (data) => {
  return request({
    url: '/setting/ymWrok/getContractTypeFlowList',
    method: 'get',
    params: data,
  })
}

// 流程设计列表
export const getJNFDList = (data) => {
  return request({
    url: '/setting/ymWrok/getSystemFlowList',
    method: 'get',
    params: data,
  })
}

// 流程设计 枚举
export const getLCSJData = (data) => {
  return request({
    url: '/setting/ymWrok/getSystemFlowType',
    method: 'get',
    params: data,
  })
}

//流程 候选人列表
export const getCandidatesList = (data) => {
  return request({
    url: '/setting/ymWrok/candidateUser',
    method: 'get',
    params: data,
  })
}

//新增模块
export const addModuleInfo = (data) => {
  return request({
    url: '/setting/project/module/saveOrUpdate',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}
//编辑模块详情
export const getDefaultModuleInfo = (data) => {
  return request({
    url: `/setting/project/module/${data.id}`,
    method: 'get',
    params: data,
  })
}
//模块列表
export const getModuleList = (data) => {
  return request({
    url: '/setting/project/module/getList',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}
//删除模块列表
export const deleteModule = (data) => {
  return request({
    url: `/setting/project/module/${data.id}`,
    method: 'DELETE',
    params: data,
  })
}

export const isSuper = (data) => {
  return request({
    url: `/setting/user/isAdmin`,
    method: 'get',
    params: data,
  })
}

export const saveOrgModuleInfo = (data) => {
  return request({
    url: '/setting/project/auth/saveOrUpdate',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}
export const getDefaultRelateCompany = (data) => {
  return request({
    url: '/setting/project/auth/getList',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

export const isAdmin = (data) => {
  return request({
    url: `/setting/user/is-authorization-personnel`,
    method: 'get',
    params: data,
  })
}
export const saveHomeModuleInfo = (data) => {
  return request({
    url: `/setting/home/page/auth/saveOrUpdate`,
    method: 'POST',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export const getDefaultHomeRelateCompany = (data) => {
  return request({
    url: '/setting/home/page/auth/getList',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}
//流程分配-根据流程ID获取所有公司信息
export const getCheckedCompanyList = (data) => {
  return request({
    url: '/setting/ymWrok/getCheckedCompanyList',
    method: 'get',
    params: data,
  })
}


// 保存流程分配
export const saveOrgWorkFlowInfo = (data) => {
  return request({
    url: '/setting/ymWrok/saveOrgWorkFlowInfo',
    method: 'post',
    data,
  })
}

// 删除已分配流程的组织信息
export const delOrgWorkFlowInfo = (data) => {
  return request({
    url: '/setting/ymWrok/delOrgWorkFlowInfo',
    method: 'post',
    data,
  })
}

//流程分配-获取所有公司信息
export const getCompanyList = (data) => {
  return request({
    url: '/setting/ymWrok/getCompanyList',
    method: 'get',
    params: data,
  })
}

// 流程审批附件下载
export const getLCfile = (data) => {
  return request({
    url: '/setting/ymWrok/fildDownloadZH',
    method: 'get',
    params: data,
    responseType: 'blob',
  })
}