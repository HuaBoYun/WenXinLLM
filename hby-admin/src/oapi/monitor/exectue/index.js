import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//  下拉按钮
export function MonitorSolution(params) {
  return request({
    url: 'cyber/GuiZeZhiXingController/jkzx/update_monitorSolution',
    method: 'PUT',
    params: transData(params),
  })
}

//  树
export function FindOrganizationByTreeAll(params) {
  return request({
    url: 'monitor/cyber/GuiZeZhiXingController/jkzx/findOrganizationByTreeAll',
    method: 'get',
    params: transData(params),
  })
}

//  列表
export function getRuleExecuteList(params) {
  return request({
    url: 'monitor/cyber/GuiZeZhiXingController/jkzx/solutionmgmt',
    method: 'get',
    params: transData(params),
  })
}
//  列表
export function getTargetExecuteList(params) {
  return request({
    url: 'monitor/cyber/ZhiBiaoZhiXingController/jkjg/kri_zhibiao_info',
    method: 'post',
    params: transData(params),
  })
}

//  模型执行列表
export function getModelssolutionmgmtList(params) {
  return request({
    url: 'monitor/cyber/MxzxController/jkzx/modelssolutionmgmt',
    method: 'get',
    params: transData(params),
  })
}

//  模型执行 - 推送
export function pushSaveUser(params) {
  return request({
    url: 'monitor/cyber/GuiZeZhiXingController/jkzx/saveuser',
    method: 'get',
    params: transData(params),
  })
}

//执行列表-执行粒度下拉框选择 -通过type选择预警方案
export function updateMonitorSolution(params) {
  return request({
    url: 'monitor/cyber/GuiZeZhiXingController/jkzx/update_monitorSolution',
    method: 'put',
    params: transData(params),
  })
}

//
export function getModelssolutionmgm2tList(params) {
  return request({
    url: 'monitor/cyber/MxzxController/jkzx/modelssolutionmgmt2',
    method: 'get',
    params: transData(params),
  })
}

//查看规则预警执行结果
export function resultmgmtyj(params) {
  return request({
    url: 'monitor/cyber/GuiZeZhiXingController/result/resultmgmtyj',
    method: 'get',
    params: transData(params),
  })
}
