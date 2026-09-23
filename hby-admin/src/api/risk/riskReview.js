import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//风险审查列表
export const riskReviewList = (params) => {
	return request({
		url: '/riskcontrol/review/riskReviewList',
		method: 'post',
		params: transData(params),
	})
}

//风险审查修改
export const updateRiskReview = (params) => {
	return request({
		url: '/riskcontrol/review/updateRiskReview',
		method: 'post',
		data: transData(params),
	})
}

//风险审查新增
export const insertRiskReview = (params) => {
	return request({
		url: '/riskcontrol/review/insertRiskReview',
		method: 'post',
		data: transData(params),
	})
}

//风险审查详情
export const riskReviewDetails = (params) => {
	return request({
		url: '/riskcontrol/review/riskReviewDetails',
		method: 'post',
		params: transData(params),
	})
}

//风险审查删除
export const riskReviewDelete = (params) => {
	return request({
		url: '/riskcontrol/review/riskReviewDelete',
		method: 'post',
		params: transData(params),
	})
}

//风险审查意见修改
export const insertRiskReviewOpinion = (params) => {
	return request({
		url: '/riskcontrol/review/insertRiskReviewOpinion',
		method: 'post',
		params: transData(params),
	})
}
//风险审查意见详情
export const riskReviewOpinionDetails = (params) => {
	return request({
		url: '/riskcontrol/review/riskReviewOpinionDetails',
		method: 'post',
		params: transData(params),
	})
}

//风险审查导出
export const exportRiskReviewList = (params) => {
	return request({
		url: '/riskcontrol/review/exportRiskReviewList',
		method: 'post',
		responseType: 'blob',
		headers: {
			'Content-Type': 'application/json;charset=UTF-8',
		},
		params,
	})
}
//风险审查意见列表
export const riskReviewOpinionList = (params) => {
	return request({
		url: '/riskcontrol/review/riskReviewOpinionList',
		method: 'post',
		params: transData(params),
	})
}