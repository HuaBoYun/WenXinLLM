import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/expatriate/apply/info/get-all-List',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function exportList(data) {
  return request({
    url: '/centralaudit/api-auth/expatriate/apply/download-express',
    method: 'post',
    responseType: 'blob',
    data,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
