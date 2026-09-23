import request from '@/utils/request'

export const getTempList = (data) => {
  return request({
    url: '/setting/temp/find',
    method: 'get',
    params: data,
  })
}

export const getTempDetail = (data) => {
  return request({
    url: '/setting/temp/get',
    method: 'get',
    params: data,
  })
}

export const saveTemp = (data) => {
  return request({
    url: '/setting/temp/save',
    method: 'post',
    data,
  })
}

export const delTemp = (data) => {
  return request({
    url: '/setting/temp/delete',
    method: 'get',
    params: data,
  })
}
