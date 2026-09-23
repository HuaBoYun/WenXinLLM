import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 模型列表
export function getModelmgmt(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/modelmgmt',
    method: 'get',
    params: transData(params),
  })
}

// 模型结果
export function getModelResult(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/resultmodel',
    method: 'get',
    params: transData(params),
  })
}

// 模型结果信息步骤
export function getModelResultSteps(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/resultmodelinfo',
    method: 'get',
    params: transData(params),
  })
}

// 新增模型
export function createModelManage(data) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/model_add',
    method: 'post',
    data: transData(data),
  })
}

// 修改模型
export function editModelManage(data) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/model_modify',
    method: 'post',
    data: transData(data),
  })
}

// 判断是否有删除权限
export function canRemoveModelManage(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/modelcheckupdate',
    method: 'get',
    params: transData(params),
  })
}

// 删除模型
export function removeModelManage(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/model_del',
    method: 'get',
    params: transData(params),
  })
}

// 修改启动状态
export function changeModelManageStatus(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/modelUpdateStatus',
    method: 'get',
    params: transData(params),
  })
}

// 执行模型
export function executeModelManage(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/model/execute',
    method: 'get',
    params: transData(params),
  })
}

// 获取行业规则库
export function getModelManageIndustry(params) {
  return request({
    url: '/monitor/cyber/MxglController/hy_mx_left',
    method: 'get',
    params: transData(params),
  })
}

// 复制模型库到行业规则库
export function copyModelManageIndustry(params) {
  return request({
    url: '/monitor/cyber/MxglController/to_hy_model_add',
    method: 'get',
    params: transData(params),
  })
}

// 获取创建人信息等
export function getModelAddInfo(params) {
  return request({
    url: '/monitor/cyber/MxglController/mxjk/to_model_add',
    method: 'get',
    params: transData(params),
  })
}

//模型管理 --第三级页面的详细结果
export function resultmodelinfo(params) {
  return request({
    url: `/monitor/cyber/MxglController/mxjk/resultmodelinfo/{index}/{signId}/{modelId}/{source}`,
    method: 'get',
    params: transData(params),
  })
}
