import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/property/trade-review'

export function getTradeReviewList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}
export function getTradeReviewDetail(id) {
  return request({ url: `${BASE}/${id}`, method: 'get' })
}
export function addTradeReview(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}
export function updateTradeReview(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}
export function deleteTradeReview(id) {
  return request({ url: `${BASE}/${id}`, method: 'delete' })
}
export function exportTradeReview() {
  return request({ url: '/monitor/v1/supervision/export/property/tradeReview', method: 'get', responseType: 'blob' })
}
