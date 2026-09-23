import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价报告-主页
export function getReportList(params) {
  return request({
    url: '/nkhg/nbkz/pjbg/nkbg/list',
    method: 'get',
    params: transData(params),
  })
}

//评价/自定义报告-添加页面-附件删除
export function deleteFile(params) {
  return request({
    url: '/nkhg/nbkz/pjbg/add/sp_del_fj',
    method: 'post',
    data: transData(params),
  })
}

//评价/自定义报告-添加页面-保存(code:-1 报告名存在,code:200 成功)
export function isExistAdd(params, data) {
  return request({
    url: '/nkhg/nbkz/pjbg/isExistAdd',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
    data: transData(data),
  })
}

//评价/自定义报告-修改页面-保存(code:-1 报告名存在,code:200 成功,修改时不传附件信息)
export function isExistUpdate(params, data) {
  console.log(params, 'params')
  console.log(data, 'data')
  return request({
    url: '/nkhg/nbkz/pjbg/isExistUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
    data: transData(data),
  })
}

//评价/自定义报告-删除
export function deleteReport(params) {
  return request({
    url: '/nkhg/nbkz/pjbg/nkbg/delete',
    method: 'post',
    params: transData(params),
  })
}

//评价/自定义报告-点击修改/详情查询
export function modifyReport(params) {
  return request({
    url: '/nkhg/nbkz/pjbg/nkbg/modify',
    method: 'get',
    params: transData(params),
  })
}

//评价/自定义报告-导出word
export function expReportFile(params) {
  return request({
    url: '/nkhg/nbkz/pjbg/sjss/expReportFile',
    method: 'get',
    params: transData(params),
  })
}

//评价/自定义报告-修改页面(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除
export function spDelFj(params) {
  return request({
    url: '/nkhg/nbkz/pjbg/update/sp_del_fj',
    method: 'post',
    params: transData(params),
  })
}

//自定义评价报告-主页
export function getZDYList(params) {
  return request({
    url: '/nkhg/nbkz/zdybg/nkbg/list',
    method: 'get',
    params: transData(params),
  })
}
