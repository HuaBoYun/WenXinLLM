/*
 * @Date: 2022-02-25 14:21:37
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-17 10:32:58
 * @FilePath: /hb-admin/src/api/setting/template.js
 */
import request from '@/utils/request'
/**
 * @description:模板下载
 * @param {*} data
 * @return {*}
 */
export function downloadTemplate(data) {
  return request({
    url: '/setting/downloadTemplate',
    method: 'post',
    data,
  })
}
