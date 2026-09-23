import request from '@/utils/request'
import { transData } from '@/utils/requestData'
//附件下载接口
export function download(params) {
	return request({
		url: '/audit/fileManage/download',
		method: 'get',
		responseType: 'blob',
		params: transData(params),
	})
}

//附件删除接口，不删除中间表关系
export function deleteFile(params) {
	return request({
		url: '/audit/fileManage/delete',
		method: 'get',
		params: transData(params),
	})
}
//预览
export function getPrivewAttInfo(params) {
	return request({
		url: '/setting/filePreview/getPrivewAttInfo',
		method: 'get',
		params: transData(params),
	})
}

//合规E刊-列表
export function getComplianceWeenlyRead(params) {
	return request({
		url: '/contract/com/complianceWeenlyRead',
		method: 'get',
		params: transData(params),
	})
}
//合规管理-列表
export function getComplianceWeenlyList(params) {
	return request({
		url: '/audit/nbsjworkSpace/complianceWeenlyList',
		method: 'get',
		params: transData(params),
	})
}

// 合规E刊-新增-保存
export function complianceWeenlySave(params) {
	return request({
		url: '/audit/nbsjworkSpace/complianceWeenlySave',
		method: 'post',
		data: transData(params),
	})
}
// 合规E刊-删除
export function complianceWeenlyDel(params) {
	return request({
		url: '/audit/nbsjworkSpace/complianceWeenlyDel',
		method: 'get',
		params: transData(params),
	})
}
// 合规E刊-详情
export function getComplianceWeenlyDetail(params) {
	return request({
		url: '/audit/nbsjworkSpace/complianceWeenlyDetail',
		method: 'get',
		params: transData(params),
	})
}
