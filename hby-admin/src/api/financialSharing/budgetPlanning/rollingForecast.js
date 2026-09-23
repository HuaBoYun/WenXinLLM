import request from '@/utils/request'

/**
 * 滚动预测管理API
 */

// 创建预测任务
export function createForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/createForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 修改预测任务
export function updateForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/updateForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询预测任务列表(分页)
export function getForecastList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/getForecastList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询预测任务
export function getForecastById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/getForecastById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询预测任务(包含数据明细)
export function getForecastWithData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/getForecastWithData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除预测任务
export function deleteForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/deleteForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量删除预测任务
export function batchDeleteForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/batchDeleteForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 提交预测任务
export function submitForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/submitForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 审批预测任务
export function approveForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/approveForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 撤销预测任务
export function withdrawForecast(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/withdrawForecast',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询预测任务统计信息
export function getForecastStatistics() {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/getForecastStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 生成预测数据
export function generateForecastData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/rollingForecast/generateForecastData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

