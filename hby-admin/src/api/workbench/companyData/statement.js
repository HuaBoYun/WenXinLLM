import { transData } from '@/utils/requestData'
import request from '@/utils/request'

//利润
export function getProfitList(params) {
  return request({
    url: '/zbgl/prjData/lrb/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}
//资产负债
export function getBalanceList(params) {
  return request({
    url: '/zbgl/prjData/zcfzb/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}
