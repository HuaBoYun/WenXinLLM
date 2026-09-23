import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 指标监控-指标管理-列表
export function getWatchIndexList(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/kri_info',
    method: 'get',
    params: transData(params),
  })
}

//指标管理-删除
export function kriInfoDel(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/kri_info_del',
    method: 'get',
    params: transData(params),
  })
}

//指标管理-启用和禁用修改指标状态
export function kriInfoStatus(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/kri_info_status',
    method: 'get',
    params: transData(params),
  })
}

//指标管理-查看
export function KriInfoDisp(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/kri_info_disp',
    method: 'get',
    params: transData(params),
  })
}

//指标管理-添加
export function KriInfoAdd(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/kri_info_add',
    method: 'get',
    params: transData(params),
  })
}

//行业指标库、指标管理,互相复制
export function copyIndex(params) {
  return request({
    url: '/monitor/cyber/ZbglController/index/copyIndex',
    method: 'get',
    params: transData(params),
  })
}

//行业指标库---main页面
export function KriInfoIndex(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/info_index',
    method: 'get',
    params: transData(params),
  })
}

// 指标预警-列表
export function getErrorIndexList(params) {
  return request({
    url: '/monitor/cyber/ZbyjController/zbjk/kri_monitor_info',
    method: 'get',
    params: transData(params),
  })
}

// 指标预警-详情
export function getErrorIndexDetail(params) {
  return request({
    url: '/monitor/cyber/ZbyjController/zbjk/to_solution_disp',
    method: 'get',
    params: transData(params),
  })
}

// 指标预警-添加指标
export function getErrorIndexAdd(params) {
  return request({
    url: '/monitor/cyber/ZbyjController/zbjk/ruleslistSelector',
    method: 'get',
    params: transData(params),
  })
}

// 指标预警-删除
export function SolutionDel(params) {
  return request({
    url: '/monitor/cyber/ZbyjController/zbjk/solution_del',
    method: 'get',
    params: transData(params),
  })
}

//指标管理 --结果
export function kriInfoResult(params) {
  return request({
    url: '/monitor/cyber/ZbglController/zbjk/kri_info_result',
    method: 'get',
    params: transData(params),
  })
}

// add project
export function SolutionSave(params) {
  return request({
    url: '/monitor/cyber/ZbyjController/zbjk/solution_save',
    method: 'get',
    params: transData(params),
  })
}

// add to ...
export function ToSolutionAdd(params) {
  return request({
    url: '/monitor/cyber/ZbyjController/zbjk/to_solution_add',
    method: 'get',
    params: transData(params),
  })
}
