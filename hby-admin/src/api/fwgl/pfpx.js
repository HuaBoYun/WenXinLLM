import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 普法计划-列表-查询
export function getPFJHList(params) {
	return request({
		url: '/fwgl/api-auth/law/train/popularize/law/plan/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//普法计划-新增/更新
export function addPFJH(params) {
	return request({
		url: '/fwgl/api-auth/law/train/popularize/law/plan/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 普法计划-详情
export function getPFJHDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/law/train/popularize/law/plan/${params.id}`,
		method: 'get',
	})
}

// 普法计划-列表-删除
export function deletePFJHList(params) {
	return request({
		url: `/fwgl/api-auth/law/train/popularize/law/plan/${params.id}`,
		method: 'delete',
	})
}

// 活动管理-列表-查询
export function getHDGLList(params) {
	return request({
		url: '/fwgl/api-auth/law/train/activity/management/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//活动管理-新增/更新
export function addHDGL(params) {
	return request({
		url: '/fwgl/api-auth/law/train/activity/management/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 活动管理-详情
export function getHDGLDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/law/train/activity/management/${params.id}`,
		method: 'get',
	})
}

// 活动管理-列表-删除
export function deleteHDGLList(params) {
	return request({
		url: `/fwgl/api-auth/law/train/activity/management/${params.id}`,
		method: 'delete',
	})
}

// 课题管理-列表-查询
export function getKTGLList(params) {
	return request({
		url: '/fwgl/api-auth/law/train/subject/management/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//课题管理-新增/更新
export function addKTGL(params) {
	return request({
		url: '/fwgl/api-auth/law/train/subject/management/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 课题管理-详情
export function getKTGLDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/law/train/subject/management/${params.id}`,
		method: 'get',
	})
}

// 课题管理-列表-删除
export function deleteKTGLList(params) {
	return request({
		url: `/fwgl/api-auth/law/train/subject/management/${params.id}`,
		method: 'delete',
	})
}

// 学法考试-列表-查询
export function getXFKSList(params) {
	return request({
		url: '/fwgl/api-auth/law/train/xf/exam/getList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//学法考试-新增/更新
export function addXFKS(params) {
	return request({
		url: '/fwgl/api-auth/law/train/xf/exam/saveOrUpdate',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

// 学法考试-详情
export function getXFKSDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/law/train/xf/exam/${params.id}`,
		method: 'get',
	})
}

// 学法考试-列表-删除
export function deleteXFKSList(params) {
	return request({
		url: `/fwgl/api-auth/law/train/xf/exam/${params.id}`,
		method: 'delete',
	})
}

// 活动管理-导出
export function exportHDGL(params) {
	return request({
		url: `/fwgl/api-auth/law/train/activity/management/download-express`,
		method: 'get',
		params,
		responseType: 'blob',
	})
}

// 普法计划-导出
export function exportPFJH(params) {
	return request({
		url: `/fwgl/api-auth/law/train/popularize/law/download-express`,
		method: 'get',
		params,
		responseType: 'blob',
	})
}

// 课题管理-导出
export function exportKTGL(params) {
	return request({
		url: `/fwgl/api-auth/law/train/subject/management/download-express`,
		method: 'get',
		params,
		responseType: 'blob',
	})
}

// 问答知识库
//访客接口
//新增留言
export function addMsg(params) {
	return request({
		url: '/fwgl/api/guest/bbs',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//根据条件查询返回留言分页列表
export function queryMsg(params) {
	return request({
		url: '/fwgl/api/guest/bbs/page',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//管理留言接口
//回复
export function replyMsg(params) {
	return request({
		url: '/fwgl/api/admin/bbs',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//批量删除留言
export function deleteMsg(params) {
	return request({
		url: '/fwgl/api/admin/bbs',
		method: 'delete',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//根据id获取留言信息
export function getMsgById(params) {
	return request({
		url: `/fwgl/api/admin/bbs/${params.id}`,
		method: 'get',
		params,
	})
}


export function getBaseData(params) {
	return request({
		url: `/fwgl/api/guest/bbs/reply/getList/${params.id}`,
		method: 'post',
		params,
	})
}

export function getLDXFList(params) {
	return request({
		url: `/fwgl/api-auth/law/train/leader/xf/getList`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

export function updateZXZXXData(params) {
	return request({
		url: `/fwgl/api-auth/law/train/leader/xf/saveOrUpdate`,
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//根据id获取留言信息
export function getLDXFDefaultInfo(params) {
	return request({
		url: `/fwgl/api-auth/law/train/leader/xf/${params.id}`,
		method: 'get',
		params,
	})
}

//根据id获取留言信息
export function deleteLDXFList(params) {
	return request({
		url: `/fwgl/api-auth/law/train/leader/xf/${params.id}`,
		method: 'delete',
		params,
	})
}

//活动管理-消息推送
export function pushHDGLInfo(params) {
	return request({
		url: `/fwgl/api-auth/law/train/sendMetting/${params.id}`,
		method: 'post',
		data: params,
	})
}