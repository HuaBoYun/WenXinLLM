import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

/**
 * 控制链分析API
 */

// 分页查询控制链分析列表
export function getControlChainList(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 根据ID查询控制链详情
export function getControlChainById(id) {
  return request({
    url: `/monitor/v1/supervision/equity/control-chain/${id}`,
    method: 'get',
  })
}

// 新增控制链分析
export function addControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 更新控制链分析
export function updateControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 删除控制链分析
export function deleteControlChain(id) {
  return request({
    url: `/monitor/v1/supervision/equity/control-chain/${id}`,
    method: 'delete',
  })
}

// 构建控制链图谱
export function buildControlChainMap(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/map',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链统计
export function getControlChainStatistics(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/statistics',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量更新控制链
export function batchUpdateControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/batch/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量删除控制链
export function batchDeleteControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出控制链数据
export function exportControlChainData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// 分析控制路径
export function analyzeControlPaths(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/paths',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 检测控制环路
export function detectControlLoops(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/loops',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 模拟控制变更影响
export function simulateControlChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/simulate',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 优化控制链结构
export function optimizeControlStructure(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/optimize',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}
// 生成控制链分析报告
export function generateControlChainReport(data) {
  return request({
    url: '/monitor/v1/supervision/equity/control-chain/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出控制链（统一导出接口）
export function exportControlChain() {
  return request({ url: '/monitor/v1/supervision/export/equity/controlChain', method: 'get', responseType: 'blob' })
}
