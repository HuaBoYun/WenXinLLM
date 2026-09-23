import request from '@/utils/request'

export function getDataList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/getDataList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getDataListNoPage(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/getDataListNoPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getDataById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/getDataById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function addData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/addData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function updateData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/updateData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function deleteData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/deleteData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function batchDeleteData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/batchDeleteData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function submitData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/submitData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function withdrawData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/withdrawData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function approveData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/approveData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function batchSubmitData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetData/batchSubmitData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 别名方法，用于兼容不同的调用方式
export function getBudgetDataList(data) {
  return getDataList(data)
}
