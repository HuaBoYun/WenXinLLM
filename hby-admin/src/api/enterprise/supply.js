/**
 * 供应链管理API接口
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

/**
 * 获取供应链列表
 */
export function getSupplyList(data) {
  return request({
    url: '/monitor/v1/enterprise/supply/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

/**
 * 根据ID获取供应链详情
 */
export function getSupplyById(id) {
  return request({
    url: `/monitor/v1/enterprise/supply/${id}`,
    method: 'get'
  })
}

/**
 * 新增供应链记录
 */
export function addSupply(data) {
  return request({
    url: '/monitor/v1/enterprise/supply/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

/**
 * 更新供应链记录
 */
export function updateSupply(data) {
  return request({
    url: '/monitor/v1/enterprise/supply/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

/**
 * 删除供应链记录
 */
export function deleteSupply(id) {
  return request({
    url: `/monitor/v1/enterprise/supply/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除供应链记录
 */
export function batchDeleteSupply(data) {
  return request({
    url: '/monitor/v1/enterprise/supply/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS
  })
}

/**
 * 导出供应链数据
 */
export function exportSupply() {
  return request({
    url: '/monitor/v1/enterprise/supply/export',
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 获取供应链统计数据
 */
export function getSupplyStatistics() {
  return request({
    url: '/monitor/v1/enterprise/supply/statistics',
    method: 'get'
  })
}
