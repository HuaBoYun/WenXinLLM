import request from '@/utils/request'

export function getFormList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/getFormList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getFormListNoPage(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/getFormListNoPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getFormById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/getFormById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function addForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/addForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function updateForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/updateForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function deleteForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/deleteForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function batchDeleteForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/batchDeleteForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function copyForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/copyForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function enableForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/enableForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function disableForm(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetForm/disableForm',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

