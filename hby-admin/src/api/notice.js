// import request from '@/utils/request'
import notice from '../../mock/controller/notice'

// export function getList() {
//   return request({
//     url: '/notice/getList',
//     method: 'get',
//   })
// }

export function getList() {
  return new Promise((resolve) => {
    resolve(notice[0].response())
  })
}
