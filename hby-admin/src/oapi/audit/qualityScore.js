// 质量评议
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 五项状态

// list
export function qualityScoreList(params) {
  return request({
    url: '/oiaudit/quality/getList',
    method: 'get',
    params: transData(params),
  })
}

// 更新
export function qualityScoreUpdate(params) {
  return request({
    url: '/oiaudit/quality/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

// 据ID获取详情
export function qualityScoreDetails(params) {
  return request({
    url: '/oiaudit/quality/detail',
    method: 'get',
    params: transData(params),
  })
}

// 据ID获取可用评分标准
export function qualityScoreDetail(params) {
  return request({
    url: '/oiaudit/reviewManagement/scoreManage/getAvailableScoreItems',
    method: 'get',
    params: transData(params),
  })
}

// 删除
export function qualityScoreDelete(params) {
  return request({
    url: '/oiaudit/quality/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 质量评议统计表

// list
export function qualityScoreStatisticalMeterList(params) {
  return request({
    url: '/oiaudit/qualityResult/getList',
    method: 'get',
    params: transData(params),
  })
}

// 更新
export function qualityScoreStatisticalMeterUpdate(params) {
  return request({
    url: '/oiaudit/qualityResult/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

// 据ID获取可用评分标准
export function qualityScoreStatisticalMeterDetail(params) {
  return request({
    url: '/oiaudit/qualityResult/detail',
    method: 'get',
    params: transData(params),
  })
}

// 删除
export function qualityScoreStatisticalMeterDelete(params) {
  return request({
    url: '/oiaudit/qualityResult/delete',
    method: 'delete',
    data: transData(params),
  })
}
// 质量评仪新增项目列表
export function getListByProject(params) {
  return request({
    url: '/oiaudit/quality/getListByProject',
    method: 'get',
    params: transData(params),
  })
}
// 质量评仪新增
export function saveOrUpdate(params) {
  return request({
    url: '/oiaudit/qualityResult/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

// 项目积分考评评分列表
export function getProjectEvalList(params) {
  return request({
    url: '/oiaudit/projectEval/getList',
    method: 'get',
    params: transData(params),
  })
}
// 删除
export function projectEvalScoreDelete(params) {
  return request({
    url: '/oiaudit/projectEval/delete',
    method: 'delete',
    data: transData(params),
  })
}
// 详情
export function projectEvalScoreDetail(params) {
  return request({
    url: '/oiaudit/projectEval/detail',
    method: 'get',
    params: transData(params),
  })
}
// 更新
export function projectEvalScoreUpdate(params) {
  return request({
    url: '/oiaudit/projectEval/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}