import request from '@/utils/request'
// import { transData } from '@/utils/requestData'

// 删除附件
export function deleteFileInfo(params) {
  return request({
    url: '/centralaudit/api-auth/fileManage/' + params.id,
    method: 'DELETE',
    // params: transData(params),
  })
}

export function getUserDetailInfo(params) {
  return request({
    url: '/centralaudit/api-auth/common/user/' + params.id,
    method: 'get',
    params: params,
  })
}

export const typeBbj = {
  'Content-Type': 'application/json',
}
