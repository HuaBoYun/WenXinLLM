import request from '@/utils/request'

export function getList() {
  return request({
    url: '/description/getList',
    method: 'get',
  })
}
