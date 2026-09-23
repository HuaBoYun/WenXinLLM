import request from '@/utils/request'
import { transData } from '@/utils/requestData'
//任务管理列表
export function getProjectData(params) {
	return request({
		url: '/audit/pjData/sjss/check_list_my_all',
		method: 'get',
		params: transData(params),
	})
}
//获取附件列表
export function getfjlistDownload(params) {
	return request({
		url: '/audit/pjData/sjgl/dgfjlist_download',
		method: 'get',
		params: transData(params),
	})
}
//项目归档列表
export function getProjectArchiveList(params) {
	return request({
		url: '/audit/pjData/sjss/project_archive_list',
		method: 'get',
		params: transData(params),
	})
}
// 项目查看 树形结构
export function getPreijectTreeData(params) {
	return request({
		url: '/audit/pjData/xmzl/getLiftMenu',
		method: 'get',
		params: transData(params),
	})
}
//获取任务管理左侧树状结构
export function getTreeDataById(params) {
	return request({
		url: '/audit/pjData/sjss/getTree',
		method: 'get',
		params: transData(params),
	})
}
//项目查看
export function getProjectDetails(params) {
	return request({
		url: '/audit/pjData/sjgl/project_look',
		method: 'get',
		params: transData(params),
	})
}
// 项目归档保存

export function savePjData(params) {
	return request({
		url: '/audit/pjData/xmgd/save',
		method: 'post',
		data: transData(params),
	})
}
//查询归档项目数量
export function getDefaultSaveProjectNums(params) {
	return request({
		url: '/audit/pjData/sjss/dg_listall',
		method: 'get',
		params: transData(params),
	})
}

// 项目查看 树形结构
export function getLiftMenu(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/xmzl/getLiftMenu',
		method: 'get',
		params: transData(params),
	})
}
// 项目查看 树形结构
export function currSsProject(params) {
	return request({
		url: '/oiaudit/project/implementPlan/curr_ss_project',
		method: 'get',
		params: transData(params),
	})
}
// 项目归档查询
export function projectArchiveList(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/projectArchiveList',
		method: 'get',
		params: transData(params),
	})
}