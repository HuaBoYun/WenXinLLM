import request from '@/utils/request'

export function getRuleList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/getRuleList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getRuleListNoPage(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/getRuleListNoPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function getRuleById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/getRuleById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function addRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/addRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function updateRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/updateRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function deleteRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/deleteRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function batchDeleteRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/batchDeleteRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function copyRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/copyRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function enableRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/enableRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

export function disableRule(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetRule/disableRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

