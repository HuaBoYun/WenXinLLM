/*
 * @Author: 康某 dev@example.com
 * @Date: 2022-08-27 15:12:08
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-27 15:29:00
 * @FilePath: \hb-admin\src\api\common.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

export function getOssConfig(query) {
	return request({
		url: '/common/oss/getConfigInfo',
		method: 'get',
		params: query,
	})
}

export function sendSms(query) {
	return request({
		url: '/common/msg/subMailCode',
		method: 'get',
		params: query,
	})
}

export function getAllProject(query) {
	return request({
		url: '/admin/project/allList',
		method: 'get',
		params: query,
	})
}

// 查看公司底下部门

export const getOrgTreeDataByCompany = (params) => {
	return request({
		url: '/setting/baseInfo/findOrganizationByTreeAllbm',
		method: 'post',
		data: params,
	})
}

// 查询公司底下得公司
export const getOrgTreeByCompany = (params) => {
	return request({
		url: '/setting/redisorg/findOrganizationByJTTreeAllGS',
		method: 'post',
		data: params,
	})
}
// 查询公司底下的部门
export const getOrgTreeByDepartment = (params) => {
	return request({
		url: '/setting/findOrganizationByTreeAllbm',
		method: 'post',
		data: params,
	})
}

// 企业数据-实时分析——跳转
export const toSsfx = (params) => {
	return request({
		url: '/setting/bi/fr/cjfx',
		method: 'post',
		data: params,
	})
}

