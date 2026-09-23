import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 专项审计关联列表
export function getLxZxsjList(params) {
	return request({
		url: '/oiaudit/plan/project/evaluation/getLxZxsjList',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}
export function updateAuditScope(params) {
	return request({
		url: '/oiaudit/plan/project/evaluation/updateAuditScope',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}

export function audit2LsaveOrUpdate(params) {
	return request({
		url: '/oiaudit/plan/leave/audit2L/saveOrUpdate',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}
export function getListDraftPlan(params) {
	return request({
		url: '/oiaudit/plan/audit/suggestion2L/getListDraftPlan',
		method: 'get',
		params: transData(params),
	})
}

export function getListForChoose(params) {
	return request({
		url: '/oiaudit/other/audit/getListForChoose',
		method: 'get',
		params: transData(params),
	})
}
export function getRzsjmxListDraftPlan(params) {
	return request({
		url: '/oiaudit/jhgljhcg/getRzsjmxListDraftPlan',
		method: 'get',
		params: transData(params),
	})
}


export function audit2LsaveOrUpdate2(params) {
	return request({
		url: '/oiaudit/plan/audit/suggestion2L/saveOrUpdate',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}


export function getListDraftPlan2(params) {
	return request({
		url: '/oiaudit/jhgljhcg/getSjdwlrsjSbList',
		method: 'get',
		params: transData(params),
	})
}

export function getListDraftPlan2CG(params) {
	return request({
		url: '/oiaudit/jhgljhchug/getSjdwlrsjSbList',
		method: 'get',
		params: transData(params),
	})
}
export function getListDraftPlan2ZG(params) {
	return request({
		url: '/oiaudit/jhgljh/getSjdwlrsjSbList',
		method: 'get',
		params: transData(params),
	})
}
export function audit2LsaveOrUpdate3(params) {
	return request({
		url: '/oiaudit/plan/leave/audit3L/saveOrUpdate',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}

export function getListDraftPlan3(params) {
	return request({
		url: '/oiaudit/jhgljhcg/getGcxmjshzList',
		method: 'get',
		params: transData(params),
	})
}
export function getListDraftPlan3CG(params) {
	return request({
		url: '/oiaudit/jhgljhchug/getGcxmjshzList',
		method: 'get',
		params: transData(params),
	})
}
export function getListDraftPlan3ZG(params) {
	return request({
		url: '/oiaudit/jhgljh/getGcxmjshzList',
		method: 'get',
		params: transData(params),
	})
}
export function audit2LsaveOrUpdate4(params) {
	return request({
		url: '/oiaudit/jsxmjbqk/saveOrUpdate',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}

export function getListDraftPlan4(params) {
	return request({
		url: '/oiaudit/jhgljhcg/getJsxmtzList',
		method: 'get',
		params: transData(params),
	})
}

export function getListDraftPlan4CG(params) {
	return request({
		url: '/oiaudit/jhgljhchug/getJsxmtzList',
		method: 'get',
		params: transData(params),
	})
}

export function getListDraftPlan4ZG(params) {
	return request({
		url: '/oiaudit/jhgljh/getJsxmtzList',
		method: 'get',
		params: transData(params),
	})
}

export function audit2LsaveOrUpdate5(params) {
	return request({
		url: '/oiaudit/gcxmjgysjh/saveOrUpdate',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: transData(params),
	})
}

export function deleteGL(params) {
	return request({
		url: '/oiaudit/jhgljhcg/deleteGL',
		method: 'get',
		params: transData(params),
	})
}


export function deleteGL2(params) {
	return request({
		url: '/oiaudit/jhgljhchug/deleteGL',
		method: 'get',
		params: transData(params),
	})
}

export function deleteGL3(params) {
	return request({
		url: '/oiaudit/jhgljh/deleteGL',
		method: 'get',
		params: transData(params),
	})
}


export function deleteGLByIds(params) {
	return request({
		url: '/oiaudit/jhgljhcg/deleteGLByIds',
		method: 'get',
		params: transData(params),
	})
}
export function deleteGLByIds2(params) {
	return request({
		url: '/oiaudit/jhgljhchug/deleteGLByIds',
		method: 'get',
		params: transData(params),
	})
}

export function deleteGLByIds3(params) {
	return request({
		url: '/oiaudit/jhgljh/deleteGLByIds',
		method: 'get',
		params: transData(params),
	})
}
export function toPreAdd(params) {
	return request({
		url: '/oiaudit/other/audit/toPreAdd',
		method: 'get',
		params: transData(params),
	})
}

// 其他审计详情接口
export function getDetailById(params) {
	return request({
		url: '/oiaudit/other/audit/getDetailById',
		method: 'get',
		params: transData(params),
	})
}

// 其他审计删除接口
export function removById(params) {
	return request({
		url: '/oiaudit/other/audit/removById',
		method: 'get',
		params: transData(params),
	})
}



// 其他审计-附件删除
export function removeAttInfo(params) {
	return request({
		url: '/oiaudit/other/audit/removeAttInfo',
		method: 'get',
		params: transData(params),
	})
}

// 其他审计新增修改保存接口
export function mengerEntity(params) {
	return request({
		url: '/oiaudit/other/audit/mengerEntity',
		method: 'POST',
		data: transData(params),
	})
}

