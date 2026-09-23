// 评议管理
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 添加评分

// 列表
export function addScoreList(params) {
  return request({
    url: '/oiaudit/reviewManagement/scoreManage/getList',
    method: 'get',
    params: transData(params),
  })
}

// 更新
export function addScoreUpdate(params) {
  return request({
    url: '/oiaudit/reviewManagement/scoreManage/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

// 据ID获取可用评分标准
export function addScoreDetail(params) {
  return request({
    url: '/oiaudit/reviewManagement/scoreManage/detail',
    method: 'get',
    params: transData(params),
  })
}

// 删除
export function addScoreDelete(params) {
  return request({
    url: '/oiaudit/reviewManagement/scoreManage/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 状态
export function addScoreStatus(params) {
  return request({
    url: '/oiaudit/reviewManagement/scoreManage/changeStatus',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

// 质量评议表

// 列表
export function qualityScoreMeterList(params) {
  return request({
    url: '/oiaudit/reviewManagement/qualityEvaluate/getList',
    method: 'get',
    params: transData(params),
  })
}

// 更新
export function qualityScoreMeterUpdate(params) {
  return request({
    url: '/oiaudit/reviewManagement/qualityEvaluate/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}

// 据ID获取可用评分标准
export function qualityScoreMeterDetail(params) {
  return request({
    url: '/oiaudit/reviewManagement/qualityEvaluate/detail',
    method: 'get',
    params: transData(params),
  })
}

// 删除
export function qualityScoreMeterDelete(params) {
  return request({
    url: '/oiaudit/reviewManagement/qualityEvaluate/delete',
    method: 'delete',
    data: transData(params),
  })
}

// 状态
export function qualityScoreMeterStatus(params) {
  return request({
    url: '/oiaudit/reviewManagement/qualityEvaluate/changeStatus',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(params),
  })
}
