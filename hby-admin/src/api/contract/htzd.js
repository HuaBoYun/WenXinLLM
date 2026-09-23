import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//合同制度--列表
export function getInnerRulePageList(data) {
	return request({
		url: '/contract/institu/getInnerRulePageList',
		method: 'post',
		data: transData(data),
	})
}
//合同制度--新增/修改
export function editInnerRule(params) {
	return request({
		url: '/contract/institu/mergeInnerRule',
		method: 'post',
		data: transData(params),
	})
}
//合同制度--新增-自动编号
export function createHTZDCode(params) {
	return request({
		url: '/contract/institu/getAutoCodeByHtzd',
		method: 'post',
		data: transData(params),
	})
}
//合同制度--编辑回填
export function selectInnerRuleInfo(data) {
	return request({
		url: '/contract/institu/selectInnerRuleInfo',
		method: 'post',
		headers: { innerid: data },
	})
}
//合同制度--列表删除
export function deleteInnerRuleInfo(data) {
	return request({
		url: '/contract/institu/deleteInnerRuleInfo',
		method: 'post',
		headers: { innerid: data },
	})
}
//合同制度--列表导出
export function exportInnerRuleInfo(data) {
	return request({
		url: '/contract/institu/expInnerRuleFile',
		method: 'get',
		responseType: 'blob',
		headers: { id: data },
	})
}
//合同制度--附件删除
export function deleteManageFile(data) {
	return request({
		url: '/contract/institu/deleteInnerRuleAtt',
		method: 'get',
		params: transData(data),
	})
}
