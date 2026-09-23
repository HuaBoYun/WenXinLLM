import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//测试跟踪-主页-点击任务跟踪
export function csgzList(params) {
  return request({
    url: '/nkhg/nbkz/csgz/csgz_list',
    method: 'get',
    params: transData(params),
  })
}

//测试跟踪-主页-点击编号
export function csgzDetail(params) {
  return request({
    url: '/nkhg/nbkz/csgz/nkcs/plan/detail',
    method: 'get',
    params: transData(params),
  })
}

//测试跟踪-主页
export function getList(params) {
  return request({
    url: '/nkhg/nbkz/nkcs/impl/control_test_track_list',
    method: 'get',
    params: transData(params),
  })
}
