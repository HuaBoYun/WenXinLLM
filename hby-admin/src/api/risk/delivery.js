import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 年度合同分析
export function contractAnalysis(params) {
  return request({
    url: '/contract/contract/contractAnalysis',
    method: 'get',
    params: transData(params),
  })
}
// 各部门合同收款汇总表 
export function ContractCollection(params) {
  return request({
    url: '/contract/contract/ContractCollection',
    method: 'get',
    params: transData(params),
  })
}
// 各部门交付合同计划 
export function orgContractPlan(params) {
  return request({
    url: '/contract/contract/orgContractPlan',
    method: 'get',
    params: transData(params),
  })
}
// 季度合同交付明细表
export function orgPayContract(params) {
  return request({
    url: '/contract/contract/orgPayContract',
    method: 'get',
    params: transData(params),
  })
}


// 合同分析动态报表查询
export function dynamicReport(params) {
  return request({
    url: '/contract/contract/dynamicReport',
    method: 'get',
    params: transData(params),
  })
}