/*
 * @Date: 2022-02-25 13:47:29
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-19 14:36:46
 * @FilePath: /hb-admin/src/utils/requestData.js
 */
import store from '@/store'
const token = store.getters['user/token']

export const transData = (data) => {
  if (!data) data = {}
  data.token = token
  return data
}
