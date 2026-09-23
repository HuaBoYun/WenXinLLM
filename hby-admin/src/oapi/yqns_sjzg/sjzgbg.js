import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
  return request({
    url: '/oiaudit/zgbg/list',
    method: 'get',
    params: transData(params),
  })
}
//删除问题整改关联三个报告
export function removeWtzgSjtzs(data) {
  return request({
    url: '/oiaudit/gzhf/removeWtzgSjtzs',
    method: 'post',
    // headers: typeBbj,
    data: transData(data),
  })
}

//保存问题整改关联三个报告
export function saveWtzgSjtzs(data) {
  return request({
    url: '/oiaudit/gzhf/saveWtzgSjtzs',
    method: 'post',
    // headers: typeBbj,
    data: transData(data),
  })
}

//问题整改获取关联三个报告
export function getWtzgReportInfoList(params) {
  return request({
    url: '/oiaudit/gzhf/getWtzgReportInfoList',
    method: 'get',
    params: transData(params),
  })
}
export function getTotalMoney(params) {
  return request({
    url: '/oiaudit/hxzg/getTotalMoney',
    method: 'get',
    params: transData(params),
  })
}
export function editInfo(data) {
  return request({
    url: '/oiaudit/zgbg/saveOrUpdate',
    method: 'post',
    headers: typeBbj,
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/oiaudit/zgbg/detail',
    method: 'get',
    params: transData(params),
  })
}
// 删除当前选中列表数据
export function deleteInfo(data) {
  return request({
    url: '/oiaudit/zgbg/delete',
    method: 'get',
    params: transData(data),
  })
}
// 删除附件
export function deleteFileInfo(data) {
  return request({
    url: '/oiaudit/zgbg/deleteAttach',
    method: 'get',
    params: transData(data),
  })
}
export function saveSjyqbcAtt(data) {
  return request({
    url: '/oiaudit/gzhf/saveSjyqbcAtt',
    method: 'post',
    data: transData(data),
  })
}

export function removeSjyqbcAtt(data) {
  return request({
    url: '/oiaudit/gzhf/removeSjyqbcAtt',
    method: 'post',
    data: transData(data),
  })
} 