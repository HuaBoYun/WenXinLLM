import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 获取模型预警列表
export function getModelRemindList(params) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modelsolutionmgmt',
    method: 'get',
    params: transData(params),
  })
}

// 获取预警结果
export function getModelRemindResult(params) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/resultmodelinfo',
    method: 'get',
    params: transData(params),
  })
}

// 执行预警
export function executeModelRemind(params) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modelsolution_execute',
    method: 'get',
    params: transData(params),
  })
}

// 删除预警
export function removeModelRemind(params) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modelsolution_del',
    method: 'get',
    params: transData(params),
  })
}

// 修改启动状态
export function changeModelRemindStatus(params) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modelsolutionUpdateStatus',
    method: 'get',
    params: transData(params),
  })
}

// 新增
export function createModelRemind(data) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modelsolution_add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;',
    },
    data: transData(data),
  })
}

// 修改
export function editModelRemind(data) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modelsolution_modify',
    method: 'post',
    data: transData(data),
  })
}

// 获取模型预警详情
export function getModelRemindDetail(params) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/to_modelsolution_modify',
    method: 'get',
    params: transData(params),
  })
}

// 获取模型列表
export function getModelList(data) {
  return request({
    url: '/monitor/cyber/MxyjController/mxjk/modellistSelector',
    method: 'post',
    data: transData(data),
  })
}
// 添加模型列表
// 删除模型列表
