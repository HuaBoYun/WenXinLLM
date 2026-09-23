import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
	return request({
		url: '/oiaudit/gzhf/list',
		method: 'get',
		params: transData(params),
	})
}

// 撤回
export function withdraw(data) {
	return request({
		url: '/oiaudit/gzhf/withdraw',
		method: 'post',
		data: transData(data),
		headers: typeBbj,
	})
}

// 完成
export function complete(data) {
	return request({
		url: '/oiaudit/gzhf/complete',
		method: 'post',
		data: transData(data),
	})
}
// 分派
export function assignment(data) {
	return request({
		url: '/oiaudit/gzhf/assignment',
		method: 'post',
		data: transData(data),
		headers: typeBbj,
	})
}

// 新增、修改数据
export function editInfo(data) {
	return request({
		url: '/oiaudit/gzhf/saveOrUpdate',
		method: 'post',
		data: transData(data),
		headers: typeBbj,
	})
}

// 查询当前选中列表数据
export function getDetailInfo(params) {
	return request({
		url: '/oiaudit/gzhf/detail',
		method: 'get',
		params: transData(params),
	})
}
// 删除附件
export function deleteFileInfo(params) {
	return request({
		url: '/oiaudit/gzhf/deleteAttach',
		method: 'get',
		params: transData(params),
	})
}
