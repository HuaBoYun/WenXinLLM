import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//会议管理-消息推送
export function getJFData(params) {
  return request({
    url: `/contract/legal/disputeStatistics`,
    method: 'post',
    data: transData(params),
  })
}
