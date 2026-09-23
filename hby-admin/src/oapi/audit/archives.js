import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 获取详细
export function getTjspBorrowDetail(params) {
	return request({
		url: '/audit/auditArchive/jhgl/tjspBorrowDetail',
		method: 'post',
		params: transData(params),
	})
}

//获取档案列表
export function getArchivesList(params) {
	return request({
		url: '/audit/auditArchive/sjgd/sjgd_newlist',
		method: 'get',
		params: transData(params),
	})
}

//获取档案借阅列表
export function getDajyNewlist(params) {
	return request({
		url: '/audit/auditArchive/sjgd/dajy_newlist',
		method: 'get',
		params: transData(params),
	})
}
//档案借阅-申请借阅
export function TjspBorrow(params) {
	return request({
		url: '/audit/auditArchive/jhgl/tjspBorrow',
		method: 'post',
		data: transData(params),
	})
}
//获取借阅日志列表
export function getJyrzNewlist(params) {
	return request({
		url: '/audit/auditArchive/sjgd/jyrz_newlist',
		method: 'get',
		params: transData(params),
	})
}
//获取借阅次数详情 /auditArchive/sjgd/jyrz_countlist

export function getJyrz_countlist(params) {
	return request({
		url: '/audit/auditArchive/sjgd/jyrz_countlist',
		method: 'get',
		params: transData(params),
	})
}

// 保存档案借阅
export function SubmitRecordApproval(params) {
	return request({
		url: '/audit/nbsjapproval/submitRecordApproval',
		method: 'post',
		data: transData(params),
	})
}

// 我的待办，档案借阅
export function GetRecordApprovalInfo(params) {
	return request({
		url: '/audit/nbsjapproval/getRecordApprovalInfo',
		method: 'get',
		params: transData(params),
	})
}

// 档案借阅通过或驳回
export function DealRecordApporvalInfo(params) {
	return request({
		url: '/audit/nbsjapproval/dealRecordApporvalInfo',
		method: 'post',
		data: transData(params),
	})
}



// 档案列表新接口
export function getDaList(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/da/daList',
		method: 'get',
		params: transData(params),
	})
}


// 档案借阅列表新接口
export function getDajyList(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/da/dajyList',
		method: 'get',
		params: transData(params),
	})
}
// 档案借阅明细新接口
export function getDajyDetail(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/da/dajyDetail',
		method: 'post',
		data: transData(params),
	})
}
// 档案借阅申请借阅新接口
export function saveDadajySaveOrUpdate(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/da/dajySaveOrUpdate',
		method: 'post',
		data: transData(params),
	})
}


// 借阅日志
export function getJyrzList(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/da/jyrzList',
		method: 'get',
		params: transData(params),
	})
}
// 借阅日志次数查看
export function getJyrzxqList(params) {
	return request({
		url: '/oiaudit/xmglsjxmb/da/jyrzxqList',
		method: 'get',
		params: transData(params),
	})
}

