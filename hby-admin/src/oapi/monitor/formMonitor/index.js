import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//  列表
export function getBussinessList(params) {
  return request({
    url: 'monitor/cyber/ywbdjkController/rulecontrol/rule_list',
    method: 'get',
    params: transData(params),
  })
}

//  修改业务表单监控状态
export function changeBussinessStatus(params) {
  return request({
    url: 'monitor/cyber/ywbdjkController/rulecontrol/modify_ruleStatus',
    method: 'get',
    params: transData(params),
  })
}
//  删除业务表单监控
export function deleteBussiness(params) {
  return request({
    url: 'monitor/cyber/ywbdjkController/rulecontrol/remove_fromRule',
    method: 'get',
    params: transData(params),
  })
}
//  保存规则
export function saveFromRule(data) {
  return request({
    url: 'monitor/cyber/ywbdjkController/rulecontrol/save_fromRule',
    method: 'post',
    data: transData(data),
  })
}

// 自定义表单 表单列表页面 查询所有在业务流程中的表单
export function findFormInfoListInFlow(params) {
  return request({
    url: 'monitor/cyber/ywbdjkController/rulecontrol/findFormInfoListInFlow',
    method: 'get',
    params: transData(params),
  })
}

//自定义表单 表单列表页面
export function findFormReportListInfo(params) {
  return request({
    url: 'monitor/cyber/ywbdjkController/rulecontrol/findFormReportListInfo',
    method: 'get',
    params: transData(params),
  })
}
