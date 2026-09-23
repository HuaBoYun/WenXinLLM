// import request from '@/utils/request'

import router from '../../mock/controller/router'
import routerSetting from '../../mock/controller/routerSetting'
import routerAudit from '../../mock/controller/routerAudit'
import routerContract from '../../mock/controller/routerContract'
import routerRisk from '../../mock/controller/routerRisk'
import routerInternal from '../../mock/controller/routerInternal'
import routerMonitor from '../../mock/controller/routerMonitor'

// export function getRouterList(params) {
//   return request({
//     url: '/router/getList',
//     method: 'get',
//     params,
//   })
// }

export function getRouterList() {
  return new Promise((resolve) => {
    resolve(router[0].response())
  })
}

// export function getRouterListForSetting(params) {
//   return request({
//     url: '/routerSetting/getListForSetting',
//     method: 'get',
//     params,
//   })
// }

export function getRouterListForSetting() {
  return new Promise((resolve) => {
    resolve(routerSetting[0].response())
  })
}

// export function getRouterListForAudit(params) {
//   return request({
//     url: '/routerAudit/getListForAudit',
//     method: 'get',
//     params,
//   })
// }

export function getRouterListForAudit() {
  return new Promise((resolve) => {
    resolve(routerAudit[0].response())
  })
}

// export function getRouterListForContract(params) {
//   return request({
//     url: '/routerContract/getListForContract',
//     method: 'get',
//     params,
//   })
// }

export function getRouterListForContract() {
  return new Promise((resolve) => {
    resolve(routerContract[0].response())
  })
}

// export function getRouterListForMonitor(params) {
//   return request({
//     url: '/routerMonitor/getListForMonitor',
//     method: 'get',
//     params,
//   })
// }

export function getRouterListForMonitor() {
  return new Promise((resolve) => {
    resolve(routerMonitor[0].response())
  })
}

// export function getRouterListForRisk(params) {
//   return request({
//     url: '/routerRisk/getListForRisk',
//     method: 'get',
//     params,
//   })
// }

export function getRouterListForRisk() {
  return new Promise((resolve) => {
    resolve(routerRisk[0].response())
  })
}

// export function getRouterListForInternal(params) {
//   return request({
//     url: '/routerInternal/getListForInternal',
//     method: 'get',
//     params,
//   })
// }

export function getRouterListForInternal() {
  return new Promise((resolve) => {
    resolve(routerInternal[0].response())
  })
}
