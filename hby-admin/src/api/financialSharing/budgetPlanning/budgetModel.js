import request from '@/utils/request'

export function getModelList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/getModelList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getModelListNoPage(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/getModelListNoPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getModelById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/getModelById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function addModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/addModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function updateModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/updateModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function deleteModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/deleteModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function batchDeleteModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/batchDeleteModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function copyModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/copyModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function enableModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/enableModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function disableModel(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetModel/disableModel',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

