import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//分页查询
export function getBusinessData(params) {
  return request({
    url: '/zbgl/gbi/export/queryBatch',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
// 获取之前获取的表信息
export function getTableList(params) {
  return request({
    url: '/zbgl/gbi/export/getTableList',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params: transData(params),
  })
}
// 获取表详细数据
export function getTableData(params) {
  return request({
    url: '/zbgl/gbi/export/getTableData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}