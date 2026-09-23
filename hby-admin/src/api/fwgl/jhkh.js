import request from '@/utils/request'
import { transData } from '@/utils/requestData'
export const uploadApi = '/fwgl/api-auth/fileManage/upload'

// 规划管理-列表-查询
export function getGHGLList(params) {
	return request({
		url: '/fwgl/api-auth/plan/examine/plan/management/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//规划管理-新增/更新
export function addGHGL(params) {
	return request({
		url: '/fwgl/api-auth/plan/examine/plan/management/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 规划管理-详情
export function getGHGLDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/plan/management/${params.id}`,
		method: 'get',
	})
}

// 规划管理-列表-删除
export function deleteGHGLList(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/plan/management/${params.id}`,
		method: 'delete',
	})
}

// 年度计划-列表-查询
export function getNDJHList(params) {
	return request({
		url: '/fwgl/api-auth/plan/examine/annual/plan/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//年度计划-新增/更新
export function addNDJH(params) {
	return request({
		url: '/fwgl/api-auth/plan/examine/annual/plan/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 年度计划-详情
export function getNDJHDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/plan/${params.id}`,
		method: 'get',
	})
}

// 年度计划-列表-删除
export function deleteNDJHList(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/plan/${params.id}`,
		method: 'delete',
	})
}

// 年度考核-列表-查询
export function getNDKHList(params) {
	return request({
		url: '/fwgl/api-auth/plan/examine/annual/examine/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//年度考核-新增/更新
export function addNDKH(params) {
	return request({
		url: '/fwgl/api-auth/plan/examine/annual/examine/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 年度考核-详情
export function getNDKHDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/${params.id}`,
		method: 'get',
	})
}

// 年度考核-列表-删除
export function deleteNDKHList(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/${params.id}`,
		method: 'delete',
	})
}

// 年度考核-附件-删除
export function deleteFile(params) {
	return request({
		url: `/fwgl/api-auth/fileManage/${params.id}`,
		method: 'delete',
	})
}

// 考核评分表-描述查询
export function getExamineQuestionList(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/topic/getList`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 考核评分表-分数列表查询
export function getExamineAnswerList(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/score/getList`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 考核评分表-分数保存
export function asveExamineScore(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/score/saveOrUpdate`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 规划管理-导出
export function exportGHGL(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/plan/management/download-express`,
		method: 'get',
		params,
		responseType: 'blob',
	})
}

// 年度计划-导出
export function exportNDJH(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/plan/download-express`,
		method: 'get',
		params,
		responseType: 'blob',
	})
}

// 年度考核-导出
export function exportNDKH(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/download-express`,
		method: 'get',
		params,
		responseType: 'blob',
	})
}

// 附件上传
export function getScoreFileData(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/score/ext/${params.id}`,
		method: 'get',
	})
}
// 附件弹框确定
export function addNDJHFile(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/annual/examine/score/ext/saveOrUpdate`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

export function getKHTZList(params) {
	return request({
		url: `/fwgl/api-auth/plan/examine/examine/get-all`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}