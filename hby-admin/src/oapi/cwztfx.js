import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getYHData(params) {
  return request({
    url: '/oiaudit/analysisReport/projectListcount',
    method: 'get',
    params,
  })
}
export function getSJXMData(params) {
  return request({
    url: '/oiaudit/analysisReport/plancount',
    method: 'get',
    params,
  })
}
export function getXMZTData(params) {
  return request({
    url: '/oiaudit/analysisReport/projectZfcount',
    method: 'get',
    params,
  })
}
export function getBNMYData(params) {
  return request({
    url: '/oiaudit/analysisReport/projectYfcount',
    method: 'get',
    params,
  })
}

export function doDelete(data) {
  return request({
    url: '/departmentManagement/doDelete',
    method: 'post',
    data,
  })
}
