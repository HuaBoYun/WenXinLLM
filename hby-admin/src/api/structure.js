import request from '@/utils/request'

export function organInfoDetail(data) {
  return request({
    url: '/setting/organInfoDetail',
    method: 'post',
    data,
  })
}

export function getAuditorInformationList(data) {
  return request({
    url: '/setting/getAuditorInformationList',
    method: 'post',
    data,
  })
}
export function saveOrUpdateOrgan(data) {
  return request({
    url: '/setting/saveOrUpdateOrgan',
    method: 'post',
    data,
  })
}
