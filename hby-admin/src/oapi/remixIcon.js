import request from '@/utils/request'

export function getIconList(params) {
  return request({
    url: '/remixIcon/getList',
    method: 'get',
    params,
  })
}
