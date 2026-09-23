// import request from '@/utils/request'

import search from '../../mock/controller/search'

// export function getList() {
//   return request({
//     url: '/search/getList',
//     method: 'get',
//   })
// }

export function getList() {
  return new Promise((resolve) => {
    resolve(search[0].response())
  })
}
