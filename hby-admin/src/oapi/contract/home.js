import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getAllData(data) {
  return request({
    url: '/contract/contract/getReportContractData',
    method: 'get',
    params: transData(data),
  })
}
