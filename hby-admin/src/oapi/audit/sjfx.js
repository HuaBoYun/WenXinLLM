import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//各公司审计项目数
export function entryNumber(params) {
  return request({
    url: '/audit/auditProject/sjfx/ggsxm',
    method: 'get',
    params: transData(params),
  })
}

//审计类型项目
export function projectType(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjlxxm',
    method: 'get',
    params: transData(params),
  })
}

//审计项目情况表
export function projectSituation(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjxmqk',
    method: 'get',
    params: transData(params),
  })
}

//查询条件年度查询
export function yearInquiry(params) {
  return request({
    url: '/audit/auditProject/sjfx/allyear',
    method: 'get',
    params: transData(params),
  })
}
