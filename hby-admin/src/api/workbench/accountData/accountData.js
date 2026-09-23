import { transData } from '@/utils/requestData'
import request from '@/utils/request'
//前缀/zbgl
//科目表-分页查询
export function getSubjectList(params) {
	return request({
		url: '/zbgl/accSubject/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//总分类账 - 列表分页查询
export function getAccountCate(params) {
	return request({
		url: '/zbgl/accSumTotal/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//凭证库 - 列表分页查询
export function getVoucherLibList(params) {
	return request({
		url: '/zbgl/accBkpf/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//凭证库 - 记账凭证
export function bookkeepingDetail(params) {
	return request({
		url: '/zbgl/accBkpf/bookkeepingDetail',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//日记账 - 列表分页查询
export function getAccountDiaryList(params) {
	return request({
		url: '/zbgl/diaryBook/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//明细账 - 列表分页查询
export function getAccountDetailList(params) {
	return request({
		url: '/zbgl/deatiledBook/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//明细账-记账凭证
export function getAccountingVoucher(params) {
	return request({
		url: '/zbgl/accbseg/jzpz',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//明细分类账 - 分页查询??
export function getAccountDetailList2(params) {
	return request({
		url: '/zbgl/accbseg/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//余额表 - 分页查询
export function getBalanceList(params) {
	return request({
		url: '/zbgl/accSum/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//辅助账 - 列表分页查询
export function getAccountAssistVoucher(params) {
	return request({
		url: '/zbgl/auxiliaryBook/list',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//辅助账 - 列表分页查询
export function getAccountAssistTreeData(params) {
	return request({
		url: '/zbgl/auxiliaryBook/assTypeList',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//下载发送附件
export function getAccountFileList(params) {
	return request({
		// url: '/zbgl/auxiliaryBook/export/generateFile',
		url: '/zbgl/export/generateFile',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}
//财务数仓-下载发送附件
export function getFinancialFileList(params) {
	return request({
		// url: '/zbgl/auxiliaryBook/export/generateFile',
		url: '/finance/export/generateFile',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
	})
}

//财务数仓-导出Excel文件
export function exportFinancialFile(params) {
	return request({
		url: '/finance/export/generateFile',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		data: JSON.stringify(transData(params)),
		responseType: 'blob',
	})
}
