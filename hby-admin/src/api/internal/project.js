import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价立项-主页查询
export function getProjectList(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/proj_initiate',
		method: 'get',
		params: transData(params),
	})
}
//评价立项删除
export function deleteProject(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/proj_delete',
		method: 'post',
		data: transData(params),
	})
}
//评价立项新增评价负责人弹框
export function userList(params) {
	return request({
		url: '/nkhg/nbkz/pjlx/list',
		method: 'get',
		params: transData(params),
	})
}
//评价立项新增评价模板弹框
export function templateList(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/proj_tmplLocation',
		method: 'get',
		params: transData(params),
	})
}
//评价立项新增
export function projectAdd(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/add',
		method: 'post',
		data: transData(params),
	})
}
//评价立项修改回填接口
export function getProjectDefaultInfo(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/proj_modify',
		method: 'get',
		params: transData(params),
	})
}
//评价立项修改保存接口
export function projectUpdate(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/proj_update',
		method: 'post',
		data: transData(params),
	})
}
//评价立项列表查看评价模板
export function getTemplateModalTableDate(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/proj_diap',
		method: 'get',
		params: transData(params),
	})
}
//评价立项列表点击授权按钮弹出列表
export function getAuthorizationModalData(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/t08_proj_org',
		method: 'get',
		params: transData(params),
	})
}
//评价立项授权弹框点击授权按钮弹出列表
export function getAuthorization2ModalData(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/t08_proj_modify',
		method: 'get',
		params: transData(params),
	})
}
//评价立项授权弹框参评人弹框列表
export function ZZUserList(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/setting_user_list',
		method: 'get',
		params: transData(params),
	})
}
//评价立项授权弹框主评人保存
export function saveZPRInfo(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/savezupingUsers',
		method: 'post',
		data: transData(params),
	})
}
//评价立项授权弹框参评人保存
export function saveCPRInfo(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/savecanpingUsers',
		method: 'post',
		data: transData(params),
	})
}
//评价立项授权弹框参评人评分列表
export function ccScoreList(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/t08_proj_assignor',
		method: 'get',
		params: transData(params),
	})
}
//评价立项授权弹框参评人评分弹框保存
export function saveCCScoreInfo(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/SaveAssignor',
		method: 'post',
		data: transData(params),
	})
}
//评价立项自动编号
export function createProjectCode(params) {
	return request({
		url: '/nkhg/nbkz/code/findAutoNumber',
		method: 'get',
		params: transData(params),
	})
}
//评价立项启动
export function startProject(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/start',
		method: 'post',
		data: transData(params),
	})
}
// 评价立项-评价人适用性修改
export function updateShiYongXing(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/updateShiYongXing',
		method: 'get',
		params: transData(params),
	})
}

// 业务编号自动创建 通用方法
export const createBusinessNo = (params) => {
	return request({
		url: '/nkhg/nbkz/code/findAutoNumberByChoice',
		method: 'get',
		params: transData(params),
	})
}

// 风险编号自动创建 riskcontrol/nbkz/code/findNumberLevelNexidByParent
export const createLevelByParent = (params) => {
	return request({
		url: '/riskcontrol/nbkz/code/findNumberLevelNexidByParent',
		method: 'get',
		params: transData(params),
	})
}
//评价立项-参评人管理列表-删除参评人
export const deletecanpingUsers = (params) => {
	return request({
		url: `/nkhg/nbkz/pjgl/deletecanpingUsers?z=${params}`,
		method: 'delete',
	})
}
//评价立项-主评人/参评人-左侧部门树
export const leftTree = (params) => {
	return request({
		url: '/nkhg/nbkz/pjgl/tree',
		method: 'get',
		params: transData(params),
	})
}

