import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//测试结果-主页
export function resultCountList(params) {
  return request({
    url: '/hgglext/nkcs/statistic/result_count_list',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-主页列表-点击详细结果
export function xxjgList(params) {
  return request({
    url: '/hgglext/cshz/xxjg_list',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-主页列表点击控制数-左侧树(非json,子节点单独node)
export function gettreeAll(params) {
  return request({
    url: '/hgglext/csjg/gettree_all',
    method: 'get',
    params: transData(params),
  })
}
//测试结果-主页列表点击不适用数-左侧树(json树)
export function gettreeBxy(params) {
  return request({
    url: '/hgglext/csjg/gettree_bxy',
    method: 'get',
    params: transData(params),
  })
}
//测试结果-主页列表点击无效数-左侧树(json树)
export function gettreeWx(params) {
  return request({
    url: '/hgglext/csjg/gettree_wx',
    method: 'get',
    params: transData(params),
  })
}
//测试结果-主页列表点击有效数-左侧树(json树)
export function gettreeYx(params) {
  return request({
    url: '/hgglext/csjg/gettree_yx',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-控制数-点击左侧树(非json,子节点单独node)-右侧列表
export function defListAll(params) {
  return request({
    url: '/hgglext/csjg/def_list_all',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-不适用数-点击左侧树(json树)-右侧列表
export function defListBxy(params) {
  return request({
    url: '/hgglext/csjg/def_list_bxy',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-无效数-点击左侧树(json树)-右侧列表
export function defListWx(params) {
  return request({
    url: '/hgglext/csjg/def_list_wx',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-有效树-点击左侧树(json树)-右侧列表
export function defListYx(params) {
  return request({
    url: '/hgglext/csjg/def_list_yx',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-详细结果列表-点击数量-左侧树
export function gettreeNumber(params) {
  return request({
    url: '/hgglext/cshz/gettree',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-详细结果列表-点击数量-右侧列表
export function defListNum(params) {
  return request({
    url: '/hgglext/cshz/def_list',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-点击数量-点击左侧树-右侧列表-点击编号查询测试结果((通用))
export function controlTestImplDetail(params) {
  return request({
    url: '/hgglext/csrw/control_test_impl_detail',
    method: 'get',
    params: transData(params),
  })
}

//测试结果-详细结果列表-导出excel
export function resultExport(params) {
  return request({
    url: '/hgglext/nkcs/result_export',
    method: 'get',
    params: params,
    responseType: 'blob',
  })
}
