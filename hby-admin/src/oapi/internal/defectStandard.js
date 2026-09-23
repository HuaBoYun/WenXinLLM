import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//缺陷标准-列表
export function defQuexianList(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_quexian_list',
    method: 'get',
    params: transData(params),
  })
}

//缺陷标准-保存
export function defQuexianSave(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_quexian_save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//缺陷标准-删除
export function defQuexianDel(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_quexian_del',
    method: 'delete',
    params: transData(params),
  })
}

//缺陷标准-查看及修改时获取数据
export function defQuexianDetail(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_quexian_detail',
    method: 'get',
    params: transData(params),
  })
}