//项目信息字段编辑页变活接口
export function getDefaultRenderData(params) {
	return request({
		url: `/setting/customize/preview/details`,
		method: 'post',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}
//项目信息字段变活接口
export function getDefaultListRenderData(params) {
	return request({
		url: `/setting/customize/preview/list`,
		method: 'post',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}
export function createProjectCodeNew(params) {
	return request({
		url: '/riskcontrol/riskEvent/get_risksj_no',
		method: 'get',
		params: transData(params),
	})
}

//附件删除接口，不删除中间表关系
export function deleteFile(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/del_fj',
		method: 'post',
		data: transData(params),
	})
}

// 评价立项删除
// /pjgl/plan/delete/{id}
export function deleteProject_new(params) {
	return request({
		url: `/nkhg/nbkz/pjgl/plan/delete/${params.id}`,
		method: 'delete',
		params: transData(params),
	})
}

//附件下载接口
export function download(params) {
	return request({
		url: '/nkhg/filec/download',
		method: 'get',
		responseType: 'blob',
		params: transData(params),
	})
}
//附件预览
export function getPrivewAttInfo(params) {
	return request({
		url: '/setting/filePreview/getPrivewAttInfo',
		method: 'get',
		params: transData(params),
	})
}

//评价计划-主页查询
export function getEvaluationPlanList(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/plan_initiate',
		method: 'get',
		params: transData(params),
	})
}

//评价计划修改回填接口
export function getPlanDefaultInfo(params) {
	return request({
		url: '/nkhg/nbkz/pjgl/plan_detail',
		method: 'get',
		params: transData(params),
	})
}

//评价计划新增
export function planAdd(params) {
	const { content, ...rest } = params
	return request({
		url: '/nkhg/nbkz/pjgl/plan_add',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		params: transData(rest),
		data: transData({ content }),
	})
}

//评价计划修改保存接口
export function planUpdate(params) {
	const { content, ...rest } = params
	return request({
		url: '/nkhg/nbkz/pjgl/plan_update',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		params: transData(rest),
		data: transData({ content }),
	})
}

//评价缺陷
//评价缺陷-列表
export function getHomepageList(params) {
	return request({
		url: '/nkhg/pjqx/getHomepage_List',
		method: 'get',
		params: transData(params),
	})
}
//评价缺陷-详情
export function getDetails(params) {
	return request({
		url: '/nkhg/pjqx/getDetails',
		method: 'get',
		params: transData(params),
	})
}
//评价缺陷-新增修改
export function saveOrUpdate(params) {
	return request({
		url: '/nkhg/pjqx/saveOrUpdate',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		method: 'post',
		data: transData(params),
	})
}
//评价缺陷-删除
export function deleteQx(params) {
	return request({
		url: '/nkhg/pjqx/deleteQx',
		method: 'post',
		data: transData(params),
	})
}
//评价缺陷-导出
export function exportPjqx(params) {
	return request({
		url: '/nkhg/nbkz/pjqx/exportPjqx',
		method: 'post',
		data: transData(params),
		responseType: 'blob',
	})
}


//评价访谈
//评价访谈-列表
export function getPjftHomepageList(params) {
	return request({
		url: '/nkhg/pjft/getHomepage_List',
		method: 'get',
		params: transData(params),
	})
}
//评价访谈-详情
export function getPjftDetails(params) {
	return request({
		url: '/nkhg/pjft/getDetails',
		method: 'get',
		params: transData(params),
	})
}
//评价访谈-新增修改
export function savePjftOrUpdate(params) {
	return request({
		url: '/nkhg/pjft/saveOrUpdate',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		method: 'post',
		data: transData(params),
	})
}
//评价访谈-删除
export function deletePjftQx(params) {
	return request({
		url: '/nkhg/pjft/deleteQx',
		method: 'post',
		data: transData(params),
	})
}
//评价访谈附件-删除
export function deleteFTfile(params) {
	return request({
		url: '/nkhg/pjft/del_fj',
		method: 'post',
		data: transData(params),
	})
}
//测试模板详情
export function getTmplDetail(params) {
	return request({
		url: '/nkhg/nbkz/csmb/def_tmpl_detail',
		method: 'get',
		params: transData(params),
	})
}
