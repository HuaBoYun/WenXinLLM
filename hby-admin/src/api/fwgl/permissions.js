import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 登记管理-列表-查询
export function getIsHeadquartersLegal(params) {
  return request({
    url: '/fwgl/api-auth/legal/common/isHeadquartersLegal',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}
