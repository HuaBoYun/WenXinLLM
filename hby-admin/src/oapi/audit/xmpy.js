import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//项目评优通知---列表
export const xmpytzList = (params) => request({
	url: '/centralaudit/api-auth/project/notice/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//项目评优通知---详情
export const getXmpytzDetail = (params) => request({
	url: `/centralaudit/api-auth/project/notice/${params.id}`,
	method: 'get',
})
//项目评优通知---删除
export const xmpytzDelete = (params) => request({
	url: `/centralaudit/api-auth/project/notice/${params.id}`,
	method: 'delete',
})
//项目评优通知---新增编辑
export const addOrUpdateXmpytz = (params) => request({
	url: '/centralaudit/api-auth/project/notice/saveOrUpdate',
	method: 'post',
	headers: {
		"Content-Type": "application/json;charset=utf-8"
	},
	data: JSON.stringify(transData(params)),
})
//项目评优申报---导出
export function reportExport(params) {
	return request({
		url: '/centralaudit/api-auth/project/notice/download-express',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
		responseType: 'blob',
	})
}
//项目评优申报---列表
export const xmpysbList = (params) => request({
	url: '/centralaudit/api-auth/project/notice/declare/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
export function exportProject(params) {
	return request({
		url: '/audit/nbsjworkSpace/getGkProjectInfoExport',
		method: 'post',
		data: transData(params),
		responseType: 'blob',
	})
}
//项目评优申报---详情
export const getXmpysbDetail = (params) => request({
	url: `/centralaudit/api-auth/project/notice/declare/${params.id}`,
	method: 'get',
})
//项目评优申报---删除
export const xmpysbDelete = (params) => request({
	url: `/centralaudit/api-auth/project/notice/declare/${params.id}`,
	method: 'delete',
})
//项目评优申报---新增编辑
export const addOrUpdateXmpysb = (params) => request({
	url: '/centralaudit/api-auth/project/notice/declare/saveOrUpdate',
	method: 'post',
	headers: {
		"Content-Type": "application/json;charset=utf-8"
	},
	data: JSON.stringify(transData(params)),
})


//项目评优评优---列表
export const xmpypyList = (params) => request({
	url: '/centralaudit/api-auth/project/notice/appraising/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//项目评优评优---详情
export const getXmpypyDetail = (params) => request({
	url: `/centralaudit/api-auth/project/notice/appraising/${params.id}`,
	method: 'get',
})
//项目评优评优---删除
export const xmpypyDelete = (params) => request({
	url: `/centralaudit/api-auth/project/notice/appraising/${params.id}`,
	method: 'delete',
})
//项目评优评优---新增编辑
export const addOrUpdateXmpypy = (params) => request({
	url: '/centralaudit/api-auth/project/notice/appraising/saveOrUpdate',
	method: 'post',
	headers: {
		"Content-Type": "application/json;charset=utf-8"
	},
	data: JSON.stringify(transData(params)),
})


//项目评优质量---列表
export const xmpyzlList = (params) => request({
	url: '/centralaudit/api-auth/project/notice/quality/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//项目评优质量---详情
export const getXmpyzlDetail = (params) => request({
	url: `/centralaudit/api-auth/project/notice/quality/${params.id}`,
	method: 'get',
})
//项目评优质量---删除
export const xmpyzlDelete = (params) => request({
	url: `/centralaudit/api-auth/project/notice/quality/${params.id}`,
	method: 'delete',
})
//项目评优质量---新增编辑
export const addOrUpdateXmpyzl = (params) => request({
	url: '/centralaudit/api-auth/project/notice/quality/saveOrUpdate',
	method: 'post',
	headers: {
		"Content-Type": "application/json;charset=utf-8"
	},
	data: JSON.stringify(transData(params)),
})

//项目评优项目弹窗---列表
export const projectList = (params) => request({
	url: '/centralaudit/api-auth/project/notice/quality/ext/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//首页通知-列表
export const getHomeList = (params) => request({
	url: '/centralaudit/api-auth/project/notice/ext/home/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//首页通知-同意
export const agree = (params) => request({
	url: `/centralaudit/api-auth/project/notice/ext/home/submit/${params.id}`,
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//首页通知-下发
export const xiafaTZ = (params) => request({
	url: `/centralaudit/api-auth/project/notice/ext/updates/${params[0].projectNoticeId}`,
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})

//首页通知-列表
export const gcjssjxmhzList = (params) => request({
	url: '/oiaudit/oil/audit/project-settlement/cost-intermediate/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})
//首页通知-列表
export const jgjssjxmhzList = (params) => request({
	url: '/oiaudit/oil/audit/project-settlement/completion/getList',
	method: 'post',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8',
	},
	data: JSON.stringify(transData(params)),
})

//项目延期申请- 列表查询
export function getXmyqsqList(params) {
	return request({
		url: '/oiaudit/xmyqsq/getList',
		method: 'get',
		params: transData(params),
	})
}

//项目延期申请- 保存或修改
export function saveXmyqsqOrupdate(params) {
	return request({
		url: '/oiaudit/xmyqsq/saveOrupdate',
		method: 'post',
		data: transData(params),
	})
}

//项目延期申请- 删除
export function deleteXmyqsqone(params) {
	return request({
		url: '/oiaudit/xmyqsq/deleteone',
		method: 'post',
		data: transData(params),
	})
}

//项目延期申请- 查询详情
export function getXmyqsqone(params) {
	return request({
		url: '/oiaudit/xmyqsq/getone',
		method: 'get',
		params: transData(params),
	})
}

//项目延期申请- 查询附件列表
export function getXmyqsqattList(params) {
	return request({
		url: '/oiaudit/xmyqsq/getattList',
		method: 'get',
		params: transData(params),
	})
}

//项目延期申请- 删除附件
export function deleteXmyqsqatt(params) {
	return request({
		url: '/oiaudit/xmyqsq/deleteatt',
		method: 'post',
		data: transData(params),
	})
}

//项目评优汇总- 列表
export function xmpyhzList(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/group/getList`,
		method: 'post',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}

//项目评优汇总- 保存
export function addOrUpdateXmpyhz(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/group/saveOrUpdate`,
		method: 'post',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}

//项目评优汇总- 详情
export function getXmpyhzDetail(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/group/${params.id}`,
		method: 'get',
		params: transData(params),
	})
}

//项目评优汇总- 删除
export function xmpyhzDelete(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/group/${params.id}`,
		method: 'delete',
		data: transData(params),
	})
}

//项目评优排序- 列表
export function xmpypxList(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/sort/getList`,
		method: 'post',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}

//项目评优排序- 保存
export function addOrUpdateXmpypx(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/sort/update`,
		method: 'post',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}


//优秀项目评选审批前置接口判断
export function cloudSP(params) {
	return request({
		url: `/centralaudit/api-auth/project/notice/declare/group/is-approval/${params.id}`,
		method: 'get',
		data: transData(params),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}