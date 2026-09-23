import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险监测指标创建列表
export function getMajorRiskCreateList(data) {
	return request({
		url: '/riskcontrol/Monitoring/getMajorRiskCreateList',
		method: 'post',
		data: transData(data),
	})
}

// 删除风险监测指标
export function deleteMajorRiskCreate(data) {
	return request({
		url: '/riskcontrol/Monitoring/delete',
		method: 'post',
		data: transData(data),
	})
}

// 获取风险监测指标详情
export function getMajorRiskCreateDetails(data) {
	return request({
		url: '/riskcontrol/Monitoring/details',
		method: 'post',
		data: transData(data),
	})
}

// 风险监测指标创建下发
export function getMajorRiskCreateSend(data) {
	return request({
		url: '/riskcontrol/Monitoring/monitoringIssued',
		method: 'post',
		data: transData(data),
	})
}

// 风险监测指标创建新增/修改
export function saveOrUpdateMajorRiskCreate(data) {
	return request({
		url: '/riskcontrol/Monitoring/saveOrUpdate',
		method: 'post',
		data: transData(data),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}

// 部门模板创建
export function getRiskMonDeptList() {
	return request({
		url: '/riskcontrol/Monitoring/getRiskMonDeptList',
		method: 'post',
	})
}
// 部门模板保存
export function saveMonDictonaryDept(data) {
	return request({
		url: '/riskcontrol/Monitoring/saveMonDictonaryDept',
		method: 'post',
		data: transData(data),
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
	})
}
//部门列表
export function getMonDictonaryDeptList(data) {
	return request({
		url: '/riskcontrol/Monitoring/getMonDictonaryDeptList',
		method: 'post',
		data: transData(data),
	})
}
//部门子列表
export function getOrgTree(data) {
	return request({
		url: '/riskcontrol/Monitoring/getOrgTree',
		method: 'get',
		params: transData(data),
	})
}

//风险监测指标创建下发人员
export function monitoringIssuedNew(data) {
	return request({
		url: '/riskcontrol/Monitoring/monitoringIssuedNew',
		method: 'post',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		params: { ids: data.ids },
		data: transData(data.data),
	})
}

// 获取下发人员列表
export function getIssuedList(data) {
	return request({
		url: '/riskcontrol/Monitoring/getIssuedList',
		method: 'post',
		params: transData(data),
	})
}

